package eddie.wu.search.two;

import org.apache.log4j.LogManager;

public class VerifyTwoTwo_IT extends VerifyTwoTwo {
	static {
		String key = LogManager.DEFAULT_CONFIGURATION_KEY;
		String value = "log4j_error.xml";
		System.setProperty(key, value);
//		Logger.getLogger(TwoTwoBoardSearch.class).setLevel(Level.WARN);
//		Logger.getLogger(GoBoardSearch.class).setLevel(Level.WARN);
//		Logger.getLogger(GoBoardForward.class).setLevel(Level.WARN);
	}
}
