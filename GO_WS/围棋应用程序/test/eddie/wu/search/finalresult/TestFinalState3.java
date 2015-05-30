package eddie.wu.search.finalresult;

import java.util.Arrays;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Block;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.global.GoBoardSearch;
import eddie.wu.search.global.ListAllState;
import eddie.wu.search.global.ThreeThreeBoardSearch;

public class TestFinalState3 extends TestCase {
	private static Logger log = Logger.getLogger(TestFinalState3.class);
	
	static{
		Logger.getLogger(TerritoryAnalysis.class).setLevel(Level.WARN);
	}

//	public void testAllFinalState() {
//		int count = 0;
//		Set<BoardColorState> finalState = new ListAllState().getFinalState(3);
//
//		for (BoardColorState state : finalState) {
//			count++;
//			System.out.print("count=" + count);
//			System.out.print(" State=" + state.getStateString());
//			System.out.print("Score=" + state.getScore());
//			if (log.isEnabledFor(Level.WARN))
//				log.warn("variant=" + state.getVariant());
//		}
//	}

	public void testFinal2() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[W, B, B]");
		text[2] = new String("[_, B, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		TerritoryAnalysis analysis = new TerritoryAnalysis(state);
		boolean state2 = analysis.isFinalState_deadExist();
		assertTrue(state2);
		assertEquals(9, analysis.finalResult_deadExist().getScore());

	}
	public void testFinal1() {
		String[] text = new String[3];
		text[0] = new String("[W, W, W]");
		text[1] = new String("[W, B, B]");
		text[2] = new String("[_, B, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		TerritoryAnalysis analysis = new TerritoryAnalysis(state);
		boolean state2 = analysis.isFinalState_deadExist();
		assertTrue(state2);
		assertEquals(9, analysis.finalResult_deadExist().getScore());

	}
	public void testFinal11() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[W, B, B]");
		text[2] = new String("[_, B, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		TerritoryAnalysis analysis = new TerritoryAnalysis(state);
		boolean state2 = analysis.isFinalState_deadExist();
		assertTrue(state2);
		assertEquals(9, analysis.finalResult_deadExist().getScore());

	}
	//WARN  (TestAllState3.java:566) - [INIT]W-->[2,2]B-->[2,3]W-->[3,2]B-->[1,2]W-->[2,1](FINAL 0)
	public void testFinal12() {
		String[] text = new String[3];
		text[0] = new String("[B, B, _]");
		text[1] = new String("[W, W, B]");
		text[2] = new String("[_, W, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		TerritoryAnalysis analysis = new TerritoryAnalysis(state,Constant.BLACK);
		boolean state2 = analysis.isFinalState_deadCleanedUp();
		assertTrue(state2);
		 state2 = analysis.isFinalState_deadExist();
		assertTrue(state2);
		assertEquals(9, analysis.finalResult_deadExist().getScore());

	}
	
	public void testState_317() {
		String[] text = new String[3];
		text[0] = new String("[_, _, _]");
		text[1] = new String("[_, B, B]");
		text[2] = new String("[B, B, B]");
		testFinalState(text, false, false);
	}
	
	//[INIT]W-->[1,1]B-->[1,3]W-->[1,2]B-->[PAS]W-->[1,1]B-->[3,1]W-->[3,2]B-->[2,3]W-->[2,1](FINAL 1)
	public void testState_3171() {
		String[] text = new String[3];
		text[0] = new String("[W, W, _]");
		text[1] = new String("[W, _, B]");
		text[2] = new String("[_, W, _]");
		testFinalState(text, false, false);
	}

	public void testState_312() {
		String[] text = new String[3];
		text[0] = new String("[_, W, W]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, _]");
		testFinalState(text, false, true);
	}
	
	public void testState_3131() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[B, _, W]");
		text[2] = new String("[_, W, _]");
		testFinalState(text, false, false);
	}
	
	public void testState_311() {
		String[] text = new String[3];
		text[0] = new String("[B, B, _]");
		text[1] = new String("[B, B, B]");
		text[2] = new String("[_, B, B]");
		testFinalState(text, true, true);
	}

	public void testState_313() {
		String[] text = new String[3];
		text[0] = new String("[_, W, W]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, W]");
		testFinalState(text, false, true);
	}

	public void testFinalState_315() {
		String[] text = new String[3];
		text[0] = new String("[_, B, _]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, W]");
		testFinalState(text, false, true);
	}
	
	/**
	 * important exceptional case, the W is live (accurately co-live if enemy play first, pure live
	 * if itself play first.), but the status should be not finalized.<br/>
	 * but i looks like 
	 */
	public void testState_319() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[_, _, _]");
		text[2] = new String("[_, _, _]");
		testFinalState(text, false, false);
	}
	
	public void testState_320() {
		String[] text = new String[3];
		text[0] = new String("[_, W, _]");
		text[1] = new String("[B, B, W]");
		text[2] = new String("[_, W, _]");
		testFinalState(text, false, true);
	}
	
	public void testState_220() {
		String[] text = new String[2];
		text[0] = new String("[W, _]");
		text[1] = new String("[_, W]");
		testFinalState(text, true, true);
	}

	public void testFinalState(String[] text, boolean deadCleanedUp,
			boolean deadExist) {
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis go = new TerritoryAnalysis(state);
		boolean finalState = go
				.isFinalState_deadCleanedUp();
		assertTrue(finalState == deadCleanedUp);
		
		if(finalState){
			System.out.println("Clean up: score:"+go.finalResult_deadCleanedUp().getScore());
			return;//no need to check dead exist case.
		}

		finalState = go.isFinalState_deadExist();
		assertTrue(finalState == deadExist);
		if(finalState){
			System.out.println("Dead Exist score:"+go.finalResult_deadExist().getScore());
		}
	}

}
