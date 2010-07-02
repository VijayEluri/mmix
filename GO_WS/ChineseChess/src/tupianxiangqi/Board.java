package tupianxiangqi;

import java.awt.*;
import java.lang.*;
import java.math.*;
import java.awt.image.*;
import java.net.URL;
import javax.swing.ImageIcon;

public class Board
    extends Canvas {
  // boolean JIANJING = false; //避免全画。

  private Image work;
  private Image[] yuansu = new Image[16];

  boolean bukexia = false; //能够下子的标志。
  byte shengli = -1;
  public static final byte HONGFANG = 0;
  public static final byte HEIFANG = 1;
  //棋盘大小为470×520；
  public static final byte BANJING = 21; //棋子半径
  public static final byte LIEKUAN = 25; //列宽
  public static final byte HANGJU = 25; //行距
  public static final byte JIANXI = 30; //作上角与原点的距离。
  byte mycolor; //执黑还是执红。黑方需倒转棋盘，便于思考。
  byte turncolor = HONGFANG; //当前手轮谁走。
  byte qidiana, qidianb; //用于渐进方式画图。
  byte zdiana, zdianb; //前进到的点。
  byte wa = 0;
  byte wb = 0;
  private Graphics gcontext;
  Qizi[] qizi = new Qizi[33];
  byte[][] zb = new byte[12][11]; //下标1起；每点的现有子编号。
  //辅助qizi的初始化。
  byte [] zhuanghuan=new byte[33];
  byte[] zhl = new byte[33]; //兵种索引。
  byte[] qishia = new byte[33]; //每个子的起始行标
  byte[] qishib = new byte[33]; //每个棋子的起始列标。
  String[] mingzi = { //红方在前。
      "",
      "车", "马", "相", "仕", "帅", "仕", "相", "马", "车",
      "炮", "炮", "兵", "兵", "兵", "兵", "兵",
      "车", "马", "象", "士", "将", "士", "象", "马", "车",
      "炮", "炮", "卒", "卒", "卒", "卒", "卒", };
  //名字错了这里统一修改。

  byte[][] shixiang = new byte[12][11]; //辅助规则实现。
  byte[][] bingzu = new byte[12][11];
  byte[][] jiangshuai = new byte[12][11];

  byte[][] shizi = new byte[12][11]; //那个位置有十字。
  byte active = 0;
  byte yactive1 = 0;
  byte yactive2 = 0;
  public Board() {

    byte i = 0, j = 0;
    byte zishu = 0;
    String imagename;
    System.out.println("Board 初始化。");
    for (i = 1; i < 10; i++) { //每个棋子的起始位置。
      qishia[i] = 10;
      qishib[i] = i;
    }
    qishia[10] = 8;
    qishib[10] = 2;
    qishia[11] = 8;
    qishib[11] = 8;
    for (i = 0; i < 5; i++) { //兵的位置
      qishia[12 + i] = 7;
      qishib[12 + i] = (byte) (1 + 2 * i);
    }
    for (i = 17; i < 26; i++) {
      qishia[i] = 1;
      qishib[i] = (byte) (i - 16);
    }
    qishia[26] = 3;
    qishib[26] = 2;
    qishia[27] = 3;
    qishib[27] = 8;
    for (i = 0; i < 5; i++) {
      qishia[28 + i] = 4;
      qishib[28 + i] = (byte) (1 + 2 * i);
    }

    shizi[3][2] = 1; //有十字标志的点。
    shizi[3][8] = 1;
    shizi[4][1] = 2; //只画右半边
    shizi[4][3] = 1;
    shizi[4][5] = 1;
    shizi[4][7] = 1;
    shizi[4][9] = 3; //只画左半边

    shizi[8][2] = 1;
    shizi[8][8] = 1;
    shizi[7][1] = 2;
    shizi[7][3] = 1;
    shizi[7][5] = 1;
    shizi[7][7] = 1;
    shizi[7][9] = 3;

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

    //用于辅助实现规则。
    shixiang[1][4] = 1; //士的有效点。
    shixiang[1][6] = 1;
    shixiang[3][4] = 1;
    shixiang[3][6] = 1;
    shixiang[2][5] = 4;
    shixiang[10][4] = 3;
    shixiang[10][6] = 3;
    shixiang[8][4] = 3;
    shixiang[8][6] = 3;
    shixiang[9][5] = 4;

    shixiang[3][1] = 2; //象的有效点。
    shixiang[3][5] = 2;
    shixiang[3][9] = 2;
    shixiang[1][3] = 2;
    shixiang[1][7] = 2;
    shixiang[5][3] = 2;
    shixiang[5][7] = 2;
    shixiang[8][1] = 2;
    shixiang[8][5] = 2;
    shixiang[8][9] = 2;
    shixiang[10][3] = 2;
    shixiang[10][7] = 2;
    shixiang[6][3] = 2;
    shixiang[6][7] = 2;

    jiangshuai[1][4] = 1; //将帅的有效点。
    jiangshuai[1][5] = 1;
    jiangshuai[1][6] = 1;
    jiangshuai[2][4] = 1;
    jiangshuai[2][5] = 1;
    jiangshuai[2][6] = 1;
    jiangshuai[3][4] = 1;
    jiangshuai[3][5] = 1;
    jiangshuai[3][6] = 1;

    jiangshuai[10][4] = 1;
    jiangshuai[10][5] = 1;
    jiangshuai[10][6] = 1;
    jiangshuai[9][4] = 1;
    jiangshuai[9][5] = 1;
    jiangshuai[9][6] = 1;
    jiangshuai[8][4] = 1;
    jiangshuai[8][5] = 1;
    jiangshuai[8][6] = 1; //共18点。

    for (i = 1; i <= 16; i++) { //初始化棋子

      wa = qishia[i];
      wb = qishib[i];
      zb[wa][wb] = i;
      qizi[i] = new Qizi(i, zhl[i],
                         wa, wb, HONGFANG);
      System.out.print(qizi[i].bianhao);
      System.out.print(mingzi[qizi[i].bianhao]);
      System.out.print(qizi[i].zhonglei);
      System.out.println(qizi[i].color);
      //System.out.println(mingzi[qizi[i].bianhao]);
    }
    for (i = 17; i <= 32; i++) {
      wa = qishia[i];
      wb = qishib[i];
      zb[wa][wb] = i;
      qizi[i] = new Qizi(i, zhl[i], wa, wb, HEIFANG);
      System.out.print(qizi[i].bianhao);
      System.out.print(mingzi[qizi[i].bianhao]);
      System.out.print(qizi[i].zhonglei);
      System.out.println(qizi[i].color);
    }
    //add(picbutton);
    /*image1 = new ImageIcon(tupianxiangqi.Frame1.class.getResource("openFile.png"));
         image2 = new ImageIcon(tupianxiangqi.Frame1.class.getResource("closeFile.png"));
         image3 = new ImageIcon(tupianxiangqi.Frame1.class.getResource("help.png"));
     */

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
         }*/
    //不能正确初始化图像。
  }

  public void init() {
    /*image1 =   image2 = new ImageIcon(tupianxiangqi.Frame1.class.getResource("closeFile.png"));
         image3 = new ImageIcon(tupianxiangqi.Frame1.class.getResource("help.png"));
     */
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
      yuansu[i+7] = new ImageIcon(tupianxiangqi.Board.class.getResource(imagename)).
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
     work = this.createImage(470, 520);
   if (work == null) {
     System.out.println("work=null");
   }
   else {
     gcontext = work.getGraphics(); //gcontext未能正确赋值。
   }



  }

  public void update(Graphics g) {
    paint(g);
  }

  public void paint(Graphics g) {
    //setBackground(Color.orange);
    byte i = 0;
    byte j = 0;
    //int yd = 0, zd = 0;
    int yd1 = 0, yd2 = 0;
    URL url;
    String imagename;


    work = this.createImage(470, 520);
    if (work == null) {
      System.out.println("work=null");
    }
    else {
      gcontext = work.getGraphics(); //gcontext未能正确赋值。
    }

    gcontext.drawImage(yuansu[0], 0, 0,461,512, this);
    System.out.println("work!=null");
    // if (JIANJING == false) {
    for (i = 1; i < 33; i++) {
      if(qizi[i].a==0||qizi[i].b==0) continue;
      yd2 = (qizi[i].a - 1) * 2 * HANGJU + JIANXI - BANJING;
      yd1 = (qizi[i].b - 1) * 2 * LIEKUAN + JIANXI - BANJING;

      System.out.println("i=" + i);
      j=(byte)(zhuanghuan[zhl[i]] +1+ qizi[i].color * 7);
      System.out.println("i=" + j);
      gcontext.drawImage(yuansu[j], yd1, yd2,2*BANJING,2*BANJING, this);
      System.out.println("huaqizi");

    }
    //System.out.println("wok.w="+work.getWidth(this) );
    g.drawImage(work, 0, 0, 461, 512, this);
    System.out.println("画整个图");
    //}

  } //paint

}
