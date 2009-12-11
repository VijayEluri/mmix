/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package util;

import java.util.Collection;

/**
 * <p>
 * CollUtil.java
 * </p>
 */
public class CollUtil {
	public static void print(Collection<?> c) {
		// Collections.
		System.out.print("[");
		for (Object b : c) {
			System.out.print(b.toString() + ", ");
		}
		System.out.println("]");

	}
}
