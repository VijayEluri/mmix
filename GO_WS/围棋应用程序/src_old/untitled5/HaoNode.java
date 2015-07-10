package untitled5;

public class HaoNode extends Object implements Cloneable{ //块号结点
  public byte hao; //-127到127;
  public HaoNode next;
  public HaoNode(byte a) {
    hao = a;
    next = null;
  }

  public HaoNode() {
   hao = 0;
   next = null;
 }

  public Object clone(){
    HaoNode temp;
    try{
       temp=(HaoNode)(super.clone());
    }catch (CloneNotSupportedException cn){
       return null;
    }

     if(next!=null){
       temp.next=(HaoNode)(next.clone());

     }
     return temp;

  }


}
