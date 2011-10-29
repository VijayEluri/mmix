package eddie.wu.domain.survive;

import java.util.List;

import eddie.wu.domain.Block;
import eddie.wu.domain.Delta;
import eddie.wu.domain.Point;
import eddie.wu.domain.Shape;

/**
 * basic function to store the knowledge about the basic of small eye from 2
 * Stones to 7 stones. <br/>
 * �Ӷ��ӵ����ӵĻ���������ۡ�
 * ���������Ѿ��ֹ���ɡ�
 * @author wueddie-wym-wrz
 * 
 */
public class SmallEye {
	public static final String STRAIGHT_SIX_STONE_EYE = "ֱ��";

	public static final String MATRIX_SIX_STONE_EYE = "����";

	public static final String FLOWER_SIX_STONE_EYE = "÷����";

	public static final String RULER_FIVE_STONE_EYE = "������";

	public static final String Z_FIVE_STONE_EYE = "����";

	public static final String T_FIVE_STONE_EYE = "����";

	public static final String FLOWER_FIVE_STONE_EYE = "÷����";

	public static final String TRAP_FIVE_STONE_EYE = "����";

	public static final String KNIFE_HANDLER_FIVE_STONE_EYE = "������";

	public static final String STRAIGHT_FIVE_STONE_EYE = "ֱ��";

	public static final String RULER_FOUR_STONE_EYE = "����";

	public static final String Z_FOUR_STONE_EYE = "����";

	public static final String T_FOUR_STONE_EYE = "����";

	public static final String RECTANGLT_FOUR_STONE_EYE = "����";

	public static final String STRAIGHT_FOUR_STONE_EYE = "ֱ��";

	public static final String BEND_THREE_STONE_EYE = "����";

	public static final String STRAIGHT_THREE_STONE_EYE = "ֱ��";

	public static final String TWO_STONE_EYE = "ֱ��";

	public static final String SINGLE_STONE_EYE = "������";
	Block block;

	/**
	 * 
	 * @param block
	 *            Eye Block
	 */
	public SmallEye(Block block) {
		this.block = block;
	}

	/**
	 * block �е����������Ǿ�������е�ʵ�����꣬��ת���ɾֲ������ꣻ�õ����֮����ת����ʵ�ʵ����ꡣ
	 * 
	 * @return
	 */

	public RelativeSurviveResult getResult() {
		int num = block.getTotalNumberOfPoint();
		switch (num) {
		case 2:
			return getResult_TwoStones();
		case 3:
			return getResult_ThreeStones();
		case 4:
			return getResult_FourStones();
		case 5:
			return getResult_FiveStones();
		}

		throw new RuntimeException(
				"the breath block should be in size from 2 to 7 stones.");
	}

	/**
	 * ���ָ����Ŀ������������Ⱥ��֡�
	 * 
	 * @return
	 */
	private RelativeSurviveResult getResult_TwoStones() {
		RelativeSurviveResult result = new RelativeSurviveResult();
		// �����������������ӿ��¡�
		result.setXianShou(new RelativeResult(RelativeSurviveResult.DIE, null));
		// ���������Է��������¡�
		result.setHouShou(new RelativeResult(RelativeSurviveResult.DIE, null));
		// �Ⱥ���û������
		result.setIndependent(true);
		// �����Ƿ����ã����Ƿ��������ԣ���������ڵ�֮��û��������ò�û�����壬���˷���һ���塣
		result.setWaste(true);
		return result;
	}

