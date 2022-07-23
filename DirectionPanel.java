package main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class DirectionPanel extends JPanel{
	
	private MyButton[] buttons;
	public DirectionPanel(Driver driver) {
		super(new GridBagLayout());
		buttons = new MyButton[4];
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 1;
		gbc.gridy = 0;
		buttons[DirectionButton.NORTH] = new DirectionButton(DirectionButton.NORTH, driver);
		add(buttons[DirectionButton.NORTH], gbc);
		gbc.gridx = 2;
		gbc.gridy = 1;
		buttons[DirectionButton.EAST] = new DirectionButton(DirectionButton.EAST, driver);
		add(buttons[DirectionButton.EAST], gbc);
		gbc.gridx = 1;
		gbc.gridy = 2;
		buttons[DirectionButton.SOUTH] = new DirectionButton(DirectionButton.SOUTH, driver);
		add(buttons[DirectionButton.SOUTH], gbc);
		gbc.gridx = 0;
		gbc.gridy = 1;
		buttons[DirectionButton.WEST] = new DirectionButton(DirectionButton.WEST, driver);
		add(buttons[DirectionButton.WEST], gbc);
	}
	public void setButtons(boolean[] directions) {
		for (int i=0; i<4; i++) {
			buttons[i].setEnabled(directions[i]);
		}
	}
	//testing
	public static void main(String[] args) {
		JFrame frame = new JFrame("test");
		DirectionPanel panel = new DirectionPanel(new Driver());
		panel.buttons[3].setEnabled(false);
		frame.add(panel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}
}
