import java.awt.AWTException;
import java.awt.Point;
import java.io.IOException;
import java.net.URL;

import org.opencv.core.Core;


public class main {

	public static void main(String[] args) throws Exception {
		System.out.println("Starting Iron Miner.");
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		IronMiner ironMiner = new IronMiner();
		ironMiner.run();
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
