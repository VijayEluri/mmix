package eddie.wu.linkedblock;

public class ErJiKuai implements Cloneable{
  HaoNode1 zikuaichuang;//��ԭʼ������ɵ�����
  byte zikuaishu;
  //��Щԭʼ��黥�����ӣ��������嵫���������Ŀ鲻����
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
      if(i<=zikuaishu) return;//�Ѿ��������š�
      zikuaishu++;
      HaoNode1 temp = new HaoNode1(kin);
      temp.next = zikuaichuang;//��ż���ǰͷ��
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