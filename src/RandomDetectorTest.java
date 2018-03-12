import static org.junit.jupiter.api.Assertions.*;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;

class RandomDetectorTest {

	RandomDetector randomDetector;
	
	void initialize() throws AWTException {
		randomDetector = new RandomDetector();
	}
	
	@Test
	void testChatDialogueFound() throws IOException, AWTException, InterruptedException {
		initialize();
		for (File file : getListOfFilesFromItemDirectory("/home/dpapp/Desktop/RunescapeAI/Images/")) {
			if (file.isFile()) {
				BufferedImage screenCapture = ImageIO.read(file);
				Point chatDialogueCornerPoint = randomDetector.findChatDialogueCornerPoint(screenCapture);
				Point speakerPoint = randomDetector.findSpeakerPointFromCornerPoint(screenCapture, chatDialogueCornerPoint);
				
				assertNotNull(speakerPoint);
				System.out.println(file.getName());
				if (speakerPoint != null) {
					System.out.println("----- Random at " + speakerPoint.x + "," + speakerPoint.y + " -----");
				}
			}
		}
	}


	public File[] getListOfFilesFromItemDirectory(String directoryPath) {
		File itemDirectory = new File(directoryPath);
		return itemDirectory.listFiles();
	}

}
