import java.awt.AWTException;
import java.awt.Point;
import java.io.IOException;
import java.net.URL;


public class main {

	public static void main(String[] args) throws AWTException, InterruptedException, IOException {
		
		//Cursor cursor = new Cursor();
		//Inventory inventory = new Inventory();
		//inventory.update();
		Items items = new Items("/home/dpapp/Desktop/RunescapeAIPics/Items/");
		//items.displayItems();
		
		System.out.println("Success!");
		//cursor.moveCursorToCoordinates(new Point(620, 420));
		
	}
}
