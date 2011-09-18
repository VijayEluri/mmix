package eddie.wu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is the abstraction of Block, which is a group of stones connected
 * together. it could also be the blank point in the board, such as big eyes,
 * shared breath between two blocks.
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class BasicBlock {
	/* general/common attribute */
	protected byte color;// 棋块颜色

	protected Set<Point> allPoints = new HashSet<Point>();// 棋块子点集合

	public BasicBlock() {

	}

	public BasicBlock(byte color) {
		this.color = color;
	}

	public void addPoint(Point Point) {
		allPoints.add(Point);
	}

	public void addPoint(BasicBlock block) {
		allPoints.addAll(block.getAllPoints());
	}

	public boolean removePoint(Point point) {
		return allPoints.remove(point);
	}

	/**
	 * @return Returns the allPoints.
	 */
	public Set<Point> getAllPoints() {
		return allPoints;
	}

	/**
	 * @param allPoints
	 *            The allPoints to set.
	 */
	public void setAllPoints(Set<Point> allPoints) {
		this.allPoints = allPoints;
	}

	/**
	 * @return Returns the color.
	 */
	public byte getColor() {
		return color;
	}

	/**
	 * @param color
	 *            The color to set.
	 */
	public void setColor(byte color) {
		this.color = color;
		// for(Iterator iter=allPoints.iterator();iter.hasNext();){
		// ((BoardPoint)iter.next()).setColor(ColorUtil.BLANK_POINT);
		// }
	}

	public Point getTopLeftPoint() {
		List<Point> list = new ArrayList<Point>(allPoints.size());
		list.addAll(this.allPoints);
		Collections.sort(list, new PointComparator());
		return list.get(0);
	}

	public Point getBehalfPoint() {
		return (Point) allPoints.iterator().next();
	}

	public String toString() {
		StringBuffer temp = new StringBuffer("[color=");
		temp.append(color);
		temp.append(",points=" + this.getTotalNumberOfPoint());
		temp.append(",allPoints=");
		temp.append(allPoints);
		temp.append("]");
		return temp.toString();
	}

	public short getTotalNumberOfPoint() {
		return (short) this.allPoints.size();
	}
}

class PointComparator implements Comparator<Point> {

	@Override
	public int compare(Point o1, Point o2) {
		int a = o1.getRow() + o1.getColumn();
		int b = o2.getRow() + o2.getColumn();
		if (a == b) {
			if (o1.getRow() < o2.getRow()) {// top one first.
				return 1;
			} else {
				return -1;
			}
		} else {
			return a - b;
		}

	}

}

class RowColumnComparator implements Comparator<Point> {
	@Override
	public int compare(Point o1, Point o2) {
		if (o1.getRow() == o2.getRow()) {
			return o1.getColumn() - o2.getColumn();
		} else {
			return o1.getRow() - o2.getRow();
		}

	}

}
