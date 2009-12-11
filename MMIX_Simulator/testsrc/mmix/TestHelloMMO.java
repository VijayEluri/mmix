package mmix;

import static mmix.cons.SpecialRegisterConstant.L;
import static mmix.cons.SpecialRegisterConstant.O;
import static mmix.cons.SpecialRegisterConstant.S;

import java.io.IOException;

import mmix.util.IOUtil;

import org.apache.log4j.Logger;

/**
 * <p>
 * TestHelloMMO.java
 * </p>
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class TestHelloMMO extends TestMMIX {

	Logger log = Logger.getLogger(this.getClass());
	static byte[] buffer = null;
	Loader loader = new Loader();

	public void test()  {
		String fileName = "test/mmix/hello.mmo";
		try {
			buffer = IOUtil.readInput(fileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		loader.load(machine, buffer);
		IOUtil.showByteArrayInHex(buffer);
		//IOUtil.showByteArrayInHex(machine.memory(), machine.memoryMap(machine.virtualAt) , 100);
		setupMain();
		//IOUtil.showByteArrayInHex(machine.memory(), machine.memoryMap(Machine.POOL_SEGMENT), 100);
		// machine.at = 0x100;

		// for (int i = 0; i < buffer.length; i++) {
		// machine.memory[0x100 + i] = buffer[i];
		// }
		log.debug("specialRegister[L]" + machine.getSpecialRegister(L));
		log.debug("specialRegister[O]" + machine.getSpecialRegister(O));
		log.debug("specialRegister[S]" + machine.getSpecialRegister(S));

		machine.execute();
		
		assertEquals(1,machine.comulativeU);
		assertEquals(17,machine.comulativeV);
		assertEquals(0,machine.countBranch);
		assertEquals(0,machine.countCorrectPrediction);
	}

	private void setupMain() {
		machine.setGeneralRegister(0, 3);
		machine.setGeneralRegister(1, 0x4000000000000008L);		
		
		machine.setOcta(0x4000000000000008L,
				0x4000000000000028L);
		machine.setOcta(0x4000000000000010L,
				0x4000000000000030L);
		machine.setOcta(0x4000000000000018L,
				0x4000000000000038L);
		machine.setOcta(0x4000000000000020L,
				0x0000000000000000L);

		machine.setOcta((0x4000000000000028L),
				0x666f6f0000000000L);// foo
		machine.setOcta((0x4000000000000030L),
				0x6261720000000000L);// bar
		machine.setOcta((0x4000000000000038L),
				0x78797a7900000000L);// xyzzy
		
	}
}
