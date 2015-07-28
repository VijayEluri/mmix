package eddie.wu.search.global;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Step;
import eddie.wu.manual.SearchNode;

/**
 * 辅助实现深度优先搜索,用于小棋盘的全局搜索. 每一层有若干个状态 <br/>
 * 每一层代表当前的状态,还有下一步轮谁走,下一步的若干候选点.<br/>
 * 搜索时展开一个候选点得到一个新的层.
 * 
 * @author Eddie
 * 
 */
public class SearchLevel {
	/**
	 * root's levelIndex = 0
	 */
	private int levelIndex;

	/**
	 * whose turn at current level/status.
	 */
	private int color;
	/**
	 * 当前层取max还是min<br/>
	 * black未必是max,比如死活计算时.
	 */
	private boolean max;

	/**
	 * TODO: highExp is needed only for root=max<br/>
	 * lowExp is needed only for root=min<br/>
	 */
	private int highExp;// for max
	private int lowExp; // for min

	private List<Candidate> candidates;
	private Iterator<Candidate> iterator;

	/**
	 * the node without play at current level's candidate <br/>
	 * just played previous level's candidate/step to reach current level.<br/>
	 * at level 0, node is a special root node;
	 */
	private SearchNode node;

	// private int numberOfCandidate = 0;
	/**
	 * 当前层临时的计算结果
	 */
	private int tempBestScore = 0;

	public SearchLevel(int level, int color) {
		this.levelIndex = level;
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
		if (isInitialized() == false)
			return false;
		if (max) {
			if (this.tempBestScore >= this.highExp)
				return true;
		} else {
			if (this.tempBestScore <= this.lowExp)
				return true;
		}
		return false;
	}

	public boolean isMax() {
		return max;
	}

	/**
	 * 下一层的结果已知，反馈给上一层。
	 * 
	 * @param score
	 */
	public void getChildScore(int score) {
		updateWithScore(score);
	}

	public int getColor() {
		return color;
	}

	public int getHighestExp() {
		return highExp;
	}

	public int getLevel() {
		return levelIndex;
	}

	public int getLowestExp() {
		return lowExp;
	}

	/**
	 * 在展开中的level的某个候选点的分数已知。
	 * 
	 * @param score
	 */
	public void getNeighborScore(int score, Step step) {
		updateWithScore(score);

	}

	/**
	 * 
	 * @return null if all candidates are handled.
	 */
	public Candidate getNextCandidate() {
		return iterator.next();
	}

	public boolean hasNext() {
		return iterator.hasNext();
	}

	public String getAllCanPoint() {
		StringBuilder sb = new StringBuilder();
		for (Candidate can : candidates) {
			sb.append(can.getStep().toNonSGFString());
		}
		return sb.toString();
	}

	/**
	 * 该层包含的局面数
	 */
	public int getNumberOfCandidate() {
		return candidates.size();
	}

	// private boolean maxFirst;//做活方先手提劫。
	// private int firstSuperiorScore;//先手提劫方劫材有利的结果
	// private int firstInteriorScore;//先手提劫方劫材不利的结果
	// private
	public void updateWithScore(int score) {
		// if (skip == true) {
		// firstSuperiorScore = score;// = score / 2;
		// }
		if (max) {
			if (score > this.tempBestScore) {
				this.tempBestScore = score;
			}
		} else {
			if (score < this.tempBestScore) {
				this.tempBestScore = score;
			}
		}

	}

	public int getTempBestScore() {
		return tempBestScore;
	}

	public void setCandidates(List<Candidate> candidates) {
		this.candidates = new ArrayList<Candidate>();
		this.candidates.addAll(candidates);
		iterator = candidates.iterator();
	}

	public void setMaxExp(int highestExp) {
		this.highExp = highestExp;
	}

	public void setMinExp(int lowestExp) {
		this.lowExp = lowestExp;
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
	public void setMax(boolean max) {
		this.max = max;
	}

	@Override
	public String toString() {
		if (max)
			return "Level [level=" + levelIndex + ", color="
					+ ColorUtil.getColorText(color) + ", whoseTurn="
					+ this.getWhoseTurnString() + ",  highestExp=" + highExp
					+ ", tempBestScore=" + tempBestScore + "]";
		else
			return "Level [level=" + levelIndex + ", whoseTurn="
					+ this.getWhoseTurnString() + ", lowestExp=" + lowExp
					+ ", tempBestScore=" + tempBestScore + "]";

	}

	public String getWhoseTurnString() {
		if (max)
			return "MAX" + "[" + ColorUtil.getColorText(color) + "]";
		else
			return "MIN" + "[" + ColorUtil.getColorText(color) + "]";

	}

	public SearchNode getNode() {
		return node;
	}

	public void setNode(SearchNode node) {
		this.node = node;
	}

	public boolean isInitialized() {
		return candidates != null;
	}

}
