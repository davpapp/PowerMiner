import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class MiningSuccessHistory {

	int queueSize;
	int numberOfTruesInQueue;
	Queue<Boolean> miningSuccessHistory;
	
	public MiningSuccessHistory() {
		queueSize = 30;
		this.numberOfTruesInQueue = queueSize;
		miningSuccessHistory = new LinkedList<Boolean>();
		for (int i = 0; i < queueSize; i++) {
			miningSuccessHistory.add(true);
		}
	}
	
	public boolean updateHistory(boolean success) {
		miningSuccessHistory.add(success);
		if (success) { 
			numberOfTruesInQueue++;
		}
		if (miningSuccessHistory.poll()) {
			numberOfTruesInQueue--;
		}
		return isMiningRateSufficient();
	}
	
	private boolean isMiningRateSufficient() {
		return numberOfTruesInQueue < (queueSize * 0.8);
	}
	
	public void resetQueue() {
		this.numberOfTruesInQueue = queueSize;
		miningSuccessHistory.clear();
		for (int i = 0; i < queueSize; i++) {
			miningSuccessHistory.add(true);
		}
	}
	
	public void displayStats() {
		System.out.println("Mining success rate: " + (numberOfTruesInQueue * 1.0 / queueSize));
	}
}
