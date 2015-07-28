package eddie.wu.search.global;

import java.util.List;

import eddie.wu.domain.Block;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.survive.RelativeResult;

/**
 * Ongoing<br/>
 * live and live calculation is interdependent with race breath search.<br/>
 * race breath should not simply target at eating the enemy. it should be
 * combined with live/dead judgment. That is only if eating can reach liveness
 * then it is meaningful. <br/>
 * we have breath race usually because we has big block with long breath so that
 * it is not so clear who will win, especially when the raw breath is close.
 * some time a stone has effect of more than one breath. example some time
 * without cost, we can increase one breath by gifting one stone.<br/>
 * in this model, two block are weak, since they cannot make two eyes and it
 * neighbor enemy block is usually of more breath. they want to survive by
 * killing neighbor weak block.<br/>
 * sometime they are more weak enemy blocks but it is small, it cannot
 * contribute in the breath race, because killing them usually will not add too
 * much breath. sometime it add 0 breath with the cost of one step. <br/>
 * some block can increase breath, some cannot not, it is an important
 * attribute. (some block even can only decrease breath.) In this case, we have
 * only option to decrease enemy's breath.<br/>
 * 
 * @author Eddie
 * 
 */
public class BreathRaceSearch extends GoBoardSearch {
	Point targetFirst;
	Point targetSecond;
	int firstColor = 0;
	int secondColor = 0;

	/**
	 * Point first try to kill Point second, it's Point first's turn. <br/>
	 * note the whoreTurn in state is not relevant!
	 * 
	 * @param state
	 * @param first
	 * @param second
	 * @param firstColor
	 */
	public BreathRaceSearch(BoardColorState state, Point first, Point second) {
		super(RelativeResult.DUAL_LIVE,RelativeResult.ALREADY_DEAD);
		targetFirst = first;
		targetSecond = second;
		firstColor = this.getGoBoard().getColor(first);
		secondColor = this.getGoBoard().getColor(second);
		assert firstColor != secondColor;
	}

	@Override
	protected List<Candidate> getCandidate(int color) {
		Block block = null;
		if (color == firstColor) {
			block = getGoBoard().getBlock(targetFirst);
		} else {
			block = getGoBoard().getBlock(targetSecond);
		}
		// expand self breath first or prevent enemy's expanding
		// it depends on whose is more urgent! Aggressive strategy is better..

		//

		/**
		 * the near optimization strategy as below is close to optimal solution 
		 * (delta<=1):<br/>
		 * 1. take all self's force expanding (先手长气).<br/>
		 * 2. respond all enemy's force expanding.<br/>
		 * 3. take all self's simple expanding (后手长气).<br/>
		 * 4. take close breath (后手紧气) move at the last.<br/>
		 * <br/>
		 *  The possible best optimal solution:<br/>
		 * 1. take all self's force expanding with enemy's response.<br/>
		 * 2. preventing one enemy's force expanding.(earning one breath).<br/>
		 * 3. respond all enemy's force expanding<br/>
		 * 4. take all self's simple expanding.<br/>
		 * 5. take close breath.<br/>
		 * 
		 * 后手长气之间如果价值相等,可以抵消.不用考虑.剩下的可以换算成先手后手结果之差. 与后手官子类似.其价值也可能很大. <br/>
		 * so we can take the result as a reference. if breath delta >=2. the
		 * result is decided.<br/>
		 * we can estimate the result as described above.<br/>
		 * 实战中围棋的复杂度不在于此.<br/>
		 * another aspect is to find the candidate which can increase the breath
		 * in other special way,like:<br/>
		 * 金鸡独立不入气<br/>
		 * 两扳长一气<br/>
		 * 做眼(虎口)长一气<br/>
		 */

		// close enemy's breath.
		if (getGoBoard().canIncreaseBreath_temporarily(block)) {
		}

		return null;
	}

	@Override
	protected TerminalState getTerminalState() {
		getGoBoard().getLastStep().isBlockEaten(targetFirst);  
		return null;
	}


	@Override
	public GoBoard getGoBoard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void stateDecided(BoardColorState boardColorState, int score) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void stateFinalizeed(BoardColorState boardColorStateN,
			int scoreTerminator) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isKnownState(BoardColorState boardColorState) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getScore(BoardColorState boardColorState) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected SearchLevel getInitLevel() {
		// TODO Auto-generated method stub
		return null;
	}

}
