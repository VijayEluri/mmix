package eddie.wu.domain.conn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BasicBlock;
import eddie.wu.domain.BlankBlock;
import eddie.wu.domain.Block;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Delta;
import eddie.wu.domain.GoBoardLadder;
import eddie.wu.domain.Group;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.StateAnalysis;
import eddie.wu.domain.comp.BlockBreathComparatorDesc;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.global.CaptureSearch;

/**
 * perform Connectivity analysis after basic block info is collected. Group
 * concept current is not maintained dynamically going forward.
 * 
 * @author Eddie
 * 
 */
public class ConnectivityAnalysis extends StateAnalysis {
	private transient static final Logger log = Logger
			.getLogger(ConnectivityAnalysis.class);

	public static final int CONNECTED = 10;
	public static final int CAN_CONNECT = 5;
	public static final int NOT_CONNECTED = 0;
	public List<Group> groups = new ArrayList<Group>();

	public ConnectivityAnalysis(int boardSize) {
		super(boardSize);
	}
	
	public ConnectivityAnalysis(byte[][] state) {
		this(state,Constant.BLACK);
	}
	public ConnectivityAnalysis(byte[][] state, int color) {
		super(state, color);

		// TODO recover the code later when connectivity is needed,
		// List<Block> blocks = this.getBlackWhiteBlocks();
		//
		// if (log.isDebugEnabled()) {
		// log.debug("全局共有" + blocks.size() + "块");
		// }
		// this.weakStrongAnalysis(blocks);
		// this.eyeConnected();
		// this.tigerMouthConnected();
		// this.connectBlock(blocks);
		// for (Iterator<Group> iter = groups.iterator(); iter.hasNext();) {
		// Group temp = iter.next();
		// if (temp.getBlocks().isEmpty()) {
		// if(log.isEnabledFor(Level.WARN)) log.warn("remove blank group!");
		// iter.remove();
		// }
		// }
		// this.logGroups(groups);
	}

	// TODO: init
	public ConnectivityAnalysis(BoardColorState colorState) {
		super(colorState);
	}

	/**
	 * 
	 * @param list
	 *            list of same color blocks.
	 * @return
	 */
	public void connectBlock(List<Block> list) {

		// sort block according to its breath, deal with strong block first.
		Collections.sort(list, new BlockBreathComparatorDesc());
		for (Block block : list) {
			this.connectBlock(block);
		}

		// return listGroup;

	}

	/**
	 * 先考虑通过眼位相连接的可能性
	 * 
	 */
	public void eyeConnected() {

		if (log.isDebugEnabled()) {
			log.debug("共有眼块" + eyeBlocks.size());
			log.debug(BasicBlock.getBehalfs(eyeBlocks));

		}

		Group group = null;
		for (BlankBlock blankBlock : this.eyeBlocks) {
			if (blankBlock.isEyeBlock() == false)
				continue;
			// TODO check whether some special situation exist. like 倒扑.
			int minBreath = 128;
			group = null;
			if (log.isDebugEnabled()) {
				log.debug("neighbor block of eyeblock "

				+ blankBlock.getBehalfPoint() + " is connected: "
						+ BasicBlock.getBehalfs(blankBlock.getNeighborBlocks()));
			}
			for (Block block : blankBlock.getNeighborBlocks()) {
				if (block.isGrouped()) {
					group = block.getGroup();
					break;
				}
			}
			if (group == null) {
				if (log.isDebugEnabled()) {
					log.debug("all the neighbor block is not grouped yet, create new group.");
				}
				group = new Group();
				groups.add(group);
			} else {
				if (log.isDebugEnabled()) {
					log.debug("at least one neighbor block is grouped, merge with existing group.");
					log.debug(group);
				}
			}

			for (Block block : blankBlock.getNeighborBlocks()) {
				Group mergedGroup = group.addBlock(block);
				groups.remove(mergedGroup);
				if (block.getBreaths() < minBreath)
					minBreath = block.getBreaths();
			}
			if (log.isDebugEnabled()) {

				log.debug("minBreath=" + minBreath);
				log.debug("group becomes " + group);
				if (minBreath == 128)
					log.debug(blankBlock);
			}

		}
		if (log.isDebugEnabled()) {
			log.debug("通过眼块相连形成的块组。" + groups.size());
			this.logGroups(groups);
		}

	}

