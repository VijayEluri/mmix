package eddie.wu.domain.analy;

import java.util.Arrays;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import util.GBKToUTF8;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.manual.StateLoader;

public class TestEyeInSmallBoard extends TestCase {
	private static final Logger log = Logger.getLogger(GBKToUTF8.class);
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
		if(log.isEnabledFor(Level.WARN)) log.warn(Arrays.deepToString(state));

		SurviveAnalysis analysis = new SurviveAnalysis(state);
		assertTrue(analysis.isAlreadyLive_dynamic(Point.getPoint(4, 2, 2)));
		assertTrue(analysis.isAlreadyLive_dynamic(Point.getPoint(4, 3, 3)));
	}

	public void testNew() {
		String[] text = new String[4];
		text[0] = new String("[B, _, B, _]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[W, W, W, W]");
		text[3] = new String("[_, W, _, B]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		GoBoard analysis = new GoBoard(state);
		analysis.printState();
		assertTrue(analysis.getBlock(Point.getPoint(4, 2, 2))
				.isAlreadyLive_Static());
		assertTrue(analysis.getBlock(Point.getPoint(4, 3, 3))
				.isAlreadyLive_Static());

	}
	
	public void testNew2() {
		String[] text = new String[4];
		text[0] = new String("[B, B, B, _]");
		text[1] = new String("[W, W, B, B]");
		text[2] = new String("[W, _, W, B]");
		text[3] = new String("[_, W, W, B]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		GoBoard analysis = new GoBoard(state);
		analysis.printState();
		assertTrue(analysis.getBlock(Point.getPoint(4, 2, 2))
				.isAlreadyLive_Static());
		assertFalse(analysis.getBlock(Point.getPoint(4, 1, 1))
				.isAlreadyLive_Static());

	}
	
	public void testNew21() {
		String[] text = new String[4];
		text[0] = new String("[B, B, B, _]");
		text[1] = new String("[W, W, B, B]");
		text[2] = new String("[W, _, W, B]");
		text[3] = new String("[_, W, B, B]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		GoBoard analysis = new GoBoard(state);
		analysis.printState();
		assertFalse(analysis.getBlock(Point.getPoint(4, 2, 2))
				.isAlreadyLive_Static());
		assertFalse(analysis.getBlock(Point.getPoint(4, 1, 1))
				.isAlreadyLive_Static());

	}
	
	public void testNew22() {
		String[] text = new String[4];
		text[0] = new String("[B, _, B, _]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[W, W, W, B]");
		text[3] = new String("[_, W, _, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		if(log.isEnabledFor(Level.WARN)) log.warn(Arrays.deepToString(state));

		GoBoard analysis = new GoBoard(state);
		analysis.printState();		
		assertFalse(analysis.getBlock(Point.getPoint(4, 3, 3))
				.isAlreadyLive_Static());
	}
}
