package eddie.wu.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 为了判断棋子之间的相对位置，即它们之间的接近方式，需要按照肩冲，小飞之类的位置来查找。
 * 这些位置由两子之间的row，column相对变化（Delta）来定义。
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class Delta {
	/**
	 * 同色子称为小尖,异色子则称为肩冲.
	 */
	private static final Delta DELTA_SHOULDER = Delta.getDelta("肩冲/小尖",1, 1);

	private byte rowDelta;
	private byte columnDelta;
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Delta(int rowDelta, int columnDelta) {
		this.rowDelta = (byte) rowDelta;
		this.columnDelta = (byte) columnDelta;
	}

	public Delta(String name, int rowDelta, int columnDelta) {
		this.name = name;
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
		return (this.rowDelta + Constant.BOARD_SIZE) * 31 + (this.columnDelta + Constant.BOARD_SIZE);
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
		return new Delta(this.name,0 - this.rowDelta, this.columnDelta);
	}

	public Delta leftRightSwitch() {
		return new Delta(this.name,this.rowDelta, 0 - this.columnDelta);
	}

	public Delta diagonalSwitch() {
		return new Delta(this.name,this.columnDelta, this.rowDelta);
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
	
	public static Delta getDelta(String name, int i, int j) {
		return new Delta(name, i, j);
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

	public int squareDistance() {
		return this.rowDelta * rowDelta + columnDelta * columnDelta;
	}

	public boolean isShoulder() {
		if (this.normalize().equals(DELTA_SHOULDER))
			return true;
		return false;
	}

}
