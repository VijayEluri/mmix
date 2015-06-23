package eddie.wu.search.smallboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Block;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.GoBoardForward;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.domain.analy.FinalResult;
import eddie.wu.domain.analy.SmallGoBoard;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.manual.SGFGoManual;
import eddie.wu.manual.StateLoader;
import eddie.wu.manual.TreeGoManual;
import eddie.wu.search.global.Candidate;
import eddie.wu.search.global.GoBoardSearch;
import eddie.wu.search.global.ListAllState;
import eddie.wu.search.small.TwoTwoBoardSearch;

/**
 * 1 second 5/4/2013<br/>
 * It doesn't depends on the dynamic live/death identification for 2*2 board.<br/>
 * but for 3*3 board, live/death identification is already quite important in
 * performance point of view.<br/>
 * 
 * @author Eddie
 * 
 */
public class TestAllState2 extends TestCase {
	private static Logger log = Logger.getLogger(TestAllState2.class);
	static {
		Constant.INTERNAL_CHECK = false;
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.ERROR);
		// Logger.getLogger(GoBoardSearch.class).setLevel(Level.ERROR);
		Logger.getLogger(GoBoardForward.class).setLevel(Level.ERROR);
		Logger.getLogger(TestAllState2.class).setLevel(Level.WARN);
	}

	/**
	 * B-->[1,1]W-->[2,2]B-->[1,2]W-->[2,1
	 */
	public void testGetCandiate() {

		String[] text = new String[2];
		text[0] = new String("[_, _]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		SmallGoBoard sa = new SmallGoBoard(BoardColorState.getInstance(state,
				Constant.BLACK));
		log.warn("symmetry=" + sa.getInitSymmetryResult().getNumberOfSymmetry());
		sa.oneStepForward(new Step(Point.getPoint(2, 1, 1), Constant.BLACK));
		log.warn("symmetry="
				+ sa.getLastStep().getSymmetry().getNumberOfSymmetry());
		sa.oneStepForward(new Step(Point.getPoint(2, 2, 2), Constant.WHITE));
		log.warn("symmetry="
				+ sa.getLastStep().getSymmetry().getNumberOfSymmetry());
		sa.oneStepForward(new Step(Point.getPoint(2, 1, 2), Constant.BLACK));
		log.warn("symmetry="
				+ sa.getLastStep().getSymmetry().getNumberOfSymmetry());
		sa.oneStepForward(new Step(Point.getPoint(2, 2, 1), Constant.WHITE));

		log.warn("symmetry="
				+ sa.getLastStep().getSymmetry().getNumberOfSymmetry());

		List<Candidate> candidate = sa.getCandidate(Constant.BLACK, false);
		if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
			sa.printState(log);
			log.warn("Without filtering symmetryic candidate:");
			for (Candidate can : candidate) {
				log.warn(can);
			}
		}

		candidate = sa.getCandidate(Constant.BLACK, true);
		if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
			log.warn("With filtering symmetryic candidate:");
			for (Candidate can : candidate) {
				log.warn(can);
			}
		}
		// we will not filter by symmetry in this context.
		assertEquals(0, sa.getLastStep().getSymmetry().getNumberOfSymmetry());

	}

	/**
	 * final path:<br/>
	 * Step [point=[1,1], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,2], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Step [point=null, color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=null, color=White, index=4, loopSuperior= false, name=null]<br/>
	 * Score=0<br/>
	 * New result after allow symmetric different candidate:<br/>
	 * Step [point=[1,1], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,2], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,2], color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,1], color=White, index=4, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,2], color=Black, index=5, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,1], color=White, index=6, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,2], color=Black, index=7, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,1], color=White, index=8, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,1], color=Black, index=9, loopSuperior= false, name=null]<br/>
	 * Step [point=Give Up, color=White, index=10, loopSuperior= false,
	 * name=null]<br/>
	 * Step [point=Give Up, color=Black, index=11, loopSuperior= false,
	 * name=null]<br/>
	 */
	public void testState1_blackFirst() {
		String[] text = new String[2];
		text[0] = new String("[_, _]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		// if(log.isEnabledFor(org.apache.log4j.Level.WARN))
		// log.warn(Arrays.deepToString(state));

		TwoTwoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.BLACK, 1,
				0);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);

		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if (log.isEnabledFor(org.apache.log4j.Level.WARN))
				log.warn(list);
			if (count % 100 == 0)
				if (log.isEnabledFor(org.apache.log4j.Level.WARN))
					log.warn("count=" + count);
		}
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(goS.getSearchProcess().size());

		assertEquals(1, score);

		String name = goS.getGoBoard().getInitColorState()
				.getStateAsOneLineString()
				+ ".sgf";
		String fileName = Constant.rootDir + "smallboard/twotwo/" + name;
		TreeGoManual manual = goS.getTreeGoManual();
		int initScore = manual.initScore();
		log.warn("Init score = " + initScore);
		manual.cleanupBadMove_firstWin(goS.initTurn, goS.getMaxExp());
		log.debug(manual.getExpandedString(false));// init variant
		log.warn(manual.getSGFBodyString(false));
		SGFGoManual.storeGoManual(fileName, manual);
		// SearchNode root = goS.getGoBoard().getRoot();
		// if(log.isEnabledFor(Level.WARN)) log.warn(root.getSGFBodyString());
		TreeGoManual tree = goS.getGoBoard().getTreeGoManual();
		if (log.isEnabledFor(Level.WARN))
			log.warn(tree.getSGFBodyString());

		goS = new TwoTwoBoardSearch(state, Constant.BLACK, 2, 1);
		score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);

		count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if (log.isEnabledFor(org.apache.log4j.Level.WARN))
				log.warn(list);
			if (count % 100 == 0)
				if (log.isEnabledFor(org.apache.log4j.Level.WARN))
					log.warn("count=" + count);
		}
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(goS.getSearchProcess().size());
		assertEquals(1, score);

	}

	public void testState1_whiteFirst() {
		String[] text = new String[2];
		text[0] = new String("[_, _]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		// if(log.isEnabledFor(org.apache.log4j.Level.WARN))
		// log.warn(Arrays.deepToString(state));

		GoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.WHITE, 0, -1);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);
		assertEquals(-1, score);

	}

	/**
	 * about 5 seconds: 04s without log<br/>
	 * Step [point=[1,1], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,2], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,2], color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,1], color=White, index=4, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,2], color=Black, index=5, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,1], color=White, index=6, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,2], color=Black, index=7, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,1], color=White, index=8, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,1], color=Black, index=9, loopSuperior= false, name=null]<br/>
	 * Step [point=Give Up, color=White, index=10, loopSuperior= false,
	 * name=null]<br/>
	 * Step [point=Give Up, color=Black, index=11, loopSuperior= false,
	 * name=null]<br/>
	 */
	public void testState1A_blackFirst() {
		String[] text = new String[2];
		text[0] = new String("[_, _]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		GoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.BLACK, 2, 1);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);
		assertEquals(1, score);
	}

	public void testState1A_whiteFirst() {
		String[] text = new String[2];
		text[0] = new String("[_, _]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		GoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.WHITE, -1, -2);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);
		assertEquals(-1, score);
	}

	/**
	 * Step [point=null, color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=null, color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Score=0<br/>
	 * new result<br/>
	 * Step [point=[1,2], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,1], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,1], color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=Give Up, color=White, index=4, loopSuperior= false,
	 * name=null]<br/>
	 * Step [point=[1,2], color=Black, index=5, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,1], color=White, index=6, loopSuperior= false, name=null]<br/>
	 * Step [point=Give Up, color=Black, index=7, loopSuperior= false,
	 * name=null]<br/>
	 * Step [point=Give Up, color=White, index=8, loopSuperior= false,
	 * name=null]<br/>
	 * Score=1
	 */
	public void testState2() {
		String[] text = new String[2];
		text[0] = new String("[B, _]");
		text[1] = new String("[_, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(Arrays.deepToString(state));

		GoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.BLACK, 1, 0);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);
		assertEquals(1, score);
	}

	public void testState2_black() {
		String[] text = new String[2];
		text[0] = new String("[W, W]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(Arrays.deepToString(state));

		GoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.BLACK, 1, 0);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);
		assertEquals(1, score);
	}

	public void testState2_A() {
		String[] text = new String[2];
		text[0] = new String("[B, _]");
		text[1] = new String("[_, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TwoTwoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.WHITE, 0,
				-1);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
			log.warn(goS.getGoBoard().getInitColorState().getStateString());
			log.warn("Score=" + score);
		}

		assertEquals(-1, score);
	}

	public void testState3_A() {
		String[] text = new String[2];
		text[0] = new String("[W, W]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		GoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.WHITE, 1, 0);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);
		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if (log.isEnabledFor(org.apache.log4j.Level.WARN))
				log.warn(list);
			if (count % 100 == 0)
				if (log.isEnabledFor(org.apache.log4j.Level.WARN))
					log.warn("count=" + count);
		}
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(goS.getSearchProcess().size());
		assertEquals(1, score);
	}

	/**
	 * the state reached firstly by passing is still not allowed for
	 * duplication. <br/>
	 * [INIT]W-->[PAS]B-->[2,1]W-->[2,2]B-->[2,1]W-->[1,2]B-->[2,2]W-->[1,1]
	 */
	public void testDuplicateA() {
		String[] text = new String[2];
		text[0] = new String("[W, W]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		GoBoard go = new GoBoard(state, Constant.WHITE);
		for (BoardColorState stateC : go.getStepHistory().getColorStates()) {
			if (log.isEnabledFor(org.apache.log4j.Level.WARN))
				log.warn(stateC.getStateString());
		}

		List<Step> steps = new ArrayList<Step>();
		Step step;
		step = new Step(null, Constant.WHITE);
		steps.add(step);
		step = new Step(Point.getPoint(2, 2, 1), Constant.BLACK);
		steps.add(step);
		step = new Step(Point.getPoint(2, 2, 2), Constant.WHITE);
		steps.add(step);
		step = new Step(Point.getPoint(2, 2, 1), Constant.BLACK);
		steps.add(step);
		step = new Step(Point.getPoint(2, 1, 2), Constant.WHITE);
		steps.add(step);
		step = new Step(Point.getPoint(2, 2, 2), Constant.BLACK);
		steps.add(step);
		step = new Step(Point.getPoint(2, 1, 1), Constant.WHITE);
		steps.add(step);

		boolean valid = false;
		for (Step stepT : steps) {
			valid = go.oneStepForward(stepT);
			if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
				log.warn(stepT + "reach state as blow: valid >>>> " + valid);
				log.warn(go.getBoardColorState().getStateString());
				log.warn("KNown history state "
						+ go.getStepHistory().getColorStates().size());
				for (BoardColorState stateC : go.getStepHistory()
						.getColorStates()) {
					log.warn(stateC.getStateString());
				}
			}

		}

		assertFalse(valid);
	}

	public void testAAA() {
		List<Step> steps = new ArrayList<Step>();
		Step step;
		step = new Step(null, Constant.WHITE);
		steps.add(step);
		step = new Step(Point.getPoint(2, 2, 1), Constant.BLACK);
		steps.add(step);
		step = new Step(null, Constant.WHITE);
		steps.add(step);
		step = new Step(Point.getPoint(2, 2, 2), Constant.BLACK);
		steps.add(step);
		step = new Step(Point.getPoint(2, 1, 1), Constant.WHITE);
		steps.add(step);
		step = new Step(null, Constant.BLACK);
		steps.add(step);
		step = new Step(Point.getPoint(2, 1, 2), Constant.WHITE);
		steps.add(step);

		String[] text = new String[2];
		text[0] = new String("[W, W]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		GoBoard go = new GoBoard(state, Constant.WHITE);
		for (BoardColorState stateC : go.getStepHistory().getColorStates()) {
			if (log.isEnabledFor(org.apache.log4j.Level.WARN))
				log.warn(stateC.getStateString());
		}

		boolean valid = false;
		for (Step stepT : steps) {
			valid = go.oneStepForward(stepT);
			if (log.isEnabledFor(org.apache.log4j.Level.WARN))
				log.warn(stepT + ">>>>" + valid);
		}
		assertFalse(valid);
		for (BoardColorState stateC : go.getStepHistory().getColorStates()) {
			if (log.isEnabledFor(org.apache.log4j.Level.WARN))
				log.warn(stateC.getStateString());
		}
	}

	public void testState3_blackFirst() {
		String[] text = new String[2];
		text[0] = new String("[W, W]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(Arrays.deepToString(state));

		GoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.BLACK, 1, 0);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);

		for (String list : goS.getSearchProcess()) {
			if (log.isEnabledFor(org.apache.log4j.Level.WARN))
				log.warn(list);
		}
		assertEquals(1, score);

	}

	public void testState3_ABB() {
		String[] text = new String[2];
		text[0] = new String("[W, W]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(Arrays.deepToString(state));

		GoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.WHITE, 1, 0);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);

		for (String list : goS.getSearchProcess()) {
			if (log.isEnabledFor(org.apache.log4j.Level.WARN))
				log.warn(list);
		}
		assertEquals(1, score);

	}

	public void testState3_duplicate() {
		String[] text = new String[2];
		text[0] = new String("[W, W]");
		text[1] = new String("[_, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(Arrays.deepToString(state));

		GoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.BLACK, 0, -1);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);

		int count = 0;
		for (String list : goS.getSearchProcess()) {
			count++;
			if (log.isEnabledFor(org.apache.log4j.Level.WARN))
				log.warn(list);
			if (count % 100 == 0)
				if (log.isEnabledFor(org.apache.log4j.Level.WARN))
					log.warn("count=" + count);
		}
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(goS.getSearchProcess().size());
		assertEquals(-1, score);

	}

	public void testState3_AB() {
		String[] text = new String[2];
		text[0] = new String("[W, W]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(Arrays.deepToString(state));

		GoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.WHITE, 2, 1);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);
		assertEquals(1, score);

		for (String list : goS.getSearchProcess()) {
			if (log.isEnabledFor(org.apache.log4j.Level.WARN))
				log.warn(list);
		}
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(goS.getSearchProcess().size());
	}

	/**
	 * still failed, too deep!
	 */
	public void testState2_AA() {
		String[] text = new String[2];
		text[0] = new String("[B, _]");
		text[1] = new String("[_, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(Arrays.deepToString(state));

		GoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.BLACK, 4, 3);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);
		assertEquals(1, score);

		goS = new TwoTwoBoardSearch(state, Constant.WHITE, -3, -4);
		score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn("Score=" + score);
		assertEquals(-1, score);
	}

	/**
	 * final path:<br/>
	 * Step [point=[1,1], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,2], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Step [point=null, color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=null, color=White, index=4, loopSuperior= false, name=null]<br/>
	 * Score=0<br/>
	 * new result:<br/>
	 * final path:<br/>
	 * Step [point=[1,1], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,2], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,2], color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,1], color=White, index=4, loopSuperior= false, name=null]<br/>
	 * Step [point=[1,1], color=Black, index=5, loopSuperior= false, name=null]<br/>
	 * Step [point=Give Up, color=White, index=6, loopSuperior= false,
	 * name=null]<br/>
	 * Step [point=[1,2], color=Black, index=7, loopSuperior= false, name=null]<br/>
	 * Step [point=[2,1], color=White, index=8, loopSuperior= false, name=null]<br/>
	 * Step [point=Give Up, color=Black, index=9, loopSuperior= false,
	 * name=null]<br/>
	 * Step [point=Give Up, color=White, index=10, loopSuperior= false,
	 * name=null]<br/>
	 * Score=1
	 */
	public void testState223() {
		String fileName = "doc/围棋程序数据/smallboard/twotwo/_WWW.sgf.complex";
		String[] text = new String[2];
		text[0] = new String("[_, W]");
		text[1] = new String("[W, W]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		TreeGoManual manual = null;
		GoBoardSearch goS = new TwoTwoBoardSearch(state, Constant.BLACK, 1, 0);
		int score = goS.globalSearch();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
			log.warn("Score=" + score);
			for (String list : goS.getSearchProcess()) {
				log.warn(list);
			}
			log.warn(goS.getSearchProcess().size());
			manual = goS.getTreeGoManual();
			log.warn("Search Tree");
			log.warn(manual.getSGFBodyString());
			log.warn("simple manual");
			log.warn(manual.getExpandedString());
			log.warn(manual.getVariant());
		}
		assertEquals(1, score);

		manual = goS.getGoBoard().getTreeGoManual();

		if (log.isEnabledFor(Level.WARN)) {
			log.warn("Tree manual");
			log.warn(manual.getSGFBodyString());
			log.warn("simple manual");
			log.warn(manual.getExpandedString());
			log.warn(manual.getVariant());
		}
		SGFGoManual.storeGoManual(fileName, manual);

	}

	public void testFinalResult() {
		String[] text = new String[2];
		text[0] = new String("[_, B]");
		text[1] = new String("[B, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis analysis = new TerritoryAnalysis(state);
		boolean state2 = analysis.isFinalState_deadExist();
		assertTrue(state2);

		state2 = analysis.isFinalState_deadCleanedUp();
		assertTrue(state2);

		int finalState = analysis.finalStateType();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(finalState);
		assertEquals(2, finalState);
		FinalResult result = analysis.finalResult_deadExist();
		if (log.isEnabledFor(org.apache.log4j.Level.WARN))
			log.warn(result);
		// if (log.isDebugEnabled())
		// log.debug(result);
	}

	/**
	 * There are only 26 real states.
	 */
	public void testAllState() {
		int count = 0;
		Set<BoardColorState> validState = new ListAllState().getValidState(2);
		log.warn("Real valid states = " + validState.size());
		for (BoardColorState state : validState) {
			int score = 0;
			score = TwoTwoBoardSearch.getAccurateScore(state);
			state.setScore(score);
		}

		for (BoardColorState state : validState) {
			count++;
			if (log.isEnabledFor(org.apache.log4j.Level.WARN)) {
				log.warn("==========================");
				log.warn("count=" + count);
				log.warn("State=" + state.getStateString());
				log.warn("Score=" + state.getScore());
				log.warn("variant=" + state.getVariant());
			}
		}
	}

}
