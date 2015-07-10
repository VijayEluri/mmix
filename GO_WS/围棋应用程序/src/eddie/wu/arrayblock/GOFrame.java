package eddie.wu.arrayblock;

import java.awt.Event;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger
;

import eddie.wu.ui.applet.GoApplet;

/**
 * 
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
 * @author not attributable
 * @version 1.0
 */

public class GOFrame extends Frame {
	private static final Logger log = Logger.getLogger(GOFrame.class);
	private DatagramSocket s;
	private InetAddress hostAddress;
	private byte[] buf = new byte[3];
	private DatagramPacket dp = new DatagramPacket(buf, buf.length);
	// private int id;
	GoApplet goapplet = new GoApplet();

	public GOFrame() throws HeadlessException {
		// this.id = id;
		try {
			s = new DatagramSocket();
			hostAddress = InetAddress.getByName("localhost");
			log.debug(hostAddress.toString());

		} catch (UnknownHostException e) {

		} catch (SocketException e) {

		}

		add(goapplet);
	}

	public boolean mouseDown(Event e, int x, int y) { // 接受鼠标输入
		log.debug("chuanbodaorongqi");
		byte a = (byte) ((x - 4) / 28 + 1); // 完成数气提子等.
		byte b = (byte) ((y - 4) / 28 + 1);
		buf[0] = 1;
		buf[1] = a;
		buf[2] = b;
		try {
			log.debug("send");
			s.send(new DatagramPacket(buf, buf.length, hostAddress, 8080));
			log.debug("send");
		} catch (IOException excep) {
			excep.printStackTrace();
		}
		try {
			log.debug("re");
			s.receive(dp);
			log.debug("re");
		} catch (IOException excep) {
			excep.printStackTrace();
		}

		buf = dp.getData();
		log.debug("chulishuju");
		goapplet.goboard.cgcl(buf[1], buf[2]);
		goapplet.KEXIA = true;
		return true;
	}

	public static void main(String[] args) {
		GOFrame weiqi = new GOFrame();
		weiqi.setVisible(true);
	}

}
