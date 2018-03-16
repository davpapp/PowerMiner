import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

public class ImageCapturer {

	public static BufferedImage captureScreenshotGameWindow() throws AWTException {
		Rectangle rectangle = new Rectangle(Constants.GAME_WINDOW_OFFSET_X, Constants.GAME_WINDOW_OFFSET_Y, Constants.GAME_WINDOW_WIDTH, Constants.GAME_WINDOW_HEIGHT);
		return new Robot().createScreenCapture(rectangle);
	 }
	
	public static BufferedImage captureScreenRectangle(Rectangle rectangle) throws AWTException {
		return new Robot().createScreenCapture(rectangle);
	}
	
}
