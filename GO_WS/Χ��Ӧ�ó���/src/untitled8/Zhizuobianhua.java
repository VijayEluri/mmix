package untitled8;

import java.awt.*;
import java.applet.Applet;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import untitled8.*;
import untitled3.DsNode;
public class Zhizuobianhua
   extends Applet {
   final static byte ZKUOHAO = -127;
   final static byte YKUOHAO = 127;
   GoBoardLian1 go = new GoBoardLian1();
   boolean xds; //�Ƿ��¶�ʽ��
   DsNode root, temp, work, temp1, temp2, workf, rootb,rootc;
   short bhs = 0; //�仯��Ŀ
   short linss = 0; //��ʼ���������,ȷ���ܷ����
   boolean WCDS=false;//��ɶ�ʽ����ı�־��
   boolean KEXIA = true;//����ͬ������ֹ����˼��ʱ�˰������
   boolean CHONGHUI = true; //�Ƿ��ػ�
   boolean fhuiqi = false; //�Ƿ����
   boolean fqianjin = false; //�Ƿ�ǰ��

   DataInputStream in; //��ʽ�������ļ�
   DataOutputStream out; //��ʽ������ļ���
   DataOutputStream out2; //�仯��������ļ�

   Button store = new Button("����仯");
   Button huiqi = new Button("����");
   Button conglai = new Button("�ص���ʼ����");
   Button pass = new Button("����һ��");
   Button wenjian = new Button("����仯�ļ�");
   Button shurudingshi = new Button("���붨ʽ�ļ�");
   TextField xingshi1 = new TextField(15);
   TextField xingshi2 = new TextField(15);

   byte fenzhi = 0;
   DsNode[] stackds = new DsNode[100];

   public void init() { //��ʼ��,��ɽ���

      setLayout(null);
      add(store);
      add(pass);
      add(huiqi);
      add(wenjian);
      add(conglai);
      add(xingshi1);
      add(xingshi2);
      add(shurudingshi);
      store.reshape(575, 115, 60, 26);
      wenjian.reshape(635, 115, 100, 26);
      huiqi.reshape(575, 141, 60, 26);
      conglai.reshape(635, 141, 100, 26);
      pass.reshape(575, 167, 60, 26);
      shurudingshi.reshape(635, 167, 100, 26);
      xingshi1.reshape(575, 200, 25, 25);
      xingshi2.reshape(635, 200, 25, 25);
      xingshi1.setText("0");
      xingshi2.setText("0");
      conglai.disable();
      huiqi.disable();
      store.disable();
      wenjian.disable();
      shurudingshi.disable();

      try {
         byte lina = 0;
         byte[] dsnode = new byte[5];
         boolean ykhjs = false; //�Ƿ��Ѿ��������š�
         DataInputStream in = new DataInputStream(
            new BufferedInputStream(
            new FileInputStream("H:\\weiqidata\\" + "��ʽ��")));
         if (in.available() == 0) {
            return;
         }
         lina = in.readByte();
         if (lina == ZKUOHAO) { //���Ͷ�ʽ��Ӧ�õ�һ�����������ϱ仯��
            in.read(dsnode); //�����Ų���������
            root = work = new DsNode(dsnode[1], dsnode[2], dsnode[3],
                                     dsnode[4], dsnode[0]);
            System.out.println("��ʽ���ĸ�Ϊ��(a=" +
                               work.zba + "," + work.zbb + ")");
            //�������������Ű�Χ
            //���Ž���ʾ�������Ǹ��ڵ�ķ�֧��
            fenzhi++;
            stackds[fenzhi] = work; //�����Ž������µ������Žӵ�work�ϡ�
            System.out.println("��һ�����ŵĹ�����Ϊ��(a=" +
                               stackds[fenzhi].zba + "," + stackds[fenzhi].zbb +
                               ")");
         }
         else { //��ʽ����ʼʱ����ֻ��һ����㡣
            in.read(dsnode, 1, 4);
            root = work = new DsNode(dsnode[1], dsnode[2], dsnode[3],
                                     dsnode[4], lina);
            System.out.println("��ʽ���ĸ�Ϊ��(a=" +
                               work.zba + "," + work.zbb + ")");
         }
         while (in.available() != 0) {
            lina = in.readByte();
            if (lina == ZKUOHAO) {
               in.read(dsnode);
               temp = new DsNode(dsnode[1], dsnode[2], dsnode[3],
                                 dsnode[4], dsnode[0]);
               System.out.println("��֧�仯���׽ڵ�Ϊ:" + temp.zba
                                  + "," + temp.zbb + ")");
               if (ykhjs == false) {
                  //�µ�������
                  work.left = temp;
                  fenzhi++;
                  work = temp;
                  stackds[fenzhi] = work;
                  System.out.println("��һ�����ŵĹ�����Ϊ��(a=" +
                                     stackds[fenzhi].zba + "," +
                                     stackds[fenzhi].zbb + ")");
               }
               else { //�����Ž��������е�������
                  //�����µ������ţ�ykhjsʧЧ��
                  ykhjs = false;
                  work.right = temp;
                  System.out.println("ͬһ�����ŵ�ԭ������Ϊ��(a=" +
                                     stackds[fenzhi].zba + "," +
                                     stackds[fenzhi].zbb + ")");
                  work = temp;
                  stackds[fenzhi] = work;
                  System.out.println("ͬһ���µ������ŵĹ�����Ϊ��(a=" +
                                     stackds[fenzhi].zba + "," +
                                     stackds[fenzhi].zbb + ")");
               }
            }
            else if (lina == YKUOHAO) {
               if (ykhjs == false) {
                  ykhjs = true;
                  work = stackds[fenzhi];
                  System.out.println(work.toString());
               }
               else {
                  stackds[fenzhi--] = null;
                  work = stackds[fenzhi];
               }
            }
            else { //�����ڵ�
               in.read(dsnode, 1, 4);
               temp = new DsNode(dsnode[1], dsnode[2], dsnode[3],
                                 dsnode[4], lina);
               work.left = temp;
               work = temp;
            }
         }
         in.close();
      }
      catch (IOException ex) {
         System.out.println("���ļ�����ʽ�����������⣡");
         System.out.println("Exception" + ex.toString());
      }
      work=root;
   }

   public void paint(Graphics g) {
      System.out.println("���뷽��paint()");
      if (CHONGHUI == true) {
         g.setColor(Color.orange);
         g.fillRect(0, 0, 560, 560);

      }
      CHONGHUI = true;
      g.setColor(Color.black);
      byte kinp = 0;
      for (int i = 1; i <= 19; i++) { //����
         g.drawLine(18, 28 * i - 10, 522, 28 * i - 10); //hor
         g.drawLine(28 * i - 10, 18, 28 * i - 10, 522); //ver
      }
      for (int i = 0; i < 3; i++) { //����λ
         for (int j = 0; j < 3; j++) {
            g.fillOval(168 * i + 99, 168 * j + 99, 6, 6);
         }
      }

      for (int i = 1; i <= 19; i++) { //�����ӵ�
         for (int j = 1; j <= 19; j++) {
            if (go.zb[i][j][0] == 1) {
               g.setColor(Color.black);
               g.fillOval(28 * i - 24, 28 * j - 24, 28, 28);
               //System.out.println("//paint the black point.");
            }
            else if (go.zb[i][j][0] == 2) {
               g.setColor(Color.white);
               g.fillOval(28 * i - 24, 28 * j - 24, 28, 28);
               // System.out.println("//paint the white point.");
            }
            kinp = go.zb[i][j][go.KSYXB];
            if (kinp != 0) { //������
               g.setColor(Color.green);
               g.drawString("" + kinp, 28 * i - 4, 28 * j - 4);

            }
         }
      }
      System.out.println("�ӷ���paint()����");
   } //else���������̺�����

   public boolean mouseDown(Event e, int x, int y) { //�����������
      if (KEXIA == true) {
         //KEXIA=false;//ֻ�л������һ��,���ܼ���.
         byte a = (byte) ( (x - 4) / 28 + 1); //����������ӵ�.
         byte b = (byte) ( (y - 4) / 28 + 1);
         go.cgcl(a, b);
         go.output();
         if (go.shoushu > linss) {
            huiqi.enable();
            conglai.enable();
            if(WCDS==true){
               store.enable();
            }else{
               shurudingshi.enable();
            }
         }

         CHONGHUI = false; //Ч�������ӱ�����ʮ��
         repaint();
         System.out.println("mousedown");
         return true; //����������,��Frame����
      }
      else {
         return true;
      }
   }

   void delete(DsNode root) {//ǰ����rootΪ���ֻ������йء�
      DsNode temp,work,temp1;

      //if (root == null)
         //return;
      work=root.left ;
      if(work==null) return;//���һ���ˡ�
      else {
         for (temp = work,temp1=root; temp != null;temp1=temp, temp = temp.right) {
            if (temp.ZZYG == true)
               delete(temp.left);
            else if (temp.xingshi1 != 0) {
                if(temp1==root) temp1.left =temp.right ;
                else temp1.right=temp.right ;
            }
            else delete(temp);
         }

      }

   }
   byte fuzhi1(DsNode root) { //Ϊ���ڵ�����Ƹ�ֵ
      //6��13�ա�
      DsNode temp = null;
      byte max = (byte) - 128;
      byte linv;
      if (root.left == null) { //�仯����
         return root.xingshi1;
      }
      temp = root.left;

      for (; temp != null; temp = temp.right) {
         linv = temp.xingshi1 = fuzhi1(temp);
         if (linv > max) {
            max = linv;
         }
      }
      root.xingshi1 = (byte) ( -max);
      return (byte) ( -max);
   }

   byte fuzhi2(DsNode root) { //Ϊ���ڵ�����Ƹ�ֵ
      //6��13�ա�
      DsNode temp = null;
      byte max = (byte) - 128;
      byte linv;
      if (root.left == null) { //�仯����
         if(root.xingshi1!=root.xingshi2){
            //if(root.xingshi1 <0)
               //System.out.println("���ӳ���Ҳ���ã�������") ;
            root.ZZYG=true;
         }
         return root.xingshi2;
      }
      temp = root.left;

      for (; temp != null; temp = temp.right) {
         linv = temp.xingshi2 = fuzhi2(temp);
         if (linv > max) {
            max = linv;
         }
      }
      root.xingshi2 = (byte) ( -max);//��Ϊxingshi1,waste 1h.
      if(root.xingshi1!=root.xingshi2){
         root.ZZYG=true;
      }

      return (byte) ( -max);
   } //��ʽ���漰���ӿ��ܲ�ֹһ����

   //�ֵܼ䰴����������
   void paixu(DsNode root) {
      DsNode temp, temp1, temp2;
      byte jishu = 0;
      if (root.left == null) {
         return;
      }
      temp = temp1 = root.left;
      if (temp1.right == null) {
         return; //�����ڵ���������
      }
      for (temp2 = temp1.right; temp2 != null; temp = temp1, temp1 = temp2,
           temp2 = temp2.right) {
         jishu++;
         if (temp2.xingshi1 > temp1.xingshi1) {
            if (jishu == 1) {
               temp1.right = temp2.right;
               temp2.right = temp1;
               root.left = temp2;
            }
            else {
               temp1.right = temp2.right;
               temp2.right = temp1;
               temp.right = temp2;
               //��ʽ����Ա�ʾ���ܼ򻯴��롣
               //ת���ӽǺ�Ҫ����š�
            } //�����⣬��˵�ɣ���ǰ����Ч��������
         }
         //��λ�Ѿ��еĽڵ㡣
      }
   }

   public void update(Graphics g) { //585
      paint(g);
   }

   /* public void paint(Graphics g) {
       int tongse;
       int xun;
       int m, n;
       int kin1;
       int p;
       int i = 0, j = 0; //the count for three kinds of point.
       //  System.out.println("//come into method paint");
       if (fzhudong == true) {
          if (fhuiqi == true) {
             m = huitemp[0][0];
             n = huitemp[0][1];
             huitemp[0][0] = -1;
             huitemp[0][1] = -1;
             xz(g, m, n);
             i = 1;
             while (huitemp[i][0] >= 0) {
                m = huitemp[i][0];
                n = huitemp[i][1];
                huitemp[i][0] = -1;
                huitemp[i][1] = -1;
                tongse = shoushu % 2 + 1;
                if (tongse == 1) {
                   g.setColor(Color.white);
                }
                else {
                   g.setColor(Color.black);
                }
                g.fillOval(28 * m + 4, 28 * n + 4, 28, 28);
                i++;
             }
             fhuiqi = false;
          }
          if (fqianjin == true) {
             m = hui[shoushu][25];
             n = hui[shoushu][26];
             tongse = (shoushu + 1) % 2 + 1;
             if (tongse == 1) {
                g.setColor(Color.black);
             }
             else {
                g.setColor(Color.white);
             }
             System.out.println("color=" + tongse);
             g.fillOval(28 * m + 4, 28 * n + 4, 28, 28);
             for (i = 1; i <= 4; i++) {
                if (hui[shoushu][2 * i - 1] < 0) {
                   break;
                }
                else {
                   m = hui[shoushu][2 * i - 1];
                   n = hui[shoushu][2 * i];
                   xz(g, m, n);
                }
             }
             for (i = 1; i <= 4; i++) {
                kin1 = hui[shoushu][8 + i];
                if (kin1 < 0) {
                   break;
                }
                else {
                   p = kuai[kin1][0][1];
                   for (j = 1; j <= p; j++) {
                      m = kuai[kin1][j][0];
                      n = kuai[kin1][j][1];
                      xz(g, m, n);
                   }
                } //else
             } //for
             fqianjin = false;
          } //else
          fzhudong = false;
       } //if
       else {
          g.setColor(Color.white);
          g.fillRect(540, 0, 200, 540);
          g.setColor(Color.orange);
          g.fillRect(0, 0, 540, 540); //33
          g.setColor(Color.black);
          for (i = 0; i < 19; i++) {
             g.drawLine(18, 18 + 28 * i, 522, 18 + 28 * i); //hor
             g.drawLine(18 + 28 * i, 18, 18 + 28 * i, 522); //ver
          }
          //  System.out.println("// paint the ver and hor line.");
          for (i = 0; i < 3; i++) {
             for (j = 0; j < 3; j++) {
                g.fillOval(168 * i + 99, 168 * j + 99, 6, 6);
             }
          }
          // System.out.println("//paint the star point.");
          for (i = 0; i <= 18; i++) {
             for (j = 0; j <= 18; j++) {
                if (zb[i][j][0] == 1) {
                   g.setColor(Color.black);
                   g.fillOval(28 * i + 4, 28 * j + 4, 28, 28);
                   //System.out.println("//paint the black point.");
                }
                else if (zb[i][j][0] == 2) {
                   g.setColor(Color.white);
                   g.fillOval(28 * i + 4, 28 * j + 4, 28, 28);
                   // System.out.println("//paint the white point.");
                }
             }
          } //paint all the points owned by black and white.
       } //else
       g.setColor(Color.black);
       for (i = 10; i < 19; i++) {
          for (j = 0; j < 10; j++) {
             //g.drawString(""+zb[j][i][2],545+15*j,15*i+50);
             //g.drawString(""+zb[j][i][0],545+15*j,15*i+210);
             if (zb[j][i][2] > 0) {
       System.out.println("zb[" + j + "][" + i + "][2]=" + zb[j][i][2]);
             }
          }
       }
       for (xun = linss + 1; xun <= shoushu; xun++) {
          tongse = (1 + xun) % 2 + 1;
          m = hui[xun][25];
          n = hui[xun][26];
          if (m >= 0 && n >= 0) {
             if (zb[m][n][2] > 0) {
                if (tongse == 1) {
                   g.setColor(Color.black);
                }
                else {
                   g.setColor(Color.white);
                }
                g.fillOval(28 * m + 4, 28 * n + 4, 28, 28); //bixu fu gai ,fou ze shu zi chong he
                if (tongse == 2) {
                   g.setColor(Color.black);
                }
                else {
                   g.setColor(Color.white);
                }
                if (xun >= linss + 10) {
                   g.drawString("" + (xun - linss), 28 * m + 11, 28 * n + 23);
                }
                else {
                   g.drawString("" + (xun - linss), 28 * m + 14, 28 * n + 23);
                }
             }
          }
       }
       //System.out.println("//come into the end of method paint");
    }*/

   public boolean action(Event e, Object what) {
      short  i = 0 ;//����ѭ������
      byte m = 0, n = 0;
      byte j=0;//��תѭ������
      int ju = 0; //��ʵ����Ҫ��ʼ��
      if (e.target == shurudingshi) {
         //������صĶ�ʽ��ȷ����ʽ���д�����ض�ʽ
         //Ӧ���������붨ʽ����ȷ����ʼ���档
         //���ư�ť�Ŀ������ʵ��
         work = root;//root��ԭ�ж�ʽ���ĸ���
         byte xuan=0;
         byte fuan=0,t=0;
         for (i = 1; i <= go.shoushu; i++) {
            m = go.hui[i][25];
            n = go.hui[i][26];
            if(xuan==0){
               if(n<10){
                  xuan=1;
                  if(m>10) xuan+=1;
               }
               else if(n>10){
                  xuan=3;
                  if(m<10) xuan+=1;
               }
            }//ȷ��xuan!=0;ֻ�ڵ�һ��ִ��
            for(j=xuan;j>1;j--){
               t=n;
               n=(byte)(20-m);
               m=t;
            }
            if(fuan==0){//��Ҫ��һ������
              if(m<n) fuan=1;
              else if(m>n) fuan=2;
            }
            if(fuan==2){
               t=m;
               m=n;
               n=t;
            }

            if (xds == true) {
               work.left = new DsNode(m, n);
               work = work.left;
            }
            else { //work����һ�ֵ��ϴ�
               for (temp1 = work; temp1 != null; temp = temp1,
                    temp1 = temp1.right) {
                  if (temp1.zba == m && temp1.zbb == n) {
                     work = temp1.left;
                     break;
                  }
               }
               if (temp1 == null) {
                  System.out.print("�ó�ʼ�仯��δ��Ϊ��ʽ�����ڼ���");
                  temp.right = work = new DsNode(m, n);
                  xds = true;
               }
            }
         }//��ʽ����ʽֻ�����볣�������ʽ���������ͨ���仯���롣
         //����ͨ����ʽ��������Ϻã���Ϊ����û�����š�
         if(xds==false){
            System.out.print("�ö�ʽ�Ѿ�����");
            WCDS=true;//�����жϺ�ʱ�����ļ���ť��
            go=new GoBoardLian1();
            repaint();
            return true;
         }
         try {
           out = new DataOutputStream(
              new BufferedOutputStream(
              new FileOutputStream("H:\\weiqidata\\" + "��ʽ��")));
           write(out, root);
           out.close();
        }
        catch (IOException ex) {
           System.out.println("the output meet some trouble!");
           System.out.println("Exception" + ex.toString());
        }
        shurudingshi.disable();
        WCDS=true;
        go=new GoBoardLian1();
            repaint();
          return true;
      }

      if (e.target == pass) {
         go.shoushu++;
         return true;
      }

      if (e.target == wenjian) {


         try {
            DataInputStream fnin = new DataInputStream(
               new BufferedInputStream(
               new FileInputStream("H:\\weiqidata\\" + "qipushu")));
            ju = fnin.readInt();
            System.out.println("ju=" + ju);
            ju++;
            fnin.close();
            DataOutputStream fnout = new DataOutputStream(
               new BufferedOutputStream(
               new FileOutputStream("H:\\weiqidata\\" + "qipushu")));
            fnout.writeInt(ju);
            fnout.close();
         }
         catch (IOException ex) {
            System.out.println("the reading of file  meet some trouble!");
            System.out.println("Exception" + ex.toString());
         }
         //����仯�󣬽������ԭ�ж�ʽ��ϵ�����޸Ķ�ʽ���ļ���
         //���������Ƕ�ʽ���У����ֵı仯���붨ʽ�����ܵı仯��
         //���������仯��ȫ�����롰��ʽ���������ļ������ڵ�һ��
         //��֧�㴦ָ���ļ����ơ��仯���ӵ�һ����֧����ʼ��
         //����ı仯�����ƭ�ţ���ƭ�ſ�ʼ��ָ���ļ����ƣ�ƭ�ű�
         //���ӷ�֧����ʼ;
         //��ʽ��ı仯ֱ�Ӵ���һ�ֿ�ʼ�洢������Ҫ�������ֲ�ͬ��
         //��ʼ���Ͼ�˫�����������ֶΡ�
         //���ֲ��Ľ���洢ΪSGF�ļ�����Ҫʱ��̬���롣

         fuzhi1(rootb);
         fuzhi2(rootb);//����ZZYG�������йأ��ĸ�ֵ��

         if(rootb.xingshi1!=0&&rootb.xingshi2!=0){
            //ƭ��
            //��Ϊrootb��rootc��ֱ�߱仯�������ǵȼ۵�
            rootb.ju=ju;
            System.out.println("�ò���ƭ�ţ�("+rootb.zba
                               +","+rootb.zbb+")");
         }
         else{//������Ϊ��ʽ���ܴﵽ���֡�
            rootb.ju=ju;  //�µı仯Ҳ��rootb��ʼ
            //����±仯�ĳ�ʼ�仯���������Ķ�ʽ�ڵ㣬�����
            //�仯���ļ���
            System.out.println("�ò�����Ϊ��ʽ��("+rootb.zba
                             +","+rootb.zbb+")");
            if(rootb.ZZYG==true){
               System.out.println("�ò��������й�");
            }

         }

         try {
            out2 = new DataOutputStream(
               new BufferedOutputStream(
               new FileOutputStream("H:\\weiqidata\\" + "qipu" + ju)));

            write(out2, rootb.left);
            out2.close();
         }
         catch (IOException ex) {
            System.out.println("the output meet some trouble!");
            System.out.println("Exception" + ex.toString());
         }
         if(rootb.xingshi1==0||rootb.xingshi2==0){
         //д�붨ʽ����ֻд�����ֵı仯��ɾ��������ı仯
            delete(rootb);
            try {
               out = new DataOutputStream(
               new BufferedOutputStream(
               new FileOutputStream("H:\\weiqidata\\" + "��ʽ��")));
               write(out, root);
               out.close();
            }
            catch (IOException ex) {
               System.out.println("the output meet some trouble!");
               System.out.println("Exception" + ex.toString());
            }
         }
         wenjian.disable();
      }

      if (e.target == conglai) {
         while (go.shoushu != linss) {
            go.clhuiqi();
         }
         conglai.disable();
         huiqi.disable();
         store.disable();
         repaint();
         return true;
      }

      if (e.target == huiqi) {
         if (go.shoushu > linss) {
            CHONGHUI = true;
            fhuiqi = true;
            go.clhuiqi();
            if (go.shoushu == linss) {
               huiqi.disable();
               conglai.disable();
               store.disable();
            }
            repaint();
         }
         else {
            System.out.println("this is original ju mian");
            //huiqi.disable();
         }
         return true;
      }


      if (e.target == store) {
         System.out.println("come into method store!");
         if (rootb == null) { //�洢ԭʼ����ʱ�Ĵ���
            System.out.print("�洢��ʼ�仯");
            linss = go.shoushu; //ȷ����ʼ�����������
            work = root;
            xds=false;//���������µ��ŷ�
            for (i = 1; i <= go.shoushu; i++) {
               m = go.hui[i][25];
               n = go.hui[i][26];
               if(work==null){
                  xds=true;
                  rootb=work=temp1.left =new DsNode(m, n);
                  System.out.print("ԭ��ʽ�ļ����ݱ䣬�����о�");
               }//����work!=null
               if (xds == true) {//work����һ��
                  work.left = new DsNode(m, n);
                  work = work.left;
               }
               else { //work����һ�֣�����ǰ�֣����ϴ�
                  for (temp1 = work; temp1 != null; temp = temp1,
                       temp1 = temp1.right) {
                     if (temp1.zba == m && temp1.zbb == n) {
                        work = temp1.left;
                        break;
                     }
                  }
                  if (temp1 == null) {
                     System.out.print("���ŷ�ԭ�Ƕ�ʽ�������о�");
                     rootb=temp.right = work = new DsNode(m, n);
                     xds = true;//rootb�����ŷ���qipu�д洢�Ժ���ŷ�
                     //Ҳ�ظ��������ŷ���������֤��
                  }
               }

            }
            if(xds==false){//���󱨸�
               System.out.print("�ó�ʼ�仯�Ѿ��ڶ�ʽ��");
               showStatus("�ó�ʼ�仯�Ѿ��ڶ�ʽ��");
               //todo:����仯�ļ���
               go=new GoBoardLian1();
               repaint();
            }
            rootc=work;//���з������һ�֣����ܺ�rootb��ͬ
            pass.disable();
         }
         else {
            System.out.print("�洢��̱仯");
            work=rootc;
            xds=false;
            if(rootc.left ==null){//�����Ѿ�����仯��
               xds=true;
            }
            else{
               work=rootc.left;
            }
            for (i = 1; i <= go.shoushu - linss; i++) {
               m = go.hui[i + linss][25];
               n = go.hui[i + linss][26];
               if(work==null){//�±仯���·�֧��
                  xds=true;
                  work=temp1.left =new DsNode(m, n);
                  System.out.print("ԭ��ʽ�ļ����ݱ䣬�����о�");
               }

               if (xds == true) {
                  work.left = new DsNode(m, n);
                  work = work.left;
               }
               else {
                  for (temp1 = work; temp1 != null; temp = temp1,
                       temp1 = temp1.right) {
                     if (temp1.zba == m && temp1.zbb == n) {
                        work = temp1.left;
                        break;

                     }
                  }
                  if (temp1 == null) {
                     System.out.print("�ñ�������ֲ棡���ڼ���");
                     temp.right = work = new DsNode(m, n);
                     xds = true;
                  }
               }
            }

            if (xds == true) {
               work.xingshi1 = (byte) Integer.parseInt(xingshi1.getText());
               work.xingshi2 = (byte) Integer.parseInt(xingshi2.getText());
               xingshi1.setText("0");
               xingshi2.setText("0");
            }
            else {
               System.out.println("�ñ仯�Ѿ�����");
               showStatus("�ñ仯�Ѿ�����");
            }
         }
         store.disable();
         wenjian.enable();
         repaint();

      }

      return true;
   }

   void write(DataOutputStream out, DsNode root) {

      DsNode temp;
      DsNode temp1;
      if (root == null) {
         return;
      }
      temp = root.right; //root�Ǳ�ʾ���Ķ������ĸ��ڵ�
      System.out.println("�ֲ�root=(" + root.zba + "," + root.zbb + ")");
      if (temp == null) { //û���ֵܣ���ʾû�зֲ�
         try {
            out.writeByte(root.xianhoushou);
            out.writeByte(root.zba);
            out.writeByte(root.zbb);
            out.writeByte(root.xingshi1);
            out.writeByte(root.xingshi2);
         }
         catch (IOException ex) {
            System.out.println("the reading of file  meet some trouble!");
            System.out.println("Exception" + ex.toString());
         }
         write(out, root.left);
         return;
      }
      else {

         try {
            out.writeByte(ZKUOHAO);
            out.writeByte(root.xianhoushou);
            out.writeByte(root.zba);
            out.writeByte(root.zbb);
            out.writeByte(root.xingshi1);
            out.writeByte(root.xingshi2);

         }
         catch (IOException ex) {
            System.out.println("the reading of file  meet some trouble!");
            System.out.println("Exception" + ex.toString());
         }

         write(out, root.left);

         try {

            out.writeByte(YKUOHAO);
         }
         catch (IOException ex) {
            System.out.println("the reading of file  meet some trouble!");
            System.out.println("Exception" + ex.toString());
         }

         for (temp1 = root; temp != null; temp1 = temp, temp = temp.right) {
            try {
               out.writeByte(ZKUOHAO);
               out.writeByte(temp.xianhoushou);
               out.writeByte(temp.zba);
               out.writeByte(temp.zbb);
               out.writeByte(temp.xingshi1);
               out.writeByte(temp.xingshi2);

            }
            catch (IOException ex) {
               System.out.println("the reading of file  meet some trouble!");
               System.out.println("Exception" + ex.toString());
            }

            write(out, temp.left);

            try {
               out.writeByte(YKUOHAO);
            }
            catch (IOException ex) {
               System.out.println("the reading of file  meet some trouble!");
               System.out.println("Exception" + ex.toString());
            }
         } //for
      } //else
   } //method

   public void xz(Graphics g, int a, int b) { //12.1
      g.setColor(Color.orange); //deal with image only.
      g.fillOval(28 * a + 4, 28 * b + 4, 28, 28);
      g.setColor(Color.black);
      if (a == 0) {
         g.drawLine(18, 28 * b + 18, 32, 28 * b + 18);
      }
      else if (a == 18) {
         g.drawLine(28 * a + 4, 28 * b + 18, 28 * a + 18, 28 * b + 18);
      }
      else {
         g.drawLine(28 * a + 4, 28 * b + 18, 28 * a + 32, 28 * b + 18);
      }
      if (b == 0) {
         g.drawLine(28 * a + 18, 18, 28 * a + 18, 32);
      }
      else if (b == 18) {
         g.drawLine(28 * a + 18, 28 * b + 4, 28 * a + 18, 28 * b + 18);
      }
      else {
         g.drawLine(28 * a + 18, 28 * b + 4, 28 * a + 18, 28 * b + 32);
      }
      if ( (a - 3) % 6 == 0 && (b - 3) % 6 == 0) {
         g.fillOval(28 * a + 15, 28 * b + 15, 6, 6);
      } //draw the star point if necessary.
      //zb[a][b][0]=0;
      //zb[a][b][3]=0;
      //zb[a][b][2]=0;
   }

}


