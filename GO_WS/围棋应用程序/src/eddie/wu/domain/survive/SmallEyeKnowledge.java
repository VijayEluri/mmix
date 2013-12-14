package eddie.wu.domain.survive;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Delta;
import eddie.wu.domain.GoBoardSymmetry;

/**
 * 
 * @author Eddie
 * 
 */
public class SmallEyeKnowledge {
	private static Map<BreathPattern, RelativeSurviveResult> map = new HashMap<BreathPattern, RelativeSurviveResult>();

	public static RelativeSurviveResult getResult(BreathPattern bp) {
		return map.get(bp);
	}

	/**
	 * 棋盘的状态有四角八边变化。需要将其标准化。这样其他的变体可以统一对待。<br/>
	 * 黑白交换后本质上是等价的，结果和whoseTurn都改变。
	 * 
	 * @return
	 */
//	public static void update(BreathPattern bp, RelativeSurviveResult rs) {
//		map.put(bp, rs);
//
//		BoardColorState state = new BoardColorState(this.boardSize,
//				this.whoseTurn);
//
//		List<BoardColorState> list = new ArrayList<BoardColorState>();
//
//		List<BoardColorState> list2 = new ArrayList<BoardColorState>();
//		// BoardColorState backSlash = new BoardColorState(this.)
//		byte[][] originalState = getMatrixState();
//		byte[][] verticalMirror = GoBoardSymmetry.verticalMirror(originalState);
//		byte[][] horizontalMirror = GoBoardSymmetry
//				.horizontalMirror(originalState);
//		byte[][] horizontalVertical = GoBoardSymmetry
//				.horizontalMirror(verticalMirror);
//		list.add(this);
//		list.add(BoardColorState.getInstance(verticalMirror, whoseTurn));
//		list.add(BoardColorState.getInstance(horizontalMirror, whoseTurn));
//		list.add(BoardColorState.getInstance(horizontalVertical, whoseTurn));
//
//		list.add(BoardColorState.getInstance(
//				GoBoardSymmetry.forwardSlashMirror(originalState), whoseTurn));
//		list.add(BoardColorState.getInstance(
//				GoBoardSymmetry.forwardSlashMirror(verticalMirror), whoseTurn));
//		list.add(BoardColorState.getInstance(
//				GoBoardSymmetry.forwardSlashMirror(horizontalMirror), whoseTurn));
//		list.add(BoardColorState.getInstance(
//				GoBoardSymmetry.forwardSlashMirror(horizontalVertical),
//				whoseTurn));
//		if (log.isInfoEnabled()) {
//			for (BoardColorState state2 : list) {
//				log.info(state2.getStateString().toString());
//			}
//		}
//
//		boolean colorSwitchIncluded = list.contains(this.blackWhiteSwitch());
//
//		Collections.sort(list, new BoardColorStateComparator());
//		if (colorSwitchIncluded) {
//			log.info("colorSwitchIncluded:" + colorSwitchIncluded);
//			return list.get(0);
//		}
//		// do not switch between black and white.
//		// BoardColorState standard = list.get(0);
//		// BoardColorState switched = standard.blackWhiteSwitch();
//		// list.clear();
//		// list.add(switched);
//		// list.add(standard);
//		// Collections.sort(list, new BoardColorStateComparator());
//
//		return list.get(0);
//	}

	/**
	 * initialized with the knowledge from human being.
	 */
	static {
		initEye2();
		initEye3();
	}

	private static void initEye2() {
		byte[][] pattern;
		BreathPattern bp;
		RelativeSurviveResult result;
		pattern = new byte[1][2];
		// for (int i = 0; i < pattern[0].length; i++) {
		// pattern[0][i] = ColorUtil.BREATH;
		// }
		bp = new BreathPattern(pattern);
		if (map.containsKey(bp) == false) {
			result = new RelativeSurviveResult();
			result.setXianShou(new RelativeResult(RelativeSurviveResult.DIE,
					null));
			result.setHouShou(new RelativeResult(RelativeSurviveResult.DIE,
					null));
			result.setIndependent(true);
			map.put(bp, result);
		}
	}

