/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.experiments;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbTestDesignTest {

    @Test
    void variantCountIsTheProductOfTheFactorLevels() {
        assertEquals(18, AbTestDesign.variantCount());
    }

    @Test
    void aVisitorAlwaysSeesTheSameValidVariant() {
        for (String id : List.of("user-1", "user-2", "alice@example.com", "")) {
            List<Object> first = AbTestDesign.variantFor(id);
            assertEquals(first, AbTestDesign.variantFor(id));
            assertTrue(AbTestDesign.HEADLINES.contains(first.get(0)));
            assertTrue(AbTestDesign.BUTTONS.contains(first.get(1)));
            assertTrue(AbTestDesign.LAYOUTS.contains(first.get(2)));
        }
        assertEquals(List.of("Save money", "blue", "two-column"), AbTestDesign.variantFor("user-1"));
        assertEquals(List.of("Save money", "orange", "single-column"), AbTestDesign.variantFor("user-2"));
    }

    @Test
    void trafficIsSpreadEvenlyAcrossAllVariants() {
        Map<String, Integer> counts = AbTestDesign.assignmentCounts(1800);
        assertEquals(18, counts.size());
        assertEquals(1800, counts.values().stream().mapToInt(Integer::intValue).sum());
        assertEquals(98, counts.values().stream().mapToInt(Integer::intValue).min().orElseThrow());
        assertEquals(102, counts.values().stream().mapToInt(Integer::intValue).max().orElseThrow());
    }

    @Test
    void pilotVariantsAreDistinctAndReproducible() {
        var pilot = AbTestDesign.pilotVariants(6, 42);
        assertEquals(pilot, AbTestDesign.pilotVariants(6, 42));
        assertEquals(6, new HashSet<>(pilot).size());
        assertEquals(List.of("Save money", "blue", "single-column"), pilot.getFirst());
    }
}
