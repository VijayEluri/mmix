package untitled9;
import untitled4.*;
public class QiKuai {
  public DianNode zichuang; //气块中各子的链表。
  public byte zishu; //气块的子数；
  public DianLian zilian;
  public QiKuai(){
     zilian=new DianLian();
     zishu=0;
     zichuang=null;
     color=0;
     dingdianshu=0;
     yanxing=0;
     minqi=0;
         zwkhao=new HaoLian();
  }
  public byte color;//也许多余，因为他从属于某个块。
  public byte dingdianshu; //单眼而言。
  public byte yanxing; //公气，假眼，真眼。
  public byte minqi; //形成眼的周围块气数的最小值。
  //如果小于等于2，就可能被打吃，眼位就没有了。
  //当然有打劫的抵抗。
  //如果等于1，就有反提或者劫争；
  public HaoLian zwkhao; //周围棋块的号。
  //HaoNode qkhao; //气块的号，就是眼，可能是假眼，也可能是打劫。
  //先用气数判断强弱，再赋予地，根据眼位大小判断强弱。
}
