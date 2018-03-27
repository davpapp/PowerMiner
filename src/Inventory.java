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
		items = new InventoryItems(Paths.INVENTORY_ITEMS_DIRECTORY_PATH);
	}
	
	/*public void update() throws IOException {
		BufferedImage image = robot.createScreenCapture(this.inventoryRectangleToCapture);
		updateAllInventorySlots(image);
	}*/
	
	public int getNumberOfItemsOfTypeInInventory(String itemType) throws IOException {
		int numberOfItemsOfType = 0;
		BufferedImage image = robot.createScreenCapture(this.inventoryRectangleToCapture);
		for (int row = 0; row < Constants.INVENTORY_NUM_ROWS; row++) {
			for (int column = 0; column < Constants.INVENTORY_NUM_COLUMNS; column++) {
				
				inventorySlots[row][column].updateInventorySlot(image);
				if (inventorySlots[row][column].getItemNameInInventorySlot(items).equals(itemType)) {
					numberOfItemsOfType++;
				}
			}
		}
		return numberOfItemsOfType++;
	}
	
	public void dropAllItemsOfType(String itemType, CursorTask cursorTask, Cursor cursor) throws Exception {
		BufferedImage image = robot.createScreenCapture(this.inventoryRectangleToCapture);
		for (int row = 0; row < Constants.INVENTORY_NUM_ROWS; row++) {
			for (int column = 0; column < Constants.INVENTORY_NUM_COLUMNS; column++) {
				
				inventorySlots[row][column].updateInventorySlot(image);
				if (inventorySlots[row][column].getItemNameInInventorySlot(items).equals(itemType)) {
					cursorTask.dropItem(cursor, this, row, column);
				}
			}
		}
	}
	
	public int getFirstIronOreInInventory() throws IOException {
		BufferedImage image = robot.createScreenCapture(this.inventoryRectangleToCapture);
		for (int row = 0; row < Constants.INVENTORY_NUM_ROWS; row++) {
			for (int column = 0; column < Constants.INVENTORY_NUM_COLUMNS; column++) {
				inventorySlots[row][column].updateInventorySlot(image);
				if (!inventorySlots[row][column].isInventorySlotEmpty(items)) {
					return (row * 7 + column);
				}
			}
		}
		return -1;
	}
	
	public int getFirstIronOreInInventoryDifferentFromLast(int lastIronOreInInventory) throws IOException {
		int ironOreInInventoryColumn = lastIronOreInInventory % 7;
		int ironOreInInventoryRow = lastIronOreInInventory / 7;
		if (lastIronOreInInventory < 0) {
			ironOreInInventoryRow = -1;
			ironOreInInventoryColumn = -1;
		}
		BufferedImage image = robot.createScreenCapture(this.inventoryRectangleToCapture);
		for (int row = 0; row < Constants.INVENTORY_NUM_ROWS; row++) {
			for (int column = 0; column < Constants.INVENTORY_NUM_COLUMNS; column++) {
				if (row == ironOreInInventoryRow && column == ironOreInInventoryColumn) {
					continue;
				}
				inventorySlots[row][column].updateInventorySlot(image);
				if (!inventorySlots[row][column].isInventorySlotEmpty(items)) {
					return (row * 7 + column);
				}
			}
		}
		return -1;
	}
	
	public void updateLastSlot() throws IOException {
		BufferedImage image = robot.createScreenCapture(this.inventoryRectangleToCapture);
		updateLastInventorySlot(image);
	}
	
	private void updateAllInventorySlots(BufferedImage image) throws IOException {
		for (int row = 0; row < Constants.INVENTORY_NUM_ROWS; row++) {
			for (int column = 0; column < Constants.INVENTORY_NUM_COLUMNS; column++) {
				inventorySlots[row][column].updateInventorySlot(image);
			}
		}
	}
	
	private void updateLastInventorySlot(BufferedImage image) throws IOException {
		inventorySlots[Constants.INVENTORY_NUM_ROWS - 1][Constants.INVENTORY_NUM_COLUMNS - 1].updateInventorySlot(image);
	}
	
	public void updateAndWriteAllInventoryImages() throws IOException {
		BufferedImage image = robot.createScreenCapture(this.inventoryRectangleToCapture);
		ImageIO.write(image, "png", new File("/home/dpapp/Desktop/RunescapeAI/Tests/Inventory/inventory_TO_RENAME.png"));
		writeAllInventorySlotImages(image);
	}
	
	private void writeAllInventorySlotImages(BufferedImage image) throws IOException {
		for (int row = 0; row < Constants.INVENTORY_NUM_ROWS; row++) {
			for (int column = 0; column < Constants.INVENTORY_NUM_COLUMNS; column++) {
				inventorySlots[row][column].writeInventorySlotImage(image, row, column);
			}
		}
	}
	
	public String getItemNameInInventorySlot(int row, int column) {
		return inventorySlots[row][column].getItemNameInInventorySlot(items);
	}
	
	public boolean inventorySlotIsEmpty(int row, int column) {
		return inventorySlots[row][column].isInventorySlotEmpty(items);
	}
	
	public boolean isLastSlotInInventoryFull() {
		return !inventorySlots[Constants.INVENTORY_NUM_ROWS - 1][Constants.INVENTORY_NUM_COLUMNS - 1].isInventorySlotEmpty(items);
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
