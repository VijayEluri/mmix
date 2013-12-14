package eddie.wu.search.global;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import util.GBKToUTF8;
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
public class TestAllState extends TestCase {

	public static final int valid_state = 1;
	public static final int final_state = 2;

	private static final Logger log = Logger.getLogger(GBKToUTF8.class);

	static {
		log.setLevel(Level.DEBUG);

	}
	int count = 0;

	private List<String> finalStates = new ArrayList<String>(100);
	// not statically recognized final states;
	private List<String> notRecFinalStates = new ArrayList<String>(100);

	/**
	 * total state count = 81<br/>
	 * invalid state count = 24<br/>
	 * final State count = 4<br/>
	 */
	public void test2_validState() {
		this.test(2, valid_state);
		System.out.println("pure states = "+validStates.size());
		for(BoardColorState state:validStates){
			System.out.println(state.getStateString());
		}
	}

	public void test2_finalState() {
		this.test(2, final_state);

	}

	/**
	 * it takes about 1 seconds <br/>
	 * total state count = 19683 <br/>
	 * invalid state count = 7008 <br/>
	 * valid state = 12675<br/>
	 * pure states = 2300<br/>
	 * <br/>
	 * 106 final state (type 1)<br/>
	 * it takes two hours for final state.
	 */
	public void test3_validState() {
		// log.setLevel(Level.ERROR);
		// Logger.getLogger(StateUtil.class).setLevel(Level.DEBUG);
		this.test(3, valid_state);
		System.out.println("pure states = "+validStates.size());
	}

	public void test3_finalState1() {
		// log.setLevel(Level.ERROR);
		Logger.getLogger(StateUtil.class).setLevel(Level.DEBUG);
		this.test(3, final_state);
		int i = 0;
		for (String state : finalStates) {
			log.debug("final state: " + (++i));
			log.debug(state);
		}
		i = 0;
		for (String state : notRecFinalStates) {
			log.debug("Not recognized final state: " + (++i));
			log.debug(state);
		}
	}

	public void test3_singleState() {

		byte[][] state = ColorUtil.initState("B_WB_WBBW", 3);
		visit(state, 0);
	}

	/**
	 * it may takes more than half an hour (2000 seconds).
	 */
	public void test4_validState() {
		this.test(4, valid_state);
	}

	/**
	 * refer to page 2 of TAOCP Volume 4 Fascicle 1.
	 */
	public void test(int boardSize, int type) {
		int row = boardSize;
		int column = boardSize;
		byte[][] state = new byte[boardSize + 2][boardSize + 2];

		while (true) {
			if (type == valid_state) {
				visit_check(state);

			} else if (type == final_state) {
				visit(state, 1);
			}

			if (notRecFinalStates.size() >= 1)
				return;

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

		if (log.isDebugEnabled())
			log.debug("total state count = " + count);
		if (log.isDebugEnabled())
			log.debug("invalid state count = " + invalidState);
		if (log.isDebugEnabled())
			log.debug("final State count = " + finalState);

		// TODO consider isomorphism of final state. maybe the count will
		// decrease
	}

	int invalidState = 0;
	int finalState = 0;

	Set<BoardColorState> validStates = new HashSet<BoardColorState>();

	private void visit_check(byte[][] state) {
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

		//StateUtil.printState(state);
	}

	private void visit(byte[][] state, int finalStateType) {

		count++;

		if (StateUtil.isValidState(state) == false) {
			invalidState++;
			return;
		}

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		try {
			if (sa.isFinalState(finalStateType)) {

				FinalResult result = sa.finalResult_noCandidate();
				sa.printState();
				if (log.isDebugEnabled())
					log.debug("Final State" + result);
				if (log.isDebugEnabled())
					log.debug("count=" + count);
				finalState++;
				finalStates.add(sa.getStateString().toString());
			} else if (notRecFinalStates.size() < 10
					&& sa.isPotentialFinal() == true) {
				if (TerritoryAnalysis.isFinalState_dynamic(state)) {
					notRecFinalStates.add(sa.getStateString().toString());
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
