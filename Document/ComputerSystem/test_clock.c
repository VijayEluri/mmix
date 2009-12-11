#include <stdio.h>
#include "clock.h"

/* Estimate the clock rate by measuring the cycles that elapse */
/* while sleeping for sleeptime seconds */
double mhz(int verbose, int sleeptime)
{
    double rate;

    start_cycle_counter();
    sleep(sleeptime);
    rate = get_cycle_counter() ;
    printf("rate=%.1f\n",rate);
    rate = rate / (1e6*sleeptime);
    if (verbose)
        printf("Processor clock rate ?= %.1f MHz\n", rate);
    return rate;
}

int main(int argc, char* args[]){
    mhz(1,1);    
}
