package tupianxiangqi;

//package tupianxiangqi;

/**
 * <p>Title:�ͻ��������Թ۵Ĺ��� </p>
 * <p>Description:���ݷ������������źž��� </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company:��ʼʱ�������� </p>
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

  private Image work; //�����⻺��ͼ��
  private Image[] yuansu = new Image[16];
  private Graphics gcontext;

  // Daojishi djs1, djs2;

  boolean kexia = false; //�ܹ����ӵı�־��
  byte shengli = -1; //0Ϊ�췽ʤ��1Ϊ�ڷ�ʤ��

  public static final byte HONGFANG = 0;
  public static final byte HEIFANG = 1;

  //���̴�СΪ470��520��
  public static final byte BANJING = 21; //���Ӱ뾶
  public static final byte LIEKUAN = 25; //�п�
  public static final byte HANGJU = 25; //�о�
  public static final byte JIANXI = 30; //���Ͻ���ԭ��ľ��롣

  byte mycolor; //ִ�ڻ���ִ�졣�ڷ��赹ת���̣�����˼����
  byte turncolor = HONGFANG; //��ǰ����˭�ߡ�

  //byte qidiana, qidianb; //���ڽ�����ʽ��ͼ��
  //byte zdiana, zdianb; //ǰ�����ĵ㡣


  //Qizi[] qizi = new Qizi[33];
  //byte[][] zb = new byte[12][11]; //�±�1��ÿ��������ӱ�š�
  //����qizi�ĳ�ʼ����
  byte[] zhuanghuan = new byte[33]; //�������ӱ�ŵõ�ͼƬ����
  byte[] zhl = new byte[33]; //����������
  //byte[] qishia = new byte[33]; //ÿ���ӵ���ʼ�б�
  // byte[] qishib = new byte[33]; //ÿ�����ӵ���ʼ�бꡣ
  /*String[] mingzi = { //�췽��ǰ��
      "",
      "��", "��", "��", "��", "˧", "��", "��", "��", "��",
      "��", "��", "��", "��", "��", "��", "��",
      "��", "��", "��", "ʿ", "��", "ʿ", "��", "��", "��",
      "��", "��", "��", "��", "��", "��", "��", };*/
  //���ִ�������ͳһ�޸ġ�

  //byte[][] shixiang = new byte[12][11]; //��������ʵ�֡�
  //byte[][] bingzu = new byte[12][11];
  //byte[][] jiangshuai = new byte[12][11];

  //byte[][] shizi = new byte[12][11]; //�Ǹ�λ����ʮ�֡�
  byte active = 0;
  //byte yactive1 = 0;
  //byte yactive2 = 0;
  public boolean mouseDown(Event e, int x, int y) {
    System.out.println("mousedown");
    byte m = 0, n = 0; //���������±ꡣ
    byte ind;
    if (kexia == true) {
      return true;
    }
    n = (byte) ( (x - JIANXI + LIEKUAN) / (2 * LIEKUAN) + 1);
    m = (byte) ( (y - JIANXI + HANGJU) / (2 * HANGJU) + 1);
    System.out.println("m=" + m);
    System.out.println("n=" + n);
    if (m >= 1 && m <= 10 && n >= 1 && n <= 9) {
      System.out.println("�ŵ��������ڡ�");
    }
    else {
      System.out.println("�ŵ��������⡣");
      return true; //ֱ���Ƴ���
    }
    if (active == 0) { //��δָ��Ҫ�ߵ��ӡ�
      System.out.println("��ǰû�л�Ծ�㡣");
      ind = xqgz.zb[m][n];
      if (ind > 0 && xqgz.qizi[ind].color == turncolor) {
        active = ind;
        System.out.println("turncolor��" + turncolor);
        repaint();
        return true;
      }
      else {
        System.out.println("�յ��Է���");
        return true;
      }
    }
    else { //�Ѿ�ָ��Ҫ�ߵ��ӡ�
      System.out.println("active=" + active);
      byte ywza = xqgz.qizi[active].a;
      byte ywzb = xqgz.qizi[active].b;


      byte actnew = xqgz.zb[m][n]; //�µĻ�ӵ��±ꡣ
      byte xun = 0; //ѭ��������
      System.out.print("�Ѿ�ָ��Ҫ�ߵ��ӡ�");
      System.out.print("a=" + ywza);
      System.out.println(";b=" + ywzb);
     // System.out.print("actnew=" + actnew);
      //System.out.print("qizi[active].zhonglei=" + qizi[active].zhonglei);
      if (actnew != 0 && xqgz.qizi[active].color == xqgz.qizi[actnew].color) {
        System.out.println("�ı���");
        active = xqgz.zb[m][n];
        repaint();
        return true; //����
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
    System.out.println("Board ��ʼ����");
    //djs1 = dj1;
    //djs2 = dj2;
    xqgz = new XiangQiGuiZe();


    //��ʮ�ĸ���־
    //������ʿ���˧��
    zhl[1] = 1; //ÿ�����ӵı���
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
    zhl[17] = 1; //ÿ�����ӵı���
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
    zhl[31] = 6; //ԭ���˴���31Ϊ30������31��zhongleiΪ�죬
    //������֡�
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
    if (work == null) { //ֻ�ܷ��������������ȷ��ʼ��
      System.out.println("work=null");
    }
    else {
      System.out.println("work!=null");
      gcontext = work.getGraphics(); //gcontextδ����ȷ��ֵ��
      if (gcontext == null) {
        return;
      }
      System.out.println("gcontext!=null");
    }

    gcontext.drawImage(yuansu[0], 0, 0, 461, 512, this);

    for (i = 1; i < 33; i++) { //�������ӡ�
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

    if (active != 0) { //�õ㱻���
      System.out.println("�õ㱻���");

      yd2 = (xqgz.qizi[active].a - 1) * 2 * HANGJU + JIANXI - BANJING;
      yd1 = (xqgz.qizi[active].b - 1) * 2 * LIEKUAN + JIANXI - BANJING;
      gcontext.setColor(Color.green);
      gcontext.drawRect(yd1 - 1, yd2 - 1, 2 * BANJING + 2, 2 * BANJING + 2);
      System.out.println("�̿��ʾ���");

    }
    g.drawImage(work, 0, 0, 461, 512, this);
    System.out.println("������ͼ");
    if (shengli == HONGFANG) {
      g.setColor(Color.red);
      g.setFont(new Font("Courier", Font.BOLD, 25));
      g.drawString("�췽ʤ", 480, 50);
    }
    else if (shengli == HEIFANG) {
      g.setColor(Color.black);
      g.setFont(new Font("Courier", Font.BOLD, 25));
      g.drawString("�ڷ�ʤ", 480, 50);
    }

  } //paint

}
