package eddie.wu.search.global;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.sun.org.glassfish.external.statistics.Stats;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Step;
import eddie.wu.manual.SearchNode;
import eddie.wu.search.ScopeScore;

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
	 * prevStep is played to reach current state/level.
	 */
	private Step prevStep;
	/**
	 * whose turn at current level/status.
	 */
	private int whoseTurn;
	/**
	 * 当前层取max还是min<br/>
	 * black未必是max,比如死活计算时.
	 */
	private boolean max;

	/**
	 * TODO: highExp is needed only for root=max<br/>
	 */
	private int expScore;// for max

	/**
	 * whether the score is achieved by both pass!
	 */
	private boolean scoreByBothPass;

	public boolean isScoreByBothPass() {
		return scoreByBothPass;
	}

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
	private int tempBestScore = Constant.UNKOWN;

	/**
	 * whether current state may directly reach duplicate states (blocked);
	 * record them for future knowledge.
	 */
	private boolean reachingDup = false;
	/**
	 * if reachingDup = false; indirect dup. states are collected in
	 * inDirectDupStates;
	 */
	private Set<BoardColorState> indirectDupStates = null;

	/**
	 * directDupState saves directly reaching duplicate states if reachingDup =
	 * true;
	 */
	private Set<BoardColorState> directDupStates = null;

	/**
	 * it save directly reaching duplicate states if reachingDup = true;
	 * otherwise, it just collects
	 */
	// private BoardColorState directDupState = null;

	public boolean isReachingDup() {
		return reachingDup;
	}

	public boolean isReachingDupIndirectly() {
		return this.indirectDupStates != null
				&& this.indirectDupStates.isEmpty() == false;
	}

	public void setReachingDup(boolean reachDup) {
		this.reachingDup = reachDup;
	}

	public Set<BoardColorState> getDirectDupStates() {
		return this.directDupStates;
	}

	/**
	 * we can reuse history dependent state only if there is only one state.
	 * 
	 * @return
	 */
	public BoardColorState getUniqueDupState() {
		Set<BoardColorState> dupStates = this.getAllDupStates();
		if (dupStates.size() != 1)
			return null;
		else
			return dupStates.iterator().next();
	}

	public void addDirectDupState(BoardColorState dupState) {
		if (directDupStates == null) { // lazy!
			directDupStates = new HashSet<BoardColorState>();
		}
		this.directDupStates.add(dupState);
		this.reachingDup = true;
	}

	/**
	 * just collecting those data, we will not reuse history dependent state if
	 * it depends on more than one state
	 * 
	 * @param dupStates
	 */
	public void addDirectDupStates(Set<BoardColorState> dupStates) {
		if (this.directDupStates == null) { // lazy!
			this.directDupStates = new HashSet<BoardColorState>();
		}
		this.directDupStates.addAll(dupStates);
	}

	public void addIndirectDupStates(Set<BoardColorState> dupStates) {
		if (this.indirectDupStates == null) { // lazy!
			this.indirectDupStates = new HashSet<BoardColorState>();
		}
		this.indirectDupStates.addAll(dupStates);
	}

	public void addIndirectDupStates(BoardColorState dupState) {
		if (this.indirectDupStates == null) { // lazy!
			this.indirectDupStates = new HashSet<BoardColorState>();
		}
		this.indirectDupStates.add(dupState);
	}

	public Set<BoardColorState> getIndirectDupStates() {
		return this.indirectDupStates;
	}

	public Set<BoardColorState> getAllDupStates() {
		Set<BoardColorState> states = new HashSet<BoardColorState>();
		if (this.reachingDup) {
			states.addAll(this.directDupStates);
		} else if (this.indirectDupStates != null
				&& indirectDupStates.isEmpty() == false) {
			states.addAll(this.indirectDupStates);
		}
		return states;
	}

	public SearchLevel(int levelIndex, int whoseTurn, Step prevStep) {
		this.levelIndex = levelIndex;
		this.whoseTurn = whoseTurn;
		this.prevStep = prevStep;
	}

	public Step getPrevStep() {
		return prevStep;
	}

	public boolean isPrevStepPass() {
		return prevStep == null;
	}

	/**
	 * means no need to consider the neighbors.
	 * 在当前局面下，前面的选择已经达到最佳效果，没有必要考虑其他的候选棋步了。<br/>
	 * 现在要求对称的计分系统。<br/>
	 * 
	 * @return
	 */
	public boolean alreadyWin() {
		// if (isInitialized() == false)
		// return false;
		if (this.tempBestScore == Constant.UNKOWN)
			return false;
		if (max) {
			if (this.tempBestScore >= this.expScore) {
				unknownChild = false;// win overwrite unknown child
				return true;
			}
		} else {
			if (this.tempBestScore <= this.expScore) {
				unknownChild = false;
				return true;
			}
		}
		return false;
	}

	public boolean alreadyLose() {
		if (this.isInitialized() ){
			if(this.iterator.hasNext() == false) return true;
		}else{
			//TODO: may remove tempScore
			
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

	public int getWhoseTurn() {
		return whoseTurn;
	}

	public int getLevel() {
		return levelIndex;
	}

	public int getExpScore() {
		return expScore;
	}

	/**
	 * 在展开中的level的某个候选点的分数已知。
	 * 
	 * @param score
	 */
	public void getNeighborScore(int score, boolean bothPass) {
		updateWithScore(score, bothPass);

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

	private boolean unknownChild;

	public boolean hasUnknownChild() {
		return unknownChild;
	}

	public void setUnknownChild() {
		this.unknownChild = true;
	}

	// private boolean maxFirst;//做活方先手提劫。
	// private int firstSuperiorScore;//先手提劫方劫材有利的结果
	// private int firstInteriorScore;//先手提劫方劫材不利的结果
	// private
	public void updateWithScore(int score) {
		updateWithScore(score, false);
	}

	public void updateWithScore(int score, boolean bothPass) {
		// if (skip == true) {
		// firstSuperiorScore = score;// = score / 2;
		// }
		// ignore unknown score and its branch
		if (score == Constant.UNKOWN) {
			unknownChild = true;
			return;
		}
		if (this.tempBestScore == Constant.UNKOWN) {
			this.tempBestScore = score; // first achievable result.
			this.scoreByBothPass = bothPass;
		}
		if (max) {
			if (score > this.tempBestScore) {
				this.tempBestScore = score;
				this.scoreByBothPass = bothPass;
			}
		} else {
			if (score < this.tempBestScore) {
				this.tempBestScore = score;
				this.scoreByBothPass = bothPass;
			}
		}

	}

	/**
	 * when apply terminal state's known score (maybe not accurate) to parent
	 * level. parent state level v.s. child state's score!
	 * 
	 * @param score
	 * @return
	 */
	public SearchLevel updateWithFuzzyScore(ScopeScore score) {
		// score maybe still initial, e.g. unknown score due to limit.
		// if (score.isInit())
		// return this;
		// boolean win = false;
		assert this.alreadyWin() == false;
		// if (score.isInit()) {
		// throw new RuntimeException("is init");
		// }
		if (score.isExact()) {
			if (max) {
				if (score.getLow() > this.tempBestScore) {
					this.tempBestScore = score.getLow();
				}
			} else {
				if (score.getLow() < this.tempBestScore) {
					this.tempBestScore = score.getLow();
				}
			}
		} else if (max) {// parent is max// child is min.
			if (score.getLow() >= expScore) {
				this.tempBestScore = score.getLow();
			} else if (score.getLow() > this.getTempBestScore()) {
				this.tempBestScore = score.getLow();
			}

		} else { // parent is min, child is max
			if (score.getHigh() <= expScore) {
				this.tempBestScore = score.getHigh();
			} else if (score.getHigh() < this.tempBestScore) {
				this.tempBestScore = score.getHigh();
			}
		}
		return this;
	}

	public int getTempBestScore() {
		return tempBestScore;
	}

	public List<Candidate> getCandidates() {
		return candidates;
	}

	public void setCandidates(List<Candidate> candidates) {
		this.candidates = new ArrayList<Candidate>();
		this.candidates.addAll(candidates);
		iterator = candidates.iterator();
	}

	public void setExpScore(int highestExp) {
		this.expScore = highestExp;
	}

	public static boolean unknownScore(int score) {
		return score == Integer.MAX_VALUE || score == Integer.MIN_VALUE + 1;
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
					+ ColorUtil.getColorText(whoseTurn) + ", whoseTurn="
					+ this.getWhoseTurnString() + ",  highestExp=" + expScore
					+ ", tempBestScore=" + tempBestScore + "]";
		else
			return "Level [level=" + levelIndex + ", whoseTurn="
					+ this.getWhoseTurnString() + ", lowestExp=" + expScore
					+ ", tempBestScore=" + tempBestScore + "]";

	}

	public String getWhoseTurnString() {
		if (max)
			return "MAX" + "[" + ColorUtil.getColorText(whoseTurn) + "]";
		else
			return "MIN" + "[" + ColorUtil.getColorText(whoseTurn) + "]";

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

	public boolean blackIsMax() {
		if (max) {
			if (whoseTurn == Constant.BLACK)
				return true;
		} else {
			if (whoseTurn == Constant.WHITE)
				return true;
		}
		return false;
	}

	public ScopeScore getScopeScore(int boardSize) {
		ScopeScore score = ScopeScore.getInitScore(boardSize);
		if (this.alreadyWin()) {
			score.updateWin(getTempBestScore(), isMax());
		} else if (this.hasNext() == false) {
			score.updateLose(getTempBestScore(), isMax());
		} else {
			return null;
		}
		return score;
	}
}
