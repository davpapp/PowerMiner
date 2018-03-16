import java.awt.Point;
import java.util.Random;
//import Jama.Matrix;

public class Randomizer {
	
	public static int nextGaussianWithinRange(double rangeBegin, double rangeEnd) {
		return (int) nextGaussianDoubleWithinRange(rangeBegin, rangeEnd);
	}
	
	public static double nextGaussianDoubleWithinRange(double rangeBegin, double rangeEnd) {
		Random random = new Random();
		double rangeMean = (rangeEnd + rangeBegin) / 2.0;
		double rangeSTD = (rangeEnd - rangeMean) / 3.0;
		
		double result = random.nextGaussian() * rangeSTD + rangeMean;
		while (result > rangeEnd || result < rangeBegin) {
			result = random.nextGaussian() * rangeSTD + rangeMean;
		}
		return result;
	}
}
