package eddie.wu.ui;

import java.awt.Frame;
/**
 * initialize to a state, then record variant after it.
 * classical problem solving such as dead - live problem, fighting problem.
 * store them in SGF format.
 * @author wueddie-wym-wrz
 *
 */
public class RecordStateAndManual extends Frame {
	public static void main(String[] args){
		RecordState weiqi = new RecordState();
		weiqi.setVisible(true);
		weiqi.setBounds(0, 0, 800, 600);
		
		
		
	}
}
