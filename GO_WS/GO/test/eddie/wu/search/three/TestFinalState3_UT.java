package eddie.wu.search.three;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Test;

import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.SmallGoBoard;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.manual.StateLoader;

public class TestFinalState3_UT extends TestCase {

	@Test
	public void test() {
		Logger.getLogger(TerritoryAnalysis.class).setLevel(Level.INFO);;
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[B, _, _]");
		text[2] = new String("[_, B, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard sa = new SmallGoBoard(state, Constant.WHITE);
		//Point point = Point.getPoint(3, 2, 1);
		assertFalse(sa.isFinalState_deadCleanedUp());
		assertTrue(sa.isFinalState_deadExist());
		assertEquals(3,sa.finalResult_deadExist());
	}

}
