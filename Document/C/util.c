#include "util.h"
static int t[] = {503,  87, 512, 61,
                   908, 170, 897, 275,
                   653, 426, 154, 509,
                   612, 677, 765, 703};
/**pa: point to the array of int
 * la: lengh of the array
 *
 */
void printArray(int* pa, int la){
        int i;
        printf("[");
        for(i=0;i<la-1;i++){
                printf("%d, ",pa[i]);
        }
        printf("%d",pa[la-1]);
        printf("]\n");
}
void reverse(int *pa, int a, int b){
	int t;
	while(a<b){
		t=pa[a];
		pa[a]=pa[b];
		pa[b]=t;
		a++, b--;
	}
}	
int* getIntArray(){
	return t;
}
