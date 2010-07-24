package toy;

import java.util.List;

import junit.framework.TestCase;

public class TestBoard extends TestCase{
	public void testGetMoves(){
		Board board = new OldDad();
		board.init();
		List<Move> moves = board.getMoves();
		for(Move move : moves){
			System.out.println(move);
		}
	}
}
