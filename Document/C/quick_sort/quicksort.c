#include <stdlib.h>
#include "util.h" 
/**
 * sort the array pa[a,b]
 */
void sort(int*pa, int a, int b){
	if((b-a)==0){
		return;
	}else if((b-a)==1){
		//only two elements, swap if reversed.
		if(pa[b]<pa[a]){
			int temp = pa[a];
			pa[a]=pa[b];
			pa[b]=temp;
		}	
	}else{
		//partition
		int i=a+1;
		int j=b;
		int paviot=pa[a];
		do{
			while(pa[i]<paviot&&i<=j){
				i++;
			}
			while(pa[j]>paviot&&i<=j){
				j--;
			}
			if(i<j){

				int temp = pa[i];
				pa[i]=pa[j];
				pa[j]=temp;
				i++,j--;
			}
		}while(i<=j);
		
		if(j==a){//pa[j] is the minimum one.
			printf("a=%d, b=%d, j=%d, i=%d", a,b, j, i);
			printArray(pa,16);
			sort(pa,i,b);
		}
		else	if(i==(b+1)){//pa[a] is the maximum one.:wq

			pa[a]=pa[j];
			pa[j]=paviot;
			printf("a=%d, b=%d, j=%d, i=%d", a,b, j, i);
			printArray(pa,16);
			sort(pa,a,j-1);
		}else{
			
		pa[a]=pa[j];
		pa[j]=paviot;
		printf("a=%d, b=%d, j=%d, i=%d", a,b, j, i);
		printArray(pa,16);


		sort(pa,a,j-1);
		sort(pa,i,b);
		}	
	}
}
