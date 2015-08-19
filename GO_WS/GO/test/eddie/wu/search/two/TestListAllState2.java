package eddie.wu.search.two;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eddie.wu.search.global.ListAllState;

public class TestListAllState2 extends TestCase {
	private static Logger log = Logger.getLogger(TestListAllState2.class);
	ListAllState las = new ListAllState();

	/**
	 * WARN (ListAllState.java:108) - total state count = 81<br/>
	 * WARN (ListAllState.java:110) - Invalid state count (whose turn
	 * independent) = 24<br/>
	 * WARN (ListAllState.java:111) - Pure invalid state count (filter by
	 * symmetry, but distinguish whore turn) = 16<br/>
	 * WARN (ListAllState.java:113) - valid state count = 57<br/>
	 * WARN (ListAllState.java:114) - valid state count (filter by symmetry, but
	 * distinguish whore turn) = 26<br/>
	 */
	public void test2_validState() {
		las.getValidState(2);

	}

	/**
	 * total state count = 81<br/>
	 * invalid state count = 24<br/>
	 * final State count = 4<br/>
	 */
	public void test2_finalState() {
		las.getFinalState(2);
	}

}
