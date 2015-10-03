package eddie.wu.search.global;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.GoBoardForward;
import eddie.wu.domain.Step;
import eddie.wu.domain.StepMemo;
import eddie.wu.domain.comp.BoardColorComparator;
import eddie.wu.manual.ExpectScore;
import eddie.wu.manual.SGFGoManual;
import eddie.wu.manual.SearchNode;
import eddie.wu.manual.SimpleGoManual;
import eddie.wu.manual.TreeGoManual;
import eddie.wu.search.HistoryDepScore;
import eddie.wu.search.ScopeScore;

/**
 * 
 * 2. no copy of board, use backward/forward feature.
 * 
 * @author Eddie Wu
 * 
 */
public abstract class GoBoardSearch {
	protected static final String DB_PASS = " DB_PASS";

	protected static final String EXHAUST = " EXHAUST";

	private int id;// for log information to distinguish instances

	/**
	 * whether we store and reuse state we ever encountered.
	 */
	public boolean store_state = true;
	public boolean reuse_state = false;

	private ExpectScore expScore;

	// set it big to deal with ladder calculation
	public int depth = 81;
	/**
	 * 1. set it small to ensure we have efficient algorithm <br/>
	 * 2. could be controlled outside. 3. it counts the number of final state we
	 * reached.
	 */

	private int NUMBER_OF_VARIANT = 50000 * 3;

	private static final Logger log = Logger.getLogger(GoBoardSearch.class);
	// when we apply the stored state.
	private static final Logger logProcess = Logger.getLogger("search.process");
	private static final Logger logApply = Logger
			.getLogger("search.state.apply");
	// when adding duplicate state.
	private static final Logger logState = Logger.getLogger("search.state.add");
	// when adding terminal state.
	private static final Logger logTerminalState = Logger
			.getLogger("search.terminal.state.add");
	private List<SearchLevel> levels = new ArrayList<SearchLevel>(depth);

	/**
	 * initial state is in level 0
	 */
	private int levelIndex = 0;
	/**
	 * during search, how many steps we forwarded.
	 */
	protected int countSteps = 0;

	/**
	 * 将每个搜索到终点的过程记录下来.便于排错.
	 */
	private List<String> searchProcess = new ArrayList<String>();

	// List<Step> bestResult;

	/**
	 * keep the search tree internally and help output result tree.
	 */
	private SearchNode root = SearchNode.createSpecialRoot();

	protected GoBoardSearch(int maxExp, int minExp) {
		expScore = new ExpectScore(maxExp, minExp);
		id++;
		log.warn("id=" + id);
	}

