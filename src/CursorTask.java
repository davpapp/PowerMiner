import java.awt.AWTException;
import java.awt.Point;

public class CursorTask {
	
	public static final int DROP_OFFSET = 40;
	public static final int DROP_BOTTOM_ROW = 539;

	
	// Human drop time: 29 seconds
	// Measured: 30 seconds, 29 seconds, 28
	public void optimizedDropAllItemsInInventory(Cursor cursor, Inventory inventory) throws Exception {
		for (int row = 0; row < 4; row++) {
			Point coordinatesToClick = dropItem(cursor, inventory, row, 0);
			for (int column = 1; column < 7; column++) {
				if (distanceBetweenPoints(coordinatesToClick, inventory.getClickCoordinatesForInventorySlot(row, column)) > 12) {
					coordinatesToClick = inventory.getClickCoordinatesForInventorySlot(row, column);
				}
				rightClickItemSlot(cursor, coordinatesToClick);
				coordinatesToClick = leftClickDropOption(cursor, coordinatesToClick, column);
			}
		}
	}
	
	private int distanceBetweenPoints(Point a, Point b) {
		return (int) (Math.hypot(a.x - b.x, a.y - b.y));
	}
	
	public void dropOre(Cursor cursor, Inventory inventory) throws Exception {
		dropItem(cursor, inventory, 0, 0);
	}
	
	public void dropAllItemsInInventory(Cursor cursor, Inventory inventory) throws Exception {
		for (int row = 0; row < 4; row++) {
			for (int column = 0; column < 7; column++) {
				dropItem(cursor, inventory, row, column);
			}
		}
	}
	
	public Point dropItem(Cursor cursor, Inventory inventory, int row, int column) throws Exception {
		Point coordinatesToRightClick = inventory.getClickCoordinatesForInventorySlot(row, column);
		Point clickedCoordinates = rightClickItemSlotWithRandomness(cursor, coordinatesToRightClick);
		return leftClickDropOption(cursor, clickedCoordinates, column);
	}
	
	public void rightClickItemSlot(Cursor cursor, Point coordinatesToRightClick) throws Exception {
		cursor.moveAndRightClickAtCoordinates(coordinatesToRightClick);
	}
	
	public Point rightClickItemSlotWithRandomness(Cursor cursor, Point coordinatesToRightClick) throws Exception {
		Point clickedCoordinates = cursor.moveAndRightlickAtCoordinatesWithRandomness(coordinatesToRightClick, 6, 6);
		return clickedCoordinates;
	}
	
	public Point hoverOverDropOption(Cursor cursor, Point coordinatesToHoverOver, int column) throws Exception {
		Point offsetCoordinatesToHoverOver = coordinatesToHoverOver;
		if (column < 6) {
			offsetCoordinatesToHoverOver.y += DROP_OFFSET;
		}
		else {
			offsetCoordinatesToHoverOver.y = DROP_BOTTOM_ROW;
		}
		System.out.println("foudn where to click...");
		return cursor.moveCursorToCoordinatesWithRandomness(offsetCoordinatesToHoverOver, 13, 2); //(offsetCoordinatesToLeftClick, 13, 10, 6);
	}
	
	public Point leftClickDropOption(Cursor cursor, Point coordinatesToLeftClick, int column) throws Exception {
		Point offsetCoordinatesToLeftClick = coordinatesToLeftClick;
		if (column < 6) {
			offsetCoordinatesToLeftClick.y += DROP_OFFSET;
		}
		else {
			offsetCoordinatesToLeftClick.y = DROP_BOTTOM_ROW;
		}
		System.out.println("foudn where to click...");
		return cursor.moveAndLeftClickAtCoordinatesWithRandomness(offsetCoordinatesToLeftClick, 13, 10, 6);
	}
}
