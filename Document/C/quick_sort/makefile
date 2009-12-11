test_quicksort.o: test_quicksort.c

util.o: util.c

quicksort.o: quicksort.c

test_sorted.o: test_sorted.c

test_quicksort: util.o test_quicksort.o quicksort.o

test_sorted: util.o test_sorted.o quicksort.o

all: test_sorted test_quicksort

clean: all
	rm test_sorted test_quicksort *.o 
