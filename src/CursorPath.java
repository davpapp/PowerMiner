/*
 * Represents each mouse path as an ArrayList of points.
 */

import java.util.ArrayList;

public class CursorPath {
		
	private ArrayList<CursorPoint> cursorPoints;
	private double theta;
	private int distance;
	private int timespan;
		
	public CursorPath(ArrayList<CursorPoint> cursorPoints)
	{
		this.cursorPoints = initializePathOfCursorPoints(cursorPoints);
		this.distance = calculateCursorPathDistance();
		this.theta = calculateCursorPathTheta();
		this.timespan = calculateCursorPathTimespan();
	}
	
	private ArrayList<CursorPoint> initializePathOfCursorPoints(ArrayList<CursorPoint> cursorPoints) {
		CursorPoint startingCursorPoint = cursorPoints.get(0);
		ArrayList<CursorPoint> translatedCursorPoints = getTranslatedCopyOfCursorPath(cursorPoints, startingCursorPoint);
		ArrayList<CursorPoint> normalizedDelayCursorPoints = getNormalizedDelayCopyOfCursorPath(translatedCursorPoints);
		return normalizedDelayCursorPoints;
	}
	
	private CursorPath getTranslatedCopyOfCursorPath(ArrayList<CursorPoint> cursorPoints, CursorPoint cursorPointToTranslateBy) {
		ArrayList<CursorPoint> offsetCursorPath = new ArrayList<CursorPoint>();
		for (CursorPoint cursorPoint : cursorPoints) {
			offsetCursorPath.add(cursorPoint.getCursorPointTranslatedBy(cursorPointToTranslateBy));
		}
		return new CursorPath(offsetCursorPath);
	}
	
	private CursorPath getNormalizedDelayCopyOfCursorPath(ArrayList<CursorPoint> cursorPoints) {
		ArrayList<CursorPoint> normalizedDelayCursorPoints = new ArrayList<CursorPoint>();
		for (int i = 0; i < cursorPoints.size() - 1; i++) {
			CursorPoint cursorPoint = cursorPoints.get(i);
			CursorPoint nextCursorPoint = cursorPoints.get(i + 1);
			normalizedDelayCursorPoints.add(cursorPoint.getCursorPointWithNewDelay(nextCursorPoint.delay - cursorPoint.delay));
		}
		normalizedDelayCursorPoints.add(cursorPoints.get(cursorPoints.size() - 1).getCursorPointWithNewDelay(0));
		return new CursorPath(normalizedDelayCursorPoints);
	}

	
	public CursorPath getScaledCopyOfCursorPath(ArrayList<CursorPoint> cursorPoints, double factorToScaleBy) {
		ArrayList<CursorPoint> scaledCursorPath = new ArrayList<CursorPoint>();
		for (CursorPoint cursorPoint : cursorPoints) {
			scaledCursorPath.add(cursorPoint.getCursorPointScaledBy(factorToScaleBy));
		}
		return new CursorPath(scaledCursorPath);
	}
	
	public CursorPath getRotatedCopyOfCursorPath(ArrayList<CursorPoint> cursorPoints, double angleToRotateBy) {
		ArrayList<CursorPoint> rotatedCursorPath = new ArrayList<CursorPoint>();
		for (CursorPoint cursorPoint : cursorPoints) {
			rotatedCursorPath.add(cursorPoint.getCursorPointRotatedBy(angleToRotateBy));
		}
		return new CursorPath(rotatedCursorPath);
	}
	
	private int calculateCursorPathTimespan() {
		int sumPathTimespanMilliseconds = 0;
		for (CursorPoint cursorPoint : this.cursorPoints) {
			sumPathTimespanMilliseconds += cursorPoint.delay;
		}
		return sumPathTimespanMilliseconds;
	}
	
	private int calculateCursorPathDistance() {
		return (int) (getStartingCursorPoint().getDistanceFrom(getEndingCursorPoint()));
	}
	
	private double calculateCursorPathTheta() {
		CursorPoint endingCursorPoint = getEndingCursorPoint();
		return Math.atan2(endingCursorPoint.y, endingCursorPoint.x);
	}
	
	public CursorPoint getStartingCursorPoint() {
		return this.cursorPoints.get(0);
	}
	
	public CursorPoint getEndingCursorPoint() {
		return this.cursorPoints.get(this.cursorPoints.size() - 1);
	}
	
	
	public boolean isCursorPathReasonable() {
		return isCursorPathTimespanReasonable() && isCursorPathDistanceReasonable() &&
				isCursorPathNumPointsReasonable();
	}
	
	private boolean isCursorPathTimespanReasonable() {
		return (this.timespan > 50 && this.timespan < 400);
	}
	
	private boolean isCursorPathDistanceReasonable() {
		return (this.distance > 0 && this.distance < 1000);
	}
	
	private boolean isCursorPathNumPointsReasonable() {
		return (this.cursorPoints.size() > 0 && this.cursorPoints.size() < 50);
	}
	
	public ArrayList<CursorPoint> getCursorPathPoints() {
		return cursorPoints;
	}
	
	public int getCursorPathDistance() {
		return distance;
	}
	
	public double getCursorPathTheta() {
		return theta;
	}

	
	public void displayCursorPoints() {
		for (CursorPoint p : this.cursorPoints) {
			p.display();
		}
		System.out.println("Number of points:" + this.cursorPoints.size() + ", Timespan:" + this.timespan);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~ End of Path ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}
}