	/**
	 * 再考虑通过虎口相连接的可能性.
	 * 
	 * @return
	 */
	public void tigerMouthConnected() {

		Group group = null;
		List<Point> tigerCandidate = new ArrayList<Point>();
		for (Point point : this.tigerMouth) {
			if (this.getBlankBlock(point).isEyeBlock())
				continue;
			else {
				tigerCandidate.add(point);
			}
		}

		if (log.isDebugEnabled()) {
			log.debug("考虑的虎口有：" + tigerCandidate);
		}
		for (Point point : tigerCandidate) {
			for (Delta delta : Constant.ADJACENTS) {
				Point neighbourPoint = point.getNeighbour(delta);
				if (neighbourPoint == null)
					continue;
				Block neighbourBlock = this.getBlock(neighbourPoint);
				if (neighbourBlock != null && neighbourBlock.isGrouped()) {
					group = neighbourBlock.getGroup();
					break;
				}
			}
			if (group == null) {
				if (log.isDebugEnabled()) {
					log.debug("all the neighbor block of tiger  mouth" + point
							+ "is not grouped yet, create new group.");
				}
				group = new Group();
				groups.add(group);
			}
			for (Delta delta : Constant.ADJACENTS) {
				Point neighbourPoint = point.getNeighbour(delta);
				if (neighbourPoint == null)
					continue;

				Block neighbourBlock = this.getBlock(neighbourPoint);
				if (neighbourBlock != null) {
					Group mergedGroup = group.addBlock(neighbourBlock);
					if (mergedGroup != null)
						groups.remove(mergedGroup);
				}
			}
		}
	}

	/**
	 * 有三种可能: 已经连接,可断可连接.已经断开.<br/>
	 * (仅从形式上说,断开之子也可能可以吃住).<br/>
	 * 因为两块之间不相邻，所以两个切断点不可能是己方的点<br/>
	 * 
	 * @param a
	 *            point in current block
	 * @param b
	 *            point in friend block
	 */
	public int isDiagonalConnected(Point a, Point b, int myColor) {
		if (this.getBreaths(a) == 1 || this.getBreaths(b) == 1)
			return this.CAN_CONNECT;

		int enemyCount = 0;
		List<Point> cutPoints = new ArrayList<Point>();
		List<Point> toCut = new ArrayList<Point>();
		int enemyColor = ColorUtil.enemyColor(myColor);
		Point temp = Point.getPoint(boardSize, a.getRow(), b.getColumn());
		if (this.getColor(temp) == enemyColor) {
			enemyCount++;
			cutPoints.add(temp);
		} else if (this.getColor(temp) == Constant.BLANK) {
			toCut.add(temp);
		}
		temp = Point.getPoint(boardSize, b.getRow(), a.getColumn());
		if (this.getColor(temp) == enemyColor) {
			enemyCount++;
			cutPoints.add(temp);
		} else if (this.getColor(temp) == Constant.BLANK) {
			toCut.add(temp);
		}

		if (enemyCount == 0 || toCut.size() == 0) {
			return this.CONNECTED;
		} else if (enemyCount == 1) {// TODO:判断断开的可能性
			if (this.getBreaths(cutPoints.get(0)) == 1)
				return this.CONNECTED;

			if (this.breathAfterPlay(toCut.get(0), enemyColor).size() <= 1)
				return this.CONNECTED;
			GoBoardLadder ladder = new GoBoardLadder(this.getBoardColorState());
			ladder.oneStepForward(toCut.get(0), enemyColor);
			if (log.isDebugEnabled())
				log.debug("this=" + ladder + " shoushu=" + ladder.getShoushu()
						+ "after one step forward");

			// BoardColorState state = StateLoader.load(fileName);
			CaptureSearch cs = new CaptureSearch(ladder.getMatrixState(),
					toCut.get(0), enemyColor, false);
			if (cs.globalSearch() == CaptureSearch.CAPTURE_SUCCESS) {
				return this.CONNECTED;
			} else {
				return this.CAN_CONNECT;
			}
			// byte[][] result = ladder.jiSuanZhengZi(toCut.get(0));
			// if (result[0][0] == 127) {// 征子成立!
			// cs.
			// log.debug(toCut.get(0) + "will be caught");
			// return this.CONNECTED;
			// } else {
			// return this.CAN_CONNECT;
			// }
		} else if (enemyCount == 2) {// TODO重要信息,善 加利用.
			for (Point cutPoint : cutPoints) {
				CaptureSearch cs = new CaptureSearch(this.getMatrixState(),
						cutPoint, this.getColor(cutPoint), false);

				if (cs.globalSearch() == CaptureSearch.CAPTURE_SUCCESS) {// 征子成立
					return this.CONNECTED;
				} else {

				}
			}
			return this.NOT_CONNECTED;
		}

		return enemyCount;
	}

