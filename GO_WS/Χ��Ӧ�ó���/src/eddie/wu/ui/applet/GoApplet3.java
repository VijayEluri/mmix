package eddie.wu.ui.applet;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.arrayblock.GoBoard;
import eddie.wu.arrayblock.GoBoard1;
//����GoBoard1
public class GoApplet3 extends Applet {
	private static final Log log = LogFactory.getLog(GoApplet3.class);
   boolean KEXIA=true;
   boolean CHONGHUI=true;
   GoBoard go=new GoBoard();
   GoBoard1 goboard=new GoBoard1(go);
   public void update (Graphics g){
      paint(g);
   }
   public void  paint(Graphics g){
      if(CHONGHUI==true){
         g.setColor(Color.orange);
         g.fillRect(0,0,560,560);
      }
      CHONGHUI=true;

      //g.setColor(Color.yellow);
      //g.fillRect(0,0,560,560);
      g.setColor(Color.black) ;
      for (int i=1; i<=19; i++)//����
      {
         g.drawLine(18,28*i-10,522,28*i-10);//hor
         g.drawLine(28*i-10,18,28*i-10,522);//ver
      }
      //  log.debug("// paint the ver and hor line.");
      for (int i=0; i<3;i++){//����λ
         for ( int j=0; j<3;j++){
            g.fillOval(168*i+99,168*j+99,6,6);
         }
      }
        // log.debug("//paint the star point.");
      for(int i=1;i<=19;i++){//�����ӵ�
         for(int j=1;j<=19;j++){
            if(goboard.zb[i][j][0]==1) {
               g.setColor (Color.black);
               g.fillOval(28*i-24,28*j-24,28,28);
               //log.debug("//paint the black point.");
            }
            else  if(goboard.zb[i][j][0]==2){
               g.setColor(Color.white);
               g.fillOval(28*i-24,28*j-24,28,28);
               // log.debug("//paint the white point.");
            }
         }
      }//for: paint all the points owned by black and white.
      //��ǰ��������.
   }//else���������̺�����

   public boolean mouseDown(Event e,int x,int y){//�����������
      if(KEXIA==true){
         //KEXIA=false;//ֻ�л������һ��,���ܼ���.
         byte a=(byte)((x-4)/28+1);//����������ӵ�.
         byte b=(byte)((y-4)/28+1);
         goboard.cgcl(a,b);
         //log.debug("weiqiFrame de mousedown");
         repaint();

         log.debug("Gocanvas de mousedown");
         return false;//����������,��Frame����
      }
      else  return true;
   }

  public boolean handleEvent(Event evt){
      if(evt.id==Event.MOUSE_DOWN)  return mouseDown(evt,evt.x,evt.y);
      else return super.handleEvent(evt);
   }

   public GoApplet3() {
      super();
   }
}



