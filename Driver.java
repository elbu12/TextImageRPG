package main;

import java.awt.Color;
import java.awt.Image;
import java.util.ArrayList;

import javax.swing.JPanel;

public class Driver{
	
	//Administrative variables
	private Scenario currentScenario;
	private Scenario[][] scenarios;
	private MyFrame frame;
	public Driver() {
		//Generate the map here
		final int mapWidth = 12;
		final int mapHeight = 7;
		//specialNodes represents the special terminal nodes
		//Add to it as more are written
		int specialNodes = 6;								//NUMBER OF SPECIAL SCENARIOS
		int[] specialNodeArray = new int[specialNodes];
		for (int i=0; i<specialNodes; i++) {
			specialNodeArray[i] = i;
		}
		Shuffler.shuffle(specialNodeArray);
		Prescenario[][] premap = MapMaker.generateMap(mapHeight, mapWidth, specialNodes);
		//Now specialNodes represents the index of the next special node to place
		specialNodes--;
		scenarios = new Scenario[mapHeight][mapWidth];
		GenericScenarioGenerator genScenGen = new GenericScenarioGenerator();
		//Also track areas where we may start
		ArrayList <Scenario> potentialBeginnings = new ArrayList <> ();
		for (int i=0; i<mapHeight; i++) {
			for (int j=0; j<mapWidth; j++) {
				Prescenario p = premap[i][j];
				if (p == null) {
					//unreachable area
					scenarios[i][j] = new Scenario(i, j);
				}
				else if (p.mustBeTerminal) {
					switch (specialNodeArray[specialNodes]) {
					case 0:
						scenarios[i][j] = getFloweringPlantScenario(i, j, p.directions);
						break;
					case 1:
						scenarios[i][j] = getFruitTreeScenario(i, j, p.directions);
						break;
					case 2:
						scenarios[i][j] = getSwampScenario(i, j, p.directions);
						break;
					case 3:
						scenarios[i][j] = getCastleScenario(i, j, p.directions);
						break;
					case 4:
						scenarios[i][j] = getCaveScenario(i, j, p.directions);
						break;
					case 5:
						scenarios[i][j] = getVillageScenario(i, j, p.directions);
						break;
					}
					specialNodes--;
				}
				else {
					scenarios[i][j] = genScenGen.get(i, j, p.directions);
					potentialBeginnings.add(scenarios[i][j]);
				}
			}
		}
		frame = new MyFrame(scenarios, this);
		if (potentialBeginnings.isEmpty()) {
			System.out.println("Error! No acceptable starting points.");
			System.exit(0);
		}
		//pick a random starting point
		setCurrentScenario(potentialBeginnings.get((int)(Math.random()*potentialBeginnings.size())));
		frame.pack();
		frame.setVisible(true);
	}
	public void setFrame(MyFrame frame) {
		this.frame = frame;
	}
	public void buttonPressed(int buttonId) {
		currentScenario.buttonPressed(buttonId, this);
	}
	public void directionButtonPressed(int buttonId) {
		currentScenario.directionButtonPressed(buttonId, this);
	}
	public void roomButtonPressed(RoomScenario room) {
		currentScenario.roomButtonPressed(room, this);
	}
	public void setCurrentScenario(Scenario scenario) {
		currentScenario = scenario;
		scenario.setKnowledge(Scenario.CURRENT);
		drawCurrentScenario();
	}
	public void drawCurrentScenario() {
		frame.drawScenario(currentScenario);
		currentScenario.updateLabel();
	}
	public void setCurrentScenario(int i, int j) {
		setCurrentScenario(scenarios[i][j]);
	}
	//Gameplay variables
	public static final String PLANT_NAME = "fugbeer";
	public static final String FRUIT_NAME = "sunfruit";
	
	private boolean haveFloweringPlant = false;
	private boolean haveSword = false;
	private int fruitsEaten = 0;
	private byte lanternStatus = 0;
	//0 lantern there
	//1 just took it
	//2 already took it
	private byte injuredManStatus = 0;
	//0 - initial request
	//1 - tried to help
	//2 - met before but never tried
	//3 - rescued
	
	public boolean getHaveSword() {
		return haveSword;
	}
	
