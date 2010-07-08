package tupianxiangqi;



//ʵ�ַ��������������ڷ������ˡ�
public class XiangQiGuiZe {

  public static final byte HONGFANG = 0;
  public static final byte HEIFANG = 1;
  public static byte CHUCUO = 10;
  public static byte SHENGLI = 2;




  public static Qizi[] qizi = new Qizi[33];
  //����qizi�ĳ�ʼ����
  private static byte[] zhl = new byte[33]; //����������
  private static byte[] qishia = new byte[33]; //ÿ���ӵ���ʼ�б�
  private static byte[] qishib = new byte[33]; //ÿ�����ӵ���ʼ�бꡣ


  private static byte[][] shixiang = new byte[12][11];
  //��������ʵ�֡�
  private static byte[][] bingzu = new byte[12][11];
  private static byte[][] jiangshuai = new byte[12][11];

  byte[][] zb = new byte[12][11]; //�±�1��;ÿ��������ӱ�š�

  private byte zhuangtai = 0; //����
  private byte turncolor = HONGFANG; //��ǰ����˭�ߡ�

  public XiangQiGuiZe() {
    init();
  }

  public byte getstate() {
    return zhuangtai;
  }

  public boolean receiveData( byte color,byte ywa,byte ywb,byte m,byte n) {
    //������������ף�
    byte [] data=new byte[6];
    data[1]=color;
    data[2]=ywa;
    data[3]=ywb;
    data[4]=m;
    data[5]=n;
    return receiveData(data);
  }

