package eddie.wu.search.candidate;

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

public class TestCandidate4 extends TestCase {

	public void testCandidate309() {
		String[] text = new String[4];
		text[0] = new String("[W, W, _, W]");
		text[1] = new String("[_, _, W, _]");
		text[2] = new String("[_, _, W, W]");
		text[3] = new String("[W, W, W, _]");
		// List<Candidate> candidate = testCandidate(text, Constant.BLACK);
		// assertEquals(Point.getPoint(3, 3, 3),
		// candidate.get(0).getStep()
		// .getPoint());

	}

	public List<Candidate> testCandidate(String[] text, int whoseTurn) {
		return testCandidate(text, whoseTurn, true);
	}

	private static Logger log = Logger.getLogger(TestCandidate4.class);
	static {
		log.setLevel(Level.INFO);
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

	public void testCandidate3090() {
		String[] text = new String[4];
		text[0] = new String("[_, W, _, W]");
		text[1] = new String("[W, B, W, _]");
		text[2] = new String("[B, B, W, W]");
		text[3] = new String("[_, W, W, _]");
		List<Candidate> candidate = testCandidate(text, Constant.WHITE);
		assertEquals(Point.getPoint(4, 4, 1), candidate.get(0).getStep()
				.getPoint());

	}

	/**
	 * we would like to prefer [4,2],[4,3], <br/>
	 * originally capturing means what it means, but later I realize it just
	 * mean a good position in breath race. <br/>
	 * so change the algorithm to ensure [4,2],[4,3],has capturing, but cannot
	 * ensure [4,2],[4,3] is over killing (waste a hand.) <br/>
	 * as a second step, we need to further sort all capturing move according to
	 * which is more urgent .
	 */
	public void testCandidate30935() {
		String[] text = new String[4];
		text[0] = new String("[B, _, B, _]");
		text[1] = new String("[_, B, W, _]");
		text[2] = new String("[W, B, W, _]");
		text[3] = new String("[_, _, _, _]");
		List<Candidate> candidate = testCandidate(text, Constant.BLACK);
		assertEquals(Point.getPoint(4, 4, 2), candidate.get(0).getStep()
				.getPoint());

	}

	public void testCandidate30936() {
		String[] text = new String[4];
		text[0] = new String("[_, _, B, _]");
		text[1] = new String("[W, B, W, B]");
		text[2] = new String("[W, B, W, _]");
		text[3] = new String("[_, _, _, _]");
		List<Candidate> candidate = testCandidate(text, Constant.BLACK);
		// {3,4] or [4,3]
		assertEquals(Point.getPoint(4, 3, 4), candidate.get(0).getStep()
				.getPoint());

	}

	/**
	 * 01[_, _, B, _]01<br/>
	 * 02[B, B, W, _]02<br/>
	 * 03[_, B, W, _]03<br/>
	 * 04[B, W, W, _]04<br/>
	 * B[3,1] is bad!
	 */
	public void testCandidate3091() {
		String[] text = new String[4];
		text[0] = new String("[_, _, B, _]");
		text[1] = new String("[B, B, W, _]");
		text[2] = new String("[_, B, W, _]");
		text[3] = new String("[B, W, W, _]");
		List<Candidate> candidate = testCandidate(text, Constant.BLACK);
		assertEquals(Point.getPoint(4, 1, 2), candidate.get(0).getStep()
				.getPoint());

	}

	public void testCandidate3092() {
		String[] text = new String[4];
		text[0] = new String("[_, _, B, _]");
		text[1] = new String("[B, B, W, _]");
		text[2] = new String("[_, B, W, _]");
		text[3] = new String("[B, W, W, _]");
		List<Candidate> candidate = testCandidate(text, Constant.BLACK);
		assertEquals(Point.getPoint(4, 1, 2), candidate.get(0).getStep()
				.getPoint());

	}

	/**
	 * 01[_, _, B, _]01<br/>
	 * 02[W, B, W, _]02<br/>
	 * 03[B, B, W, _]03<br/>
	 * 04[_, W, _, _]04
	 */
	public void testCandidate3096() {
		String[] text = new String[4];
		text[0] = new String("[_, _, B, _]");
		text[1] = new String("[W, B, W, _]");
		text[2] = new String("[B, B, W, _]");
		text[3] = new String("[_, W, _, _]");
		List<Candidate> candidate = testCandidate(text, Constant.BLACK);
		assertEquals(Point.getPoint(4, 1, 1), candidate.get(0).getStep()
				.getPoint());

	}

	public void testCandidate3094() {
		String[] text = new String[4];
		text[0] = new String("[_, _, B, _]");
		text[1] = new String("[B, B, W, _]");
		text[2] = new String("[B, B, W, _]");
		text[3] = new String("[B, W, W, _]");
		List<Candidate> candidate = testCandidate(text, Constant.WHITE);
		assertEquals(Point.getPoint(4, 1, 2), candidate.get(0).getStep()
				.getPoint());

	}

	public void testCandidate3097() {
		String[] text = new String[4];
		text[0] = new String("[B, _, B, _]");
		text[1] = new String("[_, B, W, B]");
		text[2] = new String("[B, B, W, W]");
		text[3] = new String("[_, W, W, _]");
		List<Candidate> candidate = testCandidate(text, Constant.WHITE);
		assertEquals(Point.getPoint(4, 1, 4), candidate.get(0).getStep()
				.getPoint());

	}

	public void testCandidate3093() {
		String[] text = new String[4];
		text[0] = new String("[B, _, B, _]");
		text[1] = new String("[_, B, W, W]");
		text[2] = new String("[B, B, W, B]");
		text[3] = new String("[B, W, W, _]");
		List<Candidate> candidate = testCandidate(text, Constant.WHITE);
		assertEquals(Point.getPoint(4, 4, 4), candidate.get(0).getStep()
				.getPoint());

	}

	public void testCandidate303() {
		String[] text = new String[4];
		text[0] = new String("[_, _, B, _]");
		text[1] = new String("[_, B, W, _]");
		text[2] = new String("[W, B, W, _]");
		text[3] = new String("[_, _, _, _]");
		List<Candidate> candidate = testCandidate(text, Constant.BLACK);
		assertEquals(Point.getPoint(4, 2, 4), candidate.get(0).getStep()
				.getPoint());

	}

	public void testCandidate304() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[_, B, W, _]");
		text[2] = new String("[_, B, W, _]");
		text[3] = new String("[_, _, _, _]");
		List<Candidate> candidate = testCandidate(text, Constant.BLACK);
		assertEquals(Point.getPoint(4, 1, 3), candidate.get(0).getStep()
				.getPoint());

	}

	public void testCandidate305() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[_, B, W, _]");
		text[2] = new String("[_, B, _, _]");
		text[3] = new String("[_, _, _, _]");
		List<Candidate> candidate = testCandidate(text, Constant.WHITE);
		assertEquals(Point.getPoint(4, 3, 3), candidate.get(0).getStep()
				.getPoint());

	}

	// public void testCandidate3051() {
	// String[] text = new String[4];
	// text[0] = new String("[_, _, _, _]");
	// text[1] = new String("[_, B, W, _]");
	// text[2] = new String("[_, B, _, _]");
	// text[3] = new String("[_, _, _, _]");
	// List<Candidate> candidate = testCandidate(text, Constant.WHITE);
	// assertEquals(Point.getPoint(4, 3, 3), candidate.get(0).getStep()
	// .getPoint());
	//
	// }

}
