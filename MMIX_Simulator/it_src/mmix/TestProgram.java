package mmix;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

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
public class TestProgram extends TestMMIX {

	static byte[] buffer = null;

	public void testProgramH() throws IOException {
		String fileName = "test\\ProgramH.mmo";
		readInput(fileName);
		machine.virtualAt  = 0x100;
		machine.setByteArray(0x100, buffer, 0, buffer.length);
		setupMain();
		machine.execute();
	}

	private void setupMain() {
		machine.setGeneralRegister(0, 3); 
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

	}

	public static void readInput(String fileName) throws IOException {
		File file = new File(fileName);
		FileInputStream ins = new FileInputStream(file);
		buffer = new byte[(int) file.length()];
		ins.read(buffer);
		ins.close();
	}
}
