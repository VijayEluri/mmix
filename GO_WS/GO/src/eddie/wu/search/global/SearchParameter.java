package eddie.wu.search.global;

public class SearchParameter {

	// set it big to deal with ladder calculation
	public int depth = 81;
	/**
	 * 1. set it small to ensure we have efficient algorithm <br/>
	 * 2. could be controlled outside. 3. it counts the number of final state we
	 * reached.
	 */

	public int NUMBER_OF_VARIANT = 50000 * 3;
}
