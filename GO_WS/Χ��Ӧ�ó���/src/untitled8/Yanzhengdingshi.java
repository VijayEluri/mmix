package untitled8;

import java.awt.*;
import java.applet.Applet;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import untitled8.*;
import untitled3.DsNode;
public class Yanzhengdingshi
   extends Applet {//根据定式树中存储的变化和人对抗
   //能够显示出存储的定式，从而验证定式树正确与否。
   //可以进一步将它发展成根据定式加变化文件进行对应
   //就是人工智能的开局套路部分。
   final static byte ZKUOHAO = -127;
   final static byte YKUOHAO = 127;
   GoBoardLian1 go;
   DsNode root, temp, work, temp1, temp2;
   boolean YANSHI;//演示并验证定式。
   boolean KEXIA = true;
   boolean CHONGHUI = true; //是否重绘。
    //Button shurudingshi = new Button("存入定式文件");
   //DataInputStream in; //定式树输入文件
   //DataOutputStream out; //定式树输出文件。
   Button jiaohuan=new Button("交换先后手");
   //决定开局何方先走，默认为人方先，通过按钮改为机器先；
   //自动恢复到初始局面。
   Button huifu = new Button("恢复原始局面");
   Button huiqi = new Button("悔棋");
   //增加到达特定局面的能力，可以验证期望的特定变化。
   Button tedingjm = new Button("到达指定局面");
   Button tedingjmjs=new Button("已经到达指定局面");
   // 此后认为人方走。
   DsNode[] stackds = new DsNode[25]; //SGF格式用

   byte xuan=0;//象限的旋转参数
   byte fuan=0;//是否需要反转。

   public void init() { //初始化,完成界面
      //从文件生成定式树。
      byte fenzhi = 0; //SGF格式用

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
      huiqi.disable();//这两个按钮初始时不可用
      huifu.disable();
      //shurudingshi.disable();

      go = new GoBoardLian1();
      try {
         byte lina = 0;
         byte[] dsnode = new byte[5];
         boolean ykhjs = false; //是否已经有右括号。
         DataInputStream in = new DataInputStream(
            new BufferedInputStream(
            new FileInputStream("H:\\weiqidata\\" + "定式树")));
         if (in.available() == 0) {
            return;
         }
         lina = in.readByte();
         if (lina == ZKUOHAO) { //成型定式树应该第一步有五种以上变化。
            in.read(dsnode); //左括号不会相连。
            root = work = new DsNode(dsnode[1], dsnode[2], dsnode[3],
                                     dsnode[4], dsnode[0]);
            System.out.println("定式树的根为：(a="+
                               work.zba+","+work.zbb+")" );
            //整棵树不用括号包围
            //括号仅表示括号内是父节点的分支；
            fenzhi++;
            stackds[fenzhi] = work; //右括号结束后新的左括号接到work上。
            System.out.println("第一层括号的工作点为：(a="+
                stackds[fenzhi].zba+","+stackds[fenzhi].zbb+")" );
         }
         else { //定式树初始时可能只有一种起点。
            in.read(dsnode, 1, 4);
            root = work = new DsNode(dsnode[1], dsnode[2], dsnode[3],
                                     dsnode[4], lina);
            System.out.println("定式树的根为：(a="+
                               work.zba+","+work.zbb+")" );
         }
         while (in.available() != 0) {
            lina = in.readByte();
            if (lina == ZKUOHAO) {
               in.read(dsnode);
               temp = new DsNode(dsnode[1], dsnode[2], dsnode[3],
                                 dsnode[4], dsnode[0]);
               System.out.println("分支变化的首节点为:"+ temp.zba
                                  +","+temp.zbb+")");
               if (ykhjs == false) {
                  //新的左括号
                  work.left = temp;
                  fenzhi++;
                  work = temp;
                  stackds[fenzhi] = work;
                  System.out.println("新一层括号的工作点为：(a="+
                stackds[fenzhi].zba+","+stackds[fenzhi].zbb+")" );
               }
               else { //右括号结束；并列的作括号
                  //遇到新的作括号，ykhjs失效。
                  ykhjs=false;
                  work.right = temp;
                  System.out.println("同一层括号的原工作点为：(a="+
                stackds[fenzhi].zba+","+stackds[fenzhi].zbb+")" );
                  work = temp;
                  stackds[fenzhi] = work;
                  System.out.println("同一层新的左括号的工作点为：(a="+
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
            else { //连续节点
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
         System.out.println("打开文件（定式树）遇到问题！");
         System.out.println("Exception" + ex.toString());
      }
      work=root;//备用
   }

   public void paint(Graphics g) {
      System.out.println("进入方法paint()");
      if (CHONGHUI == true) {
         g.setColor(Color.orange);
         g.fillRect(0, 0, 560, 560);
      }
      else {
         CHONGHUI = true;
      } //不想重绘则让CHONGHUI＝false
      g.setColor(Color.black);
      byte kinp = 0;
      for (int i = 1; i <= 19; i++) { //画线
         g.drawLine(18, 28 * i - 10, 522, 28 * i - 10); //hor
         g.drawLine(28 * i - 10, 18, 28 * i - 10, 522); //ver
      }
      for (int i = 0; i < 3; i++) { //画星位
         for (int j = 0; j < 3; j++) {
            g.fillOval(168 * i + 99, 168 * j + 99, 6, 6);
         }
      }

      for (int i = 1; i <= 19; i++) { //画着子点
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
               if (kinp != 0) { //输出块号
                  g.setColor(Color.green);
                  g.drawString("" + kinp, 28 * i - 4, 28 * j - 4);

               }
            }
         }
      }
      System.out.println("从方法paint()返回");
   } //else画整个棋盘和棋子

   public boolean mouseDown(Event e, int x, int y) { //接受鼠标输入
      byte j=0,t=0;
      byte m=0,n=0;
      if (KEXIA == true) {
         KEXIA=false;//只有机器完成一手,才能继续.
         byte a = (byte) ( (x - 4) / 28 + 1); //完成数气提子等.
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
         } //确保xuan!=0;只在第一步执行，坐标为视觉坐标。
         //左上为1，右上为2，右下为3，左下为4;xuan 的定义
         for (j = xuan; j > 1; j--) {
            t = n;
            n = (byte) (20 - m);
            m = t;
         }//换到左上角的标准位置，进行匹配。
         if (fuan == 0) { //需要进一步判明。
            if (m < n)
               fuan = 1;//无需反转。
            else if (m > n)
               fuan = 2;//需要反转。
         }
         if (fuan == 2) {
            t = m;
            m = n;
            n = t;
         }
         System.out.println("落点相当于："+m+","+n);
         if(work==null){
            System.out.println("定式中该变化到此结束。");
            showStatus("定式中该变化到此结束。");
            //KEXIA=true;//需要用恢复或悔棋按钮使变成真值。
            return true;
         }
         a=m;//此时a，b仅是临时变量，用于标准型的匹配；
         b=n;
         for(temp=work;temp!=null;temp=temp.right ){
            if(a==temp.zba&&b==temp.zbb){
               work=temp.left;
               break;
            }
         }
         if(temp==null){
            System.out.println("定式中没有该变化。");
            showStatus("定式中没有该变化。");
            go.clhuiqi() ;
            KEXIA=true;
            return true;
         }
         if(work==null){
            System.out.println("定式中该变化到此结束。");
            showStatus("定式中该变化到此结束。");
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
            System.out.println("选择的应对变化为："+m+","+n);
            if (fuan == 2) {//将标准型的应对变换为现实的坐标。
               t = m;
               m = n;
               n = t;
            }//处理的顺序很重要

            for (j = xuan; j > 1; j--) {//变换到实在的象限
               t = m;
               m = (byte) (20 - n);
               n = t;
            }


            go.cgcl(m,n) ;//go所用的坐标是另一个概念。
            work=work.left;
         }

         CHONGHUI = false; //效果：提子被画上十字
         repaint();
         System.out.println("mousedown");
         KEXIA=true;
         return true; //向容器传播,由Frame处理
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
      int ju = 0; //其实不需要初始化
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
         } //确保xuan!=0;只在第一步执行
         for (j = xuan; j > 1; j--) {
            t = n;
            n = (byte) (20 - m);
            m = t;
         }
         if (fuan == 0) { //需要进一步判明
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

      if (e.target == huiqi) {//应该节点也响应后退
         if (go.shoushu > 0) {//对称挂角没法恢复。只能同侧挂角。
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
      if (e.target == huifu) { //恢复到原始局面
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
