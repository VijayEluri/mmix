package tupianxiangqi;

import java.awt.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

class Qizi {

  byte zhonglei; //兵种1－7；
  byte bianhao = 0; //棋子编号1-32
  byte color = 0; //红或黑
  byte a = 0; //当前位置，1开始。
  byte b = 0; //初始位置

  public Qizi(byte bh, byte zl, byte weia, byte weib, byte col) {
    a = weia;
    b = weib;
    bianhao = bh;
    zhonglei = zl;
    color = col;
  }

  public Qizi(byte bh, byte weia, byte weib, byte col) {
    a = weia;
    b = weib;
    bianhao = bh;
    color = col;
  }

}

class Qizi1 {
  static byte[][] shizi = new byte[12][11]; //那个位置有十字。
  String name; //其实可以省略。
  byte zhonglei; //兵种1－7；
  byte bianhao = 0; //棋子编号1-32
  byte color = 0; //红或黑
  byte a = 0; //当前位置，1开始。
  byte b = 0; //初始位置

  //zuoweiquanjubianliang;
  /*public Qizi() {
    bianhao=0;
    a = 0;
    b = 0;
    name = null;
    banjing = 25;
    bcolor = Color.DARK_GRAY;
    fcolor = Color.BLACK;
     }*/

  public Qizi1(byte bh, byte zl, String tring, byte weia, byte weib, byte col) {
    a = weia;
    b = weib;
    bianhao = bh;
    zhonglei = zl;
    name = tring;
    color = col;
  }

  /*public Qizi(String string, byte ban, Color fc) {
    a = 0;
    b = 0;
    //最常用
    name = string;
    //banjing = ban;
    bcolor = Color.DARK_GRAY;
    fcolor = fc;
       }*/

  public boolean draw(Graphics g) {
    if (g == null) {
      System.out.println("errroe 1");
    }
    byte banjing = Board.BANJING;
    int yd1 = 0;
    int yd2 = 0;
    yd2 = (a - 1) * 2 * Board.HANGJU + Board.JIANXI - Board.BANJING;
    yd1 = (b - 1) * 2 * Board.LIEKUAN + Board.JIANXI - Board.BANJING;

    g.setColor(Color.lightGray);
    g.fillOval(yd1, yd2, 2 * banjing, 2 * banjing);
    //g.fillOval(a * banjing, b * banjing, banjing*2, banjing*2);
    if (color == 0) {
      g.setColor(Color.red);
    }
    else {
      g.setColor(Color.black);
    }
    g.drawString(name, yd1 + 28, yd2);
    //g.drawString(name) ;
    return true;
  }

  public boolean delete(Graphics g) {
    if (g == null) {
      System.out.println("errroe 1");
    }
    byte banjing = Board.BANJING;
    int yd1 = 0;
    int yd2 = 0;
    int hangju = Board.HANGJU;
    int liekuan = Board.LIEKUAN;
    int jianxi = Board.JIANXI;
    yd2 = (a - 1) * 2 * hangju + jianxi - banjing;
    yd1 = (b - 1) * 2 * liekuan + jianxi - banjing;
    //g.s
    //g.setColor(Color.lightGray);
    //g.fillOval(yd1,yd2,2*banjing,2*banjing);
    g.clearRect(yd1, yd2, 2 * banjing, 2 * banjing);
    //g.fillOval(a * banjing, b * banjing, banjing*2, banjing*2);
    if (shizi[a][b] == 1) {

      yd2 += banjing * 7 / 8;
      g.drawLine(yd1 + banjing / 8, yd2, yd1 + 7 * banjing / 8, yd2);
      g.drawLine(yd1 + banjing / 8, yd2 + banjing / 4, yd1 + 7 * banjing / 8,
                 yd2 + banjing / 4);
      g.drawLine(yd1 + 7 * banjing / 8, yd2 - banjing * 3 / 4,
                 yd1 + 7 * banjing / 8, yd2);
      g.drawLine(yd1 + 7 * banjing / 8, yd2 + banjing * 3 / 4,
                 yd1 + 7 * banjing / 8, yd2);
      //g.drawString(name) ;
    }
    return true;
  }

  //说明graphics对象不能这样传递；null错误；

  /*public Qizi(String string, byte ban, Color bc, Color fc) {
    name = string;
    banjing = ban;
    bcolor = bc;
    fcolor = fc;
    a = 0;
    b = 0;
       }*/

}
