package eddie.wu.search.two;

import junit.framework.TestCase;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.small.TwoTwoBoardSearch;
/**
 * 需要不同的log策略：单元测试log需要尽量详细，便于定位问题。集成测试log需要尽量简单，提升性能。 <br/>
 * 目前的解决办法是建立以_IT为后缀的父类。使用默认的Error级别减少log <br/>
 * 同时建立普通测试类用于单元测试，后者的代码需要手工改变log级别。<br/>
 * 不足之处在于测试代码卸载_IT类中，不太自然。
 * 如果更为全局的测试，比如suite则应当包含_IT子类。
 * 
 * @author think
 *
 */
public class VerifyTwoTwo_IT extends TestCase {
	public void testInitState() {
		String[] text = new String[2];
		text[0] = new String("[_, _]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState stateB = new BoardColorState(state, Constant.BLACK);
		int score_blackTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(1, score_blackTurn);

		System.out.println("with expScore = 4");
		score_blackTurn = TwoTwoBoardSearch.getAccurateScore(stateB, 4);
		assertEquals(1, score_blackTurn);
		
		System.out.println("with expScore = -4");
		score_blackTurn = TwoTwoBoardSearch.getAccurateScore(stateB, -4);
		assertEquals(1, score_blackTurn);
	}
	
	public void testInitState_white_turn() {
		String[] text = new String[2];
		text[0] = new String("[_, _]");
		text[1] = new String("[_, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		BoardColorState stateB = new BoardColorState(state, Constant.WHITE);
		int score_whiteTurn = TwoTwoBoardSearch.getAccurateScore(stateB);
		assertEquals(-1, score_whiteTurn);

//		System.out.println("with expScore = 4");
//		score_whiteTurn = TwoTwoBoardSearch.getAccurateScore(stateB, 4);
//		assertEquals(-1, score_whiteTurn);
//		
//		System.out.println("with expScore = -4");
//		score_whiteTurn = TwoTwoBoardSearch.getAccurateScore(stateB, -4);
//		assertEquals(-1, score_whiteTurn);
	}
}