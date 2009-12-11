package mmix;

import static mmix.cons.InstructionConstant.*;

/**
 * <p>TestConditionalInstruction.java
 * </p>
						
 * 
 * @author Eddie Wu 
 * @version 1.0
 * 
 */
public class TestConditionalInstruction extends TestMMIX {
	public void testBZ() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) CSZ);
		machine.setByte(0x100 + 1, 0x0);
		machine.setByte(0x100 + 2, 0x1);
		machine.setByte(0x100 + 3, 0x2);
		machine.setGeneralRegister(2, 0x100);
		
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(0)));
		this.assertEquals(0x100, machine.getGeneralRegister(0));
	}
}
