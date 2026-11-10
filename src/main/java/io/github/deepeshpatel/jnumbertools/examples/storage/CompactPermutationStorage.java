/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.storage;

import io.github.deepeshpatel.jnumbertools.base.JNumberTools;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Storing an ordering as one integer.
 * <p>
 * Real-life problem: a card game server, a playlist service or a seating planner must save
 * the exact order of N items. N bytes work, but an ordering of N items is one of N! and
 * therefore fits in about log2(N!) bits. {@code rankOf} turns an ordering into its position
 * in the list of all orderings, and {@code unrankOf} restores it. The rank is also a short,
 * comparable, indexable database value.
 */
public final class CompactPermutationStorage {

    private static final String CARD_RANKS = "A23456789TJQK";
    private static final String CARD_SUITS = "SHDC";

    private CompactPermutationStorage() {
    }

    /** @return card names for card ids 0..51, such as AS, 2S, ..., KC */
    static List<String> deck() {
        List<String> cards = new ArrayList<>();
        for (char suit : CARD_SUITS.toCharArray()) {
            for (char rank : CARD_RANKS.toCharArray()) {
                cards.add("" + rank + suit);
            }
        }
        return cards;
    }

    /** @return a reproducible shuffled order of card ids 0..51 */
    static int[] shuffledDeck(long seed) {
        List<Integer> ids = new ArrayList<>();
        for (int i = 0; i < 52; i++) {
            ids.add(i);
        }
        Collections.shuffle(ids, new Random(seed));
        return ids.stream().mapToInt(Integer::intValue).toArray();
    }

    /** @param order a permutation of 0..n-1 @return its rank among all n! permutations */
    public static BigInteger encode(int[] order) {
        return JNumberTools.rankOf().permutation().unique(order);
    }

    /** @return the permutation of 0..size-1 with the given rank */
    public static int[] decode(BigInteger rank, int size) {
        return JNumberTools.unrankOf().permutation().unique(rank, size);
    }

    public static void run() {
        int[] order = shuffledDeck(42);
        BigInteger rank = encode(order);
        int[] restored = decode(rank, 52);

        List<String> names = deck();
        System.out.print("Shuffled deck (seed 42), first 8 cards:");
        for (int i = 0; i < 8; i++) {
            System.out.print(" " + names.get(order[i]));
        }
        System.out.println();
        System.out.println();
        System.out.println("Orderings of 52 cards     : " + JNumberTools.permutations().unique(52).count());
        System.out.println("Rank of this deck         : " + rank);
        System.out.println("Rank in base 36           : " + rank.toString(36));
        System.out.println("Bits needed               : " + rank.bitLength() + " (one byte per card uses 416, six bits per card 312)");
        System.out.println("Restored deck is identical: " + Arrays.equals(order, restored));
    }

    public static void main(String[] args) {
        run();
    }
}
