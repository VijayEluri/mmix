package eddie.wu.ui.applet;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;

/**
 * Fix 1 - conversion between matrix and plane coordinate.
 * @author wueddie-wym-wrz
 *
 */
public class EmbedBoardCanvas extends Canvas {
	byte[][] state;
	boolean repaint;

	public void setRepaint(boolean repaint) {
		this.repaint = repaint;
	}

	public void setState(byte[][] zb) {
		this.state = zb;
	}

	public void update(Graphics g) { // 585
		paint(g);
	}

	public void paint(Graphics g) {
		System.out.println("EmbedBoardCanvas paint!");	
		drawBackgroud(g);		

		drawBlankBoard(g);
		
		
		drawPoints(g,state);
			// 当前步的提子.
	}// else画整个棋盘和棋子

	private void drawPoints(Graphics g, byte[][] zb) {
		
		for (int i = 1; i <= 19; i++) {// 画着子点
			for (int j = 1; j <= 19; j++) {
				if (zb[i][j] == 1) {
					g.setColor(Color.black);
					// coordinate difference between matrix and plane.
					g.fillOval(28 * j - 24, 28 * i - 24, 28, 28);
					// log.debug("//paint the black point.");
				} else if (zb[i][j] == 2) {
					g.setColor(Color.white);
					g.fillOval(28 * j - 24, 28 * i - 24, 28, 28);
					// log.debug("//paint the white point.");
				}
			}
		}// for: paint all the points owned by black and white.
	}

	private void drawBlankBoard(Graphics g) {
		g.setColor(Color.black);
		for (int i = 1; i <= 19; i++)// 画线
		{
			g.drawLine(18, 28 * i - 10, 522, 28 * i - 10);// horizontal
			g.drawLine(28 * i - 10, 18, 28 * i - 10, 522);// vertical
		}
		// log.debug("// paint the ver and hor line.");
		for (int i = 0; i < 3; i++) {// 画星位
			for (int j = 0; j < 3; j++) {
				g.fillOval(168 * i + 99, 168 * j + 99, 6, 6);
			}
		}
		// log.debug("//paint the star point.");
	}

	private void drawBackgroud(Graphics g) {
		if (repaint == true) {
			g.setColor(Color.orange);
			g.fillRect(0, 0, 560, 560);
		}		
	}

	public boolean mouseDown(Event e, int x, int y) {// 接受鼠标输入

		return false;// 向容器传播,由Frame处理

	}
}