	/**
	 * 生成更高级的信息-棋块之间的连接性.具有一定的动态性.不像棋块作为棋子的连接是静态固定的.
	 * 
	 * @param block
	 */
	public void connectBlock(Block block) {
		// if (block.isGrouped()) {// 将独立棋块并入块组。
		// return;
		// }

		for (List<Delta> deltas : Constant.CONNECTIONS) {
			connectBlock(block, deltas);
		}
	}

	public void connectBlock(Block block, List<Delta> deltas) {
		Group group;// = block.getGroup();

		if (log.isDebugEnabled()) {
			log.debug("Block " + block.getBehalfPoint() + " connection.");
		}

		for (Point point : block.getPoints()) {
			/**
			 * 小尖相连接的可能.两块棋通过小尖接近。考虑是否有敌方子切断这个连接。<br/>
			 * 这是最常见的连接情形。
			 * 
			 */
			for (Delta delta : deltas) {
				Point neighbourPoint = point.getNeighbour(delta);
				if (neighbourPoint == null)// out of border
					continue;
				Block neighbourBlock = this.getBlock(neighbourPoint);
				// neighbourBlock.isGrouped() == true we merge it.
				if (neighbourBlock == null
						|| neighbourBlock.getColor() != block.getColor())
					continue;
				if (block.equals(neighbourBlock))
					continue;
				if (block.getGroup() != null
						&& block.getGroup().equals(neighbourBlock.getGroup()))
					continue;
				if (log.isDebugEnabled()) {
					log.debug("Check 小尖  connect to " + neighbourPoint
							+ neighbourBlock.getBehalfPoint());
				}

				/**
				 * 有三种可能: 已经连接,可断可连接.已经断开.<br/>
				 * (仅从形式上说,断开之子也可能可以吃住).
				 */
				int conn = 0;
				if (deltas.equals(Constant.SHOULDERS)) {
					conn = isDiagonalConnected(point, neighbourPoint,
							block.getColor());
				} else if (deltas.equals(Constant.ONE_DISTANCE)) {
					conn = isJumpConnected(point, neighbourPoint,
							block.getColor());
				} else if (deltas.equals(Constant.SMALL_KNIGHT)) {
					conn = isSmallKnightConnected(point, neighbourPoint,
							block.getColor());
				}
				if (conn == this.CONNECTED) {
					if (log.isDebugEnabled()) {
						log.debug("小尖  connect to"
								+ neighbourBlock.getBehalfPoint());
					}
					if (neighbourBlock.isGrouped()) {
						neighbourBlock.getGroup().addBlock(block);
					} else {
						group = new Group();
						group.addBlock(block);
						groups.add(group);
						Group mergedGroup = group.addBlock(neighbourBlock);
						groups.remove(mergedGroup);
					}
					if (neighbourBlock.isAlreadyLive())
						block.addLiveFriend_canConn(neighbourBlock);
					else if (block.isAlreadyLive())
						neighbourBlock.addLiveFriend_canConn(block);

				} else if (conn == this.CAN_CONNECT) {// TODO:判断断开的可能性
					// but block is not necessarily live.
					if (neighbourBlock.isAlreadyLive())
						block.addLiveFriend_canConn(neighbourBlock);
					else if (block.isAlreadyLive())
						neighbourBlock.addLiveFriend_canConn(block);
				} else if (conn == this.NOT_CONNECTED) {// TODO重要信息,善 加利用.

				}

				// group.addBlock(neighbourBlock);
			}
			/**
			 * 一间跳连接
			 */
			// for (Delta delta : Constant.ONE_DISTANCE) {
			// // 需要计算挖断的变化.
			// Point neighbourPoint = point.getNeighbour(delta);
			// if (neighbourPoint == null)
			// continue;
			// Block neighbourBlock = this.getBlock(neighbourPoint);
			// // neighbourBlock.isGrouped() == true we merge it.
			// if (neighbourBlock == null
			// || neighbourBlock.getColor() != block.getColor())
			// continue;
			// // 有三种可能: 已经连接,可断可连接.已经断开.(仅从形式上说,断开之子也可能可以吃住).
			// int connectivity = isJumpConnected(point, neighbourPoint,
			// block.getColor());
			// if (connectivity == this.CONNECTED) {
			// if (log.isDebugEnabled()) {
			// log.debug("一间跳 connect to"
			// + neighbourBlock.getBehalfPoint());
			// }
			// Group mergedGroup = group.addBlock(neighbourBlock);
			// groups.remove(mergedGroup);
			// } else if (connectivity == this.CAN_CONNECT) {// TODO:判断断开的可能性
			//
			// } else if (connectivity == this.NOT_CONNECTED) {// TODO重要信息,尚加利用.
			//
			// }
			//
			// }
			//
			// // 小飞连接
			// for (Delta delta : Constant.SMALL_KNIGHT) {
			// Point neighbourPoint = point.getNeighbour(delta);
			// if (neighbourPoint == null)
			// continue;
			//
			// Block neighbourBlock = this.getBlock(neighbourPoint);
			// // neighbourBlock.isGrouped() == true we merge it.
			// if (neighbourBlock == null
			// || neighbourBlock.getColor() != block.getColor())
			// continue;
			// // 有三种可能: 已经连接,可断可连接.已经断开.(仅从形式上说,断开之子也可能可以吃住).
			// int connectivity = isSmallKnightConnected(point,
			// neighbourPoint, block.getColor());
			// if (connectivity == this.CONNECTED) {
			// if (log.isDebugEnabled()) {
			// log.debug("小飞 onnect to"
			// + neighbourBlock.getBehalfPoint());
			// }
			// Group mergedGroup = group.addBlock(neighbourBlock);
			// groups.remove(mergedGroup);
			// } else if (connectivity == this.CAN_CONNECT) {// TODO:判断断开的可能性
			//
			// } else if (connectivity == this.NOT_CONNECTED) {// TODO重要信息,尚加利用.
			//
			// }
			// }
		}

		// block.setGroup(group);
		// for (Point point : block.getAllBreathPoints()) {
		// if (this.isTigerMouth(point)) {// 虎口的情况
		// for (Delta delta : Constant.NEAR_NEIGHBOUR) {
		// Block neighbourBlock = this.getBlock(point
		// .getNeighbour(delta));
		// group.addBlock(neighbourBlock);
		// }
		//
		// } else {// 可能是尖(有断或者无断),或者跳.
		// for (Delta delta : Constant.NEAR_NEIGHBOUR) {
		//
		// }
		// }
		// }
		//
	}

