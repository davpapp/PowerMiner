import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MirrorDisplay extends JPanel{

    private BufferedImage image;

    public MirrorDisplay()  {
       try {
           Robot robot = new Robot();
           image = robot.createScreenCapture(new Rectangle(0, 0, 500, 500));
        } catch (AWTException ex) {
           Logger.getLogger(MirrorDisplay.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void paintComponent(Graphics g) {
        g.drawImage(image, 0, 0, null); 

    }
public static void main(String[] args) throws InterruptedException {

        JFrame test = new JFrame();
        
        while (true) {
        	test.add(new MirrorDisplay());
        	Dimension b = new Dimension(500,500);
        	test.setMinimumSize(b);

        	test.setVisible(true);
        	Thread.sleep(50);
        	test.dispose();
        }

    }
}