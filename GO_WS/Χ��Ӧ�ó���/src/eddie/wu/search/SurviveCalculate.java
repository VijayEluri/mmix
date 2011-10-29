package eddie.wu.search;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.domain.Block;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.survive.RelativeSurviveResult;
import eddie.wu.domain.survive.Result;
import eddie.wu.domain.survive.SurviveResult;

/**
 * ����ļ�������ʶ��һ�����⡣ֱ�����Է����֮���Ϊ��ʽ�ϵ�˫�Ҫȫ��������ĸ������⣬��Ҫ���� ˫��ĸ��������Ҳ�ǳ���Ҫ�漰�����⡣
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
		Result houShou = new SurviveCalculate().surviveCalculate(stateIn,
				pointInTargetBlock, false);
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
		cengshu = 0;

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
		 * 2.��ʼ���㡣 ��һ��ѭ����չ�����һ�����档 decide to completely redo it. ��δȷ��״̬����Ҫ��������.
		 */

		while (true) {

			// } else {
			temp = (go[lastJumianIndex]).getGoBoardCopy();
			log.debug("clone old board/state in index:" + lastJumianIndex);
			if (cengshu != 0) {
				Point lastPoint = temp.getLastPoint();
				zhengzijieguo[cengshu][0] = (byte) lastPoint.getRow();
				zhengzijieguo[cengshu][1] = (byte) lastPoint.getColumn();
				zhengzijieguo[0][1] = cengshu;
			}

			// }

			blockToBeEaten = temp.getBlock(pointInTargetBlock);
			// ���ӷ���ѡ����Ϊ
			// if (controllers[cengshu].getWhoseTurn() == MIN) {
			if (log.isDebugEnabled()) {
				log.debug("��ǰ������" + cengshu);
				log.debug("��һ��Ϊ" + (cengshu + 1));
				log.debug("����һ����" + controllers[cengshu].getWhoseTurn()
						+ "�ߵõ��ò�");
			}

			LocalResult result = null;
			if (controllers[cengshu].getWhoseTurn() == MAX) {
				result = temp.getLocalResultSelfFirst(pointInTargetBlock);
			}
			if (controllers[cengshu].getWhoseTurn() == MIN) {
				result = temp.getLocalResultEnemyFirst(pointInTargetBlock);
			}
			if (log.isDebugEnabled()) {
				log.debug("LocalResultOfZhengZ= " + result);
			}

			if (result.getScore()==0) {// ��������
				if (cengshu >= (SEARTHDEPTH - 1)) {
					if (log.isDebugEnabled()) {
						log.debug("������" + SEARTHDEPTH + "�㣬��û�н�������ز���ȷ���");
					}
					throw new RuntimeException("������" + SEARTHDEPTH + "�㣬��û�н��");
				}

				/*
				 * �²�Ĳ�š� ��ǰ�������ڵĲ㣬�µĲ�(dang qian de gong zuo suo zai de ceng)
				 * ��0��Ԥ�ȶ����(ceng 0 shi yu ding yi de.)
				 */
				cengshu++;
				/*
				 * �²�Ŀ�ʼ�㡣 ��ǰ��ľ�������￪ʼ���(dang qian ceng de ju mian cong zhe li
				 * kaishi bian hao)
				 */
				controllers[cengshu].setIndexForJuMian(lastJumianIndex + 1);

				if (log.isDebugEnabled()) {
					log.debug("\n\n�µĵ�ǰ����Ϊ��" + cengshu);
					log.debug("�²�Ŀ�ʼ��������Ϊ��" + (lastJumianIndex + 1));
				}

				controllers[cengshu].setNumberOfJuMian(result
						.getNumberOfCandidates());

				int count = 0;
				for (Candidate candidate : result.getCandidates()) {
					count++;
					go[lastJumianIndex + count] = candidate.getGoBoard();
					if (go[lastJumianIndex + count] == null) {
						throw new RuntimeException("go[jumianshu+count]==null");
					}
				}
				System.out.println("count=" + count);
				lastJumianIndex += result.getNumberOfCandidates();
				System.out.println("jumianshu=" + lastJumianIndex);
			}
			if (controllers[cengshu].getWhoseTurn() == MIN) {
				if (result.isTargetBlockSucceed()==false) {
					if (log.isDebugEnabled()) {
						log.debug("��Ч��Ϊ0");
					}

					if (cengshu == 0) { // ���ֱ�����ӿ��£�����ɱ��
						if (result.getCandidates().isEmpty() == false) {
							simpleResult.setPoint(result.getCandidates()
									.get(0).getPoint());
						}
						simpleResult.setSurvive(result.getScore());
						return simpleResult;
					}

					while (true) {
						//cengshu -= 1; // ���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��
						if (cengshu <= -1) {
							if (result.getCandidates().isEmpty() == false) {
								simpleResult.setPoint(result
										.getCandidates().get(0).getPoint());
							}
							simpleResult.setSurvive(result.getScore());
							return simpleResult;
						}

						controllers[cengshu].decreaseJuMian();
						if (controllers[cengshu].getNumberOfJuMian() != 0) {
							lastJumianIndex = controllers[cengshu]
									.getLastIndexForJumian();
							cleanJuMianAfter(lastJumianIndex, go);
							break;
						}else{
							cengshu -= 2; // ���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��
						}
					}
				} else if (result.isTargetBlockSucceed()) {

					while (true) {
						cengshu -= 1; // ���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��
						if (cengshu <= -1) {
							simpleResult.setSurvive(result.getScore());
							return simpleResult;
						}

						controllers[cengshu].decreaseJuMian();
						if (controllers[cengshu].getNumberOfJuMian() != 0) {
							lastJumianIndex = controllers[cengshu]
									.getLastIndexForJumian();
							cleanJuMianAfter(lastJumianIndex, go);
							break;
						}
					}

					//
				}
			}
			// // } else if (controllers[cengshu].getWhoseTurn() == MAX) {//
			// // �����ӷ���ѡ����
			// if (log.isDebugEnabled()) {
			// log.debug("��ǰ������" + cengshu);
			// log.debug("��ǰ����˭�ߣ�" + "MAX");
			// log.debug("��һ����˭�ߣ�" + "MIN");
			//
			// log.debug("����һ����MIN�ߵõ��ò�");
			// }
			//
			// // ���غ�ѡ��.���߷��ؽ��
			//
			// if (log.isDebugEnabled()) {
			// log.debug("LocalResultOfZhengZi=" + result);
			// }
			// if (result.isSelfSuccess()) {
			// cengshu -= 1; // ���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��
			// controllers[cengshu].decreaseJuMian();
			// if (controllers[cengshu].getNumberOfJuMian() != 0) {
			// jumianIndex = controllers[cengshu].getLastIndexForJumian();
			//
			// } else {
			// while (true) {
			// cengshu -= 2; // ���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��
			// if (cengshu == -1) {
			// simpleResult.setSurvive(result.getScore());
			// return simpleResult;
			// }
			// jumianIndex = dealWithCeng(jumianIndex, cengshu,
			// controllers);
			// }
			// }
			// } else if (result.isSelfFail()) {
			// while (true) {
			// cengshu -= 2; // ���������Ѿ�ȷ��������Ĳ�����Ҫ����չ��
			// if (cengshu == 0) {
			// simpleResult.setSurvive(result.getScore());
			// return simpleResult;
			// }
			// //
			// // byte lins = 0;
			// // for (lins = 2; st[lins][0] != 0; lins++) {
			// // if (log.isDebugEnabled()) {
			// // log.debug("��Ϊ:(" + za[st[lins][0] - 1]
			// // + "," + zb[st[lins][0] - 1] + ")");
			// // } //
			// // this.cgcl(za[st[lins][0]-1],zb[st[lins][0]-1]);
			// // zhengzijieguo[lins - 1][0] = za[st[lins][0] - 1];
			// // zhengzijieguo[lins - 1][1] = zb[st[lins][0] - 1];
			// //
			// // }
			// // zhengzijieguo[0][1] = (byte) (lins - 2);
			// // return zhengzijieguo;
			// // }
			//
			// controllers[cengshu].setNumberOfJuMian(controllers[cengshu]
			// .getNumberOfJuMian() - 1);
			// if (controllers[cengshu].getNumberOfJuMian() != 0) {
			// jumianIndex = controllers[cengshu]
			// .getLastIndexForJumian();
			// // st[cengshu - 2][3] = 127;
			// this.cleanJuMianAfter(jumianIndex, go);
			// break;
			// }
			// }
			//
			// } else if (result.isTie()) {
			// controllers[cengshu].setNumberOfJuMian(result
			// .getNumberOfCandidates());
			// int count = 0;
			// for (java.util.Iterator<GoBoard> iterator = result
			// .getCandidateJuMians().iterator(); iterator.hasNext();) {
			// count++;
			// go[jumianIndex + count] = (GoBoard) iterator.next();
			// if (go[jumianIndex + count] == null) {
			// throw new RuntimeException(
			// "go[jumianshu+count-1]==null");
			// }
			// }
			// System.out.println("count=" + count);
			// jumianIndex += result.getNumberOfCandidates();
			// System.out.println("jumianshu=" + jumianIndex);
			// }
			// } // max

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
