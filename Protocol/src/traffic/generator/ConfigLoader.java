package traffic.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Properties;

/**
 * For simplicity, properties file is used, actually it fit for current
 * requirement. XML support can be added in the future.
 * 
 * @author wujianfe
 * 
 */
public class ConfigLoader {

	public static BasicParam loadBasicParam(String absFilePath) throws IOException {
		BasicParam param = new BasicParam();
		Properties p = new Properties();

		FileInputStream fis = new FileInputStream(new File(absFilePath));
		p.load(fis);

		param.setHostName(p.getProperty("Host_Name"));// = localhost
		param.setPort(Integer.parseInt(p.getProperty("Port")));// = 8080

		param.setSocketType(p.getProperty("Socket_Type"));// = TCP

		param.setProtocol(p.getProperty("Protocol"));// = GTPP

		param.setVersion(p.getProperty("Version"));// =

		param.setCallTimeout(Integer.parseInt(p.getProperty("Call_Timeout")));// =
		// 2000

		param.setSmssagesPerRound(Integer.parseInt(p
				.getProperty("Messages_Per_Round")));// = 1

		param.setIntervalBetweenRound(Integer.parseInt(p
				.getProperty("Interval_Between_Round")));// = 1000
		String[] temp = p.getProperty("Response_Time_Partition").split(",\\s*");
		int[] responseTimePartition = new int[temp.length];

		for (int i = 0; i < temp.length; i++) {
			responseTimePartition[i] = Integer.parseInt(temp[i]);
		}
		// Response_Time_Partition = 5, 10, 20, 50, 100, 200, 500, 1000
		param.setResponseTimePartition(responseTimePartition);

		return param;

	}

	public static String[] listTestCaseFiles(String directory) {
		File f = new File(directory);
		if (f.isDirectory()) {
			FilenameFilter filter = new FilenameFilter() {

				public boolean accept(File dir, String name) {
					if (name.toLowerCase().endsWith(
							ParamConstant.TEST_CASE_FILE_SUFFIX1)
							|| name.toLowerCase().endsWith(
									ParamConstant.TEST_CASE_FILE_SUFFIX2)) {
						return true;
					}
					return false;
				}
			};
			return f.list(filter);
		} else {
			System.err.print(directory + "is not a directory!");
			return null;
		}
	}
}
