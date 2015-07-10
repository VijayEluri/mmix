package eddie.wu.manual;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Block;
import eddie.wu.domain.Step;

public class TestExternalSimpleSGF extends TestCase {
	private static Logger log = Logger.getLogger(TestExternalSimpleSGF.class);
	public void testendgamestate() {
		String file = "doc/围棋程序数据/网络棋谱/建桥杯11届决赛.sgf";
		SimpleGoManual goManual = SGFGoManual.loadSimpleGoManual(file);
		if(log.isEnabledFor(Level.WARN)) log.warn(goManual.getInitState().toString());
		for (Step step : goManual.getSteps()) {
			if(log.isEnabledFor(Level.WARN)) log.warn(step);
		}

	}
	
//	public void testendgamestate() {
//		String file = "doc/围棋程序数据/网络棋谱/建桥杯11届决赛.sgf";
//		SimpleGoManual goManual = SGFGoManual.loadSimpleGoManual(file);
//		if(log.isEnabledFor(Level.WARN)) log.warn(goManual.getInitSate().toString());
//		for (Step step : goManual.getSteps()) {
//			if(log.isEnabledFor(Level.WARN)) log.warn(step);
//		}
//
//	}
}
