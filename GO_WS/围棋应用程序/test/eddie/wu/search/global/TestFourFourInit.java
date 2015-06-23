package eddie.wu.search.global;

import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Block;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.domain.analy.SmallGoBoard;
import eddie.wu.manual.StateLoader;
import eddie.wu.manual.TreeGoManual;
import eddie.wu.search.small.ThreeThreeBoardSearch;

public class TestFourFourInit extends TestCase {
	private static Logger log = Logger.getLogger(Block.class);
	static {
		// Logger.getLogger(GoBoardSearch.class).setLevel(Level.WARN);
	}

	public void testProblem_internal(byte[][] state, int blackExp) {
		testProblem_internal(state, 50000, blackExp);
	}

	public int testProblem_internal(byte[][] state, int variant, int blackExp) {

		ThreeThreeBoardSearch goS = new ThreeThreeBoardSearch(state,
				Constant.BLACK, blackExp, blackExp - 1);
		goS.setVariant(variant);
		int score = goS.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn(goS.getGoBoard().getInitColorState().getStateString());

		int count = 0;

		int size = goS.getSearchProcess().size();
		if (log.isEnabledFor(Level.WARN))
			log.warn(size);
		if (size > 5000) {
			for (String list : goS.getSearchProcess()) {
				count++;
				if (count % 100 == 0) {
					if (log.isEnabledFor(Level.WARN))
						log.warn("count=" + count);
					if (log.isEnabledFor(Level.WARN))
						log.warn(list);
				}
			}
		} else if (size > 500) {

			for (String list : goS.getSearchProcess()) {
				count++;
				// if (count % 100 == 0) {
				// if (list.endsWith(" EXHAUST)")) {
				if (log.isEnabledFor(Level.WARN))
					log.warn("count=" + count);
				if (log.isEnabledFor(Level.WARN))
					log.warn(list);
				// }
				// }
			}
		} else {

			for (String list : goS.getSearchProcess()) {
				count++;
				if (log.isEnabledFor(Level.WARN))
					log.warn("count=" + count);
				if (log.isEnabledFor(Level.WARN))
					log.warn(list);

			}
		}

		TreeGoManual goM = goS.getGoBoard().getTreeGoManual();
		if (goM.isEmpty() == false) {

			if (log.isEnabledFor(Level.WARN)) {
				log.warn(goM.getSGFBodyString());
				int countVar = goM.getVariant();
				log.warn("variant=" + countVar);
			}

		}

		if (log.isEnabledFor(Level.WARN))
			log.warn(goS.getSearchProcess().size());
		if (log.isEnabledFor(Level.WARN))
			log.warn("Score=" + score);
		// assertTrue(score >= 1);
		if (goS.getBestResult() != null) {
			for (Step step : goS.getBestResult()) {
				if (log.isEnabledFor(Level.WARN))
					log.warn(step + "<br/>");
			}
		}
		goS.printKnownResult();
		return score;
	}

	/**
	 * 69333: 427s (average 120 variant/second)<br/>
	 * Score=1<br/>
	 * Step [point=[2,1], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,4], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,1], color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=[4,3], color=White, index=4, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,4], color=Black, index=5, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,2], color=White, index=6, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,3], color=Black, index=7, loopSuperior= false, name=null]<br/>
	 * Step [point=[PAS], color=White, index=8, loopSuperior= false, name=null]<br/>
	 * Step [point=[PAS], color=Black, index=9, loopSuperior= false, name=null]<br/>
	 * 
	 * Score=1<br/>
	 * Step [point=[2,1], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,4], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,1], color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=[4,3], color=White, index=4, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,4], color=Black, index=5, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,2], color=White, index=6, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,3], color=Black, index=7, loopSuperior= false, name=null]<br/>
	 * Step [point=[PAS], color=White, index=8, loopSuperior= false, name=null]<br/>
	 * Step [point=[PAS], color=Black, index=9, loopSuperior= false, name=null]<br/>
	 * [INIT]B-->[2,1]W-->[3,4]B-->[1,4]2200
	 * [INIT]B-->[2,1]W-->[3,4]B-->[3,1]3000<br/>
	 * [INIT]B-->[2,1]W-->[4,3]B-->[3,4]10000<br/>
	 * [INIT]B-->[2,1]W-->[4,4]B-->[2,4]<br/>
	 * [INIT]B-->[2,1]W-->[1,4]B-->[1,3]<br/>
	 * [INIT]B-->[2,1]W-->[4,1]B-->[3,1]<br/>
	 * [INIT]B-->[2,1]W-->[1,1]B-->[1,2]<br/>
	 * why not realize the key point of [2,4] after [2,1]
	 */
	public void testProblem1() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[_, B, B, _]");
		text[2] = new String("[_, W, W, _]");
		text[3] = new String("[_, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		int score = testProblem_internal(state, 100000, 1);
		assertEquals(1, score);
		// testProblem_internal(state, 16);
	}

