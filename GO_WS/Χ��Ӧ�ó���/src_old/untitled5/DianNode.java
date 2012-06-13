package untitled5;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */


public class DianNode{//储存点的坐标的链式数据结构
   public byte a;
   public byte b;
   public DianNode next;
   public DianNode(byte ta, byte tb,DianNode tnext ){
      a=ta;
      b=tb;
      next=tnext;
   }
   public DianNode(){
      a=0;
      b=0;
      next=null;
   }
   public DianNode(byte ta, byte tb){
      a=ta;
      b=tb;
      next=null;

   }
}


