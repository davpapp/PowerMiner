/*
 * Represents each mouse path as an ArrayList of points.
 */

import java.util.ArrayList;

public class MousePath {
		
	private ArrayList<MousePoint> path;
	private int numPoints;
	private MousePoint startingPoint;
	private MousePoint endingPoint;
	private int timespan;
		
	public MousePath(ArrayList<MousePoint> _path)
	{
		path = new ArrayList<MousePoint>(_path.size());
		for (MousePoint point : _path) {
			MousePoint pointCopy = new MousePoint(point.getX(), point.getY(), point.getTime());
			path.add(pointCopy);
		}
		numPoints = path.size();
		startingPoint = path.get(0);
		endingPoint = path.get(numPoints - 1);
		timespan = endingPoint.getTime() - startingPoint.getTime();
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
	
	public MousePoint getStartingPoint() {
		return startingPoint;
	}
	
	public MousePoint getEndingPoint() {
		return endingPoint;
	}
	
	public void display() {
		for (MousePoint p : path) {
			System.out.println("(" + p.getX() + ", " + p.getY() + "), " + p.getTime());
		}
		System.out.println("Length:" + numPoints + ", Timespan:" + timespan);
	}
}