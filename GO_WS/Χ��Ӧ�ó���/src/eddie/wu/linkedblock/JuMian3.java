package eddie.wu.linkedblock;

//import untitled4.*;
//import untitled9.*;

/**
 * <p>Title: ���̾���Ļ�����Ϣ��������zb�У������BoardLianShort
 * ת��������ר�Ŵ���Ӿ�̬�������ɸ߼���Ϣ��</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class JuMian3 {

  public static final boolean DEBUG = true;


  public byte[][][] zb = new byte[21][21][4];
  //ÿ�����ӵ�����������ԭʼ��Ϣ������������С����Դ���һ�����档
  //ǰ��ά�����̵�����,�����±��1��19;
  //����ά�±�0:����״̬:��1��2;�����Դ���һ�����棩
  //����ά�±�1:�������ı�־;
  //����ά�±�2:����;
  //����ά�±�3:�հ׵���������ı�š�
  //����ά���������ڴ����������������顢���ĸ��ֻ��ʵ����Щ
  //�����������ʵ�ֹ����Զ��������Ӻ�Ĵ�����̡�


  public short[][] zbk = new short[21][21];//ÿ�������ڵĿ�š�
  //������ӿ�ֿ����������ӿ�ĸ�����
  public byte turn;//��˭�ߡ�

  public short shoushu = 0; //��ǰ��������,����֮ǰ����.��1��ʼ;
  //���ӳɿ飬��ki��shoushu�ȼۣ����ǵ���Ȩ������΢���𣩡�

  public byte ktb = 0, ktw = 0; //�ڰױ����Ӽ���
  //���岻�󣬲���˳��������������жϻ������á�

  //����֮һ�������������ܳ���512�֡�
  public short[][] huik = new short[512][12]; //�ɿ�Ŀ��1��4
  //��ԵĿ��5��8����ԵĿ�ţ�9��11��
  //��ͬ���ʵ���ֱ��������һ����ǿ飬�����кڿ�Ͱ׿飬�Լ����顣
  public byte[][] hui = new byte[512][5];
  //��¼��ֵĹ�����Ϣ,���ڻ���;3-4���ŵ㣬1��2�ò��������.
  public ZiKuai1[] kuai = new ZiKuai1[512];
  public QiKuai1[] qikuai = new QiKuai1[128];
  public short ki = 0; //��ǰ�Ѿ��õ��Ŀ��,��ǰ����;
  public byte qiki = 0;
  byte qkzishu = 0;

  public JuMian3(byte [][][] tzb) {
    zb=tzb;//Ӧ��ԭ�ȵ����顣
    //���Ԥ�ȵ�����û�����Ӻ�������Ϣ������ܸı�ԭ���顣
  }

  public byte jskq(short rs) {
    System.out.println("���뷽�������������jskq��");
    //��������������ɿ��������Ϣ��������������
    byte qishu = 0; //����������Ӵ��Ѿ�ȷ����
    byte a = 0, b = 0;
    byte m, n;
    byte i, j;
    short zishu = kuai[rs].zishu; //�������
    DianNode1 temp = kuai[rs].zichuang;
    DianNode1 qich;
    System.out.println("kuaihao:" + rs);
    System.out.println("zishu:" + zishu);
    for (i = 1; i <= zishu; i++) {
      m = temp.a;
      n = temp.b;
      zbk[m][n] = rs; //���е����ȷ����

      for (j = 0; j < 4; j++) {
        a = (byte) (m + CS.szld[j][0]);
        b = (byte) (n + CS.szld[j][1]);
        System.out.println("a=" + a);
        System.out.println("b=" + b);
        if (zb[a][b][CS.ZTXB] == CS.BLANK) {
          System.out.println(" kongdian");
        }
        if (zb[a][b][CS.SQBZXB] == 0) {
          System.out.println(" weijisuanguo");
        }
        if (zb[a][b][CS.ZTXB] == CS.BLANK && zb[a][b][CS.SQBZXB] == 0) {
          qishu++;
          System.out.println(" qishu++;");
          zb[a][b][CS.SQBZXB] = 1;
          //���������������
          qich = new DianNode1(a, b);
          qich.next = kuai[rs].qichuang;
          kuai[rs].qichuang = qich;
        }
      }
      temp = temp.next;
    } //for
    kuai[rs].qishu = qishu;

    qich = kuai[rs].qichuang;
    for (i = 1; i <= qishu; i++) { //�ָ���־
      a = qich.a;
      b = qich.b;
      zb[a][b][CS.SQBZXB] = 0;
      zb[a][b][CS.QSXB] = qishu;
      //zbk[a][b] = rs;
      qich = qich.next;
    }
    System.out.println("�������Ϊ��" + qishu);
    kdq(rs, qishu);
    System.out.println("����jskq������");
    return qishu;
  } //2��22�ո�,ԭ��������,��ֻ����ʹ�.

  public void kdq(short kin, byte a) { //�鶨��
    System.out.println("����kdq������");
    byte m = 0, n = 0;
    short p = 0; //�����һ��ʱ�á�
    short rs = kin;
    p = kuai[rs].zishu;
    DianNode1 temp = kuai[rs].zichuang;
    for (byte i = 1; i <= p; i++) {
      m = temp.a;
      n = temp.b;
      zb[m][n][CS.QSXB] = a; //���kkhb��
      zbk[m][n] = kin; //����
      temp = temp.next;
    }
    kuai[rs].qishu = a;
    System.out.println("��" + rs + "������Ϊ" + a);
    System.out.println("����kdq������");
  }

  public void kjq(byte r, byte tiao) { //����ʱ,�ɿ�ָ�ʹͬɫ�Ӽ���
    short n = 0; //the same color block is eaten
    byte p = 0, q = 0; //û������ʱ,tiaoֻ����ͬɫ.
    short rs = (short) (128 + r);
    n = kuai[rs].zishu;
    DianNode1 temp = kuai[rs].zichuang;
    for (byte i = 1; i <= n; i++) {
      p = temp.a;
      q = temp.b;
      zjq(p, q, tiao);
      temp = temp.next;
    }
    kuai[rs].qishu = 1; //�����ָ�,����Ϊ1.
    //todo:����Ϊa,b
  }

  public void kkhb(short rs1, short rs2) { //8.2����ǰ��,����δ��
    System.out.println("����kkhb:���ϲ�");
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
    System.out.println("����kkhb:���ϲ�\n");
  }

  public boolean validate(byte a, byte b) {//����λ�õ���Ч�ԡ�
    byte m, n, qi = 0;
    //��shoushu����֮ǰ���ã�yise��tongse�ļ���������ͬ��
    byte tongse = (byte) (shoushu % 2 + 1); //yi se=1��2,������Ϊ����
    byte yise = (byte) ( (1 + shoushu) % 2 + 1); //tong se=1��2,�׺���Ϊż��
    if (a > CS.ZBXX && a < CS.ZBSX && b > CS.ZBXX && b < CS.ZBSX && zb[a][b][CS.ZTXB] == CS.BLANK) {
      //�±�Ϸ�,�õ�հ�
      if (a == hui[shoushu][2] && b == hui[shoushu][3]) { //�Ƿ���ŵ�
        System.out.print("���Ǵ��ʱ�Ľ��ŵ�,�����ҽٲ�!");
        System.out.println("���Ϊ��a=" + a + ",b=" + b);
        return false;
      }
      else {
        //System.out.println("���Ϊ��a=" + a + ",b=" + b);
        for (byte i = 0; i < 4; i++) {
          m = (byte) (a + CS.szld[i][0]);
          n = (byte) (b + CS.szld[i][1]);
          if (zb[m][n][CS.ZTXB] == CS.BLANK) {
            return true;
          }
          else if (zb[m][n][CS.ZTXB] == yise) {
            if (zb[m][n][CS.QSXB] == 1) {
              return true; //todo
            }
          }
          else if (zb[m][n][CS.ZTXB] == tongse) {
            if (zb[m][n][CS.QSXB] > 1) {
              return true;
            }
            else {
              qi += zb[m][n][CS.QSXB]; //���۳ɿ���񶼳���
              qi--;
            }
          }
        }
        if (qi == 0) {
          System.out.print("������ɱ�Ľ��ŵ㣺");
          System.out.println("a=" + a + ",b=" + b);
          return false;
        }
        else {
          System.out.print("���ǺϷ��ŵ㣺");
          System.out.println("a=" + a + ",b=" + b);
          return true;
        }
      }
    }
    else { //��һ�಻�Ϸ���.
      System.out.print("�õ㲻�Ϸ�,������֮����߸õ��Ѿ����ӣ�");
      System.out.println("a=" + a + ",b=" + b);
      return false;
    }
  }

  public void output() { //���ÿ�����Ϣ;

    DianNode1 temp = null;
    DianNode1 first = null;
    short zishu = 0;
    byte qishu = 0;
    short i = 0;
    short j = 0;
    byte m, n;

    System.out.println("���� output");
    for (i = 1; i <= ki; i++) {
      temp = kuai[i].zichuang;
      zishu = kuai[i].zishu;
      System.out.print("���:" + i + "������:" + zishu);
      if(zishu>20) continue;
      for (j = 1; j <= zishu; j++) {
        m = temp.a;
        n = temp.b;
        System.out.print("(" + m + "," + n + ")");
        temp = temp.next;
      }
      System.out.println("");
    }

    for (i = 1; i <= ki; i++) {
      temp = kuai[i].qichuang;
      qishu = kuai[i].qishu;
      if(qishu>20) continue;

      System.out.print("���:" + i + "������:" + qishu);
      for (j = 1; j <= qishu; j++) {
        m = temp.a;
        n = temp.b;
        System.out.print("(" + m + "," + n + ") ");
        temp = temp.next;
      }
      System.out.println("");
    }
    for (i = 1; i <= ki; i++) {
      HaoNode1 temp1 = kuai[i].zwyskhao;
      short kuaishu = kuai[i].zwyskshu;
      System.out.print("���:" + i + "����Χ����:" + kuaishu);
      for (j = 1; j <= kuaishu; j++) {

        System.out.print("(" + temp1.hao + ")");
        temp1 = temp1.next;
      }
      System.out.println("");
    }
    for (i = 1; i <= qiki; i++) {

      System.out.println("���:" + i + "��zishu:" + qikuai[i].zishu);

    }

    System.out.print("ki=" + ki + ";shoushu=" + shoushu);
    System.out.println(";ktw=" + ktw + ";ktb=" + ktb);
    System.out.println("���� output");
  }

  public void zjq(byte a, byte b, byte tiao) { //function 6.1
    //��������������
    byte i, m1, j, n1, yiseks = 0;
    short c1 = 0;
    short ysk[] = { //�ͱ��������Ϊ��ɫ��
        0, 0, 0, 0};
    for (i = 0; i < 4; i++) {
      m1 = (byte) (a + CS.szld[i][0]);
      n1 = (byte) (b + CS.szld[i][1]);
      if (zb[m1][n1][CS.ZTXB] == tiao) {
        c1 = zbk[m1][n1];
        /* if (c1 == 0) {
           zb[m1][n1][CS.QSXB] -= 1;
           if (zb[m1][n1][CS.QSXB] < 1) {
             System.out.println("����ʱ��������:a=" + m1 + ",b=" + n1);
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
          kdq(c1, kuai[c1 + 128].qishu -= 1);
        }
        //}
      }
    }
  }

  public void zzq(byte a, byte b, byte tiao)
  //���ڻ��壨�൱�����ӱ��ᣩ;����������ʱ��������;
  //��֮��ĳ�ӱ�������Է�������.tiaoָ���ӷ�����ɫ��
  {
    System.out.println("����zzq()");
    byte i, j, yiseks = 0;
    short c1 = 0;
    short cs;
    byte m1, n1;
    short ysk[] = {
        0, 0, 0, 0};
    for (i = 0; i < 4; i++) {
      m1 = (byte) (a + CS.szld[i][0]);
      n1 = (byte) (b + CS.szld[i][1]);
      if (zb[m1][n1][CS.ZTXB] == tiao) {
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
    zb[a][b][CS.ZTXB] = CS.BLANK;
    zb[a][b][CS.QSXB] = 0;
    zbk[a][b] = 0;
    System.out.println("����zzq()");
  }

  public void kzq(short rs, byte tiao) { //6.2 yi se kuai bei ti
    //�����ɫ��ʱ,ͬɫ����������
    short n = 0;
    byte p = 0, q = 0;

    n = kuai[rs].zishu;
    DianNode1 temp = kuai[rs].zichuang;
    System.out.println("����kzq()");
    for (byte i = 1; i <= n; i++) {
      p = temp.a;
      q = temp.b;
      zzq(p, q, tiao);
      temp = temp.next;
      //����ԭ����Ϣ,��Ҫ��������Ϣ,���ڻ���ʱ�ָ�
    }
    kuai[rs].qishu = 0;
    System.out.println("����kzq()");
  }

  public void cgcl(byte c) {
    byte a = (byte) ( (c - 1) % 19 + 1);
    byte b = (byte) ( (c - 1) / 19 + 1);
    cgcl(a, b);
  } //�ṩһά����Ľӿ�

  public void delete(short r, byte a, byte b) {
    //���ӿ��������ɾ��һ���������ꡣ
    //��Ϊ�л��壬���Բ��������������������漰�顣
    //�������ò���ȷ����������������kdq����ȷ�б�Ҫ��
    System.out.println("����delete()");

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
    System.out.println("����delete()");
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
    //���洦���Ѿ������ǺϷ��ŵ㣻
    //���Խ��ܵ�����Ϊ(a,b)��c;c=b*19+a-19;�����������
    //a����������±�,Ҳ��ƽ��ĺ�����:1-19
    //b����������±�,Ҳ����Ļ��������:1-19
    //byte c;//a,b��һά��ʾ:1-361;
    byte m1 = a; //a,b�ڷ����в��ı�
    byte n1 = b; //m1,n1Ϊa,b���ڵ�.
    byte yise = 0; //��ɫ
    byte tongse = 0; //�����ӵ�ͬɫ
    byte kys = 0, kkd = 0, kts = 0;
    //���ֵ�ļ���,kysΪ��ɫ�����,kkdΪ�������,ktsΪͬɫ�����

    byte i = 0, j = 0;
    byte ktz = 0; //���Ӽ���,�ֲ�
    byte tkd = 0;
    short kin1 = 0; //a,b��Χ�ĵ�Ŀ�����
    byte[][] zijieqi = new byte[4][2];
    short[] ysk = {
        0, 0, 0, 0}; //����ɫ���ӵĿ�����,ͬ�鲻�ظ�����
    byte yiseks = 0; //������ɫ����
    byte zijieqishu = 0; //���ӵ���Χֱ�ӵ������������������γɵ�����
    //����Ҫ������ǰ���á�
    if (validate(a, b) == false) {
      return;
    }

    System.out.println("come into method cgcl()");
    zijieqishu = jszq(a, b);
    hui[++shoushu][1] = a; //��������ǰ����,����1��ʼ����.������ͬ.
    hui[shoushu][2] = b; //��¼ÿ�������
    yise = (byte) (shoushu % 2 + 1); //yi se=1��2,������Ϊ����
    tongse = (byte) ( (1 + shoushu) % 2 + 1); //tong se=1��2,�׺���Ϊż��
    zb[a][b][CS.ZTXB] = tongse; //���Զ�̬һ��
    qikuai[zb[a][b][CS.QKSYXB]].deletezidian(a, b);
      System.out.println("qikuai[qiki].zishu="+qikuai[qiki].zishu);
    zb[a][b][CS.QKSYXB] = 0;

    ZiKuai1 linshi = new ZiKuai1();
    linshi.color = zb[a][b][CS.ZTXB];
    linshi.zishu = 1;
    linshi.addzidian(a, b);
    ki++;
    kuai[ki] = linshi;
    zbk[a][b] = ki; //count from first block
    huik[shoushu][0] = ki; //��¼���ɿ������
    byte dang = 0;
    for (i = 0; i < 4; i++) {
      m1 = (byte) (a + CS.szld[i][0]);
      n1 = (byte) (b + CS.szld[i][1]);
      if (zb[m1][n1][CS.ZTXB] == CS.BLANK) { //2.1the breath of blank
        zijieqi[dang][0] = m1;
        zijieqi[dang][1] = n1;
        dang++;
      }
    }

    System.out.println("����ͬɫ����");
    for (i = 0; i < 4; i++) { //�ٴ���ͬɫ����
      m1 = (byte) (a + CS.szld[i][0]);
      n1 = (byte) (b + CS.szld[i][1]);
      //��Ϊkkhbʱ�Ѿ��ı���ԭ����������ţ����Բ������ظ���
      //�㷨���������в�ͬ��
      if (zb[m1][n1][CS.ZTXB] == tongse && zbk[m1][n1] != ki) { //3.1
        kts++; //ͬɫ�����
        kin1 = zbk[m1][n1];
        huik[shoushu][kts] = kin1;
        HaoNode1 teli = kuai[kin1].zwyskhao; //�޸���Χ��ŵ�ָ��

        for (byte p = 1; p <= kuai[kin1].zwyskshu; p++) {
          kuai[teli.hao].deleteyisekuaihao(kin1);
          kuai[teli.hao].addyisekuaihao(ki); //�޸Ŀ��
          kuai[ki].addyisekuaihao(teli.hao);
          teli = teli.next;
        }
        kkhb(ki, kin1);

      }
      //�������С����Χ�������Сֵ��Σ�յģ���Ȼ��Ҫ����
      //�Ƿ��г������ֶΡ�

      /*if (zb[m1][n1][CS.ZTXB] == tongse) { //3.1
        kts++; //ͬɫ�����
        kin1 = zbk[m1][n1];
        for (j = 1; j < kts; j++) {
          //if(kin1==tsk[j]) break;
          if (kin1 == huik[shoushu][j]) {
            break;
          }
        } //������ͻ�Ĳ�ʹ��ѵ
        if (j == kts) { //���ظ�
          //tsk[ks++]=kin1;
          //hui[shoushu][20+ks]=kin1;
          //kuai[kin1].addkuai(ki);
          huik[shoushu][kts] = kin1;
          HaoNode1 teli = kuai[kin1].zwkhao;
          for (byte p = 1; p <= kuai[kin1].zwkshu; p++) {
            kuai[teli.hao].deletekuaihao(kin1);
            kuai[teli.hao].addkuaihao(ki);//�޸Ŀ��
            kuai[ki].addkuaihao(teli.hao);
            teli=teli.next;
          }
          kkhb(ki, kin1);
        }
        else {
          kts--;
        }
        // } //�ɿ��
             }*/
    }
    System.out.println("ͬɫ��kts=" + kts);

    System.out.println("������ɫ����");
    for (i = 0; i < 4; i++) { //�ȴ�����ɫ����
      //byte bdcds = 0; //����Ե����.
      byte bdcks = 0; //����Կ����.
      m1 = (byte) (a + CS.szld[i][0]);
      n1 = (byte) (b + CS.szld[i][1]);
      if (zb[m1][n1][CS.ZTXB] == yise) { //1.1�ұ����ڵ�
        kys++; //��ɫ�����
        kin1 = zbk[m1][n1]; //������
        //if (kin1==0)Ϊ�顣
        for (j = 0; j < yiseks; j++) {
          if (kin1 == ysk[j]) {
            break;
          }
        }
        if (j == yiseks) { //���ظ�
          ysk[yiseks++] = kin1;
          byte qi = (byte) (kuai[kin1].qishu - 1);

          if (qi == 0) { //ԭ��������
            //�����м���������Ϊ�˻��崦����
            kys--;
            tkd++; //<=4
            huik[shoushu][4 + tkd] = kin1; //�ݴ˽��ж����鴦��
            ktz += kuai[kin1].zishu; //ʵ�ʵ�������
            System.out.println("�鱻�ԣ����Ϊ��" + kin1);
            kzq(kin1, tongse); //��ɫ�鱻��,ͬɫ������.
            //��Χ�Ŀ�����ڿ��������Ҫɾ���ñ���Ŀ��
            HaoNode1 linh = kuai[kin1].zwyskhao;
            byte linhs = kuai[kin1].zwyskshu;
            for (byte tt = 1; tt <= linhs; tt++) {

              kuai[linh.hao].deleteyisekuaihao(kin1);
              linh = linh.next;
            }
            //�γ��µ����飻
            qiki++;
            qikuai[qiki] = new QiKuai1();
            qikuai[qiki].zichuang = kuai[kin1].zichuang;
            qikuai[qiki].zishu = kuai[kin1].zishu;
            qikuai[qiki].color = kuai[kin1].color;
            //�����µĶ����飬��Χ�Ŀ���ָ��ָ��ö����顣
          }

          else if (qi < 0) {
            System.out.println("��������:kin=" + kin1);
            return;
          }
          else {
            kuai[kin1].deleteqidian(a, b);
            kdq(kin1, qi);
            kuai[kin1].addyisekuaihao(ki);
            kuai[ki].addyisekuaihao(kin1); //Ҫ��ֹ�ظ�
            System.out.println("��" + kin1 + "������Ϊ" + zb[m1][n1][CS.QSXB]);
            if (qi == 1) {

              huik[shoushu][8 + tkd] = kin1;
              System.out.println("�鱻��ԣ����Ϊ��" + kin1);
            }

          }
        } //���ظ���
        //} //if kuai
      } // if==yiseks
    } //��ѭ������

    zb[a][b][CS.QSXB] = 0; //��ֹ����ʱ������.
    if (shoushu % 2 == CS.BLACK) {
      ktb += ktz;
    }
    else {
      ktw += ktz; //���ֲ����Ӽ���
    }

    System.out.println("����հ�����");
    /*for (i = 0; i < 4; i++) { //�ٴ���հ�����
      m1 = (byte) (a + CS.szld[i][0]);
      n1 = (byte) (b + CS.szld[i][1]);
      if (zb[m1][n1][CS.ZTXB] == CS.BLANK) { //2.1the breath of blank
        kkd++; //�������
        chengkuai(m1,n1);
        //kuai[ki].addqidian(m1, n1);
      }
         }*/

    if (kts == 0) { //4.1 û��ͬɫ�ڵ�
      System.out.println("û��ͬɫ�ڵ�");
      zb[a][b][2] = kkd;
      kuai[ki].qishu = kkd;
      if (kkd == 1 && ktz == 1) { //���ǽ�
        hui[shoushu][18] = hui[shoushu][1]; //��ٽ���
        hui[shoushu][19] = hui[shoushu][2];

      } //

    }

    /* for (j = 1; j <= kts; j++) {
       //hui[shoushu][20+j]=tsk[j-1];
       kkhb(ki, huik[shoushu][j]);
       //kkhb(ki,tsk[j-1]);//���ϲ�,����δ����.
     }*/
    jskq(ki);
    //������С�Ŀ飻
    if (kuai[ki].zwyskshu > 1) {
      byte p = kuai[ki].zwyskshu;
      HaoNode1 lint = kuai[ki].zwyskhao;
      byte qis = 127;
      for (byte wi = 1; wi <= p; wi++) {
        if (kuai[lint.hao].qishu < qis) {
          qis = kuai[lint.hao].qishu;
        }
        lint = lint.next;
      }
      kuai[ki].minqi = qis; //��С��������Χ���minqiҲӦ������
    }

    //11��22�գ���������������
    byte m2, n2, m3, n3, m4, n4, x, y;
    switch (zijieqishu) {
      case 1: {
        System.out.println("ֱ������Ϊ1��û���¿����ɡ�");
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
          if (zb[x][y][CS.ZTXB] == CS.BLANK) {
            System.out.println("ֱ������Ϊ2���Խǵ�Ϊ�գ�û���¿����ɡ�");
          }
          else if (jgzs(x, y) == 1) {
            System.out.println("ֱ������Ϊ2���Խǵ���Χ�գ�û���¿����ɡ�");
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
          if (zb[x][y][CS.ZTXB] == CS.BLANK) {
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
          if (zb[x][y][CS.ZTXB] == CS.BLANK) {
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
          if (zb[x][y][CS.ZTXB] == CS.BLANK) {
            System.out.println("ֱ������Ϊ2���Խǵ�Ϊ�գ�û���¿����ɡ�");
          }
          else if (jgzs(x, y) == 1) {
            System.out.println("ֱ������Ϊ2���Խǵ���Χ�գ�û���¿����ɡ�");
          }
          else {
            //bianli.
            System.out.println("ֱ������Ϊ3���������¿����ɡ�");

          }
           break;
        }

        break;
      }
      case 4: {
        byte lianjieshu = 0;
        m1 = (byte) (a + CS.szdjd[0][0]);
        n1 = (byte) (b + CS.szdjd[0][1]);
        m2 = (byte) (a + CS.szdjd[1][0]);
        n2 = (byte) (b + CS.szdjd[1][1]);
        m3 = (byte) (a + CS.szdjd[2][0]);
        n3 = (byte) (b + CS.szdjd[2][1]);
        m4 = (byte) (a + CS.szdjd[2][0]);
        n4 = (byte) (b + CS.szdjd[3][1]);
        if (zb[m1][n1][CS.ZTXB] == CS.BLANK) {
          lianjieshu++;
          //  System.out.println("ֱ������Ϊ2���Խǵ�Ϊ�գ�û���¿����ɡ�");
        }
        else if (jgzs(m1, n1) == 1) {
          lianjieshu++;
          // System.out.println("ֱ������Ϊ2���Խǵ���Χ�գ�û���¿����ɡ�");
        }

        if (zb[m1][n1][CS.ZTXB] == CS.BLANK) {
          lianjieshu++;
          //  System.out.println("ֱ������Ϊ2���Խǵ�Ϊ�գ�û���¿����ɡ�");
        }
        else if (jgzs(m1, n1) == 1) {
          lianjieshu++;
         }

        if (zb[m1][n1][CS.ZTXB] == CS.BLANK) {
          lianjieshu++;
           }
        else if (jgzs(m1, n1) == 1) {
          lianjieshu++;
       }

        if (zb[m1][n1][CS.ZTXB] == CS.BLANK) {
          lianjieshu++;
       }
        else if (jgzs(m1, n1) == 1) {
          lianjieshu++;
       }
       if(lianjieshu>=3){
         System.out.println("ֱ������Ϊ4��lianjieshuwei3��û���¿����ɡ�");

       }else{

       }
        break;
      }
    }
    System.out.println("����cgcl()����");
  }

  public byte jgzs(byte m, byte n) { //�Ź�������

    byte dang = 0; //��������
    byte i, a, b; //����ָ�ʱ����ɢ�����ɵ�����������㣻
    for (i = 0; i < 4; i++) {
      a = (byte) (m + CS.szld[i][0]);
      b = (byte) (n + CS.szld[i][1]);
      if (zb[a][b][CS.ZTXB] != CS.BLANK) { //2.1the breath of blank
        dang++;
      }
    }
    for (i = 0; i < 4; i++) {
      a = (byte) (m + CS.szdjd[i][0]);
      b = (byte) (n + CS.szdjd[i][1]);
      if (zb[a][b][CS.ZTXB] != CS.BLANK) { //2.1the breath of blank
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
     byte k0 = 0, kys = 0, kkd = 0, kts = 0, i = 0, j = 0; //the count for three kinds of point.
     byte ks = 0, kss = 0; //ks is count for block,kss for single point
     byte kin, kin1 = 0, m = 0, n = 0; //the block index.
     System.out.println("����:�������(clhuiqi)\n");
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
     System.out.println("����:" + shoushu);
     System.out.println("a=" + m + ",b=" + n);
     kin = hui[shoushu][0];
     if (kin != 0) { //kin���³ɵĿ�
       kuai[kin].color = 0;
       kuai[kin].qishu = 0;
       kuai[kin].zishu = 0;
       kuai[kin].zichuang = null;
       kuai[kin].qichuang = null;
       ki = kin; //ȫ�ֿ��ÿ��?
       ki--; //xinzeng.
       for (i = 1; i <= 4; i++) {
         if (hui[shoushu][2 * i + 12 - 1] <= 0) { //���¿�ĵ�
           break;
         }
         else {
           m = hui[shoushu][12 + 2 * i - 1]; //13-20
           n = hui[shoushu][12 + 2 * i];
           hui[shoushu][12 + 2 * i - 1] = 0;
           hui[shoushu][12 + 2 * i] = 0;
           zb[m][n][CS.QKSYXB] = 0;
           zb[m][n][CS.ZTXB] = tongse; //fang wei bian cheng
           zb[m][n][CS.QSXB] = jszq(m, n); //�����ӵ���
           System.out.println("//����ɿ�����:" + "a=" + m + ",b" + n);
         }
       } //deal with 3 sub
       for (i = 1; i <= 4; i++) { //�Ƿ�ɿ���¿�
         kin1 = hui[shoushu][20 + i]; //21-24
         hui[shoushu][20 + i] = 0;
         if (kin1 == 0) {
           break;
         }
         else {
           p = kuai[kin1 + 128].zishu;
           DianNode1 temp = kuai[kin1 + 128].zichuang;
           for (j = 1; j <= p; j++) { //���ѭ���ؼ�����������Ļ�����
             m = temp.a;
             n = temp.b;
             zb[m][n][3] = kin1; //�޸Ŀ��
             //zb[m][n][0]=tongse;
             zb[m][n][2] = kuai[kin1 + 128].qishu; //�ָ�ԭ��ɿ�ʱ����
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
         zb[m][n][CS.ZTXB] = yise;
         zb[m][n][CS.QSXB] = 1;
         zb[m][n][CS.QKSYXB] = 0;
         zjq(m, n, tongse);
         System.out.print("�ָ�������:");
         System.out.println("a=" + m + ",b=" + n);
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
         p = kuai[kin1 + 128].zishu;
         DianNode1 temp = kuai[kin1 + 128].zichuang; //shiwubiyao
         for (j = 1; j <= p; j++) {
           m = temp.a;
           n = temp.b;
           zb[m][n][CS.ZTXB] = yise;
           zb[m][n][CS.QKSYXB] = kin1;
           temp = temp.next;
         }
         tdzs += p;
       } //else
     } //for
     if (tongse == CS.BLACK) {
       ktb -= tdzs;
     }
     if (tongse == WHITE) {
       ktw -= tdzs;
     }
     for (i = 0; i < 9; i++) {
       hui[shoushu][27 + i] = 0; //2yue
     }
     shoushu--;
     System.out.println("����clhuiqi:�������\n");
   } //clhuiqi*/

  public byte jszq(byte m, byte n) { //huiqishiyong.
    byte dang = 0; //��������
    byte i, a, b; //����ָ�ʱ����ɢ�����ɵ�����������㣻
    for (i = 0; i < 4; i++) {
      a = (byte) (m + CS.szld[i][0]);
      b = (byte) (n + CS.szld[i][1]);
      if (zb[a][b][CS.ZTXB] == CS.BLANK) { //2.1the breath of blank
        dang++;
      }
    }
    return dang;
  }

  public void chengkuai(byte a, byte b) {
    //�ڰ��ӳɿ顣
    qiki++;
    qikuai[qiki] = new QiKuai1();
    qkzishu = 0;
    if (chengqikuai(a, b) == false) {
      qikuai[qiki] = null;
      qiki--;
    }
    //qiki��SQBZXBӦ������
  }

  public byte bijiaoqishu(
      short k1, short k2, byte colorxian) {
    //colorxian=���߷�����ɫ��
    //������飨�ӣ���ɱ���жϡ�
    //�� K1���ĽǶ��жϣ�
    byte m1 = kuai[k1].qishu;
    byte m2 = kuai[k2].qishu;
    byte cha = (byte) (m1 - m2);
    if (kuai[k1].color == colorxian) {
      cha += 1;
    }//�������㣬�仯�����е�chaֵ���䣬�����ԶԷ��ĽǶȵ��ã�
    //�����Ǻ�Ϊ����ֵ��ȣ����ǲ�һ.
    //bijiaoqishu(k1,k2,black)+bijiaoqishu(k2,k1,black)==1;

    //����ֵ<=0;���Ƕ�ɱʧ�ܣ�
    //һ������Ϊ1����ɱ��ʤ������֣�
    //����һ�����þ���0����ɱʧ�ܣ�Ҳ��
    //�������ӣ��������ã�
    //һ������Ϊ2������������Ȼ��ʤ(��ʤ)���������Ⱥ�Է����������ã�
    //����һ�����þ��ǣ�1���Է�������Ȼʧ�ܣ�
    //һ������Ϊ3�����������Կɻ�ʤ���޽ٲ���ʤ���������Ⱥ�Է�û�����á�
    //��һ������Ϊ��2���Է���ʹ���ȱ���Ҳû����������

    //ת����ϵΪ��>0:�ȼ�һ����ȡ����
    //�����ǵȼ۵ģ�ֻ�ǴӲ�ͬ����������
    //���ڽ����.������������ɣ�
    return cha;

  }

  public byte xingshipanduan(byte colorjiaodu) {
    //���õ���������ʱ���ã��Է����Ȼ��������ӡ�
    //����ȷ��������õĵص������ӵľֲ�
    //ͬʱҪ���ǶԱ𴦵�Ӱ�죬һ��������ֲ���Ӱ��
    //�漰���Ӳſ���Զ����Ӱ�졣
    //�еĶ�ɱ������ʾ����Ҫʵ�ʶ�ɱ����������2���Ҷ�����û��
    //������.
    byte lingxian = 0;
    return lingxian;
  }

  public boolean chengqikuai(byte a, byte b) {
    //�ռ���Ϣ�Ĺ�����,������color=CS.BLANK,���øú���,
    //�����������Ϣ
    //����פ����kuai������,���������ò����.
    //���øú���ǰ���뽫qkzishu����
    byte m1, n1;
    //byte zishu=0;
    if (qkzishu < 10) {
      qikuai[qiki].addzidian(a, b);
      qkzishu++;
    }
    else {
      qkzishu++;
      System.out.println("�ÿ����������10,���Ϊ:" + ki);
      return false;
    }
    zb[a][b][CS.SQBZXB] = 1;
    zb[a][b][CS.QKSYXB] = qiki;
    //zishu++;
    for (byte k = 0; k < 4; k++) {
      m1 = (byte) (a + CS.szld[k][0]);
      n1 = (byte) (b + CS.szld[k][1]);
      if (zb[m1][n1][CS.SQBZXB] == 0 & zb[m1][n1][CS.ZTXB] == CS.BLANK) {
        if (chengqikuai(m1, n1) == false) {
          return false;
        }
      }
    }
    return true;
  } //�ɿ�ĵ�SQBZXB==1;


  //����״̬��ȷ�������û������������ܣ�һ�������������µ��ǳ���Ĳ�����
  //���磬ʤ�˵�����Ȼ�����Լ����۶����������������֮�ࡣ��������£���ȫ
  //�����������۾���ȱ�ݣ�����������ˣ���ȻҲ����ͨ����ֹ����ѭ���������
}

