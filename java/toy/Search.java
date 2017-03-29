/**
Try to solve the series of puzzles/games.
Old Dady -> Dummy Donkey -> Century -> Century and half
In Chinese, Hua Rong Dao
 */

package toy;

import static toy.Constant.Debug;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class Search {
	public static void main(String[] args) {
		Search century = new Search();
		century.searchSolutionDeepFirst();
	}

	Board initBoard;

	// List<BasicMove> history = new ArrayList<BasicMove>();

	public List<BasicMove> searchSolutionDeepFirst() {
		int level = 0;
		boolean nosolution = false;
		Stack<Move> stack = new Stack<Move>();
		Set<BitSet> failed = new HashSet<BitSet>();// known to be bad result
		Set<BitSet> marked = new HashSet<BitSet>();// in decision. prevent the
		// loop.
		Board board = initBoard;
		while (true) {
			// if (level == 20) {
			// System.err.println("level==" + level);
			// System.exit(0);
			// }

			System.out.println("\nNew Round: " + level++);

			if (Debug) {
				System.out.println("current status show in history: ");
				for (BasicMove movea : board.getHistory()) {
					System.out.println(movea);
				}
			}

			// first, is current status the solution?
			if (board.achieveGoal()) {
				System.out.println("achieve goal. ");
				break;
			}
			/**
			 * is it known status? or a terminal status.
			 */
			List<Move> moves = null;// = board.getMoves();
			if (failed.contains(board.updateState())
					|| marked.contains(board.updateState())
					|| (moves = board.getMoves()).isEmpty()) { // back tracking.
				if (moves != null) {
					failed.add(board.updateState());
				}

				if (stack.isEmpty()) {
					if (Debug)
						System.out.println("no solution."
								+ "candidates stack is empty");
					nosolution = true;

					break;
				} else {
					if (Debug)
						System.out
								.println("back tracking. current move history is");
					for (BasicMove bm : board.getHistory()) {
						if (Debug)
							System.out.println(bm);
					}

					// while (true) {
					// if (stack.isEmpty()) {
					// System.out.println("no solution."
					// + "candidates stack is empty");
					// // System.exit(0);
					// nosolution = true;
					// break;
					// }

					Move move = stack.pop();
					if (Debug)
						System.out.println("back to the status");
					board = move.apply();
					board.addHistoryMove(move.getMove());

					continue;
				}
			}

			// third extend current state.
			marked.add(board.updateState());

			if (moves.size() == 1) {
				if (Debug)
					System.out.println("apply the only choice.");

				Move next = moves.iterator().next();
				board.apply(next);
				board.getHistory().add(next.getMove());
			} else {
				if (Debug)
					System.out.println("apply one choice of " + moves.size()
							+ " choises. others are cached.");
				Iterator<Move> a = moves.iterator();
				Move first = a.next();

				for (; a.hasNext();) {
					Move next = a.next();
					Board deepCopy = board.deepCopy();
					next.setBoard(deepCopy);
					stack.push(next);
				}
				// apply it after deep copy.
				board.apply(first);
				board.getHistory().add(first.getMove());

			}

		}// end of loop.

		if (nosolution == true) {
			System.out.println("No Solution: ");
		} else {
			System.out.println("Find the Solution: "
					+ board.getHistory().size() + " steps");
			for (BasicMove bm : board.getHistory()) {
				System.out.println(bm);
			}
		}

		for (BasicMove bm : board.getHistory()) {
			if (Debug)
				System.out.println(bm);
		}
		return board.getHistory();
	}

	/**
	 * board first search to ensure the minimum path length.
	 * 
	 * @return
	 */
	public List<BasicMove> searchSolutionBroadFirst() {
		int level = 0;
		boolean nosolution = false;
		Board goal = null;

		Set<Board> states = new HashSet<Board>();

		Set<BitSet> failed = new HashSet<BitSet>();// known to be bad result
		Set<BitSet> marked = new HashSet<BitSet>();// in decision. prevent the
													// loop.
		// Board board;//hidden the instance field.
		states.add(this.initBoard);
		outloop: while (true) {

			System.out.println("\nNew Round: " + level++);
			System.out.println("states.size() = " + states.size());
			if (states.size() == 0) {
				nosolution = true;
				break outloop;
			}
			
			

			Set<Board> statesNew = new HashSet<Board>();
			for (Board board2 : states) {
				if (level <= 5) {

					System.out.println("history size "
							+ board2.getHistory().size());
					for (BasicMove move : board2.getHistory()) {
						System.out.println(move);
					}

				}
				Board board = board2.deepCopy();

				// first, is current status the solution?
				if (board.achieveGoal()) {
					System.out.println("achieve goal. ");
					goal = board;
					break outloop;
				}
				/**
				 * is it known status? or a terminal status.
				 */
				List<Move> moves = null;// = board.getMoves();
				if (failed.contains(board.updateState())
						|| marked.contains(board.updateState())
						|| (moves = board.getMoves()).isEmpty()) {
					if (moves != null) {
						failed.add(board.updateState());
					}
				} else {

					// third extend current state.
					marked.add(board.updateState());

					for (Iterator<Move> iterator = moves.iterator(); iterator
							.hasNext();) {
						Move move = iterator.next();
						Board deepCopy = board.deepCopy();
						move.setBoard(deepCopy);
						Board newState = move.apply();
						newState.addHistoryMove(move.getMove());
						statesNew.add(newState);
					}

				}

			}// for end:
			states = statesNew;

		}// while end: of loop.

		if (nosolution == true) {
			System.out.println("No Solution: ");
		} else {
			System.out.println("Find the Solution: " + goal.getHistory().size()
					+ " steps");
			for (BasicMove bm : goal.getHistory()) {
				System.out.println(bm);
			}
		}

		return goal.getHistory();
	}

	public void setInitBoard(Board board) {
		this.initBoard = board;
	}

	BitSet targetState;

	public void setTargetBoard(Board board) {
		this.targetState = board.updateState();
	}

}

// class Board{

// }

