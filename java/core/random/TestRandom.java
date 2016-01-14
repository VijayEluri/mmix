package core.random;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

import junit.framework.TestCase;

public class TestRandom extends TestCase {
	public void populate(int[] a, Random random) {
		for (int j = 0; j < a.length; j++) {
			int k = random.nextInt(j + 1);
			// a[j] will be assigned with j+1, actually swap between a[j] and
			// a[k]
			a[j] = a[k];
			a[k] = j + 1;
		}
		// 本质还是shuffle！
		System.out.println(Arrays.toString(a));
	}

	public void test() {
		int[] a = new int[100];
		populate(a, new Random());
		int count = 0;
		for (int i = 0; i < a.length; i++) {
			count += a[i];
		}
		TestCase.assertEquals(5050, count);
		// Collections.sort(Arrays.asList(a));
	}

	public void test2() {
		int[] t = new int[16];
		Random a = new Random();
		int count = 0;
		for (int i = 0; i < 16; i++) {
			t[i] = a.nextInt() % 16;
			if (t[i] < 0) {
				t[i] =-t[i];
			}
			System.out.println(t[i]);
			count += t[i];
		}
		TestCase.assertEquals(15 * 8, count);
	}
}
