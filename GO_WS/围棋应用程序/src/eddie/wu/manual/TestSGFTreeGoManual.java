package eddie.wu.manual;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class TestSGFTreeGoManual extends TestCase {
	
	private static Logger log = Logger.getLogger(TestExternalSimpleSGF.class);
	public void testTreeGoManual() {

		String file = "doc/围棋程序数据/围棋发阳论/086_p239.all.sgf.complex";
		TreeGoManual goManual = SGFGoManual.loadTreeGoManual(file).get(0);
		if(log.isEnabledFor(Level.WARN)) log.warn(goManual.getInitState().toString());
		if(log.isEnabledFor(Level.WARN)) log.warn(goManual.getSGFBodyString());
		if(log.isEnabledFor(Level.WARN)) log.warn(goManual.getExpandedString());
		if(log.isEnabledFor(Level.WARN)) log.warn(goManual.getVariant());
		
	}
	
	public void testTreeGoManual_2() {

		String file = "doc/围棋程序数据/围棋发阳论/116_p330.all.sgf.complex";
		TreeGoManual goManual = SGFGoManual.loadTreeGoManual(file).get(0);
		if(log.isEnabledFor(Level.WARN)) log.warn(goManual.getInitState().toString());
		if(log.isEnabledFor(Level.WARN)) log.warn(goManual.getSGFBodyString());
		if(log.isEnabledFor(Level.WARN)) log.warn(goManual.getExpandedString());
		if(log.isEnabledFor(Level.WARN)) log.warn(goManual.getVariant());
		
	}

}
