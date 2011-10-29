package eddie.wu.domain.survive;

import java.util.List;

import eddie.wu.domain.Block;
import eddie.wu.domain.Delta;
import eddie.wu.domain.Point;
import eddie.wu.domain.Shape;

/**
 * basic function to store the knowledge about the basic of small eye from 2
 * Stones to 7 stones. <br/>
 * 从二子到七子的基本死活结论。
 * 二到四子已经手工完成。
 * @author wueddie-wym-wrz
 * 
 */
public class SmallEye {
	public static final String STRAIGHT_SIX_STONE_EYE = "直六";

	public static final String MATRIX_SIX_STONE_EYE = "板六";

	public static final String FLOWER_SIX_STONE_EYE = "梅花六";

	public static final String RULER_FIVE_STONE_EYE = "曲尺五";

	public static final String Z_FIVE_STONE_EYE = "折五";

	public static final String T_FIVE_STONE_EYE = "丁五";

	public static final String FLOWER_FIVE_STONE_EYE = "梅花五";

	public static final String TRAP_FIVE_STONE_EYE = "凹五";

	public static final String KNIFE_HANDLER_FIVE_STONE_EYE = "刀把五";

	public static final String STRAIGHT_FIVE_STONE_EYE = "直五";

	public static final String RULER_FOUR_STONE_EYE = "曲四";

	public static final String Z_FOUR_STONE_EYE = "折四";

	public static final String T_FOUR_STONE_EYE = "丁四";

	public static final String RECTANGLT_FOUR_STONE_EYE = "方四";

	public static final String STRAIGHT_FOUR_STONE_EYE = "直四";

	public static final String BEND_THREE_STONE_EYE = "曲三";

	public static final String STRAIGHT_THREE_STONE_EYE = "直三";

	public static final String TWO_STONE_EYE = "直二";

	public static final String SINGLE_STONE_EYE = "单子眼";
	Block block;

	/**
	 * 
	 * @param block
	 *            Eye Block
	 */
	public SmallEye(Block block) {
		this.block = block;
	}

	/**
	 * block 中的气点坐标是具体局面中的实际坐标，先转换成局部的坐标；得到结果之后，再转换回实际的坐标。
	 * 
	 * @return
	 */

	public RelativeSurviveResult getResult() {
		int num = block.getTotalNumberOfPoint();
		switch (num) {
		case 2:
			return getResult_TwoStones();
		case 3:
			return getResult_ThreeStones();
		case 4:
			return getResult_FourStones();
		case 5:
			return getResult_FiveStones();
		}

		throw new RuntimeException(
				"the breath block should be in size from 2 to 7 stones.");
	}

	/**
	 * 结果指的是目标块的死活。区分先后手。
	 * 
	 * @return
	 */
	private RelativeSurviveResult getResult_TwoStones() {
		RelativeSurviveResult result = new RelativeSurviveResult();
		// 已死，（己方）无子可下。
		result.setXianShou(new RelativeResult(RelativeSurviveResult.DIE, null));
		// 已死，（对方）不用下。
		result.setHouShou(new RelativeResult(RelativeSurviveResult.DIE, null));
		// 先后手没有区别
		result.setIndependent(true);
		// 下子是否有用，即是否有先手性，如果两个节点之间没有区别，则该步没有意义，则浪费了一手棋。
		result.setWaste(true);
		return result;
	}

