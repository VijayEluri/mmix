package eddie.wu.manual;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;

public class StateLoader {
	private static final Logger log = Logger.getLogger(StateLoader.class);

	/**
	 * 从以前制作的征子题目文件中得到ColorBoardState
	 * 
	 * @param jmin
	 * @throws IOException
	 */
	public static BoardColorState load(String fileName) {

		BoardColorState state;
		try {
			DataInputStream in = new DataInputStream(new BufferedInputStream(
					new FileInputStream(fileName)));
			state = load(in);
			in.close();
			if (log.isDebugEnabled())
				log.debug(state.getBlackPoints());
			if (log.isDebugEnabled())
				log.debug(state.getWhitePoints());
			return state;
		}

		catch (IOException ex) {
			if (log.isDebugEnabled())
				log.debug("the input meet some trouble!");
			if (log.isDebugEnabled())
				log.debug("Exception" + ex.toString());
			throw new RuntimeException(ex);
		}

	}

	public static BoardColorState load(DataInputStream jmin) throws IOException {

		int length = Constant.BOARD_SIZE + 2;
		byte[][] state = new byte[length][length];
		byte a, b, color;
		byte i, j;
		while (jmin.available() != 0) {
			a = jmin.readByte();
			b = jmin.readByte();
			color = jmin.readByte();
			state[a][b] = color;
			// an.add(new BoardPoint(Point.getPoint(Constant.BOARD_SIZE, a, b),
			// color));

			if (a < 1 || a > 19 || b < 1 || b > 19 || color < 1 || color > 2) {
				if (Constant.DEBUG_CGCL) {
					System.out.print("载入的数据有误！" + a);
					System.out.print("i=" + a);
					System.out.print("j=" + b);
					if (log.isDebugEnabled())
						log.debug("color=" + color);
				}
			}

		}

		if (Constant.DEBUG_CGCL) {
			System.out.print("载入局面");
		}
		return BoardColorState.getInstance(state, Constant.BLACK);
	}

	/**
	 * to ease the testing, use a text file to store status. <br/>
	 * 4<br/>
	 * [B, B, _, B]<br/>
	 * [B, B, B, W]<br/>
	 * [B, B, B, _]<br/>
	 * [B, B, B, B]<br/>
	 * 
	 * @param inname
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static byte[][] LoadStateFromTextFile(String inname) {

		byte[][] state = null;
		try {
			DataInputStream in = new DataInputStream(new BufferedInputStream(
					new FileInputStream(inname)));

			String line = in.readLine();
			int boardSize = Integer.valueOf(line).intValue();
			state = new byte[boardSize + 2][boardSize + 2];
			for (int i = 1; i <= boardSize; i++) {
				line = in.readLine();
				int index = 0;
				for (int j = 1; j <= boardSize; j++) {
					index = j * 3 - 2;
					if (line.charAt(index) == ColorUtil.BLACK_STRING) {
						state[i][j] = Constant.BLACK;
					} else if (line.charAt(index) == ColorUtil.WHITE_STRING) {
						state[i][j] = Constant.WHITE;
					}
				}

			}
			in.close();
		}

		catch (IOException ex) {
			log.debug("the input meet some trouble!");
			log.debug("Exception" + ex.toString());
		}
		return state;
	}

	/**
	 * for blank point, any character other than B and W are OK.<br/>
	 * client example:<br/>
	 * String[] text = new String[3];<br/>
	 * text[0] = new String("[B, W, _]");<br/>
	 * text[1] = new String("[B, B, W]");<br/>
	 * text[2] = new String("[_, B, _]");<br/>
	 * board size is not necessary to be explicitly defined.
	 * 
	 * @param textLines
	 * @return
	 */
	public static byte[][] LoadStateFromText(String[] textLines) {
		int boardSize = textLines.length;
		byte[][] state = new byte[boardSize + 2][boardSize + 2];

		String line;
		for (int i = 1; i <= boardSize; i++) {
			line = textLines[i - 1];
			int index = 0;
			for (int j = 1; j <= boardSize; j++) {
				index = j * 3 - 2;
				if (line.charAt(index) == ColorUtil.BLACK_STRING) {
					state[i][j] = Constant.BLACK;
				} else if (line.charAt(index) == ColorUtil.WHITE_STRING) {
					state[i][j] = Constant.WHITE;
				}
			}
		}

		return state;
	}
}
