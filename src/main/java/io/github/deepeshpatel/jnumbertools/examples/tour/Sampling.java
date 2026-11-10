/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.tour;

import io.github.deepeshpatel.jnumbertools.base.JNumberTools;
import io.github.deepeshpatel.jnumbertools.generator.permutation.repetitive.RepetitivePermutationBuilder;

import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * Random draws from a combinatorial space: {@code choice} and {@code sample}.
 * <p>
 * Every builder can draw random elements without generating the space. The two calls differ the
 * way rolling dice differs from dealing cards:
 * <ul>
 *   <li>{@code choice(n, random)} draws <b>with replacement</b>. Draws are independent, so the same
 *       element can come up twice. Use it to simulate (dice, Monte Carlo).</li>
 *   <li>{@code sample(n, random)} draws <b>without replacement</b>. Every element appears at most
 *       once, and asking for more elements than exist is refused. Use it to pick distinct items
 *       (test cases, a pilot group, a lottery draw).</li>
 * </ul>
 * Both take a {@link Random}, so a fixed seed makes a draw reproducible. The space here is the
 * 36 possible rolls of two dice.
 */
public final class Sampling {

    static final List<Integer> DIE = List.of(1, 2, 3, 4, 5, 6);

    private Sampling() {
    }

    private static RepetitivePermutationBuilder<Integer> twoDiceRolls() {
        return JNumberTools.permutations().repetitive(2, DIE);
    }

    /** @return number of possible rolls of two dice, 6 x 6 */
    public static int outcomes() {
        return twoDiceRolls().count().intValueExact();
    }

    /** @return {@code n} rolls drawn with replacement; the same roll may repeat */
    public static List<List<Integer>> withReplacement(int n, long seed) {
        return twoDiceRolls().choice(n, new Random(seed)).stream().toList();
    }

    /** @return {@code n} distinct rolls drawn without replacement; fails if {@code n} exceeds the space */
    public static List<List<Integer>> withoutReplacement(int n, long seed) {
        return twoDiceRolls().sample(n, new Random(seed)).stream().toList();
    }

    /** @return how many different elements {@code draw} contains */
    public static int distinct(List<?> draw) {
        return new HashSet<>(draw).size();
    }

    /** @return the library's message when asking for more distinct rolls than exist, or "accepted" */
    public static String refusalFor(int n) {
        try {
            withoutReplacement(n, 1);
            return "accepted";
        } catch (IllegalArgumentException e) {
            return e.getMessage();
        }
    }

    public static void run() {
        int space = outcomes();
        System.out.println("Two dice: " + space + " possible rolls");
        System.out.println();

        List<List<Integer>> choice = withReplacement(space, 42);
        System.out.println("choice(" + space + "), seed 42 : " + distinct(choice) + " different rolls in " + choice.size()
                + " draws (repeats are expected)");
        System.out.println("  first five: " + choice.subList(0, 5));

        List<List<Integer>> sample = withoutReplacement(space, 42);
        System.out.println("sample(" + space + "), seed 42 : " + distinct(sample) + " different rolls in " + sample.size()
                + " draws (every roll exactly once, in random order)");
        System.out.println("  first five: " + sample.subList(0, 5));

        System.out.println();
        System.out.println("sample(" + (space + 1) + ") is refused: " + refusalFor(space + 1));
        System.out.println("Same seed, same draw: " + (choice.equals(withReplacement(space, 42))
                && sample.equals(withoutReplacement(space, 42))));
    }

    public static void main(String[] args) {
        run();
    }
}
