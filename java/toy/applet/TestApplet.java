package toy.applet;

import java.applet.Applet;
import java.awt.Graphics;

public class TestApplet extends Applet {

	@Override
	public void paint(Graphics g) {
		int x = 10;
		int y = 10;
		int width = 800;
		int height = 100;
		boolean raised = true;
		g.draw3DRect(x, y, width, height, raised);
		g.drawRect(x+10, y, width, height);
		
		raised = false;
		g.draw3DRect(x+20, y, width, height, raised);
		
		g.drawString("Old Dad Game", 34, 34);
		
		g.drawOval(x, y, width, height);
		super.paint(g);
	}

}
