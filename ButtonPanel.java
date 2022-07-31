package main;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel{
	private Driver driver;
	ArrayList <MyButton> buttonList;
	public ButtonPanel(Driver driver) {
		super();
		this.driver = driver;
		setAlignmentX(LEFT_ALIGNMENT);
		buttonList = new ArrayList <MyButton> ();
	}
	public void setButtons(String... texts) {
		int i;
		for (i=0; i<texts.length; i++) {
			if (i == buttonList.size()) {
				//Need a new button
				MyButton button = new MyButton(texts[i], i, driver);
				buttonList.add(button);
				add(button);
			}
			else {
				MyButton button = buttonList.get(i);
				button.set(texts[i], i);
			}
		}
		for (; i<buttonList.size(); i++) {
			//Remove extra buttons
			buttonList.get(i).setVisible(false);
		}
	}
	//testing
	public static void main(String[] args) {
		JFrame frame = new JFrame("test");
		Driver driver = new Driver();
		ButtonPanel panel = new ButtonPanel(driver);
		panel.setButtons("yes", "no", "delete");
		frame.add(panel);
		frame.pack();
		panel.setButtons("yes", "no");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
