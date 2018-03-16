import java.awt.Point;
import java.awt.geom.Point2D;

import Jama.Matrix;

public class PathTransformer {
	
	private static Point2D generatePeakForTransformationParabola(int pathDistance) {
		double maxTransformationScale = 0.20;
		double peakX = Randomizer.nextGaussianDoubleWithinRange(pathDistance / 3, 2 * pathDistance / 3);
		double peakY = Randomizer.nextGaussianDoubleWithinRange(1 - maxTransformationScale, 1 + maxTransformationScale);
		return new Point2D.Double(peakX, peakY);
	}
	
	public static double[] generateParabolaEquation(int pathDistance) {
		Point2D p1 = new Point2D.Double(0, 1);
		Point2D p2 = generatePeakForTransformationParabola(pathDistance);
		Point2D p3 = new Point2D.Double(pathDistance, 1);
		return generateEquationForTransformationParabola(p1, p2, p3);
	}
	
	private static double[] generateEquationForTransformationParabola(Point2D p1, Point2D p2, Point2D p3) {
		if (p1.getX() == p2.getX() || p1.getX() == p3.getX() || p2.getX() == p3.getX()) {
			double[] equation = {0.0, 0.0, 1.0};
			return equation;
		}
		double[][] lhsArray = {{0, 0, 1}, {p2.getX() * p2.getX(), p2.getX(), 1}, {p3.getX() * p3.getX(), p3.getX(), 1}};
		double[] rhsArray = {p1.getY(), p2.getY(), p3.getY()};
		Matrix lhs = new Matrix(lhsArray);
        Matrix rhs = new Matrix(rhsArray, 3);
        
        //System.out.println("(" + p1.getX() + "," + p1.getY() + "), (" + p2.getX() + "," + p2.getY() + "), (" + p3.getX() + "," + p3.getY() + ")");
        
        Matrix ans = lhs.solve(rhs);
        /*System.out.println("x = " + ans.get(0, 0));
        System.out.println("y = " + ans.get(1, 0));
        System.out.println("z = " + ans.get(2, 0));*/
        
        double[] equation = {ans.get(0, 0), ans.get(1, 0), ans.get(2, 0)};
        return equation;
	}
	
	public static double getParabolaHeightAtPoint(double[] parabolaEquation, double x) {
		return (parabolaEquation[0] * x * x + parabolaEquation[1] * x + parabolaEquation[2]);
	}
	
	public static void main(String[] args) {
		Point2D p1 = new Point2D.Double(0, 1);
		Point2D p3 = new Point2D.Double(200, 1);
		Point2D p2 = generatePeakForTransformationParabola(200);
		generateEquationForTransformationParabola(p1, p2, p3);
	}
}
