package eddie.wu;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eddie.wu.util.FileUtil;
import eddie.wu.util.GBKToUTF8;

public class TestFileUtil extends TestCase {
	private static final Logger log = Logger.getLogger(GBKToUTF8.class);

	public void test() throws Exception {
		String file = "src/eddie/wu/arrayblock/GoApplet1.java";
		if (log.isDebugEnabled())
			log.debug(FileUtil.toString(file, "GBK"));
	}

	public void testConvert() throws Exception {
		String file = "src/eddie/wu/arrayblock/GoApplet1.java";
		String src = FileUtil.toString(file, "GBK");
		if (log.isDebugEnabled())
			log.debug(src);
		FileUtil.stringToFile(src, "UTF-8", file + ".utf8");
		if (log.isDebugEnabled())
			log.debug(FileUtil.toString(file + ".utf8", "UTF-8"));
	}

	public void testAll() throws Exception {
		// FileUtil.convertAll("src", "GBK", "UTF-8");
		FileUtil.convertAll("test", "GBK", "UTF-8");
	}
}
