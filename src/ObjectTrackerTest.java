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
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		String videoDirectory = "/home/dpapp/Videos/";
		//createPresentationGraphics(videoDirectory, "gameplay-2018-03-19_05.50.27.mp4");

		createPresentationGraphics(videoDirectory, "mining_guild_inv.mp4");
		/*for (File video : getListOfFilesFromDirectory(videoDirectory)) {
			if (video.isFile()) {
				testObjectTrackingInVideo(videoDirectory, video.getName());
			}
		}*/
	}
	
	public File[] getListOfFilesFromDirectory(String videoDirectoryPath) {
		File videoDirectory = new File(videoDirectoryPath);
		return videoDirectory.listFiles();
	}
	
	void createPresentationGraphics(String videoDirectory, String videoFileName) throws Exception {
		VideoCapture video = new VideoCapture(videoDirectory + videoFileName);
		ObjectDetector objectDetector = new ObjectDetector();
		
		Mat frame = new Mat();
		boolean frameReadSuccess = video.read(frame);
		int counter = 0;
		while (video.read(frame)) {
			counter++;
			if (counter % 2 == 0) {
				//counter++;
				continue;
			}
			BufferedImage screencapture = Mat2BufferedImage(frame);
			Graphics g = screencapture.getGraphics();

			ArrayList<DetectedObject> detectedObjects = objectDetector.getObjectsInImage(Mat2BufferedImage(frame), 0.30);
			for (DetectedObject detectedObject : detectedObjects) {
				Rect2d boundingBox = detectedObject.getBoundingRect2d();
				if (objectDetector.isObjectPresentInBoundingBoxInImage(detectedObjects, boundingBox, "ironOre")) {
					g.setColor(new Color(0, 255, 0));
				}
				else if (objectDetector.isObjectPresentInBoundingBoxInImage(detectedObjects, boundingBox, "ore")) {
					g.setColor(new Color(255, 0, 0));
				}
				else {
					g.setColor(new Color(255, 255, 255));
				}
				g.drawRect((int) (boundingBox.x * 735 / 510), (int) boundingBox.y, (int) (boundingBox.width * 685 / 510), (int) boundingBox.height);
			}	
			System.out.println(counter);
			ImageIO.write(screencapture, "jpg", new File(videoDirectory + "/mining_guild_inv/frame_" + videoFileName  + counter + ".jpg"));
			//counter++;
		}
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
			
			Graphics g = screencapture.getGraphics();		
			for (int i = 0; i < objectTrackers.size(); i++) {
				objectTrackers.get(i).update(frame, boundingBoxes.get(i));
				boolean trackingSuccess = objectTrackers.get(i).update(frame, boundingBoxes.get(i));
				//assertTrue(trackingSuccess);
				
				if (objectDetector.isObjectPresentInBoundingBoxInImage(detectedObjects, boundingBoxes.get(i), "ironOre")) {
					g.setColor(new Color(0, 255, 0));
				}
				else if (objectDetector.isObjectPresentInBoundingBoxInImage(detectedObjects, boundingBoxes.get(i), "ironOre")) {
					g.setColor(new Color(255, 0, 0));
				}
				else {
					g.setColor(new Color(255, 255, 255));
				}
				g.drawRect((int) boundingBoxes.get(i).x, (int) boundingBoxes.get(i).y, (int) boundingBoxes.get(i).width, (int) boundingBoxes.get(i).height);			
			}
			ImageIO.write(screencapture, "png", new File("/home/dpapp/Videos/BlurredRandom/NonBlurred/" + videoFileName  + counter + ".png"));
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
