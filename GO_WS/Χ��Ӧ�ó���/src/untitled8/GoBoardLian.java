package untitled8;
//5��26�ն��壻���庯��δ����
public class GoBoardLian {
   public final boolean DEBUG=true;
   public final byte  ZBSX=20;  //������������;
   public final byte  ZBXX=0;   //������������;

   public final byte  BLANK=0;
   public final byte  BLACK=1;  //1��ʾ����;
   public final byte  WHITE=2;  //2��ʾ����;

   public final byte[][] szld={{1,0},{0,-1},{-1,0},{0,1}};
  //�����������ӵ�,˳��ɵ�.��-��-��-��
   public final byte  ZTXB=0;   //�±�0�洢״ֵ̬;
   public final byte  SQBZXB=1; //�±�1�洢������־;
   public final byte  QSXB=2;   //�±�2�洢����;
   public final byte  KSYXB=3;  //�±�3�洢������

   public byte ki=0;       //��ǰ�Ѿ��õ��Ŀ��,��ǰ����;
   public short shoushu=0; //��ǰ����,����֮ǰ����.��1��ʼ;
   public byte ktb=0,ktw=0;//�ڰױ����Ӽ���
   public byte[][][] zb = new byte [21][21][4];
   //0:����״̬:��1��2;   //1:�������ı�־;   //2:����;
   //3:������;     //ǰ��ά�����̵�����,�����±��1��19;
   public byte  [][] hui=new byte [512][38];//0=�¿�����;
   //��¼��ֵĹ�����Ϣ,���ڻ���;1-8Ϊ���Ե��ӵ�����;9-12Ϊ���Կ������;
   //13-20Ϊ�ɿ��ӵ�����,21-24Ϊ���¿�ľɿ�����.27-32Ϊ��Ե�,33-35Ϊ
   //��ԵĿ�����;36.37Ϊ���ŵ�.25.26Ϊ�ò��������.
   public KuaiBiaoXiang[] kuai=new KuaiBiaoXiang[128];

   public byte jskq(byte r){
      //��������������ɿ��������Ϣ��������������
      byte qishu=0;      //����������Ӵ��Ѿ�ȷ����
      byte a=0,b=0;
      byte m,n;
      byte i,j;
      byte zishu=kuai[r].zishu;//�������
      DianNode temp=kuai[r].zichuang;
      DianNode qich;
      for ( i=1;i<=zishu;i++){
         m=temp.a;
         n=temp.b;
         zb[m][n][KSYXB]=r;
         for(j=0;j<4;j++){
            a=(byte)(m+szld[j][0]);
            b=(byte)(n+szld[j][1]);
            if (zb[a][b][ZTXB]==BLANK&&zb[a][b][SQBZXB]==0){
               qishu++;
               zb[a][b][SQBZXB]=1;
               //���������������
               qich=new DianNode();
               qich.a=a;
               qich.b=b;
               qich.next=kuai[r].qichuang;
               kuai[r].qichuang=qich;
            }
         }
         temp=temp.next;
      } //for
      kuai[r].qishu=qishu;

      qich=kuai[r].qichuang;
      for ( i =1;i<=qishu;i++){//�ָ���־
         a=qich.a;
         b=qich.b;
         zb[a][b][SQBZXB]=0;
         qich=qich.next;
      }
      kdq(ki,qishu);
      return qishu;
   }//2��22�ո�,ԭ��������,��ֻ����ʹ�.

   public void kdq(byte kin,byte a){//�鶨��
      byte m=0,n=0,p=0;//�����һ��ʱ�á�
      p=kuai[kin].zishu;
      DianNode temp=kuai[kin].zichuang;
      for (byte i=1;i<=p;i++){
         m=temp.a;
         n=temp.b;
         zb[m][n][QSXB]=a;
         zb[m][n][KSYXB]=kin;
         temp=temp.next;
      }
      kuai[kin].qishu=a;
   }
   public void kjq(byte r,byte tiao){//����ʱ,�ɿ�ָ�ʹͬɫ�Ӽ���
      byte n=0;//the same color block is eaten
      byte p=0,q=0;//û������ʱ,tiaoֻ����ͬɫ.
      n=kuai[r].zishu;
      DianNode temp=kuai[r].zichuang;
      for (byte i=1;i<=n;i++){
         p=temp.a;
         q=temp.b;
         zjq(p,q,tiao);
         temp=temp.next;
      }
      kuai[r].qishu=1;//�����ָ�,����Ϊ1.
   }

