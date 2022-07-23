package main;

import java.awt.Image;

public class RandomImageSequence {
	private Image[] images;
	private int index;
	public RandomImageSequence(Image[] images) {
		this.images = images;
		Shuffler.shuffle(images);
		index = 0;
	}
	public Image get() {
		Image image = images[index];
		index++;
		index %= images.length;
		return image;
	}
}
