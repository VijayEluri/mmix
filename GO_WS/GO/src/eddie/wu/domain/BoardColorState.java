/*
 * Created on 2005-4-22
 *


 */
package eddie.wu.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eddie.wu.domain.comp.RowColumnComparator;

/**
 * The most basic way to represent the state of the board without derived
 * information. it could be converted to classic matrix representation.<br/>
 * 做成不能动态改变的。每个状态其实是GoBoard某一状态的快照。<br/>
 * 或者说GoBoard的状态是动态变化的，但是BoardColorState是代表一个静止的状态。
 * 
 * @author eddie
 * 
 *         use Bit Set to represent the board Point sate! index start from 1.
 */
public class BoardColorState {
	private static final Logger log = Logger.getLogger(BoardColorState.class);

	public static BoardColorState getInstance(byte[][] board, int whoseTurn) {

		return new BoardColorState(board, whoseTurn);

	}

	public static void showDiff(BoardColorState expected, BoardColorState actual) {
		List<Point> list;// = new ArrayList<Point>();
		List<Point> list2;
		;
		if (!expected.getBlackPoints().equals(actual.getBlackPoints())) {
			if (log.isDebugEnabled())
				log.debug("Black point: correct result first ");
			list = new ArrayList<Point>();
			list.addAll(expected.getBlackPoints());
			Collections.sort(list, new RowColumnComparator());
			if (log.isDebugEnabled())
				log.debug(list);

			list2 = new ArrayList<Point>();
			list2.addAll(actual.getBlackPoints());
			Collections.sort(list2, new RowColumnComparator());
			if (log.isDebugEnabled())
				log.debug(list2);

			list.removeAll(actual.getBlackPoints());
			if (!list.isEmpty())
				if (log.isDebugEnabled())
					log.debug("only in expected" + list);

			list2.removeAll(expected.getBlackPoints());
			if (!list2.isEmpty())
				if (log.isDebugEnabled())
					log.debug("only in actual: " + list2);

		}

		if (!expected.getWhitePoints().equals(actual.getWhitePoints())) {
			if (log.isDebugEnabled())
				log.debug("White point: correct result first");
			list = new ArrayList<Point>();
			list.addAll(expected.getWhitePoints());
			Collections.sort(list, new RowColumnComparator());
			if (log.isDebugEnabled())
				log.debug(list);

			list2 = new ArrayList<Point>();
			list2.addAll(actual.getWhitePoints());
			Collections.sort(list2, new RowColumnComparator());
			if (log.isDebugEnabled())
				log.debug(list2);

			list.removeAll(actual.getWhitePoints());
			if (!list.isEmpty())
				if (log.isDebugEnabled())
					log.debug("only in expected: " + list);

			list2.removeAll(expected.getWhitePoints());
			if (!list2.isEmpty())
				if (log.isDebugEnabled())
					log.debug("only in actual: " + list2);
		}

		// log.debug("shoushu: " + expected.getShoushu() + "v.s."
		// + actual.getShoushu());
		log.debug("whoseturn: " + expected.whoseTurn + " v.s. "
				+ actual.whoseTurn);

	}

	public final int boardSize;
	/**
	 * next step color
	 */
	private int whoseTurn;

	/**
	 * short cut to know the state is different<br/>
	 * number of black stones.
	 */
	private int blackStones;

	/**
	 * number of white stones.
	 */
	private int whiteStones;

	/**
	 * index 0 is not used.
	 */
	private BitSet black;
	private BitSet white;

	/**
	 * used to decide symmetry of state, generate on the fly before using them
	 */
	private transient int rowColumnSum;
	private transient int rowSum;
	private transient int blackRowColumnSum;
	private transient int blackRowSum;

	/**
	 * 搜索相关的数据结构
	 */
	private transient int finalScore = Constant.UNKOWN;// finalized score
	/**
	 * // temp best. could be better.
	 */
	private transient int tempBestScore = Constant.UNKOWN;
	private transient int variant;// 搜索中变化的数目

	public BoardColorState(byte[][] board, int whoseTurn) {
		boardSize = board.length - 2;
		init(board, whoseTurn);
	}

	public BoardColorState(int boardSize, int whoseTurn) {
		this.whoseTurn = whoseTurn;
		this.boardSize = boardSize;
		int length = boardSize * boardSize + 1;
		black = new BitSet(length);
		white = new BitSet(length);
	}

