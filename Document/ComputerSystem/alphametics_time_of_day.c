//#include <time.h>
//#include <sys/time.h>
#include <stdio.h>
#include <stdlib.h>
#include "get_time_of_day.h"

void embed(int n, int indexFrom);
void getSwitchTable(int n, int tab[]);
int factorial(int n);

static int x[8];//
static int y[10];

int main(int argc, char* args[]) {
	x[0] = 1000;
	x[1] = 91;
	x[2] = -90;
	x[3] = 1;
	x[4] = -9000;
	x[5] = -900;
	x[6] = 10;
	x[7] = -1;
	int count = x[0] * 9 + 5 * x[1] + 6 * x[2] + 7 * x[3] + 1 * x[4] + 0
		* x[5] + 8 * x[6] + 2 * x[7];
	if (count != 0) {
		printf("init error: count=%d", count);
	}
	/**int ii=0;
	int[] tt= new int[] {9, 3, 4, 7, 1, 0, 8, 5, 2, 6};
	for (ii = 0; ii <8; ii++) {
		count += tt[ii] * x[ii];
	}
	if (count != 0) {
		printf("init error: count=%d", count);
	}*/

	time_t start;
	time_t end;
	start_timer();
	embed(10,0);
	double results = get_timer();//end=times(NULL);
//	printf("%s seconds\n",ctime(start));	
//	printf("%s \n",ctime(end));	
	printf("results=%.6f seconds\n",(results));	
	//printf("%d \n",(end-start)/CLK_TCK);	
	return 0;
}

int factorial(int n){
//	printf("n = %d\n",n);
	if(n==2 || n ==1) return n;
	return n * factorial(n-1);
}

void getSwitchTable(int n, int tab[]){
	int j=0;	
	if (n == 2) {
		tab[0] = 1;
		tab[1] = 1;
		return;
	}
	int* des = malloc(n*4);
	int* asc = malloc(n*4);
	int i=0;
	for (i = 0; i < n - 1; i++) {
		des[i] = n - 1 - i;
		asc[i] = i + 1;
	}
		
	int* temp = malloc(4*factorial(n - 1));
	getSwitchTable(n - 1, temp);

	int reverse = 0;
		
	for (i = 0; i < factorial(n - 1); i++) {
		if (reverse) {
			asc[n - 1] = temp[i];
			for(j=0;j<n;j++){
				tab[i*n+j]=asc[j];
			}
			reverse=0;
		} else {
			des[n - 1] = temp[i] + 1;
			for(j=0;j<n;j++){
				tab[i*n+j]=des[j];
			}
			reverse=1;
		}
	}
}

void embed(int n, int indexFrom) {
		
	int nn = factorial(n);
	int* tab = malloc(nn*4);;
	printf("in method embed()");
	getSwitchTable(n, tab);

	int* init = malloc(n*4);
	int i = 0;
	for (; i < n; i++) {
		init[i] = i + indexFrom;
	}

	int count;
	if (init[0] == 0 || init[4] == 0) {
	//	return;
	}else{
	count = 0;
	i = 0;
	for (; i < 8; i++) {
		count += init[i] * x[i];
	}
	if (count == 0) {
		printf("answer found: [%d, %d, %d, %d, %d, %d, %d, %d, %d, %d]\n",
			init[0],init[1],init[2],init[3],init[4],init[5],init[6],init[7],init[8],init[9]);			
	}
	}

	int posl = 0;// inclusive
	int posr = 0;// inclusive

	int temp;
	i = 0;
	for (; i < nn - 1; i++) {
		posl = tab[i] - 1;
		posr = tab[i];
		temp = init[posr];

		// swap
		init[posr] = init[posl];
		init[posl] = temp;

		if (init[0] == 0 || init[4] == 0) {
			continue;
		}
		count = 0;
		int ii = 0;
		for (ii = 0; ii < 8; ii++) {
			count += init[ii] * x[ii];
		}	
		if (count == 0) {
				printf("answer found: [%d, %d, %d, %d, %d, %d, %d, %d, %d, %d]\n",
				init[0],init[1],init[2],init[3],init[4],init[5],init[6],init[7],init[8], init[9]);			
		}
	}
}
