package eddie.wu.search.global;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import eddie.wu.manual.SGFGoManual;
import eddie.wu.manual.SearchNode;
import eddie.wu.manual.SimpleGoManual;
import eddie.wu.manual.TreeGoManual;

/**
 * 1. only for small board (3<=size<=?).<br/>
 * 2. no copy of board, use backward/forward feature.
 * 
 * @author Eddie
 * 
 */
public abstract class GoBoardSearch {
	private int NUMBER_OF_VARIANT = 5000*5;
	public int dupCount;

	public void setVariant(int variant) {
		NUMBER_OF_VARIANT = variant;
	}

	private static final Logger log = Logger.getLogger(GoBoardSearch.class);
	public static int deepth = 81;
	protected List<SearchLevel> levels = new ArrayList<SearchLevel>(deepth);
	/**
	 * initial state is in level 0
	 */
	private int levelIndex = 0;

	/**
	 * reuse same structure to do life-death search. first we try big eye.
	 * target is whether the complete block with big eye can survive.
	 * 
	 * @param state
	 * @param boardSize
	 */
	// public GoBoardSearch(byte[][] state) {
	// goBoard = new TerritoryAnalysis(state);
	// }

	/**
	 * during search, how many steps we forwarded.
	 */
	protected int countSteps;

	/**
	 * 将每个搜索到终点的过程记录下来.便于排错.
	 */
	private List<String> searchProcess = new ArrayList<String>();

	/**
	 * 禁止全局同型再现
	 */
	// Set<BoardColorState> knownState = new HashSet<BoardColorState>();