/*int k, h;
      String str;
      DsNode temp = null, old = null;
      DsNode work = null;
      h = bianhua[0][0][1]; // bian hua de shu mu,bu bao kuo zheng jie
      for (i = 1; i <= h; i++) {
        p = bianhua[i][0][0]; //bian hua de shou shu
        temp = root;
        old = null;
        for (j = 1; j <= p; j++) {
          //str=s[i][j];
          m = bianhua[i][j][0];
          n = bianhua[i][j][1];
          if (temp == null) {
            if (old != null) {
              temp = new TreeNode(m, n, str);
              System.out.println("cha ru di yi ge bianhua");
              System.out.println("a=" + temp.zba + ",b=" + temp.zbb);
              System.out.println("str=" + str);
              old.left = temp;
              temp.father = old;
              old = temp;
              temp = temp.left;
            }
            else {
              root = new TreeNode(m, n, str);
              System.out.println("cha ru gen jie dian/zhengjie");
              System.out.println("a=" + m + ",b=" + n);
              System.out.println("str=" + str);
              old = root;
              temp = root.left;
            }
          }
          else if (temp.zba == m && temp.zbb == n) {
            temp = temp.left;
          }
          else {
            work = temp.right;
            while (work != null) {
              if (work.zba == m && work.zbb == n) {
                break;
              }
              else {
                temp = work;
                work = work.right;
              }
            }
            if (work == null) {
              work = new TreeNode(m, n, str);
              System.out.println("cha ru fei zheng jie");
              System.out.println("a=" + m + ",b=" + n);
              //System.out.println("b="+n);
              System.out.println("str=" + str);
              temp.right = work;
              work.father = temp;
            }
            old = work;
            temp = work.left;
          }
        } //for1
      } //for2,bian hua shu
 */
