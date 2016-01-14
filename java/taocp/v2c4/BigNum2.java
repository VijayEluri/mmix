package taocp.v2c4;

public class BigNum2 {
	boolean negative;
	private long[] longArray;
	private static long mask = 1 << 63;

	public BigNum2 add(BigNum2 other) {
		long[] temp; // avoid new if possible.
		int otherLength = other.longArray.length;
		long carry = 0;
		int delta;
		if (longArray.length < otherLength) {
			// store result in temp. long[]
			temp = new long[otherLength];
			delta = otherLength - longArray.length;
			for (int i = otherLength; i >= 0; i--) {
				if (i - delta >= 0) {
					temp[i] = (longArray[i - delta] + other.longArray[i] + carry);
				} else {
					temp[i] = other.longArray[i] + carry;
				}
				if (temp[i] < 0) {
					carry = 1;
					temp[i] = temp[i] ^ mask;
				} else {
					carry = 0;
				}
				// carry = (temp[i] & mask) != 0 ? 1 : 0;
			}
			// copy back to this.
			if (carry == 1) { // rare case
				this.longArray = new long[otherLength + 1];
				longArray[0] = 1;
				System.arraycopy(temp, 0, longArray, 1, otherLength);
			} else {
				this.longArray = new long[otherLength];
				System.arraycopy(temp, 0, longArray, 0, otherLength);
			}
		} else {
			// change this's longArray directly.
			delta = longArray.length - otherLength;
			for (int i = otherLength; i >= 0; i--) {
				if (i - delta >= 0) {
					longArray[i] = (longArray[i] + other.longArray[i - delta] + carry);
				} else {
					longArray[i] = longArray[i] + carry;
				}
				// carry = (longArray[i] & mask) != 0 ? 1 : 0;
				if (longArray[i] < 0) {
					carry = 1;
					longArray[i] ^= mask;
				} else {
					carry = 0;
				}
			}
			if (carry == 1) { // rare case
				temp = new long[otherLength + 1];
				temp[0] = 1;
				System.arraycopy(longArray, 0, temp, 1, otherLength);
				this.longArray = temp;
			}
		}
		return this;
	}

}
