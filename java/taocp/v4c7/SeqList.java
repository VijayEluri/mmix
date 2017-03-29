package taocp.v4c7;

import java.util.Random;

public class SeqList {
	private int[] mem;
	private int head, tail;

	public SeqList(int n) {
		mem = new int[n];
	}

	public boolean isEmpty() {
		return head == tail;
	}

	public int size() {
		return tail - head;
	}

	/**
	 * O(n) is not the best!
	 * 
	 * @param element
	 * @return
	 */
	public boolean exist(int element) {
		for (int i = 0; i < tail; i++) {
			if (mem[i] == element)
				return true;
		}
		return false;
	}

	/**
	 * caller to ensure element doesn't exist!
	 * 
	 * @param element
	 */
	public void insert(int element) {
		mem[tail++] = element;
	}

	public boolean delete(int index) {
		if (index >= tail || index < 0) {
			return false;
		}
		tail--;
		if (tail != index) {
			// move the last one to delete slot.
			mem[index] = mem[tail];
		}
		return true;
	}

	public static void main(String[] args) {
		int n = 8;
		SeqList list = new SeqList(n);
		Random random = new Random(1);
		for (int i = 0; i < n; i++) {
			list.insert(random.nextInt(n));
		}
	}
}
