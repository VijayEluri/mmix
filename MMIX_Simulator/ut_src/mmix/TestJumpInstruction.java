package mmix;

import static mmix.cons.InstructionConstant.*;

/**
 * <p>TestJumpInstruction.java
 * </p>
						
 * 
 * @author Eddie Wu 
 * @version 1.0
 * 
 */
public class TestJumpInstruction extends TestMMIX{
	public void testJMP() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) JMP);
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
	
	public void testJMPB() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) JMPB);
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
