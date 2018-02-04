/* Reads a file of coordinates
 */
import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.awt.Robot;
import java.awt.event.InputEvent;
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
	public static final int MINIMUM_CLICK_LENGTH = 120;
	public static final int MAXIMUM_CLICK_LENGTH = 240;
	
	public static final int GAME_WINDOW_OFFSET_WIDTH = 100; // top left corner of main game screen, from top left corner of screen
	public static final int GAME_WINDOW_OFFSET_HEIGHT = 81;
	
	
	private Robot robot;
	private Random random = new Random();

	private ArrayList<ArrayList<CursorPath>> cursorPathsByDistance;
	
	public Cursor() throws AWTException {
		System.out.println("Initializing cursor...");
		initializeCursorPathsByDistanceFromFile("/home/dpapp/GhostMouse/coordinates.txt");

		robot = new Robot();
		random = new Random();
	}
	
	private void initializeCursorPathsByDistanceFromFile(String path) {
		initializeCursorPathsByDistance();
		ArrayList<CursorPath> cursorPaths = getArrayListOfCursorPathsFromFile(path);
		assignCursorPathsByDistance(cursorPaths);
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
	
	
	// TODO: make sure these are reasonable
	private int getRandomClickLength() {
		return random.nextInt(MAXIMUM_CLICK_LENGTH - MINIMUM_CLICK_LENGTH) + MINIMUM_CLICK_LENGTH;
	}
	
	public void leftClickCursor() throws InterruptedException {
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		Thread.sleep(getRandomClickLength());
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
		Thread.sleep(getRandomClickLength());
	}
	
	public void rightClickCursor() throws InterruptedException {
		robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
		Thread.sleep(20 + getRandomClickLength());
		robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
		Thread.sleep(getRandomClickLength());
	}
	
	public void moveAndLeftClickAtCoordinates(Point goalPoint) throws InterruptedException {
		moveCursorToCoordinates(goalPoint);
		leftClickCursor();
	}
	
	public Point moveAndLeftClickAtCoordinatesWithRandomness(Point goalPoint, int xTolerance, int yTolerance) throws InterruptedException {
		Point randomizedGoalPoint = randomizePoint(goalPoint, xTolerance, yTolerance);
		moveCursorToCoordinates(randomizedGoalPoint);
		leftClickCursor();
		return randomizedGoalPoint; // Return the point in case we need precise movement afterwards
	}
	
	public void moveAndRightClickAtCoordinates(Point goalPoint) throws InterruptedException {
		moveCursorToCoordinates(goalPoint);
		rightClickCursor();
	}
	
	public Point moveAndRightlickAtCoordinatesWithRandomness(Point goalPoint, int xTolerance, int yTolerance) throws InterruptedException {
		Point randomizedGoalPoint = randomizePoint(goalPoint, xTolerance, yTolerance);
		moveCursorToCoordinates(randomizedGoalPoint);
		rightClickCursor();
		return randomizedGoalPoint; // Return the point in case we need precise movement afterwards
	}

	public void moveCursorToCoordinates(Point goalPoint) throws InterruptedException {
		Point startingCursorPoint = getCurrentCursorPoint();
		int distanceToMoveCursor = calculateDistanceBetweenPoints(startingCursorPoint, goalPoint);
		if (distanceToMoveCursor == 0) { 
			return;
		}
		double angleToMoveCursor = calculateThetaBetweenPoints(startingCursorPoint, goalPoint);
		
		// TODO: check if exists
		CursorPath cursorPathToFollow = chooseCursorPathToFollowBasedOnDistance(distanceToMoveCursor);
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
		if (distanceToMoveCursor == 0) {
			return new CursorPath(new ArrayList<CursorPoint>());
		}
		
		int newDistanceToMoveCursor = findNearestPathLengthThatExists(distanceToMoveCursor);
		double scaleFactor = getScaleFactor(newDistanceToMoveCursor, distanceToMoveCursor);
		
		ArrayList<CursorPath> cursorPathsWithSameDistance = cursorPathsByDistance.get(newDistanceToMoveCursor);
		CursorPath scaledCursorPath = cursorPathsWithSameDistance.get(random.nextInt(cursorPathsWithSameDistance.size())).getScaledCopyOfCursorPath(1.0);
		return scaledCursorPath;
	}
	
	private int findNearestPathLengthThatExists(int distanceToMoveCursor) {
		int offset = 1;
		while (cursorPathsByDistance.get(distanceToMoveCursor + offset).size() == 0) {
			if (offset > 0) {
				offset = -(offset + 1);
			}
			else {
				offset = -offset + 1;
			}
		}
		return distanceToMoveCursor + offset;
	}
	
	private double getScaleFactor(int newDistanceToMoveCursor, int distanceToMoveCursor) {
		return (1.0 * newDistanceToMoveCursor / distanceToMoveCursor);
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
	
	private int getRandomIntSigned(int tolerance) {
		return random.nextInt(tolerance) - tolerance / 2;
	}
	
	private Point randomizePoint(Point goalPoint, int xTolerance, int yTolerance) {
		return new Point(goalPoint.x + getRandomIntSigned(xTolerance), goalPoint.y + getRandomIntSigned(yTolerance));
	}
	
	public void displayCursorPaths() {
		for (int i = 0; i < 200; i++) {
			System.out.println("There are " + cursorPathsByDistance.get(i).size() + " paths of size " + i);
		}
		System.out.println("--------------");
		for (int i = 0; i < cursorPathsByDistance.get(1).size(); i++) {
			cursorPathsByDistance.get(1).get(i).displayCursorPoints();
		}
		//cursorPathsByDistance.get(0).get(0).displayCursorPoints();
	}
}