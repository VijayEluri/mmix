package eddie.wu.search.global;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BlankBlock;
import eddie.wu.domain.Block;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.domain.survive.BreathPattern;
import eddie.wu.domain.survive.RelativeResult;
import eddie.wu.domain.survive.RelativeSurviveResult;
import eddie.wu.domain.survive.Result;
import eddie.wu.manual.StateLoader;

public class TestFiveFive extends TestCase {
	private static Logger log = Logger.getLogger(Block.class);
	/**
	 * ##01,02,03,04 <br/>
	 * 01[_, _, _, W]01<br/>
	 * 02[B, B, B, B]02<br/>
	 * 03[W, W, W, B]03<br/>
	 * 04[_, W, _, W]04<br/>
	 * ##01,02,03,04 <br/>
	 * 
	 */
	public void test1() {

		String[] text = new String[5];
		text[0] = new String("[_, W, _, W, _]");
		text[1] = new String("[W, W, W, W, W]");
		text[2] = new String("[B, B, B, B, B]");
		text[3] = new String("[_, W, W, _, B]");
		text[4] = new String("[_, _, _, _, B]");

	}

	public void test2() {

		String[] text = new String[5];
		text[0] = new String("[_, W, _, W, W]");
		text[1] = new String("[W, W, W, W, _]");
		text[2] = new String("[B, B, B, B, B]");
		text[3] = new String("[_, W, W, _, B]");
		text[4] = new String("[_, _, _, _, B]");

	}

