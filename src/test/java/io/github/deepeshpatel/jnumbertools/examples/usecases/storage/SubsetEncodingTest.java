/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.usecases.storage;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SubsetEncodingTest {

    @Test
    void smallestSubsetHasNumberZeroAndOrderOfIdsDoesNotMatter() {
        assertEquals(BigInteger.ZERO, SubsetEncoding.encode(0, 1, 2));
        assertEquals(BigInteger.valueOf(7), SubsetEncoding.encode(0, 3, 4));
        assertEquals(SubsetEncoding.encode(0, 3, 4), SubsetEncoding.encode(4, 0, 3));
    }

    @Test
    void numbersOfAllSubsetsAreExactlyTheFirstOnes() {
        assertTrue(SubsetEncoding.numbersAreExactlyTheFirstOnes(12, 5));
        assertTrue(SubsetEncoding.numbersAreExactlyTheFirstOnes(7, 1));
        assertTrue(SubsetEncoding.numbersAreExactlyTheFirstOnes(6, 6));
    }

    @Test
    void decodeUndoesEncode() {
        int[] permissions = {41, 2, 63, 11, 5, 50, 20, 33};
        BigInteger value = SubsetEncoding.encode(permissions);
        assertArrayEquals(new int[]{2, 5, 11, 20, 33, 41, 50, 63}, SubsetEncoding.decode(value, 8));
        assertTrue(value.compareTo(SubsetEncoding.subsetCount(64, 8)) < 0);
    }

    @Test
    void randomNumbersRoundTripThroughTheSubset() {
        Random random = new Random(1);
        BigInteger count = SubsetEncoding.subsetCount(100, 12);
        for (int i = 0; i < 200; i++) {
            BigInteger value = new BigInteger(count.bitLength(), random).mod(count);
            int[] subset = SubsetEncoding.decode(value, 12);
            assertEquals(12, subset.length);
            assertEquals(value, SubsetEncoding.encode(subset));
        }
    }

    @Test
    void bitsNeededAreFarBelowABitmask() {
        assertEquals(33, SubsetEncoding.bitsNeeded(64, 8));
        assertEquals(50, SubsetEncoding.bitsNeeded(100, 12));
        assertEquals(78, SubsetEncoding.bitsNeeded(1000, 10));
    }

    @Test
    void duplicateOrNegativeIdsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> SubsetEncoding.encode(3, 3, 5));
        assertThrows(IllegalArgumentException.class, () -> SubsetEncoding.encode(-1, 2));
    }
}
