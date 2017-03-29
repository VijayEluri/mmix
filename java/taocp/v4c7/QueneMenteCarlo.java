package taocp.v4c7;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class QueneMenteCarlo {
	public static void main(String[] args) {
		BigInteger[] monteCarlo = new BigInteger[10];
		// result 4->2, 8->92, 16->14772512, 24

		monteCarlo[0] = new QueneMenteCarlo(4).esttimateMonteCarlo();
		monteCarlo[1] = new QueneMenteCarlo(8).esttimateMonteCarlo();
		monteCarlo[2] = new QueneMenteCarlo(12).esttimateMonteCarlo();
		monteCarlo[3] = new QueneMenteCarlo(16).esttimateMonteCarlo();
		monteCarlo[4] = new QueneMenteCarlo(20).esttimateMonteCarlo();
		monteCarlo[5] = new QueneMenteCarlo(24).esttimateMonteCarlo();
		monteCarlo[6] = new QueneMenteCarlo(28).esttimateMonteCarlo();
		monteCarlo[7] = new QueneMenteCarlo(32).esttimateMonteCarlo();
		monteCarlo[8] = new QueneMenteCarlo(36).esttimateMonteCarlo();
		System.out.println("MonteCarlo = " + Arrays.toString(monteCarlo));

		for (int i = 1; i < 9; i++) {
			System.out.println("N=" + (4 * (i + 1))+", varirants = " + monteCarlo[i]
					+ ", times of previous:"
					+ monteCarlo[i].divide(monteCarlo[i - 1]));
		}
	}

	private int N = 4;
	private int a[], b[];

	public QueneMenteCarlo(int n) {
		N = n;
		a = new int[N]; // column choice in each row!
		b = new int[N]; // column to test in each level.
	}

	public BigInteger esttimateMonteCarlo() {
		Random random = new Random(1);
		System.out.println("N = " + N);
		BigInteger monteCarlo = BigInteger.valueOf(1);
		int count = 0;// choices in each level
		int level = 0;
		List<Integer> choice = new ArrayList<Integer>();
		while (level < N) {
			count = 0;
			choice.clear();
			while (b[level] < N) {
				// whether it is valid after new selection in current level.
				boolean valid = true;
				a[level] = b[level]++;
				for (int i = 0; i < level; i++) {
					if (a[i] == a[level]) {
						// same column is chosen for two different rows.
						valid = false;
					} else if (a[i] - a[level] == i - level
							|| a[i] - a[level] == level - i) {
						// two choices are diagonal!
						valid = false;
					}
				}
				if (valid == true) {
					count++;
					choice.add(a[level]);
				}

			}
			System.out.println("level = " + level + ", count =" + count);
			System.out.println(choice);
			if (count == 0) {
				return monteCarlo;
			}
			monteCarlo = monteCarlo.multiply(BigInteger.valueOf(count));
			int choose = choice.get(random.nextInt(count));
			a[level++] = choose;
			System.out.println("Choose " + choose);
		}

		return monteCarlo;
	}
}
