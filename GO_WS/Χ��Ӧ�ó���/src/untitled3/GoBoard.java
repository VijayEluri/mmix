package untitled3;
/**
 * <p>Title:������̹��ܵ��� </p>
 * <p>Description:����,�������� </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company:���� </p>
 * @author �⽨��
 * @version 1.0
 */

public class GoBoard {

   final byte[][] szld={{1,0},{0,-1},{-1,0},{0,1}};//�����������ӵ�,˳��ɵ�.
   short shoushu=0;//��ǰ����,����֮ǰ����.��1��ʼ;
   byte ktb=0,ktw=0;//�ڰױ����Ӽ���
   byte[][][] zb = new byte [21][21][8];//0:state;����״̬:��1��2;
   //2:breath;����;3:blockindex������;1:�������ı�־;4-7�洢����.
   //ǰ��ά�����̵�����,�����±��1��19;

   final byte  ZBSX=20;//������������;
   final byte  ZBXX=0;//������������;

   final byte  KONGDIAN=0;
   final byte  BLACK=1;//1��ʾ����;
   final byte  WHITE=2;//2��ʾ����;

   final byte  ZTXB=0;//�±�0�洢״ֵ̬;
   final byte  SQBZXB=1;//�±�1�洢������־;
   final byte  QSXB=2;//�±�2�洢����;
   final byte  KSYXB=3;//�±�3�洢������
   byte ki=0; //kuai shu����?��ǰ�Ѿ��õ��Ŀ��;
   byte  [][][] kuai = new byte [100][70][2];//mei yi kuai de ge zi zuo biao
   //�洢ÿ�����ϸ��Ϣ;50-69Ϊ����.1-49Ϊ�ӵ�;[x][0][0]Ϊ����;
   //[x][0][1]Ϊ����;����49��,���洢,����20��,������洢;
   byte  [][] hui=new byte [400][38];//0=�¿�����;1~8four single point
   //eaten,9~12 kuai suo ying of fou block eaten.13~24is same ,25,26a,b
   //��¼��ֵĹ�����Ϣ,���ڻ���;1-8Ϊ���Ե��ӵ�����;9-12Ϊ���Կ������;
   //13-20Ϊ�ɿ��ӵ�����,21-24Ϊ���¿�ľɿ�����.27-32Ϊ��Ե�,33-35Ϊ��ԵĿ�
   //����;36.37Ϊ���ŵ�.25.26Ϊ�ò��������.
   //DataOutputStream out;//qi pu shu chu wen jain��������ļ�
   //DataInputStream in;//ֱ�Ӵ��ļ����ɾ���.

