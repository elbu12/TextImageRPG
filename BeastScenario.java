package main;

import java.awt.Image;

public class BeastScenario extends RoomScenario{

	protected final Beast beast;
	private final String regularMainText;
	private final Image regularImage;
	public BeastScenario(int y, int x, int height, int width, Beast beast, String regularMainText, Image regularImage) {
		super(y, x, height, width);
		this.beast = beast;
		this.regularMainText = regularMainText;
		this.regularImage = regularImage;
	}
	public BeastScenario(int y, int x, int height, int width, Beast beast, String regularMainText, String imageFolderName) {
		this(y, x, height, width, beast, regularMainText, ImageReader.getRandom(imageFolderName));
	}
	public void roomButtonPressed(RoomScenario room, Driver driver) {
		// Can only enter if the beast is not there
		if (beast.getStatus() == 0) {
			//beast still absent
			super.roomButtonPressed(room, driver);
		}
		else {
			//beast present
			if (beast.getBeastRoom() == room) {
				//trying to approach beast. Receive warning
				beast.setMessage(6);
				driver.drawCurrentScenario();
			}
			else {
				beast.setBeastRoom(this);
				//Did you just trap yourself?
				beast.setMessage(room.getConnectedRoomsSize() < 2 ? 2 : 0);
				super.roomButtonPressed(room, driver);
			}
		}
	}
	public String getMainText() {
		switch (beast.getMessage()) {
		//case 1 is particular to the named hallway and is written out there
		case 2:
			return regularMainText;
		case 3:
			return "The room has only one exit. The beast enters. You are trapped. You fight\nfor your life but it is not enough.";
		case 4:
			return "The room has only one exit. The beast enters. You are trapped. You fight\nfor your life but it is not enough. You both die.";
		case 5:
			return "You are returned to the point when you first took the lantern.";
		case 6:
			return "Don't go there! That's where the beast is!";
		}
		//no special message
		switch (beast.getStatus()) {
		case 1:
			return regularMainText + "\nYou hear the beast nearby, following you.";
		default:
			return regularMainText;
		}
	}
	public String[] getButtons() {
		switch (beast.getMessage()) {
		case 2:
		case 3:
		case 4:
		case 5:
			return Driver.getButton("Ok");
		default:
			return NO_BUTTONS;
		}
	}
	public void buttonPressed(int buttonId, Driver driver) {
		//Only option is the OK button
		switch (beast.getMessage()) {
		case 2:
			beast.setMessage(driver.getHaveSword() ? 4 : 3);
			driver.drawCurrentScenario();
			break;
		case 3:
		case 4:
			beast.setMessage(5);
			driver.drawCurrentScenario();
			break;
		case 5:
			//Restart at lantern scenario
			setKnowledge(KNOWN);
			reset();
			beast.setMessage(0);
			beast.setStatus(0);
			driver.setCurrentScenario(beast.getRestartingRoom());
			break;
		}
	}
	public Image getImage() {
		switch (beast.getMessage()) {
		case 3:
		case 4:
		case 5:
			return ImageReader.getRandom("beastAttacking");
		default:
			return regularImage;
		}
	}
}
