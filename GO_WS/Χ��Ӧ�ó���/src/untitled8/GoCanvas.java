package untitled8;

import java.awt.*;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class GoCanvas extends Canvas {// ���ڻ����̵Ļ���
	boolean KEXIA = true;
	boolean CHONGHUI = true;
	boolean ZZ = true;
	GoBoard goboard = new GoBoard();
	JisuanBoard goji = new JisuanBoard(goboard);

	public void update(Graphics g) { // 585
		paint(g);
	}

	public void paint(Graphics g) {
		// goboard.paint(g) ;
		if (CHONGHUI == true) {
			g.setColor(Color.orange);
			g.fillRect(0, 0, 560, 560);
		}
		CHONGHUI = true;
		if (ZZ == true) {
			ZZ = false;
			g.setColor(Color.black);
			for (int i = 1; i <= 19; i++)// ����
			{
				g.drawLine(18, 28 * i - 10, 522, 28 * i - 10);// hor
				g.drawLine(28 * i - 10, 18, 28 * i - 10, 522);// ver
			}
			// System.out.println("// paint the ver and hor line.");
			for (int i = 0; i < 3; i++) {// ����λ
				for (int j = 0; j < 3; j++) {
					g.fillOval(168 * i + 99, 168 * j + 99, 6, 6);
				}
			}
			// System.out.println("//paint the star point.");
			for (int i = 1; i <= 19; i++) {// �����ӵ�
				for (int j = 1; j <= 19; j++) {
					if (goji.zb[i][j][0] == 1) {
						g.setColor(Color.black);
						g.fillOval(28 * i - 24, 28 * j - 24, 28, 28);
						// System.out.println("//paint the black point.");
					} else if (goji.zb[i][j][0] == 2) {
						g.setColor(Color.white);
						g.fillOval(28 * i - 24, 28 * j - 24, 28, 28);
						// System.out.println("//paint the white point.");
					}
				}
			}

		}

		g.setColor(Color.black);
		for (int i = 1; i <= 19; i++)// ����
		{
			g.drawLine(18, 28 * i - 10, 522, 28 * i - 10);// hor
			g.drawLine(28 * i - 10, 18, 28 * i - 10, 522);// ver
		}
		// System.out.println("// paint the ver and hor line.");
		for (int i = 0; i < 3; i++) {// ����λ
			for (int j = 0; j < 3; j++) {
				g.fillOval(168 * i + 99, 168 * j + 99, 6, 6);
			}
		}
		// System.out.println("//paint the star point.");
		for (int i = 1; i <= 19; i++) {// �����ӵ�
			for (int j = 1; j <= 19; j++) {
				if (goboard.zb[i][j][0] == 1) {
					g.setColor(Color.black);
					g.fillOval(28 * i - 24, 28 * j - 24, 28, 28);
					// System.out.println("//paint the black point.");
				} else if (goboard.zb[i][j][0] == 2) {
					g.setColor(Color.white);
					g.fillOval(28 * i - 24, 28 * j - 24, 28, 28);
					// System.out.println("//paint the white point.");
				}
			}
		}// for: paint all the points owned by black and white.

	}// else���������̺�����

	public boolean mouseDown(Event e, int x, int y) {// �����������
		if (KEXIA == true) {
			// KEXIA=false;//ֻ�л������һ��,���ܼ���.
			byte a = (byte) ((x - 4) / 28 + 1);// ����������ӵ�.
			byte b = (byte) ((y - 4) / 28 + 1);
			goboard.cgcl(a, b);
			repaint();
			// System.out.println("weiqiFrame de mousedown");
			// repaint();
			System.out.println("Gocanvas de mousedown");
			return false;// ����������,��Frame����

		} else
			return true;
	}

	public boolean handleEvent(Event evt) {
		if (evt.id == Event.MOUSE_DOWN)
			return mouseDown(evt, evt.x, evt.y);
		else
			return super.handleEvent(evt);
	}

	public GoCanvas() {
		super();
	}
}