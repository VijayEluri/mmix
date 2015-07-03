package eddie.wu.linkedblock;

public class DianNode1 implements Cloneable{//落子点的节点，有坐标和下一节点的指针。
  public byte a;
  public byte b;
  public DianNode1 next;

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

  public DianNode1(byte ta, byte tb, DianNode1 tnext) {
     a = ta;
     b = tb;
     next = tnext;
   }
   public Object clone(){
       DianNode1 temp;
       try{
          temp=(DianNode1)(super.clone());
       }catch (CloneNotSupportedException cn){
          return null;
       }

        if(next!=null){
          temp.next=(DianNode1)(next.clone());

        }
        return temp;

     }

}