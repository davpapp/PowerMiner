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

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
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
	Robot robot;
	
	public ObjectDetector() throws AWTException {
		this.model = SavedModelBundle.load("/home/dpapp/tensorflow-1.5.0/models/raccoon_dataset/results/checkpoint_22948/saved_model/", "serve");
		this.robot = new Robot();
	}
	
	public void update() throws Exception {
		// TODO: eliminate IO and pass BufferedImage directly.
		/*String fileName = "/home/dpapp/Desktop/RunescapeAI/temp/screenshot.jpg";
		BufferedImage image = captureScreenshotGameWindow();
		ImageIO.write(image, "jpg", new File(fileName));
		this.detectedObjects = getRecognizedObjectsFromImage(fileName);*/
	}
	
	public ArrayList<DetectedObject> getObjectsInImage(BufferedImage image) throws Exception {
		List<Tensor<?>> outputs = null;
		ArrayList<DetectedObject> detectedObjectsInImage = new ArrayList<DetectedObject>();
		
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
           
            int maxObjects = (int) scoresT.shape()[1];
            float[] scores = scoresT.copyTo(new float[1][maxObjects])[0];
            float[] classes = classesT.copyTo(new float[1][maxObjects])[0];
            float[][] boxes = boxesT.copyTo(new float[1][maxObjects][4])[0];

            for (int i = 0; i < scores.length; ++i) {
              if (scores[i] > 0.80) {
            	  detectedObjectsInImage.add(new DetectedObject(scores[i], classes[i], boxes[i]));
              }
            }
          }
        return detectedObjectsInImage;
	}
	
	public boolean isObjectPresentInBoundingBoxInImage(BufferedImage image, Rectangle boundingBox, String objectClass) throws Exception {
		BufferedImage subImage = image.getSubimage(boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
		ArrayList<DetectedObject> detectedObjectsInSubImage = getObjectsInImage(subImage);
		return (getObjectsOfClassInList(detectedObjectsInSubImage, objectClass).size() != 0);
	}
    
	public ArrayList<DetectedObject> getObjectsOfClassInList(ArrayList<DetectedObject> detectedObjects, String objectClass) {
		ArrayList<DetectedObject> detectedObjectsOfType = new ArrayList<DetectedObject>();
		for (DetectedObject detectedObject : detectedObjects) {
			if (detectedObject.getDetectionClass().equals(objectClass)) {
				detectedObjectsOfType.add(detectedObject);
			}
		}
		return detectedObjectsOfType;
	}

  private static Tensor<UInt8> makeImageTensor(BufferedImage image) throws IOException {
    BufferedImage img = ImageIO.read(image);
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
  
  private static void bgr2rgb(byte[] data) {
    for (int i = 0; i < data.length; i += 3) {
      byte tmp = data[i];
      data[i] = data[i + 2];
      data[i + 2] = tmp;
    }
  }
  
  public BufferedImage captureScreenshotGameWindow() throws IOException, AWTException {
		Rectangle area = new Rectangle(Constants.GAME_WINDOW_OFFSET_X, Constants.GAME_WINDOW_OFFSET_Y, Constants.GAME_WINDOW_WIDTH, Constants.GAME_WINDOW_HEIGHT);
		return robot.createScreenCapture(area);
	  }
}