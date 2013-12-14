package eddie.wu.search.global;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Point;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.manual.StateLoader;
import junit.framework.Assert;
import junit.framework.TestCase;

public class TestBigEyeBreath extends TestCase {

	/**
	 * 
	 */
	public void testKnifeHandlerFive_breath() {
		String[] text = new String[4];
		text[0] = new String("[B, B, B, _]");
		text[1] = new String("[B, B, _, W]");
		text[2] = new String("[B, B, _, _]");
		text[3] = new String("[B, B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Logger.getLogger(TerritoryAnalysis.class).setLevel(Level.WARN);

		Point point = Point.getPoint(4, 2, 1);
		boolean live = ta.isAlreadyLive_dynamic(point);
		Assert.assertFalse(live);

		boolean dead = ta.isAlreadyDead_dynamic(point);
		Assert.assertTrue(dead);

		boolean finalState_deadExist = ta.isFinalState_deadExist();
		Assert.assertFalse(finalState_deadExist);

	}

}
