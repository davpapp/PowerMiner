import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class WorldHopper {

	int numberOfOresMined;
	int queueSize;
	//int numberOfFramesWithOtherPlayers;
	//Queue<Boolean> frames;
	Queue<Boolean> oresMined;
	
	public WorldHopper() {
		numberOfOresMined = 0;
		queueSize = 30;
		//numberOfFramesWithOtherPlayers = 0;
		//frames = new LinkedList<Boolean>();
		/*for (int i = 0; i < 600; i++) {
			frames.add(false);
		}*/
		oresMined = new LinkedList<Boolean>();
		for (int i = 0; i < queueSize; i++) {
			oresMined.add(true);
		}
	}
	
	public void hopWorldsIfMiningRateLow() {
		updateOresMinedSuccessTracking();
		if (areOtherPlayersLikelyPresent()) {
			hopWorld();
		}
	}
	
	
	public void updateOresMinedSuccessTracking(boolean success) {
		oresMined.add(success);
		if (success) { 
			numberOfOresMined++;
		}
		if (oresMined.poll()) {
			numberOfOresMined--;
		}
	}
	
	private boolean areOtherPlayersLikelyPresent() {
		return  numberOfOresMined < (queueSize * 0.6);
	}
	
	private void hopWorld() {
		System.out.println("Hopping worlds!");
	}
}
