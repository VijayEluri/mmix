package toy;

/**
 * Move include current board state and next step choice/candidate
 * 
 * @author eddie
 * 
 */
public class Move {
	public Move(BasicMove move) {
		this.move = move;
	}

	// Point[] startPosition;
	// only useful in solution space search. otherwise it is implicitly knows
	Board board;
	BasicMove move;

	public BasicMove getMove() {
		return move;
	}

	public Point getEnd() {
		return move.getEnd();
	}

	public void setEnd(Point end) {
		move.setEnd(end);
	}

	public Point getStart() {
		return move.getStart();
	}

	public void setStart(Point start) {
		move.setStart(start);
	}

	// Block block;

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public Block getBlock() {
		return board.getBlock(move.getStart());
	}

	public byte getX() {
		return move.getX();
	}

	public byte getY() {
		return move.getY();
	}

	public Board apply() {
		board.apply(this);
		return board;
	}

	@Override
	public String toString() {
		return "Move " + ", start=" + move.getStart() + ",end=" + move.getEnd()
				+ "]";
	}

	@Override
	public int hashCode() {
		int result = move.hashCode();
		result = 31 * result + board.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Move) {
			Move move = (Move) obj;
			if (move.board.equals(this.board))
				return true;
			else
				return false;
		} else
			return false;
		// return super.equals(obj);
	}

}
