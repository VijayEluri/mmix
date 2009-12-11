#include <sys/time.h>
#include <unistd.h>
#include <stdio.h>
static struct timeval tstart;

/* Record current time */
void start_timer()
{
    gettimeofday(&tstart, NULL);
}

/* Get number of seconds since last call to start_timer */
double get_timer()
{
    struct timeval tfinish;
    long sec, usec;
    
    gettimeofday(&tfinish, NULL);
    printf("start at:%d seconds, %d micro seconds", tstart.tv_sec, tstart.tv_usec);
    printf("end at:%d seconds, %d micro seconds", tfinish.tv_sec, tfinish.tv_usec);
    sec = tfinish.tv_sec - tstart.tv_sec;
    usec = tfinish.tv_usec - tstart.tv_usec;
    return sec + 1e-6*usec;
}
