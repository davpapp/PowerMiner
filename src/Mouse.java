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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mouse {
	
	private ArrayList<MousePath> mousePaths;
	PointerInfo pointer;
	
	public Mouse(String path) {
		pointer = MouseInfo.getPointerInfo();
		mousePaths = new ArrayList<MousePath>();
		readFile(path);
	}
	
	public void moveMouse(int xGoal, int yGoal) {
		Point startingPoint = pointer.getLocation();
		int x = (int) startingPoint.getX();
		int y = (int) startingPoint.getY();
		
		for (MousePath mousePath : mousePaths) {
			MousePoint pathStartingPoint = mousePath.getStartingPoint();
			if (pathStartingPoint.distance(startingPoint) < 20.0) {
				System.out.println("Found possible path!");
				mousePath.display();
			}
		}
	}
	
	public void readFile(String path) {
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
	
	
	
	/// ---------------------- Getters ----------------------------------------
	
	
	public int getNumberOfPaths() {
		return mousePaths.size();
	}
	
	public ArrayList<MousePath> getMousePaths() {
		return mousePaths;	
	}
	
	public void displayPaths() {
		for (MousePath path : mousePaths) {
			path.display();
			System.out.println("----------------------------------------------------------");
		}
		System.out.println("There are " + mousePaths.size() + " paths.");
	}
}