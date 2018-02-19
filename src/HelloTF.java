import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.tensorflow.Graph;
import org.tensorflow.Session;
import org.tensorflow.Tensor;
import org.tensorflow.TensorFlow;

public class HelloTF {
	
	public static void test() throws IOException {
		//Dataset
		float[] x = new float[]{1};
		float[] y = x;
			
		//Setting parameters
		String modelDir = "/home/dpapp/tensorflow-1.5.0/models/raccoon_dataset/results/checkpoint_23826";
				
			//Reading the graph
		byte[] graphDef = Files.readAllBytes(Paths.get(modelDir, "frozen_graph_inference.pb"));
			
		Tensor input = Tensor.create(0.5f);
				
		float results = -99999f;
		try (Graph g = new Graph()) {
			g.importGraphDef(graphDef);
			try (Session s = new Session(g); 
			    Tensor result = s.runner().feed("input", input).fetch("output").run().get(0)) {	
				results = result.floatValue();
			}
		}
		System.out.println("Expected: " + x + "\tPredicted: " + results);	
	}
	
  public static void main(String[] args) throws Exception {
	  System.out.println("Running helloTF");
    try (Graph g = new Graph()) {
      final String value = "Hello from " + TensorFlow.version();

      // Construct the computation graph with a single operation, a constant
      // named "MyConst" with a value "value".
      try (Tensor t = Tensor.create(value.getBytes("UTF-8"))) {
        // The Java API doesn't yet include convenience functions for adding operations.
        g.opBuilder("Const", "MyConst").setAttr("dtype", t.dataType()).setAttr("value", t).build();
      }

      // Execute the "MyConst" operation in a Session.
      try (Session s = new Session(g);
           Tensor output = s.runner().fetch("MyConst").run().get(0)) {
        System.out.println(new String(output.bytesValue(), "UTF-8"));
      }
    }
    
    test();
  }
}