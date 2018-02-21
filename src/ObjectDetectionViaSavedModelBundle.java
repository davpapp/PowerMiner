import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.tensorflow.DataType;
import org.tensorflow.Graph;
import org.tensorflow.Output;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.TensorFlow;
import org.tensorflow.types.UInt8;

public class ObjectDetectionViaSavedModelBundle {


		/*public Tensor<UInt8> getImage() {
	    	byte[] imageBytes = readAllBytesOrExit(Paths.get("/home/dpapp/tensorflow-1.5.0/models/raccoon_dataset/test_images/ironOre_test_9.jpg)");

	    	try (Tensor<UInt8> image = constructAndExecuteGraphToNormalizeImage(imageBytes) {
	    		/*float[] labelProbabilities = executeInceptionGraph(graphDef, image);
	    		int bestLabelIdx = maxIndex(labelProbabilities);
	    		System.out.println(
	    				String.format("BEST MATCH: %s (%.2f%% likely)",
	    						labels.get(bestLabelIdx),
	    						labelProbabilities[bestLabelIdx] * 100f));
	    	}
		}*/

	
	   public static void main( String[] args ) throws Exception {
	      /*System.out.println("Reading model from TensorFlow...");
	      
	      System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	      
	      ObjectDetector objectDetector = new ObjectDetector();
	      
	      
	      objectDetector.testGetLayerTypes();
	      objectDetector.testGetLayer();
	      objectDetector.testImage();*/
	      

	      final int IMG_SIZE = 128;
	      final String value = "Hello from " + TensorFlow.version();
	      System.out.println(value);

	      //byte[] imageBytes = readAllBytesOrExit(Paths.get("/home/dpapp/tensorflow-1.5.0/models/raccoon_dataset/test_images/ironOre_test_9.jpg"));
	      //Tensor<UInt8> image = constructAndExecuteGraphToNormalizeImage(imageBytes);
	      //final long[] shape = {330, 510, 3};
	      //Tensor image = Tensor.create(DataType.UINT8, shape, ByteBuffer.wrap(imageBytes));

	      SavedModelBundle load = SavedModelBundle.load("/home/dpapp/tensorflow-1.5.0/models/raccoon_dataset/results/checkpoint_23826/saved_model/", "serve");
      
	      try (Graph g = load.graph()) {
	          try (Session s = load.session();
	               Tensor result = s.runner()
	                       .feed("image_tensor:0", image)
	                       .fetch("detection_boxes:0").run().get(0))
	          {
	              System.out.println(result.floatValue());
	          }
	      }
	      load.close();
	      
	      System.out.println("Done...");
	   }
	   
	   private static Tensor<UInt8> makeImageTensor(String filename) throws IOException {
		    BufferedImage img = ImageIO.read(new File(filename));
		    if (img.getType() != BufferedImage.TYPE_3BYTE_BGR) {
		      throw new IOException(
		          String.format(
		              "Expected 3-byte BGR encoding in BufferedImage, found %d (file: %s). This code could be made more robust",
		              img.getType(), filename));
		    }
		    byte[] data = ((DataBufferByte) img.getData().getDataBuffer()).getData();
		    // ImageIO.read seems to produce BGR-encoded images, but the model expects RGB.
		    bgr2rgb(data);
		    final long BATCH_SIZE = 1;
		    final long CHANNELS = 3;
		    long[] shape = new long[] {BATCH_SIZE, img.getHeight(), img.getWidth(), CHANNELS};
		    return Tensor.create(UInt8.class, shape, ByteBuffer.wrap(data));
		  }


	   private static void bgr2rgb(byte[] data) {
		    for (int i = 0; i < data.length; i += 3) {
		      byte tmp = data[i];
		      data[i] = data[i + 2];
		      data[i + 2] = tmp;
		    }
	   }
}

	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   
	   /*
	   private static byte[] readAllBytesOrExit(Path path) {
		    try {
		      return Files.readAllBytes(path);
		    } catch (IOException e) {
		      System.err.println("Failed to read [" + path + "]: " + e.getMessage());
		      System.exit(1);
		    }
		    return null;
		  }
	   
	   private static Tensor<UInt8> constructAndExecuteGraphToNormalizeImage(byte[] imageBytes) {
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
		                          b.cast(b.decodeJpeg(input, 3), UInt8.class),
		                          b.constant("make_batch", 0)),
		                      b.constant("size", new int[] {H, W})),
		                  b.constant("mean", mean)),
		              b.constant("scale", scale));
		      try (Session s = new Session(g)) {
		        return s.runner().fetch(output.op().name()).run().get(0).expect(UInt8.class);
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
	   */

