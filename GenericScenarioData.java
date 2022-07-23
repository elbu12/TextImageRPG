package main;

import java.awt.Image;

//Represents a particular generic scenario
//Each one requires a text to be displayed
//and a folder name from which to take images
public class GenericScenarioData {
	private final String text;
	private final RandomImageSequence images;
	public GenericScenarioData(String text, String imageFolderName) {
		this.text = text;
		images = ImageReader.getAllRandom(imageFolderName);
	}
	public String getText() {
		return text;
	}
	public Image getImage() {
		return images.get();
	}
}
