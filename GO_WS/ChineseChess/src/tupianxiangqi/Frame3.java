package tupianxiangqi;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
//import xiangqi2.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class Frame3
  extends JFrame {
  String hongfyhm = "zhanshan";
  String heifyhm = "wanger";
  String hongfzwm = "张山";
  String heifzwm = "王二";
  String hongfdj = "少校";
  String heifdj = "上尉";
  String hongfdjf = "2900";
  String heifdjf = "2500";
  int index; //用户索引。
  //Image [] yuansu=new Image[16];

  JPanel contentPane;


  Board5 xiang;
  Daojishi djs1, djs2;
  JLabel statusBar = new JLabel();
  BorderLayout borderLayout1 = new BorderLayout();
  TitledBorder titledBorder1;

  //Construct the frame
  byte mycolor;
  byte turncolor;
  public void receivedata(byte [] data){//接收到的数据如何处理
    byte i,j,k;
    if (data[0] == CS.QIDONG) {
      hongfyhm =new String(data,1,20);
      //如何从字节数组的到字符串。
      hongfzwm =new String(data,21,20);
      //中文名不能超过10个字符。
      hongfdj =new String(data,41,20);
      heifyhm =new String(data,61,20);
      heifzwm =new String(data,81,20);
      heifdj =new String(data,101,20);
      mycolor=data[102];
    }else if(data[0]==CS.QIPU){
      if(turncolor==data[1]){//对方的棋谱

      }else{
        System.out.println("棋谱错误");
      }
    }

  }
  public Frame3() {
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
    //image1 = new ImageIcon(tupianxiangqi.Frame3.class.getResource("openFile.png"));
    // image2 = new ImageIcon(tupianxiangqi.Frame3.class.getResource("closeFile.png"));
    //image3 = new ImageIcon(tupianxiangqi.Frame3.class.getResource("help.png"));
    contentPane = (JPanel)this.getContentPane();
    titledBorder1 = new TitledBorder("");
    contentPane.setLayout(borderLayout1);
    this.setSize(new Dimension(800, 560));
    this.setTitle("象棋对弈");
    statusBar.setText("正常 ");
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

    //contentPane.add(jToolBar, BorderLayout.NORTH);
    contentPane.add(statusBar, BorderLayout.SOUTH);

    JPanel zuo = new JPanel();
    zuo.setLayout(new BorderLayout());
    djs1 = new Daojishi(hongfyhm, hongfzwm, hongfdj, (byte) 0);
    djs2 = new Daojishi(heifyhm, heifzwm, heifdj, (byte) 1);
    zuo.add(djs1, BorderLayout.NORTH);
    zuo.add(djs2, BorderLayout.CENTER);
    contentPane.add(zuo, BorderLayout.WEST);
    //contentPane.add(djs2, BorderLayout.WEST);

    xiang = new Board5();
    contentPane.add(xiang, BorderLayout.CENTER);
    xiang.init();
    xiang.setEnabled(true);
    XiangQiGuiZe xiangqiguize;
    //djs.setSi ze(150,520);
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
    Frame3_AboutBox dlg = new Frame3_AboutBox(this);
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

class Frame3_jMenuFileExit_ActionAdapter
  implements ActionListener {
  Frame3 adaptee;

  Frame3_jMenuFileExit_ActionAdapter(Frame3 adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuFileExit_actionPerformed(e);
  }
}

class Frame3_jMenuHelpAbout_ActionAdapter
  implements ActionListener {
  Frame3 adaptee;

  Frame3_jMenuHelpAbout_ActionAdapter(Frame3 adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jMenuHelpAbout_actionPerformed(e);
  }
}
