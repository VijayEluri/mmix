package toy;

import static toy.Constant.BIGBOSS;
import static toy.Constant.DOUBLE;
import static toy.Constant.HORIZONTAL;
import static toy.Constant.SINGLE;
import static toy.Constant.VERTICAL;

import java.util.Arrays;

public class Block {
	private byte points; // number of points in one block.
	private byte type;// 1 for 1*1 block; 2 for 2*1; 3 for 2*2. derived
						// attribute.
	private Point[] positions;
	private String name;

	public byte getPoints() {
		return points;
	}

	public void setPoints(byte points) {
		this.points = points;
	}

	public Point[] getPositions() {
		return positions;
	}

	public void setPositions(Point[] positions) {
		this.positions = positions;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte getType() {
		return type;
	}

	public Block(int points, Point[] positions, String name) {
		this.points = (byte) points;
		this.positions = positions;
		this.name = name;
		if (points == SINGLE) {
			type = SINGLE;
		} else if (points == DOUBLE) {
			if (this.getHeight() == 1) {
				type = HORIZONTAL;
			} else {
				type = VERTICAL;
			}
		} else if (points == BIGBOSS) {
			type = BIGBOSS;
		} else {
			System.err.println("wrong type of Block" + name);
		}
	}

	/**
	 * adjacent blank point will be handled separately.
	 * 
	 * @param point
	 *            the blank point/block
	 * @return
	 */
	// public List<Move> getMoves(Point point) {
	//
	// return null;
	// }

	@Override
	public String toString() {
		return "Block [name=" + name + ", points=" + points + ", positions="
				+ Arrays.toString(positions) + ", type=" + type + "]";
	}

	public Block deepCopy() {
		Block block = new Block(this.points, this.positions, this.name);
		return block;
	}

	public int getHeight() {
		if (points == 1) {
			return 1;
		} else if (points == 4) {
			return 2;
		} else if (points == 2) {
			try{
			if (this.positions[0].getA() == this.positions[1].getA()) {
				return 1;
			} else {
				return 2;
			}
			}catch(RuntimeException e){
				System.out.println(Arrays.toString(positions));
				throw e;
			}
		} else {
			System.err.println("wrong: points = " + points);
			return 0;
		}

	}

	public int getWidth() {
		if (points == 1) {
			return 1;
		} else if (points == 4) {
			return 2;
		} else if (points == 2) {
			if (this.positions[0].getA() == this.positions[1].getA()) {
				return 2;
			} else {
				return 1;
			}
		} else {
			System.err.println("wrong: points = " + points);
			return 0;
		}

	}

	public Point getRepPoint() {
		return Point.getPoint(this.getMinA(), getMinB());
	}

	public int getMinA() {
		int min = Integer.MAX_VALUE;
		for (Point p : this.positions) {
			try {
				if (p.getA() < min) {
					min = p.getA();
				}
			} catch (Exception e) {
				System.out.println(Arrays.toString(this.positions));
			}
		}
		return min;
	}

	public int getMinB() {
		int min = Integer.MAX_VALUE;
		for (Point p : this.positions) {
			if (p.getB() < min) {
				min = p.getB();
			}
		}
		return min;
	}

}
