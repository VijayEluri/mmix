package eddie.wu.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * 为了判断棋子之间的相对位置，即它们之间的接近方式，需要按照肩冲，小飞之类的位置来查找。
 * 这些位置由两子之间的row，column相对变化（Delta）来定义。
 * @author wueddie-wym-wrz
 *
 */
public class Delta {
	private byte rowDelta;
	private byte columnDelta;

	public Delta(int rowDelta, int columnDelta) {
		this.rowDelta = (byte) rowDelta;
		this.columnDelta = (byte) columnDelta;
	}

	public byte getRowDelta() {
		return rowDelta;
	}

	public void setRowDelta(int rowDelta) {
		this.rowDelta = (byte) rowDelta;
	}

	public byte getColumnDelta() {
		return columnDelta;
	}

	public void setColumnDelta(int columnDelta) {
		this.columnDelta = (byte) columnDelta;
	}

	@Override
	public int hashCode() {
		return (this.rowDelta + 19) * 31 + (this.columnDelta + 19);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Delta) {
			Delta delta = (Delta) obj;
			return this.rowDelta == delta.rowDelta
					&& this.columnDelta == delta.columnDelta;
		} else {
			return false;
		}

	}

	public Delta topBottomSwitch() {
		return new Delta(0 - this.rowDelta, this.columnDelta);
	}

	public Delta leftRightSwitch() {
		return new Delta(this.rowDelta, 0 - this.columnDelta);
	}

	public Delta diagonalSwitch() {
		return new Delta(this.columnDelta, this.rowDelta);
	}

	/**
	 * 衍生出接近方式（比如小飞）判断用的常数。
	 * 
	 * @return
	 */
	public static List<Delta> getAppoachWays(Delta point) {
		Set<Delta> ways = new HashSet<Delta>();

		ways.add(point);
		Delta bottom = point.topBottomSwitch();
		ways.add(bottom);
		ways.add(bottom.leftRightSwitch());
		ways.add(point.leftRightSwitch());

		point = point.diagonalSwitch();
		ways.add(point);
		bottom = point.topBottomSwitch();
		ways.add(bottom);
		ways.add(bottom.leftRightSwitch());
		ways.add(point.leftRightSwitch());
		List<Delta> list = new ArrayList<Delta>();
		list.addAll(ways);
		return list;
	}

	/**
	 * 
	 * @param i
	 * @param j
	 * @return
	 */
	public static Delta getDelta(int i, int j) {
		return new Delta(i, j);
	}

	public String toString() {
		return "[" + rowDelta + "," + columnDelta + "]";
	}

	/**
	 * normalize to positive value and rowDelta>=columnDelta
	 */
	public Delta normalize() {
		int rowDelta = 0, columnDelta = 0;
		if (this.rowDelta < 0) {
			rowDelta = 0 - this.rowDelta;
		} else {
			rowDelta = this.rowDelta;
		}
		if (this.columnDelta < 0) {
			columnDelta = 0 - this.columnDelta;
		} else {
			columnDelta = this.columnDelta;
		}
		if (rowDelta >= columnDelta) {
			return new Delta(rowDelta, columnDelta);
		} else {
			return new Delta(columnDelta, rowDelta);
		}
	}
}
