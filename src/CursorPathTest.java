import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class CursorPathTest {

	ArrayList<CursorPath> cursorPaths;
	
	
	@Test
	void testCursorPaths() {
		initializeCursorPath();
		
		for (CursorPath cursorPath : cursorPaths) {
			//cursorPath.displayCursorPoints();
			
			testCursorPathStartsAtOrigin(cursorPath);
			testEndingCursorPointInCursorPathHasZeroDelay(cursorPath);
			testCursorPathDistance(cursorPath);
			testCursorPathTheta(cursorPath);
			
			testCursorPathRotation(cursorPath, Math.PI);
			testCursorPathRotation(cursorPath, 7 / 4 * Math.PI);
			//testCursorPathRotation(cursorPath, 10.32184);
			//testCursorPathRotation(cursorPath, 0.01372);
			//testCursorPathRotation(cursorPath, -0.0001238);
			//testCursorPathRotation(cursorPath, 0);
			testCursorPathScaling(cursorPath, 1.15);
			testCursorPathScaling(cursorPath, 0.48324);
			testCursorPathScaling(cursorPath, 0.9999);
			testCursorPathScaling(cursorPath, 1.8431838);
			testCursorPathScaling(cursorPath, 2.10004);
			testCursorPathScaling(cursorPath, 1.15);
			testCursorPathScaling(cursorPath, 1.001010101);
			testCursorPathScaling(cursorPath, 1.523521);
			testCursorPathScaling(cursorPath, 1.12366);
			testCursorPathScaling(cursorPath, 0.974324);
			testCursorPathScaling(cursorPath, 0.72134);
			
			//testDelays(cursorPath);
		}
	}
	
	private void initializeCursorPath() {
		CursorDataFileParser cursorDataFileParser = new CursorDataFileParser("/home/dpapp/eclipse-workspace/RunescapeAI/testfiles/cursorPathTest.txt");
		this.cursorPaths = cursorDataFileParser.getArrayListOfCursorPathsFromFile();
	}
	
	private void testCursorPathStartsAtOrigin(CursorPath cursorPath) {
		CursorPoint startingCursorPoint = cursorPath.getStartingCursorPoint();
		assertTrue(startingCursorPoint.x == 0 && startingCursorPoint.y == 0);
	}
	
	private void testEndingCursorPointInCursorPathHasZeroDelay(CursorPath cursorPath) {
		CursorPoint endingCursorPoint = cursorPath.getEndingCursorPoint();
		assertEquals(0, endingCursorPoint.delay);
	}
	
	private void testCursorPathDistance(CursorPath cursorPath) {
		int cursorPathDistance = cursorPath.getCursorPathDistance();
		CursorPoint startingCursorPoint = cursorPath.getStartingCursorPoint();
		CursorPoint endingCursorPoint = cursorPath.getEndingCursorPoint();
		
		startingCursorPoint.display();
		endingCursorPoint.display();
		int expectedDistance = (int) Math.hypot(startingCursorPoint.x - endingCursorPoint.x, startingCursorPoint.y - endingCursorPoint.y);
		assertEquals(expectedDistance, cursorPathDistance);
	}
	
	private void testCursorPathTheta(CursorPath cursorPath) {
		double theta = cursorPath.getCursorPathTheta();
		CursorPoint startingCursorPoint = cursorPath.getStartingCursorPoint();
		CursorPoint endingCursorPoint = cursorPath.getEndingCursorPoint();
		double actualTheta = Math.atan2(endingCursorPoint.x - startingCursorPoint.x, endingCursorPoint.y - startingCursorPoint.y);
		assertEquals(theta, actualTheta);
	}
	
	
	void testCursorPathRotation(CursorPath cursorPath, double angleToRotateTo) {
		CursorPath rotatedCursorPath = cursorPath.getRotatedCopyOfCursorPath(angleToRotateTo);
		assertEquals(angleToRotateTo % Math.PI, ((rotatedCursorPath.getCursorPathTheta() % Math.PI) + Math.PI) % Math.PI, 0.01);
		
		ArrayList<CursorPoint> cursorPoints = cursorPath.getCursorPathPoints();
		ArrayList<CursorPoint> rotatedCursorPoints = rotatedCursorPath.getCursorPathPoints();
		assertEquals(cursorPoints.size(), rotatedCursorPoints.size());
		
		assertEquals(cursorPath.getStartingCursorPoint().x, rotatedCursorPath.getStartingCursorPoint().x);
		assertEquals(cursorPath.getStartingCursorPoint().y, rotatedCursorPath.getStartingCursorPoint().y);

	}
	
	void testDelays(CursorPath cursorPath) {
		CursorPath rotatedCursorPath = cursorPath.getRotatedCopyOfCursorPath(2.3 / 9.0 * Math.PI);
		ArrayList<CursorPoint> cursorPoints = cursorPath.getCursorPathPoints();
		ArrayList<CursorPoint> rotatedCursorPoints = rotatedCursorPath.getCursorPathPoints();
		assertEquals(cursorPoints.size(), rotatedCursorPoints.size());
		for (int i = 0; i < cursorPoints.size(); i++) {
			assertEquals(cursorPoints.get(i).delay, rotatedCursorPoints.get(i).delay);
		}
	}

	void testCursorPathScaling(CursorPath cursorPath, double factorToScaleBy) {
		CursorPath scaledCursorPath = cursorPath.getScaledCopyOfCursorPath(factorToScaleBy);

		ArrayList<CursorPoint> cursorPoints = cursorPath.getCursorPathPoints();
		ArrayList<CursorPoint> scaledCursorPoints = scaledCursorPath.getCursorPathPoints();
		assertEquals(cursorPoints.size(), scaledCursorPoints.size());
		
		assertEquals(cursorPath.getStartingCursorPoint().x, scaledCursorPath.getStartingCursorPoint().x);
		assertEquals(cursorPath.getStartingCursorPoint().y, scaledCursorPath.getStartingCursorPoint().y);
		assertEquals(cursorPath.getCursorPathDistance() * factorToScaleBy, scaledCursorPath.getCursorPathDistance(), 3);
	}
}
