import java.net.URL;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//Mouse mouse = new Mouse("/home/dpapp/GhostMouse/coordinates.txt");
		 //URL url = main.class.getClassLoader().getResource("testfiles/mouse_path_test1.txt");
		 //System.out.println(url.getPath());
		//getResource("testfiles/mouse_path_test1.txt");
		System.out.println("Starting mouse script...");
		Mouse mouse = new Mouse("/home/dpapp/GhostMouse/coordinates.txt");
		//mouse.displayPaths();
		mouse.moveMouse(743, 414);
	}
}
