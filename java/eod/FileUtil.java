package eod;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

public class FileUtil {
	private static final Logger log = Logger.getLogger(FileUtil.class);

	public static String toString(String filePath, String charset)
			throws IOException {
		File file = new File(filePath);
		String res = null;
		int count = (int) file.length();
		int start = 0;
		byte[] a = new byte[count];

		DataInputStream dis = new DataInputStream(new FileInputStream(file));
		while (count > 0) {
			int read = dis.read(a, start, count);
			if (read < 0)
				break;
			start += read;
			count -= read;
		}
		dis.close();
		res = new String(a, charset);
		return res;
	}

}