  public boolean receiveData( byte[] mes) {
    //�����岽����false;����ͨ�岽����true;ʤ����zhuangtai=SHENGLI��

    byte m = 0, n = 0; //�յ�������±ꡣ
    byte ywza = 0, ywzb = 0;//����״̬�±�
    byte active = 0, actnew = 0; //����ź��յ��š�
    byte ca = 0, cb = 0;//�����յ�������±��ֵ��
    byte xun = 0; //ѭ��������

    //mes[]��0:��ɫ��1:������±�;2:������±�;3:�յ����±�;4:�յ����±ꡣ

    if (turncolor != mes[1]) { //��ɫ�Ƿ���ȷ��
      System.out.print("���ñ�����");
      return false;
    }
    m = mes[2];
    n = mes[3];

    if (m >= 1 && m <= 10 && n >= 1 && n <= 9) {
      System.out.println("�ŵ��������ڡ�");
      System.out.print("���Ϊ��");
      System.out.print("m=" + m);
      System.out.println("��n=" + n);

      if (zb[m][n] > 0 && zb[m][n] < 33) {
        if (qizi[zb[m][n]].color == turncolor) {
          active = zb[m][n]; //�������ӱ�š�
          System.out.println("turncolor��" + turncolor);
          System.out.println("���Ϊ������");
        }
        else {
          System.out.println("���Ϊ�Է���");
          return false;
        }
      }
      else if (zb[m][n] == 0) {
        System.out.println("���Ϊ�յ�");
        return false;
      }
      else if (zb[m][n] > 32 || zb[m][n] < 0) {
        System.out.println("����״̬����");
        zhuangtai = CHUCUO;
        return false;
      }
    }
    else {
      System.out.println("�ŵ��������⡣");
      return false; //ֱ���Ƴ���
    }

    //System.out.println("active=" + active);
    ywza = m; //qizi[active].a;
    ywzb = n; //qizi[active].b;
    m = mes[4];
    n = mes[5];
    if (m >= 1 && m <= 10 && n >= 1 && n <= 9) {
      System.out.println("�ŵ��������ڡ�");
      System.out.print("���Ϊ��");
      System.out.print("m=" + m);
      System.out.println("��n=" + n);

      if (zb[m][n] > 0 && zb[m][n] < 33) {
        if (qizi[zb[m][n]].color != turncolor) {
          actnew = zb[m][n]; //�������ӱ�š�
          System.out.println("turncolor��" + turncolor);
          System.out.println("�յ�Ϊ�Է���");
        }
        else {
          System.out.println("�յ�Ϊ������");
          return false;
        }
      }
      else if (zb[m][n] == 0) {
        System.out.println("�յ�Ϊ�յ㣬�����ƶ�");

      }
      else if (zb[m][n] > 32 || zb[m][n] < 0) {
        System.out.println("����״̬����");
        zhuangtai = CHUCUO;
        return false;
      }
    }
    else {
      System.out.println("�ŵ��������⡣");
      return false; //ֱ���Ƴ���
    }

    ca = (byte) (m - ywza);
    cb = (byte) (n - ywzb);
    //actnew = zb[m][n]; //�µĻ�ӵ��±ꡣ

    System.out.print("�յ�Ϊ��");
    System.out.print("a=" + ywza);
    System.out.println(";b=" + ywzb);
    System.out.print("actnew=" + actnew);
    System.out.print("qizi[active].zhonglei=" + zhl[active]);
    /* if (actnew != 0 && qizi[active].color == qizi[actnew].color) {
       System.out.println("��ʽ����");
       return false;
     }*/
    switch (zhl[active]) { //���кϷ����жϡ�
      case 1: { //����
        System.out.print("��ǰ��Ϊ��");
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
          System.out.println("�Ϻ�����");
          //�Ϻ�����;
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
          System.out.println("�Ϻ�����");
          break;
        }

        else {
          return false; //�������
        }

      }
      case 2: //��
        System.out.print("��ǰ��Ϊ��,");
        {
          if (Math.abs(ca * cb) != 2) {
            System.out.println("���Ϲ���");
            return false;
          }
          if (Math.abs(ca) == 1) { //ע�⸺ֵ
            if (cb == 2) {
              if (zb[ywza][ywzb + 1] != 0) {
                System.out.println("���Ϲ���:�����ȡ�");
                return false;
              }
            }
            else if (cb == -2) {
              if (zb[ywza][ywzb - 1] != 0) {
                System.out.println("���Ϲ���:�����ȡ�");
                return false;
              }
            }
          }
          else if (ca == 2) {
            if (zb[ywza + 1][ywzb] != 0) {
              System.out.println("���Ϲ���:�����ȡ�");
              return false;
            }
          }
          else if (ca == -2) {
            if (zb[ywza - 1][ywzb] != 0) {
              System.out.println("���Ϲ���:�����ȡ�");
              return false;
            }
          }
          System.out.println("�Ϻ�����");
          break;
        }
      case 3: {
        //�ڡ�
        System.out.println("��ǰ��Ϊ��");
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
          System.out.println("�����ƶ�");
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
          return false; //�������
        }
        if (actnew == 0) { //�ƶ�
          if (count > 0) {
            return false;
          }
          System.out.println("�Ϻ�����");
          break;

        }
        else if (qizi[active].color != qizi[actnew].color) {
          //����

          if (count != 1) {
            return false;
          }
          System.out.println("��������");

          System.out.println("�Ϻ�����");
          break;
        }
        else if (qizi[active].color == qizi[actnew].color) {
          //active = zb[m][n];
          System.out.println("�ı䵱ǰ��Ծ�㡣");
        }
        /*else {
          return false;
          System.out.println("�Ϻ�����");
                   }*/
        break;
      }
      case 4: { //��
        System.out.println("��ǰ��Ϊʿ");
        if (Math.abs(ca) != 1 || Math.abs(cb) != 1) {
          return false;
        }
        else if (shixiang[m][n] == 0) {
          return false;
        }

        break;
      }
      case 5: { //��
        System.out.println("��ǰ��Ϊ��");
        if (Math.abs(ca) != 2 || Math.abs(cb) != 2) {
          return false;
        }
        else if (shixiang[m][n] == 0) {
          return false;
        }
        else if (zb[ywza + ca / 2][ywzb + cb / 2] != 0) {
          System.out.println("������");
          return false;
        }
        System.out.println("�Ϻ�����");
        break;
      }
      case 6: { //��
        System.out.println("��ǰ��Ϊ��");
        if (qizi[active].color == HONGFANG) {
          System.out.println("��ǰ��Ϊ���");
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
        else if (qizi[active].color == HEIFANG) { //�ڷ�
          System.out.println("��ǰ��Ϊ�ڱ�");
          if (ca == 1) {
            if (cb != 0) {
              System.out.println("���Ϻ�����:���ӱ�");
              return false;
            }
          }
          else if (ca == 0 && ywza > 5 && Math.abs(cb) == 1) {
            System.out.println("�Ϻ�����:���ӱ�");
          }
          else {
            return false;
          }

        }
        System.out.println("�Ϻ�����");
        break;

      }
      case 7: { //˧
        System.out.println("��ǰ��Ϊ˧");
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
        System.out.println("�Ϻ�����");
        break;
      } //�Ϸ��Լ�������

    } //switch����

