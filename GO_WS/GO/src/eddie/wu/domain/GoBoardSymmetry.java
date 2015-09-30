package eddie.wu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import eddie.wu.domain.comp.SymmetryRowColumnComparator;

/**
 * 原先仅仅考虑奇数路的棋盘,如19路和11路.<br/>
 * 现在增加对偶数路棋盘的支持,比如2路和4路.
 * 
 * @author Eddie
 * 
 * 
 *         TODO: had duplicate class in other package.
 */
public abstract class GoBoardSymmetry extends BasicGoBoard {
	private transient static final Logger log = Logger
			.getLogger(GoBoardSymmetry.class);

	public GoBoardSymmetry() {
		super(Constant.BOARD_SIZE);
	}

	public GoBoardSymmetry(int boardSize) {
		super(boardSize);
	}

	// public GoBoardSymmetry(byte[][] state) {
	// super(state);
	// }

	public GoBoardSymmetry(BoardColorState colorState) {
		super(colorState);
	}

	public int pointsInHorizontalLine() {
		/**
		 * 偶数路棋盘的对称中线不在棋盘上.
		 */
		if (boardSize % 2 == 0)
			return 0;

		int count = 0;
		/**
		 * middleLine = (boardSize+1)/2; to support even number board size.
		 * boardSieze 19 middleLine 10 boardSieze 18 middleLine 10
		 */
		int middleLine = (boardSize + 2) / 2;
		for (int i = 1; i <= boardSize; i++) {
			if (getBoardPoint(middleLine, i).getColor() != ColorUtil.BLANK) {
				count++;
			}
		}
		return count;
	}

