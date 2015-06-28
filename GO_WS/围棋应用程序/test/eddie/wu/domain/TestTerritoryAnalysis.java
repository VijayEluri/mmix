package eddie.wu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.analy.EyeResult;
import eddie.wu.domain.analy.FinalResult;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.domain.analy.TerritoryAnalysis;
import eddie.wu.domain.comp.BlockBreathComparatorDesc;
import eddie.wu.domain.comp.EyeBlockSizeComparator;
import eddie.wu.domain.survive.RelativeResult;
import eddie.wu.domain.survive.Result;
import eddie.wu.manual.SimpleGoManual;
import eddie.wu.manual.SGFGoManual;
import eddie.wu.manual.StateLoader;
import eddie.wu.search.global.Candidate;
import eddie.wu.search.global.GoBoardSearch;
import eddie.wu.util.GBKToUTF8;

/**
 * RE : 共235手 黑胜3目<br/>
 * FinalResult [black=120, white=132, shared=109, tiemu=8, whoWin=White, net=20]
 * 
 * @author wueddie-wym-wrz
 * 
 */

public class TestTerritoryAnalysis extends TestCase {
	private static final Logger log = Logger.getLogger(GBKToUTF8.class);

	/**
	 * [2,14]<br/>
	 * [16,16]<br/>
	 * [14,6]<br/>
	 * [18,18]<br/>
	 * [17,7]<br/>
	 * [12,11]<br/>
	 * [16,3]<br/>
	 * [5,18]<br/>
	 * [17,15]<br/>
	 * [11,7]<br/>
	 * [13,9]<br/>
	 * [10,13]<br/>
	 * [4,19]<br/>
	 * [13,7]<br/>
	 * [19,17]<br/>
	 * [3,18]<br/>
	 * [16,8]<br/>
	 * [14,8]<br/>
	 * [9,10]<br/>
	 * [4,4]<br/>
	 * [2,4]<br/>
	 * [16,10]<br/>
	 * [18,11]<br/>
	 * [2,16]<br/>
	 * [10,9]<br/>
	 * [2,19]<br/>
	 * [1,15]<br/>
	 * [7,7]<br/>
	 * [2,7]<br/>
	 * [1,9]<br/>
	 * [4,9]<br/>
	 * [1,2]<br/>
	 * [14,17]<br/>
	 * White blocks: [2,14]<br/>
	 * [12,3]<br/>
	 * [14,13]<br/>
	 * [3,12]<br/>
	 * [16,16]<br/>
	 * [14,6]<br/>
	 * [18,18]<br/>
	 * [17,7]<br/>
	 * [14,9]<br/>
	 * [12,11]<br/>
	 * [16,3]<br/>
	 * [1,11]<br/>
	 * [7,4]<br/>
	 * [17,15]<br/>
	 * [5,18]<br/>
	 * [11,7]<br/>
	 * [13,9]<br/>
	 * [10,13]<br/>
	 * [10,5]<br/>
	 * [4,19]<br/>
	 * [8,18]<br/>
	 * [16,8]<br/>
	 * [19,17]<br/>
	 * [14,8]<br/>
	 * [13,7]<br/>
	 * [3,18]<br/>
	 * [9,10]<br/>
	 * [6,16]<br/>
	 * [4,4]<br/>
	 * [4,7]<br/>
	 * [2,3]<br/>
	 * [15,16]<br/>
	 * [14,5]<br/>
	 * [8,9]<br/>
	 * [2,4]<br/>
	 * [16,10]<br/>
	 * [18,11]<br/>
	 * [6,19]<br/>
	 * [2,16]<br/>
	 * [10,9]<br/>
	 * [9,19]<br/>
	 * [1,15]<br/>
	 * [2,19]<br/>
	 * [18,1]<br/>
	 * [2,1]<br/>
	 * [14,15]<br/>
	 * [10,7]<br/>
	 * [9,13]<br/>
	 * [7,7]<br/>
	 * [2,7]<br/>
	 * [4,9]<br/>
	 * [1,9]<br/>
	 * [1,2]<br/>
	 * [3,16]<br/>
	 * [14,17]<br/>
	 * [19,14]<br/>
	 * [2,18]<br/>
	 * [6,14]<br/>
	 * [2,6]<br/>
	 * [19,16]<br/>
	 */
	public void testPRe() {
		// String fileName = Constant.rootDir + "吴清源番棋263局/吴清源番棋002.SGF";

		SimpleGoManual manual = SGFGoManual.loadSimpleGoManual(Constant.currentManual);

		GoBoard board = new GoBoard();
		for (Step step : manual.getSteps()) {
			board.oneStepForward(step);
		}
		TerritoryAnalysis analysis = new TerritoryAnalysis(board
				.getBoardColorState().getMatrixState());

		List<Block> blocks = new ArrayList<Block>();
		blocks.addAll(analysis.getBlackBlocks());
		Collections.sort(blocks, new BlockBreathComparatorDesc());
		if(log.isEnabledFor(Level.WARN)) log.warn("Black blocks");
		for (Block block : blocks) {
			Point point = block.getBehalfPoint();
			if(log.isEnabledFor(Level.WARN)) log.warn(point + "<br/>");
		}

		blocks.addAll(analysis.getWhiteBlocks());
		Collections.sort(blocks, new BlockBreathComparatorDesc());
		if(log.isEnabledFor(Level.WARN)) log.warn("White blocks");
		for (Block block : blocks) {
			Point point = block.getBehalfPoint();
			if(log.isEnabledFor(Level.WARN)) log.warn(point + "<br/>");
		}
	}

