package taocp.v3.sort.select;

import java.util.Arrays;

import junit.framework.TestCase;
import taocp.v3.TAOCPSample;
import util.MathUtil;

public class TestStraightSelection extends TestCase {
	public void test() {
		int[] a = TAOCPSample.getSample_ZeroIndexed();
		int count = MathUtil.getSwapCount();
		StraightSelectionSort.sort_max(a);
		System.out.println(Arrays.toString(a));
		System.out.println("swap total " + (MathUtil.getSwapCount() - count)
				+ " times!");
	}
}
