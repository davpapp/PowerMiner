/*
 * Represents each mouse path as an ArrayList of points.
 * 
 * Testing: 
 * 	- Each point must be valid.
 *  - Timespan is reasonable
 *  - Does not go off screen
 *  
 *  
 */

import java.util.ArrayList;

public class MousePath {
		
	private ArrayList<Point> path;
	private int numPoints;
	private Point startingPoint;
	private Point endingPoint;
	private int timespan;
		
	public MousePath(ArrayList<Point> _path)
	{
		path = new ArrayList<Point>(_path.size());
		for (Point point : _path) {
			Point pointCopy = new Point(point.getX(), point.getY(), point.getTime());
			path.add(pointCopy);
		}
		numPoints = path.size();
		startingPoint = path.get(0);
		endingPoint = path.get(numPoints - 1);
		timespan = endingPoint.getTime() - startingPoint.getTime();
	}
	
	public ArrayList<Point> getPath() {
		return path;
	}
	
	public int getNumPoints() {
		return numPoints;
	}
	
	public void display() {
		for (Point p : path) {
			System.out.println("(" + p.getX() + ", " + p.getY() + "), " + p.getTime());
		}
		System.out.println("Length:" + numPoints + ", Timespan:" + timespan);
	}
}