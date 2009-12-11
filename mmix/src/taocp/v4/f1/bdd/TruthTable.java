/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package taocp.v4.f1.bdd;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Small utility class to handle the truth table related logic.
 * </p>
 */
public class TruthTable {
	boolean[] table;

	/**
	 * 
	 * @param s
	 *            like "01011010"
	 */
	public TruthTable(String s) {
		table = new boolean[s.length()];
		byte[] cc = s.getBytes();
		for (int i = 0; i < cc.length; i++)
			table[i] = cc[i] == '0' ? false : true;
	}

	public TruthTable(int[] s) {
		for (int i = 0; i < s.length; i++)
			table[i] = s[i] == 0 ? false : true;
	}

	public TruthTable(boolean[] s) {
		table = s;
	}

	public boolean[] getTable() {
		return table;
	}

	static boolean allFalse(boolean[] table) {
		for (boolean b : table) {
			if (b == true)
				return false;
		}
		return true;
	}

	static boolean allTrue(boolean[] table) {
		for (boolean b : table) {
			if (b == false)
				return false;
		}
		return true;
	}

	public static String getString(boolean[] table) {
		byte[] b = new byte[table.length];
		for (int i = 0; i < b.length; i++) {
			b[i] = (byte) ('0' + (table[i] ? 1 : 0));
		}
		return new String(b);
	}
	
	public static String getString(boolean[] table, char t, char f) {
		byte[] b = new byte[table.length];
		for (int i = 0; i < b.length; i++) {
			b[i] = table[i] ? (byte)t : (byte)f;
		}
		return new String(b);
	}

	public String toString() {
		return getString(table);
	}

	public static String repeatString(boolean value, int times) {
		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < times; i++) {
			bf.append(value ? "1" : "0");
		}
		return bf.toString();
	}

	public static boolean isSymmetric(String table) {
		if (table == null || table.length() == 1) {
			return false;
		}
		int n = table.length();
		String left = table.substring(0, n / 2);
		String right = table.substring(n / 2, n);
		return left.equals(right);
	}

	public static int numOfOnes(String table) {
		boolean[] tab = new TruthTable(table).getTable();
		int count = 0;
		for (int i = 0; i < tab.length; i++) {
			if (tab[i] == true) {
				count++;
			}
		}
		return count;

	}

	public int numOfOnes() {
		int count = 0;
		for (int i = 0; i < table.length; i++) {
			if (table[i] == true) {
				count++;
			}
		}
		return count;
	}

	public int[] differ(boolean[] table) {
		List<Integer> ll = new ArrayList<Integer>();
		for (int i = 0; i < table.length; i++) {
			if (table[i] != this.table[i]) {
				ll.add(i);
			}
		}

		int[] res = new int[ll.size()];
		int count = 0;
		for (int t : ll) {
			res[count++] = t;
		}
		return res;
	}
}
