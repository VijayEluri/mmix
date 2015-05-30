package eddie.wu.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.api.ZhengZiInterface;
import eddie.wu.linkedblock.LocalResultOfZhengZi;
import eddie.wu.search.Controller;
import eddie.wu.search.global.Candidate;
import eddie.wu.search.global.CaptureSearch;

/**
 * the logic of ladder calculation. 征子计算的逻辑.<br/>
 * 从GoBoard中分离出来,免得所有代码集中在一个地方.<br/>
 * 
 * ZhengZiCalculate 是另一种分离代码的方法,比较重复.<br/>
 * 
 * @author Eddie
 * 
 */
public class GoBoardLadder extends GoBoard implements ZhengZiInterface {
	/**
	 * 从征子方出发，征子成立为127，征子不成立为-127。
	 */
	public static final int LADDER_SUCCESS = 127;
	public static final int LADDER_FAIL = -127;
	private transient static final Logger log = Logger
			.getLogger(GoBoardLadder.class);
	static {
		log.setLevel(Level.DEBUG);
	}

	public GoBoardLadder(BoardColorState boardColorState, int shoushu) {
		super(boardColorState);
	}

	public GoBoardLadder(BoardColorState state) {
		super(state);
	}

	byte zhengzijieguo[][] = new byte[127][2];

	public byte[][] jiSuanZhengZi(Point point) {
		return jisuanzhengziWithClone(point);
	}

