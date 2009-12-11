package basic.access;
class MyOuter2 {
	private String x = "Outer2";

	void doStuff() {
		class MyInner {
			public void seeOuter() {
				System.out.println("Outer x is " + x);
				System.out.println("Outer x is " + x);
				// System.out.println("Local variable z is " + z); // Won't Compile!
			} // close inner class method
		} // close inner class definition
		MyInner mi = new MyInner(); // This line must come
		// after the class
		mi.seeOuter();
	} // close outer class method doStuff()
} // close outer class
