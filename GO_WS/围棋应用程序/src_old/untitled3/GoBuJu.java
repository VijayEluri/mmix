package untitled3;
/*
 * <p>Title:GoBoard类扩展，加入布局有关的函数 </p>
 * <p>Description: 用于死活题训练:shihuo 基础上加入布局的代码</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: SE</p>
 * @author wueddie
 * @version 1.0
 * 感觉上java对于围棋程序未必合适,因为与一般应用程序不同,不容易提出对象
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

   byte  jieduanb=0;//阶段标志
   byte[][] jieduan=new byte[21][21];
   //每个有子的点，还有周围三间以内的点全部使对应的jieduan=1
   //如果全局没有或少有＝0的点，则没有大场，布局结束。
   //天元位置的空点不计入，因为草肚皮的价值不大。
   public byte panduanjieduan(){//判断阶段
      short count=0;
      byte i=0;
      byte j=0;
      for( i=1;i<20;i++){
         for( j=1;j<20;j++){
            jieduan[i][j]=0;
         }
      }
      //根据已经有的局势信息，如厚薄等，为jieduan数组赋值。
      for( i=1;i<20;i++){
         for( j=1;j<20;j++){
            if(jieduan[i][j]==0){
               count++;
            }
         }
      }
      if(count!=0) return 0;//表示布局
      //1为中盘，2为终盘；
      else{
      }


     return jieduanb;

   }
   /*public byte dkyw(byte kin){
      //单块的眼位

   }*/
   public void GoBuJu(){//布局
      //每个角有八个正常占角点。
      jiao[0][0]=3;//三三
      jiao[0][1]=3;
      jiao[1][0]=4;//星
      jiao[1][1]=4;
      jiao[2][0]=3;//小目
      jiao[2][1]=4;
      jiao[3][0]=4;//小目
      jiao[3][1]=3;
      jiao[4][0]=3;//目外
      jiao[4][1]=5;
      jiao[5][0]=5;//目外
      jiao[5][1]=3;
      jiao[6][0]=4;//高目
      jiao[6][1]=5;
      jiao[7][0]=5;//高目
      jiao[7][1]=4;
      //加上四个角，共有 32 个

   }
   //考虑相对方式的变化，即只考虑扭断之类的相对关系，而不是每个子的
   //绝对位置。
   byte[][] dingdian1={{1,0},{-1,0},{0,1},{0,-1}};//每个点的直接相邻点
   byte[][] dingdian2={{1,1},{-1,1},{1,-1},{-1,-1}};//每个点的肩冲点

   public byte jiejinfangshi(byte m, byte n){//接近方式:靠还是肩冲
      for(byte i=0;i<4;i++){
         byte  u=zb[m+dingdian1[i][0]][n+dingdian1[i][1]][0];
         if(u>0) return 1;// 紧紧相邻
      }
      for(byte i=0;i<4;i++){
         byte  u=zb[m+dingdian2[i][0]][n+dingdian2[i][1]][0];
         if(u>0) return 2;// 肩冲位置
      }
      return 3;//更松！
   }//落子点（m,n）与最近点的关系。松紧？

   public boolean kechaiyi(byte m,byte n){
      //落点为（m,n）
      if(m<3||n<3) {
         return false;
      //三线或三线以上才考虑拆一。
      }else if(zb[m][n][0]>0) {
         return false; //已经有子。
      }else if (jiejinfangshi(m, n) > 2){
         return true;
      }
      return true;
   }

   public byte chaiyigeshu(byte m,byte n){//计算拆一个数
      //主要用于角部，应事前检查参数,m,n为落子点坐标。
      byte  chai=0;
      if((m<2||m>3)&&(n<2||n>3)){
         System.out.print("输入位置错误，太高或太低");
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
   //对方不正常占角，是否使角不为空
   //xuan从1到4调用。
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