	//Individual scenarios as anonymous classes
	private Scenario getFloweringPlantScenario(int y, int x, boolean[] directions) {
		Scenario scenario = new Scenario(y, x, directions) {
			byte status = 0;
			//0 - first time
			//1 - second time, unpicked
			//2 - second time, picked
			//3 - just picked
			final Image image = ImageReader.getRandom("floweringPlant");
			public Image getImage() {
				return image;
			}
			public String getMainText() {
				switch (status) {
				case 0:
					status = 1;
					return "You find the plant " + PLANT_NAME + ". Its scent makes it an effective bug\nrepellant.";
				case 1:
					return "You return to the plant " + PLANT_NAME + ". Its scent makes it an effective bug\nrepellant.";
				case 2:
					return "You return to the plant " + PLANT_NAME + ". You already picked some. You don't need\nmore.";
				default:
					status = 2;
					return "You pick some " + PLANT_NAME + ".";
				}
			}
			public String[] getButtons() {
				return (status < 2 ? getButton("Pick sone") : NO_BUTTONS);
			}

			public void buttonPressed(int buttonId, Driver driver) {
				//Only button represents picking
				status = 3;
				haveFloweringPlant = true;
				driver.drawCurrentScenario();
			}
		};
		scenario.setMapText(PLANT_NAME + " plant");
		return scenario;
	}
	private Scenario getSwampScenario(int y, int x, boolean[] directions) {
		Scenario scenario = new Scenario(y, x, directions) {
			int status = 0;
			//0 - entrance
			//1 - too buggy
			//2 - find sword
			//3 - take sword
			//4 - return; already have sword
			final Image swampPathImage = ImageReader.getRandom("swampPath");
			final Image swordImage = ImageReader.getRandom("sword");
			public Image getImage() {
				switch (status) {
				case 2:
				case 3:
					return swordImage;
				default:
					return swampPathImage;
				}
			}
			public String getMainText() {
				switch (status) {
				case 0:
					return "The path here continues into the swamp.";
				case 1:
					return "It's too buggy. You'll be eaten alive. You turn back.";
				case 2:
					return "You rub yourself with " + PLANT_NAME + " and enter the swamp. Your strategy works.\nThe bugs leave you alone. You explore and find a corpse, probably a victim\nof the insects. Surprisingly, its sword appears to be in good condition.";
				case 3:
					return "You take the sword. It's sharp.";
				default:
					return "You enter the swamp again but find nothing new.";
				}
			}
			public String[] getButtons() {
				switch (status) {
				case 0:
					return getButton("Enter swamp");
				case 2:
					return getButton("Take sword");
				default:
					return NO_BUTTONS;
				}
			}

			public void buttonPressed(int buttonId, Driver driver) {
				//Only one button possible at a time
				switch (status) {
				case 0:
					if (haveFloweringPlant) {
						status = (haveSword ? 4 : 2);
					}
					else {
						status = 1;
					}
					break;
				default:
					haveSword = true;
					status = 3;
					break;
				}
				driver.drawCurrentScenario();
			}
			public void reset() {
				status = 0;
				super.reset();
			}
		};
		scenario.setMapText("swamp");
		return scenario;
	}
	private Scenario getFruitTreeScenario(int y, int x, boolean[] directions) {
		Scenario scenario = new Scenario(y, x, directions) {
			final String[] FIGHT_FLEE_BUTTONS = new String[] {"Fight", "Flee"};
			int status = 0;
			//0 - monster still there
			//1 - approaching, no sword
			//2 - approaching with sword
			//3 - just killed monster
			//4 - monster already dead
			//5 - ate fruit
			//8 - successfully fled
			final Image monsterImage = ImageReader.getRandom("monsterPassive");
			final Image treeImage = ImageReader.getRandom("fruitTree");
			public Image getImage() {
				switch (status) {
				case 0:
				case 1:
				case 2:
				case 8:
					return monsterImage;
				default:
					return treeImage;
				}
			}
			public String getMainText() {
				switch (status) {
				case 0:
					return "You find a rare " + FRUIT_NAME + " tree. Its fruit is delicious, and you can see some\nripe ones now, but it appears to be guarded by some type of monster.";
				case 1:
					return "You cautiously approach the tree. The monster stares at you. This seems\nlike a bad idea. You back away.";
				case 2:
					return "You cautiously approach the tree. The monster stares at you. The\nmessage is clear. It's fight or flight time.";
				case 3:
					return "You proceed. The monster charges. You cut it down with your sword. The\ntree is now unguarded.";
				case 4:
					return "You return to the " + FRUIT_NAME + " tree. It still bears plenty of ripe fruit.";
				case 5:
					switch (fruitsEaten) {
					case 1:
					case 2:
					case 3:
						return "You eat one. Delicious!";
					case 4:
					case 5:
					case 6:
						return "You eat another fruit. Eating this many is probably not good for your teeth.";
					case 7:
					case 8:
					case 9:
						return "You're out of control, gorging on wild fruit.";
					case 10:
					case 11:
					case 12:
						return "You eat another one. It's not even pleasant now, just sickening.";
					default:
						return "Just stop.";
					}
				default:
					return "You escape. The monster does not seem interested in pursuing.";
				}
			}
			public String[] getButtons() {
				switch (status) {
				case 0:
					return getButton("Approach");
				case 1:
				case 8:
					return NO_BUTTONS;
				case 2:
					return FIGHT_FLEE_BUTTONS;
				default:
					return getButton("Eat fruit");
				}
			}
			
			public boolean[] getDirections() {
				if (status == 2) {
					return NO_DIRECTIONS;
				}
				return super.getDirections();
			}

			public void buttonPressed(int buttonId, Driver driver) {
				//Only one button possible at a time
				switch (status) {
				case 0:
					status = (haveSword ? 2 : 1);
					break;
				case 2:
					status = (buttonId == 0 ? 3 : 8);
					break;
				default:
					status = 5;
					fruitsEaten++;
					break;
				}
				driver.drawCurrentScenario();
			}
			public void reset() {
				if (status > 2 && status < 8) {
					status = 4;
				}
				else {
					status = 0;
				}
				super.reset();
			}
		};
		scenario.setMapText(FRUIT_NAME);
		return scenario;
	}
	/**
	 * Idea:
	 * a small castle. If you do not have fruit, a beast will be there,
	 * blocking your entrance. If you have fruit, the beast will be missing.
	 * You can then explore, finding a lantern with oil. The beast will then
	 * hunt you, necessitating escape.
	 * 
	 * Rooms:_________________
	 * tower ___hallway	____ |	tower
	 * pantry	great hall	||	servant bedroom
	 * kitchen	great hall	||	master bedroom
	 * workshop	antechamber	||	study
	 * 
	 * If you have the fruit, the monster will follow you, always one room behind,
	 * unless you backtrack.
	 */
	private Scenario castle = null;
	private BeastScenario courtyard = null;
	private JPanel castlePanel = null;
	private Scenario getCastleScenario(int y, int x, boolean[] directions) {
		Scenario castleScenario = new Scenario(y, x, directions) {
			boolean returning = false;
			Beast beast = new Beast();
			final Image image = ImageReader.getRandom("castleEntrance");
			
			public Image getImage() {
				return image;
			}
			public String getMainText() {
				return (returning ? "You return to the castle entrance." : "You find a castle in the woods.");
			}
			public String[] getButtons() {
				return getButton("Enter");
			}
			public void buttonPressed(int buttonId, Driver driver) {
				//Only button represents entering
				if (courtyard == null) {
					courtyard = getCourtyardScenario(beast);
					BeastScenario antechamber = getAntechamberScenario(beast);
					BeastScenario workshop = getWorkshopScenario(beast);
					BeastScenario greatHall = getGreatHallScenario(beast);
					BeastScenario kitchen = getKitchenScenario(beast);
					BeastScenario pantry = getPantryScenario(beast);
					BeastScenario namedHallway = getNamedHallwayScenario(beast);
					BeastScenario unnamedHallway = getUnnamedHallwayScenario(beast);
					BeastScenario tower1 = getTowerScenario(beast, 0, 13, 1);
					BeastScenario tower2 = getTowerScenario(beast, 5, 13, 2);
					BeastScenario study = getStudyScenario(beast);
					BeastScenario servantBedroom = getServantBedroomScenario(beast);
					BeastScenario masterBedroom = getMasterBedroomScenario(beast);
					BeastScenario roof = getCastleRoofScenario(beast);
					beast.setRestartingRoom(servantBedroom);
					courtyard.add(antechamber, DirectionButton.EAST);
					antechamber.add(courtyard, DirectionButton.WEST);
					antechamber.add(greatHall, DirectionButton.EAST);
					greatHall.add(antechamber, DirectionButton.WEST);
					greatHall.add(kitchen, DirectionButton.NORTH);
					greatHall.add(namedHallway, DirectionButton.SOUTH);
					greatHall.add(unnamedHallway, DirectionButton.EAST);
					kitchen.add(greatHall, DirectionButton.SOUTH);
					kitchen.add(pantry, DirectionButton.EAST);
					pantry.add(kitchen, DirectionButton.WEST);
					workshop.add(kitchen, DirectionButton.EAST);
					workshop.add(antechamber, DirectionButton.SOUTH);
					antechamber.add(workshop, DirectionButton.NORTH);
					kitchen.add(workshop, DirectionButton.WEST);
					namedHallway.add(courtyard, DirectionButton.WEST);
					courtyard.add(namedHallway, DirectionButton.EAST);
					unnamedHallway.add(namedHallway, DirectionButton.SOUTH);
					unnamedHallway.add(greatHall, DirectionButton.WEST);
					namedHallway.add(antechamber, DirectionButton.NORTH);
					namedHallway.add(greatHall, DirectionButton.NORTH);
					namedHallway.add(unnamedHallway, DirectionButton.NORTH);
					antechamber.add(namedHallway, DirectionButton.SOUTH);
					tower1.add(unnamedHallway, DirectionButton.SOUTH);
					unnamedHallway.add(tower1, DirectionButton.NORTH);
					tower2.add(namedHallway, DirectionButton.NORTH);
					study.add(namedHallway, DirectionButton.NORTH);
					namedHallway.add(study, DirectionButton.SOUTH);
					namedHallway.add(masterBedroom, DirectionButton.SOUTH);
					namedHallway.add(servantBedroom, DirectionButton.SOUTH);
					namedHallway.add(tower2, DirectionButton.SOUTH);
					masterBedroom.add(namedHallway, DirectionButton.NORTH);
					servantBedroom.add(namedHallway, DirectionButton.NORTH);
					roof.add(tower1, DirectionButton.NORTH);
					roof.add(tower2, DirectionButton.SOUTH);
					tower1.add(roof, DirectionButton.EAST);
					tower2.add(roof, DirectionButton.EAST);
					castlePanel = RoomScenario.generateMapPanel(courtyard, antechamber, workshop,
							kitchen, pantry, greatHall, namedHallway, unnamedHallway,
							tower1, tower2, study, masterBedroom, servantBedroom);
				}
				frame.setMapPanel(castlePanel);
				setCurrentScenario(courtyard);
				returning = true;
			}
			public void reset() {
				returning = false;
				super.reset();
			}
		};
		castleScenario.setMapText("castle");
		castle = castleScenario;
		return castleScenario;
	}
	public BeastScenario getCourtyardScenario(Beast beast) {
		BeastScenario courtyardScenario = new BeastScenario(0, 0, 7, 7, beast, "You enter the courtyard. It appears empty, other than the many weeds.", "courtyardWithoutBeast") {
			final Image beastImage = ImageReader.getRandom("courtyardWithBeast");
			public Image getImage() {
				return (beast.getStatus() == 2 ? beastImage : super.getImage());
			}
			public String getMainText() {
				if (beast.getStatus() == 2) {
					String s = "You see the wild beast foraging in the courtyard. You don't want to proceed\nand risk provoking it.";
					return (haveSword ?
							s + " Even with your sword, it would be a tough fight." : s);
				}
				return super.getMainText();
			}
			public String[] getButtons() {
				return getButton("Exit castle");
			}
			public void buttonPressed(int buttonId, Driver driver) {
				frame.resetMapPanel();
				if (beast.getStatus() == 1) {
					//Leaving the castle. Prevent return.
					beast.setStatus(2);
				}
				setCurrentScenario(castle);
			}
			public void updateDirectionPanel(MyFrame frame) {
				switch (beast.getStatus()) {
				case 2:
					//You cannot go further into the castle with
					//the beast there
					frame.setRoomButtons(null);
					break;
				default:
					super.updateDirectionPanel(frame);
					break;
				}
			}
		};
		courtyardScenario.setMapText("courtyard");
		return courtyardScenario;
	}
	public BeastScenario getAntechamberScenario(Beast beast) {
		BeastScenario antechamberScenario = new BeastScenario(2, 7, 2, 2, beast, "You enter an antechamber.", "antechamber");
		antechamberScenario.setMapText("antechamber");
		return antechamberScenario;
	}
	public BeastScenario getGreatHallScenario(Beast beast) {
		BeastScenario greatHallScenario = new BeastScenario(2, 9, 2, 4, beast, "You enter the great hall.", "greatHall");
		greatHallScenario.setMapText("great hall");
		return greatHallScenario;
	}
	public BeastScenario getWorkshopScenario(Beast beast) {
		BeastScenario workshopScenario = new BeastScenario(0, 7, 2, 2, beast, "You enter a workshop with some rusty tools.", "workshop");
		workshopScenario.setMapText("workshop");
		return workshopScenario;
	}
	public BeastScenario getKitchenScenario(Beast beast) {
		BeastScenario scenario = new BeastScenario(0, 9, 2, 2, beast, "You enter a kitchen with some dirty utensils.","kitchen");
		scenario.setMapText("kitchen");
		return scenario;
	}
	public BeastScenario getPantryScenario(Beast beast) {
		BeastScenario scenario = new BeastScenario(0, 11, 2, 2, beast, "You enter a pantry with some spoiled food.", "pantry");
		scenario.setMapText("pantry");
		return scenario;
	}
	private RandomImageSequence hallwayImages = ImageReader.getAllRandom("hallway");
	public BeastScenario getNamedHallwayScenario(Beast beast) {
		BeastScenario scenario = new BeastScenario(4, 7, 1, 7, beast, "You enter a hallway.", hallwayImages.get()) {
			Image beastImage = ImageReader.getRandom("hallwayWithBeast");
			boolean showBeast = false;
			public String getMainText() {
				if (beast.getMessage() == 1) {
					String s = "You see a wild beast approaching from the courtyard.";
					return (haveSword ? 
							s + " Even with your \nsword, this would be a tough fight." : s);
				}
				return super.getMainText();
			}
			public Image getImage() {
				if (beast.getMessage() == 1) {
					showBeast = true;
				}
				return (showBeast ? beastImage : super.getImage());
			}
			public void reset() {
				showBeast = false;
				super.reset();
			}
		};
		scenario.setMapText("hallway");
		return scenario;
	}
	public BeastScenario getUnnamedHallwayScenario(Beast beast) {
		BeastScenario scenario = new BeastScenario(2, 13, 2, 1, beast, "You enter a hallway.", hallwayImages.get()) {
			public void updateLabel() {
				switch (getKnowledge()) {
				case UNKNOWN:
				case IMPLIED:
					label.setBackground(Color.BLACK);
					break;
				case KNOWN:
					label.setBackground(Color.LIGHT_GRAY);
					break;
				case CURRENT:
					label.setBackground(Color.WHITE);
					break;
				}
			}
		};
		scenario.setMapText("hallway");
		return scenario;
	}
	private RandomImageSequence towerImages = ImageReader.getAllRandom("staircase");
	public BeastScenario getTowerScenario(Beast beast, int y, int x, int index) {
		BeastScenario scenario = new BeastScenario(y, x, 2, 2, beast, "You reach a staircase.", towerImages.get());
		scenario.setMapText("stairs " + index);
		return scenario;
	}
	public BeastScenario getStudyScenario(Beast beast) {
		BeastScenario scenario = new BeastScenario(5, 7, 2, 2, beast, "You enter a study. Whoever lived here liked books.", "study") {
			boolean reading= false;
			final Image bookImage = ImageReader.getRandom("beastBook");
			public Image getImage() {
				return (reading ? bookImage : super.getImage());
			}
			public String getMainText() {
				return (reading ?
						"You find some literature on raising wild beasts. Whoever lived here may\nhave kept one as a pet." :
							super.getMainText());
			}
			public void reset() {
				reading = false;
				super.reset();
			}
			public String[] getButtons() {
				if (beast.getStatus() == 1) {
					//You are trapped! Don't read; just die
					return super.getButtons();
				}
				return (reading ? NO_BUTTONS : getButton("Read"));
			}
			public void buttonPressed(int buttonId, Driver driver) {
				if (beast.getStatus() == 1) {
					super.buttonPressed(buttonId, driver);
				}
				else {
					reading = true;
					drawCurrentScenario();
				}
			}
		};
		scenario.setMapText("study");
		return scenario;
	}
	public BeastScenario getServantBedroomScenario(Beast beast) {
		BeastScenario scenario = new BeastScenario(5, 11, 2, 2, beast, "You enter a bedroom, probably the servants'.", "bedroom") {
			final Image lantern = ImageReader.getRandom("lantern");
			public Image getImage() {
				return (lanternStatus == 1 ? lantern : super.getImage());
			}
			public String getMainText() {
				switch (lanternStatus) {
				case 0:
					return super.getMainText() + " You notice a lantern on a\ntable.";
				case 1:
					return "You take the lantern. It still has plenty of fuel.";
				default:
					return super.getMainText();
				}
			}
			public String[] getButtons() {
				return (lanternStatus == 0 ? getButton("Take lantern") : super.getButtons());
			}
			public void buttonPressed(int buttonId, Driver driver) {
				if (lanternStatus == 0) {
					//just took lantern
					lanternStatus = 1;
					drawCurrentScenario();
				}
				else {
					super.buttonPressed(buttonId, driver);
				}
			}
			public void reset() {
				if (lanternStatus == 1) {
					lanternStatus = 2;
				}
				super.reset();
			}
			public void roomButtonPressed(RoomScenario room, Driver driver) {
				if (lanternStatus != 0 && beast.getStatus() == 0) {
					//summon the beast
					beast.setStatus(1);
					beast.setMessage(1);
					beast.setBeastRoom(courtyard);
					setKnowledge(KNOWN);
					reset();
					setCurrentScenario(room);
				}
				else {
					super.roomButtonPressed(room, driver);
				}
			}
		};
		scenario.setMapText("bedroom 2");
		return scenario;
	}
	public BeastScenario getMasterBedroomScenario(Beast beast) {
		BeastScenario scenario = new BeastScenario(5, 9, 2, 2, beast, "You enter a bedroom, probably the master's.", "masterBedroom");
		scenario.setMapText("bedroom 1");
		return scenario;
	}
	public BeastScenario getCastleRoofScenario(Beast beast) {
		BeastScenario scenario = new BeastScenario(-1, -1, 1, 1, beast, "You reach the castle battlements, overlooking the forest.", "castleRoof");
		scenario.setMapText("battlements");
		return scenario;
	}
	//end of beast scenarios
	
