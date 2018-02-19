import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.awt.List;
import java.io.File;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.DictValue;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Layer;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ObjectDetector {
	
	String inputImagePath;
	String inputModelPath;
	String inputModelArgumentsPath;
	
	Net net;
	
	public ObjectDetector() throws Exception {
		this.inputImagePath = "/home/dpapp/tensorflow-1.5.0/models/raccoon_dataset/test_images/ironOre_test_9.jpg";
		this.inputModelPath = "/home/dpapp/tensorflow-1.5.0/models/raccoon_dataset/results/checkpoint_23826/frozen_graph_inference.pb";
		this.inputModelArgumentsPath = "/home/dpapp/tensorflow-1.5.0/models/raccoon_dataset/generated_graph.pbtxt";
		
		File f = new File(inputImagePath);
		if(!f.exists()) throw new Exception("Test image is missing: " + inputImagePath);
		File f1 = new File(inputModelPath);
		if(!f1.exists()) throw new Exception("Test image is missing: " + inputModelPath);
		File f2 = new File(inputModelArgumentsPath);
		if(!f2.exists()) throw new Exception("Test image is missing: " + inputModelArgumentsPath);
		
		net = Dnn.readNetFromTensorflow(inputModelPath, inputModelArgumentsPath);
	}
	
	public void testGetLayerTypes() {
        ArrayList<String> layertypes = new ArrayList();
        net.getLayerTypes(layertypes);

        assertFalse("No layer types returned!", layertypes.isEmpty());
    }

    public void testGetLayer() {
        ArrayList<String> layernames = (ArrayList<String>) net.getLayerNames();

        assertFalse("Test net returned no layers!", layernames.isEmpty());

        String testLayerName = layernames.get(0);

        DictValue layerId = new DictValue(testLayerName);

        assertEquals("DictValue did not return the string, which was used in constructor!", testLayerName, layerId.getStringValue());

        Layer layer = net.getLayer(layerId);

        assertEquals("Layer name does not match the expected value!", testLayerName, layer.get_name());
    }
	
	public void testImage() throws Exception {
		Mat rawImage = Imgcodecs.imread(inputImagePath);
		Mat grayImage = new Mat();
		Imgproc.cvtColor(rawImage, grayImage, Imgproc.COLOR_RGB2GRAY);
		assertNotNull("Loading image from file failed!", rawImage);
		
		
		Mat image = new Mat();
		
        Imgproc.resize(grayImage, image, new Size(224, 224));

        Mat inputBlob = Dnn.blobFromImage(image);
        
        assertNotNull("Converting image to blob failed!", inputBlob);

        Mat inputBlobP = new Mat();
        
        Core.subtract(inputBlob, new Scalar(117.0), inputBlobP);
        
  
        net.setInput(inputBlobP);

        Mat result = net.forward();

        assertNotNull("Net returned no result!", result);
	}

	
   public static void main( String[] args ) throws Exception {
      System.out.println("Reading model from TensorFlow...");
      
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
      
      ObjectDetector objectDetector = new ObjectDetector();
      
      
      objectDetector.testGetLayerTypes();
      objectDetector.testGetLayer();
      objectDetector.testImage();
      
      System.out.println("Done...");
   }
}