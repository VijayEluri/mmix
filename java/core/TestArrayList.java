package core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import junit.framework.TestCase;

public class TestArrayList extends TestCase {
	Random random = new Random();
	int n = 100000;

	public void test() {
		ArrayList<String> list = new ArrayList<String>(n);
		HashSet<String> set = new HashSet<String>(n);
		for (int i = 0; i < n; i++) {
			int nextInt = random.nextInt();
			list.add(String.valueOf(nextInt));
			set.add(String.valueOf(nextInt));
		}
		int count = 0;
		long start = System.nanoTime();
		for (String temp : list) {
			if (list.contains(temp)) {
				count++;
			}
		}
		long end = System.nanoTime();
		System.out.println("count = " + count + " it takes " + (end - start));
		
		count = 0;
		start = System.nanoTime();
		for (String temp : list) {
			if (set.contains(temp)) {
				count++;
			}
		}
		end = System.nanoTime();
		System.out.println("count = " + count + " it takes " + (end - start));
		
	}
}
