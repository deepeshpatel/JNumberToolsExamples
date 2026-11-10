/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.tour;

import io.github.deepeshpatel.jnumbertools.base.Calculator;
import io.github.deepeshpatel.jnumbertools.base.JNumberTools;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Number systems behind ranking: factoradic, combinadic and derangadic.
 * <p>
 * Ranking a permutation or a combination is arithmetic in a special number system. The library
 * exposes those systems directly, so the arithmetic can be inspected. This example does not
 * just print the digits: it rebuilds the decimal value from the digits, and rebuilds the
 * permutation from the factoradic digits, so every claim in the output is checked by code.
 * <ul>
 *   <li><b>Factoradic</b>: digit {@code i} counts multiples of {@code i!}. For a rank, the digits are
 *       the Lehmer code of the permutation (how many unused items are skipped at each position).</li>
 *   <li><b>Combinadic</b>: a value is a sum of binomials {@code C(c_k, k) + ... + C(c_1, 1)} with
 *       {@code c_k > ... > c_1 >= 0}.</li>
 *   <li><b>Derangadic</b>: ranks derangements (permutations without fixed points).</li>
 * </ul>
 */
public final class NumberSystems {

    private static final Calculator CALC = new Calculator();

    private NumberSystems() {
    }

    /**
     * @return the factoradic digits of {@code value}, most significant first. The library lists them
     * least significant first, so they are reversed here to read like ordinary numerals.
     */
    public static List<Integer> factoradicDigits(long value) {
        return JNumberTools.numberSystem().factoradic(value).factoradicValues.reversed();
    }

    /** @return the decimal value of factoradic digits: digit i (from the right, starting at 0) times i! */
    public static BigInteger factoradicValue(List<Integer> digits) {
        BigInteger total = BigInteger.ZERO;
        for (int i = 0; i < digits.size(); i++) {
            total = total.add(BigInteger.valueOf(digits.get(i)).multiply(CALC.factorial(digits.size() - 1 - i)));
        }
        return total;
    }

    /** @return the digits written out, for example {@code 1*3! + 2*2! + 0*1! + 0*0!} */
    public static String factoradicExpression(List<Integer> digits) {
        return IntStream.range(0, digits.size())
                .mapToObj(i -> digits.get(i) + "*" + (digits.size() - 1 - i) + "!")
                .collect(Collectors.joining(" + "));
    }

    /**
     * Decodes factoradic digits as a Lehmer code: at each position take the unused item with the
     * given index. Missing leading digits are zeros.
     *
     * @return the permutation of 0..n-1 that the digits describe
     */
    public static int[] permutationFromFactoradic(List<Integer> digits, int n) {
        List<Integer> padded = new ArrayList<>(Collections.nCopies(n - digits.size(), 0));
        padded.addAll(digits);
        List<Integer> unused = new ArrayList<>(IntStream.range(0, n).boxed().toList());
        int[] permutation = new int[n];
        for (int i = 0; i < n; i++) {
            permutation[i] = unused.remove((int) padded.get(i));
        }
        return permutation;
    }

    /** @return the permutation of {@code n} items with the given rank, from the library */
    public static int[] permutationAtRank(long rank, int n) {
        return JNumberTools.unrankOf().permutation().unique(BigInteger.valueOf(rank), n);
    }

    /** @return the combinadic digits {@code c_k > ... > c_1} of {@code value} for the given degree k */
    public static List<Integer> combinadicDigits(long value, int degree) {
        return JNumberTools.numberSystem().combinadic(value, degree).combinadicValues;
    }

    /** @return the sum {@code C(c_k, k) + ... + C(c_1, 1)} for digits listed from c_k down to c_1 */
    public static BigInteger combinadicValue(List<Integer> digits) {
        BigInteger total = BigInteger.ZERO;
        for (int i = 0; i < digits.size(); i++) {
            int n = digits.get(i);
            int r = digits.size() - i;
            if (n >= r) {
                total = total.add(CALC.nCr(n, r));
            }
        }
        return total;
    }

    /** @return the sum written out, for example {@code C(4,3) + C(3,2) + C(0,1)} */
    public static String combinadicExpression(List<Integer> digits) {
        return IntStream.range(0, digits.size())
                .mapToObj(i -> "C(" + digits.get(i) + "," + (digits.size() - i) + ")")
                .collect(Collectors.joining(" + "));
    }

    /** @return the derangement of {@code order} items with the given rank, via the derangadic system */
    public static List<Integer> derangementViaNumberSystem(long rank, int order) {
        return Arrays.stream(JNumberTools.numberSystem().derangadic(rank, order).derangement()).boxed().toList();
    }

    /** @return the same derangement, taken straight from the builder */
    public static List<Integer> derangementViaBuilder(long rank, int order) {
        return JNumberTools.derangements().of(order)
                .byRanks(List.of(BigInteger.valueOf(rank)))
                .iterator().next();
    }

    public static void run() {
        List<Integer> digits = factoradicDigits(10);
        System.out.println("Factoradic");
        System.out.println("  10 = " + digits + " = " + factoradicExpression(digits) + " = " + factoradicValue(digits));
        System.out.println("  read as a Lehmer code over 4 items: " + Arrays.toString(permutationFromFactoradic(digits, 4)));
        System.out.println("  the library's permutation of rank 10: " + Arrays.toString(permutationAtRank(10, 4)));

        List<Integer> combinadic = combinadicDigits(7, 3);
        System.out.println();
        System.out.println("Combinadic, degree 3");
        System.out.println("  7 = " + combinadic + " = " + combinadicExpression(combinadic) + " = " + combinadicValue(combinadic));

        System.out.println();
        System.out.println("Derangadic, 5 items");
        System.out.println("  derangements in total: " + JNumberTools.derangements().of(5).count());
        System.out.println("  rank 20 via the number system: " + derangementViaNumberSystem(20, 5));
        System.out.println("  rank 20 via the builder      : " + derangementViaBuilder(20, 5));
    }

    public static void main(String[] args) {
        run();
    }
}
