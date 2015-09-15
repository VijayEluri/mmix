package eddie.wu.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

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
	 * 被分裂出的气块
	 */
	private Set<BlankBlock> newBlankBlocks = new HashSet<BlankBlock>();

	/**
	 * total points after the move: help to check whether the state happens
	 * before.
	 */
	private short totalPoints;

	private BoardColorState colorState;

	private SimpleNeighborState neighborState;

	/**
	 * 原先的空白气块.(将减少一子,可能会消失!)<br/>
	 * 消失的空白点块情况。有粘或者提劫之类。
	 */
	private BlankBlock originalBlankBlock;

	/**
	 * the prohibited move after this step. 处理打劫用的。
	 */
	private Point prohibittedPoint;
	private int prohibittedColor;

	private SymmetryResult symmetry;

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

	// public StepMemo(Point currentStepPoint, int color, short totalPoints,
	// Point prohibittedPoint) {
	//
	// this.step = new Step(currentStepPoint, color);
	// this.totalPoints = totalPoints;
	// this.prohibittedPoint = prohibittedPoint;
	// }

	public StepMemo(Point currentStepPoint, int color, int shoushu) {
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
	public int getColor() {
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

	public boolean isBlockEaten(Point target) {
		if (eatenBlocks.isEmpty())
			return false;
		// Set<Point> eatenP= new HashSet<Point>();
		for (Block block : eatenBlocks) {
			if (block.getPoints().contains(target)) {
				return true;
			}
		}
		return false;
	}

	public void addEatenBlock(Block block) {
		eatenBlocks.add(block);
	}

	public void addMergedBlock(Block block) {
		mergedBlocks.add(block);
	}

	public boolean isPass() {
		return step.isPass();
	}

	public Step getStep() {
		return step;
	}

	public void setStep(Step step) {
		this.step = step;
	}
	
	public String toSimpleString(){
		return step.toString();
	}

	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder(step.toString());
		sb.append("\r\n");
		if (step.isPass())
			return sb.toString();
		
		if (neighborState.isEating()) {
			TestCase.assertFalse(eatenBlocks.isEmpty());
			sb.append("下列块被提吃: ");
			for (Block block : eatenBlocks) {
				sb.append(block.getBehalfPoint());
				sb.append(", ");
			}
			sb.append("\r\n");
		}
		if (neighborState.isFriendBlockMerged()) {
			TestCase.assertFalse(mergedBlocks.isEmpty());
			sb.append("下列块被合并: ");
			for (Block block : mergedBlocks) {
				sb.append(block.getBehalfPoint());
				sb.append(", ");
			}
			sb.append("\r\n");
		}
		if (neighborState.isOriginalBlankBlockDivided()) {
			TestCase.assertNotNull(originalBlankBlock);
			sb.append("气块" + this.originalBlankBlock.getBehalfPoint()
					+ "被分裂: \r\n");

		} else if (neighborState.isOriginalBlankBlockDisappear()) {
			sb.append("气块原先仅有一子,落子气块后消失: " + step.getPoint() + "\r\n");
		} else {
			sb.append("气块原先减少一子: " + step.getPoint() + "\r\n");
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

	public SimpleNeighborState getNeighborState() {
		return neighborState;
	}

	public void setNeighborState(SimpleNeighborState neighborState) {
		this.neighborState = neighborState;
	}

	public int getProhibittedColor() {
		return prohibittedColor;
	}

	public void setProhibittedColor(int prohibittedColor) {
		this.prohibittedColor = prohibittedColor;
	}

	public SymmetryResult getSymmetry() {
		return symmetry;
	}

	public void setSymmetry(SymmetryResult symmetry) {
		this.symmetry = symmetry;
	}

}