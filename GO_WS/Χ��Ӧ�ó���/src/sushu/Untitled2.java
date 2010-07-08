package sushu;

/**
 * check visibility in Inheritance
 * 
 * @author wujianfe
 * 
 */
public class Untitled2 extends Test {

	public int pri = 9;
	public int c = 0x1c;
	public int b = 0x1b;
	public int d = 0x1d;

	public static void main(String args[]) {
		new Untitled2().test1();
	}

	public void test1() {
		System.out.println("fields In Child Class:");
		System.out.println("protect=" + d);
		System.out.println("pri=" + pri);
		System.out.println("c=" + c);
		System.out.println("b=" + b);
	}

	

}

class Test {
	private int pri = 7;
	int b = 7;
	public int c = 8;
	protected int d = 9;

	Test() {
		System.out.println("fields In Parent Class:");
		System.out.println("private = " + pri);

		System.out.println("package-private = " + b);

		System.out.println("public = " + c);

		System.out.println("protected = " + d);
	}

}