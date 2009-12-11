package taocp.v3.sort;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class IntArrayUtil {
	private static Logger log = Logger.getLogger(IntArrayUtil.class);

	public static String print(int[] inputs, Logger log) {
		StringBuffer buf = new StringBuffer("{ ");
		for (int i = 0; i < inputs.length - 1; i++) {
			buf.append(inputs[i]);
			buf.append(", ");
		}
		buf.append(inputs[inputs.length - 1]);
		buf.append(" }");
		if(log.isDebugEnabled()){
			log.debug(buf);
		}
		return buf.toString();
	}

	public static String print(int[][] inputs, Logger log) {
		StringBuffer buf = new StringBuffer("{ ");
		for (int i = 0; i < inputs.length - 1; i++) {
			buf.append(print(inputs[i], log));
			buf.append(", ");
		}
		// log.setLevel(Level.INFO);
		buf.append(print(inputs[inputs.length - 1], log));
		buf.append(" }");
		log.debug(buf);
		return buf.toString();
	}

	public static void checkArgument(int[] inputs) {
		if (inputs == null || inputs.length == 0) {
			throw new IllegalArgumentException();
		}
	}

	public static void checkArgument(int[] inputs, int start, int end) {
		if (inputs == null || start > end) {
			throw new IllegalArgumentException("start>end");
		}
	}

}
