package untitled8;

import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import untitled3.DsNode;

public class Zhizuodingshi
   extends Applet {//����ͨ������������붨ʽ
   //��������ڶ�ʽ���ļ��С�ר�������鱾��ר�����ֽ���Ķ�ʽ��
   //�ϸ��ӵĶ�ʽ��Ҫͨ��zhizuobianhua���룬��Ϊ�漰�������Ƿ�����
   //�ٲ�Ҳ������Ҫ���ǵ����ء�����Ľٲĺ������Ƿ�����ȵȡ�
   //�����඼�б��涨ʽ�����ļ��Ĵ���ʹ��ļ����ɶ�ʽ�������Ƿ�װ��
   //ĳ�����С�
   final static byte ZKUOHAO = -127;
   final static byte YKUOHAO = 127;
   GoBoardLian1 go;
   DsNode root, temp, work, temp1, temp2;
   boolean YANSHI;//��ʾ����֤��ʽ��
   boolean KEXIA = true;
   boolean CHONGHUI = true; //�Ƿ��ػ档

   DataInputStream in; //��ʽ�������ļ�
   DataOutputStream out; //��ʽ������ļ���
   Button yanshi =new Button("��ʾ��ʽ");
   Button store = new Button("���涨ʽ");
   Button huiqi = new Button("����");
   Button shurudingshi = new Button("���붨ʽ�ļ�");
   DsNode[] stackds = new DsNode[25]; //SGF��ʽ��


   public void init() { //��ʼ��,��ɽ���
      byte fenzhi = 0; //SGF��ʽ��,��stackds���ɶ�ջ

      setLayout(null);
      add(yanshi);
      add(store);
      add(huiqi);
      add(shurudingshi);
      yanshi.reshape(575, 89, 100, 26);
      store.reshape(575, 115, 100, 26);
      huiqi.reshape(575, 141, 100, 26);
      shurudingshi.reshape(575, 167, 100, 26);
      huiqi.disable();
      store.disable();
      shurudingshi.disable();

      go = new GoBoardLian1();
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
            System.out.println("��ʽ���ĸ�Ϊ��(a="+
                               work.zba+","+work.zbb+")" );
            //�������������Ű�Χ
            //���Ž���ʾ�������Ǹ��ڵ�ķ�֧��
            fenzhi++;
            stackds[fenzhi] = work; //�����Ž������µ������Žӵ�work�ϡ�
            System.out.println("��һ�����ŵĹ�����Ϊ��(a="+
                stackds[fenzhi].zba+","+stackds[fenzhi].zbb+")" );
         }
         else { //��ʽ����ʼʱ����ֻ��һ����㡣
            in.read(dsnode, 1, 4);
            root = work = new DsNode(dsnode[1], dsnode[2], dsnode[3],
                                     dsnode[4], lina);
            System.out.println("��ʽ���ĸ�Ϊ��(a="+
                               work.zba+","+work.zbb+")" );
         }
         while (in.available() != 0) {
            lina = in.readByte();
            if (lina == ZKUOHAO) {
               in.read(dsnode);
               temp = new DsNode(dsnode[1], dsnode[2], dsnode[3],
                                 dsnode[4], dsnode[0]);
               System.out.println("��֧�仯���׽ڵ�Ϊ:"+ temp.zba
                                  +","+temp.zbb+")");
               if (ykhjs == false) {
                  //�µ�������
                  work.left = temp;
                  fenzhi++;
                  work = temp;
                  stackds[fenzhi] = work;
                  System.out.println("��һ�����ŵĹ�����Ϊ��(a="+
                stackds[fenzhi].zba+","+stackds[fenzhi].zbb+")" );
               }
               else { //�����Ž��������е�������
                  //�����µ������ţ�ykhjsʧЧ��
                  ykhjs=false;
                  work.right = temp;
                  System.out.println("ͬһ�����ŵ�ԭ������Ϊ��(a="+
                stackds[fenzhi].zba+","+stackds[fenzhi].zbb+")" );
                  work = temp;
                  stackds[fenzhi] = work;
                  System.out.println("ͬһ���µ������ŵĹ�����Ϊ��(a="+
                stackds[fenzhi].zba+","+stackds[fenzhi].zbb+")" );
               }
            }
            else if (lina == YKUOHAO) {
               if (ykhjs == false) {
                  ykhjs = true;
                  work = stackds[fenzhi];
                  System.out.println(work.toString() );
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

   }

   public void paint(Graphics g) {
      System.out.println("���뷽��paint()");
      if (CHONGHUI == true) {
         g.setColor(Color.orange);
         g.fillRect(0, 0, 560, 560);
      }
      else {
         CHONGHUI = true;
      } //�����ػ�����CHONGHUI��false
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
            if(YANSHI==true){
               YANSHI=false;
               g.setColor(Color.green);
               for(i=1;i<=go.shoushu;i++){
                  g.drawString("" + i, 28 *(go.hui[i][25]) - 4,
                               28 *(go.hui[i][26]) - 4);
               }
            }
            else{
               kinp = go.zb[i][j][go.KSYXB];
               if (kinp != 0) { //������
                  g.setColor(Color.green);
                  g.drawString("" + kinp, 28 * i - 4, 28 * j - 4);

               }
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
         if (go.shoushu > 0) {
            huiqi.enable();
            store.enable();
            //shurudingshi.enable();
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
         return root.xingshi2;
      }
      temp = root.left;

      for (; temp != null; temp = temp.right) {
         linv = temp.xingshi2 = fuzhi2(temp);
         if (linv > max) {
            max = linv;
         }
      }
      root.xingshi1 = (byte) ( -max);
      return (byte) ( -max);
   } //��ʽ���漰���ӿ��ܲ�ֹһ����

   byte fuzhi3(DsNode root) { //Ϊ���ڵ�����Ƹ�ֵ
      //6��13�ա�
      DsNode temp = null;
      byte max = (byte) - 1;
      byte linv;
      if (root.left == null) { //�仯����
         return root.xianhoushou;
      }
      temp = root.left;

      for (; temp != null; temp = temp.right) {
         linv = temp.xianhoushou = fuzhi3(temp);
         if (linv > max) {
            max = linv;

         }
      }
      root.xianhoushou = (byte) ( -max);
      return (byte) ( -max);
   }

   public void update(Graphics g) { //585
      paint(g);
   }

   public boolean action(Event e, Object what) {
      short kin1 = 0, i = 0, j = 0, p = 0;
      byte m = 0, n = 0;
      byte yise = 0; //tong se
      byte tongse = 0; //yi se bei ti
      byte lins = 0;
      int ju = 0; //��ʵ����Ҫ��ʼ��
     /* if (e.target == yanshi) {
          work=root;
          while(work!=null){

             for(temp=work;temp!=null;temp=temp.right){
                if(temp.blbz ==0){
                   go.cgcl(temp.zba ,temp.zbb) ;
                   temp.blbz==1;
                }
             }
             if(temp==null){

             }

          }
      }*/
      if (e.target == shurudingshi) {
         //fuzhi3(root);

         //����仯�󣬽������ԭ�ж�ʽ��ϵ�����޸Ķ�ʽ���ļ���
         //���ֲ��Ľ���洢ΪSGF�ļ�����Ҫʱ��̬���롣
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
      }

      if (e.target == huiqi) {
         if (go.shoushu > 0) {
            CHONGHUI = true;
            go.clhuiqi();
            if (go.shoushu == 0) {
               huiqi.disable();
               store.disable();
            }
            repaint();
         }
         else {
            System.out.println("this is original ju mian");

         }
         return true;
      }
      if (e.target == store) { //���涨ʽ�����У����ָ���ԭʼ����
         //�����л��������ı�׼�������е����궼�����Ͻǡ�
         boolean xds = false; //�Ƿ��¶�ʽ��
         System.out.println("come into method store!");
         work = root;
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
             System.out.println("�����ڵ�Ϊ��"+m+","+n);

            if (root == null) {
               xds = true;
               work = root = new DsNode(m, n);
               continue;
            }//��ʽ���ļ�Ϊ�ջ򲻴��ڵ����������
            if (xds == true) {//��ʱworkΪ�ϼ��ڵ�
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
         }

         if (xds == true) {
            work.xianhoushou = -1;
         }
         else {
            System.out.println("�ñ仯�ڶ�ʽ�����Ѿ�����");
         }
         store.disable();
         shurudingshi.enable();
         huiqi.disable();
         go = new GoBoardLian1();
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

} //class
