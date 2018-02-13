import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class CursorPathTest {

	ArrayList<CursorPath> cursorPaths;
	
	
	@Test
	void testCursorPathOffset() {
		initializeCursorPath();
		
		for (CursorPath cursorPath : cursorPaths) {
			testCursorPathStartsAtOrigin(cursorPath);
			testEndingCursorPointInCursorPathHasZeroDelay(cursorPath);
			testCursorPathDistance(cursorPath);
			testCursorPathTheta(cursorPath);
			//testCursorPathLocations(cursorPath);
			testCursorPathRotation(cursorPath, Math.PI);
			testCursorPathScaling(cursorPath, 1.15);
			cursorPath.displayCursorPoints();
		}
	}

	private void testCursorPathStartsAtOrigin(CursorPath cursorPath) {
		CursorPoint startingCursorPoint = cursorPath.getStartingCursorPoint();
		assertTrue(startingCursorPoint.x == 0 && startingCursorPoint.y == 0);
	}
	
	private void testEndingCursorPointInCursorPathHasZeroDelay(CursorPath cursorPath) {
		CursorPoint endingCursorPoint = cursorPath.getEndingCursorPoint();
		assertEquals(endingCursorPoint.delay, 0);
	}
	
	private void testCursorPathDistance(CursorPath cursorPath) {
		int distance = cursorPath.getCursorPathDistance();
		CursorPoint startingCursorPoint = cursorPath.getStartingCursorPoint();
		CursorPoint endingCursorPoint = cursorPath.getEndingCursorPoint();
		
		int actualDistance = (int) Math.hypot(startingCursorPoint.x - endingCursorPoint.x, startingCursorPoint.y - endingCursorPoint.y);
		assertEquals(distance, actualDistance);
	}
	
	private void testCursorPathTheta(CursorPath cursorPath) {
		double theta = cursorPath.getCursorPathTheta();
		CursorPoint endingCursorPoint = cursorPath.getEndingCursorPoint();
		double actualTheta = Math.atan2(endingCursorPoint.y, endingCursorPoint.x);
		assertEquals(theta, actualTheta);
	}
	/*private void testCursorPathLocations(CursorPath cursorPath) {
	
	}*/
	
	void testCursorPathRotation(CursorPath cursorPath, double angleToRotateBy) {
		ArrayList<CursorPoint> cursorPoints = cursorPath.getCursorPathPoints();
		ArrayList<CursorPoint> rotatedCursorPoints = cursorPath.getRotatedCopyOfCursorPath(angleToRotateBy);
		assertEquals(cursorPoints.size(), rotatedCursorPoints.size());
		for (int i = 1; i < cursorPoints.size(); i++) {
			double originalThetaOfCursorPoint = cursorPoints.get(i).getTheta();
			double rotatedThetaOfCursorPoint = rotatedCursorPoints.get(i).getTheta();
			System.out.println((originalThetaOfCursorPoint + angleToRotateBy) % (Math.PI) + "," + rotatedThetaOfCursorPoint);
			//assertInRangeByAbsoluteValue(originalThetaOfCursorPoint + angleToRotateBy, rotatedThetaOfCursorPoint, 3);
		}
	}
	
	void testCursorPathScaling(CursorPath cursorPath, double factorToScaleBy) {
		ArrayList<CursorPoint> cursorPoints = cursorPath.getCursorPathPoints();
		ArrayList<CursorPoint> scaledCursorPoints = cursorPath.getScaledCopyOfCursorPath(factorToScaleBy);
		assertEquals(cursorPoints.size(), scaledCursorPoints.size());
		CursorPoint startingPoint = cursorPoints.get(0);
		for (int i = 0; i < cursorPoints.size(); i++) {
			double originalDistanceOfCursorPoint = cursorPoints.get(i).getDistanceFrom(startingPoint);
			double scaledDistanceOfCursorPoint = scaledCursorPoints.get(i).getDistanceFrom(startingPoint);
			assertInRangeByAbsoluteValue(originalDistanceOfCursorPoint * factorToScaleBy, scaledDistanceOfCursorPoint, 3);
		}
	}
	
	void initializeCursorPath() {
		CursorDataFileParser cursorDataFileParser = new CursorDataFileParser("/home/dpapp/eclipse-workspace/RunescapeAI/testfiles/cursorPathTest.txt");
		this.cursorPaths = cursorDataFileParser.getArrayListOfCursorPathsFromFile();

	}
	
	void assertInRangeByRatio(double expected, double actual, double toleranceRatio) {
		assertTrue((actual <= (expected * (1.0 + toleranceRatio))) && (actual >= (expected * (1.0 - toleranceRatio))));
	}
	
	void assertInRangeByAbsoluteValue(double valueToTest, double expectation, double toleranceAbsolute) {
		assertTrue((valueToTest <= (expectation + toleranceAbsolute)) && (valueToTest >= (expectation * (1 - toleranceAbsolute))));
	}
}
