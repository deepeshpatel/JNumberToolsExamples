/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.usecases.storage;

import io.github.deepeshpatel.jnumbertools.base.Calculator;
import io.github.deepeshpatel.jnumbertools.base.JNumberTools;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Storing a fixed-size subset as one small integer with the combinadic number system.
 * <p>
 * Real-life problem: a user holds exactly 8 of 64 permissions, or a record is tagged with exactly 10
 * of 1,000 labels. A bitmask needs one bit per possible item (64 or 1,000 bits). But there are only
 * C(64, 8) = 4.4 billion such subsets, so a number below that is enough: 33 bits. The combinadic
 * (combinatorial) number system gives each k-subset a unique number from 0 to C(n, k) - 1.
 * Encoding is a sum of binomials; decoding is done by the library.
 * <p>
 * Items are numbered from 0. For a subset {@code c_1 < c_2 < ... < c_k} the number is
 * {@code C(c_1, 1) + C(c_2, 2) + ... + C(c_k, k)}.
 */
public final class SubsetEncoding {

    private static final Calculator CALC = new Calculator();

    private SubsetEncoding() {
    }

    /** @return the number of the subset made of the given distinct item ids, in any order */
    public static BigInteger encode(int... ids) {
        int[] sorted = ids.clone();
        Arrays.sort(sorted);
        BigInteger value = BigInteger.ZERO;
        for (int i = 0; i < sorted.length; i++) {
            if (sorted[i] < 0 || (i > 0 && sorted[i] == sorted[i - 1])) {
                throw new IllegalArgumentException("ids must be distinct and not negative: " + Arrays.toString(ids));
            }
            int r = i + 1;
            if (sorted[i] >= r) {
                value = value.add(CALC.nCr(sorted[i], r));
            }
        }
        return value;
    }

    /** @return the item ids, ascending, of the k-subset with the given number */
    public static int[] decode(BigInteger value, int k) {
        List<Integer> descending = JNumberTools.numberSystem().combinadic(value, k).combinadicValues;
        return descending.reversed().stream().mapToInt(Integer::intValue).toArray();
    }

    /** @return number of distinct k-subsets of n items */
    public static BigInteger subsetCount(int n, int k) {
        return CALC.nCr(n, k);
    }

    /** @return bits needed to store the number of any k-subset of n items */
    public static int bitsNeeded(int n, int k) {
        return subsetCount(n, k).subtract(BigInteger.ONE).bitLength();
    }

    /** @return true if the numbers of all k-subsets of n items are exactly 0 .. C(n,k)-1, each once */
    public static boolean numbersAreExactlyTheFirstOnes(int n, int k) {
        var seen = new HashSet<BigInteger>();
        for (List<Integer> subset : JNumberTools.combinations().unique(n, k).lexOrder()) {
            seen.add(encode(subset.stream().mapToInt(Integer::intValue).toArray()));
        }
        return seen.size() == subsetCount(n, k).intValueExact()
                && IntStream.range(0, seen.size()).allMatch(i -> seen.contains(BigInteger.valueOf(i)));
    }

    public static void run() {
        int[] permissions = {41, 2, 63, 11, 5, 50, 20, 33};
        BigInteger value = encode(permissions);
        int[] restored = decode(value, permissions.length);

        System.out.println("Permissions held (8 of 64): " + Arrays.toString(Arrays.stream(permissions).sorted().toArray()));
        System.out.println("Stored as the number      : " + value);
        System.out.println("Restored from the number  : " + Arrays.toString(restored));
        System.out.println();
        System.out.println("Bits needed to store a subset");
        for (int[] nk : new int[][]{{64, 8}, {100, 12}, {1000, 10}}) {
            System.out.printf("  %2d of %-4d: %3d bits as a number, %4d bits as a bitmask (%s subsets)%n",
                    nk[1], nk[0], bitsNeeded(nk[0], nk[1]), nk[0], subsetCount(nk[0], nk[1]));
        }
        System.out.println();
        System.out.println("All 792 subsets of 5 from 12 get the numbers 0..791 exactly once: "
                + numbersAreExactlyTheFirstOnes(12, 5));
    }

    public static void main(String[] args) {
        run();
    }
}
