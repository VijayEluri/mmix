package eddie.wu.domain;

import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import eddie.wu.domain.analy.SmallGoBoard;
import eddie.wu.search.global.Candidate;

public class TestSymmetryWithSquare extends TestCase {
	private transient static final Logger log = Logger
			.getLogger(TestSymmetryWithSquare.class);

	public void test2() {
		int candidates = 0;
		int boardSize = 2;
		int[] count = new int[5];
		byte[][] state = new byte[boardSize + 2][boardSize + 2];
		for (byte i = 0; i <= 2; i++) {
			for (byte j = 0; j <= 2; j++) {
				for (byte k = 0; k <= 2; k++) {
					for (byte l = 0; l <= 2; l++) {
						state[1][1] = i;
						state[1][2] = j;
						state[2][1] = k;
						state[2][2] = l;

						SmallGoBoard go = new SmallGoBoard(state);
						int numberOfSymmetry = go.getSymmetryResult()
								.getNumberOfSymmetry();
						count[numberOfSymmetry]++;
						log.debug("[" + i + "," + j + "," + k + "," + l + "] "
								+ numberOfSymmetry);
						for (int ii = 0; ii <= boardSize + 1; ii++) {
							log.info(Arrays.toString(state[ii]));
						}
						List<Candidate> blackCandidate = go
								.getCandidate(Constant.BLACK,true);
						log.info("Candidate of Black's turn: " + blackCandidate);
						List<Candidate> whiteCandidate = go
								.getCandidate(Constant.WHITE,true);
						log.info("Candidate of White's turn: " + whiteCandidate);

						candidates += blackCandidate.size();
						candidates += whiteCandidate.size();
						if (numberOfSymmetry == 3)
							throw new RuntimeException("numberOfSymmetry==3");
					}
				}
			}
		}
		log.info(Arrays.toString(count));
		log.info(candidates);
		for (int row = 1; row < boardSize; row++) {
			for (int column = 1; column < boardSize; column++) {

			}
		}
	}

	/**
	 * 3*3 board. in all states (3 to 9 = 19683)[5832, 11664, 1458, 0, 729]
	 */
	public void test3() {
		int[] count = new int[5];
		int index = 3;
		int candidates = 0;
		int boardSize = 3;
		byte[][] state = new byte[boardSize + 2][boardSize + 2];
		for (byte i = 0; i <= 2; i++) {
			for (byte j = 0; j <= 2; j++) {
				for (byte k = 0; k <= 2; k++) {
					for (byte l = 0; l <= 2; l++) {
						for (byte m = 0; m <= 2; m++) {
							for (byte n = 0; n <= 2; n++) {
								for (byte o = 0; o <= 2; o++) {
									for (byte p = 0; p <= 2; p++) {

										for (byte q = 0; q <= 2; q++) {
											state[1][1] = i;
											state[1][2] = j;
											state[1][3] = k;
											state[2][1] = l;
											state[2][2] = m;
											state[2][3] = n;
											state[3][1] = o;
											state[3][2] = p;
											state[3][3] = q;

											index++;
											// if (index != 274)
											// continue;
											SmallGoBoard go = new SmallGoBoard(
													state);

											log.info(index);
											// log.debug("[" + i + "," + j + ","
											// + k + "]");
											// log.debug("[" + l + "," + m + ","
											// + n + "]");
											// log.debug("[" + o + "," + p + ","
											// + q + "] ");
											for (int ii = 0; ii <= boardSize + 1; ii++) {
												log.info(Arrays
														.toString(state[ii]));
											}
											int numberOfSymmetry = go
													.getSymmetryResult()
													.getNumberOfSymmetry();
											count[numberOfSymmetry]++;
											log.info(numberOfSymmetry);

											List<Candidate> blackCandidate = go
													.getCandidate(Constant.BLACK,true);
											// log.info("Candidate of Black's turn: "
											// + blackCandidate);
											List<Candidate> whiteCandidate = go
													.getCandidate(Constant.WHITE,true);
											// log.info("Candidate of White's turn: "
											// + whiteCandidate);
											candidates += blackCandidate.size();
											candidates += whiteCandidate.size();
											if (numberOfSymmetry == 3)
												throw new RuntimeException(
														"numberOfSymmetry==3");
										}
									}
								}
							}
						}
					}
				}
			}
		}
		log.info(Arrays.toString(count));
		log.info(candidates);
	}

}
