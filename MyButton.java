package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class MyButton extends JButton implements ActionListener{
	protected Driver driver;
	protected int id;
	public MyButton(String text, int id, Driver driver) {
		super(text);
		addActionListener(this);
		this.id = id;
		this.driver = driver;
		setFocusable(false);
	}
	public void actionPerformed(ActionEvent e) {
		driver.buttonPressed(id);
	}
	public void set(String text, int id) {
		setText(text);
		this.id = id;
		setVisible(true);
	}
}
