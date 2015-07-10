package eddie.wu.linkedblock;

import org.apache.log4j.Logger;

public class QiKuai1 implements Cloneable{

	private static final Logger log = Logger.getLogger(QiKuai1.class);
   public DianNode1 zichuang; //气块中各子的链表。
   public short zishu; //气块的子数；初始的气块子数为361。

   public HaoNode1 zwzkhao; //周围形成气块的棋块的第一个号。
   byte zwzkshu;

   public byte color; //也许多余，因为他从属于某个块,但是方便。
   //5表示公气，反正周围的块有黑和白。这样的块简单处理，黑1白2。
   public static final byte GONGQIKUAI = 5;

   public byte dingdianshu; //单眼而言。
   public byte yanxing; //公气，假眼，真眼。
   //直接读入局面时，只能先统一生成气块，再决定yanxing；

   public byte minqi; //形成眼的周围块气数的最小值。
   //如果小于等于2，就可能被打吃或者提子，眼位就没有了。
   //当然有打劫的抵抗。
   //如果等于1，就有反提或者劫争；
   public static final byte ZHENYAN=1;


   //HaoNode1 qkhao; //气块的号，就是眼，可能是假眼，也可能是打劫。
   //先用气数判断强弱，再赋予地，根据眼位大小判断强弱。
   public QiKuai1() {

   }

   public void addzikuaihao(short kin) {
      if (zwzkhao == null) {
         zwzkhao = new HaoNode1(kin);
         zwzkshu = 1;
      }
      else {
         HaoNode1 linshi = zwzkhao;
         byte i = 1;
         for (; i <= zwzkshu; i++) {
            if (linshi.hao == kin) {
               break;
            }
            linshi = linshi.next;
         }
         if (i <= zwzkshu) {
            return; //已经有这个块号。
         }
         zwzkshu++;
         HaoNode1 temp = new HaoNode1(kin);
         temp.next = zwzkhao; //块号加在前头。
         zwzkhao = temp;
      }

   }

   public QiKuai1(QiKuai1 old) {
      super();
      zishu = old.zishu;
      color = old.color;
      yanxing = old.yanxing;
      minqi = old.minqi;
      dingdianshu = old.dingdianshu;
      zwzkshu = old.zwzkshu;

      DianNode1 temp;
      DianNode1 back;
      //if(log.isDebugEnabled()) log.debug("qikuaizishuwei:"+zishu);

      //复制气块的子串。
      if(old!=null){
         temp = old.zichuang;
      }else{
         return;
      }
      if(temp!=null){
         zichuang = new DianNode1(temp.a, temp.b);
      }else{
         return;
      }
      back = zichuang;
      if(back==null)  if(log.isDebugEnabled()) log.debug("back==null");

      for (short i = 2; i <= zishu; i++) {
         //子数必须为short，否则会无线循环。，
         temp = temp.next;
         if(temp==null) {
            if(log.isDebugEnabled()) log.debug("temp==null" + i);
            break;
         }
         if(back==null)  if(log.isDebugEnabled()) log.debug("back==null"+i);
         back.next = new DianNode1(temp.a, temp.b);
         back = back.next;
      }

      HaoNode1 qkhtemp;
      HaoNode1 qkhback;
      qkhtemp = old.zwzkhao;
      if(qkhtemp!=null){//复制周围子块。
         zwzkhao = new HaoNode1(qkhtemp.hao);
      }else{
         return;
      }
      qkhback = zwzkhao;
      for (byte i = 2; i <= zwzkshu; i++) {
         qkhtemp = qkhtemp.next;
         qkhback.next = new HaoNode1(qkhtemp.hao);
         qkhback = qkhback.next;
      }

   }

   public void addzidian(byte m1, byte n1) {
      if (zichuang == null) {
         zichuang = new DianNode1(m1, n1);
         zishu = 1;
      }
      else {
         zishu++;
         DianNode1 temp = new DianNode1(m1, n1);
         temp.next = zichuang;
         zichuang = temp;
      }
   }

   public void init() { //用于棋盘的最初完整气块。
      zishu = 0;
      DianNode1 temp;
      byte i, j;
      for (i = 1; i < 20; i++) {
         for (j = 1; j < 20; j++) {
            zishu++;
            temp = new DianNode1(i, j);
            temp.next = zichuang;
            zichuang = temp;

         }

      }
      if(log.isDebugEnabled()) log.debug("zishju=" + zishu);
   }

   public void deletezidian(byte m1, byte n1) {
      DianNode1 temp = zichuang;
      DianNode1 forward = zichuang;
      //if(log.isDebugEnabled()) log.debug("进入deletezidian:原始子数为" + zishu);
      for (short i = 1; i <= zishu; i++) {
         if (m1 == temp.a & n1 == temp.b) {
            if (i == 1) {
               zichuang = temp.next;

            }
            else {
               forward.next = temp.next;
            }

         }
         else {
            forward = temp;
            temp = temp.next;
         }
      }
      zishu--;
      //if(log.isDebugEnabled()) log.debug("退出deletezidian:现在子数为" + zishu);
   }
   public Object clone(){
      QiKuai1 o=null;

      try{
         o=(QiKuai1)super.clone();

      }
      catch (CloneNotSupportedException de){

      }

      if (zichuang != null) {
            o.zichuang = (DianNode1) (zichuang.clone());
         }
         if (zwzkhao != null) {
            o.zwzkhao = (HaoNode1) (zwzkhao.clone());
         }

      return o;

   }

}