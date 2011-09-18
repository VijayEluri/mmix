package eddie.wu.other;

import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Color;

public class Test extends Applet {
	String theMessage = "test Applet go";

	public void setMessage(String message) {
		theMessage = message;
	}

	public String getMessage() {
		return theMessage;
	}

	public void paint(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(500, 0, 270, 530);
		g.fillRect(0, 340, 700, 190);
		g.setColor(Color.orange);
		g.fillRect(0, 0, 500, 500);// 33

		g.setColor(Color.black);
		g.drawString(theMessage, 50, 25);
		g.drawString("dahai", 100, 58);
		g.drawLine(1, 1, 120, 120);

	}

}