   public void cgcl(byte  a, byte  b){//chang gui chu li  qi zi  de qi
   //�����������
   //���Խ��ܵ�����(a,b)��c;
   //a is the row subscript of the point.
   //a����������±�,Ҳ��ƽ��ĺ�����:0-18
   //b is the column subscript of the point.
   //b����������±�,Ҳ����Ļ��������:0-18
   //byte c;//a,b��һά��ʾ:0-360;
      byte  m1=a;//���������a,bӦ�ñ��ֲ���
      byte  n1=b;
      byte  p=0;
      byte  linzishu=0;//������
      byte  yise=0;//��ɫ
      byte  tongse=0;//yise is diff color.and 2 same.ͬɫ
      byte  k0=0,k1=0,k2=0,k3=0;//k1����ɫ��,k2�ǿյ�,k3��ͬɫ��
      //the count for three kinds of point.
       //���ֵ�ļ���,k1Ϊ��ɫ�����,k2Ϊ�������,k3Ϊͬɫ�����
      short i=0,j=0;//ѭ������.
      byte  dang=0;//dang is breath of block.�������

      byte  ks=0,kss=0;//ks is count for block,kss for single point
                    //���ڵĳɿ�����Ͷ���������
      byte  kin1=0,m=0,n=0;//the block index.
      //a,b��Χ�ĵ�Ŀ�����
      byte  gq=0;//����.
      byte [] u=  {0,0,0,0,0};//position0����
      byte [] v=  {0,0,0,0,0};//�ĸ����ӵ�����
      byte [] k=  {0,0,0,0,0};//array for block index.��ͬɫ���ӵĿ�����
      byte  [] ysk={0,0,0,0};//����ɫ���ӵĿ�����,ͬ�鲻�ظ�����
      byte  yiseks=0;//������ɫ����
      byte   tzd=0,tkd=0;//the count for single pointeaten andblock eaten.
       //�Եĵ����Ϳ���
      byte  ktz=0;//���Ӽ���,�ֲ�
      byte  ktm=hui[shoushu][36];//
      byte  ktn=hui[shoushu][37];//����������(�����)


      System.out.println("come into method cgcl()");
      if (a>ZBXX&&a<ZBSX&&b>ZBXX&&b<ZBSX&&zb[a][b][ZTXB]==KONGDIAN){
       //�±�Ϸ�,�õ�հ�
         if(a==ktm&&b==ktn){// decide is it the prohibtted point?
             //�Ƿ���ŵ�
            System.out.println("���Ǵ��ʱ�Ľ��ŵ�,�����ҽٲ�!");
           return;
         }else{
            System.out.println("a="+a+",b="+b);
         }
      }
      else{//��һ�಻�Ϸ���.
         System.out.println("�õ㲻�Ϸ�,������֮����߸õ��Ѿ�����.");
         return;
      }

      shoushu++;//��������ǰ����,����1��ʼ����.������ͬ.
      hui[shoushu][25]=a;//��¼ÿ�������
      hui[shoushu][26]=b;
      yise=(byte )(shoushu%2+1);//yi se=1��2,������Ϊ����
      tongse=(byte )((1+shoushu)%2+1);//tong se=1��2,�׺���Ϊż��
      zb[a][b][ZTXB]=tongse;//���Զ�̬һ��


      for(i=0;i<4;i++){//�ȴ�����ɫ����
         m1=(byte)(a+szld[i][0]);
         n1=(byte)(b+szld[i][1]);
         if (zb[m1][n1][ZTXB]==yise) {//1.1�ұ����ڵ�
            k1++;//the count of diffrent color.��ɫ�����
            kin1=zb[m1][n1][KSYXB];//the block index for the point.66
           if (kin1==0){      //not a block.���ǿ�
               zb[m1][n1][QSXB]-=1;
              if(zb[m1][n1][QSXB]==0){//eat the diff point
                  k1--;//�����Ҫ��ȥ
                  tzd++;//�ò��ᵥ�Ӽ���
                  hui[shoushu][tzd*2-1]=m1;
                  hui[shoushu][tzd*2]=n1;
                  System.out.println("����:a="+m1+",b="+n1);
                  ktz++;  //�ò���������,�������.
                  zzq(m1,n1,tongse);//zi zhen qiͬɫ�ӽ�����.
               }
              else if(zb[m1][n1][QSXB]<0){
                  System.out.println("��������:a="+m1+",b="+n1);
                 return;
               }
            }
           else{//if (kin1==0)
              for(i=0;i<yiseks;i++){
                 if(kin1==ysk[i]) break;
               }
              if(i==yiseks){//���ظ�
                  ysk[yiseks++]=kin1;
                 byte qi=(byte)(kuai[kin1][0][0]-1);
                  kdq(kin1,qi);
                 if (kuai[kin1][0][0]==0){
                     k1--;
                     tkd++;//<=4
                     hui[shoushu][8+tkd]=kin1;
                     ktz+=kuai[kin1][0][1];//ʵ�ʵ�����
		     kzq(kin1,tongse); //increase the breath of pobyte surround
                     //��ɫ�鱻��,ͬɫ������.
                  }
                 else if(kuai[kin1][0][0]<0){
                     System.out.println("��������:kin="+kin1);
                   return;
                  }
               }// i==yiseks
            }
         }
      }//��ѭ������


      k0=k1;//k0 is count for diff point.
      zb[a][b][2]=0;//return the breath to zero.��ֹ����ʱ������.
      if(shoushu%2==1) ktb+=ktz;
      else ktw+=ktz;//���ֲ����Ӽ���

      for(i=0;i<4;i++){//�ٴ���հ�����
         m1=(byte)(a+szld[i][0]);
         n1=(byte)(b+szld[i][1]);
         if(zb[m1][n1][ZTXB]>=0){
            linzishu++;
           if(zb[m1][n1][ZTXB]==KONGDIAN){//2.1the breath of blank
            //���������ĵ��Ƿ�Ϊ��
               k2++;//�������
               u[k0+k2]=m1;
               v[k0+k2]=n1;//198
            }
         }
      }
      k0+=k2;//k0 is the total points of diff and blank.
         //k0Ϊ��ɫ������������
      dang=k2;//�����ڵ���
      for(i=0;i<4;i++){//�ٴ���ͬɫ��
         m1=(byte)(a+szld[i][0]);
         n1=(byte)(b+szld[i][1]);
         if(zb[m1][n1][ZTXB]==tongse){//3.1
            k3++;//ͬɫ�����
            kin1= zb[m1][n1][KSYXB];
           if (kin1==0){//������
               kss++; //same color single point.���������
               dang+=zb[m1][n1][QSXB];
               dang--;//current pobyte close one breath of surr point.
               u[k0+kss]=m1;//u[0] not used
               v[k0+kss]=n1;   //deal with single point.
            }
           else{//231�ɿ��
              for(i=0;i<ks;i++){
                 if(kin1==k[i]) break;
               }
              if(i==ks){//���ظ�
                  dang+=kuai[kin1][0][0];//��Ϊ����
                  dang--;
	          u[linzishu-ks]=m1;//deal with block.
                  v[linzishu-ks]=n1;
                  k[ks++]=kin1;//
               }
            }

         }
      }
                //297

         if(dang>0){//dang���ܴ�������������,��������Ϊ0.
             ktm=0;//ԭ���Ľ��ŵ�ʵЧ,��Ϊ�Ѿ�Ѱ��
             ktn=0;
             byte  [] tsk={0,0,0,0};//����ɫ���ӵĿ�����,ͬ�鲻�ظ�����
            byte  tsks=0;//������ɫ����
            byte  lin1=0,lin2=0;//2��23��
            for(i=0;i<4;i++){
                m1=(byte)(a+szld[i][0]);
                n1=(byte)(b+szld[i][1]);
               if (zb[m1][n1][ZTXB]==yise&&zb[m1][n1][QSXB]==1) {
          //1.1�ұ����ڵ��Ƿ񱻴��
                    kin1=zb[m1][n1][KSYXB];//the block index for the point.66
                  if (kin1==0){     //not a block.���ǿ�
                      lin1++;
                      hui[shoushu][26+lin1*2-1]=m1;
                      hui[shoushu][26+lin1*2]=n1;
                   }
                  else {
                        for(i=0;i<tsks;i++){
                          if(kin1==tsk[i]) break;
                           }
                           if(i==tsks){
                                  lin2++;
                                 hui[shoushu][32+lin2]=kin1;
                           }
                    }
               }
            }


         }//if dang>0
         if(dang==0){//?���������˵���,����ǿ���?û��ϵ,������ļ���.
               //showStatus("//this pobyte is prohibited,try again!");
               zzq(a,b,yise);//�൱�����Ӻ�����һ�����,�������������
               hui[shoushu][25]=0;
               hui[shoushu][26]=0;
	       shoushu--;
	       zb[a][b][0]=0;
	       return;
         }//��������ɱ
	 //showStatus("qing="+dang+a+b);
         if (k3==0){//4.1 no same color pobyte surroundû��ͬɫ�ڵ�
             System.out.println("//k3=0");
             zb[a][b][2]=dang;
              if(dang==1&&ktz==1){//���ǽ�
		  ktm=u[linzishu];//��Ϊ�ȴ�����ɫ��,�ٿհ׵�,����ͬɫ��.
		  ktn=v[linzishu];//��Ϊ���һ��?����ǽ��ϵĽ���?��4��Ϊlinzishu
	          hui[shoushu][36]=ktm;//2��23��
                  hui[shoushu][37]=ktn;//2yue23����
               }//not conform to so. en.
               if(dang>=2){
                  zb[a][b][4]=u[k1+1];
                  zb[a][b][5]=v[k1+1];
                  zb[a][b][6]=u[k1+2];
                  zb[a][b][7]=v[k1+2];
               }
               else{
                  zb[a][b][4]=u[k1+1];
                  zb[a][b][5]=v[k1+1];
               }
	    return;
         }
         if (ks==0){//4.2 only single pobyte surr.��ͬɫ��,����Ϊ������
            System.out.println("//ks=0");
            gq=0;
	    for (i=1;i<=kss;i++){//4.1 deal surr point�������ڶ�����
	        hui[shoushu][12+i*2-1]=u[k0+i];//��¼�ϲ��ɿ�Ķ�����
                hui[shoushu][12+i*2]=v[k0+i];//��13��20
              for ( j=1;j<=(kss-i);j++){//�����֮��Ĺ���
                  gq+=dd(u[k0+i],v[k0+i],u[k0+i+j],v[k0+i+j]);
               }
            }
            zb[a][b][2]=(byte)(dang-gq);
            //zb[a][b][0]=tongse;

	    zb[a][b][3]=++ki;//count from first block
            hui[shoushu][0]=ki;//��¼���ɿ������
	    kuai[ki][0][0]=zb[a][b][2];
	    kuai[ki][0][1]=(byte)(k3+1);
            kuai[ki][k3+1][0]=a;//���һ��Ϊa,b
            kuai[ki][k3+1][1]=b;
            for ( i=1;i<=k3;i++){
	       m=u[k0+i];
               n=v[k0+i];
               kuai[ki][i][0]=m;
               kuai[ki][i][1]=n;
               zb[m][n][2]=zb[a][b][2];
               zb[m][n][3]=ki;
            }
       if (zb[a][b][2]!=jskq(ki)) System.out.println("error of breath");
            /*if(zb[a][b][2]==0){
               ktm=-1;
               ktn=-1;
               kzq(ki,yise);
            }*/
            //ci chu zi ti yi kuai
            //ci shi de de gong qi jin jin she ji dian
           //hen hao chu li.jian qu gong qi ji ke .  jian qu gong qi
         }
         if(ks>0){//�����п�
             System.out.println("//ks>0");
             ki++;
             hui[shoushu][0]=ki;
             kuai[ki][0][1]=1;//������ʱ��
             kuai[ki][1][0]=a;//a,b������λ
             kuai[ki][1][1]=b;
             zb[a][b][3]=ki;
	    for ( i=1;i<=kss;i++){//330
               hui[shoushu][12+i*2-1]=u[k0+i];
               hui[shoushu][12+i*2]=v[k0+i];
               dkhb(u[k0+i],v[k0+i],ki);//���ڵ㲢����ʱ��
            }
           // dkhb(a,b,k[1]);
            for ( j=1;j<=ks;j++){
               hui[shoushu][20+j]=k[j];
               kkhb(ki,k[j]);//not deal with breath���ϲ�,����δ����.
    	    }
           // hui[shoushu][0]=ki;
            //zb[a][b][2]=tongse;
            //kuai[k[1]][0][0]=zb[a][b][2];//? need deal with breath.
	    dang=jskq(ki);
             kdq(ki,dang);
            /*if(dang==0){
               hui[shoushu][0]=ki;
               kzq(ki,yise);
               ktm=-1;
               ktn=-1;
               hui[shoushu][0]=ki;
              // return;
            }*/
        }
   }








