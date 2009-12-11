package mmix;

import mmix.cons.InstructionConstant;
import junit.framework.TestCase;
import static mmix.cons.ArithmeticExceptionConstant.*;
import static mmix.cons.InstructionConstant.*;
import static mmix.cons.SpecialRegisterConstant.*;

/**
 * JUnit Test Case for all Arithmetic Instruction
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class TestArithmeticInstruction extends TestMMIX {

	public void testAdd1() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100,0x20);
		machine.setByte(0x100 + 1,0x0);
		machine.setByte(0x100 + 2,0x1);
		machine.setByte(0x100 + 3,0x2);
		machine.setGeneralRegister(1, Long.MAX_VALUE);
		machine.setGeneralRegister(2, Long.MAX_VALUE);
		machine.execute();
		System.out.println(Long.toBinaryString(machine.getSpecialRegister(A)));
		assertEquals(true, machine.isArithmeticException(V_BIT_SHIFT));
	}

	public void testAdd2() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100,0x20);
		machine.setByte(0x100 + 1,0x0);
		machine.setByte(0x100 + 2,0x1);
		machine.setByte(0x100 + 3,0x2);
		machine.setGeneralRegister(1, Long.MIN_VALUE);
		machine.setGeneralRegister(2, Long.MIN_VALUE);
		machine.execute();
		System.out.println(Long.toBinaryString(machine.getSpecialRegister(A)));
		assertEquals(true, machine.isArithmeticException(V_BIT_SHIFT));
	}

	public void testMultiply() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100,0x20);
		machine.setByte(0x100 + 1,0x0);
		machine.setByte(0x100 + 2,0x1);
		machine.setByte(0x100 + 3,0x2);
		machine.setGeneralRegister(1, Long.MIN_VALUE);
		machine.setGeneralRegister(2, Long.MIN_VALUE);
		machine.execute();
		System.out.println(Long.toBinaryString(machine.getSpecialRegister(A)));
		assertEquals(true, machine.isArithmeticException(V_BIT_SHIFT));
	}

	public void testMultiply2() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100,0x20);
		machine.setByte(0x100 + 1,0x0);
		machine.setByte(0x100 + 2,0x1);
		machine.setByte(0x100 + 3,0x2);
		machine.setGeneralRegister(1, Integer.MIN_VALUE);
		machine.setGeneralRegister(2, Integer.MIN_VALUE);
		machine.execute();
		System.out.println(Long.toBinaryString(machine.getSpecialRegister(A)));
		assertEquals(false, machine.isArithmeticException(V_BIT_SHIFT));
	}

	public void testDIV() {

		machine.setByte(0x100,InstructionConstant.DIV);// 
		machine.setByte(0x100 + 1,0x0);
		machine.setByte(0x100 + 2,0x1);
		machine.setByte(0x100 + 3,0x2);
		machine.virtualAt = 0x100;
		machine.setGeneralRegister(1, Long.MAX_VALUE);
		machine.setGeneralRegister(2, Integer.MAX_VALUE);
		machine.execute();
		System.out.println(Long.toBinaryString(machine.getSpecialRegister(R)));
		System.out.println(Long.toBinaryString(machine.getGeneralRegister(0)));
		assertEquals(false, machine.isArithmeticException(V_BIT_SHIFT));

		machine.virtualAt = 0x100;
		machine.setGeneralRegister(1, Long.MAX_VALUE);
		machine.setGeneralRegister(2, Integer.MIN_VALUE);
		machine.execute();
		System.out.println(Long.toBinaryString(machine.getSpecialRegister(R)));
		System.out.println(Long.toBinaryString(machine.getGeneralRegister(0)));
		assertEquals(false, machine.isArithmeticException(V_BIT_SHIFT));

		machine.virtualAt = 0x100;
		machine.setGeneralRegister(1, Long.MIN_VALUE);
		machine.setGeneralRegister(2, Integer.MAX_VALUE);
		machine.execute();
		System.out.println(Long.toBinaryString(machine.getSpecialRegister(R)));
		System.out.println(Long.toBinaryString(machine.getGeneralRegister(0)));
		assertEquals(false, machine.isArithmeticException(V_BIT_SHIFT));

		machine.virtualAt = 0x100;
		machine.setGeneralRegister(1, Long.MIN_VALUE);
		machine.setGeneralRegister(2, Integer.MIN_VALUE);
		machine.execute();
		System.out.println(Long.toBinaryString(machine.getSpecialRegister(R)));
		System.out.println(Long.toBinaryString(machine.getGeneralRegister(0)));
		assertEquals(false, machine.isArithmeticException(V_BIT_SHIFT));
	}
	
	public void testDIV2() {

		machine.setByte(0x100,InstructionConstant.DIV);// 
		machine.setByte(0x100 + 1,0x0);
		machine.setByte(0x100 + 2,0x1);
		machine.setByte(0x100 + 3,0x2);
		machine.virtualAt = 0x100;
		machine.setGeneralRegister(1, 3);
		machine.setGeneralRegister(2, -2);
		machine.execute();
		System.out.println(Long.toHexString(machine.getSpecialRegister(R)));
		System.out.println(Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals(-2,machine.getGeneralRegister(0));
		assertEquals(-1,machine.getSpecialRegister(R));
		assertEquals(false, machine.isArithmeticException(V_BIT_SHIFT));
		
		machine.virtualAt = 0x100;
		machine.setGeneralRegister(1, -3);
		machine.setGeneralRegister(2, 2);
		machine.execute();
		System.out.println(Long.toHexString(machine.getSpecialRegister(R)));
		System.out.println(Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals(-2,machine.getGeneralRegister(0));
		assertEquals(1,machine.getSpecialRegister(R));
		assertEquals(false, machine.isArithmeticException(V_BIT_SHIFT));
		
		machine.virtualAt = 0x100;
		machine.setGeneralRegister(1, -3);
		machine.setGeneralRegister(2, -2);
		machine.execute();
		System.out.println(Long.toHexString(machine.getSpecialRegister(R)));
		System.out.println(Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals(1,machine.getGeneralRegister(0));
		assertEquals(-1,machine.getSpecialRegister(R));
		assertEquals(false, machine.isArithmeticException(V_BIT_SHIFT));
	}
	
	public void test2AddU(){
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) _2ADDU);
		machine.setByte(0x100 + 1, 0x1);
		machine.setByte(0x100 + 2, 0x2);
		machine.setByte(0x100 + 3, 0x3);
		machine.setGeneralRegister(2, Long.MAX_VALUE);
		machine.setGeneralRegister(3, 2);		
		machine.execute();
		System.out.println("getGeneralRegister(1) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(1)));
		assertEquals("0", Long.toHexString(machine
				.getGeneralRegister(1)));
		
		machine.virtualAt = 0x100;
		machine.setGeneralRegister(2, Long.MIN_VALUE);
		machine.execute();
		System.out.println("getGeneralRegister(1) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(1)));
		assertEquals("2", Long.toHexString(machine
				.getGeneralRegister(1)));
	}
	
	public void test4AddU(){
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) _4ADDU);
		machine.setByte(0x100 + 1, 0x1);
		machine.setByte(0x100 + 2, 0x2);
		machine.setByte(0x100 + 3, 0x3);
		machine.setGeneralRegister(2, Long.MAX_VALUE);
		machine.setGeneralRegister(3, 2);		
		machine.execute();
		System.out.println("getGeneralRegister(1) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(1)));
		assertEquals(-2,(machine
				.getGeneralRegister(1)));
		
		machine.virtualAt = 0x100;
		machine.setGeneralRegister(2, Long.MIN_VALUE);
		machine.execute();
		System.out.println("getGeneralRegister(1) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(1)));
		assertEquals("2", Long.toHexString(machine
				.getGeneralRegister(1)));
	}
	
	public void test8AddU(){
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) _8ADDU);
		machine.setByte(0x100 + 1, 0x1);
		machine.setByte(0x100 + 2, 0x2);
		machine.setByte(0x100 + 3, 0x3);
		machine.setGeneralRegister(2, Long.MAX_VALUE);
		machine.setGeneralRegister(3, 2);		
		machine.execute();
		System.out.println("getGeneralRegister(1) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(1)));
		assertEquals(-6,(machine
				.getGeneralRegister(1)));
		
		machine.virtualAt = 0x100;
		machine.setGeneralRegister(2, Long.MIN_VALUE);
		machine.execute();
		System.out.println("getGeneralRegister(1) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(1)));
		assertEquals("2", Long.toHexString(machine
				.getGeneralRegister(1)));
	}

	public void test16AddU(){
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) _16ADDU);
		machine.setByte(0x100 + 1, 0x1);
		machine.setByte(0x100 + 2, 0x2);
		machine.setByte(0x100 + 3, 0x3);
		machine.setGeneralRegister(2, Long.MAX_VALUE);
		machine.setGeneralRegister(3, 2);		
		machine.execute();
		System.out.println("getGeneralRegister(1) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(1)));
		assertEquals(-14,(machine
				.getGeneralRegister(1)));
		
		machine.virtualAt = 0x100;
		machine.setGeneralRegister(2, Long.MIN_VALUE);
		machine.execute();
		System.out.println("getGeneralRegister(1) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(1)));
		assertEquals("2", Long.toHexString(machine
				.getGeneralRegister(1)));
	}

	public void testOverflow() {
		boolean a;
		boolean b;
		for (int i = 0 - 128; i < 256 - 128; i++) {
			for (int j = 0 - 128; j < 256 - 128; j++) {
				a = ((i + j) > 127) || ((i + j) < -128);
				b = ArithmeticInstructionExecutor.addOverflow((byte)i, (byte)j);
				assertEquals(a, b);

			}
		}
	}
}
