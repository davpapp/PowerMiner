import java.awt.Point;
import java.util.Random;
import Jama.Matrix;

public class Randomizer {

	Random random;
	
	public Randomizer() {
		random = new Random();
	}
	
	public int nextGaussianWithinThreeSTDs(int mean, int STD) {
		int result = (int) (random.nextGaussian() * STD + mean);
		while (result > (mean + 3 * STD) || result < (mean - 3 * STD)) {
			result = (int) random.nextGaussian() * STD + mean;
		}
		return result;
	}
	
	public Point generatePeakForTransformationParabola(int pathDistance) {
		double maxTransformationScale = 0.2;
		int peakX = nextGaussianWithinThreeSTDs(pathDistance / 2, pathDistance / 6);
		int peakY = (int) (random.nextGaussian() * maxTransformationScale);
		return new Point(peakX, peakY);
	}
	
	public double[] generateParabolaEquation(int pathDistance, Point peakPoint) {
		double[][] lhsMatrix = {{0, 0, 1}, {peakPoint.x * peakPoint.x, peakPoint.x, 1}, {pathDistance * pathDistance, pathDistance, 1}};
		double[][] rhsMatrix = {{0, peakPoint.y, 0}};
		
		Matrix lhs = new Matrix(lhsMatrix);
		Matrix rhs = new Matrix(rhsMatrix);
		Matrix ans = lhs.solve(rhs);
		
		double[] result = {ans.get(0, 0), ans.get(1, 0), ans.get(2, 0)};
		return result;
	}
	
	//public Point transformPoint
}
