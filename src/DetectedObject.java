import java.awt.Point;
import java.awt.Rectangle;

import org.opencv.core.Rect2d;



public class DetectedObject {
	
	private Rectangle boundingBox;
	private float detectionScore;
	private String detectionClass;
	
	public DetectedObject(float detectionScore, float detectionClass, float[] detectionBox) {
		this.boundingBox = initializeBoundingBox(detectionBox);
		this.detectionScore = detectionScore;
		this.detectionClass = initializeLabel(detectionClass);
	}
	
	private Rectangle initializeBoundingBox(float[] detectionBox) {
		int offset_x = (int) (detectionBox[1] * Constants.GAME_WINDOW_WIDTH);
		int offset_y = (int) (detectionBox[0] * Constants.GAME_WINDOW_HEIGHT);
		int width = (int) (detectionBox[3] * Constants.GAME_WINDOW_WIDTH) - offset_x;
		int height = (int) (detectionBox[2] * Constants.GAME_WINDOW_HEIGHT) - offset_y;
		
		//System.out.println(detectionBox[0] + ", " + detectionBox[1] + ", " + detectionBox[2] + ", " + detectionBox[3]);
		return new Rectangle(offset_x, offset_y, width, height);
	}
	
	private String initializeLabel(float detectionClass) {
		// TODO: actually load these from a file
		String[] labels = {"NA", "ironOre", "ore"};
		return labels[(int) detectionClass];
	}
	
	public String getDetectionClass() {
		return detectionClass;
	}
	
	public Rectangle getBoundingRectangle() {
		return boundingBox;
	}
	
	public Rect2d getBoundingRect2d() {
		return new Rect2d(boundingBox.x, boundingBox.y, boundingBox.x + boundingBox.width, boundingBox.y + boundingBox.height);
	}
	
	public Point getCenterForClicking() {
		return new Point(boundingBox.x + boundingBox.width / 2 + Constants.GAME_WINDOW_OFFSET_X, boundingBox.y + boundingBox.height / 2 + Constants.GAME_WINDOW_OFFSET_Y);
	}
	
	public void display() {
		System.out.println(detectionClass + " with score " + detectionScore + " at (" + getCenterForClicking().x + "," + getCenterForClicking().y + ")");
	}
}
