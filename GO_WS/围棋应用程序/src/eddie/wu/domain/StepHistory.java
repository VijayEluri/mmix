/*
 * Created on 2005-4-21
 *


 */
package eddie.wu.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	private int virtualStepInLoop;// 打劫时寻劫材的虚手计数

	public List<StepMemo> getAllSteps() {
		return allSteps;
	}

	public StepHistory getCopy() {
		StepHistory his = new StepHistory();

		// Collections.copy(his.allSteps, steps);
		his.allSteps.addAll(this.allSteps);
		return his;

	}

	/**
	 * 
	 * @param shoushu
	 *            index from 1.
	 * @param point
	 * @param color
	 */
	public void setStep(int shoushu, Point point, int color) {
		if (shoushu - 1 != allSteps.size()) {
			throw new RuntimeException("Internal error-setStep: shoushu="
					+ shoushu + ", step history size=" + allSteps.size());
		}
		StepMemo stepMemo = new StepMemo(point, color);
		stepMemo.getStep().setIndex(shoushu);
		allSteps.add(shoushu - 1, stepMemo);

	}

	public StepMemo removeStep(int shoushu) {
		if (shoushu != allSteps.size()) {
			throw new RuntimeException("Internal error-removeStep: shoushu="
					+ shoushu + ", step history size=" + allSteps.size());
		}
		// 回退时删除记录的历史状态.
		colorStates.remove(this.getLastStep().getColorState());
		// remove if exist
		this.reachedDupStates.remove(this.getLastStep().getColorState());
		this.decreateVirtualStepInLoop();
		return allSteps.remove(shoushu - 1);
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

	public boolean areBothPass() {
		if (getAllSteps().size() >= 2) {
			if (getLastStep().isPass()) {
				if (getSecondLastStep().isPass()) {
					return true;
				}
			}
		}
		return false;
	}

	public StepMemo getLastStep() {
		if (allSteps.isEmpty())
			return null;
		return allSteps.get(allSteps.size() - 1);
	}

	public StepMemo getSecondLastStep() {
		if (allSteps.size() < 2)
			return null;
		return allSteps.get(allSteps.size() - 2);
	}

	Set<BoardColorState> colorStates = new HashSet<BoardColorState>();
	/**
	 * ever reached duplicated state. even it is not really a true loop. we will
	 * not treat its state as decided (history independent.)
	 */
	Set<BoardColorState> reachedDupStates = new HashSet<BoardColorState>();

	/**
	 * 
	 * @return 0 if no give up in history
	 */
	public int getLastGiveupColor() {
		int size = allSteps.size();
		for (int i = size - 1; i >= 0; i--) {
			StepMemo stepMemo = this.allSteps.get(i);
			if (stepMemo.isPass())
				return stepMemo.getColor();
		}
		return 0;
	}

	public boolean isDupReached(BoardColorState colorState) {
		return reachedDupStates.contains(colorState);
	}

	public boolean containState(BoardColorState colorState) {
		if (colorStates.contains(colorState)) {
			reachedDupStates.add(colorState);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 为了适应打劫的计算?????????<br/>
	 * 有让弃权一方有同型再现的权利<br/>
	 * 但是仅限于弃权是发生在同型再现所致的循环中<br/>
	 * (暂时不如此实现,因为仅在盘角曲五的特殊情况下有必要.)<br/>
	 * 目前简单地不允许实手致同型再现<br/>
	 * 
	 * @param colorState
	 * @return valid move?
	 */
	public boolean setColorState(BoardColorState colorState) {
		if (colorStates.contains(colorState)) {
			reachedDupStates.add(colorState);
			return false;
			// int color = this.getLastGiveupColor();
			// if (color == 0)
			// return false;
			// else if (ColorUtil.enemyColor(color) ==
			// colorState.getWhoseTurn()) {
			// return true;
			// } else {
			// return false;
			// }
		}
		colorStates.add(colorState);
		getLastStep().setColorState(colorState);
		return true;
	}

	public int getVirtualStepInLoop() {
		return virtualStepInLoop;
	}

	public void increateVirtualStepInLoop() {
		this.virtualStepInLoop++;
	}

	public void decreateVirtualStepInLoop() {
		this.virtualStepInLoop--;
	}

	public Set<BoardColorState> getColorStates() {
		return colorStates;
	}

	public boolean noRealStep() {
		if (this.getAllSteps().size() == 1
				&& this.getAllSteps().get(0).isPass())
			return true;
		else
			return false;
	}

	/**
	 * return last step with specified point
	 * 
	 * @param pointTemp
	 */
	public Step getLastStep(Point pointTemp) {
		for (int index = this.getAllSteps().size() - 1; index >= 0; index--) {
			if (this.getAllSteps().get(index).getStep().getPoint() == pointTemp) {
				return this.getAllSteps().get(index).getStep();
			}
		}
		return null;

	}

	public void printDupState() {
		// System.out.println("reached duplicate states:"+reachedDupStates.size());
		// for (BoardColorState state : this.reachedDupStates) {
		// System.out.println("duplicate states:");
		// System.out.println(state.getStateString());
		// }

	}
}