	public void test() {
		// String fileName = Constant.rootDir + "吴清源番棋263局/吴清源番棋002.SGF";

		SimpleGoManual manual = SGFGoManual.loadSimpleGoManual(Constant.currentManual);

		GoBoard board = new GoBoard();
		for (Step step : manual.getSteps()) {
			board.oneStepForward(step);
		}
		TerritoryAnalysis analysis = new TerritoryAnalysis(board
				.getBoardColorState().getMatrixState());

		List<Block> blocks = new ArrayList<Block>();
		blocks.addAll(analysis.getBlackBlocks());
		Collections.sort(blocks, new BlockBreathComparatorDesc());
		Set<Point> sets = new HashSet<Point>();
		Point temp = Point.getPoint(19, 13, 7);
		sets.add(temp);
		temp = Point.getPoint(19, 14, 8);
		sets.add(temp);
		temp = Point.getPoint(19, 3, 18);
		sets.add(temp);
		temp = Point.getPoint(19, 16, 8);
		sets.add(temp);
		temp = Point.getPoint(19, 2, 19);
		sets.add(temp);
		temp = Point.getPoint(19, 2, 6);
		sets.add(temp);
		temp = Point.getPoint(19, 19, 16);
		sets.add(temp);
		for (Block block : blocks) {
			Point point = block.getBehalfPoint();
			if(log.isEnabledFor(Level.WARN)) log.warn(point);
			boolean live = analysis.isAlreadyLive_dynamic(point);
			if (sets.contains(point))
				continue;
			assertTrue("" + point + " not live", live);
		}

		for (Block block : blocks) {
			if(log.isEnabledFor(Level.WARN)) log.warn(block.getBehalfPoint());
		}

		boolean deadExist = analysis.isFinalState_deadExist();
		assertTrue(deadExist);
		FinalResult result = analysis.finalResult_deadExist();
		if (log.isDebugEnabled()) {
			log.debug(result);
		}

	}

	public void testWhiteBlocks() {
		// String fileName = Constant.rootDir + "吴清源番棋263局/吴清源番棋002.SGF";

		SimpleGoManual manual = SGFGoManual.loadSimpleGoManual(Constant.currentManual);

		GoBoard board = new GoBoard();
		for (Step step : manual.getSteps()) {
			board.oneStepForward(step);
		}
		TerritoryAnalysis analysis = new TerritoryAnalysis(board
				.getBoardColorState().getMatrixState());

		List<Block> blocks = new ArrayList<Block>();
		blocks.addAll(analysis.getWhiteBlocks());
		Set<Point> sets = new HashSet<Point>();
		Point temp = Point.getPoint(19, 13, 7);
		sets.add(temp);
		temp = Point.getPoint(19, 14, 8);
		sets.add(temp);
		temp = Point.getPoint(19, 3, 16);
		sets.add(temp);
		temp = Point.getPoint(19, 16, 8);
		sets.add(temp);
		temp = Point.getPoint(19, 2, 19);
		sets.add(temp);
		temp = Point.getPoint(19, 6, 19);
		sets.add(temp);
		temp = Point.getPoint(19, 2, 6);
		sets.add(temp);
		temp = Point.getPoint(19, 19, 16);
		sets.add(temp);

		List<BlankBlock> blanks = new ArrayList<BlankBlock>();
		blanks.addAll(analysis.getBlankBlocksOnTheFly());
		Collections.sort(blanks, new EyeBlockSizeComparator());

		List<Point> targets = new ArrayList<Point>();
		for (BlankBlock blankB : blanks) {
			if (blankB.isWhiteEye()) {
				Block block = blankB.getBiggestNeighborBlock();
				Point point = block.getBehalfPoint();
				if (targets.contains(point))
					continue;
				targets.add(point);
			}
		}
		/**
		 * [[4,7], [12,3], [1,11], [6,19], [14,9], [14,13], [6,16]]
		 */
		if(log.isEnabledFor(Level.WARN)) log.warn(targets);

		for (BlankBlock blankB : blanks) {
			if (blankB.isWhiteEye()) {

				Block block = blankB.getBiggestNeighborBlock();
				Point point = block.getBehalfPoint();
				if(log.isEnabledFor(Level.WARN)) log.warn(point);
				boolean live = analysis.isAlreadyLive_dynamic(point);
				if (sets.contains(point))
					continue;
			}

		}

		// for (Block block : blocks) {
		//
		// }
		//

		Collections.sort(blocks, new BlockBreathComparatorDesc());
		for (Block block : blocks) {
			Point point = block.getBehalfPoint();
			if(log.isEnabledFor(Level.WARN)) log.warn(point);
			boolean live = analysis.isAlreadyLive_dynamic(point);
			if (sets.contains(point))
				continue;
			assertTrue("" + point + " not live", live);
		}

		for (Block block : blocks) {
			if(log.isEnabledFor(Level.WARN)) log.warn(block.getBehalfPoint());
		}

		boolean deadExist = analysis.isFinalState_deadExist();
		assertTrue(deadExist);
		FinalResult result = analysis.finalResult_deadExist();
		if (log.isDebugEnabled()) {
			log.debug(result);
		}

	}

