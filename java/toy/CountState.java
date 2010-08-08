package toy;

import java.util.BitSet;
import java.util.HashSet;
import java.util.Set;

/**
 * calculate the number of different status
 * @author wueddie-wym-wrz
 *
 */
public class CountState {
	Board board;
	
	CountState(Board board){
		this.board = board;
	}
	public int count(){
		Set<BitSet> states = new HashSet<BitSet>();
		return states.size();
		
	}
}
