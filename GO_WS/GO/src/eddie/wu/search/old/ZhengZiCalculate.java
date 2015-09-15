package eddie.wu.search.old;

import java.util.Arrays;

import org.apache.log4j.Logger;

import eddie.wu.domain.Block;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.GoBoardLadder;
import eddie.wu.domain.Point;
import eddie.wu.domain.StepMemo;
import eddie.wu.linkedblock.LocalResultOfZhengZi;

/**
 * 
 * 征子计算还是放在独立的类中较好。这样搜索算法才有可重用性。<br/>
 * 以后可以推广 到所有的敌对搜索，而不仅仅限于征子计算。 <br/>
 * 2005/08 遇到了深度克隆的问题. 用序列化实现深度克隆也遇到了问题. <br/>
 * Decision 1: <br/>
 * 用Momento模式来实现状态 * 的拷贝. 也许效率不如克隆,因为需要重新收集棋局信息. <br/>
 * 但是先保证程序可以运行更重要. 性能可以留待将来解决.
 * 
 * depends on linked.block.GoBoard
 * 
 * @author eddie
 * 
 */
public class ZhengZiCalculate {
	private static final Logger log = Logger.getLogger(ZhengZiCalculate.class);
	protected static final int SEARTHDEPTH = 120;

	protected static final int numberOfNodes = 100000; // 局面数,即展开的节点数.

	/**
	 * instance level fields
	 */

	byte zhengzijieguo[][] = new byte[127][2];

	/*
	 * 当前已有层数。
	 */
	protected byte cengshu = 0;

	/*
	 * 当前已有局面索引号。
	 */
	protected int lastJumianIndex = 0;

	/**
	 * 初始状态，层0只有有一个局面。他在栈中的编号是0
	 */
	protected Controller[] controllers;

	GoBoardLadder[] go = new GoBoardLadder[numberOfNodes];
	/*
	 * 生成与go对应局面所走点的横坐标。 生成与go对应局面所走点的纵坐标。 <br/> go[0] have no corresponding
	 * za,zb. go[0] move to go[1] with step (za[1,zb[1]). since there are
	 * several options in each state. we can not store the option/step in
	 * starting state.
	 */
	byte[] za = new byte[numberOfNodes];
	byte[] zb = new byte[numberOfNodes];
	GoBoardLadder temp;

	/**
	 * 
	 * @param state
	 *            需要计算征子的局面
	 * @param pointInTargetBlock
	 *            被征子的块的某一点.用于指定被征子的块.
	 * @return Points[] 用于表示正解.
	 */
	public Point[] jisuanzhengziWithClone(BoardColorState state,
			Point pointInTargetBlock) {
		byte[][] result = zhengziCalculate(state, pointInTargetBlock.getRow(),
				pointInTargetBlock.getColumn());
		if (log.isDebugEnabled())
			log.debug(Arrays.deepToString(result));
		Point[] points = convertArrayToPoints(boardSize, result);
		return points;
	}

	/**
	 * result[0][0]代表胜负结果
	 * 
	 * @param result
	 * @return
	 */
	public static Point[] convertArrayToPoints(int boardSize, byte[][] result) {
		Point[] points = new Point[result.length - 1];
		for (int i = 1; i < result.length; i++) {

			byte[] temp = result[i];
			if (temp[0] == 0 && temp[1] == 0)
				break;
			points[i - 1] = Point.getPoint(boardSize, temp[0], temp[1]);
			if (log.isDebugEnabled())
				log.debug(points[i - 1]);
		}
		return points;
	}

	public static byte[][] convertPointsToArray(Point[] points) {
		byte[][] result = new byte[points.length][2];
		for (int i = 0; i < points.length; i++) {
			if (points[i] == null)
				break;
			result[i][0] = points[i].getRow();
			result[i][1] = points[i].getColumn();

		}
		return result;
	}

	public byte[][] zhengziCalculate(BoardColorState state, Point point) {
		return zhengziCalculate(state, point.getRow(), point.getColumn());
	}

	private int boardSize;

