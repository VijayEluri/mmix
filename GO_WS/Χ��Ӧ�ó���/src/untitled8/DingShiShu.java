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
public class DingShiShu {//���붨ʽ���йص����ݷ�װ�����С�
   DsNode root, temp, work, temp1, temp2;
   final static byte ZKUOHAO = -127;
   final static byte YKUOHAO = 127;

   DataOutputStream out; //��ʽ������ļ���


   public DingShiShu() {

   try {
      DsNode[] stackds = new DsNode[25]; //SGF��ʽ��
     byte fenzhi = 0; //SGF��ʽ��,��stackds���ɶ�ջ
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
   void shuchu(){
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
   private void write(DataOutputStream out, DsNode root) {

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



}