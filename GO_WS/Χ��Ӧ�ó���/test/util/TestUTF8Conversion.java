package util;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

public class TestUTF8Conversion extends TestCase {
	private static final Logger log = Logger.getLogger(GBKToUTF8.class);
	
	public void testOneFile() {
		
		
		String filePath = "src\\untitled8\\GoApplet";
		if(log.isDebugEnabled()) log.debug("filePath is [" +filePath + "]");
		GBKToUTF8.convertOneFile(filePath);
	}
}
