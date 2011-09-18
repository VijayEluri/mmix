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
 * 死活的计算中意识到一个问题。直三被对方点过之后成为形式上的双活，要全面解决死活的概念问题，需要考虑
 * 双活的概念。正好这也是迟早要涉及的问题。
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
	 *            需要计算死活的局面
	 * @param pointInTargetBlock
	 *            棋块的某一点.用于指定需要计算的块.
	 * @return SurviveResult 用于表示结果（先后手都考虑到）.
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

		// 做活主体的块。
		GoBoard linkedBlockGoBoard = new GoBoard(state);
		linkedBlockGoBoard.generateHighLevelState();

		Block blockToBeEaten = linkedBlockGoBoard.getBlock(pointInTargetBlock);
		if (log.isDebugEnabled()) {
			log.debug("block to be eaten: " + blockToBeEaten);
		}

		log.debug("clone in index:" + 0);
		go[0] = linkedBlockGoBoard;

		/*
		 * 2.开始计算。 第一层循环：展开最后一个局面。
		 */

		while (true) {

			if (cengshu >= (SEARTHDEPTH - 1)) {
				if (log.isDebugEnabled()) {
					log.debug("搜索到" + SEARTHDEPTH + "层，仍没有结果，返回不精确结果");
				}
				throw new RuntimeException("搜索到" + SEARTHDEPTH + "层，仍没有结果");

			} else {
				temp = (go[jumianIndex]).getGoBoardCopy();
				if (cengshu != 0) {
					Point lastPoint = temp.getLastPoint();
					zhengzijieguo[cengshu][0] = (byte) lastPoint.getRow();
					zhengzijieguo[cengshu][1] = (byte) lastPoint.getColumn();
					zhengzijieguo[0][1] = cengshu;
				}
				/*
				 * 新层的层号。 当前工作所在的层，新的层(dang qian de gong zuo suo zai de ceng)
				 * 层0是预先定义的(ceng 0 shi yu ding yi de.)
				 */
				cengshu++;
				if (log.isDebugEnabled()) {
					log.debug("\n\n新的当前层数为：" + cengshu);
				}
				/*
				 * 新层的开始点。 当前层的局面从这里开始编号(dang qian ceng de ju mian cong zhe li
				 * kaishi bian hao)
				 */
				controllers[cengshu].setIndexForJuMian(jumianIndex + 1);

				if (log.isDebugEnabled()) {
					log.debug("新层的开始局面索引为：" + (jumianIndex + 1));
				}
			}

			log.debug("clone old board/state in index:" + jumianIndex);

			blockToBeEaten = temp.getBlock(pointInTargetBlock);
			// 征子方候选点数为
			if (controllers[cengshu].getWhoseTurn() == MIN) {
				if (log.isDebugEnabled()) {
					log.debug("当前层数？" + cengshu);
					log.debug("当前层轮谁走？" + "MIN");
					log.debug("在上一层由MAX走得到该层");
				}

				LocalResultOfZhengZi result = temp
						.getLocalResultSelfFirst(pointInTargetBlock);
				if (log.isDebugEnabled()) {
					log.debug("LocalResultOfZhengZ= " + result);
				}
				if (result.isSelfFail()) {					
					if (log.isDebugEnabled()) {
						log.debug("有效点为0");
					}				

					if (cengshu == 1) { // 征子方直接无子可下，
						simpleResult.setSurvive(result.getScore());
						return simpleResult;
					}

					while (true) {
						cengshu -= 2; // 倒数两层已经确定。减后的层数需要重新展开
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
				} else if (result.isTie()) {// 继续计算

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
						cengshu -= 2; // 倒数两层已经确定。减后的层数需要重新展开
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
