/*
 * Created on 2005-4-21
 *


 */
package eddie.wu.domain;

/**
 * @author eddie
 * 
 *         TODO To change the template for this generated type comment go to

 */
public class Constant {

	// basic constant
	public static final byte SIZEOFBOARD = 19;

	// derived constant
	public static final byte SIZEOFMATRIX = SIZEOFBOARD + 2;
	public static final byte COORDINATEOFTIANYUAN = (SIZEOFBOARD + 1) / 2;
	public static final short QIPANZONGDIANSHU = SIZEOFBOARD * SIZEOFBOARD;
	public static final byte ZBSX = SIZEOFBOARD + 1; // �����������ޣ�����߽磩;
	public static final byte ZBXX = 0; // �����������ޣ�����߽磩;

	// �������ܣ���������ᣩ���ӵ�,˳��ɵ�.��-��-��-��
	public static final byte[][] szld = { { 1, 0 }, { 0, -1 }, { -1, 0 },
			{ 0, 1 } };

	// �������ܶԽǵ�,˳��ɵ�.��-��-��-��
	public static final byte[][] szdjd = { { 1, 1 }, { 1, -1 }, { -1, -1 },
			{ -1, 1 } };
	
	
//	public static final byte ZTXB = 0; // �±�0�洢״ֵ̬�����ӡ����ӡ����ӣ�;
	public static final byte OUT_OF_BOUND = -1;
	public static final byte BLANK = 0;
	public static final byte BLACK = 1; // 1��ʾ����;
	public static final byte WHITE = 2; // 2��ʾ����;
//
//	public static final byte SQBZXB = 1; // �±�1�洢������־;
//	public static final byte QSXB = 2; // �±�2�洢���������ӻ�������������;
//	public static final byte QKSYXB = 3; // �±�3�洢�����������հ׵���ԣ�

	// ��λ��صĲ���
	// 1.�۵�λ�á�
//	public static final byte BIANYAN = 2;
//	public static final byte JIAOYAN = 1;
//	public static final byte ZHONGFUYAN = 3;
//	// 2.�۵����ʡ�
//	public static final byte JIAYAN = 1;
//	public static final byte WEIDINGYAN = 2;
//	public static final byte ZHENYAN = 3;
//	// 3.�����ԡ�
//	public static final byte WEILIANJIE = 1;
//	public static final byte KELIANJIE = 2;
//	public static final byte YILIANJIE = 3;
//	public static final byte WEIDINGLIANJIE = 4;

	public static boolean DEBUG_CGCL = true; // cgcl()��������
	// public static final boolean DEBUG_JISUANZHENGZI =false;
	public static final boolean DEBUG_JISUANSIHUO = false;
	public static final boolean DEBUG_KDQ = false;
	public static final boolean DEBUG_JSKQ = false;
	public static final boolean DEBUG_JISUANZHENGZI = true;

	// for debug
	// public int shoushu=164;

}