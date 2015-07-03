package untitled10;

import org.apache.log4j.Logger;

//import untitled4.*;
//import untitled9.*;

/**
 * <p>Title: 每个棋子交叉点对应的块号用short结构。增大范围
 * 并且使悔棋没有错误，便于搜索时的返回。且单点也作为块。增加点
 * 就变为新块，每个块的子树及其坐标是唯一的。其它信息可能随棋局
 * 进程改变。</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class BoardLianShort {
	private static final Logger log = Logger.getLogger(QiKuai1.class);
  public static final boolean DEBUG = true;

  public static final byte ZBSX = 20; //棋盘坐标上限;
  public static final byte ZBXX = 0; //棋盘坐标下限;

  public static final byte[][] szld = {
      //遍历四周邻子点,顺序可调.右-下-左-上
      {
      1, 0}
      , {
      0, -1}
      , {
      -1, 0}
      , {
      0, 1}
  };
  public static final byte[][] szdjd = {
      //遍历四周对角点,顺序可调.右-下-左-上
      {
      1, 1}
      , {
      1, -1}
      , {
      -1, -1}
      , {
      -1, 1}
  };

  public static final byte ZTXB = 0; //下标0存储状态值;
  public static final byte SQBZXB = 1; //下标1存储算气标志;
  public static final byte QSXB = 2; //下标2存储气数;
  public static final byte QKSYXB = 3; //下标3存储气块索引

  public byte[][][] zb = new byte[21][21][4];
  public short[][] zbk = new short[21][21];
  //气块和子块分开，气块是子块的附属。

  public short shoushu = 0; //当前已有手数,处理之前递增.从1开始;
  //单子成块，则ki和shoushu等价。
  public byte ktb = 0, ktw = 0; //黑白被吃子计数

  public final byte BLANK = 0;
  public final byte BLACK = 1; //1表示黑子;
  public final byte WHITE = 2; //2表示白子;
  //0:各点状态:黑1白2;   //1:计算气的标志;   //2:气数;
  //前两维是棋盘的坐标,数组下标从1到19;
  public short[][] huik = new short[512][12]; //成块的块号1－4
  //提吃的块号5－8，打吃的块号：9－11；
  //直接连接在一起就是块，所以有黑块和白块，以及气块。
  public byte[][] hui = new byte[512][5];
  //记录棋局的过程信息,用于悔棋;3-4禁着点，1－2该步落点坐标.
  public ZiKuai1[] kuai = new ZiKuai1[512];
  public QiKuai1[] qikuai = new QiKuai1[128];
  public short ki = 0; //当前已经用到的块号,用前递增;
  public byte qiki = 0;
  byte qkzishu = 0;
  public BoardLianShort() {

    byte i, j;
    final byte PANWAIDIAN = -1; //棋盘之外的标志;
    for (i = 0; i < 21; i++) { //2月22日加
      zb[0][i][0] = PANWAIDIAN;
      zb[20][i][0] = PANWAIDIAN;
      zb[i][0][0] = PANWAIDIAN;
      zb[i][20][0] = PANWAIDIAN;
    } //2月22日加
    qiki = 1;
    qikuai[qiki] = new QiKuai1();
    qikuai[qiki].color = 0;//?也许－1更好。
    //qikuai[qiki].zishu = (byte) 361;
   // qikuai[qiki].init();
    if(log.isDebugEnabled()) log.debug("qikuai[qiki].zishu="+qikuai[qiki].zishu);
    for (i = 1; i < 20; i++) {
      for (j = 1; j < 20; j++) {
        zb[i][j][QKSYXB] = 1;
        qikuai[qiki].addzidian(i, j);
      }

    }
  }

  public byte jskq(short rs) {
    if(log.isDebugEnabled()) log.debug("进入方法：计算块气（jskq）");
    //算出块的气，并完成块的所有信息：气数和气串。
    byte qishu = 0; //块的子数和子串已经确定。
    byte a = 0, b = 0;
    byte m, n;
    byte i, j;
    byte zishu = kuai[rs].zishu; //块的子数
    DianNode1 temp = kuai[rs].zichuang;
    DianNode1 qich;
    if(log.isDebugEnabled()) log.debug("kuaihao:" + rs);
    if(log.isDebugEnabled()) log.debug("zishu:" + zishu);
    for (i = 1; i <= zishu; i++) {
      m = temp.a;
      n = temp.b;
      zbk[m][n] = rs; //块中点的正确设置

      for (j = 0; j < 4; j++) {
        a = (byte) (m + szld[j][0]);
        b = (byte) (n + szld[j][1]);
        if(log.isDebugEnabled()) log.debug("a=" + a);
        if(log.isDebugEnabled()) log.debug("b=" + b);
        if (zb[a][b][ZTXB] == BLANK) {
          if(log.isDebugEnabled()) log.debug(" kongdian");
        }
        if (zb[a][b][SQBZXB] == 0) {
          if(log.isDebugEnabled()) log.debug(" weijisuanguo");
        }
        if (zb[a][b][ZTXB] == BLANK && zb[a][b][SQBZXB] == 0) {
          qishu++;
          if(log.isDebugEnabled()) log.debug(" qishu++;");
          zb[a][b][SQBZXB] = 1;
          //将气点加入气串。
          qich = new DianNode1(a, b);
          qich.next = kuai[rs].qichuang;
          kuai[rs].qichuang = qich;
        }
      }
      temp = temp.next;
    } //for
    kuai[rs].qishu = qishu;

    qich = kuai[rs].qichuang;
    for (i = 1; i <= qishu; i++) { //恢复标志
      a = qich.a;
      b = qich.b;
      zb[a][b][SQBZXB] = 0;
      zb[a][b][QSXB] = qishu;
      //zbk[a][b] = rs;
      qich = qich.next;
    }
    if(log.isDebugEnabled()) log.debug("块的气数为：" + qishu);
    kdq(rs, qishu);
    if(log.isDebugEnabled()) log.debug("方法jskq：返回");
    return qishu;
  } //2月22日改,原方法虽妙,仍只能忍痛割爱.

  public void kdq(short kin, byte a) { //块定气
    if(log.isDebugEnabled()) log.debug("方法kdq：进入");
    byte m = 0, n = 0, p = 0; //块减少一气时用。
    short rs = kin;
    p = kuai[rs].zishu;
    DianNode1 temp = kuai[rs].zichuang;
    for (byte i = 1; i <= p; i++) {
      m = temp.a;
      n = temp.b;
      zb[m][n][QSXB] = a; //配合kkhb；
      zbk[m][n] = kin; //冗余
      temp = temp.next;
    }
    kuai[rs].qishu = a;
    if(log.isDebugEnabled()) log.debug("块" + rs + "的气数为" + a);
    if(log.isDebugEnabled()) log.debug("方法kdq：返回");
  }

  public void kjq(byte r, byte tiao) { //悔棋时,成块恢复使同色子减气
    byte n = 0; //the same color block is eaten
    byte p = 0, q = 0; //没有自提时,tiao只能是同色.
    short rs = (short) (128 + r);
    n = kuai[rs].zishu;
    DianNode1 temp = kuai[rs].zichuang;
    for (byte i = 1; i <= n; i++) {
      p = temp.a;
      q = temp.b;
      zjq(p, q, tiao);
      temp = temp.next;
    }
    kuai[rs].qishu = 1; //被提块恢复,气数为1.
    //todo:气串为a,b
  }

  public void kkhb(short rs1, short rs2) { //8.2并入前块,气数未定
    if(log.isDebugEnabled()) log.debug("方法kkhb:块块合并");
    DianNode1 temp = kuai[rs1].zichuang;
    DianNode1 temp1 = kuai[rs2].zichuang;
    byte m = 0, n = 0;
    for (byte j = 1; j <= kuai[rs2].zishu; j++) {
      m = temp1.a;
      n = temp1.b;
      zbk[m][n] = rs1;
      temp1 = temp1.next;
    }

    for (byte i = 1; i < kuai[rs1].zishu; i++) {
      temp = temp.next;
    }
    temp.next = kuai[rs2].zichuang;
    kuai[rs1].zishu += kuai[rs2].zishu;
    if(log.isDebugEnabled()) log.debug("方法kkhb:块块合并\n");
  }

  public boolean validate(byte a, byte b) {
    byte m, n, qi = 0;
    //在shoushu增加之前调用，yise和tongse的计算有所不同。
    byte tongse = (byte) (shoushu % 2 + 1); //yi se=1或2,黑先行为奇数
    byte yise = (byte) ( (1 + shoushu) % 2 + 1); //tong se=1或2,白后行为偶数
    if (a > ZBXX && a < ZBSX && b > ZBXX && b < ZBSX && zb[a][b][ZTXB] == BLANK) {
      //下标合法,该点空白
      if (a == hui[shoushu][2] && b == hui[shoushu][3]) { //是否禁着点
        System.out.print("这是打劫时的禁着点,请先找劫材!");
        if(log.isDebugEnabled()) log.debug("落点为：a=" + a + ",b=" + b);
        return false;
      }
      else {
        //if(log.isDebugEnabled()) log.debug("落点为：a=" + a + ",b=" + b);
        for (byte i = 0; i < 4; i++) {
          m = (byte) (a + szld[i][0]);
          n = (byte) (b + szld[i][1]);
          if (zb[m][n][ZTXB] == BLANK) {
            return true;
          }
          else if (zb[m][n][ZTXB] == yise) {
            if (zb[m][n][QSXB] == 1) {
              return true; //todo
            }
          }
          else if (zb[m][n][ZTXB] == tongse) {
            if (zb[m][n][QSXB] > 1) {
              return true;
            }
            else {
              qi += zb[m][n][QSXB]; //不论成块与否都成立
              qi--;
            }
          }
        }
        if (qi == 0) {
          System.out.print("这是自杀的禁着点：");
          if(log.isDebugEnabled()) log.debug("a=" + a + ",b=" + b);
          return false;
        }
        else {
          System.out.print("这是合法着点：");
          if(log.isDebugEnabled()) log.debug("a=" + a + ",b=" + b);
          return true;
        }
      }
    }
    else { //第一类不合法点.
      System.out.print("该点不合法,在棋盘之外或者该点已经有子：");
      if(log.isDebugEnabled()) log.debug("a=" + a + ",b=" + b);
      return false;
    }
  }

  public void output() { //输出每块的信息;

    DianNode1 temp = null;
    DianNode1 first = null;
    byte zishu = 0;
    byte qishu = 0;
    short i = 0;
    byte j = 0;
    byte m, n;
    if(log.isDebugEnabled()) log.debug("方法 output");
    for (i = 1; i <= ki; i++) {
      temp = kuai[i].zichuang;
      zishu = kuai[i].zishu;
      System.out.print("块号:" + i + "；子数:" + zishu);
      for (j = 1; j <= zishu; j++) {
        m = temp.a;
        n = temp.b;
        System.out.print("(" + m + "," + n + ")");
        temp = temp.next;
      }
      if(log.isDebugEnabled()) log.debug("");
    }

    for (i = 1; i <= ki; i++) {
      temp = kuai[i].qichuang;
      qishu = kuai[i].qishu;
      System.out.print("块号:" + i + "；气数:" + qishu);
      for (j = 1; j <= qishu; j++) {
        m = temp.a;
        n = temp.b;
        System.out.print("(" + m + "," + n + ") ");
        temp = temp.next;
      }
      if(log.isDebugEnabled()) log.debug("");
    }
    for (i = 1; i <= ki; i++) {
      HaoNode1 temp1 = kuai[i].zwkhao;
      short kuaishu = kuai[i].zwyskshu;
      System.out.print("块号:" + i + "；周围块数:" + kuaishu);
      for (j = 1; j <= kuaishu; j++) {

        System.out.print("(" + temp1.hao + ")");
        temp1 = temp1.next;
      }
      if(log.isDebugEnabled()) log.debug("");
    }
    for (i = 1; i <= qiki; i++) {

      if(log.isDebugEnabled()) log.debug("块号:" + i + "；zishu:" + qikuai[i].zishu);

    }

    System.out.print("ki=" + ki + ";shoushu=" + shoushu);
    if(log.isDebugEnabled()) log.debug(";ktw=" + ktw + ";ktb=" + ktb);
    if(log.isDebugEnabled()) log.debug("方法 output");
  }

  public void zjq(byte a, byte b, byte tiao) { //function 6.1
    //提子增气函数。
    byte i, m1, j, n1, yiseks = 0;
    short c1 = 0;
    short ysk[] = { //和被提子相比为异色。
        0, 0, 0, 0};
    for (i = 0; i < 4; i++) {
      m1 = (byte) (a + szld[i][0]);
      n1 = (byte) (b + szld[i][1]);
      if (zb[m1][n1][ZTXB] == tiao) {
        c1 = zbk[m1][n1];
        /* if (c1 == 0) {
           zb[m1][n1][QSXB] -= 1;
           if (zb[m1][n1][QSXB] < 1) {
             if(log.isDebugEnabled()) log.debug("悔棋时气数出错:a=" + m1 + ",b=" + n1);
           }
         }*/
        //else {
        for (j = 0; j < yiseks; j++) {
          if (c1 == ysk[j]) {
            break;
          }
        }
        if (j == yiseks) { //不重复
          ysk[yiseks++] = c1;
          delete(c1, a, b);
          kdq(c1, kuai[c1 + 128].qishu -= 1);
        }
        //}
      }
    }
  }

  public void zzq(byte a, byte b, byte tiao)
  //用于悔棋（相当于落子被提）;及正常提子时己方增气;
  //总之是某子被吃引起对方的增气.tiao指提子方的颜色。
  {
    if(log.isDebugEnabled()) log.debug("方法zzq()");
    byte i, j, yiseks = 0;
    short c1 = 0;
    short cs;
    byte m1, n1;
    short ysk[] = {
        0, 0, 0, 0};
    for (i = 0; i < 4; i++) {
      m1 = (byte) (a + szld[i][0]);
      n1 = (byte) (b + szld[i][1]);
      if (zb[m1][n1][ZTXB] == tiao) {
        c1 = zbk[m1][n1];

        //else {
        for (j = 0; j < yiseks; j++) {
          if (c1 == ysk[j]) {
            break;
          }
        }
        if (j == yiseks) { //不重复
          ysk[yiseks++] = c1;
          cs = c1;
          kdq(c1, kuai[cs].qishu += 1);
          DianNode1 temp = new DianNode1();
          temp.a = a;
          temp.b = b;
          temp.next = kuai[cs].qichuang;
          kuai[cs].qichuang = temp;
        }
        //  }
      }
    }
    zb[a][b][ZTXB] = BLANK;
    zb[a][b][QSXB] = 0;
    zbk[a][b] = 0;
    if(log.isDebugEnabled()) log.debug("方法zzq()");
  }

  public void kzq(short rs, byte tiao) { //6.2 yi se kuai bei ti
    //提吃异色块时,同色块气数增加
    byte n = 0;
    byte p = 0, q = 0;

    n = kuai[rs].zishu;
    DianNode1 temp = kuai[rs].zichuang;
    if(log.isDebugEnabled()) log.debug("方法kzq()");
    for (byte i = 1; i <= n; i++) {
      p = temp.a;
      q = temp.b;
      zzq(p, q, tiao);
      temp = temp.next;
      //保留原块信息,主要是子数信息,便于悔棋时恢复
    }
    kuai[rs].qishu = 0;
    if(log.isDebugEnabled()) log.debug("方法kzq()");
  }

  public void cgcl(byte c) {
    byte a = (byte) ( (c - 1) % 19 + 1);
    byte b = (byte) ( (c - 1) / 19 + 1);
    cgcl(a, b);
  } //提供一维坐标的接口

  public void delete(short r, byte a, byte b) {
    //仅从块的气串中删除一口气的坐标。
    //因为有悔棋，所以不论增气。减气都可能涉及块。
    //单个调用不能确定最终气数，所以kdq函数确有必要。
    if(log.isDebugEnabled()) log.debug("方法delete()");

    DianNode1 temp = kuai[r].qichuang;
    DianNode1 forward = kuai[r].qichuang;
    byte qishu = kuai[r].qishu;
    for (byte i = 1; i <= qishu; i++) {
      if (a == temp.a & b == temp.b) {
        if (i == 1) {
          kuai[r].qichuang = temp.next;

        }
        else {
          forward.next = temp.next;
        }
      }
      else {
        forward = temp;
        temp = temp.next;
      }
    }
    if(log.isDebugEnabled()) log.debug("方法delete()");
  }

  public byte yiweidaoerweia(short c) {
    return (byte) ( (c - 1) % 19 + 1);
  }

  public byte yiweidaoerweib(short c) {
    return (byte) ( (c - 1) / 19 + 1);
  }

  public short erweidaoyiwei(byte a, byte b) {
    return (short) ( (b - 1) * 19 + a);
  }

  public void cgcl(byte a, byte b) { //chang gui chu li
    //常规处理，已经判明是合法着点；
    //可以接受的输入为(a,b)或c;c=b*19+a-19;完成数气提子
    //a是数组的行下标,也是平面的横坐标:1-19
    //b是数组的列下标,也是屏幕的纵坐标:1-19
    //byte c;//a,b的一维表示:1-361;
    byte m1 = a; //a,b在方法中不改变
    byte n1 = b; //m1,n1为a,b的邻点.
    byte yise = 0; //异色
    byte tongse = 0; //与落子点同色
    byte kys = 0, kkd = 0, kts = 0;
    //三种点的计数,kys为异色点计数,kkd为气点计数,kts为同色点计数

    byte i = 0, j = 0;
    byte ktz = 0; //提子计数,局部
    byte tkd = 0;
    short kin1 = 0; //a,b周围四点的块索引
    byte[][] zijieqi = new byte[4][2];
    short[] ysk = {
        0, 0, 0, 0}; //四异色邻子的块索引,同块不重复计算
    byte yiseks = 0; //四邻异色块数
    byte zijieqishu = 0; //落子点周围直接的气数，不包括提子形成的气数
    //所以要在提子前调用。
    if (validate(a, b) == false) {
      return;
    }

    if(log.isDebugEnabled()) log.debug("come into method cgcl()");
    zijieqishu = jszq(a, b);
    hui[++shoushu][1] = a; //手数处理前递增,即从1开始计数.与棋谱同.
    hui[shoushu][2] = b; //记录每步的落点
    yise = (byte) (shoushu % 2 + 1); //yi se=1或2,黑先行为奇数
    tongse = (byte) ( (1 + shoushu) % 2 + 1); //tong se=1或2,白后行为偶数
    zb[a][b][ZTXB] = tongse; //可以动态一致
    qikuai[zb[a][b][QKSYXB]].deletezidian(a, b);
      if(log.isDebugEnabled()) log.debug("qikuai[qiki].zishu="+qikuai[qiki].zishu);
    zb[a][b][QKSYXB] = 0;

    ZiKuai1 linshi = new ZiKuai1();
    linshi.color = zb[a][b][ZTXB];
    linshi.zishu = 1;
    linshi.addzidian(a, b);
    ki++;
    kuai[ki] = linshi;
    zbk[a][b] = ki; //count from first block
    huik[shoushu][0] = ki; //记录所成块的索引
    byte dang = 0;
    for (i = 0; i < 4; i++) {
      m1 = (byte) (a + szld[i][0]);
      n1 = (byte) (b + szld[i][1]);
      if (zb[m1][n1][ZTXB] == BLANK) { //2.1the breath of blank
        zijieqi[dang][0] = m1;
        zijieqi[dang][1] = n1;
        dang++;
      }
    }

    if(log.isDebugEnabled()) log.debug("处理同色邻子");
    for (i = 0; i < 4; i++) { //再处理同色邻子
      m1 = (byte) (a + szld[i][0]);
      n1 = (byte) (b + szld[i][1]);
      //因为kkhb时已经改变了原来块的索引号，所以不会再重复。
      //算法和以往少有不同。
      if (zb[m1][n1][ZTXB] == tongse && zbk[m1][n1] != ki) { //3.1
        kts++; //同色点计数
        kin1 = zbk[m1][n1];
        huik[shoushu][kts] = kin1;
        HaoNode1 teli = kuai[kin1].zwkhao; //修改周围块号的指针

        for (byte p = 1; p <= kuai[kin1].zwyskshu; p++) {
          kuai[teli.hao].deletekuaihao(kin1);
          kuai[teli.hao].addkuaihao(ki); //修改块号
          kuai[ki].addkuaihao(teli.hao);
          teli = teli.next;
        }
        kkhb(ki, kin1);

      }
      //块的气数小于周围气块的最小值是危险的，当然还要考虑
      //是否有长气的手段。

      /*if (zb[m1][n1][ZTXB] == tongse) { //3.1
        kts++; //同色点计数
        kin1 = zbk[m1][n1];
        for (j = 1; j < kts; j++) {
          //if(kin1==tsk[j]) break;
          if (kin1 == huik[shoushu][j]) {
            break;
          }
        } //变量冲突的惨痛教训
        if (j == kts) { //不重复
          //tsk[ks++]=kin1;
          //hui[shoushu][20+ks]=kin1;
          //kuai[kin1].addkuai(ki);
          huik[shoushu][kts] = kin1;
          HaoNode1 teli = kuai[kin1].zwkhao;
          for (byte p = 1; p <= kuai[kin1].zwkshu; p++) {
            kuai[teli.hao].deletekuaihao(kin1);
            kuai[teli.hao].addkuaihao(ki);//修改块号
            kuai[ki].addkuaihao(teli.hao);
            teli=teli.next;
          }
          kkhb(ki, kin1);
        }
        else {
          kts--;
        }
        // } //成块点
             }*/
    }
    if(log.isDebugEnabled()) log.debug("同色点kts=" + kts);

    if(log.isDebugEnabled()) log.debug("处理异色邻子");
    for (i = 0; i < 4; i++) { //先处理异色邻子
      //byte bdcds = 0; //被打吃点计数.
      byte bdcks = 0; //被打吃块计数.
      m1 = (byte) (a + szld[i][0]);
      n1 = (byte) (b + szld[i][1]);
      if (zb[m1][n1][ZTXB] == yise) { //1.1右边相邻点
        kys++; //异色点计数
        kin1 = zbk[m1][n1]; //块索引
        //if (kin1==0)为块。
        for (j = 0; j < yiseks; j++) {
          if (kin1 == ysk[j]) {
            break;
          }
        }
        if (j == yiseks) { //不重复
          ysk[yiseks++] = kin1;
          byte qi = (byte) (kuai[kin1].qishu - 1);

          if (qi == 0) { //原来的气。
            //不进行减气处理是为了悔棋处理方便
            kys--;
            tkd++; //<=4
            huik[shoushu][4 + tkd] = kin1; //据此进行二级块处理；
            ktz += kuai[kin1].zishu; //实际的提子数
            if(log.isDebugEnabled()) log.debug("块被吃，块号为：" + kin1);
            kzq(kin1, tongse); //异色块被提,同色子增气.
            //周围的块的相邻块号链表需要删除该被提的块号
            HaoNode1 linh = kuai[kin1].zwkhao;
            byte linhs = kuai[kin1].zwyskshu;
            for (byte tt = 1; tt <= linhs; tt++) {

              kuai[linh.hao].deletekuaihao(kin1);
              linh = linh.next;
            }
            //形成新的气块；
            qiki++;
            qikuai[qiki] = new QiKuai1();
            qikuai[qiki].zichuang = kuai[kin1].zichuang;
            qikuai[qiki].zishu = kuai[kin1].zishu;
            qikuai[qiki].color = kuai[kin1].color;
            //生成新的二级块，周围的块有指针指向该二级块。
          }

          else if (qi < 0) {
            if(log.isDebugEnabled()) log.debug("气数错误:kin=" + kin1);
            return;
          }
          else {
            kuai[kin1].deleteqidian(a, b);
            kdq(kin1, qi);
            kuai[kin1].addkuaihao(ki);
            kuai[ki].addkuaihao(kin1); //要防止重复
            if(log.isDebugEnabled()) log.debug("块" + kin1 + "气数减为" + zb[m1][n1][QSXB]);
            if (qi == 1) {

              huik[shoushu][8 + tkd] = kin1;
              if(log.isDebugEnabled()) log.debug("块被打吃，块号为：" + kin1);
            }

          }
        } //非重复块
        //} //if kuai
      } // if==yiseks
    } //用循环代替

    zb[a][b][QSXB] = 0; //防止提子时的增气.
    if (shoushu % 2 == BLACK) {
      ktb += ktz;
    }
    else {
      ktw += ktz; //将局部提子计入
    }

    if(log.isDebugEnabled()) log.debug("处理空白邻子");
    /*for (i = 0; i < 4; i++) { //再处理空白邻子
      m1 = (byte) (a + szld[i][0]);
      n1 = (byte) (b + szld[i][1]);
      if (zb[m1][n1][ZTXB] == BLANK) { //2.1the breath of blank
        kkd++; //气点计数
        chengkuai(m1,n1);
        //kuai[ki].addqidian(m1, n1);
      }
         }*/

    if (kts == 0) { //4.1 没有同色邻点
      if(log.isDebugEnabled()) log.debug("没有同色邻点");
      zb[a][b][2] = kkd;
      kuai[ki].qishu = kkd;
      if (kkd == 1 && ktz == 1) { //考虑劫
        hui[shoushu][18] = hui[shoushu][1]; //打劫禁手
        hui[shoushu][19] = hui[shoushu][2];

      } //

    }

    /* for (j = 1; j <= kts; j++) {
       //hui[shoushu][20+j]=tsk[j-1];
       kkhb(ki, huik[shoushu][j]);
       //kkhb(ki,tsk[j-1]);//块块合并,气尚未处理.
     }*/
    jskq(ki);
    //计算最小的块；
    if (kuai[ki].zwyskshu > 1) {
      byte p = kuai[ki].zwyskshu;
      HaoNode1 lint = kuai[ki].zwkhao;
      byte qis = 127;
      for (byte wi = 1; wi <= p; wi++) {
        if (kuai[lint.hao].qishu < qis) {
          qis = kuai[lint.hao].qishu;
        }
        lint = lint.next;
      }
      kuai[ki].minqi = qis; //最小的气、周围块的minqi也应该修正
    }

    //11月22日，最后处理气块的生成
    byte m2, n2, m3, n3, m4, n4, x, y;
    switch (zijieqishu) {
      case 1: {
        if(log.isDebugEnabled()) log.debug("直接气数为1，没有新块生成。");
        break;
      }
      case 2: {
        m1 = zijieqi[0][0];
        n1 = zijieqi[0][1];
        m2 = zijieqi[1][0];
        n2 = zijieqi[1][1];
        if (m1 == m2 || n1 == n2) {

        }
        else {
          x = (byte) (m1 + m2 - a);
          y = (byte) (n1 + n2 - b);
          if (zb[x][y][ZTXB] == BLANK) {
            if(log.isDebugEnabled()) log.debug("直接气数为2，对角点为空，没有新块生成。");
          }
          else if (jgzs(x, y) == 1) {
            if(log.isDebugEnabled()) log.debug("直接气数为2，对角点周围空，没有新块生成。");
          }
          else {

          }
        }
        break;
      }
      case 3: {
        m1 = zijieqi[0][0];
        n1 = zijieqi[0][1];
        m2 = zijieqi[1][0];
        n2 = zijieqi[1][1];
        m3 = zijieqi[2][0];
        n3 = zijieqi[2][1];
        if (m1 == m2 || n1 == n2) {

        }
        else {
          x = (byte) (m1 + m2 - a);
          y = (byte) (n1 + n2 - b);
          if (zb[x][y][ZTXB] == BLANK) {
      }
          else if (jgzs(x, y) == 1) {
        }
          else {
//bianli
            break;
          }

        }
        if (m1 == m3 || n1 == n3) {

        }
        else {
          x = (byte) (m1 + m3 - a);
          y = (byte) (n1 + n3 - b);
          if (zb[x][y][ZTXB] == BLANK) {
             }
          else if (jgzs(x, y) == 1) {
        }
          else {
            //bianli.
          }
          break;
        }
        if (m2 == m3 || n2 == n3) {

        }
        else {
          x = (byte) (m2 + m3 - a);
          y = (byte) (n2 + n3 - b);
          if (zb[x][y][ZTXB] == BLANK) {
            if(log.isDebugEnabled()) log.debug("直接气数为2，对角点为空，没有新块生成。");
          }
          else if (jgzs(x, y) == 1) {
            if(log.isDebugEnabled()) log.debug("直接气数为2，对角点周围空，没有新块生成。");
          }
          else {
            //bianli.
            if(log.isDebugEnabled()) log.debug("直接气数为3，可能有新块生成。");

          }
           break;
        }

        break;
      }
      case 4: {
        byte lianjieshu = 0;
        m1 = (byte) (a + szdjd[0][0]);
        n1 = (byte) (b + szdjd[0][1]);
        m2 = (byte) (a + szdjd[1][0]);
        n2 = (byte) (b + szdjd[1][1]);
        m3 = (byte) (a + szdjd[2][0]);
        n3 = (byte) (b + szdjd[2][1]);
        m4 = (byte) (a + szdjd[2][0]);
        n4 = (byte) (b + szdjd[3][1]);
        if (zb[m1][n1][ZTXB] == BLANK) {
          lianjieshu++;
          //  if(log.isDebugEnabled()) log.debug("直接气数为2，对角点为空，没有新块生成。");
        }
        else if (jgzs(m1, n1) == 1) {
          lianjieshu++;
          // if(log.isDebugEnabled()) log.debug("直接气数为2，对角点周围空，没有新块生成。");
        }

        if (zb[m1][n1][ZTXB] == BLANK) {
          lianjieshu++;
          //  if(log.isDebugEnabled()) log.debug("直接气数为2，对角点为空，没有新块生成。");
        }
        else if (jgzs(m1, n1) == 1) {
          lianjieshu++;
         }

        if (zb[m1][n1][ZTXB] == BLANK) {
          lianjieshu++;
           }
        else if (jgzs(m1, n1) == 1) {
          lianjieshu++;
       }

        if (zb[m1][n1][ZTXB] == BLANK) {
          lianjieshu++;
       }
        else if (jgzs(m1, n1) == 1) {
          lianjieshu++;
       }
       if(lianjieshu>=3){
         if(log.isDebugEnabled()) log.debug("直接气数为4，lianjieshuwei3，没有新块生成。");

       }else{

       }
        break;
      }
    }
    if(log.isDebugEnabled()) log.debug("方法cgcl()返回");
  }

  public byte jgzs(byte m, byte n) { //九宫子数。

    byte dang = 0; //气数变量
    byte i, a, b; //悔棋恢复时，解散块所成单点的气数计算；
    for (i = 0; i < 4; i++) {
      a = (byte) (m + szld[i][0]);
      b = (byte) (n + szld[i][1]);
      if (zb[a][b][ZTXB] != BLANK) { //2.1the breath of blank
        dang++;
      }
    }
    for (i = 0; i < 4; i++) {
      a = (byte) (m + szdjd[i][0]);
      b = (byte) (n + szdjd[i][1]);
      if (zb[a][b][ZTXB] != BLANK) { //2.1the breath of blank
        dang++;
      }
    }

    return dang;

  }

  /* public void clhuiqi() { //是否所有数据结构都能恢复?
     byte p = 0;
     byte yise = 0;
     byte tongse = 0; //yise is diff color.and 2 same.
     byte tdzs = 0;
     byte k0 = 0, kys = 0, kkd = 0, kts = 0, i = 0, j = 0; //the count for three kinds of point.
     byte ks = 0, kss = 0; //ks is count for block,kss for single point
     byte kin, kin1 = 0, m = 0, n = 0; //the block index.
     if(log.isDebugEnabled()) log.debug("方法:处理悔棋(clhuiqi)\n");
     tongse = (byte) ( (shoushu + 1) % 2 + 1); //tong se
     yise = (byte) (shoushu % 2 + 1);
     m = hui[shoushu][25];
     hui[shoushu][25] = 0;
     n = hui[shoushu][26];
     hui[shoushu][26] = 0;
     if (m <= 0 || n <= 0) { //弃权的恢复
       shoushu--;
       return; //
     }
     zzq(m, n, yise); //悔棋,对方增气,提子直接恢复,不用在此增气
     if(log.isDebugEnabled()) log.debug("悔棋:" + shoushu);
     if(log.isDebugEnabled()) log.debug("a=" + m + ",b=" + n);
     kin = hui[shoushu][0];
     if (kin != 0) { //kin是新成的块
       kuai[kin].color = 0;
       kuai[kin].qishu = 0;
       kuai[kin].zishu = 0;
       kuai[kin].zichuang = null;
       kuai[kin].qichuang = null;
       ki = kin; //全局可用块号?
       ki--; //xinzeng.
       for (i = 1; i <= 4; i++) {
         if (hui[shoushu][2 * i + 12 - 1] <= 0) { //成新块的点
           break;
         }
         else {
           m = hui[shoushu][12 + 2 * i - 1]; //13-20
           n = hui[shoushu][12 + 2 * i];
           hui[shoushu][12 + 2 * i - 1] = 0;
           hui[shoushu][12 + 2 * i] = 0;
           zb[m][n][QKSYXB] = 0;
           zb[m][n][ZTXB] = tongse; //fang wei bian cheng
           zb[m][n][QSXB] = jszq(m, n); //计算子的气
           if(log.isDebugEnabled()) log.debug("//计算成块点的气:" + "a=" + m + ",b" + n);
         }
       } //deal with 3 sub
       for (i = 1; i <= 4; i++) { //是否旧块成新块
         kin1 = hui[shoushu][20 + i]; //21-24
         hui[shoushu][20 + i] = 0;
         if (kin1 == 0) {
           break;
         }
         else {
           p = kuai[kin1 + 128].zishu;
           DianNode1 temp = kuai[kin1 + 128].zichuang;
           for (j = 1; j <= p; j++) { //这个循环关键，避免两块的混淆。
             m = temp.a;
             n = temp.b;
             zb[m][n][3] = kin1; //修改块号
             //zb[m][n][0]=tongse;
             zb[m][n][2] = kuai[kin1 + 128].qishu; //恢复原块成块时的气
             temp = temp.next;
           }
           //jskq(kin1); //cunchukuaiqi;
         } //else
       } //for
     } //if 是否新块
     for (i = 1; i <= 4; i++) { //是否提子
       if (hui[shoushu][2 * i - 1] <= 0) {
         break;
       }
       else {
         m = hui[shoushu][2 * i - 1];
         n = hui[shoushu][2 * i];
         hui[shoushu][2 * i - 1] = 0;
         hui[shoushu][2 * i] = 0;
         tdzs = i; //?
         zb[m][n][ZTXB] = yise;
         zb[m][n][QSXB] = 1;
         zb[m][n][QKSYXB] = 0;
         zjq(m, n, tongse);
         System.out.print("恢复被提子:");
         if(log.isDebugEnabled()) log.debug("a=" + m + ",b=" + n);
       }
     } //for
     for (i = 1; i <= 4; i++) { //是否有被提的块
       if (hui[shoushu][8 + i] == 0) {
         break;
       }
       else {
         kin1 = hui[shoushu][8 + i];
         hui[shoushu][8 + i] = 0;
         kdq(kin1, (byte) 1);
         kjq(kin1, tongse);
         p = kuai[kin1 + 128].zishu;
         DianNode1 temp = kuai[kin1 + 128].zichuang; //shiwubiyao
         for (j = 1; j <= p; j++) {
           m = temp.a;
           n = temp.b;
           zb[m][n][ZTXB] = yise;
           zb[m][n][QKSYXB] = kin1;
           temp = temp.next;
         }
         tdzs += p;
       } //else
     } //for
     if (tongse == BLACK) {
       ktb -= tdzs;
     }
     if (tongse == WHITE) {
       ktw -= tdzs;
     }
     for (i = 0; i < 9; i++) {
       hui[shoushu][27 + i] = 0; //2yue
     }
     shoushu--;
     if(log.isDebugEnabled()) log.debug("方法clhuiqi:处理悔棋\n");
   } //clhuiqi*/

  public byte jszq(byte m, byte n) { //huiqishiyong.
    byte dang = 0; //气数变量
    byte i, a, b; //悔棋恢复时，解散块所成单点的气数计算；
    for (i = 0; i < 4; i++) {
      a = (byte) (m + szld[i][0]);
      b = (byte) (n + szld[i][1]);
      if (zb[a][b][ZTXB] == BLANK) { //2.1the breath of blank
        dang++;
      }
    }
    return dang;
  }

  public void chengkuai(byte a, byte b) {
    qiki++;
    qikuai[qiki] = new QiKuai1();
    qkzishu = 0;
    if (chengqikuai(a, b) == false) {
      qikuai[qiki] = null;
      qiki--;
    }
    //qiki的SQBZXB应该清零
  }

  public byte bijiaoqishu(
      short k1, short k2, byte colorxian) {
    //以 K1方的角度判断；
    byte m1 = kuai[k1].qishu;
    byte m2 = kuai[k2].qishu;
    byte cha = (byte) (m1 - m2);
    if (kuai[k1].color == colorxian) {
      cha += 1;
    }
    //返回值<=0;就是对杀失败；
    //一方调用为1：对杀获胜，落后手；从另一方调用就是0：对杀失败，也可
    //看成弃子，先手利用；
    //一方调用为2：本方脱先仍然获胜；但是脱先后对方有先手利用；
    //从另一方调用就是－1：对方脱先仍然失败；
    //一方调用为3：本方脱先仍可获胜，且脱先后对方没有利用。
    //另一方调用为－2：对方即使脱先本方也没有先手利用
    //转换关系为：>0:先减一，再取反。
    //他们是等价的，只是从不同方面描述。
    //终于解决了.
    return cha;

  }

  public byte xingshipanduan(byte colorjiaodu) {
    //调用的契机，何时调用？对方脱先或者有提子。
    //首先确定发起调用的地点是落子的局部
    //同时要考虑对别处的影响，一般是伶近局部的影响
    //涉及征子才考虑远处的影响。
    //有的对杀用量表示，不要实际对杀。比如气长2，且短气方没有
    //changqidian.
    byte lingxian = 0;
    return lingxian;
  }

  public boolean chengqikuai(byte a, byte b) {
    //收集信息的过程中,可以令color=BLANK,调用该函数,但是气块的信息
    //不能驻留在kuai数组内,必须早点调用并清除.
    //调用该函数前必须将qkzishu清零
    byte m1, n1;
    //byte zishu=0;
    if (qkzishu < 10) {
      qikuai[qiki].addzidian(a, b);
      qkzishu++;
    }
    else {
      qkzishu++;
      if(log.isDebugEnabled()) log.debug("该块的子数超过10,块号为:" + ki);
      return false;
    }
    zb[a][b][SQBZXB] = 1;
    zb[a][b][QKSYXB] = qiki;
    //zishu++;
    for (byte k = 0; k < 4; k++) {
      m1 = (byte) (a + szld[k][0]);
      n1 = (byte) (b + szld[k][1]);
      if (zb[m1][n1][SQBZXB] == 0 & zb[m1][n1][ZTXB] == BLANK) {
        if (chengqikuai(m1, n1) == false) {
          return false;
        }
      }
    }
    return true;
  } //成块的点SQBZXB==1;

}