	/**
	 * 
	 * 
	 * 
	 * @param state
	 *            需要计算征子的局面
	 * @param row
	 *            被征子的块的某一点行坐标
	 * @param column
	 *            被征子的块的某一点列坐标
	 * @return byte[][] 用于表示正解.
	 */
	public byte[][] zhengziCalculate(BoardColorState state, byte row,
			byte column) {
		boardSize = state.boardSize;
		controllers = initControllersMaxMin();
		controllers[0].setIndexForJuMian(0);
		controllers[0].setNumberOfJuMian(1);

		// 做活主体的块。
		GoBoardLadder linkedBlockGoBoard = new GoBoardLadder(state);
		// linkedBlockGoBoard.generateHighLevelState();

		Block blockToBeEaten = linkedBlockGoBoard.getBlock(row, column);
		if (log.isDebugEnabled()) {
			log.debug("block: " + blockToBeEaten);
		}

		// if (blockToBeEaten.getColor() == ColorUtil.BLACK) {
		// linkedBlockGoBoard.setShoushu(1);// ?
		// if (log.isDebugEnabled()) {
		// log.debug("要逃征子的棋块为黑色，轮白方走能否征子？");
		// }
		// } else if (blockToBeEaten.getColor() == ColorUtil.WHITE) {
		// linkedBlockGoBoard.setShoushu(0);// ?
		// if (log.isDebugEnabled()) {
		// log.debug("要逃跑的棋块为白色，轮黑方走能否征子？");
		// }
		// }

		/*
		 * stack for all the Ju Mian
		 */

		log.debug("clone in index:" + 0);
		go[0] = this.getGoBoardLadderCopy(linkedBlockGoBoard);
		// linkedBlockGoBoard itself will be forwarded.

		/*
		 * 2.开始计算。 第一层循环：展开最后一个局面。
		 */

		while (true) {

			if (cengshu >= (SEARTHDEPTH - 1)) {
				if (log.isDebugEnabled()) {
					log.debug("搜索到" + SEARTHDEPTH + "层，仍没有结果，返回不精确结果");
				}
				return zhengzijieguo;
			} else {
				temp = (go[lastJumianIndex]).getGoBoardLadderCopy();
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
				controllers[cengshu].setIndexForJuMian(lastJumianIndex + 1);

				if (log.isDebugEnabled()) {
					log.debug("新层的开始局面索引为：" + (lastJumianIndex + 1));
				}
			}

			log.debug("clone old board/state in index:" + lastJumianIndex);

			// temp.initPoints();
			// 要展开的局面。
			blockToBeEaten = temp.getBoardPoint(row, column).getBlock();
			// 征子方候选点数为
			if (controllers[cengshu].getWhoseTurn() == Constant.MIN) {
				if (log.isDebugEnabled()) {
					log.debug("当前层数？" + cengshu);
					log.debug("当前层轮谁走？" + "MIN");
					log.debug("在上一层由MAX走得到该层");
				}

				LocalResultOfZhengZi result = temp
						.getLocalResultOfZhengZiForMAX(row, column);
				if (log.isDebugEnabled()) {
					log.debug("LocalResultOfZhengZ= " + result);
				}
				if (result.isSelfFail()) {
					// 返回，一般是征子方无子可下。
					if (log.isDebugEnabled()) {
						log.debug("有效点为0");

					}
					// TODO?st[cengshu][0] = 0;

					if (cengshu == 1) { // 征子方直接无子可下，
						zhengzijieguo[0][0] = -127;
						return zhengzijieguo;
					}

					while (true) {
						cengshu -= 2; // 倒数两层已经确定。减后的层数需要重新展开
						if (cengshu == -1) {
							zhengzijieguo[0][0] = -127;
							return zhengzijieguo;
						}
						// for (byte lins = 2; st[lins][0] != 0; lins++) {
						// if (log.isDebugEnabled()) {
						// log.debug("点为:(" + za[st[lins][0] - 1]
						// + "," + zb[st[lins][0] - 1] + ")");
						// }
						// zhengzijieguo[lins - 1][0] = za[st[lins][0] - 1];
						// zhengzijieguo[lins - 1][1] = zb[st[lins][0] - 1];
						//
						// }
						//
						// return zhengzijieguo;
						// }
						controllers[cengshu].decreaseJuMian();
						if (controllers[cengshu].getNumberOfJuMian() != 0) {
							lastJumianIndex = controllers[cengshu]
									.getLastIndexForJumian();
							cleanJuMianAfter(lastJumianIndex, go);
							break;
						}
					}
				} else if (result.isTie()) {// 继续征子计算

					controllers[cengshu].setNumberOfJuMian(result
							.getNumberOfCandidates());

					int count = 0;
					for (java.util.Iterator<GoBoard> iterator = result
							.getCandidateJuMians().iterator(); iterator
							.hasNext();) {
						count++;
						go[lastJumianIndex + count] = (GoBoardLadder) iterator
								.next();
						if (go[lastJumianIndex + count] == null) {
							throw new RuntimeException(
									"go[jumianshu+count]==null");
						}
					}
					if (log.isDebugEnabled())
						log.debug("count=" + count);
					lastJumianIndex += result.getNumberOfCandidates();
					if (log.isDebugEnabled())
						log.debug("jumianshu=" + lastJumianIndex);
				} else if (result.isSelfSuccess()) {// impossible
					// 征子成功只能是被征子方不能长气。
				}
				// if min
			} else if (controllers[cengshu].getWhoseTurn() == Constant.MAX) {// 被征子方候选点数
				if (log.isDebugEnabled()) {
					log.debug("当前层数？" + cengshu);
					log.debug("当前层轮谁走？" + "MAX");
					log.debug("上一层轮谁走？" + "MIN");

					log.debug("在上一层由MIN走得到该层");
				}

				// 返回候选点.或者返回结果

				LocalResultOfZhengZi result = temp
						.getLocalResultOfZhengZiForMIN(row, column);
				if (log.isDebugEnabled()) {
					log.debug("LocalResultOfZhengZi=" + result);
				}
				if (result.isSelfSuccess()) {
					cengshu -= 1; // 倒数两层已经确定。减后的层数需要重新展开
					controllers[cengshu].decreaseJuMian();
					if (controllers[cengshu].getNumberOfJuMian() != 0) {
						lastJumianIndex = controllers[cengshu]
								.getLastIndexForJumian();

					} else {
						while (true) {
							cengshu -= 2; // 倒数两层已经确定。减后的层数需要重新展开
							if (cengshu == -1) {
								zhengzijieguo[0][0] = -127;
								return zhengzijieguo;
							}
							lastJumianIndex = dealWithCeng(lastJumianIndex,
									cengshu, controllers);
						}
					}
				} else if (result.isSelfFail()) {
					while (true) {
						cengshu -= 2; // 倒数两层已经确定。减后的层数需要重新展开
						if (cengshu == 0) {
							zhengzijieguo[0][0] = 127;
							int count = 0;
							for (StepMemo step : temp.getStepHistory()
									.getAllSteps()) {
								count++;
								zhengzijieguo[count][0] = step
										.getCurrentStepPoint().getRow();
								zhengzijieguo[count][1] = step
										.getCurrentStepPoint().getColumn();
							}
							// go[lastJumianIndex].getStepHistory().hui;
							// for(int j=0;j<512;j++){
							// if(log.isDebugEnabled())
							// log.debug("["+hui[j][0]+","+hui[j][1]+"];");
							// }
							return zhengzijieguo;
						}
						//
						// byte lins = 0;
						// for (lins = 2; st[lins][0] != 0; lins++) {
						// if (log.isDebugEnabled()) {
						// log.debug("点为:(" + za[st[lins][0] - 1]
						// + "," + zb[st[lins][0] - 1] + ")");
						// } //
						// this.cgcl(za[st[lins][0]-1],zb[st[lins][0]-1]);
						// zhengzijieguo[lins - 1][0] = za[st[lins][0] - 1];
						// zhengzijieguo[lins - 1][1] = zb[st[lins][0] - 1];
						//
						// }
						// zhengzijieguo[0][1] = (byte) (lins - 2);
						// return zhengzijieguo;
						// }

						controllers[cengshu]
								.setNumberOfJuMian(controllers[cengshu]
										.getNumberOfJuMian() - 1);
						if (controllers[cengshu].getNumberOfJuMian() != 0) {
							lastJumianIndex = controllers[cengshu]
									.getLastIndexForJumian();
							// st[cengshu - 2][3] = 127;
							this.cleanJuMianAfter(lastJumianIndex, go);
							break;
						}
					}

				} else if (result.isTie()) {
					controllers[cengshu].setNumberOfJuMian(result
							.getNumberOfCandidates());
					int count = 0;
					for (java.util.Iterator<GoBoard> iterator = result
							.getCandidateJuMians().iterator(); iterator
							.hasNext();) {
						count++;
						go[lastJumianIndex + count] = (GoBoardLadder) iterator
								.next();
						if (go[lastJumianIndex + count] == null) {
							throw new RuntimeException(
									"go[jumianshu+count-1]==null");
						}
					}
					if (log.isDebugEnabled())
						log.debug("count=" + count);
					lastJumianIndex += result.getNumberOfCandidates();
					if (log.isDebugEnabled())
						log.debug("jumianshu=" + lastJumianIndex);
				}
			} // max

		} // while
	}

