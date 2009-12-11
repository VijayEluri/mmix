package mmix;

import static mmix.cons.SpecialRegisterConstant.L;
import static mmix.cons.SpecialRegisterConstant.O;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import mmix.util.IOUtil;

/**
 * <p>
 * TestMMIX.java
 * </p>
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class TestProgramH extends TestMMIX {
	private Logger log = Logger.getLogger(this.getClass());
	static byte[] buffer = null;

	public void testProgramH() throws IOException {
		String fileName = "test/ProgramH.mmo";
		buffer = IOUtil.readInput(fileName);
		
		//IOUtil.showByteArrayInHex(machine.memory(), machine.memoryMap(0x100), 128);
		
		machine.setByteArray(0x100, buffer, 0, buffer.length);
		setupMain();
		
//		IOUtil.showByteArrayInHex(machine.memory(), machine.memoryMap(0x100), 128);
//		IOUtil.showByteArrayInHex(machine.memory(), machine.memoryMap(0x4000000000000000L), 128);
		
		machine.virtualAt  = 0x100;
		machine.execute();
	}

	private void setupMain() {
		machine.setGeneralRegister(0, 3);
		machine.setGeneralRegister(1, 0x4000000000000008L);
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

		machine.setOcta(0x4000000000000028L,
				0x666f6f0000000000L);// foo
		machine.setOcta(0x4000000000000030L,
				0x6261720000000000L);// bar
		machine.setOcta(0x4000000000000038L,
				0x78797a7900000000L);// xyzzy
		

	}

}
