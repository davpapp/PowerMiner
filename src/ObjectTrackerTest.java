import static org.junit.jupiter.api.Assertions.*;

import java.awt.AWTException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Rect2d;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.tracking.Tracker;
import org.opencv.tracking.TrackerKCF;
import org.opencv.videoio.VideoCapture;

class ObjectTrackerTest {

	@Test
	void testObjectTracking() throws Exception {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		VideoCapture video = new VideoCapture("/home/dpapp/Videos/gameplay-2018-02-24_10.01.00.mp4");
		System.out.println("loaded video...");
		
		ObjectDetector objectDetector = new ObjectDetector();
		
		Mat frame = new Mat();
		boolean frameReadSuccess = video.read(frame);
		assertTrue(frameReadSuccess);
		
		ArrayList<DetectedObject> detectedObjects = objectDetector.getObjectsInImage(Mat2BufferedImage(frame));
		System.out.println("Tracking " + detectedObjects.size() + " objects.");
		ArrayList<Tracker> objectTrackers = new ArrayList<Tracker>();
		ArrayList<Rect2d> boundingBoxes = new ArrayList<Rect2d>();
		for (int i = 0; i < 3; i++) {
			boundingBoxes.add(detectedObjects.get(i).getBoundingRect2d());
			objectTrackers.add(TrackerKCF.create());
			objectTrackers.get(i).init(frame, boundingBoxes.get(i));
		}
		//System.out.println("bounding box: " + (int) boundingBoxes.get(0).x + ", " + (int) boundingBoxes.get(0).y + ", " + (int) boundingBoxes.get(0).width + ", " + (int) boundingBoxes.get(0).height);
		
		int counter = 0;
		while (video.read(frame)) {
			for (int i = 0; i < 3; i++) {
				objectTrackers.get(i).update(frame, boundingBoxes.get(i));
				boolean trackingSuccess = objectTrackers.get(i).update(frame, boundingBoxes.get(i));
				
				detectedObjects = objectDetector.getObjectsInImage(Mat2BufferedImage(frame));
				//System.out.println(detectedObjects.size());
				//System.out.println((int) boundingBoxes.get(i).x + ", " + (int) boundingBoxes.get(i).y + ", " + (int) boundingBoxes.get(i).width + ", " + (int) boundingBoxes.get(i).height);
				//BufferedImage subImage = screencapture.getSubimage((int) boundingBoxes.get(i).x- 10, (int) boundingBoxes.get(i).y - 10, (int) boundingBoxes.get(i).width + 20, (int) boundingBoxes.get(i).height + 20);
				
				boolean ironOreDetected = objectDetector.isObjectPresentInBoundingBoxInImage(detectedObjects, boundingBoxes.get(i), "ironOre");
				boolean oreDetected = objectDetector.isObjectPresentInBoundingBoxInImage(detectedObjects, boundingBoxes.get(i), "ore");
				
				//ImageIO.write(screencapture, "jpg", new File("/home/dpapp/Videos/frames/frame_" + counter + ".jpg"));
				//ImageIO.write(subImage, "jpg", new File("/home/dpapp/Videos/sub_frames/frame_" + counter + "_sub.jpg"));
				//System.out.println("wrote file...");
				if (i == 2) {
					System.out.println(trackingSuccess + ", ironOre: " + ironOreDetected + ", ore:" + oreDetected);
				}
			}
			counter++;
		}
		
		/*Tracker objectTracker = TrackerKCF.create();
		Rect2d boundingBox = new Rect2d(405, 177, 38, 38);
		
		int counter = 0;
		objectTracker.init(frame, boundingBox);
		while (video.read(frame) && counter < 200) {
			boolean trackingSuccess = objectTracker.update(frame, boundingBox);
			BufferedImage screencapture = Mat2BufferedImage(frame);
			boolean ironOreDetected = objectDetector.isObjectPresentInBoundingBoxInImage(screencapture, boundingBox, "ironOre", counter);
			
			ImageIO.write(screencapture, "jpg", new File("/home/dpapp/Videos/frames/frame_" + counter + ".jpg"));
			
			System.out.println(trackingSuccess + ", ironOre: " + ironOreDetected);
			counter++;
		}*/
	}
	
	private BufferedImage Mat2BufferedImage(Mat matrix)throws Exception {        
	    MatOfByte mob=new MatOfByte();
	    Imgcodecs.imencode(".jpg", matrix, mob);
	    byte ba[]=mob.toArray();

	    BufferedImage bi=ImageIO.read(new ByteArrayInputStream(ba));
	    return bi;
	}

}