	/**
	 * need to category according to the shape! because the move to make the
	 * eyes depending on the shape! brute force: ����Ӳ����
	 * 
	 * @return
	 */
	private RelativeSurviveResult getResult_ThreeStones() {
		RelativeSurviveResult result = new RelativeSurviveResult();
		Shape shape = block.getShape();
		Delta point = null;
		/**
		 * 1*3 or 3*1 pattern.
		 */
		if ((shape.getDeltaX() == 1 && shape.isLandscape() == false)
				|| (shape.getDeltaY() == 1 && shape.isLandscape() == true)) {
			int x = (shape.getMinX() + shape.getMaxX()) / 2;
			int y = (shape.getMinY() + shape.getMaxY()) / 2;
			point = Delta.getDelta(x, y);
		} else {// 2*2 pattern.
			/*
			 * ��ȷ�����ۿ�֮�㣬��ȡ��Խǵ㡣
			 */
			if (!block.getAllPoints().contains(
					Point.getPoint(shape.getMinX(), shape.getMinY()))) {
				point = Delta.getDelta(shape.getMaxX(), shape.getMaxY());
			}
			if (!block.getAllPoints().contains(
					Point.getPoint(shape.getMinX(), shape.getMaxY()))) {
				point = Delta.getDelta(shape.getMaxX(), shape.getMinY());
			}
			if (!block.getAllPoints().contains(
					Point.getPoint(shape.getMaxX(), shape.getMinY()))) {
				point = Delta.getDelta(shape.getMinX(), shape.getMaxY());
			}
			if (!block.getAllPoints().contains(
					Point.getPoint(shape.getMaxX(), shape.getMaxY()))) {
				point = Delta.getDelta(shape.getMinX(), shape.getMinY());
			}

		}

		result.setXianShou(new RelativeResult(RelativeSurviveResult.LIVE, point));
		result.setHouShou(new RelativeResult(RelativeSurviveResult.DIE, point));
		result.setIndependent(false);
		return result;
	}

	private RelativeSurviveResult getResult_FourStones() {
		RelativeSurviveResult result = new RelativeSurviveResult();

		Shape shape = block.getShape();
		Point point = null;
		if (shape.getMinDelta() == 1) {
			result.setXianShou(new RelativeResult(RelativeSurviveResult.LIVE, null));
			result.setHouShou(new RelativeResult(RelativeSurviveResult.LIVE, null));
			result.setIndependent(true);
		} else if (shape.getMinDelta() == 2) {
			if (shape.getMaxDelta() == 2) {
				result.setXianShou(new RelativeResult(RelativeSurviveResult.DIE, null));
				result.setHouShou(new RelativeResult(RelativeSurviveResult.DIE, null));
				result.setIndependent(true);
			} else if (shape.getMaxDelta() == 3) {
				List<Point> corners = block.getShapeCorners(shape);
				if(corners.size()==2){
					if(corners.get(0).isSameline(corners.get(1))){//����
						result.setXianShou(new RelativeResult(RelativeSurviveResult.LIVE, null));
						result.setHouShou(new RelativeResult(RelativeSurviveResult.DIE, null));
						result.setIndependent(false);
					}else{//����
						result.setXianShou(new RelativeResult(RelativeSurviveResult.LIVE, null));
						result.setHouShou(new RelativeResult(RelativeSurviveResult.LIVE, null));
						result.setIndependent(true);
					}
				} else{//==3������
					result.setXianShou(new RelativeResult(RelativeSurviveResult.LIVE, null));
					result.setHouShou(new RelativeResult(RelativeSurviveResult.LIVE, null));
					result.setIndependent(true);
				}
				/*
				 * ==3 there are two pattern(���ĺ�����), they have different result. hard
				 * coding is not a good solution.
				 * ģʽƥ��Ļ����ؼ�����α�
				 * 
				 * ����֮��Ķ�ɱ��һ��Ķ�ɱ��ͬ������һ����ʹ�Ե��˵���֮�ӣ�Ҳ�����ܺͼ�����������������
				 * ��ͨ���Ķ�ɱ���������Ŀ�����룬�������Է��������κ�һ����ɱ�鱻�ԣ���������һ��������
				 * �����ͨ��һ������£������������Ǳ���൱ǿ�󣬾ֲ�Ҳ��ȷ���˽������һ���䡣�������
				 * ����ܰ����Ӳ���ȵȡ�
				 * ���Ե���֮��Ĵ���Ҫ�ܼ��������ɡ����ۡ��ݱ��������ȻҲҪ������ʶ��ĳ���ֲ����е��۱�
				 * �����ġ�
				 */

			}
		}
		// result.setWaste(true);
		return result;
	}

	private RelativeSurviveResult getResult_FiveStones() {
		// TODO Auto-generated method stub
		return null;
	}
}
