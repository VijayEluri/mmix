package toy;

import static toy.Constant.Debug;
import junit.framework.TestCase;
public class TestBoard extends TestCase{
	public void testGetMoves(){
		Board board = new OldDad();
		board.init();
		
		for(Move move :  board.getMoves()){
			if(Debug) System.out.println(move);
		}
	}
}
