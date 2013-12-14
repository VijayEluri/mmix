package eddie.wu.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.comp.BlockRowColumnComparator;
import eddie.wu.domain.gift.Gift;

/**
 * include the basic data structure, but instantiation is done in subclass
 * BasicGoBoard
 * 
 * @author Administrator
 * 
 */
public abstract class AbsGoBoard {

	private transient static final Logger log = Logger
			.getLogger(AbsGoBoard.class);

	/**
	 * 
	 * @param state
	 *            index from 1.
	 */
	public static void printGoBoard(byte[][] state) {
		for (int i = 1; i < state.length - 1; i++) {
			byte[] temp = Arrays.copyOfRange(state[i], 1, state.length - 1);
			if (log.isDebugEnabled())
				log.debug(Arrays.toString(temp));
		}
	}

	public final int boardSize;

	/**
	 * 当前已有手数,处理之前递增.从1开始;
	 */
	protected int shoushu = 0;

	/**
	 * 两维是棋盘的坐标,数组下标从1到19; 气块和子块分开，气块是子块的附属。
	 */
	protected BoardPoint[][] boardPoints;

	/**
	 * other data are initialized in subclass.
	 * 
	 * @param boardSize
	 */
	public AbsGoBoard(int boardSize) {
		this.boardSize = boardSize;
	}

	/**
	 * 
	 * @return true 如果能长出一气，比如说花一手从二气长到四气。
	 */
	public boolean isBreathExtensible(Block block) {
		int oldBreath = block.getBreaths();
		for (Point breath : block.getBreathPoints()) {
			int newBreath = this.breathAfterPlay(breath, block.getColor())
					.size();
			if (newBreath >= oldBreath + 2)// 已经长出一气。
				return true;
		}

		return false;
	}

	/**
	 * 
	 * @param block
	 * @return true 如果每个候选点都是花一手棋，气数不变或者减少。
	 */
	public boolean isBreathNotExtensible(Block block) {
		int oldBreath = block.getBreaths();
		for (Point breath : block.getBreathPoints()) {
			int newBreath = this.breathAfterPlay(breath, block.getColor())
					.size();
			if (newBreath >= oldBreath + 1)// 仍有希望长气
				return false;
		}

		return true;
	}

	/**
	 * 事先计算出落子后的气数 static calculation. <br/>
	 * 算的是落子点的气数
	 * 
	 * @param step
	 * @return
	 */
	public Set<Point> breathAfterPlay(Point original, int color) {
		Set<Point> breaths = new HashSet<Point>();
		int enemyColor = ColorUtil.enemyColor(color);

		boolean eaten = false;
		Set<Block> eatenBlocks = new HashSet<Block>();
		Set<Block> friendBlocks = new HashSet<Block>();
		for (Delta deltaA : Constant.ADJACENTS) {
			Point nb = original.getNeighbour(deltaA);
			if (nb == null) {
				continue;
			}

			BoardPoint bp = this.getBoardPoint(nb);
			if (bp.getColor() == ColorUtil.BLANK) {// 落子点周围有空白点
				breaths.add(nb);
			} else if (bp.getColor() == enemyColor) {
				if (this.getBreaths(nb) == 1) {// 落子点周围有敌子可提.
					/**
					 * text[0] = new String("[B, B, B, B]");<br/>
					 * text[1] = new String("[B, B, B, _]");<br/>
					 * text[2] = new String("[B, W, W, B]");<br/>
					 * text[3] = new String("[B, W, _, B]");<br/>
					 */
					// 送吃的情况下。原有两气，有提子不可能变成一气。
					eaten = true;
					eatenBlocks.add(getBlock(nb));
					// breaths.addAll(this.getBlock(nb).getPoints());

				}
			} else if (bp.getColor() == color) {// 落子点周围有同色点
				breaths.addAll(bp.getBlock().getBreathPoints());
				breaths.remove(original);
				friendBlocks.add(bp.getBlock());
			}
		}

		if (eaten == true) {
			for (Block eatenB : eatenBlocks) {
				for (Point eatenP : eatenB.getPoints()) {
					for (Delta delta : Constant.ADJACENTS) {
						Point temp = eatenP.getNeighbour(delta);
						if (temp == null) {
							continue;
						}
						if (temp == original) {
							breaths.add(eatenP);
						} else if (this.getColor(temp) == color
								&& friendBlocks.contains(this.getBlock(temp))) {
							breaths.add(eatenP);

						}

					}
				}
			}
		}
		return breaths;
	}

