package mmix;

import static mmix.cons.SpecialRegisterConstant.L;
import mmix.util.IOUtil;
import junit.framework.TestCase;

/**
 * <p>
 * TestSubroutineInstruction.java
 * </p>
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class TestSubroutineInstruction extends TestMMIX {
	static byte[] buffer = null;

	public void testSubroutineInstruction() {
		try {
			String fileName = "test/ProgramM.mmo";
			buffer = IOUtil.readInput(fileName);

			// to ensure the mappoing
			//machine.memoryMap(0x300);

			for (int i = 0; i < buffer.length; i++) {
				machine.setByteArray(0x300, buffer, 0, buffer.length);
			}
			setupDataSegment();
//			IOUtil.showByteArrayInHex(machine.memory(), machine.memoryMap(0x300),
//					128);
//			IOUtil.showByteArrayInHex(machine.memory(), machine
//					.memoryMap(0x4000000000000000L), 128);

			int iii = 0;
			

			// add calling sequence
			byte[] value = new byte[16];
			//set $2 , 4
			value[iii + 0] = (byte) 0xe3;
			value[iii + 1] = 2;
			value[iii + 2] = 0;
			value[iii + 3] = (byte) 4;
			machine.setTetra(0x100 + iii, value, iii);

			//pushJ $1, Maximum		
			iii += 4;
			value[iii + 0] = (byte) 0xf2;
			value[iii + 1] = 1;
			value[iii + 2] = 0;
			value[iii + 3] = 127;
			machine.setTetra(0x100 + iii, value, iii);
			
			//STO $1, Max
//			iii += 4;
//			value[iii + 0] = (byte)0xAC;
//			value[iii + 1] = 1;
//			value[iii + 2] = 0;
//			value[iii + 3] = 0;
//			machine.setTetra(machine.memoryMap(0x100 + iii), value, 0);
			
			iii += 4;
			value[iii + 0] = 0;
			value[iii + 1] = 0;
			value[iii + 2] = 0;
			value[iii + 3] = 0;
			machine.setTetra(0x100 + iii, value, iii);
			//

//			IOUtil.showByteArrayInHex(machine.memory(), machine.memoryMap(0x100),
//					128);

			machine.virtualAt = 0x100;

			System.out.println("machine.getGeneralRegister(0)= "
					+ machine.getGeneralRegister(0));
			System.out.println("machine.getGeneralRegister(1)= "
					+ Long.toHexString(machine.getGeneralRegister(1)));
			System.out.println("machine.getGeneralRegister(2)= "
					+ Long.toHexString(machine.getGeneralRegister(2)));
			
			machine.execute();

			System.out.println("machine.getGeneralRegister(0)= "
					+ machine.getGeneralRegister(0));
			System.out.println("machine.getGeneralRegister(1)= "
					+ Long.toHexString(machine.getGeneralRegister(1)));
			System.out.println("machine.getGeneralRegister(2)= "
					+ Long.toHexString(machine.getGeneralRegister(2)));
			
			assertEquals(0, machine.getGeneralRegister(0));
			assertEquals(0x4000000000000101L, machine
					.getGeneralRegister(1));
			assertEquals(3, machine
					.getGeneralRegister(2));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void setupDataSegment() {
		
		machine.setGlobalRegister(0xfe, 0x4000000000000000L);
		machine.setOcta(0x4000000000000000L,
				0x4000000000000099L);// x0 is useless.
		machine.setOcta(0x4000000000000008L,
				0x4000000000000028L);
		machine.setOcta(0x4000000000000010L,
				0x4000000000000030L);
		machine.setOcta(0x4000000000000018L,
				0x4000000000000101L);
		machine.setOcta(0x4000000000000020L,
				0x0000000000000048L);
		// sort 0x4000000000000030L;0x4000000000000038L
		// ;0x4000000000000048L

		System.out.println("machine.getSpecialRegister(L);= "
				+ machine.getSpecialRegister(L));

	}

}