	// boolean blackIsMax = initLevel.blackIsMax();
	/**
	 * 初始局面在level 0中，所有的候选点尚未初始化。棋盘对应到初始状态<br/>
	 * 如果该层已经遍历所有候选点,就作出结论-得到score.<br/>
	 * 每次展开某一层中的一个候选点(如果没有初始化候选点,就马上初始化.如果已经初始化, <br/>
	 * 就从下一个没有尝试的候选点开始) initialize candidate on the fly<br/>
	 * 一般形成新的层，同时相应的棋盘状态前进了一步。该层的候选点暂时先不初始化。<br/>
	 * 这个不变量在每个循环的开始和结束处始终得到保持。<br/>
	 * 
	 * @return
	 */
	public int globalSearch() {
		GoBoard goBoard = this.getGoBoard();
		SearchLevel initLevel = getInitLevel();
		root.setMax(initLevel.isMax());
		initLevel.setNode(root);
		levels.add(initLevel);
		logProcess.debug("search with " + expScore);
		/**
		 * exit when we are at level 0 again and exhaust all the candidates;
		 * 1.Candidates are generated on the fly for current state/level<br/>
		 * 2.开始计算。 第一层循环：展开最后一个局面。 <br/>
		 * level 0: all candidates of original state.<br/>
		 * decide to completely rewrite it.<br/>
		 * 有未确定状态才需要继续搜索.
		 */
		while (true) {
			SearchLevel currentLevel = levels.get(levelIndex);
			BoardColorState currentState = this.getGoBoard()
					.getBoardColorState();
			boolean justInitialized = false;

			String stateMessage = currentState.getStateString() + "<---"
					+ currentLevel.getPrevStep();
			if (currentLevel.isInitialized()) {
				stateMessage = "Old Current Search state:" + stateMessage;

			} else {
				stateMessage = "New Current Search state:" + stateMessage;
			}
			logProcess.debug(stateMessage);

			/**
			 * 解决最终状态识别的问题.<br/>
			 * later either reach noExpansion == true or isInitialized == true.
			 */

			if (currentLevel.isInitialized() == false
					&& currentLevel.noExpansion == false) {
				if (reuse_state) {
					// identify final state per history knowledge.
					reuseEverEncounteredState(currentLevel, currentState);
				}
				if (currentLevel.noExpansion == false) {
					// check further whether we can identify it as final state
					TerminalState terminateState = getTerminalState();
					if (terminateState.isTerminalState()) {
						this.checkTerminalState(currentLevel, terminateState);
						currentLevel.noExpansion = true;
					}
				}

				/**
				 * whether search depth is reached!
				 */
				if (currentLevel.noExpansion == false) {
					if (levels.size() == depth) {
						currentLevel.setUnknownChild();
						currentLevel.noExpansion = true;
					}
				}

				// need further expansion
				if (currentLevel.noExpansion == false) {
					int color = currentLevel.getWhoseTurn();
					/**
					 * initial candidates on the fly if candidate is not
					 * initialized.
					 */
					this.initCandidate(currentLevel, color);
					justInitialized = true;
					List<Candidate> candidates = currentLevel.getCandidates();

					/**
					 * 不是最终状态，却又没有候选点，是很奇怪的事情，要注意处理。<br/>
					 * 当我把打劫的变化滤掉的时候，出现过这种情况。score＝0。因为终止于非确定状态。<br/>
					 * 需要区别对待，是应为劫材禁手导致没有候选点，还是本身就是终止状态而没有候选点<br/>
					 * 这可能是识别终止状态的能力不够所致。
					 * 
					 */
					if (candidates.isEmpty()) {
						log.error("不是最终状态，却又没有候选点");
						throw new RuntimeException(color + ": no candidate");
					}
				} else {
					// only set score for terminal state.
					currentLevel.getNode().setScore(
							currentLevel.getTempBestScore());
				}
			}

			logProcess_1(currentLevel, currentState, justInitialized);

			if (justInitialized == true) {
				logProcess.debug("Candidates is just initialized: "
						+ currentLevel.getAllCanPoint());
			} else if (currentLevel.alreadyWin()) {
				boolean noUp = false;
				if (levelIndex == 0) {
					noUp = true;
				}
				alreadyWin(currentLevel, currentState, noUp);
				if (noUp) {
					break;
				} else {
					continue;
				}
			} else if (currentLevel.exhausted()) { // not win yet.
				boolean noUp = false;
				if (levelIndex == 0) {
					noUp = true;
				}
				alreadyExhaused(currentLevel, currentState, noUp);
				if (noUp) {
					break;
				} else {
					continue;
				}
			} else {
				// on going -- Not Win, still has candidates to try.
				if (logProcess.isDebugEnabled()) {
					logProcess.debug("Not Win yet -- Old Candidate: "
							+ currentLevel.getAllCanPoint());
				}
			}

			/**
			 * goBoard is in state of level above.<br/>
			 * 现在处理候选点，展开得到下一level
			 */
			Candidate nextCandidate = currentLevel.getNextCandidate();
			Step step = nextCandidate.getStep();
			if (logProcess.isDebugEnabled()) {
				logProcess
						.debug("choose step " + step + Constant.lineSeparator);
			}
			/**
			 * 前进一步,进入新的level (普通无效点在getCandidate时滤掉）<br/>
			 * 但是复杂的同型再现是下了才能判断出来的。<br/>
			 * 虽然一般而言，双虚手终局；但是有时双虚手终局可能不代表常规的终局状态 。<br/>
			 * 比如说，假生的情形，最后一步死棋一方提劫，我方没有劫材（假设官子已经收完），只能弃权，<br/>
			 * 对方没有办法消劫，却可以趁机双虚手终局。<br/>
			 */
			boolean valid = getGoBoard().oneStepForward(step);
			if (valid == false) {
				// dead path after redesign - identify duplicates before hand.
				System.out.println(getGoBoard().getStateString().toString());
				for (StepMemo stepTemp : getGoBoard().getStepHistory()
						.getAllSteps()) {
					System.err.println(stepTemp.getStep());
					System.err.println(stepTemp.getColorState());
				}
				for (BoardColorState state : getGoBoard().getStepHistory()
						.getColorStates()) {
					System.err.println(state);
				}
				// we already check before hand when getting candidates.
				throw new RuntimeException("duplicate state leaked" + step);
			}

			countSteps++;
			long forwardMoves = getGoBoard().getForwardMoves();
			if (countSteps != forwardMoves) {
				throw new RuntimeException("coundsteps=" + countSteps
						+ " moves=" + forwardMoves + " " + step);
			}

			if (log.isInfoEnabled()) {
				log.info("check state after step " + step);
				log.info(goBoard.getStateString());
				if (goBoard.noStep() == false) {
					log.info("Last Step at " + goBoard.getLastPoint());
				}
			}

			if (step.isPass() == false
					&& goBoard.getColor(step.getPoint()) != step.getColor()) {
				String message = "step " + step + " does not take effect";
				log.error(message);
				throw new RuntimeException(message);
			}

			// reuse ever encountered states
			// check terminal state;

			/**
			 * continue expanding <br/>
			 * 生成新层。
			 */
			SearchLevel newLevel = buildNewLevel(currentLevel, step);
			updateTreeWithNewLevel(newLevel, currentLevel);
			levels.add(newLevel);
			if (levels.size() > depth) {
				String string = "levels size: " + levels.size() + " > " + depth;
				log.error(string);
				throw new RuntimeException(string);
			}

			if (this.searchProcess.size() > NUMBER_OF_VARIANT) {
				log.error("variants >" + NUMBER_OF_VARIANT);
				break;
			}
			log.warn(Constant.lineSeparator);
		} // while

		if (levelIndex == 0) {
			/**
			 * 算完了所有变化，回到level 0，此时的结果即最佳结果。
			 * 
			 */
			if (this.getClass().getName().endsWith("ThreeThreeBoardSearch")) {
				if (log.isInfoEnabled()) {
					log.info(this.getGoBoard().getInitColorState()
							.getStateString());
					log.info(this.getGoBoard().getBoardColorState()
							.getStateString());
				}
			}
			GoBoard goDemo = new GoBoard(this.getGoBoard().getInitColorState());
			log.warn("Initial state");
			goDemo.printState(log);
			SearchLevel searchLevel = levels.get(levelIndex);
			if (searchLevel.alreadyWin() == false
					&& searchLevel.hasUnknownChild()) {
				throw new RuntimeException(
						"no enough terminal state, search Depth is not enough,"
								+ this.depth);
			}
			return searchLevel.getTempBestScore();
		} else if (levelIndex < 0) {
			throw new RuntimeException("levelIndex < 0");
		} else {
			log.error("levelIndex = " + levelIndex);
			log.error(this.getClass().getName()
					+ ": cannot find result after deepth = " + levels.size());
			log.error(this.getGoBoard().getInitColorState().getStateString());
			SimpleGoManual manual = this.getGoBoard().getSimpleGoManual();
			String fileName = Constant.DEBUG_MANUAL + "globalSearch_last.sgf";
			SGFGoManual.storeGoManual(fileName, manual);

			getTreeGoManual().getExpandedString();
			manual = this.getTreeGoManual().getMostExpManual();
			fileName = Constant.DEBUG_MANUAL + "globalSearch_most_exp.sgf";
			SGFGoManual.storeGoManual(fileName, manual);

			for (StepMemo memo : this.getGoBoard().getStepHistory()
					.getAllSteps()) {
				log.error(memo.getStep());
			}
			log.error("Final State");
			log.error(this.getGoBoard().getBoardColorState().getStateString());

			GoBoardForward forward = new GoBoardForward(
					this.getGoBoard().boardSize);

			return Constant.UNKNOWN;
		}

	}

