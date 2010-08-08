package toy;

import static toy.Constant.COL;
import static toy.Constant.ROW;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static toy.Constant.Debug;
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
	public Set<BasicMove> getMoves(Board board) {
		Set<BasicMove> moves = new HashSet<BasicMove>();
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
					if(Debug) System.out.println("Considering point: " + t);
					BasicMove move = new BasicMove();
					//move.setBlock(block);
					move.setStart(board.getBlock(t).getRepPoint());
					move.setEnd(this);
					
					
					move.setX(p.a);
					move.setY(p.b);
					moves.add(move);
				}
			}

		}

		return moves;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + a;
		result = prime * result + b;
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
		Point other = (Point) obj;
		if (a != other.a)
			return false;
		if (b != other.b)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Point [a=" + a + ", b=" + b + "]";
	}

}
