package eddie.wu.domain.sgf;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BasicBlock;
import eddie.wu.domain.BasicGoBoard;
import eddie.wu.domain.BlankBlock;
import eddie.wu.domain.Block;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.GoBoardBackward;
import eddie.wu.domain.GoBoardForward;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.domain.comp.RowColumnComparator;
import eddie.wu.manual.SimpleGoManual;
import eddie.wu.manual.SGFGoManual;

public class TestGoBoardBackward extends TestCase {
	static Logger log = Logger.getLogger(TestGoBoardForward.class);
	static {
		// Logger.getLogger(Block.class).setLevel(Level.DEBUG);
		// Logger.getLogger(Block.class).setLevel(Level.DEBUG);
		// Logger.getLogger(GoBoard.class).setLevel(Level.DEBUG);
		// Logger.getLogger(BasicGoBoard.class).setLevel(Level.DEBUG);
		// Logger.getLogger(Block.class).setLevel(Level.DEBUG);
		// Logger.getLogger(GoBoardForward.class).setLevel(Level.DEBUG);
		// Logger.getLogger(GoBoardBackward.class).setLevel(Level.DEBUG);
		// Logger.getLogger(BoardColorState.class).setLevel(Level.DEBUG);
	}

	void debug_log() {
		Logger.getLogger(Block.class).setLevel(Level.DEBUG);
		Logger.getLogger(Block.class).setLevel(Level.DEBUG);
		Logger.getLogger(GoBoard.class).setLevel(Level.DEBUG);
		Logger.getLogger(BasicGoBoard.class).setLevel(Level.DEBUG);
		Logger.getLogger(Block.class).setLevel(Level.DEBUG);
		Logger.getLogger(GoBoardForward.class).setLevel(Level.DEBUG);
		Logger.getLogger(GoBoardBackward.class).setLevel(Level.DEBUG);
		Logger.getLogger(BoardColorState.class).setLevel(Level.DEBUG);
	}

	public void testOneStep() {
		_testOneStep(221);
	}
	public void _testOneStep(int stepIndex) {
		String fileName = SGFGoManual.getFileName(Constant.currentManualIndex);
		SimpleGoManual manual = SGFGoManual.loadSimpleGoManual(fileName);
		GoBoard goBoard = new GoBoard();
		for (int i = 0; i < stepIndex + 1; i++) {
			goBoard.oneStepForward(manual.getStep(i));
			log.info("go to step" + (stepIndex + 1));

		}
		debug_log();

		goBoard.oneStepBackward();

	}

	public void _testOne_backward(int manualIndex) {
//		log.setLevel(Level.DEBUG);
		String fileName = SGFGoManual.getFileName(manualIndex);
		if (log.isDebugEnabled())
			log.debug(fileName);
		SimpleGoManual manual = SGFGoManual.loadSimpleGoManual(fileName);
		GoBoard goBoard = new GoBoard();
		GoBoard goBoard2 = new GoBoard();
		for (int i = 0; i < manual.getShouShu() - 1; i++) {
			if (log.isDebugEnabled())
				log.debug("message");
			try {
				goBoard.oneStepForward(manual.getStep(i));
				goBoard.oneStepForward(manual.getStep(i + 1));
				goBoard.oneStepBackward();
				goBoard2.oneStepForward(manual.getStep(i));
			} catch (RuntimeException e) {
				if (log.isDebugEnabled())
					log.debug("shoushu=" + (i + 1) + " error at step "
							+ manual.getStep(i));
				throw e;
			}
			// assert goBoard = goBoard2.
			boolean result = goBoard.getBoardColorState().equals(
					goBoard2.getBoardColorState());
			if (result == false) {
				BoardColorState.showDiff(goBoard2.getBoardColorState(),
						goBoard.getBoardColorState());
				if (log.isDebugEnabled())
					log.debug("shoushu=" + (i + 1) + " error at step "
							+ manual.getStep(i));
			}

			assertTrue("state should equal internally!", result);

			for (Point point : Point.getAllPoints(goBoard.boardSize)) {
				boolean equals = BasicBlock.equals(
						goBoard2.getBasicBlock(point),
						goBoard.getBasicBlock(point));
				if (equals == false) {
					if (log.isDebugEnabled())
						log.debug("shoushu=" + (i + 1) + " error at step "
								+ manual.getStep(i));
					if (log.isDebugEnabled())
						log.debug("correct result: one step forward!");
					if (log.isDebugEnabled())
						log.debug(goBoard2.getBasicBlock(point));
					System.out
							.println("suspectable result: two step forward, one step backward");
					if (log.isDebugEnabled())
						log.debug(goBoard.getBasicBlock(point));
				}
				assertTrue("all block should be equal internally shoushu = "
						+ (i + 1) + ", manual index=" + manualIndex, equals);
			}
		}
		goBoard.printNeighborStateStatistic();
	}

	public void _testOne_fullBackForth(int manualIndex) {
		String fileName = SGFGoManual.getFileName(manualIndex);
		if (log.isDebugEnabled())
			log.debug(fileName);
		SimpleGoManual manual = SGFGoManual.loadSimpleGoManual(fileName);
		GoBoard goBoard = new GoBoard();
		for (Step step : manual.getSteps()) {
			goBoard.oneStepForward(step);
		}
		for (Step step : manual.getSteps()) {
			goBoard.oneStepBackward();
		}

		BlankBlock blankBlock = goBoard.getBlankBlock(1, 1);

		assertEquals(361, blankBlock.getNumberOfPoint());
	}

	

	/**
	 * maxCount16; file index68
	 */
	public void testOne() {
		_testOne_backward(239);
		// _testOne_fullBackForth(1);
	}
}