	public Scenario getCaveScenario(int y, int x, boolean[] directions) {
		Scenario scenario = new Scenario(y, x, directions){
			final String[] PROCEED_EXIT = new String[] {"Proceed", "Exit"};
			final String[] HELP_REFUSE = new String[] {"Help", "Refuse"};
			byte status = 0;
			//0 - outside
			//1 - inside
			//2 - deep inside
			boolean entering = true;
			byte message = 0;
			//0 - initial greeting
			//1 - try to help
			//2 - refuse to help
			//3 - man asks again; already tried to lift
			//4 - man asks again; never tried to lift
			final Image[] images = new Image[] {
					ImageReader.getRandom("caveEntrance"),
					ImageReader.getImage("blackScenario.png"),
					ImageReader.getRandom("cave"),
					ImageReader.getRandom("manInCave")
			};
			
			public Image getImage() {
				switch (status) {
				case 0:
					return images[0];
				case 1:
					return (lanternStatus == 0 ? images[1] : images[2]);
				default:
					return images[3];
				}
			}
			
			public String getMainText() {
				switch (status) {
				case 0:
					return (entering ? "You find a cave." : "You emerge from the cave.");
				case 1:
					if (lanternStatus == 0) {
						return "You enter the cave. It's too dark to proceed.";
					}
					else {
						switch (injuredManStatus) {
						case 0:
							return "Inside the cave, you hear a human voice, begging for help.";
						case 3:
							return "You enter the cave. It is quiet.";
						default:
							return "Inside the cave, you hear the man again, calling for help.";
						}
					}
				}
				//Interactions with man
				switch (message) {
				case 0:
					return "You find an injured man. He was exploring the cave, but tripped and hurt\nhis ankle and his torch went out. Now he has been stuck here, in the dark,\nunable to walk. He begs you to help him.";
				case 1:
					return "You try to help the man but he is too heavy to carry. He asks you to go to\nhis village nearby and tell them about him.";
				case 2:
					return "You refuse to help the man. This situation seems too suspicious.";
				case 3:
					return "You return to the injured man. He repeats his request for you to tell his\nvillage about his predicament.";
				default:
					return "You return to the injured man. He repeats his request for help.";
				}
			}
			
			public String[] getButtons() {
				switch (status) {
				case 0:
					return (entering ? getButton("Enter") : NO_BUTTONS);
				case 1:
					if (lanternStatus == 0 || injuredManStatus == 3) {
						return NO_BUTTONS;
					}
					else {
						return PROCEED_EXIT;
					}
				}
				//Interactions with man
				switch (message) {
				case 0:
				case 4:
					return HELP_REFUSE;
				default:
					return getButton("Exit");
				}
			}
			
			public void buttonPressed(int buttonId, Driver driver) {
				switch (status) {
				case 0:
					status = 1;
					entering = false;
					break;
				case 1:
					if (lanternStatus != 0 && buttonId == 0) {
						// proceed
						switch (injuredManStatus) {
						case 0:
							message = 0;
							break;
						case 1:
							message = 3;
							break;
						default:
							message = 4;
							break;
						}
						status = 2;
					}
					else {
						// Unable to proceed, no reason to proceed, or declined
						status = 0;
					}
					break;
				default:
					// Interactions with man
					if (buttonId == 1) {
						// refuse to help
						message = 2;
						injuredManStatus = 2;
					}
					else {
						switch (message) {
						case 0:
						case 4:
							// try to help
							message = 1;
							injuredManStatus = 1;
							if (village.getKnowledge() == UNKNOWN) {
								village.setKnowledge(IMPLIED);
								village.updateLabel();
							}
							break;
						default:
							// exit
							status = 0;
							break;
						}
					}
				}
				drawCurrentScenario();
			}
			public boolean[] getDirections() {
				return (status == 0 || lanternStatus == 0 || injuredManStatus == 3 ? super.getDirections() : NO_DIRECTIONS);
			}
			public void reset() {
				entering = true;
				status = 0;
				super.reset();
			}
		};
		scenario.setMapText("cave");
		return scenario;
	}
	
