import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class InventoryItemsTest {

	InventoryItems items;
	String testingItemDirectoryPath;
	
	public void initialize() throws IOException {
		System.out.println("running initialize...");
		items = new InventoryItems("/home/dpapp/Desktop/RunescapeAIPics/Items/");
		this.testingItemDirectoryPath = "/home/dpapp/Desktop/RunescapeAIPics/Tests/ItemNameRecognition/";
	}
	
	@Test
	public void testGetNameOfItemFromImage() throws IOException {
		initialize();
		for (File itemFile : items.getListOfFilesFromItemDirectory(this.testingItemDirectoryPath)) {
			if (itemFile.isFile()) {
				BufferedImage itemImage = ImageIO.read(itemFile);
				String expectedItemName = getItemNameForTest(itemFile.getName());
				assertEquals(expectedItemName, items.getNameOfItemFromImage(itemImage));
			}
		}
	}
	
	@Test
	public void testIsImageThisItem() throws IOException {
		initialize();
		for (File itemFile : items.getListOfFilesFromItemDirectory(this.testingItemDirectoryPath)) {
			if (itemFile.isFile()) {
				BufferedImage itemImage = ImageIO.read(itemFile);
				String expectedItemName = getItemNameForTest(itemFile.getName());
				if (expectedItemName.equals("empty")) {
					continue; 
				}
				assertTrue(items.isImageThisItem(itemImage, expectedItemName));
			}
		}
	}
	
	private String getItemNameForTest(String fileName) {
		return fileName.substring(0, fileName.indexOf('_'));
	}
}
