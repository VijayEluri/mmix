/*
 * Created on 2005-6-14
 *


 */
package eddie.wu.other;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
/**
 * @author eddie
 *
 * TODO To change the template for this generated type comment go to

 */
public class TestLog4j extends TestCase{
	private static final Logger log = Logger.getLogger(TestLog4j.class);
	public void testLog4j(){
		log.debug("debug:testLog4j");
		log.error("error:testLog4j");
		log.info("info::testLog4j");
	}
	
	public void testLog(){
		log.debug("log:"+log.getClass().getName());
		log.debug("log:"+log.toString());
		log.error("log:"+log.getClass().getName());
		log.error("log:"+log.toString());
		Logger log1=Logger.getLogger(eddie.wu.other.TestLog4j.class);
		assertEquals(log,log1);
		assertTrue("log==log1",log==log1);
		
	}
	
	/**
	 * conclusion: in order to make full use of log4j,
	 * especially dynamic configuration (programmatically control).
	 * manully change the log4j.xml has a limitation of non-persistent.
	 * so we can dynamic control the behavior of log4j in test case. 
	 *
	 */
	public void testDynamicConfig(){
		 
		   Logger  logger = Logger.getLogger(TestLog4j.class);
		   if(log.isDebugEnabled()) log.debug("level="+logger.getLevel());
		   // Now set its level. Normally you do not need to set the
		   // level of a logger programmatically. This is usually done
		   // in configuration files.
		   logger.setLevel(Level.DEBUG);  
		   // This request is enabled, because WARN >= INFO.
		   logger.warn("warn:Low fuel level.");
		   // This request is disabled, because DEBUG < INFO.
		   logger.debug("DEBUG:Starting search for nearest gas station.");
		   logger.info("INfo:Starting search for nearest gas station.");
		   
		   logger.setLevel(Level.INFO);  
		   // This request is enabled, because WARN >= INFO.
		   logger.warn("WARN:Low fuel level.");
		   // This request is disabled, because DEBUG < INFO.
		   logger.debug("Debug:Starting search for nearest gas station.");
		   logger.info("INfo:Starting search for nearest gas station.");

		   
	}
	
}
