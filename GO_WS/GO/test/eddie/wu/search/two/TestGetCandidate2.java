package eddie.wu.search.two;

import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import junit.framework.TestCase;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoardForward;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.domain.analy.SmallGoBoard;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.global.Candidate;

public class TestGetCandidate2 extends TestCase {
	private static Logger log = Logger.getLogger(TestAllState2.class);
	/**
	 * WARN (GoBoardSearch.java:233) - with history:
	 * W[1,1]B[2,2]W[1,2]B[2,1]W[1,2]B[1,1] WARN (GoBoardSearch.java:380) - New
	 * Candidate Just initialized: W[PAS]
	 */
	public void test() {
		Logger.getLogger(GoBoardForward.class).setLevel(Level.WARN);
		;
		String[] text = new String[2];
		text[0] = new String("[_, _]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard sb = new SmallGoBoard(state, Constant.WHITE);
		sb.oneStepForward(Point.getPoint(2,1, 1));
		sb.oneStepForward(Point.getPoint(2,2, 2));
		sb.oneStepForward(Point.getPoint(2,1, 2));
		sb.oneStepForward(Point.getPoint(2,2, 1));
		sb.oneStepForward(Point.getPoint(2,1, 2));
		sb.oneStepForward(Point.getPoint(2,1, 1));
		// sb.oneStepForward(Point.getPoint(1, 1));
		List<Candidate> candidate = sb.getCandidate(Constant.WHITE, true);
		sb.printState();
		for (Candidate can : candidate) {
			System.out.println(can);
		}

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
}
