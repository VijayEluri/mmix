#include <stdio.h>
#include <stdlib.h>
#include "/usr/include/asm/unistd.h"

_syscall1(long,ourcall,long,num);

main(){
	printf("our syscall --> num in = 5, num out = %d\n", ourcall(5));
}
