import java.awt.AWTException;
import java.awt.Point;
import java.io.IOException;
import java.net.URL;


public class main {

	public static void main(String[] args) throws Exception {
		System.out.println("Starting Willow Chopper.");
		WillowChopper willowChopper = new WillowChopper();
		willowChopper.run();
		/*Cursor cursor = new Cursor();
		CursorTask cursorTask = new CursorTask();
		Inventory inventory = new Inventory();
		
		while (true) {
			inventory.update();
			if (inventory.isInventoryFull()) {
				System.out.println("Inventory full. Emptying...");
				cursorTask.optimizedDropAllItemsInInventory(cursor, inventory);
			}
			Thread.sleep(100);
		}		*/
		//cursor.moveCursorToCoordinates(new Point(620, 420));
		
	}
}
