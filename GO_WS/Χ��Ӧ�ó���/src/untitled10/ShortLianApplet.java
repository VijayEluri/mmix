package untitled10;

import java.applet.Applet;
import java.awt.HeadlessException;

import java.applet.Applet;
import java.awt.*;

//项目10：用GoAppletLian测试GoBoardLian
//所有源代码在同一目录下，确保类的通信。
//仍有很多错误，未细致检查。
public class ShortLianApplet
    extends Applet {
  public final boolean DEBUG = true;
  //boolean KEXIA=true;
  boolean CHONGHUI = true;
  BoardLianShort goboard = new BoardLianShort();
  public void update(Graphics g) {
    paint(g);
  }

  Image work;
  Graphics g;
  public void init() {
    this.setBackground(Color.ORANGE);
    work = this.createImage(560, 560);
    if (work == null) {
      System.out.println("work==null");
    }
    else {
      g = work.getGraphics();
      System.out.println("work!=null");
    }
  }

  public void paint(Graphics gg) {
    if (work == null) {
      work = this.createImage(560, 560);
      g = work.getGraphics();
      System.out.println("work==null");
    }
    else if (g == null) {
      System.out.println("work!=null;g==null");
      g = work.getGraphics();
    }
    g.setColor(Color.orange);
    g.fillRect(0, 0, 560, 560);
    g.setColor(Color.black);
    short kinp = 0;
    for (int i = 1; i <= 19; i++) { //画线
      g.drawLine(18, 28 * i - 10, 522, 28 * i - 10); //hor
      g.drawLine(28 * i - 10, 18, 28 * i - 10, 522); //ver
    }
    for (int i = 0; i < 3; i++) { //画星位
      for (int j = 0; j < 3; j++) {
        g.fillOval(168 * i + 99, 168 * j + 99, 6, 6);
      }
    }

    for (int i = 1; i <= 19; i++) { //画着子点
      for (int j = 1; j <= 19; j++) {
        if (goboard.zb[i][j][0] == 1) {
          g.setColor(Color.black);
          g.fillOval(28 * i - 24, 28 * j - 24, 28, 28);
          //System.out.println("//paint the black point.");
        }
        else if (goboard.zb[i][j][0] == 2) {
          g.setColor(Color.white);
          g.fillOval(28 * i - 24, 28 * j - 24, 28, 28);
          // System.out.println("//paint the white point.");
        }
        kinp = goboard.zbk[i][j];
        if (kinp != 0 & DEBUG == true) { //输出块号
          g.setColor(Color.green);
          g.drawString("" + kinp, 28 * i - 14, 28 * j - 4);

        }
      }
    }
    gg.drawImage(work, 0, 0, this);
  } //else画整个棋盘和棋子

  public boolean mouseDown(Event e, int x, int y) { //接受鼠标输入
    //if(KEXIA==true){
    //KEXIA=false;//只有机器完成一手,才能继续.
    System.out.println("方法 mousedown");
    byte a = (byte) ( (x - 4) / 28 + 1); //完成数气提子等.
    byte b = (byte) ( (y - 4) / 28 + 1);
    goboard.cgcl(a, b);
    if (DEBUG == true) {
      goboard.output();
    }
    repaint();
    System.out.println("方法 mousedown");
    return true; //向容器传播,由Frame处理
    //}
    // else  return true;
  }

  public boolean handleEvent(Event evt) {
    if (evt.id == Event.MOUSE_DOWN) {
      return mouseDown(evt, evt.x, evt.y);
    }
    else {
      return super.handleEvent(evt);
    }
  }

  public ShortLianApplet() {
    super();
  }
}
