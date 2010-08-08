package toy;

import static toy.Constant.BIGBOSS;
import static toy.Constant.DOUBLE;
import static toy.Constant.SINGLE;

public class OldDad extends Board {

	@Override
	public boolean achieveGoal() {
		if (state[4][1] == BIGBOSS && state[4][2] == BIGBOSS
				&& state[5][1] == BIGBOSS && state[5][2] == BIGBOSS)
			return true;
		else
			return false;
	}

	@Override
	public void init() {
		blockList.add(new Block(SINGLE, new Point[] { Point.getPoint(3, 1) },
				"Pawn 1"));
		blockList.add(new Block(SINGLE, new Point[] { Point.getPoint(3, 4) },
				"Pawn 2"));

		blockList.add(new Block(DOUBLE, new Point[] { Point.getPoint(1, 3),
				Point.getPoint(1, 4) }, "Horizontal 1"));
		blockList.add(new Block(DOUBLE, new Point[] { Point.getPoint(2, 3),
				Point.getPoint(2, 4) }, "Horizontal 2"));
		blockList.add(new Block(DOUBLE, new Point[] { Point.getPoint(4, 3),
				Point.getPoint(4, 4) }, "Horizontal 3"));
		blockList.add(new Block(DOUBLE, new Point[] { Point.getPoint(5, 3),
				Point.getPoint(5, 4) }, "Horizontal 4"));

		blockList.add(new Block(DOUBLE, new Point[] { Point.getPoint(4, 1),
				Point.getPoint(5, 1) }, "Vertical 1"));
		blockList.add(new Block(DOUBLE, new Point[] { Point.getPoint(4, 2),
				Point.getPoint(5, 2) }, "Vertical 2"));

		Block bigBoss = new Block(BIGBOSS, new Point[] { Point.getPoint(1, 1),
				Point.getPoint(2, 1), Point.getPoint(1, 2),
				Point.getPoint(2, 2) }, "Big Boss");
		blockList.add(bigBoss);
		init(blockList);

		blankList.add(Point.getPoint(3, 2));
		blankList.add(Point.getPoint(3, 3));
	}

	@Override
	Board cloneBoard() {
		// TODO Auto-generated method stub
		return new OldDad();
	}

	@Override
	public void checkInternal() {
		if(blockList.size()!=9){
			System.out.println("there are " + blockList.size() + " blocks");
			
		}
		
	}
	
	

}
