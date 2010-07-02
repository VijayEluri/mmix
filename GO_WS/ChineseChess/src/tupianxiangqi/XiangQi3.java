package tupianxiangqi;

import javax.swing.*;
import javax.swing.UIManager;
import java.awt.*;

/**
 * <p>Title: 整个结构作乐修改</p>
 * <p>Description: 利用统一的规则类</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class XiangQi3 {
  boolean packFrame = false;

  //Construct the application
  public XiangQi3() {
    Frame3 framea = new Frame3();
   // Frame2 frameb = new Frame2();
    //Validate frames that have preset sizes
    //Pack frames that have useful preferred size info, e.g. from their layout
    if (packFrame) {
      framea.pack();
      //frameb.pack();
    }
    else {
      framea.validate();
     // frameb.validate();
    }
    //Center the window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = framea.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width ) {
      frameSize.width = screenSize.width ;
    }
    framea.setLocation( (screenSize.width - frameSize.width) / 2,
                       (screenSize.height - frameSize.height) / 2);
    framea.setVisible(true);

  }

  //Main method
  public static void main(String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    new XiangQi3();
  }
}
