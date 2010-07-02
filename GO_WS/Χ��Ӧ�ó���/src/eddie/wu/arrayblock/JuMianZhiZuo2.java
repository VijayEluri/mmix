package eddie.wu.arrayblock;

import java.awt.Frame;
import java.awt.Event;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class JuMianZhiZuo2
   extends Frame {

   //private int id;
   //GoBoard����Ҫȱ��������������ƣ�ֻ��128�顣�������ڼ����������㹻�ˡ�
   //�������ĺô������ڸ������ݣ�
   GoApplet goapplet = new GoApplet();
   Button shuangfanglunxia = new Button("˫������");
   Button sihuojisuan = new Button("�������");
   //Button baocunqipu = new Button("��������");
   Button renjiduixia = new Button("�˻�����");
   Button baichujumian = new Button("�ڳ�����");
   Button baocunjumian = new Button("�������");
   Button zairujumian = new Button("�������");
   Label zuobiao = new Label("�����������������");
   TextField zuobiaoa = new TextField();
   TextField zuobiaob = new TextField();

   boolean SHUANGFANGLUNXIA = true;
   boolean RENJIDUIXIA;
   boolean BAICHUJUMIAN;

   public JuMianZhiZuo2() {
      shuangfanglunxia.addActionListener(new LunxiaActionListener());
      renjiduixia.addActionListener(new DuixiaActionListener());
      baichujumian.addActionListener(new JumianActionListener());
      sihuojisuan.addActionListener(new SihuoActionListener());
      zairujumian.addActionListener(new ZairujumianActionListener(this));
      baocunjumian.addActionListener(new BaocunjumianActionListener(this));
      add(goapplet);
      add(shuangfanglunxia);
      add(sihuojisuan);
      add(renjiduixia);
      add(baichujumian);
      add(baocunjumian);
      add(zairujumian);
      add(zuobiao);
      add(zuobiaoa);
      add(zuobiaob);

      goapplet.setVisible(true);
      shuangfanglunxia.setVisible(true);
      sihuojisuan.setVisible(true);
      renjiduixia.setVisible(true);
      baichujumian.setVisible(true);
      zairujumian.setVisible(true);
      setLayout(null);
      goapplet.setBounds(30, 30, 560, 560);
      shuangfanglunxia.setBounds(600, 100, 100, 30);
      renjiduixia.setBounds(600, 130, 100, 30);
      baichujumian.setBounds(600, 160, 100, 30);
      zairujumian.setBounds(700, 100, 100, 30);
      sihuojisuan.setBounds(700, 130, 100, 30);
      baocunjumian.setBounds(700, 160, 100, 30);

      zuobiao.setBounds(600, 190, 150, 30);
      zuobiaoa.setBounds(600, 220, 100, 30);
      zuobiaob.setBounds(700, 220, 100, 30);
      addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent event) {
            dispose();
            System.exit(0);
         }

      });
   }

   public boolean mouseDown(Event e, int x, int y) { //�����������
      System.out.print("chuanbodaorongqi");
      if (SHUANGFANGLUNXIA == true) {
         byte a = (byte) ( (x - 4) / 28 + 1); //����������ӵ�.
         byte b = (byte) ( (y - 4) / 28 + 1);
         goapplet.KEXIA = true;
      }
      if (RENJIDUIXIA == true) {
         //zuochuyingdui
      }
      if (BAICHUJUMIAN == true) {
         goapplet.goboard.qiquan();
         goapplet.KEXIA = true;
      }
      return true;
   }

   public static void main(String[] args) {
      JuMianZhiZuo weiqi = new JuMianZhiZuo();

      weiqi.setVisible(true);
   }

   class LunxiaActionListener
      implements ActionListener {

      public void actionPerformed(ActionEvent e) {
         //��������¿��֡�
         goapplet.goboard = new GoBoard();
         goapplet.KEXIA = true;

         SHUANGFANGLUNXIA = true;
         BAICHUJUMIAN = false;
         RENJIDUIXIA = false;
         System.out.println("SHUANGFANGLUNXIA=" + SHUANGFANGLUNXIA);
      }

   }

   class SihuoActionListener
      implements ActionListener {

      public void actionPerformed(ActionEvent e) {
         //��������
         byte i, j, k;
         byte houxuan[][] = new byte[40][2];
         houxuan = goapplet.goboard.houxuandian(
            (byte) Integer.parseInt(zuobiaoa.getText()),
            (byte) Integer.parseInt(zuobiaob.getText()));
         GoBoard[] go = new GoBoard[100000];
         Stack stack = new Stack();
         stack.add(go[0]);
         while (!stack.isEmpty()) {
            //for(i=1;i<=

         }
         //ÿ��10�ֱ仯���㣬�����10��(��Ҫ���ڴ�����)��
         //��������һ��ÿ�ζ����ȼ���õ㡣
         //���������Ļ���ֻ�����ҵ���Ѳ��輴�ɡ�
         //����������������֤���е㣬������Ȩ�����½���
         //��������ɱ�壬ֻ�����⼴��
         //���������ܳɹ�����Ҫȫ����֤��
         go[0] = goapplet.goboard;
         System.out.println("�������Ľ��Ϊ��");
      }

   }

   class DuixiaActionListener
      implements ActionListener {

      public void actionPerformed(ActionEvent e) {
         goapplet.goboard = new GoBoard();
         goapplet.KEXIA = true;
         SHUANGFANGLUNXIA = false;
         BAICHUJUMIAN = false;
         RENJIDUIXIA = true;
         System.out.println("RENJIDUIXIA=" + RENJIDUIXIA);

      }

   }

   class JumianActionListener
      implements ActionListener {

      public void actionPerformed(ActionEvent e) {
         if (BAICHUJUMIAN == true) {
            goapplet.goboard.qiquan();
         }
         else {
            goapplet.goboard = new GoBoard();
            goapplet.KEXIA = true;

            SHUANGFANGLUNXIA = false;
            BAICHUJUMIAN = true;
            RENJIDUIXIA = false;
         }

         System.out.println("BAICHUJUMIAN=" + BAICHUJUMIAN);
      }
   }

   class ZairujumianActionListener
      implements ActionListener { //������档
      Frame parent;
      public ZairujumianActionListener(Frame par) {
         parent = par;
      }

      public void actionPerformed(ActionEvent e) {
         //�������
         FileDialog fd = new FileDialog(
            parent, "��������λ��", FileDialog.LOAD);
         fd.setFile("1.wjm");
         fd.setDirectory(".");
         fd.show();

         String inname = fd.getFile();
         String dir = fd.getDirectory();

         try {
            DataInputStream in = new DataInputStream(
               new BufferedInputStream(
               new FileInputStream(
               dir + inname)));

            goapplet.goboard.zairujumian(in);
            goapplet.goboard.shengchengjumian();
            in.close();
         }

         catch (IOException ex) {
            System.out.println("the input meet some trouble!");
            System.out.println("Exception" + ex.toString());
         }

         System.out.println("�������");

      }
   }

   class BaocunjumianActionListener
      implements ActionListener {
      Frame parent;
      public BaocunjumianActionListener(Frame par) {
         parent = par;
      }

      public void actionPerformed(ActionEvent e) {
         if (BAICHUJUMIAN == true) { //�������
            FileDialog fd = new FileDialog(
               parent, "��������λ��", FileDialog.SAVE);
            fd.setFile("1.wjm");
            fd.setDirectory(".");
            fd.show();

            String outname = fd.getFile();
            String dir = fd.getDirectory();
            System.out.print(outname);
            try {
               DataOutputStream out = new DataOutputStream(
                  new BufferedOutputStream(
                  new FileOutputStream(
                  dir + outname)));
               //�����ǰ�ȫ���Ե����ƣ���Ȼ�޷�д���ļ��У��ֽ�Ϊʼ��Ϊ0kb��
               //�������������������ġ�3��7�ա���ȫ���󣬴��������⣬û�йر����
               //��������
               System.out.print(out);

               goapplet.goboard.shuchujumian(out);
               out.close();
            }

            catch (IOException ex) {
               System.out.println("the output meet some trouble!");
               System.out.println("Exception" + ex.toString());
            }

            System.out.println("�������");

         }
         else { //��������
            FileDialog fd = new FileDialog(
               parent, "�������׵�λ��", FileDialog.SAVE);
            fd.setFile("1.wjm");
            fd.setDirectory(".");
            fd.show();

            String outname = fd.getFile();
            String dir = fd.getDirectory();
            System.out.print(outname);
            try {
               DataOutputStream out = new DataOutputStream(
                  new BufferedOutputStream(
                  new FileOutputStream(
                  dir + outname)));
               //�����ǰ�ȫ���Ե����ƣ���Ȼ�޷�д���ļ��У��ֽ�Ϊʼ��Ϊ0kb��
               //�������������������ġ�3��7�ա���ȫ���󣬴��������⣬û�йر����
               //��������
               // System.out.print(out);

               goapplet.goboard.shuchuqipu(out);
               out.close();
            }

            catch (IOException ex) {
               System.out.println("the output meet some trouble!");
               System.out.println("Exception" + ex.toString());
            }

            System.out.println("��������");

         }

      }
   }

}