package untitled3;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DsNode { //��ʽ�ڵ㡣
   public byte xianhoushou; //-1Ϊ���֣�1Ϊ���֣��Ƚ�ʱ���ִ��ں��֣�
   //��ʱ�Ⱥ��ֱȽ���˵�������0��ʾ��
   public int ju;//�ýڵ��̱仯���ڵ��ļ����֣�
   //����Ƕ�ʽ���У�ǰ��Ϊ��dingshibianzhao��
   //Ҳ���ܸò���ƭ�ţ��Ӵ˿��Ե������仯���ļ���ǰ��Ϊ��pianzhao����
   //���߸ò���ʽ��һ���䣬ָ��ʽ֮��ı仯����dingshizhihou��
   public byte zba;//�ò��ĺ�����
   public byte zbb;//�ò���������
   public byte xingshi1;//���ӳ���ʱ�����ƣ�
   public byte xingshi2;//���Ӳ�����ʱ�����ƣ�
   public byte blbz; //������־���߸ò��ļ�����
   //����ڸò���ɫ���ԣ�100��������Դ����������йأ�
   //�����йز����������ĸ��ֻ�������ܷ������
   public DsNode left;
   public DsNode right;
   public boolean ZZYG=false;//�����йء�
   //����ÿһ�д���ļ���
   public DsNode(byte a, byte b, byte pj1, byte pj2, byte xh) {
      left = null;
      right = null;
      xingshi1 = pj1;
      xingshi2 = pj2;
      xianhoushou = xh;
      zba = a;
      zbb = b;
   }
   //��ʽ������Ҳ���ñ�׼��ʽ����ʵ������ƥ��ʱȷ��ת����ʽ��
   public DsNode(byte a, byte b, byte pj1, byte pj2) {
      left = null;
      right = null;
      xingshi1 = pj1;
      xingshi2 = pj2;
      if(a<1||a>19){
         System.out.println("a<1||a>19");
      }

      if(b<1||b>19){
         System.out.println("b<1||b>19");
      }
      zba = a;
      zbb = b;
   }

   public DsNode(byte a, byte b) {
      left = null;
      right = null;
      xingshi1 = 0;//Ĭ��Ϊ��ʽ������
      xingshi2 = 0;
      if(a<1||a>19){
         System.out.println("a<1||a>19");
      }
      if(b<1||b>19){
         System.out.println("b<1||b>19");
      }

      zba = a;
      zbb = b;
   }

}
