import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import junit.framework.TestCase;

/**
 * finding: the current working directory is different between running test case
 * application, and Applet.<br/>
 * current working dir = C:\scm\git\git-hub\mmix\GO_WS\Χ��Ӧ�ó��� for test case and
 * application</br/> 
 * v.s. C:\scm\git\git-hub\mmix\GO_WS\Χ��Ӧ�ó���\bin for applet.
 * 
 * since current directory is environment dependent. it should be parameterized.
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class TestFileAPI extends TestCase {

	public void test() throws Exception {
		String name = "user.dir";
		String dir = System.getProperty(name);
		System.out.println("current working dir = " + dir);

		String goManualLibFileName = "doc/Χ��������/vol0000.gmd";
		byte[] manual = new byte[1024 * 4];
		DataInputStream in = null;
		in = new DataInputStream(new BufferedInputStream(new FileInputStream(
				goManualLibFileName)));
		int count = in.read(manual);
		System.out.print("read " + count + " byte. " + manual[0]);
	}

}
