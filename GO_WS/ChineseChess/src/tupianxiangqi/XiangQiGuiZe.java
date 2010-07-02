package tupianxiangqi;



//实现分离的象棋规则。用在服务器端。
public class XiangQiGuiZe {

  public static final byte HONGFANG = 0;
  public static final byte HEIFANG = 1;
  public static byte CHUCUO = 10;
  public static byte SHENGLI = 2;




  public static Qizi[] qizi = new Qizi[33];
  //辅助qizi的初始化。
  private static byte[] zhl = new byte[33]; //兵种索引。
  private static byte[] qishia = new byte[33]; //每个子的起始行标
  private static byte[] qishib = new byte[33]; //每个棋子的起始列标。


  private static byte[][] shixiang = new byte[12][11];
  //辅助规则实现。
  private static byte[][] bingzu = new byte[12][11];
  private static byte[][] jiangshuai = new byte[12][11];

  byte[][] zb = new byte[12][11]; //下标1起;每点的现有子编号。

  private byte zhuangtai = 0; //正常
  private byte turncolor = HONGFANG; //当前手轮谁走。

  public XiangQiGuiZe() {
    init();
  }

  public byte getstate() {
    return zhuangtai;
  }

  public boolean receiveData( byte color,byte ywa,byte ywb,byte m,byte n) {
    //处理输入的棋谱；
    byte [] data=new byte[6];
    data[1]=color;
    data[2]=ywa;
    data[3]=ywb;
    data[4]=m;
    data[5]=n;
    return receiveData(data);
  }

