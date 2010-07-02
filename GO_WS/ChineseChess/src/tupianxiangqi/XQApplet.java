package tupianxiangqi;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;
import java.awt.Font;
import java.awt.Graphics;

public class XQApplet
    extends Applet {
  boolean JIANJING = false; //避免全画。
  public static final byte HONGFANG = 0;
  public static final byte HEIFANG = 1;
  public static final byte BANJING = 22; //棋子半径
  public static final byte LIEKUAN = 28; //列宽
  public static final byte HANGJU = 24; //行距
  public static final byte JIANXI = 35; //作上角与原点的距离。
  byte mycolor; //执黑还是执红。黑方需倒转棋盘，便于思考。
  byte turncolor = HONGFANG; //当前手轮谁走。
  byte qidiana, qidianb; //用于渐进方式画图。
  byte zdiana, zdianb; //前进到的点。
  byte wa = 0;
  byte wb = 0;

  Qizi2[] qizi = new Qizi2[33];
  byte[][] zb = new byte[12][11]; //下标1起；每点的现有子编号。
//辅助qizi的初始化。
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
  public XQApplet() {

    byte i = 0, j = 0;
    byte zishu = 0;
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
    //借用这个数组，表示边角，吃子后恢复横竖线的不同
    /*
         for(i=2;i<10;i++){
      shizi[i][1]=4;//少画左边横线；
      shizi[i][9]=5;//少画右边横线；
         }
         for(j=2;j<9;i++){
      shizi[1][j]=6;//少画左边横线；
      shizi[i][9]=4;//少画右边横线；
         }*/
    //未必好，放弃了

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
      qizi[i] = new Qizi2(i, zhl[i],
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
      qizi[i] = new Qizi2(i, zhl[i], wa, wb, HEIFANG);
      System.out.print(qizi[i].bianhao);
      System.out.print(mingzi[qizi[i].bianhao]);
      System.out.print(qizi[i].zhonglei);
      System.out.println(qizi[i].color);
    }
    repaint();

  }

  public void update(Graphics g) {
    paint(g);
  }

  public void draw(Graphics g, byte xb) {

    g.setColor(Color.lightGray);
    int yd2 = (qizi[xb].a - 1) * 2 * HANGJU + JIANXI - BANJING;
    int yd1 = (qizi[xb].b - 1) * 2 * LIEKUAN + JIANXI - BANJING;
    String name = mingzi[qizi[xb].bianhao];
    g.fillOval(yd1, yd2, 2 * BANJING, 2 * BANJING);
    if (qizi[xb].color == HONGFANG) {
      g.setColor(Color.red);
    }
    else {
      g.setColor(Color.black);
    }
    yd2 += 28;
    yd1 += 10;
    g.setFont(new Font("TimesRoman", Font.BOLD, 25));
    g.drawString(name, yd1, yd2);

  }

  public boolean mouseDown(Event e, int x, int y) {
    System.out.println("mousedown");
    byte m = 0, n = 0; //落点的行列下标。
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
    if (active == 0) { //指定要走的子。
      System.out.println("当前没有活跃点。");
      if (zb[m][n] > 0 && qizi[zb[m][n]].color == turncolor) {
        active = zb[m][n];
        System.out.println("turncolor：" + turncolor);
        JIANJING = true;
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
      byte ywza = qizi[active].a;
      byte ywzb = qizi[active].b;

      byte ca = (byte) (m - ywza);
      byte cb = (byte) (n - ywzb);
      byte actnew = zb[m][n]; //新的活动子的下标。
      byte xun = 0; //循环变量。
      System.out.print("已经指定要走的子。");
      System.out.print("a=" + ywza);
      System.out.println(";b=" + ywzb);
      System.out.print("actnew=" + actnew);
      System.out.print("qizi[active].zhonglei=" + qizi[active].zhonglei);
      if (actnew != 0 && qizi[active].color == qizi[actnew].color) {
        System.out.println("改变活动点");
        yactive1 = ywza;
        yactive2 = ywzb;
        active = zb[m][n];
        JIANJING = true;
        repaint();
        return true; //提子
      }
      switch (qizi[active].zhonglei) { //进行合法性判断。
        case 1: { //车。
          System.out.print("当前点为车");
          if (ca == 0) {
            if (cb == 0) {
              return true;
            }
            else if (cb > 0) {
              for (xun = (byte) (ywzb + 1); xun < n; xun++) {
                if (zb[m][xun] != 0) {
                  return true;
                }
              }
            }
            else if (cb < 0) {
              for (xun = (byte) (ywzb - 1); xun > n; xun--) {
                if (zb[m][xun] != 0) {
                  return true;
                }
              }

            }
            System.out.println("合乎规则");
            //合乎规则；
            break;
          }
          else if (cb == 0) {
            if (ca > 0) {
              for (xun = (byte) (ywza + 1); xun < m; xun++) {
                if (zb[xun][n] != 0) {
                  return true;
                }
              }
            }
            else if (ca < 0) {
              for (xun = (byte) (ywza - 1); xun > m; xun--) {
                if (zb[xun][n] != 0) {
                  return true;
                }
              }

            }
            System.out.println("合乎规则");
            break;
          }

          else {
            return true; //处理完毕
          }

        }
        case 2: //马。
          System.out.print("当前点为马,");
          {
            if (Math.abs(ca * cb) != 2) {
              System.out.println("不合规则。");
              return true;
            }
            if (Math.abs(ca) == 1) { //注意负值
              if (cb == 2) {
                if (zb[ywza][ywzb + 1] != 0) {
                  System.out.println("不合规则:别马腿。");
                  return true;
                }
              }
              else if (cb == -2) {
                if (zb[ywza][ywzb - 1] != 0) {
                  System.out.println("不合规则:别马腿。");
                  return true;
                }
              }
            }
            else if (ca == 2) {
              if (zb[ywza + 1][ywzb] != 0) {
                System.out.println("不合规则:别马腿。");
                return true;
              }
            }
            else if (ca == -2) {
              if (zb[ywza - 1][ywzb] != 0) {
                System.out.println("不合规则:别马腿。");
                return true;
              }
            }
            System.out.println("合乎规则");
            break;
          }
        case 3: {
          //炮。
          System.out.println("当前点为炮");
          byte count = 0;
          if (ca == 0) {
            if (cb == 0) {
              return true;
            }
            else if (cb > 0) {
              for (xun = (byte) (ywzb + 1); xun < n; xun++) {
                if (zb[m][xun] != 0) {
                  count += 1;
                }
              }
            }
            else if (cb < 0) {
              for (xun = (byte) (ywzb - 1); xun > n; xun--) {
                if (zb[m][xun] != 0) {
                  count += 1;
                }
              }

            }

          }
          else if (cb == 0) {
            System.out.println("竖向移动");
            if (ca > 0) {
              for (xun = (byte) (ywza + 1); xun < m; xun++) {
                if (zb[xun][n] != 0) {
                  count += 1;
                }
              }
            }
            else if (ca < 0) {
              for (xun = (byte) (ywza - 1); xun > m; xun--) {
                if (zb[xun][n] != 0) {
                  count += 1;
                }
              }

            }

          }

          else {
            return true; //处理完毕
          }
          if (actnew == 0) { //移动
            if (count > 0) {
              return true;
            }
            System.out.println("合乎规则");
            break;

          }
          else if (qizi[active].color != qizi[actnew].color) {
            //提子

            if (count != 1) {
              return true;
            }
            System.out.println("属于提子");

            System.out.println("合乎规则");
            break;
          }
          else if (qizi[active].color == qizi[actnew].color) {
            //active = zb[m][n];
            System.out.println("改变当前活跃点。");
          }
          /*else {
            return true;
            System.out.println("合乎规则");
                     }*/
          break;
        }
        case 4: { //仕
          System.out.println("当前点为士");
          if (Math.abs(ca) != 1 || Math.abs(cb) != 1) {
            return true;
          }
          else if (shixiang[m][n] == 0) {
            return true;
          }

          break;
        }
        case 5: { //相
          System.out.println("当前点为象");
          if (Math.abs(ca) != 2 || Math.abs(cb) != 2) {
            return true;
          }
          else if (shixiang[m][n] == 0) {
            return true;
          }
          else if (zb[ywza + ca / 2][ywzb + cb / 2] != 0) {
            System.out.println("别象腿");
            return true;
          }
          System.out.println("合乎规则");
          break;
        }
        case 6: { //兵
          System.out.println("当前点为兵");
          if (qizi[active].color == HONGFANG) {
            System.out.println("当前点为红兵");
            if (ca == -1) {
              if (cb != 0) {
                return true;
              }
            }
            else if (ca == 0 && ywza < 6 && Math.abs(cb) == 1) {

            }
            else {
              return true;
            }
          }
          else if (qizi[active].color == HEIFANG) { //黑方
            System.out.println("当前点为黑兵");
            if (ca == 1) {
              if (cb != 0) {
                System.out.println("不合乎规则:过河兵");
                return true;
              }
            }
            else if (ca == 0 && ywza > 5 && Math.abs(cb) == 1) {
              System.out.println("合乎规则:过河兵");
            }
            else {
              return true;
            }

          }
          System.out.println("合乎规则");
          break;

        }
        case 7: { //帅
          System.out.println("当前点为帅");
          if (ca == 0 && Math.abs(cb) == 1 || cb == 0 && Math.abs(ca) == 1) {
            if (jiangshuai[m][n] == 1) {

            }
            else {
              return true;

            }
          }
          else {
            return true;
          }
          System.out.println("合乎规则");
          break;
        } //合法性检查结束。

      } //switch结束
      System.out.println("switch结束");

      qizi[active].a = m;
      qizi[active].b = n;
      zb[ywza][ywzb] = 0;
      zb[m][n] = active;
      if (qizi[21].b == qizi[5].b) {
        //&&ywzb==qizi[5].b&&qizi[5].b!=n){
        //将帅同列且当前子从该列移出。
        System.out.println("将帅同列");
        byte jsjj = 0; //将帅之间的子树。
        for (byte tt = (byte) (qizi[21].a + 1); tt < qizi[5].a; tt++) {
          if (zb[tt][qizi[21].b] != 0) {
            jsjj++;
            break;
          }
        }
        if (jsjj == 0) {
          System.out.println("将帅会面，不合规则");
          qizi[active].a = ywza;
          qizi[active].b = ywzb;
          zb[ywza][ywzb] = active;
          zb[m][n] = actnew;
          return true;
        }

      }

      if (actnew == 0) { //移动
        System.out.println("目标点为空，属于移动");
        /*qizi[active].a = m;
                 qizi[active].b = n;
                 zb[m][n] = active;
                 zb[ywza][ywzb]=0;*/
        active = 0;

      }
      else if (qizi[active].color != qizi[actnew].color) {
        //提子
        System.out.println("目标点为实，吃子");
        qizi[actnew].a = 0;
        qizi[actnew].b = 0;
        /*zb[ywza][ywzb] = 0;
                 qizi[active].a = m;
                 qizi[active].b = n;
                 zb[m][n] = active;*/
        active = 0;

      }
      if (turncolor == HONGFANG) {
        turncolor = HEIFANG;
      }
      else if (turncolor == HEIFANG) {
        turncolor = HONGFANG;
      }
      JIANJING = true; //peihehuatu
      qidiana = ywza;
      qidianb = ywzb;
      zdiana = m;
      zdianb = n;
      repaint();

      /*else if (qizi[active].color == qizi[actnew].color) {
        active = zb[m][n];
        return true;
             }*/
      //同色的可能性已经排除

    }
    /* if (zb[m][n] == 0) {
       //合法性检查并赋值。
     }
     else {
     }*/

    return true;
  }

  public void paint(Graphics g) {

    byte i = 0;
    byte j = 0;
    int yd = 0, zd = 0;
    int yd1 = 0, yd2 = 0;
    String name;
    if (JIANJING == false) {
      g.setColor(Color.black);
      yd = JIANXI + LIEKUAN * 16;
      for (i = 1; i <= 10; i++) { //画横线

        zd = 2 * (i - 1) * HANGJU + JIANXI;
        g.drawLine(JIANXI, zd,
                   yd, zd);
      }
      yd = JIANXI + HANGJU * 8;
      yd1 = JIANXI + HANGJU * 10;
      yd2 = JIANXI + HANGJU * 18;
      g.drawLine(JIANXI, yd, JIANXI, yd1);
      for (i = 1; i < 10; i++) { //画竖线

        zd = 2 * (i - 1) * LIEKUAN + JIANXI;
        g.drawLine(zd, JIANXI, zd, yd);

        g.drawLine(zd, yd1, zd, yd2);
      }
      g.drawLine(zd, yd, zd, yd1);

      //画斜线
      g.drawLine(JIANXI + 6 * LIEKUAN, JIANXI, JIANXI + 10 * LIEKUAN,
                 JIANXI + 4 * HANGJU);
      g.drawLine(JIANXI + 10 * LIEKUAN, JIANXI, JIANXI + 6 * LIEKUAN,
                 JIANXI + 4 * HANGJU);
      g.drawLine(JIANXI + 6 * LIEKUAN, JIANXI + 14 * HANGJU,
                 JIANXI + 10 * LIEKUAN, JIANXI + 18 * HANGJU);
      g.drawLine(JIANXI + 10 * LIEKUAN, JIANXI + 14 * HANGJU,
                 JIANXI + 6 * LIEKUAN, JIANXI + 18 * HANGJU);

      for (i = 1; i < 11; i++) {
        for (j = 1; j < 10; j++) {
          huashizi(g, i, j);
        }
      }

      g.setFont(new Font("TimesRoman", Font.BOLD, 25));
      //似乎这几步很费时间。因为显示出棋盘之后，停顿后才显示棋子。
      for (i = 1; i <= 16; i++) {
        g.setColor(Color.lightGray);
        yd2 = (qizi[i].a - 1) * 2 * HANGJU + JIANXI - BANJING;
        yd1 = (qizi[i].b - 1) * 2 * LIEKUAN + JIANXI - BANJING;
        name = mingzi[qizi[i].bianhao]; ;
        g.fillOval(yd1, yd2, 2 * BANJING, 2 * BANJING);
        g.setColor(Color.red);
        yd2 += 28;
        yd1 += 10;
        //g.setFont(new Font("TimesRoman",Font.BOLD,25)) ;
        g.drawString(name, yd1, yd2);
      }

      for (i = 17; i <= 32; i++) {
        g.setColor(Color.lightGray);
        yd2 = (qizi[i].a - 1) * 2 * HANGJU + JIANXI - BANJING;
        yd1 = (qizi[i].b - 1) * 2 * LIEKUAN + JIANXI - BANJING;
        name = mingzi[qizi[i].bianhao];
        g.fillOval(yd1, yd2, 2 * BANJING, 2 * BANJING);
        g.setColor(Color.black);
        yd2 += 28;
        yd1 += 10;
        g.drawString(name, yd1, yd2);

      }
      if (active != 0) { //该点被激活。

        System.out.println("该点被激活。");
        qidiana = qizi[active].a;
        qidianb = qizi[active].b;
        yd2 = (qidiana - 1) * 2 * HANGJU + JIANXI - BANJING;
        yd1 = (qidianb - 1) * 2 * LIEKUAN + JIANXI - BANJING;
        g.setColor(Color.white);
        g.drawOval(yd1 + 1, yd2 + 1, 2 * BANJING - 3, 2 * BANJING - 3);
        System.out.println("白圈表示激活。");
        qidiana = 0;
        qidianb = 0;
        JIANJING = false;
      }

    }
    else if (JIANJING == true) {
      if (active != 0) { //该点被激活。
        if (yactive1 != 0) {
          System.out.println("改变激活点。");
          yd2 = (yactive1 - 1) * 2 * HANGJU + JIANXI - BANJING;
          yd1 = (yactive2 - 1) * 2 * LIEKUAN + JIANXI - BANJING;

          g.setColor(Color.lightGray);
          g.drawOval(yd1 + 1, yd2 + 1, 2 * BANJING - 3, 2 * BANJING - 3);
          yactive1 = 0;
          yactive2 = 0;
        }
        System.out.println("该点被激活。");
        qidiana = qizi[active].a;
        qidianb = qizi[active].b;
        yd2 = (qidiana - 1) * 2 * HANGJU + JIANXI - BANJING;
        yd1 = (qidianb - 1) * 2 * LIEKUAN + JIANXI - BANJING;
        g.setColor(Color.white);
        g.drawOval(yd1 + 1, yd2 + 1, 2 * BANJING - 3, 2 * BANJING - 3);
        System.out.println("白圈表示激活。");
        qidiana = 0;
        qidianb = 0;
        JIANJING = false;
      }
      else { //移动或者提子。
        System.out.println("移动或者提子");
        yd2 = (qidiana - 1) * 2 * HANGJU + JIANXI - BANJING;
        yd1 = (qidianb - 1) * 2 * LIEKUAN + JIANXI - BANJING;
        //name = qizi[i].name;
        //g.fillOval(yd1, yd2, 2 * BANJING, 2 * BANJING);
        System.out.println("原点a=" + qidiana);
        System.out.println("原点b=" + qidianb);

        g.clearRect(yd1, yd2, 2 * BANJING, 2 * BANJING);
        if (qidiana == 1 || (qidiana == 6&&qidianb!=1&&qidianb!=9)) {}
        else{
          g.drawLine(yd1 + BANJING, yd2 + BANJING, yd1 + BANJING, yd2);
          //补画竖线:上半截
        }
        if (qidiana == 10 || (qidiana == 5&&qidianb!=1&&qidianb!=9)) {}
        else{
          g.drawLine(yd1 + BANJING, yd2 + BANJING * 2, yd1 + BANJING,
                     yd2 + BANJING);

        } //补画竖线:下半截

        //g.drawLine(yd1 + BANJING, yd2 + BANJING * 2, yd1 + BANJING, yd2);
        if (qidianb != 1) { //横线
          g.drawLine(yd1 + BANJING, yd2 + BANJING, yd1, yd2 + BANJING);
        } //左半横线
        if (qidianb != 9) {
          g.drawLine(yd1 + BANJING * 2, yd2 + BANJING, yd1 + BANJING,
                     yd2 + BANJING);

        }
        //g.drawLine(yd1 + BANJING * 2, yd2 + BANJING, yd1, yd2 + BANJING);
        System.out.println("补画直线");
        huashizi(g, qidiana, qidianb);

        System.out.println("补画标志");

        if (shixiang[qidiana][qidianb] != 0) {
          System.out.println("补画斜线");
          switch (shixiang[qidiana][qidianb]) {
            case 1: {
              yd2 = (qidiana - 1) * 2 * HANGJU + JIANXI;
              yd1 = (qidianb - 1) * 2 * LIEKUAN + JIANXI;
              int zhongxin1 = 8 * LIEKUAN + JIANXI;
              int zhongxin2 = 2 * HANGJU + JIANXI;
              g.drawLine(yd1, yd2, (yd1 + zhongxin1) / 2, (yd2 + zhongxin2) / 2);
              System.out.println("补画黑方斜线");

              break;
            }
            case 2: {

              break;
            }
            case 3: {
              yd2 = (qidiana - 1) * 2 * HANGJU + JIANXI;
              yd1 = (qidianb - 1) * 2 * LIEKUAN + JIANXI;
              int zhongxin1 = 8 * LIEKUAN + JIANXI;
              int zhongxin2 = 16 * HANGJU + JIANXI;
              g.drawLine(yd1, yd2, (yd1 + zhongxin1) / 2, (yd2 + zhongxin2) / 2);
              System.out.println("补画红方斜线");

              break;
            }
            case 4: {
              yd2 = (qidiana - 1) * 2 * HANGJU + JIANXI;
              yd1 = (qidianb - 1) * 2 * LIEKUAN + JIANXI;
              g.drawLine(yd1 - LIEKUAN, yd2 - HANGJU, yd1 + LIEKUAN,
                         yd2 + HANGJU);
              g.drawLine(yd1 - LIEKUAN, yd2 + HANGJU, yd1 + LIEKUAN,
                         yd2 - HANGJU);

              System.out.println("补画红方h或黑方四斜线");
              break;
            }
          }

        }
        draw(g, zb[zdiana][zdianb]);
        System.out.print("画新的子位于");
        System.out.print("a=" + zdiana);
        System.out.println("b=" + zdianb);
        qidiana = 0;
        qidianb = 0;
        zdiana = 0;
        zdianb = 0;
        JIANJING = false;
      }
    } //else Jianjinf==true;
    JIANJING = false;
  } //paint

  public void huashizi(Graphics g, byte i, byte j) {

    int yd1, yd2;
    yd2 = (i - 1) * 2 * HANGJU + JIANXI - BANJING;
    yd1 = (j - 1) * 2 * LIEKUAN + JIANXI - BANJING;
    yd2 += BANJING * 7 / 8;
    if (shizi[i][j] == 1) {

      g.drawLine(yd1 + BANJING / 8, yd2, yd1 + 7 * BANJING / 8, yd2); //画横线
      g.drawLine(yd1 + BANJING * 9 / 8 + 1, yd2, yd1 + 15 * BANJING / 8 + 1,
                 yd2); //画横线
      g.drawLine(yd1 + BANJING / 8, yd2 + BANJING / 4 + 1,
                 yd1 + 7 * BANJING / 8, yd2 + BANJING / 4 + 1);
      g.drawLine(yd1 + BANJING * 9 / 8 + 1, yd2 + BANJING / 4 + 1,
                 yd1 + 15 * BANJING / 8 + 1, yd2 + BANJING / 4 + 1); //画横线
      g.drawLine(yd1 + 7 * BANJING / 8, yd2 - BANJING * 3 / 4,
                 yd1 + 7 * BANJING / 8, yd2); //画直线
      g.drawLine(yd1 + 7 * BANJING / 8, yd2 + BANJING / 4 + 1,
                 yd1 + 7 * BANJING / 8, yd2 + BANJING + 1);
      g.drawLine(yd1 + 9 * BANJING / 8 + 1, yd2 - BANJING * 3 / 4,
                 yd1 + 9 * BANJING / 8 + 1, yd2); //画直线
      g.drawLine(yd1 + 9 * BANJING / 8 + 1, yd2 + BANJING / 4 + 1,
                 yd1 + 9 * BANJING / 8 + 1, yd2 + BANJING + 1);

    }
    else if (shizi[i][j] == 2) { //左边抹去、
      g.drawLine(yd1 + BANJING * 9 / 8 + 1, yd2, 1 + yd1 + 15 * BANJING / 8,
                 yd2); //画横线
      g.drawLine(yd1 + BANJING * 9 / 8 + 1, yd2 + BANJING / 4 + 1,
                 yd1 + 15 * BANJING / 8 + 1, yd2 + BANJING / 4 + 1); //画横线
      g.drawLine(yd1 + 9 * BANJING / 8 + 1, yd2 - BANJING * 3 / 4,
                 yd1 + 9 * BANJING / 8 + 1, yd2); //画直线
      g.drawLine(yd1 + 9 * BANJING / 8 + 1, yd2 + BANJING / 4 + 1,
                 yd1 + 9 * BANJING / 8 + 1, yd2 + BANJING + 1);

    }
    else if (shizi[i][j] == 3) {

      g.drawLine(yd1 + BANJING / 8, yd2, yd1 + 7 * BANJING / 8, yd2); //画横线
      g.drawLine(yd1 + BANJING / 8, yd2 + BANJING / 4 + 1,
                 yd1 + 7 * BANJING / 8, yd2 + BANJING / 4 + 1);
      g.drawLine(yd1 + 7 * BANJING / 8, yd2 - BANJING * 3 / 4,
                 yd1 + 7 * BANJING / 8, yd2); //画直线
      g.drawLine(yd1 + 7 * BANJING / 8, yd2 + BANJING / 4 + 1,
                 yd1 + 7 * BANJING / 8, yd2 + BANJING + 1);
    }

  }

}

class Qizi2 {
//static byte[][] shizi = new byte[12][11]; //那个位置有十字。
//String name; //其实可以省略。
  byte zhonglei; //兵种1－7；
  byte bianhao = 0; //棋子编号1-32
  byte color = 0; //红或黑
  byte a = 0; //当前位置，1开始。
  byte b = 0; //初始位置

  public Qizi2(byte bh, byte zl, byte weia, byte weib, byte col) {
    a = weia;
    b = weib;
    bianhao = bh;
    zhonglei = zl;
    color = col;
  }

}



