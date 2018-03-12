import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Rect2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.tracking.Tracker;
import org.opencv.tracking.TrackerBoosting;

public class ObjectTracker {
	
	Tracker objectTracker;
	private int numberOfFramesLostFor;
	
	public ObjectTracker(BufferedImage image, Rect2d boundingBox) throws IOException {
		this.objectTracker = TrackerBoosting.create();
		this.objectTracker.init(BufferedImage2Mat(image), boundingBox);
		this.numberOfFramesLostFor = 0;
	}
	
	public ObjectTracker(Mat image, Rect2d boundingBox) {
		this.objectTracker = TrackerBoosting.create();
		this.objectTracker.init(image, boundingBox);
		this.numberOfFramesLostFor = 0;
	}
	
	
	public boolean update(BufferedImage image, Rect2d boundingBox) throws IOException {
		boolean trackingSuccessful = objectTracker.update(BufferedImage2Mat(image), boundingBox);
		//System.out.print("Tracking: " + trackingSuccessful + ". Counter at ");
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
		//System.out.println(numberOfFramesLostFor);
		return numberOfFramesLostFor > 3;
	}
	
	/*private Mat bufferedImageToMat(BufferedImage sourceImage) {
		Mat mat = new Mat(sourceImage.getHeight(), sourceImage.getWidth(), CvType.CV_8UC3);
		byte[] data = ((DataBufferByte) sourceImage.getRaster().getDataBuffer()).getData();
		mat.put(0, 0, data);
		return mat;
	}*/
	
	private Mat BufferedImage2Mat(BufferedImage image) throws IOException {
	    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	    ImageIO.write(image, "jpg", byteArrayOutputStream);
	    byteArrayOutputStream.flush();
	    return Imgcodecs.imdecode(new MatOfByte(byteArrayOutputStream.toByteArray()), Imgcodecs.CV_LOAD_IMAGE_UNCHANGED);
	}
}
