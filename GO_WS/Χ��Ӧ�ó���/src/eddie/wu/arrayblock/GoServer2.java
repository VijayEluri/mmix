package eddie.wu.arrayblock;

import java.io.*;
import java.net.*;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class GoServer2 {
  private DatagramSocket s;
  byte [] buf =new byte[3];
  DatagramPacket dp=new DatagramPacket(buf,buf.length);
  byte ra,rb;
  public GoServer2() {
    try {
      s = new DatagramSocket(8080);
      System.out.print("Server start") ;
      while (true){
        s.receive(dp) ;
        buf[1]=(byte)(Math.random() *19+1);
        buf[2]=(byte)(Math.random() *19+1);
        s.send(new DatagramPacket(buf,buf.length ,dp.getAddress(),dp.getPort()));
      }
    }
    catch (SocketException e) {
      e.printStackTrace();
    }catch(IOException e){
      e.printStackTrace();
    }
  }
}