import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

class MousePathTest {
	
	Mouse mouse = new Mouse("/home/dpapp/eclipse-workspace/RunescapeAI/testfiles/mouse_path_test1.txt");
	ArrayList<MousePath> mousePaths = mouse.getMousePaths();
	
	@Test
	void mousePathLengthTest() {
		assertEquals(mousePaths.get(0).getNumPoints(), 45);
		assertEquals(mousePaths.get(1).getNumPoints(), 17);
		assertEquals(mousePaths.get(2).getNumPoints(), 33);
		assertEquals(mousePaths.get(3).getNumPoints(), 14);
		assertEquals(mousePaths.get(4).getNumPoints(), 13);
	}
	
	@Test
	void mousePathTimespanTest() {
		assertEquals(mousePaths.get(0).getTimespan(), 1225);
		assertEquals(mousePaths.get(1).getTimespan(), 192);
		assertEquals(mousePaths.get(2).getTimespan(), 458);
		assertEquals(mousePaths.get(3).getTimespan(), 157);
		assertEquals(mousePaths.get(4).getTimespan(), 142);
	}
	
	@Test
	void mousePathStartingPointTest() {
		
	}
	
	@Test
	void mousePathEndingPointTest() {
		
	}

}
