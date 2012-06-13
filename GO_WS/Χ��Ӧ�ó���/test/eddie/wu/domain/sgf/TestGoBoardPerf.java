package eddie.wu.domain.sgf;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Step;
import eddie.wu.manual.GoManual;
import eddie.wu.manual.SGFGoManual;

public class TestGoBoardPerf extends TestCase {
	private static Logger log = Logger.getLogger(GoBoard.class);

	/**
	 * seems simple forward works.
	 */
	public void testAll_forward() {
		log.setLevel(Level.ERROR);
		// for (int i = 1; i <= 263; i++) {
		// _testOne(i);
		// }
	}

	public void testOne() {
		log.setLevel(Level.INFO);
		_testOne(239);
	}

	public void _testOne(int i){
		String fileName = SGFGoManual.getFileName(i);
		if(log.isDebugEnabled()) log.debug(fileName);
		GoManual manual = SGFGoManual.loadGoManual(fileName);
		GoBoard go = new GoBoard();
		long start;
		long end ;//

		for(Step step : manual.getSteps()){
			 start = System.nanoTime();
			 go.oneStepForward(step);
			 end =  System.nanoTime();
			 step.setTime(end - start);		
			 if(log.isDebugEnabled()) log.debug("It takes "+ (end-start)+" nanoseconds");
		}
		
		

	}
}
