#include <stdio.h>
#include <stdlib.h>

void 	bubble_sort(long* a, int n);
int main(int argc, char* argv[]){
	long longA[10000];
	int i;
	printf("size=%d\n",sizeof(long));
	for(i=0; i<10000;i++){
		longA[	i]=100000 - i;
	}
		for(i=0; i<10000; i+=1000){
		printf("%d\t",longA[i]);
	}
	printf("\n");

	//sort
	bubble_sort( longA, 10000);
	
	for(i=0; i<10000; i+=1000){
		printf("%d\t",longA[i]);

	}
	printf("\n");
}

	void bubble_sort(long* a, int n){
	int i, j,index;
	int max, temp;
	for(i=n-1;i>0;i--){
		max = a[i];
		index = i;
		for(j=0;j<i;j++){
			if(a[j]>max){
				max=a[j];
				index=j;
			}
		}
		temp =a[index];
		a[index]=a[i];
		a[i]=temp;
	}
}
