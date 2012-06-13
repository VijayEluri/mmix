/*
 * Created on 2005-5-14
 *


 */
package eddie.wu.arrayblock;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;

import org.apache.log4j.Logger
;

import eddie.wu.domain.Constant;

/**
 * @author eddie
 *
 * TODO To change the template for this generated type comment go to

 */
public class GoBoard256Applet extends Applet { //用于画棋盘的画布
	private static final Logger log = Logger.getLogger(Goboard256TestApplet.class);
	GoBoard256 goBoard = new GoBoard256(Constant.BOARD_SIZE);
	  Image work;
	  Graphics g;
	  public void init() {
	    this.setBackground(Color.ORANGE);
	    work = this.createImage(560, 560);
	    if (work == null) {
	      log.debug("work==null");
	    }
	    else {
	      g = work.getGraphics();
	      log.debug("work!=null");
	    }
	  }

	  public void update(Graphics g) { //585
	    paint(g);
	  }

	  public void paint(Graphics gg) {
	    if (work == null) {
	      work = this.createImage(560, 560);
	      g = work.getGraphics();
	      log.debug("work==null");
	    }
	    else if (g == null) {
	      log.debug("work!=null;g==null");
	      g = work.getGraphics();
	    }
	    g.setColor(Color.orange);
	    g.fillRect(0, 0, 560, 560);
	    g.setColor(Color.black);
	    for (int i = 1; i <= 19; i++) { //画线
	      g.drawLine(18, 28 * i - 10, 522, 28 * i - 10); //hor
	      g.drawLine(28 * i - 10, 18, 28 * i - 10, 522); //ver
	    }
	    //  log.debug("// paint the ver and hor line.");
	    for (int i = 0; i < 3; i++) { //画星位
	      for (int j = 0; j < 3; j++) {
	        g.fillOval(168 * i + 99, 168 * j + 99, 6, 6);
	      }
	    }
	    // log.debug("//paint the star point.");
	    for (int i = 1; i <= 19; i++) { //画着子点
	      for (int j = 1; j <= 19; j++) {
	        if (goBoard.zb[i][j][0] == 1) {
	          g.setColor(Color.black);
	          g.fillOval(28 * i - 24, 28 * j - 24, 28, 28);
	          //log.debug("//paint the black point.");
	        }
	        else if (goBoard.zb[i][j][0] == 2) {
	          g.setColor(Color.white);
	          g.fillOval(28 * i - 24, 28 * j - 24, 28, 28);
	          // log.debug("//paint the white point.");
	        }
	      }
	    } //for: paint all the points owned by black and white.
	    //当前步的提子

	    gg.drawImage(work, 0, 0, this);

	  } //else画整个棋盘和棋子

	  public boolean mouseDown(Event e, int x, int y) { //接受鼠标输入

	    //KEXIA=false;//只有机器完成一手,才能继续.
	    byte a = (byte) ( (x - 4) / 28 + 1); //完成数气提子等.
	    byte b = (byte) ( (y - 4) / 28 + 1);
	    goBoard.cgcl(a, b);
	    // CHONGHUI=false;
	    repaint();
	    //log.debug("weiqiFrame de mousedown");
	    //repaint();
	    log.debug("Gocanvas de mousedown");
	    return true; //向容器传播,由Frame处理

	  }

	  public GoBoard256Applet() {
	    super();
	  }
	}
