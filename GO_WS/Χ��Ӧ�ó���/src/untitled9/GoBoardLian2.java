//package untitled9;
////2004.3.7:�ڶ���ʽ�����Ŀ���������Ľ�֮��
////��ʼ�Ľ���ʽ������ɾ�����Ŀ.
//
///**��zb����ֱ�����ɾ��棬�����ǰ��չ��������ɵ�ǰ���档
// */
//import untitled8.GoBoardLian1;
//
//public class GoBoardLian2
//    extends GoBoardLian1 {
//  //a.����÷��ı䣬����Ҳ��Ϊ�顣�������������
//  //b.��Ҳ��Ϊ����Ŀ飬����Ϊ1������ÿ������һ�顣
//  //����������Ϯ�������÷����иı䡣
//  //�������ݵ����ݽṹ����zb[][][ZTXB];
//  //��Ų�����ѹ������Ϊ��Щ��ֻ�ڹ����д��ڣ�����ʱ����
//  //���������ѹ�������巽ʽ�ı䣬��ͷ��ʼ����һ�顣
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
//  }; //�Խǵ�
//
//  //����������ļ��㣻
//  //�����ݹ���õ�ȫ�ֱ�����
//
//  //����1��û�жϵ�ķ�����
//  //����2���жϵ㣬�����γɸ��Ӿ��档
//  //byte[][][] zb2=new byte[21][21][6];//�����Χ��ɫ�ӺͿ�
//  //byte[][][] zb3=new byte[21][21][3];
//  //byte dandianshu=0;
//
//  //������1���ýе��㣨��Χû�м����ĵ㣬�����жԷ��ĵ㣩�ĸ�����
//  //������2����Χû���ӣ���Ȼ�ǿ��𡢼л�����Ͷ������֮һ��
//  //�����߷�����һ����ǰ�ᣬ�л�������1��1:1,�������ϡ�
//  //���߹���֧�ӱ����а�������أ����翪��
//  //������2�����������ͬɫ���Ӿ��루3Ϊ�磩��Ϊ�������ϡ�
//  //byte[][] dandian=new byte[100][2];
//
//  //byte qiki=127;//���Ŀ��,�ú�� 1;
//  //short kuaihao=0;
//  //Kuai[] kuaibiao=new Kuai[500];
//  byte meikuaizishu = 0; //ͳ��ÿ��������������͵��ӡ�
//  QiKuai[] qikuai = new QiKuai[100];
//  ZiKuai[] zikuai = new ZiKuai[512];
//  byte qiki = 0; //ר���������顣
//  byte ziki = -128; //��ǰ������
//  //���鵥�ã��ȽϷ��㡣
//
//  public GoBoardLian2(GoBoardLian1 goboard) {
//    super();
//    for (byte i = ZBXX; i <= ZBSX; i++) {
//      zb[i][ZBXX][SQBZXB] = -1;
//      zb[i][ZBSX][SQBZXB] = -1;
//      zb[ZBXX][i][SQBZXB] = -1;
//      zb[ZBSX][i][SQBZXB] = -1; //��ֹԽ��
//
//      for (byte j = ZBXX; j <= ZBSX; j++) {
//        zb[i][j][ZTXB] = goboard.zb[i][j][ZTXB];
//        //zb[i][j][SQBZXB]=goboard.zb[i][j][SQBZXB];
//        //zb[i][j][QSXB]=goboard.zb[i][j][QSXB];
//        //zb[i][j][KSYXB]=goboard.zb[i][j][KSYXB];
//      }
//    } //zb����ĳ�ʼ��
//    /*for (byte i=0;i<21;i++){
//       zb[i][0][SQBZXB]=-1;
//       zb[i][20][SQBZXB]=-1;
//       zb[0][i][SQBZXB]=-1;
//       zb[20][i][SQBZXB]=-1;//��ֹԽ��
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
//    ktb = goboard.ktb; //�������̳С�
//    ktw = goboard.ktw;
//    shoushu = goboard.shoushu;
//  }
//
//  public void shengchengjumian() {
//    //�����׵�λͼ��ʾ����kuai��zb�������Ӧ��Ϣ
//    byte i, j;
//    byte m, m1, n1, n;
//    byte qkxlhzs = 0;
//    byte qkxlbzs = 0;
//    byte color = 0;
//    byte othercolor = 0;
//    //��һ��ɨ�裬���ɿ飬�������������Ӵ���
//    for (i = 1; i < 20; i++) { //i��������
//      for (j = 1; j < 20; j++) { //j�Ǻ�����,���д���
//        if (zb[j][i][SQBZXB] == 1) {
//          continue; //SQBZXB�˴��൱�ڴ�����ı�־.
//        }
//        //�õ���δ����
//        meikuaizishu = 0;
//        if (zb[j][i][ZTXB] == BLACK) { //��.�ϱ�Ϊ�յ����ɫ��
//          ziki++; //��ŵ���
//          //kuaihao++;
//          zikuai[ziki].color = BLACK;
//          chengkuai(j, i, BLACK); //�ж���.���Ƿ�Ϊͬɫ��.
//        }
//        else if (zb[j][i][ZTXB] == WHITE) { //��.�ϱ�Ϊ�յ����ɫ��
//          ziki++;
//          zikuai[ziki].color = WHITE;
//          chengkuai(j, i, WHITE); //�ж���.���Ƿ�Ϊͬɫ��
//        }
//        else if (zb[j][i][ZTXB] == BLANK) {
//          qiki++;
//          chengqikuai(j, i);
//          qikuai[qiki].zishu = meikuaizishu;
//          meikuaizishu = 0;
//          continue;
//        }
//
//        //���߱����ˣ��и��ۡ�
//        zikuai[ziki + 128].zishu = meikuaizishu;
//        meikuaizishu = 0;
//      }
//    } //���ɿ�,������顣
//    for (i = 1; i < 20; i++) { //i��������
//      for (j = 1; j < 20; j++) { //j�Ǻ�����
//        zb[j][i][SQBZXB] = 0; //�ָ�ÿ�����������־
//        //if(zb[j][i][ZTXB]>0 && zb[j][i][KSYXB]==0){
//        //zb[j][i][QSXB]=jszq(j,i);
//        //}
//      }
//    }
//    //�������������������
//    for (i = -127; i <= ziki; i++) {
//      //byte qi=jskq(ki);�������������ֱ�Ӵ�������.
//      jskq(i);
//    } //�������
//
//    for (i = 1; i <= qiki; i++) { //���������֣��ҵ�ǿ�顣
//      meikuaizishu = qikuai[qiki].zishu;
//      if (meikuaizishu == 1) { //��λ
//        //���ܱ�Ȼ���ӡ�
//        m = qikuai[qiki].zichuang.a;
//        n = qikuai[qiki].zichuang.b;
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
//        if (qkxlbzs == 0) { //�ڷ�����
//          for (byte k = 0; k < 4; k++) {
//            m1 = (byte) (m + szld[k][0]);
//            n1 = (byte) (n + szld[k][1]);
//            if (zb[m1][n1][ZTXB] == BLACK) {
//
//            }
//
//          }
//        }
//        else if (qkxlhzs == 0) { //�׷����ۣ�����һ�塣
//
//        }
//        else { //˫���Ĺ�����
//
//        }
//        //qikuai[qiki--].zichuang=null;
//        //qikuai[qiki].qichuang
//        //qikuai[ki--][1][1]=0;
//        //zb[j][i][KSYXB]=0;//�ǿ�
//        //todo:��λ�Ĵ���
//        //�ҳ���Χ�飬����Ϊһ�ҵ��ӣ����١�����Ϊһ����
//        //��˫�ᣬ��Ϊһ�������໹һ����
//      }
//      else if (meikuaizishu > 1) { //����
//        qikuai[qiki].zishu = meikuaizishu;
//        m = qikuai[qiki].zichuang.a;
//        n = qikuai[qiki].zichuang.b;
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
//        if (qkxlbzs == 0) { //�ڷ��Ĵ���
//          //��Χ�Ŀ�����Խ��Խ�á�
//          for (byte k = 0; k < 4; k++) {
//            m1 = (byte) (m + szld[k][0]);
//            n1 = (byte) (n + szld[k][1]);
//            if (zb[m1][n1][ZTXB] == BLACK) {
//
//            }
//
//          }
//        }
//        else if (qkxlhzs == 0) { //�׷��Ĵ��ۣ�����һ�塣
//
//        }
//        else { //˫���Ĺ�����
//
//        }
//
//      }
//      else {
//        System.out.println("error:zishu<1");
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
//           else{//ע���ֹ����ظ���
//           }
//         }
//       }//for
//     }*/
//    byte zijishu = 0;
//    byte kzishu = 0; //�麬�е�����������ѭ��
//    DianNode temp;
//    HaoNode linhao;
//    byte kin1;
//    for (i = -127; i <= ziki; i++) {
//      color = zikuai[i].color;
//      if (color == 1) {
//        othercolor = 2;
//      }
//      else if (color == 2) {
//        othercolor = 1;
//        //�����ÿ�ĸ����ӣ��õ���Χ�飬�����ֹ�ظ���
//      }
//      kzishu = zikuai[i].zishu;
//
//      for (temp = kuai[i].zichuang; temp != null; temp = temp.next) {
//        for (j = 0; j < 4; j++) {
//
//          m = (byte) (temp.a + szld[j][0]);
//          n = (byte) (temp.b + szld[j][1]);
//          if (zb[m][n][ZTXB] == othercolor) {
//
//            //else if(zb[m][n][SQBZXB]==0){//ע���ֹ����ظ���
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
//  public void jushipanduan() { //�����ж�
//    //���ڳɿ��˼����С����Ȼ����û�����⣬������Ҫ���ϣ�
//    //���ͽ��ˡ����糣������˼�ϡ���ˣ����ǵ������Ի�����Ƿȱ��
//    //���ǵ�����������˵��ȷʵ��ȷ�����ӡ�
//    //������ڽ׶ε��жϣ���Ҫָ�꣺�Ƿ��Ѿ�ȫ����ϵ��
//    //���������Ƿ��д󳡣��ռ䡢��Ͷ�ȡ������Ƿ��Ѿ�ȷ����
//    //��ʾ�õ����̺ͼ����õ�����Ӧ����������
//    //��ʾ�����̣���Ϊ�������Ҫ���¿����µĿ�ţ�����������أ�
//    for (byte i = -127; i < 127; i++) {
//      //�ȷֳɾֲ�����ȫ���ܽᡣ
//      //Ѱ���������ӵ㣬��������Ҫ�Ժͽ����ԣ���Ҫ�����������٣�
//      //�������ǶϿ��������塣
//      //if()
//
//    }
//
//    for (byte i = -127; i < 127; i++) {
//      //���ڵĿ�������ܵĹ�ϵ�������ȳ�Ϊһ�����壻�����С��Ĺ�ϵ��
//      //����ϵ���еĹ�Ϊһ�����塣
//      if (kuai[i + 128].qishu == 1) {
//        //�����ڿ飻������ڿ���>1
//
//      }
//
//    }
//
//  }
//
//  public void chengkuai(byte a, byte b, byte color) {
//    //�ռ���Ϣ�Ĺ�����,������color=BLANK,���øú���,�����������Ϣ
//    //����פ����kuai������,���������ò����.
//    //byte max=127;
//    byte m1, n1;
//    byte othercolor = 0;
//    DianNode temp = new DianNode();
//
//    if (color == 1) {
//      othercolor = 2;
//    }
//    else if (color == 2) {
//      othercolor = 1;
//    }
//    temp.a = a;
//    temp.b = b;
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
//  } //�ɿ�ĵ�SQBZXB==1;
//
//  //9��14���ض���
//  // �۵ķ��ࣺ
//  final byte JiaYan = 1; //����
//  final byte NianJieYan = 2; //�ٲ���������˵�ֲ���ʤ������
//  //���������:�����ռ���ɴ�ٵĽ��������
//  final byte KePoYan = 3; //���ƿɳ���
//  final byte TiJieYan = 4; //2��4���ڴ�٣������в��
//  final byte ZhenYan = 5; //����
//  final byte BuQueDing = 0; //����
//  //�����Ҳ��Ǵ�ٵĻ������γ��ۡ�
//  byte yanweixingzhi(byte qkh) { //�ж���λ�����ʡ�
//    //������ʣ���Ҫ�ǳɵ��۵��ĸ������ռ�����
//    byte a = qikuai[qkh].zichuang.a;
//    byte b = qikuai[qkh].zichuang.b;
//    byte dds = 0;
//    byte m = 0, n = 0;
//    byte[] leixingjishu = {
//        0, 0, 0, 0, 0, 0};
//    byte yancolor = qikuai[zb[a][b][KSYXB]].color;
//    for (byte i = 0; i < 4; i++) {
//      m = (byte) (a + djd[i][0]);
//      n = (byte) (b + djd[i][1]); //�ĸ��Խǵ㣻
//      if (zb[m][n][ZTXB] >= 0) { //��ֹ�㲻�Ϸ�
//        dds++;
//        leixingjishu[diandexingzhi(m, n, yancolor)]++;
//      }
//    }
//    if (dds == 1) { //����
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
//        return ZhenYan; //�Ѿ�ռ������������
//      }
//      else if(leixingjishu[2] >= 3)      return JiaYan;
//    }
//    return ZhenYan;
//  }
//
//  byte diandexingzhi(byte a, byte b, byte yancolor) {
//    final byte DuiFangYiJingZhanLing = 0;
//    final byte DuiFangNianJieZhanLing = 1;
//    final byte DuiFangTiJieZhanLing = 3; //1��3��Ҫ���
//    final byte DuiFangNengZhanLing = 2;
//    final byte DuiFangBuNengZhanLing = 4;
//    final byte BuQueDing = 5;
//    //�ҷ��Ѿ�ռ����߻���֮�ࡣ
//
//    //����0��ʾ��ȷ����
//    //���е������ڵ��Ѿ���ռ�졣
//    byte i = 0, j = 0;
//    byte m = 0, n = 0;
//    if (zb[a][b][ZTXB] == yszh(yancolor) && zb[a][b][QIANGRUO] == STRONG) {
//      return 0; //ǿ���Ѿ�ֱ��ռ�졣
//    }
//
//    for (i = 0; i < 4; i++) {
//      m = (byte) (a + szld[i][0]);
//      n = (byte) (b + szld[i][1]);
//      if (zb[m][n][ZTXB] == yszh(yancolor) && zb[m][n][QIANGRUO] == STRONG) {
//
//        return DuiFangNengZhanLing; //�ܶϿ�
//      }
//
//      m = (byte) (a + djd[i][0]);
//      n = (byte) (b + djd[i][1]);
//      if (zb[m][n][ZTXB] == yszh(yancolor) && zb[m][n][QIANGRUO] == STRONG) {
//        if (lianjie(a, b, m, n) == true) {
//          return DuiFangNengZhanLing; //�ܼ��
//        }
//      }
//    }
//    return 4;
//  }
//
//  boolean lianjie(byte a, byte b, byte m, byte n) {
//    //a,b��m,n�����Ƿ�����
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
//      System.out.println("���ò�������");
//    }
//    return color;
//  }
//
//  byte yszh(byte color) { //��ɫת����ƴ��̫��������д��
//    if (color == BLACK) {
//      return WHITE;
//    }
//    else if (color == WHITE) {
//      return BLACK;
//    }
//    else {
//      System.out.println("���ò�������");
//    }
//    return color;
//  }
//
//  //void zhuanhuan��byte��
//  public void chengqikuai(byte a, byte b) {
//    //�ռ���Ϣ�Ĺ�����,������color=BLANK,���øú���,�����������Ϣ
//    //����פ����kuai������,���������ò����.
//
//    byte m1, n1;
//    byte color = 0;
//
//    DianNode temp = new DianNode();
//    temp.a = a;
//    temp.b = b;
//    temp.next = qikuai[qiki].zichuang;
//    kuai[qiki].zichuang = temp;
//
//    zb[a][b][SQBZXB] = 1;
//    //zb[a][b][KSYXB]=qiki;//��ʱ��ָ�����������
//    meikuaizishu++; //Ӧ����Ϊȫ�ֱ���
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
//  } //�ɿ�ĵ�SQBZXB==1;
//
//}
//
//
//
//
//
//class Jubu { //�ֲ������Ŀ�š�
//  HaoLian haolian;
//}
////����ľ����ڣ����ͬɫ�ӻ���Ϊ����ģ��Ϳ��Ժϲ�Ϊһ�顣
//
////�������������ȣ���û�����ӣ���Σ��Է���һ�����ܲ������ܡ�
////�ٴΣ��Է��Ķ������ܷ����ܡ�
//
//
//class ErJiKuai { //�����γɵĿ��顣
//  byte color;
//
//  byte kuaishu; //����Ŀ�����
//  HaoNode zckuaihao; //��ɿ���Ŀ�š�
//  byte zhuangtai; //����֮�ࡣ
//
//  HaoNode qkhao; //����ĺţ������ۣ������Ǽ��ۣ�Ҳ�����Ǵ�١�
//  //���������ж�ǿ�����ٸ���أ�������λ��С�ж�ǿ����
//  //�۵��������ܺܳ���
//  //һ����������Ϊ���壬�����п��ǿ�ȶ������ֵ��
//  //���Ǵ��ڵ���֮����ֶΣ�������������һ������
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
////����ȫ��������0Ϊ��ţ����Բ��ơ�

