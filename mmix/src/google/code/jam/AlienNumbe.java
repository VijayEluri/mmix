/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package google.code.jam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import util.MathUtil;

/**
 * It is a common problem of radix conversion. Input
 * 
 * Output 4 9 0123456789 oF8 Foo oF8 0123456789 13 0123456789abcdef 01 CODE
 * O!CDE? A?JM!.
 * 
 * Case #1: Foo Case #2: 9 Case #3: 10011 Case #4: JAM!
 * <p>
 * AlienNumbe.java
 * </p>
 */
public class AlienNumbe {
	static Record[] records;

	public static void readInput() {
		String name = "input.txt";
		InputStream input = AlienNumbe.class.getResourceAsStream(name);

		BufferedReader in = new BufferedReader(new InputStreamReader(input));
		try {
			String a = in.readLine();
			System.out.println(a);
			records = new Record[Integer.valueOf(a).intValue()];
			for (int i = 0; i < records.length; i++) {

				a = in.readLine();
				// System.out.println(a + " i=" + i);
				String regex = " ";
				String[] b = a.split(regex);
				records[i] = new Record();
				records[i].input = a;
				records[i].number = b[0];
				records[i].sourceL = b[1];
				records[i].targetL = b[2];
				System.out.println(records[i]);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				input.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 */
	public static void output() {
		for (int i = 0; i < records.length; i++) {
			System.out.println("Case #" + (i + 1) + ": " + records[i].output);
		}
	}

	/**
	 * first convert all the radix. the digit for the same radix can be any
	 * character.
	 * 
	 * @param rec
	 */
	static void convert(Record rec) {
		int srcR = rec.sourceL.length();// radix for src
		int tagR = rec.targetL.length();// radix for tag
		char[] tL = rec.targetL.toCharArray();
		char[] num = rec.number.toCharArray();
		// convert Foo to 100(3)
		int count = 0;
		int mul = 1;
		for (int i = num.length - 1; i >= 0; i--) {
			int t = rec.sourceL.indexOf(num[i]);// convert to decimal
			count += t * mul;
			mul *= srcR;
		}
		System.out.println("count=" + count);
		String temp = null;
		char[] tt = new char[MathUtil.log(tagR, count) + 1];
		int j = tt.length - 1;
		while (count >= tagR) {
			tt[j--] = tL[count % tagR];
			count = count / tagR;
		}
		tt[j--] = tL[count];
		System.out.println("j=" + (j + 1));

		// if (tagR > 36) {
		// rec.output = convert_2(count, tagR);
		// } else {
		// temp = Integer.toString(count, tagR);// tagR can not be more than 36
		// }
		// char[] tt = temp.toCharArray();
		// for (int i = 0; i < tt.length; i++) {
		// tt[i] = tL[tt[i] - '0'];//does not work
		// }
		rec.output = String.valueOf(tt);
	}

	static String convert_2(int count, int tagR) {

		return "0";
	}

	public static void main(String[] args) {

		readInput();

		for (int i = 0; i < records.length; i++) {
			convert(records[i]);
		}
		output();
	}

}

class Record {
	String input;
	String number;
	String sourceL;
	String targetL;
	String output;

	public String toString() {
		return number + " " + sourceL + " " + targetL;
	}
}
