import static org.junit.jupiter.api.Assertions.*;

import java.awt.AWTException;
import java.awt.Point;

import org.junit.jupiter.api.Test;

class CursorTest {
	
	Cursor cursor;
	double toleranceMin;
	double toleranceMax;
	
	void initialize() throws AWTException {
		cursor = new Cursor();
		toleranceMin = 0.9999;
		toleranceMax = 1.0001;
	}
	
	@Test
	void testCalculateThetaBetweenPoints() throws AWTException {
		initialize();
		Point a = new Point(0, 0);
		Point b = new Point(10, 0);
		Point c = new Point(0, 10);
		Point d = new Point(-10, 0);
		Point e = new Point(0, -10);
		
		assertInRange(cursor.calculateThetaBetweenPoints(a, b), 0.0);
		assertInRange(cursor.calculateThetaBetweenPoints(a, c), Math.PI / 2);
		assertInRange(cursor.calculateThetaBetweenPoints(a, d), Math.PI);
		assertInRange(cursor.calculateThetaBetweenPoints(a, e), - Math.PI / 2);
	}
	
	void assertInRange(double valueToTest, double expectation) {
		System.out.println(valueToTest + " expected: " + expectation);
		assertTrue((valueToTest <= (expectation * toleranceMax)) && (valueToTest >= (expectation * toleranceMin)));
	}

}
 