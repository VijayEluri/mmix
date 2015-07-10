package eddie.wu.ui.canvas;

import java.awt.Graphics;
import java.util.List;

import org.apache.log4j.Logger;

import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Step;
import eddie.wu.ui.applet.SiHuoYanShiApplet;
import eddie.wu.util.BoardGraphic;

/**
 * Fix 1 - conversion between matrix and plane coordinate.
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class EmbedBoardCanvas extends BasicBoardCanvas {
	private static final Logger log = Logger.getLogger(EmbedBoardCanvas.class);
	private byte[][] state;
	List<Step> steps;

	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}

	public void setState(byte[][] zb) {
		this.state = zb;
		this.boardSize = zb.length - 2;
	}

	@Override
	public void paint(Graphics g) {
		if(log.isDebugEnabled()) log.debug("EmbedBoardCanvas paint!");
		drawBackgroud(g);
		drawBlankBoard(g);
		drawPoints(g, state);
	}

	private void drawPoints(Graphics g, byte[][] zb) {
		BoardGraphic.drawPoints(g, zb);

//		for (int i = 1; i <= boardSize; i++) {// 画着子点
//			for (int j = 1; j <= boardSize; j++) {
//				if (zb[i][j] == ColorUtil.BLACK || zb[i][j] == ColorUtil.WHITE)
//					super.drawPoint(g, i, j, zb[i][j]);
//
//			}
//		}// for: paint all the points owned by black and white.

		if (this.steps == null || this.steps.isEmpty())
			return;

		int stepCount = 0;
		for (Step step : steps) {
			stepCount++;
			drawMoveNumber(g, step.getPoint(), stepCount);
		}
	}

}
