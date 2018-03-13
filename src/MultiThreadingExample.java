
public class MultiThreadingExample {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Starting multithreading...");
		TrackerThread trackerThread = new TrackerThread();
		trackerThread.start();
		DropperThread dropperThread = new DropperThread();
		dropperThread.start();
	}

}
