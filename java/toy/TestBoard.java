package toy;

import java.util.Set;

import junit.framework.TestCase;
import static toy.Constant.Debug;
public class TestBoard extends TestCase{
	public void testGetMoves(){
		Board board = new OldDad();
		board.init();
		Set<Move> moves = board.getMoves();
		for(Move move : moves){
			if(Debug) System.out.println(move);
		}
	}
}
