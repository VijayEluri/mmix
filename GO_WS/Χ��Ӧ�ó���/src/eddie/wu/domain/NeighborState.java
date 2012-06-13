package eddie.wu.domain;

import java.util.HashSet;
import java.util.Set;

/**
 * 落子点周围相邻点的情况
 * 
 * @author Eddie
 * 
 */
public class NeighborState {
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
	// 是否提子
	private boolean capturing;
	// 是否有效的落子点,无效点即自提禁着点.
	private boolean valid;
	// 是否送吃
	private boolean gift;

	/**
	 * 相邻敌子数未必等于相邻敌块数.因为多个相邻敌子可能属于同一块. <br/>
	 * enemy may not equal to enemyBlock.size() because some enemy point may
	 * belong to same enemy Block.
	 */
	private Set<Block> enemyBlocks = new HashSet<Block>();
	/**
	 * 相邻己方子数未必等于相邻己方块数.因为多个相邻己方子可能属于同一块. <br/>
	 * friend may not equal to friendBlock.size() because some friend point may
	 * belong to same friend Block.
	 */
	private Set<Block> friendBlocks = new HashSet<Block>();

	private Set<Point> blankPoints = new HashSet<Point>();

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
	 * 根据同色点的块数，而不是子数来统计。有些同色点可能属于同一块。
	 * 
	 * @return
	 */
	public int getFriend() {
		int friendBlocks = this.getFriendBlocks().size();
		if (friend != friendBlocks) {
			System.out.println("friend =" + friend + ";friendBlocks"
					+ friendBlocks);
			friend = friendBlocks;
		}
		return friend;
	}

	public void setFriend(int friend) {
		this.friend = friend;
	}

	public boolean isCapturing() {
		return capturing;
	}

	public void setCapturing(boolean capturing) {
		this.capturing = capturing;
	}

	public Set<Block> getEnemyBlocks() {
		return enemyBlocks;
	}

	public void addEnemyBlock(Block enemyBlock) {
		this.enemyBlocks.add(enemyBlock);
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

	public boolean isGift() {
		return gift;
	}

	public void setGift(boolean gift) {
		this.gift = gift;
	}

}
