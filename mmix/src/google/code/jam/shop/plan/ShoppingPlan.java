/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package google.code.jam.shop.plan;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import taocp.v4.f2.perm.PermutationGenerator;
import util.CollUtil;
import util.MathUtil;

/**
 * Looks much complex to me.
 * 
 * Input
 * 
 * 2 1 2 10 cookies 0 2 cookies:400 4 0 cookies:320 3 3 5 cookies milk! cereal 0
 * 2 cookies:360 cereal:110 4 0 cereal:90 milk:150 -3 -3 milk:200 cookies:200
 * 
 * Case #1: 400.0000000 Case #2: 519.2920690
 * 
 * <p>
 * ShoppingPlan.java
 * </p>
 */
public class ShoppingPlan {
	private static boolean debug = false;
	static Stat[] stats;

	/**
	 * 
	 */
	public static void readInput() {
		String name = "G:/MyDocuments/Download/D-large-practice.in";
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
			// records = new Record[Integer.valueOf(a).intValue()];
			stats = new Stat[Integer.valueOf(a).intValue()];

			for (int i = 0; i < stats.length; i++) {
				a = in.readLine();
				if (debug)
					System.out.println("stats=" + a);
				String regex = " ";
				String[] b = a.split(regex);
				stats[i] = new Stat();
				int numItems = Integer.valueOf(b[0]).intValue();
				stats[i].items = new Item[numItems];
				int numStores = Integer.valueOf(b[1]).intValue();
				stats[i].stores = new Store[numStores];
				stats[i].gasPrice = Integer.valueOf(b[2]).intValue();

				a = in.readLine();
				regex = " ";
				b = a.split(regex);
				stats[i].setItems(b);

				for (int j = 0; j < numStores; j++) {
					a = in.readLine();
					if (debug)
						System.out.println("store=" + a);
					regex = " ";
					b = a.split(regex);
					stats[i].stores[j] = new Store();
					stats[i].stores[j].x = Integer.valueOf(b[0]).intValue();
					stats[i].stores[j].y = Integer.valueOf(b[1]).intValue();
					for (int k = 2; k < b.length; k++) {
						String[] c = b[k].split(":");
						stats[i].stores[j].price.put(c[0], Integer
								.valueOf(c[1]).intValue());
					}

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

	/**
	 * 
	 */
	public static void output() {
		NumberFormat nfm = NumberFormat.getNumberInstance();
		// nfm.setRoundingMode(roundingMode)
		nfm.setMaximumFractionDigits(7);
		for (int i = 0; i < stats.length; i++) {

			// if(debug) System.out.println("Case #" + (i + 1) + ": "
			// + nfm.format(stats[i].minPrice));
			System.out.print("Case #" + (i + 1) + ": ");
			System.out.printf("%.7f\n", stats[i].minPrice);
		}
	}

	public static void main(String[] args) {

		readInput();
		showInput();
		for (int i = 0; i < stats.length; i++) {
			cal(stats[i]);
		}
		output();
	}

	private static void showInput() {
		for (Stat stat : stats) {
			if (debug)
				System.out.println("" + stat.items.length + " "
						+ stat.stores.length + " " + stat.gasPrice);
			for (Item item : stat.items) {
				if (debug)
					System.out.print(item.itemName
							+ (item.perishable ? "!" : "") + " ");
			}
			if (debug)
				System.out.println();
			for (Store store : stat.stores) {
				if (debug)
					System.out.print(store.x + " " + store.y + " ");
				for (Entry<String, Integer> entry : store.price.entrySet()) {
					if (debug)
						System.out.print(entry.getKey() + ":"
								+ entry.getValue() + " ");
				}
				if (debug)
					System.out.println();
			}
		}
	}

	public static void cal(Stat stat) {

		int[] counts = new int[stat.items.length];
		if (debug)
			System.out.println("items size" + counts.length);
		int i = 0;
		Store[][] itemStores = new Store[stat.items.length][];
		for (Item _item : stat.items) {
			String item = _item.itemName;
			// if (item.endsWith("!")) {
			// item = item.substring(0, item.length() - 1);
			// }

			int count = 0;
			List<Store> ll = new ArrayList<Store>();
			for (Store store : stat.stores) {
				if (store.price.containsKey(item)) {
					count++;
					ll.add(store);
				}
			}
			counts[i] = count;
			itemStores[i] = ll.toArray(new Store[0]);
			i++;
		}
		if (debug)
			System.out.println("we have ? stores for item 0: "
					+ itemStores[0].length);
		//if (debug)
			System.out.println(Arrays.toString(counts));
		int totalC=1;
		for(int cc=0;cc<counts.length;cc++){
			totalC*=counts[cc];
		}
		System.out.println("totalC="+totalC);
		
		for (int k = 0; k < counts.length; k++) {
			if (debug)
				System.out.println(Arrays.toString(itemStores[k]));
		}
		if (debug)
			System.out.println(Arrays.deepToString(itemStores));
		
		//iterate the store combination.
		Case cas = new Case();
		cas.init(counts);
		double itemP = 0;
		double tPrice = 0;//
		stat.minPrice = Integer.MAX_VALUE;
		while (cas.hasMore) {
			itemP = 0;
			for (Store store : stat.stores) {
				store.gohome = false;
			}
			int[] tt = cas.getCurr();
			if (debug)
				System.out.println("tt=" + Arrays.toString(tt));
			Set<Store> set = new HashSet<Store>();
			for (int j = 0; j < stat.items.length; j++) {
				set.add(itemStores[j][tt[j]]);
				if (stat.items[j].perishable == true) {
					itemStores[j][tt[j]].gohome = true;
				}
				itemP += (itemStores[j][tt[j]]).price
						.get(stat.items[j].itemName);
			}
			if (debug)
				System.out.println("item price is" + itemP);
			tPrice = itemP + getPrice(set, stat.gasPrice);
			if (debug)
				System.out.println("temp price is" + tPrice);
			if (tPrice < stat.minPrice) {
				stat.minPrice = tPrice;
			}
		}
		//if (debug)
			System.out.println("min price is" + stat.minPrice);
	}

	private static double getPrice(Set<Store> _set, int gasPrice) {
		List<Store> set = new ArrayList<Store>();
		set.addAll(_set);
		// CollUtil.print(set);
		int size = set.size();
		if (size <= 1) {
			return getDistance(set) * gasPrice;
		}
		int[][] res2 = new int[MathUtil.factorial(size)][size];
		PermutationGenerator.permPlainChange(size, res2);

		if (debug)
			System.out.println(Arrays.deepToString(res2));

		List<Store> ll = new ArrayList<Store>();
		double minDis = Integer.MAX_VALUE;
		double dis = 0;
		for (int i = 0; i < res2.length; i++) {
			ll.clear();
			for (int j = 0; j < res2[0].length; j++) {
				ll.add(set.get(res2[i][j] - 1));
			}
			dis = getDistance(ll);
			if (dis < minDis) {
				minDis = dis;
			}

		}
		return minDis * gasPrice;
	}

	private static double getDistance(List<Store> ll) {
		int x = 0;
		int y = 0;
		int tx = 0;
		int ty = 0;
		Store s;
		double dis = 0;
		for (int i = 0; i < ll.size(); i++) {
			s = ll.get(i);
			tx = ll.get(i).x;
			ty = ll.get(i).y;
			dis += (Math.sqrt(Math.pow(tx - x, 2) + Math.pow(ty - y, 2)));
			if (s.gohome) {
				dis += (Math.sqrt(Math.pow(tx, 2) + Math.pow(ty, 2)));
				x = 0;
				y = 0;
			} else {
				x = tx;
				y = ty;
			}
		}
		dis += (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
		return dis;
	}

}

class Stat {
	int gasPrice;
	Item[] items;
	Store[] stores;
	double minPrice;

	public void setItems(String[] b) {
		for (int k = 0; k < b.length; k++) {
			items[k] = new Item();
			if (b[k].endsWith("!")) {
				items[k].itemName = b[k].substring(0, b[k].length() - 1);
				items[k].perishable = true;
			} else {
				items[k].itemName = b[k];
			}
		}
	}
}

class Item {
	String itemName;
	boolean perishable;
}

class Store {
	int x;
	int y;
	Map<String, Integer> price = new HashMap<String, Integer>();

	public String toString() {
		return "x=" + x + "; y=" + y + "; size= " + price.size();
	}

	boolean gohome;
}

class Case {
	private int[] radix;// each radix is equal to or greater than 1.
	private int[] curr;

	public void init(int[] radix) {
		this.radix = radix;
		curr = new int[radix.length];
	}

	public int getTotalCase() {
		int total = 1;
		for (int i = 0; i < radix.length; i++) {
			total *= radix[i];
		}
		return total;
	}

	boolean hasMore = true;

	public boolean hasNext() {
		return hasMore;
	}

	public int[] getCurr() {
		int[] res = curr.clone();
		int k;
		for (k = radix.length - 1; k >= 0; k--) {
			if (curr[k] + 1 == radix[k]) {// carry on
				curr[k] = 0;
			} else {
				curr[k]++;
				break;
			}
		}
		if (k < 0) {
			hasMore = false;
		}
		return res;
	}
}
