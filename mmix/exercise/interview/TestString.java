
package interview;


public class TestString {
	public static void main(String[] args) {
		String a = "a";
		String b = "b";
		System.out.println("a = " + a);
		System.out.println("b = " + b);
		new TestString().swap(a, b);
		System.out.println("a = " + a);
		System.out.println("b = " + b);

	}

	private void swap(String a, String b) {
		String temp = b;
		b = a;
		a = temp;
		System.out.println("in swap ");
		System.out.println("a = " + a);
		System.out.println("b = " + b);
		System.out.println("in swap ");
	}
}
