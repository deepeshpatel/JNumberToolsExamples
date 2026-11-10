/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.tour;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NumberSystemsTest {

    @Test
    void factoradicDigitsAreMostSignificantFirstAndRebuildTheValue() {
        assertEquals(List.of(1, 2, 0, 0), NumberSystems.factoradicDigits(10));
        assertEquals("1*3! + 2*2! + 0*1! + 0*0!", NumberSystems.factoradicExpression(List.of(1, 2, 0, 0)));
        for (long value = 0; value <= 500; value++) {
            assertEquals(BigInteger.valueOf(value), NumberSystems.factoradicValue(NumberSystems.factoradicDigits(value)), "value " + value);
        }
    }

    @Test
    void factoradicDigitsAreTheLehmerCodeOfTheLibrarysPermutation() {
        for (int rank = 0; rank < 24; rank++) {
            var digits = NumberSystems.factoradicDigits(rank);
            assertArrayEquals(NumberSystems.permutationAtRank(rank, 4), NumberSystems.permutationFromFactoradic(digits, 4), "rank " + rank);
        }
    }

    @Test
    void combinadicDigitsDescendAndRebuildTheValue() {
        assertEquals(List.of(4, 3, 0), NumberSystems.combinadicDigits(7, 3));
        assertEquals("C(4,3) + C(3,2) + C(0,1)", NumberSystems.combinadicExpression(List.of(4, 3, 0)));
        for (long value = 0; value < 120; value++) { // C(10, 3) = 120 combinations
            var digits = NumberSystems.combinadicDigits(value, 3);
            assertEquals(BigInteger.valueOf(value), NumberSystems.combinadicValue(digits), "value " + value);
            for (int i = 0; i + 1 < digits.size(); i++) {
                assertTrue(digits.get(i) > digits.get(i + 1), "digits must descend for value " + value + ": " + digits);
            }
        }
    }

    @Test
    void derangadicAgreesWithTheBuilderForEveryDerangementOfFiveItems() {
        for (long rank = 0; rank < 44; rank++) { // !5 = 44
            assertEquals(NumberSystems.derangementViaBuilder(rank, 5), NumberSystems.derangementViaNumberSystem(rank, 5), "rank " + rank);
        }
    }
}
