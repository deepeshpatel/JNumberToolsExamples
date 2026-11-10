/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.games;

import io.github.deepeshpatel.jnumbertools.examples.games.PokerHands.Category;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PokerHandsTest {

    @Test
    void enumerationReproducesTheTextbookTable() {
        assertEquals(BigInteger.valueOf(2_598_960), PokerHands.handCount());

        Map<Category, Long> counts = PokerHands.categoryCounts();
        assertEquals(40L, counts.get(Category.STRAIGHT_FLUSH).longValue());
        assertEquals(624L, counts.get(Category.FOUR_OF_A_KIND).longValue());
        assertEquals(3_744L, counts.get(Category.FULL_HOUSE).longValue());
        assertEquals(5_108L, counts.get(Category.FLUSH).longValue());
        assertEquals(10_200L, counts.get(Category.STRAIGHT).longValue());
        assertEquals(54_912L, counts.get(Category.THREE_OF_A_KIND).longValue());
        assertEquals(123_552L, counts.get(Category.TWO_PAIR).longValue());
        assertEquals(1_098_240L, counts.get(Category.ONE_PAIR).longValue());
        assertEquals(1_302_540L, counts.get(Category.HIGH_CARD).longValue());
        assertEquals(2_598_960L, counts.values().stream().mapToLong(Long::longValue).sum());
    }

    @Test
    void classifiesKnownHands() {
        assertEquals(Category.STRAIGHT_FLUSH, PokerHands.classify(List.of(8, 9, 10, 11, 12)));    // royal flush in spades
        assertEquals(Category.STRAIGHT, PokerHands.classify(List.of(12, 13, 27, 41, 3)));         // ace-low straight, mixed suits
        assertEquals(Category.STRAIGHT_FLUSH, PokerHands.classify(List.of(0, 1, 2, 3, 12)));      // 2-3-4-5-A of spades: ace-low straight flush
        assertEquals(Category.FLUSH, PokerHands.classify(List.of(0, 1, 2, 3, 5)));                // 2-3-4-5-7 of spades
        assertEquals(Category.FLUSH, PokerHands.classify(List.of(11, 12, 0, 1, 2)));              // K-A-2-3-4 does not wrap
        assertEquals(Category.FOUR_OF_A_KIND, PokerHands.classify(List.of(0, 13, 26, 39, 1)));
        assertEquals(Category.FULL_HOUSE, PokerHands.classify(List.of(0, 13, 26, 1, 14)));
        assertEquals(Category.THREE_OF_A_KIND, PokerHands.classify(List.of(0, 13, 26, 1, 15)));
        assertEquals(Category.TWO_PAIR, PokerHands.classify(List.of(0, 13, 1, 14, 5)));
        assertEquals(Category.ONE_PAIR, PokerHands.classify(List.of(0, 13, 1, 15, 6)));
        assertEquals(Category.HIGH_CARD, PokerHands.classify(List.of(0, 14, 2, 16, 8)));
    }

    @Test
    void cardNamesUseRankThenSuit() {
        assertEquals(List.of("AS", "2H", "3D", "4C", "5S"), PokerHands.names(List.of(12, 13, 27, 41, 3)));
    }

    @Test
    void dealIsReproducible() {
        List<Integer> hand = PokerHands.deal(42);
        assertEquals(hand, PokerHands.deal(42));
        assertEquals(List.of("3S", "JH", "2C", "3C", "JC"), PokerHands.names(hand));
        assertEquals(Category.TWO_PAIR, PokerHands.classify(hand));
    }
}
