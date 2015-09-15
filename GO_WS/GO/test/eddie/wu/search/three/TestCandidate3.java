package eddie.wu.search.three;

import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.SmallGoBoard;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.global.Candidate;

public class TestCandidate3 extends TestCase {
	private static Logger log = Logger.getLogger(TestCandidate3.class);
	static {
		log.setLevel(Level.INFO);
	}

	public void testCandidate3() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[W, B, B]");
		text[2] = new String("[_, _, _]");
		testCandidate(text, Constant.WHITE);
		testCandidate(text, Constant.BLACK);

	}
	
	public void testCandidate3232() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, B, W]");
		testCandidate(text, Constant.WHITE);
		testCandidate(text, Constant.BLACK);

	}
	
	//WARN  (TestAllState3.java:575) - [INIT]W-->[1,2]B-->[2,2]W-->[2,3]B-->[1,3]W-->[2,1]B-->[3,1](-6 EXHAUST)
	public void testCandidate3001() {
		String[] text = new String[3];
		text[0] = new String("[B, _, B]");
		text[1] = new String("[W, B, W]");
		text[2] = new String("[_, _, _]");		
		testCandidate(text, Constant.BLACK);

	}
	
	public void testCandidate3002() {
		String[] text = new String[3];
		text[0] = new String("[_, W, B]");
		text[1] = new String("[W, _, B]");
		text[2] = new String("[_, W, W]");		
		testCandidate(text, Constant.WHITE);

	}
	
	
	public void testCandidate3009() {
		String[] text = new String[3];
		text[0] = new String("[B, _, B");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, _, _]");
		testCandidate(text, Constant.BLACK);

	}

	public void testCandidate300() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[_, W, _]");
		text[2] = new String("[_, B, _]");
		List<Candidate> candidate = testCandidate(text, Constant.WHITE);
		// testCandidate(text, Constant.BLACK);
		assertEquals(Point.getPoint(3, 2, 3), candidate.get(0).getStep()
				.getPoint());

	}

	public void testCandidate309() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[W, W, B]");
		text[2] = new String("[_, B, _]");
		List<Candidate> candidate = testCandidate(text, Constant.BLACK);
