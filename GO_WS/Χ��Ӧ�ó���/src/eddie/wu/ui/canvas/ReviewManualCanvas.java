package eddie.wu.ui.canvas;

import java.awt.Graphics;
import java.util.List;

import eddie.wu.domain.Constant;
import eddie.wu.domain.UIPoint;

public class ReviewManualCanvas extends BasicBoardCanvas {
	List<UIPoint> points;
	int lastMoveNumber;
	public void setLastMoveNumber(int lastMoveNumber) {
		this.lastMoveNumber = lastMoveNumber;
	}

	public void setPoints(List<UIPoint> points) {
		this.points = points;
	}

	public void paint(Graphics g) {
		System.out.println("ReviewManualCanvas paint!");
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
				drawBlackPoint(g, step.getPoint());
			} else if (step.getColor() == Constant.WHITE) {
				drawWhitePoint(g, step.getPoint());
			}
			if(step.getMoveNumber()>0){
				drawMoveNumber(g, step.getPoint(), step.getMoveNumber()-this.lastMoveNumber);
			}
		}
	}



}
