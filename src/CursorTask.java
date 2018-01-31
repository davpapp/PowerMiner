import java.awt.AWTException;

public class CursorTask extends Cursor {

	public CursorTask() throws AWTException {
		super();
	}
	
	public void dropAllItemsInInventory() {
		for (int inventoryColumn = 0; inventoryColumn < 7; inventoryColumn++) {
			for (int inventoryRow = 0; inventoryRow < 4; inventoryRow++) {
				dropItem
			}
		}
	}
	
	public void dropItem(InventorySlot inventorySlot) {
		/*Point inventorySlotCoordinates = inventorySlot.getCoordinates();
		moveAndRightClickAtCoordinates(inventorySlotCoordinates);
		moveAndLeftClickAtCoordinates();*/
	}
}
