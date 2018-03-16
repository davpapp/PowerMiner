import java.awt.AWTException;
import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;

public class WorldHopper {

	//Set<Integer> visitedWorlds;
	
	public static void hopWorld(Cursor cursor) throws Exception {
		cursor.moveAndLeftClickInBoundingRectangle(Constants.getLogoutIconRectangle());
		Thread.sleep(650, 900);
		//cursor.moveAndLeftClickInBoundingRectangle(Constants.getWorldSwitchButtonRectangle());
		Thread.sleep(2150, 3080);
		Random random = new Random();
		int x = random.nextInt(Constants.WORLD_LISTING_WIDTH) + Constants.WORLD_LISTING_OFFSET_X;
		int y = random.nextInt(Constants.WORLD_LISTING_HEIGHT) + Constants.WORLD_LISTING_OFFSET_Y;
		cursor.moveAndLeftClickAtCoordinates(new Point(x, y));
		Thread.sleep(4300, 5320);
		cursor.moveAndLeftClickInBoundingRectangle(Constants.getInventoryIconRectangle());
		System.out.println("Hopping worlds!");
	}
	
	public static void main(String[] args) throws Exception {
		Cursor cursor = new Cursor();
		for (int i = 0; i < 5; i++) {
			WorldHopper.hopWorld(cursor);
			Thread.sleep(2500);
		}
		System.out.println("Done hopping...");
		Thread.sleep(10000);
	}
}
