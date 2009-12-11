package traffic.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import junit.framework.TestCase;

public class TestConfigLoader extends TestCase {
	String directory = "C:/workspace/Eclipse/Protocol/src/traffic/generator/testcase";

	ConfigLoader ld = new ConfigLoader();

	public void test1() throws IOException {
		ConfigLoader.loadBasicParam(getFileName());
	}

	public String getFileName() {
		if (directory.endsWith("/")) {
			return directory + ParamConstant.BASIC_PARAM_FILE_NAME;
		} else {
			return directory + "/" + ParamConstant.BASIC_PARAM_FILE_NAME;
		}
	}

	public void test2() throws IOException {

		System.out.println(ConfigLoader.loadBasicParam(getFileName()).toString());
	}

	public void loadP() throws IOException {
		Properties p = new Properties();

		FileInputStream fis = new FileInputStream(new File(directory + ParamConstant.BASIC_PARAM_FILE_NAME));
		p.load(fis);
		for (Object t : p.keySet()) {
			String key = (String) t;
			System.out.println(p.getProperty(key));
		}

	}

	public void testLoadTestCases() {
		String[] fs = ConfigLoader.listTestCaseFiles(directory);
		for (String t : fs) {
			System.out.println(t);
		}
	}

}
