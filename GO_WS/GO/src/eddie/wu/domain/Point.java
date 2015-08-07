/*
 * Created on 2005-4-21
 */
package eddie.wu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eddie.wu.domain.analy.StateAnalysis;
import eddie.wu.domain.comp.SymmetryRowColumnComparator;

/**
 * @author eddie may be no use to separate out this class at the first glance.
 *         but I believe it will be a good choice with the future in the mind.
 * 
 *         FLY WEIGHT Design Pattern 享元设计模式
 */
public class Point implements java.io.Serializable {
	public static Point giveUp = null;
	// public static Point absLoopThreat = new Point()

	public static Set<Point> zhanJiao;
	public static Point XING;
	public static Set<Point> xings;// 星
	public static Point XIAOMU;
	public static Set<Point> xiaomus;// 小目
	public static Point MUWAI;
	public static Set<Point> muwais;// 目外
	public static Point GAOMU;
	public static Set<Point> gaomus;// 高目
	public static Point SANSAN;
	public static Set<Point> sansans;// 三三
	// public static

	private static Map<Integer, Point[][]> pointsMap = new HashMap<Integer, Point[][]>();
	/*
	 * valid scope from 1 to boardSize
	 */
	private final byte row;

	/*
	 * valid scope from 1 to boardSize
	 */
	private final byte column;

	public final byte boardSize;
	public final byte middleLine;

	// public static Point getPoint(int boardSize, int row, int column) {
	// if (Constant.DEBUG_CGCL == true) {
	//
	// }
	// if (pointsMap.get(Integer.valueOf(boardSize)) == null) {
	// initAllPoints(boardSize);
	// }
	// return pointsMap.get(Integer.valueOf(boardSize))[row][column];
	// }

	/**
	 * 该点作为眼位是否为中央眼位（这里二线也算中央。）
	 */
	public boolean isCenterEye() {
		Point p = this.normalize();
		return p.getRow() != 1 && p.getColumn() != 1;
	}

	/**
	 * 是否在角上
	 * 
	 * @return
	 */
	public boolean isCornerEye() {
		Point p = this.normalize();
		return p.getRow() == 1 && p.getColumn() == 1;
	}

	/**
	 * 是否在边线上
	 * 
	 * @return
	 */
	public boolean isBorderEye() {
		Point p = this.normalize();
		return p.getRow() == 1 && p.getColumn() != 1;
	}

	public boolean nearConer() {
		Point temp = this.normalize();
		return temp.getRow() <= 5 && temp.getColumn() <= 5;
	}

	/**
	 * 是否在棋盘的边,2到5线(3和4线是最正宗的边).相对中央而言. 有"金角银边草肚皮"之说.
	 * 
	 * @return
	 */
	public boolean nearBorder() {
		Point p = this.normalize();
		return p.getRow() >= 2 && p.getRow() <= 5 && p.getColumn() > 5;
	}

	/**
	 * tricky! convert form one dimension coordinate into two dimension
	 * coordinate.
	 * 
	 * @param point
	 */
	public static Point getPointFromOneDim(int boardSize, int point) {
		int row = ((point - 1) / boardSize + 1);
		int column = ((point - 1) % boardSize + 1);
		if (pointsMap.get(Integer.valueOf(boardSize)) == null) {
			initAllPoints(boardSize);
		}

		return pointsMap.get(Integer.valueOf(boardSize))[row][column];
	}

	public static Point getPoint(int row, int column) {
		int boardSize = Constant.BOARD_SIZE;
		if (pointsMap.get(Integer.valueOf(boardSize)) == null) {
			initAllPoints(boardSize);
		}
		return pointsMap.get(Integer.valueOf(boardSize))[row][column];
	}

	public static Point getPoint(int boardSize, int row, int column) {
		if (Point.isNotValid(boardSize, row, column))
			throw new RuntimeException("invalid Point [" + boardSize + ","
					+ row + "," + column + "]");
		if (pointsMap.get(Integer.valueOf(boardSize)) == null) {
			initAllPoints(boardSize);
		}
		return pointsMap.get(Integer.valueOf(boardSize))[row][column];
	}

