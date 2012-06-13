package eddie.wu.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * 记录棋局进行过程中的提子，棋块合并信息，方便悔棋，以及打谱后退时的处理。<br/>
 * TODO： 气块的合并没有处理（后退时）。
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class StepMemo implements Serializable {

	private Step step;

	/**
	 * 被提吃的棋块
	 */
	private Set<Block> eatenBlocks = new HashSet<Block>();

	/**
	 * 被合并的棋块
	 */
	private Set<Block> mergedBlocks = new HashSet<Block>();

	/**
	 * 被分裂的气块
	 */
	private BlankBlock dividedBlock;
	/**
	 * 被分裂出的气块
	 */
	private Set<BlankBlock> newBlankBlocks = new HashSet<BlankBlock>();

	/**
	 * total points after the move: help to check whether the state happens
	 * before.
	 */
	private short totalPoints;
	
	private BoardColorState colorState;

	/**
	 * 原先的空白气块.(将减少一子,可能会消失!)<br/>
	 * 消失的空白点块情况。有粘或者提劫之类。
	 */
	private BlankBlock originalBlankBlock;

	/**
	 * the prohibited move after this step. 处理打劫用的。
	 */
	private Point prohibittedPoint;

	public Set<BlankBlock> getNewBlankBlocks() {
		return newBlankBlocks;
	}

	public void addNewBlankBlock(BlankBlock newBlankBlock) {
		this.newBlankBlocks.add(newBlankBlock);
	}

	public BlankBlock getOriginalBlankBlock() {
		return originalBlankBlock;
	}

	public void setOriginalBlankBlock(BlankBlock eatenBlankBlock) {
		this.originalBlankBlock = eatenBlankBlock;
	}

	public BlankBlock getDividedBlock() {
		return dividedBlock;
	}

	public void setDividedBlock(BlankBlock block) {
		this.dividedBlock = block;
	}

	// public StepMemo(Point currentStepPoint, int color, short totalPoints,
	// Point prohibittedPoint) {
	//
	// this.step = new Step(currentStepPoint, color);
	// this.totalPoints = totalPoints;
	// this.prohibittedPoint = prohibittedPoint;
	// }

	public StepMemo(Point currentStepPoint, int color, short shoushu) {
		this.step = new Step(currentStepPoint, color, shoushu);
		// this.totalPoints = totalPoints;
	}

	public StepMemo(Point currentStepPoint, int color) {
		this.step = new Step(currentStepPoint, color);
	}

	// public StepMemo() {
	//
	// }

	/**
	 * @return Returns the color.
	 */
	public byte getColor() {
		return step.getColor();
	}

	/**
	 * @param color
	 *            The color to set.
	 */
	// public void setColor(int color) {
	// this.step.setColor(color);
	// }

	/**
	 * @return Returns the currentStepPoint.
	 */
	public Point getCurrentStepPoint() {
		return step.getPoint();
	}

	// /**
	// * @param currentStepPoint
	// * The currentStepPoint to set.
	// */
	// public void setCurrentStepPoint(Point currentStepPoint) {
	// this.step.setPoint(currentStepPoint);
	// }

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

	public boolean isGiveup() {
		return step.isGiveUp();
	}

	public Step getStep() {
		return step;
	}

	public void setStep(Step step) {
		this.step = step;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder(step.toString());
		sb.append("\r\n");
		if (!eatenBlocks.isEmpty()) {
			sb.append("下列块被提吃: ");
			for (Block block : eatenBlocks) {
				sb.append(block.getBehalfPoint());
				sb.append("\r\n");
			}
		}
		if (!mergedBlocks.isEmpty()) {
			sb.append("下列块被合并: ");
			for (Block block : mergedBlocks) {
				sb.append(block.getBehalfPoint());
				sb.append("\r\n");
			}
		}
		if (originalBlankBlock != null) {
			if (originalBlankBlock.getNumberOfPoint() == 0)
				sb.append("气块原先仅有一子,落子气块后消失: " + step.getPoint() + "\r\n");
			else {
				sb.append("气块原先减少一子: " + step.getPoint() + "\r\n");
			}
		} else if (dividedBlock != null) {
			sb.append("气块" + dividedBlock.getBehalfPoint() + "被分裂: \r\n");
		}
		return sb.toString();

		// return "StepMemo [step=" + step + ", totalPoints=" + totalPoints
		// + ", prohibittedPoint=" + prohibittedPoint + ", eatenBlocks="
		// + eatenBlocks + ", mergedBlocks=" + mergedBlocks
		// + ", eatenBlankBlock=" + eatenBlankBlock + ", dividedBlock="
		// + dividedBlock + "]";
	}

	public BoardColorState getColorState() {
		return colorState;
	}

	public void setColorState(BoardColorState colorState) {
		this.colorState = colorState;
		
	}

}