	/**
	 * TODO: whether increase breath when eating enemy block. <br/>
	 * 算的是制定目标块的气数。
	 * 
	 * @param targetBlock
	 * @param original
	 *            to eat enemy pon
	 * @param color
	 * @return
	 */
	public Set<Point> breathAfterPlay(Block targetBlock, Point original,
			int color) {
		Set<Point> breaths = new HashSet<Point>();
		breaths.addAll(targetBlock.getBreathPoints());

		int enemyColor = ColorUtil.enemyColor(color);
		for (Delta deltaA : Constant.ADJACENTS) {
			Point nb = original.getNeighbour(deltaA);
			if (nb == null) {
				continue;
			}

			BoardPoint bp = this.getBoardPoint(nb);
			if (bp.getColor() == ColorUtil.BLANK) {// 落子点周围有空白点
				// breaths.add(nb);
				assert false;
			} else if (bp.getColor() == enemyColor) {
				if (bp.getBreaths() == 1) {// 落子点周围有敌子可提.
					for (Point blankPoint : bp.getBlock().getPoints()) {
						for (Delta delta : Constant.ADJACENTS) {
							Point neighbor = blankPoint.getNeighbour(delta);
							if (neighbor == null)
								continue;
							BoardPoint neighborBP = this
									.getBoardPoint(neighbor);
							if (neighborBP.getColor() == bp.getEnemyColor()
									&& neighborBP.getBlock() == targetBlock) {
								breaths.add(blankPoint);
							}
						}
					}
				}
			} else if (bp.getColor() == color) {// 落子点周围有同色点
				breaths.addAll(bp.getBlock().getBreathPoints());
			}
		}
		breaths.remove(original);
		return breaths;
	}

	public List<BasicBlock> getAllBlocks() {
		Set<BasicBlock> blocks = new HashSet<BasicBlock>();
		for (int i = 1; i <= boardSize; i++) {
			for (int j = 1; j <= boardSize; j++) {
				blocks.add(this.getBasicBlock(i, j));
			}
		}
		List<BasicBlock> list = new ArrayList<BasicBlock>(blocks.size());
		list.addAll(blocks);
		Collections.sort(list, new BlockRowColumnComparator());

		return list;
	}

	public BasicBlock getBasicBlock(Point point) {
		return this.getBoardPoint(point).getBasicBlock();
	}

	/**
	 * get black blocks form current state structure.
	 * 
	 * @return Set
	 */
	public Set<Block> getBlackBlocksOnTheFly() {

		return getBlocksFromStateOnTheFly(ColorUtil.BLACK);
	}

	public Set<Block> getBlackWhiteBlocksOnTheFly() {
		Set<Block> blocks = new HashSet<Block>();
		blocks.addAll(this.getBlocksFromStateOnTheFly(Constant.BLACK));
		blocks.addAll(this.getBlocksFromStateOnTheFly(Constant.WHITE));
		return blocks;
	}

	public BlankBlock getBlankBlock(int row, int column) {
		return getBoardPoint(row, column).getBlankBlock();
	}

	public BlankBlock getBlankBlock(Point point) {
		return this.getBoardPoint(point).getBlankBlock();
	}

	/**
	 * get blank point blocks form current state structure.
	 * 
	 * @return Set
	 */
	public Set<BlankBlock> getBlankBlocksOnTheFly() {
		return getBlankSetFromState();
	}

	/**
	 * similar code, but the return type is different
	 * 
	 * @return
	 */
	public Set<BlankBlock> getBlankSetFromState() {
		Set<BlankBlock> blocks = new HashSet<BlankBlock>();
		BlankBlock temp = null;
		for (int i = 1; i <= boardSize; i++) {
			for (int j = 1; j <= boardSize; j++) {
				if (getColor(i, j) == ColorUtil.BLANK) {
					temp = getBoardPoint(i, j).getBlankBlock();
					if (temp.getPoints().isEmpty()) {
						if (log.isDebugEnabled())
							log.debug("empty blank block --- debug!");
					}
					blocks.add(temp);
				}
			}
		}
		// log.debug("return from getBlackBlocksFromState");
		return blocks;

	}

