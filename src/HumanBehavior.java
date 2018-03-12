import java.util.ArrayList;

public class HumanBehavior {

	private int minimumTimeTillXPCheck = 35;
	private int maximumTimeTillXPCheck = 75;
	
	long nextTimeToCheckMiningXP;
	
	public HumanBehavior() {
		nextTimeToCheckMiningXP = System.currentTimeMillis() + getNextTimeTillXPCheck();
	}
	
	public void checkMiningXP(Cursor cursor) throws Exception {
		cursor.moveAndLeftClickInBoundingRectangle(Constants.getStatsIconRectangle());
		cursor.moveInsideBoundingRectangle(Constants.getMiningXPRectangle());
		Thread.sleep(Randomizer.nextGaussianWithinRange(1750, 3420));
		cursor.moveAndLeftClickInBoundingRectangle(Constants.getInventoryIconRectangle());
	}
	
	public void randomlyCheckMiningXP(Cursor cursor) throws Exception {
		if (System.currentTimeMillis() > nextTimeToCheckMiningXP) {
			checkMiningXP(cursor);
			nextTimeToCheckMiningXP = System.currentTimeMillis() + getNextTimeTillXPCheck();
		}
	}
	
	private int getNextTimeTillXPCheck() {
		return Randomizer.nextGaussianWithinRange(1000 * 60 * minimumTimeTillXPCheck, 1000 * 60 * maximumTimeTillXPCheck);
	}
}
