package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import mmix.NumberUtil;

import org.apache.log4j.Logger;

/**
 * read the program like the ProgramH, and convert it to object file. java
 * HEX2Object <file_name>
 * 
 * input file format is as the following; 8fff0100 00000701 f4ff0003 00000701
 * 00000000 2c20776f 726c640a 00 then remove new line use regular expression.
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class HEX2Object {
	static byte[] buffer = null;

	static Logger log = Logger.getLogger(HEX2Object.class);

	public static void main(String[] args) throws IOException {

		validateInput(args);

		// arg[0]=file_name
		String fileName = args[0];

		readInput(fileName);
		String outputFileName;
		if (fileName.indexOf(".") != -1) {
			outputFileName = fileName.substring(0, fileName.indexOf("."))
					+ ".mmo";
		} else {
			outputFileName = fileName + ".mmo";
		}
		writeOut(buffer, outputFileName);
	}

	public static void readInput(String fileName) throws IOException {
		File file = new File(fileName);
		log.debug("file path = " + file.getAbsolutePath());
		log.debug("file size = " + file.length());
		FileInputStream ins = new FileInputStream(file);
		buffer = new byte[(int) file.length()];
		ins.read(buffer);
		ins.close();
	}

	public static void writeOut(byte[] buffer, String outputFileName)
			throws IOException {
		log.debug("outputFileName= " + outputFileName);
		File outputFile = new File(outputFileName);
		FileOutputStream outs = new FileOutputStream(outputFile);
		log.debug("file path = " + outputFile.getAbsolutePath());
		log.debug("file size = " + outputFile.length());
		int i;
		byte temp = 0;
		int instruction;
		byte[] outBuf = new byte[(int) buffer.length / 2];
		log.debug("");
		log.debug("before converted!");
		for (i = 0; i < buffer.length; i += 2) {
			byte[] tt = new byte[2];
			tt[0] = buffer[i];
			tt[1] = buffer[i + 1];
			String str = new String(tt);
			if (i % 8 == 6) {
				log.debug(str);
			} else {
				System.out.print(str);
			}
			// instruction = (buffer[i]-48) * 16 + (buffer[i + 1]-48);
			instruction = Integer.valueOf(str, 16);
			temp = (byte) instruction;
			outBuf[i / 2] = temp;// overflow. does not matter.
			if (temp < 0) {
				if (log.isDebugEnabled()) {
					log.debug("over flow: 0x"
							+ Integer.toHexString(instruction) + " v.s. "
							+ temp + " or 0x" + Integer.toHexString(temp));
				}
			}
		}
		log.debug("");
		log.debug("after converted!");
		for (i = 0; i < outBuf.length; i++) {
			if (i % 4 == 3) {
				log.debug(NumberUtil.byteToHex(outBuf[i]));
			} else {
				System.out.print(NumberUtil.byteToHex(outBuf[i]));
			}

		}
		outs.write(outBuf);
		outs.flush();
		outs.close();
	}

	

	private static void validateInput(String[] args) {
		for (String arg : args) {
			log.debug("arg = " + arg);
		}
		if (args.length < 1) {
			log.debug("java Simulator <file_name> [options]");
		}
	}

}
