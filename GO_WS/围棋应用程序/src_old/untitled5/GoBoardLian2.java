//package untitled5;
////2004.3.7:在对链式棋块项目进行了许多改进之后，
////开始改进链式棋块生成局面项目.
//
///**从zb数组直接生成局面，而不是按照过程逐步生成当前局面。
// */
//import java.util.List;
//
//import untitled8.GoBoardLian1;
//
//public class GoBoardLian2
//    extends GoBoardLian1 {
//  //a.块的用法改变，单子也作为块。块号另外索引。
//  //b.点也作为特殊的块，子数为1。这样每步都是一块。
//  //坐标数组沿袭，但是用法稍有改变。
//  //真正传递的数据结构就是zb[][][ZTXB];
//  //块号不够就压缩，因为有些块只在过程中存在，悔棋时有用
//  //如果经过了压缩，悔棋方式改变，从头开始再来一遍。
//  public final byte QIANGRUO = 5;
//  public final byte STRONG = 127;
//  byte[][] djd = {
//      {
//      1, 1}
//      , {
//      -1, 1}
//      , {
//      -1, -1}
//      , {
//      1, -1}
//  }; //对角点
//
//  //按象限排序的肩冲点；
//  //用作递归调用的全局变量。
//
//  //封锁1：没有断点的封锁。
//  //封锁2：有断点，可能形成复杂局面。
//  //byte[][][] zb2=new byte[21][21][6];//点的周围异色子和块
//  //byte[][][] zb3=new byte[21][21][3];
//  //byte dandianshu=0;
//
//  //独立点1：该叫单点（周围没有己方的点，但是有对方的点）的个数；
//  //独立点2：周围没有子，必然是开拆、夹击、分投、打入之一。
//  //各种走法都有一定的前提，夹击必须是1：1:1,最好有配合。
//  //或者攻击支子本身有安定的余地，比如开拆。
//  //独立点2。四周最近的同色点视距离（3为界）分为开拆和配合。
//  //byte[][] dandian=new byte[100][2];
//
//  //byte qiki=127;//气的块号,用后减 1;
//  //short kuaihao=0;
//  //Kuai[] kuaibiao=new Kuai[500];
//  byte meikuaizishu = 0; //统计每块的子数，区别块和单子。
//  QiKuai[] qikuai = new QiKuai[100];
//  ZiKuai[] zikuai = new ZiKuai[512];
//  byte qiki = 0; //专门用于气块。
//  byte ziki = -128; //用前递增。
//  //气块单用，比较方便。
//
//  public GoBoardLian2(GoBoardLian1 goboard) {
//    super();
//    for (byte i = ZBXX; i <= ZBSX; i++) {
//      zb[i][ZBXX][SQBZXB] = -1;
//      zb[i][ZBSX][SQBZXB] = -1;
//      zb[ZBXX][i][SQBZXB] = -1;
//      zb[ZBSX][i][SQBZXB] = -1; //防止越界
//
//      for (byte j = ZBXX; j <= ZBSX; j++) {
//        zb[i][j][ZTXB] = goboard.zb[i][j][ZTXB];
//        //zb[i][j][SQBZXB]=goboard.zb[i][j][SQBZXB];
//        //zb[i][j][QSXB]=goboard.zb[i][j][QSXB];
//        //zb[i][j][KSYXB]=goboard.zb[i][j][KSYXB];
//      }
//    } //zb数组的初始化
//    /*for (byte i=0;i<21;i++){
//       zb[i][0][SQBZXB]=-1;
//       zb[i][20][SQBZXB]=-1;
//       zb[0][i][SQBZXB]=-1;
//       zb[20][i][SQBZXB]=-1;//防止越界
//           }*/
//    /*
//           ki=goboard.ki;
//           for(byte i=0;i<=ki+128;i++){
//       //byte jj=kuai[i][0][1];
//       //for(byte j=0;j<70;j++){
//          kuai[i].qishu=goboard.kuai[i].qishu;
//          kuai[i].kuaizishu=goboard.kuai[i].zishu;
//          kuai[i].qichuang=goboard.kuai[i].qichuang;
//          kuai[i].zichuang=goboard.kuai[i].zichuang;
//       //}
//       //kuai[i][0][0]=goboard.kuai[i][0][0];
//           }
//           for(short i=1;i<=shoushu;i++){
//        for(byte j=0;j<38;j++){
//           hui[i][j]=goboard.hui[i][j];
//        }
//           }*/
//    ktb = goboard.ktb; //提子数继承。
//    ktw = goboard.ktw;
//    shoushu = goboard.shoushu;
//  }
//
//  public void shengchengjumian() {
//    //从棋谱的位图表示生成kuai和zb数组的相应信息
//    byte i, j;
//    byte m, m1, n1, n;
//    byte qkxlhzs = 0;
//    byte qkxlbzs = 0;
//    byte color = 0;
//    byte othercolor = 0;
//    //第一遍扫描，生成块，（包括子数和子串）
//    for (i = 1; i < 20; i++) { //i是纵坐标
//      for (j = 1; j < 20; j++) { //j是横坐标,按行处理
//        if (zb[j][i][SQBZXB] == 1) {
//          continue; //SQBZXB此处相当于处理过的标志.
//        }
//        //该点尚未处理。
//        meikuaizishu = 0;
//        if (zb[j][i][ZTXB] == BLACK) { //左.上必为空点或异色子
//          ziki++; //块号递增
//          //kuaihao++;
//          zikuai[ziki].color = BLACK;
//          chengkuai(j, i, BLACK); //判断右.下是否为同色子.
//        }
//        else if (zb[j][i][ZTXB] == WHITE) { //左.上必为空点或异色子
//          ziki++;
//          zikuai[ziki].color = WHITE;
//          chengkuai(j, i, WHITE); //判断右.下是否为同色子
//        }
//        else if (zb[j][i][ZTXB] == BLANK) {
//          qiki++;
//          chengqikuai(j, i);
//          qikuai[qiki].zishu = meikuaizishu;
//          meikuaizishu = 0;
//          continue;
//        }
//
//        //或者被吃了，有个眼。
//        zikuai[ziki + 128].zishu = meikuaizishu;
//        meikuaizishu = 0;
//      }
//    } //生成块,包括棋块。
//    for (i = 1; i < 20; i++) { //i是纵坐标
//      for (j = 1; j < 20; j++) { //j是横坐标
//        zb[j][i][SQBZXB] = 0; //恢复每个点的算气标志
//        //if(zb[j][i][ZTXB]>0 && zb[j][i][KSYXB]==0){
//        //zb[j][i][QSXB]=jszq(j,i);
//        //}
//      }
//    }
//    //计算块气，包括点气。
//    for (i = -127; i <= ziki; i++) {
//      //byte qi=jskq(ki);计算块气过程中直接储存气点.
//      jskq(i);
//    } //计算块气
//
//    for (i = 1; i <= qiki; i++) { //从气块入手，找到强块。
//      meikuaizishu = qikuai[qiki].zishu;
//      if (meikuaizishu == 1) { //眼位
//        //四周必然有子。
//        m = ((Dian)qikuai[qiki].zichuang.get(0)).getA();
//        n = ((Dian)qikuai[qiki].zichuang.get(0)).getB();
//        for (byte k = 0; k < 4; k++) {
//          m1 = (byte) (m + szld[k][0]);
//          n1 = (byte) (n + szld[k][1]);
//
//          if (zb[m1][n1][ZTXB] == WHITE) {
//            qkxlbzs++;
//          }
//          else if (zb[m1][n1][ZTXB] == BLACK) {
//            qkxlhzs++;
//          }
//        }
//        if (qkxlbzs == 0) { //黑方的眼
//          for (byte k = 0; k < 4; k++) {
//            m1 = (byte) (m + szld[k][0]);
//            n1 = (byte) (n + szld[k][1]);
//            if (zb[m1][n1][ZTXB] == BLACK) {
//
//            }
//
//          }
//        }
//        else if (qkxlhzs == 0) { //白方的眼，并成一体。
//
//        }
//        else { //双方的公气。
//
//        }
//        //qikuai[qiki--].zichuang=null;
//        //qikuai[qiki].qichuang
//        //qikuai[ki--][1][1]=0;
//        //zb[j][i][KSYXB]=0;//非块
//        //todo:眼位的处理
//        //找出周围块，气数为一且单子，则打劫。两处为一气，
//        //则双提，块为一气，则打多还一、、
//      }
//      else if (meikuaizishu > 1) { //大眼
//        qikuai[qiki].zishu = meikuaizishu;
//        m = ((Dian)qikuai[qiki].zichuang.get(0)).getA();
//        n = ((Dian)qikuai[qiki].zichuang.get(0)).getB();
//        for (byte k = 0; k < 4; k++) {
//          m1 = (byte) (m + szld[k][0]);
//          n1 = (byte) (n + szld[k][1]);
//
//          if (zb[m1][n1][ZTXB] == WHITE) {
//            qkxlbzs++;
//          }
//          else if (zb[m1][n1][ZTXB] == BLACK) {
//            qkxlhzs++;
//          }
//        }
//        if (qkxlbzs == 0) { //黑方的大眼
//          //周围的块数，越少越好。
//          for (byte k = 0; k < 4; k++) {
//            m1 = (byte) (m + szld[k][0]);
//            n1 = (byte) (n + szld[k][1]);
//            if (zb[m1][n1][ZTXB] == BLACK) {
//
//            }
//
//          }
//        }
//        else if (qkxlhzs == 0) { //白方的大眼，并成一体。
//
//        }
//        else { //双方的公气。
//
//        }
//
//      }
//      else {
//        if(log.isDebugEnabled()) log.debug("error:zishu<1");
//
//      }
//
//    }
//
//    /* byte zijishu = 0;
//     byte kuaijishu = 0;
//     for (i = 1; i <= dandianshu; i++) {
//       zijishu = 0;
//       kuaijishu = 0;
//       m = dandian[i][0];
//       n = dandian[i][1];
//       color = zb[m][n][ZTXB];
//       if (color == 1)
//         othercolor = 2;
//       else if (color == 2)
//         othercolor = 1;
//       for (j = 0; j < 4; j++) {
//         m = (byte) (dandian[i][0] + szld[j][0]);
//         n = (byte) (dandian[i][1] + szld[j][1]);
//         if (zb[m][n][ZTXB] == othercolor) {
//           if (zb[m][n][KSYXB] == 0) {
//             zb2[m][n][2 * zijishu] = m;
//             zb2[m][n][2 * zijishu + 1] = n;
//             zijishu++;
//           }
//           else{//注意防止块的重复。
//           }
//         }
//       }//for
//     }*/
//    byte zijishu = 0;
//    byte kzishu = 0; //块含有的子数，控制循环
//    Dian temp;
//    List listTemp;
//    HaoNode linhao;
//    byte kin1;
//    for (i = -127; i <= ziki; i++) {
//      color = zikuai[i].color;
//      if (color == 1) {
//        othercolor = 2;
//      }
//      else if (color == 2) {
//        othercolor = 1;
//        //遍历该块的各个子，得到周围块，主意防止重复。
//      }
//      kzishu = zikuai[i].zishu;
//      listTemp=kuai[i].zichuang;
//      for (temp = kuai[i].zichuang; temp != null; temp = temp.next) {
//        for (j = 0; j < 4; j++) {
//
//          m = (byte) (temp.a + szld[j][0]);
//          n = (byte) (temp.b + szld[j][1]);
//          if (zb[m][n][ZTXB] == othercolor) {
//
//            //else if(zb[m][n][SQBZXB]==0){//注意防止块的重复。
//            kin1 = zb[m][n][KSYXB];
//            zikuai[i].zwkhao.addkuaihao(kin1);
//
//          }
//        } //for
//
//      }
//
//    }
//  }
//
//  public void jushipanduan() { //局势判断
//    //关于成块的思考：小尖虽然连接没有问题，但是真要接上，
//    //气就紧了。比如常见的相思断。因此，它们的整体性还有所欠缺；
//    //但是单从连接性来说，确实能确保连接。
//    //棋局所在阶段的判断；主要指标：是否已经全局联系；
//    //棋子数，是否还有大场，空间、分投等。死活是否已经确定、
//    //演示用的棋盘和计算用的棋盘应该有区别吗？
//    //演示的棋盘，因为悔棋的需要，新块用新的块号；计算的棋盘呢？
//    for (byte i = -127; i < 127; i++) {
//      //先分成局部，再全局总结。
//      //寻找棋块的连接点，并评价重要性和紧迫性，重要性是子数多少，
//      //紧迫性是断开有无意义。
//      //if()
//
//    }
//
//    for (byte i = -127; i < 127; i++) {
//      //相邻的块是最紧密的关系、它们先成为一个整体；其次是小尖的关系、
//      //将关系密切的归为一个整体。
//      if (kuai[i + 128].qishu == 1) {
//        //找相邻块；如果相邻块气>1
//
//      }
//
//    }
//
//  }
//
//  public void chengkuai(byte a, byte b, byte color) {
//    //收集信息的过程中,可以令color=BLANK,调用该函数,但是气块的信息
//    //不能驻留在kuai数组内,必须早点调用并清除.
//    //byte max=127;
//    byte m1, n1;
//    byte othercolor = 0;
//    Dian temp = new Dian();
//
//    if (color == 1) {
//      othercolor = 2;
//    }
//    else if (color == 2) {
//      othercolor = 1;
//    }
//    temp.setA(a);
//    temp.setB(b);
//    temp.next = zikuai[ziki + 128].zichuang;
//    zikuai[ziki + 128].zichuang = temp;
//    zb[a][b][SQBZXB] = 1;
//    zb[a][b][KSYXB] = ziki;
//    meikuaizishu++;
//    for (byte k = 0; k < 4; k++) {
//      m1 = (byte) (a + szld[k][0]);
//      n1 = (byte) (b + szld[k][1]);
//      if (zb[m1][n1][SQBZXB] == 0 && zb[m1][n1][ZTXB] == color) {
//        chengkuai(m1, n1, color);
//      }
//      /*if(zb[m1][n1][ZTXB]==otherc){
//         if(zb[m1][n1][KSYXB]==0){
//         }
//         else{
//         }
//                }*/
//    }
//  } //成块的点SQBZXB==1;
//
//  //9月14日重读；
//  // 眼的分类：
//  final byte JiaYan = 1; //假眼
//  final byte NianJieYan = 2; //劫材有利或者说局部劫胜是真眼
//  //，否则假眼:顶点的占有由打劫的结果决定。
//  final byte KePoYan = 3; //可破可成眼
//  final byte TiJieYan = 4; //2和4都在打劫，但是有差别。
//  final byte ZhenYan = 5; //真眼
//  final byte BuQueDing = 0; //真眼
//  //提子且不是打劫的话，就形成眼。
//  byte yanweixingzhi(byte qkh) { //判断眼位的性质。
//    //点的性质：主要是成单眼的四个顶点的占有情况
//    byte a = qikuai[qkh].zichuang.a;
//    byte b = qikuai[qkh].zichuang.b;
//    byte dds = 0;
//    byte m = 0, n = 0;
//    byte[] leixingjishu = {
//        0, 0, 0, 0, 0, 0};
//    byte yancolor = qikuai[zb[a][b][KSYXB]].color;
//    for (byte i = 0; i < 4; i++) {
//      m = (byte) (a + djd[i][0]);
//      n = (byte) (b + djd[i][1]); //四个对角点；
//      if (zb[m][n][ZTXB] >= 0) { //防止点不合法
//        dds++;
//        leixingjishu[diandexingzhi(m, n, yancolor)]++;
//      }
//    }
//    if (dds == 1) { //角眼
//      if (leixingjishu[0] == 1) {
//        return JiaYan;
//      }
//      else if (leixingjishu[2] == 1) {
//        return KePoYan;
//      }
//      else if (leixingjishu[4] == 1) {
//        return ZhenYan;
//      }
//      else if (leixingjishu[5] == 1) {
//        return BuQueDing;
//      }
//    }
//    else if (dds == 2) {
//      if (leixingjishu[0] >= 1) {
//        return JiaYan;
//      }
//      else if (leixingjishu[2] == 2) {
//        return JiaYan;
//      }
//      else if (leixingjishu[2] == 1) {
//        if (leixingjishu[4] == 1) {
//          return JiaYan;
//        }
//        else {
//          return KePoYan;
//        }
//      }
//      else if (leixingjishu[5] == 1) {
//        return BuQueDing;
//      }
//
//    }
//    else if (dds == 4) {
//
//      if (leixingjishu[4] >= 2) {
//        return ZhenYan; //已经占有两个，真眼
//      }
//      else if(leixingjishu[2] >= 3)      return JiaYan;
//    }
//    return ZhenYan;
//  }
//
//  byte diandexingzhi(byte a, byte b, byte yancolor) {
//    final byte DuiFangYiJingZhanLing = 0;
//    final byte DuiFangNianJieZhanLing = 1;
//    final byte DuiFangTiJieZhanLing = 3; //1和3都要打劫
//    final byte DuiFangNengZhanLing = 2;
//    final byte DuiFangBuNengZhanLing = 4;
//    final byte BuQueDing = 5;
//    //我方已经占领或者虎口之类。
//
//    //返回0表示不确定。
//    //其中的两个邻点已经被占领。
//    byte i = 0, j = 0;
//    byte m = 0, n = 0;
//    if (zb[a][b][ZTXB] == yszh(yancolor) && zb[a][b][QIANGRUO] == STRONG) {
//      return 0; //强子已经直接占领。
//    }
//
//    for (i = 0; i < 4; i++) {
//      m = (byte) (a + szld[i][0]);
//      n = (byte) (b + szld[i][1]);
//      if (zb[m][n][ZTXB] == yszh(yancolor) && zb[m][n][QIANGRUO] == STRONG) {
//
//        return DuiFangNengZhanLing; //能断开
//      }
//
//      m = (byte) (a + djd[i][0]);
//      n = (byte) (b + djd[i][1]);
//      if (zb[m][n][ZTXB] == yszh(yancolor) && zb[m][n][QIANGRUO] == STRONG) {
//        if (lianjie(a, b, m, n) == true) {
//          return DuiFangNengZhanLing; //能尖断
//        }
//      }
//    }
//    return 4;
//  }
//
//  boolean lianjie(byte a, byte b, byte m, byte n) {
//    //a,b和m,n两点是否连接
//    byte color = yszh(zb[a][b][ZTXB]);
//    if (zb[a][n][ZTXB] == color || zb[m][b][ZTXB] == color) {
//      return false;
//    }
//    else {
//      return true;
//    }
//
//  }
//
//  byte othecolor(byte color) {
//    if (color == 1) {
//      color = 2;
//    }
//    else if (color == 2) {
//      color = 1;
//    }
//    else {
//      if(log.isDebugEnabled()) log.debug("调用参数错误");
//    }
//    return color;
//  }
//
//  byte yszh(byte color) { //颜色转换，拼音太长就用缩写。
//    if (color == BLACK) {
//      return WHITE;
//    }
//    else if (color == WHITE) {
//      return BLACK;
//    }
//    else {
//      if(log.isDebugEnabled()) log.debug("调用参数错误");
//    }
//    return color;
//  }
//
//  //void zhuanhuan（byte）
//  public void chengqikuai(byte a, byte b) {
//    //收集信息的过程中,可以令color=BLANK,调用该函数,但是气块的信息
//    //不能驻留在kuai数组内,必须早点调用并清除.
//
//    byte m1, n1;
//    byte color = 0;
//
//    Dian temp = new Dian();
//    temp.setA(a);
//    temp.setB(b);
//    temp.next = qikuai[qiki].zichuang;
//    kuai[qiki].zichuang = temp;
//
//    zb[a][b][SQBZXB] = 1;
//    //zb[a][b][KSYXB]=qiki;//此时是指气块的索引。
//    meikuaizishu++; //应该作为全局变量
//    for (byte k = 0; k < 4; k++) {
//      m1 = (byte) (a + szld[k][0]);
//      n1 = (byte) (b + szld[k][1]);
//      if (zb[m1][n1][SQBZXB] == 0 && zb[m1][n1][ZTXB] == color) {
//        chengkuai(m1, n1, color);
//      }
//      /*if(zb[m1][n1][ZTXB]==otherc){
//         if(zb[m1][n1][KSYXB]==0){
//         }
//         else{
//         }
//               }*/
//    }
//  } //成块的点SQBZXB==1;
//
//}
//
//
//
//
//
//class Jubu { //局部包含的块号。
//  HaoLian haolian;
//}
////三间的距离内，如果同色子互相为最近的，就可以合并为一块。
//
////吃子驱动，首先，有没有提子，其次，对方的一气子能不能逃跑。
////再次，对方的二气子能否逃跑。
//
//
//class ErJiKuai { //提子形成的块组。
//  byte color;
//
//  byte kuaishu; //块组的块数；
//  HaoNode zckuaihao; //组成块组的块号。
//  byte zhuangtai; //死活之类。
//
//  HaoNode qkhao; //气块的号，就是眼，可能是假眼，也可能是打劫。
//  //先用气数判断强弱，再赋予地，根据眼位大小判断强弱。
//  //眼的链不可能很长。
//  //一旦二级块判为活棋，则所有块的强度都是最大值。
//  //除非存在倒扑之类的手段，整个二级块是一个整体
//  void addKuaiHao(byte kuaihao) {
//    HaoNode temp = zckuaihao;
//    if (zckuaihao == null) {
//      temp = new HaoNode();
//      temp.hao = kuaihao;
//      temp.next = null;
//      zckuaihao = temp;
//    }
//    else {
//      HaoNode last = zckuaihao;
//      for (; temp != null; zckuaihao = temp, temp = temp.next) {
//        if (temp.hao == kuaihao) {
//          return;
//        }
//      }
//      if (temp == null) {
//        temp = new HaoNode();
//        temp.hao = kuaihao;
//        temp.next = null;
//        last.next = temp;
//
//      }
//    }
//  }
//}
//
////最大的全局气块用0为块号，忽略不计。

