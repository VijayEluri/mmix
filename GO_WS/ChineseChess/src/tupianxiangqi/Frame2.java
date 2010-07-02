package tupianxiangqi;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Frame2
    extends JFrame {
  String hongfyhm="zhanshan";
  String heifyhm="wanger";
  String hongfzwm="张山";
  String heifzwm="王二";
  String hongfdj="少校";
  String heifdj="上尉";
  int index;//用户索引。
  //Image [] yuansu=new Image[16];

  JPanel contentPane;
  JMenuBar jMenuBar1 = new JMenuBar();
  JMenu jMenuFile = new JMenu();
  JMenuItem jMenuFileExit = new JMenuItem();
  JMenu jMenuHelp = new JMenu();
  JMenuItem jMenuHelpAbout = new JMenuItem();

  Board1 xiang;
  Daojishi djs1, djs2;
  JLabel statusBar = new JLabel();
  BorderLayout borderLayout1 = new BorderLayout();
  TitledBorder titledBorder1;

  //Construct the frame
  public Frame2() {
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit() throws Exception {
    byte i = 0;
    String imagename;
    //image1 = new ImageIcon(tupianxiangqi.Frame2.class.getResource("openFile.png"));
    // image2 = new ImageIcon(tupianxiangqi.Frame2.class.getResource("closeFile.png"));
    //image3 = new ImageIcon(tupianxiangqi.Frame2.class.getResource("help.png"));
    contentPane = (JPanel)this.getContentPane();
    titledBorder1 = new TitledBorder("");
    contentPane.setLayout(borderLayout1);
    this.setSize(new Dimension(800, 560));
    this.setTitle("象棋网络对弈");
    statusBar.setText("正常 ");
    jMenuFile.setText("File");
    jMenuFileExit.setText("Exit");
    jMenuFileExit.addActionListener(new Frame2_jMenuFileExit_ActionAdapter(this));
    jMenuHelp.setText("Help");
    jMenuHelpAbout.setText("About");
    jMenuHelpAbout.addActionListener(new Frame2_jMenuHelpAbout_ActionAdapter(this));
    //jButton1.setIcon(image1);
    //jButton1.setToolTipText("Open File");
    //jButton2.setIcon(image2);
    //jButton2.setToolTipText("Close File");
    //jButton3.setIcon(image3);
    //jButton3.setToolTipText("Help");
    //contentPane.setBorder(titledBorder1);
    //jToolBar.add(jButton1);
    //jToolBar.add(jButton2);
    // jToolBar.add(jButton3);
    jMenuFile.add(jMenuFileExit);
    jMenuHelp.add(jMenuHelpAbout);
    jMenuBar1.add(jMenuFile);
    jMenuBar1.add(jMenuHelp);
    this.setJMenuBar(jMenuBar1);
    //contentPane.add(jToolBar, BorderLayout.NORTH);
    contentPane.add(statusBar, BorderLayout.SOUTH);

    JPanel zuo=new JPanel();
    zuo.setLayout(new BorderLayout()) ;
    djs1 = new Daojishi(hongfyhm,hongfzwm,hongfdj,(byte)0);
    djs2 = new Daojishi(heifyhm,heifzwm,heifdj,(byte)1);
    zuo.add(djs1,BorderLayout.NORTH);
    zuo.add(djs2,BorderLayout.CENTER);
    contentPane.add(zuo, BorderLayout.WEST);
    //contentPane.add(djs2, BorderLayout.WEST);

    //xiang = new Board1(djs1,djs2);
    contentPane.add(xiang, BorderLayout.CENTER);
    xiang.init();
    xiang.setEnabled(true);

    //djs.setSize(150,520);
    djs1.setEnabled(false);
    djs2.setEnabled(false);
    djs1.init();
    djs2.init();
    djs1.start();
    /*Toolkit tk = Toolkit.getDefaultToolkit();
        System.out.println("tk=" + tk);
        yuansu[0] = tk.getImage("Board.jpg");
        yuansu[15] = tk.getImage("pieceTex.jpg");
        for (i = 1; i <= 7; i++) { //hong fang zai qian.
     imagename = "Red" + (i - 1) + ".jpg";
     yuansu[i] = tk.getImage(imagename);
     imagename = "Blue" + (i - 1) + ".jpg";
     yuansu[7 + i] = tk.getImage(imagename);
     //yuansu[i]=this.getImage(this.getCodeBase(),"Blue"+zhl[i]+".jpg") ;;
     System.out.println("imagenameuan=" + imagename);
     System.out.println("yuansu[i]=" + yuansu[i]);
     System.out.println("yuansu[i]=" + yuansu[i].getSource());
       } */

  }

  //File | Exit action performed
  public void jMenuFileExit_actionPerformed(ActionEvent e) {
    System.exit(0);
  }

  //Help | About action performed
  public void jMenuHelpAbout_actionPerformed(ActionEvent e) {
    Frame2_AboutBox dlg = new Frame2_AboutBox(this);
    Dimension dlgSize = dlg.getPreferredSize();
    Dimension frmSize = getSize();
    Point loc = getLocation();
    dlg.setLocation( (frmSize.width - dlgSize.width) / 2 + loc.x,
                    (frmSize.height - dlgSize.height) / 2 + loc.y);
    dlg.setModal(true);
    dlg.pack();
    dlg.show();
  }


  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
      jMenuFileExit_actionPerformed(null);
    }
  }
}

class Frame2_jMenuFileExit_ActionAdapter
    implements ActionListener {
  Frame2 adaptee;

  Frame2_jMenuFileExit_ActionAdapter(Frame2 adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuFileExit_actionPerformed(e);
  }
}

class Frame2_jMenuHelpAbout_ActionAdapter
    implements ActionListener {
  Frame2 adaptee;

  Frame2_jMenuHelpAbout_ActionAdapter(Frame2 adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuHelpAbout_actionPerformed(e);
  }
}
