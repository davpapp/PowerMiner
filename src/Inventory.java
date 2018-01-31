
public class Inventory {

	InventorySlot[][] inventorySlots;
	
	public Inventory() {
		inventorySlots = new InventorySlot[4][7];
		for (int row = 0; row < 7; row++) {
			for (int column = 0; column < 4; column++) {
				inventorySlots[column][row] = new InventorySlot();
			}
		}
	}
	
	public void update() {
		// Screenshot
	}

}