    System.out.println("switch����");

    qizi[active].a = m;
    qizi[active].b = n;
    zb[ywza][ywzb] = 0;
    zb[m][n] = active;
    if (qizi[21].b == qizi[5].b) {
      //&&ywzb==qizi[5].b&&qizi[5].b!=n){
      //��˧ͬ���ҵ�ǰ�ӴӸ����Ƴ���
      System.out.println("��˧ͬ��");
      byte jsjj = 0; //��˧֮���������
      for (byte tt = (byte) (qizi[21].a + 1); tt < qizi[5].a; tt++) {
        if (zb[tt][qizi[21].b] != 0) {
          jsjj++;
          break;
        }
      }
      if (jsjj == 0) {
        System.out.println("��˧���棬���Ϲ���");
        qizi[active].a = ywza;
        qizi[active].b = ywzb;
        zb[ywza][ywzb] = active;
        zb[m][n] = actnew;
        return false;
      }

    }

    if (actnew == 0) { //�ƶ�
      System.out.println("Ŀ���Ϊ�գ������ƶ�");
      active = 0;

    }
    else if (qizi[active].color != qizi[actnew].color) {
      //����
      System.out.println("Ŀ���Ϊʵ������");
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

  //10��22�գ������Լ������������

  public void init() { //�빹������ȣ�������ʱ��������󲻱�
    byte wa = 0;
    byte wb = 0;
    byte i = 0, j = 0;
    byte zishu = 0;
    System.out.println("Board ��ʼ����");
    for (i = 1; i < 10; i++) { //ÿ�����ӵ���ʼλ�á�
      qishia[i] = 10;
      qishib[i] = i;
    }
    qishia[10] = 8;
    qishib[10] = 2;
    qishia[11] = 8;
    qishib[11] = 8;
    for (i = 0; i < 5; i++) { //����λ��
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

    //������ʿ���˧��
    zhl[1] = 1; //ÿ�����ӵı���
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
    zhl[17] = 1; //ÿ�����ӵı���
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
    zhl[31] = 6; //ԭ���˴���31Ϊ30������31��zhongleiΪ�죬
    //������֡�
    zhl[32] = 6;

    //���ڸ���ʵ�ֹ���
    shixiang[1][4] = 1; //ʿ����Ч�㡣
    shixiang[1][6] = 1;
    shixiang[3][4] = 1;
    shixiang[3][6] = 1;
    shixiang[2][5] = 4;
    shixiang[10][4] = 3;
    shixiang[10][6] = 3;
    shixiang[8][4] = 3;
    shixiang[8][6] = 3;
    shixiang[9][5] = 4;

    shixiang[3][1] = 2; //�����Ч�㡣
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

    jiangshuai[1][4] = 1; //��˧����Ч�㡣
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
    jiangshuai[8][6] = 1; //��18�㡣

    for (i = 1; i <= 16; i++) { //��ʼ������

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
