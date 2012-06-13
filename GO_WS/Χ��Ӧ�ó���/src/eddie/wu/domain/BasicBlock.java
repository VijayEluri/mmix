package eddie.wu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import eddie.wu.arrayblock.GoApplet;
import eddie.wu.domain.comp.RowColumnComparator;
import static eddie.wu.domain.survive.SmallEye.*;

/**
 * This is the abstraction of Block, which is a group of stones connected
 * together. it could also be the blank point in the board, such as big eyes,
 * shared breath between two blocks. <br/>
 * 棋块的概念就是连接在一起的黑子和白子。<br/>
 * 气块是对这一概念的简单扩展，但是气块可能和白块，黑块都相邻；而黑块只能和白块相邻。<br/>
 * 气块如果仅仅和一个颜色的棋块（可能有多个）相邻，那么他就是一个眼。否则就是公气。。<br/>
 * 棋块是最基本的单位，因为一个子也被看成是一个棋块。<br/>
 * 棋块有所有敌方棋块的链表。<br/>
 * 气块有四周所有棋块的链表<br/>
 * 
 * @author wueddie-wym-wrz
 * 
 */
public abstract class BasicBlock {
	protected static final Logger log = Logger.getLogger(BasicBlock.class);

	/**
	 * 棋块/气块颜色.也用于区别棋块和棋块<br/>
	 * general/common attribute
	 */
	protected byte color;

	/**
	 * // 棋块子点集合或者气块子点的集合.
	 */
	protected Set<Point> allPoints = new HashSet<Point>();
	
	
	private boolean calculated;
	

	// public BasicBlock() {
	//
	// }

	public BasicBlock(int color) {
		this.color = (byte) color;
	}

	public void addPoint(Point Point) {
		allPoints.add(Point);
	}

	public boolean removePoint(Point point) {
		return allPoints.remove(point);
	}

	/**
	 * @return Returns the allPoints.
	 */
	public Set<Point> getPoints() {
		return allPoints;
	}

	/**
	 * @param allPoints
	 *            The allPoints to set.
	 */
	public void setPoints(Set<Point> allPoints) {
		this.allPoints = allPoints;
	}

	/**
	 * @return Returns the color.
	 */
	public int getColor() {
		return color;
	}

	public int getEnemyColor() {
		return ColorUtil.enemyColor(color);

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
		if (list.isEmpty()) {
			if(log.isDebugEnabled()) log.debug("point list is empty!");
			throw new RuntimeException("point list is empty!");
		}
		return list.get(0);
	}

	public Point getBehalfPoint() {

		return this.getTopLeftPoint();
	}

	public Point getUniquePoint() {
		if (allPoints.size() != 1) {
			throw new RuntimeException("allPoints.size()" + allPoints.size());
		} else {
			return allPoints.iterator().next();
		}
	}

	public String toString() {
		StringBuffer temp = new StringBuffer("[color=");
		temp.append(color);
		temp.append(",points=" + this.getNumberOfPoint());
		temp.append(",allPoints=");
		temp.append(allPoints);
		temp.append("]");
		return temp.toString();
	}

	public int getNumberOfPoint() {
		return this.allPoints.size();
	}

	public boolean isBlack() {
		return getColor() == Constant.BLACK;
	}

	public boolean isBlank() {
		return getColor() == Constant.BLANK;
	}

	public boolean isWhite() {
		return getColor() == Constant.WHITE;
	}

	/**
	 * get the profile rectangle.
	 * 
	 * @return
	 */
	public Shape getShape() {
		return Shape.getShape(this.getPoints());
	}

	public static boolean equals(BasicBlock a, BasicBlock other) {
		if (a instanceof Block && other instanceof Block) {
			return Block.equals((Block) a, (Block) other);
		} else if (a instanceof BlankBlock && other instanceof BlankBlock) {
			return BlankBlock.equals((BlankBlock) a, (BlankBlock) other);
		} else {
			return false;
		}

	}

	/**
	 * 
	 * @param shape
	 * @return 根据该shape四个顶点是否为气块的一部分。返回计数。
	 */
	public List<Point> getShapeCorners(Shape shape) {
		List<Point> corners = new ArrayList<Point>();
		int count = 0;
		int boardSize = this.allPoints.iterator().next().boardSize;
		Point point;
		point = Point.getPoint(boardSize,shape.getMinX(), shape.getMinY());
		if (allPoints.contains(point)) {
			corners.add(point);
			count++;
		}
		point = Point.getPoint(boardSize,shape.getMinX(), shape.getMaxY());
		if (allPoints.contains(point)) {
			corners.add(point);
			count++;
		}
		point = Point.getPoint(boardSize,shape.getMaxX(), shape.getMinY());
		if (allPoints.contains(point)) {
			corners.add(point);
			count++;
		}
		point = Point.getPoint(boardSize,shape.getMaxX(), shape.getMaxY());
		if (allPoints.contains(point)) {
			corners.add(point);
			count++;
		}
		return corners;
	}

	public static List<Point> getBehalfs(Set<? extends BasicBlock> blocks) {
		List<Point> list = new ArrayList<Point>(blocks.size());
		for (BasicBlock block : blocks) {
			list.add(block.getTopLeftPoint());
		}
		Collections.sort(list, new RowColumnComparator());
		return list;
	}

	public static List<Point> getPointList(Set<Point> points) {
		List<Point> list = new ArrayList<Point>(points.size());
		list.addAll(points);

		Collections.sort(list, new RowColumnComparator());
		return list;
	}

	public boolean isCalculated() {
		return calculated;
	}

	public void setCalculated(boolean calculated) {
		this.calculated = calculated;
	}

}

/**
 * 按y＝x的斜线来计算块的边缘。线上的点（row+column)都一致。同一条线上顶部的点优先。
 * 
 * @author Eddie
 * 
 */
class PointComparator implements Comparator<Point> {

	@Override
	public int compare(Point o1, Point o2) {
		int a = o1.getRow() + o1.getColumn();
		int b = o2.getRow() + o2.getColumn();
		if (a == b) {
			if (o1.getRow() < o2.getRow()) {// top one first.
				return -1;
			} else {
				return 1;
			}
		} else {
			return a - b;
		}

	}

}
