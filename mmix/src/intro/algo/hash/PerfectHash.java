package intro.algo.hash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import util.MathUtil;

/**
 * Implement perfect hash in Introduction to algorithm. see Page 246.
 * 
 * @author wueddie-wym-wrz
 * 
 */

public class PerfectHash {
	/**
	 * perfect hashing based on ((a*k+b) mod p)mod m.
	 * 
	 * @param a
	 * @return
	 */
	public static Node[] perfectHashTwoLevel(int[] a) {
		int p = 101;// should be the prime larger than max of array .
		// Integer.
		// Arrays.
		int m = a.length;
		int times1 = 0;
		int times2 = 0;
		Node[] nodes = new Node[m];
		List<Integer>[] lists = new List[m];
		external: for (int i = 1; i < p; i++) {// a
			for (int j = 0; j < p; j++) {// b
				times1++;
				int[] counts = new int[m];
				int count = 0;
				for (int k = 0; k < a.length; k++) {
					counts[((i * a[k] + j) % p) % m]++;
				}
				for (int k = 0; k < m; k++) {
					count += counts[k] * counts[k];
				}
				if (count <= 2 * a.length + 1) {
					System.out.println("after try " + times1 + " times; a=" + i
							+ "; b=" + j + ";");

					for (int k = 0; k < m; k++) {
						if (counts[k] == 1) {
							nodes[k] = new Node();
							nodes[k].m = 1;
							nodes[k].a = 0;
							nodes[k].b = 0;
							nodes[k].S = new int[1];

						} else if (counts[k] > 1) {
							nodes[k] = new Node();
							nodes[k].m = counts[k] * counts[k];
							nodes[k].S = new int[counts[k] * counts[k]];
							lists[k] = new ArrayList<Integer>(counts[k]);
						}// * counts[k];
					}
					// collect mumbers in same slot
					for (int k = 0; k < a.length; k++) {
						if (lists[((i * a[k] + j) % p) % m] == null) {
							// only // one // element
							nodes[((i * a[k] + j) % p) % m].S[0] = a[k];
						} else {
							lists[((i * a[k] + j) % p) % m].add(a[k]);
						}
					}
					break external;
				}// if
			}

		}
		for (Node node : nodes) {
			if (node != null)
				System.out.println(node.toString());
		}

		// ren lao le, bu zhong yong le.
		// 1. fan zhe me di ji de cuo wu, er qie hua le zhe me duo shi jian lai
		// fa xian
		// 2. zhe she di'er ci le.

		// for (int ll = 0; ll < m && lists[ll] != null; ll++) {
		// set up seconde level hashing function.
		for (int ll = 0; ll < m; ll++) {
			if (lists[ll] == null)
				continue;
			times2 = 0;
			System.out.println(Arrays.toString(lists[ll]
					.toArray(new Integer[lists[ll].size()])));
			second_hash: for (int i = 1; i < p; i++) {// a
				internal: for (int j = 0; j < p; j++) {// b
					times2++;
					int mj = nodes[ll].m;
					int[] counts = new int[mj];

					for (int k : lists[ll]) {
						if (counts[((i * k + j) % p) % mj] != 0) {
							continue internal;
						} else {
							counts[((i * k + j) % p) % mj] = 1;
						}
					}
					System.out.println("after try " + times2 + " times; a=" + i
							+ "; b=" + j + ";");
					// no collision
					nodes[ll].a = i;
					nodes[ll].b = j;
					for (int k : lists[ll]) {
						nodes[ll].S[((i * k + j) % p) % mj] = k;
					}
					break second_hash;
				}
			}
		}
		return nodes;

	}

