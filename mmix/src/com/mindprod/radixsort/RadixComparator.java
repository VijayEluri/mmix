/*
 * @(#)RadixComparator.java
 *
 * Summary: Comparator to use is a RadixSort.
 *
 * Copyright: (c) 1998-2009 Roedy Green, Canadian Mind Products, http://mindprod.com
 *
 * Licence: This software may be copied and used freely for any purpose but military.
 *          see http://mindprod.com/contact/nonmil.html
 *
 * Requires: JDK 1.5+
 *
 * Created with: IntelliJ IDEA IDE.
 *
 * Version History:
 *  1.0 1998-01-01 - initial version.
 */

package com.mindprod.radixsort;

import java.util.Comparator;

/**
 * Comparator to use is a RadixSort.
 *
 *
 * @author Roedy Green, Canadian Mind Products
 * @since 1998
 * @version 1.0 1998-01-01 - initial version.
 */

public interface RadixComparator<T> extends Comparator<T>
    {
    // -------------------------- PUBLIC INSTANCE  METHODS --------------------------

    // Treat key as if it were a string of bytes
    // number returned must lie in the range 0..255!

    public abstract int getKeyByteAt( T a, int offset );

    // to sort fixed length Latin1 strings you might write:
    // return ((String)a).charAt(offset)&0xff;

    // to sort fixed length Unicode strings you might write:
    //  if (offset%2 == 0 /* e.g. even */)
    //     return (((String)a).charAt(offset/2)>>>8)&0xff; // high byte
    //   else
    //     return ((String)a).charAt(offset/2)&0xff; // low byte

    // To sort binary integers you have to split them up into 4
    // unsigned bytes treating the most significant byte as offset 0.
    // If you were trying to sort signed ints, you would have
    // to add a bias to them to make them all appear positive
    // before breaking them up.

    // To sort doubles, you would have to extract the bits with
    // Double.doubleToLongBits, then deal with negatives by
    //  inverting the data portion of negative numbers.
    //  1-bit sign
    //  11-bit base 2 exponent biased+1023
    //  52-bit fraction, lead 1 implied
    // Then split them into 8 unsigned bytes and provide a bias to make them
    // all unsigned positive.
    }// end interface RadixComparator
