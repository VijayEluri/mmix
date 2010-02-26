package effective.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SeriaTiming {
	private long start;
	private long end;

	public long timeSerialAndReverse(Object foo) {
		start = System.nanoTime();

		try {
			ByteArrayOutputStream outString = new ByteArrayOutputStream(4096);
			ObjectOutputStream out = new ObjectOutputStream(outString);
			out.writeObject(foo);
			out.close();

			byte[] byteArray = outString.toByteArray();
			System.out.println(foo.toString() + " " + byteArray.length);
			ObjectInputStream in = new ObjectInputStream(
					new ByteArrayInputStream(byteArray));
			Object readObject = in.readObject();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		end = System.nanoTime();
		return end - start;
	}
}
