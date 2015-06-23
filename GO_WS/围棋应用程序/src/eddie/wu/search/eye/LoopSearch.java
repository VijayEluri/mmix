package eddie.wu.search.eye;

import java.util.List;

import eddie.wu.domain.Block;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.search.global.Candidate;

/**
 * onGoing<br/>
 * 处理打劫的搜索。在先手方提劫了之后。意识到当前局面可能与劫相关，比如劫活，劫杀之类。<br/>
 * 此时比较两种搜索的结果。<br/>
 * a. Max方(即目标块方)劫材有利的结果，必须是比如128，代表活棋。<br/>
 * b. Max方(即目标块方)劫材不利的结果，必须是比如-128，代表死棋。<br/>
 * 过程中记录对方应的劫材数目，和对方不应的劫材的大小。<br/>
 * 否则当前状态尽管有打劫的形式，但是结果与劫无关。<br/>
 * 应的劫材,虚拟的。着手记录为ZHAO_JIE,YING_JIE.不应的劫材，着手记录为ZHAO_JIE,ZHAO_JIE_CONT. <br/>
 * 这样能判断出是不同的局面。<br/>
 * 这里简单的将“劫”翻译为loop。<br/>
 * 设计的要点是保持globalSearch方法不变。通过辅助方法的覆盖来实现不同的搜索。
 * 
 * @deprecated loop is a separate aspect. it may happen not only at life and
 *             death, but also in liberty/breath race.
 */
public class LoopSearch extends BigEyeSearch {
	int targetColor;
	boolean isTargetLoopSuperior;// 目标块一方是否劫材有利，有与劫的价值匹敌的大劫才

	@Override
	public int globalSearch() {

		return 128;
	}

	public LoopSearch(byte[][] state, Point target, int targetColor,
			boolean makeEye, boolean targetSuperior) {
		super(state, target, targetColor, makeEye, targetSuperior);
	}

	@Override
	protected List<Candidate> getCandidate(int color) {
		Block targetBlock = goBoard.getBlock(getTarget());
		boolean forTarget = color == targetBlock.getColor();
		// 目标方劫财有利。
		return goBoard.getCandidate_forTarget(null, getTarget(), candidates,
				color, forTarget, forTarget);
		// if(targetColor==color){
		// if()
		// }else{
		//
		// }
		// return null;
	}

	@Override
	protected boolean isTerminateState() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected int getScore() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public GoBoard getGoBoard() {
		// TODO Auto-generated method stub
		return null;
	}

}
