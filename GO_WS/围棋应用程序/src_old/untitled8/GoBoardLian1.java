package untitled8;

import org.apache.log4j.Logger;

import untitled10.QiKuai1;

public class GoBoardLian1 {
	private static final Logger log = Logger.getLogger(QiKuai1.class);
  public final boolean DEBUG = true;
  public final byte ZBSX = 20; //棋盘坐标上限;
  public final byte ZBXX = 0; //棋盘坐标下限;

  public final byte BLANK = 0;
  public final byte BLACK = 1; //1表示黑子;
  public final byte WHITE = 2; //2表示白子;

  public final byte[][] szld = {
      {
      1, 0}
      , {
      0, -1}
      , {
       -1, 0}
      , {
      0, 1}
  };
  //遍历四周邻子点,顺序可调.右-下-左-上
  public final byte ZTXB = 0; //下标0存储状态值;
  public final byte SQBZXB = 1; //下标1存储算气标志;
  public final byte QSXB = 2; //下标2存储气数;
  public final byte KSYXB = 3; //下标3存储块索引

  public byte ki = -128; //当前已经用到的块号,用前递增;
  public short shoushu = 0; //当前手数,处理之前递增.从1开始;
  public byte ktb = 0, ktw = 0; //黑白被吃子计数
  public byte[][][] zb = new byte[21][21][4];
  //0:各点状态:黑1白2;   //1:计算气的标志;   //2:气数;
  //3:块索引;     //前两维是棋盘的坐标,数组下标从1到19;
  public byte[][] hui = new byte[512][38]; //0=新块索引;
  //记录棋局的过程信息,用于悔棋;1-8为被吃单子的坐标;9-12为被吃块的索引;
  //13-20为成块子的坐标,21-24为成新块的旧块索引.27-32为打吃点,33-35为
  //打吃的块索引;36.37为禁着点.25.26为该步落点坐标.
  public KuaiBiaoXiang[] kuai = new KuaiBiaoXiang[256];

