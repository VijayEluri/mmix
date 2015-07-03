package eddie.wu.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.GoBoard;

import junit.framework.TestCase;

public class TestLog4j extends TestCase {
	private static Logger log = Logger.getLogger(GoBoard.class);

	public void test() {
		log.setLevel(Level.OFF);
		if (log.isTraceEnabled()) {
			if(log.isEnabledFor(Level.WARN));
			if(log.isEnabledFor(Level.WARN)) log.warn("warn in trace off:");
			log.error("error in trace off:");
		}
		log.setLevel(Level.ERROR);
		if (log.isTraceEnabled()) {
			if(log.isEnabledFor(Level.WARN)) log.warn("warn in trace ERROR:");
			log.error("error in trace ERROR:");
		}
		log.setLevel(Level.WARN);
		if (log.isTraceEnabled()) {
			if(log.isEnabledFor(Level.WARN)) log.warn("warn in trace WARN:");
			log.error("error in trace WARN:");
		}
	}
}
