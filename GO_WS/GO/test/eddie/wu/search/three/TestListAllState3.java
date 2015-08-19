package eddie.wu.search.three;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.search.global.ListAllState;

public class TestListAllState3 extends TestCase {
	private static Logger log = Logger.getLogger(TestListAllState3.class);
	ListAllState las = new ListAllState();

	/**
	 * WARN (ListAllState.java:108) - total state count (whose turn independent)
	 * = 19683<br/>
	 * WARN (ListAllState.java:110) - Invalid state count (whose turn
	 * independent) = 7008<br/>
	 * WARN (ListAllState.java:112) - Pure invalid state count (filter by
	 * symmetry) = 1038<br/>
	 */
	public void test3_validState() {
		las.getValidState(3);

	}

	/**
	 * WARN (ListAllState.java:108) - total state count (whose turn independent)
	 * = 19683<br/>
	 * WARN (ListAllState.java:125) - final State total count = 224<br/>
	 * WARN (ListAllState.java:126) - final State count of dead cleaned up = 32<br/>
	 * WARN (ListAllState.java:132) - final State count of dead exist = 192<br/>
	 */
	public void test3_finalState() {
		las.getFinalState(3);
	}

	
}
