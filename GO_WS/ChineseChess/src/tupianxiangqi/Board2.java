package tupianxiangqi;
//��toolkit����ͼ����ѡ�

import java.awt.*;
import java.lang.*;
import java.math.*;
import java.awt.image.*;
import java.net.URL;
import javax.swing.ImageIcon;

public class Board2
    extends Canvas {
  private Image work; //�����⻺��ͼ��
  private Image[] yuansu = new Image[16];
  private Graphics gcontext;

  boolean bukexia = false; //�ܹ����ӵı�־��
  byte shengli = -1; //0Ϊ�췽ʤ��1Ϊ�ڷ�ʤ��
  public static final byte HONGFANG = 0;
  public static final byte HEIFANG = 1;
  //���̴�СΪ470��520��
  public static final byte BANJING = 21; //���Ӱ뾶
  public static final byte LIEKUAN = 25; //�п�
  public static final byte HANGJU = 25; //�о�
  public static final byte JIANXI = 30; //���Ͻ���ԭ��ľ��롣
  byte mycolor; //ִ�ڻ���ִ�졣�ڷ��赹ת���̣�����˼����
  byte turncolor = HONGFANG; //��ǰ����˭�ߡ�
  //byte qidiana, qidianb; //���ڽ�����ʽ��ͼ��
  //byte zdiana, zdianb; //ǰ�����ĵ㡣
  byte wa = 0;
  byte wb = 0;

  Qizi[] qizi = new Qizi[33];
  byte[][] zb = new byte[12][11]; //�±�1��ÿ��������ӱ�š�
  //����qizi�ĳ�ʼ����
  byte[] zhuanghuan = new byte[33];
  byte[] zhl = new byte[33]; //����������
  byte[] qishia = new byte[33]; //ÿ���ӵ���ʼ�б�
  byte[] qishib = new byte[33]; //ÿ�����ӵ���ʼ�бꡣ
  /*String[] mingzi = { //�췽��ǰ��
      "",
      "��", "��", "��", "��", "˧", "��", "��", "��", "��",
      "��", "��", "��", "��", "��", "��", "��",
      "��", "��", "��", "ʿ", "��", "ʿ", "��", "��", "��",
      "��", "��", "��", "��", "��", "��", "��", };*/
  //���ִ�������ͳһ�޸ġ�

  byte[][] shixiang = new byte[12][11]; //��������ʵ�֡�
  byte[][] bingzu = new byte[12][11];
  byte[][] jiangshuai = new byte[12][11];

  //byte[][] shizi = new byte[12][11]; //�Ǹ�λ����ʮ�֡�
  byte active = 0;
  //byte yactive1 = 0;
  //byte yactive2 = 0;
  public boolean mouseDown(Event e, int x, int y) {
    System.out.println("mousedown");
    byte m = 0, n = 0; //���������±ꡣ
    if (bukexia == true) {
      return true;
    }
    n = (byte) ( (x - JIANXI + LIEKUAN) / (2 * LIEKUAN) + 1);
    m = (byte) ( (y - JIANXI + HANGJU) / (2 * HANGJU) + 1);
    System.out.println("m=" + m);
    System.out.println("n=" + n);
    if (m >= 1 && m <= 10 && n >= 1 && n <= 9) {
      System.out.println("�ŵ��������ڡ�");
    }
    else {
      System.out.println("�ŵ��������⡣");
      return true; //ֱ���Ƴ���
    }
    if (active == 0) { //��δָ��Ҫ�ߵ��ӡ�
      System.out.println("��ǰû�л�Ծ�㡣");
      if (zb[m][n] > 0 && qizi[zb[m][n]].color == turncolor) {
        active = zb[m][n];
        System.out.println("turncolor��" + turncolor);
        repaint();
        return true;
      }
      else {
        System.out.println("�յ��Է���");
        return true;
      }
    }
    else { //�Ѿ�ָ��Ҫ�ߵ��ӡ�
      System.out.println("active=" + active);
      byte ywza = qizi[active].a;
      byte ywzb = qizi[active].b;

      byte ca = (byte) (m - ywza);
      byte cb = (byte) (n - ywzb);
      byte actnew = zb[m][n]; //�µĻ�ӵ��±ꡣ
      byte xun = 0; //ѭ��������
      System.out.print("�Ѿ�ָ��Ҫ�ߵ��ӡ�");
      System.out.print("a=" + ywza);
      System.out.println(";b=" + ywzb);
      System.out.print("actnew=" + actnew);
      System.out.print("qizi[active].zhonglei=" + qizi[active].zhonglei);
      if (actnew != 0 && qizi[active].color == qizi[actnew].color) {
        System.out.println("�ı���");
        active = zb[m][n];
        repaint();
        return true; //����
      }
      switch (qizi[active].zhonglei) { //���кϷ����жϡ�
        case 1: { //����
          System.out.print("��ǰ��Ϊ��");
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
            System.out.println("�Ϻ�����");
            //�Ϻ�����
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
            System.out.println("�Ϻ�����");
            break;
          }

          else {
            return true; //�������
          }

        }
        case 2: //��
          System.out.print("��ǰ��Ϊ��,");
          {
            if (Math.abs(ca * cb) != 2) {
              System.out.println("���Ϲ���");
              return true;
            }
            if (Math.abs(ca) == 1) { //ע�⸺ֵ
              if (cb == 2) {
                if (zb[ywza][ywzb + 1] != 0) {
                  System.out.println("���Ϲ���:�����ȡ�");
                  return true;
                }
              }
              else if (cb == -2) {
                if (zb[ywza][ywzb - 1] != 0) {
                  System.out.println("���Ϲ���:�����ȡ�");
                  return true;
                }
              }
            }
            else if (ca == 2) {
              if (zb[ywza + 1][ywzb] != 0) {
                System.out.println("���Ϲ���:�����ȡ�");
                return true;
              }
            }
            else if (ca == -2) {
              if (zb[ywza - 1][ywzb] != 0) {
                System.out.println("���Ϲ���:�����ȡ�");
                return true;
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
            return true; //�������
          }
          if (actnew == 0) { //�ƶ�
            if (count > 0) {
              return true;
            }
            System.out.println("�Ϻ�����");
            break;

          }
          else if (qizi[active].color != qizi[actnew].color) {
            //����

            if (count != 1) {
              return true;
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
            return true;
            System.out.println("�Ϻ�����");
                     }*/
          break;
        }
        case 4: { //��
          System.out.println("��ǰ��Ϊʿ");
          if (Math.abs(ca) != 1 || Math.abs(cb) != 1) {
            return true;
          }
          else if (shixiang[m][n] == 0) {
            return true;
          }

          break;
        }
        case 5: { //��
          System.out.println("��ǰ��Ϊ��");
          if (Math.abs(ca) != 2 || Math.abs(cb) != 2) {
            return true;
          }
          else if (shixiang[m][n] == 0) {
            return true;
          }
          else if (zb[ywza + ca / 2][ywzb + cb / 2] != 0) {
            System.out.println("������");
            return true;
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
                return true;
              }
            }
            else if (ca == 0 && ywza < 6 && Math.abs(cb) == 1) {

            }
            else {
              return true;
            }
          }
          else if (qizi[active].color == HEIFANG) { //�ڷ�
            System.out.println("��ǰ��Ϊ�ڱ�");
            if (ca == 1) {
              if (cb != 0) {
                System.out.println("���Ϻ�����:���ӱ�");
                return true;
              }
            }
            else if (ca == 0 && ywza > 5 && Math.abs(cb) == 1) {
              System.out.println("�Ϻ�����:���ӱ�");
            }
            else {
              return true;
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
              return true;

            }
          }
          else {
            return true;
          }
          System.out.println("�Ϻ�����");
          break;
        } //�Ϸ��Լ�������

      } //switch����
      System.out.println("switch����");
      if (actnew != 0 && (actnew == 5 || actnew == 21)) {
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
        System.out.println("Ŀ���Ϊʵ������");
        active = 0;
        repaint();
        return true;

      }
      //qizi[active].a = m;
      //}
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
          return true;
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
        active = 0;
      }
      if (turncolor == HONGFANG) {
        turncolor = HEIFANG;
        if (actnew != 0 && zhl[actnew] == 7) {
          shengli = HONGFANG;
        }
      }
      else if (turncolor == HEIFANG) {
        turncolor = HONGFANG;
        if (actnew != 0 && zhl[actnew] == 7) {
          shengli = HEIFANG;
        }
      }

      repaint();

      /*else if (qizi[active].color == qizi[actnew].color) {
        active = zb[m][n];
        return true;
             }*/
      //ͬɫ�Ŀ������Ѿ��ų�

    }
    /* if (zb[m][n] == 0) {
       //�Ϸ��Լ�鲢��ֵ��
     }
     else {
     }*/

    return true;
  }

  public Board2() {

    byte i = 0, j = 0;
    byte zishu = 0;
    String imagename;
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

    //��ʮ�ĸ���־
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

    zhuanghuan[1] = 4;
    zhuanghuan[2] = 3;
    zhuanghuan[3] = 5;
    zhuanghuan[4] = 1;
    zhuanghuan[5] = 2;
    zhuanghuan[6] = 6;
    zhuanghuan[7] = 0;

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
      qizi[i] = new Qizi(i, zhl[i],
                         wa, wb, HONGFANG);
      System.out.print(qizi[i].bianhao);
      //System.out.print(mingzi[qizi[i].bianhao]);
      System.out.print(qizi[i].zhonglei);
      System.out.println(qizi[i].color);
      //System.out.println(mingzi[qizi[i].bianhao]);
    }
    for (i = 17; i <= 32; i++) {
      wa = qishia[i];
      wb = qishib[i];
      zb[wa][wb] = i;
      qizi[i] = new Qizi(i, zhl[i], wa, wb, HEIFANG);
      System.out.print(qizi[i].bianhao);
      //System.out.print(mingzi[qizi[i].bianhao]);
      System.out.print(qizi[i].zhonglei);
      System.out.println(qizi[i].color);
    }

  }

  public void init() {
    /*image1 =   image2 = new ImageIcon(tupianxiangqi.Frame1.class.getResource("closeFile.png"));
         image3 = new ImageIcon(tupianxiangqi.Frame1.class.getResource("help.png"));
     */
    byte i;
    String imagename;


    yuansu[0] =   Toolkit.getDefaultToolkit().getImage("Board.jpg");
    yuansu[15] = Toolkit.getDefaultToolkit().getImage("pieceTex.jpg");
    for (i = 1; i <= 7; i++) { //hong fang zai qian.
      imagename = "Red" + (i - 1) + ".jpg";
      yuansu[i] = Toolkit.getDefaultToolkit().getImage(imagename);
      imagename = "Blue" + (i - 1) + ".jpg";
      yuansu[7 + i] = Toolkit.getDefaultToolkit().getImage(imagename);
      //yuansu[i]=this.getImage(this.getCodeBase(),"Blue"+zhl[i]+".jpg") ;;
      System.out.println("imagenameuan=" + imagename);
      System.out.println("yuansu[i]=" + yuansu[i]);
      System.out.println("yuansu[i]=" + yuansu[i].getSource());
    }


  }


  public void update(Graphics g) {
    paint(g);
  }

  public void paint(Graphics g) {
    //setBackground(Color.orange);
    byte i = 0;
    byte j = 0;
    int yd1 = 0, yd2 = 0;
    byte qidiana, qidianb;
    work = this.createImage(461, 512);
    if (work == null) { //ֻ�ܷ��������������ȷ��ʼ��
      System.out.println("work=null");
    }
    else {
      System.out.println("work!=null");
      gcontext = work.getGraphics(); //gcontextδ����ȷ��ֵ��
      if (gcontext == null) {
        System.out.println("gcontext=null");
        return;
      }

    }

    gcontext.drawImage(yuansu[0], 0, 0, 461, 512, this);

    for (i = 1; i < 33; i++) {
      if (qizi[i].a == 0 || qizi[i].b == 0) {
        continue;
      }
      yd2 = (qizi[i].a - 1) * 2 * HANGJU + JIANXI - BANJING;
      yd1 = (qizi[i].b - 1) * 2 * LIEKUAN + JIANXI - BANJING;

      System.out.print("i=" + i);
      j = (byte) (zhuanghuan[zhl[i]] + 1 + qizi[i].color * 7);
      System.out.println(";j=" + j);
      gcontext.drawImage(yuansu[j], yd1, yd2, 2 * BANJING, 2 * BANJING, this);
      System.out.println("huaqizi");

    }



    if (active != 0) { //�õ㱻���
      System.out.println("�õ㱻���");

      yd2 = (qizi[active].a - 1) * 2 * HANGJU + JIANXI - BANJING;
      yd1 = (qizi[active].b - 1) * 2 * LIEKUAN + JIANXI - BANJING;
      gcontext.setColor(Color.green);
      gcontext.drawRect(yd1 - 1, yd2 - 1, 2 * BANJING + 2, 2 * BANJING + 2);
      System.out.println("�̿��ʾ���");

    }
    g.drawImage(work, 0, 0, 461, 512, this);
    System.out.println("������ͼ");
    if (shengli == HONGFANG) {
      g.setColor(Color.red);
      g.setFont(new Font("Courier", Font.BOLD, 25));
      g.drawString("�췽ʤ", 480, 50);
    }
    else if (shengli == HEIFANG) {
      g.setColor(Color.black);
      g.setFont(new Font("Courier", Font.BOLD, 25));
      g.drawString("�ڷ�ʤ", 480, 50);
    }

  } //paint


}

