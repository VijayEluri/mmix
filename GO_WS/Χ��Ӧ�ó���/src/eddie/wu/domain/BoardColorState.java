/*
 * Created on 2005-4-22
 *


 */
package eddie.wu.domain;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import eddie.wu.domain.comp.RowColumnComparator;

/**
 * The most basic way to represent the state of the board without derived
 * information. it could be converted to classic matrix representation.
 * 
 * @author eddie
 * 
 *         use Bit Set to represent the board Point sate! index start from 1.
 */
public class BoardColorState {
	private static final Logger log = Logger.getLogger(BoardColorState.class);
	public final int boardSize;
	/**
	 * index 0 is not used.
	 */
	private BitSet black;

	private BitSet white;

	public BoardColorState(int boardSize) {
		this.boardSize = boardSize;
		int length = boardSize * boardSize + 1;
		black = new BitSet(length);
		white = new BitSet(length);
	}

	/**
	 * index 0 of board[][] is not used.
	 * 
	 * @param board
	 */
	public BoardColorState(byte[][] board) {
		boardSize = board.length - 2;
		for (byte i = 1; i <= boardSize; i++) {
			for (byte j = 1; j <= boardSize; j++) {
				if (board[i][j] == ColorUtil.BLACK) {
					black.set(Point.getPoint(boardSize, i, j)
							.getOneDimensionCoordinate());
				} else if (board[i][j] == ColorUtil.WHITE) {
					white.set(Point.getPoint(boardSize, i, j)
							.getOneDimensionCoordinate());
				}
			}
		}
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

	public Set<Point> getBlackPoints() {
		Set<Point> blackPoints = new HashSet<Point>(128);
		for (short i = 1; i <= boardSize * boardSize; i++) {
			if (black.get(i))
				blackPoints.add(Point.getPointFromOneDim(boardSize, i));
		}
		return blackPoints;
	}

	public Set<Point> getWhitePoints() {
		Set<Point> whitePoints = new HashSet<Point>(128);
		for (short i = 1; i <= boardSize * boardSize; i++) {
			if (white.get(i))
				whitePoints.add(Point.getPointFromOneDim(boardSize, i));
		}
		return whitePoints;
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

	public void add(BoardPoint point) {
		if (point.getColor() == ColorUtil.BLACK) {
			black.set(point.getOneDimensionCoordinate());
		} else if (point.getColor() == ColorUtil.WHITE) {
			white.set(point.getOneDimensionCoordinate());
		}
	}

	public void add(Point point, int color) {
		if (color == ColorUtil.BLACK) {
			black.set(point.getOneDimensionCoordinate());
		} else if (color == ColorUtil.WHITE) {
			white.set(point.getOneDimensionCoordinate());
		}
	}

	public void remove(BoardPoint point) {
		if (point.getColor() == ColorUtil.BLACK) {
			black.set(point.getOneDimensionCoordinate(), false);
		} else if (point.getColor() == ColorUtil.WHITE) {
			white.set(point.getOneDimensionCoordinate(), false);
		}

	}

	/**
	 * @param points
	 *            Set of BoardPoint
	 */
	public void remove(Set<BoardPoint> points) {
		for (Iterator<BoardPoint> iter = points.iterator(); iter.hasNext();) {
			remove(iter.next());
		}
	}

	public boolean equals(Object object) {
		if (object instanceof BoardColorState) {
			BoardColorState boardState = (BoardColorState) object;
			return this.black.equals(boardState.black)
					&& this.white.equals(boardState.white);
		}
		return false;
	}

	public int hashCode() {
		return black.hashCode() + white.hashCode();
	}

	// public String toString(){
	// return black.toString()+":"+white.toString();
	// }

	public String toString() {
		StringBuffer buf = new StringBuffer("BoardPointState[blackpoint=");
		buf.append(this.getBlackPoints().toString());
		buf.append(", whitePoint=");
		buf.append(this.getWhitePoints().toString());
		buf.append("]");
		return buf.toString();

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

	}
}