	/**
	 * 
	 * 计算征子，但是不能用于含有劫争的情况。 为MAX-MIN过程。 <br/>
	 * 征子方先走。a,b是被征子方棋块中的一个点。因为块号可能改变。 该方法真正用到了搜索算法。
	 * 
	 * @param row
	 * @param column
	 * @return a list of step(Points)
	 */
	public byte[][] jisuanzhengziWithClone(Point point) {

		byte MAX = 1; // 代表征子方
		byte MIN = 2; // 代表被征子方

		byte m1, n1;

		// 做活主体的块。
		Block blockToBeEaten = getBlock(point);
		if (log.isDebugEnabled()) {
			log.debug("block: " + blockToBeEaten);
		}
		byte houxuan[][] = new byte[5][2]; // 候选点〔0〕〔0〕存储子数。
		GoBoardLadder[] go = new GoBoardLadder[100000];
		byte[] za = new byte[100000]; // 生成与go对应局面所走点的横坐标。
		byte[] zb = new byte[100000]; // 生成与go对应局面所走点的纵坐标。
		int jumianshu = 0; // 当前已有局面号。
		byte SOUSUOSHENDU = 120;
		byte cengshu = 0; // 当前已有层数。

		Controller[] controllers = new Controller[SOUSUOSHENDU];
		int[][] st = new int[SOUSUOSHENDU][5]; // 限制搜索深度为SOUSUOSHENDU。
		// 0:该层起始局面号。
		// 1:本层对下一层取max还是min
		// 2:上一层对自己的层取max还是min
		// 3:当前层已经有一个局面确定了。为相应的值。
		// 4:该层还有多少局面。＝0则该层面结束
		Controller controller = null;
		for (byte i = 0; i < SOUSUOSHENDU; i++) {
			// 限制了搜索深度。
			st[i][1] = MAX; // 对下层取MAX；该层由max下。
			// 对下层(走子之后的状态)取MAX；
			st[i][2] = MIN; // 对同一层取MIN
			controller = new Controller();
			controller.setWhoseTurn(MAX);
			controllers[i] = controller;
			i++;
			st[i][1] = MIN; // 对下层取MIN；
			st[i][2] = MAX;
			controller = new Controller();
			controller.setWhoseTurn(MIN);
			controllers[i] = controller;

		}

		byte turncolor1 = 0; // 计算计算需要何方轮走。
		// byte turncolor2 = 0; // 原始局面原本轮谁走？

		if (blockToBeEaten.getColor() == ColorUtil.BLACK) {
			// shoushu = 1;
			turncolor1 = ColorUtil.WHITE;
			if (log.isDebugEnabled()) {
				log.debug("要做活的棋块为黑色，轮白方走能否征子？");
			}
		} else if (blockToBeEaten.getColor() == ColorUtil.WHITE) {
			// shoushu = 0;
			turncolor1 = ColorUtil.BLACK;
			if (log.isDebugEnabled()) {
				log.debug("要做活的棋块为白色，轮黑方走能否征子？");
			}
		}

		// if ((shoushu % 2) == 0) { // 实际上现在轮谁走。
		// turncolor2 = ColorUtil.BLACK;
		// if (log.isDebugEnabled()) {
		// log.debug("局面上应该黑走。");
		// }
		// } else {
		// turncolor2 = ColorUtil.WHITE;
		// if (log.isDebugEnabled()) {
		// log.debug("局面上应该白走。");
		// }
		// }
		log.debug("clone in index:" + 0);
		go[0] = this.getGoBoardLadderCopy();

		// if (turncolor1 != turncolor2) {
		// go[0].giveUp();
		// zhengzijieguo[126][0] = 1;
		// if (log.isDebugEnabled()) {
		// log.debug("需要弃权一步。");
		// }
		// }

		// 1.初始化
		byte youxiaodian = 0; // 不违反落子规则的点。用于征子方MAX.
		byte haodian = 0; // 用于被征子方。从有效点中排除可以直接得出结论的点。
		GoBoardLadder temp;

		st[0][0] = 0;
		st[0][4] = 1;
		jumianshu = 0;

		// 2.开始计算。
		while (true) {
			// 第一层循环：展开最后一个局面。

			if (cengshu >= (SOUSUOSHENDU - 1)) {
				if (log.isDebugEnabled()) {
					log.debug("搜索到100层，仍没有结果，返回不精确结果");
				}
				return zhengzijieguo;
			} else {
				cengshu++; // 新层的层号。
				if (log.isDebugEnabled()) {
					log.debug("新的当前层数为：" + cengshu);
				}
				st[cengshu][0] = jumianshu + 1;
				// 新层的开始点。
				if (log.isDebugEnabled()) {
					log.debug("新层的开始局面索引为：" + (jumianshu + 1));
				}
			}

			youxiaodian = 0;
			haodian = 0;
			log.debug("clone in index:" + jumianshu);
			temp = go[jumianshu].getGoBoardLadderCopy();
			// temp.initPoints();
			// 要展开的局面。
			blockToBeEaten = temp.getBlock(point);

			if (st[cengshu][1] == MAX) {
				if (log.isDebugEnabled()) {
					log.debug("当前层轮谁走？" + "MAX");
				}
				if (log.isDebugEnabled()) {
					log.debug("上一层轮谁走？" + "MIN");
				}
				if (log.isDebugEnabled()) {
					log.debug("在上一层由MIN走得到该层");

				}
				// DianNode1 lili;
				// HaoNode1 yskh; //异色块号
				int ysks; // 异色块数
				byte tizidianshu = 0;

				ysks = blockToBeEaten.getEnemyBlocks().size();
				// yskh = zkin.getEnemyBlocks().zwyskhao;
				// for (byte jj = 1; jj <= ysks; jj++) { //加入被打吃的点；
				for (Iterator iter = blockToBeEaten.getEnemyBlocks().iterator(); iter
						.hasNext();) {
					// 记录周围一气的点。出现错误，打吃一般不适上一步生成。

					Block tempBlock = (Block) iter.next();
					if (tempBlock.getBreaths() == 1) {

						// short beidachikuaihao = yskh.hao;

						Set qi = tempBlock.getBreathPoints();
						// lili = temp.zikuai[beidachikuaihao].qichuang;

						Iterator iter2 = tempBlock.getBreathPoints().iterator();
						if (iter2.hasNext()) {
							tizidianshu++;
							Point ppp = (Point) iter2.next();
							houxuan[tizidianshu][0] = ppp.getRow();
							houxuan[tizidianshu][1] = ppp.getColumn();
						}

					}
					// yskh = yskh.next;
				}

				// lili = temp.zikuai[zkin].qichuang;
				Set temps = blockToBeEaten.getBreathPoints();
				if (blockToBeEaten.getBreaths() != 1) {
					if (log.isDebugEnabled()) {
						log.debug("错误：气数不为1。");
					}
					zhengzijieguo[0][0] = -128;
					return zhengzijieguo; // 表示方法失败。
				}

				if (temps == null) {
					if (log.isDebugEnabled()) {
						log.debug("气数不足1。");
					}
					zhengzijieguo[0][0] = -128;
					return zhengzijieguo; // 表示方法失败。
				}

				Iterator itertemp = temps.iterator();
				if (itertemp.hasNext()) {
					tizidianshu++;
					Point bp = (Point) itertemp.next();
					// TODO: two kinds of corrdinate! careful
					houxuan[tizidianshu][0] = bp.getRow(); // 可能是重复的。
					houxuan[tizidianshu][1] = bp.getColumn();
				}
				houxuan[0][0] = tizidianshu;
				if (log.isDebugEnabled()) {
					log.debug("被征子方候选点数为" + tizidianshu);
				}

				// 被征子方走。
				boolean queding = false;
				for (byte i = 1; i <= houxuan[0][0]; i++) {
					// 目前仅仅考虑候选点已知而且在搜索过程中不动态改变
					// 以后应该进行更细致的处理，根据要展开的局面确定。

					m1 = houxuan[i][0];
					n1 = houxuan[i][1];
					log.debug("clone in index" + jumianshu);
					temp = go[jumianshu].getGoBoardLadderCopy();
					// 因为temp可能已经被改变，必须重新赋值。
					// 扩展最后的局面，扩展同一个上级局面。
					if (temp.validate(m1, n1)) { // 判断合法着点
						temp.oneStepForward(Point.getPoint(boardSize, m1, n1),
								blockToBeEaten.getColor());
						youxiaodian++;
						if (log.isDebugEnabled()) {
							log.debug("第" + youxiaodian + "个候选点为:(" + m1 + ","
									+ n1 + ")");
						}

						if (temp.getBreaths(point) == 1) {
							if (log.isDebugEnabled()) {
								log.debug("落子后被征子方气数为1");
							}
						} else if (temp.getBreaths(point) == 2) {
							haodian++;
							go[jumianshu + haodian] = temp;
							za[jumianshu + haodian] = m1;
							zb[jumianshu + haodian] = n1;
							if (log.isDebugEnabled()) {
								log.debug("min走" + "(" + m1 + "," + n1 + ")");
							}

						} else if (temp.getBreaths(point) == 3) {
							if (log.isDebugEnabled()) {
								log.debug("落子后被征子方气数为3");
							}

							cengshu -= 1; // 倒数两层已经确定。减后的层数需要重新展开

							st[cengshu][4] -= 1;
							if (st[cengshu][4] != 0) {
								jumianshu = st[cengshu][0] + st[cengshu][4] - 1;
								// st[cengshu - 2][3] = 127;
								queding = true;
								break; // 跳出for循环。
							} else {
								while (true) {
									cengshu -= 2; // 倒数两层已经确定。减后的层数需要重新展开
									if (cengshu == -1) {
										zhengzijieguo[0][0] = LADDER_FAIL;
										return zhengzijieguo;
									}

									st[cengshu][4] -= 1;
									if (st[cengshu][4] != 0) {
										jumianshu = st[cengshu][0]
												+ st[cengshu][4] - 1;
										// st[cengshu - 2][3] = 127;
										queding = true;
										break;
									}
								}
							}

						}
					}
				} // for循环结束
				if (log.isDebugEnabled()) {
					log.debug("有效点为:" + youxiaodian);
				}

				if (queding == true) {
					continue;
				} else if (haodian == 0) {
					while (true) {
						cengshu -= 2; // 倒数两层已经确定。减后的层数需要重新展开
						if (cengshu == 0) {

							zhengzijieguo[0][0] = LADDER_SUCCESS;

							byte lins = 0;
							for (lins = 2; st[lins][0] != 0; lins++) {
								if (log.isDebugEnabled()) {
									log.debug("点为:(" + za[st[lins][0] - 1]
											+ "," + zb[st[lins][0] - 1] + ")");
								} // this.cgcl(za[st[lins][0]-1],zb[st[lins][0]-1]);
								zhengzijieguo[lins - 1][0] = za[st[lins][0] - 1];
								zhengzijieguo[lins - 1][1] = zb[st[lins][0] - 1];

							}
							zhengzijieguo[0][1] = (byte) (lins - 2);
							return zhengzijieguo;
						}

						st[cengshu][4] -= 1;
						if (st[cengshu][4] != 0) {
							jumianshu = st[cengshu][0] + st[cengshu][4] - 1;
							// st[cengshu - 2][3] = 127;
							break;
						}
					}

				} else {
					st[cengshu][4] = haodian;
					jumianshu += haodian;

				}
			} // max

			// zheng zi fang zou.
			else if (st[cengshu][1] == MIN) {
				if (log.isDebugEnabled()) {
					log.debug("当前层轮谁走？" + "MIN");
				}
				if (log.isDebugEnabled()) {
					log.debug("在上一层由MAX走得到该层");
				}
				// DianNode1 lili = temp.zikuai[zkin].qichuang;
				Set tep = blockToBeEaten.getBreathPoints();
				if (blockToBeEaten.getBreaths() != 2) {
					if (log.isDebugEnabled()) {
						log.debug("错误：气数不为二。");
					}
					zhengzijieguo[0][0] = LADDER_FAIL;
					return zhengzijieguo; // 表示方法失败。
				}
				// for (byte i = 1; i <= 2; i++) {
				int iii = 0;
				for (Iterator iter = tep.iterator(); iter.hasNext();) {
					if (tep.size() == 0) {
						if (log.isDebugEnabled()) {
							log.debug("气数不足二。");
						}
						zhengzijieguo[0][0] = LADDER_FAIL;
						return zhengzijieguo; // 表示方法失败。
					}
					iii++;
					Point bpp = (Point) iter.next();
					houxuan[iii][0] = bpp.getRow();
					houxuan[iii][1] = bpp.getColumn();

				}
				houxuan[0][0] = 2;
				for (byte i = 1; i <= houxuan[0][0]; i++) {
					// 目前仅仅考虑候选点已知而且在搜索过程中不动态改变
					// 以后应该进行更细致的处理，根据要展开的局面确定。

					m1 = houxuan[i][0];
					n1 = houxuan[i][1];
					log.debug("clone in index" + jumianshu);
					temp = go[jumianshu].getGoBoardLadderCopy();

					// 扩展最后的局面，扩展同一个上级局面。
					if (temp.validate(m1, n1)) { // 判断合法着点
						temp.oneStepForward(Point.getPoint(boardSize, m1, n1),
								ColorUtil.enemyColor(blockToBeEaten.getColor()));
						youxiaodian++;
						log.debug("set into in index:"
								+ (jumianshu + youxiaodian));
						go[jumianshu + youxiaodian] = temp;
						za[jumianshu + youxiaodian] = m1;
						zb[jumianshu + youxiaodian] = n1;
						if (log.isDebugEnabled()) {
							log.debug("max走" + "(" + m1 + "," + n1 + ")");
						}

					}
				}
				if (youxiaodian == 0) {
					// 返回，一般是征子方无子可下。
					if (log.isDebugEnabled()) {
						log.debug("有效点为0");

					}
					st[cengshu][0] = 0;

					if (cengshu == 1) { // 征子方直接无子可下，
						zhengzijieguo[0][0] = LADDER_FAIL;
						return zhengzijieguo;
					}

					while (true) {
						cengshu -= 2; // 倒数两层已经确定。减后的层数需要重新展开
						if (cengshu == -1) {
							zhengzijieguo[0][0] = LADDER_FAIL;

							for (byte lins = 2; st[lins][0] != 0; lins++) {
								if (log.isDebugEnabled()) {
									log.debug("点为:(" + za[st[lins][0] - 1]
											+ "," + zb[st[lins][0] - 1] + ")");
								}
								zhengzijieguo[lins - 1][0] = za[st[lins][0] - 1];
								zhengzijieguo[lins - 1][1] = zb[st[lins][0] - 1];

							}

							return zhengzijieguo;
						}

						st[cengshu][4] -= 1;
						if (st[cengshu][4] != 0) {
							jumianshu = st[cengshu][0] + st[cengshu][4] - 1;
							// st[cengshu - 2][3] = 127;
							break;
						}
					}
				} else {

					st[cengshu][4] = youxiaodian;
					jumianshu += youxiaodian;
				}

			} // if min

		} // while

	}

