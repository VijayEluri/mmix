package eddie.wu.arrayblock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.domain.Constant;

//和GoBoard的区别在于：1.无需悔棋。
//2.需要更短小精悍。
public class JuMian {
	private static final Log log=LogFactory.getLog(JuMian.class);
  public int fenshu;//搜索时落子点的评分，用于兄弟间比较排序。
  public static final byte[][] szld = Constant.szld;
  //遍历四周邻子点,顺序可调.右-下-左-上
  public static final byte ZBSX = 20; //棋盘坐标上限;
  public static final byte ZBXX = 0; //棋盘坐标下限;
  public static final byte BLANK = 0;
  public static final byte BLACK = 1; //1表示黑子;
  public static final byte WHITE = 2; //2表示白子;
  public static final byte ZTXB = 0; //下标0存储状态值;
  public static final byte SQBZXB = 1; //下标1存储算气标志;
  public static final byte QSXB = 2; //下标2存储气数;
  public static final byte KSYXB = 3; //下标3存储块索引

  public byte ki = 0; //kuai shu块数?当前已经用到的块号;
  public short shoushu = 0; //当前手数,处理之前递增.从1开始;
  public byte ktb = 0, ktw = 0; //黑白被吃子计数
  public byte[][][] zb = new byte[21][21][8];
  //0:state;各点状态:黑1白2;
  //2:breath;气数;3:blockindex块索引;1:计算气的标志;4-7存储气点.
  //前两维是棋盘的坐标,数组下标从1到19;
  public byte[][][] kuai = new byte[128][70][2];
  //mei yi kuai de ge zi zuo biao
  //存储每块的详细信息;50-69为气点.1-49为子点;[x][0][0]为气数;
  //[x][0][1]为子数;多于49子,不存储,多于20气,不具体存储;
  public byte[][] hui = new byte[512][38];
  //0=新块索引;1~8four single point
  //eaten,9~12 kuai suo ying of fou block eaten.13~24is same ,25,26a,b
  //记录棋局的过程信息,用于悔棋;1-8为被吃单子的坐标;9-12为被吃块的索引;
  //13-20为成块子的坐标,21-24为成新块的旧块索引.27-32为打吃点,33-35为打吃的块
  //索引;36.37为禁着点.25.26为该步落点坐标.
  //DataOutputStream out;//qi pu shu chu wen jain棋谱输出文件
  //DataInputStream in;//直接从文件生成局面.
  public void qiquan() { //为了不与常规处理的0.0混淆，用此函数处理弃权，
    //坐标保存为0.0
    shoushu++;
    hui[shoushu][25] = 0;
    hui[shoushu][26] = 0;
  }