	private Scenario village = null;
	private Scenario getVillageScenario(int y, int x, boolean[] directions) {
		if (village != null) {
			return village;
		}
		village = new Scenario(y, x, directions) {
			byte status = 0;
			//0 - arrived
			//1 - they ask you about injured man
			//2 - you tell them about injured man
			//3 - party
			//4 - return to party

			final Image[] images = new Image[] {
					ImageReader.getRandom("village"),
					ImageReader.getRandom("villager"),
					ImageReader.getRandom("villagersEnteringCave"),
					ImageReader.getRandom("party")
			};
			public Image getImage() {
				switch (status) {
				case 0 :
					return images[0];
				case 1:
					return images[1];
				case 2:
					return images[2];
				default:
					return images[3];
				}
			}
			public String getMainText() {
				switch (status) {
				case 0:
					return "You find a village in the forest.";
				case 1:
					return "You talk to some of the villagers. They ask if you've seen a certain man\nwho is missing.";
				case 2:
					return "You lead the villagers to the injured man in the cave. Everyone is relieved.";
				case 3:
					return "They throw a party to celebrate. You are the guest of honor.";
				default:
					return "You return to the village. Everyone is celebrating.";
				}
			}
			public String[] getButtons() {
				switch (status) {
				case 0:
					return (injuredManStatus == 0 ? getButton("Talk") :
						getButton("Mention man in cave"));
				case 2:
					return getButton("Ok");
				default:
					return NO_BUTTONS;
				}
			}

			public void buttonPressed(int buttonId, Driver driver) {
				switch (status) {
				case 0:
					status = (byte)(injuredManStatus == 0 ? 1 : 2);
					break;
				default:
					status = 3;
					injuredManStatus = 3;
					break;
				}
				drawCurrentScenario();
			}
			
			public boolean[] getDirections() {
				return (status == 2 ? NO_DIRECTIONS : super.getDirections());
			}
			
			public void reset() {
				switch (status) {
				case 1:
					status = 0;
					break;
				case 3:
					status = 4;
					break;
				}
				super.reset();
			}
		};
		village.setMapText("village");
		return village;
	}
	
	private static String[] ONE_BUTTON = new String[1];
	public static String[] getButton(String s) {
		ONE_BUTTON[0] = s;
		return ONE_BUTTON;
	}
	
	//for testing
	public static void main(String[] args) {
		for (Image i : ImageReader.getAll("floweringPlant")) {
			System.out.println(i);
		}
	}
}
