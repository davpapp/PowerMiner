import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class InventorySlot {

	private static final int INVENTORY_SLOT_WIDTH = 171 / 4;
	private static final int INVENTORY_SLOT_HEIGHT = 254 / 7;
	
	String screenshotOutputDirectory;
	Item itemInInventorySlot;
	Rectangle rectangleToCapture;
	
	public InventorySlot(int row, int column) {
		initializeRectangleToCapture(row, column);
	}
	
	private void initializeRectangleToCapture(int row, int column) {
		rectangleToCapture = new Rectangle(row * INVENTORY_SLOT_WIDTH, column * INVENTORY_SLOT_HEIGHT, INVENTORY_SLOT_WIDTH, INVENTORY_SLOT_HEIGHT);
	}
	
	public void updateInventorySlot(BufferedImage image) throws IOException {
		BufferedImage croppedInventorySlotArea = image.getSubimage(rectangleToCapture.x, rectangleToCapture.y, rectangleToCapture.width, rectangleToCapture.height);
		setItemInInventorySlotFromImage(croppedInventorySlotArea);
	}
	
	private void setItemInInventorySlotFromImage(BufferedImage croppedInventorySlotArea) throws IOException {
		if (itemIsLog(croppedInventorySlotArea)) System.out.println("LOG!");
	}
	
	public boolean itemIsLog(BufferedImage croppedInventorySlotArea) throws IOException {
		int matchingPixel = 0;
		//int nonMatchingPixel = 0;
		File image = new File("/home/dpapp/Desktop/RunescapeAIPics/Items/willowLogs.png");
		BufferedImage logImage = ImageIO.read(image);
		/*System.out.println(logImage.getWidth() + ", " + logImage.getHeight());
		System.out.println(INVENTORY_SLOT_WIDTH + ", " + INVENTORY_SLOT_HEIGHT);
		System.out.println("Dimension match?");*/
		for (int row = 0; row < INVENTORY_SLOT_WIDTH; row++) {
			for (int col = 0; col < INVENTORY_SLOT_HEIGHT; col++) {
				if (pixelsWithinRGBTolerance(croppedInventorySlotArea.getRGB(row, col), logImage.getRGB(row, col))) {
					matchingPixel++;
				}
				/*else {
					nonMatchingPixel++;
				}*/
			}
		}
		
		if (matchingPixel > 300) {
			//System.out.println("Found log with " + matchingPixel + " matches!" + nonMatchingPixel);
			return true;
		}
		//System.out.println("No match!" + matchingPixel + ", nonmatching: " + nonMatchingPixel);
		return false;
	}
	
	private boolean pixelsWithinRGBTolerance(int rgb1, int rgb2) {
		int[] colors1 = getRGBValuesFromPixel(rgb1);
		int[] colors2 = getRGBValuesFromPixel(rgb2);
		for (int i = 0; i < 3; i++) {
			if (Math.abs(colors1[i] - colors2[i]) > 5) {
				return false;
			}
		}
		/*displayColor(colors1);
		System.out.println("vs");
		displayColor(colors2);
		System.out.println();*/
		return true;
	}
	
	private void displayColor(int[] colors) {
		System.out.println(colors[0] + "," + colors[1] + "," + colors[2]);
	}
	
	private int[] getRGBValuesFromPixel(int pixel) {
		int[] colors = {(pixel)&0xFF, (pixel>>8)&0xFF, (pixel>>16)&0xFF, (pixel>>24)&0xFF};
		return colors;
	}	
}
