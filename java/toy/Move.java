package toy;

import java.util.Arrays;

/**
 * Move include current board state and next step choice/candidate
 * 
 * @author eddie
 * 
 */
public class Move {
	// Point[] startPosition;
	// only useful in solution space search. otherwise it is implicitly knows
	Board board;

	/**
	 * block and point are tightly coupled. block is a transient property
	 * derived from point
	 * 
	 * 
	 */
	Point start;

	byte x;
	byte y;

	Point end; // the target: blank point.

	// Block block;

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public Point getEnd() {
		return end;
	}

	public void setEnd(Point end) {
		this.end = end;
	}

	public boolean equals(Object o) {
		if (o instanceof Move) {
			Move m = (Move) o;
			if (m.getBlock().equals(getBlock())) {
				if (m.x == x && m.y == y) {
					return true;
				} else
					return false;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public Block getBlock() {
		return board.getBlock(start);
	}

	public byte getX() {
		return x;
	}

	public void setX(byte x) {
		this.x = x;
	}

	public byte getY() {
		return y;
	}

	public void setY(byte y) {
		this.y = y;
	}

	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {
		this.start = start;
	}

//	public boolean supplement(Move m) {
//		if (m == null)
//			return false;
//		if (m.getBlock().equals(getBlock())) {
//			if (m.x + x == 0 && m.y + y == 0)
//				return true;
//			else
//				return false;
//		} else {
//			return false;
//		}
//	}

	public Board apply() {
		board.apply(this);
		return board;
	}

	@Override
	public String toString() {
		return "Move [block=" + getBlock() + ", x=" + x + ", y=" + y
				+ ", start=" + start + ",end=" + end + "]";
	}
}
