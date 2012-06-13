package eddie.wu.domain.analy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import eddie.wu.domain.BlankBlock;
import eddie.wu.domain.Block;
import eddie.wu.domain.Constant;
import eddie.wu.domain.NeighborState;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;
import eddie.wu.search.global.BrakeEyeComparator;
import eddie.wu.search.global.Candidate;
import eddie.wu.search.global.MakeEyeComparator;

/**
 * 
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class TerritoryAnalysis extends SurviveAnalysis {
	public TerritoryAnalysis(byte[][] state) {
		super(state);
	}

	public TerritoryAnalysis(int boardSize) {
		super(boardSize);
	}

	/**
	 * The standard rule to calculate the result. suppose all the result are
	 * finalized.<br/>
	 * 判断胜负，但要求全部死子都已经提走。<br/>
	 * 这里只是简单的根据每个点的颜色来决定归属。
	 * 
	 * @return
	 */
	public FinalResult finalResult() {
		int black = 0;
		int white = 0;
		int shared = 0;

		for (int i = 1; i <= boardSize; i++) {
			for (int j = 1; j <= boardSize; j++) {
				if (getColor(i, j) == Constant.BLACK)
					black++;
				else if (getColor(i, j) == Constant.WHITE)
					white++;
				else {
					BlankBlock block = this.getBlankBlock(i, j);
					if (block.isEyeBlock()) {
						if (block.isBlackEye()) {
							black += block.getNumberOfPoint();
						} else {
							white += block.getNumberOfPoint();
						}
					} else {
						shared += block.getNumberOfPoint();
					}
				}
			}
		}
		if (black + white + shared != boardSize * boardSize)
			throw new RuntimeException("black+white+shared="
					+ (black + white + shared) + "!=boardSize*boardSize");
		FinalResult res = new FinalResult(black, white, shared);
		return res;
	}

	/**
	 * simple implementation when one side has no move candidate; it is major
	 * used in small board size global search.<br/>
	 * 
	 * 当一方已经无处可以落子时. 判定胜负.(但是仍有可能有些死子没有提掉)<br/>
	 * 目前用在小棋盘的终局状态数子.<br/>
	 * 
	 * 解决的第一个问题:棋块中的子被重复计数<br/>
	 * 
	 * @return
	 */
	public FinalResult finalResult_noCandidate() {
		Set<Block> blocks = new HashSet<Block>();
		Set<BlankBlock> blankBlocks = new HashSet<BlankBlock>();

		int black = 0;
		int white = 0;
		int shared = 0;
		FinalResult res = new FinalResult();

		for (int row = 1; row <= boardSize; row++) {
			for (int column = 1; column <= boardSize; column++) {
				int color = this.getColor(row, column);
				if (color == Constant.BLANK)
					continue;
				Block block = this.getBlock(row, column);
				if (block.isCalculated())
					continue;
				else {
					block.setCalculated(true);
				}

				if (this.isLive(block.getBehalfPoint())) {// 确定的活棋块
					if (color == Constant.BLACK) {
						black += block.getNumberOfPoint();
						res.getBlackPoints().addAll(block.getPoints());

					} else if (color == Constant.WHITE) {
						white += block.getNumberOfPoint();
						res.getWhitePoints().addAll(block.getPoints());
					}
					for (Iterator<BlankBlock> iter = block.getBreathBlocks()
							.iterator(); iter.hasNext();) {
						BlankBlock blankBlock = iter.next();
						if (blankBlock.isCalculated())
							continue;
						else {
							blankBlock.setCalculated(true);
						}

						if (blankBlock.getPoints().isEmpty()) {
							System.err
									.println("empty point list in blank block");
							iter.remove();
							continue;
						}
						if (blankBlock.isEyeBlock()) {// 棋块周围的眼块计入该棋块
							if (blankBlock.isBlackEye()) {
								black += blankBlock.getNumberOfPoint();
								res.getBlackPoints().addAll(block.getPoints());
							} else {
								white += blankBlock.getNumberOfPoint();
								res.getWhitePoints().addAll(block.getPoints());
							}
						} else { // 共享气块
							shared += blankBlock.getNumberOfPoint();
							res.getSharedPoints().addAll(block.getPoints());
						}

					}
				} else {// 死棋块 (计算过程中,isLive=false未必是死棋,终局时就可以如此判断.
					// TODO 死棋块的周围共享气块可能需要变成眼块
					// 调用该方法之前,需要根据死活情况重新计算是否为眼块
					if (color == Constant.BLACK) {
						white += block.getNumberOfPoint();
						res.getWhitePoints().addAll(block.getPoints());
					} else if (color == Constant.WHITE) {
						black += block.getNumberOfPoint();
						res.getBlackPoints().addAll(block.getPoints());
					}
				}
			}
		}

		// clean up the flag after counting
		for (Block block : blocks) {
			block.setCalculated(false);
		}
		for (BlankBlock blankBlock : blankBlocks) {
			blankBlock.setCalculated(false);
		}

		res.setBlack(black);
		res.setWhite(white);
		res.setShared(shared);
		return res;
	}

	/**
	 * 初步的启发式来决定点的归属。避免不必要的提子。 (没有完成)
	 * 
	 * @return
	 */
	public FinalResult finalResult_heuristic() {
		for (int row = 1; row <= boardSize; row++) {
			for (int column = 1; column <= boardSize; column++) {
				Block block = this.getBlock(row, column);
				if (block.getEnemyBlocks().isEmpty() == false) {
					if (block.getEnemyBlocks().size() >= 2) {
						Block eye = block.getEnemyBlocks().iterator().next();
						// EyeKnowledge
					}
				}
			}
		}
		int black = 0;
		int white = 0;
		int shared = 0;
		FinalResult res = new FinalResult(black, white, shared);
		return res;
	}

	/**
	 * 大眼死活搜索中选取候选点。<br/>
	 * 成眼和破眼方的排序标准可能不同。同时第一步和第三步的排序标准也可能不同 <br/>
	 * 排序可以避免一些无意义的局面或选择，但是我们仍然需要能够处理这些变化，。
	 * 成眼方如果选择了缩小眼位的废棋，如何继续。缩小了，用归纳法。不用继续计算。
	 * 直二是死棋。做不出两眼。<br/>计算何时该停止，做静态判断？
	 * @param scope
	 * @param color
	 * @return
	 */
	public List<Point> getCandidate(Set<Point> scope, int color, boolean forTarget) {
		List<Point> can = new ArrayList<Point>();
		List<Candidate> cans = new ArrayList<Candidate>();
		
		for (Point point : scope) {
			if (this.getColor(point) != Constant.BLANK){
				continue;
			}
			NeighborState state = this.getNeighborState(point, color);
			Candidate candidate = new Candidate();
			candidate.setStep(new Step(point, color));
			candidate.setEating(state.isCapturing());
			candidate.setBreaths(breathAfterPlay(point, color).size());
			if(forTarget&&state.isCapturing()==false){
				candidate.setEyes(this.EyesAfterPlay(point, color).size())	;
				candidate.setTigerMouths(this.tigerMouthAfterPlay(point, color).size());
			}
			cans.add(candidate);
		}
		
		if(forTarget){
			Collections.sort(cans, new MakeEyeComparator());
		}else{
			Collections.sort(cans, new BrakeEyeComparator());
		}

		// 先不考虑排序（优化）
		for(Candidate candidate2:cans){
			can.add(candidate2.getStep().getPoint());
		}

		return can;
	}

}
