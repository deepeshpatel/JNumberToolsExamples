/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.testing;

import io.github.deepeshpatel.jnumbertools.base.JNumberTools;
import io.github.deepeshpatel.jnumbertools.generator.product.simple.SimpleProductBuilder;

import java.util.List;

/**
 * Cross-platform test matrix.
 * <p>
 * Real-life problem: a web app must be tested on every browser, operating system and
 * network profile it supports. The full product is easy to enumerate, but some
 * combinations do not exist (Safari does not run on Windows) and a nightly smoke run
 * has a time budget.
 * <p>
 * The library supplies the product and a deterministic way to thin it ({@code lexOrderMth}).
 * Rules between dimensions are not a library feature, so they are applied here as a plain
 * stream filter.
 */
public final class CrossPlatformTestMatrix {

    static final List<String> BROWSERS = List.of("Chrome", "Firefox", "Safari", "Edge");
    static final List<String> SYSTEMS = List.of("Windows", "macOS", "Linux", "Android", "iOS");
    static final List<String> NETWORKS = List.of("fast", "slow-3g");

    private CrossPlatformTestMatrix() {
    }

    private static SimpleProductBuilder fullSpace() {
        return JNumberTools.cartesianProduct()
                .simpleProductOf(BROWSERS)
                .and(SYSTEMS)
                .and(NETWORKS);
    }

    /** Assumed rules for this example: Safari only on Apple systems, Edge only on Windows or macOS. */
    static boolean isSupported(List<Object> combo) {
        String browser = (String) combo.get(0);
        String system = (String) combo.get(1);
        return switch (browser) {
            case "Safari" -> system.equals("macOS") || system.equals("iOS");
            case "Edge" -> system.equals("Windows") || system.equals("macOS");
            default -> true;
        };
    }

    /** @return size of the unfiltered product */
    public static int fullSize() {
        return fullSpace().count().intValueExact();
    }

    /** @return every supported combination, in lexicographic order */
    public static List<List<Object>> supported() {
        return fullSpace().lexOrder().stream()
                .filter(CrossPlatformTestMatrix::isSupported)
                .toList();
    }

    /**
     * Nightly smoke subset: take every {@code every}-th combination of the full product
     * (rank 0, every, 2*every, ...) and keep the supported ones. Deterministic, so the same
     * night-to-night subset can be reproduced and discussed.
     */
    public static List<List<Object>> smokeSubset(int every) {
        return fullSpace().lexOrderMth(every, 0).stream()
                .filter(CrossPlatformTestMatrix::isSupported)
                .toList();
    }

    public static void run() {
        System.out.println("Full product (browser x system x network): " + fullSize());
        System.out.println("Supported combinations                   : " + supported().size());
        System.out.println();
        System.out.println("Smoke subset (every 4th rank, supported only):");
        smokeSubset(4).forEach(c -> System.out.println("  " + c));
    }

    public static void main(String[] args) {
        run();
    }
}
