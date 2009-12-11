package taocp.v4.f0.booleanevaluation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * find the shortest formula length for n variable boolean function.
 * 
 * we start from shortest formula for n=0,1 combine them to get the shortest length
 * for n+1.
 * 
 * we should be able to acquire C(f) if we consider reducing r if there are duplicate operator in formula
 * </p>
 * <p>
 * Copyright: Copyright (c) 2009
 * </p>
 * 
 * the mechanism:
 * construct/iterate the shortest formula first and tag each function based on the length
 * of this fumula,
 * every function may have different formula with different length. but since we ensure 
 * the shorter funciton will tag the fuction first, we can ensure the tagged value is shortest
 * 
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class FindNormalLength {
	static boolean debug = true;
	private static final int N = 4;//keep it fixed because it takes much time for 5. 

	private int[] L = new int[(int) (1 << ((1<< N) - 1))];//only consider normal fuction
	private int c = L.length - N - 1;//count of un-touched normal function after init.
	int f = 0;//fuction
	int r = 0;//the length of formula
	List<List<Integer>> listOfList = new ArrayList<List<Integer>>();
	//maintain a list for length from 0 To n.
	public int[] getNormalLength(){
		return L;
	}
	
	public int[] findNormalLength() {
		int k = 0;		
		initList0();
		
		List<Integer> listJ = null;
		List<Integer> listK = null;

		labeld: while (c != 0) {
			r++;
			listOfList.add(r, new LinkedList<Integer>());
			System.out.println("untouched functions count = " + c);
			System.out.println("start work for length(r) = " + r);
			
			for (int j = 0; j <= (r - 1 - j); j++) {
				k = r - 1 - j;
				listJ = listOfList.get(j);
				listK = listOfList.get(k);

				if (j == k) {
					for (int ti = 0; ti < listJ.size(); ti++) {
						for (int tj = ti; tj < listJ.size(); tj++) {
							int g = listJ.get(ti);
							int h = listJ.get(tj);
							combineGH(g, h);
							if(c == 0) break labeld;
						}
					}
				} else {
					for (int g : listJ) {
						for (int h : listK) {
							combineGH(g, h);
							if(c == 0) break labeld;
						}
					}
				}
			}
		}
		return collect();
	}

	private void combineGH(int g, int h) {
		f = g & h;
		judgef();
		f = ~g & h;
		judgef();
		f = g & ~h;
		judgef();
		f = g | h;
		judgef();
		f = g ^ h;
		judgef();
	}

	

	private void judgef() {
		if (L[f] == -1) {
			L[f] = r;
			c--;
			listOfList.get(r).add(f);
		}else{
			if(debug){
				//System.out.println("original value is: "+L[f]+"; new value is bigger, and it is "+r);
			}
		}
		
	}

	private void initList0() {
		L[0] = 0;
		for (int i = 1; i < L.length; i++) {
			L[i] = -1;
		}
		int xk = 0;
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 1; i <= N; i++) {
			xk =  ((1<< (1<<N)) - 1)
					/  ((1 << (1 << (N - i))) + 1);
			L[xk] = 0;
			if (debug) {
				System.out.println("k = " + i + "; x(k) = "
						+ Integer.toBinaryString(xk));
			}
			list.add(xk);
		}
		listOfList.add(0, list);
		System.out.println("L = " + L.length);
		System.out.println();
		
	}
	public static void main(String[] args){
		long start = System.currentTimeMillis();
		new FindNormalLength().findNormalLength();
		long end = System.currentTimeMillis();
		System.out.println("seconds = " + (end - start) );
	}
	
	private int[] collect() {
		int [] count = new int[r+1]; 
		for(int i = 0; i < L.length; i++){
			if(L[i] == -1){
				throw new RuntimeException("L[i] == -1");
				
			}else{
				count[L[i]] += 1;
			}
			
		}
		System.out.println();
		for(int i = 0; i < count.length; i++){
			System.out.println("cf = " + i + "; functions = " + (count[i]<<1));
		}
		return count;
	}
}
