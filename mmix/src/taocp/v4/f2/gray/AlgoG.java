package taocp.v4.f2.gray;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import util.MathUtil;

public class AlgoG extends GrayCode {
	public static List<Long> generate(int order) {
		List<Long> list = new ArrayList<Long>((int) Math.pow(2, order));

		checkParam(order);
		BigInteger tuple = BigInteger.valueOf(0L);
		//boolean[] a = new boolean[order];
		int j = 0;
		boolean parity = MathUtil.getOddParity(0);

		do {
			list.add(tuple.longValue());
			if (order < 7) {
				System.out.println(tuple.toString(2));
			}

			parity = !parity;// ^ true;

			if (parity) {
				j = 0;
			} else {
				j = Long.numberOfTrailingZeros(tuple.longValue()) + 1;
			}

			if (j == order) {
				break;
			} else {
				if (tuple.testBit(j)) {
					tuple = tuple.clearBit(j);
				} else {
					tuple = tuple.setBit(j);
				}
			}
		} while (true);

		return list;
	}
}