	/**
	 * 
	 * @param row
	 * @param column
	 * @return null if the point in blank
	 */
	public Block getBlock(int row, int column) {
		return getBoardPoint(row, column).getBlock();
	}

	public Block getBlock(Point point) {
		return this.getBoardPoint(point).getBlock();
	}

	/**
	 * get latest data on the fly, but it may be not fast. <br/>
	 * TODO:will have a version with accumulated data
	 * 
	 * @param color
	 * @return
	 */
	public Set<Block> getBlocksFromStateOnTheFly(int color) {
		Set<Block> blocks = new HashSet<Block>();
		Block temp = null;
		for (int i = 1; i <= boardSize; i++) {
			for (int j = 1; j <= boardSize; j++) {
				if (getBoardPoint(i, j).getColor() == color) {
					temp = getBoardPoint(i, j).getBlock();
					if (temp.getPoints().isEmpty()) {
						if (log.isDebugEnabled())
							log.debug("debug! temp.getPoints().isEmpty()");
					}
					blocks.add(temp);
				}
			}
		}
		// log.debug("return from getBlackBlocksFromState");
		return blocks;

	}

	public BoardBreathState getBoardBreathState() {
		return new BoardBreathState(this.getBreathArray());
	}

	public abstract BoardColorState getBoardColorState();

	/**
	 * index conversion related logic
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public BoardPoint getBoardPoint(int row, int column) {
		return boardPoints[row][column];
	}

	/**
	 * transfer into bi-dimension array
	 * 
	 * @return
	 */
	public byte[][] getBreathArray() {
		int matrixSize = this.boardSize + 2;
		byte[][] a = new byte[matrixSize][matrixSize];
		byte i, j, k;
		for (i = 1; i <= boardSize; i++) {
			for (j = 1; j <= boardSize; j++) {
				if (getBoardPoint(i, j).getColor() == ColorUtil.BLACK
						|| getBoardPoint(i, j).getColor() == ColorUtil.WHITE) {
					a[i][j] = (byte) getBoardPoint(i, j).getBreaths();
				}
			}
		}
		return a;
	}

	public byte[][] getColorArray() {
		int matrixSize = this.boardSize + 2;
		byte[][] a = new byte[matrixSize][matrixSize];
		byte i, j, k;
		for (i = 1; i <= boardSize; i++) {
			for (j = 1; j <= boardSize; j++) {
				a[i][j] = (byte) (getBoardPoint(i, j).getColor());
			}
		}
		return a;
	}

	public BlankBlock getBreathBlock(Point point) {
		return this.getBoardPoint(point).getBlankBlock();
	}

	public int getColor(int row, int column) {
		return getBoardPoint(row, column).getColor();
	}

	public int getColor(Point point) {
		return this.getBoardPoint(point).getColor();
	}

	/**
	 * index from 0
	 * 
	 * @return
	 */
	public char[][] getDisplayMatrixState() {
		return getBoardColorState().getDisplayMatrixState();
	}

	/**
	 * now it is same as getDisplaymatrix
	 * 
	 * @return
	 */
	public byte[][] getMatrixState() {
		return getBoardColorState().getMatrixState();
	}