	public void logProcess_1(SearchLevel currentLevel,
			BoardColorState currentState, boolean justInitialized) {
		if (logProcess.isDebugEnabled()) {
			if (currentLevel.alreadyWin()) {
				logProcess.debug("Win by score = "
						+ currentLevel.getTempBestScore() + ", "
						+ currentLevel.getExpScoreAsString());
			} else if (currentLevel.exhausted()) {
				if (currentLevel.getTempBestScore() == Constant.UNKNOWN) {
					logProcess.debug("score is unknown ");
				} else {
					logProcess.debug("Lose by score = "
							+ currentLevel.getTempBestScore() + ", "
							+ currentLevel.getExpScoreAsString());
				}
			} else if (justInitialized == false) {
				logProcess.debug("temp best score = "
						+ currentLevel.getTempBestScore());
			}
		}
		if (log.isEnabledFor(Level.WARN)) {
			log.warn("id=" + id + "; level=" + currentLevel);
			log.warn("new Round started in search" + " at previous state: ");
			this.getGoBoard().printState(log);
			if (levelIndex != 0) {
				log.warn(" with history: "
						+ getGoBoard().getCurrent()
								.getSingleManualStringToRoot(false));
			}
		}
	}

	protected SearchLevel buildNewLevel(SearchLevel preLevel, Step step) {
		assert preLevel.getWhoseTurn() == step.getColor();
		int enemyColor = ColorUtil.enemyColor(preLevel.getWhoseTurn());
		SearchLevel newLevel = new SearchLevel(++levelIndex, enemyColor, step);
		if (preLevel.isMax()) {
			newLevel.setMax(false);
			newLevel.setExpScore(this.getMinExp());
		} else {
			newLevel.setMax(true);
			newLevel.setExpScore(this.getMaxExp());
		}
		return newLevel;
	}

	abstract protected void initCandidate(SearchLevel level, int color);

	abstract public GoBoard getGoBoard();

	abstract protected SearchLevel getInitLevel();

	/**
	 * @obsolete
	 * @return
	 */
	public int getMaxExp() {
		return this.expScore.getHighExp();
	}

	public int getMinExp() {
		return this.expScore.getLowExp();
	}

	public ExpectScore getExpScore() {
		return this.expScore;
	}