	/**
	 * 初始局面在level 0中，其中有所有的候选点。<br/>
	 * 每次展开某一层中的一个候选点，一般形成新的层，包括该层的候选点。同时相应的棋盘状态前进了一步。<br/>
	 * 这个不变量在每个循环的开始和结束处始终得到保持。<br/>
	 * 
	 * @return
	 */
	public int globalSearch() {
		GoBoard goBoard = this.getGoBoard();
		/**
		 * 2.开始计算。 第一层循环：展开最后一个局面。 <br/>
		 * decide to completely rewrite it.<br/>
		 * 有未确定状态才需要继续搜索.
		 */

		while (true) {// exit when we are at level 0 again;
			SearchLevel level = levels.get(levelIndex);
			BoardColorState boardColorState = this.getGoBoard()
					.getBoardColorState();

			log.warn("level=" + level);
			/**
			 * 该层的结果已知，无需查看其他候选点。下一层的结果返回到上层的tempBestScore<br/>
			 * 此时配合处理。
			 */
			if (level.alreadyWin()) {

				int score = level.getTempBestScore();
				List<Step> steps = level.getTempBestSteps();

				int expectS = this.getGoBoard().boardSize
						* this.getGoBoard().boardSize;
				if (level.getWhoseTurn() == Constant.MAX) {
					if (level.getHighestExp() == expectS) {
						BoardColorState boardColorStateN = this.getGoBoard()
								.getBoardColorState();
						// this.stateFinalizeed(boardColorStateN,
						// level.getHighestExp());
						this.stateDecided(boardColorStateN,
								level.getHighestExp());
					}
				} else {
					if (level.getLowestExp() + expectS == 0) {
						BoardColorState boardColorStateN = this.getGoBoard()
								.getBoardColorState();
						// this.stateFinalizeed(boardColorStateN,
						// level.getLowestExp());
						this.stateDecided(boardColorStateN,
								level.getHighestExp());
					}
				}

				;
				// level.getChildScore(score, steps);

				if (levelIndex == 0) {
					// if(log.isInfoEnabled())log.info("final path:");
					// for (Step step :
					// levels.get(levelIndex).getTempBestSteps()) {
					// if(log.isInfoEnabled())log.info(step);
					// }
					GoBoard goDemo = this.getGoBoard();
					log.warn("Initial state");
					goDemo.printState(log);
					for (Step step : levels.get(levelIndex).getTempBestSteps()) {

						goDemo.oneStepForward(step);
						goDemo.printState(log);
					}
					break;
					// return score;
				}
				levels.remove(levelIndex--);
				level = levels.get(levelIndex);
				level.getChildScore(score, steps);
				getGoBoard().oneStepBackward();
				continue;
			}

			Candidate nextCandidate = level.getNextCandidate();
			/**
			 * 是否当前层的所有的候选点都处理完了。这也意味着该层对应的状态结果已知。<br/>
			 * all candidates are handled
			 */
			if (nextCandidate == null) {
				if (levelIndex == 0) {
					if (log.isDebugEnabled()) {
						log.debug("final score at level 0: "
								+ level.getTempBestScore());
						log.debug("path to final score at level 0: "
								+ level.getTempBestSteps());
					}
					break;
				} else {
					int score = level.getTempBestScore();
					this.stateDecided(boardColorState, score);
					// this.results.put(boardColorState, score);
					// this.results.put(colorStateSwitch, -score);
					// if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
					// log.warn("add non-final state with score = " + score);
					// log.warn(boardColorState.getStateString());
					//
					// log.warn("add reverse non-final state with score = "
					// + (-score));
					// log.warn(colorStateSwitch.getStateString());
					// }
					List<Step> steps = level.getTempBestSteps();
					levels.remove(levelIndex--);
					level = levels.get(levelIndex);
					level.getChildScore(score, steps);

					/**
					 * for debugging
					 * 
					 */
					List<Step> process = new ArrayList<Step>();
					for (StepMemo memo : this.getGoBoard().getStepHistory()
							.getAllSteps()) {
						process.add(memo.getStep());
					}
					searchProcess.add(getString(process, score + " EXHAUST"));

					getGoBoard().oneStepBackward();
					continue;
				}
			}

			/**
			 * goBoard is in state of level above.<br/>
			 * 现在处理候选点，展开得到下一level
			 */
			Step step = nextCandidate.getStep();
			if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
				log.warn("new Round started in search" + " at previous state: ");
				this.getGoBoard().printState(log);
				log.warn(" at level: ");
				log.warn(level);
				log.warn("choose step " + step);

				List<Candidate> candidates = level.getCandidates();
				log.warn("from " + candidates.size() + " candidate: ");
				// log.info(candidates);
				for (Candidate candidate : candidates) {
					log.info(candidate.getStep());
				}

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
				/**
				 * for debugging
				 * 
				 */
				List<Step> process = new ArrayList<Step>();
				for (StepMemo memo : this.getGoBoard().getStepHistory()
						.getAllSteps()) {
					process.add(memo.getStep());
				}

				if (this.getGoBoard().noStep()) {
					process.add(step);
					searchProcess.add(getString(process, "DUPLI "));
					dupCount++;
					if (log.isEnabledFor(org.apache.log4j.Level.WARN))
						log.warn("invalid Step: have Not taken the step."
								+ step);
				} else if (step.getColor() == this.getGoBoard().getLastStep()
						.getColor()) {
					if (log.isEnabledFor(org.apache.log4j.Level.WARN))
						log.warn("invalid Step: have taken the step." + step);
					// TODO 如全局再现.已经走了.需要回退.
					searchProcess.add(getString(process, "DUPLI "));
					dupCount++;
					getGoBoard().oneStepBackward();
				} else {
					process.add(step);
					searchProcess.add(getString(process, "DUPLI "));
					dupCount++;
					if (log.isEnabledFor(org.apache.log4j.Level.WARN))
						log.warn("invalid Step: have Not taken the step."
								+ step);
				}
				// ignore invalid candidate.666
				continue;
			}

			countSteps++;
			/**
			 * get latest state
			 */
			BoardColorState boardColorStateN = this.getGoBoard()
					.getBoardColorState();

			/**
			 * 解决最终状态识别的问题.
			 * 
			 */
			if (log.isInfoEnabled())
				log.info("check state after step " + step);
			if (log.isInfoEnabled())
				log.info(goBoard.getStateString());
			if (goBoard.noStep() == false) {
				if (log.isInfoEnabled())
					log.info("Last Step at " + goBoard.getLastPoint());
			}
			if (step.isGiveUp() == false
					&& goBoard.getColor(step.getPoint()) != step.getColor()) {
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
				if (this.isDoubleGiveup() == false) {
					List<Step> process = new ArrayList<Step>();
					for (StepMemo memo : this.getGoBoard().getStepHistory()
							.getAllSteps()) {
						process.add(memo.getStep());
					}
					String string = getString(process, "FINAL "
							+ scoreTerminator);
					searchProcess.add(string);
					if (log.isEnabledFor(Level.WARN))
						log.warn(string);
					if (log.isEnabledFor(Level.WARN))
						log.warn(this.getGoBoard().getTreeGoManual()
								.getSGFBodyString());
				} else {
					if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
						log.warn("double give up");
					}
					List<Step> process = new ArrayList<Step>();
					for (StepMemo memo : this.getGoBoard().getStepHistory()
							.getAllSteps()) {
						process.add(memo.getStep());
					}
					searchProcess.add(getString(process, "DB_PASS "
							+ scoreTerminator));
				}
				level.getNeighborScore(scoreTerminator);

				updateTreeWhenTerminate(null, level, step, scoreTerminator);

				if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
					log.warn("new state after step " + step);
					log.warn(" is final state with score = " + scoreTerminator);

					GoBoard goOld = this.getGoBoard();
					goOld.printState(log);
					BoardColorState colorState = goOld.getInitColorState();
					GoBoardForward go;
					if (colorState == null) {
						go = new GoBoardForward(goOld.boardSize);
					} else {
						go = new GoBoardForward(colorState);
					}

					for (StepMemo memo : this.getGoBoard().getStepHistory()
							.getAllSteps()) {
						log.warn(memo.getStep());
						boolean validStep = go.oneStepForward(memo.getStep());
						if (validStep == false) {
							System.out
									.println("Invalid step:" + memo.getStep());
							if (log.isInfoEnabled())
								log.info("At status:");
							go.printState(log);
							throw new RuntimeException("Invalid step:");
						}
					}

					// manual check duplicate state in log.
					go = new GoBoardForward(goOld.getInitColorState());
					for (StepMemo memo : this.getGoBoard().getStepHistory()
							.getAllSteps()) {
						boolean validStep = go.oneStepForward(memo.getStep());
						go.printState(log);
					}
					List<BoardColorState> colorStates = new ArrayList<BoardColorState>();
					colorStates.addAll(go.getStepHistory().getColorStates());
					Collections.sort(colorStates, new BoardColorComparator());
					for (BoardColorState cs : colorStates) {
						if (log.isInfoEnabled())
							log.info(cs);
					}
				}

