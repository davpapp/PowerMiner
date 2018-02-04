import java.awt.AWTException;
import java.io.IOException;

public class WillowChopper {
	
	Cursor cursor; 
	CursorTask cursorTask;
	Inventory inventory;
	
	public WillowChopper() throws AWTException, IOException 
	{
		cursor = new Cursor();
		cursorTask = new CursorTask();
		inventory = new Inventory();
	}
	
	public void run() throws IOException, InterruptedException {
		
		while (true) {
			/*
			if (character.isCharacterEngaged()) {
				// DO NOTHING
				// do things like checking the inventory
			}
			else {
				find closest willow tree
				chop willow tree
			}
			 */
			inventory.update();
			if (inventory.isInventoryFull()) {
				cursorTask.dropAllItemsInInventory(cursor, inventory);
			}
		}
	}

}
