package mmix;

import static mmix.cons.SpecialRegisterConstant.G;
import static mmix.cons.SpecialRegisterConstant.L;
import static mmix.cons.SpecialRegisterConstant.O;
import static mmix.cons.SpecialRegisterConstant.S;

import java.io.IOException;

import mmix.util.IOUtil;

import org.apache.log4j.Logger;

/**
 * <p>TestCPMMO.java
 * </p>
 * test cp.mms -- simplified version of copy.mms				
 * 
 * @author Eddie Wu 
 * @version 1.0
 * 
 */
public class TestCPMMO extends TestMMIX {
	Logger log = Logger.getLogger(this.getClass());
	static byte[] buffer = null;
	Loader loader = new Loader();

	public void test() {
		String fileName = "test/mmix/cp.mmo";
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

		log.debug("converted instruction in memory: virtual address from" +Long.toHexString(machine.virtualAt ));
		machine.showMemory(log, machine.virtualAt);
		
		
		log.debug("data seg virtual address from:: " + Long.toHexString(Machine.DATA_SEGMENT));
		machine.showMemory(log, Machine.DATA_SEGMENT);


		log.debug("pool set: heap"+ Long.toHexString(Machine.POOL_SEGMENT));
		machine.showMemory(log, Machine.POOL_SEGMENT);
		
		machine.showInternalStatus(log);

		machine.execute();
	}

		public static void main(String[] args){
			TestCPMMO m = new TestCPMMO();
			try {
				m.setUp();
			} catch (Exception e) {
				e.printStackTrace();
			}
			m.test();
		}
	
}
