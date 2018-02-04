import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CursorPointTest {
	
	@Test
	void testDistanceFrom() {
		CursorPoint a = new CursorPoint(0, 0, 0);
		CursorPoint b = new CursorPoint(3, 4, 0);
		CursorPoint c = new CursorPoint(500, 750, 0);
		CursorPoint d = new CursorPoint(284, 848, 0);
		
		assertTrue(withinRangeByRatio(a.getDistanceFrom(b), 5, 0.0001));
		assertTrue(withinRangeByRatio(a.getDistanceFrom(c), 901.387818866, 0.0001));
		assertTrue(withinRangeByRatio(a.getDistanceFrom(d), 894.293016857, 0.0001));
		assertTrue(withinRangeByRatio(b.getDistanceFrom(c), 896.395560007, 0.0001));
		assertTrue(withinRangeByRatio(c.getDistanceFrom(d), 237.191905427, 0.0001));
	}
	
	@Test
	void testCursorPointRotation() {
		CursorPoint a = new CursorPoint(0, 0, 0);
		CursorPoint b = new CursorPoint(10, 0, 0);
		//CursorPoint c = new CursorPoint(10, 20, 0);
		CursorPoint d = a.getCursorPointRotatedBy(Math.PI / 4);
		CursorPoint e = b.getCursorPointRotatedBy(Math.PI / 2);
		//CursorPoint f = c.getCursorPointRotatedBy(90);
		
		//System.out.println(a.getThetaFrom(a));
		//System.out.println(b.getThetaFrom(a));
		assertTrue(d.x == 0 && d.y == 0);
		System.out.println(e.x + ", " + e.y);
		//assertTrue(e.x == 5);
		
		//assertTrue(withinRangeByRatio())
	}
	
	boolean withinRangeByRatio(double actual, double expectation, double toleranceRatio) {
		return ((actual <= (expectation * (1 + toleranceRatio))) && (actual >= (expectation * (1 - toleranceRatio))));
	}

}
