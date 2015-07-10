package eddie.wu.domain.loop;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import junit.framework.Assert;
import junit.framework.TestCase;
import eddie.wu.domain.BlankBlock;
import eddie.wu.domain.Block;
import eddie.wu.domain.Point;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.manual.StateLoader;

public class TestLoop extends TestCase {
	/**
	 * 循环劫;如何将摇橹劫识别为活棋.两个劫可得其一.保持二气.<br/>
	 * 两个单子块是死棋,可以提走.如何识别为终止局面.<br/>
	 * 本质上也是循环劫,二劫连环,与三劫连环有很大差异.<br/>
	 * 二劫连环有三个状态: AB'<-->A'B'<-->A'B <br/>
	 * 另一方先行: AB'<-->AB<-->A'B <br/>
	 * 
	 * 先动手的一方单方找劫材.固不是劫争.正因为两劫循环不起来,故没有"两劫循环"的说法.<br/>
	 * * AB'-->A'B'-->A'B-->A'B-->AB<-->AB'-->AB'<br/>
	 * (弃权致同型再现是允许的,之后不能再继续循环!) <br/>
	 * 如果他处无棋，则按照一下循环结束.<br/>
	 * 算法思路.局部不能两眼活棋的块相邻,作为一个整体考虑,常见的例子是双活.<br/>
	 * 整个局部能够考虑的候选点其实很有限.<br/>
	 */
	public void testLoop1() {

		String[] text = new String[6];
		text[0] = new String("[_, B, B, B, B, B]"); 
		text[1] = new String("[B, B, W, W, W, W]");
		text[2] = new String("[_, B, W, _, W, B]");
		text[3] = new String("[B, B, W, W, B, _]");
		text[4] = new String("[B, W, W, B, B, B]");
		text[5] = new String("[B, W, _, W, B, _]");
		byte[][] state = StateLoader.LoadStateFromText(text);
		SurviveAnalysis sa = new SurviveAnalysis(state);
		Point target = sa.getPoint(2, 2);
		assertTrue(sa.isAlreadyLive_dynamic(target));
		assertTrue(sa.isStaticLive(target));
		target = sa.getPoint(3, 3);
		assertFalse(sa.isStaticLive(target));
		target = sa.getPoint(5, 5);
		assertFalse(sa.isStaticLive(target));
		target = sa.getPoint(6, 4);
		assertTrue(sa.isAlreadyDead_dynamic(target));
		target = sa.getPoint(3, 6);
		assertTrue(sa.isAlreadyDead_dynamic(target));
		
		

	}

	public void testLooploop() {
		String[] text = new String[5];
		text[0] = new String("[W, _, W, _, _]");
		text[1] = new String("[ , W, W, W, _]");
		text[2] = new String("[W, B, B, W, _]");
		text[3] = new String("[B, _, B, W, W]");
		text[4] = new String("[_, B, W, W, W]");
	}

	public void testLoop2() {
		String[] text = new String[5];
		text[0] = new String("[W, _, _, _, _]");
		text[1] = new String("[ , W, W, W, W]");
		text[2] = new String("[W, B, B, W, _]");
		text[3] = new String("[B, _, B, W, W]");
		text[4] = new String("[_, B, W, W, W]");
	}
	
	/**
	 * 
	 */
	public void testBentFourInCorner() {
		String[] text = new String[4];
		text[0] = new String("[B, B, _, _]");
		text[1] = new String("[B, B, B, _]");
		text[2] = new String("[B, B, B, _]");
		text[3] = new String("[B, B, B, B]");
		byte[][] state = StateLoader.LoadStateFromText(text);

		TerritoryAnalysis ta = new TerritoryAnalysis(state);
		Logger.getLogger(TerritoryAnalysis.class).setLevel(Level.WARN);

		Point point = Point.getPoint(4, 2, 1);
		Block target = ta.getBlock(point);
		Point eye = Point.getPoint(4, 1, 4);
		BlankBlock eyeBlock = ta.getBlankBlock(eye);
		boolean live = ta.isBigEye_alreadyLive(target , eyeBlock );
		assertFalse(live);
		
		boolean canLive = ta.isBigEye_canLive(target, eyeBlock);
		assertTrue(canLive);

		boolean dead = ta.isAlreadyDead_dynamic(point);
		assertFalse(dead);

		boolean finalState_deadExist = ta.isFinalState_deadExist();
		assertFalse(finalState_deadExist);

	}


}
