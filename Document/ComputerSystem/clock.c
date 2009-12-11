/**
 * The cycle counter is platform dependent, it is supposed to be availabe on 
 * x86 platform.
 */
#include <stdlib.h>
#include <stdio.h>
//#include "clock.h"

/* Initialize the cycle counter */
static unsigned cyc_hi = 0;
static unsigned cyc_lo = 0;


/* Set *hi and *lo to the high and low order bits of the cycle counter.
Implementation requires assembly code to use the rdtsc instruction. */
void access_counter(unsigned *hi, unsigned *lo)
{
   asm("rdtsc; movl %%edx,%0; movl %%eax,%1" /* Read cycle counter */
       : "=r" (*hi), "=r" (*lo) /* and move results to */
       : /* No input */ /* the two outputs */
       : "%edx", "%eax");
}

/* Record the current value of the cycle counter. */
void start_cycle_counter()
{
   printf("in start_cycle_counter\n");
   access_counter(&cyc_hi, &cyc_lo);
}

/* Return the number of cycles since the last call to start_counter. */
double get_cycle_counter()
{
   unsigned ncyc_hi, ncyc_lo;
   unsigned hi, lo, borrow;
   double result;
   
   /* Get cycle counter */
   access_counter(&ncyc_hi, &ncyc_lo);

   /* Do double precision subtraction */
   lo = ncyc_lo - cyc_lo;
   borrow = lo > ncyc_lo;
   hi = ncyc_hi - cyc_hi - borrow;
   result = (double) hi * (1 << 30) * 4 + lo;
   if (result < 0) {
       fprintf(stderr, "Error: counter returns neg value: %.0f\n", result);
   }else{
	fprintf(stderr, "result=%.1f\n",result);
   }
   printf("in get_cycle_counter\n");
   return result;
}

/* Return the number of seconds since the last call to start_counter. */
double get_cycle_counter()
{

}

