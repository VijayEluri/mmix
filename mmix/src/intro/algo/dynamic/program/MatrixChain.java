/* 
 * Copyright: Copyright (c) 2009 
 * Eddie Wu
 */
package intro.algo.dynamic.program;

import java.util.Arrays;

/**
 * <p>
 * MatrixChain.java
 * </p>
 */
public class MatrixChain {
	public static void main(String[] args) {
		int p[] = new int[] { 30, 35, 15, 5, 10, 20, 25 };
		
		int n=6;
		int[][] m = new int[n+1][n+1];//index from 1 to make it simple
		int [] s = new int[n+1];

		for(int i=1;i<=n;i++){
		    m[i][i]=0;//for length==1;
		}

		for(int i=2; i<=n;i++){//loop on length
		    for(int j=1; j+i-1<=n;j++){//try different start point
		        //range is [j,j+i-1]
		        int min=Integer.MAX_VALUE;
		        int temp=0;
		        for(int k=j;k<j+i-1;k++){//split after k_th position.
		            //temp=m[j][k]+m[k+1][j+i-1]+ p[k-1]*p[k]*p[k+1];
		        	//bug, the size of column may be not the raw size.
		        	temp=m[j][k]+m[k+1][j+i-1]+ p[j-1]*p[k]*p[j+i-1];
		            //the length of subproblem are less than i;
		            if(temp<min){
		                min=temp;
		            }
		        }
		        m[j][j+i-1]=min;
		    }
		}
		//System.out.println(Arrays.deepToString(m));
		System.out.println("minimum scarla operation is "+m[1][n]);
	}
}
