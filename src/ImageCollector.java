import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import javax.imageio.ImageIO;

public class ImageCollector {

	public String screenshotOutputDirectory;
	public Rectangle gameWindowRectangle;
	public Rectangle fullWindowRectangle;
	public Robot robot;
	
	/*
	 * Methods needed:
	 * 
	 * - Capture screen window every N seconds
	 * 
	 * initialize with: output directory, file name
	 * 
	 * detect last file name
	 */
	
	 public BufferedImage captureScreenshotGameWindow() throws IOException, AWTException {
			return robot.createScreenCapture(gameWindowRectangle);
    }
	
	public ImageCollector(String screenshotOutputDirectory) throws AWTException {
		initializeGameWindowRectangle();
		initializeFullWindowRectangle();
		this.screenshotOutputDirectory = screenshotOutputDirectory;
		this.robot = new Robot();
	}
	
	private void initializeGameWindowRectangle() {
		this.gameWindowRectangle = new Rectangle(Constants.GAME_WINDOW_OFFSET_X, Constants.GAME_WINDOW_OFFSET_Y, Constants.GAME_WINDOW_WIDTH, Constants.GAME_WINDOW_HEIGHT);
	}
	
	private void initializeFullWindowRectangle() {
		this.fullWindowRectangle = new Rectangle(0, 0, Constants.FULL_WINDOW_WIDTH, Constants.FULL_WINDOW_HEIGHT);
	}
	
	public void collectImages(String itemName) throws IOException, InterruptedException, AWTException {
		int itemCounter = getItemCounter(itemName);
		int numImagesToCapture = 50;
		for (int counter = itemCounter + 1; counter < itemCounter + numImagesToCapture + 1; counter++) {
			captureAndSaveGameWindow(itemName, counter);
			Thread.sleep(2000);
		}
	}
	
	private int getItemCounter(String itemName) {
		File[] listOfFiles = getFilesFromFolderThatStartWith(itemName);
		int counter = 0;
		for (File file : listOfFiles) {
			counter = Math.max(counter, getItemNumberFromFile(file.getName()));
		}
		return counter;
	}
	
	/*public void blurTextFromImages() throws IOException, AWTException, InterruptedException {
		File folder = new File("/home/dpapp/Videos/BlurredRandom/NonBlurred/");
		File[] listOfImages = folder.listFiles();
		//RandomDetector randomDetector = new RandomDetector();
		int count = 0;
		for (File file : listOfImages) {
			System.out.println(count);
			BufferedImage image = ImageIO.read(file);
			BufferedImage blurredImage = RandomDetector.blurDialogueFromImage(image);
			ImageIO.write(blurredImage, "png", new File("/home/dpapp/Videos/BlurredRandom/Blurred/blurredRandom" + count + ".png"));
			count++;
		}
	}*/
	
	private File[] getFilesFromFolderThatStartWith(String itemName) {
		File folder = new File(screenshotOutputDirectory);
		File[] listOfFiles = folder.listFiles(new FilenameFilter() {
			public boolean accept(File file, String name) {
				return name.startsWith(itemName);
			}
		});
		return listOfFiles;
	}
	
	private int getItemNumberFromFile(String fileName) {
		String itemNumber = fileName.substring(fileName.indexOf("_") + 1, fileName.indexOf("."));
		return Integer.parseInt(itemNumber);
	}
	
	private void captureAndSaveGameWindow(String itemName, int fileCounter) throws IOException, InterruptedException, AWTException {
		BufferedImage imageCaptured = robot.createScreenCapture(gameWindowRectangle);
		String fileName = getFileName(itemName, fileCounter);
		ImageIO.write(imageCaptured, "jpg", new File(fileName));
		System.out.println("Wrote file: " + fileName);
	}
	
	public void captureAndSaveFullWindow() throws IOException {
		BufferedImage imageCaptured = robot.createScreenCapture(fullWindowRectangle);
		ImageIO.write(imageCaptured, "jpg", new File(getFileName("fullWindow", 0)));
		System.out.println("Wrote file.");
	}
	
	private String getFileName(String itemName, int counter) {
		return screenshotOutputDirectory + itemName + "_" + counter + ".jpg";
	}
	
	private void generateInventoryImages() throws AWTException, IOException {
		Inventory inventory = new Inventory();
		inventory.updateAndWriteAllInventoryImages();
	}
	
	public static void main(String[] args) throws Exception
    {
        ImageCollector imageCollector = new ImageCollector("/home/dpapp/Desktop/RunescapeAI/Images/");
        //imageCollector.blurTextFromImages();
        //imageCollector.collectImages("chatDialogue");
        //imageCollector.generateInventoryImages();
        //imageCollector.captureAndSaveFullWindow();
    }

}
