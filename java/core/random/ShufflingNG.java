package core.random;

import junit.framework.TestCase;

public class ShufflingNG extends TestCase {
	long[] v;
	int k = 64;
	long m = 1L << 35; 
	//bug 1: int m = 1<<35
	//bug 2: long m = 1<<35. the grammar is not nice for novice used .
	//bug 3: System.out.println(2 ^ 35);
	public ShufflingNG() {
		v = new long[k];
		for (int i = 0; i < k; i++) {
			v[i] = nextX();
		}
	}

	public long next() {
		long y = nextY();
		long j = (k * y) / (m);
		System.out.println(y);
		
		System.out.println(y >>> 29);
		long temp = v[(int) j];
		long x = nextX();
		v[(int) j] = x;
		return temp;
	}

	long X = 5772156649L;

	public long nextX() {
		long temp = X;
		X = (3141592653L * X + 2718281829L) % m;
		return temp;
	}

	long Y = 1781072418L;

	public long nextY() {
		long temp = Y;
		Y = (2718281829L * Y + 3141592653L) % m;
		return temp;
	}

	public void test() {
		System.out.println(this.next());
	}
}
