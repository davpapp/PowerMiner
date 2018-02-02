import java.awt.AWTException;
import java.awt.Point;
import java.io.IOException;
import java.net.URL;


public class main {

	public static void main(String[] args) throws AWTException, InterruptedException, IOException {
		
		Cursor cursor = new Cursor();
		CursorTask cursorTask = new CursorTask();
		//cursor.displayCursorPaths();
		Inventory inventory = new Inventory();
		//inventory.update();
		cursorTask.dropBottomRow(cursor, inventory);
		/*cursorTask.dropItem(cursor, inventory, 0, 0);
		Thread.sleep(3000);
		cursorTask.dropItem(cursor, inventory, 3, 5);
		Thread.sleep(3000);
		cursorTask.dropItem(cursor, inventory, 0, 6);*/
		//Items items = new Items("/home/dpapp/Desktop/RunescapeAIPics/Items/");
		//items.displayItems();
		
		System.out.println("Success!");
		//cursor.moveCursorToCoordinates(new Point(620, 420));
		
	}
}
