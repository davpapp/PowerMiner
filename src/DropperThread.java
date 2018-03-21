import java.awt.Point;
import java.io.IOException;

public class DropperThread implements Runnable {
	Thread dropperThread;
	Point clickLocation;
	Cursor cursor;
	CursorTask cursorTask;
	
	public DropperThread(Point clickLocation, Cursor cursor, CursorTask cursorTask) {
		this.clickLocation = clickLocation;
		this.cursor = cursor;
		this.cursorTask = cursorTask;
	}
	
	@Override
	public void run() {
		try {
			cursor.moveAndRightlickAtCoordinatesWithRandomness(clickLocation, 15, 8);
			Thread.sleep(Randomizer.nextGaussianWithinRange(30, 70));
			cursorTask.hoverOverDropOption(cursor, clickLocation, 0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println("~~~~~~~~~~~~~ dropperThread finished!");
	}
	
	public void start() {
		//System.out.println("dropperThread started");
		if (dropperThread == null) {
			dropperThread = new Thread(this, "dropperThread");
			dropperThread.start();
		}
	}
	
	public void waitTillDone() throws InterruptedException {
		dropperThread.join();
	}

}
