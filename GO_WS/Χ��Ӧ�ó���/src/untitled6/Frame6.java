package untitled6;
import javax.swing.*;
import java.awt.*;
//import
public class Frame6 extends JFrame{
  JButton jButton1 = new JButton();
  JButton jButton2 = new JButton();
  JButton jButton3 = new JButton();
  Container jpanel;
  SiHuoZhiZuo shzz=new SiHuoZhiZuo();
  JButton jButton4 = new JButton();
  JButton jButton5 = new JButton();
  public Frame6() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    jButton3.setText("jButton3");
    jButton2.setText("jButton2");
    jButton1.setText("»ÚÆå");
    jpanel= this.getContentPane();

    jButton4.setText("jButton4");
    jButton5.setText("jButton5");
    jpanel.add(jButton1) ;
    jpanel.add(jButton2) ;
    jpanel.add(jButton3) ;
       //jpanel.add(jButton1) ;
    jpanel.add(shzz, BorderLayout.CENTER);
    shzz.add(jButton4, null);
    jpanel.add(jButton5, BorderLayout.NORTH);
    setSize(800,600);
    setVisible(true);
  }
  public  static void main(String args){
    new Frame6();
  }
}