	/**
	 * store the state encountered during search, since we may reach same state
	 * again and again. especially in case of small board like 2*2, where most
	 * state is history dependent, so there might be many variant. it is
	 * different with known state identified before hand.
	 * 
	 * @return
	 */
	private Map<BoardColorState, ScopeScore> stateReached = new HashMap<BoardColorState, ScopeScore>();

	public Map<BoardColorState, ScopeScore> getStateReached() {
		return stateReached;
	}

	/**
	 * history dependent state Reached, mainly for 2*2 board.
	 */
	private Map<BoardColorState, HistoryDepScore> hisDepState = new HashMap<BoardColorState, HistoryDepScore>();

	public Map<BoardColorState, HistoryDepScore> getStateHisDepReached() {
		return hisDepState;
	}

	/**
	 * Encountered state during search, but we cannot not determine the state
	 * due to resource limitation.<br/>
	 * will mark as an attribute of state.
	 */
	// private Set unknownState = new HashSet<BoardColorState>();

	abstract protected TerminalState getTerminalState();

	/**
	 * get the tree according to the search process, it is similar to the tree
	 * in the goBoard! <br/>
	 * since goBoard already record all the steps. we can clean up the bad move
	 * here to simplify the result.
	 * 
	 * @return
	 */
	public TreeGoManual getTreeGoManual() {
		TreeGoManual manual = new TreeGoManual(this.getGoBoard()
				.getInitColorState());
		manual.setRoot(root);
		manual.setExpScore(expScore);
		return manual;

	}

	// abstract protected boolean isKnownState(BoardColorState boardColorState);

	abstract protected void stateDecided(BoardColorState boardColorState,
			boolean max, int score, boolean win);

	/**
	 * 终止局面的结果保存备用。
	 * 
	 * @param boardColorStateN
	 * @param scoreTerminator
	 */
	abstract protected void stateFinalizeed(BoardColorState boardColorStateN,
			int scoreTerminator);

	public boolean tree() {
		return true;
		// return this.NUMBER_OF_VARIANT <= 150000;
	}

	public void setVariant(int variant) {
		NUMBER_OF_VARIANT = variant;
	}

	public void setDepth(int deepth) {
		this.depth = deepth;
	}

	public List<String> getSearchProcess() {
		return searchProcess;
	}

	private void updateTreeWhenTerminate(SearchLevel oldLevel, Step step,
			int scoreTerminator) {
		if (tree()) {
			SearchNode node = new SearchNode(step);
			node.setScore(scoreTerminator);
			oldLevel.getNode().addChild(node);
			node.setMax(!oldLevel.isMax());
		}
	}

	private void updateTreeWhenUnknown(SearchLevel currentLevel) {
		// if (tree()) {
		// SearchNode node = new SearchNode(currentLevel.getPrevStep());
		// node.setUnknownScore(true);
		// currentLevel.getNode().addChild(node);
		// node.setMax(!oldLevel.isMax());
		// }
	}

	/**
	 * Level means the state reached by its prevStep. it is same as the state
	 * reached by Node's step.
	 * 
	 * @param newLevel
	 * @param currentLevel
	 */
	private void updateTreeWithNewLevel(SearchLevel newLevel,
			SearchLevel currentLevel) {
		if (tree()) {
			Step step = newLevel.getPrevStep();
			SearchNode newNode = new SearchNode(step);
			currentLevel.getNode().addChild(newNode);
			newLevel.setNode(newNode);
			newNode.setMax(newLevel.isMax());
		}
	}

	/**
	 * count all the state encountered during search. here it is known state.
	 * currently it is an accurate state. for example from dead_exist judgment.
	 * 
	 * @param scoreTerminator
	 */
	private void reachTerminalState(int scoreTerminator) {
		BoardColorState boardColorStateTemp = getGoBoard().getBoardColorState()
				.normalize();
		// boardColorStateTemp.setScore(scoreTerminator);
		if (stateReached.containsKey(boardColorStateTemp)) {
			ScopeScore scopeScore = stateReached.get(boardColorStateTemp);
			scopeScore.updateAccurateScore(scoreTerminator);
			scopeScore.increaseCount();

			if (logTerminalState.isDebugEnabled()) {
				logTerminalState
						.debug("Reach Terminal and update existing state:"
								+ boardColorStateTemp);
				logTerminalState.debug("with Score " + scopeScore);
			}
		} else {// new state
			ScopeScore scopeScore = new ScopeScore(scoreTerminator);
			scopeScore.increaseCount();
			stateReached.put(boardColorStateTemp, scopeScore);
			if (logTerminalState.isDebugEnabled()) {
				logTerminalState.debug("Reach Terminal and Add new state:"
						+ boardColorStateTemp);
				logTerminalState.debug("with Score " + scopeScore);
			}
		}
	}

