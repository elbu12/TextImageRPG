package main;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class MapLabel extends JLabel{
	public static final Color PATH_COLOR = new Color(127, 127, 0);
	public static final int PATH_WIDTH = 10;
	
	private String text = null;
	private boolean[] directions = null;
	private boolean current;
	public MapLabel(ImageIcon icon) {
		super(icon);
	}
	public void set(String text, boolean[] directions, boolean current) {
		this.text = text;
		this.directions = directions;
		this.current = current;
		repaint();
	}
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (text != null) {
			//Special tile; write name
			g.setColor(current ? Color.black : Color.white);
			g.drawString(text, 10, 30);
		}
		else if (directions != null){
			//No text; draw the trail
			g.setColor(current ? Color.black : PATH_COLOR);
			int width = getWidth();
			int height = getHeight();
			for (int i=0; i<4; i++) {
				if (directions[i]) {
					switch (i) {
					case DirectionButton.NORTH:
						g.fillRect((width-PATH_WIDTH)/2, 0, PATH_WIDTH, (height+PATH_WIDTH)/2);
						break;
					case DirectionButton.EAST:
						g.fillRect((width-PATH_WIDTH)/2, (height-PATH_WIDTH)/2, (width+PATH_WIDTH)/2, PATH_WIDTH);
						break;
					case DirectionButton.SOUTH:
						g.fillRect((width-PATH_WIDTH)/2, (height-PATH_WIDTH)/2, PATH_WIDTH, (height+PATH_WIDTH)/2);
						break;
					case DirectionButton.WEST:
						g.fillRect(0, (height-PATH_WIDTH)/2, (width+PATH_WIDTH)/2, PATH_WIDTH);
						break;
					}
				}
			}
		}
	}
}
