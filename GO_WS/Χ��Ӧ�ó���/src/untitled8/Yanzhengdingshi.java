package untitled8;

import java.awt.*;
import java.applet.Applet;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import untitled8.*;
import untitled3.DsNode;
public class Yanzhengdingshi
   extends Applet {//���ݶ�ʽ���д洢�ı仯���˶Կ�
   //�ܹ���ʾ���洢�Ķ�ʽ���Ӷ���֤��ʽ����ȷ���
   //���Խ�һ��������չ�ɸ��ݶ�ʽ�ӱ仯�ļ����ж�Ӧ
   //�����˹����ܵĿ�����·���֡�
   final static byte ZKUOHAO = -127;
   final static byte YKUOHAO = 127;
   GoBoardLian1 go;
   DsNode root, temp, work, temp1, temp2;
   boolean YANSHI;//��ʾ����֤��ʽ��
   boolean KEXIA = true;
   boolean CHONGHUI = true; //�Ƿ��ػ档
    //Button shurudingshi = new Button("���붨ʽ�ļ�");
   //DataInputStream in; //��ʽ�������ļ�
   //DataOutputStream out; //��ʽ������ļ���
   Button jiaohuan=new Button("�����Ⱥ���");
   //�������ֺη����ߣ�Ĭ��Ϊ�˷��ȣ�ͨ����ť��Ϊ�����ȣ�
   //�Զ��ָ�����ʼ���档
   Button huifu = new Button("�ָ�ԭʼ����");
   Button huiqi = new Button("����");
   //���ӵ����ض������������������֤�������ض��仯��
   Button tedingjm = new Button("����ָ������");
   Button tedingjmjs=new Button("�Ѿ�����ָ������");
   // �˺���Ϊ�˷��ߡ�
   DsNode[] stackds = new DsNode[25]; //SGF��ʽ��

   byte xuan=0;//���޵���ת����
   byte fuan=0;//�Ƿ���Ҫ��ת��

   public void init() { //��ʼ��,��ɽ���
      //���ļ����ɶ�ʽ����
      byte fenzhi = 0; //SGF��ʽ��

      setLayout(null);
      add(jiaohuan);
      add(huifu);
      add(huiqi);
      add(tedingjm);
      add(tedingjmjs);

      //add(shurudingshi);
      jiaohuan.reshape(575, 89, 100, 26);
      huifu.reshape(575, 115, 100, 26);
      huiqi.reshape(575, 141, 100, 26);
      tedingjm.reshape(575,176,100,26);
      tedingjm.reshape(575,202,100,26);
      //shurudingshi.reshape(575, 167, 100, 26);
      huiqi.disable();//��������ť��ʼʱ������
      huifu.disable();
      //shurudingshi.disable();

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
      work=root;//����
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
      byte j=0,t=0;
      byte m=0,n=0;
      if (KEXIA == true) {
         KEXIA=false;//ֻ�л������һ��,���ܼ���.
         byte a = (byte) ( (x - 4) / 28 + 1); //����������ӵ�.
         byte b = (byte) ( (y - 4) / 28 + 1);
         go.cgcl(a, b);
         //go.output();
         if (go.shoushu > 0) {
            huiqi.enable();
            huifu.enable();
         }
         m=a;
         n=b;
         if (xuan == 0) {
            if (n < 10) {
               xuan = 1;
               if (m > 10)
                  xuan += 1;
            }
            else if (n > 10) {
               xuan = 3;
               if (m < 10)
                  xuan += 1;
            }
         } //ȷ��xuan!=0;ֻ�ڵ�һ��ִ�У�����Ϊ�Ӿ����ꡣ
         //����Ϊ1������Ϊ2������Ϊ3������Ϊ4;xuan �Ķ���
         for (j = xuan; j > 1; j--) {
            t = n;
            n = (byte) (20 - m);
            m = t;
         }//�������Ͻǵı�׼λ�ã�����ƥ�䡣
         if (fuan == 0) { //��Ҫ��һ��������
            if (m < n)
               fuan = 1;//���跴ת��
            else if (m > n)
               fuan = 2;//��Ҫ��ת��
         }
         if (fuan == 2) {
            t = m;
            m = n;
            n = t;
         }
         System.out.println("����൱�ڣ�"+m+","+n);
         if(work==null){
            System.out.println("��ʽ�иñ仯���˽�����");
            showStatus("��ʽ�иñ仯���˽�����");
            //KEXIA=true;//��Ҫ�ûָ�����尴ťʹ�����ֵ��
            return true;
         }
         a=m;//��ʱa��b������ʱ���������ڱ�׼�͵�ƥ�䣻
         b=n;
         for(temp=work;temp!=null;temp=temp.right ){
            if(a==temp.zba&&b==temp.zbb){
               work=temp.left;
               break;
            }
         }
         if(temp==null){
            System.out.println("��ʽ��û�иñ仯��");
            showStatus("��ʽ��û�иñ仯��");
            go.clhuiqi() ;
            KEXIA=true;
            return true;
         }
         if(work==null){
            System.out.println("��ʽ�иñ仯���˽�����");
            showStatus("��ʽ�иñ仯���˽�����");
         }

         else{
            byte min=127;
            for(temp=work;temp!=null;temp=temp.right){
               if(temp.blbz <min) {
                  min=temp.blbz;
                  work=temp;
               }
            }
            work.blbz+=1;
            m=work.zba;
            n=work.zbb;
            System.out.println("ѡ���Ӧ�Ա仯Ϊ��"+m+","+n);
            if (fuan == 2) {//����׼�͵�Ӧ�Ա任Ϊ��ʵ�����ꡣ
               t = m;
               m = n;
               n = t;
            }//�����˳�����Ҫ

            for (j = xuan; j > 1; j--) {//�任��ʵ�ڵ�����
               t = m;
               m = (byte) (20 - n);
               n = t;
            }


            go.cgcl(m,n) ;//go���õ���������һ�����
            work=work.left;
         }

         CHONGHUI = false; //Ч�������ӱ�����ʮ��
         repaint();
         System.out.println("mousedown");
         KEXIA=true;
         return true; //����������,��Frame����
      }
      else {
         return true;
      }
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
      byte t=0;
      int ju = 0; //��ʵ����Ҫ��ʼ��
      if (e.target == jiaohuan) {
         KEXIA=false;
         go = new GoBoardLian1();
         byte min=127;
         work = root;
         for (temp = work; temp != null; temp = temp.right) {
            if (temp.blbz < min) {
               min = temp.blbz;
               work = temp;

            }
         }
         work.blbz += 1;
         m=work.zba;
         n=work.zbb;
         //fuan=1;
         if (xuan == 0) {
            if (n < 10) {
               xuan = 1;
               if (m > 10)
                  xuan += 1;
            }
            else if (n > 10) {
               xuan = 3;
               if (m < 10)
                  xuan += 1;
            }
         } //ȷ��xuan!=0;ֻ�ڵ�һ��ִ��
         for (j = xuan; j > 1; j--) {
            t = n;
            n = (byte) (20 - m);
            m = t;
         }
         if (fuan == 0) { //��Ҫ��һ������
            if (m < n)
               fuan = 1;
            else if (m > n)
               fuan = 2;
         }
         if (fuan == 2) {
            t = m;
            m = n;
            n = t;
         }

         go.cgcl(work.zba, work.zbb);
         work = work.left;

         // go.cgcl(root.zba,root.zbb);
         //work=root.left ;
         KEXIA=true;
         repaint();
      }

      if (e.target == huiqi) {//Ӧ�ýڵ�Ҳ��Ӧ����
         if (go.shoushu > 0) {//�Գƹҽ�û���ָ���ֻ��ͬ��ҽǡ�
            CHONGHUI = true;
            go.clhuiqi();
            if (go.shoushu == 0) {
               huiqi.disable();
               //store.disable();
            }
            repaint();
         }
         else {
            System.out.println("this is original ju mian");

         }
         return true;
      }
      if (e.target == huifu) { //�ָ���ԭʼ����
         KEXIA=true;
         go = new GoBoardLian1();
         xuan=0;
         fuan=0;
         work=root;
         repaint();
      }
      return true;
   }
} //class
