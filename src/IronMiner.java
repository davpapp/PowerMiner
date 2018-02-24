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
				System.out.println("Found iron ore! Starting tracking!");
				Tracker objectTracker = TrackerKCF.create();
				Rect2d boundingBox = closestIronOre.getBoundingRect2d();
				objectTracker.init(getMatFromBufferedImage(screenCapture), boundingBox);
				
				cursor.moveAndLeftClickAtCoordinatesWithRandomness(closestIronOre.getCenterForClicking(), 10, 10);
				
				long mineStartTime = System.currentTimeMillis();
				int maxTimeToMine = randomizer.nextGaussianWithinRange(3500, 5000);
				
				// track until either we lose the object or too much time passes
				int lostTrackCounter = 0;
				while (((System.currentTimeMillis() - mineStartTime) < maxTimeToMine) && lostTrackCounter < 3) {
					
					screenCapture = objectDetector.captureScreenshotGameWindow();
					detectedObjects = objectDetector.getObjectsInImage(screenCapture);
					
					boolean ok = objectTracker.update(getMatFromBufferedImage(screenCapture), boundingBox);
					if (!ok || !objectDetector.isObjectPresentInBoundingBoxInImage(detectedObjects, boundingBox, "ironOre")) {
						System.out.println("Lost track for + " + lostTrackCounter + "! Finding new ore soon.");
						lostTrackCounter++;
					}
					else if (ok) {
						lostTrackCounter = 0;
						System.out.println("Tracking at " + boundingBox.x + ", " + boundingBox.y + ", " + boundingBox.width + ", " + boundingBox.height);
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
	
	private Mat getMatFromBufferedImage(BufferedImage image) {
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
		  }
	
	public int getDistanceBetweenPoints(Point startingPoint, Point goalPoint) {
		return (int) (Math.hypot(goalPoint.x - startingPoint.x, goalPoint.y - startingPoint.y));
	}
}
