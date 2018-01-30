/* Reads a file of coordinates
 */
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.PointerInfo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cursor {
	
	public static final int NUMBER_OF_DISTANCES = 600;

	private ArrayList<ArrayList<CursorPath>> cursorPathsByDistance;
	
	public Cursor() {
		ArrayList<CursorPath> cursorPaths = getArrayListOfCursorPathsFromFile("/home/dpapp/GhostMouse/coordinates.txt");// read from file or something;
		
		initializeCursorPathsByDistance();
		assignCursorPathsByDistance(cursorPaths);
	}
	
	private void initializeCursorPathsByDistance() {
		this.cursorPathsByDistance = new ArrayList<ArrayList<CursorPath>>();
		for (int i = 0; i < NUMBER_OF_DISTANCES; i++) {
			this.cursorPathsByDistance.add(new ArrayList<CursorPath>());
		}
	}
	
	private ArrayList<CursorPath> getArrayListOfCursorPathsFromFile(String path) {
		CursorDataFileParser cursorDataFileParser = new CursorDataFileParser(path);
		return cursorDataFileParser.getArrayListOfCursorPathsFromFile();
	}
	
	private void assignCursorPathsByDistance(ArrayList<CursorPath> cursorPaths) {
		for (CursorPath cursorPath : cursorPaths) {
			if (cursorPath.isCursorPathReasonable()) { 
				addCursorPathToCursorPathsByDistance(cursorPath);
			}
		}
	}
	
	private void addCursorPathToCursorPathsByDistance(CursorPath cursorPath) {
		this.cursorPathsByDistance.get(cursorPath.getCursorPathDistance()).add(cursorPath);
	}
	
	public void displaycursorPathsByDistance() {
		for (int i = 0; i < cursorPathsByDistance.size(); i++) {
			System.out.println("There are " + cursorPathsByDistance.get(i).size() + " CursorPaths of length " + i);

		}
	}
}