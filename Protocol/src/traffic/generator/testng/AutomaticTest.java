package traffic.generator.testng;

import org.testng.annotations.Test;
@Test
public class AutomaticTest {
	@Test(groups = { "functional" })
	public void test1() {
		System.out.println("functional 1");
	}

	@Test(groups = { "test" })
	public void test2() {
		System.out.println("unit test 1");
	}
}
