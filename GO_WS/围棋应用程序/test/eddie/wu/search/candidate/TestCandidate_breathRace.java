package eddie.wu.search.candidate;

import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Block;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.SmallGoBoard;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.global.Candidate;

public class TestCandidate_breathRace extends TestCase {
	public List<Candidate> testCandidate(String[] text, int whoseTurn) {
		return testCandidate(text, whoseTurn, true);
	}

	private static Logger log = Logger
			.getLogger(TestCandidate_breathRace.class);
	static {
		log.setLevel(Level.INFO);
	}

	public List<Candidate> testCandidate(String[] text, int whoseTurn,
			boolean filter) {
		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard sa = new SmallGoBoard(BoardColorState.getInstance(state,
				whoseTurn));
		List<Candidate> candidates = sa.getCandidate(whoseTurn, filter);
		if (log.isEnabledFor(Level.WARN)) {
			log.warn(sa.getBoardColorState().getStateString());
			for (Candidate candidate : candidates) {
				log.warn(candidate);
			}
		}
		return candidates;
	}

	public void test() {
		String[] text = new String[9];
		text[0] = new String("[_, _, _, _, _, _, _, _, _]");
		text[1] = new String("[_, _, _, _, _, _, _, _, _]");
		text[2] = new String("[_, _, _, _, _, _, _, _, _]");
		text[3] = new String("[_, _, _, _, _, W, _, _, _]");
		text[4] = new String("[_, _, _, _, _, _, _, _, _]");
		text[5] = new String("[B, _, B, B, B, W, W, _, _]");
		text[6] = new String("[_, B, _, _, W, B, _, _, _]");
		text[7] = new String("[B, _, W, _, W, B, _, W, _]");
		text[8] = new String("[_, _, _, _, _, _, _, _, _]");

		List<Candidate> candidate = testCandidate(text, Constant.BLACK);
		// assertEquals(Point.getPoint(3, 3, 3),

		// identify when we should searching result for breath race.
		// this case, same breath (3) and White has chance to extend (Netly)
		// breath. however
		// white Black cannot.
		// both can extend one breath with cost of one hand.
		// if it is white's turn, 5 v.s. 3 white win!
		// if it is Black;s turn 2 v.s. 3 white lose
		// one breath race result is finalized (after B or W's play), the
		// situation is simplified.
		// failure block is same as dead block to some extend!

		// check the blocks has enemy blocks.

		// candidate.get(0).getStep()
		// .getPoint());

	}

	/***
	 * 有必要实现对杀的计算!<br/>
	 * 注意这里要避免定义的递归.<br/>
	 * 角上的白块局部做不出两眼.但是可以吃掉相邻的黑块成活.所以能否作出两眼必须在一定的上下文中作出判断. <br/>
	 * 更复杂的情况,周围敌块本来是吃不到的,但是因为局部求活过程中的先手利用就变成可以吃掉敌块了.<br/>
	 * 而且这里白块不是完整大眼.
	 */
	public void test2() {
		String[] text = new String[9];
		text[0] = new String("[_, _, _, _, _, _, _, _, _]");
		text[1] = new String("[_, _, _, _, _, _, _, _, _]");
		text[2] = new String("[_, _, _, _, _, _, W, W, _]");
		text[3] = new String("[_, _, _, _, _, _, _, B, _]");
		text[4] = new String("[_, _, _, _, _, W, _, B, _]");
		text[5] = new String("[_, _, _, W, W, W, B, B, B]");
		text[6] = new String("[_, _, B, W, B, B, W, W, W]");
		text[7] = new String("[_, B, _, B, _, B, W, _, _]");
		text[8] = new String("[_, _, _, _, _, _, _, _, _]");

		// List<Candidate> candidate = testCandidate(text, Constant.WHITE); 
		byte[][] state = StateLoader.LoadStateFromText(text);
		SmallGoBoard sa = new SmallGoBoard(BoardColorState.getInstance(state,
				Constant.BLACK));
		Block block = sa.getBlock(Point.getPoint(9, 7, 7));
		int breath = sa.getMaxNewBreath(block);
		System.out.println("Max New breath: " + breath);

		block = sa.getBlock(Point.getPoint(9, 6, 7));
		breath = sa.getMaxNewBreath(block);
		System.out.println("Max New breath: " + breath);

	}

}
