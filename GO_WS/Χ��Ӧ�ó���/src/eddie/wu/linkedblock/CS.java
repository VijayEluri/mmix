package eddie.wu.linkedblock;

import eddie.wu.domain.Constant;

//����ΪConStant��
public class CS {

   public static final byte BoardSize = 19;

   public static final byte ZBSX = BoardSize + 1; //�����������ޣ�����߽磩;
   public static final byte ZBXX = 0; //�����������ޣ�����߽磩;

   public static final byte[][] szld = {
      //�������ܣ���������ᣩ���ӵ�,˳��ɵ�.��-��-��-��
      {
      1, 0}
      , {
      0, -1}
      , {
      -1, 0}
      , {
      0, 1}
   };
   public static final byte[][] szdjd = {
      //�������ܶԽǵ�,˳��ɵ�.��-��-��-��
      {
      1, 1}
      , {
      1, -1}
      , {
      -1, -1}
      , {
      -1, 1}
   };
   public static final byte ZTXB = 0; //�±�0�洢״ֵ̬�����ӡ����ӡ����ӣ�;
   public static final byte BLANK = Constant.BLANK;
   public static final byte BLACK = Constant.BLACK; //1��ʾ����;
   public static final byte WHITE = Constant.WHITE; //2��ʾ����;

   public static final byte SQBZXB = 1; //�±�1�洢������־;
   public static final byte QSXB = 2; //�±�2�洢���������ӻ�������������;
   public static final byte QKSYXB = 3; //�±�3�洢�����������հ׵���ԣ�

   // ��λ��صĲ���
   //1.�۵�λ�á�
   public static final byte BIANYAN = 2;
   public static final byte JIAOYAN = 1;
   public static final byte ZHONGFUYAN = 3;
   //2.�۵����ʡ�
   public static final byte JIAYAN = 1;
   public static final byte WEIDINGYAN = 2;
   public static final byte ZHENYAN = 3;
   //3.�����ԡ�
  public static final byte WEILIANJIE = 1;
  public static final byte KELIANJIE = 2;
  public static final byte YILIANJIE = 3;
  public static final byte WEIDINGLIANJIE = 4;

  public static final boolean DEBUG_CGCL =true; //cgcl()��������
  //public static final boolean DEBUG_JISUANZHENGZI =false;
  public static final boolean DEBUG_JISUANSIHUO =false;
  public static final boolean DEBUG_KDQ =false;
  public static final boolean DEBUG_JSKQ =false;
  public static final boolean DEBUG_JISUANZHENGZI =true;
}