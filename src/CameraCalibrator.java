import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class CameraCalibrator {
	
	int targetNumberOfDetectedObjects;
	Robot robot;
	
	public CameraCalibrator(int targetNumberOfDetectedObjects) throws AWTException {
		this.targetNumberOfDetectedObjects = targetNumberOfDetectedObjects;
		robot = new Robot();
	}
	
	public void rotateUntilObjectFound(ObjectDetector objectDetector, String objectNameToLookFor) throws Exception {
		BufferedImage screenCapture = ImageCapturer.captureScreenshotGameWindow();
		
		ArrayList<DetectedObject> detectedObjects = objectDetector.getObjectsInImage(screenCapture, 0.40);
		ArrayList<DetectedObject> detectedObjectsToLookFor = objectDetector.getObjectsOfClassInList(detectedObjects, objectNameToLookFor);
		while (detectedObjectsToLookFor.size() < targetNumberOfDetectedObjects) {
			randomlyRotateKeyboard();
			screenCapture = ImageCapturer.captureScreenshotGameWindow();
			detectedObjects = objectDetector.getObjectsInImage(screenCapture, 0.40);
			detectedObjectsToLookFor = objectDetector.getObjectsOfClassInList(detectedObjects, objectNameToLookFor);
		}
	}
	
	private void randomlyRotateKeyboard() throws InterruptedException {
		int keyPressLength = Randomizer.nextGaussianWithinRange(50, 160);
		robot.keyPress(KeyEvent.VK_LEFT);
		Thread.sleep(keyPressLength);
		robot.keyRelease(KeyEvent.VK_LEFT);
		Thread.sleep(Randomizer.nextGaussianWithinRange(80, 118));
	}
	
	public void randomlyShiftView() throws InterruptedException {
		int keyPressLength = Randomizer.nextGaussianWithinRange(50, 310);
		robot.keyPress(KeyEvent.VK_LEFT);
		Thread.sleep(keyPressLength);
		robot.keyRelease(KeyEvent.VK_LEFT);
		Thread.sleep(Randomizer.nextGaussianWithinRange(80, 118));
	}
}
