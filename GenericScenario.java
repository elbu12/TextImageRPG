package main;

import java.awt.Image;

public class GenericScenario extends Scenario{
	private final Image image;
	private final String text;
	public GenericScenario(int y, int x, boolean[] directions, String text, Image image) {
		super(y, x, directions);
		this.image = image;
		this.text = text;
	}
	public String getMainText() {
		return text;
	}
	
	public Image getImage() {
		return image;
	}
}
