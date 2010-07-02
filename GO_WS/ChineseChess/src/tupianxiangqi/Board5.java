package tupianxiangqi;

//package tupianxiangqi;

/**
 * <p>Title:客户端增加旁观的功能 </p>
 * <p>Description:根据服务器的启动信号决定 </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:开始时画空棋盘 </p>
 * @author not attributable
 * @version 1.0
 */

import java.awt.*;
import java.lang.*;
import java.math.*;
import java.awt.image.*;
import java.net.URL;
import javax.swing.ImageIcon;


public class Board5
  extends Canvas {

  XiangQiGuiZe xqgz;

  private Image work; //画面外缓冲图像
  private Image[] yuansu = new Image[16];
  private Graphics gcontext;

  // Daojishi djs1, djs2;

  boolean kexia = false; //能够下子的标志。
  byte shengli = -1; //0为红方胜，1为黑方胜；

  public static final byte HONGFANG = 0;
  public static final byte HEIFANG = 1;

  //棋盘大小为470×520；
  public static final byte BANJING = 21; //棋子半径
  public static final byte LIEKUAN = 25; //列宽
  public static final byte HANGJU = 25; //行距
  public static final byte JIANXI = 30; //作上角与原点的距离。

  byte mycolor; //执黑还是执红。黑方需倒转棋盘，便于思考。
  byte turncolor = HONGFANG; //当前手轮谁走。

  //byte qidiana, qidianb; //用于渐进方式画图。
  //byte zdiana, zdianb; //前进到的点。


  //Qizi[] qizi = new Qizi[33];
  //byte[][] zb = new byte[12][11]; //下标1起；每点的现有子编号。
  //辅助qizi的初始化。
  byte[] zhuanghuan = new byte[33]; //根据棋子编号得到图片名称
  byte[] zhl = new byte[33]; //兵种索引。
  //byte[] qishia = new byte[33]; //每个子的起始行标
  // byte[] qishib = new byte[33]; //每个棋子的起始列标。
  /*String[] mingzi = { //红方在前。
      "",
      "车", "马", "相", "仕", "帅", "仕", "相", "马", "车",
      "炮", "炮", "兵", "兵", "兵", "兵", "兵",
      "车", "马", "象", "士", "将", "士", "象", "马", "车",
      "炮", "炮", "卒", "卒", "卒", "卒", "卒", };*/
  //名字错了这里统一修改。

  //byte[][] shixiang = new byte[12][11]; //辅助规则实现。
  //byte[][] bingzu = new byte[12][11];
  //byte[][] jiangshuai = new byte[12][11];

  //byte[][] shizi = new byte[12][11]; //那个位置有十字。
  byte active = 0;
  //byte yactive1 = 0;
  //byte yactive2 = 0;
  public boolean mouseDown(Event e, int x, int y) {
    System.out.println("mousedown");
    byte m = 0, n = 0; //落点的行列下标。
    byte ind;
    if (kexia == true) {
      return true;
    }
    n = (byte) ( (x - JIANXI + LIEKUAN) / (2 * LIEKUAN) + 1);
    m = (byte) ( (y - JIANXI + HANGJU) / (2 * HANGJU) + 1);
    System.out.println("m=" + m);
    System.out.println("n=" + n);
    if (m >= 1 && m <= 10 && n >= 1 && n <= 9) {
      System.out.println("着点在棋盘内。");
    }
    else {
      System.out.println("着点在棋盘外。");
      return true; //直接推出、
    }
    if (active == 0) { //尚未指定要走的子。
      System.out.println("当前没有活跃点。");
      ind = xqgz.zb[m][n];
      if (ind > 0 && xqgz.qizi[ind].color == turncolor) {
        active = ind;
        System.out.println("turncolor：" + turncolor);
        repaint();
        return true;
      }
      else {
        System.out.println("空点或对方点");
        return true;
      }
    }
    else { //已经指定要走的子。
      System.out.println("active=" + active);
      byte ywza = xqgz.qizi[active].a;
      byte ywzb = xqgz.qizi[active].b;


      byte actnew = xqgz.zb[m][n]; //新的活动子的下标。
      byte xun = 0; //循环变量。
      System.out.print("已经指定要走的子。");
      System.out.print("a=" + ywza);
      System.out.println(";b=" + ywzb);
     // System.out.print("actnew=" + actnew);
      //System.out.print("qizi[active].zhonglei=" + qizi[active].zhonglei);
      if (actnew != 0 && xqgz.qizi[active].color == xqgz.qizi[actnew].color) {
        System.out.println("改变活动点");
        active = xqgz.zb[m][n];
        repaint();
        return true; //提子
      }
      else if (xqgz.receiveData(turncolor, ywza, ywzb, m, n) == true) {
        repaint();
        return true;
      }

      return true;
    }
  }

  public Board5() {
    // public Board5(Daojishi dj1, Daojishi dj2) {
    byte i = 0, j = 0;
    byte zishu = 0;
    byte wa = 0;
    byte wb = 0;
    String imagename;
    System.out.println("Board 初始化。");
    //djs1 = dj1;
    //djs2 = dj2;
    xqgz = new XiangQiGuiZe();


    //共十四个标志
    //车马炮士象兵帅。
    zhl[1] = 1; //每个棋子的兵种
    zhl[2] = 2;
    zhl[3] = 5;
    zhl[4] = 4;
    zhl[5] = 7;
    zhl[6] = 4;
    zhl[7] = 5;
    zhl[8] = 2;
    zhl[9] = 1;
    zhl[10] = 3;
    zhl[11] = 3;
    zhl[12] = 6;
    zhl[13] = 6;
    zhl[14] = 6;
    zhl[15] = 6;
    zhl[16] = 6;
    zhl[17] = 1; //每个棋子的兵种
    zhl[18] = 2;
    zhl[19] = 5;
    zhl[20] = 4;
    zhl[21] = 7;
    zhl[22] = 4;
    zhl[23] = 5;
    zhl[24] = 2;
    zhl[25] = 1;
    zhl[26] = 3;
    zhl[27] = 3;
    zhl[28] = 6;
    zhl[29] = 6;
    zhl[30] = 6;
    zhl[31] = 6; //原来此处的31为30，导致31的zhonglei为红，
    //错误奇怪。
    zhl[32] = 6;

    zhuanghuan[1] = 4;
    zhuanghuan[2] = 3;
    zhuanghuan[3] = 5;
    zhuanghuan[4] = 1;
    zhuanghuan[5] = 2;
    zhuanghuan[6] = 6;
    zhuanghuan[7] = 0;





  }

  public void init() {

    byte i;
    String imagename;

    for (i = 1; i <= 7; i++) { //hong fang zai qian.
      imagename = "Red" + (i - 1) + ".jpg";
      yuansu[i] = new ImageIcon(tupianxiangqi.Board.class.getResource(imagename)).
        getImage();
      System.out.println("imagenameuan=" + imagename);
      System.out.println("yuansu[i]=" + yuansu[i]);
      System.out.println("yuansu[i]=" + yuansu[i].getSource());

      imagename = "Blue" + (i - 1) + ".jpg";
      yuansu[i +
        7] = new ImageIcon(tupianxiangqi.Board.class.getResource(imagename)).
        getImage();

      //yuansu[i]=this.getImage(this.getCodeBase(),"Blue"+zhl[i]+".jpg") ;;
      System.out.println("imagenameuan=" + imagename);
      System.out.println("yuansu[i]=" + yuansu[i]);
      System.out.println("yuansu[i]=" + yuansu[i].getSource());
    }
    yuansu[0] = new ImageIcon(tupianxiangqi.Board.class.getResource("Board.jpg")).
      getImage();

    System.out.println("yuansu[i]=" + yuansu[i]);
    System.out.println("yuansu[i]=" + yuansu[i].getSource());

    yuansu[15] = new ImageIcon(tupianxiangqi.Board.class.getResource(
      "pieceTex.jpg")).getImage();

    System.out.println("yuansu[i]=" + yuansu[i]);
    System.out.println("yuansu[i]=" + yuansu[i].getSource());

  }

  public void update(Graphics g) {
    paint(g);
  }

  public void paint(Graphics g) {
    //setBackground(Color.orange);
    byte i = 0;
    byte j = 0;
    int yd1 = 0, yd2 = 0;
    byte qidiana, qidianb;
    work = this.createImage(461, 512);
    if (work == null) { //只能放在这里，否则不能正确初始化
      System.out.println("work=null");
    }
    else {
      System.out.println("work!=null");
      gcontext = work.getGraphics(); //gcontext未能正确赋值。
      if (gcontext == null) {
        return;
      }
      System.out.println("gcontext!=null");
    }

    gcontext.drawImage(yuansu[0], 0, 0, 461, 512, this);

    for (i = 1; i < 33; i++) { //遍历棋子。
      if (xqgz.qizi[i].a == 0 || xqgz.qizi[i].b == 0) {
        continue;
      }
      yd2 = (xqgz.qizi[i].a - 1) * 2 * HANGJU + JIANXI - BANJING;
      yd1 = (xqgz.qizi[i].b - 1) * 2 * LIEKUAN + JIANXI - BANJING;

      //System.out.print("i=" + i);
      j = (byte) (zhuanghuan[zhl[i]] + 1 + xqgz.qizi[i].color * 7);
      //System.out.println(";j=" + j);
      gcontext.drawImage(yuansu[j], yd1, yd2, 2 * BANJING, 2 * BANJING, this);
      //System.out.println("huaqizi");

    }

    if (active != 0) { //该点被激活。
      System.out.println("该点被激活。");

      yd2 = (xqgz.qizi[active].a - 1) * 2 * HANGJU + JIANXI - BANJING;
      yd1 = (xqgz.qizi[active].b - 1) * 2 * LIEKUAN + JIANXI - BANJING;
      gcontext.setColor(Color.green);
      gcontext.drawRect(yd1 - 1, yd2 - 1, 2 * BANJING + 2, 2 * BANJING + 2);
      System.out.println("绿框表示激活。");

    }
    g.drawImage(work, 0, 0, 461, 512, this);
    System.out.println("画整个图");
    if (shengli == HONGFANG) {
      g.setColor(Color.red);
      g.setFont(new Font("Courier", Font.BOLD, 25));
      g.drawString("红方胜", 480, 50);
    }
    else if (shengli == HEIFANG) {
      g.setColor(Color.black);
      g.setFont(new Font("Courier", Font.BOLD, 25));
      g.drawString("黑方胜", 480, 50);
    }

  } //paint

}
