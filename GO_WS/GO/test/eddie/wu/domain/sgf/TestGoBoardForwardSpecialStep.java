package eddie.wu.domain.sgf;

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
import eddie.wu.manual.SGFGoManual;
import eddie.wu.manual.SimpleGoManual;
import eddie.wu.manual.StateLoader;

public class TestGoBoardForwardSpecialStep extends TestCase {
	private static Logger log = Logger.getLogger(Block.class);

	/**
	 * [B, _, B, W]<br/>
	 * [B, B, B, W]<br/>
	 * [B, _, _, _]<br/>
	 * [_, _, B, B]<br/>
	 * [point=[2,2], color=Black, index=1, loopSuperior= false, name=null]<br/>
	 * [point=[3,3], color=White, index=2, loopSuperior= false, name=null]<br/>
	 * [point=[2,3], color=Black, index=3, loopSuperior= false, name=null]<br/>
	 * [point=[3,2], color=White, index=4, loopSuperior= false, name=null]<br/>
	 * [point=[2,1], color=Black, index=5, loopSuperior= false, name=null]<br/>
	 * [point=[3,4], color=White, index=6, loopSuperior= false, name=null] <br/>
	 * [point=[1,3], color=Black, index=7, loopSuperior= false, name=null] <br/>
	 * [point=[2,4], color=White, index=8, loopSuperior= false, name=null] <br/>
	 * [point=[3,1], color=Black, index=9, loopSuperior= false, name=null] <br/>
	 * [point=[4,2], color=White, index=10, loopSuperior= false, name=null] <br/>
	 * [point=[1,1], color=Black, index=11, loopSuperior= false, name=null] <br/>
	 * [point=[1,4], color=White, index=12, loopSuperior= false, name=null] <br/>
	 * [point=[4,4], color=Black, index=13, loopSuperior= false, name=null] <br/>
	 * [point=[4,1], color=White, index=14, loopSuperior= false, name=null] <br/>
	 * [point=[4,3], color=Black, index=15, loopSuperior= false, name=null] <br/>
	 * [point=[2,4], color=White, index=16, loopSuperior= false, name=null]<br/>
	 * [point=[1,4],
	 */
	public void test4() {
		Logger.getLogger(GoBoard.class).setLevel(Level.DEBUG);
		Logger.getLogger(GoBoardForward.class).setLevel(Level.DEBUG);
		Logger.getLogger(BoardColorState.class).setLevel(Level.DEBUG);
		String[] text = new String[4];
		text[0] = new String("[B, _, B, _]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[B, _, _, _]");
		text[3] = new String("[_, _, B, B]");

		byte[][] state = StateLoader.LoadStateFromText(text);
		GoBoard go = new GoBoard(state);
		go.oneStepForward(new Step(Point.getPoint(4, 2, 4), Constant.WHITE));
		go.output();

		// go.oneStepForward(new Step(Point.getPoint(4, 1, 4), Constant.WHITE));
		// go.output();

	}

	public void test4_steps_sgf() {
		Logger.getLogger(GoBoard.class).setLevel(Level.DEBUG);
		Logger.getLogger(GoBoardForward.class).setLevel(Level.DEBUG);
		Logger.getLogger(BoardColorState.class).setLevel(Level.DEBUG);

		String fileName = Constant.DEBUG_MANUAL + "858220614.sgf";
		SimpleGoManual goManual = SGFGoManual.loadSimpleGoManual(fileName);
		GoBoard go = new GoBoard(4);
		for (int i = 0; i < goManual.getShouShu() - 2; i++) {
			if (i == 15) {
				if (log.isEnabledFor(Level.WARN))
					log.warn("i==15");
			}

			go.oneStepForward(goManual.getStep(i));

		}
		// go.output();
		go.oneStepForward(goManual.getStep(15));
		go.output();

		for (Step step : goManual.getSteps()) {
			if (log.isEnabledFor(Level.WARN))
				log.warn(step);
		}

		// for(Step step: goManual.getSteps()){
		// go.oneStepForward(step);
		// }
		// go.output();
	}

}