	/**
	 * 不仅计算落子点周围的空白点数.还计算气的增加情况. 可以提供Candidate需要的信息.
	 * 
	 * @param original
	 * @param color
	 * @return
	 */
	public NeighborState getNeighborState(Point original, int color) {
		NeighborState state = new NeighborState();
		state.setOriginal(original);

		int enemyColor = ColorUtil.enemyColor(color);
		state.setFriendColor(color);
		state.setEnemyColor(enemyColor);
		state.setOriginalBlankBlock(this.getBlankBlock(original));

		int enemy = 0;
		int friend = 0;
		int blank = 0;
		for (Delta delta : Constant.ADJACENTS) {
			Point nb = original.getNeighbour(delta);
			if (nb == null) {
				continue;
			} else if (this.getColor(nb) == ColorUtil.BLANK) {
				// 落子点周围有空白点
				blank++;
				state.addBlankPoint(nb);
				state.setValid(true);
			} else if (getColor(nb) == enemyColor) {
				enemy++;
				state.addEnemyBlock(getBlock(nb));
				try {
					if (this.getBreaths(nb) == 1) {// 落子点周围有敌子可提.
						state.addEatenBlocks(getBlock(nb));
						state.setEating(true);
						state.setValid(true);

						// eaten block originally are capturing its enemy
						Block minBreathEnemyBlock = this.getBlock(nb)
								.getMinBreathEnemyBlock();
						if (minBreathEnemyBlock != null
								&& minBreathEnemyBlock.getBreaths() == 1) {
							state.setRemoveCapturing(true);
						}
					} else if (this.getBreaths(nb) == 2) {
						state.setCapturing(true);
						state.addCapturingBlocks(this.getBlock(nb));
					}
				} catch (RuntimeException e) {
					System.err.println("nb=" + nb);
					log.setLevel(Level.DEBUG);
					this.printInternalData(log);
					throw e;
				}
			} else if (getColor(nb) == color) {
				friend++;
				state.addFriendBlock(getBlock(nb));
				if (getBreaths(nb) > 1) {
					state.setValid(true);
				}
			}
		}
		state.setBlank(blank);
		state.setEnemy(enemy);
		state.setFriend(friend);

		if (state.isValid()) {
			if (state.isEating()) { // 有提子
				if (blank == 0) {
					if (state.getEatenBlocks().size() == 1) {

						if (state.getEatenBlocks().iterator().next()
								.getNumberOfPoint() == 1) {
							if (friend == 0)
								log.info("提劫 " + original);
							else {
								log.info("提一子 " + original);
								state.setIncreaseBreath(false);

								Set<Point> breath = new HashSet<Point>();
								for (Block friendB : state.getFriendBlocks()) {
									breath.addAll(friendB.getBreathPoints());
								}
								breath.remove(original);
								if (breath.size() + 1 <= 1) {
									state.setGifting(true);
								}
							}

							// if (friend > 0) {
							// } else {
							// BUG, loop is not gifting.
							// state.setGifting(true);
							// }
							// Gift gift = new Gift();
							// // gift.setOriginalStones(friend);BUG
							// gift.setOriginalStones(friendStones);
							// if (activeGift == true) {
							// gift.setOriginalBreath(2);
						} else {
							log.info("提一块棋 - 两子或以上 " + original);
						}

					} else {
						log.info("提两块棋或以上 " + original);
					}
					// 被倒扑的识别.
				} else {
					// 两气或者两气以上
				}
			} else { // 没有提子
				if (friend > 0) {
					Set<Point> breaths = new HashSet<Point>();
					int friendStones = 0;
					boolean activeGift = false;
					int minFriendBreath = 128;
					for (Block block : state.getFriendBlocks()) {
						if (block.getBreaths() < minFriendBreath) {
							minFriendBreath = block.getBreaths();
						}
						breaths.addAll(block.getBreathPoints());
						/**
						 * 考虑相邻子能否长气,如果可以就不必送吃了.
						 */
						// if (this.canIncreaseBreath_temporarily(block)) {
						// friendCanInc = true;
						// }
						if (block.getBreathPoints().size() >= 2) {
							activeGift = true;
						}
						friendStones += block.getNumberOfPoint();
					}
					breaths.remove(original);
					breaths.addAll(state.getBlankPoints());
					if (breaths.size() == 1) {
						/**
						 * 多子送吃,仍有可能是破眼.<br/>
						 * 要考虑对方是否必须送吃.
						 */
						state.setGifting(true);
						Gift gift = new Gift();
						// gift.setOriginalStones(friend);BUG
						gift.setOriginalStones(friendStones);
						if (activeGift == true) {
							gift.setOriginalBreath(2);
						} else {
							gift.setOriginalBreath(1);
						}
						state.setGift(gift);
						log.info("多子送吃 " + original);
					} else {
						if (minFriendBreath == 1) {
							state.setRemoveCapturing(true);
						}
					}
				} else if (blank == 1) {
					/**
					 * 单子送吃,未必不利,比如倒扑.即使不是倒扑,也可能是破眼.(
					 */
					state.setGifting(true);
					Gift gift = new Gift();
					gift.setOriginalStones(0);
					state.setGift(gift);
					log.info("单子送吃 " + original);
				}
				//
			}
		} else {// 自提不入气点
			if (log.isInfoEnabled()) {
				log.info("自提不入气点: " + original);
			}
		}

		if (state.isEating()) {
			return state;
		}

		// breath increase?
		int oldBreath = 0;
		int points = 0;
		Set<Point> breaths = new HashSet<Point>();
		breaths.addAll(state.getBlankPoints());
		if (state.getFriendBlocks().isEmpty() == false) {
			for (Block friendB : state.getFriendBlocks()) {
				breaths.addAll(friendB.getBreathPoints());
				if (friendB.getNumberOfPoint() > points) {
					points = friendB.getNumberOfPoint();
					// take biggest block's breath, make oldBreath as small as
					// possible
					oldBreath = friendB.getBreaths();

				}
			}
		}
		breaths.remove(state.getOriginal());
		state.setBreath(breaths.size());

		/**
		 * oldBreath!=0 is necessary, otherwise single stone is preferred! <br/>
		 * huge difference v.s. <br/>
		 * oldBreath!=0&&
		 */
		if (oldBreath == 0) {
			// adjustment for stand-alone play
			if (breaths.size() >= 2) {
				state.setIncreaseBreath(true);
				state.setIncreasedBreath(1);
				if (state.getEnemyBlocks().isEmpty() == false) {
					// decreasing enemy breath is counted.
					state.setIncreasedBreath(1 + 1);
				}
			} else {
				// send gift
			}
		} else {
			// decreasing enemy breath is considered increase of own breath.
			int newBreath = breaths.size() + state.getEnemyBlocks().size();
			if (newBreath > oldBreath) {
				state.setIncreaseBreath(true);
				state.setIncreasedBreath(newBreath - oldBreath);
			}
		}
		return state;
	}

