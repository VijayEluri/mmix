/*
 * Created on 2005-5-7
 *


 */
package eddie.wu.ui;



/**
 * @author eddie
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class GoBoardDebugApplet extends GoBoardApplet {
	public void init() {
		super.init();
		int j = 0;
		for (int i = 1; i <= DebugData.shoushu; i++)
			goBoard.oneStepForward(DebugData.a[j++], DebugData.a[j++]);
	}
}