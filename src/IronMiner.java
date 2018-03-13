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
	
	Cursor cursor; 
	CursorTask cursorTask;
	Inventory inventory;
	ObjectDetector objectDetector;
	Robot robot;
	HumanBehavior humanBehavior;
	CameraCalibrator cameraCalibrator;
	RandomDetector randomDetector;
	WorldHopper worldHopper;
	
	public IronMiner() throws AWTException, IOException 
	{
		cursor = new Cursor();
		cursorTask = new CursorTask();
		inventory = new Inventory();
		objectDetector = new ObjectDetector();
		robot = new Robot();
		humanBehavior = new HumanBehavior();
		cameraCalibrator = new CameraCalibrator();
		randomDetector = new RandomDetector();
	}
	
	public void run() throws Exception {
		long startTime = System.currentTimeMillis();
		int noIronOresDetected = 0;
		
		int count = 0;
		while (((System.currentTimeMillis() - startTime) / 1000.0 / 75) < 37) {
			count++;
			BufferedImage screenCapture = objectDetector.captureScreenshotGameWindow();
			
			ArrayList<DetectedObject> detectedObjects = objectDetector.getObjectsInImage(screenCapture, 0.30);
			ArrayList<DetectedObject> ironOres = objectDetector.getIronOres(detectedObjects);
			
			if (ironOres.size() == 0) {
				noIronOresDetected++;
			}
			else {
				noIronOresDetected = 0;
			}
			if (noIronOresDetected > 80) {
				cameraCalibrator.rotateUntilObjectFound(objectDetector, "ironOre");
			}
			
			DetectedObject closestIronOre = getClosestObjectToCharacter(ironOres);
			
			if (closestIronOre != null) {
				cursor.moveAndLeftClickAtCoordinatesWithRandomness(closestIronOre.getCenterForClicking(), 10, 10);
				
				System.out.println("Starting threads!");
				TrackerThread trackerThread = new TrackerThread(screenCapture, closestIronOre, objectDetector);
				trackerThread.start();
				DropperThread dropperThread = new DropperThread(inventory, cursor);
				dropperThread.start();
				
				trackerThread.waitTillDone();
				dropperThread.waitTillDone();
								
				System.out.println("Both threads finished?");
			}
			if (count % 30 == 0) {
				System.out.println("WAITING #############################################");
				Thread.sleep(5000);
			}
			//humanBehavior.randomlyCheckMiningXP(cursor);
			//randomDetector.dealWithRandoms(screenCapture, cursor);
			//dropInventoryIfFull();
		}
	}
	
	/*private void dropOre() throws Exception {
		inventory.update();
		System.out.println("Thread 1 [mouse hover] finished!");
		//cursorTask.dropOre(cursor, inventory);
	}*/
	
	/*private void trackOre(DetectedObject closestIronOre, BufferedImage screenCapture) throws Exception {
		Rect2d boundingBox = closestIronOre.getBoundingRect2d();
		ObjectTracker ironOreTracker = new ObjectTracker(screenCapture, boundingBox);
		
		long miningStartTime = System.currentTimeMillis();
		int maxTimeToMine = Randomizer.nextGaussianWithinRange(3400, 4519);
		
		boolean objectTrackingFailure = false;
		boolean oreAvailable = true;
		int oreLostCount = 0;
		while (!objectTrackingFailure && oreLostCount < 3 && !isTimeElapsedOverLimit(miningStartTime, maxTimeToMine)) {
			BufferedImage screenCapture2 = objectDetector.captureScreenshotGameWindow();
			ArrayList<DetectedObject> detectedObjects = objectDetector.getObjectsInImage(screenCapture2, 0.15);
			ArrayList<DetectedObject> ironOres = objectDetector.getObjectsOfClassInList(detectedObjects, "ironOre");
			objectTrackingFailure = ironOreTracker.update(screenCapture, boundingBox);
			oreAvailable = objectDetector.isObjectPresentInBoundingBoxInImage(ironOres, boundingBox, "ironOre");
			if (!oreAvailable) {
				oreLostCount++;
			}
			else {
				oreLostCount = 0;
			}
		}
		System.out.println("Thread 2 [track ore] finished!");
	}*/
	
	/*private void dropOre() throws IOException {
		inventory.update();
		if (inventory.containsItem("ironOre")) {
			cursor.moveAndRightlickAtCoordinatesWithRandomness(inventory.getClickCoordinatesForInventorySlot(0, 0), 15, 15);
			// move cursor down 40 pixels
			cursor.moveAndLeftClickAtCoordinatesWithRandomness(goalPoint, 20, 5);
		}
	}*/
	
	private void dropInventoryIfFull() throws Exception {
		inventory.updateLastSlot();
		if (inventory.isLastSlotInInventoryFull()) {
			cursorTask.optimizedDropAllItemsInInventory(cursor, inventory);
			Thread.sleep(Randomizer.nextGaussianWithinRange(1104, 1651));
		}
	}
	
	private boolean isTimeElapsedOverLimit(long startTime, int timeLimit) {
		return (System.currentTimeMillis() - startTime) > timeLimit;
	}
	
	private DetectedObject getClosestObjectToCharacter(ArrayList<DetectedObject> detectedObjects) {
		int closestDistanceToCharacter = Integer.MAX_VALUE;
		DetectedObject closestObjectToCharacter = null;
		
		for (DetectedObject detectedObject : detectedObjects) {
			int objectDistanceToCharacter = getDistanceBetweenPoints(Constants.getCharacterLocation(), detectedObject.getCenterForClicking());
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
