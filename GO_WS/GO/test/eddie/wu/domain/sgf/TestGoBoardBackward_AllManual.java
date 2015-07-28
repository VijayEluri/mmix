package eddie.wu.domain.sgf;

import org.apache.log4j.Level;

public class TestGoBoardBackward_AllManual extends TestGoBoardBackward {
	/**
	 * 13s for each manual
	 */
	public void testAll_backward() {
		log.setLevel(Level.ERROR);
		for (int i = 1; i <= 10; i++) {
			log.error("i=" + i);
			_testOne_backward(i);

		}
	}

	/**
	 * 13s total
	 */
	public void testAll_backforth() {
		log.setLevel(Level.ERROR);
		for (int i = 1; i <= 263; i++) {
			log.error("i=" + i);
			_testOne_fullBackForth(i);
		}
	}
}
