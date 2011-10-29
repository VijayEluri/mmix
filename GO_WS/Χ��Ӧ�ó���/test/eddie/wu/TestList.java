package eddie.wu;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class TestList extends TestCase {
	public void testRemove(){
		List<Integer> list = new ArrayList<Integer>();
		list.add(1);
		list.add(2);
		System.out.println("list size:"+list.size());
		list.remove(1);
		System.out.println("list size:"+list.size());
	}
}
