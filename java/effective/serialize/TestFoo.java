package effective.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.TestCase;

public class TestFoo extends TestCase {
	long start;
	long end;
	SeriaTiming timing = new SeriaTiming();

	public void testPerf() {
		long counta = 0;
		long countb = 0;
		Foo foo = new Foo(1, 2);
		NormalFoo nfoo = new NormalFoo(1, 2);
		for (int i = 0; i < 10; i++) {
			counta += timing.timeSerialAndReverse(foo);
			countb += timing.timeSerialAndReverse(nfoo);
		}
		System.out.println("Foo: it takes " + ((counta) >> 10)
				+ " K nano seconds.");
		System.out.println("Normal Foo: it takes " + (countb >> 10)
				+ " K nano seconds.");
	}

	public void testFoo() {
		start = System.nanoTime();
		Foo foo = new Foo(1, 2);
		// String fileName = "serialize.bin";
		byte[] fileName = new byte[4096];
		try {
			ByteArrayOutputStream outString = new ByteArrayOutputStream(4096);
			ObjectOutputStream out = new ObjectOutputStream(outString);
			out.writeObject(foo);
			out.close();
			// System.out.print(outString.toString());

			ObjectInputStream in = new ObjectInputStream(
					new ByteArrayInputStream(outString.toByteArray()));
			Object readObject = in.readObject();
			if (readObject instanceof Foo) {
				System.out.println("readObject instanceof Foo == true");
			} else {
				Foo a = (Foo) readObject;
				System.out.println(a.getX());
				System.out.println(a.getY());
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		end = System.nanoTime();
		System.out.println("Foo: it takes " + ((end - start) >> 10)
				+ " K nano seconds.");
	}

	public void testNormalFoo() {
		start = System.nanoTime();
		NormalFoo foo = new NormalFoo(1, 2);
		// String fileName = "serialize_normalfoo.bin";
		try {
			ByteArrayOutputStream outString = new ByteArrayOutputStream(4096);
			ObjectOutputStream out = new ObjectOutputStream(outString);
			out.writeObject(foo);
			out.close();
			// System.out.print(outString.toString());

			ObjectInputStream in = new ObjectInputStream(
					new ByteArrayInputStream(outString.toByteArray()));
			Object readObject = in.readObject();
			if (readObject instanceof NormalFoo) {
				System.out.println("readObject instanceof Foo == true");
			} else {
				NormalFoo a = (NormalFoo) readObject;
				System.out.println(a.getX());
				System.out.println(a.getY());
			}

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		end = System.nanoTime();
		System.out.println("Normal Foo: it takes " + ((end - start) >> 10)
				+ " K nano seconds.");
	}
}
