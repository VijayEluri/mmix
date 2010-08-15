package toy;

import static toy.Constant.BIGBOSS;
import static toy.Constant.DOUBLE;
import static toy.Constant.SINGLE;

public class Century extends Board {

	@Override
	Board cloneBoard() {
		// TODO Auto-generated method stub
		return new Century();
	}

	@Override
	public void init() {
		blockList.add(new Block(SINGLE, new Point[] { Point.getPoint(1, 1) },
				"Pawn 1"));
		blockList.add(new Block(SINGLE, new Point[] { Point.getPoint(1, 4) },
				"Pawn 2"));
		blockList.add(new Block(SINGLE, new Point[] { Point.getPoint(4, 1) },
				"Pawn 3"));
		blockList.add(new Block(SINGLE, new Point[] { Point.getPoint(4, 4) },
				"Pawn 4"));

		blockList.add(new Block(DOUBLE, new Point[] { Point.getPoint(5, 1),
				Point.getPoint(5, 2) }, "Horizontal 1"));
		blockList.add(new Block(DOUBLE, new Point[] { Point.getPoint(5, 3),
				Point.getPoint(5, 4) }, "Horizontal 2"));
		

		blockList.add(new Block(DOUBLE, new Point[] { Point.getPoint(2, 1),
				Point.getPoint(3, 1) }, "Vertical 1"));
		blockList.add(new Block(DOUBLE, new Point[] { Point.getPoint(2, 4),
				Point.getPoint(3, 4) }, "Vertical 2"));
		blockList.add(new Block(DOUBLE, new Point[] { Point.getPoint(3, 2),
				Point.getPoint(4, 2) }, "Vertical 3"));

		Block bigBoss = new Block(BIGBOSS, new Point[] { Point.getPoint(1, 3),
				Point.getPoint(2, 3), Point.getPoint(1, 2),
				Point.getPoint(2, 2) }, "Big Boss");
		blockList.add(bigBoss);
		init(blockList);

		blankList.add(Point.getPoint(3, 3));
		blankList.add(Point.getPoint(4, 3));

	}

	@Override
	public boolean achieveGoal() {
		if (state[4][2] == BIGBOSS && state[4][3] == BIGBOSS
				&& state[5][2] == BIGBOSS && state[5][3] == BIGBOSS)
			return true;
		else
			return false;
	}

	@Override
	public void checkInternal() {
		// TODO Auto-generated method stub

	}

}
