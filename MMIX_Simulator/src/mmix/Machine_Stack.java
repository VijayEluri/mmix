package mmix;


/**
 * <p>
 * Machine_Stack.java
 * </p>
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class Machine_Stack extends Machine {
	int stackTop = 0; // offset against stackBottom increase 8 every time.
	// 128K items
	int stackBottom = addressLimit - 1024 * 1024;//physical address of stack bottom

	/**
	 * only consider the memory backup not consider redundant hardware of local
	 * register
	 * 
	 * @param x
	 */

//	void push1(int x) {
//		if (x < specialRegister[L]) {
//			setOcta(stackBottom + stackTop + x * 8, x);
//			for (int i = 0; i < x; i++) {
//				setOcta(stackBottom + stackTop + i * 8, this
//						.getGeneralRegister(i));
//			}
//			for (int i = 0; i < specialRegister[L] - x - 1; i++) {
//				setGeneralRegister(i, getGeneralRegister(x + i + 1));
//			}
//			specialRegister[L] -= (x + 1);
//			stackTop += (x + 1) * 8;
//		} else {
//			setOcta(stackBottom + stackTop + (int) specialRegister[L] * 8,
//					(int) specialRegister[L]);
//			for (int i = 0; i < specialRegister[L]; i++) {
//				setOcta(stackBottom + stackTop + i * 8, this
//						.getGeneralRegister(i));
//			}
//
//			specialRegister[L] = 0;
//			stackTop += (specialRegister[L] + 1) * 8;
//		}
//	}
}
