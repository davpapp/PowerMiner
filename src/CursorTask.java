import java.awt.AWTException;
import java.awt.Point;

public class CursorTask {
	
	public static final int DROP_OFFSET = 40;
	public static final int DROP_OFFSET_BOTTOM_ROW = 10;

	public void dropAllItemsInInventory(Cursor cursor, Inventory inventory) throws InterruptedException {
		for (int row = 0; row < 4; row++) {
			for (int column = 0; column < 7; column++) {
				dropItem(cursor, inventory, row, column);
			}
		}
	}
	
	public void dropItem(Cursor cursor, Inventory inventory, int row, int column) throws InterruptedException {
		System.out.println("Dropping item...");
		Point coordinatesToRightClick = inventory.getClickCoordinatesCoordinatesForInventorySlot(row, column);
		rightClickItemSlot(cursor, coordinatesToRightClick);
		leftClickDropOption(cursor, coordinatesToRightClick, row);
	}
	
	public void rightClickItemSlot(Cursor cursor, Point coordinatesToRightClick) throws InterruptedException {
		cursor.moveAndRightlickAtCoordinatesWithRandomness(coordinatesToRightClick, 10, 10);
	}
	
	private void leftClickDropOption(Cursor cursor, Point coordinatesToLeftClick, int row) throws InterruptedException {
		Point offsetCoordinatesToLeftClick = coordinatesToLeftClick;
		if (row < 6) {
			offsetCoordinatesToLeftClick.y += DROP_OFFSET;
		}
		else {
			offsetCoordinatesToLeftClick.y += DROP_OFFSET_BOTTOM_ROW;
		}
		cursor.moveAndLeftClickAtCoordinatesWithRandomness(coordinatesToLeftClick, 10, 10);
	}
}
