/* Reads a file of coordinates
 */
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cursor {
	
	public static final int NUMBER_OF_DISTANCES = 1000;
	private Robot robot;

	private ArrayList<ArrayList<CursorPath>> cursorPathsByDistance;
	
	public Cursor() throws AWTException {
		ArrayList<CursorPath> cursorPaths = getArrayListOfCursorPathsFromFile("/home/dpapp/GhostMouse/coordinates.txt");// read from file or something;
		initializeCursorPathsByDistance();
		assignCursorPathsByDistance(cursorPaths);
		
		robot = new Robot();
	}
	
	private void initializeCursorPathsByDistance() {
		this.cursorPathsByDistance = new ArrayList<ArrayList<CursorPath>>();
		for (int i = 0; i < NUMBER_OF_DISTANCES; i++) {
			this.cursorPathsByDistance.add(new ArrayList<CursorPath>());
		}
	}
	
	private ArrayList<CursorPath> getArrayListOfCursorPathsFromFile(String path) {
		CursorDataFileParser cursorDataFileParser = new CursorDataFileParser(path);
		return cursorDataFileParser.getArrayListOfCursorPathsFromFile();
	}
	
	private void assignCursorPathsByDistance(ArrayList<CursorPath> cursorPaths) {
		for (CursorPath cursorPath : cursorPaths) {
			if (cursorPath.isCursorPathReasonable()) { 
				addCursorPathToCursorPathsByDistance(cursorPath);
			}
		}
	}
	
	private void addCursorPathToCursorPathsByDistance(CursorPath cursorPath) {
		this.cursorPathsByDistance.get(cursorPath.getCursorPathDistance()).add(cursorPath);
	}
	
	public void moveCursorToCoordinates(Point goalPoint) throws InterruptedException {
		Point startingCursorPoint = getCurrentCursorPoint();
		int distanceToMoveCursor = calculateDistanceBetweenPoints(startingCursorPoint, goalPoint);
		double angleToMoveCursor = calculateThetaBetweenPoints(startingCursorPoint, goalPoint);
		CursorPath cursorPathToFollow = chooseCursorPathToFollowBasedOnDistance(distanceToMoveCursor);
		
		//cursorPathToFollow.displayCursorPoints();
		double angleToTranslatePathBy = angleToMoveCursor - cursorPathToFollow.getCursorPathTheta();
		followCursorPath(startingCursorPoint, angleToTranslatePathBy, cursorPathToFollow);
	}
	
	private void followCursorPath(Point startingCursorPoint, double angleToTranslatePathBy, CursorPath cursorPathToFollow) throws InterruptedException {
		for (CursorPoint untranslatedCursorPoint : cursorPathToFollow.getCursorPathPoints()) {
			Point translatedPointToClick = translatePoint(startingCursorPoint, angleToTranslatePathBy, untranslatedCursorPoint);
			robotMouseMove(translatedPointToClick);
			Thread.sleep(untranslatedCursorPoint.postMillisecondDelay);
		}
	}
	
	private Point translatePoint(Point startingCursorPoint, double angleToTranslateBy, CursorPoint untranslatedCursorPoint) {
		int x = (int) (startingCursorPoint.x + Math.cos(angleToTranslateBy) * untranslatedCursorPoint.x - Math.sin(angleToTranslateBy) * untranslatedCursorPoint.y);
		int y = (int) (startingCursorPoint.y + Math.sin(angleToTranslateBy) * untranslatedCursorPoint.x + Math.cos(angleToTranslateBy) * untranslatedCursorPoint.y);
		return new Point(x, y);
	}
	
	public void robotMouseMove(Point pointToMoveCursorTo) {
		robot.mouseMove(pointToMoveCursorTo.x, pointToMoveCursorTo.y);
	}

	private CursorPath chooseCursorPathToFollowBasedOnDistance(int distanceToMoveCursor) {
		ArrayList<CursorPath> cursorPathsWithSameDistance = cursorPathsByDistance.get(distanceToMoveCursor);
		return cursorPathsWithSameDistance.get(new Random().nextInt(cursorPathsWithSameDistance.size()));
	}
	
	private int calculateDistanceBetweenPoints(Point a, Point b) {
		return (int) (Math.hypot(a.x - b.x, a.y - b.y));
	}
	
	public double calculateThetaBetweenPoints(Point a, Point b) {
		return Math.atan2(1.0 * (b.y - a.y), 1.0 * (b.x - a.x));
	}
	
	public Point getCurrentCursorPoint() {
		return MouseInfo.getPointerInfo().getLocation();
	}
	
	/*public void displaycursorPathsByDistance() {
		for (int i = 0; i < cursorPathsByDistance.size(); i++) {
			System.out.println("There are " + cursorPathsByDistance.get(i).size() + " CursorPaths of length " + i);
		}
	}*/
}