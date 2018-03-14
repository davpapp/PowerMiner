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
	ObjectDetectionHistory objectDetectionHistory;
	
	public IronMiner() throws AWTException, IOException 
	{
		int targetNumberOfDetectedOres = 2;
		cursor = new Cursor();
		cursorTask = new CursorTask();
		inventory = new Inventory();
		objectDetector = new ObjectDetector();
		robot = new Robot();
		humanBehavior = new HumanBehavior();
		randomDetector = new RandomDetector();
		worldHopper = new WorldHopper();
		cameraCalibrator = new CameraCalibrator(targetNumberOfDetectedOres);
		objectDetectionHistory = new ObjectDetectionHistory(targetNumberOfDetectedOres);
	}
	
	public void run() throws Exception {
		long startTime = System.currentTimeMillis();
		
		int count = 0;
		while (((System.currentTimeMillis() - startTime) / 1000.0 / 75) < 85) {
			BufferedImage screenCapture = objectDetector.captureScreenshotGameWindow();
			
			ArrayList<DetectedObject> detectedObjects = objectDetector.getObjectsInImage(screenCapture, 0.30);
			ArrayList<DetectedObject> ironOres = objectDetector.getIronOres(detectedObjects);
			
			readjustCameraIfObjectsAreNotBeingDetected(detectedObjects.size());
			
			DetectedObject closestIronOre = getClosestObjectToCharacter(ironOres);
			
			if (closestIronOre != null) {
				cursor.moveAndLeftClickAtCoordinatesWithRandomness(closestIronOre.getCenterForClicking(), 10, 10);
				
				int ironOreInInventory = inventory.getFirstIronOreInInventory();
				
				if (ironOreInInventory > -1) {
					int ironOreInInventoryColumn = ironOreInInventory % 7;
					int ironOreInInventoryRow = ironOreInInventory / 7;
				
					Point clickLocation = inventory.getClickCoordinatesForInventorySlot(ironOreInInventoryRow, ironOreInInventoryColumn);
				
					TrackerThread trackerThread = new TrackerThread(screenCapture, closestIronOre, objectDetector);
					trackerThread.start();
				
					DropperThread dropperThread = new DropperThread(clickLocation, cursor);
					dropperThread.start();
				
					trackerThread.waitTillDone();
					dropperThread.waitTillDone();
					
					Point rightClickLocation = cursor.getCurrentCursorPoint();
					cursorTask.leftClickDropOption(cursor, rightClickLocation, 0);
				}
				else {
					TrackerThread trackerThread = new TrackerThread(screenCapture, closestIronOre, objectDetector);
					trackerThread.start();
					trackerThread.waitTillDone();
				}
				count++;
				//System.out.println(count + ", time: " + ((System.currentTimeMillis() - startTime) / 1000 / 60));
			}
			humanBehavior.randomlyCheckMiningXP(cursor);
			randomDetector.dealWithRandoms(screenCapture, cursor);
			dropInventoryIfFull();
		}
	}

	
	private void readjustCameraIfObjectsAreNotBeingDetected(int detectedObjectsSize) throws Exception {
		boolean readjustCamera = objectDetectionHistory.updateHistory(detectedObjectsSize);
		if (readjustCamera) {
			cameraCalibrator.rotateUntilObjectFound(objectDetector, "ironOre");
			objectDetectionHistory.resetQueue();
		}
		
	}

	private void dropInventoryIfFull() throws Exception {
		inventory.updateLastSlot();
		if (inventory.isLastSlotInInventoryFull()) {
			cursorTask.optimizedDropAllItemsInInventory(cursor, inventory);
			Thread.sleep(Randomizer.nextGaussianWithinRange(1104, 1651));
		}
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
