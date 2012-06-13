package eddie.wu.arrayblock;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import org.apache.log4j.Logger
;

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
 * @author not attributable
 * @version 1.0
 */

public class GoServer {
	private static final Logger log = Logger.getLogger(GoServer.class);
	private DatagramSocket s;
	byte[] buf = new byte[3];
	DatagramPacket dp = new DatagramPacket(buf, buf.length);
	byte ra, rb;

	public GoServer() {
		try {
			s = new DatagramSocket(8080);
			log.debug("Server start");
			while (true) {
				s.receive(dp);
				buf[1] = (byte) (Math.random() * 19 + 1);
				buf[2] = (byte) (Math.random() * 19 + 1);
				s.send(new DatagramPacket(buf, buf.length, dp.getAddress(), dp
						.getPort()));
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}