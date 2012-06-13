package eddie.wu.manual;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;

/**
 * (;FF[4]GM[1]SZ[19]AP[SGFC:1.13b]
 * 
 * PB[troy]BR[12k*] PW[john]WR[11k*] KM[0.5]RE[W+12.5] DT[1998-06-15] TM[600]
 * 
 * ;B[pd];W[dp];B[pq];W[dd];B[qk];W[jd];B[fq];W[dj];B[jp];W[jj]
 * ;B[cn]LB[dn:A][po:B]C[dada: other ideas are 'A' (d6) or 'B' (q5)]
 * ;W[eo](;B[dl]C[dada: hm - looks troublesome. Usually B plays the 3,3 invasion
 * - see variation];W[qo];B[qp] ...
 * ;W[sr];B[sk];W[sg];B[pa];W[gc];B[pi];W[ph];B[de];W[ed];B[kn]
 * ;W[dh];B[eh];W[se];B[sd];W[af];B[ie];W[id];B[hf];W[hd];B[if]
 * ;W[fp];B[gq];W[qj];B[sj];W[rh];B[sn];W[so];B[sm];W[ep];B[mn]) ...
 * (;W[dq]N[wrong direction];B[qo];W[qp]) )
 * 
 * 
 * @author Eddie
 * 
 */
public class SGFGoManual {
	private static final Logger log = Logger.getLogger(SGFGoManual.class);

	/**
	 * 
	 * @param i
	 * @return
	 */
	public static String getFileName(int i) {
		String num;
		if (i < 10)
			num = "00" + i;
		else if (i < 100)
			num = "0" + i;
		else
			num = "" + i;
		String fileName = Constant.rootDir + "吴清源番棋263局/吴清源番棋" + num + ".SGF";
		return fileName;
	}

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
		out.writeByte((byte) ';');
		out.write("SZ".getBytes());
		out.writeByte((byte) '[');
		out.write(String.valueOf(manual.getBoardSize()).getBytes());
		out.writeByte((byte) ']');

		if (manual.getBlackName() == null || manual.getBlackName().isEmpty()) {
		} else {

			out.write("PB".getBytes());
			out.writeByte((byte) '[');
			out.write(manual.getBlackName().getBytes());
			out.writeByte((byte) ']');
		}

		if (manual.getWhiteName() == null || manual.getWhiteName().isEmpty()) {
		} else {
			out.write("PW".getBytes());
			out.writeByte((byte) '[');
			out.write(manual.getWhiteName().getBytes());
			out.writeByte((byte) ']');
		}

		// output init state;
		if (manual.getInitBlacks().isEmpty() == false) {
			out.write("AW".getBytes());
			out.writeByte((byte) '[');
			out.write(manual.getWhiteName().getBytes());
			out.writeByte((byte) ']');
		}

		if (manual.getInitWhites().isEmpty() == false) {

		}

		// black and whire play in turn.
		// boolean black = true;
		for (Step step : manual.getSteps()) {
			// TODO: how to express 弃权.
			if (step.isGiveUp())
				continue;
			out.writeByte((byte) ';');

			if (step.isBlack()) {
				out.write("B".getBytes());
				// black = false;
			} else if (step.isWhite()) {
				out.write("W".getBytes());
				// black = true;
			}
			out.writeByte((byte) '[');
			byte t = (byte) ('a' + step.getColumn() - 1);
			out.writeByte(t);
			t = (byte) ('a' + step.getRow() - 1);
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
		
		int boardSize = Constant.BOARD_SIZE;
		GoManual man = new GoManual(boardSize);
		Map<String, String> props = new HashMap<String, String>();

		int propIdenS = 0;
		int propIdenE = 0;
		int propValueS = 0;
		String value;
		String iden;
		int row = 0, column = 0, shoushu = 0;
		for (int i = 0; i < manual.length; i++) {
			if (manual[i] == '(') {

			} else if (manual[i] == ';') {
				propIdenS = i + 1;
			} else if (manual[i] == '[') {
				propIdenE = i; // exclusive
				propValueS = i + 1;
			} else if (manual[i] == ']') {

				iden = new String(Arrays.copyOfRange(manual, propIdenS,
						propIdenE));// , Charset.forName("GBK")
				iden = iden.trim();

				if (iden.equals(Black)) {

					row = (byte) (manual[propValueS + 1] - 'a' + 1);
					column = (byte) (manual[propValueS] - 'a' + 1);
					Point step = Point.getPoint(boardSize, row, column);
					man.addStep(step, Constant.BLACK, ++shoushu);
				} else if (iden.equals(White)) {
					row = (byte) (manual[propValueS + 1] - 'a' + 1);
					column = (byte) (manual[propValueS] - 'a' + 1);
					Point step = Point.getPoint(boardSize, row, column);
					man.addStep(step, Constant.WHITE, ++shoushu);
				} else if (iden.equals("AB") || iden.equals("AW")) {
					row = (byte) (manual[propValueS + 1] - 'a' + 1);
					column = (byte) (manual[propValueS] - 'a' + 1);
					Point step = Point.getPoint(boardSize, row, column);
					if (iden.equals("AB")) {
						man.addInitBlack(step);
					} else if (iden.equals("AW")) {
						man.addInitWhite(step);
					}
					while (true) {
						// list property
						if (i + 1 < manual.length && manual[i + 1] == '[') {
							row = (byte) (manual[i + 3] - 'a' + 1);
							column = (byte) (manual[i + 2] - 'a' + 1);
							step = Point.getPoint(boardSize, row, column);
							if (iden.equals("AB")) {
								man.addInitBlack(step);
							} else if (iden.equals("AW")) {
								man.addInitWhite(step);
							}
							i += 4;
						} else {
							break;
						}
					}

				} else if (iden.equals("SZ")) {
					value = new String(
							Arrays.copyOfRange(manual, propValueS, i));
					boardSize = Integer.valueOf(value).intValue();

				} else {
					value = new String(
							Arrays.copyOfRange(manual, propValueS, i),
							Charset.forName("GBK"));
					props.put(iden, value);
					if (log.isDebugEnabled()) {
						log.debug(value);
					}
				}

				propIdenS = i + 1;
			}
		}
		man.setBlackName(props.get("PB"));
		man.setWhiteName(props.get("PW"));
		man.setResult(props.get("RE"));
		if (log.isInfoEnabled()) {
			for (String key : props.keySet()) {
				log.info(key + " : " + props.get(key));
			}
			log.debug(man);
		}
		if(log.isDebugEnabled()) log.debug(props.get("RE"));
		if(log.isDebugEnabled()) log.debug(props.get("SZ"));
		if(log.isDebugEnabled()) log.debug(man.getInitBlacks());
		return man;
	}

}
/**
 * (;FF[4]GM[1]SZ[19]
 * 
 * GN[Copyright goproblems.com] PB[Black] HA[0] PW[White] KM[5.5] DT[1999-07-21]
 * TM[1800] RU[Japanese]
 * 
 * ;AW[bb][cb][cc][cd][de][df][cg][ch][dh][ai][bi][ci]
 * AB[ba][ab][ac][bc][bd][be][cf][bg][bh] C[Black to play and live.]
 * (;B[af];W[ah] (;B[ce];W[ag]C[only one eye this way]) (;B[ag];W[ce]) )
 * (;B[ah];W[af] (;B[ae];W[bf];B[ag];W[bf] (;B[af];W[ce]C[oops! you can't take
 * this stone]) (;B[ce];W[af];B[bg]C[RIGHT black plays under the stones and
 * lives]) ) (;B[bf];W[ae]) ) ( ;B[ae];W[ag]))
 */
