package util;

import java.math.BigInteger;
import java.util.Random;

import org.apache.log4j.Logger;

import taocp.v3.sort.IntArrayUtil;

public class MathUtil {
	private static Logger log = Logger.getLogger(MathUtil.class);

	/**
	 * calculate n*(n-1)*(n-2)...*2*1
	 * 
	 * @return
	 */
	public static int factorial(int n) {
		if (n < 0) {
			throw new IllegalArgumentException();
		} else if (n == 1 || n == 0) {
			return 1;
		} else {
			return n * factorial(n - 1);
		}

	}

	/**
	 * calculate n*(n-1)*(n-2)...*2*1
	 * 
	 * @return
	 */
	public static long factorial(long n) {
		if (n < 0) {
			throw new IllegalArgumentException();
		} else if (n == 0) {
			return 1;
		} else {
			return n * factorial(n - 1);
		}

	}

	/**
	 * get the complete permutation of n elements.
	 * 
	 * @param permutations
	 * @param inputs
	 */
	public static int[][] getPermutation(int[] inputs) {
		int i = inputs.length;
		int[][] permutations = new int[MathUtil.factorial(i)][i];
		init(permutations, inputs);
		return permutations;
	}

	/**
	 * get the complete permutation of n elements.
	 * 
	 * @param permutations
	 * @param inputs
	 */
	public static void init(int[][] permutations, int[] inputs) {
		int tmp = inputs.length;
		if (tmp == 1) {
			permutations[0][0] = inputs[0];
			return;
		} else if (tmp == 2) {
			permutations[0][0] = inputs[0];
			permutations[0][1] = inputs[1];
			permutations[1][0] = inputs[1];
			permutations[1][1] = inputs[0];
			return;
		}
		for (int i = 0; i < inputs.length; i++) {
			int t = inputs.length - 1;
			int p = MathUtil.factorial(t);
			for (int j = 0; j < p; j++) {
				permutations[i * p + j][0] = inputs[i];
			}
			IntArrayUtil.print(permutations, log);
			int[] sub = subArray(inputs, i);
			IntArrayUtil.print(sub, log);
			int[][] perm = new int[p][t];
			init(perm, sub);
			IntArrayUtil.print(perm, log);
			for (int k = 0; k < p; k++) {
				for (int l = 0; l < t; l++) {
					permutations[i * p + k][l + 1] = perm[k][l];
				}
			}
			IntArrayUtil.print(permutations, log);
		}

	}

	/**
	 * remove the element o from the array.
	 * 
	 * @param inputs
	 * @param o
	 * @return
	 */
	public static int[] subArray(int[] inputs, int o) {
		int[] ret = new int[inputs.length - 1];
		if (inputs.length == 1) {
			return ret;
		}
		for (int i = 0; i < o; i++) {
			ret[i] = inputs[i];
		}
		for (int i = o; i < ret.length; i++) {
			ret[i] = inputs[i + 1];
		}
		return ret;
	}

	public static int[] getRandomIntArray(int arraySize) {
		Random r = new Random();
		int[] a = new int[arraySize];
		for (int i = 0; i < arraySize; i++) {
			a[i] = r.nextInt(100);
		}
		return a;
	}

	public static int[] getRandomPositiveIntArray(int arraySize) {
		Random r = new Random();
		int[] a = new int[arraySize];
		for (int i = 0; i < arraySize; i++) {
			a[i] = r.nextInt(100);
			if (a[i] < 0) {
				a[i] >>>= 1;
			}
		}
		return a;
	}

	public static int[] getRandomPositiveIntArray(int arraySize, int seed) {
		Random r = new Random(seed);
		int[] a = new int[arraySize];
		for (int i = 0; i < arraySize; i++) {
			a[i] = r.nextInt(100);
			if (a[i] < 0) {
				a[i] >>>= 1;
			}
		}
		return a;
	}

	public static int[] getHistogram(int[] a) {
		int[] b = new int[100];
		for (int i = 0; i < a.length; i++) {
			b[a[i]]++;

		}
		return b;
	}

	/**
	 * 
	 * @param ty
	 *            the array of bits
	 * @param bits
	 *            number of bits; less than or equal to 32
	 * @return
	 */
	public int composeBitToInt(int[] ty, int bits) {
		int tt = 0;
		for (int ki = 0; ki < bits; ki++) {
			tt += (ty[ki] << (bits - 1 - ki));
		}
		return tt;
	}

	/**
	 * 
	 * @param ty
	 *            the array of bits
	 * @param bits
	 *            number of bits; less than or equal to 64
	 * @return
	 */
	public long composeBitToLong(int[] ty, int bits) {
		long tt = 0;
		for (int ki = 0; ki < bits; ki++) {
			tt += (ty[ki] << (bits - 1 - ki));
		}
		return tt;
	}

	public int[] decomposeToBit(int t, int bits) {
		int[] tx = new int[bits];
		for (int i = 0; i < bits; i++) {
			tx[bits - 1 - i] = (t >>> i) & 1;
		}
		return tx;
	}

	public int[] decomposeToBit(long t, int bits) {
		int[] tx = new int[bits];
		for (int i = 0; i < bits; i++) {
			tx[bits - 1 - i] = (int) ((t >>> i) & 1L);
		}
		return tx;
	}

	public static boolean getOddParity(long temp) {
		int count = BigInteger.valueOf(temp).bitCount();
		if (temp < 0) {
			count = 64 - count;
		}
		if (count % 2 == 0) {
			return false;
		} else {
			return true;
		}
	}

	public static long power(int value, int order) {
		return (long) Math.pow(value, order);
	}

	static int swapCount;

	public static int getSwapCount() {
		return swapCount;
	}

	public static void swap(int[] pa, int a, int b) {
		int temp = pa[a];
		pa[a] = pa[b];
		pa[b] = temp;
		swapCount++;
	}

	/**
	 * 
	 * @param a
	 *            a.length should >=1;
	 * @return
	 */
	public static int max(int[] a) {

		int max = a[0];
		for (int i = 1; i < a.length; i++) {
			if (a[i] > max) {
				max = a[i];
			}
		}
		return max;
	}

	public static int[] intToVector(int value, int radix) {
		int product = 1;
		int count = 0;
		while (product < value) {
			product *= radix;
			count++;
		}
		int[] b = new int[count];
		for (int i = 0; i < count; i++) {
			b[count - 1 - i] = value % radix;
			value /= radix;
		}
		return b;

	}

	public static int log(int radix, double value) {
		return (int) (Math.log(value) / Math.log(radix));
	}
}
