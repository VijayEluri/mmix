/*
 *20041003 �������ļ��㼯�������Ŀ��,�����ü��Ͽ��ʵ��
 */
package untitled5;

import java.util.*;

public class QiKuai {
    public ArrayList zichuang; //�����и��ӵ�����
    public byte zishu; //�����������
    //redundant .
    public DianLian zilian;//?
    public byte color; //Ҳ����࣬��Ϊ��������ĳ���顣
    public byte dingdianshu; //���۶��ԡ�
    public byte yanxing; //���������ۣ����ۡ�
    public byte minqi; //�γ��۵���Χ����������Сֵ��
    //���С�ڵ���2���Ϳ��ܱ���ԣ���λ��û���ˡ�
    //��Ȼ�д�ٵĵֿ���
    //�������1�����з�����߽�����
    public ArrayList zwkhao; //��Χ���ĺš�
    //HaoNode qkhao; //����ĺţ������ۣ������Ǽ��ۣ�Ҳ�����Ǵ�١�
    //���������ж�ǿ�����ٸ���أ�������λ��С�ж�ǿ����

    public QiKuai() {
       zilian = new DianLian();
       zishu = 0;
       zichuang = new ArrayList();
       color = 0;
       dingdianshu = 0;
       yanxing = 0;
       minqi = 0;
       zwkhao = new ArrayList();
   }

}
