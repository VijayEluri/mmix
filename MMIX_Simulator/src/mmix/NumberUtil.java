package mmix;

import java.math.BigInteger;

/**
 * <p>
 * NumberUtil.java
 * </p>
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class NumberUtil {

	/**
	 * 
	 * @param temp
	 * @return
	 */
	public static int getUnsignedByte(byte temp) {
		if (temp < 0) {
			return temp + 256;
		} else {
			return temp;
		}
	}

	/**
	 * 
	 * @param high
	 *            treat as unsigned int. [0..255]
	 * @param low
	 *            treat as unsigned int. [0..255]
	 * @return
	 */
	public static int getUnsignedWyde(byte high, byte low) {
		int[] nums = new int[2];
		if (low < 0) {
			nums[1] = low + 0x100;
		} else {
			nums[1] = low;

		}
		if (high < 0) {
			nums[0] = high + 0x100;
		} else {
			nums[0] = high;

		}
		return (nums[0] * 0x100 + nums[1]);

	}

	/**
	 * 
	 * @param high
	 *            [0..255]
	 * @param low
	 *            [0..255]
	 * @return
	 */
	public static int getUnsignedWyde(int high, int low) {

		return (high * 0x100 + low);

	}

	public static long getUnsignedTetra(byte high, byte low, byte lhigh,
			byte llow) {
		long[] nums = new long[4];
		if (llow < 0) {
			nums[3] = llow + 0x100;
		} else {
			nums[3] = llow;
		}
		if (lhigh < 0) {
			nums[2] = lhigh + 0x100;
		} else {
			nums[2] = lhigh;

		}
		if (low < 0) {
			nums[1] = low + 0x100;
		} else {
			nums[1] = low;

		}
		if (high < 0) {
			nums[0] = high + 0x100;
		} else {
			nums[0] = high;

		}
		return (nums[0] * 0x1000000L + nums[1] * 0x10000 + nums[2] * 0x100 + nums[3]);

	}

	/**
	 * all param are large than or equal to 0;
	 * 
	 * @param high
	 * @param low
	 * @param lhigh
	 * @param llow
	 * @return
	 */
	public static long getUnsignedTetra(int high, int low, int lhigh, int llow) {
		long[] nums = new long[4];
		if (llow < 0) {
			nums[3] = llow + 0x100;
		} else {
			nums[3] = llow;
		}
		if (lhigh < 0) {
			nums[2] = lhigh + 0x100;
		} else {
			nums[2] = lhigh;

		}
		if (low < 0) {
			nums[1] = low + 0x100;
		} else {
			nums[1] = low;

		}
		if (high < 0) {
			nums[0] = high + 0x100;
		} else {
			nums[0] = high;

		}
		return (nums[0] * 0x1000000L + nums[1] * 0x10000 + nums[2] * 0x100 + nums[3]);

	}

	public static long getUnsignedTetra2(byte high, byte low, byte lhigh,
			byte llow) {
		byte[] tt = new byte[4];
		tt[0] = high;
		tt[1] = low;
		tt[2] = lhigh;
		tt[3] = llow;
		BigInteger t = new BigInteger(tt);
		if (t.longValue() < 0) {
			return t.longValue() + 0x100000000L;
		} else {
			return t.longValue();
		}

	}

	// majorly for loading
	public static int getSignedWyde(byte high, byte low) {
		byte[] tt = new byte[2];
		tt[0] = high;
		tt[1] = low;
		BigInteger t = new BigInteger(tt);
		return t.intValue();
	}

	public static int getSignedWyde(byte[] value, int offset) {
		byte[] tt = new byte[2];
		tt[0] = value[offset];
		tt[1] = value[offset + 1];
		BigInteger t = new BigInteger(tt);
		return t.intValue();
	}

	public static int getSignedTetra(byte high, byte low, byte lhigh, byte llow) {
		byte[] tt = new byte[4];
		tt[0] = high;
		tt[1] = low;
		tt[2] = lhigh;
		tt[3] = llow;
		BigInteger t = new BigInteger(tt);
		return t.intValue();
	}

	/**
	 * 
	 * @param value
	 * @param offset
	 * @return
	 */
	public static int getSignedTetra(byte[] value, int offset) {
		byte[] tt = new byte[4];
		tt[0] = value[offset];
		tt[1] = value[offset + 1];
		tt[2] = value[offset + 2];
		tt[3] = value[offset + 3];
		BigInteger t = new BigInteger(tt);
		return t.intValue();
	}

	public static long getSignedOcta(byte[] tt) {

		BigInteger t = new BigInteger(tt);
		return t.longValue();
	}

	public static long getSignedOcta(byte[] value, int offset) {
		byte[] tt = new byte[8];
		tt[0] = value[offset];
		tt[1] = value[offset + 1];
		tt[2] = value[offset + 2];
		tt[3] = value[offset + 3];
		tt[4] = value[offset + 4];
		tt[5] = value[offset + 5];
		tt[6] = value[offset + 6];
		tt[7] = value[offset + 7];

		BigInteger t = new BigInteger(tt);
		return t.longValue();
	}

	public static byte[] bitwiseByte(byte tmp) {
		byte[] result = new byte[8];
		for (int i = 0; i < 8; i++) {
			result[i] = (byte) ((tmp >>> (7 - i)) & 1);
		}
		return result;
	}

	public static int reverseBitwiseByte(byte result[]) {
		int temp = 0;
		for (int i = 0; i < 8; i++) {
			temp += (result[7 - i] << i);// = (byte) ((tmp >>> (7 - i)) & 1);

		}
		return temp;
	}

	public static long getUnsignedTetra(int temp) {
		long value;
		//Bug:
		//if (temp > 0) {
		if (temp >= 0) {
			value = temp;
		} else {
			//Not a bug.
			//value = temp + 0x100000000L;
			value = temp + 0x100000000L;
		}
		return value;
	}

	/**
	 * add prefix 0 ifnecessary.
	 * 
	 * @param temp
	 *            <256
	 * @return
	 */
	public static String byteToHex(int temp) {
		if (temp < 16) {
			return "0" + Integer.toHexString(temp);
		}
		return Integer.toHexString(temp);
	}

	public static String byteToHex(byte temp) {
		int tt = NumberUtil.getUnsignedByte(temp);
		if (tt < 16) {
			return "0" + Integer.toHexString(tt);
		}
		return Integer.toHexString(tt);

	}

	public static String intToHex(int value) {
		// long value = getUnsignedTetra(temp);
		String str = Integer.toHexString(value);
		byte[] template = "00000000".getBytes();
		System.arraycopy(str.getBytes(), 0, template, 8 - str.length(), str
				.length());
		return new String(template);
	}

	public static String longToHex(long value) {
		// BigInteger big;
		// if (value >= 0) {
		// big = BigInteger.valueOf(value);
		// } else {
		// BigInteger bigMax = BigInteger.valueOf(Long.MAX_VALUE).add(
		// BigInteger.valueOf(1L));
		// big = BigInteger.valueOf(value).add(bigMax).add(bigMax);
		// }
		String str = Long.toHexString(value);
		byte[] template = "0000000000000000".getBytes();
		System.arraycopy(str.getBytes(), 0, template, 16 - str.length(), str
				.length());
		return new String(template);
	}

	/**
	 * //align tetras or octs used in assembler
	 * 
	 * @param currentLocation
	 * @param bytes
	 * @return
	 */
	public static long alignRight(long currentLocation, int bytes) {
		int rem = (int) currentLocation % bytes;
		if (rem != 0) {
			currentLocation += (bytes - rem);
		}
		return currentLocation;
	}

	public static long alignLeft(long currentLocation, int bytes) {
		int rem = (int) currentLocation % bytes;
		return currentLocation - rem;
	}

	public static long alignTetraRight(long currentLocation) {
		return alignRight(currentLocation, 4);
	}

	public static long alignTetraLeft(long currentLocation) {
		return alignRight(currentLocation, 4);
	}

	public static BigInteger getUnsignedLong(long value) {
		if (value >= 0) {
			return BigInteger.valueOf(value);
		} else if (value == Long.MIN_VALUE) {
			return BigInteger.valueOf(Long.MAX_VALUE)
					.add(BigInteger.valueOf(1));
		} else {
			return BigInteger.valueOf(Long.MAX_VALUE).add(
					BigInteger.valueOf(Long.MAX_VALUE)).add(
					BigInteger.valueOf(2)).add(BigInteger.valueOf(value));
		}

	}

	public static BigInteger get2Power64() {
		return BigInteger.valueOf(Long.MAX_VALUE).add(
				BigInteger.valueOf(Long.MAX_VALUE)).add(BigInteger.valueOf(2));
	}

	public static int[] byteToBits(int value) {

		if (value < 0) {
			throw new ArithmeticException();
		}
		int[] bits = new int[8];
		for (int i = 7; i >= 0; i--) {
			bits[i] = (value >> (7 - i)) & 0x1;
		}
		return bits;
	}
}
