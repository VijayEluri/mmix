/*
 * Created on 2005-4-21
 *


 */
package eddie.wu.domain;

import java.util.ArrayList;
import java.util.List;

import eddie.wu.manual.SGFGoManual;

/**
 * @author eddie
 * 
 * 
 */
public class Constant {
	// public static boolean INTERNAL_CHECK = false;
	public static boolean INTERNAL_CHECK = false;
	public static final String lineSeparator = (String) java.security.AccessController
			.doPrivileged(new sun.security.action.GetPropertyAction(
					"line.separator"));

	public static final String rootDir = "doc/围棋程序数据/";
	public static final String DEBUG_MANUAL = Constant.rootDir + "调试出错的棋谱/";
	public static final int currentManualIndex =  68;//68
	public static final String currentManual = SGFGoManual
			.getFileName(currentManualIndex);

	public static final String GLOBAL_MANUAL = Constant.rootDir
			+ "weiqigo/go/go.gmd";
	// basic constant
	public static final byte BOARD_SIZE = 19;
	public static final byte SMALL_BOARD_SIZE = 11;

	public static final int MAX = 1; // 代表征子方

	public static final int MIN = 2; // 代表被征子方

	// derived constant
	public static final byte BOARD_MATRIX_SIZE = BOARD_SIZE + 2;
	// public static final byte COORDINATEOFTIANYUAN = (BOARD_SIZE + 1) / 2;
	// public static final short QIPANZONGDIANSHU = BOARD_SIZE * BOARD_SIZE;
	// public static final byte ZBSX = BOARD_SIZE + 1; // 棋盘坐标上限（人造边界）;
	// public static final byte ZBXX = 0; // 棋盘坐标下限（人造边界）;

	// 遍历四周（横轴和纵轴）邻子点,顺序可调.右-下-左-上

	// new solution
	public static final Delta DELTA_ADJACENT = Delta.getDelta(1, 0);
	public static final List<Delta> ADJACENTS = Delta
			.getAppoachWays(DELTA_ADJACENT);

	// 遍历四周对角点,顺序可调.右-下-左-上
	// public static final byte[][] szdjd = { { 1, 1 }, { 1, -1 }, { -1, -1 },
	// { -1, 1 } };

	// 肩冲 (也即九宫顶点)
	public static final Delta DELTA_SHOULDER = Delta.getDelta(1, 1);
	public static final List<Delta> SHOULDERS = Delta
			.getAppoachWays(DELTA_SHOULDER);
	// 一间高挂
	public static final Delta DELTA_ONE_DISTANCE = new Delta(2, 0);
	public static List<Delta> ONE_DISTANCE = Delta
			.getAppoachWays(DELTA_ONE_DISTANCE);
	// 小飞
	public static final Delta DELTA_SMALL_KNIGHT = Delta.getDelta(2, 1);
	public static List<Delta> SMALL_KNIGHT = Delta
			.getAppoachWays(DELTA_SMALL_KNIGHT);
	// 象飞
	public static final Delta DELTA_ELEPHANT_KNIGHT = Delta.getDelta(2, 2);
	public static List<Delta> ELEPHANT_KNIGHT = Delta
			.getAppoachWays(DELTA_ELEPHANT_KNIGHT);
	// 二间高挂
	public static final Delta DELTA_TWO_DISTANCE = new Delta(3, 0);
	public static List<Delta> TWO_DISTANCE = Delta
			.getAppoachWays(DELTA_TWO_DISTANCE);
	// 大飞
	public static final Delta DELTA_BIG_KNIGHT = Delta.getDelta(3, 1);
	public static List<Delta> BIG_KNIGHT = Delta
			.getAppoachWays(DELTA_BIG_KNIGHT);

	// 三间接近
	public static final Delta DELTA_THREE_DISTANCE = new Delta(4, 0);
	public static List<Delta> THREE_DISTANCE = Delta
			.getAppoachWays(DELTA_TWO_DISTANCE);

	public static final List<List<Delta>> NEIGHBOR = new ArrayList<List<Delta>>();
	public static final List<List<Delta>> CONNECTIONS = new ArrayList<List<Delta>>();
	public static final List<List<Delta>> NEIGHBOR_FROM_ADJACENT = new ArrayList<List<Delta>>();

	static {
		NEIGHBOR.add(SHOULDERS);
		NEIGHBOR.add(ONE_DISTANCE);
		NEIGHBOR.add(SMALL_KNIGHT);
		NEIGHBOR.add(ELEPHANT_KNIGHT);
		NEIGHBOR.add(TWO_DISTANCE);
		NEIGHBOR.add(BIG_KNIGHT);

		NEIGHBOR_FROM_ADJACENT.add(SHOULDERS);
		NEIGHBOR_FROM_ADJACENT.addAll(NEIGHBOR);

		CONNECTIONS.add(SHOULDERS);
		CONNECTIONS.add(ONE_DISTANCE);
		CONNECTIONS.add(SMALL_KNIGHT);
	};

	public static final byte OUT_OF_BOUND = -1;
	public static final byte BLANK = 0;
	public static final byte BLACK = 1; // 1表示黑子;
	public static final byte WHITE = 2; // 2表示白子;

	//
	public static final byte EATEN = 100; // 显示被提吃的子;

	public static boolean DEBUG_CGCL = true; // cgcl()方法调试
	public static final boolean DEBUG_JISUANSIHUO = false;
	public static final boolean DEBUG_KDQ = false;
	public static final boolean DEBUG_JSKQ = false;
	public static final boolean DEBUG_JISUANZHENGZI = true;
	public static final int MAX_STEPS_IN_ONE_MANUAL_SHOW = 100;
	public static final String DING_SHI_SHU = "C:/scm/git/git-hub/mmix/GO_WS/围棋应用程序/doc/围棋程序数据/"
			+ "定式树";

	public static final int SEARCH_ERROR = 99999;

	public static final int MARK_DEAD = 128;

	public static final int UNKOWN = Integer.MIN_VALUE;
	public static final String GIVE_UP = "[PAS]";

}
//
// public static final byte ZTXB = 0; // 下标0存储状态值（无子、黑子、白子）;
// public static final byte SQBZXB = 1; // 下标1存储算气标志;
// public static final byte QSXB = 2; // 下标2存储气数（单子或者棋块的气数）;
// public static final byte QKSYXB = 3; // 下标3存储气块索引（空白点而言）

// 眼位相关的参数
// 1.眼的位置。
// public static final byte BIANYAN = 2;
// public static final byte JIAOYAN = 1;
// public static final byte ZHONGFUYAN = 3;
// // 2.眼的性质。
// public static final byte JIAYAN = 1;
// public static final byte WEIDINGYAN = 2;
// public static final byte ZHENYAN = 3;
// // 3.连接性。
// public static final byte WEILIANJIE = 1;
// public static final byte KELIANJIE = 2;
// public static final byte YILIANJIE = 3;
// public static final byte WEIDINGLIANJIE = 4;