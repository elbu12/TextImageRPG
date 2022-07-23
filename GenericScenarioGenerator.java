package main;

import java.util.ArrayList;

/**
 * Generates a generic scenario, without special content.
 * 
 * Internally contains an array of array lists, each one
 * corresponding to a certain number of directions. When
 * asked to generate a generic scenario, it will count the
 * number of directions and pick a random scenario from the
 * corresponding array list.
 * 
 * To add more generic scenarios, create a data object for
 * them and add them to the corresponding list(s).
 * 
 * Use "make" to make the data object and "add" to add it
 */
public class GenericScenarioGenerator {
	
	private ArrayList[] scenarioData;
	public GenericScenarioGenerator() {
		scenarioData = new ArrayList[5];
		for (int i=1; i<5; i++) {
			scenarioData[i] = new ArrayList <GenericScenarioData>();
		}
		//Add scenario data here
		GenericScenarioData path = make("The path continues through the forest.", "path");
		add(2, path);
		add(3, path);
		add(4, path);
		
		add(1, make("You find some old ruins, but nothing of value.", "ruins"));
	}
	private GenericScenarioData make(String text, String imageFolderName) {
		return new GenericScenarioData(text, imageFolderName);
	}
	private void add(int index, GenericScenarioData data) {
		scenarioData[index].add(data);
	}
	private void add(int index, GenericScenarioData data, int times) {
		//This method adds something multiple times, in case we want non-uniform distribution
		for (int i=0; i<times; i++) {
			add(index, data);
		}
	}
	public GenericScenario get(int y, int x, boolean[] directions) {
		int d = 0;
		for (boolean b : directions) {
			if (b) {
				d++;
			}
		}
		if (d == 0) {
			System.out.println("Error! Attempting to make generic scenario with zero directions.");
			System.exit(0);
			return null;
		}
		//Get the list for this number of directions
		ArrayList <GenericScenarioData> list = (ArrayList <GenericScenarioData>)(scenarioData[d]);
		//Get a random element from the list
		GenericScenarioData data = list.get((int)(Math.random()*list.size()));
		return new GenericScenario(y, x, directions, data.getText(), data.getImage());
	}
}