   public void dkhb(byte p,byte q,byte r){ //8.1
      DianNode temp=new DianNode();
      temp.a=p;
      temp.b=q;
      temp.next=kuai[r].zichuang;
      kuai[r].zichuang=temp;
      kuai[r].zishu+=1;//���������1��
      zb[p][q][3]=r;
      System.out.println("����dkhb:���ϲ���");
   }

   public void kkhb(byte r1,byte r2){//8.2����ǰ��,����δ��
      DianNode temp=kuai[r1].zichuang;
      byte m=0,n=0;
      System.out.println("����kkhb:���ϲ�");
      for (byte i=1;i<kuai[r1].zishu;i++){
         temp=temp.next;
      }
      temp.next=kuai[r2].zichuang;
      kuai[r1].zishu+=kuai[r2].zishu;
      System.out.println("����kkhb:���ϲ�\n");
   }

   public boolean validate(byte a, byte b){
      byte m,n,qi=0;
      //��shoushu����֮ǰ���ã�yise��tongse�ļ���������ͬ��
      byte tongse=(byte)(shoushu%2+1);//yi se=1��2,������Ϊ����
      byte yise=(byte)((1+shoushu)%2+1);//tong se=1��2,�׺���Ϊż��
      if (a>ZBXX&&a<ZBSX&&b>ZBXX&&b<ZBSX&&zb[a][b][ZTXB]==BLANK){
         //�±�Ϸ�,�õ�հ�
         if(a==hui[shoushu][36]&&b==hui[shoushu][37]){//�Ƿ���ŵ�
            System.out.print("���Ǵ��ʱ�Ľ��ŵ�,�����ҽٲ�!");
            System.out.println("���Ϊ��a=" + a + ",b=" + b);
            return  false;
         }else{
            //System.out.println("���Ϊ��a=" + a + ",b=" + b);
            for(byte i=0;i<4;i++){
               m = (byte) (a + szld[i][0]);
               n = (byte) (b + szld[i][1]);
               if (zb[m][n][ZTXB] == BLANK){
                  return true;
               }
               else if(zb[m][n][ZTXB]==yise){
                  if(zb[m][n][QSXB]==1) return true;//todo
               }
               else if(zb[m][n][ZTXB]==tongse){
                  if(zb[m][n][QSXB]>1) {
                     return true;
                  }
                  else{
                     qi+=zb[m][n][QSXB];
                     qi--;
                  }
               }
            }
            if(qi==0) {
               System.out.print("������ɱ�Ľ��ŵ㣺");
               System.out.println("a=" + a + ",b=" + b);
               return false;
            }
            else{
               System.out.print("���ǺϷ��ŵ㣺");
               System.out.println("a=" + a + ",b=" + b);
               return true;
            }
         }
      }
      else{//��һ�಻�Ϸ���.
         System.out.print("�õ㲻�Ϸ�,������֮����߸õ��Ѿ����ӣ�");
         System.out.println("a="+a+",b="+b);
         return false;
      }
   }
   public void output(){
      DianNode temp=null;
      DianNode first=null;
      byte zishu=0;
      byte qishu=0;
      byte i,j;
      byte m,n;
      for(i=1;i<=ki;i++){
         temp=kuai[i].zichuang;
         zishu=kuai[i].zishu;
         System.out.print("���:"+i+"������:"+zishu);
         for(j=1;j<=zishu;j++){
            m=temp.a;
            n=temp.b;
            System.out.print("("+m+","+n+")");
            temp=temp.next;
         }
         System.out.println("");
      }
      for(i=1;i<=ki;i++){
         temp=kuai[i].qichuang;
         qishu=kuai[i].qishu;
         System.out.print("���:"+i+"������:"+qishu);
         for(j=1;j<=qishu;j++){
            m=temp.a;
            n=temp.b;
            System.out.print("("+m+","+n+") ");
            temp=temp.next;
         }
         System.out.println("");
      }
      System.out.print("ki="+ki+";shoushu="+shoushu);
      System.out.println(";ktw="+ktw+";ktb="+ktb);
   }

