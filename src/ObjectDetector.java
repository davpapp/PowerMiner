/* Copyright 2018 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.tensorflow.SavedModelBundle;
import org.tensorflow.Tensor;
import org.tensorflow.types.UInt8;

/**
 * Java inference for the Object Detection API at:
 * https://github.com/tensorflow/models/blob/master/research/object_detection/
 */
public class ObjectDetector {
  
	SavedModelBundle model;
	
	public ObjectDetector() {
		model = SavedModelBundle.load("/home/dpapp/tensorflow-1.5.0/models/raccoon_dataset/results/checkpoint_23826/saved_model/", "serve");
	}
	
	public void getIronOreLocationsFromImage(BufferedImage image) throws IOException {
		List<Tensor<?>> outputs = null;
        
        try (Tensor<UInt8> input = makeImageTensor(image)) {
          outputs =
              model
                  .session()
                  .runner()
                  .feed("image_tensor", input)
                  .fetch("detection_scores")
                  .fetch("detection_classes")
                  .fetch("detection_boxes")
                  .run();
        }
        
        try (Tensor<Float> scoresT = outputs.get(0).expect(Float.class);
        	Tensor<Float> classesT = outputs.get(1).expect(Float.class);
            Tensor<Float> boxesT = outputs.get(2).expect(Float.class)) {
              // All these tensors have:
              // - 1 as the first dimension
              // - maxObjects as the second dimension
              // While boxesT will have 4 as the third dimension (2 sets of (x, y) coordinates).
              // This can be verified by looking at scoresT.shape() etc.
            int maxObjects = (int) scoresT.shape()[1];
            float[] scores = scoresT.copyTo(new float[1][maxObjects])[0];
            float[] classes = classesT.copyTo(new float[1][maxObjects])[0];
            float[][] boxes = boxesT.copyTo(new float[1][maxObjects][4])[0];
              // Print all objects whose score is at least 0.5.
            boolean foundSomething = false;
            for (int i = 0; i < scores.length; ++i) {
              if (scores[i] < 0.5) {
                continue;
              }
              foundSomething = true;
              System.out.printf("\tFound %-20s (score: %.4f)\n", "ironOre", 1.0000); //labels[(int) classes[i]], scores[i]);
              System.out.println("Location:");
              System.out.println("X:" + 510 * boxes[i][1] + ", Y:" + 330 * boxes[i][0] + ", width:" + 510 * boxes[i][3] + ", height:" + 330 * boxes[i][2]);
            }
            if (!foundSomething) {
              System.out.println("No objects detected with a high enough score.");
            }
          }
	}
    
    /*public void test() {
    	try (SavedModelBundle model = SavedModelBundle.load("/home/dpapp/tensorflow-1.5.0/models/raccoon_dataset/results/checkpoint_23826/saved_model/", "serve")) {
     // printSignature(model);
    
        final String filename = "/home/dpapp/tensorflow-1.5.0/models/raccoon_dataset/test_images/ironOre_test_9.jpg";
        List<Tensor<?>> outputs = null;
        
        try (Tensor<UInt8> input = makeImageTensor(filename)) {
        	System.out.println("Image was converted to tensor.");
        	long startTime = System.currentTimeMillis();
          outputs =
              model
                  .session()
                  .runner()
                  .feed("image_tensor", input)
                  .fetch("detection_scores")
                  .fetch("detection_classes")
                  .fetch("detection_boxes")
                  .run();
          System.out.println("Object detection took " + (System.currentTimeMillis() - startTime));
        }
        
        try (Tensor<Float> scoresT = outputs.get(0).expect(Float.class);
            Tensor<Float> classesT = outputs.get(1).expect(Float.class);
            Tensor<Float> boxesT = outputs.get(2).expect(Float.class)) {
          // All these tensors have:
          // - 1 as the first dimension
          // - maxObjects as the second dimension
          // While boxesT will have 4 as the third dimension (2 sets of (x, y) coordinates).
          // This can be verified by looking at scoresT.shape() etc.
          int maxObjects = (int) scoresT.shape()[1];
          float[] scores = scoresT.copyTo(new float[1][maxObjects])[0];
          float[] classes = classesT.copyTo(new float[1][maxObjects])[0];
          float[][] boxes = boxesT.copyTo(new float[1][maxObjects][4])[0];
          // Print all objects whose score is at least 0.5.
          System.out.printf("* %s\n", filename);
          boolean foundSomething = false;
          for (int i = 0; i < scores.length; ++i) {
            if (scores[i] < 0.5) {
              continue;
            }
            foundSomething = true;
            System.out.printf("\tFound %-20s (score: %.4f)\n", "ironOre", 1.0000); //labels[(int) classes[i]], scores[i]);
            System.out.println("Location:");
            System.out.println("X:" + 510 * boxes[i][1] + ", Y:" + 330 * boxes[i][0] + ", width:" + 510 * boxes[i][3] + ", height:" + 330 * boxes[i][2]);
          }
          if (!foundSomething) {
            System.out.println("No objects detected with a high enough score.");
          }
        }
     
    }
  }*/

