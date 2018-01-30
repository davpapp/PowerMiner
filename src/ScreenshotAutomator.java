import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;

public class ScreenshotAutomator {

	public String screenshotOutputDirectory;
	public Robot robot;
	public Rectangle screenAreaToCapture;
	
	public ScreenshotAutomator(String screenshotOutputDirectory) throws AWTException {
		this.screenshotOutputDirectory = screenshotOutputDirectory;
		this.screenAreaToCapture = new Rectangle(0, 0, 1920 / 2, 1080);
		this.robot = new Robot();
	}
	
	public void captureEveryNSeconds(int n) throws IOException, InterruptedException {
		for (int i = 0; i < 20; i++) {
			captureScreenshot(i);
			System.out.println("Created image: " + getImageName(i));
            Thread.sleep(n * 1000);
		}
	}

	public void captureOnKeyboardInput() throws IOException, InterruptedException {
		int counter = 0;
		Scanner scanner = new Scanner(System.in);
		while (true) {
			captureScreenshot(counter);
			scanner.nextLine();
			System.out.println("Created image: " + getImageName(counter));
			counter++;
		}
	}
	
	private void captureScreenshot(int counter) throws IOException {
		BufferedImage image = robot.createScreenCapture(this.screenAreaToCapture);
		ImageIO.write(image, "png", new File(getImageName(counter)));
	}
	
	private String getImageName(int counter) {
		return screenshotOutputDirectory + "screenshot" + counter + ".png";
	}
	
	public static void main(String[] args) throws Exception
    {
        ScreenshotAutomator screenshotAutomator = new ScreenshotAutomator("/home/dpapp/Desktop/RunescapeAIPics/");
        screenshotAutomator.captureOnKeyboardInput();
    }
}
