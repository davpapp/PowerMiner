import java.awt.AWTException;
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;

public class IronMiner {
	
	public static final int IRON_ORE_MINING_TIME_MILLISECONDS = 650;
	public static final int MAXIMUM_DISTANCE_TO_WALK_TO_IRON_ORE = 650;
	public static final Point GAME_WINDOW_CENTER = new Point(200, 300);
	
	Cursor cursor; 
	CursorTask cursorTask;
	Inventory inventory;
	
	public IronMiner() throws AWTException, IOException 
	{
		cursor = new Cursor();
		cursorTask = new CursorTask();
		inventory = new Inventory();
	}
	
	public void run() throws Exception {
		
		while (true) {
			Thread.sleep(250);
			
			mineClosestIronOre();
			
			inventory.update(); // TODO: add iron ore to inventory items
			if (inventory.isInventoryFull()) {
				System.out.println("Inventory is full! Dropping...");
				cursorTask.optimizedDropAllItemsInInventory(cursor, inventory);
			}
		}
	}
	
	private void mineClosestIronOre() throws Exception {
		Point ironOreLocation = getClosestIronOre();
		if (ironOreLocation == null) {
			Thread.sleep(1000);
		}
		cursor.moveAndLeftClickAtCoordinatesWithRandomness(ironOreLocation, 20, 20);
		Thread.sleep(IRON_ORE_MINING_TIME_MILLISECONDS);
	}
	
	private Point getClosestIronOre() {
		ArrayList<Point> ironOreLocations = getIronOreLocations();
		int closestDistanceToIronOreFromCharacter = Integer.MAX_VALUE;
		Point closestIronOreToCharacter = null;
		for (Point ironOreLocation : ironOreLocations) {
			int distanceToIronOreFromCharacter = getDistanceBetweenPoints(GAME_WINDOW_CENTER, ironOreLocation);
			if (distanceToIronOreFromCharacter < closestDistanceToIronOreFromCharacter) {
				closestDistanceToIronOreFromCharacter = distanceToIronOreFromCharacter;
				closestIronOreToCharacter = ironOreLocation;
			}
		}
		
		if (closestDistanceToIronOreFromCharacter < MAXIMUM_DISTANCE_TO_WALK_TO_IRON_ORE) {
			return closestIronOreToCharacter;
		}
		return null;
	}
	
	private ArrayList<Point> getIronOreLocations() {
		// TODO: Use trained DNN here
		return new ArrayList<Point>();
	}
	
	public int getDistanceBetweenPoints(Point startingPoint, Point goalPoint) {
		return (int) (Math.hypot(goalPoint.x - startingPoint.x, goalPoint.y - startingPoint.y));
	}
	

}
