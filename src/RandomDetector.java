import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;

public class RandomDetector {
	Robot robot;
	
	public RandomDetector() throws AWTException {
		robot = new Robot();
	}
	
	public void dealWithRandoms(BufferedImage screenCapture) throws AWTException, InterruptedException {
		if (isRandomEventPresent(screenCapture)) {
			System.out.println("Deal with random here");
		}
	}

	public boolean isRandomEventPresent(BufferedImage screenCapture) throws AWTException, InterruptedException
	{
		// only do this if it's close to center
		boolean foundDialogue = false;
		for (int x = 0; x < Constants.GAME_WINDOW_WIDTH; x += 5) {
			for (int y = 0; y < Constants.GAME_WINDOW_HEIGHT; y += 1) {
				int color = screenCapture.getRGB(x, y);
				if (pixelsAreWithinRGBTolerance(color, 10)) {
					return true;
				}
				
			}
		}
		return false;
	}
	
	private boolean pixelsAreWithinRGBTolerance(int rgb1, int rgb2) {
		int[] colors1 = getRGBValuesFromPixel(rgb1);
		int[] colors2 = getRGBValuesFromPixel(rgb2);
		for (int i = 0; i < 3; i++) {
			if (Math.abs(colors1[i] - colors2[i]) > 3) {
				return false;
			}
		}
		return true;
	}
	
	private int[] getRGBValuesFromPixel(int pixel) {
		int[] colors = {(pixel)&0xFF, (pixel>>8)&0xFF, (pixel>>16)&0xFF, (pixel>>24)&0xFF};
		return colors;
	}	
}
