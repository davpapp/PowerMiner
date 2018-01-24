import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

/*
 * Things to test:
 * - Invalid regex expression
 * - Empty paths (mouse jumps)
 * - Short paths
 * - No input file
 * - Long paths
 */

class MouseTest {

	@Test
	void testMouseLengths() {
		Mouse mouse = new Mouse("/home/dpapp/eclipse-workspace/RunescapeAI/testfiles/mouse_path_test1.txt");
		assertEquals(mouse.getNumberOfPaths(), 5);
		
		ArrayList<MousePath> mousePaths = mouse.getMousePaths();
		assertEquals(mousePaths.get(0).getNumPoints(), 45);
		assertEquals(mousePaths.get(1).getNumPoints(), 17);
		assertEquals(mousePaths.get(2).getNumPoints(), 33);
		assertEquals(mousePaths.get(3).getNumPoints(), 14);
		assertEquals(mousePaths.get(4).getNumPoints(), 13);
	}
	
}
