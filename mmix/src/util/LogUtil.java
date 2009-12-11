package util;

public class LogUtil {
	static boolean debug=true;

	public static boolean isDebug() {
		return debug;
	}

	public static void setDebug(boolean debug) {
		LogUtil.debug = debug;
	}
}
