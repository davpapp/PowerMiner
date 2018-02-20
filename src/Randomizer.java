import java.awt.Point;
import java.util.Random;
//import Jama.Matrix;

public class Randomizer {

	Random random;
	
	public Randomizer() {
		random = new Random();
	}
	
	public int nextGaussianWithinRange(double rangeBegin, double rangeEnd) {
		double rangeMean = (rangeEnd + rangeBegin) / 2.0;
		double rangeSTD = (rangeEnd - rangeMean) / 3.0;
		double result = random.nextGaussian() * rangeSTD + rangeMean;
		while (result > rangeEnd || result < rangeBegin) {
			System.out.println("Gaussian result out of range...");
			System.out.println(rangeMean + ", std: " + rangeSTD);
			result = random.nextGaussian() * rangeSTD + rangeMean;
		}
		return (int) result;
	}
	public Point generatePeakForTransformationParabola(int pathDistance) {
		double maxTransformationScale = 0.2;
		int peakX = nextGaussianWithinRange(0, pathDistance);
		int peakY = nextGaussianWithinRange(0, pathDistance * maxTransformationScale);
		return new Point(peakX, peakY);
	}
	
	/*public double[] generateParabolaEquation(int pathDistance) {
		Point peakPoint = generatePeakForTransformationParabola(pathDistance);
		double[][] lhsMatrix = {{0, 0, 1}, {peakPoint.x * peakPoint.x, peakPoint.x, 1}, {pathDistance * pathDistance, pathDistance, 1}};
		double[][] rhsMatrix = {{0, peakPoint.y, 0}};
		
		Matrix lhs = new Matrix(lhsMatrix);
		Matrix rhs = new Matrix(rhsMatrix);
		Matrix ans = lhs.solve(rhs);
		
		double[] result = {ans.get(0, 0), ans.get(1, 0), ans.get(2, 0)};
		return result;
	}*/
	
	//public Point transformPoint
}