	/**
	 * reach non-terminal state, it has details in SearchLevel structure.<br/>
	 * the result is not accurate. <br/>
	 * either already win or lose.
	 * 
	 * @param level
	 */
	private void reachState(SearchLevel level) {
		if (store_state == false)
			return;

		StepMemo lastStep = this.getGoBoard().getLastStep();
		if (level.isScoreByBothPass())
			return;
		if (lastStep != null && lastStep.isPass() == true && level.alreadyWin()) {
			return;
		}
		if(level.getTempBestScore()==Constant.UNKNOWN){
			return;
		}
		if (level.isReachingDup()) {
			if (level.alreadyWin() == false) {
				// directly reach duplicate and failed.
				this.reachHisDepState(level);
			} else { // win even with direct duplicate.
				if (level.isReachingDupIndirectly()) {
					// some state not only reach duplicates directly, it may
					// also
					// has indirect duplicates.
					reachHisDepState(level);
				} else {
					// win means lose one change reaching duplicates did not
					// impact the result
					reachHisIndepState(level);
				}
			}
		} else if (level.isReachingDupIndirectly()) {
			reachHisDepState(level);
		} else {
			reachHisIndepState(level);
		}
	}

	private void reachHisIndepState(SearchLevel level) {
		BoardColorState boardColorStateTemp = getGoBoard().getBoardColorState()
				.normalize();
		BoardColorState mirrorState = boardColorStateTemp.blackWhiteSwitch();
		ScopeScore score = null;
		logState.info("History Independent State");
		if (stateReached.containsKey(boardColorStateTemp)) {
			score = stateReached.get(boardColorStateTemp);
			updateScoreWithSearchLevel(score, level);
			score.increaseNotAppliedTimes();
			if (logState.isDebugEnabled()) {
				logState.debug("Update state:"
						+ boardColorStateTemp.getStateString());
				logState.debug("Original Score: " + score);
				logState.debug("with Score: " + level.getTempBestScore());

			}
		} else if (stateReached.containsKey(mirrorState)) {
			score = stateReached.get(mirrorState);
			ScopeScore mirrorScore = level.getScopeScore(
					this.getGoBoard().boardSize).mirror();
			score.merge(mirrorScore);
			// updateScoreWithSearchLevel(score, level);
			score.increaseNotAppliedTimes();
			if (logState.isDebugEnabled()) {
				logState.debug("Update state:" + mirrorState.getStateString());
				logState.debug("Original Score: " + score);
				logState.debug("with Score: " + level.getTempBestScore());

			}
		} else {// new state
			score = ScopeScore.getInitScore(this.getGoBoard().boardSize);
			updateScoreWithSearchLevel(score, level);
			stateReached.put(boardColorStateTemp, score);
			ScopeScore mirrorScore = score.mirror();
			stateReached.put(mirrorState, mirrorScore);

			if (logState.isDebugEnabled()) {
				logState.debug("Add state:"
						+ boardColorStateTemp.getStateString());
				logState.debug("with Score: " + score);
				logState.debug("Add state:" + mirrorState.getStateString());
				logState.debug("with Score: " + mirrorScore);
			}
		}

		if (logState.isDebugEnabled()) {
			logState.debug("Max = " + level.isMax());
			logState.debug("Win = " + level.alreadyWin());
			logState.debug("exp = " + level.getExpScore());
			logState.debug("expScore=" + expScore);
			logState.debug(this.getGoBoard().getLastStep().getStep());
			logState.debug(level.getAllCanPoint());
			logState.debug("Result Score: " + score);
		}
	}

	private void updateScoreWithSearchLevel(ScopeScore score, SearchLevel level) {
		try {
			if (level.alreadyWin()) {
				score.updateWin(level.getTempBestScore(), level.isMax());
			} else {
				score.updateLose(level.getTempBestScore(), level.isMax());
			}
			score.increaseCount();
		} catch (RuntimeException e) {
			log.error(this.getGoBoard().getBoardColorState().getStateString());
			throw e;
		}
	}

	private void updateScoreWithSearchLevel(HistoryDepScore score,
			SearchLevel level) {
		try {
			score.updateScoreWithSearchLevel(level);
		} catch (RuntimeException e) {
			log.error(this.getGoBoard().getBoardColorState().getStateString());
			throw e;
		}
	}

	/**
	 * reach History dependent and non-terminal state, it has details in
	 * SearchLevel structure.<br/>
	 * the result is not accurate.
	 * 
	 * @param level
	 */
	private void reachHisDepState(SearchLevel level) {
		// history dependent, but cannot be reused.

		if (level.isReachingDup() == false || level.getUniqueDupState() == null)
			return;
		try {
			BoardColorState boardColorStateTemp = getGoBoard()
					.getBoardColorState();
			logState.info("state" + boardColorStateTemp);
			logState.info("depends" + level.getUniqueDupState());
			HistoryDepScore score = null;
			if (hisDepState.containsKey(boardColorStateTemp)) {
				score = hisDepState.get(boardColorStateTemp);
				logState.info("update Existing History Dependent state's score:"
						+ boardColorStateTemp.getStateString() + "\t" + score);
				updateScoreWithSearchLevel(score, level);
			} else if (level.getUniqueDupState() != null) {// new state
				int boardSize = this.getGoBoard().boardSize;
				score = HistoryDepScore.getInstance(level.getUniqueDupState(),
						level.getScopeScore(boardSize));
				hisDepState.put(boardColorStateTemp, score);
				// updateScoreWithSearchLevel(score, level);
				logState.info("Add new History Dependent state's score:"
						+ boardColorStateTemp.getStateString() + "\t" + score);
			} else {

			}
		} catch (RuntimeException e) {

			throw e;
		}

	}

