/*
 * @(#)RadixSort.java
 *
 * Summary: RadixSort for and array of objects.
 *
 * Copyright: (c) 1996-2009 Roedy Green, Canadian Mind Products, http://mindprod.com
 *
 * Licence: This software may be copied and used freely for any purpose but military.
 *          see http://mindprod.com/contact/nonmil.html
 *
 * Requires: JDK 1.5+
 *
 * Created with: IntelliJ IDEA IDE.
 *
 * Version History:
 *  1.1 1998-11-10 - add name and address.
 *  1.2 1998-12-28 - use JDK 1.2 style Comparator interface.
 *  1.3 2000-09-29 - fix bug. Was not sorting first column
 *  1.4 2002-02-19 - use java.util.Comparator by default
 *  1.5 2002-03-30 - tidy code.
 *  1.6 2008-01-01 - add generics to Comparator
 */

package com.mindprod.radixsort;

/**
 * RadixSort for and array of objects.
 *
 * RadixSort beat HeapSort which beat QuickSort.
 * RadixSort works in linear time.  It is a
 * little harder to use than ordinary sorts,
 * but it can handle big sorts many times faster.
 * Its main weakness is small sorts with long
 * keys. The sort is stable.  It does not disturb
 * pre-existing order of equal keys.
 *
 * @author Roedy Green, Canadian Mind Products
 * @since 1996
 * @version 1.6 2008-01-01 - add generics to Comparator
 */

public class RadixSort<T>
    {
    // ------------------------------ CONSTANTS ------------------------------

    private static final boolean DEBUGGING = false;

    private static final String EmbeddedCopyright =
            "copyright (c) 1996-2009 Roedy Green, Canadian Mind Products, http://mindprod.com";

    // ------------------------------ FIELDS ------------------------------

    // callback object we are passed
    private RadixComparator<? super T> radixComparator;

    // pointer to source work array
    private T[] sourceArray;

    // pointer to target work array
    private T[] targetArray;

    // pointer to the array of user's objects we are sorting
    private T[] userArray;

    // used to tally how many of each key byte there were.
    private int[] counts;

    // how many bytes long the sorting key is.
    private int keyLength;

    // -------------------------- PUBLIC STATIC METHODS --------------------------

    // create a RadixSort object and sort the user's array

    public static <T> void sort( T[] userArray, RadixComparator<? super T> radixComparator, int keyLength )
        {
        RadixSort<T> h = new RadixSort<T>();
        h.radixComparator = radixComparator;
        h.userArray = userArray;
        h.keyLength = keyLength;
        if ( h.isAlreadySorted() )
            {
            return;
            }
        h.radixSort();
        if ( DEBUGGING )
            {
            // debug ensure sort is working
            if ( !h.isAlreadySorted() )
                {
                System.out.println( "Sort failed" );
                }
            }
        return;
        }// end sort

    // -------------------------- OTHER METHODS --------------------------

    // check if user's array is already sorted

    private boolean isAlreadySorted()
        {
        for ( int i = 1; i < userArray.length; i++ )
            {
            if ( radixComparator.compare( userArray[ i ], userArray[ i - 1 ] ) < 0 )
                {
                return false;
                }
            }
        return true;
        }// end isAlreadySorted

    // radixSort that works like a mechanical card
    // sorter, sorting least significant byte of the
    // key first. This works in linear time
    // proportional to key length and number of items
    // sorted.
    @SuppressWarnings( "unchecked" )
    private void radixSort()
        {
        counts = new int[256];
        sourceArray = userArray;
        // can't allocate an array of T
        // I am not clever enough to get rid of the warning.
        targetArray = ( T[] ) new Object[userArray.length];
        // sort least significant column first,
        // working back to the most significant.
        for ( int col = keyLength - 1; col >= 0; col-- )
            {
            sortCol( col );
            // swap source and target, very quick, just swapping pointers
            T[] temp = sourceArray;
            sourceArray = targetArray;
            targetArray = temp;
            }// end for

        // copy results back to userArray, if necessary
        if ( sourceArray != userArray )
            {
            System.arraycopy( sourceArray, 0, userArray, 0, sourceArray.length );
            }
        }// end radixSort

    // sort sourceArray by given column.  Put results in targetArray
    private void sortCol( int col )
        {
        // pass 1 count how many of each key there are:
        for ( int i = 0; i < counts.length; i++ )
            {
            counts[ i ] = 0;
            }
        for ( int i = 0; i < sourceArray.length; i++ )
            {
            counts[ radixComparator.getKeyByteAt( sourceArray[ i ], col ) ]++;
            }// end for

        // calculate slot number where each item will go.
        {
        int soFar = 0;
        for ( int i = 0; i < counts.length; i++ )
            {
            int temp = counts[ i ];
            counts[ i ] = soFar;
            soFar += temp;
            }// end for
        }// end block
        // pass 2 move each object to its new slot
        for ( int from = 0; from < sourceArray.length; from++ )
            {
            int keyByte = radixComparator.getKeyByteAt( sourceArray[ from ], col );
            int to = counts[ keyByte ]++;
            targetArray[ to ] = sourceArray[ from ];
            }// end for
        }// end sortCol
    }// end class RadixSort
