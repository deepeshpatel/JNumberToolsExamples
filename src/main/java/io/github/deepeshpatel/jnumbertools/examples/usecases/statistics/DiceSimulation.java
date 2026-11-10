/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.usecases.statistics;

import io.github.deepeshpatel.jnumbertools.base.JNumberTools;
import io.github.deepeshpatel.jnumbertools.generator.permutation.repetitive.RepetitivePermutationBuilder;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * The sum of two dice: the exact distribution by enumeration, against a seeded simulation.
 * <p>
 * Real-life problem: how likely is each total when two dice are rolled, and how many simulated
 * rolls are needed before the simulation can be trusted? The space is small (36 rolls), so it can
 * be enumerated and the exact answer is known. That makes it a good place to measure a Monte Carlo
 * simulation: {@code choice} draws rolls with replacement, the way real dice behave, and the error
 * against the exact answer shrinks as the number of rolls grows.
 */
public final class DiceSimulation {

    static final List<Integer> DIE = List.of(1, 2, 3, 4, 5, 6);

    private DiceSimulation() {
    }

    private static RepetitivePermutationBuilder<Integer> twoDice() {
        return JNumberTools.permutations().repetitive(2, DIE);
    }

    /** @return for each total 2..12, in how many of the 36 rolls it occurs, by enumerating all rolls */
    public static Map<Integer, Long> exactCounts() {
        Map<Integer, Long> counts = new TreeMap<>();
        for (List<Integer> roll : twoDice().lexOrder()) {
            counts.merge(roll.get(0) + roll.get(1), 1L, Long::sum);
        }
        return counts;
    }

    /** @return the exact probability of a total, in percent */
    public static double exactPercent(int total) {
        long all = exactCounts().values().stream().mapToLong(Long::longValue).sum();
        return 100.0 * exactCounts().getOrDefault(total, 0L) / all;
    }

    /** @return for each total, how often it came up in {@code rolls} simulated rolls (reproducible for a seed) */
    public static Map<Integer, Long> simulatedCounts(int rolls, long seed) {
        Map<Integer, Long> counts = new TreeMap<>();
        twoDice().choice(rolls, new Random(seed)).stream()
                .forEach(roll -> counts.merge(roll.get(0) + roll.get(1), 1L, Long::sum));
        return counts;
    }

    /** @return the largest gap, in percentage points, between a simulated and an exact probability over all totals */
    public static double largestError(int rolls, long seed) {
        Map<Integer, Long> simulated = simulatedCounts(rolls, seed);
        double largest = 0;
        for (int total : exactCounts().keySet()) {
            double simulatedPercent = 100.0 * simulated.getOrDefault(total, 0L) / rolls;
            largest = Math.max(largest, Math.abs(simulatedPercent - exactPercent(total)));
        }
        return largest;
    }

    public static void run() {
        int rolls = 10_000;
        Map<Integer, Long> exact = exactCounts();
        Map<Integer, Long> simulated = simulatedCounts(rolls, 42);

        System.out.println("Two dice: 36 rolls, enumerated; then " + rolls + " simulated rolls (seed 42)");
        System.out.println("  total  ways   exact  simulated   error");
        for (int total : exact.keySet()) {
            double simulatedPercent = 100.0 * simulated.getOrDefault(total, 0L) / rolls;
            System.out.printf("  %5d  %4d  %5.2f%%     %5.2f%%  %+5.2f%n",
                    total, exact.get(total), exactPercent(total), simulatedPercent, simulatedPercent - exactPercent(total));
        }

        System.out.println();
        System.out.println("Largest error in percentage points, by number of simulated rolls:");
        for (int n : new int[]{100, 10_000, 1_000_000}) {
            System.out.printf("  %,9d rolls: %.3f%n", n, largestError(n, 42));
        }
    }

    public static void main(String[] args) {
        run();
    }
}
