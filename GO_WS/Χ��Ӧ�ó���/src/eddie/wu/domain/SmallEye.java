package eddie.wu.domain;

/**
 * basic function to store the knowledge about the basic of small eye from 2
 * Stones to 7 stones. 从二子到七子的基本死活结论。
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class SmallEye {
	Block block;

	public SmallEye(Block block) {
		this.block = block;
	}

	/**
	 * block 中的气点坐标是具体局面中的实际坐标，先转换成局部的坐标；得到结果之后，再转换回实际的坐标。
	 * 
	 * @return
	 */

	public SurviveResult getResult() {
		int num = block.getTotalNumberOfPoint();
		switch (num) {
		case 2:
			return getResult2();
		case 3:
			return getResult3();
		case 4:
			return getResult4();
		case 5:
			return getResult5();
		}
		
		throw new RuntimeException(
				"the breath block should be in size from 2 to 7 stones.");
	}

	private SurviveResult getResult5() {
		// TODO Auto-generated method stub
		return null;
	}

	private SurviveResult getResult4() {
		SurviveResult result = new SurviveResult();
		
		Shape shape = block.getShape();
		Point point = null;
		if(shape.getMinDelta()==1){
			result.setXianShou(new Result(SurviveResult.LIVE, null));
			result.setHouShou(new Result(SurviveResult.LIVE, null));
			result.setIndependent(true);
		}else if(shape.getMinDelta()==2){
			if(shape.getMaxDelta()==2){
				result.setXianShou(new Result(SurviveResult.DIE, null));
				result.setHouShou(new Result(SurviveResult.DIE, null));
				result.setIndependent(true);
			}else{//==3 there are two pattern, they have different result.
				//hard coding is not a good solution.
				
			}
		}
		//result.setWaste(true);
		return result;
	}

	private SurviveResult getResult2() {
		SurviveResult result = new SurviveResult();
		result.setXianShou(new Result(SurviveResult.DIE, null));
		result.setHouShou(new Result(SurviveResult.DIE, null));
		result.setIndependent(true);
		result.setWaste(true);
		return result;
	}

	/**
	 * need to category according to the shape!
	 * 
	 * @return
	 */
	private SurviveResult getResult3() {
		SurviveResult result = new SurviveResult();
		Shape shape = block.getShape();
		Point point = null;
		/**
		 * 1*3 or 3*1 pattern.
		 */
		if ((shape.getDeltaX() == 1 && shape.isLandscape() == false)
				|| (shape.getDeltaY() == 1 && shape.isLandscape() == true)) {
			int x = (shape.getMinX() + shape.getMaxX()) / 2;
			int y = (shape.getMinY() + shape.getMaxY()) / 2;
			point = Point.getPoint(x, y);
		} else {// 2*2 pattern.
			if (!block.getAllPoints().contains(
					Point.getPoint(shape.getMinX(), shape.getMinY()))) {
				point = Point.getPoint(shape.getMaxX(), shape.getMaxY());
			}
			if (!block.getAllPoints().contains(
					Point.getPoint(shape.getMinX(), shape.getMaxY()))) {
				point = Point.getPoint(shape.getMaxX(), shape.getMinY());
			}
			if (!block.getAllPoints().contains(
					Point.getPoint(shape.getMaxX(), shape.getMinY()))) {
				point = Point.getPoint(shape.getMinX(), shape.getMaxY());
			}
			if (!block.getAllPoints().contains(
					Point.getPoint(shape.getMaxX(), shape.getMaxY()))) {
				point = Point.getPoint(shape.getMinX(), shape.getMinY());
			}

		}

		result.setXianShou(new Result(SurviveResult.LIVE, point));
		result.setHouShou(new Result(SurviveResult.DIE, point));
		result.setIndependent(false);
		return result;
	}
}
