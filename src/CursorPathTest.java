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
			//testCursorPathLocations(cursorPath);
			testCursorPathRotation(cursorPath, 15);
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
	
	/*private void testCursorPathLocations(CursorPath cursorPath) {
	
	}*/
	
	void testCursorPathRotation(CursorPath cursorPath, double angleToRotateBy) {
		ArrayList<CursorPoint> cursorPoints = cursorPath.getCursorPathPoints();
		ArrayList<CursorPoint> rotatedCursorPoints = cursorPath.getRotatedCopyOfCursorPath(angleToRotateBy);
		assertEquals(cursorPoints.size(), rotatedCursorPoints.size());
		CursorPoint startingPoint = cursorPoints.get(0);
		for (int i = 1; i < cursorPoints.size(); i++) {
			double originalThetaOfCursorPoint = cursorPoints.get(i).getThetaFrom(startingPoint);
			double rotatedThetaOfCursorPoint = rotatedCursorPoints.get(i).getThetaFrom(startingPoint);
			System.out.println(originalThetaOfCursorPoint + ", " + rotatedThetaOfCursorPoint);
			assertInRangeByAbsoluteValue(originalThetaOfCursorPoint + angleToRotateBy, rotatedThetaOfCursorPoint, 3);
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
	
	void assertInRangeByRatio(double valueToTest, double expectation, double toleranceRatio) {
		assertTrue((valueToTest <= (expectation * (1 + toleranceRatio))) && (valueToTest >= (expectation * (1 - toleranceRatio))));
	}
	
	void assertInRangeByAbsoluteValue(double valueToTest, double expectation, double toleranceAbsolute) {
		assertTrue((valueToTest <= (expectation + toleranceAbsolute)) && (valueToTest >= (expectation * (1 - toleranceAbsolute))));
	}
}
