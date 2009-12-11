#include <stdlib.h>
int main(int argc, char* args[]){
	printf("RAND MAX is %d",RAND_MAX); 
	int i=1;
	for(i=1; i<10;i++){
		printf("%d\n", rand());

	}
	printf("\n");
	srand(1);
	for(i=1; i<10;i++){
		printf("%d\n", rand());

	}
	printf("\n");
	srand(1);
	for(i=1; i<10;i++){
		printf("%d\n", rand());

	}
	return 0;
}