	/**
	 * 轮被征子一方走.评价各个候选点并返回结果.
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public LocalResultOfZhengZi getLocalResultOfZhengZiForMIN(byte row,
			byte column) {
		final Logger log = Logger
				.getLogger(GoBoard.class.getName() + "Zhengzi");
		LocalResultOfZhengZi result = new LocalResultOfZhengZi();
		// 做活主体的块。
		Block blockToBeEaten = getBoardPoint(row, column).getBlock();
		Set<Point> candidate = new HashSet<Point>(4);
		// 1. 确定可以提子的点.
		// TODO:高级数据结构可以还没有准备好.
		for (Iterator<Block> iter = blockToBeEaten.getEnemyBlocks().iterator(); iter
				.hasNext();) {
			// 记录周围一气的点。
			// ?出现错误，打吃一般不适上一步生成。而是再前一步形成的.

			Block tempBlock = (Block) iter.next();
			if (tempBlock.getBreaths() == 1) {
				Iterator<Point> iter2 = tempBlock.getBreathPoints().iterator();
				if (iter2.hasNext()) {
					Point ppp = (Point) iter2.next();
					candidate.add(ppp);
				}
			}
		}

		// 2. 考虑延气
		if (blockToBeEaten.getBreaths() != 1) {
			if (log.isDebugEnabled()) {
				log.debug("错误：气数不为1。");
			}
			throw new RuntimeException("错误：被征子方轮走时气数不为1。");
		}
		Set<Point> temps = blockToBeEaten.getBreathPoints();
		if (temps == null || temps.isEmpty()) {
			if (log.isDebugEnabled()) {
				log.debug("气数不足1。");
			}
			throw new RuntimeException("错误：被征子方轮走时气数不足1。");
		}

		Iterator<Point> itertemp = temps.iterator();
		if (itertemp.hasNext()) {// 因为被征子方最多只有一气。
			candidate.add((Point) itertemp.next());
		}

		if (log.isDebugEnabled()) {
			log.debug("被征子方候选点数为" + candidate.size());
		}

		// 3. 被征子方走后的效果。评价候选点。如果能够直接得到结果(一步之内)。
		// 如果被征子方提子后.征子方无子可下.则待征子方走时发现.

		int youxiaodian = 0;
		// int haodian=0;
		final int LADDER_SUCCESS = 127;
		for (Iterator<Point> iter = candidate.iterator(); iter.hasNext();) {
			GoBoardLadder temp = this.getGoBoardLadderCopy();
			// 目前仅仅考虑候选点已知而且在搜索过程中不动态改变
			// 以后应该进行更细致的处理，根据要展开的局面确定。
			Point tempPoint = (Point) iter.next();

			// 因为temp可能已经被改变，必须重新赋值。
			// 扩展最后的局面，扩展同一个上级局面。
			if (temp.validate(tempPoint, 0)) { // 判断合法着点
				temp.oneStepForward(tempPoint);
				youxiaodian++;
				if (log.isDebugEnabled()) {
					log.debug("第" + youxiaodian + "个候选点为:(" + tempPoint + ")");
				}

				if (temp.getBoardPoint(row, column).getBreaths() == 1) {
					if (log.isDebugEnabled()) {
						log.debug("落子后被征子方气数为1");
					}
				} else if (temp.getBoardPoint(row, column).getBreaths() == 2) {
					if (log.isDebugEnabled()) {
						log.debug("落子后被征子方气数为2");
					}
					result.addCandidateJuMianAndPoint(temp, tempPoint);

				} else if (temp.getBoardPoint(row, column).getBreaths() == 3) {
					if (log.isDebugEnabled()) {
						log.debug("落子后被征子方气数为3");
					}
					result.setScore(LADDER_SUCCESS);
					return result;
				} else {
					if (log.isDebugEnabled()) {
						log.debug("落子后被征子方气数为"
								+ temp.getBoardPoint(row, column).getBreaths());
					}
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("落子点非法!");
				}
			}
		} // for循环结束
			// if(result.)
			// TODO how to return result.
		if (result.getNumberOfCandidates() == 0) {
			result.setScore(-LADDER_SUCCESS);
		}
		return result;
	}

	public List<Candidate> getCaptureCandidate(Point target, int whoseTurn) {
		if (whoseTurn == this.getColor(target)) {
			return getCaptureCandidate_escape(target, whoseTurn);
		}

		else if (whoseTurn == ColorUtil.enemyColor(this.getColor(target))) {

			return getCaptureCandidate_capture(target, whoseTurn);
		} else {
			return null;
		}
	}

	/**
	 * 打吃的候选点，要保证自己有至少一气。没有合法候选点则弃权。
	 * @param target
	 * @param whoseTurn
	 * @return
	 */
	public List<Candidate> getCaptureCandidate_capture(Point target,
			int whoseTurn) {
		List<Candidate> candidates = new ArrayList<Candidate>();
		Block targetB = this.getBlock(target);
		for (Point point : targetB.getBreathPoints()) {
			if (this.breathAfterPlay(point, whoseTurn).size() >= 1) {
				Candidate candidate = new Candidate();
				candidate.setStep(new Step(point, whoseTurn, getShoushu() + 1));
				candidates.add(candidate);

			}
		}
		return candidates;
	}

