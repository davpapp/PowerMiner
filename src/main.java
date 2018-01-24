import java.net.URL;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Mouse mouse = new Mouse("/home/dpapp/GhostMouse/coordinates.txt");
		 //URL url = main.class.getClassLoader().getResource("testfiles/mouse_path_test1.txt");
		 //System.out.println(url.getPath());
		//getResource("testfiles/mouse_path_test1.txt");
		Mouse mouse = new Mouse("/home/dpapp/eclipse-workspace/RunescapeAI/testfiles/mouse_path_test1.txt");
		mouse.displayPaths();
	}
}
