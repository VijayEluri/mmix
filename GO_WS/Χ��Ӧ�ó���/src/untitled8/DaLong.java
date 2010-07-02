package untitled8;

/**
 * <p>Title: 数字对杀的表示方法</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DaLong {
   byte qishu;
   byte color;
   byte yanxing; //1：无眼；2：小眼；3：大眼；

   byte jieguo1; //本方先走的结果
   byte jieguo2; //本方后走的结果;都是从本方的立场出发；
   public static final byte huosheng = 3;
   public static final byte dajie = 2;
   //以后要考虑劫的详细分类；
   public static final byte shibai = -3;
   public static final byte shuanghuo = 0;

   byte xsx; //xian shou xing;先手性
   //0标识临界，即先走的一方获胜或双活，结果与谁先走有关、
   //1标识已经获胜或双活，2标识不仅获胜，且没有劫材的利用。
   //不能排除自杀的劫材；只是指不改变结果的手顺作为劫材；
   //－1标识已经失败，但是可以先手利用；获得更好的结果；
//   可能是从失败到获胜，也可能从失败到双活，也可能从双活到获胜
//   //－2标识已经失败，而且没有先手利用。

   //结果从不同的角度来看，有不同的表达；比如从本方角度看，是劫活；
   //从对方角度看就是劫杀；反之亦然。可见它们是本质同一的。
   //对杀的结果表示和死活题的结果表示也不尽相同；区别却比较微妙、
   //死活题可能只有某方有死活的差别。而对杀是整体的。两方都卷入。


   public byte gongqi(DaLong dl1, DaLong dl2) {
      byte k = 0;
      return k;
   }


   public void duisha(DaLong dl1, DaLong dl2) {
      //从dl1的角度出发看待胜负；由dl1先走。
      //;参数的含义与原来的duishas函数不同、0标识临界，即先走的一方
      //获胜，1标识已经获胜，2标识不仅获胜，且没有劫材的利用。－1标识
      //已经失败，但是可以先手利用；－2标识已经失败，而且没有先手利用。
      //如何表示对杀的结果，因为对杀的结果除了胜负之外，还有共活的情况、
      //用数字来进行对杀的前提是已经没有长气的手段。
      byte gongqishu = gongqi(dl1, dl2);
      byte qi1 = dl1.qishu;
      byte qi2 = dl2.qishu;
      byte yx1 = dl1.yanxing;
      byte yx2 = dl2.yanxing;
      if ( (yx1 != yx2) || (gongqishu == 0) || (yx1 == 1 && gongqishu < 2)) {
         if (yx1 > yx2) { //眼型级别不同，不可能共活
            qi1 += gongqishu;

         }
         else if (yx1 < yx2) { //公气属于眼型好的一方；
            qi2 += gongqishu;
         }//眼型相同，公气少不能构成双活时，公气双方共有；
         if (qi1 > qi2) {
            dl1.jieguo1 = huosheng; //对杀获胜
            dl1.jieguo2 = huosheng;
            dl2.jieguo1 = shibai;
            dl2.jieguo2 = shibai;
            dl1.xsx = (byte) (qi1 - qi2);
            dl2.xsx = (byte) (qi2 - qi1);
         }
         else if (qi1 < qi2) { //对杀失败
            dl1.jieguo1 = shibai; //对杀失败
            dl1.jieguo2 = shibai;
            dl2.jieguo1 = huosheng;
            dl2.jieguo2 = huosheng;

            dl1.xsx = (byte) (qi1 - qi2);
            dl2.xsx = (byte) (qi2 - qi1);
         }
         else if (qi1 == qi2) {
            dl1.jieguo1 = huosheng;
            dl1.jieguo2 = shibai;
            dl2.jieguo2 = shibai;
            dl2.jieguo1 = huosheng;
            dl1.xsx = 0;
            dl1.xsx = 0; //两者是正负对称关系。
         }

      }
      //if(yx1==yx2);眼型级别相同
      else {
         //else if (gongqishu >= 2) { //有共活的可能
         if (qi1 > qi2) {
            if (yx1 == 1 ) { //都没有眼
               qi2 += gongqishu;
               qi2--;
            }else{
               qi2 += gongqishu;
            }
            if (qi1 > qi2) {
               dl1.jieguo1 = huosheng; //对杀获胜
               dl1.jieguo2=huosheng;
               dl2.jieguo2 = shibai;
               dl2.jieguo2=shibai;
               dl1.xsx = (byte) (qi1 - qi2);
               dl1.xsx = (byte) (qi2 - qi1);
            } //return (byte)(qi1-qi2);
            //为零则临界；先行胜，后手则双活；
            else if (qi1 < qi2) {
               dl1.jieguo1 = shuanghuo; //双活
               dl1.jieguo2 = shuanghuo;
               dl2.jieguo1 = shuanghuo;
               dl2.jieguo2 = shuanghuo;
               dl1.xsx = (byte) (qi1 - qi2); //很复杂
               dl2.xsx = (byte) (qi2 - qi1);//不用考qi2
               //反过来的先手利用，它要吃qi1是太遥远了。
            }
            //return  (byte)(qi1-qi2);
            //为－1则已经双活；
            else if (qi1 == qi2) {
               dl1.jieguo1 = huosheng;
               dl1.jieguo2 = shuanghuo;
               dl2.jieguo1=shuanghuo;
               dl2.jieguo2 = shibai;
               dl1.xsx = 0;
               dl2.xsx= 0;
            }

         }

         else if (qi1 < qi2) {
            if (yx1 == 1 ) { //都没有眼
               qi1 += gongqishu;
               qi1--;
            }else{
               qi1 += gongqishu;
            }
            if (qi1 > qi2) {
               dl1.jieguo1 = shuanghuo; //双活
               dl1.jieguo2=shuanghuo;
               dl2.jieguo1 =shuanghuo;
               dl2.jieguo2=shuanghuo;
               dl1.xsx = (byte) (qi1 - qi2);
               dl2.xsx = (byte) (qi2 - qi1);
               //其实这里因为是双活，对每一方来讲都可以净，也可以先手利用
            }
            else if(qi1<qi2){
               dl1.jieguo1 = shibai; //双活
               dl1.jieguo2=shibai;
               dl2.jieguo1 =huosheng;
               dl2.jieguo2=huosheng;
               dl1.xsx = (byte) (qi1 - qi2);
               dl2.xsx = (byte) (qi2 - qi1);
            }
            else if(qi2==qi1){
               dl1.jieguo1 = shuanghuo;
               dl1.jieguo2 = shibai;
               dl2.jieguo1=huosheng;
               dl2.jieguo2 = shuanghuo;
               dl1.xsx = 0;
               dl2.xsx= 0;

            }
         }
         else if(qi1==qi2){
            dl1.jieguo1 =shuanghuo;
            dl1.jieguo2=shuanghuo;
            dl2.jieguo1 =shuanghuo;
            dl2.jieguo2=shuanghuo;
            dl1.xsx=gongqishu;
            dl2.xsx =gongqishu;
         //xsx：先手性数字太大（2以上）就没有实际意义了，
         }

      }

   }






   public DaLong() {
   }

}
/*else if (yx1 == 2 || yx1 == 3) { //都有小眼或大眼
         if (qi1 > qi2) {//获胜或双活；
            qi2 += gongqishu;
            //qi2--;
            if (qi1 > qi2) {
               dl1.jieguo1 = huosheng; //对杀获胜
               dl1.jieguo2=huosheng;
               dl2.jieguo2 = shibai;
               dl2.jieguo2=shibai;
               dl1.xsx = (byte) (qi1 - qi2);
               dl1.xsx = (byte) (qi2 - qi1);
            } //return (byte)(qi1-qi2);
            //为零则临界；先行胜，后手则双活；
            else if (qi1 < qi2) {
               dl1.jieguo1 = shuanghuo; //双活
               dl1.jieguo2 = shuanghuo;
               dl2.jieguo1 = shuanghuo;
               dl2.jieguo2 = shuanghuo;
               dl1.xsx = (byte) (qi1 - qi2); //很复杂
               dl2.xsx = (byte) (qi2 - qi1);//不用考qi2
               //反过来的先手利用，它要吃qi1是太遥远了。
            }
            //return  (byte)(qi1-qi2);
            //为－1则已经双活；
            else if (qi1 == qi2) {
               dl1.jieguo1 = huosheng;
               dl1.jieguo2 = shuanghuo;
               dl2.jieguo1=shuanghuo;
               dl2.jieguo2 = shibai;
               dl1.xsx = 0;
               dl2.xsx= 0;
            }

         }
         else if (qi1 < qi2) {//只可qi2获胜或者双活、
            qi1 += gongqishu;
            qi1--;
            if (qi1 > qi2) {
               dl1.jieguo1 = shuanghuo; //双活
               dl1.jieguo2=shuanghuo;
               dl2.jieguo1 =shuanghuo;
               dl2.jieguo2=shuanghuo;
               dl1.xsx = (byte) (qi1 - qi2);
               dl2.xsx = (byte) (qi2 - qi1);
               //其实这里因为是双活，对每一方来讲都可以净，也可以先手利用
            }
            else if(qi1<qi2){
               dl1.jieguo1 = shibai; //双活
               dl1.jieguo2=shibai;
               dl2.jieguo1 =huosheng;
               dl2.jieguo2=huosheng;
               dl1.xsx = (byte) (qi1 - qi2);
               dl2.xsx = (byte) (qi2 - qi1);
            }
            else if(qi2==qi1){
               dl1.jieguo1 = shuanghuo;
               dl1.jieguo2 = shibai;
               dl2.jieguo1=huosheng;
               dl2.jieguo2 = shuanghuo;
               dl1.xsx = 0;
               dl2.xsx= 0;

            }
         }
         else if(qi1==qi2){
            dl1.jieguo1 =shuanghuo;
            dl1.jieguo2=shuanghuo;
            dl2.jieguo1 =shuanghuo;
            dl2.jieguo2=shuanghuo;
            dl1.xsx=gongqishu;
            dl2.xsx =gongqishu;
         //xsx：先手性数字太大（2以上）就没有实际意义了，
         }
      }
   }*/
