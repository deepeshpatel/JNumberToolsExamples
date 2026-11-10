/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.logistics;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeliveryRouteTest {

    @Test
    void orderCountIsFactorial() {
        assertEquals(BigInteger.valueOf(40_320), DeliveryRoute.orderCount(8));
        assertEquals(BigInteger.valueOf(479_001_600), DeliveryRoute.orderCount(12));
    }

    @Test
    void shortestRouteOverEightStops() {
        var best = DeliveryRoute.shortest(8);
        assertEquals(List.of("A", "F", "D", "G", "C", "E", "B", "H"), best.order());
        assertEquals(29.95, best.kilometres(), 0.005);
        assertEquals(best.kilometres(), DeliveryRoute.length(best.order()), 1e-12);
        assertTrue(best.kilometres() < DeliveryRoute.length(List.of("A", "B", "C", "D", "E", "F", "G", "H")));
    }

    @Test
    void agreesWithAnIndependentSearchOverSixStops() {
        double expected = independentMinimum(new ArrayList<>(), new ArrayList<>(List.of("A", "B", "C", "D", "E", "F")));
        assertEquals(expected, DeliveryRoute.shortest(6).kilometres(), 1e-9);
    }

    @Test
    void reversingARouteKeepsItsLength() {
        var order = List.of("C", "A", "F", "B");
        var reversed = List.of("B", "F", "A", "C");
        assertEquals(DeliveryRoute.length(order), DeliveryRoute.length(reversed), 1e-9);
    }

    private static double independentMinimum(List<String> chosen, List<String> remaining) {
        if (remaining.isEmpty()) {
            return DeliveryRoute.length(chosen);
        }
        double best = Double.MAX_VALUE;
        for (int i = 0; i < remaining.size(); i++) {
            List<String> nextChosen = new ArrayList<>(chosen);
            nextChosen.add(remaining.get(i));
            List<String> nextRemaining = new ArrayList<>(remaining);
            nextRemaining.remove(i);
            best = Math.min(best, independentMinimum(nextChosen, nextRemaining));
        }
        return best;
    }
}
