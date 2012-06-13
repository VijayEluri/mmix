package eddie.wu.search.global;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;

/**
 * 辅助实现深度优先搜索,用于小棋盘的全局搜索. 每一层有若干个状态
 * 
 * @author Eddie
 * 
 */
public class Level {
	int level;
	int color;

	private int highestExp;

	public Level(int level, int color) {
		this.level = level;
		this.color = color;
	}

	/**
	 * 当前层取max还是min
	 */
	private int whoseTurn = 0;
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

	List<Step> candidates;

	public int getWhoseTurn() {
		return whoseTurn;
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

	public int getCurrentIndex() {
		return currentIndex;
	}

	public void setCurrentIndex(int currentIndex) {
		this.currentIndex = currentIndex;
	}

	public int getNumberOfCandidate() {
		return candidates.size();
	}

	// public void setNumberOfCandidate(int numberOfCandidate) {
	// this.numberOfCandidate = numberOfCandidate;
	// }

	public int getTempBestScore() {
		return tempBestScore;
	}

	public void setTempBestScore(int tempBestScore) {
		this.tempBestScore = tempBestScore;
	}

	public List<Step> getCandidates() {
		return candidates;
	}

	// public void setCandidates(List<Point> candidates) {
	// this.candidates = candidates;
	// }

	/**
	 * Initial version, do not care the sequence.
	 * 
	 * @param candidates
	 * @param color
	 */
	public void setCandidates(Set<Point> candidates, int color) {

		this.candidates = new ArrayList<Step>(candidates.size());
		for (Point candidate : candidates) {
			Step step = new Step(candidate, color);
			this.candidates.add(step);
		}

	}

	public void setCandidates(List<Point> candidates, int color) {

		this.candidates = new ArrayList<Step>(candidates.size());
		for (Point candidate : candidates) {
			Step step = new Step(candidate, color, level + 1);
			this.candidates.add(step);
		}

	}

	/**
	 * 
	 * @return null if all candidates are handled.
	 */
	public Step getNextCandidate() {
		if (currentIndex < this.getNumberOfCandidate())
			return candidates.get(currentIndex++);
		else
			return null;
	}

	public void getScore(int score) {
		if (this.whoseTurn == Constant.MAX) {
			if (score > this.tempBestScore) {
				this.tempBestScore = score;
			}
		} else if (this.whoseTurn == Constant.MIN) {
			if (score < this.tempBestScore) {
				this.tempBestScore = score;
			}
		}
	}

	public void getChildScore(int score) {
		getScore(score);
	}

	public void getNeighborScore(int score) {
		getScore(score);

	}

	public int getLevel() {
		return level;
	}

	public int getColor() {
		return color;
	}

	public int getHighestExp() {
		return highestExp;
	}

	public void setHighestExp(int highestExp) {
		this.highestExp = highestExp;
	}

	/**
	 * means no need to consider the neighbors.
	 * 
	 * @return
	 */
	public boolean alreadyWin() {
		if (this.whoseTurn == Constant.MAX) {
			if (this.tempBestScore >= this.highestExp)
				return true;
		} else if (this.whoseTurn == Constant.MIN) {
			if ((0 - this.tempBestScore >= this.highestExp))
				return true;
		}
		return false;
	}
}
