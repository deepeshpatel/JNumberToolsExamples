/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.tour;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SamplingTest {

    @Test
    void twoDiceHaveThirtySixRolls() {
        assertEquals(36, Sampling.outcomes());
    }

    @Test
    void sampleDrawsEveryRollExactlyOnceWhenAskedForTheWholeSpace() {
        for (long seed = 0; seed < 20; seed++) {
            List<List<Integer>> draw = Sampling.withoutReplacement(36, seed);
            assertEquals(36, draw.size());
            assertEquals(36, Sampling.distinct(draw), "seed " + seed);
        }
    }

    @Test
    void sampleOfFewerRollsAreDistinctToo() {
        List<List<Integer>> draw = Sampling.withoutReplacement(10, 7);
        assertEquals(10, Sampling.distinct(draw));
        assertTrue(draw.stream().allMatch(roll -> roll.size() == 2 && Sampling.DIE.containsAll(roll)));
    }

    @Test
    void choiceRepeatsRollsBecauseDrawsAreIndependent() {
        // 36 independent draws from 36 outcomes are all different with probability about 1e-15
        for (long seed = 0; seed < 20; seed++) {
            List<List<Integer>> draw = Sampling.withReplacement(36, seed);
            assertEquals(36, draw.size());
            assertTrue(Sampling.distinct(draw) < 36, "seed " + seed);
        }
    }

    @Test
    void askingForMoreDistinctRollsThanExistIsRefused() {
        assertThrows(IllegalArgumentException.class, () -> Sampling.withoutReplacement(37, 1));
        assertFalse(Sampling.refusalFor(37).equals("accepted"));
        assertEquals("accepted", Sampling.refusalFor(36));
    }

    @Test
    void aSeedMakesBothDrawsReproducible() {
        assertEquals(Sampling.withReplacement(20, 5), Sampling.withReplacement(20, 5));
        assertEquals(Sampling.withoutReplacement(20, 5), Sampling.withoutReplacement(20, 5));
        var different = new HashSet<List<List<Integer>>>();
        for (long seed = 0; seed < 10; seed++) {
            different.add(Sampling.withReplacement(20, seed));
        }
        assertTrue(different.size() > 1);
    }
}
