package eddie.wu;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import eddie.wu.domain.Constant;

import junit.framework.TestCase;

/**
 * finding: the current working directory is different between running test case
 * application, and Applet.<br/>
 * current working dir = C:\scm\git\git-hub\mmix\GO_WS\围棋应用程序 for test case and
 * application</br/> 
 * v.s. C:\scm\git\git-hub\mmix\GO_WS\围棋应用程序\bin for applet.<br/>
 * that means Applet runs in class path as current working directory.
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

		String goManualLibFileName = Constant.rootDir+"/vol0000.gmd";// "doc/围棋打谱软件/vol0000.gmd";
		byte[] manual = new byte[1024 * 4];
		DataInputStream in = null;
		in = new DataInputStream(new BufferedInputStream(new FileInputStream(
				goManualLibFileName)));
		int count = in.read(manual);
		System.out.print("read " + count + " byte. " + manual[0]);
	}

}