	private Point(int boardSize, int row, int column) {
		this.boardSize = (byte) boardSize;
		this.middleLine = (byte) ((boardSize + 2) / 2);
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
	// public void setColumn(byte column) {
	// this.column = column;
	// }

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
	// public void setRow(byte row) {
	// this.row = row;
	// }

	/**
	 * from 1 to 361
	 * 
	 * @return
	 */
	public int getOneDimensionCoordinate() {
		return getOneDimension(boardSize, row, column);
	}

	/**
	 * from 1 to 361
	 * 
	 * @return
	 */
	public static int getOneDimension(int boardSize, int row, int column) {
		return ((row - 1) * boardSize + column);
	}

	/**
	 * BUG FIX comments:<br/>
	 * even the singleton pattern is used, it is not enough to ensure the
	 * uniqueness. in the application, clone is used, and it will create another
	 * Point instance with equal [row, column]<br/>
	 * change: no more clone.
	 */
	public boolean equals(Object object) {
		if (object instanceof Point == false) {
			return false;
		}
		Point point = (Point) object;
		if (point == this) {
			return true;
		} else {
			return this.row == point.row && this.column == point.column;
		}
	}

	/**
	 * just ensure equal objects has same hashCode.
	 */
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
		if (this.getRow() <= middleLine && this.getColumn() < middleLine) {
			return true;
		}
		return false;
	}

	public boolean isLeftBottom() {
		if (this.getRow() > middleLine && this.getColumn() <= middleLine) {
			return true;
		}
		return false;
	}

	public boolean isRightTop() {
		if (this.getRow() < middleLine && this.getColumn() >= middleLine) {
			return true;
		}
		return false;
	}

	public boolean isRightBottom() {
		if (this.getRow() >= middleLine && this.getColumn() > middleLine) {
			return true;
		}
		return false;
	}

	public static boolean isValid(int boardSize, int row, int column) {
		if (row < 1 || row > boardSize || column > boardSize || column < 1)
			return false;
		else
			return true;
	}

	public static boolean isNotValid(int boardSize, int row, int column) {
		return !isValid(boardSize, row, column);
	}

	public Set<Point> getReflection(int boardSize) {
		Point point = this;
		int row = point.getRow();
		int col = point.getColumn();
		Set<Point> points = new HashSet<Point>();
		points.add(point);
		points.add(Point.getPoint(boardSize, row, boardSize + 1 - col));
		points.add(Point.getPoint(boardSize, boardSize + 1 - row, col));
		points.add(Point.getPoint(boardSize, boardSize + 1 - row, boardSize + 1
				- col));

		col = point.getRow();
		row = point.getColumn();
		points.add(Point.getPoint(boardSize, row, col));
		points.add(Point.getPoint(boardSize, row, boardSize + 1 - col));
		points.add(Point.getPoint(boardSize, boardSize + 1 - row, col));
		points.add(Point.getPoint(boardSize, boardSize + 1 - row, boardSize + 1
				- col));
		return points;
	}

	// private static S

	public static Set<Point> getAllPoints(int boardSize) {
		initAllPoints(boardSize);
		return allPointMap.get(boardSize);
	}

	public static Map<Integer, Set<Point>> allPointMap = new HashMap<Integer, Set<Point>>();

