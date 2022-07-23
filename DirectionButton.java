package main;

import java.awt.event.ActionEvent;

public class DirectionButton extends MyButton{
	
	public static final byte NORTH = 0;
	public static final byte EAST = 1;
	public static final byte SOUTH = 2;
	public static final byte WEST = 3;
	
	public DirectionButton(byte direction, Driver driver) {
		super(direction == NORTH ? "North" :
			(direction == EAST ? "East" :
				(direction == SOUTH ? "South" :
					"West")), direction, driver);
	}
	public void actionPerformed(ActionEvent e) {
		driver.directionButtonPressed(id);
	}
	public void set(String text, int id) {
		//Do not use for direction buttons
		System.out.println("Error: Trying to set text/id for direction button.");
	}
}
