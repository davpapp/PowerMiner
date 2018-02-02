import java.awt.AWTException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Inventory {

	public static final int INVENTORY_OFFSET_WIDTH = 655; //top left corner of inventory, fromm top left corner of screen
	public static final int INVENTORY_OFFSET_HEIGHT = 290; 
	public static final int INVENTORY_WIDTH = 820 - 649;// 820
	public static final int INVENTORY_HEIGHT = 350; // 530
	
	private static final int INVENTORY_SLOT_WIDTH = 171 / 4;
	private static final int INVENTORY_SLOT_HEIGHT = 254 / 7;
	
	public static final int NUM_ROWS = 4;
	public static final int NUM_COLUMNS = 7;
	
	Robot robot;
	Rectangle inventoryRectangleToCapture;
	InventorySlot[][] inventorySlots;
	InventoryItems items;
	
	public Inventory() throws AWTException, IOException {
		initializeInventoryRectangle();
		initializeInventorySlots();
		initializeItems();
		robot = new Robot();
	}
	
	private void initializeInventoryRectangle() {
		inventoryRectangleToCapture = new Rectangle(INVENTORY_OFFSET_WIDTH, INVENTORY_OFFSET_HEIGHT, INVENTORY_WIDTH, INVENTORY_HEIGHT);
	}
	
	private void initializeInventorySlots() {
		inventorySlots = new InventorySlot[4][7];
		for (int row = 0; row < 4; row++) {
			for (int column = 0; column < 7; column++) {
				inventorySlots[row][column] = new InventorySlot(row, column);
			}
		}
	}
	
	private void initializeItems() throws IOException {
		items = new InventoryItems("/home/dpapp/Desktop/RunescapeAIPics/Items/");
	}
	
	public void update() throws IOException {
		BufferedImage image = robot.createScreenCapture(this.inventoryRectangleToCapture);
		
		// For test image generation only
		//ImageIO.write(image, "png", new File(getImageName()));
		
		updateAllInventorySlots(image);
	}

	private String getImageName() {
		return ("/home/dpapp/Desktop/RunescapeAIPics/Tests/Inventory/inventory.png");
	}
	
	// For testing only
	public void updateWithFakeImageForTests(BufferedImage testImage) throws IOException {
		updateAllInventorySlots(testImage);
	}
	
	private void updateAllInventorySlots(BufferedImage image) throws IOException {
		for (int row = 0; row < 4; row++) {
			for (int column = 0; column < 7; column++) {
				inventorySlots[row][column].updateInventorySlot(image);
				//inventorySlots[row][column].writeInventorySlotImage(image, row, column);
			}
		}
	}
	
	public String getItemNameInInventorySlot(int row, int column) {
		return inventorySlots[row][column].getItemNameInInventorySlot(items);
	}
	
	public boolean isInventoryFull() {
		// TODO: this will fail if some unexpected item shows up
		for (int row = 0; row < 4; row++) {
			for (int column = 0; column < 7; column++) {
				if (inventorySlots[row][column].isInventorySlotEmpty(items)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public Point getClickCoordinatesCoordinatesForInventorySlot(int row, int column) {
		Point centerOfInventorySlot = inventorySlots[row][column].getClickablePointWithinItemSlot();
		int x = INVENTORY_OFFSET_WIDTH + row * INVENTORY_SLOT_WIDTH + centerOfInventorySlot.x;
		int y = INVENTORY_OFFSET_HEIGHT + column * INVENTORY_SLOT_HEIGHT + centerOfInventorySlot.y;
		return new Point(x, y);
	}
}
