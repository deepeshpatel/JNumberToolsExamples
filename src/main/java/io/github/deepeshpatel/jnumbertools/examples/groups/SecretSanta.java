/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.groups;

import io.github.deepeshpatel.jnumbertools.base.Calculator;
import io.github.deepeshpatel.jnumbertools.base.JNumberTools;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Secret Santa without redraws.
 * <p>
 * Real-life problem: everybody gives one gift and receives one, and nobody may draw their
 * own name. Shuffling and retrying works, but about 63 % of plain shuffles are rejected.
 * A derangement is exactly an arrangement in which nobody stays in their own place, so the
 * library can sample a valid draw directly and can also say how many valid draws exist.
 */
public final class SecretSanta {

    private SecretSanta() {
    }

    /** @return giver to receiver, in the order of {@code people}; nobody is assigned themselves */
    public static Map<String, String> assign(List<String> people, long seed) {
        List<String> receivers = JNumberTools.derangements()
                .of(people)
                .sample(1, new Random(seed))
                .iterator().next();

        Map<String, String> draw = new LinkedHashMap<>();
        for (int i = 0; i < people.size(); i++) {
            draw.put(people.get(i), receivers.get(i));
        }
        return draw;
    }

    /** @return number of valid draws for {@code n} people (the subfactorial !n) */
    public static BigInteger validDraws(int n) {
        return JNumberTools.derangements().of(n).count();
    }

    /** @return fraction of plain shuffles that would be valid, {@code !n / n!} (tends to 1/e) */
    public static double naiveShuffleSuccessRate(int n) {
        Calculator calculator = new Calculator();
        return calculator.subFactorial(n).doubleValue() / calculator.factorial(n).doubleValue();
    }

    public static void run() {
        List<String> people = List.of("Aarav", "Bella", "Chen", "Dmitri", "Elena", "Farid");

        System.out.println("People: " + people.size() + ", valid draws: " + validDraws(people.size())
                + " of " + new Calculator().factorial(people.size()) + " possible shuffles");
        System.out.printf("A plain shuffle would be valid only %.1f%% of the time%n",
                100 * naiveShuffleSuccessRate(people.size()));
        System.out.println();
        System.out.println("Draw (seed 42):");
        assign(people, 42).forEach((giver, receiver) -> System.out.println("  " + giver + " -> " + receiver));
    }

    public static void main(String[] args) {
        run();
    }
}
