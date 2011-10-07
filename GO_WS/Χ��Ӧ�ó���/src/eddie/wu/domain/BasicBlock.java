package eddie.wu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static eddie.wu.domain.survive.SmallEye.*;

/**
 * This is the abstraction of Block, which is a group of stones connected
 * together. it could also be the blank point in the board, such as big eyes,
 * shared breath between two blocks. <br/>
 * ���ĸ������������һ��ĺ��ӺͰ��ӡ�<br/>
 * �����Ƕ���һ����ļ���չ������������ܺͰ׿飬�ڿ鶼���ڣ����ڿ�ֻ�ܺͰ׿����ڡ�<br/>
 * �������������һ����ɫ����飨�����ж�������ڣ���ô������һ���ۡ�������ǹ�������<br/>
 * �����������ĵ�λ����Ϊһ����Ҳ��������һ����顣<br/>
 * ��������ез���������<br/>
 * ����������������������<br/>
 * 
 * @author wueddie-wym-wrz
 * 
 */
public abstract class BasicBlock {

	/* general/common attribute */
	protected byte color;// �����ɫ

	protected Set<Point> allPoints = new HashSet<Point>();// ����ӵ㼯��

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

	/**
	 * get the profile rectangle.
	 * 
	 * @return
	 */
	public Shape getShape() {
		return Shape.getShape(this.getAllPoints());
	}

	/**
	 * 
	 * @param shape
	 * @return ���ݸ�shape�ĸ������Ƿ�Ϊ�����һ���֡����ؼ�����
	 */
	public List<Point> getShapeCorners(Shape shape) {
		List<Point> corners = new ArrayList<Point>();
		int count = 0;
		Point point;
		point = Point.getPoint(shape.getMinX(), shape.getMinY());
		if (allPoints.contains(point)) {
			corners.add(point);
			count++;
		}
		point = Point.getPoint(shape.getMinX(), shape.getMaxY());
		if (allPoints.contains(point)) {
			corners.add(point);
			count++;
		}
		point = Point.getPoint(shape.getMaxX(), shape.getMinY());
		if (allPoints.contains(point)) {
			corners.add(point);
			count++;
		}
		point = Point.getPoint(shape.getMaxX(), shape.getMaxY());
		if (allPoints.contains(point)) {
			corners.add(point);
			count++;
		}
		return corners;
	}

	/**
	 * brute force coding of the eye name.<br/>
	 * better to use data to represent them.
	 * 
	 * @return
	 */
	public String getEyeName() {
		int size = this.allPoints.size();
		Shape shape = this.getShape();

		switch (size) {
		case 1: {
			return SINGLE_STONE_EYE;
		}
		case 2: {
			return TWO_STONE_EYE;
		}
		case 3: {
			if (shape.getMinDelta() == 1) {
				return STRAIGHT_THREE_STONE_EYE;
			} else {
				return BEND_THREE_STONE_EYE;
			}
		}
		case 4: {
			if (shape.getMinDelta() == 1) {
				return STRAIGHT_FOUR_STONE_EYE;
			} else if (shape.getMaxDelta() == 2) {
				return RECTANGLT_FOUR_STONE_EYE;
			} else {// ==3
				List<Point> shapeCorners = this.getShapeCorners(shape);
				int count = shapeCorners.size();
				if (count == 2) {
					if (shapeCorners.get(0).isSameline(shapeCorners.get(1))) {
						return T_FOUR_STONE_EYE;// ��ñ��
					} else {
						return Z_FOUR_STONE_EYE;
					}
				} else {// ==3
					return RULER_FOUR_STONE_EYE;
				}
			}
		}
		case 5: {
			if (shape.getMinDelta() == 1) {
				return STRAIGHT_FIVE_STONE_EYE;
			} else if (shape.getMinDelta() == 2) {
				List<Point> shapeCorners = this.getShapeCorners(shape);
				int count = shapeCorners.size();
				if (count == 3) {
					return KNIFE_HANDLER_FIVE_STONE_EYE;
				} else {// ==4
					return TRAP_FIVE_STONE_EYE;//
				}
			} else {// ==3
				List<Point> shapeCorners = this.getShapeCorners(shape);
				int count = shapeCorners.size();
				if (count == 0) {
					return FLOWER_FIVE_STONE_EYE;
				} else if (count == 2) {
					if (shapeCorners.get(0).isSameline(shapeCorners.get(1))) {
						return T_FIVE_STONE_EYE;
					} else {
						return Z_FIVE_STONE_EYE;
					}
				} else if (count == 3) {
					return RULER_FIVE_STONE_EYE;
				} else {
					return "÷����֮�����״";
				}
			}
		}
		case 6: {
			if (shape.getMinDelta() == 1) {
				return STRAIGHT_SIX_STONE_EYE;
			} else if (shape.getMinDelta() == 2) {
				if (shape.getMaxDelta() == 3) {
					return MATRIX_SIX_STONE_EYE;
				} else if (shape.getMaxDelta() == 4) {
					return "����֮�����״";
				} else {
					return "����֮�����״2";
				}
			}
			// shape.getMinDelta() == 3
			List<Point> shapeCorners = this.getShapeCorners(shape);
			int count = shapeCorners.size();
			if (count == 3) {
				return FLOWER_SIX_STONE_EYE;
			} else {
				return "÷����֮�����״";//
			}

		}
		case 7: {
			return "���Ӽ������ۿ�";
		}
		}
		return null;
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
