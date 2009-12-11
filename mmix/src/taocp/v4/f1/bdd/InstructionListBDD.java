/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bdd;

/**
 * <p>
 * use Instruction List to represent the BDD
 * </p>
 */
public class InstructionListBDD {
	int numOfVar;
	Instruction[] insts;
	
	/**
	 * Evaluate the function based on the input. consider the i (index) in the
	 * nodes are not consecutive.
	 */
	public boolean eval(boolean[] input) {
		int n = insts.length;
		Instruction temp = insts[0];
		while(temp.v<=numOfVar){
			if(input[temp.v-1]==true){
				temp = insts[temp.h];
			}else{
				temp = insts[temp.l];
			}
		}
		if(temp.l==0){
			return false;
		}
		return true;
	}
}
