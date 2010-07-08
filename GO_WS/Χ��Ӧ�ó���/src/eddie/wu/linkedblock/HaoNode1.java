package eddie.wu.linkedblock;

public class HaoNode1 implements Cloneable{
   //¿éºÅ½áµã
   public short hao; //0-32767;
   public HaoNode1 next;
   public HaoNode1(short a) {
      hao = a;
      next = null;
   }

   public HaoNode1() {
      hao = 0;
      next = null;
   }

   public HaoNode1(short a, HaoNode1 th) {
      hao = a;
      next = th;
   }

   public Object clone() {
      HaoNode1 temp;
      try {
         temp = (HaoNode1) (super.clone());
      }
      catch (CloneNotSupportedException cn) {
         return null;
      }

      if (next != null) {
         temp.next = (HaoNode1) (next.clone());

      }
      return temp;

   }

}