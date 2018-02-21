import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.DictValue;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Layer;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.tensorflow.DataType;
import org.tensorflow.Graph;
import org.tensorflow.Output;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.TensorFlow;
import org.tensorflow.types.UInt8;

public class ObjectDetector {
	
	String inputImagePath;
	String inputModelPath;
	String inputModelArgumentsPath;
	
	Net net;
	
	public ObjectDetector() throws Exception {
		this.inputImagePath = "/home/dpapp/tensorflow-1.5.0/models/raccoon_dataset/test_images/ironOre_test_9.jpg";
		this.inputModelPath = "/home/dpapp/tensorflow-1.5.0/models/raccoon_dataset/results/checkpoint_23826/frozen_graph_inference.pb";
		this.inputModelArgumentsPath = "/home/dpapp/tensorflow-1.5.0/models/raccoon_dataset/training/graph.pbtxt";
		
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
	
	public Mat testImage() throws Exception {
		final int IN_WIDTH = 300;
        final int IN_HEIGHT = 300;
        final float WH_RATIO = (float)IN_WIDTH / IN_HEIGHT;
        final double IN_SCALE_FACTOR = 0.007843;
        final double MEAN_VAL = 127.5;
        final double THRESHOLD = 0.2;
		
		Mat frame = Imgcodecs.imread(inputImagePath);
        Imgproc.cvtColor(frame, frame, Imgproc.COLOR_RGBA2RGB);
		assertNotNull("Loading image from file failed!", frame);
		
		Mat blob = Dnn.blobFromImage(frame, IN_SCALE_FACTOR,
                new Size(IN_WIDTH, IN_HEIGHT),
                new Scalar(MEAN_VAL, MEAN_VAL, MEAN_VAL), false, false);
        net.setInput(blob);
        Mat detections = net.forward();

        int cols = frame.cols();
        int rows = frame.rows();

        Size cropSize;
        if ((float)cols / rows > WH_RATIO) {
            cropSize = new Size(rows * WH_RATIO, rows);
        } else {
            cropSize = new Size(cols, cols / WH_RATIO);
        }

        int y1 = (int)(rows - cropSize.height) / 2;
        int y2 = (int)(y1 + cropSize.height);
        int x1 = (int)(cols - cropSize.width) / 2;
        int x2 = (int)(x1 + cropSize.width);
        Mat subFrame = frame.submat(y1, y2, x1, x2);

        cols = subFrame.cols();
        rows = subFrame.rows();

        detections = detections.reshape(1, (int)detections.total() / 7);

        for (int i = 0; i < detections.rows(); ++i) {
            double confidence = detections.get(i, 2)[0];
            if (confidence > THRESHOLD) {
                int classId = (int)detections.get(i, 1)[0];

                int xLeftBottom = (int)(detections.get(i, 3)[0] * cols);
                int yLeftBottom = (int)(detections.get(i, 4)[0] * rows);
                int xRightTop   = (int)(detections.get(i, 5)[0] * cols);
                int yRightTop   = (int)(detections.get(i, 6)[0] * rows);

                // Draw rectangle around detected object.
                Imgproc.rectangle(subFrame, new Point(xLeftBottom, yLeftBottom),
                        new Point(xRightTop, yRightTop),
                        new Scalar(0, 255, 0));
                String label = "ironOre" + ": " + confidence;
                int[] baseLine = new int[1];
                Size labelSize = Imgproc.getTextSize(label, Core.FONT_HERSHEY_SIMPLEX, 0.5, 1, baseLine);

                // Draw background for label.
                Imgproc.rectangle(subFrame, new Point(xLeftBottom, yLeftBottom - labelSize.height),
                        new Point(xLeftBottom + labelSize.width, yLeftBottom + baseLine[0]),
                        new Scalar(255, 255, 255), Core.FILLED);

                // Write class name and confidence.
                Imgproc.putText(subFrame, label, new Point(xLeftBottom, yLeftBottom),
                        Core.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 0, 0));
            }
        }
        return frame;
	}

	
   public static void main( String[] args ) throws Exception {
      System.out.println("Reading model from TensorFlow...");
      
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
      
      ObjectDetector objectDetector = new ObjectDetector();
      
      
      objectDetector.testGetLayerTypes();
      objectDetector.testGetLayer();
      objectDetector.testImage();
      /*

      final int IMG_SIZE = 128;
      final String value = "Hello from " + TensorFlow.version();

      byte[] imageBytes = readAllBytesOrExit(Paths.get("/home/dpapp/tensorflow-1.5.0/models/raccoon_dataset/test_images/ironOre_test_9.jpg"));
      Tensor image = constructAndExecuteGraphToNormalizeImage(imageBytes);

      SavedModelBundle load = SavedModelBundle.load("/home/dpapp/tensorflow-1.5.0/models/raccoon_dataset/SavedModel/saved_model.pb");

      long[] sitio2;
      try (Graph g = load.graph()) {
          try (Session s = load.session();
               Tensor result = s.runner()
                       .feed("image_tensor", image)
                       .fetch("detection_boxes").run().get(0))
          {
              sitio2 = (long[]) result.copyTo(new long[1]);
              System.out.print(sitio2[0]+"\n");
          }
      }
      load.close();
      */
      System.out.println("Done...");
   }
   
