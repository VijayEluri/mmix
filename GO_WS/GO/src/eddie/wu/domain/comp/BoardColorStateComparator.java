package eddie.wu.domain.comp;

import java.util.Comparator;

import eddie.wu.domain.BoardColorState;

/**
 * There is bug here: <br/>
 * ##01,02,03 <br/>
 * 01[B, _, B]01<br/>
 * 02[_, B, B]02<br/>
 * 03[B, B, _]03<br/>
 * ##01,02,03 <br/>
 * whoseTurn=Black<br/>
 * 
 * 
 * ##01,02,03 <br/>
 * 01[B, B, _]01<br/>
 * 02[_, B, B]02<br/>
 * 03[B, _, B]03<br/>
 * ##01,02,03 <br/>
 * whoseTurn=Black<br/>
 * cannot sort them, false<br/>
 * 
 * @deprecated
 * @author think
 *
 */
class BoardColorStateComparator implements Comparator<BoardColorState> {

	@Override
	public int compare(BoardColorState o1, BoardColorState o2) {
		int rowColumnSum1 = o1.getRowColumnSum();
		int rowColumnSum2 = o2.getRowColumnSum();
		if (rowColumnSum1 != rowColumnSum2) {
			return rowColumnSum1 - rowColumnSum2;
		}

		int rowSum1 = o1.getRowSum();
		int rowSum2 = o2.getRowSum();
		if (rowSum1 != rowSum2) {
			return rowSum1 - rowSum2;
		}

		int blackRowColumnSum1 = o1.getBlackRowColumnSum();
		int blackRowColumnSum2 = o2.getBlackRowColumnSum();
		if (blackRowColumnSum1 != blackRowColumnSum2) {
			return blackRowColumnSum1 - blackRowColumnSum2;
		}

		int blackRowSum1 = o1.getBlackRowSum();
		int blackRowSum2 = o2.getBlackRowSum();
		if (blackRowSum1 != blackRowSum2) {
			return blackRowSum1 - blackRowSum2;
		}

		int blackMin1 = o1.getMinRowColumnSum_black();
		int blackMin2 = o2.getMinRowColumnSum_black();
		if (blackMin1 != blackMin2) {
			return blackMin1 - blackMin2;
		}

		int whiteMin1 = o1.getMinRowColumnSum_white();
		int whiteMin2 = o2.getMinRowColumnSum_white();
		if (whiteMin1 != whiteMin2) {
			return whiteMin1 - whiteMin2;
		}

		System.out.println(o1.getStateString());
		System.out.println(o2.getStateString());
		System.err.println(o1.equals(o2));
		throw new RuntimeException("return 0");
		// return 0;// impossible
	}

	// /**
	// * should not contain equal object, otherwise exception.
	// */
	// @Override
	// public int compare(BoardColorState o1, BoardColorState o2) {
	// if (o1.getWhoseTurn() != o2.getWhoseTurn())
	// return o1.getWhoseTurn() - o2.getWhoseTurn();
	// if (o1.getBlackPoints().equals(o2.getBlackPoints())) {
	// if (o1.getWhitePoints().equals(o2.getWhitePoints())) {
	// //return 0;
	// } else if (o1.getWhitePoints().size() != o2.getWhitePoints().size()) {
	// return o1.getWhitePoints().size() - o2.getWhitePoints().size();
	// } else {
	// int count1 = 0, count2 = 0;
	// for (Point point : o1.getWhitePoints()) {
	// count1 += point.getOneDimensionCoordinate();
	// }
	// for (Point point : o2.getWhitePoints()) {
	// count2 += point.getOneDimensionCoordinate();
	// }
	// if (count1 != count2) {
	// return count1 - count2;
	// }
	// }
	// } else {
	// if (o1.getBlackPoints().size() != o2.getBlackPoints().size()) {
	// return o1.getBlackPoints().size() - o2.getBlackPoints().size();
	// } else {
	// int count1 = 0, count2 = 0;
	// for (Point point : o1.getBlackPoints()) {
	// count1 += point.getOneDimensionCoordinate();
	// }
	// for (Point point : o2.getBlackPoints()) {
	// count2 += point.getOneDimensionCoordinate();
	// }
	// if (count1 != count2) {
	// return count1 - count2;
	// }
	// }
	// }
	//
	// System.out.println(o1.getStateString());
	// System.out.println(o2.getStateString());
	// System.err.println(o1.equals(o2));
	// throw new RuntimeException("return 0");
	// // return 0;// impossible
	//
	// }
}