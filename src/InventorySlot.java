import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class InventorySlot {

	private static final int INVENTORY_WIDTH = 171;
	private static final int INVENTORY_HEIGHT = 254;
	
	String screenshotOutputDirectory;
	Item itemInInventorySlot;
	Rectangle rectangleToCapture;
	
	public InventorySlot(int row, int column) {
		initializeRectangleToCapture(row, column);
		this.screenshotOutputDirectory = "/home/dpapp/Desktop/RunescapeAIPics/InventorySlots/";
	}
	
	private void initializeRectangleToCapture(int row, int column) {
		int slotWidth = INVENTORY_WIDTH / 4;
		int slotHeight = INVENTORY_HEIGHT / 7;
		//System.out.println("SlotWidth: " + slotWidth + ", slotHeight: " + slotHeight);
		rectangleToCapture = new Rectangle(row * slotWidth, column * slotHeight, slotWidth, slotHeight);
	}
	
	public void updateInventorySlot(BufferedImage image) {
		//BufferedImage croppedInventorySlotArea = image.getSubimage(rectangleToCapture.x, rectangleToCapture.y, rectangleToCapture.width, rectangleToCapture.height);
	}
	
	public void writeInventorySlotToImage(BufferedImage image, int row, int column) throws IOException {
		System.out.println("Pre-cropped image is of size:" + image.getWidth() + ", " + image.getHeight());
		System.out.println(row + ", " + column);
		System.out.println("Getting image from: " + rectangleToCapture.x + ", " + rectangleToCapture.y + ", " + rectangleToCapture.width + ", " + rectangleToCapture.height);
		BufferedImage croppedInventorySlotArea = image.getSubimage(rectangleToCapture.x, rectangleToCapture.y, rectangleToCapture.width, rectangleToCapture.height);
		ImageIO.write(croppedInventorySlotArea, "png", new File(getImageName(row, column)));
	}

	private String getImageName(int row, int column) {
		return this.screenshotOutputDirectory + "screenshot" + row + "_" + column + ".png";
	}
}
