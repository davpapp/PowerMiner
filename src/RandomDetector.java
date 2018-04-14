import java.awt.AWTException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class RandomDetector {
	
	public static void dealWithRandoms(BufferedImage screenCapture, Cursor cursor) throws Exception {
		Point chatDialogueCornerPoint = findChatDialogueCornerPoint(screenCapture);
		Point speakerPoint = findSpeakerPointFromCornerPoint(screenCapture, chatDialogueCornerPoint);

		if (speakerPoint != null && isSpeakerPointCloseToCharacter(speakerPoint)) {
			cursor.moveAndRightlickAtCoordinatesWithRandomness(cursor.getOffsetPoint(speakerPoint), 3, 3);
			Thread.sleep(Randomizer.nextGaussianWithinRange(1332, 1921));
			
			if (dialogueHasDismissOption(speakerPoint)) {
				cursor.moveAndLeftClickAtCoordinatesWithRandomness(cursor.getOffsetPoint(getDismissOptionClickLocation(speakerPoint)), 40, 10, 6);
				Thread.sleep(1743, 2313);
			}
			else {
				cursor.moveCursorToCoordinatesWithRandomness(new Point(Constants.GAME_WINDOW_OFFSET_X, Constants.GAME_WINDOW_OFFSET_Y), 20, 20);
			}
		}
	}

	private static boolean dialogueHasDismissOption(Point speakerPoint) throws IOException, AWTException {
		Rectangle dialogueRectangle = new Rectangle(Constants.GAME_WINDOW_OFFSET_X + speakerPoint.x - 25, Constants.GAME_WINDOW_OFFSET_Y + speakerPoint.y, 95, 55);
		BufferedImage dialogueCapture = ImageCapturer.captureScreenRectangle(dialogueRectangle);
		for (int x = 0; x < 95; x++) {
			for (int y = 0; y < 55; y++) {
				int pixelColor = dialogueCapture.getRGB(x, y);
				if (isPixelChatColor(pixelColor)) {
					return true;
				}
			}
		}
		return false;
	}

	public static Point findChatDialogueCornerPoint(BufferedImage screenCapture) throws AWTException, InterruptedException {		
		for (int x = 30; x < Constants.GAME_WINDOW_WIDTH - 30; x++) {
			for (int y = 20; y < Constants.GAME_WINDOW_HEIGHT - 20; y++) {
				int pixelColor = screenCapture.getRGB(x, y);
				if (isPixelChatColor(pixelColor)) {
					return new Point(x, y);
				}
			}
		}
		return null;
	}
	
	private static boolean isPixelChatColor(int color) {
		int[] colorChannels = getRGBChannelsFromPixel(color);
		return (isColorWithinTolerance(colorChannels[0], 0, 3) && 
			    isColorWithinTolerance(colorChannels[1], 255, 3) &&
			    isColorWithinTolerance(colorChannels[2], 255, 3));
	}
	
	public static Point findSpeakerPointFromCornerPoint(BufferedImage screenCapture, Point chatDialogueStart) {	
		if (chatDialogueStart == null) {
			return null;
		}
		
		int rightMostChatColorPixel = chatDialogueStart.x;
		int countSinceLastChatColorPixel = 0;
		for (int x = chatDialogueStart.x; x < Constants.GAME_WINDOW_WIDTH && countSinceLastChatColorPixel < 30; x++) {
			for (int y = chatDialogueStart.y; y < chatDialogueStart.y + 20; y++) {
				int pixelColor = screenCapture.getRGB(x, y);
				if (isPixelChatColor(pixelColor)) {
					rightMostChatColorPixel = x;
					countSinceLastChatColorPixel = 0;
				}
			}
			countSinceLastChatColorPixel++;
		}
		
		int chatDialogueBoxWidth = rightMostChatColorPixel - chatDialogueStart.x;
		if (chatDialogueBoxWidth > 60 && chatDialogueBoxWidth < 400) {
			return new Point(chatDialogueStart.x + chatDialogueBoxWidth / 2, chatDialogueStart.y + 25);
		}
		return null;
	}
	
	/*public static BufferedImage blurDialogueFromImage(BufferedImage screenCapture) throws AWTException, InterruptedException {
		Point chatDialogueStart = findChatDialogueCornerPoint(screenCapture);
		if (chatDialogueStart == null) {
			return screenCapture;
		}
		
		int rightMostChatColorPixel = chatDialogueStart.x;
		int countSinceLastChatColorPixel = 0;
		for (int x = chatDialogueStart.x; x < Constants.GAME_WINDOW_WIDTH && countSinceLastChatColorPixel < 30; x++) {
			for (int y = chatDialogueStart.y; y < chatDialogueStart.y + 20; y++) {
				int pixelColor = screenCapture.getRGB(x, y);
				if (isPixelChatColor(pixelColor)) {
					rightMostChatColorPixel = x;
					countSinceLastChatColorPixel = 0;
				}
			}
			countSinceLastChatColorPixel++;
		}
		
		int chatDialogueBoxWidth = rightMostChatColorPixel - chatDialogueStart.x;
		if (chatDialogueBoxWidth > 60 && chatDialogueBoxWidth < 400) {
			//BufferedImage result = screenCapture;
			Graphics g = screenCapture.getGraphics();
			g.clearRect(chatDialogueStart.x, chatDialogueStart.y, chatDialogueBoxWidth, 25);
			System.out.println("Found dialogue! Blurring image");
		}
		return screenCapture;
	}*/
	
	private static boolean isSpeakerPointCloseToCharacter(Point speakerPoint) {
		return (Math.abs(speakerPoint.x + Constants.GAME_WINDOW_OFFSET_X - Constants.CHARACTER_CENTER_X) < 90 && Math.abs(speakerPoint.y + Constants.GAME_WINDOW_OFFSET_Y - Constants.CHARACTER_CENTER_Y) < 80);
	}
	
	private static Point getDismissOptionClickLocation(Point speakerLocation) {
		return new Point(speakerLocation.x, speakerLocation.y + 46);
	}
	
	private static boolean isColorWithinTolerance(int color1, int color2, int tolerance) {
		return (Math.abs(color1 - color2) < tolerance);
	}
	
	private static int[] getRGBChannelsFromPixel(int pixel) {
		int[] colors = {(pixel)&0xFF, (pixel>>8)&0xFF, (pixel>>16)&0xFF, (pixel>>24)&0xFF};
		return colors;
	}	
}
