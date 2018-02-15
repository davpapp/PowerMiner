import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ObjectDetector {
	
	String inputImagePath;
	String inputModelPath;
	String inputModelArgumentsPath;
	
	Net net;
	
	public ObjectDetector() {
		this.inputImagePath = "/home/dpapp/tensorflow-1.5.0/models/raccoon_dataset/test_images/ironOre_test_9.jpg";
		this.inputModelPath = "/home/dpapp/tensorflow-1.5.0/models/raccoon_dataset/training/ssd_mobilenet_v1_coco_2017_11_17/frozen_inference_graph.pb";
		//this.inputModelPath = "/home/dpapp/tensorflow-1.5.0/models/raccoon_dataset/results/checkpoint_23826/frozen_inference_graph.pb";
		this.inputModelArgumentsPath = "/home/dpapp/tensorflow-1.5.0/models/raccoon_dataset/training/ssd_mobilenet_v1_coco_2017_11_17/ssd_mobilenet_v1_coco.pbtxt";
		//this.inputModelArgumentsPath = "/home/dpapp/eclipse-workspace/RunescapeAI/TensorFlow/object-detection.pbtxt";
		net = Dnn.readNetFromTensorflow(inputModelPath, inputModelArgumentsPath);
	}
	
	public void testImage() throws Exception {
		File f = new File(inputImagePath);
		if(!f.exists()) throw new Exception("Test image is missing: " + inputImagePath);
		
		Mat rawImage = Imgcodecs.imread(inputImagePath);
		Imgproc.cvtColor(rawImage, rawImage, Imgproc.COLOR_BGR2GRAY);
		System.out.println(rawImage.width() + "x" +rawImage.height() + ", dim: " + rawImage.dims() + ", channels: " + rawImage.channels());
        
		Mat image = new Mat();
        Imgproc.resize(rawImage, image, new Size(224,224));
        System.out.println(image.width() + "x" + image.height() + ", dim: " + image.dims() + ", channels: " + image.channels());

        Mat inputBlob = Dnn.blobFromImage(image);
        assertNotNull("Converting image to blob failed!", inputBlob);
        System.out.println(inputBlob.width() + "x" + inputBlob.height() + ", " + ", dim: " + inputBlob.dims() + ", channels: " + inputBlob.channels());

        Mat inputBlobP = new Mat();
        Core.subtract(inputBlob, new Scalar(117.0), inputBlobP);
        System.out.println(inputBlobP.width() + "x" + inputBlobP.height()+ ", " + ", dim: " + inputBlobP.dims() + ", channels: " + inputBlobP.channels());

  
        net.setInput(inputBlobP);

        Mat result = net.forward();

        assertNotNull("Net returned no result!", result);

       // BufferedImage convertedImage = Mat2BufferedImage(result);
        //displayImage(convertedImage);
        //Core.MinMaxLocResult minmax = Core.minMaxLoc(result.reshape(1, 1));

        //assertTrue("No image recognized!", minmax.maxVal > 0.9);

	}
	/*
    public void displayImage(Image img2)
{   
    //BufferedImage img=ImageIO.read(new File("/HelloOpenCV/lena.png"));
    ImageIcon icon=new ImageIcon(img2);
    JFrame frame=new JFrame();
    frame.setLayout(new FlowLayout());        
    frame.setSize(img2.getWidth(null)+50, img2.getHeight(null)+50);     
    JLabel lbl=new JLabel();
    lbl.setIcon(icon);
    frame.add(lbl);
    frame.setVisible(true);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

}
	
	public BufferedImage Mat2BufferedImage(Mat m){
		// source: http://answers.opencv.org/question/10344/opencv-java-load-image-to-gui/
		// Fastest code
		// The output can be assigned either to a BufferedImage or to an Image

		    int type = BufferedImage.TYPE_BYTE_GRAY;
		    if ( m.channels() > 1 ) {
		        type = BufferedImage.TYPE_3BYTE_BGR;
		    }
		    int bufferSize = m.channels()*m.cols()*m.rows();
		    byte [] b = new byte[bufferSize];
		    m.get(0,0,b); // get all the pixels
		    BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
		    final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		    System.arraycopy(b, 0, targetPixels, 0, b.length);  
		    return image;
	}
*/
	
   public static void main( String[] args ) throws Exception {
      System.out.println("Reading model from TensorFlow...");
      
      
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
      //Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
      //System.out.println("mat = " + mat.dump());
      
      ObjectDetector objectDetector = new ObjectDetector();
      objectDetector.testImage();
      
      System.out.println("Done...");
   }
}