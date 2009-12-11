package fun.inter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * It is ironic I did not resolve it in today's hp interview. brute force is so
 * straight forward, i can finish it in 30 minutes at home.
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class Queen {
	public static void main(String[] args) {
		new Queen().queen();

	}

	private void queen() {

		List<int[]> a = new ArrayList();
		// i and sol[i] is one choice.
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (j == i)
					continue;
				if (j == 1 + i || j == 1 + i)
					continue;
				for (int k = 0; k < 8; k++) {
					if (k == i || k == j)
						continue;
					if (k == 1 + j || k == 2 + i || k == -1 + j || k == -2 + i)
						continue;
					for (int l = 0; l < 8; l++) {
						if (l == i || l == j || l == k)
							continue;
						if (l == 1 + k || l == 2 + j || l == 3 + i
								|| l == -1 + k || l == -2 + j || l == -3 + i)
							continue;

						for (int m = 0; m < 8; m++) {
							if (m == i || m == j || m == k || m == l)
								continue;
							if (m == 1 + l || m == 2 + k || m == 3 + j
									|| m == 4 + i || m == -1 + l || m == -2 + k
									|| m == -3 + j || m == -4 + i)
								continue;
							for (int n = 0; n < 8; n++) {
								if (n == i || n == j || n == k || n == l
										|| n == m)
									continue;
								if (n == 1 + m || n == 2 + l || n == 3 + k
										|| n == 4 + j || n == 5 + i
										|| n == -1 + m || n == -2 + l
										|| n == -3 + k || n == -4 + j
										|| n == -5 + i)
									continue;
								for (int o = 0; o < 8; o++) {
									if (o == i || o == j || o == k || o == l
											|| o == m || o == n)
										continue;
									if (o == 1 + n || o == 2 + m || o == 3 + l
											|| o == 4 + k || o == 5 + j
											|| o == 6 + i || o == -1 + n
											|| o == -2 + m || o == -3 + l
											|| o == -4 + k || o == -5 + j
											|| o == -6 + i)
										continue;
									for (int p = 0; p < 8; p++) {
										if (p == i || p == j || p == k
												|| p == l || p == m || p == n
												|| p == o)
											continue;

										if (p == 1 + o || p == 2 + n
												|| p == 3 + m || p == 4 + l
												|| p == 5 + k || p == 6 + j
												|| p == 7 + i || p == -1 + o
												|| p == -2 + n || p == -3 + m
												|| p == -4 + l || p == -5 + k
												|| p == -6 + j || p == -7 + i)
											continue;
										int[] sol = new int[8];
										sol[0] = i;
										sol[1] = j;
										sol[2] = k;
										sol[3] = l;
										sol[4] = m;
										sol[5] = n;
										sol[6] = o;
										sol[7] = p;
										System.out
												.println(Arrays.toString(sol));
										a.add(sol);
									}
								}
							}
						}
					}
				}
			}
		}
		System.out.println("there are " + a.size() + " solutions.");
	}
}
