import java.awt.image.BufferedImage;

import org.opencv.core.Mat;
import org.opencv.core.Rect2d;
import org.opencv.tracking.Tracker;
import org.opencv.tracking.TrackerBoosting;

public class ObjectTracker {
	
	Tracker objectTracker;
	private int numberOfFramesLostFor;
	
	public ObjectTracker(BufferedImage image, Rect2d boundingBox) {
		this.objectTracker = TrackerBoosting.create();
		this.objectTracker.init(bufferedImageToMat(image), boundingBox);
		this.numberOfFramesLostFor = 0;
	}
	
	public ObjectTracker(Mat image, Rect2d boundingBox) {
		this.objectTracker = TrackerBoosting.create();
		this.objectTracker.init(image, boundingBox);
		this.numberOfFramesLostFor = 0;
	}
	
	
	public boolean update(BufferedImage image, Rect2d boundingBox) {
		boolean trackingSuccessful = objectTracker.update(bufferedImageToMat(image), boundingBox);
		updateNumberOfFramesLostFor(trackingSuccessful);
		return isObjectTrackingFailure();
	}
	
	public boolean update(Mat image, Rect2d boundingBox) {
		boolean trackingSuccessful = objectTracker.update(image, boundingBox);
		updateNumberOfFramesLostFor(trackingSuccessful);
		return isObjectTrackingFailure();
	}
	
	private void updateNumberOfFramesLostFor(boolean trackingSuccessful) {
		if (trackingSuccessful) {
			numberOfFramesLostFor = 0;
		}
		else {
			numberOfFramesLostFor++;
		}
	}
	
	private boolean isObjectTrackingFailure() {
		return numberOfFramesLostFor > 3;
	}
}
