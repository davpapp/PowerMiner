import java.util.ArrayList;

public class MousePath {
		
	private ArrayList<Point> path;
	private int numPoints;
	private Point startingPoint;
	private Point endingPoint;
		
	public MousePath(ArrayList<Point> _path)
	{
		path = new ArrayList<Point>(_path.size());
		for (Point point : _path) {
			Point pointCopy = new Point(point.getX(), point.getY(), point.getTime());
			path.add(pointCopy);
		}
		numPoints = path.size();
		System.out.println("Created copy with size: " + path.size());
		//startingPoint = path.get(0);
		//endingPoint = path.get(numPoints - 1);
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
	}
}