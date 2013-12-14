package eddie.wu.search.global;

import java.util.Arrays;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Block;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.manual.StateLoader;

public class TestSpecialState3 extends TestCase {
	private static Logger log = Logger.getLogger(Block.class);
	public void testKnifeHandle5() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[_, _, B]");
		text[2] = new String("[B, B, B]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		if(log.isEnabledFor(Level.WARN)) log.warn(Arrays.deepToString(state));

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		Point point = Point.getPoint(3, 2, 3);
		Assert.assertFalse(sa.isAlreadyLive_dynamic(point));

		point = Point.getPoint(3, 1, 2);
		Assert.assertFalse(sa.isAlreadyLive_dynamic(point));

		Assert.assertFalse(sa.isFinalState_deadExist());
		// FinalResult finalResult = sa.finalResult();
		// if(log.isEnabledFor(Level.WARN)) log.warn(finalResult);
		// int score = finalResult.getScore();
		// if(log.isEnabledFor(Level.WARN)) log.warn("finalResult: score=" + score);
		// assertEquals(0, score);
	}

	public void testKnifeHandle5_2() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, _, B]");
		text[2] = new String("[B, B, B]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		if(log.isEnabledFor(Level.WARN)) log.warn(Arrays.deepToString(state));

		TerritoryAnalysis sa = new TerritoryAnalysis(state);

		Point point = Point.getPoint(3, 3, 1);
		Assert.assertFalse(sa.isAlreadyLive_dynamic(point));

		Assert.assertFalse(sa.isFinalState_deadExist());
	}

}
