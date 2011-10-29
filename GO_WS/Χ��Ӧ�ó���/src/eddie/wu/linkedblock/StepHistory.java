/*
 * Created on 2005-4-21
 *


 */
package eddie.wu.linkedblock;

import java.util.ArrayList;
import java.util.List;

import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Point;

/**
 * // 限制之一：棋谱手数不能超过512手。
 * 
 * @author eddie
 * 
 *         keep the history of all steps.
 */
public class StepHistory implements java.io.Serializable {

	public byte[][] hui = new byte[512][5];
	private short maxTotalPoints = 0;
	private short maxTotalPointsAfterBlack = 0;
	private short maxTotalPointsAfterWhite = 0;

	List<Step> allSteps = new ArrayList<Step>(256);

	public void addStep(short shoushu, byte row, byte column) {
		hui[shoushu][0] = row;
		hui[shoushu][1] = column;

	}

	public void addStep(Step step) {
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
}

class Step {
	private Point currentStepPoint;

	private byte color;

	private short totalPoints;

	private Point prohibittedPoint;

	public Step(Point currentStepPoint, byte color, short totalPoints,
			Point prohibittedPoint) {
		this.currentStepPoint = currentStepPoint;
		this.color = color;
		this.totalPoints = totalPoints;
		this.prohibittedPoint = prohibittedPoint;
	}

	public Step(Point currentStepPoint, byte color, short totalPoints) {
		this.currentStepPoint = currentStepPoint;
		this.color = color;
		this.totalPoints = totalPoints;
		this.prohibittedPoint = null;
	}

	public Step() {

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
}