	public boolean backwardSlashSymmetry() {
		if (possibleBackwardSlashSymmetry() == false) {
			return false;
		}

		for (int row = 1; row <= boardSize; row++) {
			for (int column = row + 1; column <= boardSize; column++) {
				if (getColor(column, row) != getColor(row, column)) {
					return false;
				}

			}
		}
		if (log.isDebugEnabled())
			log.debug("backwardSlashSymmetry");
		return true;

	}

	/**
	 * 黑白交换后的状态<br/>
	 * 黑白子互换的局面
	 * 
	 * @return
	 */
	public BoardColorState blackWhiteSwitch() {
		BoardColorState state = new BoardColorState(this.boardSize,
				ColorUtil.enemyColor(this.whoseTurn));
		state.black = (BitSet) this.white.clone();
		state.white = (BitSet) this.black.clone();
		state.blackStones = this.whiteStones;
		state.whiteStones = this.blackStones;
		state.rowColumnSum = this.rowColumnSum;
		state.rowSum = this.rowSum;
		state.blackRowColumnSum = this.rowColumnSum - this.blackRowColumnSum;
		state.blackRowSum = this.rowSum - this.blackRowSum;
		return state;
	}

	public boolean equals(Object object) {
		if (object instanceof BoardColorState == false) {
			return false;
		}
		BoardColorState boardState = (BoardColorState) object;
		if (boardState.blackStones != blackStones
				|| boardState.whiteStones != whiteStones) {
			return false;
		} else if (boardState.whoseTurn != whoseTurn) {
			// to accommodate the case we do not know whose Turn.
			if (whoseTurn == -1 || boardState.whoseTurn == -1) {
				return this.black.equals(boardState.black)
						&& this.white.equals(boardState.white);
			} else {
				return false;
			}
		} else {
			return this.black.equals(boardState.black)
					&& this.white.equals(boardState.white);
		}

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
				if (getColor(boardSize + 1 - column, boardSize + 1 - row) != getColor(
						row, column)) {
					return false;

				}

			}
		}
		if (log.isDebugEnabled())
			log.debug("forwardSlashSymmetry");
		return true;

	}

	public Set<Point> getBlackPoints() {
		Set<Point> blackPoints = new HashSet<Point>(128);
		for (short i = 1; i <= boardSize * boardSize; i++) {
			if (black.get(i)) {
				blackPoints.add(Point.getPointFromOneDim(boardSize, i));
			}
		}
		TestCase.assertEquals(blackPoints.size(), blackStones);

		return blackPoints;
	}

	public int getBlackRowColumnSum() {
		if (Constant.INTERNAL_CHECK == true) {
			int sum = 0;
			for (Point point : this.getBlackPoints()) {
				sum += point.getRow();
				sum += point.getColumn();
			}

			TestCase.assertEquals(sum, this.blackRowColumnSum);
			return sum;
		} else {
			return this.blackRowColumnSum;
		}
	}

	public int getBlackRowSum() {
		if (Constant.INTERNAL_CHECK == true) {
			int sum = 0;
			for (Point point : this.getBlackPoints()) {
				sum += point.getRow();
			}

			TestCase.assertEquals(sum, this.blackRowSum);
			return sum;
		} else {
			return this.blackRowSum;
		}
	}

	public Set<Point> getBlankPoints() {
		BitSet blank = (BitSet) black.clone();
		black.andNot(white);
		Set<Point> blankPoints = new HashSet<Point>(128);
		for (short i = 1; i <= boardSize * boardSize; i++) {
			if (blank.get(i))
				blankPoints.add(Point.getPointFromOneDim(boardSize, i));
		}
		return blankPoints;

	}

	public int getColor(int row, int column) {
		int bitIndex = Point.getOneDimension(boardSize, row, column);
		if (black.get(bitIndex) == true) {
			return Constant.BLACK;
		} else if (white.get(bitIndex) == true) {
			return Constant.WHITE;
		} else {
			return Constant.BLANK;
		}
	}

	public BoardColorState getCopy() {
		BoardColorState state = new BoardColorState(this.boardSize,
				ColorUtil.enemyColor(this.whoseTurn));
		state.black = (BitSet) this.black.clone();
		state.white = (BitSet) this.white.clone();
		state.blackStones = this.blackStones;
		state.whiteStones = this.whiteStones;
		state.rowColumnSum = this.rowColumnSum;
		state.rowSum = this.rowSum;
		state.blackRowColumnSum = this.blackRowColumnSum;
		state.blackRowSum = this.blackRowSum;
		return state;
	}

	/**
	 * 
	 * @return
	 */
	public char[][] getDisplayMatrixState() {
		int length = this.boardSize;
		char[][] matrix = new char[length][length];
		Point point;
		for (short i = 1; i <= boardSize * boardSize; i++) {
			point = Point.getPointFromOneDim(boardSize, i);
			if (black.get(i)) {

				matrix[point.getRow() - 1][point.getColumn() - 1] = ColorUtil.BLACK_STRING;
			} else if (white.get(i)) {
				matrix[point.getRow() - 1][point.getColumn() - 1] = ColorUtil.WHITE_STRING;
			} else {
				matrix[point.getRow() - 1][point.getColumn() - 1] = ColorUtil.BLANK_STRING;
			}
		}
		return matrix;
	}

	public char[][] getDisplayMatrixState(Shape shape) {
		byte[][] state = this.getMatrixState();
		char[][] matrix = new char[shape.getDeltaX() + 1][shape.getDeltaY() + 1];
		// colorState.getDisplayMatrixState();
		int row, column;
		for (int i = shape.getMinX(); i <= shape.getMaxX(); i++) {
			for (int j = shape.getMinY(); j <= shape.getMaxY(); j++) {
				row = i - shape.getMinX();
				column = j - shape.getMinY();
				if (state[i][j] == Constant.BLACK) {
					matrix[row][column] = ColorUtil.BLACK_STRING;
				} else if (state[i][j] == Constant.WHITE) {
					matrix[row][column] = ColorUtil.WHITE_STRING;
				} else if (state[i][j] == Constant.BLANK) {
					matrix[row][column] = ColorUtil.BLANK_STRING;
				}
			}

		}
		return matrix;
	}

	public byte[][] getMatrixState() {
		int length = this.boardSize + 2;
		byte[][] matrix = new byte[length][length];
		Point point;
		for (short i = 1; i <= boardSize * boardSize; i++) {
			if (black.get(i)) {
				point = Point.getPointFromOneDim(boardSize, i);
				matrix[point.getRow()][point.getColumn()] = ColorUtil.BLACK;
			} else if (white.get(i)) {
				point = Point.getPointFromOneDim(boardSize, i);
				matrix[point.getRow()][point.getColumn()] = ColorUtil.WHITE;
			}
		}
		return matrix;
	}

	public void getMatrixState(byte[][] matrix) {
		Point point;
		for (short i = 1; i <= boardSize * boardSize; i++) {
			if (black.get(i)) {
				point = Point.getPointFromOneDim(boardSize, i);
				matrix[point.getRow()][point.getColumn()] = ColorUtil.BLACK;
			} else if (white.get(i)) {
				point = Point.getPointFromOneDim(boardSize, i);
				matrix[point.getRow()][point.getColumn()] = ColorUtil.WHITE;
			}
		}
	}

	public int getMinRowColumnSum_black() {
		int sum = Integer.MAX_VALUE;
		for (Point point : this.getBlackPoints()) {
			if (point.getRowColSum() < sum) {
				sum = point.getRowColSum();
			}
		}
		return sum;
	}

	public int getMinRowColumnSum_white() {
		int sum = Integer.MAX_VALUE;
		for (Point point : this.getWhitePoints()) {
			if (point.getRowColSum() < sum) {
				sum = point.getRowColSum();
			}
		}
		return sum;
	}

	// public String toString(){
	// return black.toString()+":"+white.toString();
	// }

	public int getNumberOfPoint() {
		return this.whiteStones + this.blackStones;
	}

	/**
	 * get a copy with reverse value of whoseturn, board color is NOT switched,
	 * so that the score is same only if the state is finalized (not dependent
	 * on whose Turn) <br/>
	 * 改变轮走方。
	 * 
	 * @return
	 */
	public BoardColorState getReverseTurnCopy() {
		BoardColorState state = new BoardColorState(this.boardSize,
				ColorUtil.enemyColor(this.whoseTurn));
		state.black = (BitSet) this.black.clone();// ?
		state.white = (BitSet) this.white.clone();
		state.blackStones = this.blackStones;
		state.whiteStones = this.whiteStones;
		state.rowColumnSum = this.rowColumnSum;
		state.rowSum = this.rowSum;
		state.blackRowColumnSum = this.blackRowColumnSum;
		state.blackRowSum = this.blackRowSum;
		return state;
	}

	/**
	 * TODO: collect the statistic data in one time
	 * 
	 * @return
	 */
	public int getRowColumnSum() {
		if (Constant.INTERNAL_CHECK == true) {
			int sum = 0;
			for (Point point : this.getBlackPoints()) {
				sum += point.getRow();
				sum += point.getColumn();
			}
			for (Point point : this.getWhitePoints()) {
				sum += point.getRow();
				sum += point.getColumn();
			}
			TestCase.assertEquals(sum, this.rowColumnSum);
			return sum;
		} else {
			return this.rowColumnSum;
		}
	}

	public int getRowSum() {
		if (Constant.INTERNAL_CHECK == true) {
			int sum = 0;
			for (Point point : this.getBlackPoints()) {
				sum += point.getRow();
			}
			for (Point point : this.getWhitePoints()) {
				sum += point.getRow();
			}
			TestCase.assertEquals(sum, this.rowSum);
			return sum;
		} else {
			return this.rowSum;
		}
	}

	public int getScore() {
		return finalScore;
	}

	public StringBuilder getStateString() {
		// BoardColorState colorState = getBoardColorState();
		char[][] state = this.getDisplayMatrixState();
		StringBuilder header = new StringBuilder();
		StringBuilder result = new StringBuilder();
		result.append(Constant.lineSeparator);
		header.append("  ");
		for (int i = 0; i < this.boardSize; i++) {
			if (i == 0) {
				header.append("01,");
			} else if (i == this.boardSize - 1) {
				if (this.boardSize < 10) {
					header.append("0" + this.boardSize);
				} else {
					header.append(this.boardSize);
				}
			} else {
				if (i + 1 < 10) {
					header.append("0" + (i + 1) + ",");
				} else {
					header.append((i + 1) + ",");
				}
			}

		}
		header.append("  ");
		header.append(Constant.lineSeparator);
		String footer = header.toString();

		result.append(header.toString());
		for (int i = 0; i < state.length; i++) {
			// if(log.isDebugEnabled()) log.debug(Arrays.toString(state[i]));
			if (i + 1 < 10) {
				result.append("0" + (i + 1) + Arrays.toString(state[i]) + "0"
						+ (1 + i));
				result.append(Constant.lineSeparator);
			} else {
				result.append((i + 1) + Arrays.toString(state[i]) + (i + 1));
				result.append(Constant.lineSeparator);
			}
		}
		result.append(footer);
		result.append("whoseTurn=" + this.getWhoseTurnString());
		// result.append(Constant.lineSeparator);//
		return result;
	}

	public String getStateAsOneLineString() {
		char[][] state = this.getDisplayMatrixState();
		StringBuilder result = new StringBuilder();
		result.append(this.getWhoseTurnString());
		result.append(" ");
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++) {
				result.append(state[i][j]);
			}
			result.append(" ");
		}

		return result.toString();
	}

	public SymmetryResult getSymmetryResult() {
		SymmetryResult result = new SymmetryResult();

		result.setHorizontalSymmetry(this.horizontalSymmetry());
		result.setVerticalSymmetry(this.verticalSymmetry());
		result.setForwardSlashSymmetry(this.forwardSlashSymmetry());
		result.setBackwardSlashSymmetry(this.backwardSlashSymmetry());

		return result;

	}

	public int getVariant() {
		return variant;
	}

	// public void remove(BoardPoint point) {
	// if (point.getColor() == ColorUtil.BLACK) {
	// black.set(point.getOneDimensionCoordinate(), false);
	// blackStones--;
	// } else if (point.getColor() == ColorUtil.WHITE) {
	// white.set(point.getOneDimensionCoordinate(), false);
	// whiteStones--;
	// }
	//
	// }

	public Set<Point> getWhitePoints() {
		Set<Point> whitePoints = new HashSet<Point>(128);
		for (short i = 1; i <= boardSize * boardSize; i++) {
			if (white.get(i)) {
				whitePoints.add(Point.getPointFromOneDim(boardSize, i));
			}
		}
		TestCase.assertEquals(whitePoints.size(), whiteStones);
		return whitePoints;
	}

	public int getWhoseTurn() {
		return whoseTurn;
	}

	public String getWhoseTurnString() {
		return ColorUtil.getColorText(whoseTurn);

	}

	public int hashCode() {
		return black.hashCode() + white.hashCode();
	}

	public boolean horizontalSymmetry() {
		return horizontalSymmetry(true);
	}

	/**
	 * 棋子是否关于水平中线对称?
	 * 
	 * @return
	 */
	public boolean horizontalSymmetry(boolean sameColor) {
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
				int colorA = getColor(row, column);
				int colorB = getColor(boardSize + 1 - row, column);
				if (sameColor) {
					if (colorA != colorB) {
						return false;
					}
				} else {// black white symmetric
					if (colorA == Constant.BLANK) {
						if (colorB != Constant.BLANK) {
							return false;
						}
					} else if (ColorUtil.enemyColor(colorA) != colorB) {
						return false;
					}
				}
			}
		}
		if (log.isDebugEnabled())
			log.debug("horizontalSymmetry");
		return true;
	}

	/**
	 * index 0 of board[][] is not used.
	 * 
	 * @param board
	 */
	public void init(byte[][] board, int whoseTurn) {
		this.whoseTurn = whoseTurn;
		int length = boardSize * boardSize + 1;
		black = new BitSet(length);
		white = new BitSet(length);
		for (byte i = 1; i <= boardSize; i++) {
			for (byte j = 1; j <= boardSize; j++) {
				if (board[i][j] == ColorUtil.BLACK) {
					black.set(Point.getPoint(boardSize, i, j)
							.getOneDimensionCoordinate());
					blackStones++;
					blackRowColumnSum += (i + j);
					rowColumnSum += (i + j);
					blackRowSum += i;
					rowSum += i;

				} else if (board[i][j] == ColorUtil.WHITE) {
					white.set(Point.getPoint(boardSize, i, j)
							.getOneDimensionCoordinate());
					whiteStones++;
					rowSum += i;
					rowColumnSum += (i + j);
				}
			}
		}
	}

	public boolean isBlackTurn() {
		return this.whoseTurn == Constant.BLACK;
	}

	public boolean isWhiteTurn() {
		return this.whoseTurn == Constant.WHITE;
	}

	/**
	 * 棋盘的状态有四角八边变化。需要将其标准化。这样其他的变体可以统一对待。<br/>
	 * 黑白交换后本质上是等价的，结果和whoseTurn都改变。
	 * 
	 * @return
	 */
	public BoardColorState normalize() {
		BoardColorState state = new BoardColorState(this.boardSize,
				this.whoseTurn);

		List<BoardColorState> list = new ArrayList<BoardColorState>();

		List<BoardColorState> list2 = new ArrayList<BoardColorState>();
		// BoardColorState backSlash = new BoardColorState(this.)
		byte[][] originalState = getMatrixState();
		byte[][] verticalMirror = GoBoardSymmetry.verticalMirror(originalState);
		byte[][] horizontalMirror = GoBoardSymmetry
				.horizontalMirror(originalState);
		byte[][] horizontalVertical = GoBoardSymmetry
				.horizontalMirror(verticalMirror);
		list.add(this);
		list.add(BoardColorState.getInstance(verticalMirror, whoseTurn));
		list.add(BoardColorState.getInstance(horizontalMirror, whoseTurn));
		list.add(BoardColorState.getInstance(horizontalVertical, whoseTurn));

		list.add(BoardColorState.getInstance(
				GoBoardSymmetry.forwardSlashMirror(originalState), whoseTurn));
		list.add(BoardColorState.getInstance(
				GoBoardSymmetry.forwardSlashMirror(verticalMirror), whoseTurn));
		list.add(BoardColorState.getInstance(
				GoBoardSymmetry.forwardSlashMirror(horizontalMirror), whoseTurn));
		list.add(BoardColorState.getInstance(
				GoBoardSymmetry.forwardSlashMirror(horizontalVertical),
				whoseTurn));
		if (log.isInfoEnabled()) {
			for (BoardColorState state2 : list) {
				log.info(state2.getStateString().toString());
			}
		}

		boolean colorSwitchIncluded = list.contains(this.blackWhiteSwitch());

		Collections.sort(list, new BoardColorStateComparator());
		if (colorSwitchIncluded) {
			log.info("colorSwitchIncluded:" + colorSwitchIncluded);
			return list.get(0);
		}
		// do not switch between black and white.
		// BoardColorState standard = list.get(0);
		// BoardColorState switched = standard.blackWhiteSwitch();
		// list.clear();
		// list.add(switched);
		// list.add(standard);
		// Collections.sort(list, new BoardColorStateComparator());

		return list.get(0);
	}

	public void output() {

	}

	/**
	 * 不论奇偶路棋盘,斜的对称线总是在棋盘上.
	 * 
	 * @return
	 */
	public int pointsInBackwardSlashLine() {
		int count = 0;
		for (int row = 1; row <= boardSize; row++) {
			if (getColor(row, row) != ColorUtil.BLANK) {
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
			if (getColor(boardSize + 1 - row, row) != ColorUtil.BLANK) {
				count++;
			}
		}
		return count;
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
			if (getColor(middleLine, i) != ColorUtil.BLANK) {
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
			if (getColor(i, middleLine) != ColorUtil.BLANK) {
				count++;
			}
		}
		return count;
	}

	public boolean possibleBackwardSlashSymmetry() {
		int count = getNumberOfPoint() - pointsInBackwardSlashLine();
		return ColorUtil.evenNumberOfPoints(count);
	}

	public boolean possibleForwardSlashSymmetry() {
		int count = getNumberOfPoint() - pointsInForwardSlashLine();
		return ColorUtil.evenNumberOfPoints(count);
	}

	public boolean possibleHorizontalSymmetry() {
		int count = getNumberOfPoint() - pointsInHorizontalLine();
		return ColorUtil.evenNumberOfPoints(count);
	}

	public boolean possibleVerticalSymmetry() {
		int count = getNumberOfPoint() - pointsInVerticalMiddleLine();
		return ColorUtil.evenNumberOfPoints(count);
	}

	public void setScore(int score) {
		this.finalScore = score;
	}

	public void setVariant(int variant) {
		this.variant = variant;
	}

	
	/**
	 * 黑白子互换的局面
	 * 
	 * @return
	 */
	// public BoardColorState switchColor() {
	// BoardColorState state = new BoardColorState(this.boardSize,
	// this.whoseTurn);
	// state.black = (BitSet) this.white.clone();
	// state.white = (BitSet) this.black.clone();
	// return state;
	// }

	public String toString() {
		StringBuffer buf = new StringBuffer("BoardPointState[blackpoint=");
		buf.append(this.getBlackPoints().toString());
		buf.append(", whitePoint=");
		buf.append(this.getWhitePoints().toString());
		buf.append(", whoseturn=");
		buf.append(this.getWhoseTurnString());
		buf.append("]");
		buf.append(this.getStateString());
		return buf.toString();

	}

	public void updateWithDeadStone(Set<Point> stones) {
		for (Point stone : stones) {
			int dim = stone.getOneDimensionCoordinate();
			rowSum -= stone.getRow();
			rowColumnSum -= (stone.getRow() + stone.getColumn());
			if (black.get(dim) == true) {
				black.clear(dim);
				blackStones--;
				blackRowSum -= stone.getRow();
				blackRowColumnSum -= (stone.getRow() + stone.getColumn());
			} else if (white.get(dim) == true) {
				white.clear(dim);
				whiteStones--;
			} else {
				throw new RuntimeException("dead stones do not exist!");
			}
		}
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
				if (getColor(row, column) != getColor(row, boardSize + 1
						- column)) {
					return false;
				}
			}
		}
		if (log.isDebugEnabled())
			log.debug("verticalSymmetry");
		return true;

	}

	/**
	 * counting the score by assuming all the stones are live. used as the basis
	 * of best guess for expected number.
	 * 
	 * @return
	 */
	public int getScore_assumeAllLive() {
		return this.blackStones - this.whiteStones;
	}

}

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
		return 0;// impossible
	}
}