	/**
	 * 没有合法着点则返回弃权。  
	 * @param target
	 * @param whoseTurn
	 * @return
	 */
	public List<Candidate> getCaptureCandidate_escape(Point target,
			int whoseTurn) {
		List<Candidate> candidates = new ArrayList<Candidate>();
		Block targetB = this.getBlock(target);

		Point point = targetB.getUniqueBreath();// only one breath
		int breaths = this.breathAfterPlay(point, whoseTurn).size();
		if (breaths >= 3) {
			Candidate candidate = new Candidate();
			candidate.setStep(new Step(point, whoseTurn, getShoushu() + 1));
			candidates.add(candidate);
		} else {// ==2
			// consider remove enemy block first if possible.
			for (Block enemyB : targetB.getEnemyBlocks()) {
				if (enemyB.getBreaths() == 1) {
					Candidate candidate = new Candidate();
					candidate.setStep(new Step(enemyB.getUniqueBreath(),
							whoseTurn, getShoushu() + 1));
					candidates.add(candidate);
				}
			}
			if (breaths == 2) {// give up
				Candidate candidate = new Candidate();
				candidate.setStep(new Step(point, whoseTurn, getShoushu() + 1));
				candidates.add(candidate);
			}
		}
		if (candidates.isEmpty()) {// give up
			Candidate candidate = new Candidate();
			candidate.setStep(new Step(null, whoseTurn, getShoushu() + 1));
			candidates.add(candidate);
		}

		return candidates;
	}

