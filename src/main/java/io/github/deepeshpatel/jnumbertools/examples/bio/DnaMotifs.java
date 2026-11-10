/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.bio;

import io.github.deepeshpatel.jnumbertools.base.Calculator;
import io.github.deepeshpatel.jnumbertools.base.JNumberTools;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * DNA words: all k-mers and all distinct rearrangements of a motif.
 * <p>
 * Real-life problem: a bioinformatics pipeline needs (a) every possible DNA word of length k,
 * for example to build a k-mer index, and (b) every distinct sequence that has the same base
 * composition as a known motif. Repeated bases make many rearrangements identical, so naive
 * permutation code returns duplicates. The multiset builder returns each distinct sequence once.
 */
public final class DnaMotifs {

    static final List<Character> BASES = List.of('A', 'C', 'G', 'T');

    private DnaMotifs() {
    }

    /** @return number of DNA words of length {@code k}, 4^k */
    public static BigInteger kmerCount(int k) {
        return JNumberTools.permutations().repetitive(k, BASES).count();
    }

    /** @return the first {@code limit} DNA words of length {@code k} in lexicographic order */
    public static List<String> firstKmers(int k, int limit) {
        return JNumberTools.permutations().repetitive(k, BASES)
                .lexOrder().stream()
                .limit(limit)
                .map(DnaMotifs::join)
                .toList();
    }

    /** @return base composition of a sequence, bases in order of first appearance */
    static LinkedHashMap<Character, Integer> composition(String sequence) {
        LinkedHashMap<Character, Integer> counts = new LinkedHashMap<>();
        for (char base : sequence.toCharArray()) {
            counts.merge(base, 1, Integer::sum);
        }
        return counts;
    }

    /** @return number of distinct sequences with the same base composition as {@code motif} */
    public static BigInteger distinctRearrangements(String motif) {
        return JNumberTools.permutations().multiset(composition(motif)).count();
    }

    /** @return the first {@code limit} distinct rearrangements of {@code motif}; bases rank in order of first appearance in the motif */
    public static List<String> firstRearrangements(String motif, int limit) {
        return JNumberTools.permutations().multiset(composition(motif))
                .lexOrder().stream()
                .limit(limit)
                .map(DnaMotifs::join)
                .toList();
    }

    /** @return the same count from the multinomial formula n! / (n1! n2! ...) */
    public static BigInteger multinomialCount(String motif) {
        int[] counts = composition(motif).values().stream().mapToInt(Integer::intValue).toArray();
        return new Calculator().multinomial(counts);
    }

    private static String join(List<Character> bases) {
        var sb = new StringBuilder(bases.size());
        bases.forEach(sb::append);
        return sb.toString();
    }

    public static void run() {
        System.out.println("DNA words of length 4 : " + kmerCount(4) + ", first five " + firstKmers(4, 5));
        System.out.println("DNA words of length 12: " + kmerCount(12));
        System.out.println();

        String motif = "GATTACA";
        System.out.println("Motif " + motif + " has " + composition(motif));
        System.out.println("Orderings of 7 positions       : " + JNumberTools.permutations().unique(7).count());
        System.out.println("Distinct rearrangements        : " + distinctRearrangements(motif));
        System.out.println("Multinomial 7!/(3! 2! 1! 1!)   : " + multinomialCount(motif));
        System.out.println("First five rearrangements      : " + firstRearrangements(motif, 5));
    }

    public static void main(String[] args) {
        run();
    }
}
