import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PointTest {

	@Test
	void initializePointTest() {
		Point p = new Point(54, 4, 134);
		assertEquals(p.getX(), 54);
		assertEquals(p.getY(), 4);
		assertEquals(p.getTime(), 134);
	}
	
	@Test
	void isValidPointTest() {
		Point p1 = new Point(54, 4, 134);
		assertEquals(p1.isValid(), true);
		
		Point p2 = new Point(-3, 84, 832);
		assertEquals(p2.isValid(), false);
		
		Point p3 = new Point(1940, 84, 832);
		assertEquals(p3.isValid(), false);
		
		Point p4 = new Point(3, -5, 832);
		assertEquals(p4.isValid(), false);
		
		Point p5 = new Point(0, 1084, 832);
		assertEquals(p5.isValid(), false);
		
		Point p6 = new Point(0, 1001, -4);
		assertEquals(p6.isValid(), false);
	}

	@Test
	void isSameLocationTest() {
		Point p1 = new Point(54, 4, 134);
		Point p2 = new Point(54, 4, 832);
		Point p3 = new Point(85, 4, 832);
		Point p4 = new Point(54, 12, 832);
		assertEquals(p1.isSameLocation(p2), true);
		assertEquals(p1.isSameLocation(p3), false);
		assertEquals(p1.isSameLocation(p4), false);
	}
	
	@Test
	void distanceTest() {
		Point p1 = new Point(54, 4, 134);
		Point p2 = new Point(54, 4, 832);
		Point p3 = new Point(85, 4, 832);
		Point p4 = new Point(85, 12, 832);
		
		assertEquals(p1.distance(p2), 0.0);
		assertEquals(p1.distance(p3), 31.0);
		
		double distance = p1.distance(p4) - 32.01562118;
		assertTrue(distance >= 0 && distance < 0.00001);
	}
}
