package untitled8;
/**
 * <p>Title:计算征子 </p>
 */
public class JisuanBoard extends GoBoard{
   public JisuanBoard(GoBoard goboard) {
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
   public boolean yiqichi(byte ydm,byte ydn){//返回打吃点的一维坐标
      //借用0块储存信息,统一到函数yiqichi(int kin)
      boolean zzcl=false;
      kuai[0][0][0]=2;
      kuai[0][0][1]=1;
      kuai[0][1][0]=ydm;
      kuai[0][1][1]=ydn;
      kuai[0][50][0]=zb[ydm][ydn][4] ;
      kuai[0][50][1]=zb[ydm][ydn][5];
      kuai[0][51][0]=zb[ydm][ydn][6];
      kuai[0][51][1]=zb[ydm][ydn][7];
      zzcl= yiqichi((byte)0);
      for(byte i=0;i<70;i++){
         kuai[0][i][0]=0;
         kuai[0][i][1]=0;
      }//及时清楚副作用;
      return zzcl;
   }
   public boolean yiqichi(byte kin){//征子计算是预演,应该具有恢复到原局面的
   //功能,或者复制原局面,用副本进行计算.可能前者更佳,因为计算时也要倒退
   //kin是被征子的块的索引.
      byte i=0,j=0;//循环变量.
      byte m1=0,n1=0,m2=0,n2=0;//m1,n1为气点之一,m2,n2为气点之二.
      byte a,b;//用于传给cgcl()函数的坐标位置.
      byte cq1=0,cq2=0;//单子落下后甲方的气,判断出禁着点和气>=2;气==1表示未明确
      byte q1=0,q2=0;//q1,q2为交换两手后乙方的气.
      byte beitia=0,beitib=0,tizia=0,tizib=0;//征子反打有关,前为被提点后为提子点.
      boolean huitui=false;//回退,是在探索新局面,还是回到原局面进行新的选择.
      boolean fanda=false;//反打
      byte [][]hou=new byte[40][8];//hou xuan qi bu,候选棋步数组,从1算起
      //0:哪一手(宜从1算起);1-4为坐标,5为反打标志.40是指最多有40处有两种选择.
      byte ch=0;//ch为候选棋步计数.
      short ss=shoushu;//记录已有手数
      byte  qi=0;
      byte m,n;
      byte bzzfyanse=zb[kuai[kin][1][0]][kuai[kin][1][1]][0];//被征子方颜色
      byte zzfyanse;//征子方的颜色
      if(bzzfyanse==1)zzfyanse=2;
      else zzfyanse=1;
     //注意标示模型,否则难以理解.

      while(true)    {//必须知道被反打的坐标
         if (huitui==true){//改用已经存储的另一路选择
            huitui=false;//及早改回
            for(i=(byte)(shoushu-ss);i>=hou[ch][0];i--){
               clhuiqi();
            }
           if(hou[ch][5]==1){
             fanda=true;
             tizia=hou[ch][6];
             tizib=hou[ch][7];
             hou[ch][6]=0;
             hou[ch][7]=0;
           }
           else fanda=false;//恢复场景
            m1=hou[ch][1];//传给全局变量
            n1=hou[ch][2];//?
            m2=hou[ch][3];//传给全局变量
            n2=hou[ch][4];

            ch--;
            q2=2; //相当于只有一个候选点,可以合并代码
            q1=3;
         }

         else{//从头探索新的局面
            m1=kuai[kin][50][0];//征子点a的坐标,也即kin块的气点
            n1=kuai[kin][50][1];//50~69存储气的信息.
            m2=kuai[kin][51][0];//征子点b的坐标,也是kin块的气点
            n2=kuai[kin][51][1];

            cq1=zzddq(m1,n1);//落子于m1,n1后该点或所在块的气数
            cq2=zzddq(m2,n2);
           if(cq1>=1){
               q2=jhhdq(m2,n2,m1,n1);//落子于m1,n1.且m2,n2长气后该点或所在块的气数
            }
           else q2=3 ;
           if(cq2>=1){
               q1=jhhdq(m1,n1,m2,n2);//q1<2,不提子则无以结束.
            }
           else q1=3;
         }//else huitui==false

         if (q1>2&&q2>2){//kzzd=0;没有可以征子的点
            if(ch==0)return false;
            else {
                huitui=true;
               continue;
            }
         }
         else if(q1>=2&&q2>=2){
         //需要探索,不论是直接还是间接;不论huitui真假
           if(q1==2&&q2==2){//同时为2
              if(fanda==true){
                   hou[++ch][5]=1;
                   hou[ch][6]=tizia;
                   hou[ch][7]=tizib;
              }
              else hou[++ch][5]=0;
               hou[ch][1]=m2;//征子点
               hou[ch][2]=n2;
               hou[ch][3]=m1;
               hou[ch][4]=n1;

               hou[ch][0]=(byte)(shoushu+1-ss);//第几手的变化
            }
           if(q1==2&&q2!=2){
              byte t1=m1;
              byte t2=n1;
               m1=m2;
               n1=n2;
               m2=t1;
               n2=t2; //征子点唯一时,始终用m1,n1 表示打
               q1=q2;
               q2=2;
            }
         }
         else if(q2<2||q1<2){//有某处可局部结束征子kzzd=0
            if(q1<2&&q2<2){//同时为2
               if(fanda==true){
                  hou[++ch][5]=1;
                  hou[ch][6]=tizia;
                  hou[ch][7]=tizib;
               }
               else hou[++ch][5]=0;
               hou[ch][1]=m2;//征子点
               hou[ch][2]=n2;
               hou[ch][3]=m1;
               hou[ch][4]=n1;
               hou[ch][0]=(byte)(shoushu+1-ss);//第几手的变化
            }
            if(q1<2&&q2>=2){
               byte t1=m1;
               byte t2=n1;
               m1=m2;
               n1=n2;
               m2=t1;
               n2=t2;
            }
         }
         a=m1;//一个征子点或者选择了两点中的一点
         b=n1;
         cgcl(a,b);
         if(zb[a][b][2]==1) {//要考虑反打的问题,
            byte ksyi=zb[a][b][KSYXB];//可能块被提?考虑块的大小
            if(fanda==true){
               if(ch==0)return false;
               else {
                  huitui=true;
                 continue;
               }
            }
            else  if(ksyi>0&kuai[ksyi][0][1]>4){
              if(ch==0)return false;
              else {
                  huitui=true;
                 continue;
               }
            }
            fanda=true;
           if(ksyi==0){
               tizia=zb[a][b][4];
               tizib=zb[a][b][5];
               beitia=a;
               beitib=b;
            }
            else{
               tizia=kuai[ksyi][50][0];
               tizib=kuai[ksyi][50][1];
               beitia=0;//表示块被提
               beitib=0;//有扑入的选择
            }
         }
         if(q1>=2&&q2>=2){
            a=m2;
            b=n2;
            cgcl(a,b);
           if(kuai[ki][0][0]<2) {//可能需要判断结束与否
               clhuiqi();
               q2=1;
            }
         }

         if(q2<2||q1<2){//有某处可局部结束征子kzzd=0
            if (fanda==false) return true;
            else{
               if(ltdd(tizia,tizib,zzfyanse)==true) return false;
               a=tizia;//在反打处提子
               b=tizib;//已经判明没有连提带打?
               fanda=false;
               tizia=0;
               tizib=0;
               cgcl(a,b);
                  //   if(jszq(m2,n2)<=1) return 0;//是否?不入气
                   //不可能,fanda=true时,其小于1,其=3;
               if(beitia==0){//提了一块
               //  if(zb[kin][0][0]<2) return true;   //加上判断
                  if(kuai[ki][0][0]>2){//不必考虑q1,同样提子
                     if(ch==0)return false;
                     else {
                        huitui=true;
                        continue;
                     }
                  }//else
                  else continue;
               }
               a=m2;//提一子则兜吃
               b=n2;
               cgcl(a,b);
               a=beitia;//粘上
               b=beitib;
               beitia=0;
               beitib=0;
               cgcl(a,b);
            } //else
         }
         if(zb[kin][0][0]<2) return true;//交换后的总体判断
         else if(zb[kin][0][0]>2){//不必考虑q1,同样提子
            if(ch==0)return false;
            else {
               huitui=true;
               continue;
            }
         }//else
         else {
            if(hui[shoushu][27]>=0){//打吃一子
               if(fanda==true) {
                  if(ch==0)return false;
                  else{
                     huitui=true;
                     continue;
                  }
               }
               else{
                  fanda=true;
                  beitia=hui[shoushu][27];
                  beitib=hui[shoushu][28];
                  tizia=zb[beitia][beitib][4];
                  tizib=zb[beitia][beitib][5];
                  continue;
               }
            }
            else if(hui[shoushu][33]>0){//打吃块,近似失败
               if(ch==0)return false;
               else{
                  huitui=true;
                  continue;
               }
            }
            if(fanda==true&&ltdd(tizia,tizib,zzfyanse)){
               if(ch==0) return false;
               else{
                  huitui=true;//后退到前一分叉
                  continue;
               }
            }
         }
      }//do
   }//yiqichi
   public short xiayishou(){//返回下一手的一维表示
      byte a,b;
      while(true){
         a=(byte)(Math.random()*19+1);
         b=(byte)(Math.random()*19+1);
         if(zb[a][b][ZTXB]==0)  return(short)(b*19+a-19);
      }
   }
   public boolean ltdd(byte m1,byte n1,byte  zzfys){//m1,n1为提子点
      byte tongse=zzfys;//征子方颜色
      byte m,n;
      for(byte i=0;i<4;i++){
         m=(byte)(m1+szld[i][0]);
         n=(byte)(n1+szld[i][1]);
        if(zb[m][n][ZTXB]==zzfys&&zb[m][n][QSXB]<=2) return true;
      }//ltdd
      return false;

  }
   public byte  jhhdq(byte m1,byte n1,byte m2,byte n2){
    //交换后的气,打在m2,n2,m1,n1能长为几气;m2,n2>=1气才调用该函数
   //返回1表示只有提子一种选择
      byte qi=0;//必须重写新的函数,因为不能真正落子(初步判断,节省计算)
      byte tongse=(byte)(shoushu%2+1);
      byte  m,n;
      for(byte i=0;i<4;i++){
         m=(byte)(m1+szld[i][0]);
         n=(byte)(n1+szld[i][1]);
        if(zb[m][n][ZTXB]==BLANK){//2.1the breath of blank
           if(m==m2&&n==n2) {//m1+1点可能不能增气.两点互相为气.
              // if(jszq(m2,n2)==1) qi++;//提子则能增气??==1未必是一气
              //不可能提子!原气>=2;
              //如果m2,n2被提,则少算了一气
            }else qi++;
         }
      }
      if (qi==3) return 3;//该方向的征子已经不可能成立
    //  else  return 0;//修改,2月22日,只计算空白处,低下代码没用
      for(byte i=0;i<4;i++){
         m=(byte)(m1+szld[i][0]);
         n=(byte)(n1+szld[i][1]);
         if(zb[m][n][ZTXB]==tongse){//2.1the breath of blank
            qi+=zb[m][n][QSXB];
            qi--;
         } //可能周围两点为同一块,气数多算了.
      }
      if(qi>1) return 2;//2是不确定的,所以要实际计算
      else return qi;//0是真实的,1是有效得的(1可能为0)
   }
   public byte zzddq(byte m1, byte n1){//预算征子点的气
      byte qi=0;//气数变量
      byte tongse=(byte)(shoushu%2+1);//此时手数没有递增
      byte m,n;
      for(byte i=0;i<4;i++){
         m=(byte)(m1+szld[i][0]);
         n=(byte)(n1+szld[i][1]);
        if(zb[m][n][ZTXB]==BLANK){//2.1the breath of blank
            qi++;
         }
      }//只考虑直接的气,简单很多,而且达到了预算的目的,因为大多数的征子是这种情况
      if(qi>=1)return qi;//qi==3,应后仍>=2气;qi==2,应后可能被反打
      //qi==1;已经处于反打状态(错误, 可能成块,只能表明有一个直接的气).
      //qi==1只表示需要进一步计算,实际>=1气.qi==0表示禁着点.
      else if(qi==0){
         for(byte i=0;i<4;i++){
            m=(byte)(m1+szld[i][0]);
            n=(byte)(n1+szld[i][1]);
           if(zb[m][n][ZTXB]==tongse){
               qi+=zb[m][n][QSXB];
               qi--;
            }
         }
        if(qi==0){
           byte yise=(byte)((shoushu+1)%2+1);
           for(byte i=0;i<4;i++){
               m=(byte)(m1+szld[i][0]);
               n=(byte)(n1+szld[i][1]);
              if(zb[m][n][ZTXB]==yise&&zb[m][n][QSXB]==1){
                 return 1;//需要进一步判断
               }
            }
           return 0;
         }
        else return 1;//表示需要进一步计算.
      }
      return 1;//编译器有问题,没有这一句就不能通过
   }//只能区别有没有气.而且没有考虑打劫(虽然没有气,但是提子后可以增一气).
}//return boolean 即可 ,征子过程保存在hui数组中.
