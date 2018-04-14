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
	MiningSuccessHistory miningSuccessHistory;
	ObjectDetectionHistory objectDetectionHistory;
	
	public IronMiner() throws AWTException, IOException 
	{
		int targetNumberOfDetectedOres = 3;
		cursor = new Cursor();
		cursorTask = new CursorTask();
		inventory = new Inventory();
		objectDetector = new ObjectDetector();
		robot = new Robot();
		humanBehavior = new HumanBehavior();
		cameraCalibrator = new CameraCalibrator(targetNumberOfDetectedOres);
		miningSuccessHistory = new MiningSuccessHistory();
		objectDetectionHistory = new ObjectDetectionHistory(targetNumberOfDetectedOres);
	}
	
	public void run() throws Exception {
		long startTime = System.currentTimeMillis();
		
		int count = 0;
		int worldHops = 0;
		int lastIronOreInInventory = -1;
		int noIronOresCount = 0;
		
		while (((System.currentTimeMillis() - startTime) / 1000.0 / 60) < 163) {
			BufferedImage screenCapture = ImageCapturer.captureScreenshotGameWindow();
			ArrayList<DetectedObject> detectedObjects = objectDetector.getObjectsInImage(screenCapture, 0.30);
			ArrayList<DetectedObject> ironOres = objectDetector.getIronOres(detectedObjects);
			
			readjustCameraIfObjectsAreNotBeingDetected(detectedObjects.size());
			humanBehavior.randomlyCheckMiningXP(cursor);
			humanBehavior.randomlyRotateCamera(cameraCalibrator);
			RandomDetector.dealWithRandoms(screenCapture, cursor);
			dropInventoryIfCloseToFull();
			
			/*if (noIronOresCount > 10000) {
				return;
			}*/
						
			DetectedObject closestIronOre = getClosestObjectToCharacter(ironOres);
			
			if (closestIronOre != null) {
				noIronOresCount = 0;
				cursor.moveAndLeftClickAtCoordinatesWithRandomness(closestIronOre.getCenterForClicking(), 10, 10);
				
				int ironOreInInventory = inventory.getFirstIronOreInInventoryDifferentFromLast(lastIronOreInInventory);
				lastIronOreInInventory = ironOreInInventory;
				
				int numberOfOresInInventoryBefore = inventory.getNumberOfItemsOfTypeInInventory("ironOre");
				boolean miningSuccess = false;
				
				if (ironOreInInventory > -1) {
					int ironOreInInventoryColumn = ironOreInInventory % 7;
					int ironOreInInventoryRow = ironOreInInventory / 7;
				
					Point rightClickItemClickLocation = inventory.getClickCoordinatesForInventorySlot(ironOreInInventoryRow, ironOreInInventoryColumn);
					
					TrackerThread trackerThread = new TrackerThread(screenCapture, closestIronOre, objectDetector);
					trackerThread.start();
				
					DropperThread dropperThread = new DropperThread(rightClickItemClickLocation, cursor, cursorTask);
					dropperThread.start();
				
					trackerThread.waitTillDone();
					dropperThread.waitTillDone();
					
					cursor.leftClickCursor();
					
					if (inventory.getNumberOfItemsOfTypeInInventory("ironOre") >= numberOfOresInInventoryBefore) {
						miningSuccess = true;
					}
				}
				else {
					TrackerThread trackerThread = new TrackerThread(screenCapture, closestIronOre, objectDetector);
					trackerThread.start();
					trackerThread.waitTillDone();
					
					if (inventory.getNumberOfItemsOfTypeInInventory("ironOre") > numberOfOresInInventoryBefore) {
						miningSuccess = true;
					}
				}
				count++;
				
				//System.out.println("Ores in inventory: " + numberOfOresInInventoryBefore + ". Mining success? " + miningSuccess);
				printMiningStats(count, startTime);
				
				boolean worldHopped = hopWorldsIfMiningSuccessRateIsLow(miningSuccess);
				if (worldHopped) {
					worldHops++;
				}
				System.out.println("worldHops: " + worldHops);
			}
			else {
				noIronOresCount++;
			}
		}
	}

	private boolean hopWorldsIfMiningSuccessRateIsLow(boolean miningSuccess) throws Exception {
		boolean hopWorld = miningSuccessHistory.updateHistory(miningSuccess);
		if (hopWorld) {
			System.out.println("Hopping worlds");
			WorldHopper.hopWorld(cursor);
			miningSuccessHistory.resetQueue();
			return true;
		}
		return false;
	}
	
	private void readjustCameraIfObjectsAreNotBeingDetected(int detectedObjectsSize) throws Exception {
		boolean readjustCamera = objectDetectionHistory.updateHistory(detectedObjectsSize);
		if (readjustCamera) {
			cameraCalibrator.rotateUntilObjectFound(objectDetector, "ironOre");
			objectDetectionHistory.resetQueue();
		}
	}

	/*private void dropInventoryIfFull() throws Exception {
		inventory.updateLastSlot();
		if (inventory.isLastSlotInInventoryFull()) {
			cursorTask.optimizedDropAllItemsInInventory(cursor, inventory);
			Thread.sleep(Randomizer.nextGaussianWithinRange(1104, 1651));
		}
	}*/
	
	private void dropInventoryIfCloseToFull() throws Exception {
		if (inventory.getNumberOfItemsOfTypeInInventory("ironOre") > 12) {
			inventory.dropAllItemsOfType("ironOre", cursorTask, cursor);
			Thread.sleep(Randomizer.nextGaussianWithinRange(1104, 1651));
		}
	}

	private void printMiningStats(int numberOfIronOresMined, long startTime) {
		System.out.println(numberOfIronOresMined + " ores mined in " + ((System.currentTimeMillis() - startTime) / 1000 / 60) + " minutes");
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
	
	/*public isDetectedObjectInRange(int maxDistance) {
		if (getDistanceBetweenPoints()
	}*/
	
	public int getDistanceBetweenPoints(Point startingPoint, Point goalPoint) {
		return (int) (Math.hypot(goalPoint.x - startingPoint.x, goalPoint.y - startingPoint.y));
	}
}
