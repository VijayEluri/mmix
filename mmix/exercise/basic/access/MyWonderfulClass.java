package basic.access;

class MyWonderfulClass {
	void go() {
		Bar b = new Bar();
		b.doStuff(new Foo() {
			public void foof() {
				System.out.println("foofy");
			} // end foof method
		}); // end inner class def, arg, and end statement
	} // end go()
	public static void main(String[] a){
		new MyWonderfulClass().go();	
	}
} // end class

interface Foo {
	void foof();
}

class Bar {
	void doStuff(Foo f) {
		System.out.println("Bar");
		f.foof();
	}
}