package eddie.wu.ui.applet;
/**
 * <p>Title:围棋死活题小程序 </p>
 * <p>Description: 用于死活题训练</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: SE</p>
 * @author wueddie
 * @version 1.0
 */
import java.awt.*;
import java.applet.Applet;
import java.awt.event.*;
import java.io.*;
import java.net.*;
/**
 * 需要在网络环境下作为Applet来运行。当初是给汪剑虹围棋道场开发的。
 * @author wueddie-wym-wrz
 *
 */
public class SiHuoApplet extends Applet {
   int czhengjie=0;//zheng jie de biao zhi?
   boolean fshuzi=false;
   boolean HUIQICH=false;// hui qi shi chong hua
   boolean ZHENGCHANGCH=false;//cong wen jian da kai shi chong hua
   //int guize=0;//0:my regular;1:chinese regular;2:japanese regular;
         	//3:south korea regular; 4:ying's regular
   //int [][] wyyd=new int [40][2];
   //int wyydj=0;
   boolean fbuhelidian=false;
   boolean ftishi=false;
   int[][][] bianhua=new int[50][50][2];
   String [][] jshuo=new String[50][50];
   Tree gotree=new Tree();
   TreeNode gotemp;
   TreeNode old=null;//当前节点的父节点。

   int linss;//原始局面的手数。
   boolean isStandalone = false;
   int shoushu=0;//dang qian shou shu,caution:it increase before deal
   int a=-1;//a is the row subscript of the point.
   //int tbshoushu=0;
   int b=-1;//b is the column subscript of the point.
   int bhlda=-1,bhlidb=-1;
   boolean fchonghua=false;
   int ki=0,tbki=0;
   int ktm=-1,ktn=-1,tbktm=-1,tbktn=-1;// and position for points eaten.
   int ktb=0,ktw=0,tbktb=0,tbktw=0;  // the count for black and white point eaten.
   int[][][] zb = new int[19][19][4];//0:state;2:breath;3:blockindex
   int[][][] kuai = new int[100][50][2];//mei yi kuai de ge zi zuo biao
   int [][] hui=new int[400][27];//0=zi ti yi kuai,1~8four single point
   //eaten,9~12 kuai suo ying of fou block eaten.13~24is same ,25,26a,b
   //int[][][]tbzb = new int[19][19][4];
   //int[][][]tbkuai = new int[100][50][2];
   //int [][] tbhui=new int[400][27];
   int chuiqi=0;// hui qi de bu shu.
   int cqianjin=0;
   int [][][] huitemp=new int[2][50][2];
   int [][]tizi=new int[50][2];
   int [][]luozi=new int[50][2];
   Button tishi=new Button("提示");
   Button huiqi=new Button("悔棋");
   Button conglai=new Button("重来");
   Button yanshi=new Button("演示");
   TextArea jieshuo= new TextArea(5,10);







   public void init(){
      int i,j=-1;
      int p=0;// bian hua de shu mu
      byte [][] jsweizhi=new byte[50][2];// jie shuo xianzhi zai 50 shou.
      byte [] qipubh=new byte[500];
       String qipuwjm;
      String jieshuowjm;
      qipuwjm=getParameter("qipuwenjianming");
      System.out.println("qipuwenjianming="+qipuwjm);
      jieshuowjm=getParameter("jieshuowenjianming");
      System.out.println("jieshuowenjinaming="+jieshuowjm);
      try{
         URL base=this.getCodeBase();
         URL dbase=this.getDocumentBase() ;
         String sdbase=dbase.toString();
         int index=sdbase.lastIndexOf("/");
         sdbase=sdbase.substring(0,index+1);
         System.out.println("CodeBase="+base);
         System.out.println("documentBase="+sdbase);
         URL urlbase=new URL( base+qipuwjm);
         //URL urlbase=new URL( sdbase+qipuwjm);
         URLConnection uconn=urlbase.openConnection();
         InputStream uc=uconn.getInputStream();
         uc.read(qipubh);
         //System.out.println("ss="+1) ;
         boolean qipujieshu=false;
         //int [][]jsweizhi=new int[30][2];
         for (i=0;i<500;i++){
              j++;
            //if((int)bianhua[i]==-1) break;
            System.out.println("a="+qipubh[i]) ;
            if(qipujieshu==true){
               if(qipubh[i]==100) break;
                jsweizhi[j][0]=qipubh[i];
                System.out.println("jieshuoweizhia="+qipubh[i]);
                jsweizhi[j][1]=qipubh[++i];
                System.out.println("jieshuoweizhib="+qipubh[i]);
                continue;
            }
           if(qipubh[i]==100) break;
           else if(qipubh[i]==90) {
               p++;//xia yi ge bian hua.
               j=-1;
              continue;
            }else if(qipubh[i]==95){
                 qipujieshu=true;
                 j=-1;

            }
           else {
                  bianhua[p][j][0]=qipubh[i];
                  bianhua[p][j][1]=qipubh[++i];

             }
             //System.out.println("b="+qipubh[++i]) ;
         }
         uc.close() ;

         URL urlbasejs=new URL(base+jieshuowjm);
         URLConnection uconnjs=urlbasejs.openConnection();
         InputStream ucjs=uconnjs.getInputStream();
         DataInputStream js=
           new DataInputStream(
              new BufferedInputStream(ucjs));
          System.out.println("js="+js) ;
          BufferedReader buffin=
           new BufferedReader(new InputStreamReader(js));
            String s=new String();
            String [] tempstr=new String[50];
            j=0;
           while((s=buffin.readLine())!=null){
             //  ta.append(s);
              //int c;

              jshuo[jsweizhi[j][0]][jsweizhi[j][1]]=s;
              tempstr[j]=s;
              j++;
             System.out.println(s) ;
          }
          buffin.close ();

          /*for(i=0;i<50;i++){
              for (j=0;j<50;j++){
                   if(jshuo[i][j]!=null)  System.out.println(jshuo[i][j]) ;
              }
          }*/
             /*  StringReader in2=
                new StringReader(s);


                 while((c=in2.read())!=-1){
                      System.out.println((char)c);
                  }
               */


       /*  BufferedReader buffin=
               new BufferedReader(new InputStreamReader(uc));
         String s;
         //a=buffin.readInt
         while((s=buffin.readLine())!=null){
            jieshuo.append(s);
         }*/
      }catch(Exception e){
          e.printStackTrace() ;

      }
       gotree.insert(bianhua,jshuo);
       gotemp=gotree.getTreeNode();
       if(gotemp==null) System.out.println("gotemp is null");
       else{
          while(gotemp!=null){
               System.out.println("a="+gotemp.zba);
               System.out.println("b="+gotemp.zbb);
               gotemp=gotemp.left;
          }

          gotemp=gotree.getTreeNode();
       }
       setLayout(null);
       add(tishi);
      add(huiqi);
       add(conglai);
       add(yanshi);
       add(jieshuo);
       tishi.reshape(550,15,60,26);
       huiqi.reshape(550,60,60,26);
       conglai.reshape(550,110,60,26);
       yanshi.reshape(640,110,60,26);
       jieshuo.reshape(550,160,180,30);
       jieshuo.setText("提示按钮可以逐手给出提示\n重来按钮可以恢复到初始局面\n如果走错，直接点另一点即可");

       for(i=1;i<400;i++){
          for(j=1;j<=8;j++){
               hui[i][j]=-1;
           }
          for(j=13;j<=20;j++){
              hui[i][j]=-1;
           }
           hui[i][25]=-1;
           hui[i][26]=-1;
        }
        for(i=0;i<2;i++){
          for(j=0;j<50;j++){
              huitemp[i][j][0]=-1;
              huitemp[i][j][0]=-1;
           }
        }
        linss=bianhua[0][0][0];
        for(i=1;i<=linss;i++){

                  a=bianhua[0][i][0];
                  b=bianhua[0][i][1];
                  System.out.print("a="+a);
                  System.out.print("b="+b);
                  System.out.print("lins="+i);
                if(a<0||b<0){
                          shoushu++;
                          hui[shoushu][25]=-1;
                          hui[shoushu][26]=-1;
                  }else{
                      cgcl();
                  }
            }
            a=-1;
            b=-1;
    }

