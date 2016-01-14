package eddie.wu.domain;

import static eddie.wu.domain.survive.SmallEye.BEND_THREE_STONE_EYE;
import static eddie.wu.domain.survive.SmallEye.FLOWER_FIVE_STONE_EYE;
import static eddie.wu.domain.survive.SmallEye.FLOWER_SIX_STONE_EYE;
import static eddie.wu.domain.survive.SmallEye.KNIFE_HANDLER_FIVE_STONE_EYE;
import static eddie.wu.domain.survive.SmallEye.MATRIX_SIX_STONE_EYE;
import static eddie.wu.domain.survive.SmallEye.RECTANGLT_FOUR_STONE_EYE;
import static eddie.wu.domain.survive.SmallEye.RULER_FIVE_STONE_EYE;
import static eddie.wu.domain.survive.SmallEye.RULER_FOUR_STONE_EYE;
import static eddie.wu.domain.survive.SmallEye.SINGLE_STONE_EYE;
import static eddie.wu.domain.survive.SmallEye.STRAIGHT_FIVE_STONE_EYE;
import static eddie.wu.domain.survive.SmallEye.STRAIGHT_FOUR_STONE_EYE;
import static eddie.wu.domain.survive.SmallEye.STRAIGHT_SIX_STONE_EYE;
import static eddie.wu.domain.survive.SmallEye.STRAIGHT_THREE_STONE_EYE;
import static eddie.wu.domain.survive.SmallEye.TRAP_FIVE_STONE_EYE;
import static eddie.wu.domain.survive.SmallEye.TWO_STONE_EYE;
import static eddie.wu.domain.survive.SmallEye.T_FIVE_STONE_EYE;
import static eddie.wu.domain.survive.SmallEye.T_FOUR_STONE_EYE;
import static eddie.wu.domain.survive.SmallEye.Z_FIVE_STONE_EYE;
import static eddie.wu.domain.survive.SmallEye.Z_FOUR_STONE_EYE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import eddie.wu.domain.comp.RowColumnComparator;

