package core.sync.reentrant;

import junit.framework.TestCase;

public class TestSync extends TestCase {
	public void test() {
		A a = new A();
		a.lock();
		a.show();
	}

}

class A {
	public synchronized void show() {
		System.out.println("show()");
	}

	public synchronized void lock() {
		System.out.println("lock()");
	}
}
