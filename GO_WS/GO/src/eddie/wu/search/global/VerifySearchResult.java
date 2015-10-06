package eddie.wu.search.global;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import junit.framework.TestCase;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Step;
import eddie.wu.domain.SymmetryResult;
import eddie.wu.domain.analy.FinalResult;
import eddie.wu.domain.analy.SmallGoBoard;
import eddie.wu.manual.SearchNode;
import eddie.wu.manual.TreeGoManual;
import eddie.wu.search.two.TestAllState2;

/**
 * during global search we get two result for right score. <br/>
 * 1. first player win with right score, it proved he can reach the score no
 * matter what his opponent plays.<br/>
 * 2. First player lose with higher expect score; it proves that he cannot get
 * better result, no matter what he plays, his opponent guarantee he can get the
 * right score at most. <br/>
 * we support the result as in memory or in disk file. TODO test it
 * 
 * @author think
 *
 */
public class VerifySearchResult {
	private static Logger log = Logger.getLogger("search.verify");

	/**
	 * first player win by reaching the score/result.
	 * 
	 * @param manual
	 * @score the right score (best score for both side).
	 */
	public static void VerifyWin(BoardColorState state, int score,
			TreeGoManual manual) {
		SmallGoBoard board = new SmallGoBoard(manual.getInitState());
		int colorB = ColorUtil.enemyColor(manual.getInitTurn());
		// computer is first player. - get right play from manual
		Step initStep = manual.getCurrent().getChild().getStep();
		board.oneStepForward(initStep);
		manual.navigateToChild(initStep);
		SearchLevel currentLevel = new SearchLevel(0, colorB, initStep);
		log.debug("Verify Win for state " + state.getStateString() + ", score="
				+ score);
		log.debug("First player play at " + initStep
				+ " first.");
		verify(board, score, currentLevel, manual, state.isBlackTurn(),true);
	}

	/**
	 * first player cannot reach better result, he will try all candidates.
	 */
	public static void verifyBetterImpossible(BoardColorState state, int score,
			TreeGoManual manual) {
		SmallGoBoard board = new SmallGoBoard(manual.getInitState());
		SearchLevel currentLevel = new SearchLevel(0, manual.getInitTurn(),
				null);
		log.debug("Verify Better is not possible for state "
				+ state.getStateString() + ", score=" + score);
		verify(board, score, currentLevel, manual, state.isBlackTurn(), false);
	}

	/**
	 * 
	 * @param board
	 * @param score
	 * @param currentLevel
	 * @param manual
	 * @param max
	 *            whether the first player the max?
	 * @param win
	 *            whether the first player win?
	 */
	private static void verify(SmallGoBoard board, int score,
			SearchLevel currentLevel, TreeGoManual manual, boolean max,
			boolean win) {

		// Here one level stands for one back and forth in general,
		List<SearchLevel> levels = new ArrayList<SearchLevel>(32);
		levels.add(currentLevel);
		int levelIndex = 0;
		while (levelIndex >= 0) {
			// get any candidates.
			currentLevel = levels.get(levelIndex);
			if (currentLevel.isInitialized() == false) {
				currentLevel.setCandidates(board.getCandidate(
						currentLevel.getWhoseTurn(), false));
			}

			if (currentLevel.hasNext() == false) {
				if (levelIndex == 0)
					break;
				levels.remove(levelIndex--);
				board.oneStepBackward();
				board.oneStepBackward();
				manual.up();
				manual.up();
				continue;
			}
			// prepared before hand: before the first move.
			SymmetryResult symmetryResult = board.getSymmetryResult();
			String string = board.getStateString().toString();
			Candidate candidate = currentLevel.getNextCandidate();
			board.oneStepForward(candidate.getStep());
			log.debug(candidate.getStep() + "--->");
			if (board.areBothPass()) {
				FinalResult passScore = board.finalResult_doublePass();
				if (max) {
					if (win) {
						TestCase.assertTrue(passScore.getScore() >= score);
					} else {
						TestCase.assertTrue(passScore.getScore() <= score);
					}
				}else{
					if (win) {
						TestCase.assertTrue(passScore.getScore() <= score);
					} else {
						TestCase.assertTrue(passScore.getScore() >= score);
					}
				}

				board.oneStepBackward();
				continue;
			}

			// get proper response.
			SearchNode child = manual.getCurrent()
					.getChild(candidate.getStep());
			if (child == null) {
				if (manual.getCurrent().containsChildMove_mirrorSubTree(
						symmetryResult, candidate.getStep())) {
					log.debug("After mirroring, Manual contains move "
							+ candidate.getStep().toNonSGFString());
					child = manual.getCurrent().getChild(candidate.getStep());
				}
			}
			if (child == null) {
				log.debug("cannot find " + candidate.getStep() + "from "
						+ manual.getCurrent().getChildren());
			}
			TestCase.assertNotNull(child);
			manual.navigateToChild(candidate.getStep());
			if (manual.getCurrent().getChild() == null) {
				log.error("current state: "+string);
				log.error("last step: "+candidate.getStep());
				log.error("No response");
			}
			Step stepResponse = manual.getCurrent().getChild().getStep();

			board.oneStepForward(stepResponse);
			manual.navigateToChild(stepResponse);
			log.debug(stepResponse);
			if (board.areBothPass()) {
				FinalResult passScore = board.finalResult_doublePass();
				if (win==max) {
					TestCase.assertTrue(passScore.getScore() >= score);
				} else {
					TestCase.assertTrue(passScore.getScore() <= score);
				}
				board.oneStepBackward();
				board.oneStepBackward();
				manual.up();
				manual.up();
				continue;
			} else {
				// build new level
				SearchLevel levelNew = new SearchLevel(++levelIndex,
						currentLevel.getWhoseTurn(), stepResponse);
				levels.add(levelNew);
			}
		}
	}

}
