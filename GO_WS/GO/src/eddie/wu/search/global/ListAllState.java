package eddie.wu.search.global;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.analy.FinalResult;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.domain.state.StateUtil;

/**
 * 
 * 
 * @author Eddie
 * 
 */
public class ListAllState {

	public static final int VALID_STATE = 1;
	public static final int FINAL_STATE = 2;

	private static final Logger log;

	static {
//		String key = LogManager.DEFAULT_CONFIGURATION_KEY;
//		String value = "log4j_error.xml";
//		System.setProperty(key, value);
		log = Logger.getLogger(ListAllState.class);
		log.setLevel(Level.WARN);

	}
	int count = 0;
	int invalidState = 0;
	int finalState = 0;

	Set<BoardColorState> validStates = new HashSet<BoardColorState>();
	Set<BoardColorState> invalidStates = new HashSet<BoardColorState>();
	private Set<BoardColorState> finalStateDeadCleanedup = new HashSet<BoardColorState>(
			100);
	private Set<BoardColorState> finalStateDeadExist = new HashSet<BoardColorState>(
			100);
	// not statically recognized final states;
	private Set<BoardColorState> notRecFinalStates = new HashSet<BoardColorState>(
			100);

	public Set<BoardColorState> getValidState(int boardSize) {
		this.listAllState(boardSize, VALID_STATE);
		return this.validStates;
	}

	public Set<BoardColorState> getFinalState(int boardSize) {
		this.listAllState(boardSize, FINAL_STATE);
		finalStateDeadCleanedup.addAll(finalStateDeadExist);
		return this.finalStateDeadCleanedup;
	}

	/**
	 * refer to page 2 of TAOCP Volume 4 Fascicle 1.
	 */
	public void listAllState(int boardSize, int type) {
		int row = boardSize;
		int column = boardSize;
		byte[][] state = new byte[boardSize + 2][boardSize + 2];

		while (true) {
			if (type == VALID_STATE) {
				visit_validCheck(state);

			} else if (type == FINAL_STATE) {
				visit_finalCheck(state);
			}

			// if (notRecFinalStates.size() >= 1) {
			// log.warn("There are many not recognized final states, the last one is:");
			// new GoBoard(state).printState(log);
			// return;
			// }
			// check from the last significant position.
			row = boardSize;
			column = boardSize;
			while (true) {
				if (state[row][column] == 2) {
					state[row][column] = 0;
					if (column > 1)
						column--;
					else {
						row--;
						column = boardSize;
					}
				} else {
					break;
				}
			}
			if (row == 0) {
				break;
			} else {
				state[row][column] += 1;
			}
		}

		if (log.isEnabledFor(Level.WARN)) {
			log.warn("total state count (whose turn independent) = " + count);
			if (type == VALID_STATE) {
				log.warn("Invalid state count (whose turn independent) = "
						+ invalidState);
				log.warn("Pure invalid state count (filter by symmetry) = "
						+ invalidStates.size());
				for (BoardColorState temp : this.invalidStates) {
					log.warn(temp.getStateString());
				}
				log.warn("valid state count (whose turn independent) = "
						+ (count - invalidState));
				log.warn("valid state count (filter by symmetry, but distinguish whore turn) = "
						+ validStates.size());
				for (BoardColorState temp : this.validStates) {
					log.warn(temp.getStateString());
				}
			} else if (type == FINAL_STATE) {
				log.warn("final State total count = " + finalState);
				log.warn("final State count of dead cleaned up = "
						+ finalStateDeadCleanedup.size());
				for (BoardColorState temp : this.finalStateDeadCleanedup) {
					log.warn(temp.getStateString());
					log.warn("Score = " + temp.getScore());
				}
				log.warn("final State count of dead exist = "
						+ finalStateDeadExist.size());
				for (BoardColorState temp : this.finalStateDeadExist) {
					log.warn(temp.getStateString());
					log.warn("Score = " + temp.getScore());
				}
				if (this.notRecFinalStates.isEmpty() == false) {
					log.warn("notRecFinalStates count = "
							+ notRecFinalStates.size());
					for (BoardColorState temp : this.notRecFinalStates) {
						log.warn(temp);
					}
				}
			}

		}
		// TODO consider isomorphism of final state. maybe the count will
		// decrease
	}

