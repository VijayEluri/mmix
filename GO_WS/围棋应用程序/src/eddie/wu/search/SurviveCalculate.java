package eddie.wu.search;

import org.apache.log4j.Logger;

import eddie.wu.domain.Block;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoardSurvive;
import eddie.wu.domain.Point;
import eddie.wu.domain.survive.Result;
import eddie.wu.domain.survive.SurviveResult;

/**obsolete.
 * <br/>
 * 死活的计算中意识到一个问题。直三被对方点过之后成为形式上的双活，<br/>
 * 要全面解决死活的概念问题，需要考虑 双活的概念。正好这也是迟早要涉及的问题。
 * 
 * derived from ZhengZiCalculate. the difference is that. for ZhengZi, two side
 * may have different logic. for surviving, the logic looks similar.<br/>
 * 所谓敌之要点即我之要点.(是否可以利用我们先后受都计算的特点来提供这种敌之要点的信息.)
 * 
 * finally, the survive will also have similar issue. the behavior of two side
 * are different.<br/>
 * 做活一方的手段和杀棋一方的手段是不同的.<br/>
 * @deprecated
 */
public class SurviveCalculate extends ZhengZiCalculate {
	private static final Logger  log = Logger.getLogger(SurviveCalculate.class);
	/**
	 * overwrite the definition in supper class.
	 */
	GoBoardSurvive temp;
	GoBoardSurvive[] go = new GoBoardSurvive[numberOfNodes];

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
		cengshu = 0;

		// 做活主体的块。
		GoBoardSurvive linkedBlockGoBoard = new GoBoardSurvive(state);
		// linkedBlockGoBoard.generateHighLevelState();

		Block blockToBeEaten = linkedBlockGoBoard.getBlock(pointInTargetBlock);
		if (log.isDebugEnabled()) {
			log.debug("block to be eaten: " + blockToBeEaten);
		}

		log.debug("clone in index:" + 0);
		go[0] = linkedBlockGoBoard;

		/*
		 * 2.开始计算。 第一层循环：展开最后一个局面。 decide to completely redo it. 有未确定状态才需要继续搜索.
		 */

