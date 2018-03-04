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
		while (true) {
			long frameStartTime = System.currentTimeMillis();
			BufferedImage screenCapture = objectDetector.captureScreenshotGameWindow();
			System.out.println("looking for iron ores");
			//int count = objectDetector.getObjectsInImage(screenCapture, 0.6);
			ArrayList<DetectedObject> detectedObjects = objectDetector.getObjectsInImage(screenCapture, 0.60);
			//ArrayList<DetectedObject> ironOres = objectDetector.getObjectsOfClassInList(detectedObjects, "ironOre");
			System.out.println("Found " + detectedObjects.size() + " objects.");
			
			/*DetectedObject closestIronOre = getClosestObjectToCharacter(ironOres);
			if (closestIronOre != null) {
				Rect2d boundingBox = closestIronOre.getBoundingRect2d();
				ObjectTracker ironOreTracker = new ObjectTracker(screenCapture, boundingBox);
				
				cursor.moveAndLeftClickAtCoordinatesWithRandomness(closestIronOre.getCenterForClicking(), 10, 10);
				
				long miningStartTime = System.currentTimeMillis();
				int maxTimeToMine = randomizer.nextGaussianWithinRange(3500, 5000);
				
				boolean objectTrackingFailure = false;
				while (!objectTrackingFailure && !isTimeElapsedOverLimit(miningStartTime, maxTimeToMine)) {
					screenCapture = objectDetector.captureScreenshotGameWindow();
					objectTrackingFailure = ironOreTracker.update(screenCapture, boundingBox);
				}
			}*/
			
			// TODO: change this so that we only check the last slot for an item.
			// 
			//dropInventoryIfFull();
			System.out.println("Timespan: " + (System.currentTimeMillis() - frameStartTime));
		}
	}
	
	private void dropInventoryIfFull() throws Exception {
		inventory.update();
		if (inventory.isInventoryFull()) {
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
	
	/*private Mat getMatFromBufferedImage(BufferedImage image) {
		BufferedImage formattedImage = convertBufferedImage(image, BufferedImage.TYPE_3BYTE_BGR);
		byte[] data = ((DataBufferByte) formattedImage.getData().getDataBuffer()).getData();
		bgr2rgb(data); 
		Mat matImage = new Mat(formattedImage.getWidth(), formattedImage.getHeight(), CvType.CV_8UC3);
		byte[] pixels = ((DataBufferByte) formattedImage.getRaster().getDataBuffer()).getData();
		matImage.put(0, 0, pixels);
		return matImage;
	}
	
	private static BufferedImage convertBufferedImage(BufferedImage sourceImage, int bufferedImageType) {
	    BufferedImage image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), bufferedImageType);
	    Graphics2D g2d = image.createGraphics();
	    g2d.drawImage(sourceImage, 0, 0, null);
	    g2d.dispose();
	    return image;
	  }
	  
	  private static void bgr2rgb(byte[] data) {
		    for (int i = 0; i < data.length; i += 3) {
		      byte tmp = data[i];
		      data[i] = data[i + 2];
		      data[i + 2] = tmp;
		    }
		  }*/
	
	public int getDistanceBetweenPoints(Point startingPoint, Point goalPoint) {
		return (int) (Math.hypot(goalPoint.x - startingPoint.x, goalPoint.y - startingPoint.y));
	}
}
