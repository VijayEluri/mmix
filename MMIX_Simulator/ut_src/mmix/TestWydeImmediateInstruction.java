package mmix;

import static mmix.cons.ArithmeticExceptionConstant.V_BIT_SHIFT;
import static mmix.cons.InstructionConstant.*;
import static mmix.cons.SpecialRegisterConstant.A;
import junit.framework.TestCase;

/**
 * <p>
 * TestWydeImmediateInstructionExecutor.java
 * </p>
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class TestWydeImmediateInstruction extends TestMMIX {
	/**
	 * machine\.memory\[([^]]*)\] = ([^;]*);
	 * machine\.setByte\($1,$2\)
	 * set page 14at
	 */
	public void testWydeImmediate() {
		machine.virtualAt  = 0x100;
		int i = 0;
		machine.setByte(0x100,(byte) SETH);
		machine.setByte(i + 0x100 + 1,0x0);
		machine.setByte(i + 0x100 + 2,0x01);
		machine.setByte(i + 0x100 + 3,0x23);

		i += 4;
		machine.setByte(i + 0x100,(byte) INCMH);
		machine.setByte(i + 0x100 + 1,0x0);
		machine.setByte(i + 0x100 + 2,0x45);
		machine.setByte(i + 0x100 + 3,0x67);

		i += 4;
		machine.setByte(i + 0x100,(byte) INCML);
		machine.setByte(i + 0x100 + 1,0x0);
		machine.setByte(i + 0x100 + 2,(byte) 0x89);
		machine.setByte(i + 0x100 + 3,(byte) 0xab);

		i += 4;
		machine.setByte(i + 0x100,(byte) INCL);
		machine.setByte(i + 0x100 + 1,0x0);
		machine.setByte(i + 0x100 + 2,(byte) 0xcd);
		machine.setByte(i + 0x100 + 3,(byte) 0xef);

		machine.execute();
		System.out.println(Long.toBinaryString(machine.getGeneralRegister(0)));
		System.out.println(Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals("123456789abcdef", Long
				.toHexString(machine.getGeneralRegister(0)));
	}

}
