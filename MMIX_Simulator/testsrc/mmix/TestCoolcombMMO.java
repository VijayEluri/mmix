package mmix;

import java.io.IOException;

import mmix.util.IOUtil;

import org.apache.log4j.Logger;

/**
 * <p>
 * TestCopyMMO.java
 * </p>
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class TestCoolcombMMO extends TestMMIX {
	Logger log = Logger.getLogger(this.getClass());
	static byte[] buffer = null;
	Loader loader = new Loader();
	
//	public void testExceptionFlow(){
//		String copyedfile="test/mmix/copy.mmss";
//		test(copyedfile);
//	}

	public void testNomalFlow(){
		String copyedfile="test/mmix/copy.mms";
		test(copyedfile);
	}
	
	public void test(String copyedFile) {
		String fileName = "test/mmix/coolcomb.mmo";
		try {
			buffer = IOUtil.readInput(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		loader.load(machine, buffer);
		machine.virtualAt = machine.getGlobalRegister(255);
		log.debug("machine.virtualAt = 0x"
				+ Long.toHexString(machine.virtualAt));

		log.debug("original mmo");
		IOUtil.showByteArrayInHex(buffer);

		log.debug("converted instruction in memory");
		machine.showMemory(log);

		log.debug("data seg");
		machine.showMemory(log, Machine.DATA_SEGMENT, 100);

		//setupMain(copyedFile);
		log.debug("pool set: heap");
		machine.showMemory(log, Machine.POOL_SEGMENT, 100);

		machine.showInternalStatus(log);

		
		machine.execute();
	}

	private void setupMain(String copyedfile) {
		String program = "copy";
		String from = copyedfile;
		// from.getBytes()
		// String to = "test/mmix/copy.mmo.copy";

		machine.setGeneralRegister(0, 2);
		machine.setGeneralRegister(1, 0x4000000000000008L);
		machine.setOcta(0x4000000000000008L, 0x4000000000000028L);
		machine.setOcta((0x4000000000000010L), 0x4000000000000028L + program
				.length() + 1);
		machine.setOcta((0x4000000000000018L), 0x0000000000000000L);

		log.debug(program);
		log.debug(program.length());
		log.debug(program.getBytes().length);

		machine.setByteArray(0x4000000000000028L, program.getBytes(), 0,
				program.length());
		machine.setByte(0x4000000000000028L + program.length(), 0);
		machine.setByteArray(0x4000000000000028L + program.length() + 1, from
				.getBytes(), 0, from.length());
		machine.setByte(0x4000000000000028L + program.length() + 1
				+ from.length() + 1, 0);
	}
}
