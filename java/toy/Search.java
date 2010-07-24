/**
Try to solve the series of puzzles/games.
Old Dady -> Dummy Donkey -> Century -> Century and half
In Chinese, Hua Rong Dao
 */

package toy;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

public class Search {
	public static void main(String[] args) {
		Search century = new Search();
		// century.run();
	}

	Stack<Move> stack = new Stack<Move>();
	Set<BitSet> failed = new HashSet<BitSet>();//known to be bad result
	Set<BitSet> marked = new HashSet<BitSet>();//in decision. prevent the loop.
	
	
	
	
	

	List<Point> history = new ArrayList<Point>();

	public void searchSolution() {
		int level=0;
		while (true) {
			if(level==5){
				System.err.println("level==5");
				System.exit(0);
			}
			System.out.println("\nNew Round: "+level++);
			if (board.updateState().equals(this.targetState)) {
				System.out.println("Find the Solution: " + history);
			}
			List<Move> moves = board.getMoves();
			if (moves.isEmpty()) { // back tracking.
				if (stack.isEmpty()) {
					System.out.println("no solution.");
				} else {
					System.out.println("back tracking.");
					while (true) {
						Move move = stack.pop();
						board = move.apply();
						if (failed.contains(board.updateState())) {
							continue;
						} else {
							break;
						}
					}
					// board.
				}
			} else if (moves.size() == 1) {
				System.out.println("apply the only choice.");
				board.apply(moves.get(0));
			} else {
				System.out.println("apply one choice of " + moves.size()
						+ " choises. others are cached.");
				
				for (int i = 1; i < moves.size(); i++) {
					moves.get(i).setBoard(board.deepCopy());
					stack.push(moves.get(i));
				}
				//apply it after deep copy.
				board.apply(moves.get(0));
			}

		}
	}

	Board board;

	public void setInitBoard(Board board) {
		this.board = board;
	}

	BitSet targetState;

	public void setTargetBoard(Board board) {
		this.targetState = board.updateState();
	}

}

// class Board{

// }

