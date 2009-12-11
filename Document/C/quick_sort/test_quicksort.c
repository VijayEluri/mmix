#include "util.h"
#include "quicksort.h"
int main(int argc, char* args[]){
        int *t = getIntArray();
        printArray(t,16);
        sort(t, 0, 15);
        printArray(t,16);
}

