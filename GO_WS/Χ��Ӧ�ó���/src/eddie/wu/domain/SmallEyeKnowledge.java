package eddie.wu.domain;

import java.util.HashMap;
import java.util.Map;

import eddie.wu.linkedblock.ColorUtil;

public class SmallEyeKnowledge {
	private static Map<BreathPattern, SurviveResult> map = new HashMap<BreathPattern, SurviveResult>();
	/**
	 * initialized with the knowledge from human being.
	 */
	static {
		initEye2();
	}

	private static void initEye2() {
		byte[][] pattern;
		BreathPattern bp;
		SurviveResult result;
		pattern = new byte[1][2];
		for (int i = 0; i < pattern[0].length; i++) {
			pattern[0][i] = ColorUtil.BREATH;
		}
		bp = new BreathPattern(pattern);
		if (map.containsKey(bp) == false) {
			result = new SurviveResult();
			result.setXianShou(new Result(SurviveResult.DIE, null));
			result.setHouShou(new Result(SurviveResult.DIE, null));
			result.setIndependent(true);
			map.put(bp, result);
		}
	}

	private static void initEye3() {
		byte[][] pattern;
		BreathPattern bp;
		SurviveResult result;
		Point point = null;
		/*
		 * pattern ###
		 */
		pattern = new byte[1][3];
		for (int i = 0; i < pattern[0].length; i++) {
			pattern[0][i] = ColorUtil.BREATH;
		}
		bp = new BreathPattern(pattern);
		if (map.containsKey(bp) == false) {
			point = Point.getPoint(0, 1);// this is actually delta, index from
											// 0.
			result = new SurviveResult();
			result.setXianShou(new Result(SurviveResult.LIVE, point));
			result.setHouShou(new Result(SurviveResult.DIE, point));
			result.setIndependent(false);
			map.put(bp, result);
		}

		/*
		 * pattern # # #
		 */
		pattern = new byte[3][1];
		for (int i = 0; i < pattern.length; i++) {
			pattern[i][0] = ColorUtil.BREATH;
		}
		bp = new BreathPattern(pattern);
		if (map.containsKey(bp) == false) {
			/*
			 * this is actually delta, index from 0
			 */
			point = Point.getPoint(1, 0);
			result = new SurviveResult();
			result.setXianShou(new Result(SurviveResult.LIVE, point));
			result.setHouShou(new Result(SurviveResult.DIE, point));
			result.setIndependent(false);
			map.put(bp, result);
		}

		/*
		 * pattern ## ##
		 */
		pattern = new byte[2][2];
		for (int i = 0; i < pattern.length; i++) {
			for (int j = 0; j < pattern[0].length; j++) {
				pattern[i][j] = ColorUtil.BREATH;
			}
		}
		pattern[1][1] = ColorUtil.BLANK_POINT;
		bp = new BreathPattern(pattern);
		if (map.containsKey(bp) == false) {
			point = Point.getPoint(0, 0); // this is actually delta, index
											// from 0.
			result = new SurviveResult();
			result.setXianShou(new Result(SurviveResult.LIVE, point));
			result.setHouShou(new Result(SurviveResult.DIE, point));
			result.setIndependent(false);
			map.put(bp, result);
			iterateIsomorphism(pattern, point, result);
		}

	}

	private static void iterateIsomorphism(byte[][] pattern, Point point,
			SurviveResult result) {
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
		SurviveResult result2 = new SurviveResult();
		Point point2 = Point
				.getPoint(point.getRow(), n - 1 - point.getColumn());
		result2.getXianShou().point = point2;
		result2.getHouShou().point = point2;

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
		SurviveResult result3 = new SurviveResult();
		Point point3 = Point
				.getPoint(m - 1 - point.getRow(), point.getColumn());
		result3.getXianShou().point = point3;
		result3.getHouShou().point = point3;

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
		SurviveResult result4 = new SurviveResult();
		Point point4 = Point.getPoint(m - 1 - point.getRow(),
				n - 1 - point.getColumn());
		result3.getXianShou().point = point3;
		result3.getHouShou().point = point3;

		BreathPattern bp4 = new BreathPattern(pattern);
		if (map.containsKey(bp4) == false) {
			map.put(bp4, result);
		}
	}

}
