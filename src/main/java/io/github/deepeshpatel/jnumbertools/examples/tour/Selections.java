/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.tour;

import io.github.deepeshpatel.jnumbertools.base.Calculator;
import io.github.deepeshpatel.jnumbertools.base.JNumberTools;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

/**
 * Four ways to select {@code r} items from {@code n}, and a bag with duplicates.
 * <p>
 * Two questions decide which builder to use: does the <b>order</b> of the picks matter, and may an
 * item be picked <b>more than once</b>? Picking 2 of A, B, C gives a different count each way:
 * <pre>
 *                     no repeats                          repeats allowed
 *   order matters     permutations().nPk(r, list)    6    permutations().repetitive(r, list)   9
 *   order ignored     combinations().unique(r, list) 3    combinations().repetitive(r, list)   6
 * </pre>
 * A third question is whether the pool itself has duplicates (a bag with two A's):
 * {@code combinations().multiset(bag, r)} picks from it, and each result is a map from item to how
 * many times it was taken. Every count here is also computed from its formula with
 * {@link Calculator}, so the example checks itself.
 */
public final class Selections {

    static final List<String> LETTERS = List.of("A", "B", "C");

    static final String ORDERED_NO_REPEATS = "order matters, no repeats";
    static final String ORDERED_REPEATS = "order matters, repeats allowed";
    static final String UNORDERED_NO_REPEATS = "order ignored, no repeats";
    static final String UNORDERED_REPEATS = "order ignored, repeats allowed";

    private Selections() {
    }

    /** @return all ordered picks of {@code r} different letters (k-permutations), in lexicographic order */
    public static List<List<String>> orderedNoRepeats(int r) {
        return JNumberTools.permutations().nPk(r, LETTERS).lexOrder().stream().toList();
    }

    /** @return all ordered picks of {@code r} letters where a letter may repeat */
    public static List<List<String>> orderedRepeats(int r) {
        return JNumberTools.permutations().repetitive(r, LETTERS).lexOrder().stream().toList();
    }

    /** @return all unordered picks of {@code r} different letters */
    public static List<List<String>> unorderedNoRepeats(int r) {
        return JNumberTools.combinations().unique(r, LETTERS).lexOrder().stream().toList();
    }

    /** @return all unordered picks of {@code r} letters where a letter may repeat */
    public static List<List<String>> unorderedRepeats(int r) {
        return JNumberTools.combinations().repetitive(r, LETTERS).lexOrder().stream().toList();
    }

    /** @return every way to take {@code r} items from a bag, as item to number taken (in order of the bag) */
    public static List<Map<String, Integer>> fromBag(LinkedHashMap<String, Integer> bag, int r) {
        return JNumberTools.combinations().multiset(bag, r).lexOrder().stream().toList();
    }

    /** @return the number of picks of {@code r} from {@code n} distinct items for each of the four ways, from the library */
    public static Map<String, BigInteger> libraryCounts(int n, int r) {
        List<Integer> items = IntStream.range(0, n).boxed().toList();
        Map<String, BigInteger> counts = new LinkedHashMap<>();
        counts.put(ORDERED_NO_REPEATS, JNumberTools.permutations().nPk(r, items).count());
        counts.put(ORDERED_REPEATS, JNumberTools.permutations().repetitive(r, items).count());
        counts.put(UNORDERED_NO_REPEATS, JNumberTools.combinations().unique(r, items).count());
        counts.put(UNORDERED_REPEATS, JNumberTools.combinations().repetitive(r, items).count());
        return counts;
    }

    /** @return the same four counts from their formulas: nPr, n^r, nCr and nCr(n + r - 1, r) */
    public static Map<String, BigInteger> formulaCounts(int n, int r) {
        Calculator calculator = new Calculator();
        Map<String, BigInteger> counts = new LinkedHashMap<>();
        counts.put(ORDERED_NO_REPEATS, calculator.nPr(n, r));
        counts.put(ORDERED_REPEATS, calculator.power(n, r));
        counts.put(UNORDERED_NO_REPEATS, calculator.nCr(n, r));
        counts.put(UNORDERED_REPEATS, calculator.nCrRepetitive(n, r));
        return counts;
    }

    public static void run() {
        System.out.println("Picking 2 of " + LETTERS);
        System.out.println("  " + ORDERED_NO_REPEATS + " (" + orderedNoRepeats(2).size() + "): " + orderedNoRepeats(2));
        System.out.println("  " + ORDERED_REPEATS + " (" + orderedRepeats(2).size() + "): " + orderedRepeats(2));
        System.out.println("  " + UNORDERED_NO_REPEATS + " (" + unorderedNoRepeats(2).size() + "): " + unorderedNoRepeats(2));
        System.out.println("  " + UNORDERED_REPEATS + " (" + unorderedRepeats(2).size() + "): " + unorderedRepeats(2));

        LinkedHashMap<String, Integer> bag = new LinkedHashMap<>();
        bag.put("A", 2);
        bag.put("B", 1);
        bag.put("C", 1);
        System.out.println();
        System.out.println("Picking 2 from the bag " + bag + " (item -> number taken):");
        System.out.println("  " + fromBag(bag, 2));

        System.out.println();
        System.out.println("Picking 3 of 6 items: library count vs formula");
        Map<String, BigInteger> fromLibrary = libraryCounts(6, 3);
        Map<String, BigInteger> fromFormula = formulaCounts(6, 3);
        fromLibrary.forEach((way, count) -> System.out.printf("  %-31s %4d  formula %4d  %s%n",
                way, count, fromFormula.get(way), count.equals(fromFormula.get(way)) ? "equal" : "DIFFERENT"));
    }

    public static void main(String[] args) {
        run();
    }
}
