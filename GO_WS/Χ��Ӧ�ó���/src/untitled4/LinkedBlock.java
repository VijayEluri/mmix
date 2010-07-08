package untitled4;
import java.util.LinkedList;
import java.util.Iterator;

class LinkedBlock {
   LinkedList linkedList=new LinkedList();
   public static void main(String[] args){
     new LinkedBlock().test();
   }
   public void test(){
      linkedList.addFirst("a");
      linkedList.add("b");
      linkedList.add(2,"c00");
      //linkedList.add(4,"345:");//IndexOutOfBoundsException
      linkedList.add(3,"345:");
      linkedList.add(2,"15664");
      linkedList.add(2,"345:");//“¿¥Œ∫Û“∆345:
                               //c00
                               //345:

      linkedList.add("rrr");
      linkedList.addLast("sdfasdfa");
      Iterator eee=linkedList.iterator();
      while(eee.hasNext()){
         System.out.println(eee.next());
      }
       System.out.println("rtr");
     for(eee=linkedList.iterator();eee.hasNext();){
        System.out.println(eee.next());
     }
     System.out.println(linkedList.get(0));
     System.out.println(linkedList.get(1));
     System.out.println(linkedList.get(2));
     System.out.println(linkedList.getLast());
     System.out.println(linkedList.getFirst());
     System.out.println(linkedList.size());

   }

}