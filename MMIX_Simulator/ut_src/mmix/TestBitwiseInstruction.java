package mmix;

import static mmix.cons.InstructionConstant.MUX;
import static mmix.cons.InstructionConstant.MUXI;
import static mmix.cons.InstructionConstant.SADD;
import static mmix.cons.InstructionConstant.SADDI;
import static mmix.cons.SpecialRegisterConstant.M;

/**
 * <p>
 * TestBitwiseInstruction.java
 * </p>
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class TestBitwiseInstruction extends TestMMIX {
	public void testMUX() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) MUX);
		machine.setByte(0x100 + 1, 0x0);
		machine.setByte(0x100 + 2, 0x1);
		machine.setByte(0x100 + 3, 0x2);
		machine.setGeneralRegister(1, 0x0102040810204080L);
		machine.setGeneralRegister(2, 0x0102040810204080L);
		machine.setSpecialRegister(M,0x0102040810204080L);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals("102040810204080", Long.toHexString(machine
				.getGeneralRegister(0)));
	}
	public void testMUXI() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) MUXI);
		machine.setByte(0x100 + 1, 0x0);
		machine.setByte(0x100 + 2, 0x1);
		machine.setByte(0x100 + 3, 0xff);
		machine.setGeneralRegister(1, 0x0102040810204080L);		
		machine.setSpecialRegister(M,0x0102040810204080L);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals("1020408102040ff", Long.toHexString(machine
				.getGeneralRegister(0)));
	}
	public void testSADD() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) SADD);
		machine.setByte(0x100 + 1, 0x0);
		machine.setByte(0x100 + 2, 0x1);
		machine.setByte(0x100 + 3, 0x2);
		machine.setGeneralRegister(1, 0x0102040810204080L);
		machine.setGeneralRegister(2, 0x0102040810204080L);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals("0", Long.toHexString(machine.getGeneralRegister(0)));
	}
	
	public void testSADDI() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) SADDI);
		machine.setByte(0x100 + 1, 0x0);
		machine.setByte(0x100 + 2, 0x1);
		machine.setByte(0x100 + 3, 0xff);
		machine.setGeneralRegister(1, 0x0102040810204080L);
		
		machine.setSpecialRegister(M,0x0102040810204080L);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals("7", Long.toHexString(machine.getGeneralRegister(0)));
	}
	
}
