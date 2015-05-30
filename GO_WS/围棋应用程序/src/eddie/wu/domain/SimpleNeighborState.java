package eddie.wu.domain;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class SimpleNeighborState {
	private Logger log = Logger.getLogger(SimpleNeighborState.class);
	// 落子点
	private Point original;
	// 落子点原先所在的气块
	private BlankBlock originalBlankBlock;
	// 己方棋子颜色
	private int friendColor;
	// 敌方棋子颜色
	private int enemyColor;

	// 相邻的敌方子数
	private int enemy;
	// 相邻的空白点数
	private int blank;
	// 相邻的己方子数
	private int friend;
	// 是否有效的落子点,无效点即自提禁着点.
	private boolean valid;

	// 是否打吃对方
	private boolean capturing;

	// 是否送吃
	private boolean gifting;

	/**
	 * 增加一子的原己方块真正增加的气数（落子点四周的气可能原来就是己方块的气）。记录用于悔棋。
	 */
	private Set<Point> addedBreaths = new HashSet<Point>();

	/**
	 * divided blank Blocks
	 */
	private boolean originalBlankBlockDivided = false;
	private boolean originalBlankBlockDisappear = false;
	/**
	 * 相邻己方子数未必等于相邻己方块数.因为多个相邻己方子可能属于同一块. <br/>
	 * friend may not equal to friendBlock.size() because some friend point may
	 * belong to same friend Block.<br/>
	 * it is also the merged blocks when there are more than 2 friend blocks.
	 */
	private Set<Block> friendBlocks = new HashSet<Block>();

	private Set<Point> blankPoints = new HashSet<Point>();

	/**
	 * 相邻敌子数未必等于相邻敌块数.因为多个相邻敌子可能属于同一块. <br/>
	 * enemy may not equal to enemyBlock.size() because some enemy point may
	 * belong to same enemy Block.
	 */
	private Set<Block> enemyBlocks = new HashSet<Block>();
	private Set<Block> eatenBlocks = new HashSet<Block>();
	/**
	 * 敌块可能不再和减少一子的气块相邻。记录用于悔棋。
	 */
	private Set<Block> disconnectedEnemyBlocks = new HashSet<Block>();
	/**
	 * 敌块可能因为落子和增加一子的原己方块相邻。记录用于悔棋。
	 */
	private Set<Block> connectedEnemyBlocks = new HashSet<Block>();

	public Set<Point> getBlankPoints() {
		return blankPoints;
	}

	public void addBlankPoint(Point blankPoint) {
		this.blankPoints.add(blankPoint);
	}

	public boolean isValid() {
		return valid;
	}

	public void setValid(boolean valid) {
		this.valid = valid;
	}

	public int getEnemy() {
		return enemy;
	}

	public void setEnemy(int enemy) {
		this.enemy = enemy;
	}

	public int getBlank() {
		return blank;
	}

	public void setBlank(int blank) {
		this.blank = blank;
	}

	/**
	 * 同色点的子数
	 * 
	 * @return
	 */
	public int getFriend() {
		int friendBlocks = this.getFriendBlocks().size();
		if (friend != friendBlocks) {
			// if(log.isEnabledFor(Level.WARN)) log.warn("friend =" + friend +
			// ";friendBlocks"
			// + friendBlocks);
			friend = friendBlocks;
		}
		return friend;
	}

	/**
	 * 根据同色点的块数，而不是子数来统计。有些同色点可能属于同一块。
	 * 
	 * @return
	 */
	public int getFriendBlockNumber() {
		int friendBlocks = this.getFriendBlocks().size();
		if (friend != friendBlocks) {
			if (log.isEnabledFor(Level.WARN))
				log.warn("Warning: friend = " + friend + "; friendBlocks = "
						+ friendBlocks);

		}
		return friendBlocks;
	}

	public void setFriend(int friend) {
		this.friend = friend;
	}

	public Set<Block> getFriendBlocks() {
		return friendBlocks;
	}

	public Block getUniqueFriendBlock() {
		return friendBlocks.iterator().next();
	}

	public void addFriendBlock(Block friendBlock) {
		this.friendBlocks.add(friendBlock);
	}

	/**
	 * 相邻的子数
	 * 
	 * @return
	 */
	public int getTotal() {
		int total = enemy + friend + blank;
		return total;
	}

	public Point getOriginal() {
		return original;
	}

	public void setOriginal(Point original) {
		this.original = original;
	}

	public int getFriendColor() {
		return friendColor;
	}

	public void setFriendColor(int friendColor) {
		this.friendColor = friendColor;
	}

	public int getEnemyColor() {
		return enemyColor;
	}

	public void setEnemyColor(int enemyColor) {
		this.enemyColor = enemyColor;
	}

	public BlankBlock getOriginalBlankBlock() {
		return originalBlankBlock;
	}

	public void setOriginalBlankBlock(BlankBlock originalBlankBlock) {
		this.originalBlankBlock = originalBlankBlock;
	}

	/**
	 * simple case has lower number.
	 * 
	 * @return
	 */
	public int getSelfSonteCaseNumber() {
		if (this.friendBlocks.size() == 0)
			return 0;
		else if (this.friendBlocks.size() == 1)
			return 1;
		else
			return 2;
	}

	// 是否提子
	private boolean eating;

	public int getEnemyStoneCaseNumber() {
		if (this.isEating()) {
			return 1;

		} else {
			return 0;
		}
	}

	public int getBlankStoneCaseNumber() {
		if (this.isOriginalBlankBlockDivided()) {
			return 2;
		} else if (this.isOriginalBlankBlockDisappear()) {
			return 1;
		} else {
			return 0;
		}
	}

	public Set<Block> getDisconnectedEnemyBlocks() {
		return disconnectedEnemyBlocks;
	}

	public void setDisconnectedEnemyBlocks(Set<Block> disconnectedEnemyBlocks) {
		this.disconnectedEnemyBlocks = disconnectedEnemyBlocks;
	}

	public Set<Block> getConnectedEnemyBlocks() {
		return connectedEnemyBlocks;
	}

	public void setConnectedEnemyBlocks(Set<Block> connectedEnemyBlocks) {
		this.connectedEnemyBlocks = connectedEnemyBlocks;
	}

	public boolean isOriginalBlankBlockDivided() {
		return originalBlankBlockDivided;
	}

	public void setOriginalBlankBlockDivided(boolean originalBlankBlockDivided) {
		this.originalBlankBlockDivided = originalBlankBlockDivided;
		if (this.originalBlankBlockDivided == true)
			this.originalBlankBlockDisappear = true;
	}

	public int getCaseNumber() {
		return 9 * this.getEnemyStoneCaseNumber() + 3
				* this.getBlankStoneCaseNumber()
				+ this.getSelfSonteCaseNumber();
	}

	public boolean isOriginalBlankBlockDisappear() {
		return originalBlankBlockDisappear;
	}

	public boolean isFriendBlockMerged() {
		return this.friendBlocks.size() >= 2;
	}

	public boolean isNewSinglePointBlock() {
		return this.friendBlocks.size() == 0;
	}

	public void setOriginalBlankBlockDisappear(
			boolean originalBlankBlockDisappear) {
		this.originalBlankBlockDisappear = originalBlankBlockDisappear;
	}

	// private Set<Block> dividedBlankBlocks = new HashSet<Block>();
	public boolean isEating() {
		return eating;
	}

	public void setEating(boolean capturing) {
		this.eating = capturing;
	}

	public Set<Block> getEnemyBlocks() {
		return enemyBlocks;
	}

	public void addEnemyBlock(Block enemyBlock) {
		this.enemyBlocks.add(enemyBlock);
	}

	public Set<Block> getEatenBlocks() {
		return eatenBlocks;
	}

	public void addEatenBlocks(Block eatenBlock) {
		eatenBlocks.add(eatenBlock);
	}

	public Set<Point> getAddedBreaths() {
		return addedBreaths;
	}

	public void setAddedBreaths(Set<Point> addedBreaths) {
		this.addedBreaths = addedBreaths;
	}

	public boolean isStandAlone() {
		return friend == 0;
	}

	public boolean isGifting() {
		return gifting;
	}

	public void setGifting(boolean gift) {
		this.gifting = gift;
	}

	public boolean isCapturing() {
		return capturing;
	}

	public void setCapturing(boolean capturing) {
		this.capturing = capturing;
	}

	@Override
	public String toString() {
		return "NeighborState [" + "original=" + original + ", friendColor="
				+ friendColor + ", enemyColor=" + enemyColor + ", enemy="
				+ enemy + ", blank=" + blank + ", friend=" + friend
				+ ", capturing=" + eating + ", valid=" + valid
				+ ", originalBlankBlockDivided=" + originalBlankBlockDivided
				+ ", originalBlankBlockDisappear="
				+ originalBlankBlockDisappear + ", blankPoints=" + blankPoints
				+ "]";
	}

}
