import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class Items {
	// TODO: write tests
	HashMap<String, Item> items;
	
	public Items(String itemDirectoryPath) throws IOException {
		initializeItemsFromDirectory(itemDirectoryPath);
	}
	
	private void initializeItemsFromDirectory(String itemDirectoryPath) throws IOException {
		this.items = new HashMap<String, Item>();
		for (File itemFile : getListOfFilesFromItemDirectory(itemDirectoryPath)) {
			if (itemFile.isFile()) {
				putItemInMap(itemDirectoryPath, itemFile.getName());
			}
		}
	}
	
	public File[] getListOfFilesFromItemDirectory(String itemDirectoryPath) {
		File itemDirectory = new File(itemDirectoryPath);
		return itemDirectory.listFiles();
	}
	
	private void putItemInMap(String itemDirectoryPath, String itemFileName) throws IOException {
		Item item = new Item(itemDirectoryPath, itemFileName);
		String itemName = getItemNameFromFile(itemFileName);
		this.items.put(itemName, item);
	}
	
	public String getItemNameFromFile(String fileName) {
		return fileName.substring(0, fileName.indexOf('.'));
	}
	
	public boolean isImageThisItem(BufferedImage itemImage, String itemName) {
		if (items.containsKey(itemName)) {
			return getItemByName(itemName).itemMatchesImage(itemImage);
		}
		return false;
	}
	
	public String getNameOfItemFromImage(BufferedImage inputImage) {
		for (String itemName : items.keySet()) {
		    if (getItemByName(itemName).itemMatchesImage(inputImage)) {
		    	return itemName;
		    }
		}
		return "empty";
	}
	
	private Item getItemByName(String itemName) {
		return items.get(itemName);
	}
	
	
	
	/*public void displayItems() {
		for (HashMap.Entry<String, Item> entry : items.entrySet()) {
		    String itemName = entry.getKey();
		    Item item = entry.getValue();
		    System.out.println("Item name: " + itemName);
		}
	}*/
}
