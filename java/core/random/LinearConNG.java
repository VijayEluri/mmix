package core.random;

import java.util.Random;

import junit.framework.TestCase;

public class LinearConNG extends TestCase{
	int m = 1 << 15;
	int x;
	int a = 1, c = 3;

	public LinearConNG() {
		x = (int) System.currentTimeMillis();
	}

	public LinearConNG(int seed) {
		x = seed;
	}

	public int nextInt() {
		int temp = x;
		x = a * x + c;
		x %= m;
		return temp;
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
