package eddie.wu.linkedblock;

public class ErJiKuai implements Cloneable{
  HaoNode1 zikuaichuang;//由原始棋块号组成的链。
  byte zikuaishu;
  //这些原始棋块互相连接，构成整体但是与其它的块不相连
  public void addkuaihao(short kin) {
    if (zikuaichuang == null) {
      zikuaichuang = new HaoNode1(kin);
      zikuaishu = 1;
    }
    else {
      HaoNode1 linshi=zikuaichuang;
      byte i=1;
      for(;i<=zikuaishu;i++){
         if(linshi.hao==kin) break;
         linshi=linshi.next ;
      }
      if(i<=zikuaishu) return;//已经有这个块号。
      zikuaishu++;
      HaoNode1 temp = new HaoNode1(kin);
      temp.next = zikuaichuang;//块号加在前头。
      zikuaichuang = temp;
    }

  }
  public Object clone(){
      ErJiKuai o=null;

      try{
         o=(ErJiKuai)super.clone();
         if (zikuaichuang != null) {
            o.zikuaichuang = (HaoNode1) (zikuaichuang.clone());
         }

      }
      catch (CloneNotSupportedException de){

      }
      return o;

   }


}