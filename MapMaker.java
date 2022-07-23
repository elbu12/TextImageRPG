package main;

import java.util.ArrayList;

/**
 * Contains algorithm for building map
 * 
 * Nodes that connect to only one other node are considered "terminal."
 * They are special and get special scenarios, even if they're just
 * do-nothing scenarios. A map must have a certain minimum number of
 * terminal scenarios to allow all of the special scenarios.
 */
public class MapMaker {
	public static Prescenario[][] generateMap(int height, int width, int minimumTerminalNodes){
		//Generates a random map via a search-type algorithm
		//First, generate prescenarios which will be used to generate actual scenarios
		Prescenario[][] map = new Prescenario[height][width];
		//Place the special scenarios
		if (!divideAndPlace(map, height, width, true, minimumTerminalNodes)) {
			System.out.println("unable to place all required terminal nodes.");
			for (int i=0; i<height; i++) {
				for (int j=0; j<width; j++) {
					if (map[i][j] == null) {
						System.out.print(".");
					}
					else {
						System.out.println("X");
					}
				}
				System.out.println();
			}
			System.exit(0);
		}
		//start at the center node and work outward until we find non-special one
		Prescenario center = null;
		int cx0 = width/2;
		int cx1 = width/2;
		int cy0 = height/2;
		int cy1 = height/2;
		bigWhile:
		while (true) {
			for (int i=cx0; i<=cx1; i++) {
				if (map[cy0][i] == null) {
					//found one
					center = new Prescenario(cy0, i, false);
					break bigWhile;
				}
				if (map[cy1][i] == null) {
					//found one
					center = new Prescenario(cy1, i, false);
					break bigWhile;
				}
			}
			for (int i=cy0+1; i<cy1-1; i++) {
				if (map[i][cx0] == null) {
					//found one
					center = new Prescenario(i, cx0, false);
					break bigWhile;
				}
				if (map[i][cx1] == null) {
					//found one
					center = new Prescenario(i, cx1, false);
					break bigWhile;
				}
			}
			//We did not find one in this square
			//Have we tried the entire map now?
			if (cx0 == 0 && cy0 == 0 && cx1 == width-1 && cy1 == height-1) {
				System.out.println("Unable to find a starting point for map generation.");
				System.exit(0);
			}
			cx0 = Math.max(cx0-1, 0);
			cx1 = Math.min(cx1+1, width-1);
			cy0 = Math.max(cy0-1, 0);
			cy1 = Math.min(cy1+1, height-1);
		}
		//Now we do a randomized search
		//The list represents potential placements and their parent node
		ArrayList <Prescenario[]> list = new ArrayList <> ();
		list.add(new Prescenario[] {center, null});
		//orphans refers to special nodes disconnected from the rest
		//when orphans == 0, all of the required nodes are connected
		int orphans = minimumTerminalNodes;
		while (orphans > 0 && !list.isEmpty()) {
			//pick a random element from the list
			int r = (int)(Math.random()*list.size());
			Prescenario[] pick = list.remove(list.size()-1);
			if (r != list.size()) {
				Prescenario[] temp = list.get(r);
				list.set(r, pick);
				pick = temp;
			}
			Prescenario next = pick[0];
			int y = next.y;
			int x = next.x;
			Prescenario prev = pick[1];
			if (map[y][x] != null) {
				Prescenario old = map[y][x];
				if (old.mustBeTerminal && old.neighbors == 0) {
					//special node; now we connect it to map
					orphans--;
					old.connect(prev);
				}
				else if (Math.random() < 0.05 && !old.mustBeTerminal) {
					//allow occasional cycles
					old.connect(prev);
				}
				continue;
			}
			//Make this a prescenario and look at its neighbors
			map[y][x] = next;
			if (prev != null) {
				next.connect(prev);
			}
			for (int i=0; i<4; i++) {
				int nx = next.x;
				int ny = next.y;
				switch (i) {
				case 0:
					nx++;
					if (nx == width) {
						continue;
					}
					break;
				case 1:
					nx--;
					if (nx < 0) {
						continue;
					}
					break;
				case 2:
					ny++;
					if (ny == height) {
						continue;
					}
					break;
				default:
					ny--;
					if (ny < 0) {
						continue;
					}
					break;
				}
				Prescenario neighbor = map[ny][nx];
				if (neighbor == null) {
					neighbor = new Prescenario(ny, nx, false);
				}
				else if (neighbor.mustBeTerminal && neighbor.neighbors == 0) {
					//Also acceptable; connecting to special orphan node
				}
				else {
					continue;
				}
				list.add(new Prescenario[] {neighbor, next});
			}
			
		}
		if (orphans > 0) {
			System.out.println("unable to connect prescenarios sufficiently");
			System.exit(0);
			return null;
		}
		return map;
	}
	private static boolean divideAndPlace(Prescenario[][] map, int iheight, int jwidth, boolean horizontal, int needed) {
		if (needed == 0) {
			return true;
		}
		return divideAndPlace(map, 0, 0, iheight, jwidth, horizontal, needed);
	}
	//height and width both include the first node
	private static boolean divideAndPlace(Prescenario[][] map, int imin, int jmin, int iheight, int jwidth,
			boolean horizontal, int needed) {
		if (needed == 1) {
			int i = (imin + (int)(Math.random()*iheight));
			int j = (jmin + (int)(Math.random()*jwidth));
			map[i][j] = new Prescenario(i, j, true);
			return true;
		}
		else if (iheight == 1 && jwidth == 1) {
			//Insufficient space to place nodes!
			return false;
		}
		//Split into smaller segments
		if ((iheight == 1) || (horizontal && jwidth > 1)) {
			//split horizontally, across a vertical line
			boolean a = divideAndPlace(map, imin, jmin, iheight, jwidth/2, false, needed/2);
			boolean b = divideAndPlace(map, imin, jmin+(jwidth/2), iheight, (jwidth+1)/2, false, (needed+1)/2);
			return a && b;
		}
		else {
			//split vertically, across a horizontal line
			boolean a = divideAndPlace(map, imin, jmin, iheight/2, jwidth, true, needed/2);
			boolean b = divideAndPlace(map, imin+(iheight/2), jmin, (iheight+1)/2, jwidth, true, (needed+1)/2);
			return a && b;
		}
	}
	//testing
	public static void main(String[] args) {
		for (int i=0; i<100; i++) {
			generateMap(6, 12, 3);
		}
	}
}
