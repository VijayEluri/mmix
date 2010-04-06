package core.reflect;

import java.io.Serializable;

import junit.framework.TestCase;

public class TestPrimitive extends TestCase{
	public void test(){
		System.out.println(byte.class);
		System.out.println(Byte.class);
		System.out.println(TestCase.class);
		System.out.println(Serializable.class);
	}
	public void testArray(){
		System.out.println(new int[]{}.getClass());
		System.out.println(new Integer[]{}.getClass());
		System.out.println(new TestCase[]{}.getClass());
		System.out.println(new Serializable[]{}.getClass());
	}
}
