package api.collection;

import java.util.HashMap;
import java.util.Hashtable;

import junit.framework.TestCase;

public class TestMap extends TestCase {
	public void test() {
		HashMap<String, String> h = new HashMap<String, String>();
		h.put(null, "t");
		h.put(null, "o");
		System.out.println(h.get(null));
		assertEquals("o", h.get(null));

		Hashtable<String, String> t = new Hashtable<String, String>();
		String value = "u";

		h.put(null, value);
		h.put(null, "i");
		System.out.println(h.get(null));
		h.remove(null);
		System.out.println(h.get(null));
		// fail();

	}
}
