package untitled8;
/**��zb����ֱ�����ɾ��棬�����ǰ��չ��������ɵ�ǰ���档

 */
public class GoBoard1 extends GoBoard {
   byte zishu=0;
   byte qiki=127;//���Ŀ��,�ú�� 1;
   public GoBoard1(GoBoard goboard) {
      super();
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
      }//zb����ĳ�ʼ��

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
   //�����׵�λͼ��ʾ����kuai��zb�������Ӧ��Ϣ
      byte i,j;
      for (i=1;i<20;i++){//i��������
         for(j=1;j<20;j++){//j�Ǻ�����
            if(zb[j][i][SQBZXB]==1) continue;//SQBZXB�˴��൱�ڴ�����ı�־.
            zishu=0;
            if (zb[j][i][ZTXB]==BLACK){//��.�ϱ�Ϊ�յ����ɫ��
               ki++;
               chengkuai(j,i,BLACK); //�ж���.���Ƿ�Ϊͬɫ��.
            }
            else if(zb[j][i][ZTXB]==WHITE){//��.�ϱ�Ϊ�յ����ɫ��
               ki++;
               chengkuai(j,i,WHITE); //�ж���.���Ƿ�Ϊͬɫ��
            }
            else {
               ki++;
               chengkuai(j,i,BLANK);
               if(zishu==1){//��λ
                  kuai[ki][1][0]=0;
                  kuai[ki--][1][1]=0;
                  zb[j][i][KSYXB]=0;//�ǿ�
                  //todo:��λ�Ĵ���
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
               else System.out.println("error:zishu<1");

               continue;
            }
            if(zishu==1){
               //ki--;
               kuai[ki][1][0]=0;
               kuai[ki--][1][1]=0;
               zb[j][i][KSYXB]=0;//�ǿ�
            }
            else if(zishu>1){
               kuai[ki][0][1]=zishu;
               zishu=0;
            }
            else System.out.println("error:zishu<1");
         }
      }//���ɿ�
      for (i=1;i<20;i++){//i��������
         for(j=1;j<20;j++){//j�Ǻ�����
            zb[j][i][SQBZXB]=0;//�ָ�ÿ�����������־
            if(zb[j][i][ZTXB]>0 & zb[j][i][KSYXB]==0){
               zb[j][i][QSXB]=jszq(j,i);
            }
         }
      }//�������
      for(i=1;i<=ki;i++){
         //byte qi=jskq(ki);�������������ֱ�Ӵ�������.
         kuai[ki][0][0]=jskq(ki);
      }//�������
   }

   public void jushipanduan(){//�����ж�


   }

   public void chengkuai(byte a,byte b,byte color){
   //�ռ���Ϣ�Ĺ�����,������color=BLANK,���øú���,�����������Ϣ
   //����פ����kuai������,���������ò����.
      byte m1,n1;
      //byte zishu=0;
      if(zishu<49){
         kuai[ki][++zishu][0]=a;
         kuai[ki][zishu][1]=b;
      }
      else{
         zishu++;
         System.out.println("�ÿ����������49,���Ϊ:"+ki);
      }
      zb[a][b][SQBZXB]=1;
      zb[a][b][KSYXB]=ki;
      //zishu++;
      for (byte k=0;k<4;k++){
         m1=(byte)(a+szld[k][0]);
         n1=(byte)(b+szld[k][1]);
         if(zb[m1][n1][SQBZXB]==0 & zb[m1][n1][ZTXB]==color){
            chengkuai(m1,n1,color);
         }
      }
   }//�ɿ�ĵ�SQBZXB==1;
}