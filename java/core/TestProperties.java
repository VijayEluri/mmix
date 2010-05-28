package core;

import java.io.IOException;
import java.util.Properties;

import junit.framework.TestCase;

/**
 * Just to make it clearn the string in the properties file need to use escape
 * sequence. <br/>
 * Please note in most XML config, the application follow a different
 * approach-escape sequence is not required.
 * 
 * @author Wu Jianfeng
 * 
 */
public class TestProperties extends TestCase {
	public void test() throws IOException {
		Properties p = new Properties();
		p.load(TestProperties.class.getResourceAsStream("test.properties"));
		String object = (String) p.get("ssl-client-cert-file");
		System.out.println("ssl-client-cert-file=" + object);

		object = (String) p.get("ssl-client-cert-file2");
		System.out.println("ssl-client-cert-file2=" + object);
	}
}