/* public void duishas(DaLong dl1,DaLong dl2,byte color){
       //从dl1的角度出发看待胜负；color表示由谁先走。
       byte gongqishu=gongqi(dl1,dl2);
       byte qi1=dl1.qishu;
       byte qi2=dl2.qishu;
       byte yx1=dl1.yanxing ;
       byte yx2=dl2.yanxing ;
       if (yx1>yx2){//眼型级别不同，不可能共活
          qi1+=gongqishu;
          if(qi1>qi2) return 1;//对杀获胜
          //余一气即为已经定型，余两气即为已经确定且干净，没有劫材；
          else if(qi1<qi2) return 0;//对杀失败
          else if(dl1.color ==color) return 1;
          else return 0;
       }
       else if(yx1<yx2){//公气属于眼型好的一方；
          qi2+=gongqishu;
          if(qi1>qi2) return 1;//对杀获胜
          else if(qi1<qi2) return 0;//对杀失败
          else if(dl1.color ==color) return 1;
          else return 0;
       }
       //if(yx1==yx2);眼型级别相同
       else if(yx1==1){//都没有眼
          if(gongqishu<=1){//不可能共活
          }
          else if(gongqishu>2){//有共活的可能
          }
       else if(yx1==2){//都有小眼
          }
       else if (yx1==3){// 都有大眼
          }
       }
//结果有两种表达：第一种，不考虑由谁先走，或者说平价的是局面
//另一种：根据轮到谁走来确定结果；
    }*/
