package taocp.v4.f2.gray;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

/**
 * <p>
 * TestGrayCode.java
 * </p>
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class TestGrayCode extends TestCase {

	public void testGrayCode4() {
		for (long i : list) {
			System.out.println(Long.toBinaryString(i));
		}
		assertEquals(list, GrayCode.generate(4));
		assertEquals(list, GrayCode.generate(GrayCode.ALGO_G, 4));
	}

	public void testGrayCode() {
		for (int i = 4; i < 20; i++) {			
			assertEquals(GrayCode.generate(i), GrayCode.generate(GrayCode.ALGO_G, i));
		}
	}

	public void testGrayCode5() {
		List<Long> list = GrayCode.generate(5);
		long current = 0;
		boolean first = true;
		long firstvalue = 0;
		for (long i : list) {

			if (first) {
				first = false;
				current = i;
				firstvalue = i;
			} else {
				System.out.println(Long.toBinaryString(i) + "; "
						+ Long.toBinaryString(current));
				assertEquals(1, Long.bitCount(current ^ i));
				current = i;
			}

		}
		System.out.println(Long.toBinaryString(firstvalue) + "; "
				+ Long.toBinaryString(current));
		assertEquals(1, Long.bitCount(current ^ firstvalue));
	}

	List<Long> list = new ArrayList<Long>(16);

	
	protected void setUp() throws Exception {
		super.setUp();
		list.add(Long.parseLong("0000", 2));
		list.add(Long.parseLong("0001", 2));

		list.add(Long.parseLong("0011", 2));
		list.add(Long.parseLong("0010", 2));

		list.add(Long.parseLong("0110", 2));
		list.add(Long.parseLong("0111", 2));
		list.add(Long.parseLong("0101", 2));
		list.add(Long.parseLong("0100", 2));

		list.add(Long.parseLong("1100", 2));
		list.add(Long.parseLong("1101", 2));
		list.add(Long.parseLong("1111", 2));
		list.add(Long.parseLong("1110", 2));
		list.add(Long.parseLong("1010", 2));
		list.add(Long.parseLong("1011", 2));
		list.add(Long.parseLong("1001", 2));
		list.add(Long.parseLong("1000", 2));

	}
}
