import java.awt.Point;

public class MousePoint {
	
	private int x;
	private int y;
	private int time;
	
	public MousePoint(int _x, int _y, int _time)
	{
		x = _x;
		y = _y;
		time = _time;
	}
	
	public int getX() 
	{
		return x;
	}
	
	public int getY()
	{
		return y;
	}
	
	public int getTime()
	{
		return time;
	}
	
	public boolean isSameLocation(MousePoint p2) {
		return (this.x == p2.getX() && this.y == p2.getY());
	}
	
	public double distance(MousePoint p2) {
		return Math.hypot(this.x - p2.getX(), this.y - p2.getY());
	}
	
	public double distance(Point p2) {
		return Math.hypot(this.x - p2.getX(), this.y - p2.getY());
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (obj == null) {
	        return false;
	    }
	    if (!(obj instanceof MousePoint)) {
            return false;
        }
         
	    MousePoint p = (MousePoint) obj;
        
       // Compare the data members and return accordingly 
       return (this.x == p.x && this.y == p.y && this.time == p.time);
	}
	
	public boolean isValid() {
		return (x >= 0 && x < 1920 && y >= 0 && y < 1080 && time >= 0);
	}
}