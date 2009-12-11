package mmix;

import mmix.Machine;
import junit.framework.TestCase;

/**
 * <p>TestMMIX.java
 * </p>
 
 * 
 * @author Eddie Wu 
 * @version 1.0
 * 
 */
public class TestMMIX extends TestCase {
	protected Machine machine;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		machine = new Machine();
		machine.setUp();
	}
}
