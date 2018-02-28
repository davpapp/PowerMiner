import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class InventoryItemsTest {

	InventoryItems items;
	String testingItemDirectoryPath;
	
	public void initialize() throws IOException {
		items = new InventoryItems(Paths.INVENTORY_ITEMS_DIRECTORY_PATH);
	}
	
	@Test
	public void testGetNameOfItemFromImage() throws IOException {
		initialize();
		for (File itemFile : items.getListOfFilesFromItemDirectory(Paths.INVENTORY_ITEMS_TEST_DIRECTORY_PATH)) {
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
		for (File itemFile : items.getListOfFilesFromItemDirectory(Paths.INVENTORY_ITEMS_TEST_DIRECTORY_PATH)) {
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
