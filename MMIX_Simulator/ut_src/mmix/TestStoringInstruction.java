package mmix;

import static mmix.cons.InstructionConstant.STB;
import static mmix.cons.InstructionConstant.STCO;
import static mmix.cons.InstructionConstant.STHT;
import static mmix.cons.InstructionConstant.STO;
import static mmix.cons.InstructionConstant.STT;
import static mmix.cons.InstructionConstant.STW;
import static mmix.cons.SpecialRegisterConstant.A;
/**
 * <p>
 * TestStoringInstruction.java
 * </p>
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class TestStoringInstruction extends TestMMIX {
	public void testSTB() {
		setUpMachineForExample1();
		machine.setByte(0x100, (byte) STB);
		System.out.println("machine.getSignedOcta(1000) = 0x"
				+ Long.toHexString(machine.getSignedOcta(1000)));
		machine.execute();
		System.out.println("machine.getSignedOcta(1000) = 0x"
				+ Long.toHexString(machine.getSignedOcta(1000)));
		assertEquals((0x123006789abcdefL), machine.getSignedOcta(1000));
		this.assertEquals(0x40, machine.getSpecialRegister(A));
	}

	public void testSTW() {
		setUpMachineForExample1();
		machine.setByte(0x100, (byte) STW);
		System.out.println("machine.getSignedOcta(1000) = 0x"
				+ Long.toHexString(machine.getSignedOcta(1000)));
		machine.execute();
		System.out.println("machine.getSignedOcta(1000) = 0x"
				+ Long.toHexString(machine.getSignedOcta(1000)));
		assertEquals((0x123000089abcdefL), machine.getSignedOcta(1000));
		this.assertEquals(0x40, machine.getSpecialRegister(A));
	}

	public void testSTT() {
		setUpMachineForExample1();
		machine.setByte(0x100, (byte) STT);
		machine.execute();
		System.out.println("machine.getSignedOcta(1000) = 0x"
				+ Long.toHexString(machine.getSignedOcta(1000)));
		assertEquals((0xffff000089abcdefL), machine.getSignedOcta(1000));
		this.assertEquals(0x0, machine.getSpecialRegister(A));
	}

	public void testSTO() {
		setUpMachineForExample1();
		machine.setByte(0x100, (byte) STO);
		machine.execute();
		System.out.println("machine.getSignedOcta(1000) = 0x"
				+ Long.toHexString(machine.getSignedOcta(1000)));
		assertEquals((0xffffffffffff0000L), machine.getSignedOcta(1000));
		this.assertEquals(0x0, machine.getSpecialRegister(A));
	}
	
	public void testSTHT(){
		setUpMachineForExample1();
		machine.setByte(0x100, (byte) STHT);
		machine.execute();
		System.out.println("machine.getSignedOcta(1000) = 0x"
				+ Integer.toHexString(machine.getSignedTetra(1000)));
		assertEquals((0xffffffff), machine.getSignedTetra(1000));
	}
	
	public void testSTCO(){
		setUpMachineForExample1();
		machine.setByte(0x100, (byte) STCO);
		System.out.println("machine.getSignedOcta(1000) = 0x"
				+ Long.toHexString(machine.getSignedOcta(1000)));
		machine.execute();
		System.out.println("machine.getSignedOcta(1000) = 0x"
				+ Long.toHexString(machine.getSignedOcta(1000)));
		assertEquals((0x123016789abcdefL), machine.getSignedOcta(1000));
	}
	
	// see page 6
	public void setUpMachineForExample1() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100 + 1, 0x1);
		machine.setByte(0x100 + 2, 0x2);
		machine.setByte(0x100 + 3, 0x3);
		machine.setGeneralRegister(1, 0xffffffffffff0000L);
		machine.setGeneralRegister(2, 1000);
		machine.setGeneralRegister(3, 2);
		machine.setOcta(1000, 0x0123456789abcdefL);
	}
}
