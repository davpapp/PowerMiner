import java.awt.AWTException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.opencv.core.Rect2d;
import org.opencv.tracking.Tracker;
import org.opencv.tracking.TrackerKCF;

public class IronMiner {
	
	public static final int IRON_ORE_MINING_TIME_MILLISECONDS = 1320;
	public static final int MAXIMUM_DISTANCE_TO_WALK_TO_IRON_ORE = 400;
	public static final Point GAME_WINDOW_CENTER = new Point(Constants.GAME_WINDOW_WIDTH / 2, Constants.GAME_WINDOW_HEIGHT / 2);
	
	Cursor cursor; 
	CursorTask cursorTask;
	Inventory inventory;
	ObjectDetector objectDetector;
	Robot robot;
	Randomizer randomizer;
	
	public IronMiner() throws AWTException, IOException 
	{
		cursor = new Cursor();
		cursorTask = new CursorTask();
		inventory = new Inventory();
		objectDetector = new ObjectDetector();
		robot = new Robot();
		randomizer = new Randomizer();
	}
	
	public void run() throws Exception {
		
		while (true) {
			BufferedImage screenCapture = objectDetector.captureScreenshotGameWindow();
			ArrayList<DetectedObject> detectedObjects = objectDetector.getObjectsInImage(screenCapture);
			ArrayList<DetectedObject> ironOres = objectDetector.getObjectsOfClassInList(detectedObjects, "ironOre");
			
			DetectedObject closestIronOre = getClosestObjectToCharacter(ironOres);
			if (closestIronOre != null) {
				Tracker objectTracker = TrackerKCF.create();
				objectTracker.init(screenCapture, closestIronOre.getBoundingRect2d());
				cursor.moveAndLeftClickAtCoordinatesWithRandomness(closestIronOre.getCenterForClicking(), 10, 10);
				
				long mineStartTime = System.currentTimeMillis();
				int maxTimeToMine = randomizer.nextGaussianWithinRange(3500, 5000);
				
				
				while ((System.currentTimeMillis() - mineStartTime) < maxTimeToMine) {
					screenCapture = objectDetector.captureScreenshotGameWindow();
					objectTracker.update(screenCapture, boundingBox);
					Rectangle newBounds = new Rectangle();
					if (!objectDetector.isObjectPresentInBoundingBoxInImage(screenCapture, newBounds, "ironOre")) {
						System.out.println("Lost track! Finding new ore.");
						break;
					}
				}
			}
			
			dropInventoryIfFull();
		}
	}
	
	private void dropInventoryIfFull() throws Exception {
		inventory.update(); // TODO: add iron ore to inventory items
		if (inventory.isInventoryFull()) {
			cursorTask.optimizedDropAllItemsInInventory(cursor, inventory);
		}
	}
	
	
	private void mineClosestIronOre(ArrayList<DetectedObject> ironOres) throws Exception {
		DetectedObject closestIronOre = getClosestObjectToCharacter(ironOres);
		if (closestIronOre != null) {		
			cursor.moveAndLeftClickAtCoordinatesWithRandomness(closestIronOre.getCenterForClicking(), 10, 10);
			Thread.sleep(randomizer.nextGaussianWithinRange(IRON_ORE_MINING_TIME_MILLISECONDS - 350, IRON_ORE_MINING_TIME_MILLISECONDS + 1850));
		}
	}
	
	private DetectedObject getClosestObjectToCharacter(ArrayList<DetectedObject> detectedObjects) {
		int closestDistanceToCharacter = Integer.MAX_VALUE;
		DetectedObject closestObjectToCharacter = null;
		
		for (DetectedObject detectedObject : detectedObjects) {
			int objectDistanceToCharacter = getDistanceBetweenPoints(GAME_WINDOW_CENTER, detectedObject.getCenterForClicking());
			if (objectDistanceToCharacter < closestDistanceToCharacter) {
				closestDistanceToCharacter = objectDistanceToCharacter;
				closestObjectToCharacter = detectedObject;
			}
		}
		if (closestObjectToCharacter != null && closestDistanceToCharacter < MAXIMUM_DISTANCE_TO_WALK_TO_IRON_ORE) {
			return closestObjectToCharacter;
		}
		return null;
	}
	
	public int getDistanceBetweenPoints(Point startingPoint, Point goalPoint) {
		return (int) (Math.hypot(goalPoint.x - startingPoint.x, goalPoint.y - startingPoint.y));
	}
}
