package untitled9;

public class HaoLian { //块号链表
  public byte haoshu;
  public HaoNode root;
  public void addkuaihao(byte kin) {

    if (root == null) {
      haoshu = 1;
      root = new HaoNode(kin);
    }
    else {
      HaoNode temp = new HaoNode(kin);
      temp.next = root;
      root = temp;

    }
  }
  public void addhao(byte kin) {

    if (root == null) {
      haoshu = 1;
      root = new HaoNode(kin);
    }
    else {
      HaoNode temp = new HaoNode(kin);
      temp.next = root;
      root = temp;

    }
  }
  public void deletehao(byte kin){
    HaoNode temp = root;
    HaoNode forward =root;

     for (byte i = 1; i <= haoshu; i++) {
       if (kin == temp.hao) {
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
