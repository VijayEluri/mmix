package eddie.wu.search.global;

public class SearchParam {
	/**
	 * whether we store and reuse state we ever encountered.
	 */
	public boolean store_state = true;
	public boolean reuse_state = true;// true;
	// set it big to deal with ladder calculation
	public int depth = 81;
	/**
	 * 1. set it small to ensure we have efficient algorithm <br/>
	 * 2. could be controlled outside. 3. it counts the number of final state we
	 * reached.
	 */

	public int NUMBER_OF_VARIANT = 50000 * 3;
}
