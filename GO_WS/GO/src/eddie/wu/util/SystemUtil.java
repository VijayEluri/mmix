package eddie.wu.util;

public class SystemUtil {
	public static String getCurrentWorkingDirectory() {
		return System.getProperty("user.dir");
	}

	public static String getLineSeparator() {
		return System.getProperty("line.separator");
	}
}
