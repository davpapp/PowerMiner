import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class WorldHopper {

	int numberOfOresMined;
	int numberOfFramesWithOtherPlayers;
	Queue<Boolean> frames;
	Queue<Boolean> oresMined;
	
	public WorldHopper() {
		numberOfOresMined = 0;
		numberOfFramesWithOtherPlayers = 0;
		frames = new LinkedList<Boolean>();
		for (int i = 0; i < 600; i++) {
			frames.add(false);
		}
		oresMined = new LinkedList<Boolean>();
		for (int i = 0; i < 30; i++) {
			oresMined.add(true);
		}
	}
	
	public void hopWorldsIfOtherPlayersPresent(ArrayList<DetectedObject> players) {
		updateOtherPlayerTracking(players);
		if (areOtherPlayersLikelyPresent()) {
			hopWorld();
		}
	}
	
	private void updateOtherPlayerTracking(ArrayList<DetectedObject> players) {
		if (players.size() > 1) {
			numberOfFramesWithOtherPlayers++;
			frames.add(true);
		}
		else {
			frames.add(false);
		}
		if (frames.poll()) {
			numberOfFramesWithOtherPlayers--;
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
		return numberOfFramesWithOtherPlayers > 360 || numberOfOresMined < 18;
	}
	
	private void hopWorld() {
		System.out.println("Hopping worlds!");
	}
}
