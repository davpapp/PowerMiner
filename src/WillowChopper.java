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
	
	public void run() throws Exception {
		
		while (true) {
			Thread.sleep(250);
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
				long startTime = System.currentTimeMillis();
				System.out.println("Inventory is full! Dropping...");
				cursorTask.optimizedDropAllItemsInInventory(cursor, inventory);
				System.out.println("Dropping took " + (System.currentTimeMillis() - startTime) / 1000.0 + " seconds.");
				//cursorTask.dropAllItemsInInventory(cursor, inventory);
			}
		}
	}

}
