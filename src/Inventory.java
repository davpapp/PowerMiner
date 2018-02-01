import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Inventory {

	/*x0 = 655;
	x1 = 697;
	x2 = 738;
	x3 = 781;*/
	public static final int INVENTORY_OFFSET_WIDTH = 655; //top left corner of inventory, fromm top left corner of screen
	public static final int INVENTORY_OFFSET_HEIGHT = 290; 
	public static final int INVENTORY_WIDTH = 820 - 649;// 820
	public static final int INVENTORY_HEIGHT = 350; // 530
	
	Robot robot;
	Rectangle inventoryAreaToCapture;
	InventorySlot[][] inventorySlots;
	
	public Inventory() throws AWTException {
		initializeInventorySlots();
		robot = new Robot();
		this.inventoryAreaToCapture = new Rectangle(INVENTORY_OFFSET_WIDTH, INVENTORY_OFFSET_HEIGHT, INVENTORY_WIDTH, INVENTORY_HEIGHT);
	}
	
	private void initializeInventorySlots() {
		inventorySlots = new InventorySlot[4][7];
		for (int row = 0; row < 4; row++) {
			for (int column = 0; column < 7; column++) {
				// might need to manually create these
				inventorySlots[row][column] = new InventorySlot(row, column);
			}
		}
	}
	
	public void update() throws IOException {
		BufferedImage image = robot.createScreenCapture(this.inventoryAreaToCapture);
		updateAllInventorySlots(image);
	}

	private void updateAllInventorySlots(BufferedImage image) throws IOException {
		for (int row = 0; row < 4; row++) {
			for (int column = 0; column < 7; column++) {
				inventorySlots[row][column].writeInventorySlotToImage(image, row, column);
			}
		}
	}

}
