package untitled10;

import org.apache.log4j.Logger;

import eddie.wu.util.GBKToUTF8;

public class QiKuai1 {
	private static final Logger log = Logger.getLogger(QiKuai1.class);
  public DianNode1 zichuang; //气块中各子的链表。
  public short zishu; //气块的子数；
  public byte color; //也许多余，因为他从属于某个块,但是方便。
  //0表示公气，反正周围的块有黑和白。这样的块简单处理

  public byte dingdianshu; //单眼而言。
  public byte yanxing; //公气，假眼，真眼。
  //直接读入局面时，只能先统一生成气块，再决定yanxing；
  public byte minqi; //形成眼的周围块气数的最小值。
  //如果小于等于2，就可能被打吃，眼位就没有了。
  //当然有打劫的抵抗。
  //如果等于1，就有反提或者劫争；
  public HaoNode1 zwkhao; //周围形成气块的棋块的号。
  //HaoNode1 qkhao; //气块的号，就是眼，可能是假眼，也可能是打劫。
  //先用气数判断强弱，再赋予地，根据眼位大小判断强弱。
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
    zishu=0;
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
    if(log.isDebugEnabled()) log.debug("zishju="+zishu);
  }

  public void deletezidian(byte m1, byte n1) {
    DianNode1 temp = zichuang;
    DianNode1 forward = zichuang;
    if(log.isDebugEnabled()) log.debug("deletezidian:zishju="+zishu);
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
    if(log.isDebugEnabled()) log.debug("deletezidian:zishju="+zishu);
  }

}