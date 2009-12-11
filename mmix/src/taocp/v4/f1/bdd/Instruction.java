/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bdd;

/**
 * <p>Instruction.java
 * </p>
 */
class Instruction {
	int index; // index in the Instruction Array.

	int v; // varaiable index.

	int l; // instruction index when take low branch

	int h; // instruction index when take high branch

	public String toString() {
		return "I" + index + " = (" + v + " ? " + l + " : " + h + ")";
	}
}