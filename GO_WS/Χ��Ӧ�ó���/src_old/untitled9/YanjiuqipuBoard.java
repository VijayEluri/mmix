package untitled9;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 * 骗着也可以用，因为骗着之后的变化也是非常多的，难以穷尽；而且有时
 * 全局形势可能适合使用骗着。这时就更有效了。唯一的问题是；用骗着就
 * 是为了避开大家都熟悉的定式套路，但是现在资讯发达，骗着的普及率越
 * 来越高，效果不太理想，最好是自己的研究心得作为骗着，因为别人一般
 * 不可能事先刚好也研究过；要做到每个定式或者布局都掌握至少一个秘密
 * 武器。
 * 对棋谱的学习研究是回溯型的，总是因为结果的意料之外，开始怀疑前面
 * 是否正确。要实现这个功能，必须进行形势判断，通过前后的形势对比来
 * 得出结论。因为变化越往后，结论越准确。最简单的就是没看出对方的吃
 * 子手段需要悔棋。
 */
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import untitled10.QiKuai1;
import untitled3.DsNode;

public class YanjiuqipuBoard extends untitled8.GoBoardLian1 {
	private static final Logger log = Logger.getLogger(QiKuai1.class);
   DsNode root, temp, work, temp1, temp2;
   DsNode[] stackds = new DsNode[25]; //SGF格式用
   short zshoushu;//棋谱的总手数。
   byte[][] zuijin=new byte[21][21];//每点的相关块号。
   byte[] zuijinkh=new byte[4];//距离最近点的块号。
   byte[] zuijinfs=new byte[4] ;//最近块的接近方式。
   byte dangqiankh;
   final static byte ZKUOHAO = -127;
   final static byte YKUOHAO = 127;
   byte xgkjs;//相关块的总数

   public YanjiuqipuBoard(String qipuwenjianming) {
      //从棋谱文件读入棋谱并初始化hui数组,zshoushu的赋值
      //可包括不同棋谱文件的转化。

     //第一手肯定为独立点，且应在左上角。
     if (hui[1][0] > 10 || hui[1][1] > 10) {
        if(log.isDebugEnabled()) log.debug("棋谱格式错误！");
        trans(); //改为标准型
      }


   }
   byte julia(byte a,byte b,byte c,byte d){//两点间的距离
      return (byte)(Math.abs(a-c)+Math.abs(b-d));
      //第一种定义：坐标插绝对值的和。
   }
   byte julib(byte a,byte b,byte c,byte d){//距离的第二种定义
      byte m=(byte)(Math.abs(a-c));
      byte n=(byte)(Math.abs(b-d));
      byte t=0;
      if(m>n){
         t=n;
         n=m;
         m=t;
      }//坐标差的最大值。
         return n;
   }

   byte juli(byte a,byte b,byte c,byte d){//距离的第二种定义
      byte m=(byte)(Math.abs(a-c));
      byte n=(byte)(Math.abs(b-d));
      //byte t=(byte)Math.sqrt(m*m+n*n);
      //return t;     //两点间的距离
      int t=m*m+n*n;
      if(t>127) return (byte)(-1);//－1表示距离太远。
      else return (byte)t;        //正常返回距离的平方。
  }

   void trans(){
   //将棋谱转为标准型。
   }

