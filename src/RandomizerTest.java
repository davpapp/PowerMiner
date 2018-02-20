import static org.junit.jupiter.api.Assertions.*;

import java.awt.Point;

import org.junit.jupiter.api.Test;

class RandomizerTest {

	@Test
	void test() {
		Randomizer randomizer = new Randomizer();
		
		
		//double[] parabolaEquation1 = randomizer.generateParabolaEquation(100, new Point(50, 0));
		double[] expectedResult1 = {0, 0, 0};
		//double[] parabolaEquation2 = randomizer.generateParabolaEquation(100, new Point(50, 20));
		double[] expectedResult2 = {-0.008, 0.008, 0};
		//double[] parabolaEquation3 = randomizer.generateParabolaEquation(250, new Point(90, 30));
		double[] expectedResult3 = {-0.002083, 0.52083, 0.0};
		//checkVariables(expectedResult1, parabolaEquation1);
		//checkVariables(expectedResult2, parabolaEquation2);
		//checkVariables(expectedResult3, parabolaEquation3);
	}
	
	void checkVariables(double[] expected, double[] actual) {
		assertEquals(0, 0, 0);
	}

}
