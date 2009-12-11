package taocp.v3.sort.heap;

import util.LogUtil;
import util.MathUtil;

/**
 * consider minimum heap, root is the minimun one.
 * Heap is also a kind of balance tree, and it is much easier to maintain
 * the balance than other balance tree is.
 * @author wueddie-wym-wrz
 * 
 */
public class Heap {
	int size;// the number of elements the heap can hold at most.
	
	int count = 0;// number of elements in the heap.

	int[] x;// x[0] is not used.

	public Heap(int size) {
		this.size = size;
		x = new int[size + 1];
	}

	public boolean isEmpty() {
		return count == 0;
	}

	public boolean isFull() {
		return count == size ;
	}

	/**
	 * add elements at the end of heap.
	 * 
	 * @param value
	 */
	public void add(int value) {
		if (isFull()) {
			throw new java.lang.IllegalStateException("heap is full");
		}
		x[++count] = value;
		if (count > 1) {
			siftup(count);
		}
	}

	public int getRoot() {
		if (isEmpty()) {
			throw new java.lang.IllegalStateException("heap is empty");
		} else {
			return x[1];
		}
	}

	public int removeRoot() {
		if (isEmpty()) {
			throw new java.lang.IllegalStateException("heap is empty");
		} else {
			int res = x[1];
			x[1] = x[count--];
			if (count > 1) {
				siftDown(x[1]);
			}
			return res;
		}
	}

	/**
	 * change the value of the root of the heap.
	 * 
	 * @param value
	 */
	public void updateRoot(int value) {
		if (count < 1) {
			throw new java.lang.IllegalStateException("heap is empty");
		}
		x[1] = value;
		if (count == 1) {
			return;
		} else {
			siftDown(value);
		}

	}

	/**
	 * when add element at x[n], we reorganize the heap.
	 * 
	 * @param n
	 */
	private void siftup(int n) {
		do {
			if (x[n] < x[n / 2]) {
				MathUtil.swap(x, n, n / 2);
				n = n / 2;
			} else {
				break;
			}

		} while (n > 1);
	}

	/**
	 * when the value of x[1] is changed. reorganize the heap.
	 * 
	 * @param n
	 */
	private void siftDown(int value) {
		int index = 1;
		while (true) {
			if (2 * index > count) {// no children
				break;
			} else if (2 * index + 1 > count) {// left child only
				if (x[index] > x[index * 2]) {
					MathUtil.swap(x, index, index * 2);
					break;
				} else {
					break;
				}
			} else {// two children.
				if (x[index * 2] > x[index * 2 + 1]) {
					if (x[index] > x[index * 2 + 1]) {
						MathUtil.swap(x, index, index * 2 + 1);
						index = index * 2 + 1;
					} else {
						break;
					}
				} else {
					if (x[index] > x[index * 2]) {
						MathUtil.swap(x, index, index * 2);
						index = index * 2;
					} else {
						break;
					}
				}
			}
		}
	}

	/**
	 * heap sort directly on internal data structure. pa[0] is not used.
	 */
	public static void heapSort(int[] pa) {
		Heap h = new Heap(pa.length - 1);
		h.x = pa;	
		h.count = pa.length - 1;
		//get the minimun heap.
		int count=MathUtil.getSwapCount();
		for (int i = 2; i <= h.count; i++) {
			h.siftup(i);
			h.show();
		}
		System.out.println("swap " + (MathUtil.getSwapCount()-count)+ " times get the minimun heap!");

		for (int i = h.count; i >= 2; i--) {
			MathUtil.swap(h.x, 1, i);
			h.count--;
			h.siftDown(h.x[1]);			
		}
	}

	public void show() {
		if(LogUtil.isDebug())
			System.out.println(java.util.Arrays.toString(x));
	}
}
