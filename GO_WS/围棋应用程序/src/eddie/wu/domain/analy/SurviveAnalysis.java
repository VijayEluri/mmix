package eddie.wu.domain.analy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BlankBlock;
import eddie.wu.domain.Block;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.BoardPoint;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Delta;
import eddie.wu.domain.GoBoardLadder;
import eddie.wu.domain.Group;
import eddie.wu.domain.NeighborState;
import eddie.wu.domain.Point;
import eddie.wu.domain.Shape;
import eddie.wu.domain.Step;
import eddie.wu.domain.SymmetryResult;
import eddie.wu.domain.comp.BlockBreathComparatorDesc;
import eddie.wu.domain.comp.BlockSizeComparator;
import eddie.wu.domain.comp.GeneralBlockComparator;
import eddie.wu.domain.comp.RowColumnComparator;
import eddie.wu.domain.conn.ConnectivityAnalysis;
import eddie.wu.domain.survive.RelativeResult;
import eddie.wu.domain.survive.Result;
import eddie.wu.manual.SearchNode;
import eddie.wu.manual.TreeGoManual;
import eddie.wu.search.global.BigEyeSearch;
import eddie.wu.search.global.Candidate;
import eddie.wu.search.global.CandidateComparator;

/**
 * 判断是否两眼活棋。两眼活棋是最基本的终止局面，所以这个判断非常基础和重要。<br/>
 * 1. 这个问题还是挺复杂的，要考虑到特殊的活棋形式，比如两头蛇活棋，形式上是两个假眼。<br/>
 * 2. 还有二子气块被点的情况，一般是不入气，相当于一眼，但是也有可能对方可以提子。<br/>
 * 完整的大眼位被点入后容易成为不入气的局面，应该能直接识别为等价于眼位，而不需要提子。<br/>
 * 看来这些情况要通过高于棋块的数据结构来解决。<br/>
 * 
 * 术语的歧义.比如说不入气,可能有两个不同的意思.<br/>
 * 一是指对方落子后仅有一气,本方可以提吃.(即对方送吃)<br/>
 * 另一个意思是指对方落子后没有气,因而不能落子于该处(禁着不入气点).<br/>
 * 意思相近但是又的确有不同. <br/>
 * 分别称为GiftPoint和SuicidePoint。 <br/>
 * 
 * change history: 考虑劫活，但是只处理最简单的紧劫。<br/>
 * 
 * 原先死活的分析依赖于连接性分析的结果，因此有继承关系，现在好像没有这种依赖关系了。<br/>
 * 区分静态活棋和动态活棋；可以识别的活棋。<br/>
 * 静态识别的活棋指的是最终状态，没有变化了，反之视为动态的活棋，变化还没有彻底走完<br/>
 * 按照这个思路，双活其实是一个动态的活棋，因为送吃之后，对方活了，计算较为简单而已。<br/>
 * 活棋。确定的状态，最多有劫材利用。可以分为三种情况。<br/>
 * a. 没有劫材；b. 有有限劫材；c. 有无穷劫材。<br/>
 * 否则，对方要么可以杀棋，要么是还看不清楚。<br/>
 * <br/>
 * 2013.3.7 Sunday<br/>
 * 死活的定义目前是基于块的,总是谈论一个目标棋块的死活.这样有一些局限性,目前没有想到更好的办法<br/>
 * 只能是修补一下.<br/>
 * 活棋的定义是简单的,一块棋有两个或两个以上的不可紧的气 即可.一般的情况是真眼,<br/>
 * 也可以是直三被敌二子点入的型.更多的子也是可以的.<br/>
 * 这个是我们一般的关于净活的理解.也可以看作一个静态的定义,无需搜索.<br/>
 * 尚不足以包括双活的情况.双活的活棋可以说是一种动态的定义,需要搜索.<br/>
 * 对方是可以紧气的,我方当然提子,但是提子后未必是活棋,需要交换之后判断结果.<br/>
 * 双活是基于对方欲吃目标棋块反而导致目标棋块两眼活棋,因此可以弃权活棋(即使目前没有真正的两眼).<br/>
 * 他的特点是活棋依赖于另一块棋(或者多块棋).<br/>
 * 这和摇橹劫的双活是有本质不同的.<br/>
 * 死棋的定义是变化中被提走的棋是死棋,注意变化过程中,棋块的大小可能有很大的改变.比如块的合并.<br/>
 * 死棋定义的一个特殊困难在于,以块棋的死亡往往不是变化的终结,因此我们必须检查,目标块被提吃后<br/>
 * 是否其所有周围块都是活棋.<br/>
 * 一般来说,如果周围块仅靠提吃目标块活棋,那么目标块必须四子或者以上.<br/>
 * 如果已经有一个眼,目标块有一个子就够了. <br/>
 * 小棋盘上的搜索不用考虑得这么复杂. <br/>
 * <br/>
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class SurviveAnalysis extends ConnectivityAnalysis {

	private static final Logger log = Logger.getLogger(SurviveAnalysis.class);

	public SurviveAnalysis(BoardColorState colorState) {
		super(colorState);
	}

	public SurviveAnalysis(byte[][] state) {
		super(state);

	}

	public SurviveAnalysis(byte[][] state, int whoseTurn) {
		super(state, whoseTurn);

	}

	public SurviveAnalysis(int boardSize) {
		super(boardSize);
	}

	/**
	 * 处理不了的情况。<br/>
	 * . 块[1,12]没有真眼
	 */
	public void analyzeAllBlock() {
		int toCheck = 7;
		int count = 0;
		List<Block> blocks = new ArrayList<Block>();
		blocks.addAll(this.getBlackWhiteBlocks());
		Collections.sort(blocks, new BlockBreathComparatorDesc());
		for (Block block : blocks) {
			count++;
			if (count != toCheck) {
				continue;
			}

			boolean live = this.isAlreadyLive_dynamic(block.getBehalfPoint());
			if (log.isDebugEnabled())
				log.debug("check " + block);
			if (log.isDebugEnabled())
				log.debug("live = " + live);

			if (live == false) {
				for (BlankBlock blankBlock : block.getBreathBlocks()) {
					if (blankBlock.isEyeBlock()) {

					}
				}
			}

			if (count == toCheck)
				break;
		}
	}

	// public boolean isLadderTrue(Block block){
	// CaptureSearch cs = new CaptureSearch(ladder.getMatrixState(),
	// toCut.get(0), enemyColor, false);
	// if (cs.globalSearch() == CaptureSearch.CAPTURE_SUCCESS) {
	//
	// }

	public void analyzeAllGroup() {
		for (Group group : groups) {

			for (Block block : group.getBlocks()) {
				if (this.isAlreadyLive_dynamic(block.getBehalfPoint())) {

					if (log.isEnabledFor(Level.WARN))
						log.warn("check group " + group);
					if (log.isEnabledFor(Level.WARN))
						log.warn("group is live because its block is live"
								+ block.getBehalfPoint());

					group.setLive(true);
					break;
				}
			}
			if (group.isLive() == false) {
				int eyeAreaSize = this.getEyeArea2(group).size();
				if (eyeAreaSize >= 10) {
					group.setLive(true);
					if (log.isEnabledFor(Level.WARN))
						log.warn("group is live because of big eye area "
								+ eyeAreaSize);
				}
			}
			if (group.isLive() == false) {
				if (log.isEnabledFor(Level.WARN))
					log.warn("check group " + group);
				if (log.isEnabledFor(Level.WARN))
					log.warn("group is not live ");
			}

		}

		for (Group group : groups) {
			if (group.isLive())
				continue;
			if (log.isEnabledFor(Level.WARN))
				log.warn("not live group" + group);
			if (group.hasWeakerGroup()) {
				group.setLive(true);
				if (log.isEnabledFor(Level.WARN))
					log.warn("it becomess live because of weaker neighbor group");
			}
		}

	}

	public boolean canIncreaseBreath_netly(Block block) {

		return this.getMaxNewBreath(block) - block.getBreaths() >= 2;
		// assertTrue("target block should only have one or two breath",
		// block.getBreaths() <= 2);
		// int oldBreath = block.getBreaths();
		// for (Point breath : block.getBreathPoints()) {
		// for (Delta delta : Constant.ADJACENTS) {
		// Point neighborP = breath.getNeighbour(delta);
		// if (neighborP == null)
		// continue;
		// int newBreath = this.breathAfterPlay(breath, block.getColor())
		// .size();
		// // System.out.print("oldBreath = " + oldBreath);
		// // System.out.println("newBreath = " + newBreath);
		// if (newBreath >= oldBreath + 2) {
		// return true;
		// }
		// // TODO: if breath are same.
		// }
		// }
		// return false;
	}

	public boolean canIncreaseBreath_temporarily(Block block) {
		return this.getMaxNewBreath(block) - block.getBreaths() >= 1;
	}

	/**
	 * 要送吃的块,原先只有二气,落子后会减少一气.如果两口气都是这种情况,只有送吃一途.<br/>
	 * 但未必不行.若有一个能长气,则也是脱困之道.<br/>
	 * 还有扑的情况.<br>
	 * 简化处理.要求该块的气点除了不入气点,就是送吃点. <br/>
	 * whether the block can increase its breath. <br/>
	 * 要求原块是二气，计算是否能增加到三气，然后可以入气<br/>
	 * 两扳长一气.
	 * 
	 * @param block
	 *            该块能否长气。
	 * @return
	 */
	public int getMaxNewBreath(Block block) {
		/**
		 * 01[_, B, W]01<br/>
		 * 02[B, B, _]02<br/>
		 * 03[B, _, _]03<br/>
		 */
		// assertTrue("target block should only have one or two breath",
		// block.getBreaths() <= 2);

		/**
		 * Important: to consider the way to capture enemies!
		 */
		Set<Point> expandPoints = new HashSet<Point>();
		for (Block blockEnemy : block.getEnemyBlocks()) {
			// the point to eat the enemy
			if (blockEnemy.getBreaths() == 1) {
				/**
				 * text[0] = new String("[B, B, W, _]");<br/>
				 * text[1] = new String("[B, B, B, W]");<br/>
				 * text[2] = new String("[B, B, B, _]");<br/>
				 * text[3] = new String("[B, B, B, B]");<br/>
				 */
				Point eatingPoint = blockEnemy.getUniqueBreath();

				if (block.getBreathPoints().contains(eatingPoint)) {
					expandPoints.add(eatingPoint);
				} else {
					// eating increase at lease one breath without fill own
					// breath.NOT accurate, but enough currently.
					return block.getBreaths() + 1;
				}
			}
		}

		// increase breath by extending in current breath (eating included)
		expandPoints.addAll(block.getBreathPoints());

		int maxBreath = block.getBreaths() - 1;
		for (Point breath : expandPoints) {
			for (Delta delta : Constant.ADJACENTS) {
				Point neighborP = breath.getNeighbour(delta);
				if (neighborP == null)
					continue;
				int breaths = this.breathAfterPlay(breath, block.getColor())
						.size();
				if (breaths > maxBreath) {
					maxBreath = breaths;
				}
			}
		}

		return maxBreath;
	}

	/**
	 * whether each point's liveness is decided. TODO: enhance fake code very
	 * good test case: Exception for count=8331<br/>
	 * [B, _, W]<br/>
	 * [B, _, W]<br/>
	 * [B, B, W]<br/>
	 * 
	 * @return
	 */

	public void clearLiveDeadFlag() {

	}

	public void controlArea(Group group) {
		Shape shape = group.getShape();

		// if(shape.)
		group.getAllPoints();

	}

	// public List<Candidate> getCandidate(int color,
	// boolean filterSymmetricEquivalent) {
	// return getCandidate_smallBoard(color, filterSymmetricEquivalent, 0);
	// }

	/**
	 * 1. reduce candidates by symmetry<br/>
	 * 2. sort by priority.<br/>
	 * 3. avoid fill eye point.<br/>
	 * 4. eating/capturing first <br/>
	 * 5. captured last (gift 送吃)<br/>
	 * 5.1 送吃的子如果已经不活（无眼），优先于弃权；如果有眼位，则在弃权之后考虑<br/>
	 * 
	 * @return
	 */
	public List<Candidate> getCandidate_smallBoard(int color,
			boolean filterSymmetricEquivalent, int expectedScore) {
		assert this.boardSize <= 5;
		Map<Point, Integer> breathMap = new HashMap<Point, Integer>();

		Set<Point> points = new HashSet<Point>();
		for (int row = 1; row <= boardSize; row++) {
			for (int column = 1; column <= boardSize; column++) {
				BoardPoint boardPoint = getBoardPoint(row, column);
				if (boardPoint.getColor() == Constant.BLANK) {

					BlankBlock blankBlock = boardPoint.getBlankBlock();
					if (blankBlock.isEyeBlock()) {
						/**
						 * 3. avoid fill eye point. <br/>
						 * 防止自填眼位,尚未处理大眼位做眼的情况。
						 */
						int blocks = blankBlock.getNeighborBlocks().size();
						if (blankBlock.isSinglePointEye()) {
							// 己方不填眼，对方是否可下看气数。
							if ((color == Constant.BLACK && blankBlock
									.isBlackEye())
									|| (color == Constant.WHITE && blankBlock
											.isBlackEye() == false)) {

								/**
								 * need to handle this exceptional case, the eye
								 * might be an fake eye<br/>
								 * ## 01,02,03,04 <br/>
								 * 01[B, W, _, W]01<br/>
								 * 02[_, B, W, _]02<br/>
								 * 03[B, _, B, W]03<br/>
								 * 04[_, B, W, _]04<br/>
								 * ## 01,02,03,04 <br/>
								 */
								if (blocks == 1) {
									continue;// do not fill real eyes.
								} else {
									// may connect blocks
									boolean realSingleEye = this
											.isRealSingleEye(
													blankBlock
															.getMinBreathNeighborBlock(),
													blankBlock.getUniquePoint());
									if (realSingleEye) {
										continue;
									}
								}
								//
								// int minBreath = blankBlock
								// .getMinBreathNeighborBlock()
								// .getBreaths();
								// if (minBreath == 1) {
								// // consider connect together.
								// } else if (minBreath == 2) {
								//
								// } else {
								// // connect is not gifting
								// }

								/**
								 * $$$01,02,03,04 <br/>
								 * 01[_, B, _, B]01<br/>
								 * 02[B, B, B, _]02<br/>
								 * 03[W, W, W, B]03<br/>
								 * 04[W, _, W, _]04<br/>
								 * $$$01,02,03,04 <br/>
								 * whoseTurn=Black <br/>
								 * TODO:
								 */

							}
						} else {
							/**
							 * if big blank eye belongs to live block, then no
							 * sense for each side to play here.
							 */
							// if ((color == Constant.BLACK && blankBlock
							// .isBlackEye())
							// || (color == Constant.WHITE && blankBlock
							// .isBlackEye() == false)) {
							if (blocks == 1) {
								if (this.isAlreadyLive_dynamic(blankBlock
										.getUniqueNeighborBlock()
										.getBehalfPoint())) {
									continue;
								}
							}
							// }

						}
					} else if (blankBlock.isInitBlankBlock()) {

					} else { // not eye block
						Block liveBlock = blankBlock.getBiggestNeighborBlock();
						if (liveBlock == null) {
							if (log.isEnabledFor(Level.WARN))
								log.warn(this.getBoardColorState()
										.getStateString());
							if (log.isEnabledFor(Level.WARN))
								log.warn("blnakblock="
										+ blankBlock.getBehalfPoint());
						}

						/**
						 * make no sense to break/make live block's eye.
						 * 
						 * ##01,02,03,04 <br/>
						 * 01[W, _, W, _]01<br/>
						 * 02[_, B, _, W]02<br/>
						 * 03[W, W, W, _]03<br/>
						 * 04[_, B, B, W]04<br/>
						 * ##01,02,03,04 <br/>
						 */
						// if(live&&liveBlock.getColor()!=color){
						// if (liveBlock.isAlreadyLive() == false
						// && liveBlock.getNumberOfPoint() >= 4) {
						// Point target = liveBlock.getBehalfPoint();
						// boolean live = this.isAlreadyLive_dynamic(target);
						// liveBlock.setLive(live);
						// }
						// if (liveBlock.isAlreadyLive()
						// && liveBlock.getLiveWith() != null
						// && liveBlock.getLiveWith().contains(
						// boardPoint.getBlankBlock())) {
						// continue;
						// }

					}

					int breaths = breathAfterPlay(boardPoint.getPoint(), color)
							.size();
					if (breaths > 0) {
						points.add(boardPoint.getPoint());
						breathMap.put(boardPoint.getPoint(), breaths);
					}
				}
			}
		}

		/**
		 * 处理本质上等价的候选棋步.
		 */
		List<Point> can = new ArrayList<Point>();

		if (filterSymmetricEquivalent == true) {
			SymmetryResult symmetryResult = this.getSymmetryResult();
			if (symmetryResult.getNumberOfSymmetry() != 0) {

				Set<Point> points2 = new HashSet<Point>();
				Set<Point> listAll = new HashSet<Point>();
				for (Iterator<Point> iter = points.iterator(); iter.hasNext();) {
					Point point = iter.next();
					if (listAll.contains(point))
						continue;

					List<Point> listVar = point.deNormalize(symmetryResult);
					listAll.addAll(listVar);
					// only keep one of all the symmetric candidates.
					points2.add(listVar.get(0));

				}
				can.addAll(points2);
			} else {
				can.addAll(points);
			}
		} else {
			can.addAll(points);
		}

		List<Candidate> gifts = new ArrayList<Candidate>();
		/**
		 * decide sequence by priority.<br/>
		 * capture sequence. <br/>
		 */
		List<Candidate> candidates = new ArrayList<Candidate>();
		for (Point point : can) {
			Candidate candidate = new Candidate();
			candidate.setStep(new Step(point, color, getShoushu() + 1));
			NeighborState state = null;
			try {
				state = this.getNeighborState_forCandidate(point, color);
			} catch (RuntimeException e) {
				if (log.isEnabledFor(Level.WARN)) {
					log.warn(this.getBoardColorState().getStateString());
					log.warn("point" + point);
					// this.printState();
					throw e;
				}
			}

			/**
			 * avoid eaten dead enemy to enhance live target.
			 */
			if (state.isEating()) {
				if (state.getFriendBlockNumber() == 1) {
					Block friendBlock = state.getFriendBlocks().iterator()
							.next();
					Point pointT = friendBlock.getBehalfPoint();
					if (friendBlock.getNumberOfPoint() >= 4) {
						boolean live = this.isAlreadyLive_dynamic(pointT);
						if (live) {
							continue;
						}
					}
				}
			}
			candidate.setEating(state.isEating());
			candidate.setGifting(state.isGifting());
			candidate.setCapturing(state.isCapturing());
			candidate.setRemoveCapturing(state.getRemoveCapturing());
			candidate.setBreaths(breathMap.get(point));
			candidate.setIncreasedBreath(state.getIncreasedBreath());
			candidate.setIncreasedBreath(state.getIncreasedBreath());

			/**
			 * 缩小眼位的紧气看成类似送礼，送吃。<br/>
			 * TODO: further refinement.
			 */
			if (state.isGifting() == false) {

				this.initEyesAfterPlay_dynamic(candidate);

			}
			// not sure of the intention
			// if (state.isEating() == false && candidate.getEyes() == 0
			// && state.isCapturing()
			// && state.getCapturingBlocks().size() == 1
			// && state.getFriend() > 0) {
			// Block capturingB = state.getCapturingBlocks().iterator().next();
			// if (capturingB.getEnemyBlocks().isEmpty()) {
			// boolean pointedEye = true;
			// for (BlankBlock blankB : capturingB.getBreathBlocks()) {
			// for (Block tB : blankB.getNeighborBlocks()) {
			// if (state.getFriendBlocks().contains(tB) == false) {
			// pointedEye = false;
			// }
			// }
			// }
			// if (pointedEye) {
			// state.setGifting(true);
			// // gifts.add(candidate);
			// state.setCapturing(false);
			// }
			// } else {
			// if (capturingB.getEnemyBlocks().size() == 1) {
			// state.setGifting(true);
			// // gifts.add(candidate);
			// state.setCapturing(false);
			// }
			// }
			// } else {
			// // 同样的紧气,如果多一眼于己更有利.
			// // candidate.setEyes(this.EyesAfterPlay(point, color).size());
			//
			// }
			if (state.isGifting()) {
				if (state.getGift() == null) {
					// 单子扑入.
					gifts.add(candidate);
				} else if (state.getGift().getOriginalStones() == 0) {
					candidates.add(candidate);// gift one point usually good.
				} else if (state.getGift().getOriginalBreath() >= 2) {

					if (state.getGift().getOriginalStones() >= 6) {
						// should not gift so much.
					} else {
						gifts.add(candidate);
					}
				} else {
					candidates.add(candidate);
				}
				// } else if (state.isIncreaseBreath() == false) {
				// not favor the move cannot increase breath.
				// gifts.add(candidate);
			} else {
				candidates.add(candidate);
			}
		}

		// Collections.sort(can, new LowLineComparator());
		Collections.sort(candidates, new CandidateComparator());

		// consider give up step: comment out because it may cause no
		// candidates.
		// int score = this.finalResult_simplest().getScore();
		// if (color == Constant.MAX) {
		// if (score < 0)
		// return candidates;
		// } else {
		// if (score > 0)
		// return candidates;
		// }

		Candidate candidateP = new Candidate();
		candidateP.setStep(new Step(null, color, getShoushu() + 1));
		if (this.getStepHistory().getAllSteps().isEmpty() == false
				&& this.getLastStep().isGiveup() == true) {
			// 前一步对方弃权,下一步有限考虑弃权,有望及早到达终点状态.
			// this logic is only good for 2*2 board.
			if (boardSize == 2 || boardSize == 3) {
				candidates.add(0, candidateP);
			}
			// } else if (this.boardSize <= 3
			// && ((this.finalResult_deadCleanedUp().getScore() >= expectedScore
			// && color == Constant.BLACK) || (finalResult_deadCleanedUp()
			// .getScore() <= expectedScore && color == Constant.WHITE))) {
			// // this logic is only good for 2*2 board.
			// candidates.add(0, candidateP);
		} else {
			candidates.add(candidateP);
		}
		// 送礼点也可能是正解，但可能性较小，排在弃权后面。
		Collections.sort(gifts, new CandidateComparator());
		candidates.addAll(gifts);
		return candidates;
		// can.clear();
		// for (Candidate candidate : candidates) {
		// can.add(candidate.getStep().getPoint());
		// }
		// return can;
	}

	/**
	 * 一块棋的潜在眼位大小，估算。不是死活计算。<br/>
	 * 将一块棋沿边境线扩展，到达底线，封闭出一个区域。<br/>
	 * 需要扩展的程度也反映了眼位的成型程度<br/>
	 * 平方距离为1的扩张，只差边线没有围上。<br/>
	 * 平方距离为4的扩张，只差边线和二线没有围上。<br/>
	 * 平方距离为9的扩张，边线二线和三线都没有围上。一般不考虑<br/>
	 */
	public Set<Point> getEyeArea(Block block) {
		Set<Point> eyeArea = new HashSet<Point>();

		for (Point point : block.getPoints()) {
			for (List<Delta> listDelta : Constant.NEIGHBOR_FROM_ADJACENT) {
				for (Delta delta : listDelta) {
					Point temp = point.getNeighbour(delta);
					if (temp == null)
						continue;

					if (this.getColor(temp) == Constant.BLANK) {
						for (Delta deltaR : listDelta) {

						}
					}
				}
			}
		}
		return eyeArea;
	}

	/**
	 * 新的实现。依赖于Connectivity的算法，围成松散眼位的块往往不完整。也不可能象<br/>
	 * 眼块那样通过眼块关联起来。
	 * 
	 * @param group
	 * @return
	 */
	public Set<Point> getEyeArea2(Group group) {
		Set<Point> all = new HashSet<Point>();
		Set<Point> eyeArea = new HashSet<Point>();
		Set<Point> eyeArea_copy = new HashSet<Point>();

		Set<Point> lastLevel = new HashSet<Point>();
		Set<Point> tempLevel;
		Set<Point> nextLevel = new HashSet<Point>();
		for (Block block : group.getBlocks()) {
			lastLevel.addAll(block.getBreathPoints());
		}
		all.addAll(lastLevel);
		if (log.isDebugEnabled()) {
			log.debug("原有气点" + this.getPointList(lastLevel));
		}

		int level = 1;
		int myColor = group.getColor();
		/**
		 * 距离按照row+column来算。最多五线占边。
		 */
		do {
			log.debug("候选点为" + this.getPointList(lastLevel));
			for (Point point : lastLevel) {
				int enemy = 0;
				for (Delta delta : Constant.ADJACENTS) {
					Point temp = point.getNeighbour(delta);
					if (temp == null)
						continue;

					if (this.isBlank(temp)) {
						// if (!lastLevel.contains(temp)
						// && !eyeArea.contains(temp))
						if (!all.contains(temp)) {
							nextLevel.add(temp);
							all.add(temp);
						} else {
							// ever considered
						}
					} else if (this.getColor(temp) == myColor) {

					} else if (level > 1
							&& (this.getBlock(temp).isAlreadyLive() || this
									.getBlock(temp).getBreaths() > group
									.getBreaths())) {
						// Strong enemy!
						enemy++;
						break;
					}

				}
				if (enemy == 0) {
					if (this.no_enemy_nearby(group, point, level - 1)) {
						eyeArea.add(point);
						if (log.isDebugEnabled()) {
							log.debug("Add point" + point);
						}
					}

				}
			}
			if (log.isDebugEnabled()) {
				log.debug("第" + level + "轮后眼位点为  " + this.getPointList(eyeArea));
				eyeArea.removeAll(eyeArea_copy);
				log.debug("第" + level + "轮后新增眼位点为  "
						+ this.getPointList(eyeArea));
				eyeArea.addAll(eyeArea_copy);
				eyeArea_copy.addAll(eyeArea);

			}

			lastLevel.clear();
			tempLevel = lastLevel;
			lastLevel = nextLevel;
			nextLevel = tempLevel;

		} while (++level <= 4);

		if (log.isDebugEnabled()) {
			log.debug("size=" + eyeArea.size());
			log.debug("眼位点为  " + this.getPointList(eyeArea));
		}

		/**
		 * 临界的点不算在眼位内。 <br/>
		 * mark all the point in eye area, then the point adjacent to blank is
		 * in the boarder.<br/>
		 * Design change: extend to adjacent blank point.
		 * 
		 */
		Set<Point> extOne = new HashSet<Point>();
		for (Point point : eyeArea) {
			this.setColor(point, myColor);
		}
		for (Iterator<Point> pointIter = eyeArea.iterator(); pointIter
				.hasNext();) {
			Point point = pointIter.next();
			for (Delta delta : Constant.ADJACENTS) {
				Point temp = point.getNeighbour(delta);
				if (temp == null)
					continue;
				if (this.isBlank(temp)) {
					// pointIter.remove();
					extOne.add(temp);
					break;
				}
			}

		}
		for (Point point : eyeArea) {
			this.setColor(point, Constant.BLANK);
		}
		eyeArea.addAll(extOne);
		if (log.isDebugEnabled()) {
			log.debug("size=" + eyeArea.size());
			log.debug(extOne.toString());
			log.debug("add相邻点后眼位点为  " + this.getPointList(eyeArea));
		}

		group.setEyeArea(eyeArea);
		return eyeArea;

	}

	public Set<Point> getEyes(Block targetBlock) {
		return this.getEyes(targetBlock, targetBlock.getColor());
	}

	/**
	 * 检查不入气点(或者气块),可能是假眼. 调用该方法前，已经判断过没有大眼（子数>=7）。<br/>
	 * 所以这里每个眼块都算一个眼。<br/>
	 * 但是这些稍大一点的眼未必是真眼，比如边上的二子眼（甚至是四子眼）可能是假眼。
	 * 
	 * @param targetBlock
	 * @param myColor
	 * @return
	 */
	public Set<Point> getEyes(Block targetBlock, int myColor) {
		if (log.isEnabledFor(Level.WARN)) {
			log.warn("original eyes of block " + targetBlock.getBehalfPoint()
					+ " = " + targetBlock.getEyes());
		}
		targetBlock.clearEyes();

		Set<Point> eyes = new HashSet<Point>();
		/*
		 * 检查气点中是否有眼。二三子的眼看成一个不入气.
		 */
		for (BlankBlock blankBlock : targetBlock.getBreathBlocks()) {
			int numberOfPoint = blankBlock.getNumberOfPoint();
			if (blankBlock.isEyeBlock() == false) {// 是一个自提不入气点
				if (numberOfPoint == 1) {
					Point breath = blankBlock.getUniquePoint();
					if (isEye(targetBlock, breath)) {// 不入气点能否成眼
						eyes.add(breath);
						targetBlock.addEye(breath);
						if (log.isInfoEnabled()) {
							log.info("单子公共气块(自提不入气) " + breath);
						}
					}

				}
				continue;
			}

			if (this.isEyeValid(targetBlock, blankBlock) == false) {
				continue;
			}

			if (numberOfPoint > 1) {// 多子眼块

				Point topLeftPoint = blankBlock.getTopLeftPoint();
				eyes.add(topLeftPoint);
				targetBlock.addEye(topLeftPoint);
				if (log.isInfoEnabled()) {
					log.info("多子眼块 " + numberOfPoint + topLeftPoint);
				}

				// block breath block info should already be covered.
			} else { // 单子眼块
				Point breath = blankBlock.getUniquePoint();
				eyes.add(breath);
				targetBlock.addEye(breath);
				if (log.isInfoEnabled()) {
					log.info("单子眼块 " + numberOfPoint + breath);
				}
			}
		}
		if (log.isInfoEnabled()) {
			if (log.isEnabledFor(Level.WARN))
				log.warn("不入气点(或气块)为: " + eyes);
		}
		if (log.isEnabledFor(Level.WARN)) {
			log.warn("New DATA eyes of block " + targetBlock.getBehalfPoint()
					+ " = " + targetBlock.getEyes());
			log.warn("New DATA eyes of block " + targetBlock.getBehalfPoint()
					+ " = " + eyes);
		}
		return eyes;
	}

	/**
	 * 收集一块棋的送吃不入气点。主要是看有没有双活的情况。这是两眼活棋之外的另外一种终局<br/>
	 * 可能，因为变化没有穷尽，需用动态的计算，不能静态地判定。此时没有自提不入气点的可能。
	 * 
	 * @param targetBlock
	 *            目标棋块
	 * @return
	 */
	public Set<Point> getGiftPoints(Block targetBlock, boolean oneEyeAlready) {
		Set<Point> gifts = new HashSet<Point>();// 对方可能的送礼/吃点。
		Set<Point> breathPoints = new HashSet<Point>();

		Set<Block> enemyBlocks = new HashSet<Block>();// 共享该气块的敌块
		for (BlankBlock breathBlock : targetBlock.getBreathBlocks()) {

			if (breathBlock.isEyeBlock())
				continue;

			if (targetBlock.isBlack())
				enemyBlocks.addAll(breathBlock.getWhiteBlocks());
			else
				enemyBlocks.addAll(breathBlock.getBlackBlocks());
		}

		for (Block enemyBlock : enemyBlocks) {// consider 2 to 3 breaths.
			if (enemyBlock.getBreaths() > 3) {

				continue;
			} else if (enemyBlock.getBreaths() < 2) {
				Set<Point> breathAfterPlay = this.breathAfterPlay(
						enemyBlock.getUniqueBreath(), enemyBlock.getColor());
				if (breathAfterPlay.isEmpty()) {
					continue; // 自提不入气点。
				} else if (breathAfterPlay.size() == 1) {
					gifts.add(enemyBlock.getUniqueBreath());
				}
			}
			// enemyBlock.getBreaths() == 2
			// copy to prevent concurrent modification during oneStepForward.
			breathPoints.clear();// fix a bug here.
			breathPoints.addAll(enemyBlock.getBreathPoints());

			if (oneEyeAlready == true) {
				breathPoints.retainAll(targetBlock.getBreathPoints());
			} else {
				if (targetBlock.getBreathPoints().containsAll(breathPoints) == false) {
					// 不是共享两口公气的情况。
					continue;
				}
			}
			int countOneBreathPoint = 0;
			int countTwoBreathPoint = 0;
			for (Point breath : breathPoints) {
				Set<Point> breathPoints2 = this.breathAfterPlay(breath,
						enemyBlock.getColor());
				int breaths = breathPoints2.size();
				if (breaths == 1) {
					countOneBreathPoint++;
				} else if (breaths == 2) {
					countTwoBreathPoint++;
				}
			}

			if (oneEyeAlready == true) {
				if (countOneBreathPoint < 1) {
					if (countTwoBreathPoint < 1) {

					} else {// 有可能紧气成为双活。
						targetBlock.setUnknown(true);
						// TODO:比较双方外气。
					}
					continue;
				}
			} else {
				// 并非两个都是送吃点。
				if (countOneBreathPoint < 2)
					continue;
			}

			for (Point breath : breathPoints) {
				Set<Point> breathPoints2 = this.breathAfterPlay(breath,
						enemyBlock.getColor());
				int breaths = breathPoints2.size();

				if (breaths == 1) {
					// 直接落子是自紧一气，有没有其他的长气手段，如提子。
					if (this.canIncreaseBreath_temporarily(enemyBlock))
						continue;

					log.warn("对方尝试送吃 " + breath);
					boolean validRes = this.oneStepForward(breath,
							enemyBlock.getColor());
					// BUG here, fixed.
					// log.info("气数变为： " + enemyBlock);
					// log.info("气数变为： " + enemyBlock.getAllBreathPoints());
					// log.info("本方提子于 " + enemyBlock.getLastBreath());
					// TODO:计算过程中可能块被改变了（生成新块）。enemyBlock.getColor()
					Block newBlock = this.getBlock(breath);
					if (newBlock == null) {
						// TODO: bug
						System.out
								.println("play at " + breath + "with color="
										+ enemyBlock.getColor() + ", valid="
										+ validRes);
						if (log.isEnabledFor(Level.WARN))
							log.warn(this.getBoardColorState().getStateString());
						validRes = this.oneStepForward(breath,
								enemyBlock.getColor());
					}
					log.info("气数变为： " + newBlock.getBreaths());
					log.info("本方提子于 " + newBlock.getLastBreath());
					boolean valid = this.oneStepForward(
							newBlock.getLastBreath(), targetBlock.getColor());
					if (valid == true) {

						// change: 如果已经有一眼，提子后简单视为活棋。（实际上尚有变化）
						// TODO: dynamic search here.
						if (oneEyeAlready) {
							if (this.getRealEyes(targetBlock.getBehalfPoint(),
									false).getRealEyes().size() >= 2) {
								log.warn("确定了一个送吃点 " + breath);
								gifts.add(breath);
							}
						} else if (this.isBigEyeLive(targetBlock
								.getBehalfPoint())) {
							log.warn("确定了一个送吃点 " + breath);
							gifts.add(breath);
						}
					}
					this.oneStepBackward();
					this.oneStepBackward();

				}
			}

		}
		if (gifts.isEmpty()) {
			if (log.isEnabledFor(Level.WARN))
				log.warn("there is no gift point for status at point"
						+ targetBlock.getBehalfPoint());
		} else {
			if (log.isEnabledFor(Level.WARN))
				log.warn("gift point " + gifts + " for target block "
						+ targetBlock.getBehalfPoint());
		}
		if (boardSize > 10) {
			this.printState(targetBlock.getShape());
		} else {
			this.printState();
		}
		return gifts;
	}

	public List<Point> getPointList(Set<Point> eyeArea) {

		List<Point> list = new ArrayList<Point>(eyeArea.size());
		list.addAll(eyeArea);
		Collections.sort(list, new RowColumnComparator());

		return list;
	}

	/**
	 * whether target block can live if playing first.
	 * 
	 * @param point
	 * @return
	 */
	public boolean isCanLive_dynamic(Point point) {
		return false;
	}

	/**
	 * 
	 * 
	 * @deprecated only called when neighbor is live! TODO
	 * @param point
	 * @return
	 */
	private boolean isAlreadyDead_internal(Point point) {
		Block target = this.getBlock(point);

		Set<Point> eyePoints = new HashSet<Point>();
		// TODO:
		for (BlankBlock blankB : target.getBreathBlocks()) {
			eyePoints.addAll(blankB.getPoints());
		}

		boolean targetFirst = true;
		Result res = this.isBigEyeLive_dynamic_internal(target, eyePoints,
				targetFirst);
		return res.getSurvive() == RelativeResult.ALREADY_DEAD;
	}

	/**
	 * is the block live, currently only consider the shape of one block.<br/>
	 * 先考虑一块棋两眼的情况，比较简单。然后处理多块棋的情况。 <br/>
	 * key method!
	 * 
	 * @param point
	 *            any point in the target block<br/>
	 *            用块中的任意点来代表块.
	 * @return
	 */
	public boolean isAlreadyLive_dynamic(Point point) {
		Block targetBlock = this.getBlock(point);
		if (targetBlock == null) {
			log.warn("null block for point =" + point);
			throw new RuntimeException("null block for point =" + point);
		}
		point = targetBlock.getBehalfPoint();
		if (log.isEnabledFor(Level.WARN)) {
			log.warn("Is live for " + point + " at status: ");
			if (boardSize > 10) {
				this.printState(targetBlock.getShape());
			} else {
				this.printState(log);
			}
			log.info("for block:");
			log.info(getBlock(point));
		}

		boolean live;

		live = isOneBlockTwoEyeLive(point);
		if (live == true) {

			if (log.isEnabledFor(Level.WARN)) {
				log.warn("简单的两真眼活棋！" + point);
			}
			return true;
		}

		live = this.isBigEyeLive(point);
		if (live == true) {
			if (log.isEnabledFor(Level.WARN)) {
				log.warn("简单的大眼活棋！" + point);
			}
			return true;
		}

		Block block = getBlock(point);
		int myColor = block.getColor();
		// live with weak neighbor block

		Block enemyBlock = block.getMinBreathEnemyBlock();
		if (enemyBlock != null && enemyBlock.getBreaths() <= block.getBreaths()) {

		}

		// Set<Point> eyes = getEyes(block, myColor);

		Set<Point> eyes = this.getRealEyes(point, false).getRealEyes();
		// if (log.isInfoEnabled()) {
		log.warn("块代表点为 " + point + ", 周围眼位气块为 " + eyes);
		// }
		if (eyes.size() < 2) {
			// 双活在别处考虑
			return this.isLiveWithoutTwoEye(point);
			// return false;
			// if (eyes.isEmpty()) {// 一个眼也没有。
			// Set<Point> giftPoints = this.getGiftPoints(targetBlock, false);
			// if (log.isInfoEnabled())
			// log.info("送吃不入气点： " + giftPoints);
			// if (giftPoints.size() >= 2) {
			// log.info("没有眼，两个或以上送吃不入气点活棋。");
			// return true;
			// }
			// } else if (eyes.size() == 1) {// 已经有一眼。
			// Set<Point> giftPoints = this.getGiftPoints(targetBlock, true);
			// if (log.isInfoEnabled())
			// log.info("已经有一眼，送吃不入气点： " + giftPoints);
			// if (giftPoints.size() >= 1) {
			// log.info("一个或以上送吃不入气点活棋。");
			// return true;
			// }
			//
			// }
			//
			// if (log.isEnabledFor(Level.WARN))
			// log.warn("live = " + live);
			// return live;

		}

		// TODO:
		return isLive_Special(point, myColor);
		// return false;
	}

	// public boolean isBigEyeLive(Block target, BlankBlock eyeBlock) {
	//
	// byte[][] state = this.getMatrixState();
	// BigEyeSearch search = new BigEyeSearch(state, target.getBehalfPoint(),
	// eyeBlock, false, false);
	// int score = search.globalSearch();
	// if (score == RelativeResult.ALREADY_LIVE)
	// return true;
	// else
	// return false;
	// }

	/**
	 * 大眼，敌方先走，是否活棋。本方可能额外还有一眼。
	 * 
	 * @param target
	 * @param eyeBlock
	 * @param targetFirst
	 * @return
	 */
	public boolean isBigEye_alreadyLive(Block target, BlankBlock eyeBlock) {
		return this.isBigEyeLive_dynamic(target, eyeBlock, false);
	}

	public boolean isBigEye_canLive(Block target, BlankBlock eyeBlock) {
		return this.isBigEyeLive_dynamic(target, eyeBlock, true);
	}

	/**
	 * also consider the eyes with enemy point filled in. <br/>
	 * constraint: there should be only shared breath between target and
	 * enemyBlock
	 * 
	 * @param target
	 * @return
	 */
	public boolean isBigEyeLive(Point target) {

		Block targetBlock = getBlock(target);
		Set<Set<Point>> eyeSet = new HashSet<Set<Point>>();

		Set<Set<Point>> singleBlockEyeSet = new HashSet<Set<Point>>();
		Set<Set<Point>> multipleBlockEyeSet = new HashSet<Set<Point>>();
		Set<Set<Point>> sharedBreathSet_singleBlock = new HashSet<Set<Point>>();
		Set<Set<Point>> sharedBreathSet_multiBlock = new HashSet<Set<Point>>();
		for (BlankBlock blankBlock : targetBlock.getBreathBlocks()) {
			if (blankBlock.isEyeBlock()) {
				// for eye block, we allow several target block.
				Set<Point> eyePoints = new HashSet<Point>();
				eyePoints.addAll(blankBlock.getPoints());
				if (blankBlock.isSingleBlockEye()) {
					singleBlockEyeSet.add(eyePoints);
				} else {
					multipleBlockEyeSet.add(eyePoints);
				}
			} else {
				Set<Point> eyePoints_singleTargetBlock = this
						.getEyePoints_singleTargetBlock(targetBlock, blankBlock);
				if (eyePoints_singleTargetBlock == null) {
					Set<Point> eyePoints_multiTargetBlock = this
							.getEyePoints_multipleTargetBlock(targetBlock,
									blankBlock);
					if (eyePoints_multiTargetBlock != null) {
						sharedBreathSet_multiBlock
								.add(eyePoints_multiTargetBlock);
					}
					continue;
				}
				sharedBreathSet_singleBlock.add(eyePoints_singleTargetBlock);
			}
		}
		eyeSet.addAll(singleBlockEyeSet);
		eyeSet.addAll(multipleBlockEyeSet);
		eyeSet.addAll(sharedBreathSet_singleBlock);
		eyeSet.addAll(sharedBreathSet_multiBlock);
		for (Set<Point> points : singleBlockEyeSet) {
			if (log.isEnabledFor(Level.WARN))
				log.warn("singleBlockEyeSet=" + points);
		}
		for (Set<Point> points : multipleBlockEyeSet) {
			if (log.isEnabledFor(Level.WARN))
				log.warn("singleBlockEyeSet=" + points);
		}
		for (Set<Point> points : sharedBreathSet_singleBlock) {
			if (log.isEnabledFor(Level.WARN))
				log.warn("sharedBreathSet_singleBlock=" + points);
		}
		for (Set<Point> points : sharedBreathSet_multiBlock) {
			if (log.isEnabledFor(Level.WARN))
				log.warn("sharedBreathSet_multiBlock=" + points);
		}

		// if(log.isEnabledFor(Level.WARN)) log.warn();
		if (eyeSet.size() == 0) {
			return false;
		} else if (eyeSet.size() == 1) {
			// short cut for single block complete big eye.
			int sizeOfEye = eyeSet.iterator().next().size();
			if (sizeOfEye <= 3) {
				return false;
			}
			if (singleBlockEyeSet.isEmpty() == false) {
				/**
				 * bug here: <br/>
				 * ##01,02,03 <br/>
				 * 01[_, W, _]01 <br/>
				 * 02[_, _, _]02 <br/>
				 * 03[_, _, _]03 <br/>
				 * ## 01,02,03 <br/>
				 * whoseTurn=Black <br/>
				 */
				if (sizeOfEye >= 7 && targetBlock.getNumberOfPoint() >= 4)
					return true;
			}
			Result live = this.isBigEyeLive_dynamic_internal(getBlock(target),
					eyeSet.iterator().next(), false);
			return live.isLive();
		} else {// if (eyeSet.size() >= 2) {

			// two complete eyes ensure live.
			if (singleBlockEyeSet.size() == 2) {
				targetBlock.setLive(true);
				for (Set<Point> points : eyeSet) {
					for (Point point : points) {
						BlankBlock blankBlock = this.getBlankBlock(point);
						if (blankBlock == null)
							continue;
						targetBlock.addLiveWith(blankBlock);
					}
				}
				return true;
			} else if (singleBlockEyeSet.size() == 1) {
				Result live = this.isBigEyeLive_dynamic_internal(
						getBlock(target), singleBlockEyeSet.iterator().next(),
						false);
				return live.isLive();
			} else {
				Set<Point> targets = new HashSet<Point>();
				for (Set<Point> temp : eyeSet) {
					targets.clear();
					targets.addAll(temp);
					Result live = this.isBigEyeLive_dynamic_internal(
							getBlock(target), targets, false);
					if (live.isLive()) {
						return true;
					}
				}

			}

			// TODO:
			Set<Point> targets = new HashSet<Point>();
			for (Set<Point> temp : eyeSet) {
				targets.addAll(temp);
			}
			Result live = this.isBigEyeLive_dynamic_internal(getBlock(target),
					targets, false);
			return live.isLive();
			// return true;

		}

	}

	/**
	 * 是否大眼活棋，简化算法（计算双活足够了）。必须大于等于七气(>=7）<br/>
	 * 不要求是成眼的棋子是同一块，但是每块棋需要大于等于三气(>=3)<br/>
	 * ensure only one big eye outside
	 * 
	 * @deprecated
	 * @param target
	 * @return
	 */
	public boolean isBigEyeLive_old(Point target) {

		Block targetBlock = getBlock(target);

		for (BlankBlock blankB : targetBlock.getBreathBlocks()) {
			if (blankB.isEyeBlock() == true) {
				if (isBigEyeFullySurround(blankB) == false)
					return false;
			}
		}

		for (BlankBlock blankB : targetBlock.getBreathBlocks()) {
			// 是否有大眼气块
			if (blankB.isEyeBlock() == false) {
				// TODO, whether the filled eye is complete.
				boolean complete = true;
				for (Block enemyBlock : blankB.getNeighborBlocks()) {
					if (enemyBlock == targetBlock)
						continue;
					for (Block eB : enemyBlock.getEnemyBlocks()) {
						if (eB == targetBlock)
							continue;
						complete = false;
						break;
					}
					if (complete == false)
						break;
					for (BlankBlock enemyBreath : enemyBlock.getBreathBlocks()) {
						if (enemyBreath == blankB)
							continue;
						for (Block blockT : enemyBreath.getNeighborBlocks()) {
							if (blockT == enemyBlock)
								continue;
							if (blockT == targetBlock)
								continue;

							// new enemyblock
							if (blockT.getColor() == enemyBlock.getColor()) {
								for (Block eB : blockT.getEnemyBlocks()) {
									if (eB == targetBlock)
										continue;
									complete = false;
									break;
								}
							} else {// target block is not complete.
								complete = false;
								break;
							}
						}
						if (complete == false)
							break;
					}
					if (complete == false)
						break;
				}

				boolean live = this.isBigEyeLive_dynamic(getBlock(target),
						blankB, false);
				if (live == true)
					return true;
				else
					continue;
			}
			boolean flaw = false;
			for (Block blockSurround : blankB.getNeighborBlocks()) {
				if (blockSurround.getBreaths() == 2) {

					for (Point breath : blockSurround.getBreathPoints()) {
						if (this.breathAfterPlay(breath,
								targetBlock.getEnemyColor()).size() >= 2) {
							flaw = true;
							break;
						}
						// return false;
					}

					// if (flaw == true) {
					// if (2 < minBreath)// 当成二气，代表有缺陷的眼位。
					// minBreath = 2;
					// } else {
					// if (3 < minBreath)// 当成三气，代表没有缺陷。
					// minBreath = 3;
					// }
				} else if (blockSurround.getBreaths() == 1)
					flaw = true;
				// minBreathBlock = blockSurr;
			}

			if (blankB.getNumberOfPoint() >= 7) {
				// 是否成眼的棋块没有缺陷。
				if (flaw == false) {
					return true;

				} else {// need further calculation.
					boolean live = this.isBigEyeLive_dynamic(getBlock(target),
							blankB, false);
					return live;
				}
			} else if (blankB.getNumberOfPoint() >= 4) {
				try {
					boolean live = this.isBigEyeLive_dynamic(getBlock(target),
							blankB, false);

					return live;
				} catch (RuntimeException e) {
					log.error("Error when check big eye: " + blankB.getPoints());
					log.error(this.getStateString());
					throw e;
				}
				/**
				 * 看是否有两个点能两眼活棋，且最少的气大于等于3。
				 */
				// int count = 0;
				// List<Point> candidates = this.getBrokenPoint(blankB);
				// for (Point breath : candidates) {
				// this.oneStepForward(breath, block.getColor());
				// if (this.isLive(target))
				// count++;
				// this.oneStepBackward();
				// if (count >= 2)
				// return true;
				// // if(count>=2) break;
				//
				// }
			}

			/**
			 * isLive中，曲三或者直三，return false。因为还没有活。 isDead中，曲三或者直三，return
			 * false。因为还没有死。
			 */

		}

		return false;
	}

	/**
	 * 气块周围的黑块或者白块是否有完好的连接,不会出现倒脱靴的情形. (气块周围的子块是否有效的连接起来了.和别处的代码(连接性处理的)稍有重复
	 * 
	 * @return
	 */
	public boolean isBlockConnected(BlankBlock blankBlock) {
		Set<Block> weakB = new HashSet<Block>();
		// 围成眼位的块处于被打吃状态, 如倒扑.
		for (Block block : blankBlock.getNeighborBlocks()) {
			if (block.getBreaths() == 1)
				return false;
			else if (block.getBreaths() == 2) {
				weakB.add(block);
			}
		}
		/**
		 * 围成眼位的块处于可以被断吃的状态, 如倒脱靴.<br/>
		 * 或者对方可以打吃,粘则减少眼位大小,且对方已经点入一子.可能是有力的手段.
		 */

		for (Block block : weakB) {
			GoBoardLadder cal = new GoBoardLadder(this.getBoardColorState());
			byte[][] zhengziCalculate = cal.jiSuanZhengZi(block
					.getBehalfPoint());
			if (zhengziCalculate[0][0] == 127) {
				return false;
			}
			// TODO: return false if ladder succeed. i.e. the block could be
			// eaten by enemy!
		}
		// 每个子块都有三气或以上.
		return true;

	}

	private static int searchCount = 0;

	public Result isBigEyeLive_dynamic_internal(Block target,
			Set<Point> eyePoints, boolean targetFirst) {
		return isBigEyeLive_dynamic_internal(target, null, eyePoints,
				targetFirst);
	}

	/**
	 * 小棋盘计算时，只考虑净活做为终局状态。
	 * 
	 * @param target
	 * @param blankBlock
	 *            maybe eye block or shared breath block
	 * @param targetFirst
	 * @return
	 */
	public Result isBigEyeLive_dynamic_internal(Block target,
			Set<Point> multiTarget, Set<Point> eyePoints, boolean targetFirst) {

		// if (Constant.INTERNAL_CHECK == true) {
		// assertTrue(eyeBlock.isCompleteBlockEye());
		// }

		// bug here. shared Set cause tricky bug!
		// Set<Point> eyePoints = eyeBlock.getPoints();

		byte[][] state = this.getMatrixState();
		BigEyeSearch search;
		if (multiTarget == null) {
			search = new BigEyeSearch(state, target.getBehalfPoint(),
					target.getColor(), eyePoints, targetFirst, false);
		} else {
			search = new BigEyeSearch(state, target.getBehalfPoint(),
					target.getColor(), multiTarget, eyePoints, targetFirst,
					false);
		}
		int score = search.globalSearch();

		int count = 0;
		// List<String> process = search.getSearchProcess();
		if (log.isEnabledFor(Level.WARN))
			log.warn("BigEyeSearch: " + (++searchCount));
		if (log.isEnabledFor(Level.WARN))
			log.warn(this.getBoardColorState().getStateString());
		if (log.isEnabledFor(Level.WARN))
			log.warn("target = " + target.getBehalfPoint());
		if (log.isEnabledFor(Level.WARN))
			log.warn("eyePoints = " + eyePoints);
		if (log.isEnabledFor(Level.WARN))
			log.warn("targetFirst = " + targetFirst);
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		if (log.isEnabledFor(Level.WARN))
			log.warn("count=" + search.getSearchProcess());
		for (String list : search.getSearchProcess()) {
			count++;
			if (log.isEnabledFor(Level.WARN))
				log.warn("count=" + count);
			if (log.isEnabledFor(Level.WARN))
				log.warn(list);
		}

		SearchNode root = search.getRoot();
		if (root != null) {
			TreeGoManual tm = this.getTreeGoManual();
			if (log.isEnabledFor(Level.WARN)) {
				log.warn(tm.getSGFBodyString());
				int countVar = tm.getVariant();
				log.warn("variant=" + countVar);
			}

		} else if (root == null) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(this.getBoardColorState().getStateString());
			if (log.isEnabledFor(Level.WARN))
				log.warn("target=" + target.getBehalfPoint());
			if (log.isEnabledFor(Level.WARN))
				log.warn("eyePoints=" + eyePoints);

		}

		Result res = new Result();
		res.setSurvive(score);
		res.setTree(root);
		if (root != null) {
			res.setPoint(root.getStep());
		}
		return res;
	}

	/**
	 * care about only real big eyes without any enemy point filled in.
	 * 
	 * @param target
	 * @param eyeBlock
	 * @param targetFirst
	 * @return
	 */
	public boolean isBigEyeLive_dynamic(Block target, BlankBlock eyeBlock,
			boolean targetFirst) {

		assert eyeBlock.isEyeBlock() == true;

		Set<Point> eyePoints = new HashSet<Point>();
		// for eye block, we allow several target block.
		eyePoints.addAll(eyeBlock.getPoints());

		int size = eyePoints.size();
		// only if it is pure big eyes without enemy point.
		if (size == eyeBlock.getNumberOfPoint()) {
			if (size < 3) {
				return false;// 先后手无关
			} else if (size >= 7 && eyeBlock.isCompleteBlockEye()) {
				// TODO:提子后的杀招。
				return true;// 先后手无关
			}
		}
		Result res = isBigEyeLive_dynamic_internal(target, eyePoints,
				targetFirst);
		int score = res.getSurvive();
		if (score == RelativeResult.ALREADY_LIVE) {
			target.addLiveWith(eyeBlock);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * with one target block and its shared/eye breath block, get all the points
	 * including enemy.
	 * 
	 * @param targetBlock
	 * @param blankBlock
	 * @return
	 */
	public Set<Point> getEyePoints_singleTargetBlock(Block targetBlock,
			BlankBlock blankBlock) {
		Set<Point> eyePoints = new HashSet<Point>();

		assert blankBlock.isEyeBlock() == false;
		if (blankBlock.getNeighborBlocks().size() > 1) {
			// shared breath block
			eyePoints.addAll(blankBlock.getPoints());
			for (Block block : blankBlock.getNeighborBlocks()) {
				if (block == targetBlock) {
					continue;
				}
				if (block.getColor() == targetBlock.getColor()) {
					// targetBlock is not complete/single.
					return null;
				}
				// other block may be weak and captured.
				eyePoints.addAll(block.getPoints());

				// other shared breath block
				for (BlankBlock blankB : block.getBreathBlocks()) {
					if (blankB == blankBlock)
						continue;
					// the blankB may connect to other enemyBlock
					for (Block newB : blankB.getNeighborBlocks()) {
						if (newB == targetBlock)
							continue;
						if (newB == block)
							continue;
						return null;
					}
					eyePoints.addAll(blankB.getPoints());
				}
			}
		}

		// prevent fake big eye.
		if (eyePoints.size() > targetBlock.getNumberOfPoint() * 2) {
			return null;
		} else {
			return eyePoints;
		}
	}

	/**
	 * @deprecated not sure of its correctness. in math.
	 * @param targetBlock
	 * @param blankBlock
	 * @return
	 */
	public Set<Point> getEyePoints_multipleTargetBlock(Block targetBlock,
			BlankBlock blankBlock) {
		Set<Point> eyePoints = new HashSet<Point>();
		Set<Block> targetBlocks = new HashSet<Block>();
		assert blankBlock.isEyeBlock() == false;
		if (blankBlock.getNeighborBlocks().size() > 1) {
			// shared breath block
			eyePoints.addAll(blankBlock.getPoints());
			for (Block block : blankBlock.getNeighborBlocks()) {
				if (block == targetBlock) {
					continue;
				}
				if (block.getColor() == targetBlock.getColor()) {
					// targetBlock is not complete/single.
					targetBlocks.add(block);
					continue;
				}
				// other block may be weak and captured.
				eyePoints.addAll(block.getPoints());

				// other shared breath block
				for (BlankBlock blankB : block.getBreathBlocks()) {
					if (blankB == blankBlock)
						continue;
					// the blankB may connect to other enemyBlock
					for (Block newB : blankB.getNeighborBlocks()) {
						if (newB == targetBlock)
							continue;
						if (newB == block)
							continue;
						targetBlocks.add(newB);
					}
					eyePoints.addAll(blankB.getPoints());
				}
			}
		}

		// prevent fake big eye.
		if (eyePoints.size() > targetBlock.getNumberOfPoint() * 2) {
			return null;
		} else {
			return eyePoints;
		}
	}

	/**
	 * if true, the big eye = two eyes, otherwise = one eye. special case, = 0
	 * eyes. <br/>
	 * dynamic searching instead of static judgment. complete big eyes has at
	 * least one eyes. if size>=3, we check whether it is possible to make to
	 * eyes
	 * 
	 * @param target
	 * @param eyeBlock
	 * @param targetFirst
	 *            目标方先走
	 * 
	 * @return
	 */
	public int isCompleteBigEyeLive_Loop(Block target, BlankBlock eyeBlock,
			boolean targetFirst) {
		int size = eyeBlock.getNumberOfPoint();

		if (size < 3) {
			return RelativeResult.ALREADY_DEAD;// 先后手无关
		} else if (size >= 7) {
			return RelativeResult.ALREADY_LIVE;
		}
		byte[][] state = this.getMatrixState();
		BigEyeSearch search = new BigEyeSearch(state, target.getBehalfPoint(),
				target.getColor(), eyeBlock, false, false);
		int score = search.globalSearch();
		if (score == RelativeResult.ALREADY_LIVE)
			return score;
		else if (score == RelativeResult.ALREADY_DEAD) {
			search = new BigEyeSearch(state, target.getBehalfPoint(),
					target.getColor(), false, true);
			score = search.globalSearch();
		}

		if (score == RelativeResult.ALREADY_LIVE) {
			// TODO:whether we can live by playing first
			return RelativeResult.LOOP_LIVE;// 劫活
		} else if (score == RelativeResult.ALREADY_DEAD) {
			// TODO: whether we can live by playing first.
			return score;
		}

		/**
		 * 看是否有两个点能两眼活棋，且最少的气大于等于3。
		 */
		// int count = 0;
		// List<Point> candidates = this.getBrokenPoint(blankB);
		// for (Point breath : candidates) {
		// this.oneStepForward(breath, block.getColor());
		// if (this.isLive(target))
		// count++;
		// this.oneStepBackward();
		// if (count >= 2)
		// return true;
		// // if(count>=2) break;
		//
		// }

		return RelativeResult.LOOP_LIVE;
	}

	/**
	 * when the block is eaten, if its enemy still does not have two eyes, it is
	 * still not really dead. that means the point is still in target block's
	 * control.<br/>
	 * if the enemy block become live after eating. then the block is really
	 * dead. that's the normal case.
	 * 
	 * @return
	 */
	public boolean isEatenBlockDead() {

		return false;
	}

	/**
	 * called if only isLive == false
	 * 现在的思路是isLive返回true说明明确的活棋。否则isDead判断是否明确的死棋。<br/>
	 * 剩下的局面继续计算。只要保证不明确的局面能继续计算即可。
	 * 
	 * @return
	 */
	public boolean isDead(Point target) {
		Block block = this.getBlock(target);
		Shape shape = block.getShape();

		for (Block enemyBlock : block.getEnemyBlocks()) {
			if (enemyBlock.getBreaths() == 1
					&& enemyBlock.getNumberOfPoint() > 1)
				return false;
		}

		if (this.isRemovable_static(block))
			return true;
		// int targetBreath = block.getBreaths();
		// int weakEnemys = 0;
		// /**
		// * 周围有弱子，需要继续计算到更明确的终点状态。
		// */
		// for (Block enemyBlock : block.getEnemyBlocks()) {
		// if (enemyBlock.getBreaths() <= targetBreath){
		// weakEnemys++;
		// }
		//
		//
		// }
		// if(weakEnemys>=2) return false;

		Set<Block> enemyBlocks = new HashSet<Block>();

		Block enemyBlock = null;
		for (BlankBlock breathBlock : block.getBreathBlocks()) {
			if (breathBlock.isEyeBlock()) {
				if (shape.include(breathBlock.getShape()) == false) {
					continue;// 外眼，极其特殊情况下才有，如中央拔花之型，盘上无敌子。
				} else {
					return breathBlock.getNumberOfPoint() <= 2;
					// return null;
				}
			}
			// 处理共享气块的情况。
			boolean inner = true;
			for (Block enemy : breathBlock.getNeighborBlocks()) {
				if (enemy == block)
					continue;
				if (shape.include(enemy.getShape())) {

				} else {
					inner = false;
					break;
				}
			}
			if (inner == true) {// 点眼双活
				// points.addAll(breathBlock.getPoints());
				// 将共享气块周边的敌子也计入。

			} else { // 普通相邻双活

			}

			for (Block enemy : breathBlock.getNeighborBlocks()) {
				if (enemy == block)
					continue;
				// points.addAll(enemy.getPoints());
				enemyBlock = enemy;// 点眼的块。
				enemyBlocks.add(enemy);
			}
		}

		if (enemyBlocks.size() != 1)
			return false;
		else {
			/**
			 * 仍有提子的机会。
			 */
			if (enemyBlock.getBreaths() == 1) {
				return false;
			} else if (block.getBreaths() == 2 && enemyBlock.getBreaths() == 2) {
				/**
				 * 标准的双活局面，islive可以正确判定。
				 */
				if (block.getBreathPoints()
						.equals(enemyBlock.getBreathPoints())) {
					if (log.isEnabledFor(Level.WARN))
						log.warn("isDead=true");
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}
			// Set<Point> external = new HashSet<Point>();
			// external.addAll(block.getBreathPoints());
			// external.removeAll(enemyBlock.getBreathPoints());
			// int sharedBreath = block.getBreaths()-external.size();
			// if (sharedBreath > 2) return false;
		}

	}

	/**
	 * if the block is not already static live, we may check whether the
	 * opponent can kill him by playing first. if not, then it is also alive.
	 * otherwise we can check whether the block can become live by playing
	 * first. Just an alias!
	 * 
	 * @param point
	 * @return
	 */
	public boolean isDynamicLive(Point point) {
		return this.isAlreadyLive_dynamic(point);
	}

	/**
	 * 判断一个不入气点是否为眼位。<br/>
	 * 这里不入气点指的是对方点入我方眼块的子,只有一气,不能在形式上的公气点落子
	 * 
	 * @param breath
	 *            气点
	 * @return
	 */
	public boolean isEye(Block block, Point breath) {
		int myColor = block.getColor();
		int enemyColor = block.getEnemyColor();

		for (Delta delta : Constant.ADJACENTS) {
			Point nb = breath.getNeighbour(delta);
			if (nb == null)
				continue;

			int color = this.getColor(nb);
			if (color != enemyColor)
				continue;

			// boolean buruqi = false;
			if (getBlock(nb).getBreathPoints().size() == 1) {
				/*
				 * 进一步检查是否为对方的不入气点。效果相当于眼。 TODO：没有考虑对方可能提子！
				 */
				for (Delta delta2 : Constant.ADJACENTS) {
					Point nb2 = breath.getNeighbour(delta2);
					if (nb2 == null)
						continue;
					int color2 = this.getColor(nb2);
					if (color2 == Constant.BLANK) {
						// buruqi = false;
						return false;
					} else if (color2 == myColor) {
						if (getBlock(nb2) != block) {
							return false;
						}
					}
				}

				/**
				 * 不入气点相邻的敌块必须只和本方目标块相邻. 否则其不入气的性质可能改变.
				 */
				for (Block enemyEnemy : getBlock(nb).getEnemyBlocks()) {
					if (enemyEnemy.equals(block) == false)
						return false;
				}
				// 需要检查邻块有没有全部二气或以上。
				// if(boardPoints[m][n].getBreaths()==)
				return true;
			} else {
				return false;
			}

		}
		return false;
	}

	/**
	 * 确定了是眼位(相邻的子全部属于己方)。(TODO: 在初始化时确保正确!不用临时算)<br/>
	 * 检查是否是打劫的情况。或者是打二还一的情况，总之对方可以提子回去。
	 */
	public boolean isEyeValid(Block targetBlock, BlankBlock blankBlock) {
		// int myColor = block.getColor();
		// if (targetBlock.getNumberOfPoint() * 2 <
		// blankBlock.getNumberOfPoint()) {
		// return false;
		// }

		if (this.isBigEyeFullySurround(blankBlock) == false) {
			return false;
		}

		for (Block neighborB : blankBlock.getNeighborBlocks()) {

			// for (Delta delta : Constant.ADJACENTS) {
			// Point nb = breath.getNeighbour(delta);
			// if (nb == null)
			// continue;
			//
			// if (this.getColor(nb) == Constant.BLANK)
			// continue;
			//
			// Block blockTemp = getBlock(nb);
			// 围成breath点所在的眼块的棋块中有气数为一的。
			if (neighborB.getBreaths() == 1) {
				// TODO: big eye space ensure at least one eye.

				if (log.isInfoEnabled()) {
					if (log.isEnabledFor(Level.WARN))
						log.warn("假眼 －－－ 打N还一（N="
								+ blankBlock.getNumberOfPoint() + ")");
				}
				if (blankBlock.getNumberOfPoint() >= 5)
					return true;
				else
					return false;
			}
		}
		return true;// 是眼
	}

	/**
	 * 处理特殊的活棋状态，比如两头蛇<br/>
	 * 眼位周围的块需要全部考虑进来。 以下的代码虽然精妙，但是太复杂了。以后再想办法利用。<br/>
	 * 但是，这些眼可能属于多个棋块。眼位属于某个棋块而该棋块只有一个眼位时，该眼位是假眼。<br/>
	 */
	public boolean isLive_Special(Point point, int myColor) {
		Block block = this.getBlock(point);
		Set<Block> blocks = new HashSet<Block>();
		Set<Block> newBlocks = new HashSet<Block>();
		Set<Block> allBlocks = new HashSet<Block>();
		blocks.add(block);
		allBlocks.add(block);
		Block temp;
		/**
		 * 每一步是否有新的块加入。
		 */
		boolean extended = true;
		/**
		 * 从目标块开始扩张到所有通过眼位连接起来的块。
		 */
		while (extended == true) {
			extended = false;
			for (Block block2 : blocks) {
				for (Point eye : getEyes(block2, myColor)) {

					for (Delta delta : Constant.ADJACENTS) {
						Point nb = eye.getNeighbour(delta);
						if (nb == null)
							continue;

						if (getColor(nb) == myColor) {
							temp = this.getBlock(nb);
							// block2.addEye(eye);
							if (allBlocks.contains(temp) == false
									&& newBlocks.contains(temp) == false) {
								if (log.isInfoEnabled()) {
									if (log.isEnabledFor(Level.WARN))
										log.warn("extend to the block " + temp);
								}
								newBlocks.add(temp);
								extended = true;
							}
						}
					}
				}
			}
			allBlocks.addAll(newBlocks);
			blocks.clear();
			blocks.addAll(newBlocks);
			newBlocks.clear();
		}
		;

		Set<Point> fakeEyes = new HashSet<Point>();
		boolean removed = false;
		/*
		 * blocks 中的棋块至少有一眼。
		 */
		blocks = allBlocks;
		if (Constant.INTERNAL_CHECK) {
			for (Block blockT : blocks) {
				log.warn("block " + blockT.getBehalfPoint() + " eyes="
						+ blockT.getEyes());
			}

		}

		while (blocks.isEmpty() == false) {
			removed = false;
			for (Iterator<Block> it = blocks.iterator(); it.hasNext();) {
				Block block2 = it.next();

				if (block2.numberOfEyes() == 0) {
					// if(log.isDebugEnabled()) log.debug("");
					if (log.isDebugEnabled())
						log.debug("The block has no eye:");
					if (log.isDebugEnabled())
						log.debug(block2);
					it.remove();
					continue;
				}
				/*
				 * 只有一个不入气点的棋块，其眼位也是假眼。
				 */
				if (block2.numberOfEyes() <= 1) {
					Point eyePoint = block2.getEyes().iterator().next();
					fakeEyes.add(eyePoint);
					it.remove();
					removed = true;

					// 虽然是假眼,仍然是可以连接起来的.(work around! actually one step
					// forward!)
					Set<Point> mergedEyes = new HashSet<Point>();
					for (Block blockNeighbor : this.getBlankBlock(eyePoint)
							.getNeighborBlocks()) {
						if (blockNeighbor == block2)
							continue;
						blockNeighbor.removeEye(eyePoint);
						mergedEyes.addAll(blockNeighbor.getEyes());
					}
					for (Block blockNeighbor : this.getBlankBlock(eyePoint)
							.getNeighborBlocks()) {
						if (blockNeighbor == block2)
							continue;
						blockNeighbor.addEyes(mergedEyes);
					}
				}
			}
			if (removed == false) {//
				break;
			}

			if (log.isEnabledFor(Level.WARN)) {
				log.warn("涉及的不入气点有: " + Arrays.toString(fakeEyes.toArray()));
			}
			for (Iterator<Block> it = blocks.iterator(); it.hasNext();) {
				Block block2 = it.next();
				/*
				 * 棋块中的假眼要删除掉。
				 */
				for (Iterator<Point> itp = block2.getEyes().iterator(); itp
						.hasNext();) {
					Point point2 = itp.next();
					if (fakeEyes.contains(point2))
						itp.remove();
				}
			}

			fakeEyes = new HashSet<Point>();
		}
		if (blocks.isEmpty() == false) {
			if (log.isInfoEnabled()) {
				for (Block temp2 : blocks) {
					if (log.isEnabledFor(Level.WARN))
						log.warn("Block " + temp2.getBehalfPoint() + "两眼为: "
								+ Arrays.toString(temp2.getEyes().toArray()));
				}
			}
		}
		if (blocks.isEmpty()) {
			if (log.isEnabledFor(Level.WARN))
				log.warn("live = " + false);
			return false;
		} else if (blocks.contains(block)) {
			if (log.isEnabledFor(Level.WARN))
				log.warn("live = " + true);
			return true;
		} else {
			/*
			 * TODO: 是否有接不归的问题。
			 */
			if (log.isEnabledFor(Level.WARN))
				log.warn("live = " + true);
			return true;
		}

	}

	/**
	 * 是否双活,无眼活棋情况的一种.长生劫的情况没有考虑.<br/>
	 * 首先是一块棋没有两只眼,但是对方杀不掉.识别出这种情况之后进一步识别双活的情况.<br/>
	 * (这里活棋是指已经活棋,对方杀不掉.)
	 * 
	 * @return
	 */
	public boolean isLiveWithoutTwoEye(Point point) {
		EyeResult eyeRes = this.getRealEyes(point, false);
		Set<Point> eyes = eyeRes.getRealEyes();

		Block targetBlock = getBlock(point);

		/**
		 * 计算目标块的内气，内气太多，还没有到双活的最终局面。<br/>
		 * 内气太少，不足以形成双活
		 */

		// int giftPoint = 0;// 送吃点。
		// for (Point breath : block.getAllBreathPoints()) {
		// int enemyColor = block.getEnemyColor();
		// NeighborState state2 = this.getNeighborState(breath, enemyColor);
		// if (state2.isGift())
		// giftPoint++;
		// }

		if (eyes.isEmpty()) {// 一个眼也没有。
			Set<Point> giftPoints = this.getGiftPoints(targetBlock, false);
			if (log.isInfoEnabled())
				log.info("送吃不入气点： " + giftPoints);
			if (giftPoints.size() >= 2) {
				log.info("没有眼，两个或以上送吃不入气点活棋。");
				return true;
			} else if (giftPoints.size() == 1) {
				Set<Point> halfEyes = eyeRes.getHalfEyes();
				for (Point halfEye : halfEyes) {
					if (halfEye.getDelta(giftPoints.iterator().next()).equals(
							Delta.DELTA_SHOULDER)) {
						return true;
					}
				}
			}
		} else if (eyes.size() == 1) {// 已经有一眼。
			Set<Point> giftPoints = this.getGiftPoints(targetBlock, true);
			if (log.isInfoEnabled())
				log.info("已经有一眼，送吃不入气点： " + giftPoints);
			if (giftPoints.size() >= 1) {
				log.info("一个或以上送吃不入气点活棋。");
				return true;
			} else {
				// TODO: whether we can eat weak block to have one eyes
				// win liberty race with net = 2
				// temporary solution, need dynamic search!
				for (Block enemyBlock : targetBlock.getEnemyBlocks()) {
					if (enemyBlock.getBreaths() + 2 <= targetBlock.getBreaths()) {
						boolean enemyWeak = true;
						for (Block enemyEnemy : enemyBlock.getEnemyBlocks()) {
							if (enemyBlock.getBreaths() + 2 <= enemyEnemy
									.getBreaths()) {
								continue;
							} else {
								enemyWeak = false;
								break;
							}
						}
						if (enemyWeak) {
							if (log.isEnabledFor(Level.WARN)) {
								log.warn("Weak block as eye"
										+ enemyBlock.getBehalfPoint());
							}
							return true;
						}
					}
				}
			}

		}

		// TODO: whether we can eat weak block to have one eyes
		// win liberty race with net = 2, at least 2, 1 is not enough.

		// temporary solution, need dynamic search!
		int weakBlocks = 0;
		for (Block enemyBlock : targetBlock.getEnemyBlocks()) {
			if (enemyBlock.getBreaths() + 2 <= targetBlock.getBreaths()) {
				boolean enemyWeak = true;
				for (Block enemyEnemy : enemyBlock.getEnemyBlocks()) {
					if (enemyBlock.getBreaths() + 2 <= enemyEnemy.getBreaths()) {
						continue;
					} else {
						enemyWeak = false;
						break;
					}
				}
				// TODO: not correct, need to check the state after clean up
				// stones. single block is strong enough.
				if (enemyWeak && enemyBlock.getEnemyBlocks().size() == 1) {
					log.warn("Weak block as eye" + enemyBlock.getBehalfPoint());

					weakBlocks++;

				}
			}
		}
		if (weakBlocks >= 2)
			return true;
		return false;
	}

	/**
	 * 一块棋两眼的活棋情况。是终局状态中最为常见的一种。<br/>
	 * 即使不是简单的两眼活棋,仍有可能是活棋的其他情况.<br/>
	 * 这里的两眼指的是小的两眼.<br/>
	 * it still has some dynamic element. but it is based on reasoning, not real
	 * searching.
	 * 
	 * @param point
	 *            any point in the target block<br/>
	 * @return true if one block has two real eyes; false otherwise, false does
	 *         not necessarily mean it can not survive.
	 */
	public boolean isOneBlockTwoEyeLive(Point point) {

		EyeResult eyeResult = getRealEyes(point, false);
		Set<Point> realEyes = eyeResult.getRealEyes();
		if (log.isInfoEnabled()) {
			if (realEyes.isEmpty()) {
				if (log.isEnabledFor(Level.WARN))
					log.warn("块" + point + "没有真眼");
			} else {
				if (log.isEnabledFor(Level.WARN))
					log.warn("块" + point + " 拥有的真眼为: " + realEyes);
			}
		}
		if (realEyes.size() >= 2) {
			return true;
		} else if (realEyes.size() == 1) {
			if (eyeResult.getHalfEyes().size() >= 2)
				// 两个后手眼必可以占到一个
				return true;
		} else {
			if (eyeResult.getHalfEyes().size() >= 4)
				// 四个后手眼必可以占到二个
				// TODO:对方一子两用破眼的情况（可能性）。
				return true;
		}
		return false;
	}

	/**
	 * 这里只考虑是否已经是真眼，不考虑是否可以补一手成为真眼。 <br/>
	 * 还是三值逻辑更合适 <br/>
	 * 不够精确,被getRealEyes取代.
	 * 
	 * @param eye
	 * @return
	 */
	public boolean isRealEye_old(Point eye, int enemyColor) {

		int count = 0;// 敌方点或者空白点
		if (eye.isCenterEye()) {// 棋盘中间的眼
			for (Delta delta : Constant.SHOULDERS) {
				Point nb = eye.getNeighbour(delta);
				if (nb == null)
					continue;
				if (this.getColor(nb) == ColorUtil.BLANK
						|| getColor(nb) == enemyColor) {
					count++;
				}
			}

			if (count > 1)
				return false;
			else
				return true;
		} else {// if (eye.getMinLine() == 1) //边上的眼。

			for (Delta delta : Constant.SHOULDERS) {
				Point nb = eye.getNeighbour(delta);
				if (nb == null)
					continue;
				if (this.getColor(nb) == ColorUtil.BLANK
						|| getColor(nb) == enemyColor) {
					return false;
				}
			}

			return true;
			// } else {//角上的眼。==1
			//
			// }
		}
	}

	/**
	 * whether we can finally remove the target block from the board even target
	 * move first.
	 * 
	 * @param block
	 * @return
	 */
	public boolean isRemovable_dynamic(Block block) {
		if (block.getBreaths() <= 1)
			return true;
		if (block.getBreaths() <= 2) {

		}
		return false;
	}

	public boolean isRemovable_static(Point target) {
		Block block = this.getBlock(target);
		return isRemovable_static(block);
	}

	/**
	 * whether the block is cleaned up from board anyway. we do not care whether
	 * the enemy block will become live after capture.
	 * 
	 * as a short cut, no dynamic search here.
	 * 
	 * @param block
	 * @return
	 */
	public boolean isRemovable_static(Block block) {
		if (block.getUniqueEyeBlock() != null
				&& block.getUniqueEyeBlock().getPoints().size() >= 3)
			return false;
		if (this.canIncreaseBreath_netly(block) == true)
			return false;
		if (block.noEnemy()) {
			return false;
		}
		int minBreath = block.getMinEnemyBreath();
		if (block.getBreaths() < minBreath) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isAlreadyDead_dynamic(Point point) {
		Set<Point> targets = new HashSet<Point>();
		targets.add(point);
		return isAlreadyDead_dynamic(targets, point);
	}

	/**
	 * 不论谁先走,目标块都会被提走.<br/>
	 * To some extend, there is no static dead at all, because we have to play
	 * some where to clean up the enemy dead stones. That usually require
	 * dynamic search, but in some straight forward case, we can draw the
	 * conclusion without resorting to dynamic search, it is so called "static"
	 * dead.
	 * 
	 * * hard to define when a block is already dead. <br/>
	 * it is easy to say current big eye is not big enough to be two eyes.<br/>
	 * but it is hard to draw conclusion that it it dead because it may has weak
	 * enemy block to co-live with.
	 * 
	 * @param point
	 * @return false for further searching.
	 */
	public boolean isAlreadyDead_dynamic(Set<Point> targets, Point point) {
		Block block = this.getBlock(point);
		// TODO: cannot increase breath locally (one step) does not mean
		// cannot increase breath in long run .
		boolean canIncreaseBreath = this.canIncreaseBreath_temporarily(block);
		if (canIncreaseBreath == true) {
			// TODO: dynamic search.
			return false;
		}
		// 气数为一,又不能长气,必然可以被提子.
		if (block.getBreaths() == 1) {
			return true;
		}
		// make eyes? Issue: cannot detect fake eyes.
		// int eyes = block.getNumberOfEyeBlock();
		// if (eyes == 1) {
		// if (block.getUniqueEyeBlock().getNumberOfPoint() >= 3) {
		// return false;
		// }
		// } else if (eyes == 2) {
		// return false;
		// }

		/**
		 * TODO: 可能有打劫的例外.
		 */
		for (Block enemyB : block.getEnemyBlocks()) {
			if (enemyB.getBreaths() <= block.getBreaths()) {
				return false;
			}
		}

		// already consider the block which is adjacent indirectly
		for (BlankBlock shared : block.getBreathBlocks()) {
			for (Block enemy : shared.getNeighborBlocks()) {
				if (enemy.getColor() == block.getColor()) {
					Block friend = enemy;
					// TODO take advantage of friend block with more breath.
					// if(friend.getBreaths()>=block.getBreaths())
					continue;
				}
				if (enemy.getBreaths() <= block.getBreaths()
						&& enemy.getNumberOfPoint() >= 4) {
					return false;
				}
			}
		}

		// for(block.getShape())

		/**
		 * text[0] = new String("[B, _, B, _]");<br/>
		 * text[1] = new String("[B, B, B, W]");<br/>
		 * text[2] = new String("[W, W, W, _]");<br/>
		 * text[3] = new String("[W, B, _, W]");<br/>
		 */
		if (block.getBreaths() <= 2) {
			/**
			 * String[] text = new String[6];<br/>
			 * text[0] = new String("[_, W, _, _, W, _]");<br/>
			 * text[1] = new String("[W, W, W, W, _, W]");<br/>
			 * text[2] = new String("[W, B, _, W, B, W]");<br/>
			 * text[3] = new String("[W, W, W, B, W, W]");<br/>
			 * text[4] = new String("[W, B, B, B, _, B]");<br/>
			 * text[5] = new String("[_, W, B, _, B, _]");<br/>
			 */
			return false;
			// return true;
		} else { // >=3
			boolean neighborLive = false;
			for (Block blockN : this.getBlock(point).getEnemyBlocks()) {
				if (blockN.isAlreadyLive())
					neighborLive = true;
			}
			if (neighborLive == true) {
				return this.isAlreadyDead_internal(point);
			}
		}
		if (targets != null)
			return !potentialEyeLive(targets);
		// include unknown case in false.
		return false;
	}

	/**
	 * @deprecated
	 * @param target
	 * @return
	 */
	public boolean potentialEyeLive(Point target) {
		EyeResult eyeRes = this.getRealEyes(target, false);
		int virtualEyes = eyeRes.getRealEyes().size();
		virtualEyes += eyeRes.getHalfEyes().size();
		if (eyeRes.getBigEyes().isEmpty() == false) {
			return true;
		}
		Block block = this.getBlock(target);

		// temp work around! inlcude == 1
		if (this.bigEyeFilledWithEnemy(block, false).getEyes() >= 1)
			return true;

		if (virtualEyes >= 2) {
			return true;
		}
		return false;
	}

	public boolean potentialEyeLive(Set<Point> targets) {
		EyeResult eyeRes = this.getRealEyes(targets, false);
		int virtualEyes = eyeRes.getRealEyes().size();
		virtualEyes += eyeRes.getHalfEyes().size();
		if (eyeRes.getBigEyes().isEmpty() == false) {
			return true;
		}
		for (Point target : targets) {
			Block block = this.getBlock(target);
			if (this.bigEyeFilledWithEnemy(block, false).getEyes() > 1)
				return true;
		}
		if (virtualEyes >= 2) {
			return true;
		}
		return false;
	}

	/**
	 * It is actually hard to define and check when the target block or blocks
	 * is dead. At least it is harder then the definition of liveness.<br/>
	 * 
	 * @param targets
	 * @return
	 */
	public boolean potentialToLive(Set<Point> targets) {
		EyeResult eyeRes = this.getRealEyes(targets, false);
		int virtualEyes = eyeRes.getRealEyes().size();
		virtualEyes += eyeRes.getHalfEyes().size();
		if (eyeRes.getBigEyes().isEmpty() == false) {
			return true;
		}
		for (Point target : targets) {
			Block block = this.getBlock(target);
			if (this.bigEyeFilledWithEnemy(block, false).getEyes() > 1)
				return true;
		}
		if (virtualEyes >= 2) {
			return true;
		}

		// no two eyes, whether there is weaker enemy
		Set<Point> totalBreath = new HashSet<Point>();
		for (Point target : targets) {
			Block block = this.getBlock(target);
			totalBreath.addAll(block.getBreathPoints());
		}
		int total = totalBreath.size();
		for (Point target : targets) {
			Block block = this.getBlock(target);
			/**
			 * it is too weak. <br/>
			 * TODO: check whether the enemy is really weak, not like the race
			 * breath, in the world of live and dead, a block with only two
			 * breath could be extremely strong if it is already live.<br/>
			 * Globally we should identify the live block first, so that when
			 * judge the liveness of neighbor block, we can draw conclusion
			 * easily because the enemy block is strong, we can know it it dead
			 * if cannot reach two eyes.<br/>
			 * another complexity comes from the fact that the live block may
			 * means enemy has some benefit of controlling some point without
			 * cost, like capturing.
			 */
			if (block.isAlreadyLive() == false
					&& block.getMinBreathEnemyBlock().getBreaths() <= total) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 静态地判断为活棋（即已经活棋）。<br/>
	 * 动态性需要在搜索中走棋。然后判断出活棋。即为可以活棋。
	 * 
	 * @param point
	 * @return
	 */
	public boolean isStaticLive(Point point) {
		boolean live = isOneBlockTwoEyeLive(point);
		if (live == true) {
			if (log.isEnabledFor(Level.WARN)) {
				log.warn("简单的两真眼活棋！" + this.getBlock(point).getBehalfPoint());
			}
			return true;
		}
		// if(Block block:getBlock(point).getBreathBlocks()){
		// BigEyeSearch search = new BigEyeSearch(state, point,
		// eyeBlock, false, false);
		// int score = search.globalSearch();
		// if (score == BigEyeSearch.ALREADY_LIVE)
		// return true;
		// else
		// return false;}
		return false;
	}

	/**
	 * // mark connected block as live. shoulders only.
	 * 
	 * @param block
	 */
	public void liveBlockConnect(Block blockO) {
		Set<Block> level = new HashSet<Block>();
		Set<Block> nextLevel = new HashSet<Block>();
		level.add(blockO);
		do {
			nextLevel.clear();
			for (Block block : level) {
				block.setLevelExtended(true);// avoid duplicate
			}
			for (Block block : level) {
				this.connectBlock(block, Constant.SHOULDERS);
				if (block.getGroup() == null) { // stand alone block.
					continue;
				}

				for (Block temp : block.getGroup().getBlocks()) {
					if (temp.isLevelExtended())
						continue;
					if (temp.isAlreadyLive() == false) {
						log.warn("Block" + temp.getBehalfPoint()
								+ " become live because of connection to "
								+ block.getBehalfPoint());
						temp.setLive(true);
						if (temp.isLiveDeadMarked() == false) {
							temp.setDead(this.isDead(block.getBehalfPoint()));
							temp.setLiveDeadMarked(true);
						}
						nextLevel.add(temp);
					}
				}
			}
			level.clear();
			level.addAll(nextLevel);
		} while (level.isEmpty() == false);
	}

	/**
	 * Not live White Big block[6,16] Not live White Big block[9,13] Not live
	 * White Small block[6,19] Not live White Small block[10,7] Not live White
	 * Small block[2,6] Not live White Small block[8,18] Not live White Small
	 * block[19,16] Not live White Small block[3,16] Not live White Small
	 * block[6,14] Not live White Small block[9,19] Not live White Small
	 * block[19,14] Not live White Small block[2,18] Not live White Small
	 * block[14,15]
	 */
	public void markLiveDead(List<Block> blocks) {
		for (Block block : blocks) {
			block.setDead(this.isDead(block.getBehalfPoint()));
			block.setLive(this.isAlreadyLive_dynamic(block.getBehalfPoint()));
		}

	}

	/**
	 * decide the live_dead state recursively. first decide the live big block,
	 * then check whether weaker block is connected to it since the cut point is
	 * not valid.
	 */
	public void markLiveDead2(List<Block> blocks) {

		for (Block block : blocks) {
			if (block.isLiveDeadMarked())
				continue;

			block.setLive(this.isAlreadyLive_dynamic(block.getBehalfPoint()));

			if (block.isAlreadyLive() == false) {
				block.setDead(this.isDead(block.getBehalfPoint()));
				continue;
			} else {
				block.setLiveDeadMarked(true);
			}
			this.liveBlockConnect(block);

		}

		for (Block block : blocks) {
			if (block.isAlreadyLive() == false) {
				// weak block try to connect to Live block
				this.connectBlock(block, Constant.ONE_DISTANCE);
				// if()
				if (block.getLiveFriend_canConn() != null
						&& block.getLiveFriend_canConn().size() >= 2) {
					block.setLive(true);
					block.setLiveDeadMarked(true);
					log.warn("Block " + block.getBehalfPoint()
							+ " is live because it can connec to");
					for (Block temp : block.getLiveFriend_canConn()) {
						log.warn("Live block " + temp.getBehalfPoint());
					}
				}
			}
		}
	}

	/**
	 * 在指定距离之内没有敌方之子。
	 * 
	 * @param point2
	 * @param myColor
	 * @param level
	 * @return
	 */
	public boolean no_enemy_nearby(Group group, Point point2, int level) {
		int myColor = group.getColor();
		int levelR = level;
		Set<Point> lastLevel = new HashSet<Point>();
		Set<Point> tempLevel;
		Set<Point> nextLevel = new HashSet<Point>();
		lastLevel.add(point2);
		while (levelR-- >= 1) {
			int enemy = 0;
			for (Point point : lastLevel) {
				for (Delta delta : Constant.ADJACENTS) {
					Point temp = point.getNeighbour(delta);

					if (temp == null)
						continue;
					if (this.isBlank(temp)) {
						nextLevel.add(temp);
					} else if (this.getColor(temp) == myColor) {

					} else {
						Block tempB = this.getBlock(temp);
						if (tempB.isAlreadyLive()
								|| tempB.getBreaths() > group.getBreaths()) {
							enemy++;
							return false;
						}
					}

				}

			}

			lastLevel.clear();
			tempLevel = lastLevel;
			lastLevel = nextLevel;
			nextLevel = tempLevel;

		}

		return true;
	}

	public void showLiveDead() {
		List<Block> list = new ArrayList<Block>();
		list.addAll(this.getBlackBlocksOnTheFly());
		Collections.sort(list, new BlockSizeComparator());
		for (Block block : list) {
			if (block.isAlreadyLive() == true) {
				if (block.isAlreadyDead() == true) {// already live, also can
													// capture
					if (log.isEnabledFor(Level.WARN)) {
						log.warn("already live, also can capture");
						log.warn("problematic block: " + block.getBehalfPoint());
					}
					return;
				} else {
					// already live, cannot capture
				}
			} else {
				if (block.isAlreadyDead() == true) {// not live yet, maybe
													// captured

				} else {// not live yet, also cannot be captured.
					if (log.isEnabledFor(Level.WARN)) {
						log.warn("not live yet, also cannot be cpatured.");
						log.warn("problematic block: " + block.getBehalfPoint());
					}
					return;
				}

			}
		}

	}

	public void showLiveDead_2() {

		List<Block> blocks = new ArrayList<Block>();
		blocks.addAll(this.getWhiteBlocks());

		for (Block block : blocks) {
			block.clearLiveDead();
			if (block.getNumberOfPoint() <= 7)
				continue;// low priority
			int count = 0;
			for (BlankBlock temp : block.getBreathBlocks()) {
				if (temp.isEyeBlock())
					count++;
			}
			// more eyes is better for live.
			block.setPriority(count * 10 + block.getNumberOfPoint());
		}
		Collections.sort(blocks, new GeneralBlockComparator());
		for (Block block : blocks) {
			if (block.getNumberOfPoint() >= 7) {
				log.warn(ColorUtil.getColorText(block.getColor())
						+ " Big block" + block.getBehalfPoint());
			} else {
				log.warn(ColorUtil.getColorText(block.getColor())
						+ " Small block" + block.getBehalfPoint());
			}
			// log.warn(block.getBehalfPoint());
		}

		this.markLiveDead2(blocks);
		for (Block block : blocks) {
			if (block.isAlreadyLive() == true)
				continue;
			if (block.getNumberOfPoint() >= 7) {
				log.warn("Not live " + ColorUtil.getColorText(block.getColor())
						+ " Big block" + block.getBehalfPoint());
			} else {
				log.warn("Not live " + ColorUtil.getColorText(block.getColor())
						+ " Small block" + block.getBehalfPoint());
			}
		}

		for (Block block : blocks) {
			if (block.isAlreadyLive() == true) {
				if (block.isAlreadyDead() == true) {// already live, also can
													// capture
					System.out
							.println("already live, also can capture, special");
					log.warn("problematic block: " + block.getBehalfPoint());
					continue; // tolerable
					// return;
				} else {
					// already live, cannot capture
				}
			} else {
				if (block.isAlreadyDead() == true) {// not live yet, maybe
													// captured
					// such as three space in line.
				} else {// not live yet, also cannot be captured.
					log.warn("not live yet, also cannot be cpatured.");
					log.warn("problematic block: " + block.getBehalfPoint());
					if (block.getNumberOfPoint() > 7)
						return;
					else
						log.warn("It is OK for small block");
				}

			}
		}

	}

	/**
	 * 判断一个气点是否为真正的眼位。(1.单子眼位,2.围成眼的点属于同块.3.)<br/>
	 * 不可能为假眼，因为气点的周围属于同一个棋块。<br/>
	 * 不入气点暂不考虑。<br/>
	 * Block中的数据结构如果正确维护的话,这里的算法可以简化.当前的实现对block的数据依赖很少.
	 * 
	 * @deprecated
	 * @param targetBlock
	 *            目标棋块
	 * @param breathPoint
	 *            是目标棋块的一个气点
	 * @param myColor
	 * @return
	 */
	private boolean isSameBlockEye(Block targetBlock, Point breathPoint,
			int myColor) {
		int enemyColor = ColorUtil.enemyColor(myColor);
		for (Delta delta : Constant.ADJACENTS) {
			Point nb = breathPoint.getNeighbour(delta);
			if (nb == null)
				continue;

			int color = this.getColor(nb);
			if (color == enemyColor) {// 公气
				return false;
			} else if (color == myColor) {
				/*
				 * 没有连成一块。 not same block
				 */

				if (this.getBlock(nb) != targetBlock)
					return false;
			} else if (color == ColorUtil.BLANK) {
				// 不是单子眼。眼位为二子或以上的气块。这里不予识别和考虑。
				return false;
			}
		}
		return true;// 是真眼
	}

	/**
	 * isAlreadyLive. further distinguish, whether there is ko threat, or put it
	 * in another way, whether it is still alive if enemy play two step
	 * continuously.
	 */

}
