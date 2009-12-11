package mmix;

import static mmix.cons.InstructionConstant.*;
import static mmix.cons.SpecialRegisterConstant.M;

/**
 * <p>
 * TestLoadingInstruction.java
 * </p>
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class TestLoadingInstruction extends TestMMIX {
	public void testLDB() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) LDB);
		machine.setByte(0x100 + 1, 0x0);
		machine.setByte(0x100 + 2, 0x1);
		machine.setByte(0x100 + 3, 0x2);
		machine.setGeneralRegister(1, 0x0102040810204080L);
		machine.setGeneralRegister(2, 0x0102040810204080L+7);
		machine.setOcta(0x0102040810204080L + 0x0102040810204080L, 0x0102040810204080L);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals("ffffffffffffff80", Long.toHexString(machine
				.getGeneralRegister(0)));
		System.out.println("getGeneralRegister(0) = "
				+ machine.getGeneralRegister(0));
		assertEquals(-128, machine
				.getGeneralRegister(0));
	}
	
	public void testLDBU() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) LDBU);
		machine.setByte(0x100 + 1, 0x0);
		machine.setByte(0x100 + 2, 0x1);
		machine.setByte(0x100 + 3, 0x2);
		machine.setGeneralRegister(1, 0x0102040810204080L);
		machine.setGeneralRegister(2, 0x0102040810204080L+7);
		machine.setOcta(0x0102040810204080L + 0x0102040810204080L, 0x0102040810204080L);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals("80", Long.toHexString(machine
				.getGeneralRegister(0)));
		System.out.println("getGeneralRegister(0) = "
				+ machine.getGeneralRegister(0));
		assertEquals(128, machine
				.getGeneralRegister(0));
	}
	
	public void testLDBI() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) LDBI);
		machine.setByte(0x100 + 1, 0x0);
		machine.setByte(0x100 + 2, 0x1);
		machine.setByte(0x100 + 3, 0x7);
		machine.setGeneralRegister(1, 0x0102040810204080L);
		//machine.setGeneralRegister(2, 0x0102040810204080L+7);
		machine.setOcta(0x0102040810204080L, 0x0102040810204080L);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals("ffffffffffffff80", Long.toHexString(machine
				.getGeneralRegister(0)));
		System.out.println("getGeneralRegister(0) = "
				+ machine.getGeneralRegister(0));
		assertEquals(-128, machine
				.getGeneralRegister(0));
	}
	
	public void testLDBUI() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) LDBUI);
		machine.setByte(0x100 + 1, 0x0);
		machine.setByte(0x100 + 2, 0x1);
		machine.setByte(0x100 + 3, 0x7);
		machine.setGeneralRegister(1, 0x0102040810204080L);
		//machine.setGeneralRegister(2, 0x0102040810204080L+7);
		machine.setOcta(0x0102040810204080L, 0x0102040810204080L);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals("80", Long.toHexString(machine
				.getGeneralRegister(0)));
		System.out.println("getGeneralRegister(0) = "
				+ machine.getGeneralRegister(0));
		assertEquals(128, machine
				.getGeneralRegister(0));
	}
	
	public void testLDW() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) LDW);
		machine.setByte(0x100 + 1, 0x0);
		machine.setByte(0x100 + 2, 0x1);
		machine.setByte(0x100 + 3, 0x2);
		machine.setGeneralRegister(1, 0x0102040810204080L);
		machine.setGeneralRegister(2, 0x0102040810204080L+6);
		machine.setOcta(0x0102040810204080L + 0x0102040810204080L, 0x0102040810208080L);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals("ffffffffffff8080", Long.toHexString(machine
				.getGeneralRegister(0)));
		System.out.println("getGeneralRegister(0) = "
				+ machine.getGeneralRegister(0));
		assertEquals((short)(0x8080), machine
				.getGeneralRegister(0));
	}
	
	public void testLDWU() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) LDWU);
		machine.setByte(0x100 + 1, 0x0);
		machine.setByte(0x100 + 2, 0x1);
		machine.setByte(0x100 + 3, 0x2);
		machine.setGeneralRegister(1, 0x0102040810204080L);
		machine.setGeneralRegister(2, 0x0102040810204080L+6);
		machine.setOcta(0x0102040810204080L + 0x0102040810204080L, 0x0102040810208080L);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals("8080", Long.toHexString(machine
				.getGeneralRegister(0)));
		System.out.println("getGeneralRegister(0) = "
				+ machine.getGeneralRegister(0));
		assertEquals((0x8080), machine
				.getGeneralRegister(0));
	}
	
	public void testLDWI() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) LDWI);
		machine.setByte(0x100 + 1, 0x0);
		machine.setByte(0x100 + 2, 0x1);
		machine.setByte(0x100 + 3, 0x6);
		machine.setGeneralRegister(1, 0x0102040810204080L);
		//machine.setGeneralRegister(2, 0x0102040810204080L+6);
		machine.setOcta(0x0102040810204080L, 0x0102040810208080L);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals("ffffffffffff8080", Long.toHexString(machine
				.getGeneralRegister(0)));
		System.out.println("getGeneralRegister(0) = "
				+ machine.getGeneralRegister(0));
		assertEquals((short)(0x8080), machine
				.getGeneralRegister(0));
	}
	
	public void testLDWUI() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) LDWUI);
		machine.setByte(0x100 + 1, 0x0);
		machine.setByte(0x100 + 2, 0x1);
		machine.setByte(0x100 + 3, 0x6);
		machine.setGeneralRegister(1, 0x0102040810204080L);
		//machine.setGeneralRegister(2, 0x0102040810204080L+6);
		machine.setOcta(0x0102040810204080L, 0x0102040810208080L);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals("8080", Long.toHexString(machine
				.getGeneralRegister(0)));
		System.out.println("getGeneralRegister(0) = "
				+ machine.getGeneralRegister(0));
		assertEquals(0x8080, machine
				.getGeneralRegister(0));
	}
	
	public void testLDT() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) LDT);
		machine.setByte(0x100 + 1, 0x0);
		machine.setByte(0x100 + 2, 0x1);
		machine.setByte(0x100 + 3, 0x2);
		machine.setGeneralRegister(1, 0x0102040810204080L);
		machine.setGeneralRegister(2, 0x0102040810204080L+4);
		machine.setOcta(0x0102040810204080L + 0x0102040810204080L, 0x0102040880208080L);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals("ffffffff80208080", Long.toHexString(machine
				.getGeneralRegister(0)));
		System.out.println("getGeneralRegister(0) = "
				+ machine.getGeneralRegister(0));
		assertEquals((0x80208080), machine
				.getGeneralRegister(0));
	}
	
	public void testLDTU() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) LDTU);
		machine.setByte(0x100 + 1, 0x0);
		machine.setByte(0x100 + 2, 0x1);
		machine.setByte(0x100 + 3, 0x2);
		machine.setGeneralRegister(1, 0x0102040810204080L);
		machine.setGeneralRegister(2, 0x0102040810204080L+4);
		machine.setOcta(0x0102040810204080L + 0x0102040810204080L, 0x0102040880208080L);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals("80208080", Long.toHexString(machine
				.getGeneralRegister(0)));
		System.out.println("getGeneralRegister(0) = "
				+ machine.getGeneralRegister(0));
		assertEquals((0x80208080L), machine
				.getGeneralRegister(0));
	}
	
	public void testLDTI() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) LDTI);
		machine.setByte(0x100 + 1, 0x0);
		machine.setByte(0x100 + 2, 0x1);
		machine.setByte(0x100 + 3, 0x4);
		machine.setGeneralRegister(1, 0x0102040810204080L);
		//machine.setGeneralRegister(2, 0x0102040810204080L+4);
		machine.setOcta(0x0102040810204080L , 0x0102040880208080L);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals("ffffffff80208080", Long.toHexString(machine
				.getGeneralRegister(0)));
		System.out.println("getGeneralRegister(0) = "
				+ machine.getGeneralRegister(0));
		assertEquals((0x80208080), machine
				.getGeneralRegister(0));
	}
	public void testLDTUI() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) LDTUI);
		machine.setByte(0x100 + 1, 0x0);
		machine.setByte(0x100 + 2, 0x1);
		machine.setByte(0x100 + 3, 0x4);
		machine.setGeneralRegister(1, 0x0102040810204080L);
		//machine.setGeneralRegister(2, 0x0102040810204080L+4);
		machine.setOcta(0x0102040810204080L , 0x0102040880208080L);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals("80208080", Long.toHexString(machine
				.getGeneralRegister(0)));
		System.out.println("getGeneralRegister(0) = "
				+ machine.getGeneralRegister(0));
		assertEquals((0x80208080L), machine
				.getGeneralRegister(0));
	}
	public void testLDHT() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) LDHT);
		machine.setByte(0x100 + 1, 0x0);
		machine.setByte(0x100 + 2, 0x1);
		machine.setByte(0x100 + 3, 0x2);
		machine.setGeneralRegister(1, 0x0102040810204080L);
		machine.setGeneralRegister(2, 0x0102040810204080L+4);
		machine.setOcta(0x0102040810204080L + 0x0102040810204080L, 0x0102040880208080L);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals("8020808000000000", Long.toHexString(machine
				.getGeneralRegister(0)));
		System.out.println("getGeneralRegister(0) = "
				+ machine.getGeneralRegister(0));
		assertEquals((0x8020808000000000L), machine
				.getGeneralRegister(0));
	}	
	public void testLDHTI() {
		machine.virtualAt = 0x100;
		machine.setByte(0x100, (byte) LDHTI);
		machine.setByte(0x100 + 1, 0x0);
		machine.setByte(0x100 + 2, 0x1);
		machine.setByte(0x100 + 3, 0x4);
		machine.setGeneralRegister(1, 0x0102040810204080L);
		//machine.setGeneralRegister(2, 0x0102040810204080L+4);
		machine.setOcta(0x0102040810204080L , 0x0102040880208080L);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(0)));
		assertEquals("8020808000000000", Long.toHexString(machine
				.getGeneralRegister(0)));
		System.out.println("getGeneralRegister(0) = "
				+ machine.getGeneralRegister(0));
		assertEquals((0x8020808000000000L), machine
				.getGeneralRegister(0));
	}
	
	public void testExample1() {
		setUpMachineForExample1();
		machine.setByte(0x100, (byte) LDB);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(1)));		
		System.out.println("getGeneralRegister(0) = "
				+ machine.getGeneralRegister(1));
		assertEquals((0x45), machine
				.getGeneralRegister(1));
		
		setUpMachineForExample1();
		machine.setByte(0x100, (byte) LDW);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(1)));		
		System.out.println("getGeneralRegister(0) = "
				+ machine.getGeneralRegister(1));
		assertEquals((0x4567), machine
				.getGeneralRegister(1));
		
		setUpMachineForExample1();
		machine.setByte(0x100, (byte) LDT);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(1)));		
		System.out.println("getGeneralRegister(0) = "
				+ machine.getGeneralRegister(1));
		assertEquals((0x1234567), machine
				.getGeneralRegister(1));
		
		setUpMachineForExample1();
		machine.setByte(0x100, (byte) LDO);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(1)));		
		System.out.println("getGeneralRegister(0) = "
				+ machine.getGeneralRegister(1));
		assertEquals((0x123456789abcdefL), machine
				.getGeneralRegister(1));
		
	}
	
	public void testExample2() {
		setUpMachineForExample2();
		machine.setByte(0x100, (byte) LDB);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(1)));		
		System.out.println("getGeneralRegister(0) = "
				+ machine.getGeneralRegister(1));
		assertEquals((byte)(0xab), machine
				.getGeneralRegister(1));
		
		setUpMachineForExample2();
		machine.setByte(0x100, (byte) LDW);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(1)));		
		System.out.println("getGeneralRegister(0) = "
				+ machine.getGeneralRegister(1));
		assertEquals((short)(0x89ab), machine
				.getGeneralRegister(1));
		
		setUpMachineForExample2();
		machine.setByte(0x100, (byte) LDT);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(1)));		
		System.out.println("getGeneralRegister(0) = "
				+ machine.getGeneralRegister(1));
		assertEquals((int)(0x89abcdef), machine
				.getGeneralRegister(1));
		
		setUpMachineForExample2();
		machine.setByte(0x100, (byte) LDO);
		machine.execute();
		System.out.println("getGeneralRegister(0) = 0x"
				+ Long.toHexString(machine.getGeneralRegister(1)));		
		System.out.println("getGeneralRegister(0) = "
				+ machine.getGeneralRegister(1));
		assertEquals((0x123456789abcdefL), machine
				.getGeneralRegister(1));
		
	}
	
	//see page 6
	public void setUpMachineForExample1(){
		machine.virtualAt = 0x100;		
		machine.setByte(0x100 + 1, 0x1);
		machine.setByte(0x100 + 2, 0x2);
		machine.setByte(0x100 + 3, 0x3);
		machine.setGeneralRegister(2, 1000);
		machine.setGeneralRegister(3, 2);
		machine.setOcta(1000, 0x0123456789abcdefL);
	}
//see page 7 of MMIX: a new RISC computer for new millennium
	public void setUpMachineForExample2(){
		machine.virtualAt = 0x100;		
		machine.setByte(0x100 + 1, 0x1);
		machine.setByte(0x100 + 2, 0x2);
		machine.setByte(0x100 + 3, 0x3);
		machine.setGeneralRegister(2, 1000);
		machine.setGeneralRegister(3, 5);
		machine.setOcta(1000, 0x0123456789abcdefL);
	}
}

