package taocp.v4c7;


/**
 * Count = 2 for N = 4 <br/>
 * it takes 804808<br/>
 * Count = 92 for N = 8<br/>
 * it takes 8500514<br/>
 * Count = 14200 for N = 12<br/>
 * it takes 452,303,174 (0.5s)<br/>
 * Count = 365596 for N = 14<br/>
 * it takes 15,700,198,893 (close to 16s)<br/>
 * Count = 14772512 for N = 16<br/>
 * it takes 774,962,093,219 (close to 13min)<br/>
 * it match the result in p10 of taocp fasc5b.
 * 
 * @author think
 *
 */
public class Quene {
	public static void main(String[] args) {
		// result 4->2, 8->92, 16->14772512, 24->
		// monte carlo:
		long start, end;
		start = System.nanoTime();
		new Quene(4).backTrack();
		end = System.nanoTime();
		System.out.println("it takes " + (end - start));
		new Quene(8).backTrack();
		end = System.nanoTime();
		System.out.println("it takes " + (end - start));

		new Quene(12).backTrack();
		end = System.nanoTime();
		System.out.println("it takes " + (end - start));

		new Quene(14).backTrack();
		end = System.nanoTime();
		System.out.println("it takes " + (end - start));

		new Quene(16).backTrack();
		end = System.nanoTime();
		System.out.println("it takes " + (end - start));
	}

	private int N = 4;
	private int a[], b[];

	public Quene(int n) {
		N = n;
		a = new int[N];
		b = new int[N];// to be verified index;
	}

	public void backTrack() {
		int level = 0;
		int count = 0;

		while (level >= 0) {
			while (b[level] < N) {
				a[level] = b[level]++;
				// whether it is valid after new selection in current level.
				boolean valid = true;
				for (int i = 0; i < level; i++) {
					if (a[i] == a[level])
						valid = false;
					else if (a[i] - a[level] == i - level
							|| a[i] - a[level] == level - i)
						valid = false;
				}
				if (valid == false) {
					continue;
				}
				// valid
				if (level == N - 1) {
					count++;
					// System.out.println(Arrays.toString(a));
				} else {
					level++;
				}

			}
			// exhaust all choices
			b[level] = 0;
			level--;
		}

		System.out.println("Count = " + count + " for N = " + N);
	}
}
