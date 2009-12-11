package mmix.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import mmix.NumberUtil;

import org.apache.log4j.Logger;

/**
 * <p>
 * IOUtil.java
 * </p>
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class IOUtil {
	private static Logger log = Logger.getLogger(IOUtil.class);

	public static byte[] readInput(String fileName) throws IOException {
		File file = new File(fileName);
		if (log.isDebugEnabled()) {
			log.debug("file path = " + file.getAbsolutePath());
			log.debug("file size = " + file.length());
		}
		FileInputStream ins = new FileInputStream(file);
		byte[] buffer = new byte[(int) file.length()];
		ins.read(buffer);
		ins.close();
		return buffer;
	}

	/**
	 * output 8 byte every line
	 * 
	 * @param memory
	 * @param offset
	 * @param length
	 */
	public static void showByteArrayInHex(byte[] memory, int offset, int length) {
		log.debug("byte array:  from " + offset + " to "
				+ (offset + length) + " of menory");
		StringBuffer buf = new StringBuffer();
		for (int i = offset; i < offset + length; i++) {
			if (i % 8 == 7) {
				buf.append("" + NumberUtil.byteToHex(memory[i])+"\n");
			} else if (i % 8 == 3) {
				buf.append("" + NumberUtil.byteToHex(memory[i]) + "\t");
			} else {
				buf.append("" + NumberUtil.byteToHex(memory[i]));
			}
		}
		log.debug(buf.toString());
	}

	public static void showByteArrayInHex(byte[] buffer) {
		showByteArrayInHex(buffer, 0, buffer.length);
	}

}
