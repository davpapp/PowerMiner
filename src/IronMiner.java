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

public class IronMiner {
	
	public static final int IRON_ORE_MINING_TIME_MILLISECONDS = 2738;
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
			objectDetector.update();
			ArrayList<DetectedObject> ironOres = objectDetector.getRecognizedObjectsOfClassFromImage("ironOre");
			ArrayList<DetectedObject> ores = objectDetector.getRecognizedObjectsOfClassFromImage("ore");
			System.out.println(ironOres.size() + " ironOres, " + ores.size() + " ores.");
			
			/*for (DetectedObject ironOre : ironOres) {
				ironOre.display();
			}*/
			mineClosestIronOre(ironOres, ores);
			dropInventoryIfFull();
		}
	}
	
	private void dropInventoryIfFull() throws Exception {
		inventory.update(); // TODO: add iron ore to inventory items
		if (inventory.isInventoryFull()) {
			cursorTask.optimizedDropAllItemsInInventory(cursor, inventory);
		}
	}
	
	
	private void mineClosestIronOre(ArrayList<DetectedObject> ironOres, ArrayList<DetectedObject> ores) throws Exception {
		DetectedObject closestIronOre = getClosestObjectToCharacter(ironOres);
		if (closestIronOre != null) {
			cursor.moveAndLeftClickAtCoordinatesWithRandomness(closestIronOre.getCenterForClicking(), 10, 10);
			Thread.sleep(84, 219);
			DetectedObject closestOre = getClosestObjectToCharacter(ores);
			if (closestOre != null) {
				cursor.moveCursorToCoordinatesWithRandomness(closestOre.getCenterForClicking(), 10, 10);
			}
			Thread.sleep(randomizer.nextGaussianWithinRange(IRON_ORE_MINING_TIME_MILLISECONDS - 250, IRON_ORE_MINING_TIME_MILLISECONDS + -50));
		}
			//Thread.sleep(randomizer.nextGaussianWithinRange(150, 350));

		//cursor.moveCursorToCoordinates(goalPoint);
	}
	/*private void mineClosestIronOre(String filename) throws Exception {
		Point ironOreLocation = getClosestIronOre(filename);
		if (ironOreLocation != null) {
			System.out.println("Mineable iron at (" + (ironOreLocation.x + 103) + "," + (ironOreLocation.y + 85) + ")");
			Point actualIronOreLocation = new Point(ironOreLocation.x + 103, ironOreLocation.y + 85);
			Rect2d trackerBoundingBox = new Rec2d();
			//Rectangle trackerBoundingBox = new Rectangle(ironOreLocation.x - 10, ironOreLocation.x + 10, ironOreLocation.y - 10, ironOreLocation.y + 10);
			//tracker.init(image, trackerBoundingBox);
			cursor.moveAndLeftClickAtCoordinatesWithRandomness(actualIronOreLocation, 12, 12);
			Thread.sleep(randomizer.nextGaussianWithinRange(IRON_ORE_MINING_TIME_MILLISECONDS - 350, IRON_ORE_MINING_TIME_MILLISECONDS + 150));
		}
		
	}
	
	}*/
	
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
