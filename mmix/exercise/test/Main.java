package test;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;


public class Main {

/**
 * the value in the Properties file need to be escaped, please consider the native2ascii will generate
 * escape sequence. 
 * @param args
 * @throws Exception
 */
	public static void main(String[] args) throws Exception {
		Properties props = new Properties();
		
		props.load(Main.class.getResourceAsStream("test.properties"));
		System.err.println(props.getProperty("LOG_FILE_PATH"));
		System.err.println(props.getProperty("DOG_FILE_PATH"));
		
		InputStream inStream = new FileInputStream("test.properties");
		props.load(inStream );
		System.err.println(props.getProperty("LOG_FILE_PATH"));
		System.err.println(props.getProperty("DOG_FILE_PATH"));
	}
}
