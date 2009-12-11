/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package google.code.jam;

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
import java.util.Stack;
import java.util.Map.Entry;

import taocp.v4.f2.perm.Action;
import taocp.v4.f2.perm.PermutationGenerator;
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
public class ShoppingPlan2 {
	private static boolean debug = false;

	static Stat[] stats;

	/**
	 * 
	 */
	public static void readInput() {
		// String name = "G:/MyDocuments/Download/D-large-practice.in";
		String name = "E:/MyDocuments/Download/D-large-practice.in";
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
		// for (int i = 0; i < stats.length; i++) {
		// cal(stats[i]);
		// }
		cal(stats[1]);
		// cal(stats[2]);
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
		int maxItem = 0;
		int maxStore = 0;
		for (Stat stat : stats) {
			if (stat.items.length > maxItem) {
				maxItem = stat.items.length;
			}
			if (stat.stores.length > maxStore) {
				maxStore = stat.stores.length;
			}
		}
		// if(debug)
		System.out.println("maxItem=" + maxItem + "; maxStore=" + maxStore);
		System.out.println("maxItem!=" + MathUtil.factorial((long) maxItem));
	}

	public static void cal(Stat stat) {
		for (int j = 0; j < stat.stores.length; j++) {
			stat.stores[j].index = j;
		}

		int[] counts = new int[stat.items.length];
		int[] cheapest_counts = new int[stat.items.length];
		if (debug)
			System.out.println("items size" + counts.length);
		int i = 0;
		Store[][] itemStores = new Store[stat.items.length][];
		Store[][] cheapestItemStore = new Store[stat.items.length][];// only
		// cheapest.
		for (Item _item : stat.items) {
			String item = _item.itemName;

			int count = 0;
			List<Store> ll = new ArrayList<Store>();// stores provide this item
			int mmin = Integer.MAX_VALUE;
			// store provide this item wiht cheapest price.
			List<Store> minStores = new ArrayList<Store>();
			for (Store store : stat.stores) {
				if (store.price.containsKey(item)) {
					count++;
					ll.add(store);
					if (store.price.get(item) < mmin) {
						mmin = store.price.get(item);
						minStores.clear();
						minStores.add(store);
					} else if (store.price.get(item) == mmin) {
						minStores.add(store);
					}
				}
			}
			if (debug)
				System.out.print("item=" + item + "; ");
			_item.cheapestStores = minStores;
			for (Store store : minStores) {
				store.ll.add(i);// the item[i] is cheapest in this store.
				if (debug) {
					System.out.print(store);
					System.out.println();
				}

			}

			counts[i] = count;
			itemStores[i] = ll.toArray(new Store[0]);

			cheapestItemStore[i] = minStores.toArray(new Store[0]);
			cheapest_counts[i] = minStores.size();

			i++;
		}
		if (debug)
			System.out.println("we have ? stores for item 0: "
					+ itemStores[0].length);
		if (debug)
			System.out.println(Arrays.toString(counts));

		long totalC = 1;
		for (int cc = 0; cc < counts.length; cc++) {
			totalC *= counts[cc];
		}
		// if (debug)
		System.out.println("totalC=" + totalC);

		totalC = 1;
		for (int cc = 0; cc < cheapest_counts.length; cc++) {
			totalC *= cheapest_counts[cc];
		}
		// if (debug)
		System.out.println("For cheapest strategy: totalC=" + totalC);

		if (debug)
			System.out.println(Arrays.deepToString(itemStores));

		// see how many stores we need if we only choose cheapest stores.
		// iterate the store combination.
		Case cas = new Case();
		cas.init(cheapest_counts);
		double itemP = 0;
		double tPrice = 0;//
		stat.minPrice = Integer.MAX_VALUE;
		while (cas.hasMore) {
			itemP = 0;
			for (Store store : stat.stores) {
				store.gohome = false;
			}
			int[] tt = cas.getCurr();
			// if (debug) {
			System.out.println("tt=" + Arrays.toString(tt));
			// }
			Set<Store> set = new HashSet<Store>();
			for (int j = 0; j < stat.items.length; j++) {
				set.add(cheapestItemStore[j][tt[j]]);
				if (stat.items[j].perishable == true) {
					cheapestItemStore[j][tt[j]].gohome = true;
				}
				itemP += (cheapestItemStore[j][tt[j]]).price
						.get(stat.items[j].itemName);
			}
			if (debug)
				System.out.println("item price is" + itemP);
			// if (debug)
			System.out.println("set size =" + set.size() + ";(set size)! ="
					+ MathUtil.factorial(set.size()));
			for (Store t : set) {
				System.out.print(t.index + ", ");
			}
			System.out.println();
			tPrice = itemP + getPrice(set, stat.gasPrice);
			if (debug)
				System.out.println("temp price is" + tPrice);
			if (tPrice < stat.minPrice) {
				stat.minPrice = tPrice;
			}
		}
		// if (debug)
		System.out.println("min price is" + stat.minPrice);
		// for( i=0;i<stat.items.length;i++){
		//			
		// }

		// getLowestPrice(stat, itemStores);
	}

