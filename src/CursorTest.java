import static org.junit.jupiter.api.Assertions.*;

import java.awt.AWTException;
import java.awt.Point;

import org.junit.jupiter.api.Test;

class CursorTest {
	
	Cursor cursor;
	double cursorTolerance;
	
	void initialize() throws AWTException {
		cursor = new Cursor();
		cursorTolerance = 5;
	}
	
	@Test
	void testMoveCursorToCoordinatesHelper() throws Exception {
		initialize();
		
		testThetaBetweenPoints();
		testFindNearestPathLengthThatExists();
		testMoveCursorToCoordinates();
		//testRightClickCursor();
	}
	
	void testFindNearestPathLengthThatExists() throws Exception {
		int closestLengthForPreviousValue = 0;
		for (int i = 0; i < 2203; i++) {
			int closestLength = cursor.findNearestPathLengthThatExists(i);
			assertTrue(closestLength >= closestLengthForPreviousValue);
			closestLengthForPreviousValue = closestLength;
		}
	}
	
	void testThetaBetweenPoints() {
		Point a = new Point(0, 0);
		Point b = new Point(10, 0);
		Point c = new Point(10, 10);
		Point d = new Point(20, 10);
		assertEquals(degreesToRadians(90), cursor.getThetaBetweenPoints(a, b));
		assertEquals(degreesToRadians(45), cursor.getThetaBetweenPoints(a, c));
		assertEquals(1.1071487177940904, cursor.getThetaBetweenPoints(a, d));
		assertEquals(degreesToRadians(45), cursor.getThetaBetweenPoints(b, d));
	}
	
	void testMoveCursorToCoordinates() throws Exception {
		Point a = new Point(0, 0);
		Point b = new Point(150, 250);
		Point c = new Point(375, 190);
		Point d = new Point(375, 600);
		Point e = new Point(952, 603);
		Point f = new Point(1025, 133);
		Point g = new Point(543, 582);
		Point h = new Point(1643, 582);
		Point i = new Point(0, 582);
		Point j = new Point(954, 123);
		Point k = new Point(954, 960);
		Point l = new Point(384, 654);
		Point m = new Point(84, 43);
		Point n = new Point(84, 43);
		Point o = new Point(12, 842);
		Point p = new Point(1600, 514);
		Point q = new Point(1599, 513);
		Point r = new Point(1600, 515);
		Point s = new Point(1602, 513);
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
		testMoveCursorToCoordinates(a, g);
		testMoveCursorToCoordinates(g, h);
		testMoveCursorToCoordinates(h, i);
		testMoveCursorToCoordinates(i, j);
		testMoveCursorToCoordinates(j, k);
		testMoveCursorToCoordinates(k, l);
		testMoveCursorToCoordinates(l, m);
		testMoveCursorToCoordinates(m, n);
		testMoveCursorToCoordinates(n, o);
		testMoveCursorToCoordinates(o, p);
		testMoveCursorToCoordinates(p, q);
		testMoveCursorToCoordinates(q, r);
		testMoveCursorToCoordinates(r, s);
		testMoveCursorToCoordinates(s, a);
	}
	
	/*void testRightClickCursor() throws InterruptedException {
		Point a = new Point(375, 600);
		Point b = new Point(952, 603);
		Point c = new Point(1025, 133);
		Point d = new Point(543, 582);
		testMoveAndRightClickCursor(a, b);
		testMoveAndRightClickCursor(b, c);
		testMoveAndRightClickCursor(c, d);
		testMoveAndRightClickCursor(d, a);
	}*/
	
	/*void testMoveAndRightClickCursor(Point a, Point b) throws InterruptedException {
		cursor.robotMouseMove(a);
		cursor.moveAndRightClickAtCoordinates(b);
		Point point = cursor.getCurrentCursorPoint();
		System.out.println("Cursor ended up on " + point.x + "," + point.y);
		verifyCursorIsInCorrectPlace(point, b);
		// Way to verify that context menu is open?
	}*/
	
	void testMoveCursorToCoordinates(Point a, Point b) throws Exception {
		cursor.robotMouseMove(a);
		cursor.moveCursorToCoordinates(b);
		Point point = cursor.getCurrentCursorPoint();
		//System.out.println("Cursor ended up on " + point.x + "," + point.y);
		verifyCursorIsInCorrectPlace(point, b);
		Thread.sleep(250);
	}
	
	void verifyCursorIsInCorrectPlace(Point actualPoint, Point expectedPoint) {
		assertEquals(actualPoint.x, expectedPoint.x, cursorTolerance);
		assertEquals(actualPoint.y, expectedPoint.y, cursorTolerance);
	}
	
	
	private double degreesToRadians(double degrees) {
		return degrees * Math.PI / 180.0;
	}

}
 