   private static byte[] readAllBytesOrExit(Path path) {
	    try {
	      return Files.readAllBytes(path);
	    } catch (IOException e) {
	      System.err.println("Failed to read [" + path + "]: " + e.getMessage());
	      System.exit(1);
	    }
	    return null;
	  }
   
   private static Tensor<Float> constructAndExecuteGraphToNormalizeImage(byte[] imageBytes) {
	    try (Graph g = new Graph()) {
	      GraphBuilder b = new GraphBuilder(g);
	      // Some constants specific to the pre-trained model at:
	      // https://storage.googleapis.com/download.tensorflow.org/models/inception5h.zip
	      //
	      // - The model was trained with images scaled to 224x224 pixels.
	      // - The colors, represented as R, G, B in 1-byte each were converted to
	      //   float using (value - Mean)/Scale.
	      final int H = 224;
	      final int W = 224;
	      final float mean = 117f;
	      final float scale = 1f;

	      // Since the graph is being constructed once per execution here, we can use a constant for the
	      // input image. If the graph were to be re-used for multiple input images, a placeholder would
	      // have been more appropriate.
	      final Output<String> input = b.constant("input", imageBytes);
	      final Output<Float> output =
	          b.div(
	              b.sub(
	                  b.resizeBilinear(
	                      b.expandDims(
	                          b.cast(b.decodeJpeg(input, 3), Float.class),
	                          b.constant("make_batch", 0)),
	                      b.constant("size", new int[] {H, W})),
	                  b.constant("mean", mean)),
	              b.constant("scale", scale));
	      try (Session s = new Session(g)) {
	        return s.runner().fetch(output.op().name()).run().get(0).expect(Float.class);
	      }
	    }
	  }
   
   static class GraphBuilder {
	    GraphBuilder(Graph g) {
	      this.g = g;
	    }

	    Output<Float> div(Output<Float> x, Output<Float> y) {
	      return binaryOp("Div", x, y);
	    }

	    <T> Output<T> sub(Output<T> x, Output<T> y) {
	      return binaryOp("Sub", x, y);
	    }

	    <T> Output<Float> resizeBilinear(Output<T> images, Output<Integer> size) {
	      return binaryOp3("ResizeBilinear", images, size);
	    }

	    <T> Output<T> expandDims(Output<T> input, Output<Integer> dim) {
	      return binaryOp3("ExpandDims", input, dim);
	    }

	    <T, U> Output<U> cast(Output<T> value, Class<U> type) {
	      DataType dtype = DataType.fromClass(type);
	      return g.opBuilder("Cast", "Cast")
	          .addInput(value)
	          .setAttr("DstT", dtype)
	          .build()
	          .<U>output(0);
	    }

	    Output<UInt8> decodeJpeg(Output<String> contents, long channels) {
	      return g.opBuilder("DecodeJpeg", "DecodeJpeg")
	          .addInput(contents)
	          .setAttr("channels", channels)
	          .build()
	          .<UInt8>output(0);
	    }

	    <T> Output<T> constant(String name, Object value, Class<T> type) {
	      try (Tensor<T> t = Tensor.<T>create(value, type)) {
	        return g.opBuilder("Const", name)
	            .setAttr("dtype", DataType.fromClass(type))
	            .setAttr("value", t)
	            .build()
	            .<T>output(0);
	      }
	    }
	    Output<String> constant(String name, byte[] value) {
	      return this.constant(name, value, String.class);
	    }

	    Output<Integer> constant(String name, int value) {
	      return this.constant(name, value, Integer.class);
	    }

	    Output<Integer> constant(String name, int[] value) {
	      return this.constant(name, value, Integer.class);
	    }

	    Output<Float> constant(String name, float value) {
	      return this.constant(name, value, Float.class);
	    }

	    private <T> Output<T> binaryOp(String type, Output<T> in1, Output<T> in2) {
	      return g.opBuilder(type, type).addInput(in1).addInput(in2).build().<T>output(0);
	    }

	    private <T, U, V> Output<T> binaryOp3(String type, Output<U> in1, Output<V> in2) {
	      return g.opBuilder(type, type).addInput(in1).addInput(in2).build().<T>output(0);
	    }
	    private Graph g;
	  }
	
}