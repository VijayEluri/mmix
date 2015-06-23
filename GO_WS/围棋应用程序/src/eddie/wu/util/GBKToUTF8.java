package eddie.wu.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class GBKToUTF8 {
	private static final Logger log = Logger.getLogger(GBKToUTF8.class);
	static final String template1 = "native2ascii -encoding GBK <file_name>.java <file_name>.unicode";
	static final String template2 = "native2ascii -reverse -encoding UTF-8 <file_name>.unicode <file_name>.utf8";
	static final String template3 = "copy <file_name>.java <file_name>.bak";
	static final String template4 = "copy <file_name>.utf8 <file_name>.java";

	private static String root = "./src";

	public static void main2(String[] args) {

		List<String> fileList = getFileList(root);
		for (String filepath : fileList) {
			String command = template1.replace("<file_name>", filepath);
			executeCommand(command);
		}
		// String fileList = "c:\file.list";
		if(log.isDebugEnabled()) log.debug(fileList.get(0));
	}

	public static void main(String[] args) {

	}

	public static void convertOneFile(String filepath) {
		String command;
		command = template1.replace("<file_name>", filepath);
		if(log.isDebugEnabled()) log.debug(command);
		executeCommand(command);
		command = template2.replace("<file_name>", filepath);
		if(log.isDebugEnabled()) log.debug(command);
		executeCommand(command);
		command = template3.replace("<file_name>", filepath);
		if(log.isDebugEnabled()) log.debug(command);
		executeCommand(command);
		command = template4.replace("<file_name>", filepath);
		if(log.isDebugEnabled()) log.debug(command);
		executeCommand(command);
	}

	static void executeCommand(String cmdarray) {
		Runtime rt = Runtime.getRuntime();

		Process p = null;
		try {

			p = rt.exec(cmdarray);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		try {
			p.waitFor();
		} catch (Exception e) {
			e.printStackTrace();
		}

		long res = p.exitValue();
		String message = "Return value from shell script (" + ") is: " + res;
	}

	/**
	 * find . -name "*.java" > c:\file.list
	 * 
	 * @param root
	 * @return
	 */
	private static List<String> getFileList(String root) {

		List<String> list = new ArrayList<String>();

		return list;

	}
}
