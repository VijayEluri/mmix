package eddie.wu.search.smallboard;

import java.util.Arrays;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.manual.StateLoader;

public class TestEyeInSmallBoard extends TestCase {
	private static final Logger log = Logger
			.getLogger(TestEyeInSmallBoard.class);
	static {
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.INFO);
		;
	}

	/**
	 * 
	 */
	public void testThree() {
		String[] text = new String[4];
		text[0] = new String("[B, _, B, _]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[W, W, W, W]");
		text[3] = new String("[_, W, _, B]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));

		SurviveAnalysis analysis = new SurviveAnalysis(state);
		assertTrue(analysis.isAlreadyLive_dynamic(Point.getPoint(4, 2, 2)));
		assertTrue(analysis.isAlreadyLive_dynamic(Point.getPoint(4, 3, 3)));
		SurviveAnalysis analysis2 = new SurviveAnalysis(state);
		analysis2.printState();
		assertTrue(analysis2.isStaticLive(Point.getPoint(4, 2, 2)));
		assertTrue(analysis2.isStaticLive(Point.getPoint(4, 3, 3)));

	}

	public void testNew() {
		String[] text = new String[4];
		text[0] = new String("[B, _, B, _]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[W, W, W, W]");
		text[3] = new String("[_, W, _, B]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SurviveAnalysis analysis2 = new SurviveAnalysis(state);
		analysis2.printState();
		assertTrue(analysis2.isStaticLive(Point.getPoint(4, 2, 2)));
		assertTrue(analysis2.isStaticLive(Point.getPoint(4, 3, 3)));

	}

	public void testNew2() {
		String[] text = new String[4];
		text[0] = new String("[B, B, B, _]");
		text[1] = new String("[W, W, B, B]");
		text[2] = new String("[W, _, W, B]");
		text[3] = new String("[_, W, W, B]");

		byte[][] state = StateLoader.LoadStateFromText(text);

		SurviveAnalysis analysis2 = new SurviveAnalysis(state);
		analysis2.printState();
		assertTrue(analysis2.isStaticLive(Point.getPoint(4, 2, 2)));
		assertFalse(analysis2.isStaticLive(Point.getPoint(4, 1, 1)));

	}

	public void testNew21() {
		String[] text = new String[4];
		text[0] = new String("[B, B, B, _]");
		text[1] = new String("[W, W, B, B]");
		text[2] = new String("[W, _, W, B]");
		text[3] = new String("[_, W, B, B]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SurviveAnalysis analysis2 = new SurviveAnalysis(state);
		analysis2.printState();
		assertFalse(analysis2.isStaticLive(Point.getPoint(4, 2, 2)));
		assertFalse(analysis2.isStaticLive(Point.getPoint(4, 1, 1)));

	}

	public void testNew22() {
		String[] text = new String[4];
		text[0] = new String("[B, _, B, _]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[W, W, W, B]");
		text[3] = new String("[_, W, _, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(Level.WARN))
			log.warn(Arrays.deepToString(state));
		SurviveAnalysis analysis2 = new SurviveAnalysis(state);
		analysis2.printState();
		assertFalse(analysis2.isStaticLive(Point.getPoint(4, 3, 3)));

	}
}
