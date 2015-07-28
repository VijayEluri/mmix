package eddie.wu.search.global;

import java.util.ArrayList;
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
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.manual.SearchNode;
import eddie.wu.manual.StateLoader;
import eddie.wu.manual.TreeGoManual;
import eddie.wu.search.small.ThreeThreeBoardSearch;

public class TestProblemFromZhangChu extends TestCase {
	private static Logger log = Logger.getLogger(TestProblemFromZhangChu.class);
	static {
		// Logger.getLogger(GoBoardSearch.class).setLevel(Level.WARN);
	}

	public void testProblem_internal(byte[][] state, int blackExp) {
		testProblem_internal(state, 50000, blackExp);
	}

	public int testProblem_internal(byte[][] state, int variant, int blackExp) {
		return testProblem_internal(state, Constant.BLACK, variant, blackExp,
				blackExp - 1);
	}

	public int testProblem_internal_whiteFirst(byte[][] state, int variant,
			int whiteExp) {
		return testProblem_internal(state, Constant.WHITE, variant,
				whiteExp + 1, whiteExp);
	}

	public int testProblem_internal(byte[][] state, int color, int variant,
			int blackExp, int whiteExp) {

		ThreeThreeBoardSearch goS = new ThreeThreeBoardSearch(state, color,
				blackExp, whiteExp);
		goS.setVariant(variant);
		int score = goS.globalSearch();
		if (log.isEnabledFor(Level.WARN))
			log.warn(goS.getGoBoard().getInitColorState().getStateString());

		int size = goS.getSearchProcess().size();
		if (log.isEnabledFor(Level.WARN))
			log.warn(size);
		if (size > 500) {
			int count = 0;
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
			int count = 0;
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
			log.warn("Score=" + score);
		// assertTrue(score >= 1);
		if (goS.getBestResult() != null) {
			for (Step step : goS.getBestResult()) {
				if (log.isEnabledFor(Level.WARN))
					log.warn(step + "<br/>");
			}
		}
		return score;
	}

	/**
	 * right solution: +2
	 */
	public void testProblem1() {
		String[] text = new String[4];
		text[0] = new String("[W, _, W, W]");
		text[1] = new String("[W, B, B, W]");
		text[2] = new String("[W, B, B, W]");
		text[3] = new String("[W, W, _, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		testProblem_internal(state, 200000, 1);
		// testProblem_internal(state, 16);
	}

	public void testProblem1A() {
		String[] text = new String[4];
		text[0] = new String("[W, _, W, W]");
		text[1] = new String("[W, B, B, W]");
		text[2] = new String("[W, B, B, W]");
		text[3] = new String("[W, W, _, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		testProblem_internal(state, 200000, 1);
		// testProblem_internal(state, 16);
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

		// 27s
		testProblem_internal(state, 15000, 1);
	}

	public void testProblem6D() {
		String[] text = new String[4];
		text[0] = new String("[B, _, _, _]");
		text[1] = new String("[_, _, W, B]");
		text[2] = new String("[B, _, _, _]");
		text[3] = new String("[_, _, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		int internal = testProblem_internal(state, 150000, 16);
		assertEquals(16, internal);
	}

	/**
	 * [INIT]B-->[2,2]W-->[3,4]B-->[1,3]W-->[1,4]B-->[4,2]W-->[1,2]B-->[3,3]W-->
	 * [2,1]B-->[2,4]W-->[3,2]B-->[1,3]W-->[4,1]B-->[2,2]W-->[4,3]B-->[1,1]W-->[
	 * 3,4]B-->[3,1]W-->[2,3]B-->[4,2]W-->[1,4]B-->[3,3]W-->[1,2]B-->[4,4]W-->[2
	 * ,1]B-->[2,4]W-->[3,2]B-->[1,3]W-->[4,1]B-->[2,2]W-->[PAS](KNOWN -16)<br/>
	 * [INIT]B-->[2,2]W-->[3,4]B-->[1,3]W-->[1,4]B-->[4,2]W-->[1,2]B-->[3,3]W-->
	 * [2,1]B-->[2,4]W-->[3,4](KNOWN -16)
	 * 
	 */
	public void testProblem61() {
		String[] text = new String[4];
		text[0] = new String("[B, _, _, _]");
		text[1] = new String("[_, _, W, B]");
		text[2] = new String("[B, W, B, _]");
		text[3] = new String("[_, _, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		int score = testProblem_internal(state, 1500, 16);
		assertEquals(16, score);
		// testProblem_internal(state, 150000, 16);
	}

	// [INIT]W-->[1,4]B-->[1,3]W-->[2,2]B-->[4,2]W-->[3,2]B-->[3,4]W-->[4,3]B-->[1,2]W-->[4,1]
	// B-->[4,4]W-->[1,4]B-->[3,4]W-->[2,1]B-->[1,3]W-->[1,2]B-->[2,4]W-->[3,3](-16
	// EXHAUST)
	public void testProblem611() {
		// Logger.getLogger(SmallBoardGlobalSearch.class).setLevel(Level.WARN);
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.INFO);
		String[] text = new String[4];
		text[0] = new String("[B, _, _, _]");
		text[1] = new String("[_, _, W, B]");
		text[2] = new String("[B, _, B, _]");
		text[3] = new String("[_, _, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		testProblem_internal_whiteFirst(state, 15000, 15);
		// testProblem_internal(state, 150000, 16);
	}

	// [INIT]W-->[3,2]B-->[2,2]W-->[3,4]B-->[1,3]W-->[1,4]B-->[4,2]W-->[1,2]B-->[3,3]
	// W-->[2,1]B-->[2,4]W-->[3,2]B-->[1,3]W-->[4,1]B-->[2,2]W-->[4,3]B-->[1,1]
	// W-->[3,4]B-->[3,1]W-->[2,3]B-->[4,2]W-->[1,4]B-->[3,3]W-->[1,2]B-->[4,4]
	// W-->[2,1]B-->[2,4]W-->[3,2]B-->[PAS](KNOWN -16)

	// [INIT]W-->[3,2]B-->[2,2]W-->[3,4]B-->[1,3]W-->[1,4]B-->[4,1]W-->[1,2]B-->[4,2]W-->[2,1](FINAL
	// -16)
	public void testCandidate611E() {
		String[] text = new String[4];
		text[0] = new String("[B, W, _, W]");
		text[1] = new String("[_, B, W, _]");
		text[2] = new String("[B, W, _, W]");
		text[3] = new String("[B, _, W, W]");

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

	public void testCandidate611F() {
		String[] text = new String[4];
		text[0] = new String("[B, W, _, W]");
		text[1] = new String("[_, B, W, _]");
		text[2] = new String("[B, _, B, W]");
		text[3] = new String("[_, B, W, _]");

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

	public void testCandidate611C() {
		String[] text = new String[4];
		text[0] = new String("[B, _, _, _]");
		text[1] = new String("[_, _, W, B]");
		text[2] = new String("[B, _, B, _]");
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

	// [INIT]W-->[3,4]B-->[4,2]W-->[3,4]B-->[4,4]W-->[1,2]B-->[3,2]W-->[2,1]B-->[2,2]W-->[1,3]B-->[1,1](16
	// EXHAUST)
	public void testCandidate611D() {
		String[] text = new String[4];
		text[0] = new String("[B, W, _, _]");
		text[1] = new String("[_, _, W, B]");
		text[2] = new String("[B, _, B, _]");
		text[3] = new String("[_, B, _, B]");
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

	public void testProblem611B() {
		String[] text = new String[4];
		text[0] = new String("[B, _, _, _]");
		text[1] = new String("[W, W, W, B]");
		text[2] = new String("[B, _, B, B]");
		text[3] = new String("[_, B, _, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		testProblem_internal_whiteFirst(state, 15000, 15);
		// testProblem_internal(state, 150000, 16);
	}

	// [INIT]W-->[4,1]B-->[4,2]W-->[2,2]B-->[3,4]W-->[4,3]B-->
	// [4,4]W-->[2,1]B-->[3,2]W-->[1,3]B-->[1,4](16 EXHAUST)

	public void testCandidate611B() {
		String[] text = new String[4];
		text[0] = new String("[B, _, B, _]");
		text[1] = new String("[_, W, W, B]");
		text[2] = new String("[B, W, B, B]");
		text[3] = new String("[_, B, W, _]");
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

	public void testCandidate611() {
		String[] text = new String[4];
		text[0] = new String("[B, _, _, _]");
		text[1] = new String("[_, W, W, B]");
		text[2] = new String("[B, _, B, _]");
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

	public void testCandidate612() {
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

	public void testCandidate611b() {
		String[] text = new String[4];
		text[0] = new String("[B, _, _, _]");
		text[1] = new String("[_, W, W, B]");
		text[2] = new String("[B, _, B, _]");
		text[3] = new String("[_, B, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state, Constant.WHITE);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		List<Candidate> candidate = ta.getCandidate(Constant.WHITE, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}
	}

	public void testProblem62() {
		String[] text = new String[4];
		text[0] = new String("[B, _, _, _]");
		text[1] = new String("[_, _, W, B]");
		text[2] = new String("[B, B, W, _]");
		text[3] = new String("[_, _, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		int score = testProblem_internal(state, 15000, 1);
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
	}

	public void testProblem6B() {
		String[] text = new String[4];
		text[0] = new String("[B, _, _, _]");
		text[1] = new String("[_, _, W, B]");
		text[2] = new String("[B, _, _, _]");
		text[3] = new String("[_, _, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		int score = testProblem_internal(state, 15000, 16);
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
	}

	public void testProblem6C() {
		String[] text = new String[4];
		text[0] = new String("[B, _, _, _]");
		text[1] = new String("[_, _, W, B]");
		text[2] = new String("[B, W, _, _]");
		text[3] = new String("[_, B, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		int score = testProblem_internal(state, 15000, 16);
		if (log.isEnabledFor(Level.WARN))
			log.warn("score=" + score);
	}

	public void testCandidate6C() {
		String[] text = new String[4];
		text[0] = new String("[B, _, _, _]");
		text[1] = new String("[_, _, W, B]");
		text[2] = new String("[B, W, B, _]");
		text[3] = new String("[_, B, W, W]");
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

	// [INIT]B-->[1,3]W-->[2,2]B-->[4,2]W-->[2,1]B-->[PAS]W-->[4,1](-16 EXHAUST)
	public void testFinalStateSearch62() {
		String[] text = new String[4];
		text[0] = new String("[B, _, B, _]");
		text[1] = new String("[W, W, W, B]");
		text[2] = new String("[B, B, W, _]");
		text[3] = new String("[_, B, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		// Logger.getLogger(GoBoardSearch.class).setLevel(Level.WARN);
		testProblem_internal(state, 1500, 1);
		// testProblem_internal(state, 150000, 16);
	}

	// [INIT]B-->[PAS]W-->[4,1]B-->[1,2]W-->[1,4]B-->[1,3]W-->[1,2]B-->[3,4]W-->[1,4]B-->[2,4]W-->[3,2](-16
	// EXHAUST)
	public void testFinalState62() {
		String[] text = new String[4];
		text[0] = new String("[_, W, B, _]");
		text[1] = new String("[W, W, W, B]");
		text[2] = new String("[_, _, W, _]");
		text[3] = new String("[W, _, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		Logger.getLogger(SmallGoBoard.class).setLevel(Level.WARN);
		boolean finalState_deadExist = ta.isFinalState_deadExist();
		assertTrue(finalState_deadExist);

		// testProblem_internal(state, 150000, 16);
	}

	// [INIT]B-->[1,2]W-->[4,3]B-->[2,2]W-->[2,3]B-->[3,2]W-->[PAS]B-->[3,3]W-->[1,2]B-->[3,2]W-->[PAS]B-->[2,2]W-->[PAS]B-->[3,3](16
	// EXHAUST)
	public void testFinalState1() {
		String[] text = new String[4];
		text[0] = new String("[W, _, W, W]");
		text[1] = new String("[W, B, B, W]");
		text[2] = new String("[W, B, B, W]");
		text[3] = new String("[W, W, _, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);

		String a = "B-->[1,2]W-->[4,3]B-->[2,2]W-->[2,3]B-->[3,2]W-->[PAS]B-->[3,3]W-->[1,2]B-->[3,2]W-->[PAS]B-->[2,2]W-->[PAS]B-->[3,3]";

		for (Step step : toSteps(a)) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(step);
			ta.oneStepForward(step);
		}
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		Logger.getLogger(SmallGoBoard.class).setLevel(Level.WARN);
		boolean finalState_deadExist = ta.isFinalState_deadExist();
		assertTrue(finalState_deadExist);

		// testProblem_internal(state, 150000, 16);
	}

	public static List<Step> toSteps(String a, int boardSize) {

		int unit = "B-->[1,2]".length();
		int size = a.length() / unit;
		List<Step> steps = new ArrayList<Step>(size);
		for (int i = 0; i < size; i++) {
			String temp = a.substring(0 + unit * i, unit * (i + 1));
			Point point = null;
			int color;
			if (temp.substring(0, 1).equals("B")) {
				color = Constant.BLACK;
			} else {
				color = Constant.WHITE;
			}
			if (!temp.substring(5, 8).equals("PAS")) {
				int row = Integer.valueOf(temp.substring(5, 6));
				int column = Integer.valueOf(temp.substring(7, 8));
				point = Point.getPoint(boardSize, row, column);
			}

			Step step = new Step(point, color);
			steps.add(step);
		}
		return steps;

	}

	public static List<Step> toSteps(String a) {
		return toSteps(a, 4);
	}

	// [INIT]B-->[2,2]W-->[3,2]B-->[1,3]W-->[3,4]B-->[4,1]W-->[1,4]B-->[4,2]W-->[2,1]B-->[3,1]W-->[4,1]B-->[2,4]W-->[2,1]B-->[3,3]W-->[1,2]B-->[4,2]W-->[4,3]B-->[1,1]W-->[3,4]B-->[3,1](16
	// EXHAUST)
	public void testProblem6234() {
		String[] text = new String[4];
		text[0] = new String("[B, _, B, _]");
		text[1] = new String("[_, B, W, B]");
		text[2] = new String("[B, W, _, W]");
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

	public void testFinalState6211() {
		String[] text = new String[4];
		text[0] = new String("[B, _, B, _]");
		text[1] = new String("[W, W, W, B]");
		text[2] = new String("[_, _, W, _]");
		text[3] = new String("[W, _, W, W]");
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

	// [INIT]B-->[1,2]W-->[1,4]B-->[1,3]W-->[4,1]B-->[3,2]W-->[1,2]B-->[3,4](FINAL
	// -14)
	public void testFinalState6212() {
		String[] text = new String[4];
		text[0] = new String("[_, W, B, _]");
		text[1] = new String("[W, W, W, B]");
		text[2] = new String("[_, B, W, B]");
		text[3] = new String("[W, _, W, W]");
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

	// [INIT]B-->[1,2]W-->[1,4]B-->[1,3]W-->[4,1]B-->[3,4]W-->[1,4]B-->[3,1]W-->[3,2]B-->[1,2]W-->[1,1](FINAL
	// -16)
	public void testFinalState6216() {
		String[] text = new String[4];
		text[0] = new String("[_, _, B, W]");
		text[1] = new String("[W, W, W, _]");
		text[2] = new String("[_, W, W, _]");
		text[3] = new String("[W, _, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		Logger.getLogger(SmallGoBoard.class).setLevel(Level.WARN);
		boolean finalState_deadExist = ta.isFinalState_deadExist();
		assertTrue(finalState_deadExist);
		// int score = ta.finalResult_deadExist().getScore();
		// if(log.isEnabledFor(Level.WARN)) log.warn("score="+score);
		// assertEquals(-16, score);
		// testProblem_internal(state, 150000, 16);
	}

	// [INIT]B-->[1,2]W-->[1,4]B-->[1,3]W-->[4,1]B-->[3,4]W-->[1,4]B-->[3,1]W-->[3,2]B-->[2,4](FINAL
	// -16)
	public void testFinalState6216B() {
		String[] text = new String[4];
		text[0] = new String("[_, _, B, W]");
		text[1] = new String("[W, W, W, _]");
		text[2] = new String("[_, W, W, _]");
		text[3] = new String("[W, _, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		Logger.getLogger(SmallGoBoard.class).setLevel(Level.WARN);
		boolean finalState_deadExist = ta.isFinalState_deadExist();
		assertTrue(finalState_deadExist);
	}

	// [INIT]B-->[1,2]W-->[1,4]B-->[1,3]W-->[4,1]B-->[3,2]W-->[1,2]B-->[3,4]W-->[1,4]B-->[2,4]W-->[3,1]
	public void testFinalState621() {
		String[] text = new String[4];
		text[0] = new String("[_, W, B, _]");
		text[1] = new String("[W, W, W, B]");
		text[2] = new String("[_, B, W, _]");
		text[3] = new String("[W, _, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		Logger.getLogger(SmallGoBoard.class).setLevel(Level.WARN);
		boolean finalState_deadExist = ta.isFinalState_deadExist();
		assertTrue(finalState_deadExist);

	}

	// [INIT]B-->[2,2]W-->[1,3]B-->[1,2]W-->[4,2]B-->[3,4]W-->[1,4]B-->[PAS]W-->[4,1]B-->[2,4]W-->[3,4]B-->[2,4]W-->[3,3]B-->[2,3]W-->[4,2]B-->[3,4](16
	// EXHAUST)
	public void testCandidate6215() {
		String[] text = new String[4];
		text[0] = new String("[B, B, W, W]");
		text[1] = new String("[_, B, W, _]");
		text[2] = new String("[B, B, W, _]");
		text[3] = new String("[_, W, W, W]");
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

	public void testCandidate6217() {
		String[] text = new String[4];
		text[0] = new String("[_, _, _, W]");
		text[1] = new String("[W, W, W, B]");
		text[2] = new String("[B, B, W, _]");
		text[3] = new String("[_, B, W, W]");
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

	public void testCandidate62151() {
		String[] text = new String[4];
		text[0] = new String("[B, B, W, W]");
		text[1] = new String("[_, B, W, B]");
		text[2] = new String("[B, B, W, _]");
		text[3] = new String("[W, W, W, W]");
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

	// [INIT]B-->[1,3]W-->[2,2]B-->[4,2]W-->[2,1]B-->[PAS]W-->[4,1]B-->[1,2]W-->[1,4]B-->[1,3]W-->[1,2]B-->[3,4]W-->[1,4]B-->[2,4]W-->[3,2](-16
	// EXHAUST)
	public void testCandidate6213B() {
		String[] text = new String[4];
		text[0] = new String("[B, _, B, _]");
		text[1] = new String("[W, W, W, B]");
		text[2] = new String("[B, B, W, _]");
		text[3] = new String("[_, B, W, W]");
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

	// [INIT]B-->[4,2]W-->[2,2]B-->[1,2]W-->[1,3]B-->[PAS]W
	public void testCandidate6213() {
		String[] text = new String[4];
		text[0] = new String("[B, B, W, _]");
		text[1] = new String("[_, W, W, B]");
		text[2] = new String("[B, B, W, _]");
		text[3] = new String("[_, B, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		if (log.isEnabledFor(Level.WARN))
			log.warn(ta.getBoardColorState().getStateString());
		List<Candidate> candidate = ta.getCandidate(Constant.WHITE, false);
		for (Candidate can : candidate) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(can);
		}
		boolean finalState_deadExist = ta.isFinalState_deadExist();
		// assertTrue(finalState_deadExist);
	}

	public void testFinalState6213() {
		String[] text = new String[4];
		text[0] = new String("[B, _, W, _]");
		text[1] = new String("[W, W, W, B]");
		text[2] = new String("[B, B, W, _]");
		text[3] = new String("[_, B, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		Logger.getLogger(SmallGoBoard.class).setLevel(Level.WARN);
		boolean finalState_deadExist = ta.isFinalState_deadExist();
		assertTrue(finalState_deadExist);
	}

	// [INIT]B-->[PAS]W-->[2,2]B-->[PAS]W-->[1,3]B-->[1,4]W-->[3,4]B-->[PAS]W-->[2,4](-16
	// EXHAUST)
	public void testFinalState6214() {
		String[] text = new String[4];
		text[0] = new String("[B, _, _, _]");
		text[1] = new String("[_, W, W, B]");
		text[2] = new String("[B, B, W, _]");
		text[3] = new String("[_, _, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		Logger.getLogger(SmallGoBoard.class).setLevel(Level.WARN);
		boolean finalState_deadExist = ta.isFinalState_deadExist();
		assertTrue(finalState_deadExist);
	}

	// [INIT]B-->[4,2]W-->[2,2]B-->[1,2]W-->[1,3]B-->[PAS]W-->[2,1]B-->[1,1]W-->[4,1]B-->[PAS]
	public void testCandidate62131() {
		String[] text = new String[4];
		text[0] = new String("[B, _, W, _]");
		text[1] = new String("[W, W, W, B]");
		text[2] = new String("[_, _, W, _]");
		text[3] = new String("[W, _, W, W]");
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

	public void testCandidate62141() {
		String[] text = new String[4];
		text[0] = new String("[B, _, _, _]");
		text[1] = new String("[_, W, W, B]");
		text[2] = new String("[B, B, W, _]");
		text[3] = new String("[_, _, W, W]");
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

	// [INIT]B-->[4,2]W-->[2,2]B-->[1,2]W-->[2,1]B-->[1,4]W-->[4,1]B-->[3,2]W-->[PAS]
	public void testCandidate621() {
		String[] text = new String[4];
		text[0] = new String("[B, B, _, B]");
		text[1] = new String("[W, W, W, B]");
		text[2] = new String("[_, B, W, _]");
		text[3] = new String("[W, _, W, W]");
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

	public void testFinalState6215() {
		String[] text = new String[4];
		text[0] = new String("[B, _, B, _]");
		text[1] = new String("[W, W, W, B]");
		text[2] = new String("[_, _, W, _]");
		text[3] = new String("[W, _, W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard ta = new SmallGoBoard(state);
		Logger.getLogger(SmallGoBoard.class).setLevel(Level.WARN);
		boolean finalState_deadExist = ta.isFinalState_deadExist();
		assertTrue(finalState_deadExist);
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
}
