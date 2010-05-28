package core;

import java.util.Arrays;

import junit.framework.TestCase;

public class TestString extends TestCase {
	public void test(){
		System.out.print(Arrays.toString("a;b".split(";")));
		
	}
}
