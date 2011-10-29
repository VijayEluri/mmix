/*
 * Created on 2005-4-21
 *


 */
package eddie.wu.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eddie
 * 
 *         TODO To change the template for this generated type comment go to
 */
public class Constant {
	public static final String rootDir = "doc/Χ��������/";
	public static final String GLOBAL_MANUAL = Constant.rootDir + "weiqigo/go/go.gmd";
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
	// ���
	public static final Delta DELTA_SHOULDER = Delta.getDelta(1, 1);
	public static final List<Delta> SHOULDERS = Delta
			.getAppoachWays(DELTA_SHOULDER);
	// һ��߹�
	public static final Delta DELTA_ONE_DISTANCE = new Delta(2, 0);
	public static List<Delta> ONE_DISTANCE = Delta
			.getAppoachWays(DELTA_ONE_DISTANCE);
	// С��
	public static final Delta DELTA_SMALL_KNIGHT = Delta.getDelta(2, 1);
	public static List<Delta> SMALL_KNIGHT = Delta
			.getAppoachWays(DELTA_SMALL_KNIGHT);
	// ���
	public static final Delta DELTA_ELEPHANT_KNIGHT = Delta.getDelta(2, 2);
	public static List<Delta> ELEPHANT_KNIGHT = Delta
			.getAppoachWays(DELTA_ELEPHANT_KNIGHT);
	// ����߹�
	public static final Delta DELTA_TWO_DISTANCE = new Delta(3, 0);
	public static List<Delta> TWO_DISTANCE = Delta
			.getAppoachWays(DELTA_TWO_DISTANCE);
	// ���
	public static final Delta DELTA_BIG_KNIGHT = Delta.getDelta(3, 1);
	public static List<Delta> BIG_KNIGHT = Delta
			.getAppoachWays(DELTA_BIG_KNIGHT);

	public static final List<List<Delta>> NEIGHBOR = new ArrayList<List<Delta>>();

	static {
		NEIGHBOR.add(SHOULDERS);
		NEIGHBOR.add(ONE_DISTANCE);
		NEIGHBOR.add(SMALL_KNIGHT);
		NEIGHBOR.add(ELEPHANT_KNIGHT);
		NEIGHBOR.add(TWO_DISTANCE);
		NEIGHBOR.add(BIG_KNIGHT);

	};

	public static final byte OUT_OF_BOUND = -1;
	public static final byte BLANK = 0;
	public static final byte BLACK = 1; // 1��ʾ����;
	public static final byte WHITE = 2; // 2��ʾ����;

	public static boolean DEBUG_CGCL = true; // cgcl()��������
	public static final boolean DEBUG_JISUANSIHUO = false;
	public static final boolean DEBUG_KDQ = false;
	public static final boolean DEBUG_JSKQ = false;
	public static final boolean DEBUG_JISUANZHENGZI = true;
	public static final int MAX_STEPS_IN_ONE_MANUAL_SHOW = 100;
	public static final String DING_SHI_SHU = "C:/scm/git/git-hub/mmix/GO_WS/Χ��Ӧ�ó���/doc/Χ���������/" + "��ʽ��";

}
//
// public static final byte ZTXB = 0; // �±�0�洢״ֵ̬�����ӡ����ӡ����ӣ�;
// public static final byte SQBZXB = 1; // �±�1�洢������־;
// public static final byte QSXB = 2; // �±�2�洢���������ӻ�������������;
// public static final byte QKSYXB = 3; // �±�3�洢�����������հ׵���ԣ�

// ��λ��صĲ���
// 1.�۵�λ�á�
// public static final byte BIANYAN = 2;
// public static final byte JIAOYAN = 1;
// public static final byte ZHONGFUYAN = 3;
// // 2.�۵����ʡ�
// public static final byte JIAYAN = 1;
// public static final byte WEIDINGYAN = 2;
// public static final byte ZHENYAN = 3;
// // 3.�����ԡ�
// public static final byte WEILIANJIE = 1;
// public static final byte KELIANJIE = 2;
// public static final byte YILIANJIE = 3;
// public static final byte WEIDINGLIANJIE = 4;