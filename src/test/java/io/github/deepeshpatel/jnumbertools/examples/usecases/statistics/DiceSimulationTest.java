/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.usecases.statistics;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DiceSimulationTest {

    @Test
    void exactCountsAreTheFamiliarTriangle() {
        Map<Integer, Long> exact = DiceSimulation.exactCounts();
        assertEquals(List.of(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12), List.copyOf(exact.keySet()));
        assertEquals(List.of(1L, 2L, 3L, 4L, 5L, 6L, 5L, 4L, 3L, 2L, 1L), List.copyOf(exact.values()));
        assertEquals(36L, exact.values().stream().mapToLong(Long::longValue).sum());
        assertEquals(100.0 * 6 / 36, DiceSimulation.exactPercent(7), 1e-12);
        assertEquals(0.0, DiceSimulation.exactPercent(13), 0.0);
    }

    @Test
    void simulationCountsEveryRollAndIsReproducible() {
        Map<Integer, Long> simulated = DiceSimulation.simulatedCounts(5_000, 7);
        assertEquals(5_000L, simulated.values().stream().mapToLong(Long::longValue).sum());
        assertTrue(simulated.keySet().stream().allMatch(total -> total >= 2 && total <= 12));
        assertEquals(simulated, DiceSimulation.simulatedCounts(5_000, 7));
    }

    @Test
    void simulationConvergesOnTheExactAnswer() {
        // one standard deviation of a simulated percentage is about 0.37 points at 10,000 rolls and 0.037 at a million,
        // so these bounds are many deviations wide and hold for any seed
        for (long seed = 0; seed < 5; seed++) {
            assertTrue(DiceSimulation.largestError(10_000, seed) < 3.0, "10,000 rolls, seed " + seed);
        }
        assertTrue(DiceSimulation.largestError(1_000_000, 1) < 0.5, "1,000,000 rolls");
    }
}
