package mmix;

import static mmix.cons.InstructionConstant.*;
import static mmix.cons.SpecialRegisterConstant.M;

/**
 * <p>
 * TestBranchInstruction.java
 * </p>
 * TODO: implement this easy one later.
 * if program go to endless loop. the case fail.
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class TestBranchInstruction extends TestMMIX {
	public void testBZ() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) BZ);
		machine.setByte(0x100 + 1, 0x0);
		machine.setByte(0x100 + 2, 0x1);
		machine.setByte(0x100 + 3, 0x2);

		// next instruction
		machine.setByte(0x100 + 4, (byte) BZB);
		machine.setByte(0x100 + 4 + 1, 0x0);
		machine.setByte(0x100 + 4 + 2, 0xff);
		machine.setByte(0x100 + 4 + 3, 0xff);

		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(0)));

	}
	
	public void testBZB() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) BZB);
		machine.setByte(0x100 + 1, 0x0);
		machine.setByte(0x100 + 2, 0x1);
		machine.setByte(0x100 + 3, 0x2);

		// next instruction
		machine.setByte(0x100 + 4, (byte) BZB);
		machine.setByte(0x100 + 4 + 1, 0x0);
		machine.setByte(0x100 + 4 + 2, 0xff);
		machine.setByte(0x100 + 4 + 3, 0xff);

		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(0)));

	}
}
