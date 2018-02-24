import static org.junit.jupiter.api.Assertions.*;

import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;
import org.opencv.core.Rect2d;

class ObjectDetectorTest {

	@Test
	void testObjectDetection() throws Exception {
		ObjectDetector objectDetector = new ObjectDetector();
		BufferedImage testImage1 = ImageIO.read(new File("/home/dpapp/tensorflow-1.5.0/models/raccoon_dataset/test_images/ironOre_test_9.jpg"));
		ArrayList<DetectedObject> detectedObjects1 = objectDetector.getObjectsInImage(testImage1, 0.8);
		ArrayList<DetectedObject> detectedIronOres1 = objectDetector.getObjectsOfClassInList(detectedObjects1, "ironOre");
		ArrayList<DetectedObject> detectedOres1 = objectDetector.getObjectsOfClassInList(detectedObjects1, "ore");

		assertEquals(3, detectedIronOres1.size());
		assertEquals(2, detectedOres1.size());
		//assertDetectedObjectsAreEqual();
	}
	
	
	/*@Test
	void testObjectDetectionInSubImage() throws Exception {
		ObjectDetector objectDetector = new ObjectDetector();
		BufferedImage testImage = ImageIO.read(new File("/home/dpapp/tensorflow-1.5.0/models/raccoon_dataset/test_images/ironOre_test_9.jpg"));
	}*/
	
	void assertDetectedObjectsAreEqual(DetectedObject obj1, DetectedObject obj2) {
		
	}
	
	void assertBoundingBoxesAreEqual(Rect2d bb1, Rect2d bb2) {
		assertEquals(bb1.x, bb2.x, 3);
		assertEquals(bb1.y, bb2.y, 3);
		assertEquals(bb1.width, bb2.height, 3);
		assertEquals(bb1.width, bb2.height, 3);
	}
}
