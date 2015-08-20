package eddie.wu.search.global;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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

	private int minExpScore;
	private int maxExpScore;

	// set it big to deal with ladder calculation
	public int depth = 81;
	/**
	 * 1. set it small to ensure we have efficient algorithm <br/>
	 * 2. could be controlled outside. 3. it counts the number of final state we
	 * reached.
	 */

	private int NUMBER_OF_VARIANT = 50000 * 3;

	// transient public int dupCount;

	private static final Logger log = Logger.getLogger(GoBoardSearch.class);

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
		maxExpScore = maxExp;
		minExpScore = minExp;
		id++;
		log.warn("id=" + id);
	}

	protected SearchLevel buildNewLevel(SearchLevel preLevel) {
		int enemyColor = ColorUtil.enemyColor(preLevel.getColor());
		SearchLevel newLevel = new SearchLevel(++levelIndex, enemyColor);
		if (preLevel.isMax()) {
			newLevel.setMax(false);
			newLevel.setMinExp(this.getMinExp());
		} else {
			newLevel.setMax(true);
			newLevel.setMaxExp(this.getMaxExp());
		}
		return newLevel;
	}

	// public List<Step> getBestResult() {
	// return bestResult;
	// }

	abstract protected List<Candidate> getCandidate(int color);

	abstract public GoBoard getGoBoard();

	abstract protected SearchLevel getInitLevel();

	public int getMaxExp() {
		return this.maxExpScore;
	}

	public int getMinExp() {
		return this.minExpScore;
	}

	/**
	 * store the state encountered during search, since we may reach sate state
	 * again and again. it is different with before hand identified known state.
	 * 
	 * @return
	 */
	private Set knownState = new HashSet<BoardColorState>();

	/**
	 * Encountered state during search, but we cannot not determine the state
	 * due to resource limitation.
	 */
	//private Set unknownState = new HashSet<BoardColorState>();

	// abstract protected int getScore(BoardColorState boardColorState);

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
		TreeGoManual manual = new TreeGoManual(this.getGoBoard().getInitColorState());
		manual.setRoot(root);
		ExpectScore expScore = new ExpectScore(maxExpScore, minExpScore);
		manual.setExpScore(expScore);
		return manual;

	}

	// abstract protected boolean isKnownState(BoardColorState boardColorState);

	abstract protected void stateDecided(BoardColorState boardColorState, boolean max, int score, boolean win);

	/**
	 * 终止局面的结果保存备用。
	 * 
	 * @param boardColorStateN
	 * @param scoreTerminator
	 */
	abstract protected void stateFinalizeed(BoardColorState boardColorStateN, int scoreTerminator);

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

	private void updateTreeWhenTerminate(SearchLevel oldLevel, Step step, int scoreTerminator) {
		if (tree()) {
			SearchNode node = new SearchNode(step);
			node.setScore(scoreTerminator);
			oldLevel.getNode().addChild(node);
			node.setMax(!oldLevel.isMax());
		}
	}

	private void updateTreeWhenUnknown(SearchLevel oldLevel, Step step) {
		if (tree()) {
			SearchNode node = new SearchNode(step);
			node.setUnknownScore(true);
			oldLevel.getNode().addChild(node);
			node.setMax(!oldLevel.isMax());
		}
	}

	private void updateTreeWithNewLevel(SearchLevel newLevel, SearchLevel level, Step step) {
		if (tree()) {
			SearchNode node = new SearchNode(step);
			level.getNode().addChild(node);
			newLevel.setNode(node);
			node.setMax(newLevel.isMax());

		}
	}

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
		// level 0: all candidates of original state.
		SearchLevel initLevel = getInitLevel();
		initLevel.setNode(root);
		root.setMax(initLevel.isMax());
		levels.add(initLevel);
		/**
		 * exit when we are at level 0 again and exhaust all the candidates;
		 * 1.Candidates are generated on the fly for current state/level<br/>
		 * 2.开始计算。 第一层循环：展开最后一个局面。 <br/>
		 * decide to completely rewrite it.<br/>
		 * 有未确定状态才需要继续搜索.
		 */
		while (true) {
			SearchLevel level = levels.get(levelIndex);
			BoardColorState boardColorState = this.getGoBoard().getBoardColorState();

			log.warn("id=" + id + "; level=" + level);
			log.warn("new Round started in search" + " at previous state: ");
			this.getGoBoard().printState(log);
			if (levelIndex != 0) {
				log.warn(" with history: " + getGoBoard().getCurrent().getSingleManualStringToRoot(false));
			}

			if (level.isInitialized()) {

				/**
				 * 该层的结果已知，无需查看其他候选点。下一层的结果返回到上层的tempBestScore<br/>
				 * 此时配合处理。
				 */
				if (level.alreadyWin()) {
					log.warn(level.getWhoseTurnString() + " Already Win:");
					int score = level.getTempBestScore();

					int expectS = this.getGoBoard().boardSize * this.getGoBoard().boardSize;
					if (level.isMax()) {
						if (level.getHighestExp() == expectS) {
							BoardColorState boardColorStateN = this.getGoBoard().getBoardColorState();
							// this.stateFinalizeed(boardColorStateN,
							// level.getHighestExp());
							if (getGoBoard().getStepHistory().isDupReached(boardColorStateN) == false
									&& (getGoBoard().noStep() == true
											|| getGoBoard().getLastStep().isPass() == false)) {
								log.warn("current state is not a history dependent state");
								getGoBoard().getStepHistory().printDupState();
								// this.stateDecided(boardColorStateN,
								// level.getHighestExp());
								// better
								this.stateDecided(boardColorStateN, true, level.getTempBestScore(), true);
							}
						}
					} else { // min level
						if (level.getLowestExp() + expectS == 0) {
							BoardColorState boardColorStateN = this.getGoBoard().getBoardColorState();
							// this.stateFinalizeed(boardColorStateN,
							// level.getLowestExp());
							if (getGoBoard().getStepHistory().isDupReached(boardColorStateN) == false
									&& (getGoBoard().noStep() == true
											|| getGoBoard().getLastStep().isPass() == false)) {
								log.warn("current state is not a history dependent state");
								getGoBoard().getStepHistory().printDupState();
								// this.stateDecided(boardColorStateN,
								// level.getLowestExp());
								// better
								this.stateDecided(boardColorStateN, false, level.getTempBestScore(), true);
							}
						}
					}

					if (levelIndex == 0) {
						break;
					}
					levels.remove(levelIndex--);
					level = levels.get(levelIndex);
					level.getChildScore(score);
					getGoBoard().oneStepBackward();
					continue;
				} else if (level.hasNext() == false) { // not win yet.
					/**
					 * 是否当前层的所有的候选点都处理完了。这也意味着该层对应的状态结果已知。<br/>
					 * all candidates are handled
					 */
					log.warn(level.getWhoseTurnString() + " Already Lose after trying all candidates: ");
					log.warn(level.getAllCanPoint());
					if (levelIndex == 0) {
						if (log.isDebugEnabled()) {
							log.debug("final score at level 0: " + level.getTempBestScore());

						}
						break;
					} else {
						int score = level.getTempBestScore();
						boolean max = level.isMax();
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
						if (score == Constant.UNKOWN || level.hasUnknownChild()) {
							log.warn("score = " + score);
							level.getNode().setUnknownScore(true);
							scoreSuffix = Step.getString(process, "Unknown " + EXHAUST);
							// level.getNode().setScore(score); leave score = 0.
						}
						searchProcess.add(scoreSuffix);
						boolean hasUnknownChild = level.hasUnknownChild();

						levels.remove(levelIndex--);
						level = levels.get(levelIndex);
						if (hasUnknownChild) {
							level.setUnknownChild();
						} else {
							if (getGoBoard().getStepHistory().isDupReached(boardColorState) == false
									&& (getGoBoard().noStep() == true
											|| getGoBoard().getLastStep().isPass() == false)) {
								log.warn("current state is not a history dependent state");
								getGoBoard().getStepHistory().printDupState();
								this.stateDecided(boardColorState, max, score, false);
							}
							level.getChildScore(score);
						}
						// level.getNode()

						getGoBoard().oneStepBackward();
						continue;
					}
				} else { // on going -- Not Win, still has candidates to try.
					log.warn("Not Win yet -- Old Candidate: " + level.getAllCanPoint());
				}
			} else { // candidate is not initialized.
				int color = level.getColor();
				List<Candidate> candidates = getCandidate(color);
				level.setCandidates(candidates);
				log.warn("New Candidate Just initialized: " + level.getAllCanPoint());

				/**
				 * 不是最终状态，却又没有候选点，是很奇怪的事情，要注意处理。<br/>
				 * 当我把打劫的变化滤掉的时候，出现过这种情况。score＝0。因为终止于非确定状态。<br/>
				 * 需要区别对待，是应为劫材禁手导致没有候选点，还是本身就是终止状态而没有候选点<br/>
				 * 这可能是识别终止状态的能力不够所致。
				 * 
				 */
				if (candidates.isEmpty()) {
					if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
						log.warn("不是最终状态，却又没有候选点");
					}
					this.getGoBoard().printState();
					/**
					 * change the strategy: give up the step to decide the
					 * state.
					 */
					// has to be done in subclass, to avoid concurrent
					// modification
					// Candidate candidateP = new Candidate();
					// candidateP.setStep(new Step(null, color, goBoard
					// .getShoushu() + 1));
					// candidates.add(candidateP);

					throw new RuntimeException(color + ": no candidate");
					// + Arrays.deepToString(getGoBoard().getMatrixState()));
				}
			}

			/**
			 * goBoard is in state of level above.<br/>
			 * 现在处理候选点，展开得到下一level
			 */
			Candidate nextCandidate = level.getNextCandidate();
			Step step = nextCandidate.getStep();
			if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
				log.warn("choose step " + step);
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
				for (StepMemo stepTemp : getGoBoard().getStepHistory().getAllSteps()) {
					System.err.println(stepTemp.getStep());
					System.err.println(stepTemp.getColorState());
				}
				for (BoardColorState state : getGoBoard().getStepHistory().getColorStates()) {
					System.err.println(state);
				}
				// we already check before hand when getting candidates.
				throw new RuntimeException("duplicate state leaked" + step);
			}

			countSteps++;
			long forwardMoves = getGoBoard().getForwardMoves();
			if (countSteps != forwardMoves) {
				throw new RuntimeException("coundsteps=" + countSteps + " moves=" + forwardMoves + " " + step);
			}
			/**
			 * get latest state
			 */
			BoardColorState boardColorStateN = this.getGoBoard().getBoardColorState();

			/**
			 * 解决最终状态识别的问题.
			 * 
			 */
			if (log.isInfoEnabled()) {
				log.info("check state after step " + step);
				log.info(goBoard.getStateString());
				if (goBoard.noStep() == false) {
					log.info("Last Step at " + goBoard.getLastPoint());
				}
			}

			if (step.isPass() == false && goBoard.getColor(step.getPoint()) != step.getColor()) {
				String message = "step " + step + " does not take effect";
				log.error(message);
				throw new RuntimeException(message);
			}

			TerminalState terminateState = getTerminalState();
			if (terminateState.isTerminalState()) {
				log.warn("Teminal State");
				if (log.isInfoEnabled())
					log.info("Teminal State");
				goBoard.hasLoopInHistory();
				int scoreTerminator = terminateState.getScore();
				if (goBoard.areBothPass() == true) {
					if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
						log.warn("Both Pass");
					}
					List<Step> process = new ArrayList<Step>();
					for (StepMemo memo : this.getGoBoard().getStepHistory().getAllSteps()) {
						process.add(memo.getStep());
					}
					searchProcess.add(Step.getString(process, scoreTerminator + DB_PASS));
				} else {

					List<Step> process = new ArrayList<Step>();
					for (StepMemo memo : this.getGoBoard().getStepHistory().getAllSteps()) {
						process.add(memo.getStep());
					}
					String string = Step.getString(process, "FINAL " + scoreTerminator);
					searchProcess.add(string);
					if (log.isEnabledFor(Level.WARN)) {
						log.warn(string);
						log.warn(this.getGoBoard().getTreeGoManual().getSGFBodyString(false));
					}
				}
				level.getNeighborScore(scoreTerminator, step);

				updateTreeWhenTerminate(level, step, scoreTerminator);

				if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
					debugInvalidStep(step, scoreTerminator);
				}
				reachKnownState(scoreTerminator);
				getGoBoard().oneStepBackward();
				continue;
				// } else if (this.isKnownState(boardColorStateN)) {
				// /**
				// * we already know the result
				// */
				// int scoreTerminator = this.getScore(boardColorStateN);
				// level.getNeighborScore(scoreTerminator, step);
				// if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
				// log.warn("we already know the result after " + step);
				// // this.getGoBoard().printState();
				// log.warn(this.getGoBoard().getBoardColorState()
				// .getStateString()
				// + " the score is = " + scoreTerminator);
				// }
				//
				// /**
				// * for debugging
				// *
				// */
				// updateTreeWhenTerminate(level, step, scoreTerminator);
				// List<Step> process = new ArrayList<Step>();
				// for (StepMemo memo : this.getGoBoard().getStepHistory()
				// .getAllSteps()) {
				// process.add(memo.getStep());
				// }
				//
				// searchProcess.add(Step.getString(process, "KNOWN "
				// + scoreTerminator));
				//
				// getGoBoard().oneStepBackward();
				// continue;
			}
			if (levels.size() == depth) {

				level.setUnknownChild();
				List<Step> process = new ArrayList<Step>();
				for (StepMemo memo : this.getGoBoard().getStepHistory().getAllSteps()) {
					process.add(memo.getStep());
				}
				searchProcess.add(Step.getString(process, "Unknown FINAL"));
				updateTreeWhenUnknown(level, step);
				// cannot expand further. equivalent to ignore one candidate
				getGoBoard().oneStepBackward();
				continue;
			}

			/**
			 * continue expanding <br/>
			 * 生成新层。
			 */
			SearchLevel newLevel = buildNewLevel(level);
			updateTreeWithNewLevel(newLevel, level, step);
			levels.add(newLevel);
			if (levels.size() > depth) {
				log.error("levels >" + depth);
				break;
			} else if (this.searchProcess.size() > NUMBER_OF_VARIANT) {
				log.error("variants >" + NUMBER_OF_VARIANT);
				break;
			}
			log.warn(Constant.lineSeparator);
		} // while

		if (levelIndex <= 0) {
			/**
			 * 算完了所有变化，回到level 0，此时的结果即最佳结果。
			 * 
			 */
			if (this.getClass().getName().endsWith("ThreeThreeBoardSearch")) {
				if (log.isInfoEnabled()){
					log.info(this.getGoBoard().getInitColorState().getStateString());				
					log.info(this.getGoBoard().getBoardColorState().getStateString());
				}
			}
			GoBoard goDemo = new GoBoard(this.getGoBoard().getInitColorState());
			log.warn("Initial state");
			goDemo.printState(log);
			SearchLevel searchLevel = levels.get(levelIndex);
			if (searchLevel.alreadyWin() == false && searchLevel.hasUnknownChild()) {
				throw new RuntimeException("no enough terminal state, search Depth is not enough,");
			}
			return searchLevel.getTempBestScore();
		} else {
			log.error("levelIndex = " + levelIndex);
			log.error(this.getClass().getName() + ": cannot find result after deepth = " + levels.size());
			log.error(this.getGoBoard().getInitColorState().getStateString());
			SimpleGoManual manual = this.getGoBoard().getSimpleGoManual();
			String fileName = Constant.DEBUG_MANUAL + "globalSearch_last.sgf";
			SGFGoManual.storeGoManual(fileName, manual);

			getTreeGoManual().getExpandedString();
			manual = this.getTreeGoManual().getMostExpManual();
			fileName = Constant.DEBUG_MANUAL + "globalSearch_most_exp.sgf";
			SGFGoManual.storeGoManual(fileName, manual);

			for (StepMemo memo : this.getGoBoard().getStepHistory().getAllSteps()) {
				log.error(memo.getStep());
			}
			log.error("Final State");
			log.error(this.getGoBoard().getBoardColorState().getStateString());

			GoBoardForward forward = new GoBoardForward(this.getGoBoard().boardSize);

			return Constant.UNKOWN;
		}

	}
	
	private void reachKnownState(int scoreTerminator){
		BoardColorState boardColorStateTemp = getGoBoard().getBoardColorState();
		boardColorStateTemp.setScore(scoreTerminator);
		knownState.add(boardColorStateTemp);
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

}
