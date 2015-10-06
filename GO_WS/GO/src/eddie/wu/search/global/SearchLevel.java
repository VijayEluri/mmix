package eddie.wu.search.global;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Step;
import eddie.wu.domain.SymmetryResult;
import eddie.wu.manual.ExpectScore;
import eddie.wu.manual.SearchNode;
import eddie.wu.manual.TreeGoManual;
import eddie.wu.search.ScopeScore;
import eddie.wu.search.ScoreWithManual;

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
	 * expScore (this.getExpScore()) + max/min means the criteria for win/lose<br/>
	 * private int expScore;// for max, replaced by this.getExpScore()
	 */

	private ExpectScore expScore;

	/**
	 * whether the score is achieved by both pass!
	 */
	private boolean scoreByBothPass;

	/**
	 * true when the state is terminal one, or reaching depth limit, or with
	 * known score from reused state. it need not to expansion by candidates.
	 */
	boolean noExpansion;

	public boolean isScoreByBothPass() {
		return scoreByBothPass;
	}

	private List<Candidate> candidates;
	private Iterator<Candidate> iterator;

	/**
	 * the node without play at current level's candidate <br/>
	 * just played previous level's candidate/step (prevStep) to reach current
	 * level/state.<br/>
	 * at level 0, node is a special root node;
	 */
	private SearchNode node;

	/**
	 * 当前层临时的未初始化的计算结果
	 */
	private int tempBestScore = Constant.UNKNOWN;

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
		} // bug here: else
		if (this.indirectDupStates != null
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

	/**
	 * prevStep.isPass() previous step is a Pass prevStep == null means initial
	 * searching state!
	 * 
	 * @return
	 */
	public boolean isPrevStepPass() {
		return prevStep != null && prevStep.isPass();
	}

	/**
	 * called if score is initialized.
	 * 
	 * @return
	 */
	public boolean alreadyWin_() {
		if (max) {
			if (this.tempBestScore >= this.getExpScore()) {
				unknownChild = false;// win overwrite unknown child
				return true;
			}
		} else {
			if (this.tempBestScore <= this.getExpScore()) {
				unknownChild = false;// win overwrite unknown child
				return true;
			}
		}
		return false;
	}

	/**
	 * current Player exhausted all candidates and still does not win yet.
	 * 
	 * @return
	 */
	public boolean opponentWin_() {
		if (max) {
			if (this.tempBestScore <= this.expScore.getLowExp()) {
				return true;
			}
		} else {
			if (this.tempBestScore >= this.expScore.getHighExp()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * means no need to consider the neighbors.
	 * 在当前局面下，前面的选择已经达到最佳效果，没有必要考虑其他的候选棋步了。<br/>
	 * 现在要求对称的计分系统。<br/>
	 * 
	 * @return
	 */
	public boolean alreadyWin() {
		if (this.tempBestScore == Constant.UNKNOWN)
			return false;
		return this.alreadyWin_();
	}

	public boolean exhausted() {
		if (this.noExpansion == true) {
			return true;
		} else {
			if (this.isInitialized()) {
				if (this.iterator.hasNext()) {
					return false;
				} else {
					return true;
				}
			} else {
				// TODO: may remove tempScore
				// impossible
				throw new RuntimeException("not init!");
			}
		}

	}

	public boolean isMax() {
		return max;
	}

	public int getWhoseTurn() {
		return whoseTurn;
	}

	public int getLevel() {
		return levelIndex;
	}

	public int getExpScore() {
		if (max)
			return expScore.getHighExp();
		else
			return expScore.getLowExp();
	}

	// /**
	// * 在展开中的level的某个候选点的分数已知。
	// *
	// * @param score
	// */
	// public void getNeighborScore(int score, boolean bothPass) {
	// updateWithScore(score, bothPass);
	//
	// }

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
		if (candidates == null)
			return "no candidates";
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
		if (candidates == null)
			return 0;
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
	/**
	 * 下一层的结果已知，反馈给上一层。
	 * 
	 * @param score
	 */
	public void updateWithScore(int score) {
		updateWithScore(score, false);
	}

	public void updateWithScore(int score, boolean bothPass) {
		// if (skip == true) {
		// firstSuperiorScore = score;// = score / 2;
		// }
		// ignore unknown score and its branch
		if (score == Constant.UNKNOWN) {
			unknownChild = true;
			return;
		}
		if (this.tempBestScore == Constant.UNKNOWN) {
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
			if (score.getLow() >= this.getExpScore()) {
				this.tempBestScore = score.getLow();
			} else if (score.getLow() > this.getTempBestScore()) {
				this.tempBestScore = score.getLow();
			}

		} else { // parent is min, child is max
			if (score.getHigh() <= this.getExpScore()) {
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
		// need a safe copy!
		this.candidates = new ArrayList<Candidate>();
		this.candidates.addAll(candidates);
		iterator = candidates.iterator();
	}

	public void setExpScore(ExpectScore expScore) {
		this.expScore = expScore;
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

	public String getExpScoreAsString() {
		if (max) {
			return getExpScore() + "[Up]";
		} else {
			return getExpScore() + "[Down]";
		}
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

	// public ScopeScore getScopeScore(int boardSize) {
	// ScopeScore score = ScopeScore.getInitScore(boardSize);
	// if (this.alreadyWin()) {
	// score.updateWin(getTempBestScore(), isMax());
	// } else if (this.hasNext() == false) {
	// score.updateLose(getTempBestScore(), isMax());
	// } else {
	// return null;
	// }
	// ScopeScore score2 = getScopeScore_(boardSize);
	// if (score2.isExact() == false)
	// TestCase.assertEquals(score, score2);
	// return score2;
	// }

	public ScoreWithManual getScopeScore_(BoardColorState normalizedState,
			SymmetryResult sym) {
		int boardSize = normalizedState.boardSize;
		ScoreWithManual scopeScore = null;
		if (this.tempBestScore == Constant.UNKNOWN){
		return null;
			//return ScopeScore.getInitScore(boardSize);
		}
		if (this.alreadyWin_()) {
			TreeGoManual manual = this.getNode().wrapAsManual(
					normalizedState, sym);
			manual.cleanupBadMoveForWinner(max);
			scopeScore = new ScoreWithManual(this.tempBestScore, max, boardSize,max,manual);
			if(sym.blackWhiteSymmetric){
				scopeScore.mirrorScore();
			}
		} else if (this.hasNext() == false) {
			if (this.opponentWin_() == false) {
				// TODO
				TreeGoManual manualLose = this.getNode().wrapAsManual(
						normalizedState, sym);
				manualLose.cleanupBadMoveForWinner(!max);
				TreeGoManual manualWin = this.getNode().wrapAsManual(
						normalizedState, sym);
				manualWin.cleanupBadMoveForWinner(max);
//				scopeScore.win = manualWin;
//				scopeScore.lose = manualLose;
				scopeScore =  new ScoreWithManual(tempBestScore, manualWin, manualLose);
				if(sym.blackWhiteSymmetric){
					scopeScore.mirrorScore();
				}
				
			} else {
				TreeGoManual manual = this.getNode().wrapAsManual(
						normalizedState, sym);
				manual.cleanupBadMoveForWinner(!max);
				
				scopeScore = new ScoreWithManual(this.tempBestScore, !max, boardSize,max,manual);
				if(sym.blackWhiteSymmetric){
					scopeScore.mirrorScore();
				}
				
			}
		} else {
			return null;
		}
		return scopeScore;
	}
}
