package eddie.wu.manual;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.BoardPoint;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;

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

		BoardColorState an = new BoardColorState(Constant.BOARD_SIZE);
		byte a, b, color;
		byte i, j;
		while (jmin.available() != 0) {
			a = jmin.readByte();
			b = jmin.readByte();
			color = jmin.readByte();
			an.add(new BoardPoint(Point.getPoint(Constant.BOARD_SIZE, a, b),
					color));

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
		return an;
	}
}
