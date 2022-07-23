package main;

import java.awt.Image;
import java.io.File;

import javax.imageio.ImageIO;

//utility class for getting images
public class ImageReader {
	// gets an image from the img folder
	public static Image getImage(String name) {
		if (!name.startsWith("img")){
			name = "img/" + name;
		}
		try {
			return ImageIO.read(new File(addPrefix(name)));
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}
	
	private static File[] getFiles(String folder) {
		File file = new File(addPrefix(folder));
		File[] files = file.listFiles();
		//Filter out non-image files
		int bads = 0;
		for (int i=0; i<files.length - bads; i++) {
			file = files[i];
			String s = file.toString();
			if (!(s.endsWith("png") ||
					s.endsWith("jpg") ||
					s.endsWith("jpeg") ||
					s.endsWith("gif"))) {
				//Not an image file
				files[i] = files[files.length - (bads + 1)];
				bads++;
				i--;
			}
		}
		//make a new array without non-image files
		if (bads > 0) {
			File[] winnowed = new File[files.length - bads];
			System.arraycopy(files, 0, winnowed, 0, winnowed.length);
			return winnowed;
		}
		return files;
	}
	
	private static String addPrefix(String name){
		return (name.startsWith("img") ? name : "img/" + name);
	}
	
	// gets a random image from a particular folder
	public static Image getRandom(String folder) {
		File[] options = getFiles(folder);
		try {
			return ImageIO.read(options[(int)(Math.random()*options.length)]);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}
	
	public static RandomImageSequence getAllRandom(String folder) {
		return new RandomImageSequence(getAll(folder));
	}
	
	// gets all images from a folder
	public static Image[] getAll(String folder) {
		File[] options = getFiles(folder);
		Image[] all = new Image[options.length];
		try {
			for (int i=0; i<options.length; i++) {
				all[i] = ImageIO.read(options[i]);
			}
			return all;
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
			return null;
		}
	}
}
