import java.awt.AWTException;
import java.awt.Point;

public class CursorTask {
	
	public static final int DROP_OFFSET = 40;
	public static final int DROP_BOTTOM_ROW = 539;

	public void dropAllItemsInInventory(Cursor cursor, Inventory inventory) throws InterruptedException {
		for (int row = 0; row < 4; row++) {
			for (int column = 0; column < 7; column++) {
				dropItem(cursor, inventory, row, column);
			}
		}
	}
	
	public void dropBottomRow(Cursor cursor, Inventory inventory) throws InterruptedException {
		for (int row = 0; row < 4; row++) {
			dropItem(cursor, inventory, row, 6);
		}
	}
	
	public void dropItem(Cursor cursor, Inventory inventory, int row, int column) throws InterruptedException {
		System.out.println("Dropping item...");
		Point coordinatesToRightClick = inventory.getClickCoordinatesCoordinatesForInventorySlot(row, column);
		Point clickedCoordinates = rightClickItemSlot(cursor, coordinatesToRightClick);
		leftClickDropOption(cursor, clickedCoordinates, column);
	}
	
	public Point rightClickItemSlot(Cursor cursor, Point coordinatesToRightClick) throws InterruptedException {
		Point clickedCoordinates = cursor.moveAndRightlickAtCoordinatesWithRandomness(coordinatesToRightClick, 10, 10);
		return clickedCoordinates;
	}
	
	private void leftClickDropOption(Cursor cursor, Point coordinatesToLeftClick, int column) throws InterruptedException {
		Point offsetCoordinatesToLeftClick = coordinatesToLeftClick;
		if (column < 6) {
			offsetCoordinatesToLeftClick.y += DROP_OFFSET;
		}
		else {
			offsetCoordinatesToLeftClick.y = DROP_BOTTOM_ROW;
		}
		Point p = cursor.moveAndLeftClickAtCoordinatesWithRandomness(offsetCoordinatesToLeftClick, 10, 6);
		System.out.println(p.x + "," + p.y);
	}
}