	public void test20() {
		SimpleGoManual manual = SGFGoManual.loadSimpleGoManual(Constant.currentManual);

		GoBoard board = new GoBoard();
		for (Step step : manual.getSteps()) {
			board.oneStepForward(step);
		}
		TerritoryAnalysis analysis = new TerritoryAnalysis(board
				.getBoardColorState().getMatrixState());
		// analysis.showLiveDead_2();

		Point point = Point.getPoint(19, 14, 17);
		assertTrue(analysis.isDead(point));
		// Block block = analysis.getBlock(point);
		// block.setLive(true);
		// block.setLiveDeadMarked(true);
		// analysis.liveBlockConnect(block);
		// analysis.getEyes(block, block.getColor());

		// analysis.getEyes( targetBlock, int myColor) {
	}

	public void test2() {
		SimpleGoManual manual = SGFGoManual.loadSimpleGoManual(Constant.currentManual);

		GoBoard board = new GoBoard();
		for (Step step : manual.getSteps()) {
			board.oneStepForward(step);
		}
		TerritoryAnalysis analysis = new TerritoryAnalysis(board
				.getBoardColorState().getMatrixState());
		analysis.showLiveDead_2();

	}

	public void testTarget() {
		SimpleGoManual manual = SGFGoManual.loadSimpleGoManual(Constant.currentManual);

		GoBoard board = new GoBoard();
		for (Step step : manual.getSteps()) {
			board.oneStepForward(step);
		}
		TerritoryAnalysis analysis = new TerritoryAnalysis(board
				.getBoardColorState().getMatrixState());

		List<BlankBlock> blanks = new ArrayList<BlankBlock>();
		blanks.addAll(analysis.getBlankBlocksOnTheFly());
		Collections.sort(blanks, new EyeBlockSizeComparator());
		List<Point> eyes = new ArrayList<Point>();
		List<Point> targets = new ArrayList<Point>();

		for (BlankBlock blankB : blanks) {
			if (blankB.isWhiteEye()) {
				eyes.add(blankB.getBehalfPoint());
				Block block = blankB.getBiggestNeighborBlock();
				Point point = block.getBehalfPoint();
				if (targets.contains(point))
					continue;
				targets.add(point);
			}
		}
		if(log.isEnabledFor(Level.WARN)) log.warn(eyes);

		/**
		 * [[4,7], [12,3], [1,11], [6,19], [14,9], [14,13], [6,16]]
		 */
		if(log.isEnabledFor(Level.WARN)) log.warn(targets);

	}

	/**
	 * to support the eye filled by enemy and the eye is not complete.
	 */
	public void testLive() {
		SimpleGoManual manual = SGFGoManual.loadSimpleGoManual(Constant.currentManual);

		GoBoard board = new GoBoard();
		for (Step step : manual.getSteps()) {
			board.oneStepForward(step);
		}
		TerritoryAnalysis analysis = new TerritoryAnalysis(board
				.getBoardColorState().getMatrixState());
		Point point = Point.getPoint(19, 6, 16);

		Logger.getLogger(GoBoardSearch.class).setLevel(Level.WARN);
		boolean live = analysis.isAlreadyLive_dynamic(point);
		assertTrue(live);
	}

}