	public int getNumberOfPoint() {
		int count = 0;
		for (int row = 1; row <= boardSize; row++) {
			for (int column = 1; column <= boardSize; column++) {
				if (getBoardPoint(row, column).getColor() == Constant.BLACK
						|| getBoardPoint(row, column).getColor() == Constant.WHITE) {
					count++;
				}
			}
		}
		return count;
	}

	public Point getPoint(int row, int column) {
		return Point.getPoint(boardSize, row, column);
	}

	public int getShoushu() {
		return shoushu;
	}

	public StringBuilder getStateString() {
		BoardColorState colorState = getBoardColorState();
		return colorState.getStateString();
	}

	public Set<Block> getBlackBlocks() {
		return getBlocksFromStateOnTheFly(ColorUtil.BLACK);
	}

	/**
	 * get white blocks form current state structure.
	 * 
	 * @return Set
	 */
	public Set<Block> getWhiteBlocks() {
		return getBlocksFromStateOnTheFly(ColorUtil.WHITE);
	}

	public boolean isBlack(Point point) {
		return this.getBoardPoint(point).getColor() == Constant.BLACK;
	}

	public boolean isBlank(Point point) {
		return this.getBoardPoint(point).getColor() == Constant.BLANK;
	}

	public boolean isWhite(Point point) {
		return this.getBoardPoint(point).getColor() == Constant.WHITE;
	}

	public void printState() {
		log.warn(this.getStateString().toString());
	}

	public void errorState() {
		log.error(this.getStateString().toString());
	}

	public void printState(Logger log) {
		log.warn(this.getStateString().toString());
	}

	public void printInternalData(Logger log) {
		for (Block block : this.getBlackWhiteBlocksOnTheFly()) {
			log.debug(block);
		}
		for (BlankBlock block : this.getBlankBlocksOnTheFly()) {
			log.debug(block);
		}
	}

	/**
	 * 只打印局部。
	 * 
	 * @param shape
	 */
	public void printState(Shape shape) {
		BoardColorState colorState = getBoardColorState();
		char[][] state = colorState.getDisplayMatrixState(shape);
		for (int i = 0; i < state.length; i++) {
			// if(log.isDebugEnabled()) log.debug(Arrays.toString(state[i]));
			if (log.isEnabledFor(Level.WARN))
				log.warn(Arrays.toString(state[i]));
		}
	}

