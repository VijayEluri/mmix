package eddie.wu.search.global;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Step;
import eddie.wu.domain.SymmetryResult;
import eddie.wu.domain.analy.FinalResult;
import eddie.wu.domain.analy.SmallGoBoard;
import eddie.wu.manual.SearchNode;
import eddie.wu.manual.TreeGoManual;
import eddie.wu.search.ScopeScore;
import eddie.wu.search.ScoreWithManual;

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
		log.debug("Verify Win for state " + state.getStateString() + ", score="
				+ score);
		log.debug(manual.getSGFBodyString(false));
		TestCase.assertEquals(state, manual.getInitState());

		SmallGoBoard board = new SmallGoBoard(manual.getInitState());
		int colorB = ColorUtil.enemyColor(manual.getInitTurn());

		// computer is first player. - get right play from manual
		manual.backToRoot();
		Step initStep = manual.getCurrent().getChild().getStep();
		boolean success = board.oneStepForward(initStep);
		TestCase.assertTrue(success);
		manual.navigateToChild(initStep);

		SearchLevel currentLevel = new SearchLevel(0, colorB, initStep);
		log.debug("First player play at " + initStep + " first.");

		verify(board, score, currentLevel, manual, state.isBlackTurn(), true);
	}

	/**
	 * first player cannot reach better result, he will try all candidates.
	 */
	public static void verifyBetterImpossible(BoardColorState state, int score,
			TreeGoManual manual) {
		SmallGoBoard board = new SmallGoBoard(manual.getInitState());
		SearchLevel currentLevel = new SearchLevel(0, manual.getInitTurn(),
				null);
		manual.backToRoot();
		if (log.isDebugEnabled()) {
			log.debug("Verify Better is not possible for state "
					+ state.getStateString() + ", score=" + score);
			log.debug(manual.getSGFBodyString(false));
		}
		TestCase.assertEquals(state, manual.getInitState());
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
//		log.info(manual.getSGFBodyString(false));

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
			BoardColorState boardState = board.getBoardColorState();
			String string = boardState.getStateString().toString();
			Candidate candidate = currentLevel.getNextCandidate();
			board.oneStepForward(candidate.getStep());
			log.debug(candidate.getStep() + "--->");
			if (board.areBothPass()) {
				FinalResult passScore = board.finalResult_doublePass();
				verifyScore(win, max, score, passScore);
				board.oneStepBackward();
				continue;
			}

			// get proper response.
			SearchNode child = manual.getCurrent()
					.getChild(candidate.getStep());
			if (child == null) {
				if (manual.getCurrent().getChild() == null) {
					// boardState = board.getBoardColorState();
					ScopeScore scopeScore = GoBoardSearch
							.getScoreOfKnonwState(boardState);
					verifyScopeScoreReused(win, max, score, scopeScore);
					board.oneStepBackward();
					continue;
				} else {
					log.debug("Before mirroring: cannot find "
							+ candidate.getStep() + "from "
							+ manual.getCurrent().getChildren());
					if (manual.getCurrent().containsChildMove_mirrorSubTree(
							symmetryResult, candidate.getStep())) {
						log.debug("After mirroring, Manual contains move "
								+ candidate.getStep().toNonSGFString());
						log.debug("from children "
								+ manual.getCurrent().getChildrenStep());
						child = manual.getCurrent().getChild(
								candidate.getStep());
					} else {
						log.error("After mirrorring, no progess");
						log.error("from children "
								+ manual.getCurrent().getChildrenStep());
					}
				}
			}
			if (child == null) {
				log.error("cannot find " + candidate.getStep() + "from "
						+ manual.getCurrent().getChildren());
				log.error("Current State:" + boardState);
				ScopeScore scopeScore = GoBoardSearch
						.getScoreOfKnonwState(boardState);
				System.out.println(scopeScore);
			}
			TestCase.assertNotNull(child);
			manual.navigateToChild(candidate.getStep());

			if (manual.getCurrent().getChild() == null) {
				boardState = board.getBoardColorState();
				ScopeScore scopeScore = GoBoardSearch
						.getScoreOfKnonwState(boardState);
				verifyScopeScoreReused(win, max, score, scopeScore);
				board.oneStepBackward();
				manual.up();
				continue;
			}
			if (manual.getCurrent().getChild() == null) {
				// temporarily add the know states' manual here
				log.error("current state: " + string);
				log.error("last step: " + candidate.getStep());
				log.error("No response");
			}
			Step stepResponse = manual.getCurrent().getChild().getStep();

			board.oneStepForward(stepResponse);
			manual.navigateToChild(stepResponse);
			log.debug(stepResponse + "--response");
			if (board.areBothPass()) {
				FinalResult passScore = board.finalResult_doublePass();
				verifyScore(win, max, score, passScore);

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

	private static void verifyScopeScoreReused(boolean max, boolean win,
			int score, ScopeScore scopeScore) {
		if (win) {
			if (max) {
				TestCase.assertTrue(scopeScore.getLow() >= score);
			} else {
				TestCase.assertTrue(scopeScore.getHigh() <= score);
			}
		} else {
			if (max) {
				TestCase.assertTrue(scopeScore.getHigh() <= score);
			} else {
				TestCase.assertTrue(scopeScore.getLow() >= score);
			}
		}
	}

	/**
	 * 
	 * @param win
	 * @param max
	 * @param score
	 * @param passScore
	 */
	private static void verifyScore(boolean win, boolean max, int score,
			FinalResult passScore) {
		if (win == max) {
			TestCase.assertTrue("" + passScore + score,
					passScore.getScore() >= score);
		} else {
			TestCase.assertTrue(passScore.getScore() <= score);
		}
		// equivalent, but not as cool.
		if (win) {
			if (max) {
				TestCase.assertTrue(passScore.getScore() >= score);
			} else {
				TestCase.assertTrue(passScore.getScore() <= score);
			}
		} else {
			if (max) {
				TestCase.assertTrue(passScore.getScore() <= score);
			} else {
				TestCase.assertTrue(passScore.getScore() >= score);
			}
		}
	}

	/**
	 * after we get exact score
	 * 
	 * @param state
	 * @param score
	 * @param win
	 * @param lose
	 */
	public static void verify(BoardColorState state, int score,
			TreeGoManual win, TreeGoManual lose) {
		int maxScore = state.boardSize * state.boardSize;
		if (state.isBlackTurn()) {
			if (score == -maxScore) {
				VerifySearchResult.verifyBetterImpossible(state, score, lose);
			} else if (score == maxScore) {
				VerifySearchResult.VerifyWin(state, score, win);
			} else {
				VerifySearchResult.VerifyWin(state, score, win);
				VerifySearchResult.verifyBetterImpossible(state, score, lose);
			}
		} else if (state.isWhiteTurn()) {
			if (score == -maxScore) {
				VerifySearchResult.VerifyWin(state, score, win);
			} else if (score == maxScore) {
				VerifySearchResult.verifyBetterImpossible(state, score, lose);
			} else {
				VerifySearchResult.VerifyWin(state, score, win);
				VerifySearchResult.verifyBetterImpossible(state, score, lose);
			}
		}
	}

	/**
	 * check before add to known result.
	 * 
	 * @param state
	 * @param scoreWithManual
	 */
	public static void verify(BoardColorState state,
			ScoreWithManual scoreWithManual) {
		ScopeScore scopeScore = scoreWithManual.scopeScore;
		if (scoreWithManual.max) {
			if (scopeScore.getLow() != -state.boardSize * state.boardSize) {
				VerifySearchResult.VerifyWin(state, scopeScore.getLow(),
						scoreWithManual.win);
			}
			if (scopeScore.getHigh() != state.boardSize * state.boardSize) {
				VerifySearchResult.verifyBetterImpossible(state,
						scopeScore.getHigh(), scoreWithManual.lose);
			}
		} else {// min
			if (scopeScore.getHigh() != state.boardSize * state.boardSize) {
				VerifySearchResult.VerifyWin(state, scopeScore.getHigh(),
						scoreWithManual.win);
			}
			if (scopeScore.getLow() != 0 - state.boardSize * state.boardSize) {
				VerifySearchResult.verifyBetterImpossible(state,
						scopeScore.getLow(), scoreWithManual.lose);
			}

		}
	}
}
