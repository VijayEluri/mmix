/*
 * Created on 2005-4-22
 *


 */
package eddie.wu.domain;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

<<<<<<< HEAD
import eddie.wu.domain.BoardPoint;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Point;
=======
import eddie.wu.linkedblock.BoardPoint;
import eddie.wu.linkedblock.ColorUtil;
>>>>>>> 3d8aa49ce83f747c9170d697ba2d051c700809f6

/**
 * The most basic way to represent the state of the board without derived
 * information. it could be converted to classic matrix representation.
 * 
 * @author eddie
 * 
 *         use Bit Set to represent the board Point sate! index start from 1.
 */
public class BoardColorState {

	/*
	 * index 0 is not used.
	 */
	private BitSet black = new BitSet(362);

	private BitSet white = new BitSet(362);

	public BoardColorState() {

	}

	public BoardColorState(byte[][] board) {
		for (byte i = 1; i <= 19; i++) {
			for (byte j = 1; j <= 19; j++) {
				if (board[i][j] == ColorUtil.BLACK) {
					black.set( Point.getPoint(i, j).getOneDimensionCoordinate());
				} else if (board[i][j] == ColorUtil.WHITE) {
					white.set(Point.getPoint(i, j).getOneDimensionCoordinate());
				}
			}
		}
	}

	public byte[][] getMatrixState() {
		byte[][] matrix = new byte[21][21];
		Point point;
		for (short i = 1; i <= 361; i++) {
			if (black.get(i)) {
				point = Point.getPoint(i);
				matrix[point.getRow()][point.getColumn()] = ColorUtil.BLACK;
			} else if (white.get(i)) {
				point = Point.getPoint(i);
				matrix[point.getRow()][point.getColumn()] = ColorUtil.WHITE;
			}
		}
		return matrix;
	}

	public Set<Point> getBlackPoints() {
		Set<Point> blackPoints = new HashSet<Point>(128);
		for (short i = 1; i <= 361; i++) {
			if (black.get(i))
				blackPoints.add(Point.getPoint(i));
		}
		return blackPoints;
	}

	public Set<Point> getWhitePoints() {
		Set<Point> whitePoints = new HashSet<Point>(128);
		for (short i = 1; i <= 361; i++) {
			if (white.get(i))
				whitePoints.add(Point.getPoint(i));
		}
		return whitePoints;
	}

	public Set<Point> getBlankPoints() {
		BitSet blank = (BitSet) black.clone();
		black.andNot(white);
		Set<Point> blankPoints = new HashSet<Point>(128);
		for (short i = 1; i <= 361; i++) {
			if (blank.get(i))
				blankPoints.add(Point.getPoint(i));
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
	public void remove(Set points) {
		for (Iterator iter = points.iterator(); iter.hasNext();) {
			remove((BoardPoint) iter.next());
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
}