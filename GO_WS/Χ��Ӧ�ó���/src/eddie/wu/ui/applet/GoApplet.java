package eddie.wu.ui.applet;
import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.TextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.arrayblock.GoBoard;
import eddie.wu.arrayblock.GoBoard1;
import eddie.wu.arrayblock.JisuanBoard;
/**
 * <p>Title: </p>
 * <p>Description:
 * GoApplet
 * 和GoApplet1的区别在于前者不直接处理鼠标事件，而是向容器传播</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class GoApplet extends Applet{//用于画棋盘的画布
	private static final Log log=LogFactory.getLog(GoApplet.class);
  public boolean KEXIA=true;
  public boolean ZZ=false;
   JisuanBoard goji;
   GoBoard goboard1=new GoBoard();
  public GoBoard goboard=new GoBoard1(goboard1);
   boolean CHONGHUI=true;
   Button zhzi=new Button("计算征子");
   TextField zzk=new TextField();
   public void init(){
      add(zzk);
      add(zhzi);
   }
   public void update (Graphics g){   //585
      paint(g);
   }
   public void  paint(Graphics g){
     if(CHONGHUI==true){
        g.setColor(Color.orange);
        g.fillRect(0,0,560,560);
     }
     CHONGHUI=true;
     if(ZZ==true){
       ZZ=false;
       g.setColor(Color.black) ;
      for (int i=1; i<=19; i++)//画线
      {
         g.drawLine(18,28*i-10,522,28*i-10);//hor
         g.drawLine(28*i-10,18,28*i-10,522);//ver
      }
      //  log.debug("// paint the ver and hor line.");
      for (int i=0; i<3;i++){//画星位
         for ( int j=0; j<3;j++){
            g.fillOval(168*i+99,168*j+99,6,6);
         }
      }
        // log.debug("//paint the star point.");
      for(int i=1;i<=19;i++){//画着子点
         for(int j=1;j<=19;j++){
            if(goji.zb[i][j][0]==1) {
               g.setColor (Color.black);
               g.fillOval(28*i-24,28*j-24,28,28);
               //log.debug("//paint the black point.");
            }
            else  if(goji.zb[i][j][0]==2){
               g.setColor(Color.white);
               g.fillOval(28*i-24,28*j-24,28,28);
               // log.debug("//paint the white point.");
            }
         }
      }

     }

      g.setColor(Color.black) ;
      for (int i=1; i<=19; i++)//画线
      {
         g.drawLine(18,28*i-10,522,28*i-10);//hor
         g.drawLine(28*i-10,18,28*i-10,522);//ver
      }
      //  log.debug("// paint the ver and hor line.");
      for (int i=0; i<3;i++){//画星位
         for ( int j=0; j<3;j++){
            g.fillOval(168*i+99,168*j+99,6,6);
         }
      }
        // log.debug("//paint the star point.");
      for(int i=1;i<=19;i++){//画着子点
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
      //当前步的提子.
   }//else画整个棋盘和棋子

   public boolean mouseDown(Event e,int x,int y){//接受鼠标输入
      if(KEXIA==true){
         KEXIA=false;//只有机器完成一手,才能继续.
         byte a=(byte)((x-4)/28+1);//完成数气提子等.
         byte b=(byte)((y-4)/28+1);
         goboard.cgcl(a,b);
         CHONGHUI=false;
         repaint();
         //log.debug("weiqiFrame de mousedown");
         //repaint();
         log.debug("Gocanvas de mousedown");
         return false;//向容器传播,由Frame处理

      }
      else  return true;
   }
    public boolean action(Event e,Object what){
      if(e.target==zhzi){
        goji=new JisuanBoard(goboard);
        byte kkk=(byte)Integer.parseInt(zzk.getText().toString());

        boolean acti= goji.yiqichi(kkk);
        ZZ=true;
        repaint();
        return acti;
      }
      return true;
    }
   public boolean handleEvent(Event evt){
      if(evt.id==Event.MOUSE_DOWN)  return mouseDown(evt,evt.x,evt.y);
      else return super.handleEvent(evt);
      //else return super.processEvent(evt);
   }
//   public void processEvent(AWTEvent evt){
//	      if(evt.getID()==Event.MOUSE_DOWN)  return mouseDown(evt,evt.getSource().,evt.y);
//	      
//	      else return super.processEvent(evt);
//	   }
   public GoApplet() {
      super();
   }
}