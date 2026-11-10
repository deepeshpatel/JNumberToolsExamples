/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.config;

import io.github.deepeshpatel.jnumbertools.base.JNumberTools;

import java.util.List;

/**
 * Feature-flag configurations to test.
 * <p>
 * Real-life problem: a shop has five feature flags, each on or off. Testing all 2^5 = 32
 * configurations is fine, but with 20 flags it is a million. A common compromise is to test
 * every configuration with at most two flags switched on, which covers every flag alone and
 * every pair of flags together. The subset builder produces exactly those, sized by range.
 * Dependencies between flags are not a library feature, so they are applied as a filter.
 */
public final class FeatureFlagTesting {

    static final List<String> FLAGS = List.of("newCheckout", "darkMode", "betaSearch", "fastShipping", "giftWrap");

    private FeatureFlagTesting() {
    }

    /** Assumed rule for this example: gift wrap only exists in the new checkout. */
    static boolean isValid(List<String> enabled) {
        return !enabled.contains("giftWrap") || enabled.contains("newCheckout");
    }

    /** @return number of all on/off configurations, 2^n */
    public static int allConfigurations() {
        return JNumberTools.subsets().of(FLAGS).all().count().intValueExact();
    }

    /** @return number of configurations with between {@code from} and {@code to} flags on */
    public static int configurationsWithFlagsOn(int from, int to) {
        return JNumberTools.subsets().of(FLAGS).inRange(from, to).count().intValueExact();
    }

    /** @return configurations with at most two flags on that satisfy the dependency rule */
    public static List<List<String>> pairwiseConfigurations() {
        return JNumberTools.subsets().of(FLAGS).inRange(0, 2).lexOrder().stream()
                .filter(FeatureFlagTesting::isValid)
                .toList();
    }

    public static void run() {
        System.out.println("Flags: " + FLAGS);
        System.out.println("All configurations          : " + allConfigurations());
        System.out.println("With at most 2 flags on     : " + configurationsWithFlagsOn(0, 2));
        System.out.println("With 1 or 2 flags on        : " + configurationsWithFlagsOn(1, 2));
        System.out.println("20 flags, all configurations: " + JNumberTools.subsets().of(20).all().count());
        System.out.println();
        System.out.println("Configurations to test (at most 2 on, giftWrap needs newCheckout):");
        pairwiseConfigurations().forEach(c -> System.out.println("  " + c));
    }

    public static void main(String[] args) {
        run();
    }
}
