package tupianxiangqi;

import javax.swing.*;
import javax.swing.UIManager;
import java.awt.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class XiangQi {
  boolean packFrame = false;

  //Construct the application

  public XiangQi(String[] args) {
    Frame1 framea = new Frame1(args);
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
  public XiangQi() {
      Frame1 framea = new Frame1();
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
  public static void main( String[] args) {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    if(args.length==5){
      new XiangQi(args);
     // new XiangQi(args);
    }else{
      System.out.print("应有五个参数。");
      new XiangQi();
    }
  }
}
