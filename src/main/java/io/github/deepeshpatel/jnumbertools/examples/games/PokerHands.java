/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.games;

import io.github.deepeshpatel.jnumbertools.base.JNumberTools;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Poker hand odds by exhaustive enumeration.
 * <p>
 * Real-life problem: how likely is each five-card poker hand? Instead of trusting a table,
 * enumerate all C(52, 5) = 2 598 960 hands, classify each one and count. The result matches the
 * published table exactly, and it takes well under a second. Card ids are 0 to 51: id % 13 is
 * the rank (0 = deuce ... 12 = ace) and id / 13 is the suit.
 */
public final class PokerHands {

    /** Hand categories from weakest to strongest. A royal flush counts as a straight flush. */
    public enum Category {
        HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_OF_A_KIND, STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH
    }

    private static final String RANKS = "23456789TJQKA";
    private static final String SUITS = "SHDC";

    private PokerHands() {
    }

    /** @return number of distinct five-card hands */
    public static BigInteger handCount() {
        return JNumberTools.combinations().unique(52, 5).count();
    }

    /** @return the category of a five-card hand given as card ids 0..51 */
    public static Category classify(List<Integer> hand) {
        int[] rankCounts = new int[13];
        boolean flush = true;
        int suit = hand.getFirst() / 13;
        for (int card : hand) {
            rankCounts[card % 13]++;
            flush &= card / 13 == suit;
        }

        int pairs = 0;
        int threes = 0;
        int fours = 0;
        int distinct = 0;
        for (int count : rankCounts) {
            if (count > 0) {
                distinct++;
            }
            switch (count) {
                case 2 -> pairs++;
                case 3 -> threes++;
                case 4 -> fours++;
                default -> { }
            }
        }

        boolean straight = distinct == 5 && (isRun(rankCounts) || isWheel(rankCounts));
        if (straight && flush) return Category.STRAIGHT_FLUSH;
        if (fours == 1) return Category.FOUR_OF_A_KIND;
        if (threes == 1 && pairs == 1) return Category.FULL_HOUSE;
        if (flush) return Category.FLUSH;
        if (straight) return Category.STRAIGHT;
        if (threes == 1) return Category.THREE_OF_A_KIND;
        if (pairs == 2) return Category.TWO_PAIR;
        if (pairs == 1) return Category.ONE_PAIR;
        return Category.HIGH_CARD;
    }

    private static boolean isRun(int[] rankCounts) {
        int low = 0;
        while (rankCounts[low] == 0) {
            low++;
        }
        return low <= 8 && rankCounts[low + 1] > 0 && rankCounts[low + 2] > 0
                && rankCounts[low + 3] > 0 && rankCounts[low + 4] > 0;
    }

    /** The ace-low straight A-2-3-4-5. */
    private static boolean isWheel(int[] rankCounts) {
        return rankCounts[12] > 0 && rankCounts[0] > 0 && rankCounts[1] > 0 && rankCounts[2] > 0 && rankCounts[3] > 0;
    }

    /** Enumerates every hand once and counts the hands in each category. */
    public static Map<Category, Long> categoryCounts() {
        Map<Category, Long> counts = new EnumMap<>(Category.class);
        for (List<Integer> hand : JNumberTools.combinations().unique(52, 5).lexOrder()) {
            counts.merge(classify(hand), 1L, Long::sum);
        }
        return counts;
    }

    /** @return a reproducible random hand ("deal") */
    public static List<Integer> deal(long seed) {
        return JNumberTools.combinations().unique(52, 5).sample(1, new Random(seed)).iterator().next();
    }

    /** @return card names such as AS or 7D for card ids */
    public static List<String> names(List<Integer> hand) {
        return hand.stream().map(c -> "" + RANKS.charAt(c % 13) + SUITS.charAt(c / 13)).toList();
    }

    public static void run() {
        BigInteger total = handCount();
        System.out.println("Five-card hands: " + total);
        System.out.println();
        Map<Category, Long> counts = categoryCounts();
        Category[] strongestFirst = Category.values();
        Arrays.sort(strongestFirst, (a, b) -> b.compareTo(a));
        for (Category category : strongestFirst) {
            long n = counts.getOrDefault(category, 0L);
            System.out.printf("  %-16s %9d  %8.4f%%  1 in %.1f%n", category, n,
                    100.0 * n / total.doubleValue(), total.doubleValue() / n);
        }

        List<Integer> hand = deal(42);
        System.out.println();
        System.out.println("Deal (seed 42): " + names(hand) + " -> " + classify(hand));
    }

    public static void main(String[] args) {
        run();
    }
}