  public boolean receiveData( byte[] mes) {
    //错误棋步返回false;；普通棋步返回true;胜利则zhuangtai=SHENGLI；

    byte m = 0, n = 0; //终点的行列下标。
    byte ywza = 0, ywzb = 0;//起点的状态下标
    byte active = 0, actnew = 0; //起点编号和终点编号。
    byte ca = 0, cb = 0;//起点和终点的行列下标差值。
    byte xun = 0; //循环变量。

    //mes[]。0:颜色；1:起点行下标;2:起点列下标;3:终点行下标;4:终点列下标。

    if (turncolor != mes[1]) { //颜色是否正确。
      System.out.print("不该本方走");
      return false;
    }
    m = mes[2];
    n = mes[3];

    if (m >= 1 && m <= 10 && n >= 1 && n <= 9) {
      System.out.println("着点在棋盘内。");
      System.out.print("起点为：");
      System.out.print("m=" + m);
      System.out.println("；n=" + n);

      if (zb[m][n] > 0 && zb[m][n] < 33) {
        if (qizi[zb[m][n]].color == turncolor) {
          active = zb[m][n]; //起点的棋子编号。
          System.out.println("turncolor：" + turncolor);
          System.out.println("起点为本方点");
        }
        else {
          System.out.println("起点为对方点");
          return false;
        }
      }
      else if (zb[m][n] == 0) {
        System.out.println("起点为空点");
        return false;
      }
      else if (zb[m][n] > 32 || zb[m][n] < 0) {
        System.out.println("棋盘状态出错");
        zhuangtai = CHUCUO;
        return false;
      }
    }
    else {
      System.out.println("着点在棋盘外。");
      return false; //直接推出、
    }

    //System.out.println("active=" + active);
    ywza = m; //qizi[active].a;
    ywzb = n; //qizi[active].b;
    m = mes[4];
    n = mes[5];
    if (m >= 1 && m <= 10 && n >= 1 && n <= 9) {
      System.out.println("着点在棋盘内。");
      System.out.print("起点为：");
      System.out.print("m=" + m);
      System.out.println("；n=" + n);

      if (zb[m][n] > 0 && zb[m][n] < 33) {
        if (qizi[zb[m][n]].color != turncolor) {
          actnew = zb[m][n]; //起点的棋子编号。
          System.out.println("turncolor：" + turncolor);
          System.out.println("终点为对方点");
        }
        else {
          System.out.println("终点为本方点");
          return false;
        }
      }
      else if (zb[m][n] == 0) {
        System.out.println("终点为空点，属于移动");

      }
      else if (zb[m][n] > 32 || zb[m][n] < 0) {
        System.out.println("棋盘状态出错");
        zhuangtai = CHUCUO;
        return false;
      }
    }
    else {
      System.out.println("着点在棋盘外。");
      return false; //直接推出、
    }

    ca = (byte) (m - ywza);
    cb = (byte) (n - ywzb);
    //actnew = zb[m][n]; //新的活动子的下标。

    System.out.print("终点为：");
    System.out.print("a=" + ywza);
    System.out.println(";b=" + ywzb);
    System.out.print("actnew=" + actnew);
    System.out.print("qizi[active].zhonglei=" + zhl[active]);
    /* if (actnew != 0 && qizi[active].color == qizi[actnew].color) {
       System.out.println("格式错误");
       return false;
     }*/
    switch (zhl[active]) { //进行合法性判断。
      case 1: { //车。
        System.out.print("当前点为车");
        if (ca == 0) {
          if (cb == 0) {
            return false;
          }
          else if (cb > 0) {
            for (xun = (byte) (ywzb + 1); xun < n; xun++) {
              if (zb[m][xun] != 0) {
                return false;
              }
            }
          }
          else if (cb < 0) {
            for (xun = (byte) (ywzb - 1); xun > n; xun--) {
              if (zb[m][xun] != 0) {
                return false;
              }
            }

          }
          System.out.println("合乎规则");
          //合乎规则;
          break;
        }
        else if (cb == 0) {
          if (ca > 0) {
            for (xun = (byte) (ywza + 1); xun < m; xun++) {
              if (zb[xun][n] != 0) {
                return false;
              }
            }
          }
          else if (ca < 0) {
            for (xun = (byte) (ywza - 1); xun > m; xun--) {
              if (zb[xun][n] != 0) {
                return false;
              }
            }

          }
          System.out.println("合乎规则");
          break;
        }

        else {
          return false; //处理完毕
        }

      }
      case 2: //马。
        System.out.print("当前点为马,");
        {
          if (Math.abs(ca * cb) != 2) {
            System.out.println("不合规则。");
            return false;
          }
          if (Math.abs(ca) == 1) { //注意负值
            if (cb == 2) {
              if (zb[ywza][ywzb + 1] != 0) {
                System.out.println("不合规则:别马腿。");
                return false;
              }
            }
            else if (cb == -2) {
              if (zb[ywza][ywzb - 1] != 0) {
                System.out.println("不合规则:别马腿。");
                return false;
              }
            }
          }
          else if (ca == 2) {
            if (zb[ywza + 1][ywzb] != 0) {
              System.out.println("不合规则:别马腿。");
              return false;
            }
          }
          else if (ca == -2) {
            if (zb[ywza - 1][ywzb] != 0) {
              System.out.println("不合规则:别马腿。");
              return false;
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
            return false;
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
          return false; //处理完毕
        }
        if (actnew == 0) { //移动
          if (count > 0) {
            return false;
          }
          System.out.println("合乎规则");
          break;

        }
        else if (qizi[active].color != qizi[actnew].color) {
          //提子

          if (count != 1) {
            return false;
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
          return false;
          System.out.println("合乎规则");
                   }*/
        break;
      }
      case 4: { //仕
        System.out.println("当前点为士");
        if (Math.abs(ca) != 1 || Math.abs(cb) != 1) {
          return false;
        }
        else if (shixiang[m][n] == 0) {
          return false;
        }

        break;
      }
      case 5: { //相
        System.out.println("当前点为象");
        if (Math.abs(ca) != 2 || Math.abs(cb) != 2) {
          return false;
        }
        else if (shixiang[m][n] == 0) {
          return false;
        }
        else if (zb[ywza + ca / 2][ywzb + cb / 2] != 0) {
          System.out.println("别象腿");
          return false;
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
              return false;
            }
          }
          else if (ca == 0 && ywza < 6 && Math.abs(cb) == 1) {

          }
          else {
            return false;
          }
        }
        else if (qizi[active].color == HEIFANG) { //黑方
          System.out.println("当前点为黑兵");
          if (ca == 1) {
            if (cb != 0) {
              System.out.println("不合乎规则:过河兵");
              return false;
            }
          }
          else if (ca == 0 && ywza > 5 && Math.abs(cb) == 1) {
            System.out.println("合乎规则:过河兵");
          }
          else {
            return false;
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
            return false;

          }
        }
        else {
          return false;
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
        return false;
      }

    }

    if (actnew == 0) { //移动
      System.out.println("目标点为空，属于移动");
      active = 0;

    }
    else if (qizi[active].color != qizi[actnew].color) {
      //提子
      System.out.println("目标点为实，吃子");
      qizi[actnew].a = 0;
      qizi[actnew].b = 0;
      if (zhl[actnew] == 7) {
        zhuangtai = 2;
        /*zb[ywza][ywzb] = 0;
                 qizi[active].a = m;
                 qizi[active].b = n;
                 zb[m][n] = active;*/
      }
      active = 0;

    }

    if (turncolor == HONGFANG) {
      turncolor = HEIFANG;
    }
    else if (turncolor == HEIFANG) {
      turncolor = HONGFANG;
    }
    return true;

  }

  //10月22日，根据自己的理解剥离规则

  public void init() { //与构建器相比，可以随时清零而对象不变
    byte wa = 0;
    byte wb = 0;
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
      qizi[i] = new Qizi(i,
                         wa, wb, HONGFANG);
      System.out.print(qizi[i].bianhao);
      System.out.println(qizi[i].color);

    }
    for (i = 17; i <= 32; i++) {
      wa = qishia[i];
      wb = qishib[i];
      zb[wa][wb] = i;
      qizi[i] = new Qizi(i, wa, wb, HEIFANG);
      System.out.print(qizi[i].bianhao);
      System.out.println(qizi[i].color);
    }

  }

}