	static void getLowestPrice(Stat stat, Store[][] itemStores) {
		// iterate the store combination.
		Level level = new Level();
		Stack<Level> stack = new Stack<Level>();
		level.storeIndex = new int[stat.items.length];
		for (int j = 0; j < level.storeIndex.length; j++) {
			level.storeIndex[j] = -1;
		}
		stack.add(level);
		// decide each store's cheapest item.
		// cheap(itemStores);
		stat.minPrice = Integer.MAX_VALUE;
		exter: do {
			level = stack.pop();
			if (debug)
				System.out.println("pop " + level);
			int nextIndex = level.nextItemIndex;
			Item tItem = stat.items[nextIndex];

			for (int k = 0; k < itemStores[nextIndex].length; k++) {
				Level levelt = new Level(level);
				Store tStore = itemStores[nextIndex][k];
				levelt.storeIndex[nextIndex] = tStore.index;

				List<Integer> inll = tStore.ll;
				if (tItem.perishable) {
					for (int temp : inll) {
						if (levelt.storeIndex[temp] == -1) {
							levelt.storeIndex[temp] = tStore.index;
						} else if (tStore.price.get(tItem.itemName) != stat.stores[levelt.storeIndex[temp]].price
								.get(tItem.itemName)) {
							if (debug)
								System.out.println("meaningless compbinatoin");
							continue exter;
						}
					}
				} else {// not perishable, so can not merge other perishable
					// items.
					for (int temp : inll) {
						if (stat.items[temp].perishable) {
							// can not merge
						} else {
							levelt.storeIndex[temp] = tStore.index;
						}
					}
				}
				int kk;
				for (kk = nextIndex + 1; kk < stat.items.length; kk++) {
					if (levelt.storeIndex[kk] == -1) {
						levelt.nextItemIndex = kk;
						break;
					}
				}
				if (kk == stat.items.length) {
					if (debug)
						System.out.println("end case cal price!");
					getPrice(stat, levelt);
				} else {
					if (debug)
						System.out.println("push " + levelt);
					stack.push(levelt);
				}
			}

		} while (!stack.isEmpty());

		if (debug)
			System.out.println("min price is " + stat.minPrice);
	}

	private static void getPrice(Stat stat, Level level) {
		int[] choice = level.storeIndex;

		double itemP = 0;
		double tPrice = 0;
		for (Store store : stat.stores) {
			store.gohome = false;
		}
		int[] tt = choice;
		if (debug)
			System.out.println("tt=" + Arrays.toString(tt));
		Set<Store> set = new HashSet<Store>();
		for (int j = 0; j < stat.items.length; j++) {
			set.add(stat.stores[tt[j]]);
			if (stat.items[j].perishable == true) {
				stat.stores[tt[j]].gohome = true;
			}
			itemP += (stat.stores[tt[j]]).price.get(stat.items[j].itemName);
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

	static List<Store> tempStores;

	static double minDis = Integer.MAX_VALUE;

	static double dis = 0;

	private static double getPrice(Set<Store> _set, int gasPrice) {
		List<Store> set = new ArrayList<Store>();
		set.addAll(_set);
		tempStores = set;
		// CollUtil.print(set);
		int size = set.size();
		if (size <= 1) {
			return getDistance(set) * gasPrice;
		}
		Action act = new Action() {
			public void actOn(int[] perm) {
				List<Store> ll = new ArrayList<Store>();
				minDis = Integer.MAX_VALUE;
				dis = 0;

				for (int j = 0; j < perm.length; j++) {
					ll.add(tempStores.get(perm[j] - 1));
				}
				dis = getDistance(ll);
				if (dis < minDis) {
					minDis = dis;
				}
			}
		};
		// int[][] res2 = new int[MathUtil.factorial(size)][size];
		// PermutationGenerator.permPlainChange(size, res2);
		// out of memory();
		PermutationGenerator.permPlainChange(size, 0, act);

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

	List<Store> cheapestStores = new ArrayList<Store>();
}

class Store {
	int x;

	int y;

	Map<String, Integer> price = new HashMap<String, Integer>();

	List<Integer> ll = new ArrayList<Integer>();

	public String toString() {
		return "x=" + x + "; y=" + y + "; size= " + price.size();
	}

	boolean gohome;

	int index;
}

class Level {
	int[] storeIndex; // the index in itemStores[][]

	int nextItemIndex;

	public Level(Level level) {
		storeIndex = level.storeIndex.clone();
		nextItemIndex = level.nextItemIndex;
	}

	public Level() {
	}

	public String toString() {
		return nextItemIndex + "; " + Arrays.toString(storeIndex);
	}
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
