package untitled9;
import untitled4.*;
public class QiKuai {
  public DianNode zichuang; //�����и��ӵ�����
  public byte zishu; //�����������
  public DianLian zilian;
  public QiKuai(){
     zilian=new DianLian();
     zishu=0;
     zichuang=null;
     color=0;
     dingdianshu=0;
     yanxing=0;
     minqi=0;
         zwkhao=new HaoLian();
  }
  public byte color;//Ҳ����࣬��Ϊ��������ĳ���顣
  public byte dingdianshu; //���۶��ԡ�
  public byte yanxing; //���������ۣ����ۡ�
  public byte minqi; //�γ��۵���Χ����������Сֵ��
  //���С�ڵ���2���Ϳ��ܱ���ԣ���λ��û���ˡ�
  //��Ȼ�д�ٵĵֿ���
  //�������1�����з�����߽�����
  public HaoLian zwkhao; //��Χ���ĺš�
  //HaoNode qkhao; //����ĺţ������ۣ������Ǽ��ۣ�Ҳ�����Ǵ�١�
  //���������ж�ǿ�����ٸ���أ�������λ��С�ж�ǿ����
}
