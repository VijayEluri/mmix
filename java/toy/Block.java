package toy;

import java.util.Arrays;
import java.util.List;

public class Block {
	byte points; // number of points in one block.
	Point[] positions;
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

	public void setType(byte type) {
		this.type = type;
	}

	String name;
	byte type;// 1 for 1*1 block; 2 for 2*1; 3 for 2*2

	public Block(int points, Point[] positions, String name) {
		this.points = (byte) points;
		this.positions = positions;
		this.name = name;
		if (points == 1) {
			type = 1;
		} else if (points == 2) {
			type = 2;
		} else if (points == 4) {
			type = 3;
		} else {
			System.err.println("wrong");
		}
	}
	
	/**
	 * adjacent blank point will be handled separately.
	 * @param point the blank point/block
	 * @return
	 */
	public List<Move> getMoves(Point point) {
		
		return null;
	}

	@Override
	public String toString() {
		return "Block [name=" + name + ", points=" + points + ", positions="
				+ Arrays.toString(positions) + ", type=" + type + "]";
	}

	public Block deepCopy() {
		Block block = new Block(this.points,this.positions,this.name);
//		b.name = name;
//		b.points=this.points;
//		b.positions=this.positions;
		//b.type = this.type;
		return block;
	}
}