	private static void initEye3() {
		byte[][] pattern;
		BreathPattern bp;
		RelativeSurviveResult result;
		Delta point = null;
		/*
		 * pattern ###
		 */
		pattern = new byte[1][3];
		// for (int i = 0; i < pattern[0].length; i++) {
		// pattern[0][i] = ColorUtil.BREATH;
		// }
		bp = new BreathPattern(pattern);
		if (map.containsKey(bp) == false) {
			// this is actually delta, index from 0.
			point = Delta.getDelta(0, 1);
			result = new RelativeSurviveResult();
			result.setXianShou(new RelativeResult(RelativeSurviveResult.LIVE,
					point));
			result.setHouShou(new RelativeResult(RelativeSurviveResult.DIE,
					point));
			result.setIndependent(false);
			map.put(bp, result);
		}

		/*
		 * pattern # # #
		 */
		pattern = new byte[3][1];
		// for (int i = 0; i < pattern.length; i++) {
		// pattern[i][0] = ColorUtil.BREATH;
		// }
		bp = new BreathPattern(pattern);
		if (map.containsKey(bp) == false) {
			/*
			 * this is actually delta, index from 0
			 */
			point = Delta.getDelta(1, 0);
			result = new RelativeSurviveResult();
			result.setXianShou(new RelativeResult(RelativeSurviveResult.LIVE,
					point));
			result.setHouShou(new RelativeResult(RelativeSurviveResult.DIE,
					point));
			result.setIndependent(false);
			map.put(bp, result);
		}

		/*
		 * pattern ## ##
		 */
		pattern = new byte[2][2];
		// for (int i = 0; i < pattern.length; i++) {
		// for (int j = 0; j < pattern[0].length; j++) {
		// pattern[i][j] = ColorUtil.BREATH;
		// }
		// }
		/**
		 * 气块点取值为0。不相干的点，无论黑白，都设为OutOfBound。
		 */
		pattern[1][1] = ColorUtil.OutOfBoard;
		bp = new BreathPattern(pattern);
		if (map.containsKey(bp) == false) {
			point = Delta.getDelta(0, 0); // this is actually delta, index
											// from 0.
			result = new RelativeSurviveResult();
			result.setXianShou(new RelativeResult(RelativeSurviveResult.LIVE,
					point));
			result.setHouShou(new RelativeResult(RelativeSurviveResult.DIE,
					point));
			result.setIndependent(false);
			map.put(bp, result);
			iterateIsomorphism(pattern, point, result);
		}

	}

	private static void iterateIsomorphism(byte[][] pattern, Delta point,
			RelativeSurviveResult result) {
		int m = pattern.length;
		int n = pattern[0].length;
		/*
		 * left right switch.
		 */
		byte[][] pattern2 = new byte[m][n];
		for (int i = 0; i < pattern.length; i++) {
			for (int j = 0; j < pattern[0].length; j++) {
				pattern2[i][j] = pattern[i][n - 1 - j];
			}
		}
		RelativeSurviveResult result2 = new RelativeSurviveResult();
		Delta point2 = Delta.getDelta(point.getRowDelta(),
				n - 1 - point.getColumnDelta());
		result2.getXianShou().firstStep = point2;
		result2.getHouShou().firstStep = point2;

		BreathPattern bp2 = new BreathPattern(pattern);
		if (map.containsKey(bp2) == false) {
			map.put(bp2, result);
		}

		/*
		 * top bottom switch
		 */
		byte[][] pattern3 = new byte[m][n];
		for (int i = 0; i < pattern.length; i++) {
			for (int j = 0; j < pattern[0].length; j++) {
				pattern3[i][j] = pattern[m - 1 - i][j];
			}
		}
		RelativeSurviveResult result3 = new RelativeSurviveResult();
		Delta point3 = Delta.getDelta(m - 1 - point.getRowDelta(),
				point.getColumnDelta());
		result3.getXianShou().firstStep = point3;
		result3.getHouShou().firstStep = point3;

		BreathPattern bp3 = new BreathPattern(pattern);
		if (map.containsKey(bp3) == false) {
			map.put(bp3, result);
		}

		/**
		 * top bottom switch and left right switch.
		 */
		byte[][] pattern4 = new byte[m][n];
		for (int i = 0; i < pattern.length; i++) {
			for (int j = 0; j < pattern[0].length; j++) {
				pattern4[i][j] = pattern[m - 1 - i][n - 1 - j];
			}
		}
		RelativeSurviveResult result4 = new RelativeSurviveResult();
		Delta point4 = Delta.getDelta(m - 1 - point.getRowDelta(), n - 1
				- point.getColumnDelta());
		result3.getXianShou().firstStep = point3;
		result3.getHouShou().firstStep = point3;

		BreathPattern bp4 = new BreathPattern(pattern);
		if (map.containsKey(bp4) == false) {
			map.put(bp4, result);
		}
	}

}