	public void test3() {

		String[] text = new String[6];
		text[0] = new String("[_, W, _, W, _, W]");
		text[1] = new String("[W, W, W, W, W, W]");
		text[2] = new String("[_, W, _, W, _, W]");
		text[3] = new String("[B, B, B, B, B, W]");
		text[4] = new String("[_, W, W, _, B, W]");
		text[5] = new String("[_, _, _, _, B, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 4, 1);
		Block target = ta.getBlock(point);
		Point point2 = Point.getPoint(6, 5, 1);
		BlankBlock eyeBlock = ta.getBlankBlock(point2);
		ta.isBigEyeLive_dynamic(target, eyeBlock, false);

	}

	public void test31() {

		String[] text = new String[6];
		text[0] = new String("[_, W, _, W, _, W]");
		text[1] = new String("[W, W, W, W, W, W]");
		text[2] = new String("[_, W, _, W, _, W]");
		text[3] = new String("[B, B, B, B, B, W]");
		text[4] = new String("[B, W, W, W, B, W]");
		text[5] = new String("[_, _, _, _, B, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 4, 1);
		Block target = ta.getBlock(point);
		Point point2 = Point.getPoint(6, 6, 1);
		BlankBlock eyeBlock = ta.getBlankBlock(point2);
		ta.isBigEyeLive_dynamic(target, eyeBlock, false);

	}

	public void test32() {

		String[] text = new String[6];
		text[0] = new String("[_, W, _, W, _, W]");
		text[1] = new String("[W, W, W, W, W, W]");
		text[2] = new String("[_, W, _, W, _, W]");
		text[3] = new String("[B, B, B, B, B, W]");
		text[4] = new String("[W, W, W, _, B, W]");
		text[5] = new String("[_, _, _, _, B, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 4, 1);
		Block target = ta.getBlock(point);
		Point point2 = Point.getPoint(6, 6, 1);
		BlankBlock eyeBlock = ta.getBlankBlock(point2);
		ta.isBigEyeLive_dynamic(target, eyeBlock, false);

	}

	public void test33() {

		String[] text = new String[6];
		text[0] = new String("[_, W, _, W, _, W]");
		text[1] = new String("[W, W, W, W, W, W]");
		text[2] = new String("[_, W, _, W, _, W]");
		text[3] = new String("[B, B, B, B, B, W]");
		text[4] = new String("[_, W, W, _, B, W]");
		text[5] = new String("[_, W, _, _, B, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 4, 1);
		Block target = ta.getBlock(point);
		Point point2 = Point.getPoint(6, 6, 1);
		BlankBlock eyeBlock = ta.getBlankBlock(point2);
		ta.isBigEyeLive_dynamic(target, eyeBlock, false);

	}

	public void test34() {

		String[] text = new String[6];
		text[0] = new String("[_, W, _, W, _, W]");
		text[1] = new String("[W, W, W, W, W, W]");
		text[2] = new String("[_, W, _, W, _, W]");
		text[3] = new String("[B, B, B, B, B, W]");
		text[4] = new String("[_, W, W, _, B, W]");
		text[5] = new String("[_, _, W, _, B, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 4, 1);
		Block target = ta.getBlock(point);
		Point point2 = Point.getPoint(6, 6, 1);
		BlankBlock eyeBlock = ta.getBlankBlock(point2);
		ta.isBigEyeLive_dynamic(target, eyeBlock, false);

	}

	public void test35() {

		String[] text = new String[6];
		text[0] = new String("[_, W, _, W, _, W]");
		text[1] = new String("[W, W, W, W, W, W]");
		text[2] = new String("[_, W, _, W, _, W]");
		text[3] = new String("[B, B, B, B, B, W]");
		text[4] = new String("[_, W, W, _, B, W]");
		text[5] = new String("[W, _, _, _, B, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 4, 1);
		Block target = ta.getBlock(point);
		Point point2 = Point.getPoint(6, 6, 2);
		BlankBlock eyeBlock = ta.getBlankBlock(point2);
		boolean live = ta.isBigEyeLive_dynamic(target, eyeBlock, true);
		Assert.assertTrue(live);

	}

	public void test36() {

		String[] text = new String[6];
		text[0] = new String("[_, W, _, W, _, W]");
		text[1] = new String("[W, W, W, W, W, W]");
		text[2] = new String("[_, W, _, W, _, W]");
		text[3] = new String("[B, B, B, B, B, W]");
		text[4] = new String("[_, W, W, _, B, W]");
		text[5] = new String("[_, _, _, W, B, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 4, 1);
		Block target = ta.getBlock(point);
		Point point2 = Point.getPoint(6, 6, 1);
		BlankBlock eyeBlock = ta.getBlankBlock(point2);
		boolean live = ta.isBigEyeLive_dynamic(target, eyeBlock, false);
		Assert.assertTrue(live);

	}

	public void test341() {
		// [INIT]W-->[6,1]B-->[6,4](128 EXHAUST)
		String[] text = new String[6];
		text[0] = new String("[_, W, _, W, _, W]");
		text[1] = new String("[W, W, W, W, W, W]");
		text[2] = new String("[_, W, _, W, _, W]");
		text[3] = new String("[B, B, B, B, B, W]");
		text[4] = new String("[_, W, W, _, B, W]");
		text[5] = new String("[W, _, W, B, B, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 4, 1);
		Block target = ta.getBlock(point);
		Point point2 = Point.getPoint(6, 6, 2);
		BlankBlock eyeBlock = ta.getBlankBlock(point2);
		ta.isBigEyeLive_dynamic(target, eyeBlock, false);

	}

	public void test3411() {
		// [INIT]W-->[6,1]B-->[6,4](128 EXHAUST)
		String[] text = new String[6];
		text[0] = new String("[_, W, _, W, _, W]");
		text[1] = new String("[W, W, W, W, W, W]");
		text[2] = new String("[_, W, _, W, _, W]");
		text[3] = new String("[B, B, B, B, B, W]");
		text[4] = new String("[_, W, W, _, B, W]");
		text[5] = new String("[W, W, W, B, B, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 4, 1);
		Block target = ta.getBlock(point);
		Point point2 = Point.getPoint(6, 5, 1);
		BlankBlock eyeBlock = ta.getBlankBlock(point2);
		boolean live;
		live = ta.isBigEyeLive_dynamic(target, eyeBlock, false);
		Assert.assertFalse(live);

		/**
		 * if big eye is not live yet, we check further if it can survive if
		 * target play first.
		 */
		live = ta.isBigEyeLive_dynamic(target, eyeBlock, true);
		Assert.assertFalse(live);

		/**
		 * it is dead and only 7 breath.
		 */

	}

	/**
	 * If Black plays first, he should reach knife handler five, because then it
	 * has 9 breath. otherwise, if may only have 7 breath once external breath
	 * is closed.
	 */
	public void test34121() {
		// [INIT]W-->[6,1]B-->[6,4](128 EXHAUST)
		String[] text = new String[6];
		text[0] = new String("[_, W, _, W, _, W]");
		text[1] = new String("[W, W, W, W, W, W]");
		text[2] = new String("[_, W, _, W, _, W]");
		text[3] = new String("[B, B, B, B, B, W]");
		text[4] = new String("[_, W, W, _, B, W]");
		text[5] = new String("[W, W, W, B, B, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 4, 1);
		Block target = ta.getBlock(point);
		Point point2 = Point.getPoint(6, 5, 1);
		BlankBlock blankBlock = ta.getBlankBlock(point2);
		Set<Point> points = ta.getEyePoints_singleTargetBlock(target, blankBlock);
		if(log.isEnabledFor(Level.WARN)) log.warn(points);
		BreathPattern pattern = ta.getBreathPattern(points);
		if(log.isEnabledFor(Level.WARN)) log.warn(pattern.toString());
	}

	/**
	 * if target block is dead. it means the target cannot make two eyes and it
	 * is surrounded by strong enemy block so it also fail in breath race.
	 */
	public void test3A() {
		// [INIT]W-->[5,4]B-->[5,1]W-->[6,2]B-->[6,4]W-->[6,3]B-->[6,1]
		// W-->[5,3]B-->[6,3]W-->[5,2]B-->[6,2]W-->[3,5]B-->[5,4]W-->[5,3]B-->[5,2]W-->[3,5](DUPLI
		// )
		String[] text = new String[6];
		text[0] = new String("[_, W, _, W, _, W]");
		text[1] = new String("[W, W, W, W, W, W]");
		text[2] = new String("[_, W, _, W, _, W]");
		text[3] = new String("[B, B, B, B, B, W]");
		text[4] = new String("[B, _, _, _, B, W]");
		text[5] = new String("[B, _, _, B, B, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 4, 1);
		Block target = ta.getBlock(point);
		Point point2 = Point.getPoint(6, 5, 2);
		BlankBlock eyeBlock = ta.getBlankBlock(point2);
		ta.isBigEyeLive_dynamic(target, eyeBlock, false);

	}

	/**
	 * when we calculate whether target block can make two eyes, target side
	 * cannot pass unless it already have two eyes, because you have to take
	 * some action to build eyes. on the other hand, enemy block can pass.
	 * external breath is for enemy to close breath, target side cannot make use
	 * of external breath. of course, it is actually not easy to define external
	 * breath.
	 */
	public void test3A1() {

		String[] text = new String[6];
		text[0] = new String("[_, W, _, W, _, W]");
		text[1] = new String("[W, W, W, W, W, W]");
		text[2] = new String("[_, W, _, W, _, W]");
		text[3] = new String("[B, B, B, B, B, W]");
		text[4] = new String("[B, W, W, _, B, W]");
		text[5] = new String("[B, _, B, B, B, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 4, 1);
		Block target = ta.getBlock(point);
		Point point2 = Point.getPoint(6, 5, 4);
		BlankBlock eyeBlock = ta.getBlankBlock(point2);
		boolean live = ta.isBigEyeLive_dynamic(target, eyeBlock, false);
		if(log.isEnabledFor(Level.WARN)) log.warn(ta.getBoardColorState().getStateString());
		Assert.assertFalse(live);

	}

	// [INIT]W-->[5,4]B-->[6,2]W-->[5,3]B-->[5,2]W-->[3,5]B-->[PAS]W-->[3,1]B-->[PAS]

	public void testPrintState() {
		String[] text = new String[6];
		text[0] = new String("[_, W, _, W, _, W]");
		text[1] = new String("[W, W, W, W, W, W]");
		text[2] = new String("[_, W, _, W, _, W]");
		text[3] = new String("[B, B, B, B, B, W]");
		text[4] = new String("[B, W, W, _, B, W]");
		text[5] = new String("[B, _, B, B, B, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		String a = "W-->[5,4]B-->[6,2]W-->[5,3]B-->[5,2]W-->[3,5]B-->[PAS]W-->[3,1]B-->[PAS]";
		List<Step> steps = TestProblemFromZhangChu.toSteps(a, 6);

		for (Step step : steps) {
			if(log.isEnabledFor(Level.WARN)) log.warn(step);
			ta.oneStepForward(step);
		}
		if(log.isEnabledFor(Level.WARN)) log.warn(ta.getBoardColorState().getStateString());
	}

	public void test4() {

		String[] text = new String[6];
		text[0] = new String("[_, W, _, W, _, W]");
		text[1] = new String("[W, W, W, W, W, W]");
		text[2] = new String("[B, B, B, W, _, W]");
		text[3] = new String("[B, _, B, B, B, W]");
		text[4] = new String("[_, _, _, B, B, W]");
		text[5] = new String("[_, _, B, B, B, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 4, 1);
		Block target = ta.getBlock(point);
		Point point2 = Point.getPoint(6, 5, 1);
		BlankBlock eyeBlock = ta.getBlankBlock(point2);

		Set<Point> points = new HashSet<Point>();
		points.addAll(eyeBlock.getPoints());
		Result res = ta.isBigEyeLive_dynamic_internal(target, points, false);
		boolean live = res.isLive();
		Assert.assertFalse(live);

		res = ta.isBigEyeLive_dynamic_internal(target, points, true);
		live = res.isLive();
		Assert.assertTrue(live);

		points = ta.getEyePoints_singleTargetBlock(target, eyeBlock);
		if(log.isEnabledFor(Level.WARN)) log.warn(points);
		BreathPattern pattern = ta.getBreathPattern(points);
		if(log.isEnabledFor(Level.WARN)) log.warn(pattern.toString());

		RelativeSurviveResult rs = new RelativeSurviveResult();
		RelativeResult xianShou = new RelativeResult();
		xianShou.setSurvive(RelativeResult.ALREADY_LIVE);
		rs.setXianShou(xianShou);
		RelativeResult houShou = new RelativeResult();
		houShou.setSurvive(RelativeResult.ALREADY_DEAD);
		rs.setHouShou(houShou);

		//SmallEyeKnowledge.update(pattern, rs);

	}

}
