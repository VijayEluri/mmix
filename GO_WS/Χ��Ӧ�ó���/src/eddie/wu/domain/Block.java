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
	Logger log = Logger.getLogger(Block.class);
	/**
	 * 周围敌方的棋块.
	 */
	private Set<Block> enemyBlocks = new HashSet<Block>();

	/** 棋块气点集合 */
	private Set<Point> allBreathPoints = new HashSet<Point>();//
	/**
	 * 气点对应的气块集合<br/>
	 * record all the blocks the breath point belong to, note that not all the
	 * Points in those blocks are breath for current block.
	 */
	private Set<BlankBlock> breathBlocks = new HashSet<BlankBlock>();
	private boolean breathCalculated;

	// 不入气点的集合。死活计算用，自行初始化。
	private Set<Point> eyes = new HashSet<Point>();
	// 不入气点的集合,单个不能活棋，属于假眼。死活计算用，自行初始化。
	private Set<Point> fakeEyes = new HashSet<Point>();

	private Group group;
	/**
	 * used to hash, ensure the block has same hash during its life cycle. when
	 * block merges, get the top left point as the first point.
	 */
	private Point firstPoint;

	/**
	 * whether it is already part of group.
	 */
	public boolean isGrouped() {
		return group != null;
	}

	// public void setGrouped(boolean grouped) {
	// this.grouped = grouped;
	// }

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

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
	 * 己方块和并后,旧块和相邻块单向脱离关系.新块和旧块的敌块建立双向的相邻关系.
	 * 
	 * @param newBlock
	 */
	public void changeEnemyBlockTo(Block newBlock) {
		for (Block block : enemyBlocks) {
			// unlink old block (one-direction).
			block.removeEnemyBlock_oneWay(this);
			block.addEnemyBlock_twoWay(newBlock);
		}

	}

	public Block(int color) {
		super(color);
	}

	/**
	 * 单点的眼位
	 * 
	 * @param Point
	 */
	public void addEye(Point Point) {
		eyes.add(Point);
	}

	public Set<Point> getEyes() {
		return eyes;
	}

	public int numberOfEyes() {
		return eyes.size();
	}

	public void removeEye(Point Point) {
		fakeEyes.add(Point);
		eyes.remove(Point);
	}

	public void addEyes(Set<Point> eye) {
		eyes.addAll(eye);
	}

	public void addBreathPoint(BoardPoint point) {
		addBreathPoint(point.getPoint());
	}

	public void addBreathPoint(Point point) {
		// if (point.isNotValid()) {
		// throw new RuntimeException("point is not valid when add Breath"
		// + point);
		// }
		if (allBreathPoints.add(point)) {

		} else {// bug fix. it is possible to return false; because of shared
				// breath.
				// throw new
				// RuntimeException("failed when remove Breath Point"+point);
			log.info("return false when add Breath Point:" + point);
		}
	}

	public void removeBreathPoint(Point point) {
		// if (point.isNotValid()) {
		// throw new RuntimeException(
		// "point is not valid when remove Breath Point" + point);
		// }
		if (allBreathPoints.remove(point)) {

		} else {// bug fix. it is possible to return false; because of shared
				// breath.
				// throw new
				// RuntimeException("failed when remove Breath Point"+point);
			log.info("return false when remove Breath Point:" + point);
		}
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

	public int getBreaths() {
		return this.allBreathPoints.size();
	}

	public Point getLastBreath() {
		if (allBreathPoints.size() == 1)
			return allBreathPoints.iterator().next();
		if (log.isDebugEnabled())
			log.debug(allBreathPoints);
		throw new RuntimeException("allBreathPoints.size()!=1"
				+ allBreathPoints);
	}

	public void addEnemyBlock_oneWay(Block block) {
		this.enemyBlocks.add(block);
	}

	public void addEnemyBlock_twoWay(Block block) {
		this.enemyBlocks.add(block);
		block.enemyBlocks.add(this);
	}

	/**
	 * 解除自己和相邻敌块的关系(双向解除).
	 * 
	 * @param block
	 */
	public void removeEnemyBlocks_TwoWay() {
		for (Iterator<Block> iter = enemyBlocks.iterator(); iter.hasNext();) {
			Block enemyBlock = iter.next();
			enemyBlock.enemyBlocks.remove(this);
			iter.remove();
		}
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

	/**
	 * 解除自己和相邻敌块的关系(单向解除,自己不再指向相邻敌块).
	 * 
	 * @param block
	 */
	public void removeEnemyBlock_oneWay(Block block) {
		this.enemyBlocks.remove(block);
	}

	/**
	 * 解除自己和相邻气块的关系(单向解除,自己不再指向相邻气块).
	 * 
	 * @param blankBlock
	 */
	public void removeBreathBlock_oneWay(BlankBlock blankBlock) {
		this.breathBlocks.remove(blankBlock);
	}

	public String toString() {
		StringBuffer temp = new StringBuffer("\r\n[color=");
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
			list = new ArrayList<Point>(allBreathPoints.size());
			list.addAll(allBreathPoints);
			Collections.sort(list, new RowColumnComparator());
			temp.append(list);
			try {
				if (this.breathBlocks.isEmpty() == false) {
					temp.append(",\r\nbreathBlocks=[");
					for (BlankBlock tempBlock : this.breathBlocks) {
						temp.append(tempBlock.getTopLeftPoint());
						temp.append(", total=");
						temp.append(tempBlock.getNumberOfPoint());
						temp.append(",eyeBlock=" + tempBlock.isEyeBlock()
								+ ", ");
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

	/**
	 * @return Returns the allBreathPoints.
	 */
	public Set<Point> getBreathPoints() {
		return allBreathPoints;
	}

	/**
	 * @param allBreathPoints
	 *            The allBreathPoints to set.
	 */
	public void setBreathPoints(Set<Point> allBreathPoints) {
		this.allBreathPoints = allBreathPoints;
	}

	/**
	 * @return Returns the breathBlock.
	 */
	public Set<BlankBlock> getBreathBlocks() {
		return breathBlocks;
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
	}

	/**
	 * @param breathBlock
	 *            The breathBlock to set.
	 */
	public void setBreathBlocks(Set<BlankBlock> breathBlock) {
		this.breathBlocks = breathBlock;
	}

	/**
	 * @return Returns the enemyBlock.
	 */
	public Set<Block> getEnemyBlocks() {
		return enemyBlocks;
	}

	/**
	 * @param enemyBlock
	 *            The enemyBlock to set.
	 */
	public void setEnemyBlocks(Set<Block> enemyBlock) {
		this.enemyBlocks = enemyBlock;
	}

	public Object clone() {
		Block temp = null;
		try {
			temp = (Block) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		temp.allBreathPoints = (Set<Point>) ((HashSet<Point>) allBreathPoints)
				.clone();
		temp.allPoints = (Set) ((HashSet) allPoints).clone();
		temp.enemyBlocks = (Set) ((HashSet) enemyBlocks).clone();
		return temp;
	}

	public boolean isBreathCalculated() {
		return breathCalculated;
	}

	public void setBreathCalculated(boolean breathCalculated) {
		this.breathCalculated = breathCalculated;
	}

	/**
	 * 记录棋块周围的扩展点,包括拆一和拆二.<br/>
	 * 这个信息需要及时更新.
	 */
	private Set<Point> extendOne = new HashSet<Point>();
	private Set<Point> extendTwo = new HashSet<Point>();

	public Set<Point> getExtendOne() {
		return extendOne;
	}

	public Set<Point> getExtendTwo() {
		return extendTwo;
	}

	public void addExtendOnePoint(Point point) {
		extendOne.add(point);
	}

	public void addExtendTwoPoint(Point point) {
		extendTwo.add(point);
	}

	public void clearBreath() {
		this.allBreathPoints.clear();
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

	public void removeFromAllBreathBlock() {
		for (BlankBlock blankB : this.breathBlocks) {
			boolean removed = blankB.removeNeighborBlock_twoWay(this);
			if (removed == false)
				throw new RuntimeException("removed = false");
		}
	}

	public int getEnemyColor() {

		return ColorUtil.enemyColor(color);
	}

	/**
	 * should set it first before using it
	 */
	private transient boolean live;

	/**
	 * all the enemy is live,itself is not live yet. there is no chance to
	 * survive. state is unknown if both live and dead is false. which means
	 * fight is ongoing ( 对杀尚在进行中.)
	 */
	private transient boolean dead;

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isDead() {
		return dead;
	}

	public void setDead(boolean dead) {
		this.dead = dead;
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

	public Set<Point> getUniqueEyePoint() {
		if (this.getNumberOfEyeBlock() != 1)
			throw new RuntimeException("getNumberOfEyeBlock()!=1");
		return this.getBreathBlocks().iterator().next().getPoints();
	}

}
