package eddie.wu.other;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TestList extends TestCase {
	public void test() {
		List<Object> aList = new ArrayList<Object>();
		aList.add(null);
		aList.add(null);
		System.out.println(aList.size());
		System.out.println(aList.isEmpty());
		Assert.assertEquals(2, aList.size());
	}
	public void test2() {
		Set<Object> aSet = new HashSet<Object>();
		aSet.add(null);
		aSet.add(null);
		System.out.println(aSet.size());
		System.out.println(aSet.isEmpty());
		Assert.assertEquals(1, aSet.size());
	}
}
