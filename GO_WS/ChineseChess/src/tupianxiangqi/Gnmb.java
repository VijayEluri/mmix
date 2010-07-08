package tupianxiangqi;
import javax.swing.*;
import java.awt.*;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Gnmb extends JPanel{
  Image image1;
  public void paint(Graphics g){
    //Image work=this.createImage(150,512);
    g.drawImage(image1,0,0,150,512,this);
  }
  public Gnmb(){
    image1 = new ImageIcon(tupianxiangqi.Frame1.class.getResource(
      "pieceTex.jpg")).getImage();

  }
}