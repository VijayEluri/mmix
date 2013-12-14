package eddie.wu.search.candidate;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.SmallGoBoard;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.global.Candidate;

public class TestGetCandidate extends TestCase {
	private static Logger log = Logger.getLogger(TestGetCandidate.class);
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
	
	public void testCandidate300() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[_, W, _]");
		text[2] = new String("[_, B, _]");
		List<Candidate> candidate = testCandidate(text, Constant.WHITE);
//		testCandidate(text, Constant.BLACK);
		Assert.assertEquals(Point.getPoint(3,2,3),candidate.get(0).getStep().getPoint());

	}
	
	public void testCandidate309() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[W, W, B]");
		text[2] = new String("[_, B, _]");
		List<Candidate> candidate = testCandidate(text, Constant.BLACK);
		Assert.assertEquals(Point.getPoint(3,3,3),candidate.get(0).getStep().getPoint());

	}

	public void testCandidate303() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[W, B, _]");
		text[2] = new String("[_, _, _]");
		List<Candidate> candidate = testCandidate(text, Constant.BLACK);
		Assert.assertEquals(Point.getPoint(3, 2, 3), candidate.get(0).getStep()
				.getPoint());

	}
	
	public void testCandidate304() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[_, B, _]");
		text[2] = new String("[_, _, _]");
		List<Candidate> candidate = testCandidate(text, Constant.WHITE);
		Assert.assertEquals(Point.getPoint(3, 2, 3), candidate.get(0).getStep()
				.getPoint());

	}
	
	public void testCandidate305() {
		String[] text = new String[3];
		text[0] = new String("[W, W, W]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		List<Candidate> candidate = testCandidate(text, Constant.BLACK);
		Assert.assertEquals(Point.getPoint(3, 2, 2), candidate.get(0).getStep()
				.getPoint());
		
		candidate = testCandidate(text, Constant.BLACK, false);
		Assert.assertEquals(Point.getPoint(3, 2, 2), candidate.get(0).getStep()
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
		//Assert.assertEquals(Point.getPoint(3, 2, 2), candidate.get(0).getStep());

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
		//want to avoid B[1,3]
		//but in case 301, we want b[2,2]
	}

	public void testCandidate421() {
		String[] text = new String[3];
		text[0] = new String("[B, B, _]");
		text[1] = new String("[W, W, B]");
		text[2] = new String("[W, W, _]");
		testCandidate(text, Constant.WHITE);
	}
	
	public List<Candidate> testCandidate(String[] text, int whoseTurn) {
		return testCandidate(text,whoseTurn, true);
	}

	public List<Candidate> testCandidate(String[] text, int whoseTurn, boolean filter) {
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
