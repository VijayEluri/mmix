package untitled10;

public class DianNode1 {
  public byte a;
  public byte b;
  public DianNode1 next;
  public DianNode1(byte ta, byte tb, DianNode1 tnext) {
    a = ta;
    b = tb;
    next = tnext;
  }

  public DianNode1() {
    a = 0;
    b = 0;
    next = null;
  }

  public DianNode1(byte ta, byte tb) {
    a = ta;
    b = tb;
    next = null;

  }

}