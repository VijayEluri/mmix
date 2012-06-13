 package eddie.wu.conn;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Constant;
import eddie.wu.domain.analy.StateAnalysis;
import eddie.wu.domain.conn.ConnectivityAnalysis;
import eddie.wu.manual.GoManual;
import eddie.wu.manual.SGFGoManual;
import junit.framework.TestCase;

public class TestConnectivity extends TestCase{
	private transient static final Logger log = Logger
			.getLogger(ConnectivityAnalysis.class);

	
	public void test1(){
		String file = "doc/围棋程序数据/死活题局面/简单死活题/围棋天地_第一感教室36_1段以下.wjm";
				byte [][] state = StateAnalysis.LoadState(file);
		ConnectivityAnalysis analysis = new ConnectivityAnalysis(state);
		//analysis
		
		
	}
	
	/**
	 * did not pass the test.
	 */
	public void test128(){
		log.setLevel(Level.DEBUG);
		GoManual goManual = SGFGoManual.loadGoManual(Constant.currentManual);
		ConnectivityAnalysis analysis = new ConnectivityAnalysis(goManual.getFinalState());
//		analysis.gete
	}

}
