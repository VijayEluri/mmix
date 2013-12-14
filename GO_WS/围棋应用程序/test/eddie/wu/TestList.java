package eddie.wu;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import util.GBKToUTF8;

import junit.framework.TestCase;

public class TestList extends TestCase {
	private static final Logger log = Logger.getLogger(GBKToUTF8.class);

	public void testRemove() {
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		if (log.isDebugEnabled())
			log.debug("list size:" + list.size());
		list.remove(1);
		if (log.isDebugEnabled())
			log.debug("list size:" + list.size());
	}
}
