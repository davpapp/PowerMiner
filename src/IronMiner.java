import java.awt.AWTException;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect2d;
import org.opencv.tracking.Tracker;
import org.opencv.tracking.TrackerBoosting;
import org.opencv.tracking.TrackerGOTURN;
import org.opencv.tracking.TrackerKCF;
import org.opencv.tracking.TrackerMOSSE;

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
		long startTime = System.currentTimeMillis();
		long garbageCollectionTime = System.currentTimeMillis() + 60 * 5 * 1000;
		int framesWithoutObjects = 0;
		
		while (((System.currentTimeMillis() - startTime) / 1000.0 / 60) < 85) {
			long frameStartTime = System.currentTimeMillis();
			BufferedImage screenCapture = objectDetector.captureScreenshotGameWindow();
			System.out.println("looking for iron ores");
			
			ArrayList<DetectedObject> detectedObjects = objectDetector.getObjectsInImage(screenCapture, 0.30);
			ArrayList<DetectedObject> ironOres = objectDetector.getObjectsOfClassInList(detectedObjects, "ironOre");
			System.out.println("Found " + detectedObjects.size() + " objects.");
			
			if (ironOres.size() == 0) {
				framesWithoutObjects++;
				System.out.println("no objects found!");
			}
			else {
				framesWithoutObjects = 0;
			}
			if (framesWithoutObjects > 50) {
				CameraCalibrator cameraCalibrator = new CameraCalibrator();
				cameraCalibrator.rotateUntilObjectFound("ironOre");
			}
			
			DetectedObject closestIronOre = getClosestObjectToCharacter(ironOres);
			if (closestIronOre != null) {
				Rect2d boundingBox = closestIronOre.getBoundingRect2d();
				ObjectTracker ironOreTracker = new ObjectTracker(screenCapture, boundingBox);
				
				cursor.moveAndLeftClickAtCoordinatesWithRandomness(closestIronOre.getCenterForClicking(), 10, 10);
				
				long miningStartTime = System.currentTimeMillis();
				int maxTimeToMine = randomizer.nextGaussianWithinRange(3400, 4519);
				
				boolean objectTrackingFailure = false;
				boolean oreAvailable = true;
				int oreLostCount = 0;
				while (!objectTrackingFailure && oreLostCount < 3 && !isTimeElapsedOverLimit(miningStartTime, maxTimeToMine)) {
					long trackingFrameStartTime = System.currentTimeMillis();
					
					screenCapture = objectDetector.captureScreenshotGameWindow();
					detectedObjects = objectDetector.getObjectsInImage(screenCapture, 0.15);
					ironOres = objectDetector.getObjectsOfClassInList(detectedObjects, "ironOre");
					objectTrackingFailure = ironOreTracker.update(screenCapture, boundingBox);
					oreAvailable = objectDetector.isObjectPresentInBoundingBoxInImage(ironOres, boundingBox, "ironOre");
					if (!oreAvailable) {
						oreLostCount++;
					}
					else {
						oreLostCount = 0;
					}
					System.out.println("Tracking timespan: " + (System.currentTimeMillis() - trackingFrameStartTime));
				}
			}
			
			
			// Garbage Collection
			if (((System.currentTimeMillis() - garbageCollectionTime) / 1000.0 / 60) > 0) {
				System.out.println("Running garbage collection.");
				System.gc();
				garbageCollectionTime = System.currentTimeMillis() + randomizer.nextGaussianWithinRange(8500, 19340) * 60;
			}
			dropInventoryIfFull();
		}
	}
	
	private void dropInventoryIfFull() throws Exception {
		inventory.updateLastSlot();
		if (inventory.isLastSlotInInventoryFull()) {
			cursorTask.optimizedDropAllItemsInInventory(cursor, inventory);
		}
	}
	
	private boolean isTimeElapsedOverLimit(long startTime, int timeLimit) {
		return (System.currentTimeMillis() - startTime) > timeLimit;
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
