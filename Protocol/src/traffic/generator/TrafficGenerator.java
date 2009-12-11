package traffic.generator;

import java.io.IOException;

import com.hp.atom.workflow.api.RAEngine;
import com.hp.atom.workflow.api.impl.WorkflowEngine;

/**
 * Act as a client to send request to specified server.
 * 
 * @author wujianfe
 * 
 */
public class TrafficGenerator {
	private static boolean debug;
	private static final String BASIC_PARAM = ParamConstant.BASIC_PARAM_FILE_NAME;
	BasicParam bp;

	/**
	 * As a command line tool, we need to specify the command line options. user
	 * -D <directory> to specify the folder of basic_param.xml and other
	 * configuration files;
	 * 
	 * sample usage: java TrafficGenerator -D
	 * "C:/workspace/Eclipse/Protocol/src/traffic/generator"
	 * 
	 * if current working directory is same as the directory of basic_param.xml,
	 * -D option is not necessary.
	 * 
	 * 
	 * in real situation we will use JARs java -jar
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		debug = true;
		if (debug == true) {
			for (String temp : args) {
				System.out.println(temp);
			}
		}
		int argc = args.length;
		if (argc < 1) {

		} else if (argc == 1) {// to support -D<dir>
			if (args[0].startsWith("-D")) {
				new TrafficGenerator().launch(args[0].substring(2));
			}
		} else if (argc == 2) {
			new TrafficGenerator().launch(args[1]);
		} else {
			System.out.println("The Usage is ");
			System.out
					.println("\tjava TrafficGenerator -D <dir to basic_param.properties> ");
		}
	}

	public static String getFileName(String absDirPath, String fileName) {
		if (absDirPath.endsWith("/")) {
			absDirPath += fileName;
		} else {
			absDirPath += "/";
			absDirPath += fileName;
		}
		return absDirPath;
	}

	/**
	 * This is also the API for the client, if you want to run traffic
	 * generator, just call: <br />
	 * new TrafficGenerator().launch(String absFilePath).
	 * 
	 * @param absDirPath
	 *            Absolute path to basic_param.properties
	 * @throws IOException
	 */
	public void launch(final String absDirPath) throws IOException {
		String absFilePath = getFileName(absDirPath, BASIC_PARAM);
		
		if (debug == true) {
			System.out.println("absFilePath is " + absFilePath);
		}
		bp = ConfigLoader.loadBasicParam(absFilePath);
		
		
		String[] testcases = ConfigLoader.listTestCaseFiles(absDirPath);
		
		// bp will be used in Connector.
		
		// then call Workflow engine.
		for(String testcase:testcases){
			testcase = getFileName(absDirPath,testcase);
			System.out.println(testcase);
			//How to know whether the case is a success or failure.
			
			RAEngine engine = new WorkflowEngine();
			//engine.signal(evt);
		}
		
		//how can workflow cooperate with this.
	}
}
