package untitled9;
import java.lang.Exception;
public class HaoNode extends Object implements Cloneable{ //��Ž��
  public byte hao; //-127��127;
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
