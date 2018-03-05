import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class CameraCalibrator {
	
	Robot robot;
	Randomizer randomizer;
	
	public CameraCalibrator() throws AWTException {
		robot = new Robot();
		randomizer = new Randomizer();
	}
	
	public void rotateUntilObjectFound(String objectNameToLookFor) throws Exception {
		ObjectDetector objectDetector = new ObjectDetector();
		BufferedImage screenCapture = objectDetector.captureScreenshotGameWindow();
		
		ArrayList<DetectedObject> detectedObjects = objectDetector.getObjectsInImage(screenCapture, 0.30);
		ArrayList<DetectedObject> detectedObjectsToLookFor = objectDetector.getObjectsOfClassInList(detectedObjects, objectNameToLookFor);
		while (detectedObjectsToLookFor.size() == 0) {
			randomlyRotateKeyboard();
			screenCapture = objectDetector.captureScreenshotGameWindow();
			detectedObjects = objectDetector.getObjectsInImage(screenCapture, 0.30);
			detectedObjectsToLookFor = objectDetector.getObjectsOfClassInList(detectedObjects, objectNameToLookFor);
		}
	}
	
	private void randomlyRotateKeyboard() throws InterruptedException {
		Randomizer randomizer = new Randomizer();
		int keyPressLength = randomizer.nextGaussianWithinRange(350, 1105);
		robot.keyPress(KeyEvent.VK_LEFT);
		Thread.sleep(keyPressLength);
		robot.keyRelease(KeyEvent.VK_LEFT);
		Thread.sleep(randomizer.nextGaussianWithinRange(120, 250));
	}
}