    public void init() { //初始化,完成界面
     byte fenzhi = 0; //SGF格式用,与stackds构成堆栈

     try {
        byte lina = 0;
        byte[] dsnode = new byte[5];
        boolean ykhjs = false; //是否已经有右括号。
        DataInputStream in = new DataInputStream(
           new BufferedInputStream(
           new FileInputStream("H:\\weiqidata\\" + "定式树")));
        if (in.available() == 0) {
           return;
        }
        lina = in.readByte();
        if (lina == ZKUOHAO) { //成型定式树应该第一步有五种以上变化。
           in.read(dsnode); //左括号不会相连。
           root = work = new DsNode(dsnode[1], dsnode[2], dsnode[3],
                                    dsnode[4], dsnode[0]);
           if(log.isDebugEnabled()) log.debug("定式树的根为：(a="+
                              work.zba+","+work.zbb+")" );
           //整棵树不用括号包围
           //括号仅表示括号内是父节点的分支；
           fenzhi++;
           stackds[fenzhi] = work; //右括号结束后新的左括号接到work上。
           if(log.isDebugEnabled()) log.debug("第一层括号的工作点为：(a="+
               stackds[fenzhi].zba+","+stackds[fenzhi].zbb+")" );
        }
        else { //定式树初始时可能只有一种起点。
           in.read(dsnode, 1, 4);
           root = work = new DsNode(dsnode[1], dsnode[2], dsnode[3],
                                    dsnode[4], lina);
           if(log.isDebugEnabled()) log.debug("定式树的根为：(a="+
                              work.zba+","+work.zbb+")" );
        }
        while (in.available() != 0) {
           lina = in.readByte();
           if (lina == ZKUOHAO) {
              in.read(dsnode);
              temp = new DsNode(dsnode[1], dsnode[2], dsnode[3],
                                dsnode[4], dsnode[0]);
              if(log.isDebugEnabled()) log.debug("分支变化的首节点为:"+ temp.zba
                                 +","+temp.zbb+")");
              if (ykhjs == false) {
                 //新的左括号
                 work.left = temp;
                 fenzhi++;
                 work = temp;
                 stackds[fenzhi] = work;
                 if(log.isDebugEnabled()) log.debug("新一层括号的工作点为：(a="+
               stackds[fenzhi].zba+","+stackds[fenzhi].zbb+")" );
              }
              else { //右括号结束；并列的作括号
                 //遇到新的作括号，ykhjs失效。
                 ykhjs=false;
                 work.right = temp;
                 if(log.isDebugEnabled()) log.debug("同一层括号的原工作点为：(a="+
               stackds[fenzhi].zba+","+stackds[fenzhi].zbb+")" );
                 work = temp;
                 stackds[fenzhi] = work;
                 if(log.isDebugEnabled()) log.debug("同一层新的左括号的工作点为：(a="+
               stackds[fenzhi].zba+","+stackds[fenzhi].zbb+")" );
              }
           }
           else if (lina == YKUOHAO) {
              if (ykhjs == false) {
                 ykhjs = true;
                 work = stackds[fenzhi];
                 if(log.isDebugEnabled()) log.debug(work.toString() );
              }
              else {
                 stackds[fenzhi--] = null;
                 work = stackds[fenzhi];
              }
           }
           else { //连续节点
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
        if(log.isDebugEnabled()) log.debug("打开文件（定式树）遇到问题！");
        if(log.isDebugEnabled()) log.debug("Exception" + ex.toString());
     }
     //增加布局树的初始化。
  }

   void pipeidingshi(byte m,byte n){

      for(DsNode linshi=root;linshi!=null;linshi=linshi.right){
         if(linshi.zba ==m&&linshi.zbb==n){

         }
      }


   }
   //统计每块三线，四线子，及五、六线子的个数，作为内外的参考标准
   void read(){//从棋谱中获取知识并存储在布局树中，用单一标准型
      byte [][]juli={{0,0},{1,0},{1,1},{2,0},{2,1},{2,2},{3,0},
         {3,1},{3,2},{4,0},{4,1},{3,3},{4,2},{5,0},{4,3},{5,1}};
      //距离相等但不同的型可以旋转和翻转得到。
      //不同的相对坐标标识不同的型，同时也区分不同的距离远近。
      DsNode [] dingshiwork=new DsNode[4];
      //匹配四角时不同的当前工作点。
      byte []othercolor=new byte[4];//相关块的轮走方颜色。
      byte dingshishu=0;//已经找到的定式数；
      byte syshoum=0;
      byte ssyshoum=0;
      byte syshoun=0;
      byte ssyshoun=0;
      byte julim=0;//两点间的横坐标之差；
      byte julin=0;//两点间的竖坐标之差。
      byte zbm=0;  //附近点的横坐标；
      byte zbn=0;
      byte kongjiaoshu =4;//剩余的空角数。
      byte dangqianm,dangqiann;//棋谱输入中当前点的横竖坐标；
      byte zjkjs=0;//最近块的计数，同一块不能反复记入。
      byte t;
      short i;
      boolean tongkuai=false;
      byte j,k;
      for(i=1;i<=zshoushu;i++){//遍历棋谱。
         //简单的把棋谱存在数组中，既便别的程序采用更复杂的棋谱形式
         //也可以简单的转化为一个数组。
         dangqianm=hui[i][25];
         dangqiann=hui[i][26];
         cgcl(hui[i][25],hui[i][26]);

         if(i==1){//第一手肯定为独立点，且应在左上角。
            if(dangqianm>10||dangqiann>10){
               if(log.isDebugEnabled()) log.debug("棋谱格式错误！");
               //trans();改为
            }


         }else{//先判断是否为独立点
            for(j=1;j<=11;j++){//按距离扫描各点；
               julim=juli[i][0];
               julin=juli[i][1];
               if(julim==julin||julin==0){//有四个对称点
                 // 转化：(a,b)变成（－b，a）
                 for(k=0;k<4;k++){
                     t=julim;
                     julim=(byte)(-julin);
                     julin=t;
                     zbm=(byte)(hui[i][25]+julim);
                     zbn=(byte)(hui[i][26]+julin);
                     if(zbm>0&&zbm<20&&zbn>0&&zbn<20){
                        if(zb[zbm][zbn][0]>0) {
                           tongkuai=false;
                           for(byte tkt=0;tkt<zjkjs;tkt++){
                              if(zuijin[zbm][zbn]==zuijinkh[tkt])
                                 tongkuai=true;
                           }
                           if(tongkuai=false){
                              zuijinkh[zjkjs]=zuijin[zbm][zbn];
                              zuijinfs[zjkjs++]=j;
                           }
                        }
                     }

                  }
               }
               else{//有八个对称点。
                  //同样转化，加上
                  for(k=0;k<4;k++){
                     t=julim;
                     julim=(byte)(-julin);
                     julin=t;
                     zbm=(byte)(hui[i][25]+julim);
                     zbn=(byte)(hui[i][26]+julin);
                     if(zbm>0&&zbm<20&&zbn>0&&zbn<20){
                        if(zb[zbm][zbn][0]>0) {
                           tongkuai=false;
                           for(byte tkt=0;tkt<zjkjs;tkt++){
                              if(zuijin[zbm][zbn]==zuijinkh[tkt])
                                 tongkuai=true;
                           }
                           if(tongkuai=false){
                              zuijinkh[zjkjs]=zuijin[zbm][zbn];
                              zuijinfs[zjkjs++]=j;
                           }

                        }
                     }
                 }  //循环后julim和julin回到原值。
                 t=julim;
                 julim=julin;
                 julin=t;
                 for(k=0;k<4;k++){
                     t=julim;
                     julim=(byte)(-julin);
                     julin=t;
                     zbm=(byte)(hui[i][25]+julim);
                     zbn=(byte)(hui[i][26]+julin);
                     if(zbm>0&&zbm<20&&zbn>0&&zbn<20){
                        if(zb[zbm][zbn][0]>0) {
                           tongkuai=false;
                           for(byte tkt=0;tkt<zjkjs;tkt++){
                              if(zuijin[zbm][zbn]==zuijinkh[tkt])
                                 tongkuai=true;
                           }
                           if(tongkuai=false){
                              zuijinkh[zjkjs]=zuijin[zbm][zbn];
                              zuijinfs[zjkjs++]=j;
                           }

                        }
                     }
                 }
               }
            }//for j;
            if(zjkjs==0){//附近无子。
               xgkjs++;//新的相关块、
               zuijin[dangqianm][dangqiann]=xgkjs;
               //匹配定式？第一步
               dangqiankh=xgkjs;
               pipeidingshi(dangqianm,dangqiann);
            }
            else if(zjkjs==1){
               if (zuijinkh[1]==dangqiankh){//没有脱先
               zuijin[dangqianm][dangqiann]=dangqiankh;
               }//前者是最近的相关块
               else{//换到它处
                  dangqiankh=zuijinkh[1];
                  if(othercolor[dangqiankh]==
                     zb[dangqianm][dangqiann][0]){//继续定式

                  }else{//脱先的定式

                  }
               }
            }else {//两块或两块以上。
               if (zuijinkh[1]==dangqiankh){//没有脱先
                   //更改原先多个相关块的块号，形成新的块；

               }//前者是最近的相关块
               else{


               }

            }

            /*else{ //并非另辟空角或边。例如吊入大空
               //总之是在继续某处的变化而不是重新起手。
               boolean tuoxian=true;
               for(byte w=1;w<=zjkjs;w++){
                  if (zuijinkh[w]==dangqiankh){
                     //如果与多处相关，则不能称为定式，具有全局相关性
                     //可以考虑作为布局定式，如星位的四间夹。
                     tuoxian=false;
                     break;
                  }else{

                  }
               }
               if(tuoxian==true){
                 //已有的相关块进行定式匹配。
                 dangqiankh=zuijinkh[1];
                 //是否回头进行另一处的定式。延续它处的变化
               }
               else{
               //延续本处的变化。
               //是否离当前块不是最近。另一种意义的脱先，但保持与当前块
               //的相关性。
               }
            }*/
            byte julis=juli(hui[i][25],hui[i][26],
                             hui[i-1][25],hui[i-1][26]);
            byte juliss=juli(hui[i][25],hui[i][26],
                              hui[i-2][25],hui[i-2][26]);
            if(julis<juliss){

            }

         }//else
      }//for

   }//method
}
//9月14日；
//下在棋盘上的点可以分类为：
//独立点：周围没有子（不论同色还是异色）
//如打入、开拆、分投、侵消。
//要区分开拆和打入；
//成块点：和已经有的己方子成块，也可能同时有异色子相邻。
//如长、冲、贴和粘。
//紧气点：和已经有的对方子相邻，紧气，但是没有同色子相邻。
//如挖、碰、靠。