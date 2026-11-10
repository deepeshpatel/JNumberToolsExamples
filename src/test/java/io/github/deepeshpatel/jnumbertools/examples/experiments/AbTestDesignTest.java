/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.experiments;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    }

    /** Stability across releases is the feature: if this changes, every visitor is reassigned. */
    @Test
    void assignmentIsStableAcrossReleases() {
        assertEquals(List.of("Save time", "green", "single-column"), AbTestDesign.variantFor("user-1"));
        assertEquals(List.of("Save money", "orange", "two-column"), AbTestDesign.variantFor("alice@example.com"));
    }

    @Test
    void differentExperimentsAssignIndependently() {
        int different = 0;
        for (int i = 0; i < 200; i++) {
            String id = "user-" + i;
            if (!AbTestDesign.variantFor("checkout-2026", id).equals(AbTestDesign.variantFor(id))) {
                different++;
            }
        }
        // independent assignments agree about 1 time in 18, so most visitors differ
        assertTrue(different > 150, "only " + different + " of 200 visitors changed variant");
    }

    @Test
    void trafficIsSpreadAboutEvenlyForSequentialAndRandomIds() {
        Map<String, Integer> counts = AbTestDesign.assignmentCounts(1800);
        assertEquals(18, counts.size());
        assertEquals(1800, counts.values().stream().mapToInt(Integer::intValue).sum());
        assertTrue(counts.values().stream().allMatch(c -> c > 60 && c < 140), counts.toString());

        var uuidCounts = new HashMap<String, Integer>();
        for (int i = 0; i < 1800; i++) {
            uuidCounts.merge(AbTestDesign.variantFor(UUID.nameUUIDFromBytes(("v" + i).getBytes()).toString()).toString(), 1, Integer::sum);
        }
        assertEquals(18, uuidCounts.size());
        assertTrue(uuidCounts.values().stream().allMatch(c -> c > 60 && c < 140), uuidCounts.toString());
    }

    @Test
    void pilotVariantsAreDistinctAndReproducible() {
        var pilot = AbTestDesign.pilotVariants(6, 42);
        assertEquals(pilot, AbTestDesign.pilotVariants(6, 42));
        assertEquals(6, pilot.size());
        assertEquals(6, new HashSet<>(pilot).size());
    }
}
