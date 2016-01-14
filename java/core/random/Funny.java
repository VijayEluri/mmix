package core.random;

import java.util.Random;

import junit.framework.TestCase;

public class Funny extends TestCase {
	/**
	 * can always reach 1 as the terminator!
	 * @param n
	 */
	public void cal(int n) {
		long count = 0;
		while (n != 1) {
			if (n % 2 == 0) {
				n >>>= 1;
				System.out.println(n);
			} else {
				n = 3 * n + 1;
				System.out.println(n);
			}
			count++;
		}
		System.out.println("steps = " + count);
	}

	public void test() {
		int n = 45;
		System.out.println("Original inpu = " + n);
		cal(n);
	}

	public void test_random() {
		int n = new Random().nextInt();
		System.out.println("Original input = " + n);
		cal(n);
	}
}