	public void testProblem122() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[B, B, B, W]");
		text[2] = new String("[B, W, W, _]");
		text[3] = new String("[_, W, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		int score = testProblem_internal(state, 5000, 1);
		assertEquals(1, score);
		// testProblem_internal(state, 16);
	}

	public void testProblem11() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[_, W, W, _]");
		text[3] = new String("[_, _, _, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		int score = testProblem_internal(state, 1500, 1);
		assertTrue(score > 1);
		// testProblem_internal(state, 16);
	}

	// [3,4]-->[3,1]-->[4,2]-->[4,3]-->[2,4]-->[1,2]-->[1,3]-->[4,4]-->[1,4]-->[4,1]-->[1,1]-->null-->

	public void testProblem12() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, W]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[_, W, W, _]");
		text[3] = new String("[_, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		int score = testProblem_internal(state, 1500, 1);
		assertTrue(score > 1);
		// testProblem_internal(state, 16);
	}

	public void testCandidate12B() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, W]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[_, W, W, _]");
		text[3] = new String("[_, _, _, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		Point point = Point.getPoint(4, 2, 1);
		boolean live = ta.isAlreadyLive_dynamic(point);
		assertTrue(live);
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);

		ta.getBlock(point).setLive(true);
		for (Candidate can : candidate) {
			System.out.print(can.getStep().getPoint() + "-->");
		}
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}

	}

	public void testCandidate12C() {
		String[] text = new String[4];
		text[0] = new String("[B, W, _, W]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[_, W, W, _]");
		text[3] = new String("[_, _, _, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		Point point = Point.getPoint(4, 2, 1);
		boolean live = ta.isAlreadyLive_dynamic(point);
		assertTrue(live);

		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);

		for (Candidate can : candidate) {
			System.out.print(can.getStep().getPoint() + "-->");
		}
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}

	}

	// [INIT]W-->[3,1]B-->[3,4]W-->[4,3]B-->[4,4]W-->[4,2]B-->[4,1]W-->[3,2]B-->[3,3]W-->[4,2]B-->[3,1]W-->[1,1]B-->[4,3]W-->[3,2]B-->[4,2]W-->[1,2](FINAL
	// 128)
	public void testCandidate12D() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, W]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[_, W, W, _]");
		text[3] = new String("[_, _, _, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);

		String a = "W-->[3,1]B-->[3,4]W-->[4,2]B-->[4,3]W-->[4,4]B-->[PAS]W-->[4,3]B-->[4,1]";

		for (Step step : TestProblemFromZhangChu.toSteps(a)) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(step);
			ta.oneStepForward(step);
		}
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		// Logger.getLogger(SmallGoBoard.class).setLevel(Level.WARN);
		// boolean finalState_deadExist = ta.isFinalState_deadExist();
		// assertTrue(finalState_deadExist);
		//
		// if(log.isEnabledFor(Level.WARN))
		// log.warn(ta.getBoardColorState().getStateString());
		// Point point = Point.getPoint(4, 2, 1);
		// boolean live = ta.isAlreadyLive_dynamic(point);
		// assertTrue(live);

		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);

		for (Candidate can : candidate) {
			System.out.print(can.getStep().getPoint() + "-->");
		}
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}

	}

	// [INIT]W-->[3,1]B-->[3,4]W-->[4,2]B-->[4,3]W-->[4,4]B-->[PAS]W-->[4,1]B-->[4,3](FINAL
	// 128)
	public void testCandidate12E() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, W]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[_, W, W, _]");
		text[3] = new String("[_, _, _, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);

		String a = "W-->[3,1]B-->[3,4]W-->[4,2]B-->[4,3]W-->[4,4]B-->[PAS]";

		for (Step step : TestProblemFromZhangChu.toSteps(a)) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(step);
			ta.oneStepForward(step);
		}
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		// Logger.getLogger(SmallGoBoard.class).setLevel(Level.WARN);
		// boolean finalState_deadExist = ta.isFinalState_deadExist();
		// assertTrue(finalState_deadExist);
		//
		// if(log.isEnabledFor(Level.WARN))
		// log.warn(ta.getBoardColorState().getStateString());
		// Point point = Point.getPoint(4, 2, 1);
		// boolean live = ta.isAlreadyLive_dynamic(point);
		// assertTrue(live);

		List<Candidate> candidate = ta.getCandidate(Constant.WHITE, false);

		for (Candidate can : candidate) {
			System.out.print(can.getStep().getPoint() + "-->");
		}
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}

	}

	public void testProblem13() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[_, W, W, _]");
		text[3] = new String("[W, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		int score = testProblem_internal(state, 1500, 1);
		assertTrue(score > 1);
		// testProblem_internal(state, 16);
	}

	public void testProblem14() {
		String[] text = new String[4];
		text[0] = new String("[W, _, _, _]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[_, W, W, _]");
		text[3] = new String("[_, _, _, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		int score = testProblem_internal(state, 5000, 1);
		assertTrue(score > 1);
		// testProblem_internal(state, 16);
	}

	public void testCandidate14B() {
		String[] text = new String[4];
		text[0] = new String("[W, _, _, _]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[_, W, W, _]");
		text[3] = new String("[_, _, _, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}
		for (Candidate can : candidate) {
			System.out.print(can.getStep().getPoint() + "-->");
		}

		Point point = Point.getPoint(4, 2, 4);
		ta.oneStepForward(point, Constant.BLACK);
		boolean condition = ta.isAlreadyLive_dynamic(point);
		assertTrue(condition);
	}

	public void testProblem141() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[_, W, W, _]");
		text[3] = new String("[_, _, _, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		int score = testProblem_internal(state, 1500, 1);
		assertTrue(score > 1);
		// testProblem_internal(state, 16);
	}

	public void testCandidate11AB() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[_, W, W, _]");
		text[3] = new String("[_, _, _, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		List<Candidate> candidate = ta.getCandidate(Constant.WHITE, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}
		for (Candidate can : candidate) {
			System.out.print(can.getStep().getPoint() + "-->");
		}
	}

	public void testCandidate11A() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[_, W, W, _]");
		text[3] = new String("[_, _, _, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		List<Candidate> candidate = ta.getCandidate(Constant.WHITE, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}
	}

	public void testCandidate11B() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[W, W, W, _]");
		text[3] = new String("[_, _, _, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		Point point = Point.getPoint(4, 2, 1);
		boolean condition = ta.isAlreadyLive_dynamic(point);
		assertTrue(condition);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}
	}

	public void testCandidate1() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[_, B, B, _]");
		text[2] = new String("[_, W, W, _]");
		text[3] = new String("[_, _, _, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}
	}

	public void testCandidate13() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[_, W, W, W]");
		text[3] = new String("[_, _, _, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}
	}

	// [INIT]B-->[3,4]W-->[4,3]B-->[1,4]W-->[3,1]
	// B-->[PAS]W-->[1,2]B-->[4,4]W-->[2,4]B-->[4,4]W-->[3,4](-16 EXHAUST)
	public void testCandidate14BC() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, B]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[W, W, W, B]");
		text[3] = new String("[W, _, W, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}
	}

	// [INIT]B-->[3,4]W-->[4,3]B-->[1,4]W-->[3,1]B-->[1,2]W-->[PAS]B-->[4,4]W
	public void testCandidate14BCD() {
		String[] text = new String[4];
		text[0] = new String("[_, B, _, B]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[W, W, W, B]");
		text[3] = new String("[W, _, W, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}
	}

	public void testCandidate14() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[_, W, W, _]");
		text[3] = new String("[_, _, W, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}
	}

	public void testCandidate15() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[_, W, W, _]");
		text[3] = new String("[_, _, _, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}
	}

	public void testCandidate16() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[_, W, W, _]");
		text[3] = new String("[W, _, _, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}
	}

	public void testCandidate17() {
		String[] text = new String[4];
		text[0] = new String("[W, _, _, _]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[_, W, W, _]");
		text[3] = new String("[_, _, _, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}
	}

	// [INIT]B-->[2,1]W-->[3,4]B-->[3,1]W-->[1,1]B-->[1,4]
	public void testCandidate18() {
		String[] text = new String[4];
		text[0] = new String("[W, _, _, _]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[B, W, W, W]");
		text[3] = new String("[_, _, _, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}
	}

	// B-->[2,1]W-->[1,2]B-->[2,4]W-->[1,3]B
	/**
	 * close ememy's breath, but the enemy is the pointed eye
	 */
	public void testCandidate19() {
		String[] text = new String[4];
		text[0] = new String("[_, W, W, _]");
		text[1] = new String("[B, B, B, B]");
		text[2] = new String("[_, W, W, W]");
		text[3] = new String("[_, _, _, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}
	}

	// [INIT]B-->[2,1]W-->[2,4]B-->[1,3]W-->[3,1]B-->[4,3]W-->[3,4]B-->[4,2]
	// W-->[1,4]B-->[4,1]W-->[4,4]B-->[4,2]W-->[PAS]B-->[4,3]W-->[4,1]B-->[4,2]W-->[4,3]B-->[4,2]W-->[4,3]B-->[3,4]W-->[3,1]B-->[2,4]W-->[1,2]B-->[1,1]W-->[4,1]B-->[3,2](EXHAUST
	// 12)
	public void testCandidate20() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[B, B, B, W]");
		text[2] = new String("[_, W, W, _]");
		text[3] = new String("[_, _, _, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}
	}

	public void testCandidate21() {
		String[] text = new String[4];
		text[0] = new String("[_, _, B, W]");
		text[1] = new String("[B, B, B, W]");
		text[2] = new String("[W, W, W, W]");
		text[3] = new String("[_, B, _, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		List<Candidate> candidate = ta.getCandidate(Constant.WHITE, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}
	}

	// [INIT]B-->[2,1]W-->[1,4]B-->[1,2]W
	public void testCandidate22() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, W]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[_, W, W, _]");
		text[3] = new String("[_, _, _, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}
	}

	public void testCandidate11() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[_, B, _, _]");
		text[2] = new String("[_, _, _, _]");
		text[3] = new String("[_, _, _, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());

		List<Candidate> candidate = ta.getCandidate(Constant.WHITE, false);
		if (log.isEnabledFor(Level.WARN))
			log.warn("total candidate: " + candidate.size());
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}

		candidate = ta.getCandidate(Constant.WHITE, true);
		if (log.isEnabledFor(Level.WARN))
			log.warn("reduce candidate by symmetry:" + candidate.size());
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}
	}

	public void testCandidate12() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, _]");
		text[1] = new String("[_, B, B, _]");
		text[2] = new String("[_, _, W, _]");
		text[3] = new String("[_, _, _, _]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());

		List<Candidate> candidate = ta.getCandidate(Constant.WHITE, false);
		if (log.isEnabledFor(Level.WARN))
			log.warn("total candidate: " + candidate.size());
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}

		candidate = ta.getCandidate(Constant.WHITE, true);
		if (log.isEnabledFor(Level.WARN))
			log.warn("reduce candidate by symmetry:" + candidate.size());
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}
	}

	/**
	 * right solution: +16<br/>
	 * 4769<br/>
	 * Score=16<br/>
	 * Step [point=[4,2], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[4,3], color=White, index=14, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,2], color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=[4,1], color=White, index=4, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,1], color=Black, index=5, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,1], color=White, index=6, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,1], color=Black, index=7, loopSuperior= false, name=null]<br/>
	 * Step [point=[4,4], color=White, index=8, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,1], color=Black, index=9, loopSuperior= false, name=null]<br/>
	 * Step [point=[PAS], color=White, index=10, loopSuperior= false, name=null]<br/>
	 * Step [point=[4,2], color=Black, index=11, loopSuperior= false, name=null]<br/>
	 * Step [point=[PAS], color=White, index=12, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,4], color=Black, index=13, loopSuperior= false, name=null]<br/>
	 */
	public void testProblem2() {
		String[] text = new String[4];
		text[0] = new String("[W, B, _, B]");
		text[1] = new String("[W, _, _, B]");
		text[2] = new String("[B, W, B, W]");
		text[3] = new String("[B, _, W, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		testProblem_internal(state, 15000, 1);
		testProblem_internal(state, 15000, 16);
	}

	/**
	 * right solution: +16 from net work<br/>
	 * 18664<br/>
	 * Step [point=[2,2], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,3], color=White, index=14, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,3], color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,4], color=White, index=15, loopSuperior= false, name=null]<br/>
	 * Step [point=[4,3], color=Black, index=5, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,2], color=White, index=16, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,4], color=Black, index=7, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,4], color=White, index=17, loopSuperior= false, name=null]<br/>
	 * Step [point=[4,4], color=Black, index=9, loopSuperior= false, name=null]<br/>
	 * Step [point=[PAS], color=White, index=10, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,4], color=Black, index=19, loopSuperior= false, name=null]<br/>
	 * Step [point=[PAS], color=White, index=12, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,1], color=Black, index=13, loopSuperior= false, name=null]<br/>
	 */
	public void testProblem3() {
		String[] text = new String[4];
		text[0] = new String("[_, _, W, _]");
		text[1] = new String("[B, _, _, B]");
		text[2] = new String("[B, W, _, W]");
		text[3] = new String("[B, W, _, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		testProblem_internal(state, 1);
		testProblem_internal(state, 16);
	}

	public void testCandidate3() {
		String[] text = new String[4];
		text[0] = new String("[W, _, W, _]");
		text[1] = new String("[B, B, _, B]");
		text[2] = new String("[B, W, _, W]");
		text[3] = new String("[B, W, _, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}
	}

	/**
	 * score = 16 9811<br/>
	 * Step [point=[2,2], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,2], color=White, index=14, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,3], color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,4], color=White, index=4, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,4], color=Black, index=5, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,4], color=White, index=6, loopSuperior= false, name=null]<br/>
	 * Step [point=[4,4], color=Black, index=7, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,1], color=White, index=8, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,4], color=Black, index=9, loopSuperior= false, name=null]<br/>
	 * Step [point=[PAS], color=White, index=10, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,3], color=Black, index=11, loopSuperior= false, name=null]<br/>
	 * Step [point=[PAS], color=White, index=12, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,1], color=Black, index=13, loopSuperior= false, name=null]<br/>
	 */
	public void testProblem4() {
		String[] text = new String[4];
		text[0] = new String("[_, _, B, B]");
		text[1] = new String("[_, _, W, B]");
		text[2] = new String("[B, W, _, W]");
		text[3] = new String("[B, W, B, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		testProblem_internal(state, 1);
		testProblem_internal(state, 16);
	}

	/**
	 * easy!<br/>
	 * 1439<br/>
	 * Score=16<br/>
	 * Step [point=[3,3], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,4], color=White, index=6, loopSuperior= false, name=null]<br/>
	 * Step [point=[4,2], color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,3], color=White, index=7, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,3], color=Black, index=5, loopSuperior= false, name=null]<br/>
	 */
	public void testProblem5() {
		String[] text = new String[4];
		text[0] = new String("[_, B, B, W]");
		text[1] = new String("[B, B, _, W]");
		text[2] = new String("[W, W, _, _]");
		text[3] = new String("[_, _, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		testProblem_internal(state, 1);
		testProblem_internal(state, 16);
	}

	/**
	 * score = 1<br/>
	 * 107084<br/>
	 * Step [point=[3,3], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,2], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,2], color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=[4,2], color=White, index=4, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,4], color=Black, index=5, loopSuperior= false, name=null]<br/>
	 * Step [point=[PAS], color=White, index=6, loopSuperior= false, name=null]<br/>
	 * Step [point=[PAS], color=Black, index=7, loopSuperior= false, name=null]<br/>
	 * [INIT]B-->[3,3]W-->[3,2]B-->[2,2]W-->[3,4]B-->[1,3]W-->[1,4]B-->[4,2]W-->
	 * [4,1]B-->[2,4]W-->[2,1]B-->[3,3]W-->[1,2]B-->[4,2]W-->[4,3]B-->[1,1]W-->[
	 * 3,4]B-->[3,1]W-->[2,3]B-->[4,2]W-->[1,4]B-->[3,3]W-->[1,2]B-->[4,4]W-->[2
	 * ,1]B-->[2,4](EXHAUST 4) score=16<br/>
	 * 128482<br/>
	 * Step [point=[3,3], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,2], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,2], color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,4], color=White, index=4, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,3], color=Black, index=5, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,4], color=White, index=6, loopSuperior= false, name=null]<br/>
	 * Step [point=[4,2], color=Black, index=7, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,2], color=White, index=8, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,3], color=Black, index=9, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,1], color=White, index=10, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,4], color=Black, index=11, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,2], color=White, index=12, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,3], color=Black, index=13, loopSuperior= false, name=null]<br/>
	 * Step [point=[4,1], color=White, index=14, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,2], color=Black, index=15, loopSuperior= false, name=null]<br/>
	 * Step [point=[4,3], color=White, index=16, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,1], color=Black, index=17, loopSuperior= false, name=null]<br/>
	 * Step [point=[4,4], color=White, index=18, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,1], color=Black, index=19, loopSuperior= false, name=null]<br/>
	 * Step [point=[PAS], color=White, index=20, loopSuperior= false, name=null]<br/>
	 * Step [point=[4,2], color=Black, index=21, loopSuperior= false, name=null]<br/>
	 */
	public void testProblem6() {
		String[] text = new String[4];
		text[0] = new String("[B, _, _, _]");
		text[1] = new String("[_, _, W, B]");
		text[2] = new String("[B, _, _, _]");
		text[3] = new String("[_, _, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		testProblem_internal(state, 150000, 1);
		testProblem_internal(state, 150000, 16);
	}

	public void testCandidate6() {
		String[] text = new String[4];
		text[0] = new String("[B, _, _, _]");
		text[1] = new String("[_, _, W, B]");
		text[2] = new String("[B, _, _, _]");
		text[3] = new String("[_, _, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		List<Candidate> candidate = ta.getCandidate(Constant.BLACK, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}
	}

	/**
	 * score <= 0.<br/>
	 * Step [point=[3,3], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,2], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,3], color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,2], color=White, index=4, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,4], color=Black, index=5, loopSuperior= false, name=null]<br/>
	 * Step [point=[4,2], color=White, index=6, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,3], color=Black, index=7, loopSuperior= false, name=null]<br/>
	 * 
	 * 69843<br/>
	 * Score=1<br/>
	 * Step [point=[2,3], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,3], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,4], color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,2], color=White, index=4, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,3], color=Black, index=5, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,2], color=White, index=6, loopSuperior= false, name=null]<br/>
	 * Step [point=[PAS], color=Black, index=7, loopSuperior= false, name=null]<br/>
	 * Step [point=[PAS], color=White, index=8, loopSuperior= false, name=null]<br/>
	 * 有典型性,我都不知从何下手,只能死算. Score=-3<br/>
	 * Step [point=[2,2], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[4,2], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,4], color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,3], color=White, index=4, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,3], color=Black, index=5, loopSuperior= false, name=null]<br/>
	 * Step [point=[4,4], color=White, index=6, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,1], color=Black, index=7, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,2], color=White, index=8, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,3], color=Black, index=9, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,2], color=White, index=10, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,4], color=Black, index=11, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,4], color=White, index=12, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,1], color=Black, index=13, loopSuperior= false, name=null]<br/>
	 * Step [point=[PAS], color=White, index=14, loopSuperior= false, name=null]<br/>
	 * Step [point=[PAS], color=Black, index=15, loopSuperior= false, name=null]<br/>
	 * 
	 * Score=-16<br/>
	 * Step [point=[3,4], color=Black, index=13, loopSuperior= false, name=null]<br/>
	 * Step [point=[4,2], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,3], color=Black, index=14, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,2], color=White, index=4, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,3], color=Black, index=15, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,2], color=White, index=6, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,4], color=Black, index=16, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,3], color=White, index=8, loopSuperior= false, name=null]<br/>
	 * Step [point=[PAS], color=Black, index=9, loopSuperior= false, name=null]<br/>
	 * Step [point=[4,4], color=White, index=10, loopSuperior= false, name=null]<br/>
	 * Step [point=[PAS], color=Black, index=11, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,4], color=White, index=19, loopSuperior= false, name=null]<br/>
	 * 
	 * Score=-5<br/>
	 * that's the solution! Step [point=[3,3], color=Black, index=1,
	 * loopSuperior= false, name=null]<br/>
	 * Step [point=[2,3], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,2], color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,1], color=White, index=4, loopSuperior= false, name=null]<br/>
	 * Step [point=[3,4], color=Black, index=5, loopSuperior= false, name=null]<br/>
	 * Step [point=[4,2], color=White, index=6, loopSuperior= false, name=null]<br/>
	 * Step [point=[PAS], color=Black, index=7, loopSuperior= false, name=null]<br/>
	 * Step [point=[PAS], color=White, index=8, loopSuperior= false, name=null]<br/>
	 */

	public void testProblem7() {
		String[] text = new String[4];
		text[0] = new String("[W, W, _, W]");
		text[1] = new String("[_, _, _, W]");
		text[2] = new String("[W, _, _, _]");
		text[3] = new String("[B, _, B, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		testProblem_internal(state, 15000 * 5, -15);

	}

	public void testCandidate7() {
		String[] text = new String[4];
		text[0] = new String("[W, W, B, _]");
		text[1] = new String("[_, W, B, _]");
		text[2] = new String("[W, W, W, B]");
		text[3] = new String("[B, _, B, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		List<Candidate> candidate = ta.getCandidate(Constant.WHITE, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}
	}

	/**
	 * score == 2.<br/>
	 * Step [point=[4,2], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,4], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,1], color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,1], color=White, index=4, loopSuperior= false, name=null] <br/>
	 * Step [point=[1,2], color=Black, index=5, loopSuperior= false, name=null] <br/>
	 * Step [point=[PAS], color=White, index=6, loopSuperior= false, name=null] <br/>
	 * Step [point=[PAS], color=Black, index=7, loopSuperior= false, name=null]
	 */
	public void testProblem8() {
		String[] text = new String[4];
		text[0] = new String("[_, B, B, _]");
		text[1] = new String("[_, W, W, B]");
		text[2] = new String("[B, W, B, W]");
		text[3] = new String("[W, _, B, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		testProblem_internal(state, 1);
		testProblem_internal(state, 16);
	}

	public void testFinalState13() {
		String[] text = new String[4];
		text[0] = new String("[B, _, W, _]");
		text[1] = new String("[B, B, B, W]");
		text[2] = new String("[W, W, W, W]");
		text[3] = new String("[_, B, B, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		Logger.getLogger(SmallGoBoard.class).setLevel(Level.WARN);
		boolean finalState_deadExist = ta.isFinalState_deadExist();
		assertTrue(finalState_deadExist);
		int score = ta.finalResult_deadExist().getScore();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(-16, score);
		// testProblem_internal(state, 150000, 16);
	}

	public void testFinalState13B() {
		String[] text = new String[4];
		text[0] = new String("[_, B, B, _]");
		text[1] = new String("[B, B, B, W]");
		text[2] = new String("[W, W, W, _]");
		text[3] = new String("[W, B, _, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		Logger.getLogger(SmallGoBoard.class).setLevel(Level.WARN);

		Point point = Point.getPoint(4, 2, 1);
		boolean live = ta.isAlreadyLive_dynamic(point);
		assertTrue(live);
		boolean finalState_deadExist = ta.isFinalState_deadExist();
		assertTrue(finalState_deadExist);
		int score = ta.finalResult_deadExist().getScore();
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
		assertEquals(-2, score);
		// testProblem_internal(state, 150000, 16);
	}

	/**
	 * [INIT]B-->[3,4]W-->[4,4]B-->[1,3]W-->[3,4](FINAL 0)<br/>
	 * how to identify that the empty point is not decided yet.
	 */
	public void testFinalState13B2() {
		String[] text = new String[4];
		text[0] = new String("[_, _, B, _]");
		text[1] = new String("[B, B, B, W]");
		text[2] = new String("[B, W, W, W]");
		text[3] = new String("[_, W, _, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		Logger.getLogger(SmallGoBoard.class).setLevel(Level.INFO);

		// Point point = Point.getPoint(4, 2, 1);
		// boolean live = ta.isAlreadyLive_dynamic(point);
		// assertTrue(live);
		boolean finalState_deadExist = ta.isFinalState_deadExist();
		assertTrue(finalState_deadExist);
	}

	/**
	 * CO-LIVE could be solved with big eye model. co live: live without two
	 * eyes (fake eye is allowed)
	 */
	public void testFinalState13B3() {
		String[] text = new String[4];
		text[0] = new String("[B, B, B, _]");
		text[1] = new String("[B, B, B, W]");
		text[2] = new String("[B, W, W, W]");
		text[3] = new String("[_, W, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		Logger.getLogger(SmallGoBoard.class).setLevel(Level.INFO);

		Point point = Point.getPoint(4, 2, 1);
		boolean live = ta.isAlreadyLive_dynamic(point);
		assertTrue(live);
		// boolean finalState_deadExist = ta.isFinalState_deadExist();
		// assertTrue(finalState_deadExist);
		// int score = ta.finalResult_deadExist().getScore();
		// assertEquals(0, score);
	}

	// [INIT]B-->[2,2]W-->[2,3]B-->[1,3]W-->[2,1]B-->[1,1]W-->[2,1]B-->[2,2]W-->[1,2]B-->[1,4]W-->[4,2](FINAL
	// -16)
	public void testFinalState13B4() {
		String[] text = new String[4];
		text[0] = new String("[B, B, B, _]");
		text[1] = new String("[B, B, B, W]");
		text[2] = new String("[B, W, W, W]");
		text[3] = new String("[_, W, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		Logger.getLogger(SmallGoBoard.class).setLevel(Level.INFO);

		Point point = Point.getPoint(4, 2, 1);
		boolean live = ta.isAlreadyLive_dynamic(point);
		assertTrue(live);

	}

	public void testPrintState() {
		String[] text = new String[4];
		text[0] = new String("[W, B, _, B]");
		text[1] = new String("[W, _, _, B]");
		text[2] = new String("[B, W, B, W]");
		text[3] = new String("[B, _, W, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		String a = "B-->[2,2]W-->[2,3]B-->[1,3]W-->[2,1]B-->[1,1]W-->[2,1]";
		List<Step> steps = TestProblemFromZhangChu.toSteps(a, 4);

		for (Step step : steps) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(step);
		}
		for (Step step : steps) {
			ta.oneStepForward(step);
		}
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());

		boolean finalState_deadExist = ta.isFinalState_deadExist();
		assertTrue(finalState_deadExist);
		int score = ta.finalResult_deadExist().getScore();
		assertEquals(-16, score);
	}

	public void testPrintState2() {
		String[] text = new String[4];
		text[0] = new String("[W, B, _, B]");
		text[1] = new String("[W, _, _, B]");
		text[2] = new String("[B, W, B, W]");
		text[3] = new String("[B, _, W, W]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		String a = "B-->[2,2]W-->[2,3]B-->[1,3]";
		List<Step> steps = TestProblemFromZhangChu.toSteps(a, 4);

		for (Step step : steps) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(step);
		}
		for (Step step : steps) {
			ta.oneStepForward(step);
		}
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());

		boolean finalState_deadExist = ta.isFinalState_deadExist();
		assertFalse(finalState_deadExist);

	}

}
