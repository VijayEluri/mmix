package eddie.wu.ui.canvas;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.util.List;

import eddie.wu.domain.Point;
import eddie.wu.domain.Step;

/**
 * Fix 1 - conversion between matrix and plane coordinate.
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class EmbedBoardCanvas extends BasicBoardCanvas {
	byte[][] state;

	List<Step> steps;

	public void setSteps(List<Step> steps) {
		this.steps = steps;
	}

	public void setState(byte[][] zb) {
		this.state = zb;
	}

	

	public void paint(Graphics g) {
		System.out.println("EmbedBoardCanvas paint!");
		drawBackgroud(g);
		drawBlankBoard(g);
		drawPoints(g, state);
	}

	private void drawPoints(Graphics g, byte[][] zb) {

		for (int i = 1; i <= 19; i++) {// »­×Å×Óµã
			for (int j = 1; j <= 19; j++) {
				if (zb[i][j] == 1) {
					super.drawBlackPoint(g, i, j);
				} else if (zb[i][j] == 2) {
					super.drawWhitePoint(g, i, j);
				}
			}
		}// for: paint all the points owned by black and white.

		if (this.steps == null || this.steps.isEmpty())
			return;

		int stepCount = 0;
		for (Step step : steps) {
			stepCount++;
			drawMoveNumber(g, step.getPoint(), stepCount);			
		}
	}

	

	
}
