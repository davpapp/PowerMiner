public class CursorPoint {
	
	public int x;
	public int y;
	public int postMillisecondDelay; // how much to sleep for after moving here
	
	public CursorPoint(int x, int y, int postMillisecondDelay) {
		this.x = x;
		this.y = y;
		this.postMillisecondDelay = postMillisecondDelay;
	}
	
	public void setPostMillisecondDelay(int postMillisecondDelay) {
		this.postMillisecondDelay = postMillisecondDelay;
	}
	
	public void display() {
		System.out.println("Point: (" + x + "," + y + "), delay: " + postMillisecondDelay);
	}
}