/**
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class BlankBlock extends BasicBlock {
	Logger log = Logger.getLogger(Block.class);
	/**
	 * 气块是否是眼位? <br/>
	 * true for blank block, false for normal black/white block.
	 */

	private boolean eyeBlock;

	/**
	 * 气块是何方的眼位 <br/>
	 * only meaningful when it is an eye block.
	 */
	private boolean blackEye;

	private Set<Block> blackBlocks = new HashSet<Block>();
	private Set<Block> whiteBlocks = new HashSet<Block>();

	public Set<Block> getBlackBlocks() {
		return blackBlocks;
	}

	/**
	 * generate on the fly.
	 * 
	 * @return
	 */
	public Set<Block> getNeighborBlocks() {
		this.isEyeBlock();// IMPORTANT: to get/guarantee blackEye initialized
		if (eyeBlock == true) {
			if (blackEye == true) {
				return blackBlocks;
			} else {
				return whiteBlocks;
			}
		} else {
			Set<Block> allBlocks = new HashSet<Block>();
			allBlocks.addAll(blackBlocks);
			allBlocks.addAll(whiteBlocks);
			return allBlocks;
		}
	}

	public void addBlackBlock(Block e) {
		blackBlocks.add(e);
	}

	public Set<Block> getWhiteBlocks() {
		return whiteBlocks;
	}

	public void addWhiteBlock(Block e) {
		whiteBlocks.add(e);
	}

	/**
	 * maintain one way relationship
	 * 
	 * @param e
	 */
	public void addNeighborBlock_oneWay(Block e) {
		if (e.getColor() == ColorUtil.BLACK)
			blackBlocks.add(e);
		else if (e.getColor() == ColorUtil.WHITE)
			whiteBlocks.add(e);
	}

	public void addNeighborBlock_twoWay(Block e) {
		if (e.getColor() == ColorUtil.BLACK)
			blackBlocks.add(e);
		else if (e.getColor() == ColorUtil.WHITE)
			whiteBlocks.add(e);
		e.getBreathBlocks().add(this);
	}

	/**
	 * it could also be used when back-warding.
	 * 
	 * @param eatenBlock
	 *            maybe not eaten
	 * 
	 * @return
	 */
	public boolean removeNeighborBlock_twoWay(Block eatenBlock) {
		eatenBlock.getBreathBlocks().remove(this);
		if (eatenBlock.getColor() == ColorUtil.BLACK)
			return blackBlocks.remove(eatenBlock);
		else if (eatenBlock.getColor() == ColorUtil.WHITE)
			return whiteBlocks.remove(eatenBlock);
		else
			return false;
	}

	/**
	 * 解除该空白块和相邻块的关系。
	 * 
	 * @return
	 */
	public void removeNeighborBlocks_twoWay() {
		for (Iterator<Block> iter = this.whiteBlocks.iterator(); iter.hasNext();) {
			Block block = iter.next();
			block.removeBreathBlock_oneWay(this);
			iter.remove();
		}
		for (Iterator<Block> iter = this.blackBlocks.iterator(); iter.hasNext();) {
			Block block = iter.next();
			block.removeBreathBlock_oneWay(this);
			iter.remove();
		}
	}

	/**
	 * 单向解除相邻关系.(dancing link)
	 */
	public boolean removeNeighborBlock_oneWay(Block eatenBlock) {
		if (eatenBlock.getColor() == ColorUtil.BLACK)
			return blackBlocks.remove(eatenBlock);
		else if (eatenBlock.getColor() == ColorUtil.WHITE)
			return whiteBlocks.remove(eatenBlock);
		else
			return false;
	}

	/**
	 * 自己仍指向相邻敌块；相邻棋块不再指向自己. <br/>
	 * 两个相邻块都消失的话，两个detach之后仍有足够信息恢复原样。
	 */
	public void dettachFromNeighbor() {
		for (Block block : this.blackBlocks) {
			boolean removed = block.getBreathBlocks().remove(this);
			if (log.isDebugEnabled()) {
				log.debug("breath Block " + this.getBehalfPoint()
						+ " dettach from  Block " + block.getBehalfPoint()
						+ " removed=" + removed);
			}
		}
		for (Block block : this.whiteBlocks) {
			boolean removed = block.getBreathBlocks().remove(this);
			if (log.isDebugEnabled()) {
				log.debug("breath Block " + this.getBehalfPoint()
						+ " dettach from  Block " + block.getBehalfPoint()
						+ " removed=" + removed);
			}
		}
	}

	/**
	 * 悔棋时恢复和相邻块的联系(根据原先保留的单向联系)<br/>
	 * (with link from this to neighbor, add link from neighbor to this）
	 * 
	 */
	public void attachToNeighbor() {
		for (Block block : blackBlocks) {
			if (block.getBreathBlocks().contains(this)) {
				if (log.isDebugEnabled()) {
					log.debug("Black Block " + block.getBehalfPoint()
							+ " already point to " + this.getBehalfPoint());
				}
			} else {
				boolean added = block.getBreathBlocks().add(this);
				if (log.isDebugEnabled()) {
					log.debug("Black Block " + block.getBehalfPoint()
							+ " repoint to " + this.getBehalfPoint()
							+ " added=" + added);
				}
			}
			// 气块中的点不都是邻块的气。
			// if (block.getBreathPoints().containsAll(this.allPoints)) {
			// if (log.isDebugEnabled()) {
			// log.debug("Black Block " + block.getBehalfPoint()
			// + " already contain the breath in"
			// + this.getBehalfPoint());
			// }
			//
			// } else {
			// block.getBreathPoints().addAll(this.allPoints);
			// if (log.isDebugEnabled()) {
			// log.debug("Black Block " + block.getBehalfPoint()
			// + " add the breaths" + this.allPoints + " in"
			// + this.getBehalfPoint());
			// }
			// }
		}

		for (Block block : whiteBlocks) {
			if (block.getBreathBlocks().contains(this)) {
				if (log.isDebugEnabled()) {
					log.debug("White Block " + block.getBehalfPoint()
							+ " already point to " + this.getBehalfPoint());
				}
			} else {
				boolean added = block.getBreathBlocks().add(this);
				if (log.isDebugEnabled()) {
					log.debug("Whte Block " + block.getBehalfPoint()
							+ " repoint to disappeared blanl block"
							+ this.getBehalfPoint() + " added=" + added);
				}
			}

			// if (block.getBreathPoints().containsAll(this.allPoints)) {
			// if (log.isDebugEnabled()) {
			// log.debug("White Block " + block.getBehalfPoint()
			// + " already contain the breath in"
			// + this.getBehalfPoint());
			// }
			//
			// } else {
			// block.getBreathPoints().addAll(this.allPoints);
			// if (log.isDebugEnabled()) {
			// log.debug("White Block " + block.getBehalfPoint()
			// + " add the breaths in" + this.getBehalfPoint());
			// }
			// }
		}
	}

	public BlankBlock() {
		super(ColorUtil.BLANK);
	}

	public boolean isEyeBlock() {
		/**
		 * allow the empty blank block. (some single point blank block will
		 * disappear after you play on it.
		 */
		if (this.allPoints.isEmpty()) {// TODO
			String message = "wrong blank block without points in it" + this;
			if (log.isDebugEnabled()) {

				log.debug(message);
			}
			// throw new RuntimeException(message);
			return false;
		}

		boolean eyeB = false;
		if (this.blackBlocks.isEmpty() && this.whiteBlocks.isEmpty()) {
			eyeB = false;
			blackEye = false;
		} else if (!this.blackBlocks.isEmpty() && !this.whiteBlocks.isEmpty()) {
			eyeB = false;
		} else {
			eyeB = true;
			if (blackBlocks.isEmpty() == false)
				blackEye = true;
			else {
				blackEye = false;
			}
		}
		if (eyeB != eyeBlock) {
			if (log.isDebugEnabled())
				log.debug("block" + this.getBehalfPoint() + " eyeBlock="
						+ eyeBlock + "; correct value is " + eyeB);
		}
		eyeBlock = eyeB;
		return eyeBlock;
	}

	/**
	 * //complete eye means it is surrounded by one block.
	 * 
	 * @return
	 */
	public boolean isCompleteBlockEye() {
		if (blackEye)
			return this.blackBlocks.size() == 1;
		else
			return this.whiteBlocks.size() == 1;
	}

	public boolean isBlackEye() {
		// return blackEye; Bug here, may not initialized yet.
		if (this.isEyeBlock()) {
			return this.blackEye == true;
		}else{
			return false;
		}
	}

	public boolean isLiveEye() {
		for (Block block : this.getNeighborBlocks()) {
			if (block.isAlreadyLive())
				continue;
			else
				return false;
		}
		return true;
	}

	/**
	 * only meaningful for eye block.
	 * 
	 * @return
	 */
	public int getEyeColor() {
		if (blackEye)
			return ColorUtil.BLACK;
		else
			return ColorUtil.WHITE;
	}

	public int getEyeEnemyColor() {
		if (blackEye)
			return ColorUtil.WHITE;
		else
			return ColorUtil.BLACK;
	}

	public boolean isSinglePointEye() {
		return getPoints().size() == 1;
	}

	public boolean setEyeBlock(boolean eyeBlock) {
		return this.eyeBlock = eyeBlock;
	}

	public void setBlackEye(boolean hasBlack) {
		this.blackEye = hasBlack;

	}

	/**
	 * Inactive BlankBlock@19ec4ed [color=0, eyeBlock=false, allPoints=1,
	 * representative=[2,1]neighbour black block = []neighbour white block =
	 * [[1,1],]
	 */
	@Override
	public String toString() {
		StringBuffer temp = new StringBuffer("\r\n" + this.getActiveString()
				+ "BlankBlock" + "@" + Integer.toHexString(this.hashCode())
				+ "[color=");
		temp.append(color);

		temp.append(", representative=");
		temp.append(this.getTopLeftPoint());
		List<Point> list = new ArrayList<Point>(allPoints.size());
		list.addAll(allPoints);
		Collections.sort(list, new RowColumnComparator());
		temp.append(", eyeBlock=" + this.eyeBlock + "]");

		temp.append(",\r\nallPoints=");
		temp.append(allPoints.size());
		if (allPoints.size() < 10) {
			temp.append(list.toString());
			temp.append("\r\n");
		}
		temp.append("neighbour black block = [");

		list = new ArrayList<Point>(allPoints.size());
		for (Block block : this.blackBlocks) {
			list.add(block.getBehalfPoint());
		}
		Collections.sort(list, new RowColumnComparator());
		for (Point point : list) {
			temp.append(point);
			temp.append(",");
		}

		temp.append("]");
		temp.append("\r\nneighbour white block = [");

		list = new ArrayList<Point>(allPoints.size());
		for (Block block : this.whiteBlocks) {
			list.add(block.getBehalfPoint());
		}
		Collections.sort(list, new RowColumnComparator());
		for (Point point : list) {
			temp.append(point);
			temp.append(",");
		}

		temp.append("]");
		return temp.toString();
	}

	/**
	 * 气块是否在角上。
	 * 
	 * @return
	 */
	public boolean isBreathCorner() {
		int count = 0;
		boolean includeCorner = false;
		for (Point point : allPoints) {
			if (point.isCornerEye()) {
				includeCorner = true;
				count++;
			} else if (point.isBorderEye()) {
				count++;
			}
		}
		return includeCorner && 2 * count > allPoints.size();
	}

	/**
	 * brute force coding of the eye name.<br/>
	 * better to use data to represent them.
	 * 
	 * @return
	 */
	public String getEyeName() {
		int size = this.allPoints.size();
		Shape shape = this.getShape();

		switch (size) {
		case 1: {
			return SINGLE_STONE_EYE;
		}
		case 2: {
			return TWO_STONE_EYE;
		}
		case 3: {
			if (shape.getMinDelta() == 1) {
				return STRAIGHT_THREE_STONE_EYE;
			} else {
				return BEND_THREE_STONE_EYE;
			}
		}
		case 4: {
			if (shape.getMinDelta() == 1) {
				return STRAIGHT_FOUR_STONE_EYE;
			} else if (shape.getMaxDelta() == 2) {
				return RECTANGLT_FOUR_STONE_EYE;
			} else {// ==3
				List<Point> shapeCorners = this.getShapeCorners(shape);
				int count = shapeCorners.size();
				if (count == 2) {
					if (shapeCorners.get(0).isSameline(shapeCorners.get(1))) {
						return T_FOUR_STONE_EYE;// 笠帽四
					} else {
						return Z_FOUR_STONE_EYE;
					}
				} else {// ==3
					return RULER_FOUR_STONE_EYE;
				}
			}
		}
		case 5: {
			if (shape.getMinDelta() == 1) {
				return STRAIGHT_FIVE_STONE_EYE;
			} else if (shape.getMinDelta() == 2) {
				List<Point> shapeCorners = this.getShapeCorners(shape);
				int count = shapeCorners.size();
				if (count == 3) {
					return KNIFE_HANDLER_FIVE_STONE_EYE;
				} else {// ==4
					return TRAP_FIVE_STONE_EYE;//
				}
			} else {// ==3
				List<Point> shapeCorners = this.getShapeCorners(shape);
				int count = shapeCorners.size();
				if (count == 0) {
					return FLOWER_FIVE_STONE_EYE;
				} else if (count == 2) {
					if (shapeCorners.get(0).isSameline(shapeCorners.get(1))) {
						return T_FIVE_STONE_EYE;
					} else {
						return Z_FIVE_STONE_EYE;
					}
				} else if (count == 3) {
					return RULER_FIVE_STONE_EYE;
				} else {
					return "梅花五之外的形状";
				}
			}
		}
		case 6: {
			if (shape.getMinDelta() == 1) {
				return STRAIGHT_SIX_STONE_EYE;
			} else if (shape.getMinDelta() == 2) {
				if (shape.getMaxDelta() == 3) {
					return MATRIX_SIX_STONE_EYE;
				} else if (shape.getMaxDelta() == 4) {
					return "板六之外的形状";
				} else {
					return "板六之外的形状2";
				}
			}
			// shape.getMinDelta() == 3
			List<Point> shapeCorners = this.getShapeCorners(shape);
			int count = shapeCorners.size();
			if (count == 3) {
				return FLOWER_SIX_STONE_EYE;
			} else {
				return "梅花六之外的形状";//
			}

		}
		case 7: {
			return "七子及以上眼块";
		}
		}
		return null;
	}

	// @Override
	// public boolean equals(Object o) {
	// if (this == o)
	// return true;
	// if (o instanceof BlankBlock) {
	// BlankBlock other = (BlankBlock) o;
	// if (other.getColor() == this.getColor()
	// && this.getAllPoints().equals(other.getAllPoints())
	// && this.getBlackBlockRepre().equals(
	// other.getBlackBlockRepre())
	// && this.getWhiteBlockRepre().equals(
	// other.getWhiteBlockRepre())) {
	// return true;
	// } else {
	// return false;
	// }
	// }
	// return false;
	// }
	public static boolean equals(BlankBlock a, BlankBlock other) {
		if (a == other)
			return true;

		return (other.getColor() == a.getColor()
				&& a.getPoints().equals(other.getPoints())
				&& a.getBlackBlockRepre().equals(other.getBlackBlockRepre()) && a
				.getWhiteBlockRepre().equals(other.getWhiteBlockRepre()));

	}

	/**
	 * side effort of using identity equality - cannot define Set equality with
	 * business meaning.
	 * 
	 * @param a
	 * @param other
	 * @return
	 */
	public static boolean equals(Set<BlankBlock> a, Set<BlankBlock> other) {
		if (a.size() != other.size())
			return false;
		return true;
		// TODO!

	}

	public Set<Point> getBlackBlockRepre() {
		Set<Point> points = new HashSet<Point>();
		for (Block block : this.blackBlocks) {
			points.add(block.getBehalfPoint());
		}
		return points;
	}

	public Set<Point> getWhiteBlockRepre() {
		Set<Point> points = new HashSet<Point>();
		for (Block block : this.whiteBlocks) {
			points.add(block.getBehalfPoint());
		}
		return points;
	}

	/**
	 * To prevent the has code change after the block content change. we use as
	 * less member as possible. to put it into a map, ensure the allPoints is
	 * correctly initialized and will not change during map operation.
	 */
	// public int hashCode() {
	// return this.allPoints.hashCode();
	// // return this.allPoints.hashCode() + this.allBreathPoints.hashCode() *
	// // 17;
	// }

	/**
	 * 当一个眼块形成时,这个眼块要进一步做出明确的眼位,应该优先考虑哪些点.
	 * 
	 * @return
	 */
	public List<Point> getCandidates() {

		List<Point> candidates = new ArrayList<Point>();
		Set<Point> allPoints = new HashSet<Point>();
		allPoints.addAll(getPoints());
		for (Block neighborB : getNeighborBlocks()) {
			allPoints.removeAll(neighborB.getBreathPoints());
		}
		if (allPoints.isEmpty() == false) {// 中心点容易成眼.如梅花五
			candidates.addAll(allPoints);
		}
		allPoints.clear();
		allPoints.addAll(getPoints());
		allPoints.removeAll(candidates);
		// sort the left candidate point
		for (Point point : allPoints) {

		}

		return candidates;
	}

	public int getNeighborMinimumBreath() {
		int minBreath = Integer.MAX_VALUE;
		for (Block block : this.blackBlocks) {
			if (block.getBreaths() < minBreath) {
				minBreath = block.getBreaths();
			}
		}

		for (Block block : this.whiteBlocks) {
			if (block.getBreaths() < minBreath) {
				minBreath = block.getBreaths();
			}
		}
		if (minBreath == Integer.MAX_VALUE) {
			throw new RuntimeException(
					"minbreath is not correct cause by wrong neighbor block data.");
		}
		return minBreath;
	}

	/**
	 * 两个棋块之间共享的气.前提是两个棋块共享气块.<br/>
	 * 有公气才有双活.必须双方都不活,才可能利用公气双活.一方活了,公气可以紧了,对方不活了.<br/>
	 * 举个 <br/>
	 * 
	 * @return
	 */
	public Set<Point> getSharedBreath(BasicGoBoard go, Block myBlock,
			Block enemyBlock) {
		Set<Point> sharedBreath = new HashSet<Point>();
		for (Point point : this.getPoints()) {
			int black = 0;
			int white = 0;
			for (Delta delta : Constant.ADJACENTS) {
				Point adjPoint = point.getNeighbour(delta);
				if (adjPoint == null)
					continue;
				if (go.getColor(adjPoint) == Constant.BLACK)
					black++;
				if (go.getColor(adjPoint) == Constant.WHITE)
					white++;
			}
			if (black >= 1 && white >= 1)
				sharedBreath.add(point);
		}
		return sharedBreath;
	}

	public Block getMinBreathNeighborBlock() {
		int min = 128;
		Block minBlock = null;
		for (Block block : this.blackBlocks) {
			if (block.getBreaths() < min) {
				minBlock = block;
				min = block.getBreaths();
			}
		}
		for (Block block : this.whiteBlocks) {
			if (block.getBreaths() < min) {
				minBlock = block;
				min = block.getBreaths();
			}
		}
		return minBlock;
	}

	public Block getBiggestNeighborBlock() {
		int max = 0;
		Block maxBlock = null;
		for (Block block : this.blackBlocks) {
			if (block.getNumberOfPoint() > max) {
				maxBlock = block;
				max = block.getNumberOfPoint();
			}
		}
		for (Block block : this.whiteBlocks) {
			if (block.getNumberOfPoint() > max) {
				maxBlock = block;
				max = block.getNumberOfPoint();
			}
		}
		return maxBlock;
	}

	/**
	 * ensure eye belongs to only one block
	 * 
	 * @return
	 */
	public Block getUniqueNeighborBlock() {
		return getNeighborBlocks().iterator().next();
	}

	public boolean isSingleBlockEye() {
		assert this.isEyeBlock() == true;
		return this.getNeighborBlocks().size() == 1;
	}

	public boolean isWhiteEye() {
		if (this.isEyeBlock()) {// init data
			return this.blackEye == false;
		} else {
			return false;
		}
	}

	public boolean isInitBlankBlock() {
		return this.whiteBlocks.isEmpty() && this.blackBlocks.isEmpty();
	}
}