	public void logStateReached() {
		if (logApply.isInfoEnabled()) {
			logApply.info("History Independet State statistics:");
			int count = 0;
			int apply = 0;
			int notApply = 0;
			for (Entry<BoardColorState, ScopeScore> entry : this.stateReached
					.entrySet()) {
				logApply.debug(entry.getKey().getStateString());
				logApply.debug(entry.getValue().toString());
				if (entry.getValue().getCount() > 1) {
					count += entry.getValue().getCount();
					count--;
				}
				apply += entry.getValue().getAppliedTimes();
				notApply += entry.getValue().getNotAppliedTimes();
			}
			logApply.info("total termial state duplicated: " + count);
			logApply.info("total Not saved duplicate state: " + notApply);
			logApply.info("total saved duplicate state: " + apply);
		}
	}

	private void debugInvalidStep(Step step, int scoreTerminator) {
		log.warn("new state after step " + step);
		log.warn(" is final state with score = " + scoreTerminator);
		// log.warn(getGoBoard().getCurrent()
		// .getSingleManualStringToRoot(false));

		GoBoard goOld = this.getGoBoard();
		goOld.printState(log);
		BoardColorState colorState = goOld.getInitColorState();
		GoBoardForward go;
		if (colorState == null) {
			go = new GoBoardForward(goOld.boardSize);
		} else {
			go = new GoBoardForward(colorState);
		}

		for (StepMemo memo : this.getGoBoard().getStepHistory().getAllSteps()) {
			// log.warn(memo.getStep());
			boolean validStep = go.oneStepForward(memo.getStep());
			if (validStep == false) {
				System.out.println("Invalid step:" + memo.getStep());
				if (log.isInfoEnabled())
					log.info("At status:");
				go.printState(log);
				throw new RuntimeException("Invalid step:");
			}
		}

		// manual check duplicate state in log.
		go = new GoBoardForward(goOld.getInitColorState());
		for (StepMemo memo : this.getGoBoard().getStepHistory().getAllSteps()) {
			boolean validStep = go.oneStepForward(memo.getStep());
			// go.printState(log);
		}
		List<BoardColorState> colorStates = new ArrayList<BoardColorState>();
		colorStates.addAll(go.getStepHistory().getColorStates());
		Collections.sort(colorStates, new BoardColorComparator());
		for (BoardColorState cs : colorStates) {
			if (log.isInfoEnabled())
				log.info(cs);
		}
	}

	public void logHistoryDepStateReached() {
		if (logApply.isInfoEnabled() == false)
			return;
		logApply.info("History Dependent State statistics");
		int count = 0;
		int apply = 0;
		int notApply = 0;
		for (Entry<BoardColorState, HistoryDepScore> entry : this.hisDepState
				.entrySet()) {
			logApply.info(entry.getKey().getStateString());
			for (Entry<BoardColorState, ScopeScore> entry2 : entry.getValue()
					.getMap().entrySet()) {
				logApply.info(entry2.getKey().getStateString());
				logApply.info(entry2.getValue().toString());
				if (entry2.getValue().getCount() > 1) {
					count += entry2.getValue().getCount();
					count--;
				}
				apply += entry2.getValue().getAppliedTimes();
				notApply += entry2.getValue().getNotAppliedTimes();
			}
		}
		logApply.info("total termial state duplicated: " + count);
		logApply.info("total Not saved duplicate state: " + notApply);
		logApply.info("total saved duplicate state: " + apply);

	}

	public void reuseIndependentState(SearchLevel level,
			BoardColorState newState, boolean mirror) {
		boolean alreadyWin = false;
		ScopeScore scopeScore = stateReached.get(newState);
		if (mirror) {
			scopeScore = scopeScore.mirror();
		}
		level.updateWithFuzzyScore(scopeScore);
		scopeScore.increaseAppliedTimes();
		alreadyWin = level.alreadyWin();
		if (alreadyWin) {
			level.noExpansion = true;
		}
		if (logApply.isDebugEnabled()) {
			if (mirror) {
				logApply.debug("Current State's mirror (after normalization) is "
						+ newState.getStateString());
				logApply.debug("Mirrored Independent State is applied");
			} else {
				logApply.debug("Current State (after normalization) is "
						+ newState.getStateString());
				logApply.debug("Original Independent State is applied");
			}

			if (alreadyWin) {
				logApply.debug("Terminate by winning");
			} else {
				logApply.debug("Need further try...");
			}
		}

	}

