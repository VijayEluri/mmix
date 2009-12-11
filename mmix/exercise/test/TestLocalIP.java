package test;
import java.net.InetAddress;
import java.net.UnknownHostException;

 
 public class TestLocalIP {
	public static void main(String[] args) {
		InetAddress[] all=null;
		try {
			System.out.println(" " + InetAddress.getLocalHost());
			System.out.println(" " + InetAddress.getLocalHost().getHostName());
			System.out.println(" " + InetAddress.getLocalHost().getHostAddress());
			System.out.println(" " + InetAddress.getLocalHost().getCanonicalHostName());
			all = InetAddress
					.getAllByName(InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < all.length; i++) {
			System.out.println( " i= " + i + "; " + all[i].getHostAddress());
		}
	}
}
