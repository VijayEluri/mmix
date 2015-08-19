package eddie.wu.search.global;

import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Block;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.state.StateUtil;
import junit.framework.TestCase;

public class TestListAllState extends TestCase {
	private static Logger log = Logger.getLogger(TestListAllState.class);
	ListAllState las = new ListAllState();

	/**
	 * total state count = 81<br/>
	 * invalid state count = 24<br/>
	 * final State count = 4<br/>
	 */
	public void test2_validState() {
		Set<BoardColorState> validStates = las.getValidState(2);
		if (log.isEnabledFor(Level.WARN)) {
			log.warn("pure states = " + validStates.size());
		}
		int count = 0;
		for (BoardColorState state : validStates) {
			if (state.isBlackTurn()) {
				count++;
				if (log.isEnabledFor(Level.WARN))
					log.warn("count = " + count);
				if (log.isEnabledFor(Level.WARN))
					log.warn(state.getStateString());
			}
		}

	}

	public void test2_finalState() {
		Set<BoardColorState> validStates = las.getFinalState(2);
		if (log.isEnabledFor(Level.WARN))
			log.warn("pure final states = " + validStates.size());
		int count = 0;
		for (BoardColorState state : validStates) {
			if (state.isBlackTurn()) {
				count++;
				System.out.print("count = " + count);
				System.out.print(", score = " + state.getScore());
				if (log.isEnabledFor(Level.WARN))
					log.warn(state.getStateString());
			}
		}
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
		Set<BoardColorState> validStates = las.getValidState(3);
		if (log.isEnabledFor(Level.WARN))
			log.warn("pure valid states = " + validStates.size());
		int count = 0;
//		for (BoardColorState state : validStates) {
//			if (state.isBlackTurn()) {
//				count++;
//				System.out.println("count = " + count);
//				if (log.isEnabledFor(Level.WARN)) {
//					log.warn(state.getStateString());
//				}
//			}
//		}
	}

	/**
	 * total state count = 19683 <br/>
	 * invalid state count = 7008 <br/>
	 * final State count = 98
	 */
	public void test3_finalState1() {
		las.getFinalState(3);
//		Set<BoardColorState> validStates = las.getFinalState(3);
//		if (log.isEnabledFor(Level.WARN)) {
//			log.warn("pure final states = " + validStates.size());
//		}
//		int count = 0;
//		for (BoardColorState state : validStates) {
//			if (state.isBlackTurn()) {
//				count++;
//
//				if (log.isEnabledFor(Level.WARN)) {
//					log.warn("count = " + count);
//					log.warn(", score = " + state.getScore());
//					log.warn(state.getStateString());
//				}
//			}
//		}
		// i = 0;
		// for (BoardColorState state : notRecFinalStates) {
		// log.debug("Not recognized final state: " + (++i));
		// log.debug(state);
		// }
	}

	// public void test3_singleState() {
	//
	// byte[][] state = ColorUtil.initState("B_WB_WBBW", 3);
	// visit_finalCheck(state);
	// }
	//
	// /**
	// * it may takes more than half an hour (2000 seconds).
	// */
	// public void test4_validState() {
	// this.listAllState(4, VALID_STATE);
	// }

	public void testInitState() {
		byte[][] state = new byte[5][5];
		//try {
			BoardColorState blackFirst = new BoardColorState(state,
					Constant.BLACK).normalize();
			log.warn(blackFirst.toString());
//		} catch (Exception e) {
//			this.fail();
//		}
	}

}
