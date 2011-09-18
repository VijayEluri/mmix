package toy;

import java.io.Serializable;

/**
 * immutable. Differentiate between rep. point and start point.later one is used
 * to calculate delta x and y.
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class BasicMove implements Serializable {
	/**
	 * for 2*2 or 1*2 block. the point should be the top left one in the block.
	 */
	protected Point representative;

	protected Point start;

	// the target: blank point.
	protected Point end;

	/**
	 * block and point are tightly coupled. block is a transient property
	 * derived from point
	 * 
	 * 
	 */

	transient byte x = -1;
	transient byte y = -1;

	public byte getX() {
		if (x == -1) {
			return (byte) (this.getEnd().getA() - this.getStart().getA());
		} else
			return x;
	}

	public byte getY() {
		if (y == -1) {
			return (byte) (this.getEnd().getB() - this.getStart().getB());
		} else
			return y;
	}


	public Point getEnd() {
		return end;
	}

	public void setEnd(Point end) {
		this.end = end;
	}

	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {
		this.start = start;
	}

	@Override
	public String toString() {
		return "BasicMove [start=" + start + ", end=" + end + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (representative.hashCode());
		result = prime * result + (this.getX());
		result = prime * result + (this.getY());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasicMove other = (BasicMove) obj;

		if (representative == null) {
			if (other.representative != null)
				return false;
		} else if (!representative.equals(other.representative))
			return false;
		if (this.getX() != other.getX())
			return false;
		if (this.getY() != other.getY())
			return false;
		return true;
	}

	public void setRepresentative(Point repPoint) {
		this.representative = repPoint;

	}

}
