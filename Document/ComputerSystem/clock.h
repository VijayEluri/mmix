/*initialize the counter*/
extern void start_cycle_counter();
/*Returns: number of cycles since last call to start counter*/
extern double get_cycle_counter();

/**
 * The client code looks like:
 * start_cycle_counter();
 * {the code to be measured}
 * double cycles = get_cycle_counter();
 * since the measurement is done in the same platform. the cycle is a reasonable 
 * unit. but can also be converted to seconds or milli seconds if necessary.
 */