		while (true) {

			// } else {
			temp = (go[lastJumianIndex]).getGoBoardSurviveCopy();
			log.debug("clone old board/state in index:" + lastJumianIndex);
			if (cengshu != 0) {
				Point lastPoint = temp.getLastPoint();
				zhengzijieguo[cengshu][0] = (byte) lastPoint.getRow();
				zhengzijieguo[cengshu][1] = (byte) lastPoint.getColumn();
				zhengzijieguo[0][1] = cengshu;
			}

			// }

			blockToBeEaten = temp.getBlock(pointInTargetBlock);
			// 征子方候选点数为
			// if (controllers[cengshu].getWhoseTurn() == MIN) {
			if (log.isDebugEnabled()) {
				log.debug("当前层数？" + cengshu);
				log.debug("下一层为" + (cengshu + 1));
				log.debug("在上一层由" + controllers[cengshu].getWhoseTurn()
						+ "走得到该层");
			}

			LocalResult result = null;
			if (controllers[cengshu].getWhoseTurn() == Constant.MAX) {
				result = temp.getLocalResultSelfFirst(pointInTargetBlock);
			}
			if (controllers[cengshu].getWhoseTurn() == Constant.MIN) {
				result = temp.getLocalResultEnemyFirst(pointInTargetBlock);
			}
			if (log.isDebugEnabled()) {
				log.debug("LocalResultOfZhengZ= " + result);
			}

			if (result.getScore() == 0) {// 继续计算
				if (cengshu >= (SEARTHDEPTH - 1)) {
					if (log.isDebugEnabled()) {
						log.debug("搜索到" + SEARTHDEPTH + "层，仍没有结果，返回不精确结果");
					}
					throw new RuntimeException("搜索到" + SEARTHDEPTH + "层，仍没有结果");
				}

				/*
				 * 新层的层号。 当前工作所在的层，新的层(dang qian de gong zuo suo zai de ceng)
				 * 层0是预先定义的(ceng 0 shi yu ding yi de.)
				 */
				cengshu++;
				/*
				 * 新层的开始点。 当前层的局面从这里开始编号(dang qian ceng de ju mian cong zhe li
				 * kaishi bian hao)
				 */
				controllers[cengshu].setIndexForJuMian(lastJumianIndex + 1);

				if (log.isDebugEnabled()) {
					log.debug("\n\n新的当前层数为：" + cengshu);
					log.debug("新层的开始局面索引为：" + (lastJumianIndex + 1));
				}

				controllers[cengshu].setNumberOfJuMian(result
						.getNumberOfCandidates());

				int count = 0;
				for (Candidate candidate : result.getCandidates()) {
					count++;
					go[lastJumianIndex + count] = (GoBoardSurvive) candidate
							.getGoBoard();
					if (go[lastJumianIndex + count] == null) {
						throw new RuntimeException("go[jumianshu+count]==null");
					}
				}
				if(log.isDebugEnabled()) log.debug("count=" + count);
				lastJumianIndex += result.getNumberOfCandidates();
				if(log.isDebugEnabled()) log.debug("jumianshu=" + lastJumianIndex);
			}
			if (controllers[cengshu].getWhoseTurn() == Constant.MIN) {
				if (result.isTargetBlockSucceed() == false) {
					if (log.isDebugEnabled()) {
						log.debug("有效点为0");
					}

					if (cengshu == 0) { // 做活方直接无子可下，或者杀方
						if (result.getCandidates().isEmpty() == false) {
//							simpleResult.setPoint(result.getCandidates().get(0)
//									);
						}
						simpleResult.setSurvive(result.getScore());
						return simpleResult;
					}

					while (true) {
						// cengshu -= 1; // 倒数两层已经确定。减后的层数需要重新展开
						if (cengshu <= -1) {
							if (result.getCandidates().isEmpty() == false) {
//								simpleResult.setPoint(result.getCandidates()
//										.get(0).getPoint());
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
						} else {
							cengshu -= 2; // 倒数两层已经确定。减后的层数需要重新展开
						}
					}
				} else if (result.isTargetBlockSucceed()) {

					while (true) {
						cengshu -= 1; // 倒数两层已经确定。减后的层数需要重新展开
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
			// // 被征子方候选点数
			// if (log.isDebugEnabled()) {
			// log.debug("当前层数？" + cengshu);
			// log.debug("当前层轮谁走？" + "MAX");
			// log.debug("上一层轮谁走？" + "MIN");
			//
			// log.debug("在上一层由MIN走得到该层");
			// }
			//
			// // 返回候选点.或者返回结果
			//
			// if (log.isDebugEnabled()) {
			// log.debug("LocalResultOfZhengZi=" + result);
			// }
			// if (result.isSelfSuccess()) {
			// cengshu -= 1; // 倒数两层已经确定。减后的层数需要重新展开
			// controllers[cengshu].decreaseJuMian();
			// if (controllers[cengshu].getNumberOfJuMian() != 0) {
			// jumianIndex = controllers[cengshu].getLastIndexForJumian();
			//
			// } else {
			// while (true) {
			// cengshu -= 2; // 倒数两层已经确定。减后的层数需要重新展开
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
			// cengshu -= 2; // 倒数两层已经确定。减后的层数需要重新展开
			// if (cengshu == 0) {
			// simpleResult.setSurvive(result.getScore());
			// return simpleResult;
			// }
			// //
			// // byte lins = 0;
			// // for (lins = 2; st[lins][0] != 0; lins++) {
			// // if (log.isDebugEnabled()) {
			// // log.debug("点为:(" + za[st[lins][0] - 1]
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
			// if(log.isDebugEnabled()) log.debug("count=" + count);
			// jumianIndex += result.getNumberOfCandidates();
			// if(log.isDebugEnabled()) log.debug("jumianshu=" + jumianIndex);
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
