/*
 * Created on 2005-4-21
 *
 * Eddie Wu CopyRight @2005
 */
package eddie.wu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.comp.RowColumnComparator;

/**
 * some concern; because the blank/white block is different from blank point
 * block. and we use same type block to present them, so must decide whether to
 * invoke special method according to the color. (Update, separate blank block
 * with black/white block.we use name blank block instead of breath block
 * because the point in breath blocks maybe not the breath.)
 * 
 * 棋块--breath only retain in the black/white block. the breath of the point in
 * the blank block is nonsense. 1. block is divide into three sub-type; one is
 * blank point block(and breath block is special. surround only by same color
 * block, this is models as a flag in blank block instead of a class) another is
 * black block the last is white block
 * 
 * 2. black/white block has enemy Block. blank point block has no enemy Block.
 * 3. black/white block has breath Block. blank point block has no breath Block.
 * 4. black/white block has breath point set. blank point block has no set of
 * breath point 5. blank block has surrounded blank/white block or both.
 * 
 * change 1; decide to contain point. not BoardPoint. avoid directional
 * reference.
 * 
 * change 2; separate blank block with normal black/white block.
 * 
 * @author eddie
 */
public class Block extends BasicBlock implements Cloneable,
		java.io.Serializable {
	public static boolean equals(Block a, Block other) {

		if (other.getColor() == a.getColor()
				&& a.getPoints().equals(other.getPoints())
				&& a.getBreathPoints().equals(other.getBreathPoints())
				&& a.getEnemyBlockRepre().equals(other.getEnemyBlockRepre())
				&& a.getBreathBlockRepre().equals(other.getBreathBlockRepre())) {
			return true;
		} else {
			return false;
		}

	}

	Logger log = Logger.getLogger(Block.class);

	/**
	 * 周围敌方的棋块.
	 */
	private Set<Block> enemyBlocks = new HashSet<Block>();
	/** 棋块气点集合 */
	private Set<Point> breathPoints = new HashSet<Point>();//
	/**
	 * 气点对应的气块集合<br/>
	 * record all the blocks the breath point belong to, note that not all the
	 * Points in those blocks are breath for current block.
	 */
	private Set<BlankBlock> breathBlocks = new HashSet<BlankBlock>();

	private boolean breathCalculated;
	private boolean liveDeadMarked;
	private boolean levelExtended;

	// 不入气点的集合。死活计算用，自行初始化。
	private Set<Point> eyes = new HashSet<Point>();

	// 不入气点的集合,单个不能活棋，属于假眼。死活计算用，自行初始化。
	private Set<Point> fakeEyes = new HashSet<Point>();
	private Group group;

	private int priority; // used for sorting.

	/**
	 * used to hash, ensure the block has same hash during its life cycle. when
	 * block merges, get the top left point as the first point.
	 */
	private Point firstPoint;

	/**
	 * one block may be has potential connection to two live block, then it
	 * become live.
	 */
	private Set<Block> liveFriend_canConn;

	/**
	 * block may have already connected with live block
	 */
	private Set<Block> liveFriend_connected;

	// public void setGrouped(boolean grouped) {
	// this.grouped = grouped;
	// }

	/**
	 * 记录棋块周围的扩展点,包括拆一和拆二.<br/>
	 * 这个信息需要及时更新.
	 */
	private Set<Point> extendOne = new HashSet<Point>();

	private Set<Point> extendTwo = new HashSet<Point>();

	// public boolean isSharedBreathBlock() {
	// return sharedBreathBlock;
	// }
	// public void setSharedBreathBlock(boolean sharedBreathBlock) {
	// this.sharedBreathBlock = sharedBreathBlock;
	// }
	// public boolean isBreathBlock() {
	// return color == Constant.BLANK;
	// }

	// public void setBreathBlock(boolean breathBlock) {
	// this.breathBlock = breathBlock;
	// }

	/**
	 * should set it first before using it<br/>
	 * 以后特指两眼活棋。和双活区别开来。
	 */
	private transient boolean live;

	/**
	 * 已经识别出双活。看一步就可以决定的那种，
	 */
	private transient boolean coLive;

	/**
	 * all the enemy is live,itself is not live yet. there is no chance to
	 * survive. state is unknown if both live and dead is false. which means
	 * fight is ongoing ( 对杀尚在进行中.)
	 */
	private transient boolean dead;

	/**
	 * live or dead is not decided yet. quick way to mark that we need further
	 * calculation.
	 */
	private transient boolean unknown;

	public Block(int color) {
		super(color);
	}

	public void addBreathBlock_oneWay(BlankBlock abreathBlock) {
		breathBlocks.add(abreathBlock);
	}

	public void addBreathBlock_twoWay(BlankBlock abreathBlock) {
		breathBlocks.add(abreathBlock);
		if (this.isBlack()) {
			abreathBlock.addBlackBlock(this);
		} else {
			abreathBlock.addWhiteBlock(this);
		}
		if (log.isDebugEnabled()) {
			String message = "Block " + this.getBehalfPoint()
					+ " connected with breathBlock "
					+ abreathBlock.getBehalfPoint();
			log.debug(message);
		}
	}

	public void addBreathPoint(BoardPoint point) {
		addBreathPoint(point.getPoint());
	}

	public void addBreathPoint(Point point) {
		// if (point.isNotValid()) {
		// throw new RuntimeException("point is not valid when add Breath"
		// + point);
		// }
		if (breathPoints.add(point)) {

		} else {// bug fix. it is possible to return false; because of shared
				// breath.
				// throw new
				// RuntimeException("failed when remove Breath Point"+point);
			log.info("return false when add Breath Point:" + point);
		}
	}

	public void addBreathPoints(Set<Point> addedBreaths) {
		this.breathPoints.addAll(addedBreaths);

	}

	public void addEnemyBlock_oneWay(Block block) {
		this.enemyBlocks.add(block);
	}

	// a special point on behalf the whole bolck.
	// byte row;
	// byte column;

	// public Block(byte row, byte column){
	// super();
	// this.row=row;
	// this.column=column;
	// color=Constant.BLANK_POINT;
	// }

	public void addEnemyBlock_twoWay(Block block) {
		this.enemyBlocks.add(block);
		block.enemyBlocks.add(this);
		if (log.isDebugEnabled()) {
			String message = new String("Block " + block.getBehalfPoint()
					+ " connected with" + this.getBehalfPoint());
			log.debug(message);
		}
	}

	public void addExtendOnePoint(Point point) {
		extendOne.add(point);
	}

	public void addExtendTwoPoint(Point point) {
		extendTwo.add(point);
	}

	/**
	 * 单点的眼位
	 * 
	 * @param Point
	 */
	public void addEye(Point Point) {
		eyes.add(Point);
	}

	public void addEyes(Set<Point> eye) {
		eyes.addAll(eye);
	}

	/**
	 * 悔棋时恢复和气块的联系(根据原先保留的单向联系from this to breath, add link from breath to this）
	 * 
	 */
	public void attachToBreath() {
		for (BlankBlock blankBlock : breathBlocks) {
			if (this.isBlack()) {
				if (blankBlock.getBlackBlocks().contains(this)) {
					if (log.isDebugEnabled()) {
						log.debug("Blank Block " + blankBlock.getBehalfPoint()
								+ " already point to " + this.getBehalfPoint());
					}
				} else {
					boolean added = blankBlock.getBlackBlocks().add(this);
					if (log.isDebugEnabled()) {
						log.debug("Blank Block " + blankBlock.getBehalfPoint()
								+ " repoint to " + this.getBehalfPoint()
								+ " added=" + added);
					}
				}
			} else if (this.isWhite()) {
				if (blankBlock.getWhiteBlocks().contains(this)) {
					if (log.isDebugEnabled()) {
						log.debug("Blank Block " + blankBlock.getBehalfPoint()
								+ " already point to " + this.getBehalfPoint());
					}
				} else {
					boolean added = blankBlock.getWhiteBlocks().add(this);
					if (log.isDebugEnabled()) {
						log.debug("Blank Block " + blankBlock.getBehalfPoint()
								+ " repoint to " + this.getBehalfPoint()
								+ " added=" + added);
					}
				}
			}

		}
	}

	/**
	 * 悔棋时恢复和敌块的联系(根据原先保留的单向联系from this to enemy, add link from enemy to this）
	 * 
	 */
	public void attachToEnemy() {
		for (Block block : enemyBlocks) {
			if (block.enemyBlocks.contains(this)) {
				if (log.isDebugEnabled()) {
					log.debug("Block " + block.getBehalfPoint()
							+ " already point to " + this.getBehalfPoint());
				}
			} else {
				boolean added = block.enemyBlocks.add(this);
				if (log.isDebugEnabled()) {
					log.debug("Block " + block.getBehalfPoint()
							+ " repoint to " + this.getBehalfPoint()
							+ " added=" + added);
				}
				for (Point point : this.allPoints) {
					if (block.getBreathPoints().contains(point)) {
						block.getBreathPoints().remove(point);
						if (log.isDebugEnabled()) {
							log.debug("remove breath" + point);
						}
					}
				}
			}

		}
	}

	/**
	 * 己方块和并后,旧块和相邻块单向脱离关系.新块和旧块的敌块建立双向的相邻关系.
	 * 
	 * @param newBlock
	 */
	public void changeEnemyBlockTo(Block newBlock) {
		for (Block block : enemyBlocks) {
			// unlink old block (one-direction).
			if (block.isActive() == false)
				continue;
			block.removeEnemyBlock_oneWay(this);
			block.addEnemyBlock_twoWay(newBlock);
		}

	}

	public void clearBreath() {
		this.breathPoints.clear();
	}

	public void clearEyes() {
		this.eyes.clear();
	}

//	public Object clone() {
//		Block temp = null;
//		try {
//			temp = (Block) super.clone();
//		} catch (CloneNotSupportedException e) {
//			e.printStackTrace();
//		}
//		temp.breathPoints = (Set<Point>) ((HashSet<Point>) breathPoints)
//				.clone();
//		temp.allPoints = (Set) ((HashSet) allPoints).clone();
//		temp.enemyBlocks = (Set) ((HashSet) enemyBlocks).clone();
//		return temp;
//	}

	/**
	 * 自己仍指向相邻气块；相邻气块不再指向自己
	 */
	public void dettachFromBreath() {
		for (BlankBlock blankBlock : this.breathBlocks) {
			boolean removed = false;
			if (this.isBlack()) {
				removed = blankBlock.getBlackBlocks().remove(this);
			} else {
				removed = blankBlock.getWhiteBlocks().remove(this);
			}

			if (log.isDebugEnabled()) {
				log.debug("Block " + this.getBehalfPoint()
						+ " dettach from breath Block "
						+ blankBlock.getBehalfPoint() + " removed=" + removed);
			}
		}
	}

	/**
	 * 自己仍指向相邻敌块；相邻敌块不再指向自己
	 */
	public void dettachFromEnemy() {
		for (Block block : enemyBlocks) {
			boolean removed = block.enemyBlocks.remove(this);
			if (log.isDebugEnabled()) {
				log.debug("Block " + this.getBehalfPoint()
						+ " dettach from enemyBlock " + block.getBehalfPoint()
						+ " removed=" + removed);
			}

		}
	}

	public Set<Point> getAllPointsInSingleEye() {
		if (this.getNumberOfEyeBlock() != 1)
			throw new RuntimeException("getNumberOfEyeBlock()!=1");
		for (BlankBlock blankBlock : this.getBreathBlocks()) {
			if (blankBlock.isEyeBlock()) {
				return blankBlock.getPoints();
			}
		}
		return null;
	}

	/**
	 * The simple way to represent all the enemy blocks - one representative
	 * point for each breath block.
	 * 
	 * @return
	 */
	public Set<Point> getBreathBlockRepre() {
		Set<Point> points = new HashSet<Point>();
		for (BlankBlock block : this.breathBlocks) {
			points.add(block.getBehalfPoint());
		}
		return points;
	}

	/**
	 * @return Returns the breathBlock.
	 */
	public Set<BlankBlock> getBreathBlocks() {
		return breathBlocks;
	}

	/**
	 * @return Returns the allBreathPoints.
	 */
	public Set<Point> getBreathPoints() {
		return breathPoints;
	}

	/**
     *  
     */
	// public void changeColorToBlank() {
	// // TODO Auto-generated method stub
	// allBreathPoints.clear();
	// color = ColorUtil.BLANK;
	// enemyBlocks = null;
	// breathBlocks = null;
	// }

	public int getBreaths() {
		return this.breathPoints.size();
	}

	/**
	 * The simple way to represent all the enemy blocks - one representative
	 * point for each enemy block.
	 * 
	 * @return
	 */
	public Set<Point> getEnemyBlockRepre() {
		Set<Point> points = new HashSet<Point>();
		for (Block block : this.enemyBlocks) {
			points.add(block.getBehalfPoint());
		}
		return points;
	}

	/**
	 * @return Returns the enemyBlock.
	 */
	public Set<Block> getEnemyBlocks() {
		return enemyBlocks;
	}

	public int getEnemyColor() {

		return ColorUtil.enemyColor(color);
	}

	public Set<Point> getExtendOne() {
		return extendOne;
	}

	/**
	 * @param breathBlock
	 *            The breathBlock to set.
	 */
	// public void setBreathBlocks(Set<BlankBlock> breathBlock) {
	// this.breathBlocks = breathBlock;
	// }

	public Set<Point> getExtendTwo() {
		return extendTwo;
	}

	/**
	 * 本质上都是公气，有些称为外气是因为相关的棋块不是我们当前关心的。
	 * 
	 * @return
	 */
	public Set<Point> getExternalOrSharedBreath() {
		Set<Point> breaths = new HashSet<Point>();
		breaths.addAll(this.getBreathPoints());
		for (BlankBlock blankBlock : this.getBreathBlocks()) {
			if (blankBlock.isEyeBlock()) {
				breaths.removeAll(blankBlock.getPoints());
			}
		}
		if (log.isEnabledFor(Level.WARN)) {
			log.warn("external breath" + breaths);
		}
		return breaths;
	}

	public Set<Point> getEyes() {
		return eyes;
	}

	/**
	 * To prevent the has code change after the block content change. we use as
	 * less member as possible. to put it into a map, ensure the allPoints is
	 * correctly initialized and will not change during map operation.<br/>
	 * but when we deal with enemy blocks, its points may increase in the life
	 * cycle, so we have to use its first point as representative. <br/>
	 * 该算法有很大的问题,棋块和气块的大小是会动态改变的.用set来维护非常麻烦. 相邻关系也许不应该动态维护 . <br/>
	 * change 2012/02/05 switch to use basic hash code in Object. that is
	 * reference equality.
	 */
	// public int hashCode() {
	// // return this.getFirstPoint().hashCode();
	// return this.allPoints.hashCode();
	//
	// // return this.allPoints.hashCode() + this.allBreathPoints.hashCode() *
	// // 17;
	// }

	public Point getFirstPoint() {
		// TODO Auto-generated method stub
		return this.firstPoint;
	}

	public Group getGroup() {
		return group;
	}

	public Point getLastBreath() {
		if (breathPoints.size() == 1)
			return breathPoints.iterator().next();
		if (log.isDebugEnabled())
			log.debug(breathPoints);
		throw new RuntimeException("allBreathPoints.size()!=1" + breathPoints);
	}

	public boolean noEnemy() {
		return enemyBlocks.isEmpty();
	}

	public Block getMinBreathEnemyBlock() {
		int min = 128;
		Block minBlock = null;
		for (Block block : enemyBlocks) {
			// avoid to return live block!
			if (block.isAlreadyLive())
				continue;
			if (block.getBreaths() < min) {
				minBlock = block;
				min = block.getBreaths();
			}
		}
		return minBlock;
	}

	public Block getMaxBreathEnemyBlock() {
		int max = 0;
		Block maxBlock = null;
		for (Block block : enemyBlocks) {
			if (block.getBreaths() > max) {
				maxBlock = block;
				max = block.getBreaths();
			}
		}
		return maxBlock;
	}

	public int getMinEnemyBreath() {
		int min = 128;
		for (Block block : enemyBlocks) {
			if (block.isAlreadyLive()) {
				// live block has more than 128 breath
				continue;
			}
			if (block.getBreaths() < min) {
				min = block.getBreaths();
			}
		}
		// if no enemy ==> enemy has long breath.
		return min;
	}

	public int getNumberOfEyeBlock() {
		int count = 0;
		for (BlankBlock breathBlock : this.getBreathBlocks()) {
			if (breathBlock.isEyeBlock()) {
				count++;
			}
		}
		return count;
	}

	public Point getUniqueBreath() {
		TestCase.assertEquals(1, this.breathPoints.size());
		return this.breathPoints.iterator().next();
	}

	public BlankBlock getUniqueEyeBlock() {
		for (BlankBlock blank : this.breathBlocks) {
			if (blank.isEyeBlock())
				return blank;
		}
		return null;
	}

	public boolean isBreathCalculated() {
		return breathCalculated;
	}

	public boolean isCoLive() {
		return coLive;
	}

	public boolean isAlreadyDead() {
		return dead;
	}

	/**
	 * whether it is already part of group.
	 */
	public boolean isGrouped() {
		return group != null;
	}

	public boolean isAlreadyLive() {
		return live;
	}

	public boolean isUnknown() {
		return unknown;
	}

	public int numberOfEyes() {
		return eyes.size();
	}

	/**
	 * 解除自己和相邻气块的关系(单向解除,自己不再指向相邻气块).
	 * 
	 * @param blankBlock
	 */
	public void removeBreathBlock_oneWay(BlankBlock blankBlock) {
		this.breathBlocks.remove(blankBlock);
	}

	/**
	 * 解除自己和相邻气块的关系(双向解除).
	 * 
	 * @param block
	 */
	public void removeBreathBlocks_TwoWay() {
		for (Iterator<BlankBlock> iter = breathBlocks.iterator(); iter
				.hasNext();) {
			BlankBlock breathBlock = iter.next();
			breathBlock.removeNeighborBlock_oneWay(this);
			iter.remove();
		}
	}

	public void removeBreathPoint(Point point) {
		// if (point.isNotValid()) {
		// throw new RuntimeException(
		// "point is not valid when remove Breath Point" + point);
		// }
		if (breathPoints.remove(point)) {

		} else {
			/*
			 * bug fix. it is possible to return false; because of shared
			 * breath. throw new
			 * RuntimeException("failed when remove Breath Point"+point);
			 */

			log.info("return false when remove Breath Point:" + point
					+ " from block" + this.getBehalfPoint());
		}
	}

	public void removeBreathPoints(Set<Point> addedBreaths) {
		this.breathPoints.removeAll(addedBreaths);

	}

	/**
	 * 解除自己和相邻敌块的关系(单向解除,自己不再指向相邻敌块).
	 * 
	 * @param block
	 */
	public void removeEnemyBlock_oneWay(Block block) {
		this.enemyBlocks.remove(block);
	}

	/**
	 * 解除自己和相邻敌块的关系(双向解除).
	 * 
	 * @param block
	 */
	public void removeEnemyBlocks_TwoWay() {
		for (Iterator<Block> iter = enemyBlocks.iterator(); iter.hasNext();) {
			Block enemyBlock = iter.next();
			boolean remove = enemyBlock.enemyBlocks.remove(this);
			if (remove == false) {
				String message = "failed to remove " + this + "from"
						+ enemyBlock;
				throw new RuntimeException(message);
			}
			iter.remove();
		}
	}

	/**
	 * 解除自己和相邻敌块的关系(双向解除).
	 * 
	 * @param block
	 */
	public void removeEnemyBlocks_TwoWay(Block enemyBlock) {

		boolean remove = enemyBlock.enemyBlocks.remove(this);
		if (remove == false) {
			String message = "failed to remove " + this + "from" + enemyBlock;
			throw new RuntimeException(message);
		}
		remove = enemyBlocks.remove(enemyBlock);
		if (remove == false) {
			String message = "failed to remove " + enemyBlock + "from" + this;
			throw new RuntimeException(message);
		}

	}

	public void removeEye(Point Point) {
		fakeEyes.add(Point);
		eyes.remove(Point);
	}

	/**
	 * deprecated. concurrent exception
	 */
	public void removeFromAllBreathBlock() {
		for (BlankBlock blankB : this.breathBlocks) {
			boolean removed = blankB.removeNeighborBlock_twoWay(this);
			if (removed == false)
				throw new RuntimeException("removed = false");
		}
	}

	public void setBreathCalculated(boolean breathCalculated) {
		this.breathCalculated = breathCalculated;
	}

	/**
	 * @param allBreathPoints
	 *            The allBreathPoints to set.
	 */
	public void setBreathPoints(Set<Point> allBreathPoints) {
		this.breathPoints = allBreathPoints;
	}

	public void setCoLive(boolean coLive) {
		this.coLive = coLive;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
	}

	/**
	 * @param enemyBlock
	 *            The enemyBlock to set.
	 */
	public void setEnemyBlocks(Set<Block> enemyBlock) {
		this.enemyBlocks = enemyBlock;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public void setUnknown(boolean unknown) {
		this.unknown = unknown;
	}

	public String toString() {
		StringBuffer temp = new StringBuffer("\r\n" + this.getActiveString()
				+ "Block" + "@" + Integer.toHexString(hashCode())
				+ "[\r\ncolor=");
		List<Point> list = new ArrayList<Point>(allPoints.size());
		list.addAll(allPoints);
		Collections.sort(list, new RowColumnComparator());
		if (color == ColorUtil.BLACK || color == ColorUtil.WHITE) {
			temp.append(color);
			temp.append(", points=" + this.getNumberOfPoint());
			temp.append(", breaths = " + this.getBreaths());

			temp.append(",\r\nallPoints=");
			temp.append(list);
			temp.append(",\r\nallBreathPoints=");
			// temp.append(allBreathPoints);
			list = new ArrayList<Point>(breathPoints.size());
			list.addAll(breathPoints);
			Collections.sort(list, new RowColumnComparator());
			temp.append(list);
			try {
				if (this.breathBlocks.isEmpty() == false) {
					temp.append(",\r\nbreathBlocks=" + this.breathBlocks.size()
							+ "[\r\n");
					for (BlankBlock tempBlock : this.breathBlocks) {
						temp.append("\t[");
						temp.append(tempBlock.getTopLeftPoint());
						temp.append(", total=");
						temp.append(tempBlock.getNumberOfPoint());
						if (tempBlock.getNumberOfPoint() < 10) {
							temp.append(tempBlock.getPoints());
						}
						temp.append(",eyeBlock=" + tempBlock.isEyeBlock()
								+ "], \r\n");
					}
					temp.append("]");
				}
			} catch (Exception e) {
				if (log.isDebugEnabled())
					log.debug("Block " + this.getTopLeftPoint());
				throw new RuntimeException("Block " + this.getTopLeftPoint());
			}

			if (this.enemyBlocks.isEmpty() == false) {
				temp.append(",\r\nenemyBlocks=[");
				for (Block tempBlock : this.enemyBlocks) {
					temp.append(tempBlock.getTopLeftPoint() + ", ");
				}
				temp.append("]");
			}
		} else {

		}

		/*
		 * bug fix 2
		 */
		// cause recursive invocation and get Stack Over Flow Error!
		// temp.append(",enemyBlocks=");
		// temp.append(this.enemyBlocks);
		temp.append("]");
		return temp.toString();
	}

	/**
	 * final state, one block with two eyes. so that no further change is
	 * possible <br/>
	 * static means there is no more valid candidate step for its opponent. even
	 * the step which has no impact but change board state.
	 * @deprecated use the one in surviveAnalysis
	 * @return false if not live or its live but there is possible candidate
	 *         step for its opponent even it makes no sense.
	 */
//	public boolean isAlreadyLive_Static() {
//		if (this.getBreaths() < 2)
//			return false;
//
//		int eyes = 0;
//		Set<Block> friendBlock = new HashSet<Block>();
//
//		for (BlankBlock blankBlock : this.getBreathBlocks()) {
//			if (blankBlock.isEyeBlock() == true) {// 眼位
//
//				if (blankBlock.getNumberOfPoint() != 1) {
//					// 单块所成的眼必为真眼。
//					if (blankBlock.getNeighborBlocks().size() == 1) {
//						// 大眼没有最终定型。但是可以保证有一眼,暂时记为一眼.
//						eyes += 1;
//					} else {
//						continue;
//					}
//				} else {
//					// 单块所成的眼必为真眼。
//					if (blankBlock.getNeighborBlocks().size() == 1) {
//						eyes += 1;
//					} else if (blankBlock.getNeighborBlocks().size() == 2) {
//						fakeEyes.add(blankBlock.getUniquePoint());
//						friendBlock.addAll(blankBlock.getNeighborBlocks());
//					}
//				}
//			} else {// 公气
//				if (blankBlock.getNumberOfPoint() != 1) {
//					// 可能双活，但是没有最终定型。
//					continue;
//				} else {
//					// 是否是对方的自提不入气点。？
//					/**
//					 * BBBBBBB<br/>
//					 * B_W_W_B<br/>
//					 * BBBBBBB<br/>
//					 * or<br/>
//					 * BBBBBBB<br/>
//					 * B_WWW_B<br/>
//					 * BBBBBBB<br/>
//					 * or<br/>
//					 * BBBBWWW<br/>
//					 * B_WWB_B<br/>
//					 * 
//					 */
//
//					boolean singleExternalBlock = true;
//					for (Block tempBlock : blankBlock.getNeighborBlocks()) {
//						if (tempBlock.color == color) {
//							Block myBlock = tempBlock;
//							if (this != myBlock) {// 多个己方块围住气点.
//								singleExternalBlock = false;
//							}
//						} else {
//							Block enemyBlock = tempBlock;
//							if (enemyBlock.getBreaths() > 1) {
//								singleExternalBlock = false;
//							}
//							for (Block block : enemyBlock.getEnemyBlocks()) {
//								if (this != block) {// 多个己方块围住气点旁边的敌块..
//									singleExternalBlock = false;
//								}
//							}
//						}
//					}
//					if (singleExternalBlock == true)
//						eyes++;
//				}
//
//			}
//		}
//
//		return eyes >= 2;
//		// if (fakeEyes.isEmpty()) {
//		// return eyes >= 2;
//		// } else if (fakeEyes.size() == 1) {
//		// return false;
//		// } else if (fakeEyes.size() == 2) {
//		// if (friendBlock.size() == 2) {
//		// return true;
//		// } else {// TODO: 两头蛇之类，或者多块的情况。
//		// return false;
//		// }
//		//
//		// // if (fakeEyes.get(0).getDelta(fakeEyes.get(1))
//		// // .equals(Delta.DELTA_SHOULDER)) {
//		// // return true;
//		// // } else {// 两头蛇之类，或者多块的情况。
//		// // return false;
//		// // }
//		// } else {// 可能尚未定型
//		// return false;
//		// }
//	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public void clearLiveDead() {
		this.live = false;
		this.dead = false;
		this.priority = 0;
	}

	public boolean isLiveDeadMarked() {
		return liveDeadMarked;
	}

	public void setLiveDeadMarked(boolean liveDeadMarked) {
		this.liveDeadMarked = liveDeadMarked;
	}

	public Set<Block> getLiveFriend_canConn() {
		return liveFriend_canConn;
	}

	public void addLiveFriend_canConn(Block liveFriend) {
		if (this.liveFriend_canConn == null) {
			this.liveFriend_canConn = new HashSet<Block>();
		}
		this.liveFriend_canConn.add(liveFriend);
	}

	public Set<Block> getLiveFriend_connected() {
		return liveFriend_connected;
	}

	public void addLiveFriend_connected(Block liveFriend_connected) {
		if (this.liveFriend_connected == null) {
			this.liveFriend_connected = new HashSet<Block>();
		}
		this.liveFriend_connected.add(liveFriend_connected);
	}

	public boolean isLevelExtended() {
		return levelExtended;
	}

	public void setLevelExtended(boolean levelExtended) {
		this.levelExtended = levelExtended;
	}

	// public Delta getMinDistance(Block targetB){
	//
	// }

	Set<BlankBlock> liveWith = new HashSet<BlankBlock>();

	public Set<BlankBlock> getLiveWith() {
		return liveWith;
	}

	public void addLiveWith(BlankBlock liveWith) {
		this.liveWith.add(liveWith);
	}

	public void addLiveWithSet(Set<BlankBlock> liveWithSet) {
		this.liveWith.addAll(liveWithSet);
	}

	/**
	 * sometime we need more steps to cleanup the block due to special
	 * situation like 不入气.
	 * @param goBoard
	 * @return
	 */
	public int stepsToCleanUp(GoBoard goBoard) {
		int breath = this.getBreaths();
		
		
		return breath;

	}

}
