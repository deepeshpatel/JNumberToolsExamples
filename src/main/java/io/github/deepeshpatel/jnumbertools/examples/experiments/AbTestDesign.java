/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.experiments;

import io.github.deepeshpatel.jnumbertools.base.JNumberTools;
import io.github.deepeshpatel.jnumbertools.generator.product.simple.SimpleProductBuilder;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

/**
 * Multivariate (A/B/n) experiment design.
 * <p>
 * Real-life problem: a landing page has 3 headlines, 3 button colours and 2 layouts. Every
 * combination is a variant (18 in total). Two practical needs follow. First, every visitor must
 * always see the same variant, without a lookup table: hash the visitor id to a rank and jump
 * straight to that variant. Second, when traffic only supports a few variants, pick a reproducible
 * set of distinct ones to pilot.
 */
public final class AbTestDesign {

    static final List<String> HEADLINES = List.of("Save time", "Save money", "Join today");
    static final List<String> BUTTONS = List.of("green", "blue", "orange");
    static final List<String> LAYOUTS = List.of("single-column", "two-column");

    private AbTestDesign() {
    }

    private static SimpleProductBuilder variants() {
        return JNumberTools.cartesianProduct()
                .simpleProductOf(HEADLINES)
                .and(BUTTONS)
                .and(LAYOUTS);
    }

    /** @return number of variants, 3 x 3 x 2 */
    public static int variantCount() {
        return variants().count().intValueExact();
    }

    /** @return the variant for a visitor: stable for the same id, no storage needed */
    public static List<Object> variantFor(String visitorId) {
        BigInteger rank = BigInteger.valueOf(Math.floorMod(visitorId.hashCode(), variantCount()));
        return variants().byRanks(List.of(rank)).iterator().next();
    }

    /** @return how many of {@code visitors} synthetic visitors (user-0, user-1, ...) each variant received */
    public static Map<String, Integer> assignmentCounts(int visitors) {
        Map<String, Integer> counts = new TreeMap<>();
        for (int i = 0; i < visitors; i++) {
            counts.merge(variantFor("user-" + i).toString(), 1, Integer::sum);
        }
        return counts;
    }

    /** @return {@code n} distinct variants to pilot, reproducible for a seed */
    public static List<List<Object>> pilotVariants(int n, long seed) {
        return variants().sample(n, new Random(seed)).stream().toList();
    }

    public static void run() {
        System.out.println("Variants: " + variantCount() + " (" + HEADLINES.size() + " headlines x "
                + BUTTONS.size() + " buttons x " + LAYOUTS.size() + " layouts)");
        System.out.println();
        for (String id : List.of("user-1", "user-2", "alice@example.com")) {
            System.out.println("Visitor " + id + " always sees " + variantFor(id));
        }

        Map<String, Integer> counts = assignmentCounts(1800);
        System.out.println();
        System.out.println("1800 visitors over " + counts.size() + " variants: fewest "
                + counts.values().stream().min(Integer::compare).orElseThrow() + ", most "
                + counts.values().stream().max(Integer::compare).orElseThrow() + " (ideal 100 each)");

        System.out.println();
        System.out.println("Pilot with six distinct variants (seed 42):");
        pilotVariants(6, 42).forEach(v -> System.out.println("  " + v));
    }

    public static void main(String[] args) {
        run();
    }
}
