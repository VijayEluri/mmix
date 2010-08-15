package toy.min;

import static toy.Constant.Debug;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import toy.BasicMove;
import toy.Board;
import toy.Move;

public class CompleteSearch {
	/**
	 * initial board status.
	 */
	Board board ;

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}
	
	void initGraph(){
		int level = 0;
		Stack<Move> stack = new Stack<Move>();
		Set<BitSet> failed = new HashSet<BitSet>();// known to be bad result
		Set<BitSet> sucede = new HashSet<BitSet>();// known to be bad result
		Set<BitSet> marked = new HashSet<BitSet>();// in decision. prevent the loop.
		while (true) {		
			System.out.println("\nNew Round: " + level++);

			if (Debug) {
				System.out.println("current status show in history: ");
				for (BasicMove movea : board.getHistory()) {
					System.out.println(movea);
				}
			}

			if(board.achieveGoal()){
				sucede.add(board.updateState());
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
									
					break;
				} else {
					if (Debug)
						System.out
								.println("back tracking. current move history is");
					

					Move move = stack.pop();
					if (Debug)
						System.out.println("back to the status");
					board = move.apply();
					board.getHistory().add(move.getMove());	
					
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
		
	}
	
	void shortestPath(){
		
	}
	
}
