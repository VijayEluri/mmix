package eddie.wu.search;

import eddie.wu.domain.Constant;
import eddie.wu.manual.TreeGoManual;

public class ScoreWithManual {
	public ScopeScore scopeScore;
	/**
	 * to support/prove the score, we have to have those two manuals.
	 */
	public boolean max;
	public TreeGoManual win;
	public TreeGoManual lose;

	public ScoreWithManual(ScopeScore score) {
		this.scopeScore = score;
	}

	public ScoreWithManual(int score, boolean up, int boardSize, boolean max,
			TreeGoManual manual) {
		this.scopeScore = new ScopeScore(score, up, boardSize);
		this.max = max;
		if (max == up) {
			win = manual;
		} else {
			lose = manual;
		}
	}

	public ScoreWithManual(int score, TreeGoManual win, TreeGoManual lose) {
		this.scopeScore = ScopeScore.getAccurateScore(score);
		this.win = win;
		this.lose = lose;
	}

	/**
	 * *
	 * 
	 * @param mirrorScore
	 *            Not modified
	 */
	public void merge(ScoreWithManual scoreWithManual) {
		if (scopeScore.low < scoreWithManual.scopeScore.getLow()) {
			scopeScore.low = scoreWithManual.scopeScore.getLow();
			if (max) {
				this.win = scoreWithManual.win;
			} else {
				this.lose = scoreWithManual.lose;
			}
		}
		if (scopeScore.high > scoreWithManual.scopeScore.getHigh()) {
			scopeScore.high = scoreWithManual.scopeScore.getHigh();
			if (max) {
				this.lose = scoreWithManual.lose;
			} else {
				this.win = scoreWithManual.win;
			}
		}

		if (scopeScore.low > scopeScore.high) {
			throw new RuntimeException("low = " + scopeScore.low + ", high = "
					+ scopeScore.high);
		}

	}

	public void mirrorScore() {
		scopeScore = scopeScore.mirror();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("ScoreWithManual [");
		sb.append(scopeScore.toString());
		if (win != null) {
			sb.append("win=");
			sb.append(Constant.lineSeparator + win.getSGFBodyString(false));
		}
		if (lose != null) {
			sb.append("lose=");
			sb.append(Constant.lineSeparator + lose.getSGFBodyString(false));
		}
		return sb.toString();
	}

}