	/**
	 * need to category according to the shape! because the move to make the
	 * eyes depending on the shape! brute force: 死记硬背。
	 * 
	 * @return
	 */
	private RelativeSurviveResult getResult_ThreeStones() {
		RelativeSurviveResult result = new RelativeSurviveResult();
		Shape shape = block.getShape();
		Delta point = null;
		/**
		 * 1*3 or 3*1 pattern.
		 */
		if ((shape.getDeltaX() == 1 && shape.isLandscape() == false)
				|| (shape.getDeltaY() == 1 && shape.isLandscape() == true)) {
			int x = (shape.getMinX() + shape.getMaxX()) / 2;
			int y = (shape.getMinY() + shape.getMaxY()) / 2;
			point = Delta.getDelta(x, y);
		} else {// 2*2 pattern.
			/*
			 * 先确定非眼块之点，再取其对角点。
			 */
			if (!block.getAllPoints().contains(
					Point.getPoint(shape.getMinX(), shape.getMinY()))) {
				point = Delta.getDelta(shape.getMaxX(), shape.getMaxY());
			}
			if (!block.getAllPoints().contains(
					Point.getPoint(shape.getMinX(), shape.getMaxY()))) {
				point = Delta.getDelta(shape.getMaxX(), shape.getMinY());
			}
			if (!block.getAllPoints().contains(
					Point.getPoint(shape.getMaxX(), shape.getMinY()))) {
				point = Delta.getDelta(shape.getMinX(), shape.getMaxY());
			}
			if (!block.getAllPoints().contains(
					Point.getPoint(shape.getMaxX(), shape.getMaxY()))) {
				point = Delta.getDelta(shape.getMinX(), shape.getMinY());
			}

		}

		result.setXianShou(new RelativeResult(RelativeSurviveResult.LIVE, point));
		result.setHouShou(new RelativeResult(RelativeSurviveResult.DIE, point));
		result.setIndependent(false);
		return result;
	}

	private RelativeSurviveResult getResult_FourStones() {
		RelativeSurviveResult result = new RelativeSurviveResult();

		Shape shape = block.getShape();
		Point point = null;
		if (shape.getMinDelta() == 1) {
			result.setXianShou(new RelativeResult(RelativeSurviveResult.LIVE, null));
			result.setHouShou(new RelativeResult(RelativeSurviveResult.LIVE, null));
			result.setIndependent(true);
		} else if (shape.getMinDelta() == 2) {
			if (shape.getMaxDelta() == 2) {
				result.setXianShou(new RelativeResult(RelativeSurviveResult.DIE, null));
				result.setHouShou(new RelativeResult(RelativeSurviveResult.DIE, null));
				result.setIndependent(true);
			} else if (shape.getMaxDelta() == 3) {
				List<Point> corners = block.getShapeCorners(shape);
				if(corners.size()==2){
					if(corners.get(0).isSameline(corners.get(1))){//丁四
						result.setXianShou(new RelativeResult(RelativeSurviveResult.LIVE, null));
						result.setHouShou(new RelativeResult(RelativeSurviveResult.DIE, null));
						result.setIndependent(false);
					}else{//折四
						result.setXianShou(new RelativeResult(RelativeSurviveResult.LIVE, null));
						result.setHouShou(new RelativeResult(RelativeSurviveResult.LIVE, null));
						result.setIndependent(true);
					}
				} else{//==3曲尺四
					result.setXianShou(new RelativeResult(RelativeSurviveResult.LIVE, null));
					result.setHouShou(new RelativeResult(RelativeSurviveResult.LIVE, null));
					result.setIndependent(true);
				}
				/*
				 * ==3 there are two pattern(丁四和折四), they have different result. hard
				 * coding is not a good solution.
				 * 模式匹配的话，关键是如何表达。
				 * 
				 * 点眼之后的对杀和一般的对杀不同，做眼一方即使吃到了点眼之子，也不可能和己方的其他块相连。
				 * 而通常的对杀，至少有四块棋参与，两两将对方隔开，任何一个对杀块被吃，都导致另一方的两个
				 * 棋块联通。一般情况下，两块相连都是变得相当强大，局部也就确定了结果，告一段落。个别的例
				 * 外可能包括接不归等等。
				 * 所以点眼之后的处理还要能记忆这是由“点眼”演变而来。当然也要考虑能识别某个局部是有点眼变
				 * 化来的。
				 */

			}
		}
		// result.setWaste(true);
		return result;
	}

	private RelativeSurviveResult getResult_FiveStones() {
		// TODO Auto-generated method stub
		return null;
	}
}
