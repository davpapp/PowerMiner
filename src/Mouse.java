/* Reads a file of coordinates
 */
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mouse {
	private ArrayList<ArrayList<ArrayList<MousePath>>> grid;
	int granularity;
	//private ArrayList<MousePath> mousePaths;
	private int windowWidth;
	private int windowHeight;
	PointerInfo pointer;
	
	public Mouse(String path, int windowWidth, int windowHeight) {
		this.windowWidth = windowWidth;
		this.windowHeight = windowHeight;
		granularity = 10;
		// TODO: Is there another way to get the pointer location??
		pointer = MouseInfo.getPointerInfo();
		
		grid = new ArrayList<ArrayList<ArrayList<MousePath>>>();
		for (int i = 0; i < 2 * (windowWidth / granularity) + 1; i++) {
			grid.add(new ArrayList<ArrayList<MousePath>>());
			for (int j = 0; j < 2 * (windowHeight / granularity) + 1; j++) {
				grid.get(i).add(new ArrayList<MousePath>());
			}
		}
		
		System.out.println("Grid size: " + grid.size() + "x" + grid.get(0).size());
		ArrayList<MousePath> mousePaths = readFile(path);
		assignPathsToGrid(mousePaths);
	}
	
	public void moveMouse(int endingX, int endingY) {
		int[] mouseLoc = getMouseLocation();
		int deltaX = endingX - mouseLoc[0];
		int deltaY = endingY - mouseLoc[1];
		int[] gridIndex = getGridIndex(deltaX, deltaY);
		
		// Fetch from map
	}
	

	public int[] getMouseLocation() {
		Point startingPoint = pointer.getLocation();
		int x = (int) startingPoint.getX();
		int y = (int) startingPoint.getY();
		int loc[] = {x, y};
		return loc;
	}
	
	
	public int[] getGridIndex(int deltaX, int deltaY) {
		int offsetX = windowWidth / granularity;
		int offsetY = windowHeight / granularity;
		int[] gridIndex = {deltaX / granularity + offsetX, deltaY / granularity + offsetY};
		return gridIndex;	
	}
	
	public void assignPathsToGrid(ArrayList<MousePath> mousePaths) {
		for (MousePath mousePath : mousePaths) {
			int deltaX = mousePath.getDeltaX();
			int deltaY = mousePath.getDeltaY();

			int[] gridIndex = getGridIndex(deltaX, deltaY);
			//System.out.println(deltaX + "," + deltaY);
			//System.out.println("index: " + gridIndex[0] + "," + gridIndex[1]);
			grid.get(gridIndex[0]).get(gridIndex[1]).add(mousePath);
		}
	}
	
	public ArrayList<MousePath> readFile(String path) {
		ArrayList<MousePath> mousePaths = new ArrayList<MousePath>();
		try {
			File file = new File(path);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			Pattern linePattern = Pattern.compile("[0-9]*,[0-9]*,[0-9]*$");
			
			String line;
			MousePoint lastPoint = new MousePoint(0, 0, 0);
			int numberOfRepeats = 0;
			ArrayList<MousePoint> currentPath = new ArrayList<MousePoint>();
			currentPath.add(lastPoint);

			while ((line = bufferedReader.readLine()) != null) {
				if (!isLineValid(line, linePattern)) {
					System.out.println(line + " does not match regex -- SKIPPING");
					continue;
				}

				MousePoint point = getPointFromLine(line);
				if (!point.isValid()) {
					continue;
				}
				
				if (point.isSameLocation(lastPoint)) {
					numberOfRepeats++;
					if (numberOfRepeats == 20) {
						if (currentPath.size() < 5) {
							continue;
						}
						MousePath newPath = new MousePath(currentPath);
						mousePaths.add(newPath); // Deep copies
						currentPath.clear();
					}
				}
				else {
					numberOfRepeats = 0;
					currentPath.add(point);
				}
				lastPoint = point;
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mousePaths;
	}

	private boolean isLineValid(String line, Pattern linePattern) {	
		Matcher matcher = linePattern.matcher(line);
		if (matcher.find()) {
			return true;
		}
		return false;
	}
	
	private MousePoint getPointFromLine(String line) {
		String[] parts = line.split(Pattern.quote(","));
		return new MousePoint(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]));
	}
	
	
	public void displayPaths() {
		for (int i = 0; i < 2 * (windowWidth / granularity) + 1; i++) {
			for (int j = 0; j < 2 * (windowHeight / granularity) + 1; j++) {
				if (grid.get(i).get(j).size() > 0) {
					System.out.println("(" + i + "," + j + ")");
					System.out.println("There are " + grid.get(i).get(j).size() + " paths in this delta range.");
				}
			}
		}
	}
}