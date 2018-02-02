import static org.junit.jupiter.api.Assertions.*;

import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

class InventoryTest {

	Inventory inventory;
	String testingInventoryDirectoryPath;
	
	public void initialize() throws AWTException, IOException {
		inventory = new Inventory();
		this.testingInventoryDirectoryPath = "/home/dpapp/Desktop/RunescapeAIPics/Tests/Inventory/";
	}
	
	@Test
	public void testGetNameInItemInventorySlot() throws IOException, AWTException {
		initialize();
		
		// TODO: add image for uploading custom image to Inventory
		BufferedImage testImage = loadBufferedImage("inventory_0.png");
		inventory.updateWithFakeImageForTests(testImage);
		assertEquals(inventory.getItemNameInInventorySlot(0, 0), "willowLogs");
		assertEquals(inventory.getItemNameInInventorySlot(3, 6), "empty");
	}
	
	public BufferedImage loadBufferedImage(String fileName) throws IOException {
		File itemFile = new File(this.testingInventoryDirectoryPath + fileName);
		BufferedImage itemImage = ImageIO.read(itemFile);
		return itemImage;
	}

}
