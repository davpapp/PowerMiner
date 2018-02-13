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
		CursorPoint c = new CursorPoint(10, 20, 0);
		CursorPoint d = a.getCursorPointRotatedBy(Math.PI / 4);
		CursorPoint e = b.getCursorPointRotatedBy(Math.PI / 3);
		CursorPoint f = c.getCursorPointRotatedBy(-Math.PI / 6);
		CursorPoint g = b.getCursorPointRotatedBy(-Math.PI / 6);
		
		assertTrue(d.x == 0 && d.y == 0);
		assertTrue(e.x == 5 && e.y == 8);
		assertTrue(f.x == 18 && f.y == 12);
		assertTrue(g.x == 8 && g.y == -4);		
	}
	
	@Test
	void testCursorPointScaling() {
		CursorPoint a = new CursorPoint(0, 0, 0);
		CursorPoint b = new CursorPoint(100, 0, 0);
		CursorPoint c = new CursorPoint(100, 200, 0);
		CursorPoint d = new CursorPoint(-400, -300, 0);
		CursorPoint e = a.getCursorPointScaledBy(1.1);
		CursorPoint f = b.getCursorPointScaledBy(0.95);
		CursorPoint g = c.getCursorPointScaledBy(1.23);
		CursorPoint h = d.getCursorPointScaledBy(1.07);
		
		assertTrue(e.x == 0 && e.y == 0);
		assertTrue(f.x == 95 && f.y == 0);
		assertTrue(g.x == 123 && g.y == 246);
		assertTrue(h.x == -428 && h.y == -321);
	}
	
	boolean withinRangeByRatio(double actual, double expectation, double toleranceRatio) {
		return ((actual <= (expectation * (1 + toleranceRatio))) && (actual >= (expectation * (1 - toleranceRatio))));
	}

}
