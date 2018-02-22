import java.awt.AWTException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class IronMiner {
	
	public static final int IRON_ORE_MINING_TIME_MILLISECONDS = 2738;
	public static final int MAXIMUM_DISTANCE_TO_WALK_TO_IRON_ORE = 400;
	public static final Point GAME_WINDOW_CENTER = new Point(510 / 2, 330 / 2);
	
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
			//Thread.sleep(250);
			
			String filename = "/home/dpapp/Desktop/RunescapeAI/temp/screenshot.jpg";
			BufferedImage image = captureScreenshotGameWindow();
			ImageIO.write(image, "jpg", new File(filename));
			mineClosestIronOre(filename);
			
			dropInventoryIfFull();
		}
	}
	
	private void dropInventoryIfFull() throws Exception {
		inventory.update(); // TODO: add iron ore to inventory items
		if (inventory.isInventoryFull()) {
			cursorTask.optimizedDropAllItemsInInventory(cursor, inventory);
		}
	}
	
	private void mineClosestIronOre(String filename) throws Exception {
		Point ironOreLocation = getClosestIronOre(filename);
		/*if (ironOreLocation == null) {
			Thread.sleep(1000);
		}*/
		if (ironOreLocation != null) {
			System.out.println("Mineable iron at (" + (ironOreLocation.x + 103) + "," + (ironOreLocation.y + 85) + ")");
			Point actualIronOreLocation = new Point(ironOreLocation.x + 103, ironOreLocation.y + 85);
			cursor.moveAndLeftClickAtCoordinatesWithRandomness(actualIronOreLocation, 12, 12);
			Thread.sleep(randomizer.nextGaussianWithinRange(IRON_ORE_MINING_TIME_MILLISECONDS - 350, IRON_ORE_MINING_TIME_MILLISECONDS + 150));
		}
		
	}
	
	private Point getClosestIronOre(String filename) throws IOException {
		ArrayList<Point> ironOreLocations = objectDetector.getIronOreLocationsFromImage(filename);
		System.out.println(ironOreLocations.size());
		int closestDistanceToIronOreFromCharacter = Integer.MAX_VALUE;
		Point closestIronOreToCharacter = null;
		for (Point ironOreLocation : ironOreLocations) {
			int distanceToIronOreFromCharacter = getDistanceBetweenPoints(GAME_WINDOW_CENTER, ironOreLocation);
			if (distanceToIronOreFromCharacter < closestDistanceToIronOreFromCharacter) {
				closestDistanceToIronOreFromCharacter = distanceToIronOreFromCharacter;
				closestIronOreToCharacter = new Point(ironOreLocation.x, ironOreLocation.y);
			}
		}
		
		if (closestIronOreToCharacter != null && closestDistanceToIronOreFromCharacter < MAXIMUM_DISTANCE_TO_WALK_TO_IRON_ORE) {
			return closestIronOreToCharacter;
		}
		return null;
	}
	
	
	public int getDistanceBetweenPoints(Point startingPoint, Point goalPoint) {
		return (int) (Math.hypot(goalPoint.x - startingPoint.x, goalPoint.y - startingPoint.y));
	}
	
	private BufferedImage captureScreenshotGameWindow() throws IOException {
		Rectangle area = new Rectangle(103, 85, 510, 330);
		return robot.createScreenCapture(area);
		
	}
}
