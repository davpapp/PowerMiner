import java.awt.AWTException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Inventory {

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
		inventoryRectangleToCapture = new Rectangle(Constants.INVENTORY_WINDOW_OFFSET_X, Constants.INVENTORY_WINDOW_OFFSET_Y, Constants.INVENTORY_WINDOW_WIDTH, Constants.INVENTORY_WINDOW_HEIGHT);
	}
	
	private void initializeInventorySlots() {
		inventorySlots = new InventorySlot[4][7];
		for (int row = 0; row < Constants.INVENTORY_NUM_ROWS; row++) {
			for (int column = 0; column < Constants.INVENTORY_NUM_COLUMNS; column++) {
				inventorySlots[row][column] = new InventorySlot(row, column);
			}
		}
	}
	
	private void initializeItems() throws IOException {
		items = new InventoryItems("/home/dpapp/Desktop/RunescapeAI/Items/");
	}
	
	public void update() throws IOException {
		BufferedImage image = robot.createScreenCapture(this.inventoryRectangleToCapture);
		updateAllInventorySlots(image);
	}
	
	private void updateAllInventorySlots(BufferedImage image) throws IOException {
		for (int row = 0; row < Constants.INVENTORY_NUM_ROWS; row++) {
			for (int column = 0; column < Constants.INVENTORY_NUM_COLUMNS; column++) {
				inventorySlots[row][column].updateInventorySlot(image);
			}
		}
	}
	
	public void updateAndWriteAllInventorySlotsToImages() throws IOException {
		BufferedImage image = robot.createScreenCapture(this.inventoryRectangleToCapture);
		writeAllInventorySlotsToImages(image);
	}
	
	private void writeAllInventorySlotsToImages(BufferedImage image) throws IOException {
		for (int row = 0; row < Constants.INVENTORY_NUM_ROWS; row++) {
			for (int column = 0; column < Constants.INVENTORY_NUM_COLUMNS; column++) {
				inventorySlots[row][column].writeInventorySlotImage(image, row, column);
			}
		}
	}
	
	public String getItemNameInInventorySlot(int row, int column) {
		return inventorySlots[row][column].getItemNameInInventorySlot(items);
	}
	
	public boolean isInventoryFull() {
		// TODO: this will fail if some unexpected item shows up
		for (int row = 0; row < Constants.INVENTORY_NUM_ROWS; row++) {
			for (int column = 0; column < Constants.INVENTORY_NUM_COLUMNS; column++) {
				if (inventorySlots[row][column].isInventorySlotEmpty(items)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public Point getClickCoordinatesForInventorySlot(int row, int column) {
		Point centerOfInventorySlot = inventorySlots[row][column].getClickablePointWithinItemSlot();
		int x = Constants.INVENTORY_WINDOW_OFFSET_X + row * Constants.INVENTORY_SLOT_WIDTH + centerOfInventorySlot.x;
		int y = Constants.INVENTORY_WINDOW_OFFSET_Y + column * Constants.INVENTORY_SLOT_HEIGHT + centerOfInventorySlot.y;
		return new Point(x, y);
	}
	
	// For testing only
	public void updateWithFakeImageForTests(BufferedImage testImage) throws IOException {
		updateAllInventorySlots(testImage);
	}
}
