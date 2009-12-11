package api.collection;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

import junit.framework.TestCase;

public class TestSet extends TestCase {
	public void testHashSet() {
		HashSet<String> h = new HashSet<String>();
		h.add(null);
		h.add(null);
		System.out.println(h.size());
		assertEquals(1, h.size());

	}

	public void testTreeSet() {

		TreeSet<String> t = new TreeSet<String>();
		try {
			t.add(null);
		} catch (RuntimeException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			fail();
		}
		try {
			t.add(null);
			fail();
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println(e);

		}

	}

	public void testList() {
		ArrayList<String> h = new ArrayList<String>();
		h.add(null);
		h.add(null);
		System.out.println(h.size());
		assertEquals(2, h.size());
		System.out.println(h.isEmpty());
		assertEquals(2, h.size());
	}

}
