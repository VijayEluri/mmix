package eddie.wu.linkedblock;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.domain.Point;

public class LoadExercise {
	private static final Log log = LogFactory.getLog(GoBoard.class);

	public BoardColorState loadZhengZi() {
		BoardColorState state=null;
		try {
			DataInputStream in = new DataInputStream(new BufferedInputStream(
					new FileInputStream("D:\\WorkSpace\\GO_WS\\short链式棋块\\doc\\" + "征子")));

			state=zairujumian(in);

			in.close();
		}

		catch (IOException ex) {
			log.debug("the input meet some trouble!");
			log.debug("Exception" + ex.toString());
		}
		return state;
	}

	public BoardColorState zairujumian(DataInputStream jmin) throws IOException {
		BoardColorState state = new BoardColorState();
		byte a, b, color;
		
		while (jmin.available() != 0) {
			a = jmin.readByte();
			b = jmin.readByte();
			color = jmin.readByte();
			if (log.isDebugEnabled()) {
				log.debug("i=" + a);
				log.debug("j=" + b);
				log.debug("color=" + color);
			}
			if (a < 1 || a > 19 || b < 1 || b > 19 || color < 1 || color > 2) {
				if (log.isDebugEnabled()) {
					log.debug("载入的数据有误！" + a);
				}
				return null;
			} else {
				//old file is in format: column first.convert here
				state.add(new BoardPoint(new Point(b,a),color));
				//state.add(new BoardPoint(Point.getPoint(b,a),color));
			}

		}

		if (log.isDebugEnabled()) {
			log.debug("载入局面success");
		}
		return state;
	}
}
