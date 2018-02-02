import java.awt.AWTException;
import java.awt.Point;

public class CursorTask {
	
	public static final int DROP_OFFSET = 40;
	public static final int DROP_BOTTOM_ROW = 539;

	
	// Human drop time: 29 seconds
	
	// Measured: 
	public void optimizedDropAllItemsInInventory(Cursor cursor, Inventory inventory) throws InterruptedException {
		for (int row = 0; row < 4; row++) {
			Point coordinatesToClick = dropItem(cursor, inventory, row, 0);
			for (int column = 1; column < 7; column++) {
				if (distanceBetweenPoints(coordinatesToClick, inventory.getClickCoordinatesCoordinatesForInventorySlot(row, column)) > 12) {
					coordinatesToClick = inventory.getClickCoordinatesCoordinatesForInventorySlot(row, column);
				}
				rightClickItemSlot(cursor, coordinatesToClick);
				coordinatesToClick = leftClickDropOption(cursor, coordinatesToClick, column);
			}
		}
	}
	
	private int distanceBetweenPoints(Point a, Point b) {
		return (int) (Math.hypot(a.x - b.x, a.y - b.y));
	}
	
	public void dropAllItemsInInventory(Cursor cursor, Inventory inventory) throws InterruptedException {
		for (int row = 0; row < 4; row++) {
			for (int column = 0; column < 7; column++) {
				dropItem(cursor, inventory, row, column);
			}
		}
	}
	
	/*public void dropBottomRow(Cursor cursor, Inventory inventory) throws InterruptedException {
		for (int row = 0; row < 4; row++) {
			dropItem(cursor, inventory, row, 6);
		}
	}*/
	
	public Point dropItem(Cursor cursor, Inventory inventory, int row, int column) throws InterruptedException {
		Point coordinatesToRightClick = inventory.getClickCoordinatesCoordinatesForInventorySlot(row, column);
		Point clickedCoordinates = rightClickItemSlotWithRandomness(cursor, coordinatesToRightClick);
		return leftClickDropOption(cursor, clickedCoordinates, column);
	}
	
	public void rightClickItemSlot(Cursor cursor, Point coordinatesToRightClick) throws InterruptedException {
		cursor.moveAndRightClickAtCoordinates(coordinatesToRightClick);
	}
	
	public Point rightClickItemSlotWithRandomness(Cursor cursor, Point coordinatesToRightClick) throws InterruptedException {
		Point clickedCoordinates = cursor.moveAndRightlickAtCoordinatesWithRandomness(coordinatesToRightClick, 6, 6);
		return clickedCoordinates;
	}
	
	private Point leftClickDropOption(Cursor cursor, Point coordinatesToLeftClick, int column) throws InterruptedException {
		Point offsetCoordinatesToLeftClick = coordinatesToLeftClick;
		if (column < 6) {
			offsetCoordinatesToLeftClick.y += DROP_OFFSET;
		}
		else {
			offsetCoordinatesToLeftClick.y = DROP_BOTTOM_ROW;
		}
		return cursor.moveAndLeftClickAtCoordinatesWithRandomness(offsetCoordinatesToLeftClick, 10, 6);
	}
}
