package toy;

import static toy.Constant.BIGBOSS;
import static toy.Constant.DOUBLE;
import static toy.Constant.SINGLE;

/**
 * ²å³áÄÑ·É
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class ChaChiNanFei extends HuaRongDao {

	@Override
	Board cloneBoard() {

		return new ChaChiNanFei();
	}

	@Override
	public void init() {
		blockList.add(new Block(SINGLE, new Point[] { Point.getPoint(3, 1) },
				"Pawn 1"));
		blockList.add(new Block(SINGLE, new Point[] { Point.getPoint(4, 1) },
				"Pawn 2"));
		blockList.add(new Block(SINGLE, new Point[] { Point.getPoint(3, 4) },
				"Pawn 3"));
		blockList.add(new Block(SINGLE, new Point[] { Point.getPoint(4, 4) },
				"Pawn 4"));

		blockList.add(new Block(DOUBLE, new Point[] { Point.getPoint(3, 2),
				Point.getPoint(3, 2) }, "Horizontal 1"));
		blockList.add(new Block(DOUBLE, new Point[] { Point.getPoint(4, 2),
				Point.getPoint(4, 3) }, "Horizontal 1"));
		blockList.add(new Block(DOUBLE, new Point[] { Point.getPoint(5, 2),
				Point.getPoint(5, 3) }, "Horizontal 1"));
		

		blockList.add(new Block(DOUBLE, new Point[] { Point.getPoint(1, 1),
				Point.getPoint(2, 1) }, "Vertical 1"));	
		blockList.add(new Block(DOUBLE, new Point[] { Point.getPoint(1, 4),
				Point.getPoint(2, 4) }, "Vertical 2"));

		Block bigBoss = new Block(BIGBOSS, new Point[] { Point.getPoint(1, 2),
				Point.getPoint(2, 2), Point.getPoint(1, 3),
				Point.getPoint(2, 3) }, "Big Boss");
		blockList.add(bigBoss);
		init(blockList);

		blankList.add(Point.getPoint(5, 1));
		blankList.add(Point.getPoint(5, 4));

	}

}
