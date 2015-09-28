package eddie.wu.search.global;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Step;
import eddie.wu.domain.analy.FinalResult;
import eddie.wu.domain.analy.SmallGoBoard;
import eddie.wu.manual.SearchNode;
import eddie.wu.manual.TreeGoManual;

/**
 * during global search we get two result for right score. <br/>
 * 1. first player win with right score, it proved he can reach the score no
 * matter what his opponent plays.<br/>
 * 2. First player lose with higher expect score; it proves that he cannot get
 * better result, no matter what he plays, his opponent guarantee he can get the
 * right score at most. <br/>
 * we support the result as in memory or in disk file.
 * TODO test it
 * @author think
 *
 */
public class VerifySearchResult {
	/**
	 * first player win by reaching the score/result.
	 * 
	 * @param manual
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
		verify(board, score, currentLevel, manual);
	}

	private static void verify(SmallGoBoard board, int score,
			SearchLevel currentLevel, TreeGoManual manual) {

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
				levels.remove(levelIndex--);
				board.oneStepBackward();
				board.oneStepBackward();
				continue;
			}

			Candidate candidate = currentLevel.getNextCandidate();
			board.oneStepForward(candidate.getStep());
			if (board.areBothPass()) {
				FinalResult passScore = board.finalResult_doublePass();
				TestCase.assertTrue(passScore.getScore() >= score);
				board.oneStepBackward();
				continue;
			}

			// get proper response.
			SearchNode child = manual.getCurrent()
					.getChild(candidate.getStep());
			TestCase.assertNotNull(child);
			manual.navigateToChild(candidate.getStep());
			Step stepResponse = manual.getCurrent().getChild().getStep();

			board.oneStepForward(stepResponse);
			if (board.areBothPass()) {
				FinalResult passResult = board.finalResult_doublePass();
				TestCase.assertTrue(passResult.getScore() >= score);
				board.oneStepBackward();
				board.oneStepBackward();
				continue;
			} else {
				// build new level
				SearchLevel levelNew = new SearchLevel(++levelIndex,
						currentLevel.getWhoseTurn(), stepResponse);
				levels.add(levelNew);
			}
		}
	}

	/**
	 * first player cannot reach better result, he will try all candidates.
	 */
	public static void verifyBetterImpossible(BoardColorState state, int score,
			TreeGoManual manual) {
		SmallGoBoard board = new SmallGoBoard(manual.getInitState());
		SearchLevel currentLevel = new SearchLevel(0, manual.getInitTurn(),
				null);
		verify(board, score, currentLevel, manual);
	}

}
