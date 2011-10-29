package eddie.wu.manual;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

<<<<<<< HEAD
import eddie.wu.domain.BoardPoint;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;
import eddie.wu.linkedblock.BoardColorState;
=======
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;
import eddie.wu.linkedblock.BoardPoint;
>>>>>>> 3d8aa49ce83f747c9170d697ba2d051c700809f6

public class StateLoader {
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
			System.out.println(state.getBlackPoints());
			System.out.println(state.getWhitePoints());
			return state;
		}

		catch (IOException ex) {
			System.out.println("the input meet some trouble!");
			System.out.println("Exception" + ex.toString());
			throw new RuntimeException(ex);
		}

	}

	public static BoardColorState load(DataInputStream jmin) throws IOException {

		BoardColorState an = new BoardColorState();
		byte a, b, color;
		byte i, j;
		while (jmin.available() != 0) {
			a = jmin.readByte();
			b = jmin.readByte();
			color = jmin.readByte();
			an.add(new BoardPoint(Point.getPoint(b, a), color));

			if (a < 1 || a > 19 || b < 1 || b > 19 || color < 1 || color > 2) {
				if (Constant.DEBUG_CGCL) {
					System.out.print("载入的数据有误！" + a);
					System.out.print("i=" + a);
					System.out.print("j=" + b);
					System.out.println("color=" + color);
				}
			}

		}

		if (Constant.DEBUG_CGCL) {
			System.out.print("载入局面");
		}
		return an;
	}
}
