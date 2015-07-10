package eddie.wu.ui.applet;

import java.awt.Button;
import java.awt.Event;
import java.awt.TextField;

import org.apache.log4j.Logger
;

import eddie.wu.arrayblock.ArrayGoBoard;
import eddie.wu.arrayblock.GoBoard1;
import eddie.wu.arrayblock.JisuanBoard;
import eddie.wu.domain.Constant;
import eddie.wu.ui.canvas.EmbedBoardCanvas;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: GoApplet 和GoApplet1的区别在于前者不直接处理鼠标事件，而是向容器传播
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class GoApplet extends EmbedBoardCanvas {// 用于画棋盘的画布
	private static final Logger log = Logger.getLogger(GoApplet.class);
	public boolean KEXIA = true;
	public boolean ZZ = false;
	JisuanBoard goji;
	ArrayGoBoard goboard1 = new ArrayGoBoard(Constant.BOARD_SIZE);
	public ArrayGoBoard goboard = new GoBoard1(goboard1);
	boolean CHONGHUI = true;
	Button zhzi = new Button("计算征子");
	TextField zzk = new TextField();

//	public void init() {
//		add(zzk);
//		add(zhzi);
//	}

	public boolean action(Event e, Object what) {
		if (e.target == zhzi) {
			goji = new JisuanBoard(goboard);
			byte kkk = (byte) Integer.parseInt(zzk.getText().toString());

			boolean acti = goji.yiqichi(kkk);
			ZZ = true;
			repaint();
			return acti;
		}
		return true;
	}

	public boolean handleEvent(Event evt) {
		if (evt.id == Event.MOUSE_DOWN)
			return mouseDown(evt, evt.x, evt.y);
		else
			return super.handleEvent(evt);
		// else return super.processEvent(evt);
	}

	// public void processEvent(AWTEvent evt){
	// if(evt.getID()==Event.MOUSE_DOWN) return
	// mouseDown(evt,evt.getSource().,evt.y);
	//
	// else return super.processEvent(evt);
	// }
	public GoApplet() {
		super();
	}
}