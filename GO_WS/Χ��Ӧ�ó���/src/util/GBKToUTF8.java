package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GBKToUTF8 {
	private static final String template = "native2ascii -encoding GBK <file_name>.java "
			+ "<file_name>.unicode; "
			+ "native2ascii -reverse -encoding UTF-8  <file_name>.unicode <file_name>.utf8; "
			+ "copy <file_name>.java <file_name>.bak; "
			+ "copy <file_name>.utf8 <file_name>.java ";

	private static String root = "C:/scm/github/mmix/GO_WS/";

	public static void main2(String[] args) {

		for (String filepath : getFileList(root)) {
			String command = template.replace("<file_name>", filepath);
			executeCommand(command);
		}
		String fileList = "c:\file.list";

	}

	public static void main(String[] args) {

		String command = template.replace("<file_name>",
				"src/untitled8/GoApplet");
		System.out.println("command is [" + command + "]");
		executeCommand(command);
	}

	private static void executeCommand(String cmdarray) {
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