   public void update (Graphics g){                             //585
       paint(g);
   }

   public boolean mouseDown(Event e,int x,int y){
      a=(x-4)/28;
      b=(y-4)/28;
      int i,j;
      TreeNode work;
      String s;
      fshuzi=false;
      
      showStatus("a="+a+", b="+b);
      if (a>=0&&a<=18&&b>=0&&b<=18&&zb[a][b][2]==0){
         if(a==ktm&&b==ktn){// decide is it the prohibtted point?
            showStatus("this point is not permmitted,try another point!");
         }else
         {
            work=gotemp;
           if(gotemp==null){
              if(czhengjie==0){
               jieshuo.setText("你已经赢了");
                  //jieshuo.setText("变化已经结束，\n请单击\"重来\"按钮");
               }
              else{
                  jieshuo.setText("你已经失败了");
                  // jieshuo.setText("变化已经结束，\n请单击\"重来\"按钮");
               }
              return true;
            }
            fchonghua=true;
            ftishi=false;
           if(fbuhelidian==true) {
               clhuiqi();
               chuiqi++;//?
               fbuhelidian=false;
            }

           if(a==gotemp.zba&&b==gotemp.zbb){
               czhengjie-=1;
            }
           while(gotemp!=null){
               if(a==gotemp.zba&&b==gotemp.zbb){

                    a=gotemp.zba;
                    b=gotemp.zbb;
                    s=gotemp.jieshuo;
                    jieshuo.setText(s);
                    System.out.println("s="+s);
                    cgcl();
                    a=-1;
                    b=-1;
                  break;
                }else{
                   gotemp=gotemp.right;

                }

              }//while
	       if(gotemp==null){
                 fbuhelidian=true;
                 // czhengjie=true;
                  cgcl();
                  cqianjin++;
                  gotemp=work;
                  jieshuo.setText("再想想，有没有更好的解法");
               // shi yi fei zheng jie ;
               }else{
                  czhengjie+=1;
                  old=gotemp;
                  gotemp=gotemp.left;

                  if (gotemp==null){
                       jieshuo.setText("恭喜你，你已经赢了");
                  }else{
                     s=gotemp.jieshuo;
                     a=gotemp.zba;
                     b=gotemp.zbb;
                   if(s!=null) {
                        s=jieshuo.getText()+"\n"+s;
                       jieshuo.setText(s);

                      }
                      old=gotemp;
                     gotemp=gotemp.left;
                     cgcl();
                     cqianjin++;
                  }
               }
                repaint();//else
          }//else
      }//if
      else{
          showStatus("it is not the right point,please play again!");


      }
       a=-1;
       b=-1;
       return true;
   }
   public void cgcl(){//chang gui chu li  qi zi  de qi
      int p=0;
      int yise=0;
      int tongse=0;//yise is diff color.and 2 same.
      int k0=0,k1=0,k2=0,k3=0,i=0,j=0;//the count for three kinds of point.
      int dang=0,ktz=0;//,kq=0,p=0,q=0; //dang is breath of block.
      int ks=0,kss=0;//ks is count for block,kss for single point
      int kin1=0,kin2=0,kin3=0,kin4=0,gq=0,m=0,n=0;//the block index.
      int[] u=  {0,0,0,0,0};//position
      int[] v=  {0,0,0,0,0};
      int[] k=  {0,0,0,0,0};//array for block index.
      int  tzd=0,tkd=0;//the   count for single pointeaten andblock eaten.
       System.out.println("//come into method jhcl.jiaohu chu li");
       shoushu++;
       hui[shoushu][25]=a;
       hui[shoushu][26]=b;
       yise=shoushu%2+1;//yi se:tiao=2
       tongse=(1+shoushu)%2+1;//tong se:tiao=1
       zb[a][b][0]=tongse;
       if ((a+1)<=18&&zb[a+1][b][0]==yise) {//1.1
            k1++;//the count of diffrent color.
            kin1=zb[a+1][b][3];//the block index for the point.66
            if (kin1==0){      //not a block.
               zb[a+1][b][2]-=1;
               if(zb[a+1][b][2]<=0){//eat the diff point
                  k1--;
                  tzd++;
                  hui[shoushu][tzd*2-1]=a+1;
                  hui[shoushu][tzd*2]=b;
                  ktz++;  //single point eaten was count
                  zzq(a+1,b,tongse);//zi zhen qi
               }
            }
            else{
               int qi=kuai[kin1][0][0]-1;
               kdq(kin1,qi);
               if (kuai[kin1][0][0]<=0){
                  k1--;
                  tkd++;
                  hui[shoushu][8+tkd]=kin1;
                  ktz+=kuai[kin1][0][1];
		  kzq(kin1,tongse); //increase the breath of point surround
               }
            }
         }
         if((a-1)>=0&&zb[a-1][b][0]==yise) {//1.2
            k1++;
            kin2=zb[a-1][b][3];
            if (kin2==0){//99
               zb[a-1][b][2]-=1;
               if(zb[a-1][b][2]<=0){
    	          k1--;
                  tzd++;
                  hui[shoushu][tzd*2-1]=a-1;
                  hui[shoushu][tzd*2]=b;
                  ktz++;
                  zzq(a-1,b,tongse);//zi zhen qi
               }
            }
            else if(kin2!=kin1){
                  int qi=kuai[kin2][0][0]-1;
               kdq(kin2,qi);
               //kuai[kin2][0][0]-=1;
               if(kuai[kin2][0][0]<=0){
                  k1--;
                  tkd++;
                  hui[shoushu][8+tkd]=kin2;
                   ktz+=kuai[kin2][0][1];
                  kzq(kin2,tongse); //kuai zheng qi
	       }
            }     //overpaint

         }
         if((b+1)<=18&&zb[a][b+1][0]==yise) {//1.3
            k1++;
            kin3=zb[a][b+1][3];
            if (kin3==0){
               zb[a][b+1][2]-=1;//132
               if(zb[a][b+1][2]<=0){
	          k1--;
                  tzd++;
                  hui[shoushu][tzd*2-1]=a;
                  hui[shoushu][tzd*2]=b+1;
                  ktz++;
                  zzq(a,(b+1),tongse);//zi zhen qi
               }
            }
            else if (kin3!=kin2&&kin3!=kin1){
                  int qi=kuai[kin3][0][0]-1;
               kdq(kin3,qi);
               //kuai[kin3][0][0]-=1;
               if (kuai[kin3][0][0]<=0){
                  k1--;
                  tkd++;
                  hui[shoushu][8+tkd]=kin3;
                  ktz+=kuai[kin3][0][1];
                  kzq(kin3,tongse); //kuai zheng qi
	       }
            }
         }
         if((b-1)>=0&&zb[a][b-1][0]==yise){//1.4
            k1++;
            kin4=zb[a][b-1][3];//the subscipt 3 mean the block index
            if (kin4==0){
               zb[a][b-1][2]-=1;
               if (zb[a][b-1][2]<=0){//165
                  k1--;
                  tzd++;
                  hui[shoushu][tzd*2-1]=a;
                  hui[shoushu][tzd*2]=b-1;
                  ktz++;
                  zzq(a,(b-1),tongse);//zi zhen qi
               }
            }
            else if (kin4!=kin3&&kin4!=kin2&&kin4!=kin1) {
             int qi=kuai[kin4][0][0]-1;
               kdq(kin4,qi);
               //kuai[kin4][0][0]-=1;
               if(kuai[kin4][0][0]<=0){
                  k1--;
                  tkd++;
                  hui[shoushu][8+tkd]=kin4;
                  ktz+=kuai[kin4][0][1];
                  kzq(kin4,tongse); //kuai zheng qi
	       }
            }   //overpain
         }
         k0=k1;//k0 is count for diff point.
         zb[a][b][2]=0;//return the breath to zero.
         if(shoushu%2==1) ktb+=ktz;
         else ktw+=ktz;

         if((a+1)<=18&&zb[a+1][b][0]==0){//2.1the breath of blank

            k2++;
            u[k0+k2]=a+1;
            v[k0+k2]=b;//198
         }
         if((a-1)>=0&&zb[a-1][b][0]==0){//2.2

            k2++;
            u[k0+k2]=a-1;
            v[k0+k2]=b;
         }
         if((b+1)<=18&&zb[a][b+1][0]==0){//2.3
            k2++;
            u[k0+k2]=a;
            v[k0+k2]=b+1;
         }
         if((b-1)>=0&&zb[a][b-1][0]==0){//2.4
            k2++;
            u[k0+k2]=a;
            v[k0+k2]=b-1;
         }
         k0+=k2;//k0 is the total points of diff and blank.
         dang=k2;
         if((a+1)<=18&&zb[a+1][b][0]==tongse){//3.1
            k3++;
            kin1= zb[a+1][b][3];
            if (kin1==0){
               kss++;           //same color single point.
               dang+=zb[a+1][b][2];
               dang--;//current point close one breath of surr point.
               u[k0+kss]=a+1;//u[0] not used
               v[k0+kss]=b;   //deal with single point.
            }
            else{//231
               dang+=kuai[kin1][0][0];
               dang--;
	       u[4-ks]=a+1;//deal with block.
               v[4-ks]=b;
               ks++;
               k[ks]=kin1;//
            }

         }
         if ((a-1)>=0&&zb[a-1][b][0]==tongse){//3/.2
            k3++;
            kin2= zb[a-1][b][3];
            if (kin2==0){
               kss++;
               dang+=zb[a-1][b][2];
               dang--;
               u[k0+kss]=a-1;
               v[k0+kss]=b;
	    }
            else if (kin2!=kin1){
               dang+=kuai[kin2][0][0];
               dang--;
	       u[4-ks]=a-1;
               v[4-ks]=b;
               ks++;
               k[ks]=kin2;
            }//ks biao shi you ji ge bu tong de kuai shu

         }
         if((b+1)<=18&&zb[a][b+1][0]==tongse){//3.3
            k3++;
            kin3= zb[a][b+1][3];
            if (kin3==0) {
               kss++;
               dang+=zb[a][b+1][2];//264
               dang--;
               u[k0+kss]=a;
               v[k0+kss]=b+1;
            }
            else if (kin3!=kin2&&kin3!=kin1){
               dang+=kuai[kin3][0][0];
               dang--;
               u[4-ks]=a;
               v[4-ks]=b+1;
               ks++;
               k[ks]=kin3;
            }

         }
         if((b-1)>=0&&zb[a][b-1][0]==tongse){//3.4
            k3++;
            kin4= zb[a][b-1][3];
            if (kin4==0) {
               kss++;
               dang+=zb[a][b-1][2];
               dang--;
	       u[k0+kss]=a;
               v[k0+kss]=b-1;//kss is single point.
            }
            else if (kin4!=kin3&&kin4!=kin2&&kin4!=kin1){
               dang+=kuai[kin4][0][0];
               dang--;
               u[4-ks]=a;
               v[4-ks]=b-1;
               ks++;
               k[ks]=kin4; //ks is block.
            }

         }          //297
         if(dang>0){
             ktm=-1;
             ktn=-1;
         }
         if(dang==0){

               showStatus("this point is prohibited,try again!");
               zzq(a,b,yise);
               hui[shoushu][25]=-1;
               hui[shoushu][26]=-1;
	       shoushu--;
	       zb[a][b][0]=0;
	       return;

         }
	 showStatus("qing="+dang+";  a="+a+",b="+b+";");
         if (k3==0){//4.1 no same color point surround
             System.out.println("//k3=0");

               zb[a][b][2]=dang;
              if(dang==1&ktz==1){
		  ktm=u[k0];
		  ktn=v[k0];
	       }//not conform to so. en.
              //else if(dang>1&&ktz==1){
              // wyydj++;
              // wyyd[wyydj][0]=hui[shoushu][1];
              // wyyd[wyydj][1]=hui[shoushu][2];
               //}
	    return;
         }
         if (ks==0){//4.2 only single point surr.
            System.out.println("ks=0");
            gq=0;
	    for (i=1;i<=kss;i++){//4.1 deal surr point
	        hui[shoushu][12+i*2-1]=u[k0+i];
                hui[shoushu][12+i*2]=v[k0+i];
              for ( j=1;j<=(kss-i);j++){
                  gq+=dd(u[k0+i],v[k0+i],u[k0+i+j],v[k0+i+j]);
               }
            }
            zb[a][b][2]=dang-gq;
            //zb[a][b][0]=tongse;

	    zb[a][b][3]=++ki;//count from first block
            hui[shoushu][0]=ki;
	    kuai[ki][0][0]=zb[a][b][2];
	    kuai[ki][0][1]=k3+1;
            kuai[ki][k3+1][0]=a;
            kuai[ki][k3+1][1]=b;
            for ( i=1;i<=k3;i++){
	       m=u[k0+i];
               n=v[k0+i];
               kuai[ki][i][0]=m;
               kuai[ki][i][1]=n;
               zb[m][n][2]=zb[a][b][2];
               zb[m][n][3]=ki;
            }
           /*if(zb[a][b][2]==0){
               ktm=-1;
               ktn=-1;
               kzq(ki,yise);
            }*/
            //ci shi de de gong qi jin jin she ji dian
           //hen hao chu li.jian qu gong qi ji ke .  jian qu gong qi
         }
         if(ks>0){
             System.out.println("ks>0");

             ki++;
             hui[shoushu][0]=ki;
             kuai[ki][0][1]=1;
             kuai[ki][1][0]=a;
             kuai[ki][1][1]=b;
             zb[a][b][3]=ki;
	    for ( i=1;i<=kss;i++){//330
               hui[shoushu][12+i*2-1]=u[k0+i];
               hui[shoushu][12+i*2]=v[k0+i];
               dkhb(u[k0+i],v[k0+i],ki);
            }
           // dkhb(a,b,k[1]);
            for ( j=1;j<=ks;j++){
               hui[shoushu][20+j]=k[j];
               kkhb(ki,k[j]);//not deal with breath
    	    }
            //zb[a][b][2]=tongse;
            //kuai[k[1]][0][0]=zb[a][b][2];//? need deal with breath.
	    dang=jskq(ki);
             kdq(ki,dang);
            /*if(dang==0){
               hui[shoushu][0]=ki;
               kzq(ki,yise);
               ktm=-1;
               ktn=-1;
               //hui[shoushu][0]=ki;
              // return;
            }*/
        }
   }
   public void paint(Graphics g)
   {
      int p=0;//kuai de zi shu .
      int yise=0;
      int tongse=0;//yise is diff color.and 2 same.
      int k0=0,k1=0,k2=0,k3=0;//the count for three kinds of point.
      int i=0,j=0,k;
      int dang=0,ktz=0;//,kq=0,p=0,q=0; //dang is breath of block.
      int ks=0,kss=0;//ks is count for block,kss for single point
      int kin1=0,kin2=0,kin3=0,kin4=0,gq=0,m=0,n=0;//the block index.
      int[] u=  {0,0,0,0,0};//position
      int[] v=  {0,0,0,0,0};

      int  tzd=0,tkd=0;//the   count for single pointeaten andblock eaten.
      int xun;
      System.out.println("//come into method paint");

      /*if(fbuhelidian==true){
             if(shoushu%2==0) g.setColor(Color.black);
             else g.setColor(Color.white);
             g.fillOval(28*a+4,28*b+4,28,28);
             return;
      }*/
    if(fchonghua==true){
      for(i=0;i<chuiqi;i++){
         xun=shoushu-i-2;
         tongse=(xun+1)%2+1;//tong se
         // yise=shoushu%2+1;
         m=huitemp[i][0][0];
         n=huitemp[i][0][1];
         huitemp[i][0][0]=-1;
         huitemp[i][0][1]=-1;
         xz(g,m,n);
        if(tongse==2)
            g.setColor(Color.black);
        else
            g.setColor(Color.white);
        j=1;
        while(huitemp[i][j][0]>=0){
           m=huitemp[i][j][0];
           n=huitemp[i][j][1];
           huitemp[i][j][0]=-1;
           huitemp[i][j][1]=-1;
           g.fillOval(28*m+4,28*n+4,28,28);
           j++;
        }
      }
      for(i=0;i<cqianjin;i++){
          xun=shoushu+1-cqianjin+i;
          tongse=(xun+1)%2+1;
          m=hui[xun][25];
          n=hui[xun][26];
         //xz(g,m,n);
        if(tongse==1)
            g.setColor(Color.black);
        else
            g.setColor(Color.white);
         g.fillOval(28*m+4,28*n+4,28,28);
          if(hui[xun][1]>=0||hui[xun][9]>0){
             try{
                     Thread.currentThread().sleep(3*1000);
                 }catch(InterruptedException e){
                 }
          }

         for(j=1;j<5;j++){
             if(hui[xun][2*j-1]<0) break;
            else {
              m=hui[xun][2*j-1];
              n=hui[xun][2*j];
              xz(g,m,n);
            }

         }
         for(j=1;j<5;j++){
             kin1=hui[xun][8+j];
             if(kin1<=0) break;
            else {
                p=kuai[kin1][0][1];
                for(k=1;k<=p;k++){
                     m=kuai[kin1][k][0];
                     n=kuai[kin1][k][1];
                     xz(g,m,n);
                 }
              }

         }

       }
         if(ftishi==true) {
             g.setColor(Color.red);
             m=gotemp.zba;
             n=gotemp.zbb;
             g.drawString("A",28*m+14,28*n+23);
             a=-1;
             b=-1;

         }
   fchonghua=false;
   return;
   }

         g.setColor(Color.white);
         g.fillRect(540,0,200,540);
	 g.setColor(Color.orange);
         g.fillRect(0,0,540,540);//33
         g.setColor(Color.black);
         for ( i=0; i<19; i++)
         {
            g.drawLine(18,18+28*i,522,18+28*i);//hor
            g.drawLine(18+28*i,18,18+28*i,522);//ver
         }// paint the ver and hor line.
         for (i=0; i<3;i++){
	    for ( j=0; j<3;j++){
	       g.fillOval(168*i+99,168*j+99,6,6);
            }
         }//paint the star point.
         for(i=0;i<=18;i++){
            for(j=0;j<=18;j++){
               if(zb[i][j][0]==1) {
                    g.setColor (Color.black);
                    g.fillOval(28*i+4,28*j+4,28,28);
                 }
               else  if(zb[i][j][0]==2){
                    g.setColor(Color.white);
                    g.fillOval(28*i+4,28*j+4,28,28);
                }
             }
          }//paint all the points owned by black and white.
          g.setColor(Color.black);
         for (i=10;i<19;i++){
           // g.drawString(""+kuai[i][0][0],600,300+15*i);
           // g.drawString(""+kuai[i][0][1],650,300+15*i);
            for ( j=0;j<10;j++){
	       g.drawString(""+zb[j][i][2],545+15*j,15*i+60);
               g.drawString(""+zb[j][i][0],545+15*j,15*i+230);
            }
         }

          if(fshuzi==true){//biao shang shu zi
          // ke yi zhu bu  DA KAI
            for( xun=linss+1;xun<=shoushu;xun++){
                tongse=(1+xun)%2+1;
                m=hui[xun][25];
                n=hui[xun][26];
              if(zb[m][n][2]>0){
                 if(tongse==1)
                      g.setColor(Color.black);
                 else
                      g.setColor(Color.white);
                   g.fillOval(28*m+4,28*n+4,28,28);
                  if(tongse==2)
                      g.setColor(Color.black);
                 else
                      g.setColor(Color.white);
                 if(xun>=linss+10)
                      g.drawString(""+(xun-linss),28*m+11,28*n+23);
                 else
                      g.drawString(""+(xun-linss),28*m+14,28*n+23);
                 }

                //hua zhao zi dian
               /*if(hui[xun][0]!=0){//hua zi ti dian
                    kin1=hui[xun][0];
                    p=kuai[kin1][0][1];
                   for (i=1;i<=p;i++)
                   {
                       m=kuai[kin1][i][0];
                       n=kuai[kin1][i][1];
                       xz(g,m,n);
                   }
               }*/
              //else {
                  /*for(i=1;i<=4;i++){
                      if(hui[xun][2*i-1]<0)
                               break;
                      else{
                            m=hui[xun][2*i-1];
                            n=hui[xun][2*i];
                            xz(g,m,n);
                      }

                   }
                  for(i=1;i<=4;i++){
                     if(hui[xun][8+i]<=0){
                         break;
                       }
                      else{
                           kin1=hui[xun][8+i];
                           p=kuai[kin1][0][1];
                         for(j=1;j<=p;j++){
                              m=kuai[kin1][j][0];
                              n=kuai[kin1][j][1];
                              xz(g,m,n);
                           }
                        }
                     }//}for 1*/

                  }//for 2
                 // return;
               }


           if(ftishi==true) {
             g.setColor(Color.red);
             m=gotemp.zba;
             n=gotemp.zbb;
             g.drawString("A",28*m+14,28*n+23);
             a=-1;
             b=-1;
         }

      }

