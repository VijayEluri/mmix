package mmix;

import static mmix.cons.SpecialRegisterConstant.L;
import static mmix.cons.SpecialRegisterConstant.O;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import junit.framework.Assert;

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
public class TestProgramM extends TestMMIX {
	Logger log = Logger.getLogger(this.getClass());
	static byte[] buffer = null;

	public void testProgramM() {
		try {
			String fileName = "test/ProgramM.mmo";
			buffer = IOUtil.readInput(fileName);

			machine.setByteArray(0x100, buffer, 0, buffer.length);
			log.debug("before set memory");
			machine.showMemory(log, 0x4000000000000000L);
			setupMain();
			log.debug("after set memory");
			machine.showMemory(log, 0x4000000000000000L);
			
			
			machine.setGeneralRegister(0, 4);
			machine.setGlobalRegister(0xfe, 0x4000000000000000L);
			machine.showInternalStatus(log);

			
			//machine.showMemory(log, 0x4000000000000000L);

			//overwrite the pop instruction.
			byte[] value = new byte[4];
			value[0] = 0;
			value[1] = 0;
			value[2] = 0;
			value[3] = 0;
			//
			machine.showMemory(log, 0x100);
			machine.setTetra(0x100 + 40, value, 0);
			machine.showMemory(log, 0x100);

			machine.virtualAt = 0x100;

			machine.showInternalStatus(log);
			machine.execute();
			machine.showInternalStatus(log);
			Assert.assertEquals(3, machine.getGeneralRegister(0));
			Assert.assertEquals(0x4000000000000101L, machine.getGeneralRegister(1));
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	private void setupMain() {
		
		//machine.setGlobalRegister(0xfe, 0x4000000000000000L);
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
		
		log.debug("machine.getSpecialRegister(L);= "
				+ machine.getSpecialRegister(L));

	}

	

}
