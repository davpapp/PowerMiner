import java.io.IOException;

public class DropperThread implements Runnable {
	Thread dropperThread;
	Inventory inventory;
	Cursor cursor;
	
	public DropperThread(Inventory inventory, Cursor cursor) {
		this.inventory = inventory;
		this.cursor = cursor;
	}
	
	@Override
	public void run() {
		try {
			inventory.update();
			cursor.moveAndRightlickAtCoordinatesWithRandomness(inventory.getClickCoordinatesForInventorySlot(0, 0), 15, 15);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("~~~~~~~~~~~~~ dropperThread finished!");
	}
	
	public void start() {
		System.out.println("dropperThread started");
		if (dropperThread == null) {
			dropperThread = new Thread(this, "dropperThread");
			dropperThread.start();
		}
	}
	
	public void waitTillDone() throws InterruptedException {
		dropperThread.join();
	}

}
