/*
 * I made this class when I was trying to do color and feature matching with Haartraining in OpenCV.
 * I no longer really use this, except for printCursorColor, which also prints the cursor's coordinates.
 */


import java.awt.AWTException;
import java.awt.Color;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class ColorAnalyzer {

	String inputImageDirectory;
	
	public ColorAnalyzer() {
		inputImageDirectory = "/home/dpapp/Desktop/RunescapeAIPics/colorDetection/";
	}
	
	public BufferedImage readImageFromFile(String imageFileName) throws IOException {
		File imageFile = new File(getImagePath(imageFileName));
		return ImageIO.read(imageFile);
	}
	
	private String getImagePath(String imageFileName) {
		return this.inputImageDirectory + imageFileName;
	}
	
	public void showColorDistribution(String imageName) throws IOException {
		HashMap<Integer, Integer> colorFrequencies = new HashMap<Integer, Integer>();
		
		BufferedImage image = readImageFromFile(imageName);
		for (int row = 0; row < image.getWidth(); row++) {
			for (int col = 0; col < image.getHeight(); col++) {
				int color = image.getRGB(row, col); //getRGBValuesFromPixel(image.getRGB(row, col));
				int count = colorFrequencies.containsKey(color) ? colorFrequencies.get(color) : 0;
				colorFrequencies.put(color, count + 1);
			}
		}
		System.out.println(image.getWidth() + ", " + image.getHeight());
		displayColorDistribution(colorFrequencies);
	}
	
	public void displayColorDistribution(HashMap<Integer, Integer> colorFrequencies) {
		int numDifferentColors = 0;
		for (HashMap.Entry<Integer, Integer> entry: colorFrequencies.entrySet()) {
			numDifferentColors++;
			int[] color = getRGBValuesFromPixel(entry.getKey());
			int count = entry.getValue();
			System.out.println("(" + color[0] + "," + color[1] + "," + color[2] + "): " + count);
		}
		System.out.println("Number of different colors: " + numDifferentColors);
	}
	
	private int[] getRGBValuesFromPixel(int pixel) {
		int[] colors = {(pixel)&0xFF, (pixel>>8)&0xFF, (pixel>>16)&0xFF, (pixel>>24)&0xFF};
		return colors;
	}	
	
	private boolean pixelsAreWithinRGBTolerance(int rgb1, int rgb2, int tolerance) {
		int[] colors1 = getRGBValuesFromPixel(rgb1);
		int[] colors2 = getRGBValuesFromPixel(rgb2);
		for (int i = 0; i < 3; i++) {
			if (Math.abs(colors1[i] - colors2[i]) > tolerance) {
				return false;
			}
		}
		return true;
	}
	
	public void printCursorColor() throws IOException, InterruptedException, AWTException {
		BufferedImage image;
		Robot robot = new Robot();
		
		while (true) {
		    image = robot.createScreenCapture(new Rectangle(0, 0, 1920, 1080));
			Point cursorPosition = getCurrentCursorPoint();	
			int[] colors = getRGBValuesFromPixel(image.getRGB(cursorPosition.x, cursorPosition.y));
			//System.out.println(colors[0] + "," + colors[1] + "," + colors[2]);
			System.out.println("(" + cursorPosition.x + "," + cursorPosition.y + "), Color: (" + colors[0] + "," + colors[1] + "," + colors[2]); 
			Thread.sleep(100);
		}
	}
	
	private boolean isColorInRange(int rgb1) {
		int colors[] = {makeRGBIntFromComponents(47,88,81),
						makeRGBIntFromComponents(46,88,81)};
		for (int color : colors) {
			if (pixelsAreWithinRGBTolerance(rgb1, color, 10)) {
				return true;
			}
		}
		return false;
	}
	
	public void colorImage() throws IOException {
		BufferedImage image = readImageFromFile("screenshot0.png");
		for (int row = 0; row < image.getWidth(); row++) {
			for (int col = 0; col < image.getHeight(); col++) {
				if (isColorInRange(image.getRGB(row, col))) {
					System.out.println("match!");
					image.setRGB(row, col, makeRGBIntFromComponents(255, 0, 0));
				}
			}
		}
		ImageIO.write(image, "png", new File(getImageName("filtered")));
	}
	
	private int makeRGBIntFromComponents(int red, int green, int blue) {
		return red*65536 + green*256 + blue;
	}
	
	private String getImageName(String imageName) {
		return inputImageDirectory + imageName + ".png";
	}
	
	public Point getCurrentCursorPoint() {
		return MouseInfo.getPointerInfo().getLocation();
	}
	
	public static void main(String[] args) throws Exception
    {
        ColorAnalyzer colorAnalyzer = new ColorAnalyzer();
        //colorAnalyzer.showColorDistribution("screenshot21.jpg");
        colorAnalyzer.printCursorColor();
        //colorAnalyzer.colorImage();
    }
}
