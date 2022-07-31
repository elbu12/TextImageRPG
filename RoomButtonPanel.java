package main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayDeque;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Box;
import javax.swing.JButton;

public class RoomButtonPanel extends JPanel{
	public static final String DEFAULT_ROOM_BUTTON = "unknown room";
	
	private Driver driver;
	private ArrayList[] buttonLists;		//visible buttons
	private Box[] boxes;					//contain the buttons
	private JButton[] placeholders;			//spacing
	public RoomButtonPanel(Driver driver) {
		super(new GridBagLayout());
		this.driver = driver;
		buttonLists = new ArrayList[4];
		boxes = new Box[4];
		placeholders = new JButton[4];
		GridBagConstraints gbc = new GridBagConstraints();
		for (int i=0; i<4; i++) {
			buttonLists[i] = new ArrayList <RoomButton>();
			Box box;
			JButton placeholder;
			switch (i) {
			case DirectionButton.NORTH:
				box = Box.createHorizontalBox();
				gbc.gridx = 1;
				gbc.gridy = 0;
				placeholder = new JButton("North");
				break;
			case DirectionButton.EAST:
				box = Box.createVerticalBox();
				gbc.gridx = 2;
				gbc.gridy = 1;
				placeholder = new JButton("East");
				break;
			case DirectionButton.SOUTH:
				box = Box.createHorizontalBox();
				gbc.gridx = 1;
				gbc.gridy = 2;
				placeholder = new JButton("South");
				break;
			default:
				box = Box.createVerticalBox();
				gbc.gridx = 0;
				gbc.gridy = 1;
				placeholder = new JButton("West");
				break;
			}
			add(box, gbc);
			boxes[i] = box;
			placeholders[i] = placeholder;
			placeholder.setEnabled(false);
			box.add(placeholder);
		}
	}
	public void setButtons(byte direction, ArrayList <RoomScenario> rooms) {
		Box box = boxes[direction];
		ArrayList <RoomButton> list = (ArrayList <RoomButton>)(buttonLists[direction]);
		//Do we have a leftover spacer?
		boolean placeholder = (list.isEmpty() || !list.get(0).isVisible());
		//Remove the placeholder if we will add buttons
		if (rooms.size() > 0 && placeholder) {
			box.remove(placeholders[direction]);
		}
		//Set the buttons
		for (int i=0; i<rooms.size(); i++) {
			RoomScenario room = rooms.get(i);
			if (i < list.size()) {
				//Edit an existing button
				list.get(i).set(room);
			}
			else {
				//Create a new one
				RoomButton button = new RoomButton(room, driver);
				list.add(button);
				box.add(button);
			}
		}
		//Remove extras
		for (int i=list.size()-1; i>=rooms.size(); i--) {
			list.get(i).setVisible(false);
		}
		//Add a placeholder if we have no buttons
		if (rooms.isEmpty() && !placeholder) {
			box.add(placeholders[direction]);
		}
		//Repaint here to guarantee removed buttons are removed
		repaint();
	}
	//testing
	public static void main(String[] args) {
	}
}
