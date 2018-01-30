import static org.junit.jupiter.api.Assertions.*;

import java.awt.AWTException;
import java.awt.Point;

import org.junit.jupiter.api.Test;

class CursorTest {
	
	Cursor cursor;
	double cursorTolerance;
	
	void initialize() throws AWTException {
		cursor = new Cursor();
		cursorTolerance = 3;
	}
	
	@Test
	void testMoveCursorToCoordinatesHelper() throws InterruptedException, AWTException {
		initialize();
		Point a = new Point(0, 0);
		Point b = new Point(150, 250);
		Point c = new Point(375, 190);
		Point d = new Point(375, 600);
		Point e = new Point(952, 603);
		Point f = new Point(1025, 133);
		Point g = new Point(543, 582);
		testMoveCursorToCoordinates(a, b);
		testMoveCursorToCoordinates(b, c);
		testMoveCursorToCoordinates(c, d);
		testMoveCursorToCoordinates(d, e);
		testMoveCursorToCoordinates(e, f);
		testMoveCursorToCoordinates(f, g);
		testMoveCursorToCoordinates(g, c);
		testMoveCursorToCoordinates(c, f);
		testMoveCursorToCoordinates(f, b);
		testMoveCursorToCoordinates(b, a);
	}
	
	void testMoveCursorToCoordinates(Point a, Point b) throws InterruptedException {
		cursor.robotMouseMove(a);
		cursor.moveCursorToCoordinates(b);
		Point point = cursor.getCurrentCursorPoint();
		verifyCursorIsInCorrectPlace(point, b);
	}
	
	void verifyCursorIsInCorrectPlace(Point actualPoint, Point expectedPoint) {
		assertInRangeByAbsoluteValue(actualPoint.x, expectedPoint.x, cursorTolerance);
		assertInRangeByAbsoluteValue(actualPoint.y, expectedPoint.y, cursorTolerance);
	}
	
	void assertInRangeByPercentage(double valueToTest, double expectation, double tolerancePercentage) {
		System.out.println(valueToTest + " expected: " + expectation);
		assertTrue((valueToTest <= (expectation * (1 + tolerancePercentage))) && (valueToTest >= (expectation * (1 - tolerancePercentage))));
	}
	
	void assertInRangeByAbsoluteValue(double valueToTest, double expectation, double toleranceAbsolute) {
		System.out.println(valueToTest + " expected: " + expectation);
		assertTrue((valueToTest <= (expectation + toleranceAbsolute)) && (valueToTest >= (expectation * (1 - toleranceAbsolute))));
	}

}
 