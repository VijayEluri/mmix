/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package google.code.jam.engine;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

import junit.framework.TestCase;

/**
 * <p>
 * TestLeastEngine.java
 * </p>
 */
public class TestLeastEngine extends TestCase {
	LeastEngineSwitch les = new LeastEngineSwitch();

	public void test() {
		String[] engine = new String[] { "Yeehaw", "NSM", "Dont Ask", "B9",
				"Googol" };
		String[] query = new String[] { "Yeehaw", "Yeehaw", "Googol", "B9",
				"Googol", "NSM", "B9", "NSM", "Dont Ask", "Googol" };
		les.engines = engine;
		les.queries = query;
		int n = query.length;
		EngineSeq[] res = les.getSequence();
		System.out.println(Arrays.toString(res));
	}

	public void test2() {
		String[] engine = new String[] { "Yeehaw", "NSM", "Dont Ask", "B9",
				"Googol" };
		String[] query = new String[] { "Yeehaw", "Yeehaw", "Googol", "B9",
				"Googol", "NSM", "B9", "NSM", "Dont Ask", "Googol" };
		les.engines = engine;
		les.queries = query;
		int n = query.length;
		EngineSeq[] res = les.getSequence();
		System.out.println(Arrays.toString(res));
	}

	boolean debug = true;

	public void test3() {
		readInput();
		for (int i = 0; i < cases.length; i++) {
			System.out.println("Case #" + (i + 1) + ": "
					+ cases[i].getSequence2());
		}

	}

	LeastEngineSwitch[] cases;

	public void readInput() {
		String name = "G:/MyDocuments/Download/A-large-practice(2).in";
		// String name =
		// "G:/MyDocuments/Download/A-small-practice.in.shoppingplan.sample";
		InputStream input = null;
		BufferedReader in = null;
		try {
			input = new FileInputStream(name);
			in = new BufferedReader(new InputStreamReader(input));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			String a = in.readLine();
			if (debug)
				System.out.println("cases=" + a);

			cases = new LeastEngineSwitch[Integer.valueOf(a).intValue()];

			for (int i = 0; i < cases.length; i++) {
				cases[i] = new LeastEngineSwitch();
				a = in.readLine();
				if (debug)
					System.out.println("engines=" + a);
				cases[i].engines = new String[Integer.valueOf(a).intValue()];
				for (int j = 0; j < cases[i].engines.length; j++) {
					a = in.readLine();
					if (debug) {
						System.out.println("engine=" + a);
					}
					cases[i].engines[j] = a;
				}
				a = in.readLine();
				if (debug)
					System.out.println("queries=" + a);
				cases[i].queries = new String[Integer.valueOf(a).intValue()];
				for (int j = 0; j < cases[i].queries.length; j++) {
					a = in.readLine();
					if (debug)
						System.out.println("query=" + a);
					cases[i].queries[j] = a;

				}
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
}
