package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class RoomScenario extends Scenario{
	/**
	 * Note: Room dimensions are in arbitrary units, but get scaled
	 * for pixel representation. Currently, a 2x2 room seems standard.
	 */
	public static final int LABEL_SCALE_HORIZONTAL = 50;
	public static final int LABEL_SCALE_VERTICAL = LABEL_SCALE_HORIZONTAL;
	
	private final int height;
	private final int width;
	private ArrayList[] connectedRooms;
	private RoomScenario[] connectedRoomArray = null;
	
	public RoomScenario(int y, int x, int height, int width) {
		super(y, x);
		this.height = height;
		this.width = width;
		connectedRooms = new ArrayList[4];
		for (int i=0; i<4; i++) {
			connectedRooms[i] = new ArrayList <RoomScenario>();
		}
	}
	public int getHeight() {
		return height;
	}
	public int getWidth() {
		return width;
	}
	public String getMapText() {
		switch (getKnowledge()) {
		case UNKNOWN:
		case IMPLIED:
			//Room unidentified
			return RoomButtonPanel.DEFAULT_ROOM_BUTTON;
		default:
			return super.getMapText();
		}
	}
	protected void makeLabel() {
		setLabel(new JLabel(""));
		label = new JLabel("");
		label.setOpaque(true);
		label.setPreferredSize(new Dimension(
				width * LABEL_SCALE_HORIZONTAL,
				height * LABEL_SCALE_VERTICAL));
		label.setBackground(Color.BLACK);
		label.setBorder(new LineBorder(Color.BLACK));
		label.setHorizontalAlignment(SwingConstants.CENTER);
	}
	public void updateLabel() {
		if (label == null) {
			//Some rooms may not appear on the map
			return;
		}
		switch (getKnowledge()) {
		case UNKNOWN:
		case IMPLIED:
			label.setBackground(Color.BLACK);
			label.setText("");
			break;
		case KNOWN:
			label.setBackground(Color.LIGHT_GRAY);
			label.setText(getMapText());
			break;
		default:
			label.setBackground(Color.WHITE);
			label.setText(getMapText());
			break;
		}
	}
	public void directionButtonPressed(int buttonId, Driver driver) {
		//If only one connected room is in that direction,
		//attempt to enter it
		if (connectedRooms[buttonId].size() == 1) {
			ArrayList <RoomScenario> list = (ArrayList <RoomScenario>)(connectedRooms[buttonId]);
			roomButtonPressed(list.get(0), driver);
		}
	}
	public void roomButtonPressed(RoomScenario room, Driver driver) {
		// Move to this new location
		// redraw the current scenario
		setKnowledge(KNOWN);
		reset();
		driver.setCurrentScenario(room);
	}
	public void add(RoomScenario room, byte direction) {
		connectedRooms[direction].add(room);
		//invalidates room array
		connectedRoomArray = null;
	}
	public void updateDirectionPanel(MyFrame frame) {
		frame.setRoomButtons(connectedRooms);
	}
	public static JPanel generateMapPanel(RoomScenario... rooms) {
		JPanel panel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		for (RoomScenario room : rooms) {
			room.makeLabel();
			JLabel label = room.getLabel();
			gbc.gridx = room.getx();
			gbc.gridy = room.gety();
			gbc.gridwidth = room.getWidth();
			gbc.gridheight = room.getHeight();
			panel.add(label, gbc);
		}
		return panel;
	}
	public int getConnectedRoomsSize() {
		int size = 0;
		for (ArrayList list : connectedRooms) {
			size += list.size();
		}
		return size;
	}
	public RoomScenario[] getConnectedRooms() {
		if (connectedRoomArray == null) {
			connectedRoomArray = new RoomScenario[getConnectedRoomsSize()];
			int i = 0;
			for (ArrayList list : connectedRooms) {
				for (Object o : list) {
					connectedRoomArray[i] = (RoomScenario)(o);
					i++;
				}
			}
		}
		return connectedRoomArray;
	}
}
