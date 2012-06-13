//package untitled8;
////import untitled1.JisuanBoard;
//import java.awt.*;
//import java.awt.event.*;
//import javax.swing.*;
//import javax.swing.border.*;
//
///**
// * <p>Title: </p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: </p>
// * @author unascribed
// * @version 1.0
// */
//
//public class WeiqiAppFrame2 extends JFrame {
//  JPanel contentPane;
//  ImageIcon image1;
//  ImageIcon image2;
//  ImageIcon image3;
//  JLabel statusBar = new JLabel();
//  BorderLayout borderLayout1 = new BorderLayout();
//  JPanel jPanel1 = new JPanel();
//  Border border1;
//  Border border2;
//  Border border3;
//  JButton jButton4 = new JButton();
//  JButton jButton5 = new JButton();
//  JButton jButton6 = new JButton();
//
//  GoCanvas gocanvas=new GoCanvas();//
//  JisuanBoard jsgo;
//  //Construct the frame
//  public WeiqiAppFrame2() {
//    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
//    try {
//      jbInit();
//    }
//    catch(Exception e) {
//      e.printStackTrace();
//    }
//  }
//   public boolean mouseDown(Event e,int x, int y){
//
//      JisuanBoard jsgo=new JisuanBoard(gocanvas.goboard);
//      if(log.isDebugEnabled()) log.debug("weiqiFrame 's mousedown");
//      short c=jsgo.xiayishou();
//      byte a=(byte)((c-1)%19+1);
//      byte b=(byte)((c-1)/19+1);
//      if(log.isDebugEnabled()) log.debug("应手为:"+c);
//      gocanvas.goboard.cgcl(a,b);
//      gocanvas.KEXIA =true;
//      gocanvas.repaint();
//      return true;
//   }
//  //Component initialization
//  private void jbInit() throws Exception  {
//    image1 = new ImageIcon(untitled8.WeiqiAppFrame1.class.getResource("openFile.gif"));
//    image2 = new ImageIcon(untitled8.WeiqiAppFrame1.class.getResource("closeFile.gif"));
//    image3 = new ImageIcon(untitled8.WeiqiAppFrame1.class.getResource("help.gif"));
//    //setIconImage(Toolkit.getDefaultToolkit().createImage(WeiqiAppFrame1.class.getResource("[Your Icon]")));
//    contentPane = (JPanel) this.getContentPane();
//
//    border1 = BorderFactory.createLineBorder(SystemColor.controlText,1);
//    border2 = BorderFactory.createLineBorder(Color.orange,2);
//    border3 = BorderFactory.createLineBorder(Color.black,2);
//    contentPane.setLayout(borderLayout1);
//    this.setSize(new Dimension(417, 240));
//    this.setTitle("围棋智能程序");
//    statusBar.setText(" ");
//    jPanel1.setForeground(Color.gray);
//    jPanel1.setBorder(border3);
//    jPanel1.setLayout(null);
//    jButton4.setBounds(new Rectangle(586 , 18, 79, 29));
//    jButton4.setText("jButton4");
//    jButton5.setBounds(new Rectangle(586 , 47, 79, 29));
//    jButton5.setText("jButton5");
//    jButton6.setBounds(new Rectangle(586 , 73, 79, 29));
//    jButton6.setText("jButton6");
//
//    contentPane.setPreferredSize(new Dimension(600, 600));
//
//    contentPane.add(statusBar, BorderLayout.SOUTH);
//    contentPane.add(jPanel1, BorderLayout.CENTER);
//
//     gocanvas.setBackground(Color.orange);
//     gocanvas.setBounds(new Rectangle(0, 0, 586, 590));
//    jPanel1.add(gocanvas,null);
//    jPanel1.add(jButton8, null);
//    jPanel1.add(jButton9, null);
//    jPanel1.add(jButton11, null);
//    jPanel1.add(jButton10, null);
//    jPanel1.add(jButton6, null);
//    jPanel1.add(jButton7, null);
//    jPanel1.add(jButton5, null);
//    jPanel1.add(jButton4, null);
//  }
//  //File | Exit action performed
//  public void jMenuFileExit_actionPerformed(ActionEvent e) {
//    System.exit(0);
//  }
//  //Help | About action performed
//  public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
//    WeiqiAppFrame1_AboutBox dlg = new WeiqiAppFrame1_AboutBox(this);
//    Dimension dlgSize = dlg.getPreferredSize();
//    Dimension frmSize = getSize();
//    Point loc = getLocation();
//    dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
//    dlg.setModal(true);
//    dlg.show();
//  }
//  //Overridden so we can exit when window is closed
//  protected void processWindowEvent(WindowEvent e) {
//    super.processWindowEvent(e);
//    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
//      jMenuFileExit_actionPerformed(null);
//    }
//  }
//}
