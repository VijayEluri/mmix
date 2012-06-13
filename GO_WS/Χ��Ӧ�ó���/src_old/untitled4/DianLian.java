package untitled4;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DianLian {
   public DianLian() {
      dianshu=0;
      root=null;
   }
  public byte dianshu;
  public DianNode root;
   public void adddian(byte m1,byte n1){
      if(root==null){
         root=new DianNode(m1,n1);
      }else{
         DianNode temp=new DianNode(m1,n1);
            temp.next=root;
            root=temp;
      }
   }
   public void deletedian(byte m1,byte n1){
      DianNode temp = root;
   DianNode forward =root;

   for (byte i = 1; i <= dianshu; i++) {
     if (m1 == temp.a & n1 == temp.b) {
       if (i == 1) {
        root = temp.next;

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

   }

}