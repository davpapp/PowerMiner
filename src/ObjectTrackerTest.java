import static org.junit.jupiter.api.Assertions.*;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
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
import org.opencv.tracking.TrackerMIL;
import org.opencv.tracking.TrackerBoosting;
import org.opencv.tracking.TrackerMedianFlow;
import org.opencv.videoio.VideoCapture;

class ObjectTrackerTest {

	@Test
	void testObjectTracking() throws Exception {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		VideoCapture video = new VideoCapture("/home/dpapp/Videos/gameplay-2018-02-24_14.24.53.mp4");
		System.out.println("loaded video...");
		
		ObjectDetector objectDetector = new ObjectDetector();
		
		Mat frame = new Mat();
		boolean frameReadSuccess = video.read(frame);
		assertTrue(frameReadSuccess);
		
		ArrayList<DetectedObject> detectedObjects = objectDetector.getObjectsInImage(Mat2BufferedImage(frame), 0.60);
		System.out.println("Tracking " + detectedObjects.size() + " objects.");
		
		ArrayList<Tracker> objectTrackers = new ArrayList<Tracker>();
		ArrayList<Rect2d> boundingBoxes = new ArrayList<Rect2d>();
		for (int i = 0; i < detectedObjects.size(); i++) {
			boundingBoxes.add(detectedObjects.get(i).getBoundingRect2d());
			objectTrackers.add(TrackerBoosting.create());
			objectTrackers.get(i).init(frame, boundingBoxes.get(i));
		}
		//System.out.println("bounding box: " + (int) boundingBoxes.get(0).x + ", " + (int) boundingBoxes.get(0).y + ", " + (int) boundingBoxes.get(0).width + ", " + (int) boundingBoxes.get(0).height);
		
		int counter = 0;
		while (video.read(frame)) {
			BufferedImage screencapture = Mat2BufferedImage(frame);
			detectedObjects = objectDetector.getObjectsInImage(screencapture, 0.3);
			
			for (int i = 0; i < objectTrackers.size(); i++) {
				objectTrackers.get(i).update(frame, boundingBoxes.get(i));
				boolean trackingSuccess = objectTrackers.get(i).update(frame, boundingBoxes.get(i));
				
				//System.out.println(detectedObjects.size());
				//System.out.println((int) boundingBoxes.get(i).x + ", " + (int) boundingBoxes.get(i).y + ", " + (int) boundingBoxes.get(i).width + ", " + (int) boundingBoxes.get(i).height);
				//BufferedImage subImage = screencapture.getSubimage((int) boundingBoxes.get(i).x- 10, (int) boundingBoxes.get(i).y - 10, (int) boundingBoxes.get(i).width + 20, (int) boundingBoxes.get(i).height + 20);
				
				boolean ironOreDetected = objectDetector.isObjectPresentInBoundingBoxInImage(detectedObjects, boundingBoxes.get(i), "ironOre");
				boolean oreDetected = objectDetector.isObjectPresentInBoundingBoxInImage(detectedObjects, boundingBoxes.get(i), "ore");
				
				Graphics g = screencapture.getGraphics();
				if (ironOreDetected) {
					g.setColor(Color.GREEN);
				}
				else if (oreDetected) {
					g.setColor(Color.RED);
				}
				else {
					g.setColor(Color.WHITE);
				}
				g.drawRect((int) boundingBoxes.get(i).x, (int) boundingBoxes.get(i).y, (int) boundingBoxes.get(i).width, (int) boundingBoxes.get(i).height);
				
				//ImageIO.write(subImage, "jpg", new File("/home/dpapp/Videos/sub_frames/frame_" + counter + "_sub.jpg"));
				//System.out.println("wrote file...");
				/*if (i == ) {
					System.out.println(trackingSuccess + ", ironOre: " + ironOreDetected + ", ore:" + oreDetected);
				}*/
				
			}
			System.out.println("Wrote image...");
			ImageIO.write(screencapture, "jpg", new File("/home/dpapp/Videos/frames/frame_" + counter + ".jpg"));
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
