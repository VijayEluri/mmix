/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v3.sort;

/**
 * no interface for static method can be made.
 * <p>
 * Sort.java
 * </p>
 */
public interface Sort {
	public abstract void sort(int[] a);

	public abstract void resetMetrics();

	public abstract Metrics getMetrics();
}
