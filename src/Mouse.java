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
	private HashMap<Integer[], ArrayList<MousePath>> gridMap;
	//private ArrayList<MousePath> mousePaths;
	PointerInfo pointer;
	
	public Mouse(String path) {
		// TODO: Is there another way to get the pointer location??
		pointer = MouseInfo.getPointerInfo();
		gridMap = new HashMap<Integer[], ArrayList<MousePath>>();
		ArrayList<MousePath> mousePaths = readFile(path);
		assignPathsToGrid(mousePaths);
	}
	
	public void moveMouse(int endingX, int endingY) {
		int[] mouseLoc = getMouseLocation();
		int deltaX = endingX - mouseLoc[0];
		int deltaY = endingY - mouseLoc[1];
		Integer[] gridKey = getGridMapKey(deltaX, deltaY);
		
		// Fetch from map
	}
	

	public int[] getMouseLocation() {
		Point startingPoint = pointer.getLocation();
		int x = (int) startingPoint.getX();
		int y = (int) startingPoint.getY();
		int loc[] = {x, y};
		return loc;
	}
	
	
	public Integer[] getGridMapKey(int deltaX, int deltaY) {
		Integer[] gridKey = {deltaX / 100, deltaY / 100};
		return gridKey;	
	}
	
	public void assignPathsToGrid(ArrayList<MousePath> mousePaths) {
		Integer[] key1 = getGridMapKey(0, 0);
		Integer[] key2 = getGridMapKey(0, 0);
		gridMap.put(key1, new ArrayList<MousePath>());
		if (gridMap.containsKey(key2)) {
			System.out.println("same key!");
		}
		if (gridMap.containsKey(key1)) {
			System.out.println("Same key2!");
		}
		/*for (MousePath mousePath : mousePaths) {
			int deltaX = mousePath.getDeltaX();
			int deltaY = mousePath.getDeltaY();
			Integer[] gridKey = getGridMapKey(deltaX, deltaY);
			
			if (gridMap.containsKey(gridKey)) {
				System.out.println("Same category!");
				gridMap.get(gridKey).add(mousePath);
			}
			else {
				ArrayList<MousePath> newPath = new ArrayList<MousePath>();
				newPath.add(mousePath);
				gridMap.put(gridKey, newPath);
			}			
		}*/
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
		System.out.println("Displaying paths in HashMap...");
		for (HashMap.Entry<Integer[], ArrayList<MousePath>> entry : gridMap.entrySet()) {
			Integer[] gridKey = entry.getKey();
			System.out.println("Key is: (" + gridKey[0] + ", " + gridKey[1] + ")");
			
			ArrayList<MousePath> mousePaths = entry.getValue();
			System.out.println("There are " + mousePaths.size() + " paths with these deltas.");
			for (MousePath path : mousePaths) {
				//path.display();
				System.out.println("----------------------------------------------------------");
			}
			System.out.println("Size of HashMap: " + gridMap.size());
		}
	}
}