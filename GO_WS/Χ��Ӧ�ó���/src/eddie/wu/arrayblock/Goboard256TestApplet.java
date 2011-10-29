/*
 * Created on 2005-5-14
 *


 */
package eddie.wu.arrayblock;

import java.awt.Color;
import java.awt.Event;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.manual.LoadGMDGoManual;

/**
 * @author eddie
 *
 * TODO To change the template for this generated type comment go to

 */
public class Goboard256TestApplet 

extends GoBoard256Applet {
	private static final Log log=LogFactory.getLog(Goboard256TestApplet.class);
	int shoushu = 0;

	int count = 0;

	byte[] temp = new LoadGMDGoManual("../doc/Χ��������").loadOneFromAllGoManual(1,453);

	public void init() {
		this.setBackground(Color.ORANGE);
		work = this.createImage(560, 560);
		if (work == null) {
			log.debug("work==null");
		} else {
			g = work.getGraphics();
			log.debug("work!=null");
		}
		//Constant.DEBUG_CGCL = false;
		for (int i = 1; i <= 229; i++) {
			shoushu++;
			log.debug("shoushu=" + shoushu);
			log.debug("a=" + temp[count] + ",b=" + temp[count + 1]);
			goBoard.cgcl(temp[count++], temp[count++]);

		}
	}
	public boolean mouseDown(Event e, int x, int y) { //�����������
	    //if(KEXIA==true){
	    //KEXIA=false;//ֻ�л������һ��,���ܼ���.
	    log.debug("���� mousedown");
	    if(count>=temp.length) return true;
	    byte a = (byte) ( (x - 4) / 28 + 1); //����������ӵ�.
	    byte b = (byte) ( (y - 4) / 28 + 1);
	    a=temp[count++];
	    b=temp[count++];
	    shoushu++;
	    goBoard.cgcl( a,b);
//	    if (Constant.\== true) {
//	      goBoard.output();
//	    }
	    repaint();
	    log.debug("���� mousedown");
	    return true; //����������,��Frame����
	    //}
	    // else  return true;
	  }
}
