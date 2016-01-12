package taocp.v2c4;

import java.util.Arrays;

/**
 * To support non-negative integer of any precision, each long store a number <
 * 2 ^ 63. estimation: about 9 * 10 ^ 18. <br/>
 * strategy: extend storage dynamically on the fly. <br/>
 * zero is by empty long array.
 * 
 * @author think
 *
 */
public class BigNum {
	private static long mask = 1L << 63;
	// private static long mask2 = mask^0;
	boolean negative;
	private long[] words;

	// private long[] long[];

	public static BigNum valueOf(String input) {
		int n = input.length() / 18 + 1;
		long[] words = new long[n];
		BigNum res = new BigNum();

		int index = 0;
		byte[] bytes = input.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			// temp = temp * 10 + bytes[i] - 32;
			res.multiply(10);
			res.add(bytes[i] - 32);
		}
		return res;
		// return new BigNum(words, false);
	}

	// default to accept digit number of base 10
	public BigNum(String input) {
		int n = input.length() / 18 + 1;
		long[] words = new long[n];

		int index = 0;
		byte[] bytes = input.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			// temp = temp * 10 + bytes[i] - 32;
			this.multiply(10);
			this.add(bytes[i] - 32);

		}
	}

	public boolean isZero() {
		return words.length == 0;
	}

	public boolean isPositive() {
		return negative == false && words.length != 0;
	}

	public boolean isNegative() {
		return negative && words.length != 0;
	}

	private BigNum() {
		words = new long[0];// zero by default
	}

	public BigNum(long word) {
		if (word == 0) {
			words = new long[0];
		} else if (word == Long.MIN_VALUE) { // tricky case!
			words = new long[2];
			words[0] = 1;
			negative = true;
		} else {
			this.words = new long[1];
			if (word < 0) {
				negative = true;
				words[0] = 0 - word;
			} else {
				words[0] = word;
			}

		}
	}

	public BigNum(long[] words, boolean negative) {
		for (int i = 0; i < words.length; i++) {
			if (words[i] < 0) { // sign bit (64th if indexing from 1) is set
				throw new RuntimeException(
						"invalid multi precision big number " + words[i]);
			}
		}
		if (words.length < 1) {
			throw new RuntimeException("invalid big number - empty array");
		} else if ((words.length > 1 && words[0] == 0)) {
			throw new RuntimeException("invalid big number - leading zero");
		}
		init(words, negative);
	}

	public void init(long[] words, boolean negative) {
		this.words = words;
		this.negative = negative;
	}

	private BigNum multiply(long t) {
		if (this.isZero())
			return this;
		else if (t == 0) {
			this.words = new long[0];
			return this;
		} else if (t == Long.MIN_VALUE) {
			// multiply -2^63
			long[] resWords = new long[words.length + 1];
			resWords[words.length] = 0;
			System.arraycopy(words, 0, resWords, 0, words.length);
			this.words = resWords;
			this.negative = !this.negative;
		}

		// both are not 0; abs value <= Long.MAX_VALUE.
		long[] carry = new long[words.length + 1];
		for (int i = words.length - 1; i >= 0; i--) {
			long[] res = multiply(words[i], t);
			words[i] = res[1];
			carry[i] = res[0];
		}
		words = add(carry, words);

		// if (carry[0] > 0) {
		// // have to extend original long[]!
		// long[] resWords = new long[words.length + 1];
		// resWords[0] = carry;
		// System.arraycopy(words, 0, resWords, 1, words.length);
		// this.words = resWords;
		// }
		return this;
		// BigNum right = new BigNum(t);
		// return this.multiply(right);
	}

	private BigNum multiply(BigNum right) {
		if (this.isZero())
			return this;
		else if (right.isZero()) {
			this.words = new long[1];
			return this;
		}
		if (this.negative == right.negative) {
			this.negative = false;
		} else {
			this.negative = true;
		}

		// below only consider positive integer number.
		long[] resWords = new long[words.length + right.words.length];
		long[] carry = null;
		long[] temp = null;
		for (int j = right.words.length - 1; j >= 0; j--) {
			temp = new long[words.length + right.words.length - 1 - j];
			carry = new long[words.length + right.words.length - j];
			for (int i = words.length - 1; i >= 0; i--) {
				long[] res = multiply(words[i], right.words[j]);
				carry[i] = res[0];
				temp[i] = res[1];
				add(resWords, temp);
				add(resWords, carry);
			}
		}
		this.words = standardize(resWords);
		return this;
	}

	public BigNum add(long t) {
		BigNum right = new BigNum(t);
		return this.add(right);
	}

	/**
	 * itself is modifiable. either change content in words, or change words
	 * reference itself <br/>
	 * without consider whether it is negative
	 * 
	 * @param right
	 *            is not modifiable
	 * @return
	 */
	public BigNum add(BigNum right) {
		if (this == right) {
			// tricky case!
			this.multiply(2);
		}
		long[] resWords;
		long[] small = null, big = null;
		if (this.negative == right.negative) {

			if (words.length < right.words.length) {
				resWords = new long[right.words.length];
				small = words;
				big = right.words;
				// copy the big one to the result
				System.arraycopy(big, 0, resWords, 0, big.length);
				this.words = resWords;
			} else {
				big = words;
				small = right.words;
			}
			this.words = add(big, small);
		} else { // convert to subtraction.
			if (words.length > right.words.length) {
				big = words;
				small = right.words;
			} else if (words.length < right.words.length) {
				// safe copy since right is NOT modifiable.
				resWords = new long[right.words.length];
				System.arraycopy(right.words, 0, resWords, 0,
						right.words.length);
				small = words;
				big = resWords;
			} else {
				for (int i = 0; i < words.length; i++) {
					if (words[i] > right.words[i]) {
						big = words;
						small = right.words;
						break;
					} else if (words[i] < right.words[i]) {
						// safe copy since right is NOT modifiable.
						resWords = new long[right.words.length];
						System.arraycopy(right.words, 0, resWords, 0,
								right.words.length);
						small = words;
						big = resWords;
						break;
					}
				}
			}
			if (small == null) {
				// return zero
				this.negative = false;
				this.words = new long[0];
			}
			// decide the sign!
			if (this.words == small) {

				this.negative = !(this.negative);

			}
			// always sub small from big!
			this.words = subtract(big, small);
		}
		return this;

	}

	/**
	 * core implementation of adding two long array. big is changed, small is
	 * NOT changed. <br/>
	 * big could have leading zeros!
	 * 
	 * @param big
	 *            is the bigger one.
	 * @param small
	 * @return
	 */
	private static long[] add(long[] big, long[] small) {
		int delta = big.length - small.length;
		long carry = 0;
		for (int i = big.length - 1; i >= 0; i--) {
			if (i - delta >= 0) {
				big[i] += small[i - delta];
			}
			big[i] += carry;
			if (big[i] < 0) {
				carry = 1;
				big[i] ^= mask; // XOR
			} else {
				carry = 0;
				if (i - delta < 0) {
					break;
				}
			}
		}
		if (carry == 1) {
			// extend dynamically.
			long[] resWords = new long[big.length + 1];
			resWords[0] = 1;
			System.arraycopy(big, 0, resWords, 1, big.length);
			return resWords;
		} else {
			return big;
		}
	}

	/**
	 * 
	 * @param big
	 *            is ensured to larger than small.
	 * @param small
	 * @return
	 */
	private static long[] subtract(long[] big, long[] small) {
		int delta = big.length - small.length;
		long borrow = 0; // 1 means borrowed.
		for (int i = big.length - 1; i >= 0; i--) {
			if (i - delta >= 0) {
				long temp = big[i] - small[i - delta] + borrow;
				if (temp >= 0) {
					big[i] = temp;
					borrow = 0;
				} else {
					// caution of overflow.
					big[i] = Long.MAX_VALUE + temp + 1;
					borrow = 1;
				}
			} else {
				if (borrow == 0)
					break;
				if (big[i] > 0) {
					big[i] -= borrow;
					borrow = 0;
					break;
				} else if (big[i] == 0) {
					big[i] = Long.MAX_VALUE;
					// borrow = 1;
				} else {
					throw new RuntimeException("negative big[i]");
				}
			}
		}
		return standardize(big);

	}

	public static long[] standardize(long[] big) {
		// standardize the long array (removing leading zeros).
		int count = 0;
		for (int i = 0; i <= big.length - 1; i++) {
			if (big[i] != 0)
				break;
			else
				count++;
		}
		if (count != 0) {
			return Arrays.copyOfRange(big, count, big.length);
		} else {
			return big;
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (negative)
			sb.append("-");

		return sb.toString();
	}

	public BigNum getCopy() {
		BigNum temp = new BigNum();
		temp.init(this.words, this.negative);
		return temp;
	}

	public String toHexString() {
		StringBuilder sb = new StringBuilder();
		if (negative)
			sb.append("-");
		sb.append("[");
		for (int i = 0; i < words.length; i++) {
			sb.append(Long.toHexString(words[i]));
			sb.append(",");
		}
		sb.append("]");
		return sb.toString();
	}

	public String toDecimalString() {
		StringBuilder sb = new StringBuilder();
		if (negative)
			sb.append("-");
		byte[] temp = new byte[words.length * 19];
		BigNum copy = this.getCopy();
		copy.divideBy(10);
		return sb.toString();
	}

	public BigNum divideBy(long i) {
		long[] temp = new long[1];
		temp[0] = i;
		return null;
	}

	public BigNum divideBy(BigNum other) {

		return null;
	}

	/**
	 * divide long to three integers then multiply. <br/>
	 * (63 = 1 + 31 + 31) to enhance java multiply to support 63 <br/>
	 * 63 bits * 63 bit -> 126 bit. <br/>
	 * 50 to 100 cycles, much worse than hardware
	 * 
	 * @param aa
	 *            >0
	 * @param bb
	 *            >0
	 * @return res[0] * 2^63 + res[1]
	 */
	public static long[] multiply(long aa, long bb) {
		long mask31 = 1L << 31 - 1;
		long mask32 = 1L << 32 - 1;
		long[] res = new long[2];

		long[] a = new long[3];
		long[] b = new long[3];
		a[2] = aa & mask31;
		a[1] = (aa >> 31) & mask31;
		a[0] = (aa >> 31);// & mask31;

		b[2] = bb & mask31;
		b[1] = (bb >> 31) & mask31;
		b[0] = (bb >> 31);// & mask31;

		res[1] = a[2] * b[2];

		long[] temp = new long[2];
		long t = a[1] * b[2] + a[2] * b[1];
		temp[1] = (t & mask32) << 31;
		temp[0] = t >> 32;
		add(res, temp);

		if (b[0] > 0) {
			temp[1] = (aa & 1) << 62; // last bit become first bit
			temp[0] = (aa & (1L << 62)) >> 1;
			add(res, temp);
		}
		if (a[0] > 0) {
			temp[1] = ((bb & 1) << 62);
			temp[0] = (bb & (1L << 62)) >> 1;
			add(res, temp);
		}

		return res;
	}
}
