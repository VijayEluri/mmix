#include "util.h"
#include "quicksort.h"
int main(int argc, char* args[]){
        int* t = getIntArray();
	printf("%s","original Array is: \n");                   
        printArray(t,16);
        sort(t, 0, 15);
        printf("%s","sorted Array is: \n");        
        printArray(t,16);
	printf("\n");
	
	printf("%s","original sorted Array is: \n");                   
        printArray(t,16);
	sort(t, 0, 15);
	printf("%s","sorted Array is: \n");        
        printArray(t,16);
	printf("\n");
	
	reverse(t,0,15);
	printf("%s","Reversed Array is: ");
	printArray(t,16);
	sort(t, 0, 15);
	printf("%s","sorted Array is: ");
        printArray(t,16);
}