	/**
	 * 清除当前局面之后的局面
	 * 
	 * @param jumianshu
	 * @param go
	 */
	void cleanJuMianAfter(int jumianshu, GoBoard[] go) {
		for (int dd = jumianshu + 1; dd < this.numberOfNodes
				&& go[jumianshu] != null; dd++) {
			go[jumianshu] = null;
		}
	}

	protected int dealWithCeng(int jumianshu, byte cengshu,
			Controller[] controllers) {
		controllers[cengshu].setNumberOfJuMian(controllers[cengshu]
				.getNumberOfJuMian() - 1);
		if (controllers[cengshu].getNumberOfJuMian() != 0) {
			jumianshu = controllers[cengshu].getLastIndexForJumian();

		}
		return jumianshu;
	}

	/*
	 * 限制了搜索深度。
	 */
	protected Controller[] initControllersMaxMin() {
		Controller[] controllers = new Controller[SEARTHDEPTH];
		Controller controller;

		for (byte i = 0; i < controllers.length; i++) {
			controller = new Controller();
			controller.setWhoseTurn(Constant.MAX);
			controllers[i] = controller;
			i++;
			controller = new Controller();
			controller.setWhoseTurn(Constant.MIN);
			controllers[i] = controller;

		}
		return controllers;
	}

	Controller[] initControllersMinMax() {
		Controller[] controllers = new Controller[SEARTHDEPTH];
		Controller controller;

		for (byte i = 0; i < controllers.length; i++) {
			controller = new Controller();
			controller.setWhoseTurn(Constant.MIN);
			controllers[i] = controller;
			i++;
			controller = new Controller();
			controller.setWhoseTurn(Constant.MAX);
			controllers[i] = controller;

		}
		return controllers;
	}

	GoBoardLadder getGoBoardLadderCopy(GoBoardLadder goBoard) {
		return goBoard.getGoBoardLadderCopy();
	}
}
