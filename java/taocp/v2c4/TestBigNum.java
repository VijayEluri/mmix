package taocp.v2c4;

import junit.framework.TestCase;

public class TestBigNum extends TestCase {
	public void test1() {
		long temp = 1L << 63;
		BigNum a = new BigNum(temp);
		System.out.println(a.toHexString());
		BigNum b = new BigNum(temp);
		System.out.println(b.toHexString());
		a.add(b);
		System.out.println(a.toHexString());

		a.add(a);
		// long[] a = new long[5];
		System.out.println(a.toDecimalString());

	}

	public void test2() {
		long temp = 1L << 63;
		BigNum a = new BigNum(temp);
		System.out.println(a.toHexString());
		BigNum b = new BigNum(temp);
		System.out.println(b.toHexString());
		a.add(b);
		System.out.println(a.toHexString());
	}
	
	public void test3(){
		long a = Long.MAX_VALUE;
		String aa = Long.toString(a);
		System.out.println(aa);
		BigNum bigNum = BigNum.valueOf(aa);
		System.out.println(bigNum);
	}
}
