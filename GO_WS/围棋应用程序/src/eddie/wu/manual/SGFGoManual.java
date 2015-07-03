package eddie.wu.manual;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.util.FileUtil;

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
	public static final String WHITE = "W";
	public static final String BLACK = "B";
	public static final Logger log = Logger.getLogger(SGFGoManual.class);

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

	public static void storeGoManual(String fileName, SimpleGoManual manual) {

		DataOutputStream out = null;
		byte[] manB = null;
		try {
			File file = new File(fileName);
			out = new DataOutputStream(new BufferedOutputStream(
					new FileOutputStream(file)));
			manB = new byte[(int) file.length()];

			writeGoManual(out, manual);
		} catch (IOException ex) {
			if (log.isEnabledFor(Level.WARN))
				log.warn("file name=" + fileName);
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

	public static void storeGoManual(String fileName, TreeGoManual manual) {

		DataOutputStream out = null;
		byte[] manB = null;
		try {
			File file = new File(fileName);
			out = new DataOutputStream(new BufferedOutputStream(
					new FileOutputStream(file)));
			manB = new byte[(int) file.length()];

			writeGoManual(out, manual);
		} catch (IOException ex) {
			if (log.isEnabledFor(Level.WARN))
				log.warn("file name=" + fileName);
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

	public static void storeGoManual(String fileName, List<TreeGoManual> manuals) {

		DataOutputStream out = null;
		byte[] manB = null;
		try {
			File file = new File(fileName);
			out = new DataOutputStream(new BufferedOutputStream(
					new FileOutputStream(file)));
			manB = new byte[(int) file.length()];

			writeGoManual(out, manuals);
		} catch (IOException ex) {
			if (log.isEnabledFor(Level.WARN))
				log.warn("file name=" + fileName);
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

	public static void writeGoManualHeader(DataOutputStream out,
			AbsGoManual manual) throws IOException {
		out.writeByte((byte) '(');
		out.writeByte((byte) ';');
		out.write("SZ".getBytes());
		out.writeByte((byte) '[');
		out.write(String.valueOf(manual.getBoardSize()).getBytes());
		out.writeByte((byte) ']');

		out.write("PL".getBytes());
		out.writeByte((byte) '[');
		if (manual.getInitTurn() == Constant.BLACK) {
			out.write("B".getBytes());
		} else {
			out.write("W".getBytes());
		}
		out.writeByte((byte) ']');

		out.write("RE".getBytes());
		out.writeByte((byte) '[');

		out.write(String.valueOf(manual.getResult()).getBytes());

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
			out.write("AB".getBytes());// Add Black
			for (Point pointTemp : manual.getInitBlacks()) {
				out.writeByte((byte) '[');
				writePoint(out, pointTemp);
				out.writeByte((byte) ']');
			}
		}

		if (manual.getInitWhites().isEmpty() == false) {
			out.write("AW".getBytes());// Add White
			for (Point pointTemp : manual.getInitWhites()) {
				out.writeByte((byte) '[');
				writePoint(out, pointTemp);
				out.writeByte((byte) ']');
			}
		}

	}

	public static void writeGoManual(DataOutputStream out,
			List<TreeGoManual> manuals) throws IOException {
		for (TreeGoManual manual : manuals) {
			writeGoManual(out, manual);
		}
	}

	public static void writeGoManual(DataOutputStream out, SimpleGoManual manual)
			throws IOException {
		// byte[] temps = new byte[];

		writeGoManualHeader(out, manual);
		// black and white play in turn.
		// boolean black = true;
		for (Step step : manual.getSteps()) {
			// TODO: how to express 弃权.
			// if (step.isGiveUp())
			// continue;
			// out.writeByte((byte) ';');
			//
			// if (step.isBlack()) {
			// out.write(BLACK.getBytes());
			// // black = false;
			// } else if (step.isWhite()) {
			// out.write(WHITE.getBytes());
			// // black = true;
			// }
			// out.writeByte((byte) '[');
			// writePoint(out, step.getPoint());
			// out.writeByte((byte) ']');
			out.writeBytes(step.toSGFString());
		}

		out.writeByte((byte) ')');
		// return null;
	}

	/**
	 * due to SGF definition, columns comes first in SGF from top left.
	 * 
	 * @param out
	 * @param point
	 * @throws IOException
	 */
	private static void writePoint(DataOutputStream out, Point point)
			throws IOException {
		byte t;
		t = (byte) ('a' + point.getColumn() - 1);
		out.writeByte(t);
		t = (byte) ('a' + point.getRow() - 1);
		out.writeByte(t);
	}

	/**
	 * usually one file store one simple manual.
	 * 
	 * @return
	 */
	public static SimpleGoManual loadSimpleGoManual(String fileName) {
		byte[] manB = FileUtil.loadFileAsBytes(fileName);
		return toSimpleGoManual(manB);
	}

	public static List<TreeGoManual> loadTreeGoManual(String fileName) {
		byte[] manB = FileUtil.loadFileAsBytes(fileName);
		return toTreeGoManual(manB);
	}

	public static final String Black = BLACK;
	public static final String White = WHITE;

	public static SimpleGoManual toSimpleGoManual(byte[] manual) {

		int boardSize = Constant.BOARD_SIZE;
		SimpleGoManual man = new SimpleGoManual(boardSize, Constant.BLACK);
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

				if (iden.equals(Black) || iden.equals(White)) {
					Point step = null;
					if (propValueS == i) {
						// pass like B[]
					} else {
						row = (byte) (manual[propValueS + 1] - 'a' + 1);
						column = (byte) (manual[propValueS] - 'a' + 1);
						// support pass as B[tt]
						if (row <= boardSize && column <= boardSize) {
							step = Point.getPoint(boardSize, row, column);
						}
					}
					if (iden.equals(Black)) {
						man.addStep(step, Constant.BLACK, ++shoushu);
					} else if (iden.equals(White)) {
						man.addStep(step, Constant.WHITE, ++shoushu);
					}

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
					if (boardSize <= 1) {
						boardSize = Constant.BOARD_SIZE;
					}
					man.setBoardSize(boardSize);

				} else if (iden.equals("PL")) {
					value = new String(
							Arrays.copyOfRange(manual, propValueS, i));
					if (value.equals("B"))
						man.setInitTurn(Constant.BLACK);
					else if (value.equals("W"))
						man.setInitTurn(Constant.WHITE);

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
		if (log.isDebugEnabled())
			log.debug(props.get("RE"));
		if (log.isDebugEnabled())
			log.debug(props.get("SZ"));
		if (log.isDebugEnabled())
			log.debug(man.getInitBlacks());
		return man;
	}

	public static void writeGoManual(DataOutputStream out, TreeGoManual manual)
			throws IOException {
		writeGoManualHeader(out, manual);
		out.writeBytes(manual.getSGFBodyString());
		out.writeByte((byte) ')');
	}

	/**
	 * copy from toGoManual.enhance to support TreeGoManual
	 * 
	 * @param manual
	 * @return
	 */
	public static List<TreeGoManual> toTreeGoManual(byte[] manual) {

		int boardSize = Constant.BOARD_SIZE;
		TreeGoManual man = null;
		Map<String, String> props = new HashMap<String, String>();
		SearchNode node;
		SearchNode root = null, child;
		SearchNode current = null;

		List<TreeGoManual> list = new ArrayList<TreeGoManual>();
		Stack<SearchNode> stack = new Stack<SearchNode>();

		int propIdenS = 0;
		int propIdenE = 0;
		int propValueS = 0;
		String value;
		String iden;
		int row = 0, column = 0, shoushu = 0;
		for (int i = 0; i < manual.length; i++) {
			if (manual[i] == '(') {
				if (stack.isEmpty()) {// new manual
					root = SearchNode.getSpecialRoot();
					stack.add(root);
					current = root;
					man = new TreeGoManual(root, boardSize, Constant.BLACK);
					shoushu = 0;
				} else {
					// if(stack.pop()==current){
					//
					// }
					stack.add(current);// remember the branch point.
					// node
				}
			} else if (manual[i] == ')') {
				current = stack.pop();
				shoushu = current.countSteps();
				if (stack.isEmpty()) {
					// TreeGoManual tgm = new TreeGoManual(root.getChild());
					// man.setRoot(root.getChild());

					list.add(man);
				}

			} else if (manual[i] == ';') {
				propIdenS = i + 1;
			} else if (manual[i] == '[') {
				propIdenE = i; // exclusive
				propValueS = i + 1;
			} else if (manual[i] == ']') {

				iden = new String(Arrays.copyOfRange(manual, propIdenS,
						propIdenE));// , Charset.forName("GBK")
				iden = iden.trim();

				if (iden.equals(Black) || iden.equals(White)) {
					// support both B[] and B[tt]
					Point step = null;
					if (propValueS == i) {

					} else {
						row = (byte) (manual[propValueS + 1] - 'a' + 1);
						column = (byte) (manual[propValueS] - 'a' + 1);
						if (row <= boardSize && column <= boardSize) {
							step = Point.getPoint(boardSize, row, column);
						}
					}
					Step move = null;
					if (iden.equals(Black)) {
						move = new Step(step, Constant.BLACK, ++shoushu);
					} else {
						move = new Step(step, Constant.WHITE, ++shoushu);

					}
					child = current.getChild(move);
					if (child != null) { // redundant
						current = child;
					} else {
						child = new SearchNode(move);
						current.addChild(child);
						current = child;
					}

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
					if (boardSize <= 1)
						boardSize = Constant.BOARD_SIZE;
					man.setBoardSize(boardSize);

				} else if (iden.equals("PL")) {
					value = new String(
							Arrays.copyOfRange(manual, propValueS, i));
					if (value.equals("B"))
						man.setInitTurn(Constant.BLACK);
					else if (value.equals("W"))
						man.setInitTurn(Constant.WHITE);

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
		if (log.isDebugEnabled())
			log.debug(props.get("RE"));
		if (log.isDebugEnabled())
			log.debug(props.get("SZ"));
		if (log.isDebugEnabled())
			log.debug(man.getInitBlacks());
		return list;
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
