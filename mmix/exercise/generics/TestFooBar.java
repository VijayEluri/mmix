package generics;



import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

public class TestFooBar extends TestCase {
	public void test() {
		Foo foo = new Foo();
		Bar bar = new Bar();
		List<String> list = new ArrayList<String>();
		List<Integer> name = new ArrayList<Integer>();
		bar.bar(name);
		bar.bar(list);
	}
}
