package mmix;

import java.io.IOException;

import mmix.util.IOUtil;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * <p>
 * TestSimMMO.java
 * </p>
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class TestSimMMO extends TestMMIX {
	Logger log = Logger.getLogger(this.getClass());
	static byte[] buffer = null;
	Loader loader = new Loader();
	// "mmix -Dhello.mmb hello world"
	// "mmix <options> sim hello.mmb"
	// [oracle@ora10gapp1 mmix]$ ./mmix sim hello.mmb world
	// hello, world

	String sim = "sim";
	String hello = "test/mmix/hello.mmb";
	String world = "world";

	public void test() {
		String fileName = "test/mmix/sim.mmo";
		try {
			buffer = IOUtil.readInput(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Level temp = log.getLevel();
		machine.log.setLevel(Level.ERROR);
		loader.load(machine, buffer);
		machine.log.setLevel(temp);
		
		machine.virtualAt = machine.getGlobalRegister(255);
		if(log.isInfoEnabled()){
		log.info("machine.virtualAt = 0x"
				+ Long.toHexString(machine.virtualAt));}

		log.debug("original mmo");
		IOUtil.showByteArrayInHex(buffer);

		log.debug("converted instruction in memory");
		machine.showMemory(log);

		log.debug("data seg");
		machine.showMemory(log, Machine.DATA_SEGMENT, 100);

		setupMain();
		log.debug("heap seg: pool set");
		machine.showMemory(log, Machine.POOL_SEGMENT, 100);

		machine.showInternalStatus(log);

		log.debug("arg 0 = " + machine.getStringByNull(0x4000000000000028L));
		log.debug("arg 1 = "
				+ machine.getStringByNull(0x4000000000000028L + sim.length() + 1));
		log.debug("arg 2 = "
				+ machine.getStringByNull(0x4000000000000028L + sim.length() + 1 + hello.length()+1));
		
		log.debug("arg 0 = " + machine.getStringByNull(machine.getSignedOcta(0x4000000000000008L)));
		log.debug("arg 1 = "
				+ machine.getStringByNull(machine.getSignedOcta(0x4000000000000008L + 8)));
		log.debug("arg 2 = "
				+ machine.getStringByNull(machine.getSignedOcta(0x4000000000000008L + 16)));
		machine.execute();
	}

	private void setupMain() {

		machine.setGeneralRegister(0, 3);
		machine.setGeneralRegister(1, 0x4000000000000008L);
		machine.setOcta(0x4000000000000008L, 0x4000000000000028L);
		machine.setOcta((0x4000000000000010L), 0x4000000000000028L + sim
				.length() + 1);
		machine.setOcta((0x4000000000000018L), 0x4000000000000028L
				+ sim.length() + 1 + hello.length() + 1);
		machine.setOcta((0x4000000000000020L), 0x0000000000000000L);

		log.debug(sim);
		log.debug(sim.length());
		log.debug(sim.getBytes().length);

		machine.setByteArray(0x4000000000000028L, sim.getBytes(), 0, sim
				.length());
		machine.setByte(0x4000000000000028L + sim.length(), 0);

		machine.setByteArray(0x4000000000000028L + sim.length() + 1, hello
				.getBytes(), 0, hello.length());
		machine.setByte(0x4000000000000028L + sim.length() + 1 + hello.length()
				+ 1, 0);

		machine.setByteArray(0x4000000000000028L + sim.length() + 1
				+ hello.length() + 1, world.getBytes(), 0, world.length());
		machine.setByte(0x4000000000000028L + sim.length() + 1 + hello.length()
				+ 1 + world.length() + 1, 0);
	}
}
