package core;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.SortedMap;

import junit.framework.TestCase;



public class TestCharset extends TestCase {
	public void test() {
		Charset name = Charset.forName("ASCII");
		System.out.println(name.displayName());
		System.out.println(name.aliases());

		name = Charset.forName("UTF-8");
		System.out.println(name.displayName());
		System.out.println(name.aliases());

		name = Charset.forName("UTF-16");
		System.out.println(name.displayName());
		System.out.println(name.aliases());

		name = Charset.forName("UTF-16Be");
		System.out.println(name.displayName());
		System.out.println(name.aliases());

		name = Charset.forName("UTF-16Le");
		System.out.println(name.displayName());
		System.out.println(name.aliases());

		name = Charset.defaultCharset();
		System.out.println(name.displayName());
		System.out.println(name.aliases());
		
		//getAllCharset();
	}

	private void getAllCharset() {
		SortedMap<String,Charset> map = Charset.availableCharsets();
		System.out.println("Availavel set: ");
		for(String setN : map.keySet()){
			System.out.println(setN);
		}
	}

	public void testEndian() {
		String input = "ÖÐ¹ú";
		ByteBuffer buffer = doEncode2(Charset.forName("UTF-16"), input);
		assertEquals(6, buffer.limit());
		buffer = doEncode2(Charset.forName("UTF-16Be"), input);
		assertEquals(4, buffer.limit());
		buffer = doEncode2(Charset.forName("UTF-16Le"), input);
		assertEquals(4, buffer.limit());
		
		try {
			byte[] bytes = input.getBytes("UTF-16");
			System.out.println(bytes.length);
			bytes = input.getBytes("UTF-16Be");
			System.out.println(bytes.length);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	private ByteBuffer doEncode2(Charset cs, String input) {
		ByteBuffer bb = cs.encode(input);
		System.out.println("Charset: " + cs.name());
		System.out.println(" Input: " + input);
		System.out.println("Encoded: " + Arrays.toString(bb.array()));
		System.out.println("Size is: " + bb.limit());
		return bb;
	}

	/**
	 * For a given Charset and input string, encode the chars and print out the
	 * resulting byte encoding in a readable form.
	 */
	private static void doEncode(Charset cs, String input) {
		ByteBuffer bb = cs.encode(input);
		System.out.println("Charset: " + cs.name());
		System.out.println(" Input: " + input);
		System.out.println("Encoded: ");
		for (int i = 0; bb.hasRemaining(); i++) {
			int b = bb.get();
			int ival = ((int) b) & 0xff;
			char c = (char) ival;
			// Keep tabular alignment pretty
			if (i < 10)
				System.out.print(" ");
			// Print index number
			System.out.print(" " + i + ": ");
			// Better formatted output is coming someday...
			if (ival < 16)
				System.out.print("0");
			// Print the hex value of the byte
			System.out.print(Integer.toHexString(ival));
			// If the byte seems to be the value of a
			// printable character, print it. No guarantee
			// it will be.
			if (Character.isWhitespace(c) || Character.isISOControl(c)) {
				System.out.println("");
			} else {
				System.out.println(" (" + c + ")");
			}
		}
		System.out.println("");
	}

}
