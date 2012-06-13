package untitled9;

import untitled4.*;

public class ZiKuai {
  public byte color; //块的颜色
  public byte zishu; //块的子数；
  public byte qishu; //块的气数；
  public DianNode zichuang; //块中各子的链表:首点表示。
  public DianNode qichuang; //块的各气的链表；
  public byte yanxing; //公气，假眼，真眼。
  public ZiKuai() {
    zilian = new DianLian();
    qilian = new DianLian();
    zwkhao = new HaoLian();
    qkhao = new HaoLian();
    color = 0;
    minqi = 0;
    zishu = 0;
    qishu = 0;
    zichuang = null;
    qichuang = null;
  }

  public DianLian zilian;
  public DianLian qilian;
  public byte minqi; //周围块气数的最小值。
  public HaoLian zwkhao; //周围异色块的号。
  //是否要按气数排序？
  public HaoLian qkhao; //周围气块的号，就是眼，可能是假眼，也可能是打劫。
  //先用气数判断强弱，再赋予地，根据眼位大小判断强弱。
  /*void addkuaihao( byte kin1){
             linhao =new HaoNode();
             linhao.hao=kin1;
             linhao.next =zwkhao;
             zwkh=linhao;
      }*/

}
