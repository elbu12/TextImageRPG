package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener{
	private Driver driver;
	public KeyHandler(Driver driver) {
		this.driver = driver;
	}
	
	public void keyTyped(KeyEvent e) {}
	
	public void keyPressed(KeyEvent e) {
		// We only notice the pressing of the direction keys
		switch (e.getKeyCode()) {
		case KeyEvent.VK_UP:
			driver.directionButtonPressed(DirectionButton.NORTH);
			break;
		case KeyEvent.VK_RIGHT:
			driver.directionButtonPressed(DirectionButton.EAST);
			break;
		case KeyEvent.VK_DOWN:
			driver.directionButtonPressed(DirectionButton.SOUTH);
			break;
		case KeyEvent.VK_LEFT:
			driver.directionButtonPressed(DirectionButton.WEST);
			break;
		}
	}

	public void keyReleased(KeyEvent e) {}
}