	public static Node[] perfectHashTwoLevelOO(int[] a) {
		int p = 101;// should be the prime larger than max of array .
		int m = a.length;

		TwoLevelModuloHash gh = TwoLevelModuloHash.getGoodHash(p, m, a);

		// apply the good hash we choosed.
		int[] counts = new int[m];
		Node[] nodes = new Node[m];
		List<Integer>[] lists = new List[m];
		for (int k = 0; k < a.length; k++) {
			counts[gh.hash(a[k])]++;
		}
		for (int k = 0; k < m; k++) {
			if (counts[k] == 1) {
				nodes[k] = new Node();
				nodes[k].m = 1;
				nodes[k].a = 0;
				nodes[k].b = 0;
				nodes[k].S = new int[1];

			} else if (counts[k] > 1) {
				nodes[k] = new Node();
				nodes[k].m = counts[k] * counts[k];
				nodes[k].S = new int[counts[k] * counts[k]];
				lists[k] = new ArrayList<Integer>(counts[k]);
			}// * counts[k];
		}
		// collect mumbers in same slot
		for (int k = 0; k < a.length; k++) {
			if (lists[gh.hash(a[k])] == null) {// only one element
				nodes[gh.hash(a[k])].S[0] = a[k];
			} else {
				lists[gh.hash(a[k])].add(a[k]);
			}
		}
		// ren lao le, bu zhong yong le.
		// 1. fan zhe me di ji de cuo wu, er qie hua le zhe me duo shi jian lai
		// fa xian
		// 2. zhe she di'er ci le.

		// for (int ll = 0; ll < m && lists[ll] != null; ll++) {
		// set up seconde level hashing function.
		for (int listIndex = 0; listIndex < m; listIndex++) {
			if (lists[listIndex] == null)
				continue;
			System.out.println(Arrays.toString(lists[listIndex]
					.toArray(new Integer[0])));

			gh = TwoLevelModuloHash.getGoodHash(p, nodes[listIndex].m, a);
			nodes[listIndex].a = gh.a;
			nodes[listIndex].b = gh.b;
			for (int k : lists[listIndex]) {
				nodes[listIndex].S[gh.hash(k)] = k;
			}

		}
		return nodes;

	}

	Node[] nodes;

	public PerfectHash getPerfectHash(int[] a) {
		nodes = perfectHashTwoLevelOO(a);
		return this;
	}

	public Task get(int key) {
		return null;
	}

	public static int[] perfectHashOneLevel(int[] a) {
		int p = 1003;// should be the prime larger than max of array .
		// Integer.
		// Arrays.
		int m = a.length * 2;
		int times1 = 0;
		int pa = 0;
		int pb = 0;

		external: for (int i = 1; i < p; i++) {// a
			internal: for (int j = 0; j < p; j++) {// b
				times1++;
				int[] counts = new int[m];

				for (int k = 0; k < a.length; k++) {
					if (counts[((i * a[k] + j) % p) % m] != 0) {
						continue internal;
					} else {
						counts[((i * a[k] + j) % p) % m] = 1;
					}
				}

				System.out.println("after try " + times1 + " times; a=" + i
						+ "; b=" + j + ";");
				pa = i;
				pb = j;

				break external;
			}// if
		}
		int[] b = new int[m];
		for (int k = 0; k < a.length; k++) {
			b[((pa * a[k] + pb) % p) % m] = a[k];
		}
		return b;
	}
	
	/**
	 * Perfect Hash based on the dot product of A^T . K
	 * 
	 * @param a
	 * @return
	 */
	public static Node[] perfectHash2(int[] a) {
		Node[] nodes = null;// = new Node[];
		int m = a.length;

		// get the total bits in maxmimun element in a.
		int max = MathUtil.max(a);
		int[] vec = MathUtil.intToVector(max, m);
		//

		Random r = new Random();
		for (int i = 0; i < vec.length; i++) {
			vec[i] = (r.nextInt() % m);
		}
		VectorHash vh = new VectorHash(vec.length, m);
		for (int i = 0; i < a.length; i++) {

		}

		return nodes;
	}
}

class Node {
	int m;

	int a;

	int b;

	int[] S;

	public String toString() {
		return "[m=" + m + "; a=" + a + "; b=" + b + "; S="
				+ Arrays.toString(S) + "]";
	}

	

}
