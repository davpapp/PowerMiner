import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class PointTest {

	@Test
	void initializePointTest() {
		MousePoint p = new MousePoint(54, 4, 134);
		assertEquals(p.getX(), 54);
		assertEquals(p.getY(), 4);
		assertEquals(p.getTime(), 134);
	}
	
	@Test
	void isValidPointTest() {
		MousePoint p1 = new MousePoint(54, 4, 134);
		assertEquals(p1.isValid(), true);
		
		MousePoint p2 = new MousePoint(-3, 84, 832);
		assertEquals(p2.isValid(), false);
		
		MousePoint p3 = new MousePoint(1940, 84, 832);
		assertEquals(p3.isValid(), false);
		
		MousePoint p4 = new MousePoint(3, -5, 832);
		assertEquals(p4.isValid(), false);
		
		MousePoint p5 = new MousePoint(0, 1084, 832);
		assertEquals(p5.isValid(), false);
		
		MousePoint p6 = new MousePoint(0, 1001, -4);
		assertEquals(p6.isValid(), false);
	}

	@Test
	void isSameLocationTest() {
		MousePoint p1 = new MousePoint(54, 4, 134);
		MousePoint p2 = new MousePoint(54, 4, 832);
		MousePoint p3 = new MousePoint(85, 4, 832);
		MousePoint p4 = new MousePoint(54, 12, 832);
		assertEquals(p1.isSameLocation(p2), true);
		assertEquals(p1.isSameLocation(p3), false);
		assertEquals(p1.isSameLocation(p4), false);
	}
	
	@Test
	void distanceTest() {
		MousePoint p1 = new MousePoint(54, 4, 134);
		MousePoint p2 = new MousePoint(54, 4, 832);
		MousePoint p3 = new MousePoint(85, 4, 832);
		MousePoint p4 = new MousePoint(85, 12, 832);
		
		assertEquals(p1.distance(p2), 0.0);
		assertEquals(p1.distance(p3), 31.0);
		
		double distance = p1.distance(p4) - 32.01562118;
		assertTrue(distance >= 0 && distance < 0.00001);
	}
}
