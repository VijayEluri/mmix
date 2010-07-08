/*
 * Created on 2005-4-21
 */
package eddie.wu.domain;

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

	private static Point[][] points = new Point[21][21];
	static {
		byte i, j;
		for (i = 1; i < 20; i++) {
			for (j = 1; j < 20; j++) {
				points[i][j] = new Point(i, j);
			}
		}
	}

	public static Point getPoint(byte row, byte column) {
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
	public Point(short point) {
		this((byte) ((point - 1) / Constant.SIZEOFBOARD + 1),
				(byte) ((point - 1) % Constant.SIZEOFBOARD + 1));
	}

	public Point(int point) {
		this((byte) ((point - 1) / Constant.SIZEOFBOARD + 1),
				(byte) ((point - 1) % Constant.SIZEOFBOARD + 1));
	}

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
	 * 
	 * @return
	 */
	public short getOneDimensionCoordinate() {
		return (short) ((this.row - 1) * Constant.SIZEOFBOARD + this
				.getColumn());
	}

	public boolean equals(Object object) {
		if (object instanceof Point) {
			Point point = (Point) object;
			return this.row == point.row && this.column == point.column;
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
}