   public boolean action(Event e,Object what){

      int kin1=0,i=0,j=0,m=0,n=0,p=0;
      int yise=0;//tong se
      int tongse=0;//yi se bei ti
      int lins=0;
       chuiqi=0;//?
       fchonghua=false;
       fshuzi=false;
      if (e.target==huiqi){
             chuiqi=0;
             fchonghua=true;
            jieshuo.setText("") ;
            ftishi=false;
      //   fshuzi=false;
         if(shoushu>linss){
            if(fbuhelidian==true) {
                fbuhelidian=false;
                clhuiqi();//wu ying zhao de huiqi
                chuiqi++;//?
            }  else if(czhengjie==0&&gotemp==null)
            {
               clhuiqi();
               chuiqi++;//?
               gotemp=old;
              /*if(gotemp.father!=null&&gotemp!=gotemp.father.left) {
                                czhengjie-=1;
               }
              while(gotemp.father!=null&&gotemp!=gotemp.father.left) {
                     gotemp=gotemp.father;
               }*/
               old=gotemp.father;
            }
           else{
               clhuiqi();
               chuiqi++;//?
               clhuiqi();
               chuiqi++;//?
               gotemp=old.father;
              if(gotemp.father!=null&&gotemp!=gotemp.father.left) {
                  czhengjie-=1;
               }
              while(gotemp.father!=null&&gotemp!=gotemp.father.left) {
                  gotemp=gotemp.father;
               }
               old=gotemp.father;
            }   //bian hua de hui qi
            repaint();
         }else{
            System.out.println("//this is original ju mian");
            jieshuo.setText("已经是原始局面，请选择着点");
              //huiqi.disable();
         }
            return true;
      }
      if(e.target==tishi)
      {
          System.out.println("come into method tishi!");
          if(gotemp!=null){
             a=gotemp.zba;
             b=gotemp.zbb;
             ftishi =true;
             repaint();
          }// ti shi dang qian shou
          else{
              jieshuo.setText("变化已经结束，请选择\"重来\"按钮");
          }

          return true;
       }
       if(e.target==conglai){
           System.out.println("come into method conglai!");
           while(shoushu!=linss){
              clhuiqi();
           }
           jieshuo.setText("提示按钮可以逐手给出提示；\n重来按钮可以恢复到初始局面；\n演示按钮可以显示正解\n如果走错，直接点另一点即可");

           ftishi=false;
           fbuhelidian=false;
           czhengjie=0;
           gotemp=gotree.getTreeNode();

           repaint();
           return true;
       }
       if(e.target==yanshi){
           jieshuo.setText("");
          while(shoushu!=linss){
              clhuiqi();
           }
           fbuhelidian=false;
           ftishi=false;
          fshuzi=true;
          gotemp=gotree.getTreeNode();
          while(gotemp!=null){
             a=gotemp.zba;
             b=gotemp.zbb;
             cgcl();
             gotemp=gotemp.left;
          }
         // gotemp=gotree.getTreeNode();
          repaint();
          return true;
       }
       return true;
    }