/*int k, h;
   String str;
   DsNode temp = null, old = null;
   DsNode work = null;
   h = bianhua[0][0][1]; // bian hua de shu mu,bu bao kuo zheng jie
   for (i = 1; i <= h; i++) {
     p = bianhua[i][0][0]; //bian hua de shou shu
     temp = root;
     old = null;
     for (j = 1; j <= p; j++) {
       //str=s[i][j];
       m = bianhua[i][j][0];
       n = bianhua[i][j][1];
       if (temp == null) {
         if (old != null) {
           temp = new TreeNode(m, n, str);
           System.out.println("cha ru di yi ge bianhua");
           System.out.println("a=" + temp.zba + ",b=" + temp.zbb);
           System.out.println("str=" + str);
           old.left = temp;
           temp.father = old;
           old = temp;
           temp = temp.left;
         }
         else {
           root = new TreeNode(m, n, str);
           System.out.println("cha ru gen jie dian/zhengjie");
           System.out.println("a=" + m + ",b=" + n);
           System.out.println("str=" + str);
           old = root;
           temp = root.left;
         }
       }
       else if (temp.zba == m && temp.zbb == n) {
         temp = temp.left;
       }
       else {
         work = temp.right;
         while (work != null) {
           if (work.zba == m && work.zbb == n) {
             break;
           }
           else {
             temp = work;
             work = work.right;
           }
         }
         if (work == null) {
           work = new TreeNode(m, n, str);
           System.out.println("cha ru fei zheng jie");
           System.out.println("a=" + m + ",b=" + n);
           //System.out.println("b="+n);
           System.out.println("str=" + str);
           temp.right = work;
           work.father = temp;
         }
         old = work;
         temp = work.left;
       }
     } //for1
   } //for2,bian hua shu
 */
