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
	
	/*
	 * Methods needed:
	 * 
	 * - Capture screen window every N seconds
	 * 
	 * initialize with: output directory, file name
	 * 
	 * detect last file name
	 */
	
	public ImageCollector(String screenshotOutputDirectory) {
		initializeGameWindowRectangle();
		this.screenshotOutputDirectory = screenshotOutputDirectory;
	}
	
	private void initializeGameWindowRectangle() {
		this.gameWindowRectangle = new Rectangle(103, 85, 510, 330);
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
		Robot robot = new Robot();
		BufferedImage imageCaptured = robot.createScreenCapture(gameWindowRectangle);
		String fileName = getFileName(itemName, fileCounter);
		ImageIO.write(imageCaptured, "jpg", new File(fileName));
		System.out.println("Wrote file: " + fileName);
	}
	
	private String getFileName(String itemName, int counter) {
		return screenshotOutputDirectory + itemName + "_" + counter + ".jpg";
	}
	
	private void generateInventorySlotImages() throws AWTException, IOException {
		Inventory inventory = new Inventory();
		inventory.updateAndWriteAllInventorySlotsToImages();
	}
	
	public static void main(String[] args) throws Exception
    {
        ImageCollector imageCollector = new ImageCollector("/home/dpapp/Desktop/RunescapeAI/TensorFlow/IronOre/");
        //imageCollector.collectImages("ironOre");
        imageCollector.generateInventorySlotImages();
    }
}
