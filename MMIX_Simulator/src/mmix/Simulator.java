package mmix;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

/**
 * Simulator for MMIX. The grammar in command line is as the following: java
 * Simulator <file_name> [options] example: java Simulator test/ProgramH.mmo the
 * current path is D:\WorkSpace\CS\MMIX_Simulator\ in this project.
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class Simulator {
	static Logger log = Logger.getLogger(Simulator.class);

	InstructionExecutor[] executors = new InstructionExecutor[256];

	// private Simulator simulator;

	public static void main(String[] args) throws IOException {

		for (String arg : args) {
			log.debug("arg = " + arg);
		}
		if (args.length < 1) {
			log.debug("java Simulator <file_name> [options]");
		}
		// arg[0]=file_name
		String fileName = args[0];
		// Simulator simulator = new Simulator();
		// simulator.setUp();
		File file = new File(fileName);
		log.debug("file path = " + file.getAbsolutePath());
		log.debug("file size = " + file.length());
		FileInputStream ins = new FileInputStream(file);
		byte[] b = new byte[(int) file.length()];
		
		ins.read(b);
		int instruction = 0;
		int i = 0;
		for (byte temp : b) {			
			System.out.print((temp));
			i++;
			if (i % 4 == 0) {
				log.debug("");
			}
		}
		log.debug("");
		log.debug("");
		i = 0;
		for (byte temp : b) {
			if (temp < 0) {
				instruction = temp + 256;
			}else{
				instruction = temp;
			}
			// NumberFormatter a ;
			// a.
			System.out.print((instruction));
			i++;
			if (i % 4 == 0) {
				log.debug("");
			}
		}
		log.debug("");
		log.debug("");
		i = 0;
		for (byte temp : b) {
			if (temp < 0) {
				instruction = temp + 256;
			}else{
				instruction = temp;
			}
			// NumberFormatter a ;
			// a.
			System.out.print(Integer.toHexString(instruction));
			i++;
			if (i % 4 == 0) {
				log.debug("");
			}
		}
		log.debug("");
		log.debug("");
		byte temp =0;
		for(i =0 ; i<file.length(); i++){
			temp = b[i];
			if (temp < 0) {
				instruction = temp + 256;
			}else{
				instruction = temp;
			}
			System.out.print((instruction));			
			if (i % 4 == 0) {
				log.debug("");
			}
		}
		Machine machineState = new Machine();
		machineState.setUp().execute();
		// return 0;
	}

//	public static String byteToHex(int t) {
//		int a = t / 16;
//		int b = t % 16;
//		StringBuffer buf = new StringBuffer();
//		if()
//		return "" + t / 16 + t % 16 + " ";
//	}
	// private void setUp() {
	// machineState = new MachineState();
	// for (int i = 1; i <= 10; i++) {
	// executors[i] = new LoadingInstructionExecutor();
	// }
	// for (int i = 0; i < 256; i++) {
	// executors[i].setMachineState(machineState);
	// }
	//
	// }
}
