package eddie.wu.domain;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.arrayblock.GoBoard256;
import eddie.wu.domain.analy.StateAnalysis;
import eddie.wu.manual.SimpleGoManual;
import eddie.wu.manual.SGFGoManual;

/**
 * check whether the basic data structure is correctly maintained. like the
 * enemy/neighbor Blocks <br/>
 * state analysis is similar to the GoBoard imple. regarding the high level data
 * structure generation
 * 
 * @author Eddie
 * 
 */
public class TestStateAnalyze extends TestCase {
	private static Logger log = Logger.getLogger(StateAnalysis.class);

	/**
	 * seems simple forward works.
	 */
	public void testAll_forward() {
		log.setLevel(Level.ERROR);
		Logger.getLogger(GoBoard.class).setLevel(Level.ERROR);
		for (int i = 1; i <= 263; i++) {
			String fileName = SGFGoManual.getFileName(i);
			if(log.isDebugEnabled()) log.debug(fileName);
			SimpleGoManual manual = SGFGoManual.loadSimpleGoManual(fileName);
			log.info(manual);
			_testOne_forward2(manual, i);
		}
	}

	public void testOne_forward() {
		int index = Constant.currentManualIndex;
		String fileName = SGFGoManual.getFileName(index);
		if(log.isDebugEnabled()) log.debug(fileName);
		SimpleGoManual manual = SGFGoManual.loadSimpleGoManual(fileName);
		log.info(manual);
		_testOne_forward2(manual, index);
	}

	/**
	 * 利用从从局面生成的相邻关系来检查每步即时生成的相邻关系.
	 * 
	 * @param manual
	 * @param manualIndex
	 */
	public void _testOne_forward2(SimpleGoManual manual, int manualIndex) {

		GoBoard go = new GoBoard();

		StateAnalysis sa;
		for (Step step : manual.getSteps()) {
			try {
				go.oneStepForward(step);

				BoardColorState boardState = go.getBoardColorState();

				sa = new StateAnalysis(go.getMatrixState());

				List<BasicBlock> allBlocks = go.getAllBlocks();
				List<BasicBlock> allBlocks2 = sa.getAllBlocks();
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
