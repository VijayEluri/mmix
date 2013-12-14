package eddie.wu.domain.sgf;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.arrayblock.GoBoard256;
import eddie.wu.domain.BasicBlock;
import eddie.wu.domain.BasicGoBoard;
import eddie.wu.domain.Block;
import eddie.wu.domain.BoardBreathState;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.GoBoardBackward;
import eddie.wu.domain.GoBoardForward;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.manual.SimpleGoManual;
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
		// log.setLevel(Level.ERROR);
		// String key = "-Dlog4j.configuration=log4j_error.xml";
		// String value = "log4j_error.xml";
		// System.setProperty(key, value);

		//
		// Logger.getLogger(Block.class).setLevel(Level.ERROR);
		// Logger.getLogger(GoBoard.class).setLevel(Level.ERROR);
		// Logger.getLogger(BasicGoBoard.class).setLevel(Level.ERROR);
		// Logger.getLogger(Block.class).setLevel(Level.ERROR);
		// Logger.getLogger(GoBoard256.class).setLevel(Level.ERROR);
		// Logger.getLogger(GoBoard256.class).setLevel(Level.ERROR);
		// Logger.getLogger(GoBoardForward.class).setLevel(Level.ERROR);
		// Logger.getLogger(GoBoardBackward.class).setLevel(Level.ERROR);
		// Logger.getLogger(BoardColorState.class).setLevel(Level.ERROR);
		//
		int maxCount = 0;
		int maxCountFileIndex = 0;
		for (int i = 1; i <= 263; i++) {
			// if (i == 128)
			// continue;// array impl. cannot handle so many blocks
			// if (i == 154)
			// continue;
			// if (i == 239)
			// continue;
			String fileName = SGFGoManual.getFileName(i);
			if (log.isDebugEnabled())
				log.debug(fileName);
			SimpleGoManual manual = SGFGoManual.loadSimpleGoManual(fileName);
			log.info(manual);
			int[] result = _testOne_forward1(manual, i);
			int count = 0;

			for (int index = 0; index < result.length; index++) {
				if (result[index] != 0)
					count++;
			}
			if (count > maxCount) {
				maxCount = count;
				maxCountFileIndex = i;
			}
			if(log.isEnabledFor(Level.WARN)) log.warn(i + " " + Arrays.toString(result));
		}
		if(log.isEnabledFor(Level.WARN)) log.warn("maxCount" + maxCount + ";  file index"
				+ maxCountFileIndex);
	}

	public void testOne_forward() {
		log.setLevel(Level.WARN);
		Logger.getLogger(BoardColorState.class).setLevel(Level.DEBUG);
		int index = Constant.currentManualIndex;
		String fileName = SGFGoManual.getFileName(index);
		if (log.isDebugEnabled())
			log.debug(fileName);
		SimpleGoManual manual = SGFGoManual.loadSimpleGoManual(fileName);
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
	public int[] _testOne_forward1(SimpleGoManual manual, int manualIndex) {

		GoBoard go = new GoBoard();
		GoBoard256 ago = new GoBoard256(Constant.BOARD_SIZE);

		for (Step step : manual.getSteps()) {
			try {
				go.oneStepForward(step);
				if (step.getIndex() == 237) {
					if (log.isDebugEnabled())
						log.debug("237");
				}
				ago.oneStepForward(step);

				BoardColorState boardState = go.getBoardColorState();
				boolean result = boardState.equals(ago.getBoardState());
				if (result == false) {
					BoardColorState.showDiff(ago.getBoardState(),
							go.getBoardColorState());
					if (log.isDebugEnabled())
						log.debug("error at step " + step + "manual index="
								+ manualIndex);
					assertTrue(result);
				}

				result = Arrays.deepEquals(go.getMatrixState(),
						ago.getMatrixState());
				if (result == false)
					if (log.isDebugEnabled())
						log.debug("error at step " + step + "manual index="
								+ manualIndex);
				assertTrue(result);

				BoardBreathState breathState = go.getBoardBreathState();
				BoardBreathState breathState2 = ago.getBoardBreathState();
				result = breathState.equals(breathState2);
				if (result == false) {
					BoardBreathState.showDiff(breathState, breathState2);
					if (log.isDebugEnabled())
						log.debug("error at step " + step + "manual index="
								+ manualIndex);

				}
				assertTrue(result);

				result = Arrays.deepEquals(go.getMatrixState(),
						ago.getMatrixState());
				if (result == false)
					if (log.isDebugEnabled())
						log.debug("error at step " + step + "manual index="
								+ manualIndex);
				assertTrue(result);

				go.output();
			} catch (Exception e) {
				if (log.isDebugEnabled())
					log.debug("error at step " + step + "manual index="
							+ manualIndex);
				throw new RuntimeException(e);
			}
		}
		go.printNeighborStateStatistic();
		return go.getCaseCount();
	}

	/**
	 * 利用从从局面生成的相邻关系来检查每步即时生成的相邻关系.
	 * 
	 * @param manual
	 * @param manualIndex
	 */
	public void _testOne_forward2(SimpleGoManual manual, int manualIndex) {

		GoBoard go = new GoBoard();

		GoBoard go2;
		for (Step step : manual.getSteps()) {
			try {
				go.oneStepForward(step);

				BoardColorState boardState = go.getBoardColorState();

				go2 = new GoBoard(boardState);

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
						if (log.isDebugEnabled())
							log.debug("Expected result");
						if (log.isDebugEnabled())
							log.debug(allBlocks.get(i));
						if (log.isDebugEnabled())
							log.debug("Actual result: ");
						if (log.isDebugEnabled())
							log.debug(allBlocks2.get(i));
					}
					assertTrue(
							"all block should be equal internally shoushu = "
									+ go.getLastStep(), equals);
				}
			} catch (RuntimeException e) {
				if (log.isDebugEnabled())
					log.debug("error at step " + step + "manual index="
							+ manualIndex);
				throw e;
			}
		}

	}
}
