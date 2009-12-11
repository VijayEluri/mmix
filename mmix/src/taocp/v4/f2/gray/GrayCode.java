package taocp.v4.f2.gray;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * GrayCode.java
 * </p>
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class GrayCode {
	public static final String ALGO_G = "AlgoG";

	public static int count(int order) {
		checkParam(order);
		return (int) Math.pow(2, order);
	}

	/**
	 * how to traverse all the sequence with the gray code property - only one
	 * bit change between consecutive (adjacent) number.
	 * 
	 * @param order
	 * @return
	 */
	// public static int countOfGrayCodeSequence(int order){
	// int a = Integer.parseInt("0001", 2);
	// int b = Integer.parseInt("0010", 2);
	// int c = Integer.parseInt("0100", 2);
	// int d = Integer.parseInt("1000", 2);
	// int[] mask = new int[4];
	// mask[0] = a;
	// mask[1] = b;
	// mask[2] = c;
	// mask[3] = d;
	//		
	// checkParam(order);
	// int count = (int)Math.pow(2, order);
	// int start = 0;
	// Set<Integer> temp = new HashSet<Integer>(count);temp.add(start);
	// while (true){
	//		
	// start ^=
	// temp
	// }
	// return 0;
	// }
	public static List<Long> generate(String algoName, int order) {
		if(algoName.equalsIgnoreCase(ALGO_G)){
			return AlgoG.generate(order);	
		}else{
			return generate( order);
		}
	}
	
	public static List<Long> generate(int order) {
		checkParam(order);
		int length = (int) Math.pow(2, order);
		List<Long> list = new ArrayList<Long>(length);
		for (long i = 0; i < length; i++) {
			/**
			 * see page 4.
			 * g(i) = i ^ (i/2)
			 */
			list.add(i ^ (i >>> 1));
		}
		return list;
	}

	protected static void checkParam(int order) {
		if (order >= 31) {
			throw new InvalidParameterException(
					"order should be strictly less than 31");
		}
	}
}
