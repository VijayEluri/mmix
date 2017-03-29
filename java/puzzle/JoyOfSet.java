package puzzle;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

public class JoyOfSet {
	public static void main(String[] arags) {
		Test1();
		Test2();
	}

	private static void Test1() {
		Set<Short> set = new HashSet<Short>();
		for (short i = 0; i < 100; i++) {
			set.add(i);
			set.remove(i - 1);
		}
		TestCase.assertEquals(100, set.size());
		System.out.println(set.size());
	}

	private static void Test2() {
		Set<Integer> set = new HashSet<Integer>();
		for (int i = 0; i < 100; i++) {
			set.add(i);
			set.remove(i - 1);
		}
		TestCase.assertEquals(1, set.size());
		System.out.println(set.size());
	}
}
