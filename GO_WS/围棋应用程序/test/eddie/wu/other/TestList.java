package eddie.wu.other;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eddie.wu.util.GBKToUTF8;

public class TestList extends TestCase {
	private static final Logger log = Logger.getLogger(GBKToUTF8.class);
	
	public void test() {
		List<Object> aList = new ArrayList<Object>();
		aList.add(null);
		aList.add(null);
		if(log.isDebugEnabled()) log.debug(aList.size());
		if(log.isDebugEnabled()) log.debug(aList.isEmpty());
		assertEquals(2, aList.size());
	}
	public void test2() {
		Set<Object> aSet = new HashSet<Object>();
		aSet.add(null);
		aSet.add(null);
		if(log.isDebugEnabled()) log.debug(aSet.size());
		if(log.isDebugEnabled()) log.debug(aSet.isEmpty());
		assertEquals(1, aSet.size());
	}
}
