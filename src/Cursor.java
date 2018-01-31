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
	
	private Robot robot;
	private Random random = new Random();

	private ArrayList<ArrayList<CursorPath>> cursorPathsByDistance;
	
	public Cursor() throws AWTException {
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
	
	
	
	private int getRandomClickLength() {
		return random.nextInt(MAXIMUM_CLICK_LENGTH - MINIMUM_CLICK_LENGTH) + MINIMUM_CLICK_LENGTH;
	}
	
	public void leftClickCursor() throws InterruptedException {
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		Thread.sleep(getRandomClickLength());
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}
	
	public void rightClickCursor() throws InterruptedException {
		robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
		Thread.sleep(getRandomClickLength());
		robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
	}
	
	public void moveAndLeftClickAtCoordinates(Point goalPoint) throws InterruptedException {
		moveCursorToCoordinates(goalPoint);
		leftClickCursor();
	}
	
	public void moveAndRightClickAtCoordinates(Point goalPoint) throws InterruptedException {
		moveCursorToCoordinates(goalPoint);
		rightClickCursor();
	}

	public void moveCursorToCoordinates(Point goalPoint) throws InterruptedException {
		Point startingCursorPoint = getCurrentCursorPoint();
		int distanceToMoveCursor = calculateDistanceBetweenPoints(startingCursorPoint, goalPoint);
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
		ArrayList<CursorPath> cursorPathsWithSameDistance = cursorPathsByDistance.get(distanceToMoveCursor);
		// TODO: Error check if path of this size exists
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
}