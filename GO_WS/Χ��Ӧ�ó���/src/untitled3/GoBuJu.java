package untitled3;
/*
 * <p>Title:GoBoard����չ�����벼���йصĺ��� </p>
 * <p>Description: ����������ѵ��:shihuo �����ϼ��벼�ֵĴ���</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: SE</p>
 * @author wueddie
 * @version 1.0
 * �о���java����Χ�����δ�غ���,��Ϊ��һ��Ӧ�ó���ͬ,�������������
 */
import java.awt.*;
import java.applet.Applet;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import untitled8.GoBoardLian1;

public class GoBuJu extends GoBoardLian1 {



   byte jiao[][]=new byte[8][2];

   public void zhengzi(){

   }

   byte  jieduanb=0;//�׶α�־
   byte[][] jieduan=new byte[21][21];
   //ÿ�����ӵĵ㣬������Χ�������ڵĵ�ȫ��ʹ��Ӧ��jieduan=1
   //���ȫ��û�л����У�0�ĵ㣬��û�д󳡣����ֽ�����
   //��Ԫλ�õĿյ㲻���룬��Ϊ�ݶ�Ƥ�ļ�ֵ����
   public byte panduanjieduan(){//�жϽ׶�
      short count=0;
      byte i=0;
      byte j=0;
      for( i=1;i<20;i++){
         for( j=1;j<20;j++){
            jieduan[i][j]=0;
         }
      }
      //�����Ѿ��еľ�����Ϣ����񱡵ȣ�Ϊjieduan���鸳ֵ��
      for( i=1;i<20;i++){
         for( j=1;j<20;j++){
            if(jieduan[i][j]==0){
               count++;
            }
         }
      }
      if(count!=0) return 0;//��ʾ����
      //1Ϊ���̣�2Ϊ���̣�
      else{
      }


     return jieduanb;

   }
   /*public byte dkyw(byte kin){
      //�������λ

   }*/
   public void GoBuJu(){//����
      //ÿ�����а˸�����ռ�ǵ㡣
      jiao[0][0]=3;//����
      jiao[0][1]=3;
      jiao[1][0]=4;//��
      jiao[1][1]=4;
      jiao[2][0]=3;//СĿ
      jiao[2][1]=4;
      jiao[3][0]=4;//СĿ
      jiao[3][1]=3;
      jiao[4][0]=3;//Ŀ��
      jiao[4][1]=5;
      jiao[5][0]=5;//Ŀ��
      jiao[5][1]=3;
      jiao[6][0]=4;//��Ŀ
      jiao[6][1]=5;
      jiao[7][0]=5;//��Ŀ
      jiao[7][1]=4;
      //�����ĸ��ǣ����� 32 ��

   }
   //������Է�ʽ�ı仯����ֻ����Ť��֮�����Թ�ϵ��������ÿ���ӵ�
   //����λ�á�
   byte[][] dingdian1={{1,0},{-1,0},{0,1},{0,-1}};//ÿ�����ֱ�����ڵ�
   byte[][] dingdian2={{1,1},{-1,1},{1,-1},{-1,-1}};//ÿ����ļ���

   public byte jiejinfangshi(byte m, byte n){//�ӽ���ʽ:�����Ǽ��
      for(byte i=0;i<4;i++){
         byte  u=zb[m+dingdian1[i][0]][n+dingdian1[i][1]][0];
         if(u>0) return 1;// ��������
      }
      for(byte i=0;i<4;i++){
         byte  u=zb[m+dingdian2[i][0]][n+dingdian2[i][1]][0];
         if(u>0) return 2;// ���λ��
      }
      return 3;//���ɣ�
   }//���ӵ㣨m,n���������Ĺ�ϵ���ɽ���

   public boolean kechaiyi(byte m,byte n){
      //���Ϊ��m,n��
      if(m<3||n<3) {
         return false;
      //���߻��������ϲſ��ǲ�һ��
      }else if(zb[m][n][0]>0) {
         return false; //�Ѿ����ӡ�
      }else if (jiejinfangshi(m, n) > 2){
         return true;
      }
      return true;
   }

   public byte chaiyigeshu(byte m,byte n){//�����һ����
      //��Ҫ���ڽǲ���Ӧ��ǰ������,m,nΪ���ӵ����ꡣ
      byte  chai=0;
      if((m<2||m>3)&&(n<2||n>3)){
         System.out.print("����λ�ô���̫�߻�̫��");
         return -1;
      }
      if(m>2&m<3) {
         if(kechaiyi((byte)(m+2),(byte)n)) chai++;
         if(kechaiyi((byte)(m-2),(byte)n)) chai++;
      }
      if(n>2&n<3) {
         if(kechaiyi(m,(byte)(n+2))) chai++;
         if(kechaiyi(m,(byte)(n-2))) chai++;
       }
       return chai;
   }

   public boolean iskongjiao(byte xuan){
   //�Է�������ռ�ǣ��Ƿ�ʹ�ǲ�Ϊ��
   //xuan��1��4���á�
      byte j=0,m=0,n=0,t=0;
      for(byte i=0;i<8;i++){
         m =jiao[i][0];
         n =jiao[i][1];
         for (j = xuan; j > 1; j--) {
               t = m;
               m = (byte) (20 - n);
               n = t;
         }
         if(chaiyigeshu(m,n)>=2) return true;

      }
      return false;
   }







   int jszqt(int a, int b){
       int dang=0;
       if((a+1)<=18&&zb[a+1][b][0]==0){//2.1the breath of blank
            dang++;
         }
         if((a-1)>=0&&zb[a-1][b][0]==0){//2.2
            dang++;
         }
         if((b+1)<=18&&zb[a][b+1][0]==0){//2.3
            dang++;
         }
         if((b-1)>=0&&zb[a][b-1][0]==0){//2.4
            dang++;
         }
         return dang;

   }

   //one point was eaten


   public int xl(int a,int b,int r){//9.1dian kuai xiang ling
   int gq=0;//6.12
   if ((a+1)<=18&&zb[a+1][b][3]==r)
         gq++;
   if ((a-1)>=0&&zb[a-1][b][3]==r)
         gq++;
   if ((b+1)<=18&&zb[a][b+1][3]==r)
         gq++;
   if ((b-1)>=0&&zb[a][b-1][3]==r)
         gq++;
   return gq;
   }











}
