package taocp.v2c4;

import java.util.Arrays;

public class SeqLinkedList {
	int[] array;
	// int head;

	public SeqLinkedList(int n) {
		// only store value from 1 to n.
		array = new int[n];
		// head = n; // empty by default.

	}

	public SeqLinkedList fill() {
		for (int i = 0; i < array.length; i++) {
			array[i] = i + 1;
		}
		// head = 0;
		return this;
	}

	public boolean contains(int temp) {
		if (temp <= 0 || temp > array.length)
			return false;

		int index = 0;
		while (index < array.length) {
			if (array[index] == temp)
				return true;

			index = array[index];
			if (index == 0)
				break;
		}
		return false;	
	}
	
	public SeqLinkedList add(int temp) {
		if (temp >= 1 && temp <= array.length) {
			int index = 0;
			while (index < array.length) {
				if (array[index] == temp) {
					if (temp == array.length) {
						array[index] = 0;
					} else {
						array[index] = array[temp];
					}
					break;
				}else if(array[index]> temp){
					
				}
				
				index = array[index];
				if (index == 0)
					break;
			}
		}
		return this;
	}

	public SeqLinkedList delete(int temp) {
		if (temp >= 1 && temp <= array.length) {
			int index = 0;
			while (index < array.length) {
				if (array[index] == temp) {
					if (temp == array.length) {
						array[index] = 0;
					} else {
						array[index] = array[temp];
					}
					break;
				}
				index = array[index];
				if (index == 0)
					break;
			}
		}
		return this;
	}

	public boolean isEmpty() {
		return array[0] == 0;
	}

	public void printAll() {
		StringBuilder sb = new StringBuilder();
		int index = 0;
		while (index < array.length && array[index] != 0) {
			sb.append(array[index]);
			sb.append(",");
			index = array[index];
		}
		System.out.println(sb.toString());
		System.out.println(Arrays.toString(array));
	}

	public static void main(String[] args) {
		SeqLinkedList list = new SeqLinkedList(8);
		System.out.println(list.isEmpty());
		list.printAll();
		list.fill();
		list.printAll();
		list.delete(6);
		list.printAll();
		list.delete(8);
		list.printAll();
		list.delete(1);
		list.printAll();
		list.delete(2);
		list.printAll();
		list.delete(3);
		list.printAll();
		list.delete(4);
		list.printAll();
		list.delete(5);
		list.printAll();
		System.out.println(list.contains(7));
		list.delete(7);
		list.printAll();
		System.out.println(list.isEmpty());
	}

}
