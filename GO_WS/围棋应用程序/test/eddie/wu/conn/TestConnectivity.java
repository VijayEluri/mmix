 package eddie.wu.conn;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.analy.StateAnalysis;
import eddie.wu.domain.conn.ConnectivityAnalysis;
import eddie.wu.manual.SimpleGoManual;
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
	 * see the benefit of having SGF.
	 */
	public void testendgamestate(){
		String file = "doc/围棋程序数据/网络棋谱/endgame math go 1.sgf";
		SimpleGoManual goManual = SGFGoManual.loadSimpleGoManual(file);
		BoardColorState sate = goManual.getInitState();
		if(log.isEnabledFor(Level.WARN)) log.warn("Black stone: "+goManual.getInitBlacks().size());
		if(log.isEnabledFor(Level.WARN)) log.warn("White stone: "+goManual.getInitWhites().size());
		
	}
	
	/**
	 * did not pass the test.
	 */
	public void test128(){
		log.setLevel(Level.DEBUG);
		SimpleGoManual goManual = SGFGoManual.loadSimpleGoManual(Constant.currentManual);
		ConnectivityAnalysis analysis = new ConnectivityAnalysis(goManual.getFinalState());
//		analysis.gete
	}

}