   //one pobyte was eaten
   void zzq(byte  a,byte  b,byte  tiao)//���ڻ���;������ʱ��������;
   {//function 6.1��֮��ĳ�ӱ������������.
      byte c1=0,i,j,yiseks=0;
      byte m1,n1;
      byte ysk[]={0,0,0,0};
      for(i=0;i<4;i++){
         m1=(byte)(a+szld[i][0]);
         n1=(byte)(b+szld[i][1]);
        if(zb[m1][n1][ZTXB]==tiao){
            c1= zb[m1][n1][KSYXB];
           if (c1==0){
               zb[m1][n1][QSXB]+=1;
            }
           else{
               for(j=0;j<yiseks;j++){
                 if(c1==ysk[j]) break;
               }
              if(j==yiseks){//���ظ�
                  ysk[yiseks++]=c1;
                  kdq(c1,kuai[c1][0][0]+=1);
	       }

	   }
        }
      }

      zb[a][b][0]=0;
      zb[a][b][2]=0;
      zb[a][b][3]=0;
   }
   public void kzq(byte r,byte tiao){//6.2 yi se kuai bei ti
   //�����ɫ��ʱ,ͬɫ����������
      byte n=0;
      byte p=0,q=0;
      n=kuai[r][0][1];
      for (byte i=1;i<=n;i++){
         p=kuai[r][i][0];
         q=kuai[r][i][1];
         zzq(p,q,tiao);
         //����ԭ����Ϣ,��Ҫ��������Ϣ,���ڻ���ʱ�ָ�
      }
      kuai[r][0][0]=0;

   }
   void zjq(byte a,byte b,byte tiao)//����ʱ���ӵĻָ�
   {//function 6.1
      byte c1=0,i,m1,j,n1,yiseks=0;
      byte ysk[]={0,0,0,0};
      for(i=0;i<4;i++){
         m1=(byte)(a+szld[i][0]);
         n1=(byte)(b+szld[i][1]);
        if(zb[m1][n1][ZTXB]==tiao){
            c1= zb[m1][n1][3];
           if (c1==0){
               zb[m1][n1][QSXB]-=1;
              if(zb[m1][n1][QSXB]<1)
                  System.out.println("����ʱ��������:a="+m1+",b="+n1);
            }
           else{
              for(j=0;j<yiseks;j++){
                 if(c1==ysk[j]) break;
               }
              if(j==yiseks){//���ظ�
                  ysk[yiseks++]=c1;
                  kdq(c1,kuai[c1][0][0]-=1);
	       }
            }
         }
      }


   }
   public void kjq(byte r,byte tiao){//����ʱ,�ɿ�ָ�ʹͬɫ�Ӽ���
      byte n=0;//the same color block is eaten
      byte p=0,q=0;//û������ʱ,tiaoֻ����ͬɫ.
       n=kuai[r][0][1];
      for (byte i=1;i<=n;i++){
         p=kuai[r][i][0];
         q=kuai[r][i][1];
         zjq(p,q,tiao);
      }
      kuai[r][0][0]=1;//�����ָ�,����Ϊ1.
   }
   public byte dd(byte a,byte b,byte c,byte d ){//7.1diang diang gong qi
      byte gq=0;// consider four points only.
      if (zb[a][d][0]==0)//�㹲��ֻ�����ֿ���,���λ��Ϊ����һ����
         gq++;//���߲��ؿ���,�ɵ�ǰ�ŵ��������м�,�����ظ���������.
      if (zb[c][b][0]==0)
         gq++;
      System.out.println("����dd,���㹲��="+gq+"\n");
      return gq;
   }