   public void zjq(byte a,byte b,byte tiao)//����ʱ���ӵĻָ�
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
                  delete(c1,a,b);
                  kdq(c1,kuai[c1].qishu-=1);
	       }
            }
         }
      }
   }
   public void zzq(byte  a,byte  b,byte  tiao)
   //���ڻ��壨�൱�����ӱ��ᣩ;����������ʱ��������;
   //��֮��ĳ�ӱ�������Է�������.tiaoָ���ӷ�����ɫ��
   {
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
                  kdq(c1,kuai[c1].qishu+=1);
                  DianNode temp=new DianNode();
                  temp.a=a;
                  temp.b=b;
                  temp.next=kuai[c1].qichuang;
                  kuai[c1].qichuang=temp;
	       }
	    }
         }
      }
      zb[a][b][ZTXB]=BLANK;
      zb[a][b][QSXB]=0;
      zb[a][b][KSYXB]=0;
   }
   public void kzq(byte r,byte tiao){//6.2 yi se kuai bei ti
   //�����ɫ��ʱ,ͬɫ����������
      byte n=0;
      byte p=0,q=0;
      n=kuai[r].zishu;
      DianNode temp=kuai[r].zichuang;
      for (byte i=1;i<=n;i++){
         p=temp.a;
         q=temp.b;
         zzq(p,q,tiao);
         temp=temp.next;
         //����ԭ����Ϣ,��Ҫ��������Ϣ,���ڻ���ʱ�ָ�
      }
      kuai[r].qishu=0;
   }

   public boolean cgcl(byte  c){//true ��ʾ����
      byte a=(byte)((c-1)%19+1);
      byte b=(byte)((c-1)/19+1);
      return cgcl(a,b);
   }//�ṩһά����Ľӿ�

   public void delete(byte r,byte a,byte b){
      //���ӿ��������ɾ��һ���������ꡣ
      if(r<0) r=(byte)(0-r);
      DianNode temp=kuai[r].qichuang;
      DianNode forward=kuai[r].qichuang;
      byte qishu=kuai[r].qishu;
      for (byte i=1;i<=qishu;i++){
         if(a==temp.a&b==temp.b){
            if(i==1){
               kuai[r].qichuang=temp.next;

            }else{
               forward.next=temp.next ;
            }
         }
         else{
            forward=temp;
            temp=temp.next;
         }
      }
   }

   public boolean cgcl(byte  a, byte  b){//chang gui chu li
   //���Խ��ܵ�����Ϊ(a,b)��c;c=b*19+a-19;�����������
   //a����������±�,Ҳ��ƽ��ĺ�����:1-19
   //b����������±�,Ҳ����Ļ��������:1-19
   //byte c;//a,b��һά��ʾ:1-361;
      byte  m1=a;      //a,b�ڷ����в��ı�
      byte  n1=b;      //m1,n1Ϊa,b���ڵ�.
      byte  yise=0;    //��ɫ
      byte  tongse=0;  //ͬɫ
      byte  k1=0,k2=0,k3=0;
      //���ֵ�ļ���,k1Ϊ��ɫ�����,k2Ϊ�������,k3Ϊͬɫ�����
      byte  ks=0,kss=0;//���ڵĳɿ�����Ͷ���������
      byte i=0,j=0;
      byte  ktz=0;     //���Ӽ���,�ֲ�
      byte  tzd=0,tkd=0;//�������ͳɿ����
      byte  kin1=0;    //a,b��Χ�ĵ�Ŀ�����
      byte [] tsk=  {0,0,0,0,0};//��ͬɫ���ӵĿ�����
      byte [] ysk={0,0,0,0};//����ɫ���ӵĿ�����,ͬ�鲻�ظ�����
      byte yiseks=0;   //������ɫ����



      if(validate(a,b)==false) return false;
      System.out.println("come into method cgcl()");
      System.out.println("������ɫ����");
      hui[++shoushu][25]=a;//��������ǰ����,����1��ʼ����.������ͬ.
      hui[shoushu][26]=b;  //��¼ÿ�������
      yise=(byte)(shoushu%2+1);//yi se=1��2,������Ϊ����
      tongse=(byte)((1+shoushu)%2+1);//tong se=1��2,�׺���Ϊż��
      zb[a][b][ZTXB]=tongse;//���Զ�̬һ��

      for(i=0;i<4;i++){//�ȴ�����ɫ����
         byte bdcds=0;//����Ե����.
         byte bdcks=0;//����Կ����.
         m1=(byte)(a+szld[i][0]);
         n1=(byte)(b+szld[i][1]);
         if (zb[m1][n1][ZTXB]==yise) {//1.1�ұ����ڵ�
            k1++;//��ɫ�����
            kin1=zb[m1][n1][KSYXB];//������
            if (kin1==0){      //���ǿ�
               zb[m1][n1][QSXB]-=1;
               if(zb[m1][n1][QSXB]==0){//eat the diff point
                  k1--;//�����Ҫ��ȥ
                  tzd++;
                  hui[shoushu][tzd*2-1]=m1;
                  hui[shoushu][tzd*2]=n1;
                  System.out.println("����:("+m1+","+n1+")");
                  ktz++;  //���Ӽ���
                  zzq(m1,n1,tongse);//ͬɫ�ӣ���Ե�ǰ�֣�������.
               }
               else if(zb[m1][n1][QSXB]==1){
                  hui[shoushu][27+bdcds++]=m1;
                  hui[shoushu][27+bdcds++]=n1;
                  System.out.println("("+m1+","+n1+")"+"�����");
               }
               else if(zb[m1][n1][QSXB]<0){
                  System.out.println("��������:a="+m1+",b="+n1);
                  return false;
               }
               else{
                  System.out.println("("+m1+","+n1+")"+"������Ϊ"+zb[m1][n1][QSXB]);
               }
            }
            else{//if (kin1==0)Ϊ�顣
               for(j=0;j<yiseks;j++){
                  if(kin1==ysk[j]) break;
               }
               if(j==yiseks){//���ظ�
                  ysk[yiseks++]=kin1;
                  byte qi=(byte)(kuai[kin1].qishu-1);
                  delete(kin1,a,b);
                  kdq(kin1,qi);
                  if (kuai[kin1].qishu==0){
                     k1--;
                     tkd++;//<=4
                     hui[shoushu][8+tkd]=kin1;
                     ktz+=kuai[kin1].zishu;//ʵ�ʵ�������
                     System.out.println("�鱻�ԣ����Ϊ��"+kin1);
		     kzq(kin1,tongse); //��ɫ�鱻��,ͬɫ������.
                  }
                  else if (kuai[kin1].qishu==1){
                     hui[shoushu][33+bdcks++]=kin1;
                     System.out.println("�鱻��ԣ����Ϊ��"+kin1);
                  }
                  else if(kuai[kin1].qishu<0){
                     System.out.println("��������:kin="+kin1);
                     return false;
                  }
                  else{
                     System.out.println("��"+kin1+"������Ϊ"+zb[m1][n1][QSXB]);
                  }
               }//���ظ���
            }//if kuai
         }// if==yiseks
      }//��ѭ������



      zb[a][b][QSXB]=0;//��ֹ����ʱ������.
      if(shoushu%2==BLACK) ktb+=ktz;
      else ktw+=ktz;//���ֲ����Ӽ���

      System.out.println("����հ�����");
      for(i=0;i<4;i++){//�ٴ���հ�����
         m1=(byte)(a+szld[i][0]);
         n1=(byte)(b+szld[i][1]);
         if(zb[m1][n1][ZTXB]==BLANK){//2.1the breath of blank
            k2++;//�������
         }
      }
      System.out.println("����ͬɫ����");
      for(i=0;i<4;i++){         //�ٴ���ͬɫ����
         m1=(byte)(a+szld[i][0]);
         n1=(byte)(b+szld[i][1]);
         if(zb[m1][n1][ZTXB]==tongse){//3.1
            k3++;              //ͬɫ�����
            kin1= zb[m1][n1][KSYXB];
            if (kin1==0){      //������
               kss++;          //ͬɫ���������
               hui[shoushu][12+kss*2-1]=m1;//��¼�ϲ��ɿ�Ķ�����
               hui[shoushu][12+kss*2]=n1;
            }
            else{              //�ɿ��
               for(j=0;j<ks;j++){
                  //if(kin1==tsk[j]) break;
                  if(kin1==hui[shoushu][21+j]) break;
               }//������ͻ�Ĳ�ʹ��ѵ
               if(j==ks){      //���ظ�
                  //tsk[ks++]=kin1;
                  //hui[shoushu][20+ks]=kin1;
                  hui[shoushu][21+ks++]=kin1;
               }
            }//�ɿ��
         }
      }
      System.out.println("ͬɫ��k3="+k3);

      if (k3==0){//4.1 û��ͬɫ�ڵ�
         System.out.println("û��ͬɫ�ڵ�");
         zb[a][b][2]=k2;
         if(k2==1&&ktz==1){//���ǽ�
            hui[shoushu][36]=hui[shoushu][1];
            hui[shoushu][37]=hui[shoushu][2];
         }//

      }
      else{//4.2 ��ͬɫ��
         if(ks==0)
            System.out.println("��ͬɫ��,����Ϊ������");
         if(ks>0)//�����п�
            System.out.println("��ͬɫ��,�Ҳ���Ϊ��");

         zb[a][b][KSYXB]=++ki;//count from first block
         hui[shoushu][0]=ki;//��¼���ɿ������
         DianNode temp=new DianNode();
         KuaiBiaoXiang linshi=new KuaiBiaoXiang();
         temp.a=a;
         temp.b=b;
         linshi.color=tongse;
         linshi.zishu=1;
         linshi.zichuang=temp;
         kuai[ki]=linshi;

         for (i=1;i<=kss;i++){//�������ڶ�����
            //��¼�ϲ��ɿ�Ķ�����(��13��20)
            m1=hui[shoushu][12+i*2-1];
            n1=hui[shoushu][12+i*2];
            dkhb(m1,n1,ki);
         }
         for ( j=1;j<=ks;j++){
            //hui[shoushu][20+j]=tsk[j-1];
            kkhb(ki,hui[shoushu][20+j]);
            //kkhb(ki,tsk[j-1]);//���ϲ�,����δ����.
         }
         jskq(ki);

      }
       if(ktz>0) return true;
       else return false;
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
      if(kin>0){//kin���³ɵĿ�
         /*for(i=0;i<70;i++){
            kuai[kin][i][0]=0;
            kuai[kin][i][1]=0;
         }*/
         kuai[kin].qishu=0;
         kuai[kin].zishu=0;
         kuai[kin].zichuang =null;
         kuai[kin].qichuang =null;
         ki=kin;//ȫ�ֿ��ÿ��?
         ki--;//xinzeng.
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
               p=kuai[kin1].zishu;
               DianNode temp=kuai[kin1].zichuang;
               for(j=1;j<=p;j++){
                  m=temp.a;
                  n=temp.b;
                  zb[m][n][3]=kin1;//�޸Ŀ��
                  //zb[m][n][0]=tongse;
                  zb[m][n][2]=kuai[kin1].qishu;//�ָ�ԭ��ɿ�ʱ����
                  temp=temp.next;
               }
               jskq(kin1);//cunchukuaiqi;
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
            p=kuai[kin1].zishu;
            DianNode temp=kuai[kin1].zichuang;
           for(j=1;j<=p;j++){
               m=temp.a;
               n=temp.b;
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
   public GoBoardLian() {
        byte i,j;
      final byte  PANWAIDIAN=-1;//����֮��ı�־;
      for(i=0;i<21;i++){//2��22�ռ�
         zb[0][i][0]=PANWAIDIAN;
         zb[20][i][0]=PANWAIDIAN;
         zb[i][0][0]=PANWAIDIAN;
         zb[i][20][0]=PANWAIDIAN;
      }//2��22�ռ�
   }

   public byte jszq(byte m,byte n){//huiqishiyong.
      byte dang=0;//��������
      byte i,a,b;//����ָ�ʱ����ɢ�����ɵ�����������㣻
      for(i=0;i<4;i++){
          a=(byte)(m+szld[i][0]);
          b=(byte)(n+szld[i][1]);
          if(zb[a][b][ZTXB]==BLANK){//2.1the breath of blank
            dang++;
          }
      }
       return dang;
   }
}



class DianNode{//�������������ʽ���ݽṹ
   byte a;
   byte b;
   DianNode next;
   public DianNode(byte ta, byte tb,DianNode tnext ){
      a=ta;
      b=tb;
      next=tnext;
   }
   public DianNode(){
      a=0;
      b=0;
      next=null;
   }
}
class ZiBiaoXiang{
   byte qishu;//
   DianNode qichuang;
}

/*byte dds=dingdianshu(a,b);
     byte qikuaishu=0;
     byte qikuaidian[][]=new byte[4][2];

     if(k2==2){
        if((u[k0+k2]-u[k0+1])*(v[k0+k2]-v[k0+1])==1){//б�Խ�
           if(zb[u[k0+k2]][v[k0+1]][ZTXB]==BLANK||
           zb[u[k0+1]][v[k0+k2]][ZTXB]==BLANK){//���Ƿ�Ͽ�
              qikuaidian[qikuaishu][0]=u[k0+k2];
              qikuaidian[qikuaishu++][1]=v[k0+k2];
              //qikuaishu=1;
           }
           else{
              qikuaidian[qikuaishu][0]=u[k0+1];
              qikuaidian[qikuaishu++][1]=v[k0+1];
              qikuaidian[qikuaishu][0]=u[k0+k2];
              qikuaidian[qikuaishu++][1]=v[k0+k2];
              //qikuaishu=2;
           }
        }
        else {
           //qikuaishu=2;
           qikuaidian[qikuaishu][0]=u[k0+1];
           qikuaidian[qikuaishu++][1]=v[k0+1];
           qikuaidian[qikuaishu][0]=u[k0+k2];
           qikuaidian[qikuaishu++][1]=v[k0+k2];
        }
     }
     else if(k2==3){
        if(zb[u[k0+2]][v[k0+1]][ZTXB]==BLANK||
        zb[u[k0+1]][v[k0+2]][ZTXB]==BLANK){
           //qikuaishu=1;
           qikuaidian[qikuaishu][0]=u[k0+1];
           qikuaidian[qikuaishu++][1]=v[k0+1];
        }
        else{
           qikuaidian[qikuaishu][0]=u[k0+1];
           qikuaidian[qikuaishu++][1]=v[k0+1];
           qikuaidian[qikuaishu][0]=u[k0+2];
           qikuaidian[qikuaishu++][1]=v[k0+2];
           //qikuaishu=2;
        }
        if(zb[u[k0+2]][v[k0+3]][ZTXB]==BLANK||
        zb[u[k0+3]][v[k0+2]][ZTXB]==BLANK){
           System.out.println("����ȫ����");
        }
        else{
           qikuaidian[qikuaishu][0]=u[k0+k3];
           qikuaidian[qikuaishu++][1]=v[k0+k3];
           //qikuaishu++;
        }
     }
     else if(k2==4){
        if(dds==2){
        }
        if(dds==3){
        }
        if(dds==4){
        //todo:ȷ���������ԭʼ��.
        }

     }//4��18��
     for(i=0;i<4;i++){
        m1=qikuaidian[i][0];
        n1=qikuaidian[i][1];

        if(m1!=0){
           zishu=0;
           chengkuai(m1,n1,BLANK);
           if(zishu==kuai[zb[a][b][KSYXB]][0][1]-1){
              kuai[zb[a][b][KSYXB]][0][1]-=1;
              break;
           }
           else{

           }
        }
        else break;

     }*/
     //���ݳɿ��㷨ͳ������,���Ƿ������¿�
