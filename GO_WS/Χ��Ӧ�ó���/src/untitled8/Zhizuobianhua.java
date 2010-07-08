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
   boolean xds; //是否新定式。
   DsNode root, temp, work, temp1, temp2, workf, rootb,rootc;
   short bhs = 0; //变化数目
   short linss = 0; //初始局面的手数,确定能否悔棋
   boolean WCDS=false;//完成定式输入的标志。
   boolean KEXIA = true;//用于同步，防止机器思考时人按下鼠标
   boolean CHONGHUI = true; //是否重绘
   boolean fhuiqi = false; //是否悔棋
   boolean fqianjin = false; //是否前进

   DataInputStream in; //定式树输入文件
   DataOutputStream out; //定式树输出文件。
   DataOutputStream out2; //变化树的输出文件

   Button store = new Button("储存变化");
   Button huiqi = new Button("悔棋");
   Button conglai = new Button("回到初始局面");
   Button pass = new Button("放弃一手");
   Button wenjian = new Button("存入变化文件");
   Button shurudingshi = new Button("存入定式文件");
   TextField xingshi1 = new TextField(15);
   TextField xingshi2 = new TextField(15);

   byte fenzhi = 0;
   DsNode[] stackds = new DsNode[100];

   public void init() { //初始化,完成界面

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
            System.out.println("定式树的根为：(a=" +
                               work.zba + "," + work.zbb + ")");
            //整棵树不用括号包围
            //括号仅表示括号内是父节点的分支；
            fenzhi++;
            stackds[fenzhi] = work; //右括号结束后新的左括号接到work上。
            System.out.println("第一层括号的工作点为：(a=" +
                               stackds[fenzhi].zba + "," + stackds[fenzhi].zbb +
                               ")");
         }
         else { //定式树初始时可能只有一种起点。
            in.read(dsnode, 1, 4);
            root = work = new DsNode(dsnode[1], dsnode[2], dsnode[3],
                                     dsnode[4], lina);
            System.out.println("定式树的根为：(a=" +
                               work.zba + "," + work.zbb + ")");
         }
         while (in.available() != 0) {
            lina = in.readByte();
            if (lina == ZKUOHAO) {
               in.read(dsnode);
               temp = new DsNode(dsnode[1], dsnode[2], dsnode[3],
                                 dsnode[4], dsnode[0]);
               System.out.println("分支变化的首节点为:" + temp.zba
                                  + "," + temp.zbb + ")");
               if (ykhjs == false) {
                  //新的左括号
                  work.left = temp;
                  fenzhi++;
                  work = temp;
                  stackds[fenzhi] = work;
                  System.out.println("新一层括号的工作点为：(a=" +
                                     stackds[fenzhi].zba + "," +
                                     stackds[fenzhi].zbb + ")");
               }
               else { //右括号结束；并列的作括号
                  //遇到新的作括号，ykhjs失效。
                  ykhjs = false;
                  work.right = temp;
                  System.out.println("同一层括号的原工作点为：(a=" +
                                     stackds[fenzhi].zba + "," +
                                     stackds[fenzhi].zbb + ")");
                  work = temp;
                  stackds[fenzhi] = work;
                  System.out.println("同一层新的左括号的工作点为：(a=" +
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
      work=root;
   }

   public void paint(Graphics g) {
      System.out.println("进入方法paint()");
      if (CHONGHUI == true) {
         g.setColor(Color.orange);
         g.fillRect(0, 0, 560, 560);

      }
      CHONGHUI = true;
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
            kinp = go.zb[i][j][go.KSYXB];
            if (kinp != 0) { //输出块号
               g.setColor(Color.green);
               g.drawString("" + kinp, 28 * i - 4, 28 * j - 4);

            }
         }
      }
      System.out.println("从方法paint()返回");
   } //else画整个棋盘和棋子

   public boolean mouseDown(Event e, int x, int y) { //接受鼠标输入
      if (KEXIA == true) {
         //KEXIA=false;//只有机器完成一手,才能继续.
         byte a = (byte) ( (x - 4) / 28 + 1); //完成数气提子等.
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

         CHONGHUI = false; //效果：提子被画上十字
         repaint();
         System.out.println("mousedown");
         return true; //向容器传播,由Frame处理
      }
      else {
         return true;
      }
   }

   void delete(DsNode root) {//前提是root为两分或征子有关。
      DsNode temp,work,temp1;

      //if (root == null)
         //return;
      work=root.left ;
      if(work==null) return;//最后一步了。
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
   byte fuzhi1(DsNode root) { //为各节点的形势赋值
      //6月13日。
      DsNode temp = null;
      byte max = (byte) - 128;
      byte linv;
      if (root.left == null) { //变化结束
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

   byte fuzhi2(DsNode root) { //为各节点的形势赋值
      //6月13日。
      DsNode temp = null;
      byte max = (byte) - 128;
      byte linv;
      if (root.left == null) { //变化结束
         if(root.xingshi1!=root.xingshi2){
            //if(root.xingshi1 <0)
               //System.out.println("征子成立也不好，不合理") ;
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
      root.xingshi2 = (byte) ( -max);//误为xingshi1,waste 1h.
      if(root.xingshi1!=root.xingshi2){
         root.ZZYG=true;
      }

      return (byte) ( -max);
   } //定式中涉及征子可能不止一个？

   //兄弟间按照优劣排序。
   void paixu(DsNode root) {
      DsNode temp, temp1, temp2;
      byte jishu = 0;
      if (root.left == null) {
         return;
      }
      temp = temp1 = root.left;
      if (temp1.right == null) {
         return; //单个节点无需排序
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
               //形式用相对表示就能简化代码。
               //转换视角后要变符号。
            } //有问题，再说吧，事前排序效果不明。
         }
         //定位已经有的节点。
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
      short  i = 0 ;//手数循环变量
      byte m = 0, n = 0;
      byte j=0;//旋转循环变量
      int ju = 0; //其实不需要初始化
      if (e.target == shurudingshi) {
         //输入相关的定式，确保定式树中存在相关定式
         //应该首先输入定式，再确定初始局面。
         //控制按钮的可用与否实现
         work = root;//root是原有定式树的根。
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
            }//确保xuan!=0;只在第一步执行
            for(j=xuan;j>1;j--){
               t=n;
               n=(byte)(20-m);
               m=t;
            }
            if(fuan==0){//需要进一步判明
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
            else { //work是下一手的老大
               for (temp1 = work; temp1 != null; temp = temp1,
                    temp1 = temp1.right) {
                  if (temp1.zba == m && temp1.zbb == n) {
                     work = temp1.left;
                     break;
                  }
               }
               if (temp1 == null) {
                  System.out.print("该初始变化尚未列为定式！现在加入");
                  temp.right = work = new DsNode(m, n);
                  xds = true;
               }
            }
         }//定式树方式只能输入常规简明定式，征子相关通过变化输入。
         //还是通过定式渠道输入较好，因为其中没有劣着。
         if(xds==false){
            System.out.print("该定式已经存在");
            WCDS=true;//用于判断何时禁用文件按钮。
            go=new GoBoardLian1();
            repaint();
            return true;
         }
         try {
           out = new DataOutputStream(
              new BufferedOutputStream(
              new FileOutputStream("H:\\weiqidata\\" + "定式树")));
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
         //输入变化后，将结果和原有定式联系，并修改定式树文件；
         //输入的如果是定式变招，两分的变化存入定式树，总的变化，
         //包括其它变化，全部输入“定式变着树”文件。并在第一个
         //分支点处指出文件名称。变化树从第一个分支处开始。
         //输入的变化如果是骗着，在骗着开始处指出文件名称，骗着变
         //化从分支处开始;
         //定式后的变化直接从下一手开始存储。而且要区别两种不同的
         //开始，毕竟双方都可能有手段。
         //将局部的结果存储为SGF文件，需要时动态调入。

         fuzhi1(rootb);
         fuzhi2(rootb);//增加ZZYG（征子有关）的赋值；

         if(rootb.xingshi1!=0&&rootb.xingshi2!=0){
            //骗着
            //因为rootb到rootc是直线变化，所以是等价的
            rootb.ju=ju;
            System.out.println("该步是骗着：("+rootb.zba
                               +","+rootb.zbb+")");
         }
         else{//可以作为定式，能达到两分。
            rootb.ju=ju;  //新的变化也从rootb开始
            //如果新变化的初始变化含有这样的定式节点，则调入
            //变化树文件。
            System.out.println("该步可作为定式：("+rootb.zba
                             +","+rootb.zbb+")");
            if(rootb.ZZYG==true){
               System.out.println("该步与征子有关");
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
         //写入定式树，只写入两分的变化。删除不合理的变化
            delete(rootb);
            try {
               out = new DataOutputStream(
               new BufferedOutputStream(
               new FileOutputStream("H:\\weiqidata\\" + "定式树")));
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
         if (rootb == null) { //存储原始局面时的处理
            System.out.print("存储初始变化");
            linss = go.shoushu; //确定初始局面的手数。
            work = root;
            xds=false;//表明出现新的着法
            for (i = 1; i <= go.shoushu; i++) {
               m = go.hui[i][25];
               n = go.hui[i][26];
               if(work==null){
                  xds=true;
                  rootb=work=temp1.left =new DsNode(m, n);
                  System.out.print("原定式的继续演变，现在研究");
               }//以下work!=null
               if (xds == true) {//work是上一手
                  work.left = new DsNode(m, n);
                  work = work.left;
               }
               else { //work是下一手（即当前手）的老大
                  for (temp1 = work; temp1 != null; temp = temp1,
                       temp1 = temp1.right) {
                     if (temp1.zba == m && temp1.zbb == n) {
                        work = temp1.left;
                        break;
                     }
                  }
                  if (temp1 == null) {
                     System.out.print("该着法原非定式，现在研究");
                     rootb=temp.right = work = new DsNode(m, n);
                     xds = true;//rootb是新着法，qipu中存储以后的着法
                     //也重复包括新着法，可以验证。
                  }
               }

            }
            if(xds==false){//错误报告
               System.out.print("该初始变化已经在定式中");
               showStatus("该初始变化已经在定式中");
               //todo:读入变化文件。
               go=new GoBoardLian1();
               repaint();
            }
            rootc=work;//新招法的最后一手，可能和rootb相同
            pass.disable();
         }
         else {
            System.out.print("存储后继变化");
            work=rootc;
            xds=false;
            if(rootc.left ==null){//可能已经加入变化了
               xds=true;
            }
            else{
               work=rootc.left;
            }
            for (i = 1; i <= go.shoushu - linss; i++) {
               m = go.hui[i + linss][25];
               n = go.hui[i + linss][26];
               if(work==null){//新变化的新分支。
                  xds=true;
                  work=temp1.left =new DsNode(m, n);
                  System.out.print("原定式的继续演变，现在研究");
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
                     System.out.print("该变在这里分叉！现在加入");
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
               System.out.println("该变化已经存在");
               showStatus("该变化已经存在");
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
      temp = root.right; //root是表示树的二叉树的根节点
      System.out.println("局部root=(" + root.zba + "," + root.zbb + ")");
      if (temp == null) { //没有兄弟，表示没有分叉
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
