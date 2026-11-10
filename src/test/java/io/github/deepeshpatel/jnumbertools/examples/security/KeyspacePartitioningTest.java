/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.security;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;

import static io.github.deepeshpatel.jnumbertools.examples.security.KeyspacePartitioning.ALPHANUMERIC;
import static io.github.deepeshpatel.jnumbertools.examples.security.KeyspacePartitioning.DIGITS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class KeyspacePartitioningTest {

    @Test
    void interleavedWorkersCoverThePinSpaceExactlyOnce() {
        var all = new HashSet<String>();
        int total = 0;
        for (int w = 0; w < 8; w++) {
            List<String> mine = KeyspacePartitioning.interleaved(4, DIGITS, 8, w);
            assertEquals(1250, mine.size());
            total += mine.size();
            all.addAll(mine);
        }
        assertEquals(10_000, total);
        assertEquals(10_000, all.size());
        assertEquals(List.of("0003", "0011", "0019"), KeyspacePartitioning.interleaved(4, DIGITS, 8, 3).subList(0, 3));
    }

    @Test
    void contiguousSlicesAreGaplessAndCoverTheWholeSpace() {
        var slices = KeyspacePartitioning.contiguous(8, ALPHANUMERIC, 4);
        BigInteger total = BigInteger.valueOf(62).pow(8);

        assertEquals(4, slices.size());
        assertEquals(BigInteger.ZERO, slices.getFirst().fromRank());
        assertEquals(total, slices.getLast().toRankExclusive());
        for (int i = 0; i + 1 < slices.size(); i++) {
            assertEquals(slices.get(i).toRankExclusive(), slices.get(i + 1).fromRank());
        }
        assertEquals(total, slices.stream().map(KeyspacePartitioning.Slice::size).reduce(BigInteger.ZERO, BigInteger::add));
        assertEquals("aaaaaaaa", slices.getFirst().first());
        assertEquals("99999999", slices.getLast().last());
        assertEquals("pFaaaaaa", slices.get(1).first());
    }

    @Test
    void lastSliceIsSmallerWhenWorkersDoNotDivideTheSpace() {
        var slices = KeyspacePartitioning.contiguous(4, DIGITS, 3);
        assertEquals(List.of(3334, 3334, 3332), slices.stream().map(s -> s.size().intValueExact()).toList());
        assertTrue(slices.getLast().last().equals("9999"));
    }
}
