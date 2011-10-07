package eddie.wu.linkedblock;

/**
 * similar to QiKuai1, only clone() is removed.
 * @author wueddie-wym-wrz
 *
 */
public class QiKuai2 implements Cloneable{


   public DianNode1 zichuang; //�����и��ӵ�����
   public short zishu; //�������������ʼ����������Ϊ361��

   public HaoNode1 zwzkhao; //��Χ�γ���������ĺš�
   byte zwzkshu;

   public byte color; //Ҳ����࣬��Ϊ��������ĳ����,���Ƿ��㡣
   //5��ʾ������������Χ�Ŀ��кںͰס������Ŀ�򵥴�����1��2��
   public static final byte GONGQIKUAI = 5;

   public byte dingdianshu; //���۶��ԡ�
   public byte yanxing; //���������ۣ����ۡ�
   //ֱ�Ӷ������ʱ��ֻ����ͳһ�������飬�پ���yanxing��

   public byte minqi; //�γ��۵���Χ����������Сֵ��
   //���С�ڵ���2���Ϳ��ܱ���Ի������ӣ���λ��û���ˡ�
   //��Ȼ�д�ٵĵֿ���
   //�������1�����з�����߽�����
   public static final byte ZHENYAN=1;

   public Object clone(){
      QiKuai2 o=null;

      try{
         o=(QiKuai2)super.clone();
         if (zichuang != null) {
            o.zichuang = (DianNode1) (zichuang.clone());
         }
         if (zwzkhao != null) {
            o.zwzkhao = (HaoNode1) (zichuang.clone());
         }
      }
      catch (CloneNotSupportedException de){

      }
      return o;

   }
   //HaoNode1 qkhao; //����ĺţ������ۣ������Ǽ��ۣ�Ҳ�����Ǵ�١�
   //���������ж�ǿ�����ٸ���أ�������λ��С�ж�ǿ����
   public QiKuai2() {

   }

   public void addzikuaihao(short kin) {
      if (zwzkhao == null) {
         zwzkhao = new HaoNode1(kin);
         zwzkshu = 1;
      }
      else {
         HaoNode1 linshi = zwzkhao;
         byte i = 1;
         for (; i <= zwzkshu; i++) {
            if (linshi.hao == kin) {
               break;
            }
            linshi = linshi.next;
         }
         if (i <= zwzkshu) {
            return; //�Ѿ��������š�
         }
         zwzkshu++;
         HaoNode1 temp = new HaoNode1(kin);
         temp.next = zwzkhao; //��ż���ǰͷ��
         zwzkhao = temp;
      }

   }

   public QiKuai2(QiKuai1 old) {
      super();
      zishu = old.zishu;
      color = old.color;
      yanxing = old.yanxing;
      minqi = old.minqi;
      dingdianshu = old.dingdianshu;
      zwzkshu = old.zwzkshu;

      DianNode1 temp;
      DianNode1 back;
     // System.out.println("qikuaizishuwei:"+zishu);

      //����������Ӵ���
      if(old!=null){
         temp = old.zichuang;
      }else{
         return;
      }
      if(temp!=null){
         zichuang = new DianNode1(temp.a, temp.b);
      }else{
         return;
      }
      back = zichuang;
      if(back==null)  System.out.println("back==null");

      for (short i = 2; i <= zishu; i++) {
         //��������Ϊshort�����������ѭ������
         temp = temp.next;
         if(temp==null) {
            System.out.println("temp==null" + i);
            break;
         }
         if(back==null)  System.out.println("back==null"+i);
         back.next = new DianNode1(temp.a, temp.b);
         back = back.next;
      }

      HaoNode1 qkhtemp;
      HaoNode1 qkhback;
      qkhtemp = old.zwzkhao;
      if(qkhtemp!=null){//������Χ�ӿ顣
         zwzkhao = new HaoNode1(qkhtemp.hao);
      }else{
         return;
      }
      qkhback = zwzkhao;
      for (byte i = 2; i <= zwzkshu; i++) {
         qkhtemp = qkhtemp.next;
         qkhback.next = new HaoNode1(qkhtemp.hao);
         qkhback = qkhback.next;
      }

   }

   public void addzidian(byte m1, byte n1) {
      if (zichuang == null) {
         zichuang = new DianNode1(m1, n1);
         zishu = 1;
      }
      else {
         zishu++;
         DianNode1 temp = new DianNode1(m1, n1);
         temp.next = zichuang;
         zichuang = temp;
      }
   }

   public void init() { //�������̵�����������顣
      zishu = 0;
      DianNode1 temp;
      byte i, j;
      for (i = 1; i < 20; i++) {
         for (j = 1; j < 20; j++) {
            zishu++;
            temp = new DianNode1(i, j);
            temp.next = zichuang;
            zichuang = temp;

         }

      }
      System.out.println("zishju=" + zishu);
   }

   public void deletezidian(byte m1, byte n1) {
      DianNode1 temp = zichuang;
      DianNode1 forward = zichuang;
      System.out.println("����deletezidian:ԭʼ����Ϊ" + zishu);
      for (short i = 1; i <= zishu; i++) {
         if (m1 == temp.a & n1 == temp.b) {
            if (i == 1) {
               zichuang = temp.next;

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
      zishu--;
      System.out.println("�˳�deletezidian:��������Ϊ" + zishu);
   }

}
