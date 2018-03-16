/*
 * Represents each mouse path as an ArrayList of points.
 */

import java.util.ArrayList;
import java.util.Random;

public class CursorPath {
		
	private ArrayList<CursorPoint> cursorPoints;
	private double theta;
	private int distance;
	private int timespan;
	Randomizer randomizer;
		
	public CursorPath(ArrayList<CursorPoint> cursorPoints)
	{
		this.cursorPoints = initializePathOfCursorPoints(cursorPoints);
		this.distance = calculateCursorPathDistance();
		this.theta = calculateCursorPathTheta();
		this.timespan = calculateCursorPathTimespan();
		this.randomizer = new Randomizer();
	}
	
	public CursorPath(ArrayList<CursorPoint> cursorPoints, boolean setInitializiationOff)
	{
		this.cursorPoints = cursorPoints;
		this.distance = calculateCursorPathDistance();
		this.theta = calculateCursorPathTheta();
		this.timespan = calculateCursorPathTimespan();
	}
	
	private ArrayList<CursorPoint> initializePathOfCursorPoints(ArrayList<CursorPoint> cursorPoints) {
		CursorPoint startingCursorPoint = cursorPoints.get(0);
		ArrayList<CursorPoint> translatedCursorPoints = getTranslatedListOfCursorPoints(cursorPoints, startingCursorPoint);
		ArrayList<CursorPoint> normalizedDelayCursorPoints = getNormalizedDelayListOfCursorPoints(translatedCursorPoints);
		return normalizedDelayCursorPoints;
	}
	
	private ArrayList<CursorPoint> getTranslatedListOfCursorPoints(ArrayList<CursorPoint> cursorPoints, CursorPoint cursorPointToTranslateBy) {
		ArrayList<CursorPoint> translatedCursorPath = new ArrayList<CursorPoint>();
		for (CursorPoint cursorPoint : cursorPoints) {
			translatedCursorPath.add(cursorPoint.getCursorPointTranslatedBy(cursorPointToTranslateBy));
		}
		return translatedCursorPath;
	}
	
	private ArrayList<CursorPoint> getNormalizedDelayListOfCursorPoints(ArrayList<CursorPoint> cursorPoints) {
		ArrayList<CursorPoint> normalizedDelayCursorPoints = new ArrayList<CursorPoint>();
		for (int i = 0; i < cursorPoints.size() - 1; i++) {
			CursorPoint cursorPoint = cursorPoints.get(i);
			CursorPoint nextCursorPoint = cursorPoints.get(i + 1);
			normalizedDelayCursorPoints.add(cursorPoint.getCursorPointWithNewDelay(nextCursorPoint.delay - cursorPoint.delay));
		}
		normalizedDelayCursorPoints.add(cursorPoints.get(cursorPoints.size() - 1).getCursorPointWithNewDelay(0));
		return normalizedDelayCursorPoints;
	}

	public CursorPath getScaledCopyOfCursorPath(double factorToScaleBy) {
		ArrayList<CursorPoint> scaledCursorPoints= new ArrayList<CursorPoint>();
		for (CursorPoint cursorPoint : this.cursorPoints) {
			scaledCursorPoints.add(cursorPoint.getCursorPointScaledBy(factorToScaleBy));
		}
		return new CursorPath(scaledCursorPoints, true);
	}
	
	public CursorPath getRotatedCopyOfCursorPath(double angleToRotateTo) {
		ArrayList<CursorPoint> rotatedCursorPoints = new ArrayList<CursorPoint>();
		double angleToRotateBy = this.theta - angleToRotateTo;
		for (CursorPoint cursorPoint : this.cursorPoints) {
			rotatedCursorPoints.add(cursorPoint.getCursorPointRotatedBy(angleToRotateBy));
		}
		return new CursorPath(rotatedCursorPoints, true);
	}
	
	public CursorPath getCopyOfCursorPathTransformedByParabola() {
		double[] parabolaEquation = PathTransformer.generateParabolaEquation(this.getCursorPathDistance());
		ArrayList<CursorPoint> transformedCursorPoints = new ArrayList<CursorPoint>();
		for (CursorPoint cursorPoint : this.cursorPoints) {
			transformedCursorPoints.add(cursorPoint.getCursorPointTransformedByParabola(parabolaEquation));
		}
		return new CursorPath(transformedCursorPoints, true);	
	}
	
	private int calculateCursorPathTimespan() {
		int sumPathTimespanMilliseconds = 0;
		for (CursorPoint cursorPoint : this.cursorPoints) {
			sumPathTimespanMilliseconds += cursorPoint.delay;
		}
		return sumPathTimespanMilliseconds;
	}
	
	private int calculateCursorPathDistance() {
		return (int) (getEndingCursorPoint().getDistanceFromOrigin());
	}
	
	private double calculateCursorPathTheta() {
		return getEndingCursorPoint().getThetaFromOrigin();
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
		return (this.timespan > 50 && this.timespan < 300);
	}
	
	private boolean isCursorPathDistanceReasonable() {
		return (this.distance > 0 && this.distance < 2200);
	}
	
	private boolean isCursorPathNumPointsReasonable() {
		return (this.cursorPoints.size() > 0 && this.cursorPoints.size() < 40);
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
		System.out.println("Number of points:" + this.cursorPoints.size() + ", Timespan:" + this.timespan + ", Distance: " + this.distance + ", Theta: " + this.theta);
		System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~ End of Path ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
	}
}