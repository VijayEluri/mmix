package eddie.wu.math;

import java.util.Random;

public class RandomA {

	public void testA() {
		Random ramdom = new Random(100);
		int count = 0;
		for (int i = 0; i < 100; i++) {
			int j = ramdom.nextInt();
			System.out.println(j);
			count += j;
		}
		System.out.println("count = " + count);
	}

	public static void main(String[] args) {
		new RandomA().testA();
	}
}
