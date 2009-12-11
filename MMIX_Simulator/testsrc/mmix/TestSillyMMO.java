package mmix;

import java.io.IOException;

import mmix.util.IOUtil;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * <p>
 * TestCopyMMO.java
 * </p>
 *  ./mmix -P copy copy.mms
 *
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class TestSillyMMO extends TestMMIX {
	Logger log = Logger.getLogger(this.getClass());
	static byte[] buffer = null;
	Loader loader = new Loader();
	

	
	public void test() {
		//Logger.getRootLogger().setLevel(Level.ERROR);
		
		String fileName = "test/mmix/silly.mmo";
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

		setupMain();
		log.debug("pool set: heap");
		machine.showMemory(log, Machine.POOL_SEGMENT, 100);

		machine.showInternalStatus(log);

		log.debug("arg 0 = " + machine.getStringByNull(0x4000000000000028L));		
		machine.execute();
	}

	private void setupMain() {
		String program = "test/mmix/silly.mmo";
		
		machine.setGeneralRegister(0, 1);
		machine.setGeneralRegister(1, 0x4000000000000008L);
		machine.setOcta(0x4000000000000008L, 0x4000000000000028L);
		machine.setOcta((0x4000000000000010L),  0x0000000000000000L);
		

		machine.setByteArray(0x4000000000000028L, program.getBytes(), 0,
				program.length());
		machine.setByte(0x4000000000000028L + program.length(), 0);
		
	}
}
