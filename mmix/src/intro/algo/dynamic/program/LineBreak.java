package intro.algo.dynamic.program;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LineBreak {
	int M = 40;// M chars each line

	int n;
	// index from 0;
	int[] a;// neatness of a[i]->a[n]
	List<Integer>[] al;// the index for each break of substring a[i]->a[n]
	List<Integer>[] an;// the neatness for each break.

	public static void main(String[] args) {
		String input = "Are you part ace coder, part adrenaline junkie? Do you have a knack for seeing a problem and immediately discerning the likely solution? Maybe you have been coding for years, are bored with the old design-build-review-test-ship-repeat routine, and yearn for some faster-paced challenges? Or perhaps you are a seasoned software engineer who is also a genius at jockeying networks and administering UNIX clusters. ";
		LineBreak lb = new LineBreak();
		lb.lineBreak(input);
	}

	private void lineBreak(String input) {
		String[] s = input.split(" ");
		n = s.length;
		a = new int[n];
		al = new List[n];
		an = new List[n];
		for (int i = 0; i < n; i++) {
			a[i] = -1;
		}

		// store final result
		List<Integer> ll2 = new ArrayList<Integer>();// start index
		List<Integer> neatness2 = new ArrayList<Integer>();// neatness

		int cubeNeat2 = optimalRecursive(s, 0, ll2, neatness2);
		System.out.println("final result of cubeNeat2= " + cubeNeat2);

		System.out.println(Arrays.toString(ll2.toArray(new Integer[0])));
		System.out.println(Arrays.toString(neatness2.toArray(new Integer[0])));
		output2(s, ll2);
		for (int i = 0; i < n; i++) {
			if (a[i] != -1) {
				System.out.println("i=" + i + "; neatness=" + a[i]);
			}
		}
	}

	void output(String[] s2, List<Integer> ll, List<Integer> neatness) {
		Integer[] br = ll.toArray(new Integer[0]);
		System.out.println("there are " + br.length + " lines;");
		for (int i = 0; i < br.length - 1; i++) {
			int a = br[i];
			int b = br[i + 1];
			for (int k = a; k < b - 1; k++) {
				System.out.print(s2[k] + " ");
			}
			System.out.print(s2[b - 1]);
			// System.out.println();
			System.out.println(" - neatness is " + neatness.get(i));
		}
		// last line
		for (int k = br[br.length - 1]; k < s2.length - 1; k++) {
			System.out.print(s2[k] + " ");
		}
		System.out.print(s2[s2.length - 1]);
		System.out.println(" - neatness is " + neatness.get(br.length - 1));
	}

	void output2(String[] s2, List<Integer> ll) {
		Integer[] br = ll.toArray(new Integer[0]);
		System.out.println("there are " + br.length + " lines;");
		for (int i = 0; i < br.length - 1; i++) {
			int a = br[i];
			int b = br[i + 1];
			int L = 0;
			for (int k = a; k < b - 1; k++) {
				System.out.print(s2[k] + " ");
				L += s2[k].length() + 1;
			}
			System.out.print(s2[b - 1]);
			L += s2[b - 1].length();
			// System.out.println();
			System.out.println(" - neatness is " + (M - L));
		}
		// last line
		for (int k = br[br.length - 1]; k < s2.length - 1; k++) {
			System.out.print(s2[k] + " ");
		}
		System.out.print(s2[s2.length - 1]);
		System.out.println(" - neatness is " + 0);
	}

	/**
	 * 
	 * @param s
	 * @param si
	 *            start index of S, decide the substring of S.
	 * @param ll2
	 * @param neatness2
	 * @return
	 */
	private int optimalRecursive(String[] s, int si, List<Integer> ll2,
			List<Integer> neatness2) {

		// to get the reference result!
		List<Integer> ll = new ArrayList<Integer>();
		List<Integer> neatness = new ArrayList<Integer>();

		int cubeNeat = localfit(s, si, ll, neatness);
		int cubeNeat2 = 0;
		if (ll.size() <= 1) {
			cubeNeat2 = 0;
			ll2 = ll;
			a[si] = cubeNeat2;
			neatness2 = neatness;
			al[si] = ll2;
			an[si] = neatness2;
			return cubeNeat2;
		} else if (ll.size() == 2) {
			cubeNeat2 = cubeNeat;
			a[si] = cubeNeat2;
			ll2 = ll;
			neatness2 = neatness;
			al[si] = ll2;
			an[si] = neatness2;
			return cubeNeat2;
		}
		int minNeat = cubeNeat;// may have better result.

		int tempNeat = 0;
		int maxNeat = (int) Math.pow(cubeNeat, 1.0 / 3.0);
		int index = ll.get(1) - 1;
		int length = neatness.get(0);// initial neatness value
		boolean reference = true;
		while (index != ll.get(0)) {// shift s[index] to next line
			length += s[index].length() + 1;
			if (length > maxNeat) {
				break;
			}
			if (neatness.get(1) < s[index].length() + 1) {// can not hold it
				// without extend
				if (s[ll.get(2) - 1].length() > maxNeat) {
					break;
				}
			}
			if (a[index] != -1) {
				tempNeat = (int) Math.pow(length, 3) + a[index];

				ll2.clear();
				ll2.add(si);
				ll2.addAll(al[index]);

				neatness2.clear();
				neatness2.add(length);
				neatness2.addAll(an[index]);
			} else {
				// String[] sub = Arrays.copyOfRange(s, index, s.length);
				List<Integer> ll22 = new ArrayList<Integer>();
				List<Integer> neatness22 = new ArrayList<Integer>();
				int cubeNeat22 = optimalRecursive(s, index, ll22, neatness22);
				tempNeat = (int) Math.pow(length, 3) + cubeNeat22;

				ll2.clear();
				ll2.add(si);
				ll2.addAll(ll22);

				neatness2.clear();
				neatness2.add(length);
				neatness2.addAll(neatness22);
			}
			if (tempNeat < minNeat) {
				minNeat = tempNeat;
				reference = false;
			}
			index--;
		}// while
		if (reference == true) {// the first line do not need change, already
			// optimal
			List<Integer> ll22 = new ArrayList<Integer>();
			List<Integer> neatness22 = new ArrayList<Integer>();
			System.out.println("use reference!");
			System.out.println();
			// bug
			// index = ll.get(1);
			if (a[ll.get(1)] != -1) {
				cubeNeat2 = (int) Math.pow(neatness.get(0), 3) + a[ll.get(1)];
				ll2.clear();
				ll2.add(si);
				ll2.addAll(al[ll.get(1)]);

				neatness2.clear();
				neatness2.add(neatness.get(0));
				neatness2.addAll(an[ll.get(1)]);
				System.out.println("cached result, cubeNeat2=" + cubeNeat2);
			} else {
				// String[] sub = Arrays.copyOfRange(s, ll.get(1), s.length);

				int cubeNeat22 = optimalRecursive(s, ll.get(1), ll22,
						neatness22);
				ll2.clear();
				ll2.add(si);
				ll2.addAll(ll22);

				neatness2.clear();
				neatness2.add(neatness.get(0));
				neatness2.addAll(neatness22);
				cubeNeat2 = (int) Math.pow(neatness.get(0), 3) + cubeNeat22;
				System.out.println("calculated result, cubeNeat2=" + cubeNeat2);
			}
		} else {
			cubeNeat2 = minNeat;
			System.out.println("adjust effectively. cubeNeat2=" + cubeNeat2);
		}
		a[si] = cubeNeat2;
		al[si] = ll2;
		an[si] = neatness2;
		return cubeNeat2;
	}

	private void optimal(String[] s, List<Integer> ll, List<Integer> neatness) {

		Integer[] llI = ll.toArray(new Integer[0]);
		int m = llI.length;// lines in tight mode
		Integer[] neatI = neatness.toArray(new Integer[0]);

		for (int j = llI[m - 1]; j < n; j++) {
			a[j] = 0;
		}

		// for(int i=m-2;i>=0;i--){
		//			
		// }

		for (int i = llI[m - 1] - 1; i >= 0; i--) {
			String[] si = Arrays.copyOfRange(s, i, n);
			List<Integer> ll2 = new ArrayList<Integer>();
			List<Integer> neatness2 = new ArrayList<Integer>();
			// localfit(s, ll2, neatness2);
		}
	}

	/**
	 * each line is as tight as possible, a good reference solution even it may
	 * not be the optimal solution.
	 * 
	 * @param s2
	 *            the global string
	 * @param si
	 *            start index of sub string.
	 * @param ll
	 * @param neatness
	 */
	private int localfit(String[] s2, int si, List<Integer> ll,
			List<Integer> neatness) {
		String[] s = Arrays.copyOfRange(s2, si, s2.length);
		//Arrays.
		int L = 0;
		int tLine = 0;
		ll.add(0 + si);
		tLine = s[0].length();
		int allNeat = 0;
		for (int i = 1; i < s.length; i++) {
			L = s[i].length();
			tLine += (L + 1);
			if (tLine > M) {
				neatness.add((M - (tLine - (L + 1))));
				allNeat += (int) Math.pow((M - (tLine - (L + 1))), 3);
				ll.add(i + si);// new line

				tLine = L;
			}
		}
		neatness.add(0);// nestness is 0 for last line.

		int maxNeat = (int) Math.pow(allNeat, 1.0 / 3.0);
		System.out.println("single neat can not be large than " + maxNeat);

		output(s2, ll, neatness);
		return allNeat;
	}
}