				getGoBoard().oneStepBackward();
				continue;
			} else if (this.isKnownState(boardColorStateN)) {
				/**
				 * we already know the result
				 */
				int scoreTerminator = this.getScore(boardColorStateN);
				level.getNeighborScore(scoreTerminator);
				if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
					log.warn("we already know the result after " + step);
					// this.getGoBoard().printState();
					log.warn(this.getGoBoard().getBoardColorState()
							.getStateString()
							+ " the score is = " + scoreTerminator);
				}

				/**
				 * for debugging
				 * 
				 */
				updateTreeWhenTerminate(null, level, step, scoreTerminator);
				List<Step> process = new ArrayList<Step>();
				for (StepMemo memo : this.getGoBoard().getStepHistory()
						.getAllSteps()) {
					process.add(memo.getStep());
				}

				searchProcess
						.add(getString(process, "KNOWN " + scoreTerminator));

				getGoBoard().oneStepBackward();
				continue;
			}

			/**
			 * continue expanding <br/>
			 * 生成新层的候选点。
			 */
			int color = ColorUtil.enemyColor(level.getColor());
			List<Candidate> candidatesNextLevel = getCandidate(color);

			/**
			 * 不是最终状态，却又没有候选点，是很奇怪的事情，要注意处理。<br/>
			 * 当我把打劫的变化滤掉的时候，出现过这种情况。score＝0。因为终止于非确定状态。<br/>
			 * 需要区别对待，是应为劫材禁手导致没有候选点，还是本身就是终止状态而没有候选点<br/>
			 * 这可能是识别终止状态的能力不够所致。
			 * 
			 */
			if (candidatesNextLevel.isEmpty()) {
				if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
					log.warn(ColorUtil.getColorText(color)
							+ " turn: no candidate after step " + step);
					log.warn("不是最终状态，却又没有候选点");
				}
				this.getGoBoard().printState();

				// candidate = getCandidate(color);// ?
				// FinalResult finalResult =
				// getGoBoard().finalResult_noCandidate();
				// level.getNeighborScore(getScore());

				// printState();
				// if(log.isEnabledFor(org.apache.log4j.Level.WARN))
				// log.warn("equivalent to final state");
				// level.getNeighborScore(0);
				// getGoBoard().oneStepBackward();
				//
				// continue;
				/**
				 * change the strategy: give up the step to decide the state.
				 */
				Candidate candidateP = new Candidate();
				candidateP.setStep(new Step(null, color,
						goBoard.getShoushu() + 1));
				candidatesNextLevel.add(candidateP);

				// continue;
				// throw new RuntimeException(color + ": no candidate"
				// + Arrays.deepToString(getGoBoard().getMatrixState()));
			}

			// if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
			if (log.isInfoEnabled())
				log.info(goBoard.getStateString());
			for (Candidate candidate : candidatesNextLevel) { //

				if (log.isInfoEnabled())
					log.info(candidate);
			}
			// }
			SearchLevel newLevel = buildNewLevel(level, candidatesNextLevel);

			updateTreeWhenTerminate(newLevel, level, step, Integer.MIN_VALUE);
			// if (tree()) {
			// Node node = new Node(step);
			//
			// newLevel.setNode(node);
			// if(level.getNode()==null){
			// assert level.getLevel()==0;
			// if(root==null){
			// root = node;
			// }else{
			// root.addBrother(node);
			// }
			// }else{
			// level.getNode().addChild(node);
			// }
			// }
			levels.add(newLevel);
			if (levels.size() > deepth) {
				log.error("levels >" + deepth);
				break;
			} else if (this.searchProcess.size() - dupCount > NUMBER_OF_VARIANT) {
				log.error("variants >" + NUMBER_OF_VARIANT);
				break;
			}

		} // while

		if (levelIndex <= 0) {
			/**
			 * 算完了所有变化，回到level 0，此时的结果即最佳结果。
			 * 
			 */
			if (this.getClass().getName().endsWith("ThreeThreeBoardSearch")) {
				if (log.isInfoEnabled())
					log.info(this.getGoBoard().getInitColorState()
							.getStateString());
				if (log.isInfoEnabled())
					log.info("final path:");
				for (Step step : levels.get(levelIndex).getTempBestSteps()) {
					if (log.isInfoEnabled())
						log.info(step);
				}
				if (log.isInfoEnabled())
					log.info(this.getGoBoard().getBoardColorState()
							.getStateString());
			}
			GoBoard goDemo = new GoBoard(this.getGoBoard().getInitColorState());
			log.warn("Initial state");
			goDemo.printState(log);

			bestResult = new ArrayList<Step>();
			bestResult.addAll(levels.get(levelIndex).getTempBestSteps());
			boolean valid = true;
			String initS = goDemo.getBoardColorState().toString();
			for (Step step : bestResult) {
				valid = goDemo.oneStepForward(step);
				if (valid == false) {
					if (log.isEnabledFor(Level.WARN))
						log.warn("Invalid Step" + step);
					if (log.isEnabledFor(Level.WARN))
						log.warn(initS);
					break;
				}
				goDemo.printState(log);
			}
			if (valid == false) {

				if (log.isEnabledFor(Level.WARN))
					log.warn("of All Steps:");
				for (Step step : bestResult) {
					if (log.isEnabledFor(Level.WARN))
						log.warn(step);
				}
			}

			return levels.get(levelIndex).getTempBestScore();
		} else {
			log.error("levelIndex = " + levelIndex);
			log.error(this.getClass().getName()
					+ ": cannot find result after deepth = " + levels.size());
			log.error(this.getGoBoard().getInitColorState().getStateString());
			for (Step step : levels.get(levelIndex).getTempBestSteps()) {
				log.error(step);
			}
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
			// GoBoardForward forward = new GoBoardForward()
			return Constant.UNKOWN;
		}

	}

	private void updateTreeWhenTerminate(SearchLevel newLevel,
			SearchLevel level, Step step, int scoreTerminator) {
		if (tree()) {
			SearchNode node = new SearchNode(step);
			node.setScore(scoreTerminator);
			if (level.getNode() == null) {
				assert level.getLevel() == 0;

				root.addChild(node);

			} else {
				assert level.getLevel() != 0;
				level.getNode().addChild(node);
			}
			if (newLevel != null) {
				newLevel.setNode(node);
			}
		}
	}

	abstract protected List<Candidate> getCandidate(int color);

	abstract protected TerminalState getTerminalState();

	abstract protected boolean isDoubleGiveup();

	abstract protected void printKnownResult();

	abstract protected int getLowestExp();

	abstract protected int getHighestExp();

	abstract public GoBoard getGoBoard();

	abstract void stateDecided(BoardColorState boardColorState, int score);

	public abstract void stateFinalizeed(BoardColorState boardColorStateN,
			int scoreTerminator);

	public abstract boolean isKnownState(BoardColorState boardColorState);

	public abstract int getScore(BoardColorState boardColorState);

	protected SearchLevel buildNewLevel(SearchLevel preLevel,
			List<Candidate> candidates) {
		int enemyColor = ColorUtil.enemyColor(preLevel.getColor());
		SearchLevel newLevel = new SearchLevel(++levelIndex, enemyColor);
		if (preLevel.getWhoseTurn() == Constant.MIN) {
			newLevel.setWhoseTurn(Constant.MAX);
			newLevel.setHighestExp(this.getHighestExp());
			newLevel.setTempBestScore(Integer.MIN_VALUE);
		} else if (preLevel.getWhoseTurn() == Constant.MAX) {
			newLevel.setWhoseTurn(Constant.MIN);
			newLevel.setLowestExp(this.getLowestExp());
			newLevel.setTempBestScore(Integer.MAX_VALUE);
		}
		newLevel.setCandidates(candidates, enemyColor);
		return newLevel;
	}

	public List<String> getSearchProcess() {
		return searchProcess;
	}

	List<Step> bestResult;

	public List<Step> getBestResult() {
		return bestResult;
	}

	public static String getString(List<Step> list, String score) {
		StringBuilder sb = new StringBuilder();
		sb.append("[INIT]");
		for (Step step : list) {
			if (step.isBlack()) {
				sb.append("B-->");
			} else {
				sb.append("W-->");
			}
			if (step.getPoint() == null) {
				sb.append("[PAS]");
			} else {
				sb.append(step.getPoint());
			}
		}
		sb.append("(" + score + ")");
		return sb.toString();
	}

	private SearchNode root = SearchNode.getSpecialRoot();

	public boolean tree() {
		return this.NUMBER_OF_VARIANT < 10000;
	}

	public SearchNode getRoot() {
		return root;
	}

	/**
	 * get the tree according to the search process, it is similar to the tree
	 * in the goBoard!
	 * 
	 * @return
	 */
	public TreeGoManual getTreeGoManual() {
		TreeGoManual manual = new TreeGoManual(this.getGoBoard()
				.getInitColorState());
		manual.setRoot(root);
		// root.setFather(null);
		return manual;
	}

	// public void setRoot(Node root) {
	// this.root = root;
	// }
}
