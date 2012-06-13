package eddie.wu.arrayblock;
import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.TextField;

import org.apache.log4j.Logger;

import eddie.wu.domain.Constant;
import eddie.wu.domain.analy.SurviveAnalysis;
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
	private static final Logger log = Logger.getLogger(GoApplet.class);
   boolean KEXIA=true;
   boolean ZZ=false;
   JisuanBoard goji;
   ArrayGoBoard goboard1=new ArrayGoBoard(Constant.BOARD_SIZE);
   ArrayGoBoard goboard=new GoBoard1(goboard1);
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
      for (int i=1; i<=Constant.BOARD_SIZE; i++)//画线
      {
         g.drawLine(18,28*i-10,522,28*i-10);//hor
         g.drawLine(28*i-10,18,28*i-10,522);//ver
      }
      //  if(log.isDebugEnabled()) log.debug("// paint the ver and hor line.");
      for (int i=0; i<3;i++){//画星位
         for ( int j=0; j<3;j++){
            g.fillOval(168*i+99,168*j+99,6,6);
         }
      }
        // if(log.isDebugEnabled()) log.debug("//paint the star point.");
      for(int i=1;i<=Constant.BOARD_SIZE;i++){//画着子点
         for(int j=1;j<=Constant.BOARD_SIZE;j++){
            if(goji.zb[i][j][0]==1) {
               g.setColor (Color.black);
               g.fillOval(28*i-24,28*j-24,28,28);
               //if(log.isDebugEnabled()) log.debug("//paint the black point.");
            }
            else  if(goji.zb[i][j][0]==2){
               g.setColor(Color.white);
               g.fillOval(28*i-24,28*j-24,28,28);
               // if(log.isDebugEnabled()) log.debug("//paint the white point.");
            }
         }
      }

     }

      g.setColor(Color.black) ;
      for (int i=1; i<=Constant.BOARD_SIZE; i++)//画线
      {
         g.drawLine(18,28*i-10,522,28*i-10);//hor
         g.drawLine(28*i-10,18,28*i-10,522);//ver
      }
      //  if(log.isDebugEnabled()) log.debug("// paint the ver and hor line.");
      for (int i=0; i<3;i++){//画星位
         for ( int j=0; j<3;j++){
            g.fillOval(168*i+99,168*j+99,6,6);
         }
      }
        // if(log.isDebugEnabled()) log.debug("//paint the star point.");
      for(int i=1;i<=Constant.BOARD_SIZE;i++){//画着子点
         for(int j=1;j<=Constant.BOARD_SIZE;j++){
            if(goboard.zb[i][j][0]==1) {
               g.setColor (Color.black);
               g.fillOval(28*i-24,28*j-24,28,28);
               //if(log.isDebugEnabled()) log.debug("//paint the black point.");
            }
            else  if(goboard.zb[i][j][0]==2){
               g.setColor(Color.white);
               g.fillOval(28*i-24,28*j-24,28,28);
               // if(log.isDebugEnabled()) log.debug("//paint the white point.");
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
         //if(log.isDebugEnabled()) log.debug("weiqiFrame de mousedown");
         //repaint();
         if(log.isDebugEnabled()) log.debug("Gocanvas de mousedown");
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
   }
   public GoApplet() {
      super();
   }
}