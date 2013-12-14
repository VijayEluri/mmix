package eddie.wu.domain.sgf;

import java.util.Collections;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Step;
import eddie.wu.manual.SimpleGoManual;
import eddie.wu.manual.SGFGoManual;
/**
 *-Dlog4j.configuration=log4j_error.xml
 * @author Eddie
 *
 */
public class TestGoBoardBackwardPerf extends TestCase {
	private static Logger log = Logger.getLogger(TestGoBoardBackwardPerf.class);

	/**
	 * seems simple backward works.<br/>
	 * average time 5,582,098 ms
	 */
	public void testAll_backward() {
		log.setLevel(Level.WARN);

		int[] time = new int[263];
		for (int i = 1; i <= 263; i++) {
			time[i - 1] = _testOne_inAll(i);
			// if (log.isEnabledFor(Level.WARN)) {
			// log.warn("It takes " + time[i-1] + " nanoseconds "
			// + "for manual " + i);
			// }
		}
		int aver = Step.outputOverAllStatistic(time, 1, log);
		Assert.assertTrue(
				"average time should be less than 10 ms for one manual.",
				aver < 10000000);
	}

	public int _testOne_inAll(int i) {
		String fileName = SGFGoManual.getFileName(i);
		SimpleGoManual manual = SGFGoManual.loadSimpleGoManual(fileName);
		GoBoard go = new GoBoard();

		for (Step step : manual.getSteps()) {
			go.oneStepForward(step);
		}
		Collections.reverse(manual.getSteps());
		
		long startOverall;
		long endOverall;//

		startOverall = System.nanoTime();

		for (Step step : manual.getSteps()) {

			go.oneStepBackward();

		}
		endOverall = System.nanoTime();
		return (int) (endOverall - startOverall);

	}

	/**
	 * average time 214125 ms
	 */
	public void testOne() {
		log.setLevel(Level.INFO);
		_testOne(239);
	}

	/**
	 * average time 63288 ms
	 * @param i
	 */
	public void _testOne(int i) {
		String fileName = SGFGoManual.getFileName(i);
		if (log.isDebugEnabled())
			log.debug(fileName);
		SimpleGoManual manual = SGFGoManual.loadSimpleGoManual(fileName);
		GoBoard go = new GoBoard();

		long start, startOverall;
		long end, endOverall;//
		for (Step step : manual.getSteps()) {
			go.oneStepForward(step);
		}
		Collections.reverse(manual.getSteps());

		startOverall = System.nanoTime();

		for (Step step : manual.getSteps()) {
			start = System.nanoTime();
			go.oneStepBackward();
			end = System.nanoTime();
			step.setTime(end - start);
		}
		endOverall = System.nanoTime();
		if (log.isEnabledFor(Level.WARN)) {
			log.warn("It takes " + (endOverall - startOverall)
					+ " nanoseconds " + "for manual " + i + " with "
					+ manual.getShouShu() + " steps");

		}
		int aver = Step.outputStatistic(manual.getSteps(), 1, log);
		Assert.assertTrue(
				"average time should be less than 0.1 ms for one step.",
				aver < 100000);

	}
}
