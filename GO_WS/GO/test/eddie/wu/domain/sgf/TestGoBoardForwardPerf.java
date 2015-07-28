package eddie.wu.domain.sgf;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Step;
import eddie.wu.manual.SimpleGoManual;
import eddie.wu.manual.SGFGoManual;
/**
 * -Dlog4j.configuration=log4j_error.xml
 * @author Eddie
 *
 */
public class TestGoBoardForwardPerf extends TestCase {
	private static Logger log = Logger.getLogger(TestGoBoardForwardPerf.class);

	/**
	 * seems simple forward works.<br/>
	 * average time 61166495 ms
	 */
	public void testAll_forward() {
		log.setLevel(Level.WARN);
		
		int[] time = new int[263];
		for (int i = 1; i <= 263; i++) {
			time[i - 1] = _testOne_inAll(i);
			// if (log.isEnabledFor(Level.WARN)) {
			// log.warn("It takes " + time[i-1] + " nanoseconds "
			// + "for manual " + i);
			// }
		}
		int aver = Step.outputOverAllStatistic(time, 10, log);
		assertTrue(
				"average time should be less than 100 ms for one manual.",
				aver < 100000000);
	}

	public int _testOne_inAll(int i) {
		String fileName = SGFGoManual.getFileName(i);
		SimpleGoManual manual = SGFGoManual.loadSimpleGoManual(fileName);
		GoBoard go = new GoBoard();
		long startOverall;
		long endOverall;//
		startOverall = System.nanoTime();

		for (Step step : manual.getSteps()) {
			go.oneStepForward(step);
		}
		endOverall = System.nanoTime();
		return (int) (endOverall - startOverall);

	}

	public void testOne() {
		log.setLevel(Level.INFO);
		_testOne(239);
	}

	public void _testOne(int i) {
		String fileName = SGFGoManual.getFileName(i);
		if (log.isDebugEnabled())
			log.debug(fileName);
		SimpleGoManual manual = SGFGoManual.loadSimpleGoManual(fileName);
		GoBoard go = new GoBoard();
		long start, startOverall;
		long end, endOverall;//
		startOverall = System.nanoTime();

		for (Step step : manual.getSteps()) {
			start = System.nanoTime();
			go.oneStepForward(step);
			end = System.nanoTime();
			step.setTime(end - start);
		}
		endOverall = System.nanoTime();
		if (log.isEnabledFor(Level.WARN)) {
			log.warn("It takes " + (endOverall - startOverall)/1000000
					+ " seconds " + "for manual " + i + " with "
					+ manual.getShouShu() + " steps");

		}
		int aver = Step.outputStatistic(manual.getSteps(), 1, log);
		assertTrue(
				"average time should be less than 1 ms for one step.",
				aver < 1000);

	}
}
