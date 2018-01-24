
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Mouse {
	
	private ArrayList<MousePath> mousePaths;
	
	public Mouse(String path) {
		mousePaths = new ArrayList<MousePath>();
		readFile(path);
	}
	
	public void readFile(String path) {
		try {
			File file = new File(path);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			Pattern linePattern = Pattern.compile("x:[0-9]* y:[0-9]* [0-9]*");
			
			String line;
			Point lastPoint = new Point(0, 0, 0);
			int numberOfRepeats = 0;
			ArrayList<Point> currentPath = new ArrayList<Point>();
			currentPath.add(lastPoint);
			
			//int count = 0;
			while ((line = bufferedReader.readLine()) != null) {
				if (!isLineValid(line, linePattern)) {
					System.out.println(line + " does not match regex -- SKIPPING");
					continue;
				}
				//count += 1;
				//if (count > 1000) break;
				Point point = getPointFromLine(line);
				
				if (point.getX() == lastPoint.getX() && point.getY() == lastPoint.getY()) {
					numberOfRepeats += 1;
					//System.out.println("Mouse at same point...");
					if (numberOfRepeats == 20) {
						//System.out.println("Creating new path!");
						//System.out.println("Current path length:" + currentPath.size());
						MousePath newPath = new MousePath(currentPath);
						mousePaths.add(newPath);
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
	
	private Point getPointFromLine(String line) {
		String[] parts = line.split(Pattern.quote(":"));
		/*System.out.println(line);
		System.out.println(parts[0]);
		System.out.println(parts[1]);
		System.out.println(parts[2]);*/
		return new Point(0, 0, 0);
	}
	
	public void displayPaths() {
		for (MousePath path : mousePaths) {
			path.display();
			System.out.println("----------------------------------------------------------");
		}
	}
}