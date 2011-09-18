package eddie.wu.search;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.domain.Block;
import eddie.wu.domain.Point;
import eddie.wu.domain.Result;
import eddie.wu.domain.SurviveResult;
import eddie.wu.linkedblock.BoardColorState;
import eddie.wu.linkedblock.Controller;
import eddie.wu.linkedblock.GoBoard;
import eddie.wu.linkedblock.LocalResultOfZhengZi;

/**
 * ����ļ�������ʶ��һ�����⡣ֱ�����Է����֮���Ϊ��ʽ�ϵ�˫�Ҫȫ��������ĸ������⣬��Ҫ����
 * ˫��ĸ��������Ҳ�ǳ���Ҫ�漰�����⡣
 * 
 * derived from ZhengZiCalculate. the difference is that. for ZhengZi, two side
 * may have different logic. for surviving, the logic looks similar.
 * 
 * finally, the survive will also have similar issue. the behavior of two side
 * are different.
 */
public class SurviveCalculate extends ZhengZiCalculate {
	private static final Log log = LogFactory.getLog(SurviveCalculate.class);

	public SurviveResult surviveCalculate(BoardColorState stateIn,
			Point pointInTargetBlock) {
		SurviveResult result = new SurviveResult();
		Result xianShou = this.surviveCalculate(stateIn, pointInTargetBlock,
				true);
		Result houShou = this.surviveCalculate(stateIn, pointInTargetBlock,
				false);
		result.setHouShou(houShou);
		result.setXianShou(xianShou);
		return result;
	}

	/**
	 * 
	 * @param state
	 *            ��Ҫ��������ľ���
	 * @param pointInTargetBlock
	 *            ����ĳһ��.����ָ����Ҫ����Ŀ�.
	 * @return SurviveResult ���ڱ�ʾ������Ⱥ��ֶ����ǵ���.
	 */
	public Result surviveCalculate(BoardColorState stateIn,
			Point pointInTargetBlock, boolean selfFirst) {
		Result simpleResult = new Result();
		byte[][] state = stateIn.getMatrixState();
		if (selfFirst) {
			controllers = initControllersSelfFirst();
		} else {
			controllers = initControllersEnemyFirst();
		}
		controllers[0].setIndexForJuMian(0);
		controllers[0].setNumberOfJuMian(1);

		// ��������Ŀ顣
		GoBoard linkedBlockGoBoard = new GoBoard(state);
		linkedBlockGoBoard.generateHighLevelState();

		Block blockToBeEaten = linkedBlockGoBoard.getBlock(pointInTargetBlock);
		if (log.isDebugEnabled()) {
			log.debug("block to be eaten: " + blockToBeEaten);
		}

		log.debug("clone in index:" + 0);
		go[0] = linkedBlockGoBoard;

		/*
		 * 2.��ʼ���㡣 ��һ��ѭ����չ�����һ�����档
		 */

		while (true) {

			if (cengshu >= (SEARTHDEPTH - 1)) {
				if (log.isDebugEnabled()) {
					log.debug("������" + SEARTHDEPTH + "�㣬��û�н�������ز���ȷ���");
				}
				throw new RuntimeException("������" + SEARTHDEPTH + "�㣬��û�н��");

			} else {
				temp = (go[jumianIndex]).getGoBoardCopy();
				if (cengshu != 0) {
					Point lastPoint = temp.getLastPoint();
					zhengzijieguo[cengshu][0] = (byte) lastPoint.getRow();
					zhengzijieguo[cengshu][1] = (byte) lastPoint.getColumn();
					zhengzijieguo[0][1] = cengshu;
				}
				/*
				 * �²�Ĳ�š� ��ǰ�������ڵĲ㣬�µĲ�(dang qian de gong zuo suo zai de ceng)
				 * ��0��Ԥ�ȶ����(ceng 0 shi yu ding yi de.)
				 */
				cengshu++;
				if (log.isDebugEnabled()) {
					log.debug("\n\n�µĵ�ǰ����Ϊ��" + cengshu);
				}
				/*
				 * �²�Ŀ�ʼ�㡣 ��ǰ��ľ�������￪ʼ���(dang qian ceng de ju mian cong zhe li
				 * kaishi bian hao)
				 */
				controllers[cengshu].setIndexForJuMian(jumianIndex + 1);

				if (log.isDebugEnabled()) {
					log.debug("�²�Ŀ�ʼ��������Ϊ��" + (jumianIndex + 1));
				}
			}

			log.debug("clone old board/state in index:" + jumianIndex);

			blockToBeEaten = temp.getBlock(pointInTargetBlock);
			// ���ӷ���ѡ����Ϊ
			if (controllers[cengshu].getWhoseTurn() == MIN) {
				if (log.isDebugEnabled()) {
					log.debug("��ǰ������" + cengshu);
					log.debug("��ǰ����˭�ߣ�" + "MIN");
					log.debug("����һ����MAX�ߵõ��ò�");
				}

				LocalResultOfZhengZi result = temp
						.getLocalResultSelfFirst(pointInTargetBlock);
				if (log.isDebugEnabled()) {
					log.debug("LocalResultOfZhengZ= " + result);
				}
				if (result.isSelfFail()) {					
					if (log.isDebugEnabled()) {
						log.debug("��Ч��Ϊ0");
					}				

					if (cengshu == 1) { // ���ӷ�ֱ�����ӿ��£�
						simpleResult.setSurvive(result.getScore());
						return simpleResult;
					}

					while (true) {
						cengshu -= 2; // ���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��
						if (cengshu == -1) {
							simpleResult.setSurvive(result.getScore());
							return simpleResult;
						}
						
						controllers[cengshu].decreaseJuMian();
						if (controllers[cengshu].getNumberOfJuMian() != 0) {
							jumianIndex = controllers[cengshu]
									.getLastIndexForJumian();
							cleanJuMianAfter(jumianIndex, go);
							break;
						}
					}
				} else if (result.isTie()) {// ��������

					controllers[cengshu].setNumberOfJuMian(result
							.getNumberOfCandidates());

					int count = 0;
					for (java.util.Iterator<GoBoard> iterator = result
							.getCandidateJuMians().iterator(); iterator
							.hasNext();) {
						count++;
						go[jumianIndex + count] = (GoBoard) iterator.next();
						if (go[jumianIndex + count] == null) {
							throw new RuntimeException(
									"go[jumianshu+count]==null");
						}
					}
					System.out.println("count=" + count);
					jumianIndex += result.getNumberOfCandidates();
					System.out.println("jumianshu=" + jumianIndex);
				} else if (result.isSelfSuccess()) {
					
					while (true) {
						cengshu -= 2; // ���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��
						if (cengshu == -1) {
							simpleResult.setSurvive(result.getScore());
							return simpleResult;
						}
						
						controllers[cengshu].decreaseJuMian();
						if (controllers[cengshu].getNumberOfJuMian() != 0) {
							jumianIndex = controllers[cengshu]
									.getLastIndexForJumian();
							cleanJuMianAfter(jumianIndex, go);
							break;
						}
					}
					
//				
				}
				
			}

		} // while
	}

	/**
	 * self means the side/color of the target block. make live -> high positive
	 * score; max means live die -> negative score; min means die
	 * 
	 * @return
	 */
	private Controller[] initControllersSelfFirst() {
		return this.initControllersMaxMin();
	}

	private Controller[] initControllersEnemyFirst() {
		return this.initControllersMinMax();
	}
}
