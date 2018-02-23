import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class cascadeTrainingImageCollector {

	public String imageOutputDirectory;
	public String imageInputDirectory;
	public Robot robot;
	public int imageDimension;

	public cascadeTrainingImageCollector(String imageOutputDirectory) throws AWTException {
		this.imageOutputDirectory = imageOutputDirectory;
		this.robot = new Robot();
		this.imageDimension = 40;
	}
	
	public void captureEveryNSeconds(int n) throws IOException, InterruptedException {
		for (int i = 0; i < 60; i++) {
			captureScreenshotAroundMouse(i);
			System.out.println(i);
            Thread.sleep(n * 1000);
		}
	}
	
	public void captureWindowEveryNMilliseconds(int n) throws InterruptedException, IOException {
		for (int i = 0; i < 1000; i++) {
			captureScreenshotGameWindow(i);
			System.out.println("Created image: " + getImageName(i));
            Thread.sleep(n);
		}
	}
	
	private void captureScreenshotGameWindow(int counter) throws IOException {
		Rectangle area = new Rectangle(103, 85, 510, 330);
		BufferedImage image = robot.createScreenCapture(area);
		ImageIO.write(image, "jpg", new File(getImageName(counter)));
	}
	
	private void captureScreenshotAroundMouse(int counter) throws IOException {
		BufferedImage image = robot.createScreenCapture(getRectangleAroundCursor());
		ImageIO.write(image, "jpg", new File(getImageName(counter)));
	}
	
	private Rectangle getRectangleAroundCursor() {
		Point cursorPoint = getCurrentCursorPoint();
		return new Rectangle(cursorPoint.x - imageDimension / 2, cursorPoint.y - imageDimension / 2, imageDimension, imageDimension);
	}
	
	private Point getCurrentCursorPoint() {
		return MouseInfo.getPointerInfo().getLocation();
	}
	
	private String getImageName(int counter) {
		return imageOutputDirectory + "screenshot" + counter + ".jpg";
	}
	
	/*private void resizeImagesInDirectory() throws IOException {
		File folder = new File("/home/dpapp/Desktop/RunescapeAIPics/CascadeTraining/Testing/");
		File[] listOfFiles = folder.listFiles();

		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				resizeImage(listOfFiles[i], i);
				System.out.println("Cropped " + listOfFiles[i].getName());
		    }
		}
	}*/
	
	/*private void resizeImage(File imageFile, int counter) throws IOException {
		BufferedImage screenshot = ImageIO.read(imageFile);
		//Rectangle resizeRectangle = new Rectangle(103, 85, 510, 330);
		BufferedImage resizedImage = screenshot.getSubimage(103, 85, 510, 330);
		ImageIO.write(resizedImage, "jpg", new File(getImageName(counter)));
	}*/
	
	public static void main(String[] args) throws Exception
    {
		System.out.println("Starting image collection...");
        cascadeTrainingImageCollector imageCollector = new cascadeTrainingImageCollector(Paths.TENSORFLOW_TRAINING_IMAGE_OUTPUT_DIRECTORY);
        //imageCollector.resizeImagesInDirectory();
		imageCollector.captureWindowEveryNMilliseconds(2000);
		//cascadeTrainingImageCollector imageCollector = new cascadeTrainingImageCollector("/home/dpapp/Desktop/RunescapeAIPics/CascadeTraining/Testing/");
		//imageCollector.captureWindowEveryNMilliseconds(1);
    }
}
