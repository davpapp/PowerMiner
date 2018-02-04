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
	
	public double getDistanceFrom(CursorPoint b) {
		return Math.hypot(this.x - b.x, this.y - b.y);
	}
	
	public double getThetaFrom(CursorPoint b) {
		return Math.atan2(1.0 * b.y, 1.0 * b.x);
	}
	
	public CursorPoint getCursorPointTranslatedBy(CursorPoint startingCursorPoint) {
		return new CursorPoint(this.x - startingCursorPoint.x, this.y - startingCursorPoint.y, this.delay - startingCursorPoint.delay);
	}
	
	public CursorPoint getCursorPointScaledBy(double scaleFactor) {
		return (new CursorPoint((int) (this.x * scaleFactor), (int) (this.y * scaleFactor), (int) (this.delay * scaleFactor)));
	}
	
	public CursorPoint getCursorPointRotatedBy(double angleOfRotation) {
		int rotatedX = (int) (this.x + Math.cos(angleOfRotation) * this.x - Math.sin(angleOfRotation) * this.y);
		int rotatedY = (int) (this.y + Math.sin(angleOfRotation) * this.x + Math.cos(angleOfRotation) * this.y);
		return (new CursorPoint(rotatedX, rotatedY, this.delay));
	}
	
	public CursorPoint getCursorPointWithNewDelay(int delay) {
		return (new CursorPoint(this.x, this.y, delay));
	}
	
	public void display() {
		System.out.println("Point: (" + x + "," + y + "), delay: " + delay);
	}
}