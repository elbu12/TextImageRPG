package main;

/**
 * This represents a node that will be used to generate a scenario.
 * Part of the map-building algorithm.
 */

public class Prescenario {
	public boolean[] directions;
	public int neighbors;
	public int x;
	public int y;
	public boolean mustBeTerminal;
	public Prescenario(int y, int x) {
		this(y, x, false);
	}
	public Prescenario(int y, int x, boolean mustBeTerminal) {
		directions = new boolean[] {false, false, false, false};
		neighbors = 0;
		this.x = x;
		this.y = y;
		this.mustBeTerminal = mustBeTerminal;
	}
	public void connect(Prescenario p) {
		neighbors++;
		p.neighbors++;
		if (p.x > x) {
			//East
			directions[DirectionButton.EAST] = true;
			p.directions[DirectionButton.WEST] = true;
		}
		else if (p.x < x) {
			//West
			directions[DirectionButton.WEST] = true;
			p.directions[DirectionButton.EAST] = true;
		}
		else if (p.y > y) {
			//South
			directions[DirectionButton.SOUTH] = true;
			p.directions[DirectionButton.NORTH] = true;
		}
		else {
			//North
			directions[DirectionButton.NORTH] = true;
			p.directions[DirectionButton.SOUTH] = true;
		}
	}
}
