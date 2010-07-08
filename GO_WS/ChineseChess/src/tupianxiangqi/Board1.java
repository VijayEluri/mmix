package tupianxiangqi;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

import java.awt.*;
import java.lang.*;
import java.math.*;
import java.awt.image.*;
import java.net.URL;
import javax.swing.ImageIcon;

public class Board1
  extends Canvas {

  ClsClient myclient;
  byte[][] hui = new byte[128][6];
  byte shoushu = 0;
  byte[] data = new byte[1024];
  //0:被吃子的编号，1颜色2.3为起点坐标，
  //4，5为终点坐标。

  private Image work; //画面外缓冲图像
  private Image[] yuansu = new Image[16];
  private Graphics gcontext;

  Daojishi djs1, djs2;

  boolean bukexia = true; //能够下子的标志。
  byte shengli = -1; //0为红方胜，1为黑方胜；
  public static final byte HONGFANG = 0;
  public static final byte HEIFANG = 1;
  //棋盘大小为470×520；
  public static final byte BANJING = 21; //棋子半径
  public static final byte LIEKUAN = 25; //列宽
  public static final byte HANGJU = 25; //行距
  public static final byte JIANXI = 30; //作上角与原点的距离。

  byte mycolor; //执黑还是执红。黑方需倒转棋盘，便于思考。
  byte ocolor;
  //>1表示观战。
  byte state = 0; //表示还没有开始。
  byte turncolor = HONGFANG; //当前手轮谁走。

  //byte qidiana, qidianb; //用于渐进方式画图。
  //byte zdiana, zdianb; //前进到的点。

  Qizi[] qizi = new Qizi[33];
  byte[][] zb = new byte[12][11]; //下标1起；每点的现有子编号。
  //辅助qizi的初始化。
  byte[] zhuanghuan = new byte[33];
  byte[] zhl = new byte[33]; //兵种索引。
  byte[] qishia = new byte[33]; //每个子的起始行标
  byte[] qishib = new byte[33]; //每个棋子的起始列标。

  byte[][] shixiang = new byte[12][11]; //辅助规则实现。
  byte[][] bingzu = new byte[12][11];
  byte[][] jiangshuai = new byte[12][11];

  byte active = 0;
  //byte yactive1 = 0;
  //byte yactive2 = 0;

  public void huiqi() {
    byte tizibianhao = hui[shoushu][0];
    byte qiziyanse = hui[shoushu][1];
    byte qidiana = hui[shoushu][2];
    byte qidianb = hui[shoushu][3];
    byte zhongdiana = hui[shoushu][4];
    byte zhongdianb = hui[shoushu][5];
    byte sss = zb[zhongdiana][zhongdianb]; //qizibianaho

    qizi[sss].a = qidiana;
    qizi[sss].b = qidianb;
    zb[qidiana][qidianb] = sss;
    if (tizibianhao > 0) { //提子

      qizi[tizibianhao].a = zhongdiana;
      qizi[tizibianhao].b = zhongdianb;
      zb[zhongdiana][zhongdianb] = tizibianhao;
    }
    else if (tizibianhao == 0) { //没有提子。
      zb[zhongdiana][zhongdianb] = 0;
    }
    hui[shoushu][0] = 0;
    hui[shoushu][1] = 0;
    hui[shoushu][2] = 0;
    hui[shoushu][3] = 0;
    hui[shoushu][4] = 0;
    hui[shoushu][5] = 0;
    shoushu--;
    if (turncolor == CS.HONGFANG) {
      turncolor = CS.HEIFANG;
      System.out.println("yuanwei:hongfang;县委黑方");
      /*djs1.stop();
      djs2.init();
      djs2.start();*/
    }
    else if (turncolor == CS.HEIFANG) {
      turncolor = CS.HONGFANG;
      System.out.println("yuanwei:红方;县委hongfang");

      /*djs2.stop();
      djs1.init();
      djs1.start();*/
    }
    repaint();
  }

  public void jieshouqipu(byte[] datain) {
    System.out.println("接收到棋谱，在board中处理");

    byte qiziyanse = datain[3];
    byte qidiana = datain[4];
    byte qidianb = datain[5];
    byte zhongdiana = datain[6];
    byte zhongdianb = datain[7];
    byte sss = zb[zhongdiana][zhongdianb];
    byte s = zb[qidiana][qidianb];
    if (qiziyanse != turncolor) {
      System.out.println("qipu颜色错误。");
    }
    if(sss!=datain[2]){
      System.out.println("qipu提子编号错误。");
    }
    zb[zhongdiana][zhongdianb] = zb[qidiana][qidianb];
    qizi[s].a = zhongdiana;
    qizi[s].b = zhongdianb;
    zb[qidiana][qidianb] = 0;
    if (sss > 0) {
      qizi[sss].a = 0;
      qizi[sss].b = 0;
      //hui[shoushu][0] = sss;
    }
    if (turncolor == CS.HONGFANG) {
      turncolor = CS.HEIFANG;
      djs1.stop();
      System.out.print("收到棋谱后红方计时器 djs1.stop()");
      System.out.println("棋谱为：" + qiziyanse + qidiana + qidianb
                         + zhongdiana + zhongdianb);

      djs2.init();
      djs2.start();
    }
    else if (turncolor == CS.HEIFANG) {
      turncolor = CS.HONGFANG;
      djs2.stop();
      System.out.print("收到棋谱后黑方计时器 djs2.stop()");
      System.out.println("棋谱为：" + qiziyanse + qidiana + qidianb
                         + zhongdiana + zhongdianb);

      djs1.init();
      djs1.start();
    }
    shoushu++;
    hui[shoushu][0]=sss;
    hui[shoushu][1] = datain[3];
    hui[shoushu][2] = datain[4];
    hui[shoushu][3] = datain[5];
    hui[shoushu][4] = datain[6];
    hui[shoushu][5] = datain[7];
    bukexia = false;
    System.out.println("棋谱在board中处理完毕");
    repaint();
  }

  public boolean mouseDown(Event e, int x, int y) {
    System.out.println("mousedown");
    byte m = 0, n = 0; //落点的行列下标。
    if (bukexia == true) {
      return true;
    }
    n = (byte) ( (x - JIANXI + LIEKUAN) / (2 * LIEKUAN) + 1);
    m = (byte) ( (y - JIANXI + HANGJU) / (2 * HANGJU) + 1);
    //System.out.println("m=" + m);
    //System.out.println("n=" + n);
    if (m >= 1 && m <= 10 && n >= 1 && n <= 9) {

      System.out.println("着点在棋盘内。");
      if (mycolor == CS.HEIFANG) {
        m = (byte) (11 - m); //转化成棋盘坐标
        n = (byte) (10 - n);
      }
    }
    else {
      //System.out.println("着点在棋盘外。");
      return true; //直接推出、
    }
    if (active == 0) { //尚未指定要走的子。
      //System.out.println("当前没有活跃点。");
      if (zb[m][n] > 0 && qizi[zb[m][n]].color == turncolor) {
        active = zb[m][n];
        //System.out.println("turncolor：" + turncolor);
        repaint();
        return true;
      }
      else {
        //System.out.println("空点或对方点");
        return true;
      }
    }
    else { //已经指定要走的子。
      //System.out.println("active=" + active);
      byte ywza = qizi[active].a;
      byte ywzb = qizi[active].b;

      byte ca = (byte) (m - ywza);
      byte cb = (byte) (n - ywzb);
      byte actnew = zb[m][n]; //新的活动子的下标。
      byte xun = 0; //循环变量。
      System.out.print("已经指定要走的子。");
      System.out.print("a=" + ywza);
      System.out.println(";b=" + ywzb);
      System.out.print("a=" + m);
      System.out.println(";b=" + n);
      //System.out.print("actnew=" + actnew);
      //System.out.print("qizi[active].zhonglei=" + qizi[active].zhonglei);
      if (actnew != 0 && qizi[active].color == qizi[actnew].color) {
        System.out.println("改变活动点");
        active = zb[m][n];
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
            //System.out.println("合乎规则");
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
            //System.out.println("合乎规则");
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
              //System.out.println("不合规则。");
              return true;
            }
            if (Math.abs(ca) == 1) { //注意负值
              if (cb == 2) {
                if (zb[ywza][ywzb + 1] != 0) {
                  //System.out.println("不合规则:别马腿。");
                  return true;
                }
              }
              else if (cb == -2) {
                if (zb[ywza][ywzb - 1] != 0) {
                  //System.out.println("不合规则:别马腿。");
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
            System.out.println("合乎马的规则");
            break;
          }
        case 3: {
          //炮。
          System.out.println("当前点为炮");
          byte count = 0;
          if (ca == 0) {
            if (cb == 0) {
              System.out.println("位置不变的错误");
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
            System.out.println("横向移动");
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
          System.out.print("count=" + count);

          if (actnew == 0) { //移动
            if (count > 0) {
              System.out.println("炮要平移，而中间有子");
              return true;
            }
            System.out.println("合乎规则");
            break;

          }
          else if (qizi[active].color != qizi[actnew].color) {
            System.out.println("//提子");

            if (count != 1) {
              System.out.println("炮要隔子吃子，但中间无子或有两子以上");
              return true;
            }
            //System.out.println("属于提子");

            //System.out.println("合乎规则");
            break;
          }
          else if (qizi[active].color == qizi[actnew].color) {
            //active = zb[m][n];
            System.out.println("改变当前活跃点。zheshi bu kenengde .");
          }

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
            //System.out.println("别象腿");
            return true;
          }
          //System.out.println("合乎规则");
          break;
        }
        case 6: { //兵
          //System.out.println("当前点为兵");
          if (qizi[active].color == HONGFANG) {
            //System.out.println("当前点为红兵");
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
          System.out.println("合乎向的规则");
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
          //System.out.println("合乎规则");
          break;
        } //合法性检查结束。

      } //switch结束
      //System.out.println("switch结束");
      /* if (actnew != 0 && (actnew == 5 || actnew == 21)) {
         if (turncolor == HONGFANG) {
           shengli = HONGFANG;
         }
         else if (turncolor == HEIFANG) {
           shengli = HEIFANG;
         }
         bukexia = true;
         qizi[active].a = m;
         qizi[active].b = n;
         zb[ywza][ywzb] = 0;
         qizi[actnew].a = 0;
         qizi[actnew].b = 0;
         zb[m][n] = active;
         //System.out.println("目标点为实，吃子");
         active = 0;
         repaint();
         return true;
       }*/
      //qizi[active].a = m;
      //}
      qizi[active].a = m;
      qizi[active].b = n;
      zb[ywza][ywzb] = 0;
      zb[m][n] = active;
      if (qizi[21].b == qizi[5].b) {
        //&&ywzb==qizi[5].b&&qizi[5].b!=n){
        //将帅同列且当前子从该列移出。
        //System.out.println("将帅同列");
        byte jsjj = 0; //将帅之间的子树。
        for (byte tt = (byte) (qizi[21].a + 1); tt < qizi[5].a; tt++) {
          if (zb[tt][qizi[21].b] != 0) {
            jsjj++;
            break;
          }
        }
        if (jsjj == 0) {
          //System.out.println("将帅会面，不合规则");
          qizi[active].a = ywza;
          qizi[active].b = ywzb;
          zb[ywza][ywzb] = active;
          zb[m][n] = actnew;
          return true;
        }

      }
      shoushu++;
      System.out.println("当前手为："+shoushu);
      hui[shoushu][1] = turncolor;
      hui[shoushu][2] = ywza;
      hui[shoushu][3] = ywzb;
      hui[shoushu][4] = m;
      hui[shoushu][5] = n;
      if (actnew == 0) { //移动
        //System.out.println("目标点为空，属于移动");
        hui[shoushu][0]=0;
        data[3]=0;
        active = 0;
      }
      else if (qizi[active].color != qizi[actnew].color) {
        //提子
        hui[shoushu][0] = actnew;
        data[3]=actnew;
        //System.out.println("目标点为实，吃子");
        qizi[actnew].a = 0;
        qizi[actnew].b = 0;
        active = 0;
      }
      data[4] = turncolor;
      if (turncolor == HONGFANG) {
        turncolor = HEIFANG;

        djs1.stop();
        //djs2.stop();
        System.out.print("红方计时器djs1.stop()");
        if (actnew != 0 && zhl[actnew] == 7) {
          shengli = HONGFANG;
          bukexia = true;
          djs1.bt = false;
          //djs1.stop();
          //djs2.stop();
        }
        else {
          djs2.init();
          djs2.start();
        }

      }
      else if (turncolor == HEIFANG) {
        turncolor = HONGFANG;

        djs2.stop();
        System.out.print("黑方计时器djs2.stop()");
        if (actnew != 0 && zhl[actnew] == 7) {
          shengli = HEIFANG;
          bukexia = true;
          // djs1.stop();
          // djs2.stop();
          djs2.bt = false;

        }
        else {
          djs1.init();
          djs1.start();
        }
      }
      else {
        System.out.print("turncolorcuowu");
      }
      bukexia = true;

      data[0] = (byte) 3;
      data[1] = (byte) 6;
      data[2] = CS.QIPU;
      //data[3]=CS.QIPU;

      data[5] = ywza;
      data[6] = ywzb;
      data[7] = m;
      data[8] = n;

      System.out.println("发送棋谱(" + data[4]+"方)"+data[3]
                         + ywza + ywzb + m + n);
      myclient.sendData(data,0,0);
      if(mycolor==CS.HEIFANG){
        djs1.shengyubushu=djs1.bushu-shoushu/2;
        djs2.shengyubushu=djs2.bushu-shoushu/2;

      }else if (mycolor==CS.HONGFANG){
        djs1.shengyubushu=djs1.bushu-shoushu/2-1;
        djs2.shengyubushu=djs2.bushu-shoushu/2;

      }

      repaint();
    }

    return true;
  }

  public Board1(Daojishi dj1, Daojishi dj2, ClsClient myClient) {

    byte i = 0, j = 0;
    byte wa = 0;
    byte wb = 0;
    byte zishu = 0;
    String imagename;
    //System.out.println("Board 初始化。");
    djs1 = dj1;
    djs2 = dj2;
    myclient = myClient;
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
      //System.out.print(qizi[i].bianhao);
      //System.out.print(qizi[i].zhonglei);
      //System.out.println(qizi[i].color);

    }
    for (i = 17; i <= 32; i++) {
      wa = qishia[i];
      wb = qishib[i];
      zb[wa][wb] = i;
      qizi[i] = new Qizi(i, zhl[i], wa, wb, HEIFANG);
      //System.out.print(qizi[i].bianhao);
      //System.out.print(qizi[i].zhonglei);
      //System.out.println(qizi[i].color);
    }

  }

  public void init() { //初始化图像资源。yijin

    byte i;
    String imagename;

    for (i = 1; i <= 7; i++) { //hong fang zai qian.
      imagename = "Red" + (i - 1) + ".jpg";
      yuansu[i] = new ImageIcon(tupianxiangqi.Board.class.getResource(imagename)).
        getImage();
      //System.out.println("imagenameuan=" + imagename);
      //System.out.println("yuansu[i]=" + yuansu[i]);
      //System.out.println("yuansu[i]=" + yuansu[i].getSource());

      imagename = "Blue" + (i - 1) + ".jpg";
      yuansu[i +
        7] = new ImageIcon(tupianxiangqi.Board.class.getResource(imagename)).
        getImage();

      //yuansu[i]=this.getImage(this.getCodeBase(),"Blue"+zhl[i]+".jpg") ;;
      ////System.out.println("imagenameuan=" + imagename);
      ////System.out.println("yuansu[i]=" + yuansu[i]);
      ////System.out.println("yuansu[i]=" + yuansu[i].getSource());
    }
    yuansu[0] = new ImageIcon(tupianxiangqi.Board.class.getResource("Board.jpg")).
      getImage();

    //System.out.println("yuansu[i]=" + yuansu[i]);
    //System.out.println("yuansu[i]=" + yuansu[i].getSource());

    yuansu[15] = new ImageIcon(tupianxiangqi.Board.class.getResource(
      "pieceTex.jpg")).getImage();

    ////System.out.println("yuansu[i]=" + yuansu[i]);
    ////System.out.println("yuansu[i]=" + yuansu[i].getSource());

  }

  public void update(Graphics g) {
    paint(g);
  }

  public void qidong(byte color) { //双方举手后得到启动命令
    mycolor = color;
    turncolor = CS.HONGFANG;
    state = 1;
    if (mycolor == CS.HONGFANG) {
      bukexia = false;
    }
    else if (mycolor == CS.HEIFANG) {
      bukexia = true;
      //djs1.init();
    }
    djs1.start();
    repaint();
  }

  public void paint(Graphics g) {
    //setBackground(Color.orange);
    byte i = 0;
    byte j = 0;
    int yd1 = 0, yd2 = 0;
    byte qidiana, qidianb;
    work = this.createImage(461, 512);
    if (work == null) { //只能放在这里，否则不能正确初始化
      //System.out.println("work=null");
    }
    else {
      //System.out.println("work!=null");
      gcontext = work.getGraphics(); //gcontext未能正确赋值。
      if (gcontext == null) {
        return;
      }
      //System.out.println("gcontext!=null");
    }
    //System.out.println("gcontext？!=null");
    gcontext.drawImage(yuansu[0], 0, 0, 461, 512, this);

    if (state == 0) {

    } //启动前不画棋子。
    else { //启动后画出棋子
      for (i = 1; i < 33; i++) {
        if (qizi[i].a == 0 || qizi[i].b == 0) {
          continue;
        }
        if (mycolor == CS.HONGFANG) {
          yd2 = (qizi[i].a - 1) * 2 * HANGJU + JIANXI - BANJING;
          yd1 = (qizi[i].b - 1) * 2 * LIEKUAN + JIANXI - BANJING;

        }
        else if (mycolor == CS.HEIFANG) {
          yd2 = ( (11 - qizi[i].a) - 1) * 2 * HANGJU + JIANXI - BANJING;
          yd1 = ( (10 - qizi[i].b) - 1) * 2 * LIEKUAN + JIANXI - BANJING;

        } //黑方则颠倒棋盘。

        j = (byte) (zhuanghuan[zhl[i]] + 1 + qizi[i].color * 7);
        ////System.out.println(";j=" + j);
        gcontext.drawImage(yuansu[j], yd1, yd2, 2 * BANJING, 2 * BANJING, this);
        ////System.out.println("huaqizi");

      }
    }
    if (active != 0) { //该点被激活。
      //System.out.println("该点被激活。");
      if (mycolor == CS.HONGFANG) {
        yd2 = (qizi[active].a - 1) * 2 * HANGJU + JIANXI - BANJING;
        yd1 = (qizi[active].b - 1) * 2 * LIEKUAN + JIANXI - BANJING;
      }
      else if (mycolor == CS.HEIFANG) {
        yd2 = ( (11 - qizi[active].a) - 1) * 2 * HANGJU + JIANXI - BANJING;
        yd1 = ( (10 - qizi[active].b) - 1) * 2 * LIEKUAN + JIANXI - BANJING;

      }
      gcontext.setColor(Color.green);
      gcontext.drawRect(yd1 - 1, yd2 - 1, 2 * BANJING + 2, 2 * BANJING + 2);
      System.out.println("绿框表示激活。");
    }

    g.drawImage(work, 0, 0, 461, 512, this);
    System.out.println("画整个图");

  } //paint

}
