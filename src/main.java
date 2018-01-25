import java.net.URL;

public class main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		System.out.println("Starting mouse script...");
		System.out.println("Fetching mouse paths from script...");
		Mouse mouse = new Mouse("/home/dpapp/GhostMouse/coordinates.txt");
		mouse.displayPaths();
		System.out.println("Finished...");
	}
}
