package taocp.v4.f2.gray;

import junit.framework.TestCase;

public class TestAlgoG extends TestGrayCode{
	public void testGrayCode4() {
		for (long i : list) {
			System.out.println(Long.toBinaryString(i));
		}

		assertEquals(list, AlgoG.generate(4));

	}
}
