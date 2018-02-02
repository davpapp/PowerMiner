import static org.junit.jupiter.api.Assertions.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.jupiter.api.Test;

class InventorySlotTest {

	
	InventorySlot inventorySlot;
	
	void initialize() {
		inventorySlot = new InventorySlot(0, 0);
	}
	
	@Test
	void test() throws IOException {
		initialize();
		File image = new File("/home/dpapp/Desktop/RunescapeAIPics/Tests/screenshot0_0.png");
		BufferedImage testImage = ImageIO.read(image);
		assertTrue(inventorySlot.itemIsLog(testImage));
	}

}