  public byte jskq(byte r) {
    if(log.isDebugEnabled()) log.debug("方法jskq：");
    short rs = (short) (128 + r);
    //算出块的气，并完成块的所有信息：气数和气串。
    byte qishu = 0; //块的子数和子串已经确定。
    byte a = 0, b = 0;
    byte m, n;
    byte i, j;
    byte zishu = kuai[rs].zishu; //块的子数
    DianNode temp = kuai[rs].zichuang;
    DianNode qich;
    for (i = 1; i <= zishu; i++) {
      m = temp.a;
      n = temp.b;
      zb[m][n][KSYXB] = r;
      for (j = 0; j < 4; j++) {
        a = (byte) (m + szld[j][0]);
        b = (byte) (n + szld[j][1]);
        if (zb[a][b][ZTXB] == BLANK && zb[a][b][SQBZXB] == 0) {
          qishu++;
          zb[a][b][SQBZXB] = 1;
          //将气点加入气串。
          qich = new DianNode();
          qich.a = a;
          qich.b = b;
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
      qich = qich.next;
    }
    kdq(r, qishu);
    if(log.isDebugEnabled()) log.debug("方法jskq：返回");
    return qishu;
  } //2月22日改,原方法虽妙,仍只能忍痛割爱.

  public void kdq(byte kin, byte a) { //块定气
    if(log.isDebugEnabled()) log.debug("方法kdq：进入");
    byte m = 0, n = 0, p = 0; //块减少一气时用。
    short rs = (short) (128 + kin);
    p = kuai[rs].zishu;
    DianNode temp = kuai[rs].zichuang;
    for (byte i = 1; i <= p; i++) {
      m = temp.a;
      n = temp.b;
      zb[m][n][QSXB] = a; //配合kkhb；
      zb[m][n][KSYXB] = kin;
      temp = temp.next;
    }
    kuai[rs].qishu = a;
    if(log.isDebugEnabled()) log.debug("方法kdq：返回");
  }

  public void kjq(byte r, byte tiao) { //悔棋时,成块恢复使同色子减气
    byte n = 0; //the same color block is eaten
    byte p = 0, q = 0; //没有自提时,tiao只能是同色.
    short rs = (short) (128 + r);
    n = kuai[rs].zishu;
    DianNode temp = kuai[rs].zichuang;
    for (byte i = 1; i <= n; i++) {
      p = temp.a;
      q = temp.b;
      zjq(p, q, tiao);
      temp = temp.next;
    }
    kuai[rs].qishu = 1; //被提块恢复,气数为1.
    //todo:气串为a,b
  }

  public void dkhb(byte p, byte q, byte r) { //8.1
    if(log.isDebugEnabled()) log.debug("方法dkhb:点块合并。");
    if(log.isDebugEnabled()) log.debug("r=" + r);
    short rs = (short) (128 + r);
    DianNode temp = new DianNode();
    temp.a = p;
    temp.b = q;
    temp.next = kuai[rs].zichuang;
    kuai[rs].zichuang = temp;
    kuai[rs].zishu += 1; //块的子数增1。
    zb[p][q][3] = r;
    if(log.isDebugEnabled()) log.debug("方法dkhb:点块合并。");
  }

  public void kkhb(byte r1, byte r2) { //8.2并入前块,气数未定
    short rs1 = (short) (128 + r1);
    short rs2 = (short) (128 + r2);
    DianNode temp = kuai[rs1].zichuang;
    byte m = 0, n = 0;
    if(log.isDebugEnabled()) log.debug("方法kkhb:块块合并");
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
      if (a == hui[shoushu][36] && b == hui[shoushu][37]) { //是否禁着点
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

  public void output() {
    DianNode temp = null;
    DianNode first = null;
    byte zishu = 0;
    byte qishu = 0;
    byte i, j;
    byte m, n;
    for (i = 1; i <= ki + 128; i++) {
      temp = kuai[i].zichuang;
      zishu = kuai[i].zishu;
      System.out.print("块号:" + (i - 128) + "；子数:" + zishu);
      for (j = 1; j <= zishu; j++) {
        m = temp.a;
        n = temp.b;
        System.out.print("(" + m + "," + n + ")");
        temp = temp.next;
      }
      if(log.isDebugEnabled()) log.debug("");
    }
    for (i = 1; i <= ki + 128; i++) {
      temp = kuai[i].qichuang;
      qishu = kuai[i].qishu;
      System.out.print("块号:" + (i - 128) + "；气数:" + qishu);
      for (j = 1; j <= qishu; j++) {
        m = temp.a;
        n = temp.b;
        System.out.print("(" + m + "," + n + ") ");
        temp = temp.next;
      }
      if(log.isDebugEnabled()) log.debug("");
    }
    System.out.print("ki=" + ki + ";shoushu=" + shoushu);
    if(log.isDebugEnabled()) log.debug(";ktw=" + ktw + ";ktb=" + ktb);
  }

  public void zjq(byte a, byte b, byte tiao) { //function 6.1
    byte c1 = 0, i, m1, j, n1, yiseks = 0;
    byte ysk[] = {
        0, 0, 0, 0};
    for (i = 0; i < 4; i++) {
      m1 = (byte) (a + szld[i][0]);
      n1 = (byte) (b + szld[i][1]);
      if (zb[m1][n1][ZTXB] == tiao) {
        c1 = zb[m1][n1][3];
        if (c1 == 0) {
          zb[m1][n1][QSXB] -= 1;
          if (zb[m1][n1][QSXB] < 1) {
            if(log.isDebugEnabled()) log.debug("悔棋时气数出错:a=" + m1 + ",b=" + n1);
          }
        }
        else {
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
        }
      }
    }
  }

  public void zzq(byte a, byte b, byte tiao)
  //用于悔棋（相当于落子被提）;及正常提子时己方增气;
  //总之是某子被吃引起对方的增气.tiao指提子方的颜色。
  {
    if(log.isDebugEnabled()) log.debug("方法zzq()");
    byte c1 = 0, i, j, yiseks = 0;
    short cs;
    byte m1, n1;
    byte ysk[] = {
        0, 0, 0, 0};
    for (i = 0; i < 4; i++) {
      m1 = (byte) (a + szld[i][0]);
      n1 = (byte) (b + szld[i][1]);
      if (zb[m1][n1][ZTXB] == tiao) {
        c1 = zb[m1][n1][KSYXB];
        if (c1 == 0) {
          zb[m1][n1][QSXB] += 1;
        }
        else {
          for (j = 0; j < yiseks; j++) {
            if (c1 == ysk[j]) {
              break;
            }
          }
          if (j == yiseks) { //不重复
            ysk[yiseks++] = c1;
            cs = (short) (128 + c1);
            kdq(c1, kuai[cs].qishu += 1);
            DianNode temp = new DianNode();
            temp.a = a;
            temp.b = b;
            temp.next = kuai[cs].qichuang;
            kuai[cs].qichuang = temp;
          }
        }
      }
    }
    zb[a][b][ZTXB] = BLANK;
    zb[a][b][QSXB] = 0;
    zb[a][b][KSYXB] = 0;
     if(log.isDebugEnabled()) log.debug("方法zzq()");
  }

  public void kzq(byte r, byte tiao) { //6.2 yi se kuai bei ti
    //提吃异色块时,同色块气数增加
    byte n = 0;
    byte p = 0, q = 0;
    short rs = (short) (128 + r);
    n = kuai[rs].zishu;
    DianNode temp = kuai[rs].zichuang;
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

  public void delete(byte rr, byte a, byte b) {
    //仅从块的气串中删除一口气的坐标。
    if(log.isDebugEnabled()) log.debug("方法delete()");
    short r = (short) (128 + rr);
    DianNode temp = kuai[r].qichuang;
    DianNode forward = kuai[r].qichuang;
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

  public void cgcl(byte a, byte b) { //chang gui chu li
    //可以接受的输入为(a,b)或c;c=b*19+a-19;完成数气提子
    //a是数组的行下标,也是平面的横坐标:1-19
    //b是数组的列下标,也是屏幕的纵坐标:1-19
    //byte c;//a,b的一维表示:1-361;
    byte m1 = a; //a,b在方法中不改变
    byte n1 = b; //m1,n1为a,b的邻点.
    byte yise = 0; //异色
    byte tongse = 0; //同色
    byte k1 = 0, k2 = 0, k3 = 0;
    //三种点的计数,k1为异色点计数,k2为气点计数,k3为同色点计数
    byte ks = 0, kss = 0; //相邻的成块点数和独立单点数
    byte i = 0, j = 0;
    byte ktz = 0; //提子计数,局部
    byte tzd = 0, tkd = 0; //单点数和成块点数
    byte kin1 = 0; //a,b周围四点的块索引
    byte[] tsk = {
        0, 0, 0, 0}; //四同色邻子的块索引
    byte[] ysk = {
        0, 0, 0, 0}; //四异色邻子的块索引,同块不重复计算
    byte yiseks = 0; //四邻异色块数

    if (validate(a, b) == false) {
      return;
    }
    if(log.isDebugEnabled()) log.debug("come into method cgcl()");
    if(log.isDebugEnabled()) log.debug("处理异色邻子");
    hui[++shoushu][25] = a; //手数处理前递增,即从1开始计数.与棋谱同.
    hui[shoushu][26] = b; //记录每步的落点
    yise = (byte) (shoushu % 2 + 1); //yi se=1或2,黑先行为奇数
    tongse = (byte) ( (1 + shoushu) % 2 + 1); //tong se=1或2,白后行为偶数
    zb[a][b][ZTXB] = tongse; //可以动态一致

    for (i = 0; i < 4; i++) { //先处理异色邻子
      byte bdcds = 0; //被打吃点计数.
      byte bdcks = 0; //被打吃块计数.
      m1 = (byte) (a + szld[i][0]);
      n1 = (byte) (b + szld[i][1]);
      if (zb[m1][n1][ZTXB] == yise) { //1.1右边相邻点
        k1++; //异色点计数
        kin1 = zb[m1][n1][KSYXB]; //块索引
        if (kin1 == 0) { //不是块
          zb[m1][n1][QSXB] -= 1;
          if (zb[m1][n1][QSXB] == 0) { //eat the diff point
            k1--; //被提点要减去
            tzd++;
            hui[shoushu][tzd * 2 - 1] = m1;
            hui[shoushu][tzd * 2] = n1;
            if(log.isDebugEnabled()) log.debug("提子:(" + m1 + "," + n1 + ")");
            ktz++; //提子计数
            zzq(m1, n1, tongse); //同色子（相对当前手）将增气.
          }
          else if (zb[m1][n1][QSXB] == 1) {
            hui[shoushu][27 + bdcds++] = m1;
            hui[shoushu][27 + bdcds++] = n1;
            if(log.isDebugEnabled()) log.debug("(" + m1 + "," + n1 + ")" + "被打吃");
          }
          else if (zb[m1][n1][QSXB] < 0) {
            if(log.isDebugEnabled()) log.debug("气数错误:a=" + m1 + ",b=" + n1);
            return;
          }
          else {
            if(log.isDebugEnabled()) log.debug("(" + m1 + "," + n1 + ")" + "气数减为" +
                               zb[m1][n1][QSXB]);
          }
        }
        else { //if (kin1==0)为块。
          for (j = 0; j < yiseks; j++) {
            if (kin1 == ysk[j]) {
              break;
            }
          }
          if (j == yiseks) { //不重复
            ysk[yiseks++] = kin1;
            byte qi = (byte) (kuai[kin1 + 128].qishu - 1);
            delete(kin1, a, b);
            kdq(kin1, qi);
            if (kuai[kin1 + 128].qishu == 0) {
              k1--;
              tkd++; //<=4
              hui[shoushu][8 + tkd] = kin1;
              ktz += kuai[kin1 + 128].zishu; //实际的提子数
              if(log.isDebugEnabled()) log.debug("块被吃，块号为：" + kin1);
              kzq(kin1, tongse); //异色块被提,同色子增气.
            }
            else if (kuai[kin1 + 128].qishu == 1) {
              hui[shoushu][33 + bdcks++] = kin1;
              if(log.isDebugEnabled()) log.debug("块被打吃，块号为：" + kin1);
            }
            else if (kuai[kin1 + 128].qishu < 0) {
              if(log.isDebugEnabled()) log.debug("气数错误:kin=" + kin1);
              return;
            }
            else {
              if(log.isDebugEnabled()) log.debug("块" + kin1 + "气数减为" + zb[m1][n1][QSXB]);
            }
          } //非重复块
        } //if kuai
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
    for (i = 0; i < 4; i++) { //再处理空白邻子
      m1 = (byte) (a + szld[i][0]);
      n1 = (byte) (b + szld[i][1]);
      if (zb[m1][n1][ZTXB] == BLANK) { //2.1the breath of blank
        k2++; //气点计数
      }
    }
    if(log.isDebugEnabled()) log.debug("处理同色邻子");
    for (i = 0; i < 4; i++) { //再处理同色邻子
      m1 = (byte) (a + szld[i][0]);
      n1 = (byte) (b + szld[i][1]);
      if (zb[m1][n1][ZTXB] == tongse) { //3.1
        k3++; //同色点计数
        kin1 = zb[m1][n1][KSYXB];
        if (kin1 == 0) { //独立点
          kss++; //同色独立点计数
          hui[shoushu][12 + kss * 2 - 1] = m1; //记录合并成块的独立点
          hui[shoushu][12 + kss * 2] = n1;
        }
        else { //成块点
          for (j = 0; j < ks; j++) {
            //if(kin1==tsk[j]) break;
            if (kin1 == hui[shoushu][21 + j]) {
              break;
            }
          } //变量冲突的惨痛教训
          if (j == ks) { //不重复
            //tsk[ks++]=kin1;
            //hui[shoushu][20+ks]=kin1;
            hui[shoushu][21 + ks++] = kin1;
          }
        } //成块点
      }
    }
    if(log.isDebugEnabled()) log.debug("同色点k3=" + k3);

    if (k3 == 0) { //4.1 没有同色邻点
      if(log.isDebugEnabled()) log.debug("没有同色邻点");
      zb[a][b][2] = k2;
      if (k2 == 1 && ktz == 1) { //考虑劫
        hui[shoushu][36] = hui[shoushu][1];
        hui[shoushu][37] = hui[shoushu][2];
      } //
    }
    else { //4.2 有同色点
      if (ks == 0) {
        if(log.isDebugEnabled()) log.debug("有同色点,但都为独立点");
      }
      if (ks > 0) { //相邻有块
        if(log.isDebugEnabled()) log.debug("有同色点,且部分为块");

      }
      zb[a][b][KSYXB] = ++ki; //count from first block
      hui[shoushu][0] = ki; //记录所成块的索引
      DianNode temp = new DianNode();
      KuaiBiaoXiang linshi = new KuaiBiaoXiang();
      temp.a = a;
      temp.b = b;
      linshi.color = zb[a][b][ZTXB];
      linshi.zishu = 1;
      linshi.zichuang = temp;
      kuai[ki + 128] = linshi;

      for (i = 1; i <= kss; i++) { //处理相邻独立点
        //记录合并成块的独立点(从13到20)
        m1 = hui[shoushu][12 + i * 2 - 1];
        n1 = hui[shoushu][12 + i * 2];
        dkhb(m1, n1, ki);
      }
      for (j = 1; j <= ks; j++) {
        //hui[shoushu][20+j]=tsk[j-1];
        kkhb(ki, hui[shoushu][20 + j]);
        //kkhb(ki,tsk[j-1]);//块块合并,气尚未处理.
      }
      jskq(ki);

    }
    if(log.isDebugEnabled()) log.debug("方法cgcl()返回");
  }

  public void clhuiqi() { //是否所有数据结构都能恢复?
    byte p = 0;
    byte yise = 0;
    byte tongse = 0; //yise is diff color.and 2 same.
    byte tdzs = 0;
    byte k0 = 0, k1 = 0, k2 = 0, k3 = 0, i = 0, j = 0; //the count for three kinds of point.
    byte ks = 0, kss = 0; //ks is count for block,kss for single point
    byte kin, kin1 = 0, m = 0, n = 0; //the block index.
    if(log.isDebugEnabled()) log.debug("方法clhuiqi:处理悔棋\n");
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
      kuai[kin+128].color = 0;
      kuai[kin+128].qishu = 0;
      kuai[kin+128].zishu = 0;
      kuai[kin+128].zichuang = null;
      kuai[kin+128].qichuang = null;
      ki = kin; //全局可用块号?
      ki--;//xinzeng.
      for (i = 1; i <= 4; i++) {
        if (hui[shoushu][2 * i + 12 - 1] <= 0) { //成新块的点
          break;
        }
        else {
          m = hui[shoushu][12 + 2 * i - 1]; //13-20
          n = hui[shoushu][12 + 2 * i];
          hui[shoushu][12 + 2 * i - 1] = 0;
          hui[shoushu][12 + 2 * i] = 0;
          zb[m][n][KSYXB] = 0;
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
          p = kuai[kin1+128].zishu;
          DianNode temp = kuai[kin1+128].zichuang;
          for (j = 1; j <= p; j++) {
            m = temp.a;
            n = temp.b;
            zb[m][n][3] = kin1; //修改块号
            //zb[m][n][0]=tongse;
            zb[m][n][2] = kuai[kin1+128].qishu; //恢复原块成块时的气
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
        zb[m][n][KSYXB] = 0;
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
        p = kuai[kin1+128].zishu;
        DianNode temp = kuai[kin1+128].zichuang;//shiwubiyao
        for (j = 1; j <= p; j++) {
          m = temp.a;
          n = temp.b;
          zb[m][n][ZTXB] = yise;
          zb[m][n][KSYXB] = kin1;
          temp=temp.next;
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
  } //clhuiqi

  public GoBoardLian1() {
    if(log.isDebugEnabled()) log.debug("方法GoBoardLian1()");
    byte i, j;
    final byte PANWAIDIAN = -1; //棋盘之外的标志;
    for (i = 0; i < 21; i++) { //2月22日加
      zb[0][i][0] = PANWAIDIAN;
      zb[20][i][0] = PANWAIDIAN;
      zb[i][0][0] = PANWAIDIAN;
      zb[i][20][0] = PANWAIDIAN;
    } //2月22日加
      if(log.isDebugEnabled()) log.debug("方法GoBoardLian1()");
  }

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
}
