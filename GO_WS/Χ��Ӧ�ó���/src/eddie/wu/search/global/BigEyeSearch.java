package eddie.wu.search.global;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import eddie.wu.domain.Block;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;

/**
 * 二三眼的知识先固化在代码中或者实现输入，这里的搜索从四子的完整眼位开始。<br/>
 * 且二子眼的死活，各种状态都是确定的状态（）<br/>
 * 三子眼的死活，情况比较复杂，尽管技术上只是点眼而已。</br/> 眼位刚刚出现是，很容易知道是大眼死活，一旦对方点入，不再是一个纯的大眼，如何识别呢？<br/>
 * 
 * 
 * @author Eddie
 * 
 */
public class BigEyeSearch extends GoBoardSearch {
	Set<Point> candidates = new HashSet<Point>();
	Point target;
	// targetBlock;
	/**
	 * target block has only one big eye block.<br/>
	 * 
	 * 有两种搜索，做眼和破眼。即对于一个局面的先后手结果。
	 * 
	 * @param state
	 * @param target
	 */
	public BigEyeSearch(byte[][] state, Point target, boolean makeEye) {
		super(state);
		
		Block targetBlock = goBoard.getBlock(target);
		Set<Point> eyePoints = targetBlock.getUniqueEyePoint();
		candidates.addAll(eyePoints);
		
		this.target = target;
		int color = goBoard.getColor(target);// 有眼块的颜色
		int enemyColor = ColorUtil.enemyColor(color);

		// level 0: all candidates of original state.
		Level level;
		List<Point> candidates;
		if (makeEye == true) {
			level = new Level(0, color);// 做眼方先下
			level.setWhoseTurn(Constant.MAX);
			candidates = getCandidate(color);
			level.setCandidates(candidates, color);
			level.setHighestExp(9);
			level.setTempBestScore(Integer.MIN_VALUE);
		} else {
			level = new Level(0, enemyColor);// 破眼方先下
			level.setWhoseTurn(Constant.MIN);
			candidates = getCandidate(enemyColor);
			level.setCandidates(candidates, enemyColor);
			level.setHighestExp(9);
			level.setTempBestScore(Integer.MAX_VALUE);
		}
		

		
		levels.add(level);

		
		
	}
	@Override
	protected boolean isTerminateState() {
		return goBoard.isLive(target) || goBoard.isDead();
	}
	
	@Override
	protected int getScore() {
		if(goBoard.isLive(target)==true) return 8;
		return 0;
	}

	@Override
	protected List<Point> getCandidate(int color) {
		Block targetBlock = goBoard.getBlock(target);
		boolean forTarget = color == targetBlock.getColor();
		return goBoard.getCandidate(candidates, color, forTarget);
	}
}
