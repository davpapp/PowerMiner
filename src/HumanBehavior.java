import java.util.ArrayList;

public class HumanBehavior {

	private int minimumTimeTillXPCheck = 25;
	private int maximumTimeTillXPCheck = 73;
	private int minimumTimeTillCameraRotation = 32;
	private int maximumTimeTillCameraRotation = 61;
	
	long nextTimeToCheckMiningXP;
	long nextTimeToRotateCamera;
	
	public HumanBehavior() {
		nextTimeToCheckMiningXP = System.currentTimeMillis() + getNextTimeTillMiningXPCheck();
		nextTimeToRotateCamera = System.currentTimeMillis() + getNextTimeTillCameraRotation();
	}
	
	public void checkMiningXP(Cursor cursor) throws Exception {
		cursor.moveAndLeftClickInBoundingRectangle(Constants.getStatsIconRectangle());
		Thread.sleep(Randomizer.nextGaussianWithinRange(280, 420));
		cursor.moveInsideBoundingRectangle(Constants.getMiningXPRectangle());
		Thread.sleep(Randomizer.nextGaussianWithinRange(1750, 3420));
		cursor.moveAndLeftClickInBoundingRectangle(Constants.getInventoryIconRectangle());
	}
	
	public void randomlyCheckMiningXP(Cursor cursor) throws Exception {
		if (System.currentTimeMillis() > nextTimeToCheckMiningXP) {
			checkMiningXP(cursor);
			System.out.println("Checking mining XP!");
			nextTimeToCheckMiningXP = System.currentTimeMillis() + getNextTimeTillMiningXPCheck();
		}
	}
	
	public void randomlyRotateCamera(CameraCalibrator cameraCalibrator) throws Exception {
		if (System.currentTimeMillis() > nextTimeToRotateCamera) {
			cameraCalibrator.randomlyShiftView();
			System.out.println("Rotating camera!");
			nextTimeToRotateCamera = System.currentTimeMillis() + getNextTimeTillCameraRotation();
		}
	}
	
	private int getNextTimeTillMiningXPCheck() {
		return Randomizer.nextGaussianWithinRange(1000 * 60 * minimumTimeTillXPCheck, 1000 * 60 * maximumTimeTillXPCheck);
	}
	
	private int getNextTimeTillCameraRotation() {
		return Randomizer.nextGaussianWithinRange(1000 * 60 * minimumTimeTillCameraRotation, 1000 * 60 * maximumTimeTillCameraRotation);
	}
}