	private int isSmallKnightConnected(Point point, Point neighbourPoint,
			int color) {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * 简化版本.不存觑(刺)的情况,即视为已经连接 <br/>
	 * TODO 可连接也可断开的局面,属于焦点,如何表达.至少是三值逻辑.
	 * 
	 * @param point
	 * @param neighbourPoint
	 * @param color
	 * @return
	 */
	private int isJumpConnected(Point point, Point neighbourPoint, int color) {
		// TODO Auto-generated method stub
		int row, column;
		int count = 0;// 刺的数目
		int enemyColor = ColorUtil.enemyColor(color);
		if (point.getRow() == neighbourPoint.getRow()) {
			row = point.getRow();
			column = (point.getColumn() + neighbourPoint.getColumn()) / 2;
			if (this.getColor(row, column) == enemyColor) {
				return this.NOT_CONNECTED;
			} else {
				if (this.getColor(row, column + 1) == enemyColor) {
					count++;
				}

				if (this.getColor(row, column - 1) == enemyColor) {
					count++;
				}
			}

		} else {// same column
			column = point.getColumn();
			row = (point.getRow() + neighbourPoint.getRow()) / 2;//
			if (this.getColor(row, column) == enemyColor)
				return this.NOT_CONNECTED;

			if (this.getColor(row + 1, column) == enemyColor) {
				count++;
			}
			if (this.getColor(row - 1, column) == enemyColor) {
				count++;
			}
		}
		if (count > 0)
			return this.CAN_CONNECT;
		else
			return CONNECTED;

	}

	/**
	 * analyze the strength of a single block. also need to consider its enemy
	 * surrounded.
	 * 
	 * @param block
	 */
	public void weakStrongAnalysis(List<Block> blocks) {
		for (Block block : blocks) {
			int count = this.extensionable(block);
			if (block.getBreaths() > 8) {// strong enough except special case

			} else if (block.getBreaths() > 4) {//

			} else {// in potential risk.

			}
		}
	}

	/**
	 * 
	 * @param blocks
	 */
	public void weakStrongAnalysis(Group group) {
		List<Block> blocks = new ArrayList<Block>();// = ;
		for (Block block : group.getBlocks()) {
			this.extensionable(block);
			if (block.getBreaths() > 8) {// strong enough except special case

			} else if (block.getBreaths() > 4) {//

			} else {// in potential risk.

			}
		}
	}

	/**
	 * 块中棋子的可发展性决定棋块的发展性.发展大即有足够的生存空间. <br/>
	 * 天元单独放一子有四个方向可以发展.<br/>
	 * 计算每个点的发展方向个数,每个发展方向的深度. <br/>
	 * 本方的开拆界限按照开拆点的选择不能离敌方更进的原则确定.以此计算每个点的开拆.<br/>
	 * refer to GoBuJu.
	 * 
	 * @param block
	 */
	public int extensionable(Block block) {
		int totalCount = 0;
		int extCount;

		for (Point point : block.getPoints()) {

			if (point.isBorderEye()) { // bottom line is ignored.
				continue;
			}
			extCount = 0;
			for (Delta delta : Constant.ONE_DISTANCE) {
				if (extensible(point, delta, 2, block.getColor()) == true) {
					extCount++;
					block.addExtendOnePoint(point.getNeighbour(delta));
				}
			}
			if (extCount > 2) {// 多个方向可以扩展
				totalCount = totalCount + extCount + 1;
			} else if (extCount == 2) {
				totalCount += 3;
			} else if (extCount == 1) {// ==1
				totalCount += extCount;
			} else
				return 0;

			// 如果能够拆二,则累加计算
			extCount = 0;
			for (Delta delta : Constant.TWO_DISTANCE) {
				if (extensible(point, delta, 3, block.getColor()) == true) {
					extCount++;
					block.addExtendTwoPoint(point.getNeighbour(delta));
				}
			}
			if (extCount > 2) {
				totalCount = totalCount + 2;
			} else if (extCount == 2) {
				totalCount += 2;
			} else {// ==1
				totalCount += 1;
			}

			// 如果能够拆三,则累加计算
			// extCount = 0;
			// for (Delta delta : Constant.t) {
			// if (extensible(point, delta, 4, block.getColor()) == true)
			// extCount++;
			// }
			// if (extCount > 2) {
			// totalCount = totalCount + 2;
			// } else if (extCount == 2) {
			// totalCount += 2;
			// } else {// ==1
			// totalCount += 1;
			// }
		}
		return totalCount;
	}

	/**
	 * 
	 * @param point
	 * @param ext
	 * @param n
	 *            = 2 for 拆一.
	 * @return
	 */
	public boolean extensible(Point point, Delta delta, int n, int myColor) {
		Point ext = point.getNeighbour(delta);
		if (ext == null)
			return false;
		if (this.getColor(ext) != ColorUtil.BLANK) // 已经被占领
			return false;

		// 直线扩展路径上必须都为空白点
		if (delta.getRowDelta() == 0) {
			if (delta.getColumnDelta() > 0) {
				for (int i = 1; i < delta.getColumnDelta(); i++) {
					ext = point.getNeighbour(0, i);
					if (ext == null || this.getColor(ext) != ColorUtil.BLANK)
						return false;
				}
			} else {
				for (int i = 1 + delta.getColumnDelta(); i < 0; i++) {
					ext = point.getNeighbour(0, i);
					if (ext == null || this.getColor(ext) != ColorUtil.BLANK)
						return false;
				}
			}
		} else {
			if (delta.getColumnDelta() > 0) {
				for (int i = 1; i < delta.getRowDelta(); i++) {
					ext = point.getNeighbour(i, 0);
					if (ext == null || this.getColor(ext) != ColorUtil.BLANK)
						return false;
				}
			} else {
				for (int i = delta.getRowDelta(); i < 0; i++) {
					ext = point.getNeighbour(i, 0);
					if (ext == null || this.getColor(ext) != ColorUtil.BLANK)
						return false;
				}
			}
		}

		// 扩展点周围最近的棋块应该是正在计算扩展点的棋块本身..
		// 这样一来,扩展点周围如果有己方的棋子,扩展也不成立了.
		// 扩展点不可能在一路或者二路上.因为已经到边了.
		Point temp;
		ext = point.getNeighbour(delta);
		int enemyColor = ColorUtil.enemyColor(myColor);
		for (List<Delta> list : Constant.NEIGHBOR) {
			for (Delta neighbor : list) {
				if (neighbor.squareDistance() < n * n) {
					// temp = point.getNeighbour(neighbor);
					temp = ext.getNeighbour(neighbor);
					// if (temp != null && this.getColor(temp) == enemyColor)
					if (temp != null && this.getColor(temp) != ColorUtil.BLANK)
						return false;
				} else {
					return true;
				}

			}
		}
		return true;
	}

	public void logGroups(List<Group> groups) {
		if (log.isInfoEnabled() == false)
			return;

		log.info("全局共有" + groups.size() + "块组");

		for (Group group : groups) {
			if (group.isBlack())
				log.info(group);

		}
		for (Group group : groups) {
			if (group.isWhite())
				log.info(group);
		}
		for (Group group : groups) {
			for (Block block : group.getBlocks()) {
				// log.info(BasicBlock.getBehalfs(group.getBlocks()));
				if (block.getExtendOne().isEmpty() == false) {
					log.info("Block  " + block.getTopLeftPoint()
							+ " 扩展点(拆一)为: " + block.getExtendOne());
				}
				if (block.getExtendTwo().isEmpty() == false) {
					log.info("Block  " + block.getTopLeftPoint()
							+ " 扩展点(拆二)为: " + block.getExtendTwo());
				}
			}
		}

	}

	/**
	 * 棋块之间形式上可以断开,即存在断点的情况,需要判断对方的断是否成立.又一些简单的模式.比如 <br.>
	 * _XXXO<br/>
	 * ____X<br/>
	 * _____<br/>
	 * _____<br/>
	 * 的棋型,X方是可以连接的.除了征子的手法之外,打后枷吃也是一法,且对方没有引征的利用,更好.
	 * 一方面这个模式可以固化在代码中,无需临时计算.同时这种模式也可以用来验算吃子模块的计算是否正确.
	 * 关于这个模式,除了棋子要匹配外,对每个相关的块的气数也有要求.上方的X要求有5气.下方的X要求有3气. 目的是保证如下局面枷吃成立. _XXXO<br/>
	 * __OOX<br/>
	 * _X_X_<br/>
	 * _____<br/>
	 * 似乎模式匹配也很麻烦,还需要开发模式数据库,还是动态地临时计算较好 . 为了先做出原型,开发了一个简单的模式.
	 * (但是应用这些模式却颇费劲.即使拔每个模式的四角八边变化都存储了.如何决定切断点附近取几路去做模式匹配呢?) XO<br/>
	 * ___X<br/>
	 * ____<br/>
	 * 要求正好在二线和三线.上下线的X分别为3气和3气.
	 */

}
