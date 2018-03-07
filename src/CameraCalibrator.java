import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class CameraCalibrator {
	
	Robot robot;
	
	public CameraCalibrator() throws AWTException {
		robot = new Robot();
	}
	
	public void rotateUntilObjectFound(ObjectDetector objectDetector, String objectNameToLookFor) throws Exception {
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
		int keyPressLength = Randomizer.nextGaussianWithinRange(150, 505);
		robot.keyPress(KeyEvent.VK_LEFT);
		Thread.sleep(keyPressLength);
		robot.keyRelease(KeyEvent.VK_LEFT);
		Thread.sleep(Randomizer.nextGaussianWithinRange(120, 250));
	}
}
