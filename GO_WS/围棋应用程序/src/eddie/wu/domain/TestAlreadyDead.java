package eddie.wu.domain;

import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import util.GBKToUTF8;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.manual.StateLoader;

public class TestAlreadyDead extends TestCase {
	private static final Logger log = Logger.getLogger(GBKToUTF8.class);
	// [INIT]B-->[6,3]W-->[7,5]B-->[7,6]W-->[6,5](FINAL -128)
	public void testRealLifeProblem_20131392_6D() {
		String[] text = new String[7];
		text[0] = new String("[_, _, W, _, _, _, _]");
		text[1] = new String("[_, _, W, _, _, _, _]");
		text[2] = new String("[_, _, W, _, _, _, _]");
		text[3] = new String("[W, W, W, W, W, W, _]");
		text[4] = new String("[W, B, B, W, B, _, _]");
		text[5] = new String("[W, B, B, B, W, B, B]");
		text[6] = new String("[W, W, B, _, W, B, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point =null;
		
		point = Point.getPoint(7, 7, 4);
		Set<Point> set = ta.breathAfterPlay(point, Constant.BLACK);//breathAfterPlay()
		if(log.isEnabledFor(Level.WARN)) log.warn(set);
		point= Point.getPoint(7, 5, 2);
		boolean dead = ta.isAlreadyDead_dynamic(point);
		assertFalse(dead);
	}
}