	public void reuseDependentState(SearchLevel level,
			BoardColorState newState, boolean mirror) {
		HistoryDepScore scopeScore = hisDepState.get(newState);
		boolean alreadyWin = false;
		for (Entry<BoardColorState, ScopeScore> depScore : scopeScore.getMap()
				.entrySet()) {
			BoardColorState state = depScore.getKey();
			if (mirror) {
				// TODO. denormalize.
				state.blackWhiteSwitch();
			}
			if (getGoBoard().getStepHistory().containState(state)) {
				ScopeScore temp = depScore.getValue();
				if (mirror)
					temp = temp.mirror();
				level.updateWithFuzzyScore(temp);
				level.addIndirectDupStates(depScore.getKey());
				alreadyWin = level.alreadyWin();
				if (alreadyWin) {
					level.noExpansion = true;
					// depScore.increaseAppliedTimes();
					// getGoBoard().oneStepBackward();
				}
				if (logApply.isDebugEnabled()) {
					if (mirror) {
						logApply.debug("Current State's mirror (after normalization) is "
								+ newState.getStateString());
						logApply.debug("Mirrored dependent State is applied");
					} else {
						logApply.debug("Current State (after normalization) is "
								+ newState.getStateString());
						logApply.debug("Original dependent State is applied");
					}
					logApply.debug("Dep state is" + depScore.getKey());
					logApply.debug(depScore.getValue());
					if (alreadyWin) {
						logApply.debug("Terminate by Win ");
					} else {
						logApply.debug("Terminate by Lose ");
					}
				}

				// break;
			}
		}
	}

	/**
	 * check whether the state is already encountered during search. if the
	 * score is accurate enough to know win or lose! level.noExpansion will be
	 * set to true.
	 *
	 */
	public void reuseEverEncounteredState(SearchLevel level,
			BoardColorState currentState) {
		BoardColorState newState = currentState.normalize();
		BoardColorState newStateMirror = newState.blackWhiteSwitch()
				.normalize();
		if (this.stateReached.containsKey(newState)) {
			reuseIndependentState(level, newState, false);
		} else if (this.stateReached.containsKey(newStateMirror)) {
			reuseIndependentState(level, newStateMirror, true);
		}
		if (level.noExpansion)
			return;
		if (this.hisDepState.containsKey(newState)) {
			reuseDependentState(level, newState, false);
		} else if (this.hisDepState.containsKey(newStateMirror)) {
			reuseDependentState(level, newStateMirror, true);
		}

	}

