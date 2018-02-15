import java.io.File;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;

public class ObjectDetector {
	
	String inputImagePath;
	String inputModelPath;
	
	public ObjectDetector() {
		this.inputImagePath = "/home/dpapp/tensorflow-1.5.0/models/raccoon-dataset/test_images/ironOre_test_9.jpg";
		this.inputModelPath = "/home/dpapp/eclipse-workspace/RunescapeAI/TensorFlow/frozen_graph.pb";
		String inputModelArgumentsPath = "/home/dpapp/eclipse-workspace/RunescapeAI/TensorFlow/ssd_mobilenet_v1_coco.pbtxt";

		File  inputModelFile = new File(inputModelPath);
		String correctFilePath = inputModelFile.toString();
		File inputFile = new File(inputModelArgumentsPath);
		System.out.println(inputModelFile.toString());
		System.out.println(inputFile.toString());
		Net net = Dnn.readNetFromTensorflow(correctFilePath, inputModelArgumentsPath);
	}
	
	public void setUp() {
		//String
	}
	
   public static void main( String[] args ) {
      System.out.println("Reading model from TensorFlow...");
      
      
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
      //Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
      //System.out.println("mat = " + mat.dump());
      
      ObjectDetector objectDetector = new ObjectDetector();
      
      System.out.println("Done...");
   }
}