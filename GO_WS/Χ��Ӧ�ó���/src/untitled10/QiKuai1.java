package untitled10;

public class QiKuai1 {

  public DianNode1 zichuang; //�����и��ӵ�����
  public short zishu; //�����������
  public byte color; //Ҳ����࣬��Ϊ��������ĳ����,���Ƿ��㡣
  //0��ʾ������������Χ�Ŀ��кںͰס������Ŀ�򵥴���

  public byte dingdianshu; //���۶��ԡ�
  public byte yanxing; //���������ۣ����ۡ�
  //ֱ�Ӷ������ʱ��ֻ����ͳһ�������飬�پ���yanxing��
  public byte minqi; //�γ��۵���Χ����������Сֵ��
  //���С�ڵ���2���Ϳ��ܱ���ԣ���λ��û���ˡ�
  //��Ȼ�д�ٵĵֿ���
  //�������1�����з�����߽�����
  public HaoNode1 zwkhao; //��Χ�γ���������ĺš�
  //HaoNode1 qkhao; //����ĺţ������ۣ������Ǽ��ۣ�Ҳ�����Ǵ�١�
  //���������ж�ǿ�����ٸ���أ�������λ��С�ж�ǿ����
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
    zishu=0;
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
    System.out.println("zishju="+zishu);
  }

  public void deletezidian(byte m1, byte n1) {
    DianNode1 temp = zichuang;
    DianNode1 forward = zichuang;
    System.out.println("deletezidian:zishju="+zishu);
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
    System.out.println("deletezidian:zishju="+zishu);
  }

}