   void chushihua(){
        int i,j,k;
        a=-1;
        b=-1;
        shoushu=0;
        ktm=-1;
        ktn=-1;
        ki=0;
        ktw=0;
        ktb=0;
        for(i=0;i<=18;i++){
            for(j=0;j<=18;j++){
                  zb[i][j][0]=0;
                  zb[i][j][2]=0;
                  zb[i][j][3]=0;
            }
        }
        for(i=0;i<400;i++){
            for(j=0;j<27;j++){
                  hui[i][j]=0;
            }
           for(j=1;j<9;j++){
                  hui[i][j]=-1;
            }
           for(j=13;j<21;j++){
                  hui[i][j]=-1;
            }
            hui[i][25]=-1;
            hui[i][26]=-1;
        }
        for(k=1;k<100;k++){
             kuai[k][0][0]=0;
             kuai[k][0][1]=0;
             for(i=1;i<50;i++){
                 kuai[k][i][0]=0;
                 kuai[k][i][1]=0;
             }
        }

   }

   int jszq(int a, int b){
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
   void zzq(int a,int b,int tiao)
   {//function 6.1
      int c1=0,c2=0,c3=0,c4=0,i;
      if((a+1)<=18&&zb[a+1][b][0]==tiao){
         c1= zb[a+1][b][3];
         if (c1==0){
            zb[a+1][b][2]+=1;
         }
         else{
           int qi;
            qi=kuai[c1][0][0]+=1;
            kdq(c1,qi);
	 }
      }
      if((a-1)>=0&&zb[a-1][b][0]==tiao){
         c2= zb[a-1][b][3];
         if (c2==0){
            zb[a-1][b][2]+=1;
         }
         else if(c2!=c1){
           int qi;
            qi=kuai[c2][0][0]+=1;
            kdq(c2,qi);
         }
      }
      if((b+1)<=18&&zb[a][b+1][0]==tiao){
         c3= zb[a][b+1][3];
         if (c3==0){
            zb[a][b+1][2]+=1;
         }
         else if (c3!=c2&&c3!=c1){
            int qi;
            qi=kuai[c3][0][0]+=1;
            kdq(c3,qi);
         }//400
      }
      if((b-1)>=0&&zb[a][b-1][0]==tiao){
         c4= zb[a][b-1][3];
         if (c4==0){
            zb[a][b-1][2]+=1;
         }
         else if (c4!=c3&&c4!=c2&&c4!=c1){
            int qi;
            qi=kuai[c4][0][0]+=1;
            kdq(c4,qi);
         }
      }
      zb[a][b][0]=0;
      zb[a][b][2]=0;
      zb[a][b][3]=0;
   }
   public void kzq(int r,int tiao){//6.2 tong se kuai bei ti
      int n=0;//the same color block is eaten
      int p=0,q=0;
      n=kuai[r][0][1];
      for (int i=1;i<=n;i++){
         p=kuai[r][i][0];
         q=kuai[r][i][1];
         zzq(p,q,tiao);
        // kuai[r][i][0]=0;
        // kuai[r][i][1]=0;
      }
      kuai[r][0][0]=0;
     // kuai[r][0][1]=0;
   }
   void zjq(int a,int b,int tiao)
   {//function 6.1
      int c1=0,c2=0,c3=0,c4=0,i;
      if((a+1)<=18&&zb[a+1][b][0]==tiao){
         c1= zb[a+1][b][3];
         if (c1==0){
            zb[a+1][b][2]-=1;
         }
         else{
           int qi;
            qi=kuai[c1][0][0]-=1;
            kdq(c1,qi);
	 }
      }
      if((a-1)>=0&&zb[a-1][b][0]==tiao){
         c2= zb[a-1][b][3];
         if (c2==0){
            zb[a-1][b][2]-=1;
         }
         else if(c2!=c1){
           int qi;
            qi=kuai[c2][0][0]-=1;
            kdq(c2,qi);
         }
      }
      if((b+1)<=18&&zb[a][b+1][0]==tiao){
         c3= zb[a][b+1][3];
         if (c3==0){
            zb[a][b+1][2]-=1;
         }
         else if (c3!=c2&&c3!=c1){
            int qi;
            qi=kuai[c3][0][0]-=1;
            kdq(c3,qi);
         }//400
      }
      if((b-1)>=0&&zb[a][b-1][0]==tiao){
         c4= zb[a][b-1][3];
         if (c4==0){
            zb[a][b-1][2]-=1;
         }
         else if (c4!=c3&&c4!=c2&&c4!=c1){
            int qi;
            qi=kuai[c4][0][0]-=1;
            kdq(c4,qi);
         }
      }
   }
   public void kjq(int r,int tiao){//6.2 tong se kuai bei ti
      int n=0;//the same color block is eaten
      int p=0,q=0;
       n=kuai[r][0][1];
      for (int i=1;i<=n;i++){
         p=kuai[r][i][0];
         q=kuai[r][i][1];
         zjq(p,q,tiao);
        // kuai[r][i][0]=0;
      //   kuai[r][i][1]=0;
      }
      kuai[r][0][0]=1;
     // kuai[r][0][1]=0;
   }
   public int dd(int a,int b,int c,int d ){//7.1diang diang gong qi
      int gq=0;// consider four points only.
      if (zb[a][d][0]==0)
         gq++;
      if (zb[c][b][0]==0)
         gq++;
      return gq;
   }

   //6.7diang kuai he bing
   public void dkhb(int p,int q,int r){ //8.1
      int ss=kuai[r][0][1]+1;
      kuai[r][ss][0]=p;
      kuai[r][ss][1]=q;
      kuai[r][0][1]=ss;
      zb[p][q][3]=r;
     // zb[p][q][2]=zb[a][b][2];
   }
   //6.8 ss1 shi zu yao kuai!
   public void kkhb(int r1,int r2){//8.2
      int ss1=kuai[r1][0][1];
      int ss2=kuai[r2][0][1];
      int m=0,n=0;
      for (int i=1;i<=ss2;i++){
	 m=0;
         n=0;                                   //476
         m=kuai[r2][i][0];
         n=kuai[r2][i][1];
         zb[m][n][3]=r1;
        // zb[m][n][2]=zb[a][b][2];

       //  kuai[r2][i][0]=0;
        // kuai[r2][i][1]=0;
         kuai[r1][ss1+i][0]=m;
         kuai[r1][ss1+i][1]=n;
      }

      /*for (int i=1;i<=ss1;i++){
	 m=0;
         n=0;                                   //476
         m=kuai[r1][i][0];
         n=kuai[r1][i][1];
         zb[m][n][2]=zb[a][b][2];
      }leave breath decision */

      kuai[r1][0][1]=ss1+ss2;
     // kuai[r2][0][1]=0;
     // kuai[r2][0][0]=0;
   }

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


//10.1ji suan kuai qi.
   public int jskq(int r2){
      int gq=0;//the breath of the block
      int k1=0;// xiang ling cheng du wei 2;
      int k2=0;// xiang ling cheng du wei 3;
      int k3=0;
      int a=0,b=0,p=0,q=0;


      q=kuai[r2][0][1];
      for (int i =1;i<=q;i++){
         a=kuai[r2][i][0];
         b=kuai[r2][i][1];

         if ((a+1)<=18&&zb[a+1][b][0]==0){
            gq++;
            if(xl((a+1),b,r2)==2)
               k1++;
            if(xl((a+1),b,r2)==3){
               k2++;
            }
            if(xl((a+1),b,r2)==4){
               k3++;
            }
         }
         if ((a-1)>=0&&zb[a-1][b][0]==0){
            gq++;
            if(xl((a-1),b,r2)==2)
               k1++;
            if(xl((a-1),b,r2)==3){
               k2++;
            }
            if(xl((a-1),b,r2)==4){
               k3++;
            }
         }
         if ((b+1)<=18&&zb[a][b+1][0]==0){
            gq++;
            if(xl(a,(b+1),r2)==2)
               k1++;
            if(xl(a,(b+1),r2)==3){
               k2++;
            }
            if(xl(a,(b+1),r2)==4){
               k3++;
            }
         }                                                              //573
         if ((b-1)>=0&&zb[a][b-1][0]==0){
            gq++;
            if(xl(a,(b-1),r2)==2)
               k1++;
            if(xl(a,(b-1),r2)==3){
               k2++;
            }
            if(xl(a,(b-1),r2)==4){
               k3++;
            }
         }
      }                                                          //574
      gq-=k1/2;
      gq-=2*k2/3;
      gq-=3*k3/4;
      return gq;
   }

   public void kdq(int kin,int a){//11.1 kuai ding qi
      int m=0,n=0,p=0;
      p=kuai[kin][0][1];
      for (int i=1;i<=p;i++){
          m=kuai[kin][i][0];
          n=kuai[kin][i][1];
          zb[m][n][2]=a;
      }
      kuai[kin][0][0]=a;
   }
   public void xz(Graphics g,int a,int b){//12.1
      g.setColor(Color.orange);//deal with image only.
      g.fillOval(28*a+4,28*b+4,28,28);
      g.setColor(Color.black);
      if(a==0){
           g.drawLine(18,28*b+18,32,28*b+18);
      }
      else if(a==18){
           g.drawLine(28*a+4,28*b+18,28*a+18,28*b+18);
      }else {
           g.drawLine(28*a+4,28*b+18,28*a+32,28*b+18);
      }
      if(b==0){
          g.drawLine(28*a+18,18,28*a+18,32);
      }else if(b==18){
          g.drawLine(28*a+18,28*b+4,28*a+18,28*b+18);
      }else{
          g.drawLine(28*a+18,28*b+4,28*a+18,28*b+32);
      }
      if((a-3)%6==0&&(b-3)%6==0){
         g.fillOval(28*a+15,28*b+15,6,6);
      }//draw the star point if necessary.
      zb[a][b][0]=0;
      zb[a][b][3]=0;
      zb[a][b][2]=0;
   }





   public void cqpchuli(){
      int p=0;
      int yise=0;
      int tongse=0;//yise is diff color.and 2 same.
      int k0=0,k1=0,k2=0,k3=0,i=0,j=0;//the count for three kinds of point.
      int dang=0,ktz=0;//,kq=0,p=0,q=0; //dang is breath of block.
      int ks=0,kss=0;//ks is count for block,kss for single point
      int kin1=0,kin2=0,kin3=0,kin4=0,gq=0,m=0,n=0;//the block index.
      int[] u=  {0,0,0,0,0};//position
      int[] v=  {0,0,0,0,0};
      int[] k=  {0,0,0,0,0};//array for block index.
      int  tzd=0,tkd=0;//the   count for single pointeaten andblock eaten.
       // zhuang=false;
       System.out.println("hello");
       yise=shoushu%2+1;//yi se:tiao=2
       tongse=(1+shoushu)%2+1;//tong se:tiao=1
       zb[a][b][0]=tongse;
      if ((a+1)<=18&&zb[a+1][b][0]==yise) {//1.1
          k1++;//the count of diffrent color.
          kin1=zb[a+1][b][3];//the block index for the point.66
         if (kin1==0){      //not a block.
             zb[a+1][b][2]-=1;
            if(zb[a+1][b][2]<=0){//eat the diff point
                  k1--;
                  tzd++;
                  hui[shoushu][tzd*2-1]=a+1;
                  hui[shoushu][tzd*2]=b;
                  ktz++;  //single point eaten was count
                 // ktm=a+1;//the point just be eaten.
                 // ktn=b;
		  //g.setColor(Color.orange);
                  //g.fillOval(28*(a+1)+4,28*b+4,28,28);
                 // xz(g,a+1,b);
                  zzq(a+1,b,tongse);//zi zhen qi
               }
            }
            else{
               int qi=kuai[kin1][0][0]-1;
               kdq(kin1,qi);
               if (kuai[kin1][0][0]<=0){
                  k1--;
                  tkd++;
                  hui[shoushu][8+tkd]=kin1;
                  ktz+=kuai[kin1][0][1];
                  //xk(g,kin1);//eaten the block.only paint
		  kzq(kin1,tongse); //increase the breath of point surround
               }
            }
         }
        if((a-1)>=0&&zb[a-1][b][0]==yise) {//1.2
            k1++;
            kin2=zb[a-1][b][3];
           if (kin2==0){//99
               zb[a-1][b][2]-=1;
              if(zb[a-1][b][2]<=0){
    	          k1--;
                  tzd++;
                  hui[shoushu][tzd*2-1]=a-1;
                  hui[shoushu][tzd*2]=b;
                  ktz++;

	          //xz(g,a-1,b);
                  zzq(a-1,b,tongse);//zi zhen qi
                 // g.drawString("zb[a][b][2]",600,30);
               }
            }
           else if(kin2!=kin1){
              int qi=kuai[kin2][0][0]-1;
                kdq(kin2,qi);
                //kuai[kin2][0][0]-=1;
              if(kuai[kin2][0][0]<=0){
                  k1--;
                  tkd++;
                  hui[shoushu][8+tkd]=kin2;
                  ktz+=kuai[kin2][0][1];
                 // xk(g,kin2);
                  kzq(kin2,tongse); //kuai zheng qi
	       }
            }     //overpaint

         }
         if((b+1)<=18&&zb[a][b+1][0]==yise) {//1.3
            k1++;
            kin3=zb[a][b+1][3];
            if (kin3==0){
               zb[a][b+1][2]-=1;//132
               if(zb[a][b+1][2]<=0){
	          k1--;
                  tzd++;
                  hui[shoushu][tzd*2-1]=a;
                  hui[shoushu][tzd*2]=b+1;
                  ktz++;
		  //xz(g,a,b+1);
                  zzq(a,(b+1),tongse);//zi zhen qi
               }
            }
            else if (kin3!=kin2&&kin3!=kin1){
                  int qi=kuai[kin3][0][0]-1;
               kdq(kin3,qi);
               //kuai[kin3][0][0]-=1;
               if (kuai[kin3][0][0]<=0){
                  k1--;
                  tkd++;
                  hui[shoushu][8+tkd]=kin3;
                  ktz+=kuai[kin3][0][1];
                 // xk(g,kin3);
                  kzq(kin3,tongse); //kuai zheng qi
	       }
            }
         }
         if((b-1)>=0&&zb[a][b-1][0]==yise){//1.4
            k1++;
            kin4=zb[a][b-1][3];//the subscipt 3 mean the block index
            if (kin4==0){
               zb[a][b-1][2]-=1;
               if (zb[a][b-1][2]<=0){//165
                  k1--;
                  tzd++;
                  hui[shoushu][tzd*2-1]=a;
                  hui[shoushu][tzd*2]=b-1;
                  ktz++;
		 // xz(g,a,b-1);
                  zzq(a,(b-1),tongse);//zi zhen qi
               }
            }
            else if (kin4!=kin3&&kin4!=kin2&&kin4!=kin1) {
             int qi=kuai[kin4][0][0]-1;
               kdq(kin4,qi);
               //kuai[kin4][0][0]-=1;
               if(kuai[kin4][0][0]<=0){
                  k1--;
                  tkd++;
                  hui[shoushu][8+tkd]=kin4;
                  ktz+=kuai[kin4][0][1];
                 // xk(g,kin4);
                  kzq(kin4,tongse); //kuai zheng qi
	       }
            }   //overpain
         }
         k0=k1;//k0 is count for diff point.
         zb[a][b][2]=0;//return the breath to zero.
         if(shoushu%2==1) ktb+=ktz;//black eat white
         else ktw+=ktz;

         if((a+1)<=18&&zb[a+1][b][0]==0){//2.1the breath of blank
            //dang++;
            k2++;
            u[k0+k2]=a+1;
            v[k0+k2]=b;//198
         }
         if((a-1)>=0&&zb[a-1][b][0]==0){//2.2
            //dang++;
            k2++;
            u[k0+k2]=a-1;
            v[k0+k2]=b;
         }
         if((b+1)<=18&&zb[a][b+1][0]==0){//2.3
           // dang++;
            k2++;
            u[k0+k2]=a;
            v[k0+k2]=b+1;
         }
         if((b-1)>=0&&zb[a][b-1][0]==0){//2.4
            //dang++;
            k2++;
            u[k0+k2]=a;
            v[k0+k2]=b-1;
         }
         dang=k2;
         k0+=k2;//k0 is the total points of diff and blank.

         if((a+1)<=18&&zb[a+1][b][0]==tongse){//3.1
            k3++;
            kin1= zb[a+1][b][3];
            if (kin1==0){
               kss++;           //same color single point.
               dang+=zb[a+1][b][2];
               dang--;//current point close one breath of surr point.
               u[k0+kss]=a+1;//u[0] not used
               v[k0+kss]=b;   //deal with single point.
            }
            else{//231
               dang+=kuai[kin1][0][0];
               dang--;
	       u[4-ks]=a+1;//deal with block.
               v[4-ks]=b;
               ks++;
               k[ks]=kin1;//
            }

         }
         if ((a-1)>=0&&zb[a-1][b][0]==tongse){//3/.2
            k3++;
            kin2= zb[a-1][b][3];
            if (kin2==0){
               kss++;
               dang+=zb[a-1][b][2];
               dang--;
               u[k0+kss]=a-1;
               v[k0+kss]=b;
	    }
            else if (kin2!=kin1){
               dang+=kuai[kin2][0][0];
               dang--;
	       u[4-ks]=a-1;
               v[4-ks]=b;
               ks++;
               k[ks]=kin2;
            }//ks biao shi you ji ge bu tong de kuai shu

         }
         if((b+1)<=18&&zb[a][b+1][0]==tongse){//3.3
            k3++;
            kin3= zb[a][b+1][3];
            if (kin3==0) {
               kss++;
               dang+=zb[a][b+1][2];//264
               dang--;
               u[k0+kss]=a;
               v[k0+kss]=b+1;
            }
            else if (kin3!=kin2&&kin3!=kin1){
               dang+=kuai[kin3][0][0];
               dang--;
               u[4-ks]=a;
               v[4-ks]=b+1;
               ks++;
               k[ks]=kin3;
            }

         }
         if((b-1)>=0&&zb[a][b-1][0]==tongse){//3.4
            k3++;
            kin4= zb[a][b-1][3];
            if (kin4==0) {
               kss++;
               dang+=zb[a][b-1][2];
               dang--;
	       u[k0+kss]=a;
               v[k0+kss]=b-1;//kss is single point.
            }
            else if (kin4!=kin3&&kin4!=kin2&&kin4!=kin1){
               dang+=kuai[kin4][0][0];
               dang--;
               u[4-ks]=a;
               v[4-ks]=b-1;
               ks++;
               k[ks]=kin4; //ks is block.
            }

         }          //297
         if(dang>0){
             ktm=-1;
             ktn=-1;
         }
         /*if(dang==0){//check the file is right or not
	    if(guize==1|guize==2|guize==3){
               showStatus("this point is prohibited,give up one hand!");
	       xz(g,a,b);
               zzq(a,b,yise);
               hui[shoushu][25]=-1;
               hui[shoushu][26]=-1;
	       shoushu--;
	       zb[a][b][0]=0;
               a=-1;
               b=-1;
	       return;
	    }
         }*/
	 showStatus("qing="+dang+a+b);
         if (k3==0){//4.1 no same color point surround
             System.out.println("k3=0");
             zb[a][b][2]=dang;
             if(dang==1&ktz==1){
		  ktm=u[4];
		  ktn=v[4];
              }
              a=-1;
              b=-1;
	     return;
         }
         if (ks==0){//4.2 only single point surr.
            System.out.println("ks=0");
            gq=0;
	    for (i=1;i<=kss;i++){//4.1 deal surr point
	        hui[shoushu][12+i*2-1]=u[kss];
                hui[shoushu][12+i*2]=v[kss];
              for ( j=1;j<=(kss-i);j++){
                  gq+=dd(u[k0+i],v[k0+i],u[k0+i+j],v[k0+i+j]);
               }
            }
            zb[a][b][2]=dang-gq;
            //zb[a][b][0]=tongse;

	    zb[a][b][3]=++ki;//count from first block
	    kuai[ki][0][0]=zb[a][b][2];
	    kuai[ki][0][1]=k3+1;
            kuai[ki][k3+1][0]=a;
            kuai[ki][k3+1][1]=b;
            for ( i=1;i<=k3;i++){
	       m=u[k0+i];
               n=v[k0+i];
               kuai[ki][i][0]=m;
               hui[shoushu][12+i*2-1]=m;
               kuai[ki][i][1]=n;
               hui[shoushu][12+i*2]=n;
               zb[m][n][2]=zb[a][b][2];
               zb[m][n][3]=ki;
            }
            if(zb[a][b][2]==0){
               ktm=-1;
               ktn=-1;
               hui[shoushu][0]=ki;
	       //xk(g,ki);
               kzq(ki,yise);
            }
            //ci shi de de gong qi jin jin she ji dian
           //hen hao chu li.jian qu gong qi ji ke .  jian qu gong qi
         }
         if(ks>0){
             System.out.println("ks>0");
             for(i=1;i<=ks;i++){

             hui[shoushu][20+i]=k[ks];
             }
             ki++;
             kuai[ki][0][1]=1;
             kuai[ki][1][0]=a;
             kuai[ki][1][1]=b;
             zb[a][b][3]=ki;
	    for ( i=1;i<=kss;i++){//330
               hui[shoushu][12+i*2-1]=u[k0+i];
               hui[shoushu][12+i*2]=v[k0+i];
               dkhb(u[k0+i],v[k0+i],ki);
            }
           // dkhb(a,b,k[1]);
            for ( j=1;j<=ks;j++){
               hui[shoushu][20+j]=k[j];
               kkhb(ki,k[j]);//not deal with breath
    	    }
            //zb[a][b][2]=tongse;
            //kuai[k[1]][0][0]=zb[a][b][2];//? need deal with breath.
	    dang=jskq(ki);
             kdq(ki,dang);
            if(dang==0){
               hui[shoushu][0]=ki;
               kzq(ki,yise);
               a=-1;
               b=-1;
               ktm=-1;
               ktn=-1;
            }
        }
         a=-1;
         b=-1;
     }
   public void clhuiqi(){
      int p=0;
      int yise=0;
      int tongse=0;//yise is diff color.and 2 same.
      int k0=0,k1=0,k2=0,k3=0,i=0,j=0;//the count for three kinds of point.
      int dang=0,ktz=0;//,kq=0,p=0,q=0; //dang is breath of block.
      int ks=0,kss=0;//ks is count for block,kss for single point
      int kin1=0,kin2=0,kin3=0,kin4=0,gq=0,m=0,n=0;//the block index.
       int tzs =0;
      int  tzd=0,tkd=0;//the   count for single pointeaten andblock eaten.

          tongse=(shoushu+1)%2+1;//tong se
          yise=shoushu%2+1;
         /*if(hui[shoushu][0]!=0){//zi ti yi kuai,qie ci kuai zai zhi qian xing cheng /
             kin1=hui[shoushu][0];
             kjq(kin1,yise);
             p=kuai[kin1][0][1];

                  for (i=1;i<=p;i++)
                  {
                     m=kuai[kin1][i][0];
                     n=kuai[kin1][i][1];
                     zb[m][n][2]=1;
                     zb[m][n][0]=tongse;
                  }
                     m=hui[shoushu][25];
                     n=hui[shoushu][26];
                     zzq(m,n,yise);
                     zb[m][n][0]=0;
                     zb[m][n][3]=0;
                     zb[m][n][2]=0;
                 //deal with zb 0 and 2 subscipt
                // kuai[kin1][0][0]=0;
                // kuai[kin1][0][1]=0;
                 for(i=1;i<=4;i++){
                    if(hui[shoushu][2*i+12-1]<0){
                         break;
                     }
                    else{
                         m=hui[shoushu][12+2*i-1];
                         n=hui[shoushu][12+2*i];
                         zb[m][n][3]=0;
                    }
                 }//deal with 3 sub
                 for(i=1;i<=4;i++){
                      kin1=hui[shoushu][20+i];
                    if(kin1==0)
                       break;
                    else{
                          p=kuai[kin1][0][1];
                        for(j=1;j<=p;j++){
                              m=kuai[kin1][j][0];
                              n=kuai[kin1][j][1];
                              zb[m][n][3]=kin1;
                              kuai[kin1][0][0]=1;
                          }
                     }

                 }
                 System.out.println("deal with self eaten in regret");

             }*/
              //else {//fei zi ti yi kuai,dan gai kuai zhi qian wei xing cheng
                   m=hui[shoushu][25];
                   hui[shoushu][25]=-1;
                   n=hui[shoushu][26];
                   hui[shoushu][26]=-1;

                   huitemp[chuiqi][0][0]=m;
                   huitemp[chuiqi][0][1]=n;


                   zzq(m,n,yise);
                   System.out.println("//regret the first step:"+shoushu);

                 if(hui[shoushu][0]>0){
                      ki=hui[shoushu][0];
                    for(i=0;i<50;i++){
                        kuai[ki][i][0]=0;
                        kuai[ki][i][1]=0;
                      }
                  for(i=1;i<=4;i++){//mei you zi ti, xing cheng xin kuai de fan chu li
                    if(hui[shoushu][2*i+12-1]<0){
                         break;
                     }
                    else{
                         m=hui[shoushu][12+2*i-1];
                         hui[shoushu][12+2*i-1]=-1;
                         n=hui[shoushu][12+2*i];
                         hui[shoushu][12+2*i]=-1;
                         zb[m][n][3]=0;
                         //zb[m][n][0]=tongse;
                         zb[m][n][2]=jszq(m,n);
                         System.out.println("//ji suan zi de qi");
                    }
                 }//deal with 3 sub
                 for(i=1;i<=4;i++){
                      kin1=hui[shoushu][20+i];
                    if(kin1==0)
                       break;
                    else{
                          hui[shoushu][20+i]=0;
                          p=kuai[kin1][0][1];
                        for(j=1;j<=p;j++){
                              m=kuai[kin1][j][0];
                              n=kuai[kin1][j][1];
                              zb[m][n][3]=kin1;
                              //zb[m][n][0]=tongse;
                              zb[m][n][2]=kuai[kin1][0][0];
                         }
                     }
                 }//for
            }
              for(i=1;i<=4;i++){//chu li ti zi.
                       if(hui[shoushu][2*i-1]<0)
                               break;
                      else{
                            m=hui[shoushu][2*i-1];
                            hui[shoushu][2*i-1]=-1;
                            n=hui[shoushu][2*i];
                            hui[shoushu][2*i]=-1;

                            tzs++;
                            huitemp[chuiqi][tzs][0]=m;
                            huitemp[chuiqi][tzs][1]=n;

                            zb[m][n][0]=yise;
                            zb[m][n][2]=1;
                            zb[m][n][3]=0;
                            zjq(m,n,tongse);
                      }
                   }
              for(i=1;i<=4;i++){
                  if(hui[shoushu][8+i]<=0){
                         break;
                    }
                  else{
                         kin1=hui[shoushu][8+i];
                         hui[shoushu][8+i]=0;
                         kdq(kin1,1);
                         kjq(kin1,tongse);
                         p=kuai[kin1][0][1];
                      for(j=1;j<=p;j++){
                              m=kuai[kin1][j][0];
                              n=kuai[kin1][j][1];

                              tzs++;
                            huitemp[chuiqi][tzs][0]=m;
                            huitemp[chuiqi][tzs][1]=n;

                              zb[m][n][0]=yise;
                              zb[m][n][3]=kin1;

                         }

                    }
                 }
            shoushu--;
   }



}

