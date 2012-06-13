package eddie.wu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import eddie.wu.domain.comp.RowColumnComparator;

/**
 * Group is the collection of Block which are connected with or without
 * condition.
 * 
 * 
 * 
 * TODO:the connection detail between blocks. change 1 to block. use identity as
 * equal condition without overriding.
 * 
 * @author Eddie
 * 
 */
public class Group {
	private static final Logger log = Logger.getLogger(Group.class);
	private boolean live;

	Set<Point> eyeArea = new HashSet<Point>();

	/**
	 * loosely connected blocks
	 */
	Set<Block> blocks = new HashSet<Block>();

	Set<Point> blockBehalfs = new HashSet<Point>();
	/**
	 * the blocks in the group is at least loosely connected. (in another way,
	 * it is not broken yet). for the part which is already connected, we keep
	 * is in separate Set (strongly connected).
	 */
	Set<Block> connected = new HashSet<Block>();

	/**
	 * 编译器的局限性（将block.getGroup() 改为otherGroup，解决了一个bug）<br/>
	 * 重构并不能保证不改变程序的语义。也算是一个有趣的发现吧。
	 * 
	 * @param block
	 * @return null if no group is merged, corresponding group which is merged.
	 */
	public Group addBlock(Block block) {
		if (block.isGrouped()) {// merging
			Group otherGroup = block.getGroup();
			if (otherGroup == this)
				return null;
			blocks.addAll(otherGroup.getBlocks());
			blockBehalfs.addAll(otherGroup.blockBehalfs);
			for (Block block2 : otherGroup.getBlocks()) {
				block2.setGroup(this);
			}
			otherGroup.blocks.clear();
			otherGroup.blockBehalfs.clear();
			return otherGroup;
		} else {
			blocks.add(block);
			blockBehalfs.add(block.getBehalfPoint());
			block.setGroup(this);
			return null;
		}
	}

	public Set<Block> getBlocks() {
		return blocks;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(
				"Group with block representative: ");
		List<Point> list = BasicBlock.getBehalfs(blocks);
		sb.append(list.toString());

		// sb.append("\r\n");
		// list = BasicBlock.getPointList(blockBehalfs);
		// sb.append(list.toString());

		return sb.toString();
	}

	public Set<Point> getAllPoints() {
		Set<Point> points = new HashSet<Point>();
		for (Block block : blocks) {
			points.addAll(block.getPoints());
		}
		return points;
	}

	/**
	 * get the profile rectangle.
	 * 
	 * @return
	 */
	public Shape getShape() {
		return Shape.getShape(getAllPoints());
	}

	public int getColor() {
		if (blocks.isEmpty()) {
			if(log.isDebugEnabled()) log.debug("Group has no block");
			return Constant.BLANK;
		}
		return blocks.iterator().next().getColor();
	}

	public boolean isBlack() {

		return getColor() == Constant.BLACK;
	}

	public boolean isWhite() {

		return getColor() == Constant.WHITE;
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	Set<Point> breaths = new HashSet<Point>();

	public int getBreaths() {
		breaths.clear();
		for (Block block : blocks) {
			breaths.addAll(block.getBreathPoints());
		}
		// 每个连接减少二气,如小尖连接和假眼连接.
		return breaths.size() - (blocks.size() - 1) * 2;
	}

	public Set<Point> getEyeArea() {
		return eyeArea;
	}

	public void setEyeArea(Set<Point> eyeArea) {
		this.eyeArea = eyeArea;
	}

	public boolean hasWeakerGroup() {
		for (Block block : blocks) {
			for (Block enemyB : block.getEnemyBlocks()) {
				Group enemyGroup = enemyB.getGroup();
				if (enemyGroup.isLive())
					continue;
				if (enemyGroup.getEyeArea().size() < this.getEyeArea().size()
						&& enemyGroup.getBreaths() < this.getBreaths()) {
					return true;
				}
			}
		}
		return false;
	}

}
