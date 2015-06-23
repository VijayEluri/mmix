package eddie.wu.life;

import java.util.Set;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Block;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Group;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.domain.survive.RelativeResult;
import eddie.wu.domain.survive.Result;
import eddie.wu.manual.StateLoader;
import eddie.wu.util.GBKToUTF8;
import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * 试验<<围棋死活辞典>>中的题目, the first part.
 * 
 * @author Eddie
 * 
 */
public class TestRealLife extends TestCase {
	private static final Logger log = Logger.getLogger(TestRealLife.class);

	public void test34_p82() {
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
		Point point = null;

		point = Point.getPoint(7, 7, 4);
		Set<Point> set = ta.breathAfterPlay(point, Constant.BLACK);// breathAfterPlay()
		if(log.isEnabledFor(Level.WARN)) log.warn(set);
		point = Point.getPoint(7, 5, 2);
		boolean dead = ta.isAlreadyDead_dynamic(point);
		assertFalse(dead);
	}

	public void test40_p91() {
		String[] text = new String[7];
		text[0] = new String("[_, _, W, _, _, _, _]");
		text[1] = new String("[_, _, W, _, W, W, W]");
		text[2] = new String("[_, _, W, W, _, _, _]");
		text[3] = new String("[B, B, W, B, B, B, B]");
		text[4] = new String("[B, _, B, W, W, _, _]");
		text[5] = new String("[_, B, B, W, _, W, _]");
		text[6] = new String("[_, _, B, _, W, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = null;
		boolean dead;

		point = Point.getPoint(7, 5, 5);
		dead = ta.isAlreadyDead_dynamic(point);
		assertFalse(dead);
		ta.isCanLive_dynamic(point);

		point = Point.getPoint(7, 4, 4);
		Set<Point> set = ta.breathAfterPlay(point, Constant.BLACK);// breathAfterPlay()

		if(log.isEnabledFor(Level.WARN)) log.warn(set);
		dead = ta.isAlreadyDead_dynamic(point);
		assertFalse(dead);
		ta.isCanLive_dynamic(point);

		SurviveAnalysis survive = new SurviveAnalysis(state);
		Group group = new Group();
		point = Point.getPoint(7, 4, 4);
		group.addBlock(survive.getBlock(point));
		Set<Point> eyeArea2 = survive.getEyeArea2(group);
		if(log.isEnabledFor(Level.WARN)) log.warn(eyeArea2);

		boolean live = false;
		boolean targetFirst = true;
		for (Block block : group.getBlocks()) {
			point = block.getBehalfPoint();

			Block target = survive.getBlock(point);
			Logger.getLogger(SurviveAnalysis.class).setLevel(Level.WARN);
			Result result = ta.isBigEyeLive_dynamic_internal(target, eyeArea2,
					targetFirst);
			live = result.getSurvive() == RelativeResult.ALREADY_LIVE;
			if(log.isEnabledFor(Level.WARN)) log.warn("target=" + point + ", live=" + live);
			if (live)
				break;
		}
		assertTrue(live);

	}

}
