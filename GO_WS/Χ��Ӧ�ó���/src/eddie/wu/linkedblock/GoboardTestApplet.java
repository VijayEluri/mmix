/*
 * Created on 2005-4-22
 *


 */
package eddie.wu.linkedblock;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;
import eddie.wu.manual.ConvertGoManual;
import eddie.wu.manual.LoadGoManual;


/**
 * @author eddie 修改后用于打谱, 可以前进到指定步数. 可以转换方向.
 * 
 */
public class GoboardTestApplet

extends Applet {
	private static final Log log = LogFactory.getLog(GoboardTestApplet.class);

	public final boolean DEBUG = true;

	// boolean KEXIA=true;
	boolean CHONGHUI = true;

	boolean qizikuaihao;

	boolean qiziqishu;

	GoBoard goBoard = new GoBoard();

	public void update(Graphics g) {
		paint(g);
	}

	Image work;

	Graphics g;

	int shoushu = 0;

	int count = 0;
	
	//byte[] temp = LoadGoManual.loadOneFromAllGoManual(1, 453);
	byte[] temp = new LoadGoManual("../doc/围棋打谱软件/").loadSingleGoManual();
	public void init() {
		this.setBackground(Color.ORANGE);
		work = this.createImage(560, 560);
		if (work == null) {
			log.debug("work==null");
		} else {
			g = work.getGraphics();
			log.debug("work!=null");
		}
		Constant.DEBUG_CGCL = false;
		ConvertGoManual convert=new ConvertGoManual();
		temp= convert.convertFormat(temp);
//		for (int i = 1; i <= 229; i++) {
//			shoushu++;
//			log.debug("shoushu=" + shoushu);
//			log.debug("a=" + temp[count] + ",b=" + temp[count + 1]);
//			goBoard.forwardOneStep(temp[count++], temp[count++]);
//
//		}
//		Button forward=new Button ("Forward");
//		Button backward=new Button( "Backward");
//		Button toStep=new Button( "toStep");
//		TextField toWhichStep=new TextField();
//		this.add(forward);
//		this.add(backward);
//		this.add(toStep);
//		this.add(toWhichStep);
	}

	public void paint(Graphics gg) {
		if (work == null) {
			work = this.createImage(560, 560);
			g = work.getGraphics();
			log.debug("work==null");
		} else if (g == null) {
			log.debug("work!=null;g==null");
			g = work.getGraphics();
		}
		g.setColor(Color.orange);
		g.fillRect(0, 0, 560, 560);
		g.setColor(Color.black);
		short kinp = 0;
		for (int i = 1; i <= 19; i++) { // 画线
			g.drawLine(18, 28 * i - 10, 522, 28 * i - 10); // hor
			g.drawLine(28 * i - 10, 18, 28 * i - 10, 522); // ver
		}
		for (int i = 0; i < 3; i++) { // 画星位
			for (int j = 0; j < 3; j++) {
				g.fillOval(168 * i + 99, 168 * j + 99, 6, 6);
			}
		}

		BoardColorState boardState = goBoard.getBoardColorState();
		log.debug(boardState);
		Point point = null;
		for (java.util.Iterator iter = boardState.getBlackPoints().iterator(); iter
				.hasNext();) {
			point = (Point) iter.next();
			g.setColor(Color.black);
			g.fillOval(28 * point.getRow() - 24, 28 * point.getColumn() - 24,
					28, 28);
		}
		for (java.util.Iterator iter = boardState.getWhitePoints().iterator(); iter
				.hasNext();) {
			point = (Point) iter.next();
			g.setColor(Color.white);
			g.fillOval(28 * point.getRow()- 24, 28 * point.getColumn() - 24,
					28, 28);
		}

		gg.drawImage(work, 0, 0, this);
	} // else画整个棋盘和棋子

	public boolean mouseDown(Event e, int x, int y) { // 接受鼠标输入
		// if(KEXIA==true){
		// KEXIA=false;//只有机器完成一手,才能继续.
		log.debug("方法 mousedown");
		if (count >= temp.length)
			return true;
		byte a = (byte) ((x - 4) / 28 + 1); // 完成数气提子等.
		byte b = (byte) ((y - 4) / 28 + 1);
		a = temp[count++];
		b = temp[count++];
		shoushu++;
		goBoard.oneStepForward(a, b);
		if (DEBUG == true) {
			goBoard.output();
		}
		repaint();
		log.debug("方法 mousedown");
		return true; // 向容器传播,由Frame处理
		// }
		// else return true;
	}

	public boolean handleEvent(Event evt) {
		if (evt.id == Event.MOUSE_DOWN) {
			return mouseDown(evt, evt.x, evt.y);
		} else {
			return super.handleEvent(evt);
		}
	}

	public GoboardTestApplet() {
		super();
	}
}
