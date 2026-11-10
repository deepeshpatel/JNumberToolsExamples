/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.usecases.statistics;

import io.github.deepeshpatel.jnumbertools.base.Calculator;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StratifiedSurveyTest {

    @Test
    void sampleSpacesMatchTheBinomialFormulas() {
        Calculator c = new Calculator();
        assertEquals(c.nCr(40, 8).multiply(c.nCr(25, 5)).multiply(c.nCr(15, 3)), StratifiedSurvey.stratifiedSampleSpace());
        assertEquals(c.nCr(80, 16), StratifiedSurvey.simpleSampleSpace());
        assertTrue(StratifiedSurvey.stratifiedSampleSpace().compareTo(StratifiedSurvey.simpleSampleSpace()) < 0);
    }

    @Test
    void everyStratifiedSampleHasExactlyTheQuotaFromEachStratum() {
        List<String> everyone = StratifiedSurvey.everyone();
        for (long seed = 0; seed < 30; seed++) {
            List<Object> sample = StratifiedSurvey.stratifiedSample(seed);
            assertEquals(16, sample.size());
            assertEquals(16, new HashSet<>(sample).size(), "no one is surveyed twice");
            assertTrue(everyone.containsAll(sample.stream().map(Object::toString).toList()));
            assertArrayEquals(new int[]{8, 5, 3}, StratifiedSurvey.stratumCounts(sample));
        }
        assertEquals(StratifiedSurvey.stratifiedSample(5), StratifiedSurvey.stratifiedSample(5));
    }

    @Test
    void simpleRandomSamplesDriftFromTheFairShare() {
        int[] spread = StratifiedSurvey.engineerSpreadInSimpleSamples(1000, 42);
        assertTrue(spread[0] < 8, "some simple samples have too few engineers");
        assertTrue(spread[1] > 8, "some simple samples have too many engineers");
    }

    @Test
    void peopleAreNumberedPerStratum() {
        assertEquals("ENG-01", StratifiedSurvey.members(0).getFirst());
        assertEquals("SALES-25", StratifiedSurvey.members(1).getLast());
        assertEquals(80, StratifiedSurvey.everyone().size());
    }
}
