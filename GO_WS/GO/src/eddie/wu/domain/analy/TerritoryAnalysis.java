package eddie.wu.domain.analy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BasicBlock;
import eddie.wu.domain.BlankBlock;
import eddie.wu.domain.Block;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoardForward;
import eddie.wu.domain.Point;
import eddie.wu.domain.comp.BlockBreathComparatorDesc;
import eddie.wu.search.global.GoBoardSearch;
import eddie.wu.search.small.SmallBoardGlobalSearch;

/**
 * 
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class TerritoryAnalysis extends SurviveAnalysis {
	private static final Logger log = Logger.getLogger(TerritoryAnalysis.class);

	public TerritoryAnalysis(byte[][] state) {
		super(state);
	}

	public TerritoryAnalysis(byte[][] state, int whoseTurn) {
		super(state, whoseTurn);
	}

	public TerritoryAnalysis(int boardSize) {
		super(boardSize);
	}

	public TerritoryAnalysis(BoardColorState colorState) {
		super(colorState);
	}

	/**
	 * simple implementation when one side has no move candidate; it is major
	 * used in small board size global search.<br/>
	 * 
	 * 当一方已经无处可以落子时. 判定胜负.(但是仍有可能有些死子没有提掉)<br/>
	 * 目前用在小棋盘的终局状态数子.<br/>
	 * 
	 * 解决的第一个问题:棋块中的子被重复计数<br/>
	 * 
	 * @deprecated originally used when we found no candidate for some state.
	 * @return
	 */
	public FinalResult finalResult_noCandidate() {
		Set<Block> blocks = new HashSet<Block>();
		Set<BlankBlock> blankBlocks = new HashSet<BlankBlock>();

		int black = 0;
		int white = 0;
		int shared = 0;
		FinalResult res = new FinalResult();

		for (int row = 1; row <= boardSize; row++) {
			for (int column = 1; column <= boardSize; column++) {
				int color = this.getColor(row, column);
				if (color == Constant.BLANK)
					continue;
				Block block = this.getBlock(row, column);
				if (block.isCalculated())
					continue;
				else {
					block.setCalculated(true);
				}

				if (this.isAlreadyLive_dynamic(block.getBehalfPoint())) {// 确定的活棋块
					if (color == Constant.BLACK) {
						black += block.getNumberOfPoint();
						res.getBlackPoints().addAll(block.getPoints());

					} else if (color == Constant.WHITE) {
						white += block.getNumberOfPoint();
						res.getWhitePoints().addAll(block.getPoints());
					}
					for (Iterator<BlankBlock> iter = block.getBreathBlocks()
							.iterator(); iter.hasNext();) {
						BlankBlock blankBlock = iter.next();
						if (blankBlock.isCalculated())
							continue;
						else {
							blankBlock.setCalculated(true);
						}

						if (blankBlock.getPoints().isEmpty()) {
							System.err
									.println("empty point list in blank block");
							iter.remove();
							continue;
						}
						if (blankBlock.isEyeBlock()) {// 棋块周围的眼块计入该棋块
							if (blankBlock.isBlackEye()) {
								black += blankBlock.getNumberOfPoint();
								res.getBlackPoints().addAll(block.getPoints());
							} else {
								white += blankBlock.getNumberOfPoint();
								res.getWhitePoints().addAll(block.getPoints());
							}
						} else { // 共享气块
							shared += blankBlock.getNumberOfPoint();
							res.getSharedPoints().addAll(block.getPoints());
						}

					}
				} else {// 死棋块 (计算过程中,isLive=false未必是死棋,终局时就可以如此判断.
					// TODO 死棋块的周围共享气块可能需要变成眼块
					// 调用该方法之前,需要根据死活情况重新计算是否为眼块
					if (color == Constant.BLACK) {
						white += block.getNumberOfPoint();
						res.getWhitePoints().addAll(block.getPoints());
					} else if (color == Constant.WHITE) {
						black += block.getNumberOfPoint();
						res.getBlackPoints().addAll(block.getPoints());
					}
				}
			}
		}

		// clean up the flag after counting
		for (Block block : blocks) {
			block.setCalculated(false);
		}
		for (BlankBlock blankBlock : blankBlocks) {
			blankBlock.setCalculated(false);
		}

		res.setBlack(black);
		res.setWhite(white);
		res.setShared(shared);
		return res;
	}

	/**
	 * 初步的启发式来决定点的归属。避免不必要的提子。 (没有完成)
	 * 
	 * @return
	 */
	public FinalResult finalResult_heuristic() {
		for (int row = 1; row <= boardSize; row++) {
			for (int column = 1; column <= boardSize; column++) {
				Block block = this.getBlock(row, column);
				if (block.getEnemyBlocks().isEmpty() == false) {
					if (block.getEnemyBlocks().size() >= 2) {
						Block eye = block.getEnemyBlocks().iterator().next();
						// EyeKnowledge
					}
				}
			}
		}
		int black = 0;
		int white = 0;
		int shared = 0;
		FinalResult res = new FinalResult(black, white, shared);
		return res;
	}

	/**
	 * Still buggy, because the already knows state is reused without
	 * considering the history.
	 * 
	 * @param state
	 * @return
	 */
	public static boolean isFinalState_dynamic(byte[][] state) {

		int boardSize = state.length - 2;
		int maxExp = boardSize * boardSize;
		int minExp = 0 - maxExp;
		GoBoardSearch goS1 = new SmallBoardGlobalSearch(state, Constant.BLACK,
				maxExp, minExp);
		int score1 = goS1.globalSearch();

		GoBoardSearch goS2 = new SmallBoardGlobalSearch(state, Constant.WHITE,
				maxExp, minExp);
		int score2 = goS2.globalSearch();

		if (score1 == score2) {
			if (score1 != Constant.UNKOWN) {
				if (log.isEnabledFor(Level.WARN))
					log.warn("Score1=" + score1);
				return true;
			}
		}
		return false;
	}

	public static final int DOUBLE_PASS = 1;
	public static final int DEAD_CLEANED_UP = 2;
	public static final int DEAD_EXIST = 3;

	/**
	 * statically analyze whether the state is final (return value >0). and the
	 * type details: <br/>
	 * 0. The final really means that there is nothing left to fight against.
	 * put it in another way, no matter whose turn it is for current status, the
	 * result is same. <br/>
	 * 1. all the stones in the board is pure live with two or more single eyes,
	 * co-live is not included. that means all the dead stone is already cleaned
	 * up.<br/>
	 * 单官已经收完<br/>
	 * there is no candidate for each side so long as they will not fill their
	 * own eyes.<br/>
	 * 1.1 big eyes is allowed.对方可以送吃拖延时间. 2. co-live is included<br/>
	 * 3. dead block is not cleaned up yet.<br/>
	 * 4.
	 */
	public int finalStateType() {
		if (this.areBothPass()) {
			return DOUBLE_PASS;
		} else if (this.isFinalState_deadCleanedUp()) {
			return DEAD_CLEANED_UP;
		} else if (this.isFinalState_deadExist()) {
			/**
			 * fixed a bug when dealing with<br/>
			 * text[0] = new String("[_, _, _]");<br/>
			 * text[1] = new String("[B, B, W]");<br/>
			 * text[2] = new String("[_, W, _]");<br/>
			 * should match with finalResult();
			 */
			return DEAD_EXIST;
		} else {// not mature:
		}
		return -1;
	}

	/**
	 * 最纯粹的终局场面 <br/>
	 * also static judgment.
	 * 
	 * @return
	 */
	public boolean isFinalState_deadCleanedUp() {
		for (BlankBlock blankB : this.getBlankBlocksOnTheFly()) {
			// 没有双活，终局没有公气。
			if (blankB.isEyeBlock() == false)
				return false;
			// 眼位都是单眼，没有入子余地。
			if (blankB.getNumberOfPoint() != 1)
				return false;
		}

		for (Block block : this.getBlackWhiteBlocks()) {
			// if (this.isAlreadyLive_dynamic(block.getBehalfPoint()) == false)
			if (this.isStaticLive(block.getBehalfPoint()) == false)
				return false;
		}
		return true;
	}

	/**
	 * 和1的区别在于，允许双活，非双活处要求同前<br/>
	 * 死子已经提走，但是因为允许双活的关系。有额外的落子点（自填眼之外有送吃点） <br/>
	 * 双活有多种，有的往前看一步即可知双活，送吃后对方大眼活棋。有的需要全局搜索多步。
	 * 
	 * @return
	 */
	public boolean isFinalState_static2() {
		// TODO not done yet.
		for (Block block : this.getBlackWhiteBlocks()) {
			if (this.isAlreadyLive_dynamic(block.getBehalfPoint()) == false)
				return false;
		}
		Set<BlankBlock> blankBlocks = this.getBlankBlocksOnTheFly();
		for (BlankBlock blankB : blankBlocks) {
			// 没有双活，终局没有公气。
			if (blankB.isEyeBlock() == false)
				return false;
			// 眼位都是单眼，没有入子余地。
			if (blankB.getNumberOfPoint() != 1)
				return false;
		}

		return true;
		// return false;
	}

	/**
	 * Performance optimization: to filter some obvious not finalized state.
	 * 
	 * @return
	 */
	public boolean isPotentialFinal() {
		List<Block> blocks = new ArrayList<Block>();
		blocks.addAll(this.getBlackWhiteBlocks());
		/**
		 * 原始气块,尚未落子.
		 */
		if (blocks.isEmpty())
			return false;

		int count = 0;
		// 有被打吃的情况，不视为终局。
		for (Block block : blocks) {
			if (block.getBreaths() < 2)
				return false;
			count += block.getNumberOfPoint();
		}
		// 棋盘不能太空
		if (count * 2 < boardSize * boardSize)
			return false;

		return true;
	}

	public boolean isFinalState(int type) {
		switch (type) {
		case DEAD_CLEANED_UP:
			return this.isFinalState_deadCleanedUp();// break;
		case DEAD_EXIST:
			return this.isFinalState_deadExist();// break;
		}
		return false;
	}

	/**
	 * 死子存在的情况下.从初步的finalState变化到死子全部清除的finalState,双方的走法都不会导致<br/>
	 * 活棋被打吃;但是存有双活的情况则是例外.中间涉及打吃的非final状态.且导致的finalState,<br/>
	 * 其结果可能不同.<br/>
	 * 关键是确定双活的变化过程是否由同型再现主导,或者说.打破双活之后提子是否确定终局.<br/>
	 * 2*2小棋盘的例子中,因为是同型再现主导,因此状态与结果的对应关系不唯一,还要依赖于导致局面的历史.
	 * 
	 * @return
	 * @deprecated
	 */
	public boolean isFinalState_deadExist_old() {

		try {

			String message = "+++++++++++final result+++++++++";
			this.check_internal(message);
			check(message);

		} catch (RuntimeException e) {
			throw e;
		}

		if (this.areBothPass() == true)
			return true;

		List<Block> blocks = new ArrayList<Block>();
		blocks.addAll(this.getBlackWhiteBlocks());
		/**
		 * 原始气块,尚未落子.
		 */
		if (blocks.isEmpty())
			return false;

		// 有大块被打吃的情况，(且其邻块又不是活棋)不可能终局或者可能性很低。
		for (Block block : blocks) {
			if (block.getBreaths() < 2 && block.getNumberOfPoint() >= 4)
				// Block is changed during isAlreadyLive_dynamic, hence
				// exception.
				for (Block temp : block.getEnemyBlocks()) {
					// if (this.isAlreadyLive_dynamic(temp.getBehalfPoint()) ==
					// false)
					if (this.isStaticLive(temp.getBehalfPoint()) == false)
						return false;
				}

		}
		/**
		 * 01[W, _, B]01<br/>
		 * 02[B, B, B]02<br/>
		 * 03[_, _, B]03<br/>
		 */
		for (Block block : blocks) {
			block.setUnknown(false);
			block.setLive(false);
			block.setDead(false);
		}

		Collections.sort(blocks, new BlockBreathComparatorDesc());
		for (Block block : blocks) {
			// block.setLive(this.isAlreadyLive_dynamic(block.getBehalfPoint()));
			block.setLive(this.isStaticLive(block.getBehalfPoint()));
			if (block.isUnknown())
				return false;
		}

		for (Block block : blocks) {
			if (block.isAlreadyLive())
				continue;

			boolean allEnemyLive = true;
			if (block.getEnemyBlocks().isEmpty()) {
				allEnemyLive = false;
			} else {
				for (Block enemyBlock : block.getEnemyBlocks()) {
					if (enemyBlock.isAlreadyLive() == false) {
						allEnemyLive = false;
						break;
					}
				}
			}
			// 可能有问题,也许仍有活棋手段,处于可杀可活状态.
			if (allEnemyLive == true) {
				block.setDead(true);
			}
		}

		boolean unknown = false;
		for (Block block : blocks) {
			if (block.isAlreadyLive() == false
					&& block.isAlreadyDead() == false) {
				unknown = true;
				break;
			} else {
				log.warn("Block" + block.getBehalfPoint() + "? "
						+ block.isAlreadyLive());
			}
		}

		if (unknown == true) {
			return false;
		} else {

			/**
			 * side effect, remove the dead block.
			 */
			return true;
		}

		// return false;
	}

	/**
	 * _static one leave the variant to the search engine.<br/>
	 * cannot be dynamic one, need to clean up the dead internally at least<br/>
	 * co-live is supported because it just means two block are live and they
	 * has no two eyes.
	 * 
	 * @return
	 */
	public boolean isFinalState_deadExist() {

		try {
			String message = "+++++++++++final result+++++++++";
			this.check_internal(message);
			check(message);

		} catch (RuntimeException e) {
			throw e;
		}

		if (this.areBothPass() == true)
			return true;

		List<Block> blocks = new ArrayList<Block>();
		blocks.addAll(this.getBlackWhiteBlocks());
		/**
		 * 原始气块,尚未落子.
		 */
		if (blocks.isEmpty()) {
			return false;
		}
		/**
		 * capturing each other! <br/>
		 * ##01,02,03,04 <br/>
		 * 01[W, W, W, W]01 <br/>
		 * 02[W, _, W, W]02 <br/>
		 * 03[W, W, W, W]03 <br/>
		 * 04[W, W, W, W]04 <br/>
		 * ###01,02,03,04 <br/>
		 * whoseTurn=Black
		 */
		try {
			for (Block block : blocks) {
				if (block.getBreaths() == 1) {
					Block minBreathEnemyBlock = block.getMinBreathEnemyBlock();
					// no enemy block, but still could have only one breath.
					if (minBreathEnemyBlock == null) {
						if (block.getNumberOfPoint() >= 6)
							return false;
					} else if (minBreathEnemyBlock.getBreaths() == 1) {
						if (block.getNumberOfPoint()
								+ minBreathEnemyBlock.getNumberOfPoint() >= 6)
							return false;
					}
				}
			}
		} catch (RuntimeException e) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(this.getBoardColorState().getStateString());
			throw e;
		}
		// 有大块被打吃的情况，(且其邻块又不是活棋)不可能终局或者可能性很低。
		// for (Block block : blocks) {
		// if (block.getBreaths() < 2 && block.getNumberOfPoint() >= 4)
		// //Block is changed during isAlreadyLive_dynamic, hence exception.
		// for (Block temp : block.getEnemyBlocks()) {
		// //if (this.isAlreadyLive_dynamic(temp.getBehalfPoint()) == false)
		// if (this.isStaticLive(temp.getBehalfPoint()) == false)
		// return false;
		// }
		//
		// }
		/**
		 * 01[W, _, B]01<br/>
		 * 02[B, B, B]02<br/>
		 * 03[_, _, B]03<br/>
		 */
		for (Block block : blocks) {
			block.setUnknown(false);
			block.setLive(false);
			block.setDead(false);
		}

		Collections.sort(blocks, new BlockBreathComparatorDesc());
		for (Block block : blocks) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(block.getBehalfPoint());
		}
		for (Block block : blocks) {
			BoardColorState copy = this.getBoardColorState();
			if (Constant.DEBUG_CGCL) {
				this.debugIngernalDataStructure_beforeForward(copy);
			}

			/**
			 * avoid take care of dead small block. but need to consider
			 * exceptional case where several small blocks with one big eye
			 * block,
			 */
			if (block.getNumberOfPoint() < 4) {
				int eyes = block.getNumberOfEyeBlock();
				if (eyes == 0) {
					continue;
				}
			}

			// if (boardSize < 5) {
			if (boardSize <= 3) {
				block.setLive(this.isStaticLive(block.getBehalfPoint()));
			} else {
				// TODO:may change board data structure to be inconsistent.
				block.setLive(this.isAlreadyLive_dynamic(block.getBehalfPoint()));

			}
			if (Constant.DEBUG_CGCL) {
				this.debugIngernalDataStructure(copy, this.getBoardColorState());
			}

			if (block.isUnknown())
				return false;
		}

		for (Block block : blocks) {
			if (block.isAlreadyLive() == true)
				continue;
			if (boardSize < 5) {
				block.setDead(this.isRemovable_static(block));
			} else {
				block.setDead(this.isAlreadyDead_dynamic(block.getBehalfPoint()));
			}
			if (block.isUnknown())
				return false;
		}

		// for (Block block : blocks) {
		// if (block.isLive())
		// continue;
		//
		// boolean allEnemyLive = true;
		// if (block.getEnemyBlocks().isEmpty()) {
		// allEnemyLive = false;
		// } else {
		// for (Block enemyBlock : block.getEnemyBlocks()) {
		// if (enemyBlock.isLive() == false) {
		// allEnemyLive = false;
		// break;
		// }
		// }
		// }
		// // 可能有问题,也许仍有活棋手段,处于可杀可活状态.
		// if (allEnemyLive == true) {
		// block.setDead(true);
		// }
		// }
		//
		if (log.isInfoEnabled()) {
			if (log.isEnabledFor(Level.WARN))
				log.warn("before adjustment:");
			for (Block block : blocks) {

				log.warn("Block" + block.getBehalfPoint() + " Live? "
						+ block.isAlreadyLive() + "; Dead? "
						+ block.isAlreadyDead());

			}
		}
		/**
		 * text[0] = new String("[_, W, B, _]");<br/>
		 * text[1] = new String("[W, W, W, B]");<br/>
		 * text[2] = new String("[_, _, W, _]");<br/>
		 * text[3] = new String("[W, _, W, W]");<br/>
		 * ensure W[4,1] is recognized as live.<br/>
		 * solution is to change W[4,1] by connecting to W[1,2], which is
		 * already live.<br/>
		 * text[0] = new String("[_, W, B, _]");<br/>
		 * text[1] = new String("[W, W, W, B]");<br/>
		 * text[2] = new String("[_, B, W, _]");<br/>
		 * text[3] = new String("[W, _, W, W]");<br/>
		 * ensure W[4,1] is recognized as live.<br/>
		 */
		Set<Block> liveBlocks = new HashSet<Block>();
		for (Block block : blocks) {
			if (block.isAlreadyLive() == false)
				continue;
			liveBlocks.add(block);
		}

		for (Block block : liveBlocks) {

			for (BlankBlock blankB : block.getBreathBlocks()) {
				if (blankB.isEyeBlock() == false) {
					// continue;
					// may be double connected
					for (Block blockF : blankB.getNeighborBlocks()) {
						if (blockF == block)
							continue;
						if (blockF.getColor() == block.getColor()
								&& blockF.getBreaths() >= 2) {
							int oldBreath = blockF.getBreaths();
							Set<Point> breaths = new HashSet<Point>();
							breaths.addAll(blockF.getBreathPoints());
							breaths.removeAll(block.getBreathPoints());
							if (oldBreath - breaths.size() >= 2
									&& blockF.isAlreadyLive() == false) {
								blockF.setLive(true);
								log.warn("block" + blockF.getBehalfPoint()
										+ " become live due to "
										+ block.getBehalfPoint());
							}
						}
					}
				} else {
					for (Block blockF : blankB.getNeighborBlocks()) {
						if (blockF == block)
							continue;
						if (blockF.getBreaths() >= 2) {
							blockF.setLive(true);
							log.warn("block" + blockF.getBehalfPoint()
									+ " become live due to "
									+ block.getBehalfPoint());
						}
					}
				}
				// if(blankB.getNumberOfPoint()>=2){
				//
				// }else if
			}
		}

		boolean unknown = false;
		if (log.isInfoEnabled())
			if (log.isEnabledFor(Level.WARN))
				log.warn("After adjustment:");
		for (Block block : blocks) {
			log.warn("Block" + block.getBehalfPoint() + " Live? "
					+ block.isAlreadyLive() + "; Dead? "
					+ block.isAlreadyDead());

			if (block.isAlreadyLive() == false
					&& block.isAlreadyDead() == false) {
				unknown = true;
				break;
			} else if (block.isAlreadyDead()) {
				for (Block enemyB : block.getEnemyBlocks()) {
					if (enemyB.isAlreadyDead()) {
						unknown = true;
						break;
					}
				}
			}

		}

		if (unknown == true) {
			return false;
		} else {

			/**
			 * side effect, remove the dead block.
			 */
			return true;
		}

		// return false;
	}

	/**
	 * The standard rule to calculate the result. suppose all the result are
	 * finalized.<br/>
	 * 判断胜负，但要求每块棋的死活已定。<br/>
	 * 
	 * 
	 * @return
	 */
	public FinalResult finalResult_deadExist() {
		// 双虚手终局
		// StepHistory history = this.getStepHistory();
		// if (history.getAllSteps().size() >= 2) {
		// if (history.getLastStep().isGiveup()) {
		// if (history.getSecondLastStep().isGiveup()) {
		//
		// }
		// }
		// }
		if (this.areBothPass()) {
			return this.finalResult_deadCleanedUp();
		}

		int black = 0;
		int white = 0;
		int shared = 0;
		try {

			String message = "+++++++++++final result+++++++++";
			this.check_internal(message);
			check(message);

		} catch (RuntimeException e) {
			throw e;
		}
		// hot fix, not sure why it is fixed,.
		// this.isFinalState();
		for (int i = 1; i <= boardSize; i++) {
			for (int j = 1; j <= boardSize; j++) {
				if (getColor(i, j) == Constant.BLACK) {
					if (this.getBlock(i, j).isAlreadyLive()) {
						black++;
					} else if (this.getBlock(i, j).isAlreadyDead()) {
						white++;
					}
				} else if (getColor(i, j) == Constant.WHITE) {
					if (this.getBlock(i, j).isAlreadyLive()) {
						white++;
					} else if (this.getBlock(i, j).isAlreadyDead()) {
						black++;
					}
					// white++;
				} else {
					BlankBlock blankBlock = this.getBlankBlock(i, j);
					if (blankBlock.isEyeBlock()) {

						if (blankBlock.isBlackEye()) {
							if (blankBlock.isLiveEye())
								black++;
							else
								white++;
							// black += block.getNumberOfPoint();
						} else {
							if (blankBlock.isLiveEye())
								white++;
							else
								black++;
							// white += block.getNumberOfPoint();
						}
					} else {
						int blackN = 0, whiteN = 0;
						for (Block nBlock : blankBlock.getBlackBlocks()) {
							if (nBlock.isAlreadyLive() == true)
								blackN++;
							break;
						}
						for (Block nBlock : blankBlock.getWhiteBlocks()) {
							if (nBlock.isAlreadyLive() == true)
								whiteN++;
							break;
						}
						if (blackN > 0 && whiteN > 0) {
							shared++;
						} else if (blackN == 0) {
							white++;
						} else if (whiteN == 0) {
							black++;
						}
						// shared += block.getNumberOfPoint();
					}
				}
			}
		}
		FinalResult res = new FinalResult(black, white, shared);
		if (black + white + shared != boardSize * boardSize) {
			log.error("Error: " + res.toString());
			log.error(this.getStateString());
			for (Block blackBlock : this.getBlackBlocksOnTheFly()) {
				if (log.isEnabledFor(Level.ERROR)) {
					log.error("blackBlock live? " + blackBlock.isAlreadyLive());
					log.error(blackBlock);
				}
			}
			for (Block whiteBlock : this.getWhiteBlocks()) {
				if (log.isEnabledFor(Level.ERROR)) {
					log.error("whiteBlock live? " + whiteBlock.isAlreadyLive());
					log.error(whiteBlock);
				}

			}
			for (BlankBlock blankBlock : this.getBlankBlocksOnTheFly()) {
				if (log.isEnabledFor(Level.ERROR)) {
					log.error("blankBlock eye? " + blankBlock.isEyeBlock());
					log.error("blankBlock live? " + blankBlock.isLiveEye());
					log.error(blankBlock);
				}
			}
			throw new RuntimeException(res.toString());
		}

		return res;
	}

}
