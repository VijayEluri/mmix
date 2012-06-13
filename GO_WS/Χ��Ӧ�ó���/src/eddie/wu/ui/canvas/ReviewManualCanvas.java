package eddie.wu.ui.canvas;

import java.awt.Graphics;
import java.util.List;

import org.apache.log4j.Logger;

import eddie.wu.domain.Constant;
import eddie.wu.domain.UIPoint;

public class ReviewManualCanvas extends BasicBoardCanvas {
	private static final Logger log = Logger.getLogger(EmbedBoardCanvas.class);
	List<UIPoint> points;

	// int lastMoveNumber;

	// public void setLastMoveNumber(int lastMoveNumber) {
	// this.lastMoveNumber = lastMoveNumber;
	// }
	public ReviewManualCanvas(int boardSize) {
		this.boardSize = boardSize;
	}

	public void setPoints(List<UIPoint> points) {
		this.points = points;
	}

	public void paint(Graphics g) {
		if (log.isDebugEnabled())
			log.debug("ReviewManualCanvas paint!");
		drawBackgroud(g);
		drawBlankBoard(g);
		drawPoints(g, points);
	}

	/**
	 * 
	 * @param g
	 * @param zb
	 */
	private void drawPoints(Graphics g, List<UIPoint> points) {

		for (UIPoint step : points) {
			if (step.getColor() == Constant.BLACK) {
				drawBlackPoint(g, step.getPoint(), step.isEaten());
			} else if (step.getColor() == Constant.WHITE) {
				drawWhitePoint(g, step.getPoint(), step.isEaten());
			}
			if (step.getMoveNumber() > 0) {
				// display real number instead of always starting from 1 in each
				// page.
				drawMoveNumber(g, step.getPoint(), step.getMoveNumber(),
						step.getColor());

				// drawMoveNumber(g, step.getPoint(), step.getMoveNumber()
				// - this.lastMoveNumber, step.getColor());
			}
		}
	}

}
