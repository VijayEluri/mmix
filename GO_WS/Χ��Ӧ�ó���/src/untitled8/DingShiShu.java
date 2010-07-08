package untitled8;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import untitled3.DsNode;
public class DingShiShu {//将与定式树有关的内容封装到类中。
   DsNode root, temp, work, temp1, temp2;
   final static byte ZKUOHAO = -127;
   final static byte YKUOHAO = 127;

   DataOutputStream out; //定式树输出文件。


   public DingShiShu() {

   try {
      DsNode[] stackds = new DsNode[25]; //SGF格式用
     byte fenzhi = 0; //SGF格式用,与stackds构成堆栈
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


   }
   void shuchu(){
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
   private void write(DataOutputStream out, DsNode root) {

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



}