	public boolean isFinalState_capture(Point target, int targetColor) {
		if (this.noStep()) {
			return false;
		} else if (this.getLastStep().isGiveup()) {
			return true;
		}
		//被征子的块走了一步
		if (this.getLastStep().getColor() == targetColor) {
			return this.getBreaths(target) != 2;
		} else {		//征子方走了一步
			return this.getBreaths(target) != 1;// impossible.
		}
	}

	public int getScore_capture(Point target, int targetColor) {
		 if (this.getLastStep().isGiveup()) {
			 if (this.getLastStep().getColor() == targetColor) {
				 //无法达到两气。
				 return CaptureSearch.CAPTURE_SUCCESS;
			 }else{
				 //无法达到一气。
				 return CaptureSearch.CAPTURE_FAILURE;
				
				
			 }
		}
		
		int breaths = this.getBreaths(target);
		if (this.getLastStep().getColor() == targetColor) {
			if (breaths > 2)
				return CaptureSearch.CAPTURE_FAILURE;
			else if (breaths < 2)
				return CaptureSearch.CAPTURE_SUCCESS;
		} else {
			if (breaths > 1)
				return CaptureSearch.CAPTURE_FAILURE;
			else if (breaths < 1)
				return CaptureSearch.CAPTURE_SUCCESS;
		}
		return CaptureSearch.UNKNOWN;
	}

