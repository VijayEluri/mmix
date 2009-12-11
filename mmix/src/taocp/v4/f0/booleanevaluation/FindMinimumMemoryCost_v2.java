package taocp.v4.f0.booleanevaluation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * based on find normal length.
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class FindMinimumMemoryCost_v2 {
	static boolean debug = false;
	private static final int N = 4;//keep it fixed because it takes much time for 5. 

	private int[] L = new int[(int) (1 << ((1<< N) - 1))];//only consider normal fuction
	private int c = L.length - N - 1;//count of un-touched normal function after init.
	
	int r = 0;//the length of formula
	List<List<Integer>> listOfList = new ArrayList<List<Integer>>();
	//maintain a list for length from 0 To n.

	public void findMinimumMemoryCost() {
		
		initList0();
		
		List<Integer> listR = null;
		

		labeld: while (c != 0) {
			r++;
			System.out.println("untouched functions count = " + c);
			System.out.println("start work for length(r) = " + r);
			listOfList.add(r, new LinkedList<Integer>());		
			
			listR = listOfList.get(r-1);
			//System.out.println(listR);
			for(int temp:listR){
				Set<Integer> set = new MinimumMemoryChain().convertTruthTable4(temp);
				//System.out.println(set);
				for(int tt: set){
					judgef(tt);
				}
				if(c == 0) break labeld;
			}
						
		}
		collect();
	}

	

	/**
	 * 
	 * @param f fuction
	 */

	private void judgef(int f ) {
		if (L[f] == -1) {
			L[f] = r;
			c--;
			listOfList.get(r).add(f);
		}else{
			if(debug){
				System.out.println("original value is: "+L[f]+"; new value is bigger, and it is "+r);
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
		System.out.println(list);
		System.out.println();
		
	}
	public static void main(String[] args){
		long start = System.currentTimeMillis();
		new FindMinimumMemoryCost_v2().findMinimumMemoryCost();
		long end = System.currentTimeMillis();
		System.out.println("seconds = " + (end - start) );
	}
	
	private void collect() {
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
	}
}