	static void initAllPoints(int boardSize) {
		Point[][] points = new Point[boardSize + 2][boardSize + 2];
		Set<Point> allPoints = new HashSet<Point>();
		for (int i = 1; i <= boardSize; i++) {
			for (int j = 1; j <= boardSize; j++) {
				points[i][j] = new Point(boardSize, i, j);
				allPoints.add(points[i][j]);
				pointsMap.put(boardSize, points);
				allPointMap.put(Integer.valueOf(boardSize), allPoints);
			}
		}
		if (boardSize < 9)
			return;
		zhanJiao = new HashSet<Point>();
		XING = Point.getPoint(boardSize, 4, 4);
		xings = XING.getReflection(boardSize);
		zhanJiao.addAll(xings);
		XIAOMU = Point.getPoint(boardSize, 3, 4);
		xiaomus = XIAOMU.getReflection(boardSize);
		zhanJiao.addAll(xiaomus);
		GAOMU = Point.getPoint(boardSize, 4, 5);
		gaomus = GAOMU.getReflection(boardSize);
		zhanJiao.addAll(gaomus);
		MUWAI = Point.getPoint(boardSize, 3, 5);
		muwais = MUWAI.getReflection(boardSize);
		zhanJiao.addAll(muwais);
		SANSAN = Point.getPoint(boardSize, 3, 3);
		sansans = SANSAN.getReflection(boardSize);
		zhanJiao.addAll(sansans);
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
	 * 
	 * @return
	 */
	public int getMinLine() {
		return this.normalize().getRow();
	}

	/**
	 * 棋子在几线上？取高的。
	 * 
	 * @return
	 */

	public int getMaxLine() {
		return this.normalize().getColumn();
	}

	public boolean isDiagonal(Point other) {
		if (Math.abs(this.getRow() - other.getRow()) == 1
				&& Math.abs(this.getColumn() - other.getColumn()) == 1) {
			return true;
		}
		return false;
	}

	public boolean isSameline(Point other) {
		if (this.getRow() == other.getRow()
				|| this.getColumn() == other.getColumn()) {
			return true;
		}
		return false;
	}

	/**
	 * 将点[4,16]规范为[4,4],易于识别出是星位。 [5,3]规范为[3,5],row<=column
	 * 
	 * @return
	 */
	public Point normalize() {
		int row, column, middle;
		middle = (boardSize + 1) / 2;
		if (this.getRow() > middle) {
			row = boardSize + 1 - this.row;
		} else {
			row = this.row;
		}
		if (this.getColumn() > middle) {
			column = boardSize + 1 - this.column;
		} else {
			column = this.column;
		}
		if (row <= column)
			return Point.getPoint(boardSize, row, column);
		else
			return Point.getPoint(boardSize, column, row);
	}

	/**
	 * 
	 * @param delta
	 * @return null if target is out of board.
	 */
	public Point getNeighbour(Delta delta) {
		int row = this.row + delta.getRowDelta();
		int column = this.column + delta.getColumnDelta();
		if (Point.isValid(boardSize, row, column)) {
			return Point.getPoint(boardSize, row, column);
		} else
			return null;

	}

	/**
	 * 从大棋盘转化到知识表达的小棋盘（反向转换）。不检查点的合法性。
	 * 
	 * @param delta
	 * @return
	 */
	public Point getNeighbourReverse(Delta delta) {
		int row = this.row - delta.getRowDelta();
		int column = this.column - delta.getColumnDelta();

		return Point.getPoint(boardSize, row, column);

	}

	public Delta getDelta(Point other) {
		return Delta.getDelta(row - other.row, column - other.column);
	}

	public Delta getAbsDelta(Point other) {
		int a, b;
		if (row < other.row) {
			a = other.row - row;
		} else {
			a = row - other.row;
		}
		if (column < other.column) {
			b = other.column - column;
		} else {
			b = column - other.column;
		}
		return Delta.getDelta(a, b);
	}

	public Point getNeighbour(int rowDelta, int columnDelta) {
		int row = this.row + rowDelta;
		int column = this.column + columnDelta;
		if (Point.isValid(boardSize, row, column)) {
			return Point.getPoint(boardSize, row, column);
		} else
			return null;

	}

	public void getColor(StateAnalysis a) {
		a.getColor(this);
	}

	/**
	 * refer to GoBoardSymmetry
	 * 
	 * @return
	 */
	public Point horizontalMirror() {

		int row = boardSize + 1 - getRow();
		int column = getColumn();
		return Point.getPoint(boardSize, row, column);
	}

	public Point verticalMirror() {
		int row = getRow();
		int column = boardSize + 1 - getColumn();
		return Point.getPoint(boardSize, row, column);
	}

	public Point forwardSlashMirror() {
		int row = boardSize + 1 - getColumn();
		int column = boardSize + 1 - getRow();
		return Point.getPoint(boardSize, row, column);
	}

	public Point backwardSlashMirror() {
		int row = getColumn();
		int column = getRow();
		return Point.getPoint(boardSize, row, column);
	}

	public Point convert(SymmetryResult operation) {
		if (operation.isBackwardSlashSymmetry()) {
			return backwardSlashMirror();
		}
		if (operation.isForwardSlashSymmetry()) {
			return forwardSlashMirror();
		}
		if (operation.isHorizontalSymmetry()) {
			return horizontalMirror();
		}
		if (operation.isVerticalSymmetry()) {
			return verticalMirror();
		}
		return this;
	}

	/**
	 * the lesser of value, the higher of priority.
	 * 
	 * @return
	 */
	public int getPriorityByLine() {
		int line = this.getMinLine();
		if (line > 3)
			return line - 3;
		else if (line == 3)
			return 0;
		else
			return 3 - line;
	}

	public int getRowColSum() {
		return row + column;
	}

	public Point normalize(SymmetryResult symmetryResult) {
		return deNormalize(symmetryResult).get(0);
	}

	/**
	 * brute force (ugly) implementation. 展开得到所有对称的候选点.
	 * 
	 * @param point
	 * @param symmetryResult
	 * @return all the symmetric points
	 */
	public List<Point> deNormalize(SymmetryResult symmetryResult) {
		Point point = this;
		List<Point> list = new ArrayList<Point>();
		int numberOfSymmetry = symmetryResult.getNumberOfSymmetry();
		Point horizontalMirror = point.horizontalMirror();
		Point verticalMirror = point.verticalMirror();
		if (numberOfSymmetry == 4) {
			list.add(horizontalMirror);
			list.add(verticalMirror);
			list.add(horizontalMirror.verticalMirror());
			list.add(point);
			List<Point> list2 = new ArrayList<Point>();
			list2.addAll(list);
			for (Point temp : list2) {
				// allow duplicates in list.
				list.add(temp.backwardSlashMirror());
				list.add(temp.forwardSlashMirror());
			}
		} else if (numberOfSymmetry == 2) {
			if (symmetryResult.isHorizontalSymmetry()) {
				list.add(horizontalMirror);
			}
			if (symmetryResult.isVerticalSymmetry()) {
				list.add(verticalMirror);
			}
			if (symmetryResult.isForwardSlashSymmetry()) {
				list.add(point.forwardSlashMirror());
			}
			if (symmetryResult.isBackwardSlashSymmetry()) {
				list.add(point.backwardSlashMirror());
			}

			/**
			 * get the combination of conversion; second conversion is done. now
			 * first // conversion.
			 */
			// if (list.size() < 2) {
			// log.debug(list);
			// } else {
			Point pointA = list.get(1);
			if (symmetryResult.isHorizontalSymmetry()) {
				list.add(pointA.horizontalMirror());
			} else if (symmetryResult.isVerticalSymmetry()) {
				list.add(pointA.verticalMirror());
			} else if (symmetryResult.isForwardSlashSymmetry()) {
				list.add(pointA.forwardSlashMirror());
			} else if (symmetryResult.isBackwardSlashSymmetry()) {
				list.add(pointA.backwardSlashMirror());
			}
			// }
			list.add(point);

		} else if (numberOfSymmetry == 1) {
			if (symmetryResult.isHorizontalSymmetry()) {
				list.add(horizontalMirror);
			} else if (symmetryResult.isVerticalSymmetry()) {
				list.add(verticalMirror);
			} else if (symmetryResult.isForwardSlashSymmetry()) {
				list.add(point.forwardSlashMirror());
			} else if (symmetryResult.isBackwardSlashSymmetry()) {
				list.add(point.backwardSlashMirror());
			}
			list.add(point);
		}

		Collections.sort(list, new SymmetryRowColumnComparator());
		return list;
	}

}
