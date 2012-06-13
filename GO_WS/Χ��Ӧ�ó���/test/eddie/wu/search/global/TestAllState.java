package eddie.wu.search.global;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import util.GBKToUTF8;
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
	int count = 0;
	private static final Logger log = Logger.getLogger(GBKToUTF8.class);

	static {
		log.setLevel(Level.DEBUG);

	}

	public void test2() {
		this.test(2);

	}

	/**
	 * it takes about 1 seconds <br/>
	 * total state count = 19683 <br/>
	 * invalid state count = 7008
	 */
	public void test3() {
		this.test(3);
	}

	public void test3_singleState() {

		byte[][] state = ColorUtil.initState("B_WB_WBBW", 3);
		visit(state);
	}

	/**
	 * it may takes more than half an hour (2000 seconds).
	 */
	public void test4() {
		this.test(4);
	}

	/**
	 * refer to page 2 of TAOCP Volume 4 Fascicle 1.
	 */
	public void test(int boardSize) {
		int row = boardSize;
		int column = boardSize;
		byte[][] state = new byte[boardSize + 2][boardSize + 2];

		while (true) {
			// if(log.isDebugEnabled()) log.debug(Arrays.deepToString(state));
			visit(state);
			// visit_check(state)
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

	private void visit_check(byte[][] state) {
		count++;
		if (log.isDebugEnabled())
			log.debug("count=" + count);
		// if(count<580) return;
		if (StateUtil.isValidState(state) == false) {
			invalidState++;
			return;
		}

		StateUtil.printState(state);
	}

	private void visit(byte[][] state) {

		count++;

		if (StateUtil.isValidState(state) == false) {
			invalidState++;
			return;
		}

		if (count % 100 == 0) {
			System.out.println("count=" + count);
		}

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		if (count == 115) {
			sa.printState();
		}
		try {
			if (sa.isFinalState()) {

				FinalResult result = sa.finalResult_noCandidate();
				sa.printState();
				if (log.isDebugEnabled())
					log.debug("Final State" + result);
				if (log.isDebugEnabled())
					log.debug("count=" + count);
				finalState++;
			}
		} catch (RuntimeException e) {

			if (log.isDebugEnabled())
				log.debug("Exception for count=" + count);
			sa.printState();
			throw e;
		}

	}
}
