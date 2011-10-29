package eddie.wu.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
/**
 * 记录棋局进行过程中的提子，棋块合并信息，方便悔棋，以及打谱后退时的处理。
 * TODO： 气块的合并没有处理（后退时）。
 * @author wueddie-wym-wrz
 *
 */
public class StepMemo implements Serializable{
	private Point currentStepPoint;
	private byte color;

	/*
	 * total points after the move: help to check whether the state happens
	 * before.
	 */
	private short totalPoints;

	/*
	 * the prohibited move after this step. 处理打劫用的。
	 */
	private Point prohibittedPoint;
	/**
	 * 被提吃 的棋块  
	 */
	private Set<Block> eatenBlocks = new HashSet<Block>();
	/**
	 * 被合并的棋块
	 */
	
	private Set<Block> mergedBlocks = new HashSet<Block>();
	/**
	 * 被分裂出的气块
	 */
	private Set<Block> dividedBlocks = new HashSet<Block>();

	//private Block originalBlankBlock;
	
	public Set<Block> getDividedBlocks() {
		return dividedBlocks;
	}

	public void addDividedBlocks(Block block) {
		this.dividedBlocks.add(block);
	}

	public StepMemo(Point currentStepPoint, byte color, short totalPoints,
			Point prohibittedPoint) {
		this.currentStepPoint = currentStepPoint;
		this.color = color;
		this.totalPoints = totalPoints;
		this.prohibittedPoint = prohibittedPoint;
	}

	public StepMemo(Point currentStepPoint, byte color, short totalPoints) {
		this.currentStepPoint = currentStepPoint;
		this.color = color;
		this.totalPoints = totalPoints;
	}

	public StepMemo(Point currentStepPoint, byte color) {
		this.currentStepPoint = currentStepPoint;
		this.color = color;
	}

	public StepMemo() {

	}

	/**
	 * @return Returns the color.
	 */
	public byte getColor() {
		return color;
	}

	/**
	 * @param color
	 *            The color to set.
	 */
	public void setColor(byte color) {
		this.color = color;
	}

	/**
	 * @return Returns the currentStepPoint.
	 */
	public Point getCurrentStepPoint() {
		return currentStepPoint;
	}

	/**
	 * @param currentStepPoint
	 *            The currentStepPoint to set.
	 */
	public void setCurrentStepPoint(Point currentStepPoint) {
		this.currentStepPoint = currentStepPoint;
	}

	/**
	 * @return Returns the prohibittedPoint.
	 */
	public Point getProhibittedPoint() {
		return prohibittedPoint;
	}

	/**
	 * @param prohibittedPoint
	 *            The prohibittedPoint to set.
	 */
	public void setProhibittedPoint(Point prohibittedPoint) {
		this.prohibittedPoint = prohibittedPoint;
	}

	/**
	 * @return Returns the totalPoints.
	 */
	public short getTotalPoints() {
		return totalPoints;
	}

	/**
	 * @param totalPoints
	 *            The totalPoints to set.
	 */
	public void setTotalPoints(short totalPoints) {
		this.totalPoints = totalPoints;
	}

	public Set<Block> getMergedBlocks() {
		return mergedBlocks;
	}

	public Set<Block> getEatenBlocks() {
		return eatenBlocks;
	}

	public void addEatenBlock(Block block) {
		eatenBlocks.add(block);
	}

	public void addMergedBlock(Block block) {
		mergedBlocks.add(block);
	}
}