	public int pointsInVerticalMiddleLine() {

		/**
		 * 偶数路棋盘的对称中线不在棋盘上.
		 */
		if (boardSize % 2 == 0)
			return 0;
		/**
		 * middleLine = (boardSize+1)/2; to support even number board size.
		 * boardSieze 19 middleLine 10 boardSieze 18 middleLine 10
		 */
		int middleLine = (boardSize + 2) / 2;

		int count = 0;
		for (int i = 1; i <= boardSize; i++) {
			if (getBoardPoint(i, middleLine).getColor() != ColorUtil.BLANK) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 不论奇偶路棋盘,斜的对称线总是在棋盘上.
	 * 
	 * @return
	 */
	public int pointsInForwardSlashLine() {
		int count = 0;
		for (int row = 1; row <= boardSize; row++) {
			if (getBoardPoint(boardSize + 1 - row, row).getColor() != ColorUtil.BLANK) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 不论奇偶路棋盘,斜的对称线总是在棋盘上.
	 * 
	 * @return
	 */
	public int pointsInBackwardSlashLine() {
		int count = 0;
		for (int row = 1; row <= boardSize; row++) {
			if (getBoardPoint(row, row).getColor() != ColorUtil.BLANK) {
				count++;
			}
		}
		return count;
	}

	/**
	 * whether the state is symmetry<br/>
	 * 棋子是否关于垂直中线对称?
	 * 
	 * @return
	 */
	public boolean verticalSymmetry() {
		if (possibleVerticalSymmetry() == false) {
			return false;
		}
		/**
		 * middleLine = (boardSize+1)/2; to support even number board size.
		 * boardSieze 19 middleLine 10 boardSieze 18 middleLine 10
		 */
		int middleLine = (boardSize + 2) / 2;
		for (int row = 1; row <= boardSize; row++) {
			for (int column = 1; column < middleLine; column++) {
				BoardPoint leftBoardPoint = getBoardPoint(row, column);
				BoardPoint rightBoardPoint = getBoardPoint(row, boardSize + 1
						- column);
				if (leftBoardPoint.getColor() != rightBoardPoint.getColor()) {
					if (log.isDebugEnabled()) {
						log.debug(leftBoardPoint);
						log.debug(rightBoardPoint);
					}
					return false;
				}
			}
		}
		if (log.isDebugEnabled())
			log.debug("verticalSymmetry");
		return true;

	}

	/**
	 * 棋子是否关于水平中线对称?
	 * 
	 * @return
	 */
	public boolean horizontalSymmetry() {
		if (possibleHorizontalSymmetry() == false) {
			return false;
		}
		/**
		 * middleLine = (boardSize+1)/2; to support even number board size.
		 * boardSieze 19 middleLine 10 boardSieze 18 middleLine 10
		 */
		int middleLine = (boardSize + 2) / 2;
		for (int column = 1; column <= boardSize; column++) {
			for (int row = 1; row < middleLine; row++) {
				BoardPoint leftBoardPoint = getBoardPoint(row, column);
				BoardPoint rightBoardPoint = getBoardPoint(boardSize + 1 - row,
						column);
				if (leftBoardPoint.getColor() != rightBoardPoint.getColor()) {
					if (log.isDebugEnabled()) {
						log.debug(leftBoardPoint);
						log.debug(rightBoardPoint);
					}
					return false;
				}
			}
		}
		if (log.isDebugEnabled())
			log.debug("horizontalSymmetry");
		return true;
	}

	public boolean backwardSlashSymmetry() {
		if (possibleBackwardSlashSymmetry() == false) {
			return false;
		}

		for (int row = 1; row <= boardSize; row++) {
			for (int column = row + 1; column <= boardSize; column++) {
				BoardPoint bottomBoardPoint = getBoardPoint(column, row);
				BoardPoint topBoardPoint = getBoardPoint(row, column);
				if (bottomBoardPoint.getColor() != topBoardPoint.getColor()) {
					if (log.isDebugEnabled()) {
						log.debug(topBoardPoint);
						log.debug(bottomBoardPoint);
					}
					return false;
				}

			}
		}
		if (log.isDebugEnabled())
			log.debug("backwardSlashSymmetry");
		return true;

	}

	/**
	 * 是否关于前向斜轴对称.
	 * 
	 * @return
	 */
	public boolean forwardSlashSymmetry() {
		if (possibleForwardSlashSymmetry() == false) {
			return false;
		}

		for (int row = 1; row <= boardSize; row++) {
			for (int column = 1; column < boardSize + 1 - row; column++) {
				BoardPoint topBoardPoint = getBoardPoint(row, column);
				BoardPoint bottomBoardPoint = getBoardPoint(boardSize + 1
						- column, boardSize + 1 - row);
				if (bottomBoardPoint.getColor() != topBoardPoint.getColor()) {

					if (log.isDebugEnabled()) {
						log.debug(topBoardPoint);
						log.debug(bottomBoardPoint);
					}

					return false;

				}

			}
		}
		if (log.isDebugEnabled())
			log.debug("forwardSlashSymmetry");
		return true;

	}

	public SymmetryResult getSymmetryResult() {
		SymmetryResult result = new SymmetryResult();

		result.setHorizontalSymmetry(this.horizontalSymmetry());
		result.setVerticalSymmetry(this.verticalSymmetry());
		result.setForwardSlashSymmetry(this.forwardSlashSymmetry());
		result.setBackwardSlashSymmetry(this.backwardSlashSymmetry());

		return result;

	}

	// public int getIsomorphisms() {
	//
	//
	// SymmetryResult symmetryResult = this.getSymmetryResult();
	// int numberOfSymmetry = symmetryResult.getNumberOfSymmetry();
	// if (numberOfSymmetry >= 3)
	// return 1;
	//
	// List<byte[][]> list = new ArrayList<byte[][]>();
	// //int size = this.boardSize + 2;
	// byte[][] horizontalMirror ;
	// byte[][] verticalMirror ;
	// byte[][] forwardSlashMirror;
	// byte[][] backwardSlashMirror;
	//
	// if (symmetryResult.isHorizontalSymmetry() == false) {
	// horizontalMirror = horizontalMirror(this.getMatrixState());
	// list.add(horizontalMirror);
	// }
	// if (symmetryResult.isVerticalSymmetry()== false) {
	// verticalMirror = verticalMirror(this.getMatrixState());
	// boolean duplicate=false;
	// for(byte[][] temp:list){
	// if(Arrays.deepEquals(temp, verticalMirror)) duplicate=true;
	// }
	// if(duplicate==false)
	// list.add(verticalMirror);
	// }
	// if (symmetryResult.isForwardSlashSymmetry()== false) {
	// forwardSlashMirror = forwardSlashMirror(this.getMatrixState());
	// boolean duplicate=false;
	// for(byte[][] temp:list){
	// if(Arrays.deepEquals(temp, forwardSlashMirror)) duplicate=true;
	// }
	// if(duplicate==false)
	// list.add(forwardSlashMirror);
	// }
	// if (symmetryResult.isBackwardSlashSymmetry()== false) {
	// backwardSlashMirror = backwardSlashMirror(this.getMatrixState());
	// boolean duplicate=false;
	// for(byte[][] temp:list){
	// if(Arrays.deepEquals(temp, backwardSlashMirror)) {
	// duplicate=true;
	// break;
	// }
	// }
	// if(duplicate==false)
	// list.add(backwardSlashMirror);
	// }
	// numberOfSymmetry--;//first round is easy, sure not identical.
	// if(numberOfSymmetry<0) return list.size()+1;
	//
	// /**
	// * apply second non-symmetry.
	// */
	// list.add(this.getMatrixState());
	//
	//
	// numberOfSymmetry--;//first round is easy, sure not identical.
	// if(numberOfSymmetry<0) return list.size();
	//
	// while (numberOfSymmetry-- >0) {
	//
	//
	// /**
	// * get the combination of conversion; second conversion is done. now
	// * first // conversion.
	// */
	// // if (list.size() < 2) {
	// // log.debug(list);
	// // } else {
	// Point pointA = list.get(1);
	// if (symmetryResult.isHorizontalSymmetry()) {
	// list.add(pointA.horizontalMirror());
	// } else if (symmetryResult.isVerticalSymmetry()) {
	// list.add(pointA.verticalMirror());
	// } else if (symmetryResult.isForwardSlashSymmetry()) {
	// list.add(pointA.forwardSlashMirror());
	// } else if (symmetryResult.isBackwardSlashSymmetry()) {
	// list.add(pointA.backwardSlashMirror());
	// }
	// // }
	// list.add(point);
	//
	// } else if (numberOfSymmetry == 1) {
	// if (symmetryResult.isHorizontalSymmetry()) {
	// list.add(horizontalMirror);
	// } else if (symmetryResult.isVerticalSymmetry()) {
	// list.add(verticalMirror);
	// } else if (symmetryResult.isForwardSlashSymmetry()) {
	// list.add(forwardSlashMirror());
	// } else if (symmetryResult.isBackwardSlashSymmetry()) {
	// list.add(backwardSlashMirror());
	// }
	// list.add(point);
	// }
	//
	// }

	public static byte[][] verticalMirror(byte[][] original) {

		int size = original.length;
		int boardSize = size - 2;
		byte[][] state = new byte[size][size];
		Set<Point> allPoints = Point.getAllPoints(boardSize);
		if (allPoints == null) {
			System.err.println("allPoints==null");
		}
		for (Point point : allPoints) {
			Point mirror = point.verticalMirror();
			state[mirror.getRow()][mirror.getColumn()] = original[point
					.getRow()][point.getColumn()];
		}

		return state;
	}

	public static byte[][] backwardSlashMirror(byte[][] original) {
		int size = original.length;
		int boardSize = size - 2;
		byte[][] state = new byte[size][size];

		for (Point point : Point.getAllPoints(boardSize)) {
			Point mirror = point.backwardSlashMirror();
			state[mirror.getRow()][mirror.getColumn()] = original[point
					.getRow()][point.getColumn()];
		}

		return state;
	}

	// for(int row = 1;row<=boardSize;row++){
	// for(int column = 1;column<=boardSize;column++){
	//
	// }
	// }

	public static byte[][] horizontalMirror(byte[][] original) {
		int size = original.length;
		int boardSize = size - 2;
		byte[][] state = new byte[size][size];

		for (Point point : Point.getAllPoints(boardSize)) {
			Point mirror = point.horizontalMirror();
			state[mirror.getRow()][mirror.getColumn()] = original[point
					.getRow()][point.getColumn()];
		}

		return state;
	}

	public static byte[][] forwardSlashMirror(byte[][] original) {
		int size = original.length;
		int boardSize = size - 2;
		byte[][] state = new byte[size][size];

		for (Point point : Point.getAllPoints(boardSize)) {
			Point mirror = point.forwardSlashMirror();
			state[mirror.getRow()][mirror.getColumn()] = original[point
					.getRow()][point.getColumn()];
		}

		return state;
	}

	public boolean possibleVerticalSymmetry() {
		int count = this.getNumberOfPoint() - pointsInVerticalMiddleLine();
		return ColorUtil.evenNumberOfPoints(count);
	}

	public boolean possibleHorizontalSymmetry() {
		int count = getNumberOfPoint() - pointsInHorizontalLine();
		return ColorUtil.evenNumberOfPoints(count);
	}

	public boolean possibleForwardSlashSymmetry() {
		int count = getNumberOfPoint() - pointsInForwardSlashLine();
		return ColorUtil.evenNumberOfPoints(count);
	}

	public boolean possibleBackwardSlashSymmetry() {
		int count = getNumberOfPoint() - pointsInBackwardSlashLine();
		return ColorUtil.evenNumberOfPoints(count);
	}

	/**
	 * 将四角把边的变化（状态）转化到左上角。前提是角部的变化才行。
	 */
	public byte[][] convertToTopLeft(Point target) {
		byte[][] matrixState = this.getMatrixState();
		if (target.isRightTop()) {
			return GoBoardSymmetry.verticalMirror(matrixState);
		} else if (target.isLeftBottom()) {
			return GoBoardSymmetry.horizontalMirror(matrixState);
		} else if (target.isRightBottom()) {
			return GoBoardSymmetry.forwardSlashMirror(matrixState);
		} else if (target.isLeftTop()) {
			return matrixState;
		} else {
			throw new RuntimeException("target is not in any area: " + target);
		}

	}

	/**
	 * TODO:
	 */
	public static void addResult(Map<BoardColorState, Integer> results,
			BoardColorState boardColorState, int score) {
		byte[][] finalState = boardColorState.getMatrixState();
		int whoseTurn = boardColorState.getWhoseTurn();
		results.put(boardColorState, score);
		results.put(BoardColorState.getInstance(verticalMirror(finalState),
				whoseTurn), score);
		results.put(BoardColorState.getInstance(horizontalMirror(finalState),
				whoseTurn), score);
		results.put(BoardColorState.getInstance(forwardSlashMirror(finalState),
				whoseTurn), score);

	}
	@Deprecated
	public byte[][] convertFromTopLeft(Point target) {
		byte[][] matrixState = this.getMatrixState();
		if (target.isRightTop()) {
			return verticalMirror(matrixState);
		} else if (target.isLeftBottom()) {
			return horizontalMirror(matrixState);
		} else if (target.isRightBottom()) {
			return forwardSlashMirror(matrixState);
		} else if (target.isLeftTop()) {
			return matrixState;
		} else {
			throw new RuntimeException("target is not in any area: " + target);
		}
	}

	/**
	 * with which operation the point can be normalized to the standard point.
	 * 
	 * @param point
	 *            next step to play
	 * @param symmetryResult
	 *            of current state
	 * @return
	 */
	public static SymmetryResult getNormalizeOperation(Point point,
			SymmetryResult symmetryResult) {
		List<PointSymmetry> list = new ArrayList<PointSymmetry>();
		int numberOfSymmetry = symmetryResult.getNumberOfSymmetry();

		Point horizontalMirror = point.horizontalMirror();
		SymmetryResult horizontalSym = new SymmetryResult();
		horizontalSym.setHorizontalSymmetry(true);

		Point verticalMirror = point.verticalMirror();
		SymmetryResult verticalSym = new SymmetryResult();
		verticalSym.setVerticalSymmetry(true);

		SymmetryResult verticalHorizontalSym = new SymmetryResult();
		verticalHorizontalSym.setVerticalSymmetry(true);
		verticalHorizontalSym.setHorizontalSymmetry(true);
		// fix a bug-we need to get the simplest/smaller result
		// some time one symmetry get same result as no symmetry
		list.add(new PointSymmetry(point, new SymmetryResult()));

		if (numberOfSymmetry == 4) {
			list.add(new PointSymmetry(horizontalMirror, horizontalSym));
			list.add(new PointSymmetry(verticalMirror, verticalSym));
			list.add(new PointSymmetry(horizontalMirror.verticalMirror(),
					verticalHorizontalSym));
			list.add(new PointSymmetry(point, new SymmetryResult()));
			Collections.sort(list, new PointSymmetryComparator());

			List<PointSymmetry> list2 = new ArrayList<PointSymmetry>();
			list2.add(list.get(0));
			SymmetryResult backwardSlashSym = list.get(0).getSymmetry()
					.getCopy();
			backwardSlashSym.setBackwardSlashSymmetry(true);
			list2.add(new PointSymmetry(list.get(0).getPoint()
					.backwardSlashMirror(), backwardSlashSym));
			Collections.sort(list2, new PointSymmetryComparator());
			return list2.get(0).getSymmetry();
		} else {
			if (symmetryResult.isHorizontalSymmetry()) {
				list.add(new PointSymmetry(horizontalMirror, horizontalSym));
			}
			if (symmetryResult.isVerticalSymmetry()) {
				list.add(new PointSymmetry(verticalMirror, verticalSym));
			}
			if (symmetryResult.isForwardSlashSymmetry()) {
				SymmetryResult forwardSlashSym = new SymmetryResult();
				forwardSlashSym.setForwardSlashSymmetry(true);
				list.add(new PointSymmetry(point.forwardSlashMirror(),
						forwardSlashSym));
			}
			if (symmetryResult.isBackwardSlashSymmetry()) {
				SymmetryResult backwardSlashSym = new SymmetryResult();
				backwardSlashSym.setBackwardSlashSymmetry(true);
				list.add(new PointSymmetry(point.backwardSlashMirror(),
						backwardSlashSym));
			}
			list.add(new PointSymmetry(point, new SymmetryResult()));
			if (numberOfSymmetry == 1) {
				Collections.sort(list, new PointSymmetryComparator());
				return list.get(0).getSymmetry();
			}

			/**
			 * get the combination of conversion; second conversion is done. <br/>
			 * after first conversion of two symmetry. we have at most 3 point,
			 */
			Point pointA = list.get(1).getPoint();
			SymmetryResult res = list.get(1).getSymmetry().getCopy();
			if (symmetryResult.isHorizontalSymmetry()
					&& res.isHorizontalSymmetry() == false) {
				res.setHorizontalSymmetry(true);
				list.add(new PointSymmetry(pointA.horizontalMirror(), res));
			} else if (symmetryResult.isVerticalSymmetry()
					&& res.isVerticalSymmetry() == false) {
				res.setVerticalSymmetry(true);
				list.add(new PointSymmetry(pointA.verticalMirror(), res));
			} else if (symmetryResult.isForwardSlashSymmetry()
					&& res.isForwardSlashSymmetry() == false) {
				res.setForwardSlashSymmetry(true);
				list.add(new PointSymmetry(pointA.forwardSlashMirror(), res));
			} else if (symmetryResult.isBackwardSlashSymmetry()
					&& res.isBackwardSlashSymmetry() == false) {
				res.setBackwardSlashSymmetry(true);
				list.add(new PointSymmetry(pointA.backwardSlashMirror(), res));
			}
			Collections.sort(list, new PointSymmetryComparator());
			return list.get(0).getSymmetry();

		}

	}
}

class PointSymmetry {
	private Point point;
	private SymmetryResult symmetry;

	public PointSymmetry(Point point, SymmetryResult symmetry) {
		this.point = point;
		this.symmetry = symmetry;
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public SymmetryResult getSymmetry() {
		return symmetry;
	}

	public void setSymmetry(SymmetryResult symmetry) {
		this.symmetry = symmetry;
	}

	public PointSymmetry getCopy() {

		return new PointSymmetry(this.point, this.symmetry);
	}

}

class PointSymmetryComparator implements Comparator<PointSymmetry> {
	@Override
	public int compare(PointSymmetry o1, PointSymmetry o2) {
		int sum1 = o1.getPoint().getRow() + o1.getPoint().getColumn();
		int sum2 = o2.getPoint().getRow() + o2.getPoint().getColumn();
		if (sum1 != sum2) {
			return sum1 - sum2;
		}

		return o1.getPoint().getRow() - o2.getPoint().getRow();

	}

}