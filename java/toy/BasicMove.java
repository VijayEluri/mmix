package toy;

/**
 * immutable.
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class BasicMove {
	/**
	 * for 2*2 or 1*2 block. the point should be the top left one in the block.
	 */
	protected Point start;
	
	// the target: blank point.
	protected Point end; 

	/**
	 * block and point are tightly coupled. block is a transient property
	 * derived from point
	 * 
	 * 
	 */

	byte x = -1;
	byte y = -1;

	public byte getX() {
		if (x == -1) {
			return (byte) (this.getEnd().getA() - this.getStart().getA());
		} else
			return x;
	}

	public void setX(byte x) {
		this.x = x;
	}

	public byte getY() {
		if (y == -1) {
			return (byte) (this.getEnd().getB() - this.getStart().getB());
		} else
			return y;
	}

	public void setY(byte y) {
		this.y = y;
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
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
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
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}

}
