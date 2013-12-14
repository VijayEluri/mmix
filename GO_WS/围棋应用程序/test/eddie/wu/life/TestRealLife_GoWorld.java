package eddie.wu.life;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Block;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Group;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.EyeResult;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.domain.survive.RelativeResult;
import eddie.wu.domain.survive.Result;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.global.Candidate;

/**
 * 试验围棋天地上的面向业余棋手的题目。
 * 
 * @author Eddie
 * 
 */
public class TestRealLife_GoWorld extends TestCase {
	private static final Logger log = Logger
			.getLogger(TestRealLife_GoWorld.class);

	public void testRealLifeProblem_20131392_2() {
		String[] text = new String[6];
		text[0] = new String("[_, W, _, _, W, W]");
		text[1] = new String("[W, W, W, W, W, _]");
		text[2] = new String("[W, B, W, B, W, _]");
		text[3] = new String("[W, B, _, B, W, W]");
		text[4] = new String("[W, B, _, _, B, B]");
		text[5] = new String("[W, B, _, _, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 2, 1);
		boolean live = ta.isStaticLive(point);
		Assert.assertTrue(live);

		SurviveAnalysis survive = new SurviveAnalysis(state);
		Group group = new Group();
		group.addBlock(survive.getBlock(Point.getPoint(6, 3, 2)));
		group.addBlock(survive.getBlock(Point.getPoint(6, 3, 4)));
		group.addBlock(survive.getBlock(Point.getPoint(6, 5, 5)));

		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		Set<Point> eyeArea2 = survive.getEyeArea2(group);
		if (log.isEnabledFor(Level.WARN))
			log.warn(eyeArea2);

		point = Point.getPoint(6, 3, 2);
		// live = ta.isAlreadyLive_dynamic(point);
		boolean targetFirst = true;
		Block target = survive.getBlock(point);
		Result result = ta.isBigEyeLive_dynamic_internal(target, eyeArea2,
				targetFirst);
		live = result.getSurvive() == RelativeResult.ALREADY_LIVE;
		// live = ta.isBigEyeLive_dynamic(target, eyeBlock, targetFirst)
		Assert.assertTrue(live);

	}

	/**
	 * TODO: 929
	 */
	public void testRealLifeProblem_20131392_3() {
		String[] text = new String[6];
		text[0] = new String("[_, W, _, _, W, W]");
		text[1] = new String("[W, W, W, W, W, _]");
		text[2] = new String("[W, W, W, B, W, W]");
		text[3] = new String("[W, W, B, B, B, _]");
		text[4] = new String("[W, B, B, _, _, _]");
		text[5] = new String("[W, _, _, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 2, 1);
		boolean live = ta.isStaticLive(point);
		Assert.assertTrue(live);

		SurviveAnalysis survive = new SurviveAnalysis(state);
		Group group = new Group();
		group.addBlock(survive.getBlock(Point.getPoint(6, 5, 2)));

		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		Set<Point> eyeArea2 = survive.getEyeArea2(group);
		if (log.isEnabledFor(Level.WARN))
			log.warn(eyeArea2);

		point = Point.getPoint(6, 5, 2);
		// live = ta.isAlreadyLive_dynamic(point);
		boolean targetFirst = true;
		Block target = survive.getBlock(point);
		Result result = ta.isBigEyeLive_dynamic_internal(target, eyeArea2,
				targetFirst);
		live = result.getSurvive() == RelativeResult.ALREADY_LIVE;
		// live = ta.isBigEyeLive_dynamic(target, eyeBlock, targetFirst)
		Assert.assertTrue(live);

	}

	// 375
	public void testRealLifeProblem_20131392_4() {
		String[] text = new String[6];
		text[0] = new String("[_, W, _, _, W, W]");
		text[1] = new String("[W, W, W, W, W, _]");
		text[2] = new String("[W, B, B, B, B, W]");
		text[3] = new String("[W, B, W, W, B, W]");
		text[4] = new String("[W, B, _, W, B, W]");
		text[5] = new String("[W, B, _, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 2, 1);
		boolean live = ta.isStaticLive(point);
		Assert.assertTrue(live);

		SurviveAnalysis survive = new SurviveAnalysis(state);
		Group group = new Group();
		group.addBlock(survive.getBlock(Point.getPoint(6, 3, 2)));

		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		Set<Point> eyeArea2 = survive.getEyeArea2(group);
		if (log.isEnabledFor(Level.WARN))
			log.warn(eyeArea2);

		point = Point.getPoint(6, 3, 2);
		// live = ta.isAlreadyLive_dynamic(point);
		boolean targetFirst = true;
		Block target = survive.getBlock(point);
		Result result = ta.isBigEyeLive_dynamic_internal(target, eyeArea2,
				targetFirst);
		live = result.getSurvive() == RelativeResult.ALREADY_LIVE;
		// live = ta.isBigEyeLive_dynamic(target, eyeBlock, targetFirst)
		Assert.assertTrue(live);

	}

	// [INIT]B-->[6,5]W-->[6,2]B-->[6,3]W-->[4,6](FINAL -128)
	public void testRealLifeProblem_20131392_6D() {
		String[] text = new String[6];
		text[0] = new String("[_, W, _, _, W, _]");
		text[1] = new String("[W, W, W, W, _, W]");
		text[2] = new String("[W, B, _, W, B, W]");
		text[3] = new String("[W, W, W, B, W, W]");
		text[4] = new String("[W, B, B, B, _, B]");
		text[5] = new String("[_, W, B, _, B, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 5, 2);
		boolean live = ta.isAlreadyDead_dynamic(point);
		Assert.assertFalse(live);
		point = Point.getPoint(6, 5, 6);
		live = ta.isAlreadyDead_dynamic(point);
		Assert.assertFalse(live);
		point = Point.getPoint(6, 6, 5);
		live = ta.isAlreadyDead_dynamic(point);
		Assert.assertFalse(live);

	}

	public void testRealLifeProblem_20131392_6() {
		String[] text = new String[6];
		text[0] = new String("[_, W, _, _, W, _]");
		text[1] = new String("[W, W, W, W, _, W]");
		text[2] = new String("[W, B, _, W, B, W]");
		text[3] = new String("[W, W, W, B, W, _]");
		text[4] = new String("[W, B, B, B, _, B]");
		text[5] = new String("[_, _, _, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 2, 1);
		boolean live = ta.isStaticLive(point);
		Assert.assertTrue(live);

		SurviveAnalysis survive = new SurviveAnalysis(state);
		Group group = new Group();
		point = Point.getPoint(6, 5, 2);
		group.addBlock(survive.getBlock(point));
		point = Point.getPoint(6, 5, 6);
		group.addBlock(survive.getBlock(point));
		// point = Point.getPoint(6, 4, 5);
		// group.addBlock(survive.getBlock(point));

		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		Set<Point> eyeArea2 = survive.getEyeArea2(group);
		if (log.isEnabledFor(Level.WARN))
			log.warn(eyeArea2);

		// live = ta.isAlreadyLive_dynamic(point);
		boolean targetFirst = true;
		for (Block block : group.getBlocks()) {
			point = block.getBehalfPoint();

			Block target = survive.getBlock(point);
			Logger.getLogger(SurviveAnalysis.class).setLevel(Level.WARN);
			Result result = ta.isBigEyeLive_dynamic_internal(target, eyeArea2,
					targetFirst);
			live = result.getSurvive() == RelativeResult.ALREADY_LIVE;
			if (log.isEnabledFor(Level.WARN))
				log.warn("target=" + point + ", live=" + live);
			if (live)
				break;
		}
		Assert.assertTrue(live);

	}

	public void testRealLifeProblem_20131393_5_six() {
		String[] text = new String[7];
		text[0] = new String("[_, _, W, _, _, _, _]");
		text[1] = new String("[_, _, W, _, _, _, _]");
		text[2] = new String("[_, _, W, _, _, _, _]");
		text[3] = new String("[W, W, W, W, W, W, _]");
		text[4] = new String("[W, B, B, W, B, _, _]");
		text[5] = new String("[W, B, _, B, _, B, B]");
		text[6] = new String("[W, W, B, _, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(7, 4, 1);
		boolean live = false;
		live = ta.isAlreadyLive_dynamic(point);
		Assert.assertTrue(live);
	}

	public void testRealLifeProblem_20131393_5_six_2() {
		String[] text = new String[6];
		text[0] = new String("[_, _, W, B, B, _]");
		text[1] = new String("[_, _, W, _, B, _]");
		text[2] = new String("[_, _, W, _, B, _]");
		text[3] = new String("[W, W, W, B, B, _]");
		text[4] = new String("[B, B, B, B, B, _]");
		text[5] = new String("[B, _, B, _, B, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 4, 1);
		boolean live = false;
		live = ta.isAlreadyLive_dynamic(point);
		Assert.assertTrue(live);
	}

	public void testRealLifeProblem_20131393_7D() {
		String[] text = new String[7];
		text[0] = new String("[_, _, B, _, _, _, _]");
		text[1] = new String("[_, _, B, _, _, _, _]");
		text[2] = new String("[_, _, B, _, _, B, _]");
		text[3] = new String("[B, B, B, B, B, _, _]");
		text[4] = new String("[B, W, _, _, _, B, _]");
		text[5] = new String("[B, W, W, B, W, W, W]");
		text[6] = new String("[B, W, _, B, W, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(7, 4, 1);

		// boolean live = false;
		// live = ta.isAlreadyLive_dynamic(point);
		// Assert.assertTrue(live);

		// TODO: connectivity
		ta.getBlock(point).setLive(true);
		point = Point.getPoint(7, 3, 6);
		ta.getBlock(point).setLive(true);
		point = Point.getPoint(7, 5, 6);
		ta.getBlock(point).setLive(true);

		TerritoryAnalysis survive = ta;
		Group group = new Group();
		point = Point.getPoint(7, 5, 2);
		group.addBlock(survive.getBlock(point));
		point = Point.getPoint(7, 6, 5);
		group.addBlock(survive.getBlock(point));

		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		Set<Point> eyeArea2 = survive.getEyeArea2(group);
		if (log.isEnabledFor(Level.WARN))
			log.warn(eyeArea2);

		List<Candidate> candidate = ta.getCandidate(null, point, eyeArea2,
				Constant.WHITE, true, false);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		if (log.isEnabledFor(Level.WARN))
			log.warn(candidate);

		// candidate = ta.getCandidate(point, eyeArea2, Constant.BLACK, false,
		// false);
		// if(log.isEnabledFor(Level.WARN)) log.warn(candidate);
	}

	// [INIT]B-->[7,4]W-->[7,5]B-->[6,4]W-->[5,4]B-->[5,5]W-->[7,3]B-->[5,3]W-->[5,7]B-->[6,4]W
	public void testRealLifeProblem_20131393_7F() {
		String[] text = new String[7];
		text[0] = new String("[_, _, B, _, _, _, _]");
		text[1] = new String("[_, _, B, _, _, _, _]");
		text[2] = new String("[_, _, B, _, _, B, _]");
		text[3] = new String("[B, B, B, B, B, _, _]");
		text[4] = new String("[B, W, B, W, B, B, _]");
		text[5] = new String("[B, W, W, _, W, W, W]");
		text[6] = new String("[B, W, W, _, W, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(7, 4, 1);
		ta.getBlock(point).setLive(true);
		point = Point.getPoint(7, 3, 6);
		ta.getBlock(point).setLive(true);
		point = Point.getPoint(7, 5, 6);
		ta.getBlock(point).setLive(true);

		TerritoryAnalysis survive = ta;
		Group group = new Group();
		point = Point.getPoint(7, 5, 2);
		group.addBlock(survive.getBlock(point));
		point = Point.getPoint(7, 6, 5);
		group.addBlock(survive.getBlock(point));

		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		Set<Point> eyeArea2 = survive.getEyeArea2(group);
		if (log.isEnabledFor(Level.WARN))
			log.warn(eyeArea2);

		EyeResult result = ta.getRealEyes(group, false);
		;
		if (log.isEnabledFor(Level.WARN))
			log.warn(result);

		List<Candidate> candidate = ta.getCandidate(null, point, eyeArea2,
				Constant.WHITE, true, false);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		if (log.isEnabledFor(Level.WARN))
			log.warn(candidate);

	}

	public void testRealLifeProblem_20131393_7G() {
		String[] text = new String[7];
		text[0] = new String("[_, _, B, _, _, _, _]");
		text[1] = new String("[_, _, B, _, _, _, _]");
		text[2] = new String("[_, _, B, _, _, B, _]");
		text[3] = new String("[B, B, B, B, B, _, _]");
		text[4] = new String("[B, W, _, _, _, B, _]");
		text[5] = new String("[B, W, W, _, W, W, W]");
		text[6] = new String("[B, W, _, B, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(7, 4, 1);
		ta.getBlock(point).setLive(true);
		point = Point.getPoint(7, 3, 6);
		ta.getBlock(point).setLive(true);
		point = Point.getPoint(7, 5, 6);
		ta.getBlock(point).setLive(true);

		TerritoryAnalysis survive = ta;
		Group group = new Group();
		point = Point.getPoint(7, 5, 2);
		group.addBlock(survive.getBlock(point));
		point = Point.getPoint(7, 6, 5);
		group.addBlock(survive.getBlock(point));

		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		Set<Point> eyeArea2 = survive.getEyeArea2(group);
		if (log.isEnabledFor(Level.WARN))
			log.warn(eyeArea2);

		point = Point.getPoint(7, 5, 2);
		List<Candidate> candidate = ta.getCandidate(null, point, eyeArea2,
				Constant.WHITE, true, false);
		//if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
//		if (log.isEnabledFor(Level.WARN))
			log.warn(candidate);
			if(log.isEnabledFor(Level.WARN)) log.warn(candidate);

	}

	public void testRealLifeProblem_20131393_7E() {
		String[] text = new String[7];
		text[0] = new String("[_, _, B, _, _, _, _]");
		text[1] = new String("[_, _, B, _, _, _, _]");
		text[2] = new String("[_, _, B, _, _, B, _]");
		text[3] = new String("[B, B, B, B, B, _, _]");
		text[4] = new String("[B, W, _, W, _, B, _]");
		text[5] = new String("[B, W, W, B, W, W, W]");
		text[6] = new String("[B, W, _, B, W, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(7, 4, 1);
		ta.getBlock(point).setLive(true);
		point = Point.getPoint(7, 3, 6);
		ta.getBlock(point).setLive(true);
		point = Point.getPoint(7, 5, 6);
		ta.getBlock(point).setLive(true);

		TerritoryAnalysis survive = ta;
		Group group = new Group();
		point = Point.getPoint(7, 5, 2);
		group.addBlock(survive.getBlock(point));
		point = Point.getPoint(7, 6, 5);
		group.addBlock(survive.getBlock(point));

		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		Set<Point> eyeArea2 = survive.getEyeArea2(group);
		if (log.isEnabledFor(Level.WARN))
			log.warn(eyeArea2);

		List<Candidate> candidate = ta.getCandidate(null, point, eyeArea2,
				Constant.BLACK, false, false);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		if (log.isEnabledFor(Level.WARN))
			log.warn(candidate);

	}

	// [INIT]B-->[7,4]W-->[7,5]B-->[6,4]W-->[5,4]B-->[5,5]W-->[7,3]B-->[6,4]W-->[7,4]B-->[5,3]
	// [INIT]B-->[7,4]W-->[7,5]B-->[6,4]W-->[5,4]B-->[5,5]W-->[7,3]B-->[6,4]W-->[7,4]B-->[5,3]W
	public void testRealLifeProblem_20131393_7I() {
		String[] text = new String[7];
		text[0] = new String("[_, _, B, _, _, _, _]");
		text[1] = new String("[_, _, B, _, _, _, _]");
		text[2] = new String("[_, _, B, _, _, B, _]");
		text[3] = new String("[B, B, B, B, B, _, _]");
		text[4] = new String("[B, W, B, W, B, B, _]");
		text[5] = new String("[B, W, W, _, W, W, W]");
		text[6] = new String("[B, W, W, W, W, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point target = Point.getPoint(7, 7, 2);
		boolean dead = ta.isAlreadyDead_dynamic(target);
		Assert.assertTrue(dead);

		// boolean fina = ta.isFinalState_deadExist();
		// Assert.assertTrue(dead);

	}

	public void testRealLifeProblem_20131393_7L() {
		String[] text = new String[7];
		text[0] = new String("[_, _, B, _, _, _, _]");
		text[1] = new String("[_, _, B, _, _, _, _]");
		text[2] = new String("[_, _, B, _, _, B, B]");
		text[3] = new String("[B, B, B, B, B, _, W]");
		text[4] = new String("[B, W, B, B, B, B, W]");
		text[5] = new String("[B, W, W, B, W, W, W]");
		text[6] = new String("[B, W, _, B, W, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point target = Point.getPoint(7, 7, 5);
		Set<Point> targets = new HashSet<Point>();
		targets.add(target);
		target = Point.getPoint(7, 7, 2);
		targets.add(target);
		boolean live = ta.potentialEyeLive(targets);
		Assert.assertFalse(live);

	}

	public void testRealLifeProblem_20131393_7K() {
		String[] text = new String[7];
		text[0] = new String("[_, _, B, _, _, _, _]");
		text[1] = new String("[_, _, B, _, _, _, _]");
		text[2] = new String("[_, _, B, _, _, B, W]");
		text[3] = new String("[B, B, B, B, B, _, W]");
		text[4] = new String("[B, _, B, B, B, B, W]");
		text[5] = new String("[B, W, B, B, W, W, W]");
		text[6] = new String("[B, B, B, B, W, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point target = Point.getPoint(7, 7, 5);
		Set<Point> targets = new HashSet<Point>();
		targets.add(target);
		boolean live = ta.potentialEyeLive(targets);
		Assert.assertFalse(live);

		// boolean fina = ta.isFinalState_deadExist();
		// Assert.assertTrue(dead);

	}

	public void testRealLifeProblem_20131393_7J() {
		// Logger.getLogger(GoBoardSearch.class).setLevel(Level.WARN);
		String[] text = new String[7];
		text[0] = new String("[_, _, B, _, _, _, _]");
		text[1] = new String("[_, _, B, _, _, _, _]");
		text[2] = new String("[_, _, B, _, _, B, _]");
		text[3] = new String("[B, B, B, B, B, _, _]");
		text[4] = new String("[B, W, _, _, _, B, _]");
		text[5] = new String("[B, W, W, W, W, W, W]");
		text[6] = new String("[B, W, _, B, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(7, 6, 2);
		Set<Point> targets = new HashSet<Point>();
		targets.add(point);
		boolean condition = ta.isAlreadyDead_dynamic(targets, point);
		Assert.assertFalse(condition);
	}

	public void testRealLifeProblem_20131393_7() {
		// Logger.getLogger(GoBoardSearch.class).setLevel(Level.WARN);
		String[] text = new String[7];
		text[0] = new String("[_, _, B, _, _, _, _]");
		text[1] = new String("[_, _, B, _, _, _, _]");
		text[2] = new String("[_, _, B, _, _, B, _]");
		text[3] = new String("[B, B, B, B, B, _, _]");
		text[4] = new String("[B, W, _, _, _, B, _]");
		text[5] = new String("[B, W, W, _, W, W, W]");
		text[6] = new String("[B, W, _, _, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(7, 4, 1);
		ta.getBlock(point).setLive(true);
		point = Point.getPoint(7, 3, 6);
		ta.getBlock(point).setLive(true);
		point = Point.getPoint(7, 5, 6);
		ta.getBlock(point).setLive(true);

		TerritoryAnalysis survive = ta;
		Set<Point> targets = new HashSet<Point>();
		Group group = new Group();
		point = Point.getPoint(7, 5, 2);
		targets.add(point);
		group.addBlock(survive.getBlock(point));
		point = Point.getPoint(7, 6, 5);
		targets.add(point);
		group.addBlock(survive.getBlock(point));

		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		Set<Point> eyeArea2 = survive.getEyeArea2(group);
		if (log.isEnabledFor(Level.WARN))
			log.warn(eyeArea2);

		List<Candidate> candidate = ta.getCandidate(null, point, eyeArea2,
				Constant.WHITE, true, false);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		if (log.isEnabledFor(Level.WARN))
			log.warn(candidate);

		candidate = ta.getCandidate(null, point, eyeArea2, Constant.BLACK,
				false, false);
		if (log.isEnabledFor(Level.WARN))
			log.warn(candidate);

		boolean live = false;// live = ta.isAlreadyLive_dynamic(point);
		boolean targetFirst = false;
		for (Block block : group.getBlocks()) {
			point = block.getBehalfPoint();
			point = Point.getPoint(7, 5, 2);
			int count = 0;
			Block target = survive.getBlock(point);
			Logger.getLogger(SurviveAnalysis.class).setLevel(Level.WARN);
			Result result = ta.isBigEyeLive_dynamic_internal(target, targets,
					eyeArea2, targetFirst);
			live = result.getSurvive() == RelativeResult.ALREADY_LIVE;
			if (log.isEnabledFor(Level.WARN))
				log.warn("target=" + point + ", live=" + live);
			if (live)
				break;
			count++;
			if (count == 1)
				break;
		}
		Assert.assertFalse(live);

	}

	public void testRealLifeProblem_20131393_5() {
		String[] text = new String[7];
		text[0] = new String("[_, _, W, _, _, _, _]");
		text[1] = new String("[_, _, W, _, _, _, _]");
		text[2] = new String("[_, _, W, _, _, _, _]");
		text[3] = new String("[W, W, W, W, W, W, _]");
		text[4] = new String("[W, B, B, W, B, _, _]");
		text[5] = new String("[W, B, _, B, _, B, B]");
		text[6] = new String("[W, W, B, _, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(7, 4, 1);
		boolean live = false;
		// live = ta.isAlreadyLive_dynamic(point);
		// Assert.assertTrue(live);
		ta.getBlock(point).setLive(true);

		SurviveAnalysis survive = new SurviveAnalysis(state);
		Group group = new Group();
		point = Point.getPoint(7, 5, 2);
		group.addBlock(survive.getBlock(point));
		point = Point.getPoint(7, 5, 5);
		group.addBlock(survive.getBlock(point));
		point = Point.getPoint(7, 6, 4);
		group.addBlock(survive.getBlock(point));
		point = Point.getPoint(7, 7, 3);
		group.addBlock(survive.getBlock(point));
		point = Point.getPoint(7, 6, 6);
		group.addBlock(survive.getBlock(point));
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		Set<Point> eyeArea2 = survive.getEyeArea2(group);
		if (log.isEnabledFor(Level.WARN))
			log.warn(eyeArea2);

		// live = ta.isAlreadyLive_dynamic(point);
		// boolean targetFirst = true;
		// for (Block block : group.getBlocks()) {
		// point = block.getBehalfPoint();
		// point = Point.getPoint(7, 5, 2);
		// int count = 0;
		// Block target = survive.getBlock(point);
		// Logger.getLogger(SurviveAnalysis.class).setLevel(Level.WARN);
		// Result result = ta.isBigEyeLive_dynamic_internal(target, eyeArea2,
		// targetFirst);
		// live = result.getSurvive() == RelativeResult.ALREADY_LIVE;
		// if(log.isEnabledFor(Level.WARN)) log.warn("target=" + point +
		// ", live=" + live);
		// if (live)
		// break;
		// count++;
		// if (count == 1)
		// break;
		// }
		// Assert.assertTrue(live);

	}

	public void testRealLifeProblem_20131393_1() {
		String[] text = new String[6];
		text[0] = new String("[_, _, _, _, W, _]");
		text[1] = new String("[W, W, W, W, W, W]");
		text[2] = new String("[_, _, _, W, B, B]");
		text[3] = new String("[W, W, B, B, _, _]");
		text[4] = new String("[W, B, _, _, _, _]");
		text[5] = new String("[_, _, _, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 2, 1);
		boolean live = ta.isStaticLive(point);
		Assert.assertTrue(live);

		SurviveAnalysis survive = new SurviveAnalysis(state);
		Group group = new Group();
		point = Point.getPoint(6, 5, 2);
		group.addBlock(survive.getBlock(point));
		point = Point.getPoint(6, 3, 5);
		group.addBlock(survive.getBlock(point));
		point = Point.getPoint(6, 4, 3);
		group.addBlock(survive.getBlock(point));

		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		Set<Point> eyeArea2 = survive.getEyeArea2(group);
		if (log.isEnabledFor(Level.WARN))
			log.warn(eyeArea2);

		// live = ta.isAlreadyLive_dynamic(point);
		boolean targetFirst = true;
		for (Block block : group.getBlocks()) {
			point = block.getBehalfPoint();

			Block target = survive.getBlock(point);
			Logger.getLogger(SurviveAnalysis.class).setLevel(Level.WARN);
			Result result = ta.isBigEyeLive_dynamic_internal(target, eyeArea2,
					targetFirst);
			live = result.getSurvive() == RelativeResult.ALREADY_LIVE;
			if (log.isEnabledFor(Level.WARN))
				log.warn("target=" + point + ", live=" + live);
			if (live)
				break;
		}
		Assert.assertTrue(live);

	}

	public void testRealLifeProblem_20131392_8() {
		String[] text = new String[6];
		text[0] = new String("[_, W, _, _, W, _]");
		text[1] = new String("[W, W, W, W, W, W]");
		text[2] = new String("[W, _, W, B, B, B]");
		text[3] = new String("[W, W, B, _, _, _]");
		text[4] = new String("[W, B, B, _, B, W]");
		text[5] = new String("[W, W, W, _, B, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 2, 1);
		boolean live = ta.isStaticLive(point);
		Assert.assertTrue(live);

		SurviveAnalysis survive = new SurviveAnalysis(state);
		Group group = new Group();
		point = Point.getPoint(6, 5, 2);
		group.addBlock(survive.getBlock(point));
		point = Point.getPoint(6, 3, 4);
		group.addBlock(survive.getBlock(point));
		point = Point.getPoint(6, 5, 5);
		group.addBlock(survive.getBlock(point));

		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		Set<Point> eyeArea2 = survive.getEyeArea2(group);
		if (log.isEnabledFor(Level.WARN))
			log.warn(eyeArea2);

		// live = ta.isAlreadyLive_dynamic(point);
		boolean targetFirst = true;
		for (Block block : group.getBlocks()) {
			point = block.getBehalfPoint();

			Block target = survive.getBlock(point);
			Logger.getLogger(SurviveAnalysis.class).setLevel(Level.WARN);
			Result result = ta.isBigEyeLive_dynamic_internal(target, eyeArea2,
					targetFirst);
			live = result.getSurvive() == RelativeResult.ALREADY_LIVE;
			if (log.isEnabledFor(Level.WARN))
				log.warn("target=" + point + ", live=" + live);
			if (live)
				break;
		}
		Assert.assertTrue(live);

	}

	public void testRealLifeProblem_20131392_7() {
		String[] text = new String[6];
		text[0] = new String("[_, W, _, _, W, _]");
		text[1] = new String("[W, W, W, W, _, W]");
		text[2] = new String("[W, B, _, W, W, W]");
		text[3] = new String("[W, W, W, _, B, _]");
		text[4] = new String("[B, B, B, B, W, B]");
		text[5] = new String("[B, _, _, _, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 2, 1);
		boolean live = ta.isStaticLive(point);
		Assert.assertTrue(live);

		SurviveAnalysis survive = new SurviveAnalysis(state);
		Group group = new Group();
		point = Point.getPoint(6, 5, 2);
		group.addBlock(survive.getBlock(point));
		point = Point.getPoint(6, 5, 6);
		group.addBlock(survive.getBlock(point));
		point = Point.getPoint(6, 4, 5);
		group.addBlock(survive.getBlock(point));

		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		Set<Point> eyeArea2 = survive.getEyeArea2(group);
		if (log.isEnabledFor(Level.WARN))
			log.warn(eyeArea2);

		// live = ta.isAlreadyLive_dynamic(point);
		boolean targetFirst = true;
		for (Block block : group.getBlocks()) {
			point = block.getBehalfPoint();

			Block target = survive.getBlock(point);
			Logger.getLogger(SurviveAnalysis.class).setLevel(Level.WARN);
			Result result = ta.isBigEyeLive_dynamic_internal(target, eyeArea2,
					targetFirst);
			live = result.getSurvive() == RelativeResult.ALREADY_LIVE;
			if (log.isEnabledFor(Level.WARN))
				log.warn("target=" + point + ", live=" + live);
			if (live)
				break;
		}
		Assert.assertTrue(live);

	}

	public void testRealLifeProblem_20131392_5() {
		String[] text = new String[6];
		text[0] = new String("[_, W, _, _, W, _]");
		text[1] = new String("[W, W, W, W, W, _]");
		text[2] = new String("[W, B, _, W, B, _]");
		text[3] = new String("[W, W, W, W, B, _]");
		text[4] = new String("[W, B, B, B, _, B]");
		text[5] = new String("[W, W, W, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 2, 1);
		boolean live = ta.isStaticLive(point);
		Assert.assertTrue(live);

		SurviveAnalysis survive = new SurviveAnalysis(state);
		Group group = new Group();
		point = Point.getPoint(6, 5, 2);
		group.addBlock(survive.getBlock(point));
		point = Point.getPoint(6, 5, 6);
		group.addBlock(survive.getBlock(point));
		point = Point.getPoint(6, 4, 5);
		group.addBlock(survive.getBlock(point));

		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		Set<Point> eyeArea2 = survive.getEyeArea2(group);
		if (log.isEnabledFor(Level.WARN))
			log.warn(eyeArea2);

		// live = ta.isAlreadyLive_dynamic(point);
		boolean targetFirst = true;
		for (Block block : group.getBlocks()) {
			point = block.getBehalfPoint();

			Block target = survive.getBlock(point);
			Logger.getLogger(SurviveAnalysis.class).setLevel(Level.WARN);
			Result result = ta.isBigEyeLive_dynamic_internal(target, eyeArea2,
					targetFirst);
			live = result.getSurvive() == RelativeResult.ALREADY_LIVE;
			if (log.isEnabledFor(Level.WARN))
				log.warn("target=" + point + ", live=" + live);
			if (live)
				break;
		}
		Assert.assertTrue(live);

	}

	public void testRealLifeProblem() {
		String[] text = new String[6];
		text[0] = new String("[W, W, W, W, W, B]");
		text[1] = new String("[_, W, _, _, W, _]");
		text[2] = new String("[W, W, W, W, W, W]");
		text[3] = new String("[W, W, B, B, B, _]");
		text[4] = new String("[W, B, _, B, W, _]");
		text[5] = new String("[W, B, _, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 4, 1);
		boolean live = ta.isStaticLive(point);
		Assert.assertTrue(live);
		SurviveAnalysis survive = new SurviveAnalysis(state);
		Group group = new Group();
		group.addBlock(survive.getBlock(Point.getPoint(6, 5, 2)));
		group.addBlock(survive.getBlock(Point.getPoint(6, 4, 3)));

		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		Set<Point> eyeArea2 = survive.getEyeArea2(group);
		if (log.isEnabledFor(Level.WARN))
			log.warn(eyeArea2);
		// assertEquals(result, live);

		point = Point.getPoint(6, 4, 4);
		// live = ta.isAlreadyLive_dynamic(point);
		boolean targetFirst = true;
		Block target = survive.getBlock(point);
		Result result = ta.isBigEyeLive_dynamic_internal(target, eyeArea2,
				targetFirst);
		live = result.getSurvive() == RelativeResult.ALREADY_LIVE;
		// live = ta.isBigEyeLive_dynamic(target, eyeBlock, targetFirst)
		Assert.assertTrue(live);

	}

	public void testRealLifeProblem2() {
		String[] text = new String[6];
		text[0] = new String("[W, W, W, W, W, B]");
		text[1] = new String("[_, W, _, _, W, _]");
		text[2] = new String("[W, W, W, W, W, W]");
		text[3] = new String("[W, W, B, B, B, B]");
		text[4] = new String("[W, B, _, B, W, _]");
		text[5] = new String("[W, B, _, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 4, 4);
		boolean dead = ta.isAlreadyDead_dynamic(point);
		Assert.assertFalse(dead);
	}

	public void testRealLifeProblem3() {
		String[] text = new String[6];
		text[0] = new String("[W, W, W, W, W, B]");
		text[1] = new String("[_, W, _, _, W, _]");
		text[2] = new String("[W, W, W, W, W, W]");
		text[3] = new String("[W, W, B, B, B, B]");
		text[4] = new String("[W, B, _, B, W, _]");
		text[5] = new String("[W, B, _, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Point point = Point.getPoint(6, 4, 1);
		boolean live = ta.isStaticLive(point);
		Assert.assertTrue(live);
		SurviveAnalysis survive = new SurviveAnalysis(state);
		Group group = new Group();
		group.addBlock(survive.getBlock(Point.getPoint(6, 5, 2)));
		group.addBlock(survive.getBlock(Point.getPoint(6, 4, 3)));

		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.DEBUG);
		Set<Point> eyeArea2 = survive.getEyeArea2(group);
		if (log.isEnabledFor(Level.WARN))
			log.warn(eyeArea2);
		// assertEquals(result, live);live = ta.isStaticLive(point);
		Assert.assertTrue(live);

		point = Point.getPoint(6, 4, 4);
		live = ta.isAlreadyLive_dynamic(point);
		Assert.assertFalse(live);

	}

}