   //6.7diang kuai he bing
   public void dkhb(byte p,byte q,byte r){ //8.1
      byte ss=(byte)(kuai[r][0][1]+1);//���������1;
      kuai[r][ss][0]=p;
      kuai[r][ss][1]=q;
      kuai[r][0][1]=ss;
      zb[p][q][3]=r;
      System.out.println("����dkhb:���ϲ�\n");
   }
   //6.8 ss1 shi zu yao kuai!
   public void kkhb(byte r1,byte r2){//8.2����ǰ��,����δ��
      byte ss1=kuai[r1][0][1];
      byte ss2=kuai[r2][0][1];
      byte m=0,n=0;
      for (byte i=1;i<=ss2;i++){
	 m=kuai[r2][i][0];
         n=kuai[r2][i][1];
         zb[m][n][3]=r1;
        //����ԭ����Ϣ
         kuai[r1][ss1+i][0]=m;
         kuai[r1][ss1+i][1]=n;
      }
      kuai[r1][0][1]=(byte)(ss1+ss2);
      System.out.println("����dkhb:���ϲ�");
   }



   byte jskq(byte r2){
      byte qishu=0;   //the breath of the block
      byte a=0,b=0;
      byte m,n;
      byte zishu=kuai[r2][0][1];//�������
      byte i,j;
      for ( i=1;i<=zishu;i++){
         m=kuai[r2][i][0];
         n=kuai[r2][i][1];
         for(j=0;j<4;j++){
            a=(byte)(m+szld[j][0]);
            b=(byte)(n+szld[j][1]);
           if (zb[a][b][ZTXB]==KONGDIAN&&zb[a][b][SQBZXB]==0){
               qishu++;
               zb[a][b][1]=1;
               kuai[r2][49+qishu][0]=a;//�洢�����±�50��ʼ
               kuai[r2][49+qishu][1]=b;
            }
         }
      } //for

      for ( i =1;i<=qishu;i++){//�ָ���־
         a=kuai[r2][49+i][0];
         b=kuai[r2][49+i][1];
         zb[a][b][SQBZXB]=0;
      }
      return qishu;
   }//2��22�ո�,ԭ��������,��ֻ����ʹ�.
//10.1ji suan kuai qi.


