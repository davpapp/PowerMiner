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
import org.opencv.core.Point;
import org.opencv.core.Rect2d;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.tracking.Tracker;
import org.opencv.tracking.TrackerKCF;
import org.opencv.tracking.TrackerMIL;
import org.opencv.tracking.TrackerBoosting;
import org.opencv.tracking.TrackerGOTURN;
import org.opencv.tracking.TrackerMedianFlow;
import org.opencv.videoio.VideoCapture;

class ObjectTrackerTest {

	
	@Test
	void testObjectTrackingWithAllVideosInDirectory() throws Exception {
		String videoDirectory = "/home/dpapp/Videos/";
		for (File video : getListOfFilesFromDirectory(videoDirectory)) {
			if (video.isFile()) {
				testObjectTrackingInVideo(videoDirectory, video.getName());
			}
		}
	}
	
	public File[] getListOfFilesFromDirectory(String videoDirectoryPath) {
		File videoDirectory = new File(videoDirectoryPath);
		return videoDirectory.listFiles();
	}
	
	void testObjectTrackingInVideo(String videoDirectory, String videoFileName) throws Exception {
		VideoCapture video = new VideoCapture(videoDirectory + videoFileName);
		
		ObjectDetector objectDetector = new ObjectDetector();
		
		Mat frame = new Mat();
		boolean frameReadSuccess = video.read(frame);
		assertTrue(frameReadSuccess);
		
		ArrayList<DetectedObject> detectedObjects = objectDetector.getObjectsInImage(Mat2BufferedImage(frame), 0.80);
		System.out.println("Tracking " + detectedObjects.size() + " objects.");
		
		ArrayList<ObjectTracker> objectTrackers = new ArrayList<ObjectTracker>();
		ArrayList<Rect2d> boundingBoxes = new ArrayList<Rect2d>();
		
		for (int i = 0; i < detectedObjects.size(); i++) {
			boundingBoxes.add(detectedObjects.get(i).getBoundingRect2d());
			objectTrackers.add(new ObjectTracker(frame, detectedObjects.get(i).getBoundingRect2d()));
		}
		
		int counter = 0;
		while (video.read(frame)) {
			BufferedImage screencapture = Mat2BufferedImage(frame);
			
			//Graphics g = screencapture.getGraphics();		
			for (int i = 0; i < objectTrackers.size(); i++) {
				objectTrackers.get(i).update(frame, boundingBoxes.get(i));
				boolean trackingSuccess = objectTrackers.get(i).update(frame, boundingBoxes.get(i));
				assertTrue(trackingSuccess);
							
				//g.drawRect((int) boundingBoxes.get(i).x, (int) boundingBoxes.get(i).y, (int) boundingBoxes.get(i).width, (int) boundingBoxes.get(i).height);			
			}
			ImageIO.write(screencapture, "jpg", new File(videoDirectory + "/frame_" + videoFileName  + counter + ".jpg"));
			counter++;
		}
	}

	private BufferedImage Mat2BufferedImage(Mat matrix)throws Exception {        
	    MatOfByte mob=new MatOfByte();
	    Imgcodecs.imencode(".jpg", matrix, mob);
	    byte ba[]=mob.toArray();

	    BufferedImage bi=ImageIO.read(new ByteArrayInputStream(ba));
	    return bi;
	}

}