  public void cgcl(byte a, byte b) { //chang gui chu li  qi zi  de qi
    //可以接受的输入为(a,b)或c;c=b*19+a-19;完成数气提子
    //a is the row subscript of the point.
    //a是数组的行下标,也是平面的横坐标:0-18
    //b is the column subscript of the point.
    //b是数组的列下标,也是屏幕的纵坐标:0-18
    //byte c;//a,b的一维表示:0-360;
    byte m1 = a; //a,b在方法中不改变
    byte n1 = b; //m1,n1为a,b的邻点.
    byte linzishu = 0; //邻子数
    byte yise = 0; //异色
    byte tongse = 0; //yise is diff color.and 2 same.同色
    byte k0 = 0, k1 = 0, k2 = 0, k3 = 0; //the count for three kinds of point.
    //三种点的计数,k1为异色点计数,k2为气点计数,k3为同色点计数
    byte i = 0, j = 0;
    byte dang = 0; //dang is breath of block.块的气数
    byte ktz = 0; //提子计数,局部
    byte ks = 0, kss = 0; //ks is count for block,kss for single point
    //相邻的成块点数和独立单点数
    byte kin1 = 0, m = 0, n = 0; //the block index.
    //a,b周围四点的块索引
    byte gq = 0; //共气.
    byte[] u = {
        0, 0, 0, 0, 0}; //position0不用
    byte[] v = {
        0, 0, 0, 0, 0}; //四个邻子的坐标
    byte[] tsk = {
        0, 0, 0, 0}; //array for block index.四同色邻子的块索引
    byte[] ysk = {
        0, 0, 0, 0}; //四异色邻子的块索引,同块不重复计算
    byte yiseks = 0; //四邻异色块数
    byte tzd = 0, tkd = 0; //the count for single pointeaten andblock eaten.
    //吃的点数和块数
    byte ktm = hui[shoushu][36]; //禁着点在上一步产生.
    byte ktn = hui[shoushu][37]; //禁着点的坐标(打劫产生的禁着点)

    log.debug("come into method cgcl()");
    if (a > ZBXX && a < ZBSX && b > ZBXX && b < ZBSX && zb[a][b][ZTXB] == BLANK) {
      //下标合法,该点空白
      if (a == ktm && b == ktn) { //是否禁着点
        log.debug("这是打劫时的禁着点,请先找劫材!");
        return;
      }
      else {
        log.debug("a=" + a + ",b=" + b);
      } //仍有可能是无气禁着点!
    }
    else { //第一类不合法点.
      log.debug("该点不合法,在棋盘之外或者该点已经有子.");
      log.debug("a=" + a + ",b=" + b);
      return;
    }

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
        k1++; //the count of diffrent color.异色点计数
        kin1 = zb[m1][n1][KSYXB]; //the block index for the point.66
        if (kin1 == 0) { //not a block.不是块
          zb[m1][n1][QSXB] -= 1;
          if (zb[m1][n1][QSXB] == 0) { //eat the diff point
            k1--; //被提点要减去
            tzd++;
            hui[shoushu][tzd * 2 - 1] = m1;
            hui[shoushu][tzd * 2] = n1;
            log.debug("提子:a=" + m1 + ",b=" + n1);
            ktz++; //single pobyte eaten was count
            zzq(m1, n1, tongse); //zi zhen qi同色子将增气.
          }
          else if (zb[m1][n1][QSXB] == 1) {
            hui[shoushu][27 + bdcds++] = m1;
            hui[shoushu][27 + bdcds++] = n1;
          }
          else if (zb[m1][n1][QSXB] < 0) {
            log.debug("气数错误:a=" + m1 + ",b=" + n1);
            return;
          }
        }
        else { //if (kin1==0)
          for (j = 0; j < yiseks; j++) {
            if (kin1 == ysk[j]) {
              break;
            }
          }
          if (j == yiseks) { //不重复
            ysk[yiseks++] = kin1;
            byte qi = (byte) (kuai[kin1][0][0] - 1);
            kdq(kin1, qi);
            if (kuai[kin1][0][0] == 0) {
              k1--;
              tkd++; //<=4
              hui[shoushu][8 + tkd] = kin1;
              ktz += kuai[kin1][ (byte) 0][1]; //实际的提子数
              kzq(kin1, tongse); //increase the breath of pobyte surround
              //异色块被提,同色子增气.
            }
            else if (kuai[kin1][0][0] == 1) {
              hui[shoushu][32 + bdcks++] = kin1;
            }
            else if (kuai[kin1][ (byte) 0][ (byte) 0] < 0) {
              log.debug("气数错误:kin=" + kin1);
              return;
            }
          } // i==yiseks
        }
      }
    } //用循环代替

    k0 = k1; //k0 is count for diff point.
    zb[a][b][2] = 0; //return the breath to zero.防止提子时的增气.
    if (shoushu % 2 == BLACK) {
      ktb += ktz;
    }
    else {
      ktw += ktz; //将局部提子计入

    }
    for (i = 0; i < 4; i++) { //再处理空白邻子
      m1 = (byte) (a + szld[i][0]);
      n1 = (byte) (b + szld[i][1]);
      if (zb[m1][n1][ZTXB] >= 0) {
        linzishu++;
        if (zb[m1][n1][ZTXB] == BLANK) { //2.1the breath of blank
          //考虑相邻四点是否为气
          k2++; //气点计数
          u[k0 + k2] = m1;
          v[k0 + k2] = n1; //存到每点的信息中
        }
      }
    }

    k0 += k2; //k0 is the total points of diff and blank.
    //k0现在为异色点和气点的总数
    dang = k2; //落子点四邻的直接气
    /*byte dds=dingdianshu(a,b);
               byte qikuaishu=0;
               byte qikuaidian[][]=new byte[4][2];
               if(k2==2){
       if((u[k0+k2]-u[k0+1])*(v[k0+k2]-v[k0+1])==1){//斜对角
          if(zb[u[k0+k2]][v[k0+1]][ZTXB]==BLANK||
          zb[u[k0+1]][v[k0+k2]][ZTXB]==BLANK){//气是否断开
             qikuaidian[qikuaishu][0]=u[k0+k2];
             qikuaidian[qikuaishu++][1]=v[k0+k2];
             //qikuaishu=1;
          }
          else{
             qikuaidian[qikuaishu][0]=u[k0+1];
             qikuaidian[qikuaishu++][1]=v[k0+1];
             qikuaidian[qikuaishu][0]=u[k0+k2];
             qikuaidian[qikuaishu++][1]=v[k0+k2];
             //qikuaishu=2;
          }
       }
       else {
          //qikuaishu=2;
          qikuaidian[qikuaishu][0]=u[k0+1];
          qikuaidian[qikuaishu++][1]=v[k0+1];
          qikuaidian[qikuaishu][0]=u[k0+k2];
          qikuaidian[qikuaishu++][1]=v[k0+k2];
       }
               }
               else if(k2==3){
       if(zb[u[k0+2]][v[k0+1]][ZTXB]==BLANK||
       zb[u[k0+1]][v[k0+2]][ZTXB]==BLANK){
          //qikuaishu=1;
          qikuaidian[qikuaishu][0]=u[k0+1];
          qikuaidian[qikuaishu++][1]=v[k0+1];
       }
       else{
          qikuaidian[qikuaishu][0]=u[k0+1];
          qikuaidian[qikuaishu++][1]=v[k0+1];
          qikuaidian[qikuaishu][0]=u[k0+2];
          qikuaidian[qikuaishu++][1]=v[k0+2];
          //qikuaishu=2;
       }
       if(zb[u[k0+2]][v[k0+3]][ZTXB]==BLANK||
       zb[u[k0+3]][v[k0+2]][ZTXB]==BLANK){
          log.debug("三气全相连");
       }
       else{
          qikuaidian[qikuaishu][0]=u[k0+k3];
          qikuaidian[qikuaishu++][1]=v[k0+k3];
          //qikuaishu++;
       }
               }
               else if(k2==4){
       if(dds==2){
       }
       if(dds==3){
       }
       if(dds==4){
       //todo:确定各气块的原始点.
       }
               }//4月18日
               for(i=0;i<4;i++){
       m1=qikuaidian[i][0];
       n1=qikuaidian[i][1];
       if(m1!=0){
          zishu=0;
          chengkuai(m1,n1,BLANK);
          if(zishu==kuai[zb[a][b][KSYXB]][0][1]-1){
             kuai[zb[a][b][KSYXB]][0][1]-=1;
             break;
          }
          else{
          }
       }
       else break;
               }*/
    //根据成块算法统计子数,看是否生成新块

    for (i = 0; i < 4; i++) { //再处理同色邻子
      m1 = (byte) (a + szld[i][0]);
      n1 = (byte) (b + szld[i][1]);
      if (zb[m1][n1][ZTXB] == tongse) { //3.1
        k3++; //同色点计数
        kin1 = zb[m1][n1][KSYXB];
        if (kin1 == 0) { //独立点
          kss++; //same color single point.独立点计数
          dang += zb[m1][n1][QSXB];
          dang--; //current pobyte close one breath of surr point.
          u[k0 + kss] = m1; //u[0] not used
          v[k0 + kss] = n1; //deal with single point.
        }
        else { //231成块点
          for (j = 0; j < ks; j++) {
            if (kin1 == tsk[j]) {
              break;
            }
          }
          if (j == ks) { //不重复
            dang += kuai[kin1][0][0]; //此为气数
            dang--;
            u[linzishu - ks] = m1; //deal with block.
            v[linzishu - ks] = n1;
            tsk[ks++] = kin1; //
          }
        } //成块点
      }
    }
    /*if(dang>0){//dang可能大于真正的气数,但不可能为0.
       ktm=0;//原来的禁着点实效,因为已经寻劫
       ktn=0;
       byte [] tsk={0,0,0,0};//四异色邻子的块索引,同块不重复计算
       byte  tsks=0;//四邻异色块数
       byte  lin1=0,lin2=0;//2月23日
       for(i=0;i<4;i++){
          m1=(byte)(a+szld[i][0]);
          n1=(byte)(b+szld[i][1]);
          if (zb[m1][n1][ZTXB]==yise&&zb[m1][n1][QSXB]==1) {
        //1.1右边相邻点是否被打吃
             kin1=zb[m1][n1][KSYXB];//the block index for the point.66
             if (kin1==0){     //not a block.不是块
                lin1++;
                hui[shoushu][26+lin1*2-1]=m1;
                hui[shoushu][26+lin1*2]=n1;
             }
             else {
                for(i=0;i<tsks;i++){
                   if(kin1==tsk[i]) break;
                }
                if(i==tsks){
                   lin2++;
                   hui[shoushu][32+lin2]=kin1;
                }
             }
          }
       }
               }//if dang>0*/
    if (dang == 0) { //?仅仅考虑了单点,如果是块呢?没关系,都是虚的计算.
      //showStatus("//this point is prohibited,try again!");
      zzq(a, b, yise); //相当于落子和提子一步完成,本函数完成提子
      for (i = 25; i <= 35; i++) {
        hui[shoushu][i] = 0;
      }
      shoushu--;
      zb[a][b][ZTXB] = BLANK;
      return;
    } //不允许自杀showStatus("qing="+dang+a+b);

    if (k3 == 0) { //4.1 no same color pobyte surround没有同色邻点
      log.debug("//k3=0");
      zb[a][b][2] = dang;
      if (dang == 1 && ktz == 1) { //考虑劫
        ktm = u[linzishu]; //因为先处理异色子,再空白点,又无同色点.
        ktn = v[linzishu]; //必为最后一点?如果是角上的劫呢?由4改为linzishu
        hui[shoushu][36] = ktm; //空白点即为打劫的禁着点.
        hui[shoushu][37] = ktn; //2yue23日增
      } //not conform to so. en.
      if (dang >= 2) { //为征子计算提供信息
        zb[a][b][4] = u[k1 + 1];
        zb[a][b][5] = v[k1 + 1];
        zb[a][b][6] = u[k1 + 2];
        zb[a][b][7] = v[k1 + 2];
      }
      else { //dang==1
        zb[a][b][4] = u[k1 + 1];
        zb[a][b][5] = v[k1 + 1];
      }
      return;
    }

    if (ks == 0) { //4.2 only single pobyte surr.有同色点,但都为独立点
      log.debug("//ks=0");
      gq = 0;
      for (i = 1; i <= kss; i++) { //4.1 deal surr point处理相邻独立点
        hui[shoushu][12 + i * 2 - 1] = u[k0 + i]; //记录合并成块的独立点
        hui[shoushu][12 + i * 2] = v[k0 + i]; //从13到20
        for (j = 1; j <= (kss - i); j++) { //计算点之间的共气
          gq += dd(u[k0 + i], v[k0 + i], u[k0 + i + j], v[k0 + i + j]);
        }
      }
      zb[a][b][QSXB] = (byte) (dang - gq);
      zb[a][b][KSYXB] = ++ki; //count from first block
      hui[shoushu][0] = ki; //记录所成块的索引
      kuai[ki][0][0] = zb[a][b][2];
      kuai[ki][0][1] = (byte) (k3 + 1);
      kuai[ki][k3 + 1][0] = a; //最后一点为a,b
      kuai[ki][k3 + 1][1] = b;
      for (i = 1; i <= k3; i++) { //将周围点合并到块中
        m = u[k0 + i];
        n = v[k0 + i];
        kuai[ki][i][0] = m;
        kuai[ki][i][1] = n;
        zb[m][n][4] = 0; //块的气信息不在点中存储
        zb[m][n][5] = 0;
        zb[m][n][6] = 0;
        zb[m][n][7] = 0;
        zb[m][n][QSXB] = zb[a][b][QSXB];
        zb[m][n][KSYXB] = ki;
      }
      if (zb[a][b][2] != jskq(ki)) {
        log.debug("error of breath");
      }
    }

    if (ks > 0) { //相邻有块
      log.debug("//ks>0");
      ki++;
      hui[shoushu][0] = ki;
      kuai[ki][0][1] = 1; //建立临时块
      kuai[ki][1][0] = a; //a,b存在首位
      kuai[ki][1][1] = b;
      zb[a][b][KSYXB] = ki;
      for (i = 1; i <= kss; i++) { //点合并入块
        hui[shoushu][12 + i * 2 - 1] = u[k0 + i];
        hui[shoushu][12 + i * 2] = v[k0 + i];
        dkhb(u[k0 + i], v[k0 + i], ki); //相邻点并入临时块
      }
      // dkhb(a,b,k[1]);
      for (j = 1; j <= ks; j++) {
        hui[shoushu][20 + j] = tsk[j - 1];
        kkhb(ki, tsk[j - 1]); //not deal with breath块块合并,气尚未处理.
      }
      // hui[shoushu][0]=ki;
      //zb[a][b][2]=tongse;
      //kuai[k[1]][0][0]=zb[a][b][2];//? need deal with breath.
      dang = jskq(ki);
      kdq(ki, dang);
      /*if(dang==0){
         hui[shoushu][0]=ki;
         kzq(ki,yise);
         ktm=-1;
         ktn=-1;
         hui[shoushu][0]=ki;
        // return;
                       }*/
    }
  }

  //one point was eaten
  public void zzq(byte a, byte b, byte tiao) { //function 6.1总之是某子被吃引起的增气.
    byte c1 = 0, i, j, yiseks = 0;
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
          if (zb[m1][n1][QSXB] == 2) {
            zb[m1][n1][6] = a;
            zb[m1][n1][7] = b;
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
            kdq(c1, kuai[c1][0][0] += 1);
          }

        }
      }
    }
    zb[a][b][ZTXB] = BLANK;
    zb[a][b][QSXB] = 0;
    zb[a][b][KSYXB] = 0;
    zb[a][b][4] = 0;
    zb[a][b][5] = 0;
    zb[a][b][6] = 0;
    zb[a][b][7] = 0;
  }

  public void kzq(byte r, byte tiao) { //6.2 yi se kuai bei ti
    //提吃异色块时,同色块气数增加
    byte n = 0;
    byte p = 0, q = 0;
    n = kuai[r][0][1];
    for (byte i = 1; i <= n; i++) {
      p = kuai[r][i][0];
      q = kuai[r][i][1];
      zzq(p, q, tiao);
      //保留原块信息,主要是子数信息,便于悔棋时恢复
    }
    kuai[r][0][0] = 0;

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
            log.debug("悔棋时气数出错:a=" + m1 + ",b=" + n1);
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
            kdq(c1, kuai[c1][0][0] -= 1);
          }
        }
      }
    }

  }

  public void kjq(byte r, byte tiao) { //悔棋时,成块恢复使同色子减气
    byte n = 0; //the same color block is eaten
    byte p = 0, q = 0; //没有自提时,tiao只能是同色.
    n = kuai[r][0][1];
    for (byte i = 1; i <= n; i++) {
      p = kuai[r][i][0];
      q = kuai[r][i][1];
      zjq(p, q, tiao);
    }
    kuai[r][0][0] = 1; //被提块恢复,气数为1.
  }

  public byte dd(byte a, byte b, byte c, byte d) { //7.1diang diang gong qi
    byte gq = 0; // consider four points only.
    if (zb[a][d][0] == 0) { //点共气只有两种可能,相对位置为肩冲和一间跳
      gq++; //后者不必考虑,由当前着点连接在中间,不会重复计入气数.
    }
    if (zb[c][b][0] == 0) {
      gq++;
    }
    log.debug("方法dd,计算共气=" + gq + "\n");
    return gq;
  }

  //6.7diang kuai he bing

  public void dkhb(byte p, byte q, byte r) { //8.1
    byte ss = (byte) (kuai[r][0][1] + 1); //块的子数增1;
    kuai[r][ss][0] = p;
    kuai[r][ss][1] = q;
    kuai[r][0][1] = ss;
    zb[p][q][3] = r;
    zb[p][q][4] = 0;
    zb[p][q][5] = 0;
    zb[p][q][6] = 0;
    zb[p][q][7] = 0;
    log.debug("方法dkhb:点块合并\n");
  }

  //6.8 ss1 shi zu yao kuai!
  public void kkhb(byte r1, byte r2) { //8.2并入前块,气数未定
    byte ss1 = kuai[r1][0][1];
    byte ss2 = kuai[r2][0][1];
    byte m = 0, n = 0;
    for (byte i = 1; i <= ss2; i++) {
      m = kuai[r2][i][0];
      n = kuai[r2][i][1];
      zb[m][n][3] = r1;
      //保留原块信息
      kuai[r1][ss1 + i][0] = m;
      kuai[r1][ss1 + i][1] = n;
    }
    kuai[r1][0][1] = (byte) (ss1 + ss2);
    log.debug("方法dkhb:点块合并");
  }

  public byte jskq(byte r2) {
    byte qishu = 0; //the breath of the block
    byte a = 0, b = 0;
    byte m, n;
    byte zishu = kuai[r2][0][1]; //块的手数
    byte i, j;
    for (i = 1; i <= zishu; i++) {
      m = kuai[r2][i][0];
      n = kuai[r2][i][1];
      for (j = 0; j < 4; j++) {
        a = (byte) (m + szld[j][0]);
        b = (byte) (n + szld[j][1]);
        if (zb[a][b][ZTXB] == BLANK && zb[a][b][SQBZXB] == 0) {
          qishu++;
          zb[a][b][1] = 1;
          kuai[r2][49 + qishu][0] = a; //存储气点下标50开始
          kuai[r2][49 + qishu][1] = b;
        }
      }
    } //for

    for (i = 1; i <= qishu; i++) { //恢复标志
      a = kuai[r2][49 + i][0];
      b = kuai[r2][49 + i][1];
      zb[a][b][SQBZXB] = 0;
    }
    return qishu;
  } //2月22日改,原方法虽妙,仍只能忍痛割爱.