   public void kdq(byte kin,byte a){//11.1 kuai ding qi�鶨��
      byte m=0,n=0,p=0;
      p=kuai[kin][0][1];
      for (byte i=1;i<=p;i++){
          m=kuai[kin][i][0];
          n=kuai[kin][i][1];
          zb[m][n][2]=a;
      }
      kuai[kin][0][0]=a;
   }





   public void clhuiqi(){//�Ƿ��������ݽṹ���ָܻ�?
      byte p=0;
      byte yise=0;
      byte tongse=0;//yise is diff color.and 2 same.
      byte tdzs=0;
      byte k0=0,k1=0,k2=0,k3=0,i=0,j=0;//the count for three kinds of point.
      byte ks=0,kss=0;//ks is count for block,kss for single point
      byte kin, kin1=0,m=0,n=0;//the block index.

      tongse=(byte)((shoushu+1)%2+1);//tong se
      yise=(byte)(shoushu%2+1);
      m=hui[shoushu][25];
      hui[shoushu][25]=0;
      n=hui[shoushu][26];
      hui[shoushu][26]=0;
      if(m<=0||n<=0){//��Ȩ�Ļָ�
         shoushu--;
        return;//
      }
      zzq(m,n,yise);//����,�Է�����,����ֱ�ӻָ�,�����ڴ�����
      System.out.println("����:"+shoushu);
      System.out.println("a="+m+",b="+n);
      kin=hui[shoushu][0];
      if(kin>0){//�Ƿ���¿�,�Դ˴�������
         for(i=0;i<70;i++){
            kuai[kin][i][0]=0;
            kuai[kin][i][1]=0;
         }
         ki=kin;//ȫ�ֿ��ÿ��?
         for(i=1;i<=4;i++){
           if(hui[shoushu][2*i+12-1]<0){//���¿�ĵ�
              break;
            }
           else{
               m=hui[shoushu][12+2*i-1];//13-20
               n=hui[shoushu][12+2*i];
               hui[shoushu][12+2*i-1]=0;
               hui[shoushu][12+2*i]=0;
               zb[m][n][3]=0;
               zb[m][n][0]=tongse;//fang wei bian cheng
               zb[m][n][2]=jszq(m,n);//�����ӵ���
               System.out.println("//����ɿ�����:"+"a="+m+",b"+n);
            }
         }//deal with 3 sub
         for(i=1;i<=4;i++){//�Ƿ�ɿ���¿�
            kin1=hui[shoushu][20+i];//21-24
            hui[shoushu][20+i]=0;
           if(kin1==0)
              break;
           else{
               p=kuai[kin1][0][1];
              for(j=1;j<=p;j++){
                  m=kuai[kin1][j][0];
                  n=kuai[kin1][j][1];
                  zb[m][n][3]=kin1;//�޸Ŀ��
                  //zb[m][n][0]=tongse;
                  zb[m][n][2]=kuai[kin1][0][0];//�ָ�ԭ��ɿ�ʱ����
               }
            }//else
         }//for
      }//if �Ƿ��¿�
      for(i=1;i<=4;i++){//�Ƿ�����
         if(hui[shoushu][2*i-1]<=0)
            break;
         else{
            m=hui[shoushu][2*i-1];
            n=hui[shoushu][2*i];
            hui[shoushu][2*i-1]=0;
            hui[shoushu][2*i]=0;
            tdzs=i;//?
            zb[m][n][ZTXB]=yise;
            zb[m][n][QSXB]=1;
            zb[m][n][KSYXB]=0;
            zjq(m,n,tongse);
            System.out.print("�ָ�������:");
            System.out.println("a="+m+",b="+n);
         }
      }//for

      for(i=1;i<=4;i++){//�Ƿ��б���Ŀ�
         if(hui[shoushu][8+i]<=0){
            break;
         }
         else{
            kin1=hui[shoushu][8+i];
            hui[shoushu][8+i]=0;
            kdq(kin1,(byte)1);
            kjq(kin1,tongse);
            p=kuai[kin1][0][1];
           for(j=1;j<=p;j++){
               m=kuai[kin1][j][0];
               n=kuai[kin1][j][1];
               zb[m][n][0]=yise;
               zb[m][n][3]=kin1;
            }
            tdzs+=p;
         }//else
      }//for
      if(tongse==BLACK)
         ktb-=tdzs;
      if(tongse==WHITE)
         ktw-=tdzs;
       for(i=0;i<9;i++){
         hui[shoushu][27+i]=0;//2yue
      }
      shoushu--;
      System.out.println("����clhuiqi:�������\n");
   }//clhuiqi

   public GoBoard() {
        byte i;
      final byte  PANWAIDIAN=-1;//����֮��ı�־;
      for(i=0;i<21;i++){//2��22�ռ�
         zb[0][i][0]=PANWAIDIAN;
         zb[20][i][0]=PANWAIDIAN;
         zb[i][0][0]=PANWAIDIAN;
         zb[i][20][0]=PANWAIDIAN;
      }//2��22�ռ�
   }
   byte jszq(byte m,byte n){
      byte dang=0;//��������
      byte i,a,b;
      for(i=0;i<4;i++){
          a=(byte)(m+szld[i][0]);
          b=(byte)(n+szld[i][1]);
         if(zb[a][b][ZTXB]==KONGDIAN){//2.1the breath of blank
            dang++;
          }
      }
       return dang;
   }
}