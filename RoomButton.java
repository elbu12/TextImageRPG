package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

//Similar to a direction button, but leads to
//a room within a building

public class RoomButton extends JButton implements ActionListener{
	private RoomScenario room;
	private Driver driver;
	
	public RoomButton(RoomScenario room, Driver driver) {
		super(room.getMapText());
		this.room = room;
		this.driver = driver;
		addActionListener(this);
		setFocusable(false);
	}
	public void actionPerformed(ActionEvent e) {
		driver.roomButtonPressed(room);
	}
	public void set(RoomScenario room) {
		setText(room.getMapText());
		this.room = room;
		setVisible(true);
	}
}
