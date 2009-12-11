/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package test;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * <p>TakeScreenShot.java
 * </p>
 */
public class TakeScreenShot {
	public static void captureScreen(String fileName) throws Exception {

	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    Rectangle screenRectangle = new Rectangle(screenSize);
	    Robot robot = new Robot();
	    BufferedImage image = robot.createScreenCapture(screenRectangle);
	    ImageIO.write(image, "png", new File(fileName));
	    
	}
}
