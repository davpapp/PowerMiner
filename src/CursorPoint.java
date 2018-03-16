import java.awt.Point;

public class CursorPoint {
	
	public int x;
	public int y;
	public int delay;
	
	public CursorPoint(int x, int y, int delay) {
		this.x = x;
		this.y = y;
		this.delay = delay;
	}
	
	public double getDistanceFromOrigin() {
		return Math.hypot(this.x, this.y);
	}
	
	public double getThetaFromOrigin() {
		return Math.atan2(this.x, this.y);
	}
	
	public CursorPoint getCursorPointTranslatedBy(CursorPoint startingCursorPoint) {
		return new CursorPoint(x - startingCursorPoint.x, y - startingCursorPoint.y, delay - startingCursorPoint.delay);
	}
	
	public CursorPoint getCursorPointScaledBy(double scaleFactor) {
		return (new CursorPoint((int) (this.x * scaleFactor), (int) (this.y * scaleFactor), (int) (delay * scaleFactor)));
	}
	
	public CursorPoint getCursorPointRotatedBy(double angleOfRotation) {
		int rotatedX = (int) (Math.cos(angleOfRotation) * this.x - Math.sin(angleOfRotation) * this.y);
		int rotatedY = (int) (Math.sin(angleOfRotation) * this.x + Math.cos(angleOfRotation) * this.y);
		return (new CursorPoint(rotatedX, rotatedY, delay));
	}
	
	public CursorPoint getCursorPointTransformedByParabola(double[] parabolaEquation) {
		double transformationFactor = PathTransformer.getParabolaHeightAtPoint(parabolaEquation, this.getDistanceFromOrigin());
		int transformedX = (int) (transformationFactor * this.x);
		int transformedY = (int) (transformationFactor * this.y);
		int transformedDelay = (int) (transformationFactor * this.delay);
		return (new CursorPoint(transformedX, transformedY, transformedDelay));
	}
	
	public CursorPoint getCursorPointWithNewDelay(int delay) {
		return (new CursorPoint(this.x, this.y, delay));
	}
	
	public void display() {
		System.out.println("Point: (" + x + "," + y + "), delay: " + delay);
	}
}