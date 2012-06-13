package eddie.wu.arrayblock;
/**从zb数组直接生成局面，而不是按照过程逐步生成当前局面。
未完成。
 所谓局面的生成就是指生成表示当前局面的数据结构.
 */
import org.apache.log4j.Logger;

import eddie.wu.arrayblock.ArrayGoBoard;
public class GoBoard12 extends ArrayGoBoard {
	private static final Logger log = Logger.getLogger(ArrayGoBoard.class);
   byte zishu=0;
   byte qiki=127;//气的块号,用后减 1;
   public GoBoard12(ArrayGoBoard goboard) {
      super(goboard.getBoardSize());
      for(int i=ZBXX;i<=ZBSX;i++){
         for(int j=ZBXX;j<=ZBSX;j++){
              zb[i][j][ZTXB]=goboard.zb[i][j][ZTXB];
              zb[i][j][SQBZXB]=goboard.zb[i][j][SQBZXB];
              zb[i][j][QSXB]=goboard.zb[i][j][QSXB];
              zb[i][j][KSYXB]=goboard.zb[i][j][KSYXB];
              zb[i][j][4]=goboard.zb[i][j][4];
              zb[i][j][5]=goboard.zb[i][j][5];
              zb[i][j][6]=goboard.zb[i][j][6];
              zb[i][j][7]=goboard.zb[i][j][7];
         }
      }//zb数组的初始化

      shoushu=goboard.shoushu;
      ki=goboard.ki;
      for(byte i=0;i<=ki;i++){
         //byte jj=kuai[i][0][1];
         for(byte j=0;j<70;j++){
            kuai[i][j][0]=goboard.kuai[i][j][0];
            kuai[i][j][1]=goboard.kuai[i][j][1];
         }
         //kuai[i][0][0]=goboard.kuai[i][0][0];
      }

      for(short i=1;i<=shoushu;i++){
          for(byte j=0;j<38;j++){
             hui[i][j]=goboard.hui[i][j];
          }
      }
      ktb=goboard.ktb;
      ktw=goboard.ktw;
   }

   public void shengchengjumian(){
   //从棋谱的位图表示生成kuai和zb数组的相应信息
      byte i,j;
      for (i=1;i<20;i++){//i是纵坐标
         for(j=1;j<20;j++){//j是横坐标
            if(zb[j][i][SQBZXB]==1) continue;//SQBZXB此处相当于处理过的标志.
            zishu=0;
            if (zb[j][i][ZTXB]==BLACK){//左.上必为空点或异色子
               ki++;
               chengkuai(j,i,BLACK); //判断右.下是否为同色子.
            }
            else if(zb[j][i][ZTXB]==WHITE){//左.上必为空点或异色子
               ki++;
               chengkuai(j,i,WHITE); //判断右.下是否为同色子
            }
            else {
               ki++;
               chengkuai(j,i,BLANK);
               if(zishu==1){//眼位
                  kuai[ki][1][0]=0;
                  kuai[ki--][1][1]=0;
                  zb[j][i][KSYXB]=0;//非块
                  //todo:眼位的处理
               }
               else if(zishu>1){
                  kuai[ki][0][1]=zishu;
                  zishu=0;
                  for(byte p=0;p<70;p++){
                     kuai[qiki][p][0]=kuai[ki][p][0];
                     kuai[ki][p][0]=0;
                     kuai[qiki][p][1]=kuai[ki][p][1];
                     kuai[ki][p][1]=0;
                  }
                  qiki--;
                  ki--;
               }
               else if(log.isDebugEnabled()) log.debug("error:zishu<1");

               continue;
            }
            if(zishu==1){
               //ki--;
               kuai[ki][1][0]=0;
               kuai[ki--][1][1]=0;
               zb[j][i][KSYXB]=0;//非块
            }
            else if(zishu>1){
               kuai[ki][0][1]=zishu;
               zishu=0;
            }
            else if(log.isDebugEnabled()) log.debug("error:zishu<1");
         }
      }//生成块
      for (i=1;i<20;i++){//i是纵坐标
         for(j=1;j<20;j++){//j是横坐标
            zb[j][i][SQBZXB]=0;//恢复每个点的算气标志
            if(zb[j][i][ZTXB]>0 & zb[j][i][KSYXB]==0){
               zb[j][i][QSXB]=jszq(j,i);
            }
         }
      }//计算点气
      for(i=1;i<=ki;i++){
         //byte qi=jskq(ki);计算块气过程中直接储存气点.
         kuai[ki][0][0]=jskq(ki);
      }//计算块气
   }

   public void jushipanduan(){//局势判断


   }

   public void chengkuai(byte a,byte b,byte color){
   //收集信息的过程中,可以令color=BLANK,调用该函数,但是气块的信息
   //不能驻留在kuai数组内,必须早点调用并清除.
      byte m1,n1;
      //byte zishu=0;
      if(zishu<49){
         kuai[ki][++zishu][0]=a;
         kuai[ki][zishu][1]=b;
      }
      else{

         if(log.isDebugEnabled()) log.debug("该块的子数超过49,块号为:"+ki);

      }
      zb[a][b][SQBZXB]=1;
      zb[a][b][KSYXB]=(byte)ki;
      //zishu++;
      for (byte k=0;k<4;k++){
         m1=(byte)(a+szld[k][0]);
         n1=(byte)(b+szld[k][1]);
         if(zb[m1][n1][SQBZXB]==0 & zb[m1][n1][ZTXB]==color){
            chengkuai(m1,n1,color);
         }
      }
   }//成块的点SQBZXB==1;
}