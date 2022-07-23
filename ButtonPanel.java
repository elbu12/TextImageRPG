package main;

import java.util.ArrayDeque;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ButtonPanel extends JPanel{
	private Driver driver;
	ArrayList <MyButton> buttonList;
	ArrayDeque <MyButton> extras;
	public ButtonPanel(Driver driver) {
		super();
		this.driver = driver;
		setAlignmentX(LEFT_ALIGNMENT);
		buttonList = new ArrayList <MyButton> ();
		extras = new ArrayDeque <MyButton> ();
	}
	public void setButtons(String... texts) {
		int i;
		for (i=0; i<texts.length; i++) {
			if (i == buttonList.size()) {
				//Need a new button
				MyButton button;
				if (extras.isEmpty()) {
					//Make a new one
					button = new MyButton(texts[i], i, driver);
				}
				else {
					button = extras.pop();
					button.set(texts[i], i);
				}
				buttonList.add(button);
				add(button);
			}
			else {
				buttonList.get(i).set(texts[i], i);
			}
		}
		while (buttonList.size() > i) {
			//Remove extra buttons
			MyButton button = buttonList.remove(buttonList.size()-1);
			extras.push(button);
			remove(button);
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