	private void visit_validCheck(byte[][] state) {
		count++;
		if (log.isDebugEnabled())
			log.debug("count=" + count);
		// if(count<580) return;
		try {
			BoardColorState blackFirst = new BoardColorState(state,
					Constant.BLACK).normalize();

			if (StateUtil.isValidState(state) == false) {
				if (invalidStates.contains(blackFirst) == false) {
					// needn't care whose turn since it is invalid anyway.
					invalidStates.add(blackFirst);
				}
				invalidState++;
				return;
			} else {
				if (validStates.contains(blackFirst) == false) {
					validStates.add(blackFirst);
					// white first will be converted to black first (with
					// opposite score)
					// important to switch color first.
					BoardColorState whiteFirst = new BoardColorState(state,
							Constant.WHITE).blackWhiteSwitch().normalize();
					if (validStates.contains(whiteFirst) == false) {
						validStates.add(whiteFirst);
					}
				}

			}
		} catch (Exception e) {
			log.warn(Arrays.toString(state));
		}
		// StateUtil.printState(state);
	}

	private void visit_finalCheck(byte[][] state) {

		count++;

		if (StateUtil.isValidState(state) == false) {
			invalidState++;
			return;
		}

		TerritoryAnalysis sa = new TerritoryAnalysis(state, Constant.BLACK);

		try {
			BoardColorState normalize = sa.getBoardColorState().normalize();
			if (finalStateDeadCleanedup.contains(normalize))
				return;
			else if (sa.isFinalState_deadCleanedUp()) {

				FinalResult result = sa.finalResult_deadCleanedUp();
				normalize.setScore(result.getScore());

				if (log.isDebugEnabled()) {
					log.debug("It is final state: dead stone cleaned up.");
					sa.printState(log);
					log.debug("Final State" + result);
					log.debug("count=" + count);
				}
				finalState += 2;
				finalStateDeadCleanedup.add(normalize);
				/*
				 * we also store the white first state even though the score is
				 * same. and we convert it to black first format. so low level
				 * we only store black first state, if whose turn doesn't match,
				 * we need to convert.
				 */
				BoardColorState whiteFirst = new TerritoryAnalysis(state,
						Constant.WHITE).getBoardColorState().blackWhiteSwitch()
						.normalize();
				whiteFirst.setScore(0 - result.getScore());
				finalStateDeadCleanedup.add(whiteFirst);
			} else if (finalStateDeadExist.contains(normalize)) {
				return;
			} else if (sa.isFinalState_deadExist()) {

				FinalResult result = sa.finalResult_deadExist();
				normalize.setScore(result.getScore());

				if (log.isDebugEnabled()) {
					log.warn("It is final state: dead Stone exist");
					log.warn("score=" + normalize.getScore());
					sa.printState(log);
					log.warn("");
					log.debug("Final State" + result);
					log.debug("count=" + count);
				}
				finalState += 2;
				finalStateDeadExist.add(normalize);
				BoardColorState whiteFirst = new TerritoryAnalysis(state,
						Constant.WHITE).getBoardColorState().blackWhiteSwitch()
						.normalize();
				whiteFirst.setScore(0 - result.getScore());
				finalStateDeadExist.add(whiteFirst);
			} else if (notRecFinalStates.size() < 0
					&& sa.isPotentialFinal() == true) {
				if (TerritoryAnalysis.isFinalState_dynamic(state)) {
					notRecFinalStates.add(sa.getBoardColorState());
				}

			}
		} catch (RuntimeException e) {

			if (log.isDebugEnabled())
				log.debug("Exception for count=" + count);
			sa.printState();
			throw e;
		}

	}
}
