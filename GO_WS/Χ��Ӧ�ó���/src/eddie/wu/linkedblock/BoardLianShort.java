package eddie.wu.linkedblock;

import java.io.*;

//�������鶼���������С�
//

//bug֮һ��С�ɹң��л����׷�ǿ�г�ͷ���ڷ�����ȡ�ƣ��׷�
//����ʱδ�ܳ��¿顣2004��2��15�ա��Ѿ�����
/**
 * <p>Title: ÿ�����ӽ�����Ӧ�Ŀ����short�ṹ������Χ
 * ����ʹ����û�д��󣬱�������ʱ�ķ��أ����ھ�����Ϊ�����������飬
 * ���ǿ������棬�����������幦�ܣ����幦��ֻ�ڽ��������ʹ�ã���
 * �ҵ���Ҳ��Ϊ�顣���ӵ�ͱ�Ϊ�¿飬���巵��ʱԭ����Ȼ���á�
 * ÿ�������������������Ψһ�ġ�������Ϣ���������
 * ���̸ı䡣</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class BoardLianShort
   implements Cloneable {

   public static final boolean DEBUG = true;

   public static final byte QIPANDAXIAO = 19; //���̴�С

   public static final byte ZBSX = QIPANDAXIAO + 1; //�����������ޣ�����߽磩;
   public static final byte ZBXX = 0; //�����������ޣ�����߽磩;

   public static final byte[][] szld = {
      //�������ܣ���������ᣩ���ӵ�,˳��ɵ�.��-��-��-��
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
      //�������ܶԽǵ�,˳��ɵ�.����-����-����-����
      {
      1, 1}
      , {
      1, -1}
      , {
      -1, -1}
      , {
      -1, 1}
   };

   public byte[][][] zb = new byte[21][21][4];
   //ÿ�����ӵ�����������ԭʼ��Ϣ������������С����Դ���һ�����档
   //ǰ��ά�����̵�����,�����±��1��19;
   //����ά�±�0:����״̬:��1��2;�����Դ���һ�����棩
   //����ά�±�1:�������ı�־;����������ʱҲ������־��
   //����ά�±�2:����;
   //����ά�±�3:�հ׵���������ı�š��ǿհ׵���Ϊ�㡣
   //����ά���������ڴ����������������顢���ĸ��ֻ��ʵ����Щ
   //�����������ʵ�ֹ����Զ��������Ӻ�Ĵ�����̡�

   public static final byte ZTXB = 0; //�±�0�洢״ֵ̬�����ӡ����ӡ����ӣ�;
   public final byte BLANK = 0;
   public final byte BLACK = 1; //1��ʾ����;
   public final byte WHITE = 2; //2��ʾ����;

   public static final byte SQBZXB = 1; //�±�1�洢������־;
   public static final byte QSXB = 2; //�±�2�洢���������ӻ�������������;
   public static final byte QKSYXB = 3; //�±�3�洢�����������հ׵���ԣ�

   public short[][] zbk = new short[21][21]; //ÿ�������ڵĿ�š�
   //������ӿ�ֿ����������ӿ�ĸ�����

   public short shoushu = 0; //��ǰ��������,����֮ǰ����.��1��ʼ;
   //���ӳɿ飬��ki��shoushu�ȼۣ����ǵ���Ȩ������΢���𣩡�

   public byte tiheizishu = 0, tibaizishu = 0; //�ڰױ����Ӽ���
   //���岻�󣬲���˳��������������жϻ������á�

   //����֮һ�������������ܳ���512�֡�
   public short[][] huik = new short[512][12]; //�ɿ�Ŀ��1��4
   //��ԵĿ��5��8����ԵĿ�ţ�9��11��
   //��ͬ���ʵ���ֱ��������һ����ǿ飬�����кڿ�Ͱ׿飬�Լ����顣
   public byte[][] hui = new byte[512][5];
   //��¼��ֵĹ�����Ϣ,���ڻ���;3-4���ŵ㣬1��2�ò��������.
   public ZiKuai1[] zikuai = new ZiKuai1[512];
   //�ӿ�Ĵ��������Ĵ���������ͬ���ӿ�û��ʵ�����еĿ鶼�Ǵ��ڵ�
   //���������������һ��
   public QiKuai1[] qikuai = new QiKuai1[128];
   public ErJiKuai[] erjikuai = new ErJiKuai[128];
   public short zikuaishu = 0; //��ǰ�Ѿ��õ��Ŀ��,��ǰ����;
   public byte qikuaishu = 0;
   public byte erjikuaishu = 0;
   short qikuaizishu = 0; //��ȫ����ϵ���õľֲ�������

   public BoardLianShort(BoardLianShort oldgo) {
      super();

      byte i, j, k;
      short t;

      final byte PANWAIDIAN = -1; //����֮��ı�־;
      for (i = ZBXX; i <= ZBSX; i++) { //2��22�ռ�
         zb[ZBXX][i][ZTXB] = PANWAIDIAN;
         zb[ZBSX][i][ZTXB] = PANWAIDIAN;
         zb[i][ZBXX][ZTXB] = PANWAIDIAN;
         zb[i][ZBSX][ZTXB] = PANWAIDIAN;
      } //2��22�ռ�

      for (i = 1; i < 20; i++) {
         for (j = 1; j < 20; j++) {
            for (k = 0; k < 4; k++) {
               zb[i][j][k] = oldgo.zb[i][j][k];
            }
            zbk[i][j] = oldgo.zbk[i][j];
         }

      }
      zikuaishu = oldgo.zikuaishu;
      qikuaishu = oldgo.qikuaishu;
      tibaizishu = oldgo.tibaizishu;
      tiheizishu = oldgo.tiheizishu;
      shoushu = oldgo.shoushu;

      for (i = 1; i <= qikuaishu; i++) {
         //if(CS.DEBUG_CGCL) System.out.println("qikuai="+i);

         qikuai[i] = new QiKuai1(oldgo.qikuai[i]);
      }
      for (t = 1; t <= zikuaishu; t++) {
         //if(CS.DEBUG_CGCL) System.out.println("zikuai="+t);
         if (oldgo.zikuai[t] == null) {
            // if(CS.DEBUG_CGCL) System.out.println("null is zikuai="+t);
         }
         if (oldgo.zikuai[t].active) {
            //if(CS.DEBUG_CGCL) System.out.println("��Ч��Ϊ��"+t);

         } //ֻ�������õ����
         zikuai[t] = new ZiKuai1(oldgo.zikuai[t]);
      }

      for (t = 0; t < 512; t++) {
         for (j = 0; j < 5; j++) {

            hui[t][j] = oldgo.hui[t][j];
         }
      }
      for (t = 0; t < 512; t++) {
         for (j = 0; j < 12; j++) {
            huik[t][j] = oldgo.huik[t][j];
         }
      }

   }

   public BoardLianShort() {

      byte i, j;
      final byte PANWAIDIAN = -1; //����֮��ı�־;
      for (i = ZBXX; i <= ZBSX; i++) { //2��22�ռ�
         zb[ZBXX][i][ZTXB] = PANWAIDIAN;
         zb[ZBSX][i][ZTXB] = PANWAIDIAN;
         zb[i][ZBXX][ZTXB] = PANWAIDIAN;
         zb[i][ZBSX][ZTXB] = PANWAIDIAN;
      } //2��22�ռ�

      qikuaishu = 1;
      qikuai[qikuaishu] = new QiKuai1();
      qikuai[qikuaishu].color = 0; //?Ҳ��1���á�
      //qikuai[qikuaishu].zishu = (byte) 361;
      // qikuai[qikuaishu].init();
      for (i = 1; i < 20; i++) {
         for (j = 1; j < 20; j++) {
            zb[i][j][QKSYXB] = 1;
            qikuai[qikuaishu].addzidian(i, j);
         }
      }
      if (CS.DEBUG_CGCL) {
         System.out.println("qikuai[qikuaishu].zishu=" +
                            qikuai[qikuaishu].zishu);

      }
   }

   public void qikuaixinxi(byte kin) {
      byte i = kin;
      DianNode1 temp;
      short zishu = 0;
      byte j = 0;
      byte m, n;
      if (CS.DEBUG_CGCL) {
         System.out.println("������������");

      }
      temp = qikuai[i].zichuang;
      zishu = qikuai[i].zishu;
      if (CS.DEBUG_CGCL) {
         System.out.println("���:" + i + "����������:" + zishu);
      }
      if (zishu > 10) {
         return;
      }
      for (j = 1; j <= qikuai[i].zishu; j++) {
         if (temp == null) {
            return;
         }
         m = temp.a;
         n = temp.b;
         if (CS.DEBUG_CGCL) {
            System.out.println("(" + m + "," + n + ") ");
         }
         temp = temp.next;

      }

   }

   public byte pingfen(byte a, byte b) {
      byte fenshu = 0; //0��ʾ��ȷ����
      short zikin = zbk[a][b];

      return fenshu;
   }

   public byte yanxingpanduan(byte kin) {
      //�����жϡ�
      byte yanxing = 0;
      byte i, j;
      byte m, n;
      byte a, b; //���۵�����
      byte color = qikuai[kin].color;
      byte othercolor;
      byte zwzkshu = qikuai[kin].zwzkshu;
      short zishu = qikuai[kin].zishu;
      HaoNode1 temp;
      boolean danduqikuai = true;
      byte yandeweizhi = 0;
      //1:���ۣ�2�����ۣ�3���и��ۡ�
      temp = qikuai[kin].zwzkhao;
      for (i = 1; i <= zwzkshu; i++) {

         if (zikuai[temp.hao].zwqkshu > 1) {
            danduqikuai = false;
            break;
         }
         temp = temp.next;
      }
      if (danduqikuai) {
         //�����ǵ������飬�������������۵Ŀ����ӣ����ǲ���
         //ͨ���������ӡ�

      }
      else {
         //�ж��������أ��ñ���㷨�����
         return 0;
      }

      if (zishu == 1) { //�����������
         yandeweizhi = danyanweizhi(kin); //���۵�λ�á�
         color = qikuai[kin].color;
         if (color == CS.BLACK) {
            othercolor = CS.WHITE;
         }
         else if (color == CS.WHITE) {
            othercolor = CS.BLACK;
         }
         else {
            othercolor = CS.BLANK;
         }
         a = qikuai[kin].zichuang.a;
         b = qikuai[kin].zichuang.b;
         for (i = 0; i < 4; i++) {
            m = (byte) (a + szdjd[i][0]);
            n = (byte) (b + szdjd[i][1]);
            if (zb[m][n][ZTXB] == othercolor) {

            }
            else if (zb[m][n][ZTXB] == CS.BLANK) {
               if (shifoukongzhidian(m, n)) {

               }

            }
         }

         switch (zwzkshu) { //��򵥵������
            case 1: {
               return CS.ZHENYAN;
            }
            case 2: {
               //��Χ�鶼ֻ��һ�����顣
               //���������ӿ�������ԡ�
               if (yandeweizhi == 3) {
                  //�������Ǳ������ĶԽǵ㡣
               }
            }
            case 3: {
               //�۵���ٲ��Ǿ�����Ҫ��������Χ�ĵ��Ӻ�ǿ��
               //Ҳ����˵ֻ�����漰�����������������塣
            }
            case 4: {

            }

            default:
               return yanxing;
         }
      }
      else {
         return yanxing;
      }
   }

   public byte danyanweizhi(byte kin) {
      //�жϵ������۵�λ�á�
      byte yandeweizhi = 0;
      //�������и���λ���߽���λ��
      if (qikuai[kin].zwzkshu != 1) {
         if (CS.DEBUG_CGCL) {
            System.out.println("������������Ϊһ��" + kin);
         }
         return 0;
      }
      byte m = qikuai[kin].zichuang.a;
      byte n = qikuai[kin].zichuang.b;
      if (m == 1 || m == 19) {
         if (n == 1 || n == 19) { //����
            yandeweizhi = CS.JIAOYAN;
         }
         else { //���ұ��ۡ�
            yandeweizhi = CS.BIANYAN;
         }

      }
      else {
         if (n == 1 || n == 19) { //���±���
            yandeweizhi = CS.BIANYAN;
         }
         else { //�и���
            yandeweizhi = CS.ZHONGFUYAN;
         }

      }
      return yandeweizhi;

   }

   public byte jskq(short rs) {
      if (CS.DEBUG_CGCL) {
         System.out.println("���뷽�������������jskq��");
         //��������������ɿ��������Ϣ��������������
      }
      byte qishu = 0; //����������Ӵ��Ѿ�ȷ����
      byte a = 0, b = 0;
      byte m, n;
      byte i, j;
      short zishu = zikuai[rs].zishu; //�������
      DianNode1 temp = zikuai[rs].zichuang;
      DianNode1 qich;
      //if(CS.DEBUG_CGCL) System.out.println("kuaihao:" + rs);
      //if(CS.DEBUG_CGCL) System.out.println("zishu:" + zishu);
      for (i = 1; i <= zishu; i++) {
         m = temp.a;
         n = temp.b;
         zbk[m][n] = rs; //���е����ȷ����

         for (j = 0; j < 4; j++) {
            a = (byte) (m + szld[j][0]);
            b = (byte) (n + szld[j][1]);
            if (CS.DEBUG_CGCL) {
               System.out.print("a=" + a);
            }
            if (CS.DEBUG_CGCL) {
               System.out.print("b=" + b);
            }
            if (zb[a][b][ZTXB] == BLANK) {
               //if(CS.DEBUG_CGCL) System.out.print(" kongdian");
            }
            if (zb[a][b][SQBZXB] == 0) {
               //if(CS.DEBUG_CGCL) System.out.print(" weijisuanguo");
            }
            if (zb[a][b][ZTXB] == BLANK && zb[a][b][SQBZXB] == 0) {
               qishu++;
               //if(CS.DEBUG_CGCL) System.out.println(" ��Ϊһ����");
               zb[a][b][SQBZXB] = 1;
               //���������������
               qich = new DianNode1(a, b);
               qich.next = zikuai[rs].qichuang;
               zikuai[rs].qichuang = qich;
            }
         }
         temp = temp.next;
      } //for
      zikuai[rs].qishu = qishu;

      qich = zikuai[rs].qichuang;
      for (i = 1; i <= qishu; i++) { //�ָ���־
         a = qich.a;
         b = qich.b;
         zb[a][b][SQBZXB] = 0;
         zb[a][b][QSXB] = qishu;
         //zbk[a][b] = rs;
         qich = qich.next;
      }
      if (CS.DEBUG_CGCL) {
         System.out.println("�������Ϊ��" + qishu);
      }
      kdq(rs, qishu);
      if (CS.DEBUG_CGCL) {
         System.out.println("����jskq������");
      }
      return qishu;
   } //2��22�ո�,ԭ��������,��ֻ����ʹ�.

   public void kdq(short kin, byte a) { //�鶨��
      if (CS.DEBUG_CGCL) {
         System.out.println("����kdq������");
      }
      byte m = 0, n = 0;
      short p = 0; //�����һ��ʱ�á�
      short rs = kin;
      p = zikuai[rs].zishu;
      DianNode1 temp = zikuai[rs].zichuang;
      for (byte i = 1; i <= p; i++) {
         m = temp.a;
         n = temp.b;
         zb[m][n][QSXB] = a; //���kkhb��
         zbk[m][n] = kin; //����
         temp = temp.next;
      }
      zikuai[rs].qishu = a;
      if (CS.DEBUG_CGCL) {
         System.out.println("��" + rs + "������Ϊ" + a);
      }
      if (CS.DEBUG_CGCL) {
         System.out.println("����kdq������");
      }
   }

   public void qkdsy(byte kin) { //���鶨������
      //���ӽ�����Χ�ӿ����ӵĹ���
      if (CS.DEBUG_CGCL) {
         System.out.println("����qkdsy������" + kin);
      }
      byte m = 0, n = 0;
      short p = 0; //�����һ��ʱ�á�
      byte rs = kin;
      p = qikuai[rs].zishu;
      DianNode1 temp = qikuai[rs].zichuang;
      HaoNode1 linshi = qikuai[kin].zwzkhao;
      for (short i = 1; i <= p; i++) {
         m = temp.a;
         n = temp.b;
         zb[m][n][SQBZXB] = 0; //���kkhb��
         zb[m][n][QKSYXB] = kin; //����
         temp = temp.next;
      }
      for (byte j = 1; j <= qikuai[kin].zwzkshu; j++) {
         zikuai[linshi.hao].addqikuaihao(kin);
         linshi = linshi.next;
      }
      // zikuai[rs].zishu = a;
      //if(CS.DEBUG_CGCL) System.out.println("����" + rs + "������Ϊ" + a);
      if (CS.DEBUG_CGCL) {
         System.out.println("����qkdsy������");
      }
   }

   public void kjq(byte r, byte tiao) { //����ʱ,�ɿ�ָ�ʹͬɫ�Ӽ���
      short n = 0; //the same color block is eaten
      byte p = 0, q = 0; //û������ʱ,tiaoֻ����ͬɫ.
      short rs = (short) (128 + r);
      n = zikuai[rs].zishu;
      DianNode1 temp = zikuai[rs].zichuang;
      for (byte i = 1; i <= n; i++) {
         p = temp.a;
         q = temp.b;
         zjq(p, q, tiao);
         temp = temp.next;
      }
      zikuai[rs].qishu = 1; //�����ָ�,����Ϊ1.
      //todo:����Ϊa,b
   }

   public void kkhb(short rs1, short rs2) { //8.2����ǰ��,����δ��
      if (CS.DEBUG_CGCL) {
         System.out.println("����kkhb:���ϲ�");
      }
      DianNode1 temp = zikuai[rs1].zichuang;
      DianNode1 temp1 = zikuai[rs2].zichuang;
      byte m = 0, n = 0;
      for (byte j = 1; j <= zikuai[rs2].zishu; j++) {
         m = temp1.a;
         n = temp1.b;
         zbk[m][n] = rs1;
         temp1 = temp1.next;
      }

      for (byte i = 1; i < zikuai[rs1].zishu; i++) {
         temp = temp.next;
      }
      temp.next = zikuai[rs2].zichuang;
      zikuai[rs1].zishu += zikuai[rs2].zishu;
      if (CS.DEBUG_CGCL) {
         System.out.println("����kkhb:���ϲ�\n");
      }
   }

   public boolean validate(byte a, byte b) { //����λ�õ���Ч�ԡ�
      byte m, n, qi = 0;
      //��shoushu����֮ǰ���ã�yise��tongse�ļ���������ͬ��
      byte tongse = (byte) (shoushu % 2 + 1);
      //yi se=1��2,������Ϊ����
      byte yise = (byte) ( (1 + shoushu) % 2 + 1);
      //tong se=1��2,�׺���Ϊż��
      if (a > ZBXX && a < ZBSX && b > ZBXX && b < ZBSX &&
          zb[a][b][ZTXB] == BLANK) {
         //�±�Ϸ�,�õ�հ�
         if (a == hui[shoushu][3] && b == hui[shoushu][4]) { //�Ƿ���ŵ�
            if (CS.DEBUG_CGCL) {
               System.out.print("���Ǵ��ʱ�Ľ��ŵ�,�����ҽٲ�!");
            }
            if (CS.DEBUG_CGCL) {
               System.out.println("���Ϊ��a=" + a + ",b=" + b);
            }
            return false;
         }
         else {
            //if(CS.DEBUG_CGCL) System.out.println("���Ϊ��a=" + a + ",b=" + b);
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
                     qi += zb[m][n][QSXB]; //���۳ɿ���񶼳���
                     qi--;
                  }
               }
            }
            if (qi == 0) {
               if (CS.DEBUG_CGCL) {
                  System.out.print("������ɱ�Ľ��ŵ㣺");
               }
               if (CS.DEBUG_CGCL) {
                  System.out.println("a=" + a + ",b=" + b);
               }
               return false;
            }
            else {
               if (CS.DEBUG_CGCL) {
                  System.out.print("���ǺϷ��ŵ㣺");
               }
               if (CS.DEBUG_CGCL) {
                  System.out.println("a=" + a + ",b=" + b);
               }
               return true;
            }
         }
      }
      else { //��һ�಻�Ϸ���.
         if (CS.DEBUG_CGCL) {
            System.out.print("�õ㲻�Ϸ�,������֮����߸õ��Ѿ����ӣ�");
         }
         if (CS.DEBUG_CGCL) {
            System.out.println("a=" + a + ",b=" + b);
         }
         return false;
      }
   }

   public void output() { //���ÿ�����Ϣ;
      //todo:��Χ�������Ϣ��Ȼ��ȫ�档��Ҫ��һ��������
      DianNode1 temp = null;
      DianNode1 first = null;
      short zishu = 0;
      byte qishu = 0;
      short i = 0;
      byte j = 0;
      byte m, n;
      if (CS.DEBUG_CGCL) {
         System.out.println("���� output");
      }
      if (CS.DEBUG_CGCL) {
         System.out.println("1.�����������");
      }
      for (i = 1; i <= zikuaishu; i++) {
         if (zikuai[i].active == false) {
            continue;
         }
         temp = zikuai[i].zichuang;
         zishu = zikuai[i].zishu;
         if (CS.DEBUG_CGCL) {
            System.out.print("���:" + i + "������:" + zishu);
         }
         if (zishu > 10) {
            continue;
         }
         for (j = 1; j <= zishu; j++) {
            m = temp.a;
            n = temp.b;
            if (zbk[m][n] != i) {
               if (CS.DEBUG_CGCL) {
                  System.out.print("����������" + i);
               }
            }
            if (CS.DEBUG_CGCL) {
               System.out.print("(" + m + "," + n + ")");
            }
            temp = temp.next;
         }
         if (CS.DEBUG_CGCL) {
            System.out.println("");
         }
      }
      if (CS.DEBUG_CGCL) {
         System.out.println("2.�����������");

      }
      for (i = 1; i <= zikuaishu; i++) {
         if (zikuai[i].active == false) {
            continue;
         }
         temp = zikuai[i].qichuang;
         qishu = zikuai[i].qishu;
         if (CS.DEBUG_CGCL) {
            System.out.print("���:" + i + "������:" + qishu);
         }
         if (qishu > 10) {
            continue;
         }
         for (j = 1; j <= qishu; j++) {
            m = temp.a;
            n = temp.b;
            if (CS.DEBUG_CGCL) {
               System.out.print("(" + m + "," + n + ") ");
            }
            temp = temp.next;
         }
         if (CS.DEBUG_CGCL) {
            System.out.println("");
         }
      }
      if (CS.DEBUG_CGCL) {
         System.out.println("3.���������Χ���");
      }
      for (i = 1; i <= zikuaishu; i++) {
         if (zikuai[i].active == false) {
            continue;
         }
         HaoNode1 temp1 = zikuai[i].zwyskhao;
         short kuaishu = zikuai[i].zwyskshu;
         if (CS.DEBUG_CGCL) {
            System.out.print("���:" + i + "����Χ����:" + kuaishu);
         }
         for (j = 1; j <= kuaishu; j++) {

            if (CS.DEBUG_CGCL) {
               System.out.print("(" + temp1.hao + ")");
            }
            temp1 = temp1.next;
         }
         if (CS.DEBUG_CGCL) {
            System.out.println("");
         }
      }

      if (CS.DEBUG_CGCL) {
         System.out.println("4.��������Χ�������");
      }
      for (i = 1; i <= zikuaishu; i++) {
         if (zikuai[i].active == false) {
            continue;
         }
         HaoNode1 temp1 = zikuai[i].zwqkhao;
         short kuaishu = zikuai[i].zwqkshu;
         if (CS.DEBUG_CGCL) {
            System.out.print("���:" + i + "����Χ������:" + kuaishu);
         }
         for (j = 1; j <= kuaishu; j++) {

            if (CS.DEBUG_CGCL) {
               System.out.print("(" + temp1.hao + ")");
            }
            temp1 = temp1.next;
         }
         if (CS.DEBUG_CGCL) {
            System.out.println("");
         }
      }

      if (CS.DEBUG_CGCL) {
         System.out.println("5.������������");

      }
      for (i = 1; i <= qikuaishu; i++) {
         temp = qikuai[i].zichuang;
         zishu = qikuai[i].zishu;
         if (CS.DEBUG_CGCL) {
            System.out.println("���:" + i + "����������:" + zishu);
         }
         if (zishu > 10) {
            continue;
         }
         for (j = 1; j <= qikuai[i].zishu; j++) {
            if (temp == null) {
               break;
            }
            m = temp.a;
            n = temp.b;
            if (CS.DEBUG_CGCL) {
               System.out.println("(" + m + "," + n + ") ");
            }
            temp = temp.next;

         }

      }

      if (CS.DEBUG_CGCL) {
         System.out.print("zikuaishu=" + zikuaishu + ";shoushu=" + shoushu);
      }
      if (CS.DEBUG_CGCL) {
         System.out.println(";tibaizishu=" + tibaizishu + ";tiheizishu=" +
                            tiheizishu);
      }
      if (CS.DEBUG_CGCL) {
         System.out.println("���� output ����");
      }
   }

   public void zjq(byte a, byte b, byte tiao) { //function 6.1
      //��������������
      byte i, m1, j, n1, yiseks = 0;
      short c1 = 0;
      short ysk[] = { //�ͱ��������Ϊ��ɫ��
         0, 0, 0, 0};
      for (i = 0; i < 4; i++) {
         m1 = (byte) (a + szld[i][0]);
         n1 = (byte) (b + szld[i][1]);
         if (zb[m1][n1][ZTXB] == tiao) {
            c1 = zbk[m1][n1];
            /* if (c1 == 0) {
               zb[m1][n1][QSXB] -= 1;
               if (zb[m1][n1][QSXB] < 1) {
                if(CS.DEBUG_CGCL) System.out.println("����ʱ��������:a=" + m1 + ",b=" + n1);
               }
             }*/
            //else {
            for (j = 0; j < yiseks; j++) {
               if (c1 == ysk[j]) {
                  break;
               }
            }
            if (j == yiseks) { //���ظ�
               ysk[yiseks++] = c1;
               delete(c1, a, b);
               kdq(c1, zikuai[c1 + 128].qishu -= 1);
            }
            //}
         }
      }
   }

   public void zzq(byte a, byte b, byte tiao)
   //���ڻ��壨�൱�����ӱ��ᣩ;����������ʱ��������;
   //��֮��ĳ�ӱ�������Է�������.tiaoָ���ӷ�����ɫ��
   {
      if (CS.DEBUG_CGCL) {
         System.out.println("����zzq()");
      }
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
            if (j == yiseks) { //���ظ�
               ysk[yiseks++] = c1;
               cs = c1;
               kdq(c1, zikuai[cs].qishu += 1);
               DianNode1 temp = new DianNode1();
               temp.a = a;
               temp.b = b;
               temp.next = zikuai[cs].qichuang;
               zikuai[cs].qichuang = temp;
            }
            //  }
         }
      }
      zb[a][b][ZTXB] = BLANK;
      zb[a][b][QSXB] = 0;
      zbk[a][b] = 0;
      if (CS.DEBUG_CGCL) {
         System.out.println("����zzq()");
      }
   }

   public void kzq(short rs, byte tiao) { //6.2 yi se kuai bei ti
      //�����ɫ��ʱ,ͬɫ����������
      short n = 0;
      byte p = 0, q = 0;

      n = zikuai[rs].zishu;
      DianNode1 temp = zikuai[rs].zichuang;
      if (CS.DEBUG_CGCL) {
         System.out.println("����kzq()");
      }
      for (byte i = 1; i <= n; i++) {
         p = temp.a;
         q = temp.b;
         zzq(p, q, tiao);
         temp = temp.next;
         //����ԭ����Ϣ,��Ҫ��������Ϣ,���ڻ���ʱ�ָ�
      }
      zikuai[rs].qishu = 0;
      if (CS.DEBUG_CGCL) {
         System.out.println("����kzq()");
      }
   }

   public void shanchuqikuaihao(byte kin) {
      byte kuaishu = qikuai[kin].zwzkshu;
      HaoNode1 linshi = qikuai[kin].zwzkhao;
      for (byte i = 1; i <= kuaishu; i++) {
         zikuai[linshi.hao].deleteqikuaihao(kin);

      }
   }

   public byte[][] houxuandian(byte a, byte b) {
      short zkin = zbk[a][b];
      return houxuandian(zkin);
   }

   public byte[][] houxuandian(short zkin) {
      byte[][] fanhui = new byte[40][2];

      if (CS.DEBUG_CGCL) {
         System.out.println("Ҫ����Ŀ�Ϊ��" + zkin);

      }
      short qikin;
      byte zijishu = 0;
      byte kuaishu = zikuai[zkin].zwqkshu;
      HaoNode1 linshihao = zikuai[zkin].zwqkhao;

      DianNode1 linshidian;
      for (byte i = 1; i <= kuaishu; i++) {
         qikin = linshihao.hao;

         if (qikuai[qikin].color != 5) {
            if (CS.DEBUG_CGCL) {
               System.out.println("��ӵ�е�����Ϊ��" + qikin);
            }
            linshidian = qikuai[qikin].zichuang;
            for (byte j = 1; j <= qikuai[qikin].zishu; j++) {

               fanhui[++zijishu][0] = linshidian.a;
               fanhui[zijishu][1] = linshidian.b;
               if (CS.DEBUG_CGCL) {
                  System.out.println("����Ϊ��" +
                                     linshidian.a + "/" + linshidian.b);
               }
               if (zijishu >= 39) {
                  fanhui[0][0] = 39;
                  return fanhui;
               }
               linshidian = linshidian.next;
            }
         }
         linshihao = linshihao.next;
      }
      fanhui[0][0] = zijishu;
      return fanhui;
   }

   public void cgcl(byte c) {
      byte a = (byte) ( (c - 1) % 19 + 1);
      byte b = (byte) ( (c - 1) / 19 + 1);
      cgcl(a, b);
   } //�ṩһά����Ľӿ�

   public byte jisuansihuo(byte a, byte b, boolean xianhou) {
      //xianhΪtrue;��ΪMAXMIN���̡�����ΪMINMAX���̡�
      //�÷��������õ��������㷨��
      byte j, k;
      byte sihuo = 0;
      byte MAX = 1; //�������ӷ������ӳ�����127��־
      byte MIN = 2; //�������ӷ������Ӳ������ã�127��ʾ
      byte turncolor1 = 0; //������Ҫ�η����ߡ�
      byte turncolor2 = 0; //ԭʼ����ԭ����˭�ߣ�
      byte m1, n1;
      byte SOUSUOSHENDU = 20;
      int jumianshu = 0; //��ǰ���о���š�
      byte cengshu = 0; //��ǰ���в�����
      //�Լ�ʵ�ֵĶ�ջ��
      //boolean keyiqiquan = false;
      short zkin = zbk[a][b]; //��������Ŀ�š�
      byte houxuan[][] = new byte[40][2]; //��ѡ��
      BoardLianShort[] go = new BoardLianShort[100000];
      int[][] st = new int[SOUSUOSHENDU][5]; //�����������Ϊ20��
      //0:�ò���ʼ����š�
      //1:�������һ��ȡmax����min
      //2:��һ����Լ��Ĳ�ȡmax����min
      //3:��ǰ���Ѿ���һ������ȷ���ˡ�Ϊ��Ӧ��ֵ��
      //4:�ò㻹�ж��پ��档��0��ò������
      st[0][0] = 0;
      st[0][4] = 1;

      if (xianhou) { //��������a,b�����֡�
         for (byte i = 0; i < SOUSUOSHENDU; i++) {
            //������������ȡ�
            st[i][1] = MAX; //���²�ȡMAX��
            //���²�(����֮���״̬)ȡMAX��
            st[i][2] = MIN; //��ͬһ��ȡMIN
            i++;
            st[i][1] = MIN; //���²�ȡMAX��
            st[i][2] = MAX;
         }
      }
      else { //������֡�

         for (byte i = 0; i < SOUSUOSHENDU; i++) {
            st[i][1] = MIN; //���²�ȡMAX��
            //��ͬһ��ȡMIN
            st[i][2] = MAX;
            i++;
            st[i][1] = MAX; //���²�ȡMAX��
            st[i][2] = MIN;

         }
      }

      if (zikuai[zkin].color == CS.BLACK) {
         if (xianhou) {
            turncolor1 = CS.BLACK; //Ӧ����˭�ߡ�
            if (CS.DEBUG_CGCL) {
               System.out.println("Ҫ��������Ϊ��ɫ���ֺڷ����ܷ����");
            }
         }
         else {
            turncolor1 = CS.WHITE;
            if (CS.DEBUG_CGCL) {
               System.out.println("Ҫ��������Ϊ��ɫ���ְ׷����ܷ�ɱ�壿");

            }
         }
      }
      else if (zikuai[zkin].color == CS.WHITE) {
         if (xianhou) {
            turncolor1 = CS.WHITE;
            if (CS.DEBUG_CGCL) {
               System.out.println("Ҫ��������Ϊ��ɫ���ְ׷����ܷ����");

            }
         }
         else {
            turncolor1 = CS.BLACK;
            if (CS.DEBUG_CGCL) {
               System.out.println("Ҫ��������Ϊ��ɫ���ֺڷ����ܷ�ɱ�壿");

            }
         }
      }

      if ( (shoushu % 2) == 0) { //ʵ����������˭�ߡ�
         turncolor2 = CS.BLACK;
         if (CS.DEBUG_CGCL) {
            System.out.println("������Ӧ�ú��ߡ�");

         }
      }
      else {
         turncolor2 = CS.WHITE;
         if (CS.DEBUG_CGCL) {
            System.out.println("������Ӧ�ð��ߡ�");

         }
      }

      if (turncolor1 == turncolor2) {
         go[0] = new BoardLianShort(this);
         //this�����ı䡣
      }
      else {
         go[0] = new BoardLianShort(this);
         go[0].qiquan();
         if (CS.DEBUG_CGCL) {
            System.out.println("��Ҫ��Ȩһ����");

         }
      }

      houxuan = houxuandian(zkin);

      byte youxiaodian = 0; //��Υ�����ӹ���ĵ㡣
      byte haodian = 0; //����Ч�����ų�����ֱ�ӵó����۵ĵ㡣
      BoardLianShort temp;
      boolean tichi;
      while (true) {
         //��һ��ѭ����չ�����һ�����档
         cengshu++; //�²�Ĳ�š�
         if (cengshu >= SOUSUOSHENDU) {
            if (CS.DEBUG_CGCL) {
               System.out.println("������20�㣬��û�н�������ز���ȷ���");
            }
         }
         if (CS.DEBUG_CGCL) {
            System.out.println("�µĵ�ǰ����Ϊ��" + cengshu);
         }
         if (st[cengshu][1] == MAX) {
            if (CS.DEBUG_CGCL) {
               System.out.println("��ǰ����˭�ߣ�" + "MAX");

            }
         }
         else if (st[cengshu][1] == MIN) {
            if (CS.DEBUG_CGCL) {
               System.out.println("��ǰ����˭�ߣ�" + "MIN");

            }
         }

         st[cengshu][0] = jumianshu + 1;
         //�²�Ŀ�ʼ�㡣
         if (CS.DEBUG_CGCL) {
            System.out.println("�²�Ŀ�ʼ��������Ϊ��"
                               + (jumianshu + 1));

         }
         youxiaodian = 0;
         haodian = 0;
         tichi = false;

         for (byte i = 1; i <= houxuan[0][0]; i++) {
            //Ŀǰ�������Ǻ�ѡ����֪���������������в���̬�ı�
            //�Ժ�Ӧ�ý��и�ϸ�µĴ�������Ҫչ���ľ���ȷ����

            m1 = houxuan[i][0];
            n1 = houxuan[i][1];

            temp = new BoardLianShort(go[jumianshu]);
            //��չ���ľ��棬��չͬһ���ϼ����档
            if (temp.validate(m1, n1)) { //�жϺϷ��ŵ�
               temp.cgcl(m1, n1);
               youxiaodian++;
               if (st[cengshu][1] == MIN) {
                  if (temp.zb[a][b][QSXB] == 1) {
                     if (CS.DEBUG_CGCL) {
                        System.out.println("������������Ϊ1");
                        //����Ϊ1���������彫��ɱ��
                        //keyiqiquan=true;
                     }
                     continue;
                  }
                  else { //�����������۵��������
                     haodian++;
                     if (CS.DEBUG_CGCL) {
                        System.out.println("չ����ڼ�����" + haodian);
                     }
                     if (CS.DEBUG_CGCL) {
                        System.out.println("a=" + m1 + "b=" + n1);

                     }
                     go[jumianshu + haodian] = temp;
                  }
               }
               else if (st[cengshu][1] == MAX) {
                  if (CS.DEBUG_CGCL) {
                     System.out.println("a=" + m1 + "b=" + n1);
                     //չ���õ��Ĳ���MAX�¡���min�µõ���ǰ��

                  }
                  if (temp.zbk[a][b] == 0) {
                     //����ԵĿ�û�п�ţ�Ҳ����zbk[a][b]==0
                     if (CS.DEBUG_CGCL) {
                        System.out.println("�Ѿ������");
                        //�Ѿ�����ԡ�
                     }
                     st[cengshu][0] = 0;
                     tichi = true;
                     break; //�ò��Ѿ���ȷ����������㹻���Է�����һ�㡣

                  }
                  else {
                     haodian++;
                     if (CS.DEBUG_CGCL) {
                        System.out.println("չ����ڼ�����" + haodian);
                     }
                     if (CS.DEBUG_CGCL) {
                        System.out.println("a=" + m1 + "b=" + n1);

                     }
                     go[jumianshu + haodian] = temp;
                  }
               }

               //jumianshu++;//���ܵ�������ΪҪչ��ͬһ��ԭʼ���档

            } //if is youxiaodian.
         } //for

         if (tichi == true && st[cengshu][1] == MAX) {
            //�ò��Ѿ�ȷ��
            while (true) {
               st[cengshu - 1][4] -= 1;
               if (st[cengshu - 1][4] == 0) {
                  cengshu -= 2;
                  if (CS.DEBUG_CGCL) {
                     System.out.println("�˵�������" + cengshu);
                  }
                  if (CS.DEBUG_CGCL) {
                     System.out.println("�Ҹò���һ��Ϊ-127���ò㲻�����չ��");

                  }
                  st[cengshu][3] = -127;
                  if (cengshu == 0) {
                     sihuo = -127;
                     return sihuo;
                  }
                  else if (cengshu == 1) {
                     sihuo = -127;
                     return sihuo;
                  }
               }
               else {
                  jumianshu = st[cengshu - 1][0]
                     + st[cengshu - 1][4] - 1;

                  cengshu--; //important

                  //st[cengshu - 2][3] = -127;
                  break;
               }
            }
         }

         else if (youxiaodian == 0) {
            //���أ�һ���ǹ��������ӿ��¡�
            if (CS.DEBUG_CGCL) {
               System.out.println("��Ч��Ϊ0");

               //�ݼ�֮��Ĳ�����һ��ȷ��״̬��
               /* if (st[cengshu][1] == MIN) {
                //��Ϊ����������û����Ч���ӵ㣬��֮���Ӻ�����Ϊһ��
                //���������ۻ��壬Ҳ������һ�����ԡ�
                      BoardLianShort lt = new BoardLianShort(go[jumianshu]);
                      jumianshu++;
                      go[jumianshu] = lt;
                      lt.qiquan();
                      st[cengshu][0] = jumianshu;
                      st[cengshu][4] = 1;
                }*/
            }
            if (st[cengshu][1] == MAX) { //����
               st[cengshu][0] = 0;
               //cengshu--;
               //jumianshu=st[cengshu-1][0]-1;
               //min����һ�����塣
               //��һ��min�������¿��ǡ�
               cengshu -= 2; //���������Ѿ�ȷ����
               if (cengshu == 0) {
                  sihuo = 127;
                  return sihuo;
               }

               while (true) {
                  st[cengshu][4] -= 1;
                  if (st[cengshu][4] == 0) {
                     cengshu -= 2;
                     if (cengshu == 0) {
                        sihuo = 127;
                        return sihuo;
                     }
                     else if (cengshu == -1) {
                        sihuo = 127;
                        return sihuo;
                     }

                  }
                  else {
                     jumianshu = st[cengshu][0] + st[cengshu][4] - 1;
                     //st[cengshu - 2][3] = 127;
                     break;
                  }
               }
               continue; //
            }
         }
         else { //��Ч�㲻Ϊ�㡣
            if (haodian == 0) { //�����������Ȩ
               if (CS.DEBUG_CGCL) {
                  System.out.println("�õ�Ϊ0");
               }
               temp = new BoardLianShort(go[jumianshu]);
               jumianshu++;
               go[jumianshu] = temp;
               temp.qiquan();
               st[cengshu][0] = jumianshu;
               st[cengshu][4] = 1;

            }
            else {
               if (CS.DEBUG_CGCL) {
                  System.out.println("�õ����Ϊ��" + haodian);
               }
               st[cengshu][4] = haodian;
               jumianshu += haodian;

            }

         }
      }
   }

   /**
    * �����Ա����洢���ӵĽ������Ϊ����ʱ�仯�٣������洢�ǳ����ġ�
    * ������ʱ������ѡ�񶼳���������ѡ��һ�ּ��ɣ������ӽ���ı��
    */
   byte zhengzijieguo[][] = new byte[127][2];
   public byte[][] jiSuanZhengZi(byte a, byte b) {
      //�������ӣ����ǲ������ں��н����������
      //ΪMAXMIN���̡�
      //�����ӷ����������ӳ���Ϊ127�����Ӳ�����Ϊ-127��
      //���ӷ����ߡ�a,b�Ǳ����ӷ�����е�һ���㡣��Ϊ��ſ��ܸı䡣
      //�÷��������õ��������㷨��

      byte MAX = 1; //�������ӷ�
      byte MIN = 2; //�������ӷ�

      byte m1, n1;

      short zkin = zbk[a][b]; //��������Ŀ�š�

      byte houxuan[][] = new byte[5][2]; //��ѡ�㡲0����0���洢������
      BoardLianShort[] go = new BoardLianShort[100000];
      byte[] za = new byte[100000]; //������go��Ӧ�������ߵ�ĺ����ꡣ
      byte[] zb = new byte[100000]; //������go��Ӧ�������ߵ�������ꡣ
      int jumianshu = 0; //��ǰ���о���š�
      byte SOUSUOSHENDU = 120;
      byte cengshu = 0; //��ǰ���в�����
      int[][] st = new int[SOUSUOSHENDU][5]; //�����������Ϊ100��
      //0:�ò���ʼ����š�
      //1:�������һ��ȡmax����min
      //2:��һ����Լ��Ĳ�ȡmax����min
      //3:��ǰ���Ѿ���һ������ȷ���ˡ�Ϊ��Ӧ��ֵ��
      //4:�ò㻹�ж��پ��档��0��ò������

      for (byte i = 0; i < SOUSUOSHENDU; i++) {
         //������������ȡ�
         st[i][1] = MAX; //���²�ȡMAX���ò���max�¡�
         //���²�(����֮���״̬)ȡMAX��
         st[i][2] = MIN; //��ͬһ��ȡMIN
         i++;
         st[i][1] = MIN; //���²�ȡMAX��
         st[i][2] = MAX;
      }

      byte turncolor1 = 0; //���������Ҫ�η����ߡ�
      byte turncolor2 = 0; //ԭʼ����ԭ����˭�ߣ�
      if (zikuai[zkin].color == CS.BLACK) {
         turncolor1 = CS.WHITE;
         if (CS.DEBUG_JISUANZHENGZI) {
            System.out.println("Ҫ��������Ϊ��ɫ���ְ׷����ܷ����ӣ�");
         }
      }
      else if (zikuai[zkin].color == CS.WHITE) {
         turncolor1 = CS.BLACK;
         if (CS.DEBUG_JISUANZHENGZI) {
            System.out.println("Ҫ��������Ϊ��ɫ���ֺڷ����ܷ����ӣ�");
         }
      }

      if ( (shoushu % 2) == 0) { //ʵ����������˭�ߡ�
         turncolor2 = CS.BLACK;
         if (CS.DEBUG_JISUANZHENGZI) {
            System.out.println("������Ӧ�ú��ߡ�");
         }
      }
      else {
         turncolor2 = CS.WHITE;
         if (CS.DEBUG_JISUANZHENGZI) {
            System.out.println("������Ӧ�ð��ߡ�");
         }
      }
      go[0] = (BoardLianShort)this.clone();
      if (turncolor1 != turncolor2) {
         go[0].qiquan();
         zhengzijieguo[126][0] = 1;
         if (CS.DEBUG_JISUANZHENGZI) {
            System.out.println("��Ҫ��Ȩһ����");
         }
      }
      //1.��ʼ��
      byte youxiaodian = 0; //��Υ�����ӹ���ĵ㡣�������ӷ�MAX.
      byte haodian = 0; //���ڱ����ӷ�������Ч�����ų�����ֱ�ӵó����۵ĵ㡣
      BoardLianShort temp;

      st[0][0] = 0;
      st[0][4] = 1;
      jumianshu = 0;

      //2.��ʼ���㡣
      while (true) {
         //��һ��ѭ����չ�����һ�����档

         if (cengshu >= (SOUSUOSHENDU - 1)) {
            if (CS.DEBUG_JISUANZHENGZI) {
               System.out.println("������100�㣬��û�н�������ز���ȷ���");
            }
            return zhengzijieguo;
         }
         else {
            cengshu++; //�²�Ĳ�š�
            if (CS.DEBUG_JISUANZHENGZI) {
               System.out.println("�µĵ�ǰ����Ϊ��" + cengshu);
            }
            st[cengshu][0] = jumianshu + 1;
            //�²�Ŀ�ʼ�㡣
            if (CS.DEBUG_JISUANZHENGZI) {
               System.out.println("�²�Ŀ�ʼ��������Ϊ��"
                                  + (jumianshu + 1));
            }
         }

         youxiaodian = 0;
         haodian = 0;

         temp = (BoardLianShort) (go[jumianshu].clone());
         //Ҫչ���ľ��档
         zkin = temp.zbk[a][b];
         if (st[cengshu][1] == MAX) {
            if (CS.DEBUG_JISUANZHENGZI) {
               System.out.println("��ǰ����˭�ߣ�" + "MAX");
            }
            if (CS.DEBUG_JISUANZHENGZI) {
               System.out.println("��һ����˭�ߣ�" + "MIN");
            }
            if (CS.DEBUG_JISUANZHENGZI) {
               System.out.println("����һ����MIN�ߵõ��ò�");

            }
            DianNode1 lili;
            HaoNode1 yskh; //��ɫ���
            byte ysks; //��ɫ����
            byte tizidianshu = 0;

            ysks = temp.zikuai[zkin].zwyskshu;
            yskh = temp.zikuai[zkin].zwyskhao;
            for (byte jj = 1; jj <= ysks; jj++) { //���뱻��Եĵ㣻
               //��¼��Χһ���ĵ㡣���ִ��󣬴��һ�㲻����һ�����ɡ�

               if (yskh == null) {
                  if (CS.DEBUG_JISUANZHENGZI) {
                     System.out.println("��Ŵ���");
                  }
                  break;
               }
               if (temp.zikuai[yskh.hao].qishu == 1) {

                  short beidachikuaihao = yskh.hao;
                  if (beidachikuaihao > 0) {
                     lili = temp.zikuai[beidachikuaihao].qichuang;
                     tizidianshu++;
                     houxuan[tizidianshu][0] = lili.a;
                     houxuan[tizidianshu][1] = lili.b;
                  }
                  else {
                     if (CS.DEBUG_JISUANZHENGZI) {
                        System.out.println("��Ŵ���");

                     }
                  }
               }
               yskh = yskh.next;
            }

            lili = temp.zikuai[zkin].qichuang;
            if (temp.zikuai[zkin].qishu != 1) {
               if (CS.DEBUG_JISUANZHENGZI) {
                  System.out.println("����������Ϊ1��");
               }
               zhengzijieguo[0][0] = -128;
               return zhengzijieguo; //��ʾ����ʧ�ܡ�
            }

            if (lili == null) {
               if (CS.DEBUG_JISUANZHENGZI) {
                  System.out.println("��������1��");
               }
               zhengzijieguo[0][0] = -128;
               return zhengzijieguo; //��ʾ����ʧ�ܡ�
            }
            tizidianshu++;
            houxuan[tizidianshu][0] = lili.a; //�������ظ��ġ�
            houxuan[tizidianshu][1] = lili.b;
            houxuan[0][0] = tizidianshu;
            if (CS.DEBUG_JISUANZHENGZI) {
               System.out.println("�����ӷ���ѡ����Ϊ" + tizidianshu);
            }

            //�����ӷ��ߡ�
            boolean queding = false;
            for (byte i = 1; i <= houxuan[0][0]; i++) {
               //Ŀǰ�������Ǻ�ѡ����֪���������������в���̬�ı�
               //�Ժ�Ӧ�ý��и�ϸ�µĴ�������Ҫչ���ľ���ȷ����

               m1 = houxuan[i][0];
               n1 = houxuan[i][1];
               temp = new BoardLianShort(go[jumianshu]);
               //��Ϊtemp�����Ѿ����ı䣬�������¸�ֵ��
               //��չ���ľ��棬��չͬһ���ϼ����档
               if (temp.validate(m1, n1)) { //�жϺϷ��ŵ�
                  temp.cgcl(m1, n1);
                  youxiaodian++;
                  if (CS.DEBUG_JISUANZHENGZI) {
                     System.out.println("��" + youxiaodian
                                        + "����ѡ��Ϊ:(" + m1 + "," + n1 + ")");
                  }

                  if (temp.zb[a][b][QSXB] == 1) {
                     if (CS.DEBUG_JISUANZHENGZI) {
                        System.out.println("���Ӻ����ӷ�����Ϊ1");
                     }
                  }
                  else if (temp.zb[a][b][QSXB] == 2) {
                     haodian++;
                     go[jumianshu + haodian] = temp;
                     za[jumianshu + haodian] = m1;
                     zb[jumianshu + haodian] = n1;
                     if (CS.DEBUG_JISUANZHENGZI) {
                        System.out.println("min��" + "(" + m1 + "," + n1 + ")");
                     }

                  }
                  else if (temp.zb[a][b][QSXB] == 3) {
                     if (CS.DEBUG_JISUANZHENGZI) {
                        System.out.println("���Ӻ����ӷ�����Ϊ3");
                     }

                     cengshu -= 1; //���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��

                     st[cengshu][4] -= 1;
                     if (st[cengshu][4] != 0) {
                        jumianshu = st[cengshu][0] + st[cengshu][4] - 1;
                        //st[cengshu - 2][3] = 127;
                        queding = true;
                        break; //����forѭ����
                     }
                     else {
                        while (true) {
                           cengshu -= 2; //���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��
                           if (cengshu == -1) {
                              zhengzijieguo[0][0] = -127;
                              return zhengzijieguo;
                           }

                           st[cengshu][4] -= 1;
                           if (st[cengshu][4] != 0) {
                              jumianshu = st[cengshu][0] + st[cengshu][4] - 1;
                              //st[cengshu - 2][3] = 127;
                              queding = true;
                              break;
                           }
                        }
                     }

                  }
               }
            } //forѭ������
            if (CS.DEBUG_JISUANZHENGZI) {
               System.out.println("��Ч��Ϊ:" + youxiaodian);
            }

            if (queding == true) {
               continue;
            }
            else if (haodian == 0) {
               while (true) {
                  cengshu -= 2; //���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��
                  if (cengshu == 0) {
                     zhengzijieguo[0][0] = 127;

                     byte lins = 0;
                     for (lins = 2; st[lins][0] != 0; lins++) {
                        if (CS.DEBUG_JISUANZHENGZI) {
                           System.out.println("��Ϊ:(" + za[st[lins][0] - 1]
                                              + "," + zb[st[lins][0] - 1] + ")");
                        } //this.cgcl(za[st[lins][0]-1],zb[st[lins][0]-1]);
                        zhengzijieguo[lins - 1][0] = za[st[lins][0] - 1];
                        zhengzijieguo[lins - 1][1] = zb[st[lins][0] - 1];

                     }
                     zhengzijieguo[0][1] = (byte) (lins - 2);
                     return zhengzijieguo;
                  }

                  st[cengshu][4] -= 1;
                  if (st[cengshu][4] != 0) {
                     jumianshu = st[cengshu][0] + st[cengshu][4] - 1;
                     //st[cengshu - 2][3] = 127;
                     break;
                  }
               }

            }
            else {
               st[cengshu][4] = haodian;
               jumianshu += haodian;

            }
         } //max
         else if (st[cengshu][1] == MIN) {
            if (CS.DEBUG_JISUANZHENGZI) {
               System.out.println("��ǰ����˭�ߣ�" + "MIN");
            }
            if (CS.DEBUG_JISUANZHENGZI) {
               System.out.println("����һ����MAX�ߵõ��ò�");
            }
            DianNode1 lili = temp.zikuai[zkin].qichuang;
            if (temp.zikuai[zkin].qishu != 2) {
               if (CS.DEBUG_JISUANZHENGZI) {
                  System.out.println("����������Ϊ����");
               }
               zhengzijieguo[0][0] = -127;
               return zhengzijieguo; //��ʾ����ʧ�ܡ�
            }
            for (byte i = 1; i <= 2; i++) {
               if (lili == null) {
                  if (CS.DEBUG_JISUANZHENGZI) {
                     System.out.println("�����������");
                  }
                  zhengzijieguo[0][0] = -127;
                  return zhengzijieguo; //��ʾ����ʧ�ܡ�
               }
               houxuan[i][0] = lili.a;
               houxuan[i][1] = lili.b;
               lili = lili.next;
            }
            houxuan[0][0] = 2;
            for (byte i = 1; i <= houxuan[0][0]; i++) {
               //Ŀǰ�������Ǻ�ѡ����֪���������������в���̬�ı�
               //�Ժ�Ӧ�ý��и�ϸ�µĴ�������Ҫչ���ľ���ȷ����

               m1 = houxuan[i][0];
               n1 = houxuan[i][1];
               temp = new BoardLianShort(go[jumianshu]);

               //��չ���ľ��棬��չͬһ���ϼ����档
               if (temp.validate(m1, n1)) { //�жϺϷ��ŵ�
                  temp.cgcl(m1, n1);
                  youxiaodian++;
                  go[jumianshu + youxiaodian] = temp;
                  za[jumianshu + youxiaodian] = m1;
                  zb[jumianshu + youxiaodian] = n1;
                  if (CS.DEBUG_JISUANZHENGZI) {
                     System.out.println("max��" + "(" + m1 + "," + n1 + ")");
                  }

               }
            }
            if (youxiaodian == 0) {
               //���أ�һ�������ӷ����ӿ��¡�
               if (CS.DEBUG_JISUANZHENGZI) {
                  System.out.println("��Ч��Ϊ0");

               }
               st[cengshu][0] = 0;

               if (cengshu == 1) { //���ӷ�ֱ�����ӿ��£�
                  zhengzijieguo[0][0] = -127;
                  return zhengzijieguo;
               }

               while (true) {
                  cengshu -= 2; //���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��
                  if (cengshu == -1) {
                     zhengzijieguo[0][0] = -127;

                     for (byte lins = 2; st[lins][0] != 0; lins++) {
                        if (CS.DEBUG_JISUANZHENGZI) {
                           System.out.println("��Ϊ:(" + za[st[lins][0] - 1]
                                              + "," + zb[st[lins][0] - 1] + ")");
                        }
                        zhengzijieguo[lins - 1][0] = za[st[lins][0] - 1];
                        zhengzijieguo[lins - 1][1] = zb[st[lins][0] - 1];

                     }

                     return zhengzijieguo;
                  }

                  st[cengshu][4] -= 1;
                  if (st[cengshu][4] != 0) {
                     jumianshu = st[cengshu][0] + st[cengshu][4] - 1;
                     //st[cengshu - 2][3] = 127;
                     break;
                  }
               }
            }
            else {

               st[cengshu][4] = youxiaodian;
               jumianshu += youxiaodian;
            }

         } //if min

      } //while
   }

   public byte[][] jisuanzhengzijiezheng(byte a, byte b) {
      //�������ӣ����ǲ������ں��н����������
      //ΪMAXMIN���̡�
      //�����ӷ����������ӳ���Ϊ127�����Ӳ�����Ϊ-127��
      //���ӷ����ߡ�a,b�Ǳ����ӷ�����е�һ���㡣��Ϊ��ſ��ܸı䡣
      //�÷��������õ��������㷨��

      byte MAX = 1; //�������ӷ�
      byte MIN = 2; //�������ӷ�
      byte JIE = 3;
      byte m1, n1;

      short zkin = zbk[a][b]; //��������Ŀ�š�

      byte houxuan[][] = new byte[5][2]; //��ѡ�㡲0����0���洢������
      BoardLianShort[] go = new BoardLianShort[100000];
      byte[] za = new byte[100000]; //������go��Ӧ�������ߵ�ĺ����ꡣ
      byte[] zb = new byte[100000]; //������go��Ӧ�������ߵ�������ꡣ
      byte[] zc = new byte[100000];
      //��־�Ÿþ����Ѿ������������һ�ֲ���ֱ���ᡣ

      int jumianshu = 0; //��ǰ���о���š�
      byte SOUSUOSHENDU = 120;
      byte cengshu = 0; //��ǰ���в�����
      int[][] st = new int[SOUSUOSHENDU][5]; //�����������Ϊ100��
      //0:�ò���ʼ����š�
      //1:�������һ��ȡmax����min
      //2:����
      //3:��ǰ���Ѿ�ȡ�õ���Ӧ��ֵ��
      //4:�ò㻹�ж��پ��档��0��ò������

      for (byte i = 0; i < SOUSUOSHENDU; i++) {
         //������������ȡ�
         st[i][1] = MAX; //���²�ȡMAX���ڸò��������max�¡�
         //���²�(����֮���״̬)ȡMAX��
         st[i][2] = MIN; //��ͬһ��ȡMIN
         st[i][3] = -127;
         i++;
         st[i][1] = MIN; //���²�ȡMIN���ڸò��������mIN�¡�
         //���²�(����֮���״̬)ȡMIN��
         st[i][2] = MAX;
         st[i][3] = 127;
      }

      byte turncolor1 = 0; //���������Ҫ�η����ߡ�
      byte turncolor2 = 0; //ԭʼ����ԭ����˭�ߣ�
      if (zikuai[zkin].color == CS.BLACK) {
         turncolor1 = CS.WHITE;
         if (CS.DEBUG_JISUANZHENGZI) {
            System.out.println("Ҫ��������Ϊ��ɫ���ְ׷����ܷ����ӣ�");
         }
      }
      else if (zikuai[zkin].color == CS.WHITE) {
         turncolor1 = CS.BLACK;
         if (CS.DEBUG_JISUANZHENGZI) {
            System.out.println("Ҫ��������Ϊ��ɫ���ֺڷ����ܷ����ӣ�");
         }
      }

      if ( (shoushu % 2) == 0) { //ʵ����������˭�ߡ�
         turncolor2 = CS.BLACK;
         if (CS.DEBUG_JISUANZHENGZI) {
            System.out.println("������Ӧ�ú��ߡ�");
         }
      }
      else {
         turncolor2 = CS.WHITE;
         if (CS.DEBUG_JISUANZHENGZI) {
            System.out.println("������Ӧ�ð��ߡ�");
         }
      }
      go[0] = (BoardLianShort)this.clone();
      if (turncolor1 != turncolor2) {
         go[0].qiquan();
         zhengzijieguo[126][0] = 1;
         if (CS.DEBUG_JISUANZHENGZI) {
            System.out.println("��Ҫ��Ȩһ����");
         }
      }
      //1.��ʼ��
      byte youxiaodian = 0; //��Υ�����ӹ���ĵ㡣�������ӷ�MAX.
      byte haodian = 0; //���ڱ����ӷ�������Ч�����ų�����ֱ�ӵó����۵ĵ㡣
      BoardLianShort temp;

      st[0][0] = 0;
      st[0][4] = 1;
      jumianshu = 0;

      //2.��ʼ���㡣
      while (true) {
         //��һ��ѭ����չ�����һ�����档

         if (cengshu >= (SOUSUOSHENDU - 1)) {
            if (CS.DEBUG_JISUANZHENGZI) {
               System.out.println("������100�㣬��û�н�������ز���ȷ���");
            }
            return zhengzijieguo;
         }
         else {
            cengshu++; //�²�Ĳ�š�
            if (CS.DEBUG_JISUANZHENGZI) {
               System.out.println("�µĵ�ǰ����Ϊ��" + cengshu);
            }
            st[cengshu][0] = jumianshu + 1;
            //�²�Ŀ�ʼ�㡣
            if (CS.DEBUG_JISUANZHENGZI) {
               System.out.println("�²�Ŀ�ʼ��������Ϊ��"
                                  + (jumianshu + 1));
            }
         }

         youxiaodian = 0;
         haodian = 0;

         temp = (BoardLianShort) (go[jumianshu].clone());
         //Ҫչ���ľ��档
         zkin = temp.zbk[a][b];
         if (st[cengshu][1] == MAX) {
            if (CS.DEBUG_JISUANZHENGZI) {
               System.out.println("��ǰ����˭�ߣ�" + "MAX");

               System.out.println("��һ����˭�ߣ�" + "MIN");

               System.out.println("����һ����MIN�ߵõ��ò�");

            }
            DianNode1 lili;
            HaoNode1 yskh; //��ɫ���
            byte ysks; //��ɫ����
            byte tizidianshu = 0;

            ysks = temp.zikuai[zkin].zwyskshu;
            yskh = temp.zikuai[zkin].zwyskhao;
            for (byte jj = 1; jj <= ysks; jj++) { //���뱻��Եĵ㣻
               //��¼��Χһ���ĵ㡣���ִ��󣬴��һ�㲻����һ�����ɡ�

               if (yskh == null) {
                  if (CS.DEBUG_JISUANZHENGZI) {
                     System.out.println("��Ŵ���");
                  }
                  break;
               }
               if (temp.zikuai[yskh.hao].qishu == 1) {

                  short beidachikuaihao = yskh.hao;
                  if (beidachikuaihao > 0) {
                     lili = temp.zikuai[beidachikuaihao].qichuang;
                     tizidianshu++;
                     houxuan[tizidianshu][0] = lili.a;
                     houxuan[tizidianshu][1] = lili.b;
                  }
                  else {
                     if (CS.DEBUG_JISUANZHENGZI) {
                        System.out.println("��Ŵ���");

                     }
                  }
               }
               yskh = yskh.next;
            }

            lili = temp.zikuai[zkin].qichuang;
            if (temp.zikuai[zkin].qishu != 1) {
               if (CS.DEBUG_JISUANZHENGZI) {
                  System.out.println("����������Ϊ1��");
               }
               zhengzijieguo[0][0] = -128;
               return zhengzijieguo; //��ʾ����ʧ�ܡ�
            }

            if (lili == null) {
               if (CS.DEBUG_JISUANZHENGZI) {
                  System.out.println("��������1��");
               }
               zhengzijieguo[0][0] = -128;
               return zhengzijieguo; //��ʾ����ʧ�ܡ�
            }
            tizidianshu++;
            houxuan[tizidianshu][0] = lili.a; //�������ظ��ġ�
            houxuan[tizidianshu][1] = lili.b;
            houxuan[0][0] = tizidianshu;
            if (CS.DEBUG_JISUANZHENGZI) {
               System.out.println("�����ӷ���ѡ����Ϊ" + tizidianshu);
            }

            //�����ӷ��ߡ�
            boolean queding = false;
            for (byte i = 1; i <= houxuan[0][0]; i++) {
               //Ŀǰ�������Ǻ�ѡ����֪���������������в���̬�ı�
               //�Ժ�Ӧ�ý��и�ϸ�µĴ�������Ҫչ���ľ���ȷ����

               m1 = houxuan[i][0];
               n1 = houxuan[i][1];
               temp = new BoardLianShort(go[jumianshu]);
               //��Ϊtemp�����Ѿ����ı䣬�������¸�ֵ��
               //��չ���ľ��棬��չͬһ���ϼ����档
               if (temp.validate(m1, n1)) { //�жϺϷ��ŵ�
                  temp.cgcl(m1, n1);
                  youxiaodian++;
                  if (CS.DEBUG_JISUANZHENGZI) {
                     System.out.println("��" + youxiaodian
                                        + "����ѡ��Ϊ:(" + m1 + "," + n1 + ")");
                  }

                  if (temp.zb[a][b][QSXB] == 1) {
                     if (CS.DEBUG_JISUANZHENGZI) {
                        System.out.println("���Ӻ����ӷ�����Ϊ1");
                     }
                  }
                  else if (temp.zb[a][b][QSXB] == 2) {
                     haodian++;
                     go[jumianshu + haodian] = temp;
                     za[jumianshu + haodian] = m1;
                     zb[jumianshu + haodian] = n1;
                     if (CS.DEBUG_JISUANZHENGZI) {
                        System.out.println("min��" + "(" + m1 + "," + n1 + ")");
                     }
                     //cgcl��Ӧ���д�ٵ���Ϣ��
                     /*if (temp.zb[m1][n1][QSXB] == 1) {
                         if (temp.zikuai[temp.huik[temp.shoushu][5]].zishu == 1) {
                           System.out.println("dajie");
                        }
                                           }*/
                     if (temp.hui[temp.shoushu][3] != 0 &&
                         temp.hui[temp.shoushu][4] != 0) { //���ϲ�ľ�������
                        int shangcengjumianshu = st[cengshu - 1][4];
                        if (st[cengshu - 1][4] > 1) {
                           BoardLianShort tempjiaohuan;
                           tempjiaohuan = go[jumianshu];
                           go[jumianshu] = go[jumianshu - shangcengjumianshu +
                              1];
                           go[jumianshu - shangcengjumianshu +
                              1] = tempjiaohuan;
                           queding = true;
                           break;
                           //����չ����ʹ����ٵ��ŷ�����չ����

                        }
                        else if (st[cengshu - 1][4] == 1) {
                           zc[jumianshu + haodian] = JIE;
                        }
                        else {

                        }
                     }
                  }
                  else if (temp.zb[a][b][QSXB] == 3) {
                     if (CS.DEBUG_JISUANZHENGZI) {
                        System.out.println("���Ӻ����ӷ�����Ϊ3");
                     }
                     //�����Ļ��ݡ�
                     byte yuancengshu = cengshu;
                     //��ǰ��ΪMAX���ݼ������һ��Ϊmin��
                     //��ǰ����һ������Ϊ��127������һ�����չ��ǰ����Ϊ��127��
                     cengshu -= 1; //���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��

                     st[cengshu][4] -= 1;
                     if (st[cengshu][4] != 0) {
                        jumianshu = st[cengshu][0] + st[cengshu][4] - 1;
                        //st[cengshu - 2][3] = 127;
                        queding = true;
                        break; //����forѭ����
                     }
                     else { //�ò��Ѿ�ȫ��չ����
                        if (zc[st[cengshu][0] - 1] == JIE) {
                           //��Ȼ�����㣬�������ڴ�٣���ʵ��һ����ٵı仯��
                           cengshu--;

                           st[cengshu][4] -= 1;
                           if (st[cengshu][4] != 0) {
                              jumianshu = st[cengshu][0] + st[cengshu][4] - 1;
                              //st[cengshu - 2][3] = 127;
                              st[cengshu - 1][3] = JIE;
                              queding = true;
                              break; //����forѭ����
                           }

                        }
                        else {//�ǽٵ������
                           while (true) {
                              cengshu -= 2; //���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��
                              if (cengshu == -1) {
                                 zhengzijieguo[0][0] = -127;
                                 return zhengzijieguo;
                              }

                              st[cengshu][4] -= 1;
                              if (st[cengshu][4] != 0) {
                                 jumianshu = st[cengshu][0] + st[cengshu][4] -
                                    1;
                                 //st[cengshu - 2][3] = 127;
                                 queding = true;
                                 break;
                              }
                           }
                        }
                     }

                  }
               }
            } //forѭ������
            if (CS.DEBUG_JISUANZHENGZI) {
               System.out.println("��Ч��Ϊ:" + youxiaodian);
            }

            if (queding == true) {
               continue;
            }
            else if (haodian == 0) {
               while (true) {
                  cengshu -= 2; //���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��
                  if (cengshu == 0) {
                     zhengzijieguo[0][0] = 127;

                     byte lins = 0;
                     for (lins = 2; st[lins][0] != 0; lins++) {
                        if (CS.DEBUG_JISUANZHENGZI) {
                           System.out.println("��Ϊ:(" + za[st[lins][0] - 1]
                                              + "," + zb[st[lins][0] - 1] + ")");
                        } //this.cgcl(za[st[lins][0]-1],zb[st[lins][0]-1]);
                        zhengzijieguo[lins - 1][0] = za[st[lins][0] - 1];
                        zhengzijieguo[lins - 1][1] = zb[st[lins][0] - 1];

                     }
                     zhengzijieguo[0][1] = (byte) (lins - 2);
                     return zhengzijieguo;
                  }

                  st[cengshu][4] -= 1;
                  if (st[cengshu][4] != 0) {
                     jumianshu = st[cengshu][0] + st[cengshu][4] - 1;
                     //st[cengshu - 2][3] = 127;
                     break;
                  }
               }

            }
            else {
               st[cengshu][4] = haodian;
               jumianshu += haodian;

            }
         } //max
         else if (st[cengshu][1] == MIN) {
            if (CS.DEBUG_JISUANZHENGZI) {
               System.out.println("��ǰ����˭�ߣ�" + "MIN");
            }
            if (CS.DEBUG_JISUANZHENGZI) {
               System.out.println("����һ����MAX�ߵõ��ò�");
            }
            DianNode1 lili = temp.zikuai[zkin].qichuang;
            if (temp.zikuai[zkin].qishu != 2) {
               if (CS.DEBUG_JISUANZHENGZI) {
                  System.out.println("����������Ϊ����");
               }
               zhengzijieguo[0][0] = -127;
               return zhengzijieguo; //��ʾ����ʧ�ܡ�
            }
            for (byte i = 1; i <= 2; i++) {
               if (lili == null) {
                  if (CS.DEBUG_JISUANZHENGZI) {
                     System.out.println("�����������");
                  }
                  zhengzijieguo[0][0] = -127;
                  return zhengzijieguo; //��ʾ����ʧ�ܡ�
               }
               houxuan[i][0] = lili.a;
               houxuan[i][1] = lili.b;
               lili = lili.next;
            }
            houxuan[0][0] = 2;
            for (byte i = 1; i <= houxuan[0][0]; i++) {
               //Ŀǰ�������Ǻ�ѡ����֪���������������в���̬�ı�
               //�Ժ�Ӧ�ý��и�ϸ�µĴ�������Ҫչ���ľ���ȷ����

               m1 = houxuan[i][0];
               n1 = houxuan[i][1];
               temp = new BoardLianShort(go[jumianshu]);

               //��չ���ľ��棬��չͬһ���ϼ����档
               if (temp.validate(m1, n1)) { //�жϺϷ��ŵ�
                  temp.cgcl(m1, n1);
                  youxiaodian++;
                  go[jumianshu + youxiaodian] = temp;
                  za[jumianshu + youxiaodian] = m1;
                  zb[jumianshu + youxiaodian] = n1;
                  if (CS.DEBUG_JISUANZHENGZI) {
                     System.out.println("max��" + "(" + m1 + "," + n1 + ")");
                  }

               }
            }
            if (youxiaodian == 0) {
               //���أ�һ�������ӷ����ӿ��¡�
               if (CS.DEBUG_JISUANZHENGZI) {
                  System.out.println("��Ч��Ϊ0");

               }
               st[cengshu][0] = 0;

               if (cengshu == 1) { //���ӷ�ֱ�����ӿ��£�
                  zhengzijieguo[0][0] = -127;
                  return zhengzijieguo;
               }

               while (true) {
                  cengshu -= 2; //���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��
                  if (cengshu == -1) {
                     zhengzijieguo[0][0] = -127;

                     for (byte lins = 2; st[lins][0] != 0; lins++) {
                        if (CS.DEBUG_JISUANZHENGZI) {
                           System.out.println("��Ϊ:(" + za[st[lins][0] - 1]
                                              + "," + zb[st[lins][0] - 1] + ")");
                        }
                        zhengzijieguo[lins - 1][0] = za[st[lins][0] - 1];
                        zhengzijieguo[lins - 1][1] = zb[st[lins][0] - 1];

                     }

                     return zhengzijieguo;
                  }

                  st[cengshu][4] -= 1;
                  if (st[cengshu][4] != 0) {
                     jumianshu = st[cengshu][0] + st[cengshu][4] - 1;
                     //st[cengshu - 2][3] = 127;
                     break;
                  }
               }
            }
            else {

               st[cengshu][4] = youxiaodian;
               jumianshu += youxiaodian;
            }

         } //if min

      } //while
   }

   public void delete(short r, byte a, byte b) {
      //���ӿ��������ɾ��һ���������ꡣ
      //��Ϊ�л��壬���Բ��������������������漰�顣
      //�������ò���ȷ����������������kdq����ȷ�б�Ҫ��
      if (CS.DEBUG_CGCL) {
         System.out.println("����delete()");

      }
      DianNode1 temp = zikuai[r].qichuang;
      DianNode1 forward = zikuai[r].qichuang;
      byte qishu = zikuai[r].qishu;
      for (byte i = 1; i <= qishu; i++) {
         if (a == temp.a & b == temp.b) {
            if (i == 1) {
               zikuai[r].qichuang = temp.next;

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
      if (CS.DEBUG_CGCL) {
         System.out.println("����delete()");
      }
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
      //������һ�����ѵĿ��⣬������������Ҫ�Կ���
      //ͨ�����ֵ���ʷ������������ʹ����ȫ���滻��
      //�������ֿ����Ѿ��ڱ𴦱����ã����⸴�ӻ��ˡ�
      //���洦���Ѿ������ǺϷ��ŵ㣻
      //���Խ��ܵ�����Ϊ(a,b)��c;c=b*19+a-19;�����������
      //a����������±�,Ҳ��ƽ��ĺ�����:1-19
      //b����������±�,Ҳ����Ļ��������:1-19
      //byte c;//a,b��һά��ʾ:1-361;

      byte m1 = a; //(a,b)�ڷ����в��ı�
      byte n1 = b; //(m1,n1)Ϊ(a,b)���ڵ�.

      byte yise = 0; //�����ӵ���ɫ
      byte tongse = 0; //�����ӵ�ͬɫ

      //���ֵ�ļ���
      byte yisedianshu = 0; //Ϊ(a,b)��Χ��ɫ�����
      byte kongbaidianshu = 0; //Ϊ(a,b)��Χ�հ׵����
      byte tongsedianshu = 0; //Ϊ(a,b)��Χͬɫ�����

      byte i = 0, j = 0; //ѭ������
      byte jubutizishu = 0; //�ֲ�������(ÿһ����������)

      byte tkd = 0;

      byte[][] zijieqi = new byte[4][2]; //���ڻ�������ʱ�á�
      byte yuanshizhijieqishu = 0; //ֱ�����������ӵ���Χֱ�ӵ�����
      //�����������γɵ�����,����Ҫ������ǰ���á�

      short kin1 = 0; //a,b��Χ�ĵ�Ŀ�����
      short[] ysk = {
         0, 0, 0, 0}; //����ɫ���ӵĿ�����,ͬ�鲻�ظ�����
      byte yiseks = 0; //������ɫ����

      byte yuanqikuaisuoyin; //���ӵ�ԭ�����ڵ������������
      short yuanqikuaizishu; //���ӵ�ԭ�����ڵ������������
      short zishujishu; //���ӵ����������������֮�͡�

      if (CS.DEBUG_CGCL) {
         System.out.println("���뷽��cgcl()");
      }

      if (validate(a, b) == false) { //1.�ж����ӵ����Ч�ԡ�
         return;
      }

      shoushu++;
      hui[shoushu][1] = a; //��������ǰ����,����1��ʼ����.������ͬ.
      hui[shoushu][2] = b; //��¼ÿ�������
      if (CS.DEBUG_CGCL) {
         System.out.println("���ӵ�:" + a + "/" + b);
      }

      //���������ж����ӵ����ɫ������
      yise = (byte) (shoushu % 2 + 1); //yi se=1��2,������Ϊ����
      tongse = (byte) ( (1 + shoushu) % 2 + 1); //tong se=1��2,�׺���Ϊż��

      zb[a][b][ZTXB] = tongse; //���Զ�̬һ��

      //2.�������ӵ���Χ��ֱ�����㡣

      yuanqikuaisuoyin = zb[a][b][QKSYXB];
      if (CS.DEBUG_CGCL) {
         System.out.println("���ӵ�������������Ϊ��" + yuanqikuaisuoyin);
      }
      qikuaixinxi(yuanqikuaisuoyin);
      zb[a][b][QKSYXB] = 0;
      qikuai[yuanqikuaisuoyin].deletezidian(a, b);
      yuanqikuaizishu = qikuai[yuanqikuaisuoyin].zishu;
      //�Ѿ��۳����ӵ㡣
      if (CS.DEBUG_CGCL) {
         System.out.println("���ӵ���������������Ϊ��"
                            + yuanqikuaizishu);
         //ԭ�������Ӻ��������
      }

      if (qikuai[yuanqikuaisuoyin].zishu == 0) {
         shanchuqikuaihao(yuanqikuaisuoyin);

      }
      zishujishu = 0; //�ж��Ƿ������������ɡ�

      if (CS.DEBUG_CGCL) {
         System.out.println("�㡢������ʼ���ӿ顣");
      }

      ZiKuai1 linshi = new ZiKuai1();

      linshi.color = zb[a][b][ZTXB];
      linshi.zishu = 1;
      linshi.addzidian(a, b); //���ӿ�ĳ�ʼ����
      zikuaishu++;
      zikuai[zikuaishu] = linshi;
      zbk[a][b] = zikuaishu; //count from first block
      huik[shoushu][0] = zikuaishu; //��¼���ɿ������

      if (CS.DEBUG_CGCL) {
         System.out.print("һ����¼ֱ������ֱ������Ϊ");
      }
      for (i = 0; i < 4; i++) { //ֱ�ӵ����������������ɵ�����
         m1 = (byte) (a + szld[i][0]);
         n1 = (byte) (b + szld[i][1]);
         if (zb[m1][n1][ZTXB] == BLANK) { //2.1the breath of blank
            zijieqi[yuanshizhijieqishu][0] = m1;
            zijieqi[yuanshizhijieqishu][1] = n1;
            yuanshizhijieqishu++;
         }
      }
      if (CS.DEBUG_CGCL) {
         System.out.println("���ӵ���Χ��ԭʼֱ������Ϊ��"
                            + yuanshizhijieqishu);

      }

      if (CS.DEBUG_CGCL) {
         System.out.println("��������ͬɫ����");
      }
      for (i = 0; i < 4; i++) { //�ٴ���ͬɫ����
         m1 = (byte) (a + szld[i][0]);
         n1 = (byte) (b + szld[i][1]);
         //��Ϊkkhbʱ�Ѿ��ı���ԭ����������ţ����Բ������ظ���
         //�㷨���������в�ͬ��

         kin1 = zbk[m1][n1];

         if (zb[m1][n1][ZTXB] == tongse && kin1 != zikuaishu) { //3.1
            if (CS.DEBUG_CGCL) {
               System.out.println("ͬɫ�㣺a=" + m1 + ",b=" + n1);
            }
            tongsedianshu++; //ͬɫ�����
            if (CS.DEBUG_CGCL) {
               System.out.println("kin1��" + kin1);
            }
            huik[shoushu][tongsedianshu] = kin1;
            HaoNode1 teli = zikuai[kin1].zwyskhao; //�޸���Χ��ŵ�ָ��

            if (teli == null) {
               if (CS.DEBUG_CGCL) {
                  System.out.println("meiyoulinkuai" + kin1);
               }
            }
            for (byte p = 1; p <= zikuai[kin1].zwyskshu; p++) {
               zikuai[teli.hao].deleteyisekuaihao(kin1);
               zikuai[teli.hao].addyisekuaihao(zikuaishu); //�޸Ŀ��
               zikuai[zikuaishu].addyisekuaihao(teli.hao);
               teli = teli.next;
            }

            zikuai[kin1].active = false; //���ϲ��Ŀ���������ʧ��new
            kkhb(zikuaishu, kin1);

         }
         //�������С����Χ�������Сֵ��Σ�յģ���Ȼ��Ҫ����
         //�Ƿ��г������ֶΡ�

         /*if (zb[m1][n1][ZTXB] == tongse) { //3.1
           tongsedianshu++; //ͬɫ�����
           kin1 = zbk[m1][n1];
           for (j = 1; j < tongsedianshu; j++) {
             //if(kin1==tsk[j]) break;
             if (kin1 == huik[shoushu][j]) {
               break;
             }
           } //������ͻ�Ĳ�ʹ��ѵ
           if (j == tongsedianshu) { //���ظ�
             //tsk[ks++]=kin1;
             //hui[shoushu][20+ks]=kin1;
             //kuai[kin1].addkuai(zikuaishu);
             huik[shoushu][tongsedianshu] = kin1;
             HaoNode1 teli = zikuai[kin1].zwkhao;
             for (byte p = 1; p <= zikuai[kin1].zwkshu; p++) {
               zikuai[teli.hao].deletekuaihao(kin1);
               zikuai[teli.hao].addkuaihao(zikuaishu);//�޸Ŀ��
               zikuai[zikuaishu].addkuaihao(teli.hao);
               teli=teli.next;
             }
             kkhb(zikuaishu, kin1);
           }
           else {
             tongsedianshu--;
           }
           // } //�ɿ��
                }*/
      }
      if (CS.DEBUG_CGCL) {
         System.out.println("ͬɫ����Ϊ��" + tongsedianshu);

      }
      if (CS.DEBUG_CGCL) {
         System.out.println("����������ɫ����");
      }
      for (i = 0; i < 4; i++) { //�ȴ�����ɫ����
         //byte bdcds = 0; //����Ե����.
         byte bdcks = 0; //����Կ����.
         m1 = (byte) (a + szld[i][0]);
         n1 = (byte) (b + szld[i][1]);
         if (zb[m1][n1][ZTXB] == yise) { //1.1�ұ����ڵ�
            yisedianshu++; //��ɫ�����
            kin1 = zbk[m1][n1]; //������
            //if (kin1==0)Ϊ�顣
            for (j = 0; j < yiseks; j++) {
               if (kin1 == ysk[j]) {
                  break;
               }
            }
            if (j == yiseks) { //���ظ�
               ysk[yiseks++] = kin1;
               byte qi = (byte) (zikuai[kin1].qishu - 1);

               if (qi == 0) { //ԭ��������
                  //�����м���������Ϊ�˻��崦����
                  yisedianshu--;
                  tkd++; //<=4
                  huik[shoushu][4 + tkd] = kin1; //�ݴ˽��ж����鴦��

                  jubutizishu += zikuai[kin1].zishu; //ʵ�ʵ�������
                  zikuai[kin1].active = false; //���Ե���鲻�ٴ��������ϡ�
                  if (CS.DEBUG_CGCL) {
                     System.out.println("�鱻�ԣ����Ϊ��" + kin1);
                  }
                  kzq(kin1, tongse); //��ɫ�鱻��,ͬɫ������.
                  //��Χ�Ŀ�����ڿ��������Ҫɾ���ñ���Ŀ��

                  //�γ��µ����飻
                  qikuaishu++;
                  if (CS.DEBUG_CGCL) {
                     System.out.println("�γ��µ����飻" + qikuaishu);
                  }
                  qikuai[qikuaishu] = new QiKuai1();
                  qikuai[qikuaishu].zichuang = zikuai[kin1].zichuang;
                  qikuai[qikuaishu].zishu = zikuai[kin1].zishu;
                  qikuai[qikuaishu].color = tongse;
                  qkdsy(qikuaishu);
                  //�����µĶ����飬��Χ�Ŀ���ָ��ָ��ö����顣

                  HaoNode1 linh = zikuai[kin1].zwyskhao;
                  byte linhs = zikuai[kin1].zwyskshu;
                  for (byte tt = 1; tt <= linhs; tt++) {

                     zikuai[linh.hao].deleteyisekuaihao(kin1);
                     zikuai[linh.hao].addqikuaihao(qikuaishu); //��������š�
                     linh = linh.next;
                  }

               }

               else if (qi < 0) {
                  if (CS.DEBUG_CGCL) {
                     System.out.println("��������:kin=" + kin1);
                  }
                  return;
               }
               else {
                  zikuai[kin1].deleteqidian(a, b);
                  kdq(kin1, qi);
                  zikuai[kin1].addyisekuaihao(zikuaishu);
                  zikuai[zikuaishu].addyisekuaihao(kin1); //Ҫ��ֹ�ظ�
                  if (CS.DEBUG_CGCL) {
                     System.out.println("��" + kin1 + "������Ϊ" + zb[m1][n1][QSXB]);
                  }
                  if (qi == 1) {

                     huik[shoushu][8 + tkd] = kin1;
                     if (CS.DEBUG_CGCL) {
                        System.out.println("�鱻��ԣ����Ϊ��" + kin1);
                     }
                  }
                  DianNode1 dian;
                  byte ii = 0;
                  for (ii = 1; ii <= zikuai[kin1].qishu; ii++) {
                     dian = zikuai[kin1].qichuang;
                     m1 = dian.a;
                     n1 = dian.b;
                     if (zb[m1][n1][QKSYXB] == yuanqikuaisuoyin) {
                        break;
                     }
                     dian = dian.next;
                  }
                  if (ii > zikuai[kin1].qishu) {
                     zikuai[kin1].deleteqikuaihao(yuanqikuaisuoyin);
                     if (CS.DEBUG_CGCL) {
                        System.out.println("����һ���������顣" + yuanqikuaisuoyin);
                     }
                  }
                  if (CS.DEBUG_CGCL) {
                     System.out.println("����cgcl()����");

                  }
               }
            } //���ظ���
            //} //if kuai
         } // if==yiseks
      } //��ѭ������

      zb[a][b][QSXB] = 0; //��ֹ����ʱ������.
      if (shoushu % 2 == BLACK) {
         tiheizishu += jubutizishu;
      }
      else {
         tibaizishu += jubutizishu; //���ֲ����Ӽ���
      }

      if (CS.DEBUG_CGCL) {
         System.out.println("����հ�����");
      }
      for (i = 0; i < 4; i++) { //�ٴ���հ�����
         m1 = (byte) (a + szld[i][0]);
         n1 = (byte) (b + szld[i][1]);
         if (zb[m1][n1][ZTXB] == BLANK) { //2.1the breath of blank
            kongbaidianshu++; //�������

         }
      }

      if (tongsedianshu == 0) { //4.1 û��ͬɫ�ڵ㣿����
         if (CS.DEBUG_CGCL) {
            System.out.println("û��ͬɫ�ڵ�");
         }
         zb[a][b][QSXB] = kongbaidianshu;
         zikuai[zikuaishu].qishu = kongbaidianshu;
         if (kongbaidianshu == 1 && jubutizishu == 1) { //���ǽ�
            hui[shoushu][3] = zikuai[huik[shoushu][5]].zichuang.a; //��ٽ���
            hui[shoushu][4] = zikuai[huik[shoushu][5]].zichuang.b;
            //Ӧ���Ǳ���֮�ӳ�Ϊ���֡�

         } //

      }

      /* for (j = 1; j <= tongsedianshu; j++) {
         //hui[shoushu][20+j]=tsk[j-1];
         kkhb(zikuaishu, huik[shoushu][j]);
         //kkhb(zikuaishu,tsk[j-1]);//���ϲ�,����δ����.
       }*/
      jskq(zikuaishu);
      //������С�Ŀ飻
      if (zikuai[zikuaishu].zwyskshu > 1) {
         byte p = zikuai[zikuaishu].zwyskshu;
         HaoNode1 lint = zikuai[zikuaishu].zwyskhao;
         byte qis = 127;
         for (byte wi = 1; wi <= p; wi++) {
            if (zikuai[lint.hao].qishu < qis) {
               qis = zikuai[lint.hao].qishu;
            }
            lint = lint.next;
         }
         zikuai[zikuaishu].minqi = qis; //��С��������Χ���minqiҲӦ������
      }

      //11��22�գ���������������
      byte m2, n2, m3, n3, m4, n4, x, y;
      switch (yuanshizhijieqishu) {
         case 1: {
            if (CS.DEBUG_CGCL) {
               System.out.println("ֱ������Ϊ1��û���¿����ɡ�");
            }
            zikuai[zikuaishu].addqikuaihao(yuanqikuaisuoyin);
            break;
         }
         case 2: {
            if (CS.DEBUG_CGCL) {
               System.out.print("ֱ������Ϊ2��");

            }
            m1 = zijieqi[0][0];
            n1 = zijieqi[0][1];
            m2 = zijieqi[1][0];
            n2 = zijieqi[1][1];
            if (m1 == m2 || n1 == n2) {
               //�����㡣�����¿�
               if (CS.DEBUG_CGCL) {
                  System.out.print("����ͬ�ᡣ");

               }
               qikuaizishu = 0;
               qikuaishu++;
               qikuai[qikuaishu] = new QiKuai1();
               short ttt = chengqikuai(m1, n1);
               if (CS.DEBUG_CGCL) {
                  System.out.println("chengqikuai()=" + ttt + "yuanqikuaizishu" +
                                     yuanqikuaizishu);
               }
               if (ttt == yuanqikuaizishu) {
                  //meiyou �γ��¿顣Ϊ��ʹ���п鶼�ǵ�ǰ������ڵĿ�
                  //����ʹ�ɿ����ʹ�á�
                  if (CS.DEBUG_CGCL) {
                     System.out.println("����û���¿�����");

                  }
                  qikuai[yuanqikuaisuoyin] = qikuai[qikuaishu];
                  qikuai[qikuaishu] = null;
                  qikuaishu--; //��ʵ�ʴ��ڵĿ���һ�¡�
                  qkdsy(yuanqikuaisuoyin);
               }
               else {
                  qikuaizishu = 0;
                  qikuaishu++;
                  qikuai[qikuaishu] = new QiKuai1();
                  if (CS.DEBUG_CGCL) {
                     System.out.println("���¿����ɡ�" + qikuaishu);

                  }
                  chengqikuai(m2, n2);
                  qikuai[yuanqikuaisuoyin] = qikuai[qikuaishu];
                  qkdsy(yuanqikuaisuoyin); ; //ֻ�ǻָ������־
                  qikuai[qikuaishu] = null;
                  qikuaishu--; //��ʵ�ʴ��ڵĿ���һ�¡�

                  qkdsy(qikuaishu); //�¿�ҲҪ������

               }
            }
            else {
               if (CS.DEBUG_CGCL) {
                  System.out.print("���㲻ͬ�ᣬ");

               }
               x = (byte) (m1 + m2 - a);
               y = (byte) (n1 + n2 - b);
               if (zb[x][y][ZTXB] == BLANK) {
                  if (CS.DEBUG_CGCL) {
                     System.out.println("�Խǵ�Ϊ�գ�û���¿����ɡ�");
                  }
               }
               else if (jgzs(x, y) == 1) {
                  if (CS.DEBUG_CGCL) {
                     System.out.println("�Խǵ���Χ�գ�û���¿����ɡ�");
                  }
               }
               else {

                  qikuaizishu = 0;
                  qikuaishu++;
                  if (CS.DEBUG_CGCL) {
                     System.out.println("���������ɿ飺" + qikuaishu);

                  }
                  qikuai[qikuaishu] = new QiKuai1();

                  if (chengqikuai(m1, n1) == yuanqikuaizishu) {
                     //meiyou �γ��¿顣
                     if (CS.DEBUG_CGCL) {
                        System.out.println("û���¿����ɡ�");

                     }
                     qikuai[qikuaishu] = null;
                     qikuaishu--; //��ʵ�ʴ��ڵĿ���һ�¡�

                     qkdsy(yuanqikuaisuoyin); ; //ֻ�ǻָ������־

                  }
                  else {

                     qikuaizishu = 0;
                     qikuaishu++;
                     if (CS.DEBUG_CGCL) {
                        System.out.println("���������ɿ飺" + qikuaishu);
                     }
                     qikuai[qikuaishu] = new QiKuai1();
                     chengqikuai(m2, n2);
                     qikuai[yuanqikuaisuoyin] = qikuai[qikuaishu];
                     qikuai[qikuaishu] = null;
                     qikuaishu--; //��ʵ�ʴ��ڵĿ���һ�¡�
                     qkdsy(qikuaishu);
                     qkdsy(yuanqikuaisuoyin); //���µ����鶨Ϊ�ɵ�����

                  }

               }
            }
            break;
         }
         case 3: {
            byte lianjieshu = 0;
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
                  lianjieshu++;
               }
               else if (jgzs(x, y) == 1) {
                  lianjieshu++;
               }

            }
            if (m1 == m3 || n1 == n3) {

            }
            else {
               x = (byte) (m1 + m3 - a);
               y = (byte) (n1 + n3 - b);
               if (zb[x][y][ZTXB] == BLANK) {
                  lianjieshu++;

               }
               else if (jgzs(x, y) == 1) {
                  lianjieshu++;

               }

            }
            if (m2 == m3 || n2 == n3) {

            }
            else {
               x = (byte) (m2 + m3 - a);
               y = (byte) (n2 + n3 - b);
               if (zb[x][y][ZTXB] == BLANK) {
                  lianjieshu++;
               }
               else if (jgzs(x, y) == 1) {
                  lianjieshu++;
               }

            }
            if (lianjieshu >= 2) {
               if (CS.DEBUG_CGCL) {
                  System.out.println("û���¿����ɡ�");

               }
            }
            else {
               zishujishu = 0;

               for (byte bianli = 0; bianli < 3; bianli++) {
                  m1 = (byte) (zijieqi[bianli][0]);
                  n1 = (byte) (zijieqi[bianli][1]);
                  qikuaizishu = 0;
                  qikuaishu++;
                  qikuai[qikuaishu] = new QiKuai1();

                  zishujishu += chengqikuai(m1, n1);
                  if (zishujishu == yuanqikuaizishu) { //�Ѿ�ɨ�����
                     if (bianli == 0) {
                        //meiyou �γ��¿顣

                        qikuai[qikuaishu] = null;
                        qikuaishu--; //��ʵ�ʴ��ڵĿ���һ�¡�
                        qkdsy(yuanqikuaisuoyin);
                     }
                     else if (bianli == 1) {
                        qikuai[yuanqikuaisuoyin] = qikuai[qikuaishu];
                        qkdsy(yuanqikuaisuoyin);
                        qikuai[qikuaishu] = null;
                        qikuaishu--;
                        qkdsy(qikuaishu);
                     }
                     else {
                        qikuai[yuanqikuaisuoyin] = qikuai[qikuaishu];
                        qkdsy( (byte) (yuanqikuaisuoyin));
                        qkdsy( (byte) (qikuaishu - 2));

                        qikuai[qikuaishu] = null;
                        qikuaishu--;
                        qkdsy(qikuaishu);

                     }
                     break;

                  }
               }

            }
            break;
         }
         case 4: {
            byte lianjieshu = 0;
            for (byte bianli = 0; bianli < 4; bianli++) {
               m1 = (byte) (a + szdjd[bianli][0]);
               n1 = (byte) (b + szdjd[bianli][1]);
               if (zb[m1][n1][ZTXB] == BLANK) {
                  lianjieshu++;
                  //ͨ���Խǵ�����
               }
               else if (jgzs(m1, n1) == 1) {
                  lianjieshu++;
                  // ͨ���Ź�����
               }
            }

            if (lianjieshu >= 3) {
               if (CS.DEBUG_CGCL) {
                  System.out.println("ֱ������Ϊ4��������Ϊ3��û���¿����ɡ�");
               }
            }
            else {
               zishujishu = 0;

               for (byte bianli = 0; bianli < 4; bianli++) {
                  m1 = (byte) (zijieqi[bianli][0]);
                  n1 = (byte) (zijieqi[bianli][1]);
                  qikuaizishu = 0;
                  qikuaishu++;
                  qikuai[qikuaishu] = new QiKuai1();
                  zishujishu += chengqikuai(m1, n1);
                  if (zishujishu == yuanqikuaizishu) { //�Ѿ�ɨ�����
                     if (bianli == 0) {
                        //meiyou �γ��¿顣
                        //qikuai[yuanqikuaisuoyin] = qikuai[qikuaishu];
                        qikuai[qikuaishu] = null;
                        qikuaishu--; //��ʵ�ʴ��ڵĿ���һ�¡�
                        qkdsy(yuanqikuaisuoyin);

                     }
                     else if (bianli == 1) {
                        qikuai[yuanqikuaisuoyin] = qikuai[qikuaishu];
                        qkdsy(yuanqikuaisuoyin);
                        qikuai[qikuaishu] = null;
                        qikuaishu--;
                        qkdsy(qikuaishu);

                     }
                     else if (bianli == 2) {
                        qikuai[yuanqikuaisuoyin] = qikuai[qikuaishu];
                        qkdsy( (byte) (yuanqikuaisuoyin));
                        qkdsy( (byte) (qikuaishu - 2));

                        qikuai[qikuaishu] = null;
                        qikuaishu--;
                        qkdsy(qikuaishu);

                     }
                     else {
                        qikuai[yuanqikuaisuoyin] = qikuai[qikuaishu];
                        qkdsy( (byte) (yuanqikuaisuoyin));
                        qkdsy( (byte) (qikuaishu - 3));
                        qkdsy( (byte) (qikuaishu - 2));

                        qikuai[qikuaishu] = null;
                        qikuaishu--;
                        qkdsy(qikuaishu);
                     }

                     break;
                  }
               }

            }
            break;
         }
      }

      //�µ�����ԭ����������ڳ̶ȸı�
      //output();
   }

   public byte jgzs(byte m, byte n) { //�Ź�������
      //m,nΪ�Ź����ĵ㡣�����ĵ㲻���룩
      byte dang = 0; //��������
      byte i, a, b; //����ָ�ʱ����ɢ�����ɵ�����������㣻
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

   /* public void clhuiqi() { //�Ƿ��������ݽṹ���ָܻ�?
      byte p = 0;
      byte yise = 0;
      byte tongse = 0; //yise is diff color.and 2 same.
      byte tdzs = 0;
      byte k0 = 0, yisedianshu = 0, kongbaidianshu = 0, tongsedianshu = 0, i = 0, j = 0; //the count for three kinds of point.
      byte ks = 0, kss = 0; //ks is count for block,kss for single point
      byte kin, kin1 = 0, m = 0, n = 0; //the block index.
      if(CS.DEBUG_CGCL) System.out.println("����:�������(clhuiqi)\n");
      tongse = (byte) ( (shoushu + 1) % 2 + 1); //tong se
      yise = (byte) (shoushu % 2 + 1);
      m = hui[shoushu][25];
      hui[shoushu][25] = 0;
      n = hui[shoushu][26];
      hui[shoushu][26] = 0;
      if (m <= 0 || n <= 0) { //��Ȩ�Ļָ�
        shoushu--;
        return; //
      }
      zzq(m, n, yise); //����,�Է�����,����ֱ�ӻָ�,�����ڴ�����
      if(CS.DEBUG_CGCL) System.out.println("����:" + shoushu);
      if(CS.DEBUG_CGCL) System.out.println("a=" + m + ",b=" + n);
      kin = hui[shoushu][0];
      if (kin != 0) { //kin���³ɵĿ�
        zikuai[kin].color = 0;
        zikuai[kin].qishu = 0;
        zikuai[kin].zishu = 0;
        zikuai[kin].zichuang = null;
        zikuai[kin].qichuang = null;
        zikuaishu = kin; //ȫ�ֿ��ÿ��?
        zikuaishu--; //xinzeng.
        for (i = 1; i <= 4; i++) {
          if (hui[shoushu][2 * i + 12 - 1] <= 0) { //���¿�ĵ�
            break;
          }
          else {
            m = hui[shoushu][12 + 2 * i - 1]; //13-20
            n = hui[shoushu][12 + 2 * i];
            hui[shoushu][12 + 2 * i - 1] = 0;
            hui[shoushu][12 + 2 * i] = 0;
            zb[m][n][QKSYXB] = 0;
            zb[m][n][ZTXB] = tongse; //fang wei bian cheng
            zb[m][n][QSXB] = jszq(m, n); //�����ӵ���
       if(CS.DEBUG_CGCL) System.out.println("//����ɿ�����:" + "a=" + m + ",b" + n);
          }
        } //deal with 3 sub
        for (i = 1; i <= 4; i++) { //�Ƿ�ɿ���¿�
          kin1 = hui[shoushu][20 + i]; //21-24
          hui[shoushu][20 + i] = 0;
          if (kin1 == 0) {
            break;
          }
          else {
            p = zikuai[kin1 + 128].zishu;
            DianNode1 temp = zikuai[kin1 + 128].zichuang;
            for (j = 1; j <= p; j++) { //���ѭ���ؼ�����������Ļ�����
              m = temp.a;
              n = temp.b;
              zb[m][n][3] = kin1; //�޸Ŀ��
              //zb[m][n][0]=tongse;
              zb[m][n][2] = zikuai[kin1 + 128].qishu; //�ָ�ԭ��ɿ�ʱ����
              temp = temp.next;
            }
            //jskq(kin1); //cunchukuaiqi;
          } //else
        } //for
      } //if �Ƿ��¿�
      for (i = 1; i <= 4; i++) { //�Ƿ�����
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
          if(CS.DEBUG_CGCL) System.out.print("�ָ�������:");
          if(CS.DEBUG_CGCL) System.out.println("a=" + m + ",b=" + n);
        }
      } //for
      for (i = 1; i <= 4; i++) { //�Ƿ��б���Ŀ�
        if (hui[shoushu][8 + i] == 0) {
          break;
        }
        else {
          kin1 = hui[shoushu][8 + i];
          hui[shoushu][8 + i] = 0;
          kdq(kin1, (byte) 1);
          kjq(kin1, tongse);
          p = zikuai[kin1 + 128].zishu;
          DianNode1 temp = zikuai[kin1 + 128].zichuang; //shiwubiyao
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
        tiheizishu -= tdzs;
      }
      if (tongse == WHITE) {
        tibaizishu -= tdzs;
      }
      for (i = 0; i < 9; i++) {
        hui[shoushu][27 + i] = 0; //2yue
      }
      shoushu--;
      if(CS.DEBUG_CGCL) System.out.println("����clhuiqi:�������\n");
    } //clhuiqi*/

   public byte jszq(byte m, byte n) { //huiqishiyong.
      byte dang = 0; //��������
      byte i, a, b; //����ָ�ʱ����ɢ�����ɵ�����������㣻
      for (i = 0; i < 4; i++) {
         a = (byte) (m + szld[i][0]);
         b = (byte) (n + szld[i][1]);
         if (zb[a][b][ZTXB] == BLANK) { //2.1the breath of blank
            dang++;
         }
      }
      return dang;
   }

   public byte bijiaoqishu(
      short k1, short k2, byte colorxian) {
      //��ͬһ���ĽǶȳ������Ⱥ��ֲ�һ����
      //�� K1���ĽǶ��жϣ�
      byte m1 = zikuai[k1].qishu;
      byte m2 = zikuai[k2].qishu;
      byte cha = (byte) (m1 - m2);
      if (zikuai[k1].color == colorxian) {
         cha += 1;
      }
      //����ֵ<=0;���Ƕ�ɱʧ�ܣ�
      //һ������Ϊ1����ɱ��ʤ������֣�����һ�����þ���0����ɱʧ�ܣ�Ҳ��
      //�������ӣ��������ã�
      //һ������Ϊ2������������Ȼ��ʤ���������Ⱥ�Է����������ã�
      //����һ�����þ��ǣ�1���Է�������Ȼʧ�ܣ�
      //һ������Ϊ3�����������Կɻ�ʤ�������Ⱥ�Է�û�����á�
      //��һ������Ϊ��2���Է���ʹ���ȱ���Ҳû����������
      //ת����ϵΪ��>0:�ȼ�һ����ȡ����
      //�����ǵȼ۵ģ�ֻ�ǴӲ�ͬ����������
      //���ڽ����.
      return cha;

   }

   public byte bijiaoqishu2( //�����ȽϵĽ�����������в�����ͬ
      //����˫���������ڷ��š�
      short k1, short k2, byte colorxian) {
      //��k1�ĽǶȳ�����
      //��ͬһ���ĽǶȳ������Ⱥ��ֲ�2��������
      //�� K1���ĽǶ��жϣ�
      byte m1 = zikuai[k1].qishu;
      byte m2 = zikuai[k2].qishu;
      byte cha = (byte) (2 * (m1 - m2));
      if (zikuai[k1].color == colorxian) {
         cha += 1;
      }
      else if (zikuai[k2].color == colorxian) {
         cha -= 1;
      }
      //����ֵ<=0;���Ƕ�ɱʧ�ܣ�
      //һ������Ϊ1����ɱ��ʤ������֣�����һ�����þ���-1����ɱʧ�ܣ�Ҳ��
      //�������ӣ��������ã�
      //һ������Ϊ3������������Ȼ��ʤ���������Ⱥ�Է����������ã�
      //����һ�����þ��ǣ�1���Է�������Ȼʧ�ܣ�
      //һ������Ϊ5�����������Կɻ�ʤ�������Ⱥ�Է�û�����á�
      //��һ������Ϊ��5���Է���ʹ���ȱ���Ҳû����������
      //ת����ϵΪ��ȡ����
      //2004��2��16��
      return cha;

   }

   public byte bijiaoqishu3(
      short k1, short k2, byte gongqi, byte colorxian) {
      //�������Ķ�ɱ
      //�����ȽϵĽ�����������в�����ͬ
      //����˫���������ڷ��š�

      //��k1�ĽǶȳ�����
      //��ͬһ���ĽǶȳ������Ⱥ��ֲ�2��������
      //�� K1���ĽǶ��жϣ�
      byte m1 = zikuai[k1].qishu;
      byte m2 = zikuai[k2].qishu;
      byte cha = (byte) (2 * (m1 - m2));
      if (zikuai[k1].color == colorxian) {
         cha += 1;
      }
      else if (zikuai[k2].color == colorxian) {
         cha -= 1;
      }
      cha -= (2 * (gongqi - 1));
      //����ֵ<=0;���Ƕ�ɱʧ�ܣ�
      //һ������Ϊ1����ɱ��ʤ������֣�����һ�����þ���-1����ɱʧ�ܣ�Ҳ��
      //�������ӣ��������ã�
      //һ������Ϊ3������������Ȼ��ʤ���������Ⱥ�Է����������ã�
      //����һ�����þ��ǣ�1���Է�������Ȼʧ�ܣ�
      //һ������Ϊ5�����������Կɻ�ʤ�������Ⱥ�Է�û�����á�
      //��һ������Ϊ��5���Է���ʹ���ȱ���Ҳû����������
      //ת����ϵΪ��ȡ����
      //2004��2��16��
      return cha;

   }

   public byte xingshipanduan(byte colorjiaodu) {
      //���õ���������ʱ���ã��Է����Ȼ��������ӡ�
      //����ȷ��������õĵص������ӵľֲ�
      //ͬʱҪ���ǶԱ𴦵�Ӱ�죬һ��������ֲ���Ӱ��
      //�漰���Ӳſ���Զ����Ӱ�졣
      //�еĶ�ɱ������ʾ����Ҫʵ�ʶ�ɱ����������2���Ҷ�����û��
      //changqidian.
      byte lingxian = 0;
      return lingxian;
   }

   /**
    * refacted and move to StateAnalysis
    */
   public void shengchengjumian() {
      //�����׵�λͼ��ʾ����kuai��zb�������Ӧ��Ϣ
      //����ʽ�������ɾ�����Ŀ�������ģ���Ҫ�޸ġ�
      //�ú���Ӧ���ڳ�ʼ����ʱ���á�

      byte i, j;
      byte m, m1, n1, n;
      byte qkxlhzs = 0; //�������ں�����
      byte qkxlbzs = 0;
      byte color = 0;
      byte othercolor = 0;

      //��һ��ɨ�裬���ɿ飬�������������Ӵ���
      for (i = 1; i < 20; i++) { //i��������
         for (j = 1; j < 20; j++) { //j�Ǻ�����,���д���
            if (zb[j][i][SQBZXB] == 1) {
               continue; //SQBZXB�˴��൱�ڴ�����ı�־.
            }
            //�õ���δ����
            qikuaizishu = 0; // ÿ���ӵļ���,������������
            meikuaizishu = 0; // ÿ���ӵļ��������������ӿ�
            if (zb[j][i][ZTXB] == BLACK) { //��.�ϱ�Ϊ�յ����ɫ��
               zikuaishu++; //��ŵ���
               zikuai[zikuaishu] = new ZiKuai1();
               zikuai[zikuaishu].color = BLACK;
               chengkuai(j, i, BLACK); //�ж���.���Ƿ�Ϊͬɫ��.
               zikuai[zikuaishu].zishu = meikuaizishu;
               //if(CS.DEBUG_CGCL) System.out.print("zishucuowu:"+zikuaishu);
            }
            else if (zb[j][i][ZTXB] == WHITE) { //��.�ϱ�Ϊ�յ����ɫ��
               zikuaishu++;
               zikuai[zikuaishu] = new ZiKuai1();
               zikuai[zikuaishu].color = WHITE;
               chengkuai(j, i, WHITE); //�ж���.���Ƿ�Ϊͬɫ��
               zikuai[zikuaishu].zishu = meikuaizishu;
               // if(CS.DEBUG_CGCL) System.out.print("zishucuowu:"+zikuaishu);
            }
            else if (zb[j][i][ZTXB] == BLANK) {
               qikuaishu++;
               qikuai[qikuaishu] = new QiKuai1();
               chengqikuai(j, i);
               qikuai[qikuaishu].zishu = qikuaizishu;
               //if(CS.DEBUG_CGCL) System.out.print("zishucuowu:"+qikuaishu) ;
            }
            qikuaizishu = 0;
         }
      } //���ɿ�,������顣

      for (i = 1; i < 20; i++) { //i��������
         for (j = 1; j < 20; j++) { //j�Ǻ�����
            zb[j][i][SQBZXB] = 0; //�ָ�ÿ�����������־
            //if(zb[j][i][ZTXB]>0 && zb[j][i][KSYXB]==0){
            //zb[j][i][QSXB]=jszq(j,i);
            //}
         }
      }

      //�������������������
      for (i = 1; i <= zikuaishu; i++) {
         //byte qi=jskq(ki);�������������ֱ�Ӵ�������.
         jskq(i);
      } //�������

      for (i = 1; i <= qikuaishu; i++) { //���������֣��ҵ�ǿ�顣
         short meikuaizishu = qikuai[i].zishu;
         if (meikuaizishu == 1) { //��λ
            //���ܱ�Ȼ���ӡ�
            m = qikuai[qikuaishu].zichuang.a;
            n = qikuai[qikuaishu].zichuang.b;
            for (byte k = 0; k < 4; k++) {
               m1 = (byte) (m + szld[k][0]);
               n1 = (byte) (n + szld[k][1]);

               if (zb[m1][n1][ZTXB] == WHITE) {
                  qkxlbzs++;
               }
               else if (zb[m1][n1][ZTXB] == BLACK) {
                  qkxlhzs++;
               }
            }
            if (qkxlbzs == 0) { //�ڷ�����
               qikuai[i].color = CS.BLACK;
               if (qikuaizuixiaoqi(i) > 1) {
                  //���ɵ��۲����������ƻ���
                  erjikuaishu++;
                  erjikuai[erjikuaishu] = new ErJiKuai();
                  for (byte k = 0; k < 4; k++) {
                     m1 = (byte) (m + szld[k][0]);
                     n1 = (byte) (n + szld[k][1]);
                     if (zb[m1][n1][ZTXB] == BLACK) {
                        erjikuai[erjikuaishu].addkuaihao(zbk[m1][n1]);
                     }

                  }

               }
               else {
                  //���������鱾���ڱ����״̬��
               }

            }
            else if (qkxlhzs == 0) { //�׷����ۣ�����һ�塣
               qikuai[i].color = CS.WHITE;
               if (qikuaizuixiaoqi(i) > 1) {
                  erjikuaishu++;
                  erjikuai[erjikuaishu] = new ErJiKuai();
                  for (byte k = 0; k < 4; k++) {
                     m1 = (byte) (m + szld[k][0]);
                     n1 = (byte) (n + szld[k][1]);
                     if (zb[m1][n1][ZTXB] == BLACK) {
                        erjikuai[erjikuaishu].addkuaihao(zbk[m1][n1]);
                     }

                  }

               }
               else {
                  //todo
               }

            }
            else { //˫���Ĺ�����
               qikuai[i].color = 5;
            }
            //qikuai[qikuaishu--].zichuang=null;
            //qikuai[qikuaishu].qichuang
            //qikuai[ki--][1][1]=0;
            //zb[j][i][KSYXB]=0;//�ǿ�
            //todo:��λ�Ĵ���
            //�ҳ���Χ�飬����Ϊһ�ҵ��ӣ����١�����Ϊһ����
            //��˫�ᣬ��Ϊһ�������໹һ����
         }
         else if (meikuaizishu > 1) { //����
            // qikuai[qikuaishu].zishu = meikuaizishu;
            DianNode1 tee = qikuai[i].zichuang;
            for (short hh = 1; hh <= meikuaizishu; hh++) {
               m = tee.a;
               n = tee.b;
               for (byte k = 0; k < 4; k++) {
                  m1 = (byte) (m + szld[k][0]);
                  n1 = (byte) (n + szld[k][1]);

                  if (zb[m1][n1][ZTXB] == WHITE) {
                     qkxlbzs++;
                  }
                  else if (zb[m1][n1][ZTXB] == BLACK) {
                     qkxlhzs++;
                  }
               }
               tee = tee.next;
            }
            if (qkxlbzs == 0) { //�ڷ��Ĵ���
               //��Χ�Ŀ�����Խ��Խ�á�
               if (qikuaizuixiaoqi(i) > 1) {
                  erjikuaishu++;
                  erjikuai[erjikuaishu] = new ErJiKuai();

                  for (short hh = 1; hh <= meikuaizishu; hh++) {
                     m = tee.a;
                     n = tee.b;

                     for (byte k = 0; k < 4; k++) {
                        m1 = (byte) (m + szld[k][0]);
                        n1 = (byte) (n + szld[k][1]);
                        if (zb[m1][n1][ZTXB] == BLACK) {
                           qikuai[i].addzikuaihao(zbk[m1][n1]);
                           erjikuai[erjikuaishu].addkuaihao(zbk[m1][n1]);
                        }

                     }
                  }
               }
            }
            else if (qkxlhzs == 0) { //�׷��Ĵ��ۣ�����һ�塣
               if (qikuaizuixiaoqi(i) > 1) {
                  erjikuaishu++;
                  erjikuai[erjikuaishu] = new ErJiKuai();

                  for (short hh = 1; hh <= meikuaizishu; hh++) {
                     m = tee.a;
                     n = tee.b;

                     for (byte k = 0; k < 4; k++) {
                        m1 = (byte) (m + szld[k][0]);
                        n1 = (byte) (n + szld[k][1]);
                        if (zb[m1][n1][ZTXB] == WHITE) {
                           qikuai[i].addzikuaihao(zbk[m1][n1]);
                           erjikuai[erjikuaishu].addkuaihao(zbk[m1][n1]);
                        }

                     }
                  }
               }

            }
            else { //˫���Ĺ�����

            }

         }
         else {
            if (CS.DEBUG_CGCL) {
               System.out.println("error:zishu<1");

            }
         }

      }

      /* byte zijishu = 0;
       byte kuaijishu = 0;
       for (i = 1; i <= dandianshu; i++) {
         zijishu = 0;
         kuaijishu = 0;
         m = dandian[i][0];
         n = dandian[i][1];
         color = zb[m][n][ZTXB];
         if (color == 1)
           othercolor = 2;
         else if (color == 2)
           othercolor = 1;
         for (j = 0; j < 4; j++) {
           m = (byte) (dandian[i][0] + szld[j][0]);
           n = (byte) (dandian[i][1] + szld[j][1]);
           if (zb[m][n][ZTXB] == othercolor) {
             if (zb[m][n][KSYXB] == 0) {
               zb2[m][n][2 * zijishu] = m;
               zb2[m][n][2 * zijishu + 1] = n;
               zijishu++;
             }
             else{//ע���ֹ����ظ���
             }
           }
         }//for
       }*/
      byte zijishu = 0;
      short kzishu = 0; //�麬�е�����������ѭ��
      DianNode1 temp;
      HaoNode1 linhao;
      short zikin1;
      byte zikin2;
      for (i = 1; i <= zikuaishu; i++) {
         color = zikuai[i].color;
         if (color == CS.BLACK) {
            othercolor = CS.WHITE;
         }
         else if (color == CS.WHITE) {
            othercolor = CS.BLACK;
            //�����ÿ�ĸ����ӣ��õ���Χ��ɫ�飬�����ֹ�ظ���
         }
         kzishu = zikuai[i].zishu;

         for (temp = zikuai[i].zichuang; temp != null; temp = temp.next) {
            for (j = 0; j < 4; j++) {

               m = (byte) (temp.a + szld[j][0]);
               n = (byte) (temp.b + szld[j][1]);
               if (zb[m][n][ZTXB] == othercolor) {

                  //else if(zb[m][n][SQBZXB]==0){//ע���ֹ����ظ���
                  zikin1 = zbk[m][n];
                  zikuai[i].addyisekuaihao(zikin1);

               }
               else if (zb[m][n][ZTXB] == CS.BLANK) {

                  //else if(zb[m][n][SQBZXB]==0){//ע���ֹ����ظ���
                  zikin2 = zb[m][n][QKSYXB];
                  zikuai[i].addqikuaihao(zikin2);

               }

            } //for

         }

      }
   }

   public void chengkuai1(byte a, byte b) { //������
      qikuaishu++;
      qikuai[qikuaishu] = new QiKuai1();
      qikuaizishu = 0;
      if (chengqikuai2(a, b) == false) {
         qikuai[qikuaishu] = null;
         qikuaishu--;
      }
      //qikuaishu��SQBZXBӦ������
   }

   short meikuaizishu;
   public void chengkuai(byte a, byte b, byte color) {
      //�ռ���Ϣ�Ĺ�����,������color=BLANK,���øú���,�����������Ϣ
      //����פ����kuai������,���������ò����.
      //byte max=127;
      byte m1, n1;
      byte othercolor = 0;
      DianNode1 temp = new DianNode1();

      if (color == 1) {
         othercolor = 2;
      }
      else if (color == 2) {
         othercolor = 1;
      }
      temp.a = a;
      temp.b = b;
      temp.next = zikuai[zikuaishu].zichuang; //����Ѿ�Ԥ��
      zikuai[zikuaishu].zichuang = temp;
      zb[a][b][SQBZXB] = 1;
      zbk[a][b] = zikuaishu;
      meikuaizishu++; //��Ϊ���ڶ���Ҳ��飬����ȡ����
      for (byte k = 0; k < 4; k++) {
         m1 = (byte) (a + szld[k][0]);
         n1 = (byte) (b + szld[k][1]);
         if (zb[m1][n1][SQBZXB] == 0 && zb[m1][n1][ZTXB] == color) {
            chengkuai(m1, n1, color);
         }
      }
   } //�ɿ�ĵ�SQBZXB==1;

   public short chengqikuai(byte a, byte b) {
      //��Ϊ�����жϵ���ʱ����ÿ�ε��ö������־������
      //�������������־��
      byte m1, n1;

      qikuaizishu++;
      //if(CS.DEBUG_CGCL) System.out.println("�����Ϊ��" + qikuaishu);
      qikuai[qikuaishu].addzidian(a, b); //?
      if (CS.DEBUG_CGCL) {
         System.out.println("�������ӵ㣺" + a + "/" + b);
      }
      zb[a][b][SQBZXB] = 1;
      zb[a][b][QKSYXB] = qikuaishu;

      for (byte k = 0; k < 4; k++) {
         m1 = (byte) (a + szld[k][0]);
         n1 = (byte) (b + szld[k][1]);
         if (zb[m1][n1][SQBZXB] != 0) {
            continue;
         }

         if (zb[m1][n1][ZTXB] == BLANK) {
            chengqikuai(m1, n1);

         }
         else if (zb[m1][n1][ZTXB] == BLACK || zb[m1][n1][ZTXB] == WHITE) {
            qikuai[qikuaishu].addzikuaihao(zbk[m1][n1]);
            //���������֮���ٽ�������Ϊ����ſ��ܻ�䡣
         }
      }
      return qikuaizishu;
   } //�ɿ�ĵ�SQBZXB==1;

   public boolean chengqikuai2(byte a, byte b) {
      //�������ͷ���
      //�ռ���Ϣ�Ĺ�����,������color=BLANK,���øú���,�����������Ϣ
      //����פ����kuai������,���������ò����.
      //���øú���ǰ���뽫qkzishu����
      byte m1, n1;
      //byte zishu=0;
      if (qikuaizishu < 10) {
         qikuai[qikuaishu].addzidian(a, b);
         qikuaizishu++;
      }
      else {
         qikuaizishu++;
         if (CS.DEBUG_CGCL) {
            System.out.println("�ÿ����������10,���Ϊ:" + zikuaishu);
         }
         return false;
      }
      zb[a][b][SQBZXB] = 1;
      zb[a][b][QKSYXB] = qikuaishu;
      //zishu++;
      for (byte k = 0; k < 4; k++) {
         m1 = (byte) (a + szld[k][0]);
         n1 = (byte) (b + szld[k][1]);
         if (zb[m1][n1][SQBZXB] == 0 & zb[m1][n1][ZTXB] == BLANK) {
            if (chengqikuai2(m1, n1) == false) {
               return false;
            }
         }
      }
      return true;
   }

   public void qiquan() {
      shoushu++;
      hui[shoushu][1] = 0;
      hui[shoushu][2] = 0;

   }

   public byte qikuaiyanse(byte kin) {
      //�ж��������ɫ������ʹ���ڵ����ָ������顣
      byte yanse = 0;
      byte i, j;
      byte a, b;
      byte m1, n1;
      short zishu = qikuai[kin].zishu;
      DianNode1 temp = qikuai[kin].zichuang;
      if (zishu > 20) {
         if (CS.DEBUG_CGCL) {
            System.out.println("����̫�󣬲������ǵ�ɫ���顣");
         }
         return 5;
      }
      for (j = 1; j <= zishu; j++) {
         a = temp.a;
         b = temp.b;
         for (i = 0; i < 4; i++) { //ֱ�ӵ����������������ɵ�����

            m1 = (byte) (a + szld[i][0]);
            n1 = (byte) (b + szld[i][1]);
            if (zb[m1][n1][ZTXB] == BLACK) { //2.1the breath of blank
               //if(CS.DEBUG_CGCL) System.out.print("��¼ֱ������");
               if (yanse == 0) {
                  yanse = BLACK;
               }
               else if (yanse == WHITE) {
                  return 5; //��ɫ��
               }

            }
            else if (zb[m1][n1][ZTXB] == WHITE) { //2.1the breath of blank
               if (yanse == 0) {
                  yanse = WHITE;
               }
               else if (yanse == BLACK) {
                  return 5; //��ɫ��
               }

            }
         }
      }
      if (yanse == BLACK || yanse == WHITE) {

      }
      return yanse;
   }

   public byte yisekuaizuixiaoqi(short kin) {
      //���������Χ��ɫ���ӿ����С����
      byte minqi = 127;
      HaoNode1 linshi = zikuai[kin].zwyskhao;
      byte i = 1;
      byte qishu = 0;
      byte zwyskshu = zikuai[kin].zwyskshu;
      for (; i <= zwyskshu; i++) {
         if (linshi == null) {
            if (CS.DEBUG_CGCL) {
               System.out.print("�ÿ����Χ��ɫ�����������" + kin);
            }
            break;
         }
         qishu = zikuai[linshi.hao].qishu;
         if (qishu < minqi) {
            minqi = qishu;
         }
         linshi = linshi.next;
      }
      zikuai[kin].minqi = minqi;

      return minqi;
   }

   public byte qikuaizuixiaoqi(byte kin) {
      //����������Χ���ӿ����С����
      byte minqi = 127;
      HaoNode1 linshi = qikuai[kin].zwzkhao;
      byte i = 1;
      byte qishu = 0;
      byte zwzkshu = qikuai[kin].zwzkshu;
      for (; i <= zwzkshu; i++) {
         if (linshi == null) {
            if (CS.DEBUG_CGCL) {
               System.out.print("�ÿ����Χ�����������" + kin);
            }
            break;
         }
         qishu = zikuai[linshi.hao].qishu;
         if (qishu < minqi) {
            minqi = qishu;
         }
         linshi = linshi.next;
      }
      qikuai[kin].minqi = minqi;

      return minqi;
   }

   //������Ƶ�ĸ�����绢��֮�ࡣ����ǿ�Ŀ��Ƶ㡣���Ƶ�������ʶ�����͡�
   public byte lianjiexing(short kin1, short kin2) {

      return CS.WEIDINGLIANJIE;

   }

   public void zairujumian(DataInputStream jmin) throws IOException {

      byte a, b, color;
      byte i, j;
      for (i = 1; i < 20; i++) {
         for (j = 1; j < 20; j++) {
            zb[i][j][QKSYXB] = 0;
            qikuai[qikuaishu].deletezidian(i, j);
         }
      }
      qikuai[qikuaishu] = null;
      qikuaishu--;
      while (jmin.available() != 0) {
         a = jmin.readByte();
         b = jmin.readByte();
         color = jmin.readByte();
         zb[a][b][ZTXB] = color;
         if (a < 1 | a > 19 | b < 1 | b > 19 | color < 1 | color > 2) {
            if (CS.DEBUG_CGCL) {
               System.out.print("�������������" + a);
            }
            if (CS.DEBUG_CGCL) {
               System.out.print("i=" + a);
            }
            if (CS.DEBUG_CGCL) {
               System.out.print("j=" + b);
            }
            if (CS.DEBUG_CGCL) {
               System.out.println("color=" + color);
            }
         }

      }

      if (CS.DEBUG_CGCL) {
         System.out.print("�������");

      }
   }

   public boolean shifoukongzhidian(byte a, byte b) {
      //���÷���֤�õ�հס�
      if (jszq(a, b) == 1) {

      }
      else {

      }
      return true;
   }

   public Object clone() {
      BoardLianShort temp = null;
      byte i, j, k;
      short t;
      try {
         temp = (BoardLianShort) (super.clone());
      }
      catch (CloneNotSupportedException de) {
         de.printStackTrace();
      }

      for (i = 1; i <= qikuaishu; i++) {
         //if(CS.DEBUG_CGCL) System.out.println("qikuai="+i);
         if (qikuai[i] != null) {
            temp.qikuai[i] = (QiKuai1) qikuai[i].clone();
         }
      }
      for (t = 1; t <= zikuaishu; t++) {
         //if(CS.DEBUG_CGCL) System.out.println("zikuai="+t);
         if (temp.zikuai[t] != null) {
            temp.zikuai[t] = (ZiKuai1) zikuai[t].clone();
         }

      }
      return temp;

   }

   //���������۵��ʶ�𡣱������������飬������������㡣
   //���ǻ�Ҫ�漰�����������١�

   /*public void duishaxingjubu(ErJiKuai erjikuai){
     //��ɱ�;ֲ�����ʶ��
     //������������,erji
     //ȷ��ÿһ������ʡ����ܷ񳤣�
     byte []zikuai =new byte[];
     for(byte i=0;i<erjikuai.){
     }
      }*/
   public void shuchujumian(DataOutputStream jmout) throws IOException {
      byte i, j;
      for (i = 1; i < 20; i++) {
         for (j = 1; j < 20; j++) {
            if (zb[i][j][ZTXB] != 0) {
               jmout.writeByte( (byte) i);
               jmout.writeByte( (byte) j);
               jmout.writeByte( (byte) zb[i][j][ZTXB]);
               if (CS.DEBUG_CGCL) {
                  System.out.print("i=" + i);
               }
               if (CS.DEBUG_CGCL) {
                  System.out.println("j=" + j);
               }
            }
         }
      }
      jmout.flush();
      if (CS.DEBUG_CGCL) {
         System.out.print("�������");

      }
   }
   public byte chaiershu(byte a,byte b){//�����
      byte ershu=0;
      return ershu;
   }

   /**
	 * transfer into bidimension array
	 * 
	 * @return
	 */
	public byte[][] getStateArray() {
		byte[][] a = new byte[21][21];
		byte i, j, k;
		for (i = 0; i < 21; i++) {
			for (j = 0; j < 21; j++) {
				a[i][j] = zb[j][i][ZTXB];

			}
		}
		return a;
	}
}