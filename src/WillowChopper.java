import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class WillowChopper {
	
	Cursor cursor; 
	CursorTask cursorTask;
	Inventory inventory;
	ObjectDetector objectDetector;
	Robot robot;
	
	public WillowChopper() throws AWTException, IOException 
	{
		//cursor = new Cursor();
		//cursorTask = new CursorTask();
		inventory = new Inventory();
		objectDetector = new ObjectDetector();
		robot = new Robot();
	}
	
	public void run() throws Exception {
		System.out.println("Starting ironMiner...");
		while (true) {
			String filename = "/home/dpapp/Desktop/RunescapeAI/temp/screenshot.jpg";
			BufferedImage image = captureScreenshotGameWindow();
			ImageIO.write(image, "jpg", new File(filename));
			ArrayList<Point> ironOreLocations = objectDetector.getIronOreLocationsFromImage(filename);
			System.out.println("--------------------------------\n\n");
			/*
			if (character.isCharacterEngaged()) {
				// DO NOTHING
				// do things like checking the inventory
			}
			else {
				find closest willow tree
				chop willow tree
			}
			 */
			/*inventory.update();
			if (inventory.isInventoryFull()) {
				long startTime = System.currentTimeMillis();
				System.out.println("Inventory is full! Dropping...");
				cursorTask.optimizedDropAllItemsInInventory(cursor, inventory);
				System.out.println("Dropping took " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds.");
				//cursorTask.dropAllItemsInInventory(cursor, inventory);
			}*/
		}
	}
	
	private BufferedImage captureScreenshotGameWindow() throws IOException {
		Rectangle area = new Rectangle(103, 85, 510, 330);
		return robot.createScreenCapture(area);
		
	}
	

}