  private static void printSignature(SavedModelBundle model) throws Exception {
    /*MetaGraphDef m = MetaGraphDef.parseFrom(model.metaGraphDef());
    SignatureDef sig = m.getSignatureDefOrThrow("serving_default");
    int numInputs = sig.getInputsCount();
    int i = 1;
    System.out.println("MODEL SIGNATURE");
    System.out.println("Inputs:");
    for (Map.Entry<String, TensorInfo> entry : sig.getInputsMap().entrySet()) {
      TensorInfo t = entry.getValue();
      System.out.printf(
          "%d of %d: %-20s (Node name in graph: %-20s, type: %s)\n",
          i++, numInputs, entry.getKey(), t.getName(), t.getDtype());
    }
    int numOutputs = sig.getOutputsCount();
    i = 1;
    System.out.println("Outputs:");
    for (Map.Entry<String, TensorInfo> entry : sig.getOutputsMap().entrySet()) {
      TensorInfo t = entry.getValue();
      System.out.printf(
          "%d of %d: %-20s (Node name in graph: %-20s, type: %s)\n",
          i++, numOutputs, entry.getKey(), t.getName(), t.getDtype());
    }*/
    System.out.println("-----------------------------------------------");
  }

  /*private static String[] loadLabels(String filename) throws Exception {
    String text = new String(Files.readAllBytes(Paths.get(filename)), StandardCharsets.UTF_8);
    StringIntLabelMap.Builder builder = StringIntLabelMap.newBuilder();
    TextFormat.merge(text, builder);
    StringIntLabelMap proto = builder.build();
    int maxId = 0;
    for (StringIntLabelMapItem item : proto.getItemList()) {
      if (item.getId() > maxId) {
        maxId = item.getId();
      }
    }
    String[] ret = new String[maxId + 1];
    for (StringIntLabelMapItem item : proto.getItemList()) {
      ret[item.getId()] = item.getDisplayName();
    }
    String[] label = {"ironOre"};
    return label;
  }*/

  private static void bgr2rgb(byte[] data) {
    for (int i = 0; i < data.length; i += 3) {
      byte tmp = data[i];
      data[i] = data[i + 2];
      data[i + 2] = tmp;
    }
  }

  private static Tensor<UInt8> makeImageTensor(BufferedImage img) throws IOException {
    //BufferedImage img = ImageIO.read(new File(filename));
    if (img.getType() != BufferedImage.TYPE_3BYTE_BGR) {
      throw new IOException(
          String.format(
              "Expected 3-byte BGR encoding in BufferedImage, found %d (file: %s). This code could be made more robust"));
    }
    byte[] data = ((DataBufferByte) img.getData().getDataBuffer()).getData();
    // ImageIO.read seems to produce BGR-encoded images, but the model expects RGB.
    bgr2rgb(data);
    final long BATCH_SIZE = 1;
    final long CHANNELS = 3;
    long[] shape = new long[] {BATCH_SIZE, img.getHeight(), img.getWidth(), CHANNELS};
    return Tensor.create(UInt8.class, shape, ByteBuffer.wrap(data));
  }
}
