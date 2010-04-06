package effective.singleton;

import junit.framework.TestCase;

public class TestSingleton extends TestCase{
	public void test(){
		Elvis.INSTANCE.leaveTheBuilding();
	}
}
