package main;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

/*
 * The superclass for all scenarios. Instantiating it creates a blank scenario.
 *
 */

public class Scenario {
	public static final byte UNKNOWN = 0;
	public static final byte IMPLIED = 1;
	public static final byte KNOWN = 2;
	public static final byte CURRENT = 3;
	public static final Image BLACK = ImageReader.getImage("black.png");
	public static final Image WHITE = ImageReader.getImage("white.png");
	public static final Image GRAY = ImageReader.getImage("gray.png");
	public static final Image GREEN = ImageReader.getImage("green.png");

	protected static final String[] OK = new String[] {"Ok"};
	protected static final String[] NO_BUTTONS = new String[0];
	protected static final boolean[] NO_DIRECTIONS = new boolean[] {false, false, false, false};
	
	private final int x;
	private final int y;
	private String mapText;
	private boolean[] directions;
	private byte knowledge;
	protected JLabel label = null;
	
	public Scenario(int y, int x, boolean[] directions) { //always y before x!
		this.x = x;
		this.y = y;
		mapText = null;
		this.directions = directions;
		knowledge = UNKNOWN;
	}
	
	public Scenario(int y, int x) { //always y before x!
		this(y, x, NO_DIRECTIONS);
	}
	
	public Image getImage() {
		return null;
	}
	public String getMainText() {
		return null;
	}
	public String[] getButtons() {
		return NO_BUTTONS;
	}
	public void buttonPressed(int buttonId, Driver driver) {
		
	}
	public void directionButtonPressed(int buttonId, Driver driver) {
		//Check if moving that direction is allowed
		if (getDirections()[buttonId]) {
			//Move to this new location
			//redraw the current scenario
			setKnowledge(KNOWN);
			int newx = x;
			int newy = y;
			switch(buttonId) {
			case DirectionButton.NORTH:
				newy--;
				break;
			case DirectionButton.EAST:
				newx++;
				break;
			case DirectionButton.SOUTH:
				newy++;
				break;
			case DirectionButton.WEST:
				newx--;
				break;
			}
			reset();
			driver.setCurrentScenario(newy, newx);
		}
	}
	public void roomButtonPressed(RoomScenario room, Driver driver) {
		//Only relevant for room scenarios
	}
	public boolean[] getDirections() {
		return directions;
	}
	public void updateDirectionPanel(MyFrame frame) {
		frame.setDirections(getDirections());
	}
	public int getx() {
		return x;
	}
	public int gety() {
		return y;
	}
	public String getMapText() {
		return mapText;
	}
	public void setMapText(String mapText) {
		this.mapText = mapText;
	}
	public byte getKnowledge() {
		return knowledge;
	}
	public void setKnowledge(byte knowledge) {
		//This should generally be accompanied by a call to draw
		this.knowledge = knowledge;
	}
	protected void reset() {
		//Called when leaving a scenario
		updateLabel();
	}
	public JLabel getLabel() {
		return label;
	}
	protected void setLabel(JLabel label) {
		this.label = label;
	}
	protected void makeLabel() {
		label = new MapLabel(new ImageIcon(BLACK));
	}
	public void updateLabel() {
		ImageIcon icon = (ImageIcon)(label.getIcon());
		MapLabel mLabel = (MapLabel)(label);
		int d = 0;
		boolean[] directions = getDirections();
		for (boolean b : directions) {
			if (b) {
				d++;
			}
		}
		switch (knowledge) {
		case UNKNOWN:
			icon.setImage(BLACK);
			mLabel.set(null, null, false);
			break;
		case IMPLIED:
			icon.setImage(GRAY);
			mLabel.set(mapText, null, false);
			break;
		default:
			icon.setImage(knowledge == KNOWN ? GREEN : WHITE);
			mLabel.set(mapText, (d < 2 ? null : directions), knowledge == CURRENT);
			break;
		}
	}
}
