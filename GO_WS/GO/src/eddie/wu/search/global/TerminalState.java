package eddie.wu.search.global;

import eddie.wu.domain.analy.FinalResult;

public class TerminalState {
	private boolean terminalState;
	private FinalResult finalResult;
	private int score;

	public boolean isTerminalState() {
		return terminalState;
	}

	public void setTerminalState(boolean terminalState) {
		this.terminalState = terminalState;
	}

	public FinalResult getFinalResult() {
		return finalResult;
	}

	public void setFinalResult(FinalResult finalResult) {
		this.finalResult = finalResult;
		this.score = finalResult.getScore();
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

}