	public void setBlock(Point point, BlankBlock block) {
		getBoardPoint(point).setBlankBlock(block);
	}

	public void setBlock(Point point, Block block) {
		getBoardPoint(point).setBlock(block);
	}

	public void setColor(int row, int column, int color) {
		getBoardPoint(row, column).setColor(color);
	}

	public void setColor(Point point, int color) {
		getBoardPoint(point).setColor(color);
	}

	public void setShoushu(int shoushu) {
		this.shoushu = shoushu;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		char[][] cs = this.getDisplayMatrixState();
		for (int i = 0; i < boardSize; i++) {
			sb.append(Arrays.toString(cs[i]));
			sb.append("\r\n");
		}
		return sb.toString();
	}

	private BasicBlock getBasicBlock(int i, int j) {
		return getBoardPoint(i, j).getBasicBlock();
	}

	/**
	 * clear calculate flag
	 * 
	 */
	protected void clearFlag() {
		byte i, j;
		for (i = 1; i <= boardSize; i++) {
			for (j = 1; j <= boardSize; j++) {
				getBoardPoint(i, j).setCalculatedFlag(false);
			}
		}
	}

	/**
	 * only internal structure will index from 0. in api level. it always index
	 * from 1 to make life easier.
	 * 
	 * @param point
	 * @return
	 */
	protected BoardPoint getBoardPoint(Point point) {
		return boardPoints[point.getRow()][point.getColumn()];
	}

	protected int getBreaths(Point point) {
		BoardPoint boardPoint = this.getBoardPoint(point);
		if (boardPoint == null) {
			boardPoint = this.getBoardPoint(point);
		}
		return boardPoint.getBreaths();

	}

	protected void setBoardPoint(BoardPoint boardPoint) {
		boardPoints[boardPoint.getRow()][boardPoint.getColumn()] = boardPoint;
	}

	/**
	 * verify calculate Flag
	 * 
	 */
	protected void verifyFlag() {
		byte i, j;
		for (i = 1; i <= boardSize; i++) {
			for (j = 1; j <= boardSize; j++) {
				if (getBoardPoint(i, j).isCalculatedFlag()) {
					String string = "Flag Error: [" + i + "," + j + "]";

					log.error(string);
					throw new RuntimeException(string);
				}
			}
		}
	}

	public boolean canIncreaseBreath_netly(Block block) {
		// Assert.assertTrue("target block should only have one or two breath",
		// block.getBreaths() <= 2);
		for (Point breath : block.getBreathPoints()) {
			for (Delta delta : Constant.ADJACENTS) {
				Point neighborP = breath.getNeighbour(delta);
				if (neighborP == null)
					continue;
				int breaths = this.breathAfterPlay(breath, block.getColor())
						.size();
				if (breaths >= block.getBreaths() + 2)
					return true;
				// TODO: if breath are same.
			}
		}
		return false;
	}

	/**
	 * 白雪皑皑呢 要送吃的块,原先只有二气,落子后会减少一气.如果两口气都是这种情况,只有送吃一途.<br/>
	 * 但未必不行.若有一个能长气,则也是脱困之道.<br/>
	 * 还有扑的情况.<br>
	 * 简化处理.要求该块的气点除了不入气点,就是送吃点. <br/>
	 * whether the block can increase its breath. <br/>
	 * 要求原块是二气，计算是否能增加到三气，然后可以入气
	 * 
	 * @param block
	 *            该块能否长气。
	 * @return
	 */
	public boolean canIncreaseBreath_temporarily(Block block) {
		/**
		 * 01[_, B, W]01<br/>
		 * 02[B, B, _]02<br/>
		 * 03[B, _, _]03<br/>
		 */
		Assert.assertTrue("target block should only have one or two breath",
				block.getBreaths() <= 2);
		for (Point breath : block.getBreathPoints()) {
			for (Delta delta : Constant.ADJACENTS) {
				Point neighborP = breath.getNeighbour(delta);
				if (neighborP == null)
					continue;
				int breaths = this.breathAfterPlay(breath, block.getColor())
						.size();
				if (breaths > block.getBreaths())
					return true;
				// TODO: if breath are same.
			}
		}
		return false;
	}

}
