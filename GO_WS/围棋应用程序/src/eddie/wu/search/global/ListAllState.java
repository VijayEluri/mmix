package eddie.wu.search.global;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.ColorUtil;
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
		String key = LogManager.DEFAULT_CONFIGURATION_KEY;
		String value = "log4j_error.xml";
		System.setProperty(key, value);
		log = Logger.getLogger(ListAllState.class);
		log.setLevel(Level.WARN);

	}
	int count = 0;
	int invalidState = 0;
	int finalState = 0;

	Set<BoardColorState> validStates = new HashSet<BoardColorState>();

	private Set<BoardColorState> finalStates = new HashSet<BoardColorState>(100);
	// not statically recognized final states;
	private Set<BoardColorState> notRecFinalStates = new HashSet<BoardColorState>(
			100);

	public Set<BoardColorState> getValidState(int boardSize) {
		this.listAllState(boardSize, VALID_STATE);
		return this.validStates;
	}

	public Set<BoardColorState> getFinalState(int boardSize) {
		this.listAllState(boardSize, FINAL_STATE);
		return this.finalStates;
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
			log.warn("total state count = " + count);
			// if (type == valid_state) {
			log.warn("invalid state count = " + invalidState);
			// }
			if (type == FINAL_STATE) {
				log.warn("final State count = " + finalState);
			}
			for (BoardColorState temp : this.finalStates) {
				log.warn(temp.getStateString());
			}
			if (this.notRecFinalStates.isEmpty() == false) {
				log.warn("notRecFinalStates count = "
						+ notRecFinalStates.size());
				for (BoardColorState temp : this.notRecFinalStates) {
					log.warn(temp);
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
		if (StateUtil.isValidState(state) == false) {
			invalidState++;
			return;
		} else {
			BoardColorState blackFirst = new BoardColorState(state,
					Constant.BLACK).normalize();
			BoardColorState whiteFirst = new BoardColorState(state,
					Constant.WHITE).normalize();
			if (!validStates.contains(blackFirst)) {
				validStates.add(blackFirst);
			}
			if (!validStates.contains(whiteFirst)) {
				validStates.add(whiteFirst);
			}
		}

		// StateUtil.printState(state);
	}

	private void visit_finalCheck(byte[][] state) {

		count++;

		if (StateUtil.isValidState(state) == false) {
			invalidState++;
			return;
		}

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		try {
			BoardColorState normalize = sa.getBoardColorState().normalize();
			if (sa.isFinalState_deadCleanedUp()) {

				FinalResult result = sa.finalResult_deadCleanedUp();
				normalize.setScore(result.getScore());
				log.warn("It is final state");
				sa.printState(log);

				if (log.isDebugEnabled())
					log.debug("Final State" + result);
				if (log.isDebugEnabled())
					log.debug("count=" + count);
				finalState++;
				finalStates.add(normalize);
			} else if (sa.isFinalState_deadExist()) {

				FinalResult result = sa.finalResult_deadExist();
				normalize.setScore(result.getScore());

				log.warn("It is final state");
				sa.printState(log);

				if (log.isDebugEnabled())
					log.debug("Final State" + result);
				if (log.isDebugEnabled())
					log.debug("count=" + count);
				finalState++;
				finalStates.add(normalize);
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
