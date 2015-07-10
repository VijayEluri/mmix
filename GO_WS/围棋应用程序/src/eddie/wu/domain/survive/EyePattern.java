package eddie.wu.domain.survive;

import java.util.HashSet;
import java.util.Set;

import eddie.wu.domain.Delta;
import eddie.wu.domain.Point;

/**
 * 处理完整大眼的死活表示,举例来说，如何表达刀把五后手时是死棋这一知识点。<br/>
 * 刀把五有不同的形式。
 * 
 * @author Eddie
 * 
 */
public class EyePattern {
	// private byte eyeSize;
	private Set<Point> eyePoints = new HashSet<Point>();

	/**
	 * myself point neighbor directly to eye point. <br/>
	 * it has enough information to decide whether the eye is in center or
	 * corner.
	 */

	private Set<Point> selfPoints = new HashSet<Point>();

	/**
	 * Point to connect myself blocks.<br/>
	 * which decide whether the block is a complete block
	 */

	private Set<Point> shoulderPoints = new HashSet<Point>();

	public int getEyeSize() {
		return eyePoints.size();
	}

	public Set<Point> getEyePoints() {
		return eyePoints;
	}

	public void addEyePoints(Set<Point> eyePoints) {
		this.eyePoints.addAll(eyePoints);
	}

	public Set<Point> getSelfPoints() {
		return selfPoints;
	}

	public void addSelfPoints(Set<Point> selfPoints, Delta delta) {
		for (Point point : selfPoints) {
			Point neighbor = point.getNeighbourReverse(delta);
			this.selfPoints.add(neighbor);
		}

	}

	public Set<Point> getShoulderPoints() {
		return shoulderPoints;
	}

	// public void addShoulderPoints(Set<Point> shouldPoints) {
	// this.shoulderPoints.addAll(shouldPoints);
	// }
	public void addShoulderPoints(Set<Point> shoulderPoints, Delta delta) {
		for (Point point : shoulderPoints) {
			Point neighbor = point.getNeighbourReverse(delta);
			this.shoulderPoints.add(neighbor);
		}

	}

	public int numberOfMyPoint() {
		return this.selfPoints.size();
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof EyePattern == false)
			return false;
		EyePattern other = (EyePattern) obj;
		/**
		 * 眼位大小是否相等
		 */
		if (this.getEyeSize() != other.getEyeSize())
			return false;
		/**
		 * 眼位位置是否等价（中央，边，角）
		 */
		if (this.numberOfMyPoint() != other.numberOfMyPoint())
			return false;

		/**
		 * 眼位朝向是否等价，即四角八边的变体。
		 */
		if (this.eyePoints.equals(other.getEyePoints()) == false)
			return false;
		
		/**
		 * 以上条件满足，selfPoints应该等价。
		 */
		
		/**
		 * 
		 */
		return super.equals(obj);
	}

}