//		assertEquals(Point.getPoint(3, 3, 3), candidate.get(0).getStep()
//				.getPoint());
		//pass is also perfect candidate!
		assertEquals(Point.giveUp, candidate.get(0).getStep()
		.getPoint());
	}

	public void testCandidate303() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[W, B, _]");
		text[2] = new String("[_, _, _]");
		List<Candidate> candidate = testCandidate(text, Constant.BLACK);
		assertEquals(Point.getPoint(3, 2, 3), candidate.get(0).getStep()
				.getPoint());

	}

	public void testCandidate304() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[_, B, _]");
		text[2] = new String("[_, _, _]");
		List<Candidate> candidate = testCandidate(text, Constant.WHITE);
		assertEquals(Point.getPoint(3, 2, 3), candidate.get(0).getStep()
				.getPoint());

	}

	public void testCandidate305() {
		String[] text = new String[3];
		text[0] = new String("[W, W, W]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		List<Candidate> candidate = testCandidate(text, Constant.BLACK);
		assertEquals(Point.getPoint(3, 2, 2), candidate.get(0).getStep()
				.getPoint());

		candidate = testCandidate(text, Constant.BLACK, false);
		assertEquals(Point.getPoint(3, 2, 2), candidate.get(0).getStep()
				.getPoint());

	}

	public void testCandidate30() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[W, B, B]");
		text[2] = new String("[_, W, _]");
		testCandidate(text, Constant.WHITE);
		testCandidate(text, Constant.BLACK);

	}

	public void testCandidate302() {
		String[] text = new String[3];
		text[0] = new String("[_, B, _]");
		text[1] = new String("[W, _, _]");
		text[2] = new String("[W, W, _]");
		testCandidate(text, Constant.WHITE);
		testCandidate(text, Constant.BLACK);

	}

	public void testCandidate301() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[_, _, B]");
		text[2] = new String("[B, B, B]");
		testCandidate(text, Constant.WHITE);
		List<Candidate> candidate = testCandidate(text, Constant.BLACK);

	}

	/**
	 * ##01,02,03 <br/>
	 * 01[_, B, W]01<br/>
	 * 02[W, W, _]02<br/>
	 * 03[_, B, W]03<br/>
	 * ##01,02,03 <br/>
	 */
	public void testCandidate306() {
		String[] text = new String[3];
		text[0] = new String("[_, B, W]");
		text[1] = new String("[W, W, _]");
		text[2] = new String("[_, B, W]");
		List<Candidate> candidate = null;
		candidate = testCandidate(text, Constant.WHITE);
		//eating is also good candidate!
		assertEquals(Point.getPoint(3, 1, 1), candidate.get(0).getStep()
				.getPoint());
//		assertEquals(Point.getPoint(3, 2, 3), candidate.get(0).getStep()
//				.getPoint());
		candidate = testCandidate(text, Constant.BLACK);

		assertEquals(Point.getPoint(3, 2, 3), candidate.get(0).getStep()
				.getPoint());
	}

	// WARN (TestAllState3.java:72) -
	// [INIT]B-->[1,2]W-->[PAS]B-->[1,3]W-->[3,3]B-->[PAS](2 EXHAUST)
	public void testCandidate33() {
		String[] text = new String[3];
		text[0] = new String("[_, B, B]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, W]");
		// testCandidate(text,Constant.WHITE);
		testCandidate(text, Constant.BLACK);

	}

	public void testCandidate31() {
		String[] text = new String[3];
		text[0] = new String("[_, W, W]");
		text[1] = new String("[W, B, B]");
		text[2] = new String("[_, W, _]");
		testCandidate(text, Constant.WHITE);
	}

	public void testCandidate32() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, _]");
		testCandidate(text, Constant.BLACK);
	}

	public void testCandidate34() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, _]");
		testCandidate(text, Constant.WHITE);
	}

	public void testCandidate35() {
		String[] text = new String[3];
		text[0] = new String("[_, W, W]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, _]");
		testCandidate(text, Constant.WHITE);
	}

	public void testCandidate36() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[B, B, _]");
		text[2] = new String("[B, _, _]");
		testCandidate(text, Constant.BLACK);
	}

	public void testCandidate37() {
		String[] text = new String[3];
		text[0] = new String("[W, W, W]");
		text[1] = new String("[W, B, _]");
		text[2] = new String("[_, _, _]");
		testCandidate(text, Constant.BLACK);
	}

	public void testCandidate38() {
		String[] text = new String[3];
		text[0] = new String("[W, _, _]");
		text[1] = new String("[_, W, _]");
		text[2] = new String("[_, B, _]");
		testCandidate(text, Constant.BLACK);
	}

	public void testCandidate39() {
		String[] text = new String[3];
		text[0] = new String("[_, _, W]");
		text[1] = new String("[_, W, B]");
		text[2] = new String("[_, B, B]");
		testCandidate(text, Constant.BLACK);
	}

	public void testCandidate40() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[B, W, _]");
		text[2] = new String("[_, B, _]");
		testCandidate(text, Constant.WHITE);
	}

	public void testCandidate41() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, W, B]");
		text[2] = new String("[_, B, B]");
		testCandidate(text, Constant.WHITE);
	}

	public void testCandidate42() {
		String[] text = new String[3];
		text[0] = new String("[W, _, _]");
		text[1] = new String("[_, W, B]");
		text[2] = new String("[_, B, B]");
		testCandidate(text, Constant.WHITE);
		// want to avoid B[1,3]
		// but in case 301, we want b[2,2]
	}

	public void testCandidate421() {
		String[] text = new String[3];
		text[0] = new String("[B, B, _]");
		text[1] = new String("[W, W, B]");
		text[2] = new String("[W, W, _]");
		testCandidate(text, Constant.WHITE);
	}

	public void testCandidate422() {
		String[] text = new String[3];
		text[0] = new String("[B, B, _]");
		text[1] = new String("[B, _, _]");
		text[2] = new String("[_, W, _]");
		testCandidate(text, Constant.WHITE);
	}
	
	public void testCandidate4231() {
		String[] text = new String[3];
		text[0] = new String("[_, B, _]");
		text[1] = new String("[B, W, B]");
		text[2] = new String("[_, W, _]");
		testCandidate(text, Constant.BLACK);
	}

	/**
	 * 01[W, W, _]01<br/>
	 * 02[B, B, W]02<br/>
	 * 03[_, _, _]03<br/>
	 * ##01,02,03 whoseTurn=Black
	 * 
	 * @param text
	 * @param whoseTurn
	 * @return
	 */
	public void testCandidate423() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, _, _]");
		testCandidate(text, Constant.BLACK);
	}

	public List<Candidate> testCandidate(String[] text, int whoseTurn) {
		return testCandidate(text, whoseTurn, true);
	}

	public List<Candidate> testCandidate(String[] text, int whoseTurn,
			boolean filter) {
		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard sa = new SmallGoBoard(BoardColorState.getInstance(state,
				whoseTurn));
		List<Candidate> candidates = sa.getCandidate(whoseTurn, filter);
		if (log.isEnabledFor(Level.WARN)) {
			log.warn(sa.getBoardColorState().getStateString());
			for (Candidate candidate : candidates) {
				log.warn(candidate);
			}
		}
		return candidates;
	}

}
