package eddie.wu.search.global;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Point;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.manual.StateLoader;

public class TestBigEyeLiveBreath extends TestCase {

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
		assertFalse(live);

		boolean dead = ta.isAlreadyDead_dynamic(point);
		assertTrue(dead);

		// we use static judgment in method _deadExist
		boolean finalState_deadExist = ta.isFinalState_deadExist();
		assertFalse(finalState_deadExist);

		point = Point.getPoint(4, 2, 4);
		live = ta.isAlreadyLive_dynamic(point);
		assertTrue(live);

	}

}
