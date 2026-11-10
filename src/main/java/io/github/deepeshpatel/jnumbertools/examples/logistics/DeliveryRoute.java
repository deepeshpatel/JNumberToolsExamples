/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.logistics;

import io.github.deepeshpatel.jnumbertools.base.JNumberTools;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Shortest delivery round trip by brute force.
 * <p>
 * Real-life problem: a van leaves the depot, visits every stop once and returns. For a handful
 * of stops, trying every visiting order is fast, simple and provably optimal. The library
 * enumerates the orders without recursion code, and its count shows when brute force stops
 * being sensible (8 stops: 40 320 orders, 12 stops: 479 001 600).
 */
public final class DeliveryRoute {

    /** Stop name to (x, y) in kilometres. The depot is at (0, 0). */
    static final Map<String, double[]> STOPS = stops();

    /** A round trip, with its length in kilometres. */
    public record Route(List<String> order, double kilometres) {
    }

    private DeliveryRoute() {
    }

    private static Map<String, double[]> stops() {
        Map<String, double[]> m = new LinkedHashMap<>();
        m.put("A", new double[]{2, 3});
        m.put("B", new double[]{5, 1});
        m.put("C", new double[]{6, 6});
        m.put("D", new double[]{1, 7});
        m.put("E", new double[]{8, 4});
        m.put("F", new double[]{3, 5});
        m.put("G", new double[]{7, 8});
        m.put("H", new double[]{4, 2});
        return m;
    }

    /** @return the length of depot, then the stops in order, then back to the depot */
    public static double length(List<String> order) {
        double total = 0;
        double[] previous = {0, 0};
        for (String stop : order) {
            double[] here = STOPS.get(stop);
            total += Math.hypot(here[0] - previous[0], here[1] - previous[1]);
            previous = here;
        }
        return total + Math.hypot(previous[0], previous[1]);
    }

    /** @return number of visiting orders for {@code stops} stops */
    public static BigInteger orderCount(int stops) {
        return JNumberTools.permutations().unique(stops).count();
    }

    /** @return the shortest round trip over the first {@code stopCount} stops; the first of equal routes wins */
    public static Route shortest(int stopCount) {
        List<String> names = List.copyOf(STOPS.keySet()).subList(0, stopCount);
        Route best = null;
        for (List<String> order : JNumberTools.permutations().unique(names).lexOrder()) {
            double km = length(order);
            if (best == null || km < best.kilometres()) {
                best = new Route(order, km);
            }
        }
        return best;
    }

    public static void run() {
        for (int n : new int[]{4, 8, 10, 12}) {
            System.out.println(n + " stops: " + orderCount(n) + " possible orders");
        }
        System.out.println();

        Route naive = new Route(List.copyOf(STOPS.keySet()), length(List.copyOf(STOPS.keySet())));
        Route best = shortest(STOPS.size());
        System.out.printf("Alphabetical route %s: %.2f km%n", naive.order(), naive.kilometres());
        System.out.printf("Shortest route     %s: %.2f km%n", best.order(), best.kilometres());
    }

    public static void main(String[] args) {
        run();
    }
}
