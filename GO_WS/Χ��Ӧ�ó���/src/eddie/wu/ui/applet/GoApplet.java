package eddie.wu.ui.applet;

import java.awt.Button;
import java.awt.Event;
import java.awt.TextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.arrayblock.GoBoard;
import eddie.wu.arrayblock.GoBoard1;
import eddie.wu.arrayblock.JisuanBoard;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description: GoApplet ��GoApplet1����������ǰ�߲�ֱ�Ӵ�������¼�����������������
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

public class GoApplet extends EmbedBoardCanvas {// ���ڻ����̵Ļ���
	private static final Log log = LogFactory.getLog(GoApplet.class);
	public boolean KEXIA = true;
	public boolean ZZ = false;
	JisuanBoard goji;
	GoBoard goboard1 = new GoBoard();
	public GoBoard goboard = new GoBoard1(goboard1);
	boolean CHONGHUI = true;
	Button zhzi = new Button("��������");
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