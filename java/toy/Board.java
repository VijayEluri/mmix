package toy;

import static toy.Constant.COL;
import static toy.Constant.ROW;
import static toy.Constant.Debug;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import static toy.Constant.*;

/**
 * Record Current Status of the Board.
 * 
 * Board aggregate blocks.
 * 
 * @author eddie
 * 
 */
public abstract class Board {

	public Set<Block> getBlockList() {
		return blockList;
	}

	List<BasicMove> history = new ArrayList<BasicMove>();
	Set<Block> blockList = new HashSet<Block>();
	Set<Point> blankList = new HashSet<Point>();

	// byte[][] state = new byte[ROW + 1][COL + 1];// check duplicate state

	Block[][] blocks = new Block[ROW + 2][COL + 2];
	Move lastMove = null;
	// duplicate with bitset, but is handy.
	byte[][] state = new byte[ROW + 2][COL + 2];

	/**
	 * update state array by blocks. dose not distinguish vertical and
	 * horizontal.
	 */
	public BitSet updateState() {
		BitSet bs = new BitSet();
		for (int i = 1; i <= ROW; i++) {
			for (int j = 1; j <= COL; j++) {
				Block block = blocks[i][j];
				if (block != null) {
					state[i][j] = block.getType();
					if (state[i][j] == BIGBOSS) {
						bs.set(3 * ((i - 1) * COL + (j - 1)));
					} else {
						if (state[i][j] >= 2) {// high bit
							bs.set(3 * ((i - 1) * COL + (j - 1)) + 1);
						}
						if (state[i][j] % 2 == 1) {// low bit
							bs.set(3 * ((i - 1) * COL + (j - 1)) + 2);
						}
					}
				}
			}
		}
		return bs;
	}

	/**
	 * distinguish vertical and horizontal.
	 * 
	 * @return
	 */
	BitSet updateState2() {
		BitSet bs = new BitSet();
		for (int i = 1; i <= ROW; i++) {
			for (int j = 1; j <= COL; j++) {
				Block block = blocks[i][j];
				if (block != null) {
					state[i][j] = block.getType();
					if (state[i][j] >= 2) {// high bit
						bs.set(3 * ((i - 1) * COL + (j - 1)));
					}
					if (state[i][j] % 2 == 1) {// low bit
						bs.set(2 * ((i - 1) * COL + (j - 1)) + 1);
					}
				}
			}
		}
		return bs;
	}

	public Board deepCopy() {
		Board board = cloneBoard();
		Set<Block> blockLista = new HashSet<Block>(blockList.size());
	
		Set<Point> blankLista = new HashSet<Point>(this.blankList.size());
		for (Point point : blankList) {
			blankLista.add(point);
		}
		board.blankList = blankLista;

		board.blocks = new Block[ROW + 2][COL + 2];
		for (int i = 1; i <= ROW; i++) {
			for (int j = 1; j <= COL; j++) {
				if (blocks[i][j] != null) {
					if (blockLista.contains(blocks[i][j]) == false) {
						board.blocks[i][j] = blocks[i][j].deepCopy();
						blockLista.add(board.blocks[i][j]);
					}
				}
			}
		}

		board.lastMove = this.lastMove;
		board.init(blockLista);
		board.updateState();

		for (BasicMove bm : history) {
			board.history.add(bm);
		}
		// board.h
		return board;
	}

	abstract Board cloneBoard();

	public void run() {
		init();
		// list of moves
		Set<Move> moves = getMoves();

		if (moves.isEmpty()) {
			// back track one level.
		}
		for (Iterator<Move> iter = moves.iterator(); iter.hasNext();) {
			Move move = iter.next();
			iter.remove();
			apply(move);// change board status.
		}
		// for(Move move : moves){
		//
		//
		// }

	}

	public Set<Move> getMoves() {
		Set<BasicMove> basicMoves = new HashSet<BasicMove>();
		for (Point point : blankList) {
			if (Debug)
				System.out.println("Considering blank point: " + point);
			basicMoves.addAll(point.getMoves(this));
		}

		Set<Move> moves = new HashSet<Move>();
		for (BasicMove m : basicMoves) {
			Move move = new Move(m);
			move.setBoard(this);
			moves.add(move);
		}

		return moves;
	}

	public void apply(Move move) {
		// TODO Auto-generated method stub
		if (Debug)
			System.out.println("applying move " + move);
		Block block = move.getBlock();
		// empty first
		for (Point point : block.getPositions()) {
			this.blocks[point.getA()][point.getB()] = null;
		}

		// move second
		Point[] temp = new Point[block.getPositions().length];
		int i = 0;
		for (Point point : block.getPositions()) {
			int a = point.getA() + move.getX();
			int b = point.getB() + move.getY();

			this.blocks[a][b] = block;
			temp[i++] = Point.getPoint(a, b);
		}

		// before
		for (Point p : blankList) {
			if (Debug)
				System.out.println(p);
		}

		for (Point point : block.getPositions()) {
			if (this.blocks[point.getA()][point.getB()] == null) {
				if (Debug)
					System.out.println("add blank point: " + point);
				this.blankList.add(point);
			}
		}

		block.setPositions(temp);

		if (Debug)
			System.out.println("remove blank point:" + move.getEnd());
		this.blankList.remove(move.getEnd());

		for (Iterator<Point> it = blankList.iterator(); it.hasNext();) {
			Point next = it.next();
			if (this.getBlock(next) != null && blankList.contains(next)) {
				if (Debug)
					System.out.println("remove blank point:" + next);
				it.remove();
			}
		}
		// after
		for (Point p : blankList) {
			if (Debug)
				System.out.println(p);
		}

		this.lastMove = move;
		// getBlank list
	}

	// public void apply(Point start, int x, int y) {
	// Block block = this.getBlock(start);
	//
	// }

	public Block getBlock(Point p) {
		return blocks[p.getA()][p.getB()];
	}

	void init(Set<Block> list) {
		for (Block block : list) {
			for (Point p : block.getPositions()) {
				blocks[p.getA()][p.getB()] = block;
			}
		}

	}

	/**
	 * initial state for Old dad
	 */
	public abstract void init();

	public abstract boolean achieveGoal();

	@Override
	public boolean equals(Object o) {
		if (o instanceof Board) {
			Board board = (Board) o;
			if (this.updateState().equals(board.updateState())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.updateState().hashCode();
	}

	// transient info -- start point and target
	Point start;

	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {
		this.start = start;
	}

	public List<BasicMove> getHistory() {
		return history;
	}

	public void addHistoryMove(BasicMove move) {
		this.history .add(move);
	}

	public abstract void checkInternal();
}
