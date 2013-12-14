package eddie.wu.search.global;

import java.util.List;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.GoBoard;

/**
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
 * attribute. (some block even can only decrease breath.) In this case, we have only
 * option to decrease enemy's breath.<br/>
 * 
 * @author Eddie
 * 
 */
public class BreathRaceSearch extends GoBoardSearch {

	@Override
	protected List<Candidate> getCandidate(int color) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected TerminalState getTerminalState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean isDoubleGiveup() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void printKnownResult() {
		// TODO Auto-generated method stub

	}

	@Override
	protected int getLowestExp() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int getHighestExp() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public GoBoard getGoBoard() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	void stateDecided(BoardColorState boardColorState, int score) {
		// TODO Auto-generated method stub

	}

	@Override
	public void stateFinalizeed(BoardColorState boardColorStateN,
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

}
