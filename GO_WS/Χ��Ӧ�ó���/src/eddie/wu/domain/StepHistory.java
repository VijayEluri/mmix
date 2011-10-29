/*
 * Created on 2005-4-21
 *


 */
package eddie.wu.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eddie.wu.linkedblock.ColorUtil;

/**
 * 
 * @author eddie
 * 
 *         keep the history of all steps.
 */
public class StepHistory implements java.io.Serializable {

	private short maxTotalPoints = 0;
	private short maxTotalPointsAfterBlack = 0;
	private short maxTotalPointsAfterWhite = 0;

	List<StepMemo> allSteps = new ArrayList<StepMemo>(256);

	public List<StepMemo> getAllSteps() {
		return allSteps;
	}

	public void setStep(short shoushu, byte row, byte column, byte color) {
		if (shoushu - 1 != allSteps.size()) {
			throw new RuntimeException("Internal error-setStep: shoushu=" + shoushu
					+ ", step history size=" + allSteps.size());
		}
		StepMemo stepMemo = new StepMemo();
		stepMemo.setColor(color);
		stepMemo.setCurrentStepPoint(Point.getPoint(row, column));
		allSteps.add(shoushu - 1, stepMemo);
	}
	
	public StepMemo removeStep(short shoushu) {
		if (shoushu  != allSteps.size()) {
			throw new RuntimeException("Internal error-removeStep: shoushu=" + shoushu
					+ ", step history size=" + allSteps.size());
		}		
		return allSteps.remove(shoushu-1);
	}

	/**
	 * ???
	 * 
	 * @param step
	 */
	public void addStep(StepMemo step) {
		allSteps.add(step);
		if (this.maxTotalPoints < step.getTotalPoints()) {
			this.maxTotalPoints = step.getTotalPoints();
		}
		if (step.getColor() == ColorUtil.BLACK) {
			if (this.maxTotalPointsAfterBlack < step.getTotalPoints()) {
				this.maxTotalPointsAfterBlack = step.getTotalPoints();
			}
		} else if (step.getColor() == ColorUtil.WHITE) {
			if (this.maxTotalPointsAfterWhite < step.getTotalPoints()) {
				this.maxTotalPointsAfterWhite = step.getTotalPoints();
			}
		}
	}

	/**
	 * @return Returns the maxTotalPoints.
	 */
	public short getMaxTotalPoints() {
		return maxTotalPoints;
	}

	/**
	 * @return Returns the maxTotalPointsAfterBlack.
	 */
	public short getMaxTotalPointsAfterBlack() {
		return maxTotalPointsAfterBlack;
	}

	/**
	 * @return Returns the maxTotalPointsAfterWhite.
	 */
	public short getMaxTotalPointsAfterWhite() {
		return maxTotalPointsAfterWhite;
	}

	public StepMemo getStep(int index) {
		return allSteps.get(index);
	}
}
