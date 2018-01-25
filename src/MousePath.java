/*
 * Represents each mouse path as an ArrayList of points.
 */

import java.util.ArrayList;

public class MousePath {
		
	private ArrayList<MousePoint> path;
	private int numPoints;
	private int deltaX;
	private int deltaY;
	private int timespan;
	
	private int boundUp;
	private int boundDown;
	private int boundLeft;
	private int boundRight;
		
	public MousePath(ArrayList<MousePoint> mousePoints)
	{
		path = new ArrayList<MousePoint>(mousePoints.size());
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		for (MousePoint point : mousePoints) {
			int x = point.getX();
			int y = point.getY();
			maxX = Math.max(maxX, x);
			maxY = Math.max(maxY, y);
			minX = Math.min(minX, x);
			minY = Math.min(minY, y);
			MousePoint pointCopy = new MousePoint(x, y, point.getTime());
			path.add(pointCopy);
		}
		numPoints = path.size();
		MousePoint startingPoint = path.get(0);
		boundUp = maxY - startingPoint.getY();
		boundDown = startingPoint.getY() - minY;
		boundLeft = startingPoint.getX() - minX;
		boundRight = maxX - startingPoint.getX();
		
		timespan = path.get(numPoints - 1).getTime() - startingPoint.getTime();
	}
	
	public ArrayList<MousePoint> getPath() {
		return path;
	}
	public int getNumPoints() {
		return numPoints;
	}
	public int getTimespan() {
		return timespan;
	}
	public int getDeltaX() {
		return deltaX;
	}
	public int getDeltaY() {
		return deltaY;
	}
	
	public int getBoundUp() {
		return boundUp;
	}
	public int getBoundDown() {
		return boundDown;
	}
	public int getBoundLeft() {
		return boundLeft;
	}
	public int getBoundRight() {
		return boundRight;
	}
	
	public void display() {
		for (MousePoint p : path) {
			System.out.println("(" + p.getX() + ", " + p.getY() + "), " + p.getTime());
		}
		System.out.println("Length:" + numPoints + ", Timespan:" + timespan);
	}
}