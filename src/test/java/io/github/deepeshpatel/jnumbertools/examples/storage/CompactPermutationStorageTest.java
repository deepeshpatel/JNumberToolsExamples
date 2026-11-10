/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.storage;

import io.github.deepeshpatel.jnumbertools.base.Calculator;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CompactPermutationStorageTest {

    private static final BigInteger DECK_ORDERINGS = new Calculator().factorial(52);

    @Test
    void identityHasRankZeroAndReverseHasTheLastRank() {
        int[] identity = IntStream.range(0, 52).toArray();
        int[] reverse = IntStream.range(0, 52).map(i -> 51 - i).toArray();
        assertEquals(BigInteger.ZERO, CompactPermutationStorage.encode(identity));
        assertEquals(DECK_ORDERINGS.subtract(BigInteger.ONE), CompactPermutationStorage.encode(reverse));
    }

    @Test
    void shuffledDecksRoundTripExactly() {
        for (long seed = 0; seed < 25; seed++) {
            int[] order = CompactPermutationStorage.shuffledDeck(seed);
            BigInteger rank = CompactPermutationStorage.encode(order);
            assertTrue(rank.signum() >= 0 && rank.compareTo(DECK_ORDERINGS) < 0);
            assertArrayEquals(order, CompactPermutationStorage.decode(rank, 52));
        }
    }

    @Test
    void rankFitsInFarFewerBitsThanOneByteForEachCard() {
        BigInteger rank = CompactPermutationStorage.encode(CompactPermutationStorage.shuffledDeck(42));
        assertEquals(224, rank.bitLength());
        assertTrue(rank.bitLength() <= DECK_ORDERINGS.bitLength());
        assertTrue(DECK_ORDERINGS.bitLength() < 52 * 8);
    }

    @Test
    void deckHasFiftyTwoDistinctCards() {
        var cards = CompactPermutationStorage.deck();
        assertEquals(52, new HashSet<>(cards).size());
        assertEquals("AS", cards.getFirst());
        assertEquals("KC", cards.getLast());
        int[] order = CompactPermutationStorage.shuffledDeck(7);
        assertEquals(52, Arrays.stream(order).distinct().count());
    }
}
