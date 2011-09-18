/*
 * Created on 2005-4-21
 */
package eddie.wu.domain;

import go.Board;

import java.util.HashSet;
import java.util.Set;

/**
 * @author eddie may be no use to separate out this class at the first glance.
 *         but I believe it will be a good choice with the future in the mind.
 * 
 *         FLY WEIGHT Design Pattern
 */
public class Point implements java.io.Serializable {
	/*
	 * valid scope 1-19
	 */
	private byte row;

	/*
	 * valid scope 1-19
	 */
	private byte column;

	public static Point getPoint(byte row, byte column) {
		return points[row][column];
	}

	/**
	 * tricky! convert form one dimension coordinate into two dimension
	 * coordinate.
	 * 
	 * @param point
	 */
	public static Point getPoint(int point) {
		int row = ((point - 1) / Constant.SIZEOFBOARD + 1);
		int column = ((point - 1) % Constant.SIZEOFBOARD + 1);
		return points[row][column];
	}

	public static Point getPoint(int row, int column) {
		return points[row][column];
	}

	public Point() {

	}

	public Point(byte row, byte column) {
		this.row = row;
		this.column = column;
	}

	public Point(int row, int column) {
		this.row = (byte) row;
		this.column = (byte) column;
	}

	/**
	 * tricky! convert form one dimension coordinate into two dimension
	 * coordinate.
	 * 
	 * @param point
	 */
	// public Point(short point) {
	// this((byte) ((point - 1) / Constant.SIZEOFBOARD + 1),
	// (byte) ((point - 1) % Constant.SIZEOFBOARD + 1));
	// }
	//
	// public Point(int point) {
	// this((byte) ((point - 1) / Constant.SIZEOFBOARD + 1),
	// (byte) ((point - 1) % Constant.SIZEOFBOARD + 1));
	// }

	/**
	 * @return Returns the column.
	 */
	public byte getColumn() {
		return column;
	}

	/**
	 * @param column
	 *            The column to set.
	 */
	public void setColumn(byte column) {
		this.column = column;
	}

	/**
	 * @return Returns the row.
	 */
	public byte getRow() {
		return row;
	}

	/**
	 * @param row
	 *            The row to set.
	 */
	public void setRow(byte row) {
		this.row = row;
	}

	/**
	 * from 1 to 361
	 * @return
	 */
	public short getOneDimensionCoordinate() {
		return (short) ((this.row - 1) * Constant.SIZEOFBOARD + this
				.getColumn());
	}

	public boolean equals(Object object) {
		if (object instanceof Point) {
			Point point = (Point) object;
			return point == this;
		} else
			return false;
	}

	public int hashCode() {
		return getOneDimensionCoordinate();
	}

	public String toString() {
		return "[" + row + "," + column + "]";
	}

	/**
	 * not include horizontal axis and vertical axis themselves. *
	 */
	public boolean isLeftTop() {
		if (this.getRow() <= Constant.COORDINATEOFTIANYUAN
				&& this.getColumn() < Constant.COORDINATEOFTIANYUAN) {
			return true;
		}
		return false;
	}

	public boolean isLeftBottom() {
		if (this.getRow() > Constant.COORDINATEOFTIANYUAN
				&& this.getColumn() <= Constant.COORDINATEOFTIANYUAN) {
			return true;
		}
		return false;
	}

	public boolean isRightTop() {
		if (this.getRow() < Constant.COORDINATEOFTIANYUAN
				&& this.getColumn() >= Constant.COORDINATEOFTIANYUAN) {
			return true;
		}
		return false;
	}

	public boolean isRightBottom() {
		if (this.getRow() >= Constant.COORDINATEOFTIANYUAN
				&& this.getColumn() > Constant.COORDINATEOFTIANYUAN) {
			return true;
		}
		return false;
	}

	public boolean isValid() {
		if (this.row < 1 || this.row > 19 || this.column > 19
				|| this.column < 1)
			return false;
		else
			return true;
	}

	public boolean isNotValid() {
		return !isValid();
	}

	public static Set<Point> zhanJiao;

	public static Set<Point> xing;// 星
	public static Set<Point> xiaomu;// 小目
	public static Set<Point> muwai;// 目外
	public static Set<Point> gaomu;// 高目
	public static Set<Point> sansan;// 三三
	public static Point[][] points = new Point[Board.BOARD_SIZE + 2][Board.BOARD_SIZE + 2];

	public Set<Point> getReflection() {
		Point point = this;
		int row = point.getRow();
		int col = point.getColumn();
		Set<Point> points = new HashSet<Point>();
		points.add(point);
		points.add(Point.getPoint(row, Board.BOARD_SIZE + 1 - col));
		points.add(Point.getPoint(Board.BOARD_SIZE + 1 - row, col));
		points.add(Point.getPoint(Board.BOARD_SIZE + 1 - row, Board.BOARD_SIZE
				+ 1 - col));

		col = point.getRow();
		row = point.getColumn();
		points.add(Point.getPoint(row, col));
		points.add(Point.getPoint(row, Board.BOARD_SIZE + 1 - col));
		points.add(Point.getPoint(Board.BOARD_SIZE + 1 - row, col));
		points.add(Point.getPoint(Board.BOARD_SIZE + 1 - row, Board.BOARD_SIZE
				+ 1 - col));
		return points;
	}

	static {
		for (int i = 1; i <= Board.BOARD_SIZE; i++) {
			for (int j = 1; j <= Board.BOARD_SIZE; j++) {
				points[i][j] = new Point(i, j);
			}
		}

		xing = Point.getPoint(4, 4).getReflection();
		xiaomu = Point.getPoint(3, 4).getReflection();
		gaomu = Point.getPoint(4, 5).getReflection();
		muwai = Point.getPoint(3, 5).getReflection();
		sansan = Point.getPoint(3, 3).getReflection();
	}

	public int getMinCoordinate() {
		if (row <= column)
			return row;
		else
			return column;
	}

	public int getMaxCoordinate() {
		if (row <= column)
			return column;
		else
			return row;
	}

	/**
	 * 棋子在几线上？取低的。
	 * @return
	 */
	public int getMinLine() {
		int row;
		int column;
		if (this.row > 10)
			row = 20 - this.row;
		else
			row = this.row;
		if (this.column > 10)
			column = 20 - this.column;
		else
			column = this.column;
		if (row <= column)
			return row;
		else
			return column;
	}
	
	/**
	 * 棋子在几线上？取高的。
	 * @return
	 */
	
	public int getMaxLine() {
		int row;
		int column;
		if (this.row > 10)
			row = 20 - this.row;
		else
			row = this.row;
		if (this.column > 10)
			column = 20 - this.column;
		else
			column = this.column;
		if (row <= column)
			return column;
		else
			return row;
	}
}
