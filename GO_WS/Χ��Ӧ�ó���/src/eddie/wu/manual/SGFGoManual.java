package eddie.wu.manual;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.domain.Point;

public class SGFGoManual {
	private static final Log log = LogFactory.getLog(SGFGoManual.class);

	public static void storeGoManual(String fileName, GoManual manual) {

		DataOutputStream out = null;
		byte[] manB = null;
		try {
			File file = new File(fileName);
			out = new DataOutputStream(new BufferedOutputStream(
					new FileOutputStream(file)));
			manB = new byte[(int) file.length()];

			writeGoManual(out, manual);
		} catch (IOException ex) {
			log.debug("the input meet some trouble!");
			log.debug("Exception" + ex.toString());
			throw new RuntimeException(ex);
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			log.debug("finally");
		}
	}

	public static Object writeGoManual(DataOutputStream out, GoManual manual)
			throws IOException {
		// byte[] temps = new byte[];
		out.writeByte((byte) '(');
		if (manual.getBlackName() == null || manual.getBlackName().isEmpty()) {
		}else{
			out.writeByte((byte) ';');
			out.write("PB".getBytes());
			out.writeByte((byte) '[');
			out.write(manual.getBlackName().getBytes());
			out.writeByte((byte) ']');
		}

		if (manual.getWhiteName() == null || manual.getWhiteName().isEmpty()) {
		}else{
			out.write("PW".getBytes());
			out.writeByte((byte) '[');
			out.write(manual.getWhiteName().getBytes());
			out.writeByte((byte) ']');
		}
		//black and whire play in turn.
		boolean black = true;
		for (Point p : manual.getSteps()) {
			out.writeByte((byte) ';');

			if (black) {
				out.write("B".getBytes());
				black = false;
			} else {
				out.write("W".getBytes());
				black = true;
			}
			out.writeByte((byte) '[');
			byte t = (byte) ('a' + p.getColumn() - 1);
			out.writeByte(t);
			t = (byte) ('a' + p.getRow() - 1);
			out.writeByte(t);
			out.writeByte((byte) ']');
		}

		out.writeByte((byte) ')');
		return null;
	}

	/**
	 * usually one file store one simple manual.
	 * 
	 * @return
	 */
	public static GoManual loadGoManual(String fileName) {

		DataInputStream in = null;
		byte[] manB = null;
		try {
			File file = new File(fileName);
			in = new DataInputStream(new BufferedInputStream(
					new FileInputStream(file)));
			manB = new byte[(int) file.length()];
			in.read(manB);
			log.debug("in.available()=" + in.available());
			log.debug("file.length()=" + file.length());
			return toGoManual(manB);
		} catch (IOException ex) {
			log.debug("the input meet some trouble!");
			log.debug("Exception" + ex.toString());
			throw new RuntimeException(ex);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			log.debug("finally");
		}

	}

	public static final String Black = "B";
	public static final String White = "W";

	public static GoManual toGoManual(byte[] manual) {
		GoManual man = new GoManual();

		Map<String, String> props = new HashMap<String, String>();

		int propIdenS = 0;
		int propIdenE = 0;
		int propValueS = 0;
		String value;
		String iden;
		int row = 0, column = 0;
		for (int i = 0; i < manual.length; i++) {
			if (manual[i] == '(') {

			} else if (manual[i] == ';') {
				propIdenS = i + 1;
			} else if (manual[i] == '[') {
				propIdenE = i; // exclusive
				propValueS = i + 1;
			} else if (manual[i] == ']') {

				iden = new String(Arrays.copyOfRange(manual, propIdenS,
						propIdenE));
				value = new String(Arrays.copyOfRange(manual, propValueS, i));
				if (iden.equals(Black)) {
					if (log.isDebugEnabled()) {
						log.debug(value);
					}
					row = (byte) (manual[propValueS + 1] - 'a' + 1);
					column = (byte) (manual[propValueS] - 'a' + 1);
					Point step = Point.getPoint(row, column);
					man.addStep(step);
				} else if (iden.equals(White)) {
					row = (byte) (manual[propValueS + 1] - 'a' + 1);
					column = (byte) (manual[propValueS] - 'a' + 1);
					Point step = Point.getPoint(row, column);
					man.addStep(step);
				} else {
					props.put(iden, value);
				}

				propIdenS = i + 1;
			}
		}
		man.setBlackName(props.get("PB"));
		man.setWhiteName(props.get("PW"));
		if (log.isInfoEnabled()) {
			for (String key : props.keySet()) {
				log.info(key + " : " + props.get(key));
				// log.debug(props);
			}
			log.debug(man);
		}
		System.out.println(props.get("RE"));
		return man;
	}

}
