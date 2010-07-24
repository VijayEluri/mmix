package toy;

import static toy.Constant.COL;
import static toy.Constant.ROW;

import java.util.ArrayList;
import java.util.List;

public class Point {

	public byte getA() {
		return a;
	}

	public void setA(byte a) {
		this.a = a;
	}

	public byte getB() {
		return b;
	}

	public void setB(byte b) {
		this.b = b;
	}

	static Point[][] points = new Point[ROW + 2][COL + 2];
	static {
		for (int i = 1; i <= ROW; i++) {
			for (int j = 0; j <= COL; j++) {
				points[i][j] = new Point(i, j);
			}
		}
	}

	private Point(int a, int b) {
		this.a = (byte) a;
		this.b = (byte) b;
	}

	public static Point getPoint(int a, int b) {
		return points[a][b];
	}

	private byte a;
	private byte b;

	/**
	 * bad smell here.
	 */
	private static List<Point> periphery = new ArrayList<Point>(4);
	static {
		periphery.add(new Point(1, 0));
		periphery.add(new Point(0, 1));
		periphery.add(new Point(-1, 0));
		periphery.add(new Point(0, -1));

	}

	/**
	 * based on current blank point. list all the possible moves
	 * 
	 * @param board
	 * @param point
	 * @return
	 */
	public List<Move> getMoves(Board board) {
		List<Move> moves = new ArrayList<Move>();
		Point t;

		for (Point p : periphery) {
			t = points[a - p.a][b - p.b];
			
			//important: out of bound
			if(t==null) continue;
			
			Block block = board.getBlock(t);
			if (block != null) {

				boolean accept = true;
				for (Point single : block.getPositions()) {
					Block temp = board.getBlock(Point.getPoint(single.getA()
							+ p.a, single.getB() + p.b));
					if (temp == null || temp == block) {
						continue;
					} else {
						accept = false;
						break;
					}

				}
				if (accept == true) {
					System.out.println("Considering point: " + t);
					Move move = new Move();
					//move.setBlock(block);
					move.setStart(t);
					move.setEnd(this);
					move.setBoard(board);
					
					move.setX(p.a);
					move.setY(p.b);
					moves.add(move);
				}
			}

		}

		return moves;
	}

	@Override
	public String toString() {
		return "Point [a=" + a + ", b=" + b + "]";
	}

}
