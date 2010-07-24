package toy;

import static toy.Constant.COL;
import static toy.Constant.ROW;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Record Current Status of the Board.
 * 
 * Board aggregate blocks.
 * 
 * @author eddie
 * 
 */
public abstract class Board {

	Set<Block> blockList = new HashSet<Block>();
	List<Point> blankList = new ArrayList<Point>();

	// byte[][] state = new byte[ROW + 1][COL + 1];// check duplicate state

	Block[][] blocks = new Block[ROW + 2][COL + 2];
	Move lastMove = null;

	/**
	 * update state array by blocks.
	 */
	BitSet updateState() {
		BitSet bs = new BitSet();
		for (int i = 1; i <= ROW; i++) {
			for (int j = 1; j <= COL; j++) {
				Block block = blocks[i][j];
				if (block != null) {
					// state[i][j] = block.getType();
					bs.set(2 * (i * (COL + j)));
					bs.set(2 * (i * (COL + j) + 1));
				}
			}
		}
		return bs;
	}

	public Board deepCopy() {
		Board board = cloneBoard();
		Set<Block> blockLista = new HashSet<Block>(blockList.size());
		// Collections.copy(blockList, blockLista);

		List<Point> blankLista = new ArrayList<Point>(this.blankList.size());
		Collections.copy(blankList, blankLista);// blockList.

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

		return board;
	}

	abstract Board cloneBoard();

	public void run() {
		init();
		// list of moves
		List<Move> moves = getMoves();

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

	public List<Move> getMoves() {
		List<Move> moves = new ArrayList<Move>();
		for (Point point : blankList) {
			System.out.println("Considering blank point: " + point);
			moves.addAll(point.getMoves(this));
		}

//		for (Iterator<Move> iter = moves.iterator(); iter.hasNext();) {
//			Move move = iter.next();
//			if (move.supplement(lastMove)) {
//				iter.remove();
//			}
//		}
		return moves;
	}

	public void apply(Move move) {
		// TODO Auto-generated method stub
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

		for (Point point : block.getPositions()) {
			if (this.blocks[point.getA()][point.getB()] == null) {
				System.out.println("add blank point: " + point);
				this.blankList.add(point);
			}
		}

		block.setPositions(temp);

		System.out.println("remove blank point:" + move.getEnd());
		this.blankList.remove(move.getEnd());

		for (Iterator<Point> it = blankList.iterator(); it.hasNext();) {
			Point next = it.next();
			if (this.getBlock(next) != null && blankList.contains(next)) {
				System.out.println("remove blank point:" + next);
				it.remove();
			}
		}

		this.lastMove=move;
		// getBlank list
	}

	public void apply(Point start, int x, int y) {
		Block block = this.getBlock(start);

	}

	public Block getBlock(Point p) {
		return blocks[p.getA()][p.getB()];
	}

	void init(Set<Block> list) {
		for (Block block : list) {
			for (Point p : block.positions) {
				blocks[p.getA()][p.getB()] = block;
			}
		}

	}

	/**
	 * initial state for Old dad
	 */
	public abstract void init();

	// void initFinalTarget() {
	// blockList.add(new Block(1, new Point[] { Point.getPoint(3, 1) },
	// "Pawn 1"));
	// blockList.add(new Block(1, new Point[] { Point.getPoint(3, 4) },
	// "Pawn 2"));
	//
	// blockList.add(new Block(2, new Point[] { Point.getPoint(1, 3),
	// Point.getPoint(1, 4) }, "Horizontal 1"));
	// blockList.add(new Block(2, new Point[] { Point.getPoint(2, 3),
	// Point.getPoint(2, 4) }, "Horizontal 2"));
	// blockList.add(new Block(2, new Point[] { Point.getPoint(4, 3),
	// Point.getPoint(4, 4) }, "Horizontal 3"));
	// blockList.add(new Block(2, new Point[] { Point.getPoint(5, 3),
	// Point.getPoint(5, 4) }, "Horizontal 4"));
	//
	// blockList.add(new Block(2, new Point[] { Point.getPoint(4, 1),
	// Point.getPoint(5, 1) }, "Vertical 1"));
	// blockList.add(new Block(2, new Point[] { Point.getPoint(4, 2),
	// Point.getPoint(5, 2) }, "Vertical 2"));
	//
	// blockList.add(new Block(4, new Point[] { Point.getPoint(1, 1),
	// Point.getPoint(2, 1), Point.getPoint(1, 2),
	// Point.getPoint(2, 2) }, "Big Boss"));
	// init(blockList);
	//
	// blankList.add(Point.getPoint(3, 2));
	// blankList.add(Point.getPoint(3, 3));
	// }

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

	// @Override
	// public int hashCode() {
	// return state.hashCode();
	// }

	// transient info -- start point and target
	Point start;

	public Point getStart() {
		return start;
	}

	public void setStart(Point start) {
		this.start = start;
	}

}
