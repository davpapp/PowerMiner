import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import org.opencv.core.Rect2d;

public class TrackerThread implements Runnable {
	Thread trackerThread;
	BufferedImage screenCapture;
	DetectedObject closestIronOre;
	ObjectDetector objectDetector;
	
	public TrackerThread(BufferedImage screenCapture, DetectedObject closestIronOre, ObjectDetector objectDetector) {
		this.screenCapture = screenCapture;
		this.closestIronOre = closestIronOre;
		this.objectDetector = objectDetector;
	}
	
	@Override
	public void run() {
		Rect2d boundingBox = closestIronOre.getBoundingRect2d();
		ObjectTracker ironOreTracker;
		try {
			ironOreTracker = new ObjectTracker(screenCapture, boundingBox);

			long miningStartTime = System.currentTimeMillis();
			int maxTimeToMine = Randomizer.nextGaussianWithinRange(3400, 4519);
			
			boolean objectTrackingFailure = false;
			boolean oreAvailable = true;
			int oreLostCount = 0;
			while (!objectTrackingFailure && oreLostCount < 3 && !isTimeElapsedOverLimit(miningStartTime, maxTimeToMine)) {
				long frameStartTime = System.currentTimeMillis();
				screenCapture = ImageCapturer.captureScreenshotGameWindow();
				ArrayList<DetectedObject> detectedObjects = objectDetector.getObjectsInImage(screenCapture, 0.15);
				ArrayList<DetectedObject> ironOres = objectDetector.getObjectsOfClassInList(detectedObjects, "ironOre");
				objectTrackingFailure = ironOreTracker.update(screenCapture, boundingBox);
				oreAvailable = objectDetector.isObjectPresentInBoundingBoxInImage(ironOres, boundingBox, "ironOre");
				if (!oreAvailable) {
					oreLostCount++;
				}
				else {
					oreLostCount = 0;
				}
				//System.out.println("Threaded tracker took " + (System.currentTimeMillis() - frameStartTime) + " milliseconds.");
				//System.out.println("trackerThread working...");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println("~~~~~~~~~~~~~ trackerThread finished!");
	}
	
	private boolean isTimeElapsedOverLimit(long startTime, int timeLimit) {
		return (System.currentTimeMillis() - startTime) > timeLimit;
	}
	
	public void start() {
		//System.out.println("TrackerThread started");
		if (trackerThread == null) {
			trackerThread = new Thread(this, "trackerThread");
			trackerThread.start();
		}
	}

	public void waitTillDone() throws InterruptedException {
		trackerThread.join();
	}

}
