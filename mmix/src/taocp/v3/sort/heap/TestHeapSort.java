package taocp.v3.sort.heap;

import java.util.Arrays;

import junit.framework.TestCase;
import taocp.v3.TAOCPSample;
import taocp.v3.sort.insert.InsertSort;

public class TestHeapSort extends TestCase {
	public void test() {
		// Heap h = new Heap(12);
		int[] a = new int[] { 12, 20, 29, 35, 23, 40, 26, 51, 15, 17, 19, 22 };

		HeapSort.sort(a);
		System.out.println(Arrays.toString(a));
		int[] expect = a.clone();
		System.out.print(expect);
		System.out.print(a);

		new InsertSort().sort(expect);
		assertTrue(Arrays.equals(expect, a));

		a = TAOCPSample.getSample_ZeroIndexed();
		HeapSort.sort(a);
		System.out.println(Arrays.toString(a));
		expect = a.clone();
		System.out.print(expect);
		System.out.print(a);

		new InsertSort().sort(expect);
		assertTrue(Arrays.equals(expect, a));
	}
}
