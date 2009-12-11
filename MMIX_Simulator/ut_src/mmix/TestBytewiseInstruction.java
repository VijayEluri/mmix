package mmix;

import static mmix.cons.ArithmeticExceptionConstant.V_BIT_SHIFT;
import static mmix.cons.InstructionConstant.*;
import static mmix.cons.SpecialRegisterConstant.A;

/**
 * <p>
 * TestBitwiseInstructionExecutor.java
 * </p>
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class TestBytewiseInstruction extends TestMMIX {
	public void testBDIF() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100,(byte) BDIF);
		machine.setByte(0x100 + 1,0x0);
		machine.setByte(0x100 + 2,0x1);
		machine.setByte(0x100 + 3,0x2);
		machine.setGeneralRegister(1, 0x0102040810204080L);
		machine.setGeneralRegister(2, 0x0102040810204080L);
		machine.execute();
		System.out.println(Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals("0", Long.toHexString(machine.getGeneralRegister(0)));
		
		machine.virtualAt = 0x100;
		machine.setGeneralRegister(1, 0x0010204081020408L);
		machine.setGeneralRegister(2, 0x0102040810204080L);
		machine.execute();
		System.out.println(Long.toHexString(machine.getGeneralRegister(0)));
		//"000e1c3871000000"
		assertEquals("e1c3871000000", Long.toHexString(machine.getGeneralRegister(0)));
	}
	
	public void testWDIF() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100,(byte) WDIF);
		machine.setByte(0x100 + 1,0x0);
		machine.setByte(0x100 + 2,0x1);
		machine.setByte(0x100 + 3,0x2);
		machine.setGeneralRegister(1, 0x0102040810204080L);
		machine.setGeneralRegister(2, 0x0102040810204080L);
		machine.execute();
		System.out.println(Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals("0", Long.toHexString(machine.getGeneralRegister(0)));
		
		machine.virtualAt = 0x100;
		machine.setGeneralRegister(1, 0x0010204081020408L);
		machine.setGeneralRegister(2, 0x0102040810204080L);
		machine.execute();
		System.out.println(Long.toHexString(machine.getGeneralRegister(0)));
		//"000e1c3871000000"
		assertEquals("1c3870e20000", Long.toHexString(machine.getGeneralRegister(0)));
	}
	public void testMOR() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100,(byte) MOR);
		machine.setByte(0x100 + 1,0x0);
		machine.setByte(0x100 + 2,0x1);
		machine.setByte(0x100 + 3,0x2);
		machine.setGeneralRegister(1, 0x0102040810204080L);
		machine.setGeneralRegister(2, 0x0102040810204080L);
		machine.execute();
		System.out.println(Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals("8040201008040201", Long.toHexString(machine.getGeneralRegister(0)));
	}
	public void testMXOR() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100,(byte) MXOR);
		machine.setByte(0x100 + 1,0x0);
		machine.setByte(0x100 + 2,0x1);
		machine.setByte(0x100 + 3,0x2);
		machine.setGeneralRegister(1, 0x0102040810204080L);
		machine.setGeneralRegister(2, 0x0102040810204080L);
		machine.execute();
		System.out.println(Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals("8040201008040201", Long.toHexString(machine.getGeneralRegister(0)));
	}
}
