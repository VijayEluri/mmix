package eddie.wu.search.global;

import java.util.ArrayList;
import java.util.List;

import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Step;

/**
 * 辅助实现深度优先搜索,用于小棋盘的全局搜索. 每一层有若干个状态
 * 
 * @author Eddie
 * 
 */
public class Level {
	private int level;// root's level = 0
	private int color;// whose turn at current level/status.
	/**
	 * 当前层取max还是min
	 */
	private int whoseTurn = 0;

	/**
	 * 当前是否计算一个打劫脱先的变化（score计算不同。）
	 */
	// boolean skip;
	private int highestExp;// for max
	private int lowestExp; // for min

	/**
	 * 待处理局面的索引号.(=已经处理的局面数)
	 */
	private int currentIndex = 0;
	/**
	 * 该层包含的局面数
	 */
	// private int numberOfCandidate = 0;
	/**
	 * 当前层临时的计算结果
	 */
	private int tempBestScore = 0;

	/**
	 * 当前层临时的计算结果对应的走法
	 */
	private List<Step> tempBestSteps = new ArrayList<Step>();

	List<Candidate> candidates;

	public Level(int level, int color) {
		this.level = level;
		this.color = color;
	}

	/**
	 * means no need to consider the neighbors.
	 * 在当前局面下，前面的选择已经达到最佳效果，没有必要考虑其他的候选棋步了。<br/>
	 * 现在要求对称的计分系统。<br/>
	 * 
	 * @return
	 */
	public boolean alreadyWin() {
		if (this.whoseTurn == Constant.MAX) {
			if (this.tempBestScore >= this.highestExp)
				return true;
		} else if (this.whoseTurn == Constant.MIN) {
			if (this.tempBestScore <= this.lowestExp)
				return true;
		} else {
			throw new RuntimeException("whoseTurn = " + whoseTurn);
		}
		return false;
	}

	public List<Candidate> getCandidates() {
		return candidates;
	}

	/**
	 * 下一层的结果已知，反馈给上一层。
	 * 
	 * @param score
	 */
	public void getChildScore(int score, List<Step> steps) {
		List<Step> steps2 = new ArrayList<Step>();
		steps2.add(this.getCurrentCandidate().getStep());
		steps2.addAll(steps);
		getScore(score, steps2);
	}

	public int getColor() {
		return color;
	}

	public int getCurrentIndex() {
		return currentIndex;
	}

	// public void setNumberOfCandidate(int numberOfCandidate) {
	// this.numberOfCandidate = numberOfCandidate;
	// }

	public int getHighestExp() {
		return highestExp;
	}

	public int getLevel() {
		return level;
	}

	public int getLowestExp() {
		return lowestExp;
	}

	// public void setCandidates(List<Point> candidates) {
	// this.candidates = candidates;
	// }

	/**
	 * 在展开中的level的某个候选点的分数已知。
	 * @param score
	 */
	public void getNeighborScore(int score) {
		List<Step> steps = new ArrayList<Step>();
		steps.add(this.getCurrentCandidate().getStep());
		getScore(score, steps);

	}

	/**
	 * 
	 * @return null if all candidates are handled.
	 */
	public Candidate getNextCandidate() {
		if (currentIndex < this.getNumberOfCandidate())
			return candidates.get(currentIndex++);
		else
			return null;
	}

	public Candidate getCurrentCandidate() {
		if (currentIndex - 1 >= 0
				&& currentIndex - 1 < this.getNumberOfCandidate())
			return candidates.get(currentIndex - 1);
		else
			return null;
	}

	public int getNumberOfCandidate() {
		return candidates.size();
	}

	// private boolean maxFirst;//做活方先手提劫。
	// private int firstSuperiorScore;//先手提劫方劫材有利的结果
	// private int firstInteriorScore;//先手提劫方劫材不利的结果
	// private
	public void getScore(int score, List<Step> steps) {
		// if (skip == true) {
		// firstSuperiorScore = score;// = score / 2;
		// }
		if (this.whoseTurn == Constant.MAX) {
			if (score > this.tempBestScore) {
				this.tempBestScore = score;
				this.tempBestSteps.clear();
				this.tempBestSteps.addAll(steps);
			}
		} else if (this.whoseTurn == Constant.MIN) {
			if (score < this.tempBestScore) {
				this.tempBestScore = score;
				this.tempBestSteps.clear();
				this.tempBestSteps.addAll(steps);
			}
		}
		
	}

	public int getTempBestScore() {
		return tempBestScore;
	}

	public int getWhoseTurn() {
		return whoseTurn;
	}

	/**
	 * Initial version, do not care the sequence.
	 * 
	 * @param candidates
	 * @param color
	 */
	// public void setCandidates(Set<Point> candidates, int color) {
	//
	// this.candidates = new ArrayList<Step>(candidates.size());
	// for (Point candidate : candidates) {
	// Step step = new Step(candidate, color);
	// this.candidates.add(step);
	// }
	//
	// }

	public void setCandidates(List<Candidate> candidates, int color) {

		this.candidates = candidates;
		// for (Point candidate : candidates) {
		// Step step = new Step(candidate, color, level + 1);
		// this.candidates.add(step);
		// }

	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public void setHighestExp(int highestExp) {
		this.highestExp = highestExp;
	}

	public void setLowestExp(int lowestExp) {
		this.lowestExp = lowestExp;
	}

	public void setTempBestScore(int tempBestScore) {
		this.tempBestScore = tempBestScore;
	}

	/**
	 * 从上一个状态(level), max 即黑方下, min即白方下; max指的是取所有候选点的max.
	 * 
	 * 候选点若需要继续扩展,则所致的状态在下一个level.一个level有一个隐式的当前状态.
	 * 
	 * @param whoseTurn
	 */
	public void setWhoseTurn(int whoseTurn) {
		this.whoseTurn = whoseTurn;
	}

	// public boolean isSkip() {
	// return skip;
	// }
	//
	// public void setSkip(boolean skip) {
	// this.skip = skip;
	// }

	@Override
	public String toString() {
		if (whoseTurn == Constant.MAX)
			return "Level [level=" + level + ", color="
					+ ColorUtil.getColorText(color) + ", whoseTurn="
					+ this.getWhoseTurnString() + ",  highestExp=" + highestExp
					+ ", currentIndex=" + currentIndex + ", tempBestScore="
					+ tempBestScore + "]";
		else if (whoseTurn == Constant.MIN)
			return "Level [level=" + level + ", color="
					+ ColorUtil.getColorText(color) + ", whoseTurn="
					+ this.getWhoseTurnString() + ", lowestExp=" + lowestExp
					+ ", currentIndex=" + currentIndex + ", tempBestScore="
					+ tempBestScore + "]";
		return null;

	}

	private String getWhoseTurnString() {
		if (whoseTurn == Constant.MAX)
			return "MAX";
		else if (whoseTurn == Constant.MIN)
			return "MIN";
		return null;
	}

	public List<Step> getTempBestSteps() {
		return tempBestSteps;
	}

}
