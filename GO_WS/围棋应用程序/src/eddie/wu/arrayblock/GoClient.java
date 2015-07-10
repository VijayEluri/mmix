package eddie.wu.arrayblock;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class GoClient extends Thread {
	private DatagramSocket s;
	private InetAddress hostAddress;
	private byte[] buf = new byte[3];
	private DatagramPacket dp = new DatagramPacket(buf, buf.length);
	private int id;

	public GoClient(int id) {
		this.id = id;
		try {
			s = new DatagramSocket();
			hostAddress = InetAddress.getByName("localhost");

		} catch (UnknownHostException e) {

		} catch (SocketException e) {

		}

	}

	public void run() {
		try {
			for (byte i = 0; i < 10; i++) {
				// s.send(new DatagramPacket(buf, buf.length, hostAddress,
				// GoServer.INPORT));
				s.receive(dp);
				buf = dp.getData();

			}
		} catch (IOException e) {

		}

	}
}