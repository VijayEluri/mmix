package eddie.wu.search;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.domain.Block;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.linkedblock.ColorUtil;
import eddie.wu.linkedblock.LocalResultOfZhengZi;

/**
 * (zheng zi ji suan hai shi fang zai du li de Class zhong jiao hao.)
 * 征子计算还是放在独立的类中较好。这样搜索算法才有可重用性。 以后可以推广 到所有的敌对搜索，而不仅仅限于征子计算。 2005/08 遇到了深度克隆的问题.
 * 用序列化实现深度克隆也遇到了问题. Decision 1: 用Momento模式来实现状态 * 的拷贝. 也许效率不如克隆,因为需要重新收集棋局信息.
 * 但是先保证程序可以运行更重要. 性能可以留待将来解决.
 * 
 * depends on linked.block.GoBoard
 * 
 * @author eddie
 * 
 */
public class ZhengZiCalculate {
	private static final Log log = LogFactory.getLog(ZhengZiCalculate.class);
	static final int SEARTHDEPTH = 120;

	static final int MAX = 1; // 代表征子方

	static final int MIN = 2; // 代表被征子方

	static final int numberOfNodes = 100000; // 局面数,即展开的节点数.

	/**
	 * instance level fields
	 */

	byte zhengzijieguo[][] = new byte[127][2];

	/*
	 * 当前已有层数。
	 */
	byte cengshu = 0;

	/*
	 * 当前已有局面索引号。
	 */
	int lastJumianIndex = 0;

	/**
	 * 初始状态，层0只有有一个局面。他在栈中的编号是0
	 */
	Controller[] controllers;

	GoBoard[] go = new GoBoard[numberOfNodes];
	/*
	 * 生成与go对应局面所走点的横坐标。 生成与go对应局面所走点的纵坐标。 <br/> go[0] have no corresponding
	 * za,zb. go[0] move to go[1] with step (za[1,zb[1]). since there are
	 * several options in each state. we can not store the option/step in
	 * starting state.
	 */
	byte[] za = new byte[numberOfNodes];
	byte[] zb = new byte[numberOfNodes];
	GoBoard temp;

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
		Point[] points = convertArrayToPoints(result);
		return points;
	}

	public static Point[] convertArrayToPoints(byte[][] result) {
		Point[] points = new Point[result.length];
		for (int i = 0; i < result.length; i++) {

			byte[] temp = result[i];
			if (temp[0] == 0 && temp[1] == 0)
				break;
			points[i] = Point.getPoint(temp[0], temp[1]);
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
		controllers = initControllersMaxMin();
		controllers[0].setIndexForJuMian(0);
		controllers[0].setNumberOfJuMian(1);

		// 做活主体的块。
		GoBoard linkedBlockGoBoard = new GoBoard(state);
		linkedBlockGoBoard.generateHighLevelState();

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
		go[0] = this.getGoBoardCopy(linkedBlockGoBoard);
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
				temp = (go[lastJumianIndex]).getGoBoardCopy();
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
			if (controllers[cengshu].getWhoseTurn() == MIN) {
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
						go[lastJumianIndex + count] = (GoBoard) iterator.next();
						if (go[lastJumianIndex + count] == null) {
							throw new RuntimeException(
									"go[jumianshu+count]==null");
						}
					}
					System.out.println("count=" + count);
					lastJumianIndex += result.getNumberOfCandidates();
					System.out.println("jumianshu=" + lastJumianIndex);
				} else if (result.isSelfSuccess()) {// impossible
					// 征子成功只能是被征子方不能长气。
				}
				// if min
			} else if (controllers[cengshu].getWhoseTurn() == MAX) {// 被征子方候选点数
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
							lastJumianIndex = dealWithCeng(lastJumianIndex, cengshu,
									controllers);
						}
					}
				} else if (result.isSelfFail()) {
					while (true) {
						cengshu -= 2; // 倒数两层已经确定。减后的层数需要重新展开
						if (cengshu == 0) {
							zhengzijieguo[0][0] = 127;
							//byte[][] hui = go[lastJumianIndex].getStepHistory().hui;
							// for(int j=0;j<512;j++){
							// System.out.println("["+hui[j][0]+","+hui[j][1]+"];");
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
						go[lastJumianIndex + count] = (GoBoard) iterator.next();
						if (go[lastJumianIndex + count] == null) {
							throw new RuntimeException(
									"go[jumianshu+count-1]==null");
						}
					}
					System.out.println("count=" + count);
					lastJumianIndex += result.getNumberOfCandidates();
					System.out.println("jumianshu=" + lastJumianIndex);
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
	Controller[] initControllersMaxMin() {
		Controller[] controllers = new Controller[SEARTHDEPTH];
		Controller controller;

		for (byte i = 0; i < controllers.length; i++) {
			controller = new Controller();
			controller.setWhoseTurn(MAX);
			controllers[i] = controller;
			i++;
			controller = new Controller();
			controller.setWhoseTurn(MIN);
			controllers[i] = controller;

		}
		return controllers;
	}

	Controller[] initControllersMinMax() {
		Controller[] controllers = new Controller[SEARTHDEPTH];
		Controller controller;

		for (byte i = 0; i < controllers.length; i++) {
			controller = new Controller();
			controller.setWhoseTurn(MIN);
			controllers[i] = controller;
			i++;
			controller = new Controller();
			controller.setWhoseTurn(MAX);
			controllers[i] = controller;

		}
		return controllers;
	}

	GoBoard getGoBoardCopy(GoBoard goBoard) {
		GoBoard temp = new GoBoard(goBoard.getBoardColorState());
		temp.generateHighLevelState();
		return temp;
	}
}
