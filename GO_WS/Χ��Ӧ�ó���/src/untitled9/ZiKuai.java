package untitled9;

import untitled4.*;

public class ZiKuai {
  public byte color; //�����ɫ
  public byte zishu; //���������
  public byte qishu; //���������
  public DianNode zichuang; //���и��ӵ�����:�׵��ʾ��
  public DianNode qichuang; //��ĸ���������
  public byte yanxing; //���������ۣ����ۡ�
  public ZiKuai() {
    zilian = new DianLian();
    qilian = new DianLian();
    zwkhao = new HaoLian();
    qkhao = new HaoLian();
    color = 0;
    minqi = 0;
    zishu = 0;
    qishu = 0;
    zichuang = null;
    qichuang = null;
  }

  public DianLian zilian;
  public DianLian qilian;
  public byte minqi; //��Χ����������Сֵ��
  public HaoLian zwkhao; //��Χ��ɫ��ĺš�
  //�Ƿ�Ҫ����������
  public HaoLian qkhao; //��Χ����ĺţ������ۣ������Ǽ��ۣ�Ҳ�����Ǵ�١�
  //���������ж�ǿ�����ٸ���أ�������λ��С�ж�ǿ����
  /*void addkuaihao( byte kin1){
             linhao =new HaoNode();
             linhao.hao=kin1;
             linhao.next =zwkhao;
             zwkh=linhao;
      }*/

}