//10.1ji suan kuai qi.

  public void kdq(byte kin, byte a) { //11.1 kuai ding qi块定气
    byte m = 0, n = 0, p = 0;
    p = kuai[kin][0][1];
    for (byte i = 1; i <= p; i++) {
      m = kuai[kin][i][0];
      n = kuai[kin][i][1];
      zb[m][n][2] = a;
    }
    kuai[kin][0][0] = a;
  }

  public void clhuiqi() { //是否所有数据结构都能恢复?
    byte p = 0;
    byte yise = 0;
    byte tongse = 0; //yise is diff color.and 2 same.
    byte tdzs = 0;
    byte k0 = 0, k1 = 0, k2 = 0, k3 = 0, i = 0, j = 0; //the count for three kinds of point.
    byte ks = 0, kss = 0; //ks is count for block,kss for single point
    byte kin, kin1 = 0, m = 0, n = 0; //the block index.

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
    log.debug("悔棋:" + shoushu);
    log.debug("a=" + m + ",b=" + n);
    kin = hui[shoushu][0];
    if (kin > 0) { //是否成新块,自此错误缩进
      for (i = 0; i < 70; i++) {
        kuai[kin][i][0] = 0;
        kuai[kin][i][1] = 0;
      }
      ki = kin; //全局可用块号?
      for (i = 1; i <= 4; i++) {
        if (hui[shoushu][2 * i + 12 - 1] < 0) { //成新块的点
          break;
        }
        else {
          m = hui[shoushu][12 + 2 * i - 1]; //13-20
          n = hui[shoushu][12 + 2 * i];
          hui[shoushu][12 + 2 * i - 1] = 0;
          hui[shoushu][12 + 2 * i] = 0;
          zb[m][n][3] = 0;
          zb[m][n][0] = tongse; //fang wei bian cheng
          zb[m][n][2] = jszq(m, n); //计算子的气
          log.debug("//计算成块点的气:" + "a=" + m + ",b" + n);
        }
      } //deal with 3 sub
      for (i = 1; i <= 4; i++) { //是否旧块成新块
        kin1 = hui[shoushu][20 + i]; //21-24
        hui[shoushu][20 + i] = 0;
        if (kin1 == 0) {
          break;
        }
        else {
          p = kuai[kin1][0][1];
          for (j = 1; j <= p; j++) {
            m = kuai[kin1][j][0];
            n = kuai[kin1][j][1];
            zb[m][n][3] = kin1; //修改块号
            //zb[m][n][0]=tongse;
            zb[m][n][2] = kuai[kin1][0][0]; //恢复原块成块时的气
          }
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
        log.debug("恢复被提子:");
        log.debug("a=" + m + ",b=" + n);
      }
    } //for

    for (i = 1; i <= 4; i++) { //是否有被提的块
      if (hui[shoushu][8 + i] <= 0) {
        break;
      }
      else {
        kin1 = hui[shoushu][8 + i];
        hui[shoushu][8 + i] = 0;
        kdq(kin1, (byte) 1);
        kjq(kin1, tongse);
        p = kuai[kin1][0][1];
        for (j = 1; j <= p; j++) {
          m = kuai[kin1][j][0];
          n = kuai[kin1][j][1];
          zb[m][n][0] = yise;
          zb[m][n][3] = kin1;
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
    log.debug("方法clhuiqi:处理悔棋\n");
  } //clhuiqi

  public JuMian() {
    byte i, j;
    final byte PANWAIDIAN = -1; //棋盘之外的标志;
    for (i = 0; i < 21; i++) { //2月22日加
      zb[0][i][0] = PANWAIDIAN;
      zb[20][i][0] = PANWAIDIAN;
      zb[i][0][0] = PANWAIDIAN;
      zb[i][20][0] = PANWAIDIAN;
    } //2月22日加
  }

  public byte jszq(byte m, byte n) {
    byte dang = 0; //气数变量
    byte i, a, b;
    for (i = 0; i < 4; i++) {
      a = (byte) (m + szld[i][0]);
      b = (byte) (n + szld[i][1]);
      if (zb[a][b][ZTXB] == BLANK) { //2.1the breath of blank
        dang++;
      }
    }
    return dang;
  }

  public byte dingdianshu(byte m1, byte n1) {
    byte[][] dingdian = {
        {
        1, 1}
        , {
        1, -1}
        , {
         -1, -1}
        , {
         -1, 1}
    };
    //右上-右下-左下-左上
    byte i, dds = 0;
    byte m, n;
    for (i = 0; i < 4; i++) {
      m = (byte) (m1 + dingdian[i][0]);
      n = (byte) (n1 + dingdian[i][1]);
      if (zb[m][n][ZTXB] == BLACK || zb[m][n][ZTXB] == WHITE) {
        dds++;
      }
    }
    return dds;
  }

}