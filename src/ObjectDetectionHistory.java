import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class ObjectDetectionHistory {

	int queueSize;
	int targetNumberOfDetectedObjects;
	int numberOfTruesInQueue;
	Queue<Boolean> numberOfDetectedObjectsHistory;
	
	public ObjectDetectionHistory(int targetNumberOfDetectedObjects) {
		queueSize = 150;
		this.targetNumberOfDetectedObjects = targetNumberOfDetectedObjects;
		this.numberOfTruesInQueue = queueSize;
		numberOfDetectedObjectsHistory = new LinkedList<Boolean>();
		for (int i = 0; i < queueSize; i++) {
			numberOfDetectedObjectsHistory.add(true);
		}
	}
	
	public boolean updateHistory(int numberOfDetectedObjects) {
		updateHistory(numberOfDetectedObjects >= targetNumberOfDetectedObjects);
		return isObjectDetectionUnsuccessful();
	}
	
	public void updateHistory(boolean success) {
		numberOfDetectedObjectsHistory.add(success);
		if (success) { 
			numberOfTruesInQueue++;
		}
		if (numberOfDetectedObjectsHistory.poll()) {
			numberOfTruesInQueue--;
		}
	}
	
	private boolean isObjectDetectionUnsuccessful() {
		return numberOfTruesInQueue < (queueSize * 0.6);
	}
	
	public void resetQueue() {
		this.numberOfTruesInQueue = queueSize;
		numberOfDetectedObjectsHistory.clear();
		for (int i = 0; i < queueSize; i++) {
			numberOfDetectedObjectsHistory.add(true);
		}
	}
	
	public void displayStats() {
		System.out.println("Object detection rate: " + (numberOfTruesInQueue * 1.0 / queueSize));
	}
}
