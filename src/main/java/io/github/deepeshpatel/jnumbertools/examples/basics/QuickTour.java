/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.basics;

import io.github.deepeshpatel.jnumbertools.base.JNumberTools;

import java.util.ArrayList;
import java.util.List;

/**
 * A quick tour of JNumberTools: one call per generator family.
 * <p>
 * Real-life angle: before enumerating anything, ask how big the space is. A lock with
 * 20 distinct positions has more orderings than anyone can try, yet the library
 * reports that count instantly, and a 52-card poker hand space (2.6 million) is small
 * enough to enumerate in a blink.
 */
public final class QuickTour {

    private QuickTour() {
    }

    /** @return the printed lines, so that tests can check them */
    public static List<String> lines() {
        List<String> out = new ArrayList<>();

        out.add("Permutations of 4 items          : " + JNumberTools.permutations().unique(4).count());
        out.add("First 3 permutations of A,B,C    : "
                + JNumberTools.permutations().unique(List.of("A", "B", "C")).lexOrder().stream().limit(3).toList());
        out.add("Permutations of 20 items         : " + JNumberTools.permutations().unique(20).count());

        out.add("5 choose 2                       : " + JNumberTools.combinations().unique(5, 2).count());
        out.add("Poker hands (52 choose 5)        : " + JNumberTools.combinations().unique(52, 5).count());

        out.add("Subsets of 3 items               : " + JNumberTools.subsets().of(3).all().count());
        out.add("Derangements of 4 items          : " + JNumberTools.derangements().of(4).count());

        out.add("Sizes x colours                  : "
                + JNumberTools.cartesianProduct()
                        .simpleProductOf(List.of("S", "M", "L"))
                        .and(List.of("red", "blue"))
                        .count());
        return out;
    }

    public static void run() {
        lines().forEach(System.out::println);
    }

    public static void main(String[] args) {
        run();
    }
}
