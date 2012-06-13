//package untitled8;
//
//
//import java.awt.*;
///**
// * <p>Title: </p>
// * <p>Description: </p>
// * <p>Copyright: Copyright (c) 2003</p>
// * <p>Company: </p>
// * @author not attributable
// * @version 1.0
// */
//
//public class GoCanvas1 extends Canvas {
//  //当前手有特殊标志：红心圆点。
//   boolean FDANGBU=false;//hui qi shi shi fou dang bu.
//   boolean FSHUZI=false;//shi fou biao chu shu zi.
//   boolean fbuhelidian=false;//zhi zuo shi wei yu kao lv de dian.
//   boolean ftishi=false;//shi fou ti shi .
//   boolean fzhudong=false;//shi fou xu yao quan bu zhong hua
//   boolean fdonghua=false;
//   int czhengjie=0;//zheng jie de biao zhi?
//   int linss;//原始局面的手数。
//   int chuiqi=0;// hui qi de bu shu.<3
//   int cqianjin=0;// xiang qian de bu shu.<3
//   int [][][] huitemp=new int[2][50][2];//yong yu paint de xie tiao
//   int tsa=-1,tsb=-1;//ti shi de wei zhi.yong yu shang chu ti shi biao zhi
//
//
//
//
//
//   boolean isStandalone = false;
//   int ki=0;//dangqian kuaishu
//   int ktm=-1,ktn=-1;// position for points eaten.
//   int ktb=0,ktw=0;// white point eaten.
//   int shoushu=0;//dang qian shou shu,caution:it increase before deal
//   int a=-1;//a is the row subscript of the point.
//   int b=-1;//b is the column subscript of the point
//   int hda=-1, hdb=-1;
//   //int hdse=0;
//   int[][][] zb = new int[19][19][4];//0:state;2:breath;3:blockindex
//   int[][][] kuai = new int[100][50][2];//mei yi kuai de ge zi zuo biao
//   int [][] hui=new int[400][29];//0=zi ti yi kuai,1~8four single point
//   //eaten,9~12 kuai suo ying of fou block eaten.13~24is same ,25,26a,b
//
//   int qpdx=28 ;//qipangdaxiao;
//   int qpbj=qpdx/2;//
//   int xbj=3;// xing dian de ban jing.
//   int tyzj=28;//tuo yuan zhi jing;
//   int bjjx=4;//qi pang bian jie jian xi.
//   //tu xing yong hu jie mian
//
//  public GoCanvas1() {
//  }
//  public void paint(Graphics g)
//   {
//      int p=0;//kuai de zi shu .
//      int tongse=0;//yise is diff color.and 2 same.
//      int i=0,j=0,k;
//      int kin1=0,m=0,n=0;//the block index.
//      int xun;
//      int yanchi;
//      if(log.isDebugEnabled()) log.debug("//come into method paint");
//      if(fzhudong==true){
//         if(tsa>=0&&tsb>=0){
//             xz(g,tsa,tsb);
//             tsa=-1;
//             tsb=-1;
//         }
//         for(i=0;i<chuiqi;i++){
//            xun=shoushu-i-cqianjin+chuiqi;
//            tongse=(xun+1)%2+1;//tong se
//            m=huitemp[i][0][0];
//            n=huitemp[i][0][1];
//            huitemp[i][0][0]=-1;
//            huitemp[i][0][1]=-1;
//            xz(g,m,n);
//           if(tongse==2)
//               g.setColor(Color.black);
//           else
//               g.setColor(Color.white);
//            j=1;
//           while(huitemp[i][j][0]>=0){
//               m=huitemp[i][j][0];
//               n=huitemp[i][j][1];
//               huitemp[i][j][0]=-1;
//               huitemp[i][j][1]=-1;
//               g.fillOval(qpdx*m+bjjx,qpdx*n+bjjx,tyzj,tyzj);
//               j++;
//            }
//         }
//         chuiqi=0;
//         for(i=0;i<cqianjin;i++){
//             xun=shoushu+1-cqianjin+i;
//             tongse=(xun+1)%2+1;
//             m=hui[xun][25];
//             n=hui[xun][26];
//
//           if((hui[xun][1]>=0||hui[xun][9]>0)&&i==1){
//              try{
//                  Thread.currentThread().sleep(800);
//               }catch(InterruptedException e){
//               }
//              /* yanchi=1000000;
//               while(yanchi>0)yanchi--;*/
//            }
//           if(tongse==1)
//               g.setColor(Color.black);
//           else
//               g.setColor(Color.white);
//            g.fillOval(qpdx*m+bjjx,qpdx*n+bjjx,tyzj,tyzj);
//           for(j=1;j<5;j++){
//              if(hui[xun][2*j-1]<0) break;
//              else {
//                  m=hui[xun][2*j-1];
//                  n=hui[xun][2*j];
//                  xz(g,m,n);
//               }
//
//            }
//           for(j=1;j<5;j++){
//               kin1=hui[xun][8+j];
//              if(kin1<=0) break;
//              else {
//                  p=kuai[kin1][0][1];
//                 for(k=1;k<=p;k++){
//                     m=kuai[kin1][k][0];
//                     n=kuai[kin1][k][1];
//                     xz(g,m,n);
//                  }
//               }
//            }//for
//         }//for
//         cqianjin=0;
//
//
//         if(ftishi==true) {
//            g.setColor(Color.red);
//            m=gotemp.zba;
//            n=gotemp.zbb;
//            tsa=m;
//            tsb=n;
//            g.drawString("A",qpdx*m+qpbj+1,qpdx*n+qpbj+bjjx+5);
//            //a=-1;
//            //b=-1;
//         }
//         g.setColor(Color.white) ;
//         g.fillRect(500,0,270,530);
//         g.fillRect(0,340,700,190);
//         g.setColor(Color.black);
//         for (i=10;i<19;i++){
//           for ( j=0;j<10;j++){
//               //g.drawString(" ",545+15*j,15*i+60);
//               //g.drawString(" ",545+15*j,15*i+230);
//               //g.drawString(""+zb[j+9][i][3],500+25*j,15*i-90);
//               kin1= zb[j+9][i][3];
//               // if (kin1>0){
//                  //g.drawString(""+kuai[kin1][0][0],30+25*j,15*i+230) ;
//               //}
//               //g.drawString(""+zb[j+9][i][2],500+25*j,15*i+60);
//               //g.drawString(""+zb[j+9][i][0],500+25*j,15*i+230);
//            }
//         }
//         fzhudong=false;
//      }else{
//         //g.setColor(Color.white);
//         //g.fillRect(qpdx*19+1+2*bjjx,0,200,qpdx*19+2*bjjx);
//         g.setColor(Color.white) ;
//         g.fillRect(500,0,270,530);
//         g.fillRect(0,340,700,190);
//         g.setColor(Color.orange);
//         g.fillRect(0,0,qpdx*19+2*bjjx,qpdx*19+2*bjjx);//33
//         g.setColor(Color.black);
//         for ( i=0; i<19; i++)
//         {
//            g.drawLine(qpbj+bjjx,qpbj+bjjx+qpdx*i,18*qpdx+bjjx+qpbj,qpbj+bjjx+qpdx*i);//hor
//            g.drawLine(qpbj+bjjx+qpdx*i,qpbj+bjjx,qpbj+bjjx+qpdx*i,qpdx*18+bjjx+qpbj);//ver
//         }// paint the ver and hor line.
//         for (i=0; i<3;i++){
//            for ( j=0; j<3;j++){
//               g.fillOval(qpdx*6*i+3*qpdx+bjjx+qpbj-xbj,qpdx*6*j+3*qpdx+bjjx+qpbj-xbj,xbj*2+1,xbj*2+1);
//            }
//         }//paint the star point.
//
//          //paint all the points owned by black and white.
//
//
//         //biao shang shu zi
//          // ke yi zhu bu  DA KAI
//            if(fdonghua==true&&FSHUZI==true){
//               for(xun=1;xun<=linss;xun++){
//                  tongse=(xun+1)%2+1;
//                  m=hui[xun][25];
//                  n=hui[xun][26];
//                  if(m<0||n<0) continue;
//                  if(tongse==1)
//                     g.setColor(Color.black);
//                  else
//                     g.setColor(Color.white);
//                  g.fillOval(qpdx*m+bjjx,qpdx*n+bjjx,tyzj,tyzj);
//
//                 for(j=1;j<5;j++){
//                    if(hui[xun][2*j-1]<0) break;
//                    else {
//                        m=hui[xun][2*j-1];
//                        n=hui[xun][2*j];
//                        xz(g,m,n);
//                     }
//                  }
//           for(j=1;j<5;j++){
//               kin1=hui[xun][8+j];
//              if(kin1<=0) break;
//              else {
//                  p=kuai[kin1][0][1];
//                 for(k=1;k<=p;k++){
//                     m=kuai[kin1][k][0];
//                     n=kuai[kin1][k][1];
//                     xz(g,m,n);
//                  }
//               }
//            }//for
//         }//for
//              for(xun=linss+1;xun<=shoushu;xun++){
//                  tongse=(xun+1)%2+1;
//                  m=hui[xun][25];
//                  n=hui[xun][26];
//                 if(xun>linss+1){
//                   try{
//                       Thread.currentThread().sleep(800);
//                     }catch(InterruptedException e){
//                     }
//                  }
//                 if(tongse==1)
//                     g.setColor(Color.black);
//                 else
//                     g.setColor(Color.white);
//                  g.fillOval(qpdx*m+bjjx,qpdx*n+bjjx,tyzj,tyzj);
//                 if(tongse==2)
//                      g.setColor(Color.black);
//                 else
//                      g.setColor(Color.white);
//                 if(xun>=linss+10)
//                      g.drawString(""+(xun-linss),qpdx*m+bjjx+qpbj-7,qpdx*n+bjjx+qpbj+5);
//                 else
//                      g.drawString(""+(xun-linss),qpdx*m+bjjx+qpbj-4,qpdx*n+bjjx+qpbj+5);
//                 for(j=1;j<5;j++){
//                    if(hui[xun][2*j-1]<0) break;
//                    else {
//                        m=hui[xun][2*j-1];
//                        n=hui[xun][2*j];
//                        xz(g,m,n);
//                     }
//                  }
//           for(j=1;j<5;j++){
//               kin1=hui[xun][8+j];
//              if(kin1<=0) break;
//              else {
//                  p=kuai[kin1][0][1];
//                 for(k=1;k<=p;k++){
//                     m=kuai[kin1][k][0];
//                     n=kuai[kin1][k][1];
//                     xz(g,m,n);
//                  }
//               }
//            }//for
//         }//for
//         fdonghua=false;
//         }//if fdonghua==true
//         else{
//         for(i=0;i<=18;i++){
//            for(j=0;j<=18;j++){
//               if(zb[i][j][0]==1) {
//                  g.setColor (Color.black);
//                  g.fillOval(qpdx*i+bjjx,qpdx*j+bjjx,tyzj,tyzj);
//               }
//               else  if(zb[i][j][0]==2){
//                  g.setColor(Color.white);
//                  g.fillOval(qpdx*i+bjjx,qpdx*j+bjjx,tyzj,tyzj);
//               }
//            }//for j
//         }//for i
//          if(FSHUZI==true){
//            for( xun=linss+1;xun<=shoushu;xun++){
//               tongse=(1+xun)%2+1;
//               m=hui[xun][25];
//               n=hui[xun][26];
//              if(m<0||n<0){
//                  if(log.isDebugEnabled()) log.debug("zheng jie zhong you qi quan.");
//                  break;
//               }
//              if(zb[m][n][2]>0){
//                 if(tongse==1)
//                      g.setColor(Color.black);
//                 else
//                      g.setColor(Color.white);
//                   g.fillOval(qpdx*m+bjjx,qpdx*n+bjjx,tyzj,tyzj);
//                  if(tongse==2)
//                      g.setColor(Color.black);
//                 else
//                      g.setColor(Color.white);
//                 if(xun>=linss+10)
//                      g.drawString(""+(xun-linss),qpdx*m+bjjx+qpbj-7,qpdx*n+bjjx+qpbj+5);
//                 else
//                      g.drawString(""+(xun-linss),qpdx*m+bjjx+qpbj-4,qpdx*n+bjjx+qpbj+5);
//                 }//if
//             }//for
//          }
//         if(ftishi==true) {
//            g.setColor(Color.red);
//            m=gotemp.zba;
//            n=gotemp.zbb;
//            g.drawString("A",qpdx*m+qpbj+1,qpdx*n+qpbj+bjjx+5);
//            a=-1;
//            b=-1;
//         }
//         }//else
//      }//else fzhudong==false
//
//      g.setColor(Color.black);
//      for (i=10;i<19;i++){
//         for ( j=0;j<10;j++){
//            //g.drawString(""+zb[j+9][i][3],500+25*j,15*i-90);
//            kin1= zb[j+9][i][3];
//           if (kin1>0){
//               //g.drawString(""+kuai[kin1][0][0],30+25*j,15*i+230) ;
//            }
//            //g.drawString(""+zb[j+9][i][2],500+25*j,15*i+60);
//            //g.drawString(""+zb[j+9][i][0],500+25*j,15*i+230);
//         }
//      }//ti gong ge dian xin xi.
//      if(FSHUZI==false&&shoushu>linss){
//         if(hda>0&&hdb>0&&zb[hda][hdb][0]>0){
//           if(zb[hda][hdb][0]==1)g.setColor(Color.black);
//           else if(zb[hda][hdb][0]==2) g.setColor(Color.white);
//            g.fillOval(qpdx*hda+bjjx+qpbj-xbj,qpdx*hdb+bjjx+qpbj-xbj,xbj*2+1,xbj*2+1);
//         }
//         g.setColor(Color.red);
//         m=hui[shoushu][25];
//         n=hui[shoushu][26];
//         hda=m;
//         hdb=n;
//            //hdse=(1+shoushu)%2+1;
//         g.fillOval(qpdx*m+bjjx+qpbj-xbj,qpdx*n+bjjx+qpbj-xbj,xbj*2+1,xbj*2+1);
//      }//hua dang qian hong dian.
//   }
//
//}