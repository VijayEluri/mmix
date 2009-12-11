#include <stdio.h>
int main(int argc, char* args[]) {
    start_timer();
    int a=5;
    double a = get_timer();
    printf("take %.6f seconds",a);
}