	/**
	 * 轮Max方即征子方走时，考虑所有可能的候选点并且排除某些确定状态的点。 <br/>
	 * 只有两种可能结果：1。没有合法着点。 2. 继续计算（取决于后面的进展）
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public LocalResultOfZhengZi getLocalResultOfZhengZiForMAX(byte row,
			byte column) {
		final Logger log = Logger
				.getLogger(GoBoard.class.getName() + "Zhengzi");
		LocalResultOfZhengZi result = new LocalResultOfZhengZi();

		Block blockToBeEaten = getBoardPoint(row, column).getBlock();
		if (blockToBeEaten.getBreaths() != 2) {
			if (log.isDebugEnabled()) {
				log.debug("错误：气数不为二。" + blockToBeEaten.getBreaths());
			}
			zhengzijieguo[0][0] = -127;
			throw new RuntimeException("错误：征子方落子前目标快气数不为二。");
			// return zhengzijieguo; // 表示方法失败。
		}

		Set<Point> candidate = new HashSet<Point>(4);
		candidate.addAll(blockToBeEaten.getBreathPoints());

		byte m1, n1;
		// 遍历并且评价候选点,事后评价.需要落子.
		for (Iterator<Point> iter = candidate.iterator(); iter.hasNext();) {
			GoBoardLadder temp = this.getGoBoardLadderCopy();
			// 目前仅仅考虑候选点已知而且在搜索过程中不动态改变
			// 以后应该进行更细致的处理，根据要展开的局面确定。
			Point tempPoint = (Point) iter.next();

			// 扩展最后的局面，扩展同一个上级局面。
			if (temp.validate(tempPoint, 0)) { // 判断合法着点
				temp.oneStepForward(tempPoint);
				result.addCandidateJuMianAndPoint(temp, tempPoint);
			}
		}
		if (result.getCandidatePoints().size() == 0) {
			// 返回，一般是征子方无子可下。
			if (log.isDebugEnabled()) {
				log.debug("有效点为0");

			}
			result.setScore(-128);
		}

		return result;
	}

	public GoBoardLadder getGoBoardLadderCopy() {
		if (log.isDebugEnabled())
			log.debug("this=" + this + " shoushu = " + shoushu);
		if (log.isDebugEnabled())
			log.debug(getStepHistory().getAllSteps());

		GoBoardLadder temp = new GoBoardLadder(this.getBoardColorState(),
				this.getShoushu());
		temp.setStepHistory(this.getStepHistory().getCopy());
		temp.lastPoint = this.lastPoint;

		if (log.isDebugEnabled())
			log.debug(temp.getStepHistory().getAllSteps());
		if (shoushu != temp.getStepHistory().getAllSteps().size()) {

			throw new RuntimeException("shoushu" + shoushu
					+ "temp.getStepHistory().getAllSteps().size()=="
					+ temp.getStepHistory().getAllSteps().size());
		}
		// temp.generateHighLevelState();
		// temp.lastPoint = this.lastPoint;
		return temp;
	}
}
