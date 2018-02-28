import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect2d;
import org.opencv.tracking.Tracker;
import org.opencv.tracking.TrackerBoosting;
import org.opencv.videoio.VideoCapture;

public class ObjectTrackerSpeedTest {
	
	static Robot robot;
	// Screen capture: 6.5 ms (tested on 10,000 frames)
	// Object tracker: 15 ms (tested on 4000 frames)

	public static void main(String[] args) throws AWTException, IOException {
		// TODO Auto-generated method stub
		robot = new Robot();
		//System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		
		Tracker tracker = TrackerBoosting.create();
		Rect2d boundingBox = new Rect2d(405, 177, 38, 38);
		//Mat image = 
		//tracker.init(image, boundingBox);
		
		
		/*long startTime = System.currentTimeMillis();
		int limit = 10000;
		for (int i = 0; i < limit; i++) {
			BufferedImage screencapture = captureScreenshotGameWindow();
			if (i % 100 == 0) {
				System.out.println(i);
			}
		}*/
		
		int limit = 0;
		VideoCapture video = new VideoCapture("/home/dpapp/Videos/gameplay-2018-02-23_11.50.00.mp4");
		Mat frame = new Mat();
		boolean frameReadSuccess = video.read(frame);
		
		tracker.init(frame, boundingBox);
		
		long totalTime = 0;
		while (video.read(frame)) {
			long startTimeUpdate = System.currentTimeMillis();
			tracker.update(frame, boundingBox);
			totalTime += (System.currentTimeMillis() - startTimeUpdate);
			limit++;
			if (limit % 100 == 0) {
				System.out.println(limit);
			}
		}
		
		
		System.out.println(limit + " frames took " + (totalTime) / 1000 + " seconds.");
		System.out.println("FPS:" + limit * 1.0 / (totalTime / 1000.0));
		System.out.println(totalTime * 1.0 / limit);
	}

	
	public static BufferedImage captureScreenshotGameWindow() throws IOException, AWTException {
		Rectangle area = new Rectangle(Constants.GAME_WINDOW_OFFSET_X, Constants.GAME_WINDOW_OFFSET_Y, Constants.GAME_WINDOW_WIDTH, Constants.GAME_WINDOW_HEIGHT);
		return robot.createScreenCapture(area);
	  }
}
