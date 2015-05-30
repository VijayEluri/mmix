package eddie.wu.domain;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import eddie.wu.domain.gift.Gift;

/**
 * 落子点周围相邻点的情况<br/>
 * 247 [95, 82, 25, 0, 2, 20, 20, 10, 2, 5, 4, 2, 25, 3, 1, 4, 1, 0]
 * 
 * @author Eddie
 * 
 */
public class NeighborState extends SimpleNeighborState {
	private Logger log = Logger.getLogger(NeighborState.class);
	/**
	 * 1 if even make a single real eye!
	 */
	private int eyes;

	// eating block which is already dead when we are still safe.
	private boolean eatingDead;	private int removeCapturing;
	/**
	 * details of sending gift.
	 */
	private Gift gift;

	/**
	 * prevent captured one point without increasing breath.
	 */
	// private boolean increaseBreath = true;
	private boolean attacking = false;
	private int connection = 0;
	private int increasedBreath = 0;

	private int breath;

	private Set<Block> capturingBlocks = new HashSet<Block>();

		public void addCapturingBlocks(Block capturingBlock) {
		capturingBlocks.add(capturingBlock);
	}

	@Override
	public String toString() {
		return super.toString() + "NeighborState [" + ", gift=" + isGifting() + "]";
	}

	

	public Gift getGift() {
		return gift;
	}

	public void setGift(Gift gift) {
		this.gift = gift;
	}

	public int getRemoveCapturing() {
		return removeCapturing;
	}

	public void setRemoveCapturing(int blocks) {
		this.removeCapturing = blocks;
		// remove capturing also prevent eating, they are equivalent.
		// adjust code in comparator!
		// if(blocks>=1) this.eating = true;

	}

	public Set<Block> getCapturingBlocks() {
		return capturingBlocks;
	}

	public int getIncreasedBreath() {
		return increasedBreath;
	}

	public void setIncreasedBreath(int increasedBreath) {
		this.increasedBreath = increasedBreath;
	}

	public boolean isEatingDead() {
		return eatingDead;
	}

	public void setEatingDead(boolean eatingDead) {
		this.eatingDead = eatingDead;
	}

	public int getBreath() {
		return breath;
	}

	public void setBreath(int breath) {
		this.breath = breath;
	}

	public int getEyes() {
		return eyes;
	}

	public void setEyes(int eyes) {
		this.eyes = eyes;
	}

	public int getMinEnemyBreath() {
		int min = 128;
		for (Block block : this.getEnemyBlocks()) {
			if (block.getBreaths() < min) {
				min = block.getBreaths();
			}
		}
		// if no enemy ==> enemy has long breath.
		return min;
	}

	public boolean isAttacking() {
		return attacking;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public int getConnection() {
		return connection;
	}

	public void setConnection(int connection) {
		this.connection = connection;
	}
}
