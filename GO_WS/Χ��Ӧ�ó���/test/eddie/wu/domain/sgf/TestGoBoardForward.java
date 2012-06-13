package eddie.wu.domain.sgf;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.arrayblock.GoBoard256;
import eddie.wu.domain.BasicBlock;
import eddie.wu.domain.BoardBreathState;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.manual.GoManual;
import eddie.wu.manual.SGFGoManual;

/**
 * Test with 吴清源围棋263局(SGF格式) <br/>
 * 第128局，Array实现处理不了。 (已经改好了)<br/>
 * 第154局，让二子棋，都处理不了。(已经改好了)<br/>
 * 第154局，让六子棋，都处理不了。(已经改好了)<br/>
 * 
 * @author Eddie
 * 
 */
public class TestGoBoardForward extends TestCase {
	private static Logger log = Logger.getLogger(GoBoard.class);

	/**
	 * seems simple forward works.
	 */
	public void testAll_forward() {
		log.setLevel(Level.ERROR);
		for (int i = 239; i <= 263; i++) {
			// if (i == 128)
			// continue;// array impl. cannot handle so many blocks
			// if (i == 154)
			// continue;
			// if (i == 239)
			// continue;
			String fileName = SGFGoManual.getFileName(i);
			if(log.isDebugEnabled()) log.debug(fileName);
			GoManual manual = SGFGoManual.loadGoManual(fileName);
			log.info(manual);
			_testOne_forward1(manual, i);
		}
	}

	public void testOne_forward() {
		int index = Constant.currentManualIndex;
		String fileName = SGFGoManual.getFileName(index);
		if(log.isDebugEnabled()) log.debug(fileName);
		GoManual manual = SGFGoManual.loadGoManual(fileName);
		log.info(manual);

		_testOne_forward1(manual, index);
		_testOne_forward2(manual, index);
	}

	/**
	 * 利用原有的基于数组的实现来检查新的实现
	 * 
	 * @param manual
	 * @param manualIndex
	 */
	public void _testOne_forward1(GoManual manual, int manualIndex) {

		GoBoard go = new GoBoard();
		GoBoard256 ago = new GoBoard256(Constant.BOARD_SIZE);

		for (Step step : manual.getSteps()) {
			try {
				go.oneStepForward(step);
				if (step.getIndex() == 237) {
					if(log.isDebugEnabled()) log.debug("237");
				}
				ago.oneStepForward(step);

				BoardColorState boardState = go.getBoardColorState();
				boolean result = boardState.equals(ago.getBoardState());
				if (result == false) {
					BoardColorState.showDiff(ago.getBoardState(),
							go.getBoardColorState());
					if(log.isDebugEnabled()) log.debug("error at step " + step
							+ "manual index=" + manualIndex);

				}
				assertTrue(result);

				result = Arrays.deepEquals(go.getMatrixState(),
						ago.getMatrixState());
				if (result == false)
					if(log.isDebugEnabled()) log.debug("error at step " + step
							+ "manual index=" + manualIndex);
				assertTrue(result);

				BoardBreathState breathState = go.getBoardBreathState();
				BoardBreathState breathState2 = ago.getBoardBreathState();
				result = breathState.equals(breathState2);
				if (result == false) {
					BoardBreathState.showDiff(breathState, breathState2);
					if(log.isDebugEnabled()) log.debug("error at step " + step
							+ "manual index=" + manualIndex);

				}
				assertTrue(result);

				result = Arrays.deepEquals(go.getMatrixState(),
						ago.getMatrixState());
				if (result == false)
					if(log.isDebugEnabled()) log.debug("error at step " + step
							+ "manual index=" + manualIndex);
				assertTrue(result);

				go.output();
			} catch (RuntimeException e) {
				if(log.isDebugEnabled()) log.debug("error at step " + step + "manual index="
						+ manualIndex);
				throw e;
			}
		}

	}

	/**
	 * 利用从从局面生成的相邻关系来检查每步即时生成的相邻关系.
	 * 
	 * @param manual
	 * @param manualIndex
	 */
	public void _testOne_forward2(GoManual manual, int manualIndex) {

		GoBoard go = new GoBoard();

		GoBoard go2;
		for (Step step : manual.getSteps()) {
			try {
				go.oneStepForward(step);

				BoardColorState boardState = go.getBoardColorState();

				go2 = new GoBoard(boardState, step.getIndex());

				// it's very slow
				// for (Point point : Point.allPoints) {
				// boolean equals = BasicBlock.equals(
				// go2.getBasicBlock(point), go.getBasicBlock(point));
				// if (equals == false) {
				// if(log.isDebugEnabled()) log.debug("Expected result");
				// if(log.isDebugEnabled()) log.debug(go2.getBasicBlock(point));
				// if(log.isDebugEnabled()) log.debug("Actual result: ");
				// if(log.isDebugEnabled()) log.debug(go.getBasicBlock(point));
				// }
				// assertTrue(
				// "all block should be equal internally shoushu = "
				// + go.getLastStep(), equals);
				// }

				List<BasicBlock> allBlocks = go.getAllBlocks();
				List<BasicBlock> allBlocks2 = go2.getAllBlocks();
				for (int i = 0; i < allBlocks.size(); i++) {
					boolean equals = BasicBlock.equals(allBlocks.get(i),
							allBlocks2.get(i));
					if (equals == false) {
						if(log.isDebugEnabled()) log.debug("Expected result");
						if(log.isDebugEnabled()) log.debug(allBlocks.get(i));
						if(log.isDebugEnabled()) log.debug("Actual result: ");
						if(log.isDebugEnabled()) log.debug(allBlocks2.get(i));
					}
					assertTrue(
							"all block should be equal internally shoushu = "
									+ go.getLastStep(), equals);
				}
			} catch (RuntimeException e) {
				if(log.isDebugEnabled()) log.debug("error at step " + step + "manual index="
						+ manualIndex);
				throw e;
			}
		}

	}
}
