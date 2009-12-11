package mmix;

/**
 * test against test.mmo from mmixware.
 */
import static mmix.cons.SpecialRegisterConstant.G;
import static mmix.cons.SpecialRegisterConstant.L;
import static mmix.cons.SpecialRegisterConstant.O;
import static mmix.cons.SpecialRegisterConstant.S;

import java.io.IOException;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import mmix.util.IOUtil;

/**
 * <p>
 * TestTestMMO.java
 * </p>
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class TestTestMMO extends TestMMIX {
	Logger log = Logger.getLogger(this.getClass());
	static byte[] buffer = null;
	Loader loader = new Loader();

	/**
	 * make sure we can load the mmo file and finish conversion correctly.
	 * 
	 * @throws IOException
	 */
	public void test() throws IOException {
		String fileName = "test/mmix/test.mmo";
		buffer = IOUtil.readInput(fileName);
		log.debug("the content read from mmo file");
		IOUtil.showByteArrayInHex(buffer);

		try {
			loader.load(machine, buffer);
		} catch (Exception e) {
			log.debug(e.getMessage());
			e.printStackTrace();
		}

		log.debug("the converted instruction");
		// data segment 1
		log.debug(Long.toHexString(Machine.DATA_SEGMENT));
		log.debug((Machine.DATA_SEGMENT));
		// mmix require a full-fledged virtual memory mapping system.
//		IOUtil.showByteArrayInHex(machine.memory(), machine
//				.memoryMap(Machine.DATA_SEGMENT), 100);

		// fix me: hack
		// instruction segment - 1
		log.debug(Long.toHexString(0x12345678cL));
		log.debug((0x12345678cL));
		// mmix require a full-fledged virtual memory mapping system.
//		IOUtil.showByteArrayInHex(machine.memory(), machine
//				.memoryMap(0x12345678cL), 100);

		// // instruction segment - 2
		// log.debug(Long.toHexString(0x12345678cL + 16 * 1024));
		// log.debug((0x12345678cL + 16 * 1024));
		//
		// IOUtil.showByteArrayInHex(machine.memory(), machine
		// .memoryMap( 0x12345678cL + 16 * 1024),
		// 100);

		// instruction segment - 3
		log.debug(Long.toHexString(0x12345a768L));
		log.debug((0x12345a768L));

//		IOUtil.showByteArrayInHex(machine.memory(), machine
//				.memoryMap(0x12345a768L), 100);

		// TODO identify the location of main
		machine.virtualAt = (0x12345678cL);
		setupMain();
		// IOUtil.showByteInHex(machine.memory(),
		// machine.memoryMap(Machine.POOL_SEGMENT), 100);
		// machine.at = 0x100;

		// for (int i = 0; i < buffer.length; i++) {
		// machine.memory[0x100 + i] = buffer[i];
		// }
		log.debug("specialRegister[L]" + machine.getSpecialRegister(L));
		log.debug("specialRegister[O]" + machine.getSpecialRegister(O));
		log.debug("specialRegister[S]" + machine.getSpecialRegister(S));
		log.debug("specialRegister[G]" + machine.getSpecialRegister(G));

		for (Entry<Long, Integer> tmp : machine.tlb.entrySet()) {
			log.debug(Long
					.toHexString(tmp.getKey() << machine.PAGE_SIZE_SHIFT)
					+ "->" + (tmp.getValue() >> machine.PAGE_SIZE_SHIFT));
		}

		machine.execute();
		// machine.execute();
	}

	private void setupMain() {
		// TODO Auto-generated method stub

	}
}
