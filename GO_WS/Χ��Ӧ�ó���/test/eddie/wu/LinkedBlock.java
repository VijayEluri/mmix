package eddie.wu;
import java.util.LinkedList;
import java.util.Iterator;

import org.apache.log4j.Logger;

import util.GBKToUTF8;

class LinkedBlock {
	private static final Logger log = Logger.getLogger(GBKToUTF8.class);
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
      linkedList.add(2,"345:");//依次后移345:
                               //c00
                               //345:

      linkedList.add("rrr");
      linkedList.addLast("sdfasdfa");
      Iterator eee=linkedList.iterator();
      while(eee.hasNext()){
         if(log.isDebugEnabled()) log.debug(eee.next());
      }
       if(log.isDebugEnabled()) log.debug("rtr");
     for(eee=linkedList.iterator();eee.hasNext();){
        if(log.isDebugEnabled()) log.debug(eee.next());
     }
     if(log.isDebugEnabled()) log.debug(linkedList.get(0));
     if(log.isDebugEnabled()) log.debug(linkedList.get(1));
     if(log.isDebugEnabled()) log.debug(linkedList.get(2));
     if(log.isDebugEnabled()) log.debug(linkedList.getLast());
     if(log.isDebugEnabled()) log.debug(linkedList.getFirst());
     if(log.isDebugEnabled()) log.debug(linkedList.size());

   }

}