	public void checkTerminalState(SearchLevel level,
			TerminalState terminateState) {
		Step step = level.getPrevStep();

		if (logProcess.isInfoEnabled())
			logProcess.info("Teminal State");
		getGoBoard().hasLoopInHistory();
		int scoreTerminator = terminateState.getScore();
		if (terminateState.isBothPass() == true) {
			if (logProcess.isDebugEnabled()) {
				logProcess.debug("Both Pass");
				logProcess.debug("level reaching dup " + level.isReachingDup());
			}
			List<Step> process = new ArrayList<Step>();
			for (StepMemo memo : this.getGoBoard().getStepHistory()
					.getAllSteps()) {
				process.add(memo.getStep());
			}
			searchProcess.add(Step
					.getString(process, scoreTerminator + DB_PASS));
		} else {

			List<Step> process = new ArrayList<Step>();
			for (StepMemo memo : this.getGoBoard().getStepHistory()
					.getAllSteps()) {
				process.add(memo.getStep());
			}
			String string = Step.getString(process, "FINAL " + scoreTerminator);
			searchProcess.add(string);
			if (log.isEnabledFor(Level.WARN)) {
				log.warn(string);
				log.warn(this.getGoBoard().getTreeGoManual()
						.getSGFBodyString(false));
			}
		}
		level.updateWithScore(scoreTerminator, terminateState.isBothPass());
		level.getNode().setScore(level.getTempBestScore());
		if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
			debugInvalidStep(step, scoreTerminator);
		}
		// actually both pass reach previous state with pass
		// the result is not accurate at all.
		if (terminateState.isBothPass() == false
				&& level.isPrevStepPass() == false) {
			reachTerminalState(scoreTerminator);
		}
	}

	/**
	 * 该层的结果已知，无需查看其他候选点。下一层的结果返回到上层的tempBestScore<br/>
	 * 此时配合处理。
	 * 
	 * @param noUp
	 *            is true if we are at the top already.
	 */
	public void alreadyWin(SearchLevel currentLevel,
			BoardColorState currentState, boolean noUp) {
		int score = currentLevel.getTempBestScore();
		// log.warn(currentLevel.getWhoseTurnString() + " Already Win: " +
		// score);

		if (currentLevel.isPrevStepPass() == false) {
			reachState(currentLevel);
		}
		if (noUp)
			return;
		// boolean scoreHistoryDep = level.isScoreHistoryDep();
		// for winner, the direct dup. state doesn't impact result.
		Set<BoardColorState> dupStates = currentLevel.getAllDupStates();
		dupStates.remove(currentState);

		levels.remove(levelIndex--);
		SearchLevel upLevel = levels.get(levelIndex);
		upLevel.updateWithScore(score);
		// bug here, multiple children level may race on parent
		// level.
		// level.setScoreHistoryDep(scoreHistoryDep);
		if (dupStates.isEmpty() == false) {
			// level.setScoreHistoryDep(scoreHistoryDep);
			upLevel.addIndirectDupStates(dupStates);
			logState.debug("historyDep = " + true
					+ ", adding following state up level." + upLevel.hashCode());
			for (BoardColorState state : dupStates) {
				logState.debug(Constant.lineSeparator + state);
			}
			logState.debug("historyDep = " + true);
		}
		getGoBoard().oneStepBackward();
	}

	/**
	 * 是否当前层的所有的候选点都处理完了。这也意味着该层对应的状态结果已知。<br/>
	 * all candidates are handled (implicitly also not win yet !)
	 */
	public void alreadyExhaused(SearchLevel currentLevel,
			BoardColorState currentState, boolean noUp) {

		int score = currentLevel.getTempBestScore();
		boolean max = currentLevel.isMax();

		if (currentLevel.noExpansion == false) {
			log.warn(currentLevel.getWhoseTurnString()
					+ " Already Lose after trying all candidates: ");
			log.warn(currentLevel.getAllCanPoint());
		} else {
			if (score == Constant.UNKNOWN) {
				log.warn(currentLevel.getWhoseTurnString() + " Unknown state");
			} else if (currentLevel.hasUnknownChild()) {
				log.warn(currentLevel.getWhoseTurnString()
						+ " Unknown state even with score " + score);
			} else {
				log.warn(currentLevel.getWhoseTurnString()
						+ " Lose as known or terminal state");
			}
		}
		/**
		 * for debugging
		 * 
		 */
		List<Step> process = new ArrayList<Step>();
		for (StepMemo memo : this.getGoBoard().getStepHistory().getAllSteps()) {
			process.add(memo.getStep());
		}

		String scoreSuffix = Step.getString(process, score + EXHAUST);

		// in general after exhausting all candidates, the state
		// is decided
		// the exception is that we don't know the score in case
		// of too deep.
		if (score == Constant.UNKNOWN || currentLevel.hasUnknownChild()) {
			log.warn("score = " + score);
			currentLevel.getNode().setUnknownScore(true);
			scoreSuffix = Step.getString(process, "Unknown " + EXHAUST);
			// level.getNode().setScore(score); leave score = 0.
		}
		searchProcess.add(scoreSuffix);

		Set<BoardColorState> dupStates = currentLevel.getAllDupStates();
		if (dupStates.isEmpty() == false) {
			logState.debug("historyDep = " + true + ", begin");
			for (BoardColorState state : dupStates) {
				logState.debug(Constant.lineSeparator + state);
			}
			logState.debug("historyDep = " + true + ", end");
		}
		try {
			this.reachState(currentLevel);
		} catch (RuntimeException e) {
			this.getGoBoard().errorState();
			ScopeScore score2 = this.stateReached.get(this.getGoBoard()
					.getBoardColorState());
			log.error(score2);
			log.error("temp best score = " + score);
			// for(Map.Entry<BoardColorState,ScopeScore>
			// entry:this.stateReached.entrySet()){
			// log.error(entry.getKey().toString());
			// log.error(entry.getValue().toString());
			// }
			throw e;
		}
		if (noUp)
			return;
		boolean hasUnknownChild = currentLevel.hasUnknownChild();
		Set<BoardColorState> indirect = currentLevel.getIndirectDupStates();
		if (indirect != null)
			indirect.remove(currentState);

		levels.remove(levelIndex--);
		SearchLevel uplevel = levels.get(levelIndex);
		// bug here, multiple children level may race on parent
		// level.

		if (dupStates.isEmpty() == false) {
			uplevel.addIndirectDupStates(dupStates);
			// uplevel.setScoreHistoryDep(historyDep);
			logState.debug("historyDep = " + true);
			logState.debug(dupStates);
		}
		if (hasUnknownChild) {
			uplevel.setUnknownChild();
		} else {
			if (getGoBoard().getStepHistory().isDupReached(currentState) == false
					&& (getGoBoard().noStep() == true || getGoBoard()
							.getLastStep().isPass() == false)) {
				log.warn("current state is not a history dependent state");
				getGoBoard().getStepHistory().printDupState();
				this.stateDecided(currentState, max, score, false);
			}
			uplevel.updateWithScore(score);
		}
		getGoBoard().oneStepBackward();

	}

}