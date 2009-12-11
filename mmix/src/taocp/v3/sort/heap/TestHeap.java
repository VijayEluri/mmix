package taocp.v3.sort.heap;

import java.util.Arrays;
import java.util.Random;

import junit.framework.TestCase;
import taocp.v3.TAOCPSample;
import util.LogUtil;
import util.MathUtil;

public class TestHeap extends TestCase {
	public void testHeap() {
		Heap h = new Heap(12);
		int[] a = new int[] { 12, 20, 29, 35, 23, 40, 26, 51, 15, 17, 19, 22 };
		for (int i = 0; i < a.length; i++) {
			h.add(a[i]);
			System.out.println("adding " + a[i]);
			h.show();
		}

		for (int i = 0; i < a.length; i++) {
			a[i] = h.removeRoot();
			System.out.println("removing " + a[i]);
			h.show();
		}
		System.out.println(Arrays.toString(a));
	}

	public void testTAOCPSample() {
		int[] a = TAOCPSample.getSample_OneIndexed();
		int count=MathUtil.getSwapCount();
		Heap.heapSort(a);
		System.out.println(Arrays.toString(a));
		System.out.println("swap " + (MathUtil.getSwapCount()-count)+ " times!");
	}

	public void testPearlSample() {

		int[] a = new int[] { 0, 12, 20, 29, 35, 23, 40, 26, 51, 15, 17, 19, 22 };
		Heap.heapSort(a);
		System.out.println(Arrays.toString(a));
	}

	public void testHeapSortPerformance() {
		LogUtil.setDebug(false);
		long start, end;
		Random r = new Random();
		int a[] = new int[1024 * 1024];
		start = System.currentTimeMillis();
		for (int i = 0; i < a.length; i++) {
			a[i] = r.nextInt();
			if (a[i] < 0) {
				a[i] >>>= 1;
			}
		}
		end = System.currentTimeMillis();
		System.out.println("it takes " + (end - start)
				+ " mini-seconds to init");

		start = System.currentTimeMillis();
		Heap.heapSort(a);
		end = System.currentTimeMillis();
		System.out.println("it takes " + (end - start)
				+ " mini-seconds to heap sort");
	}

}
