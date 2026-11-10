/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.lottery;

import io.github.deepeshpatel.jnumbertools.base.Calculator;
import io.github.deepeshpatel.jnumbertools.base.JNumberTools;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Odds and ticket numbering for a 6-from-49 lottery.
 * <p>
 * Real-life problem: how many tickets exist, how likely is each prize tier, and where does a
 * given ticket sit in the full ordered list of tickets? Every ticket has a unique rank
 * (its position in lexicographic order), and the rank can be turned back into the ticket.
 * That makes a ticket storable and printable as a single number.
 */
public final class LotteryOdds {

    static final int POOL = 49;
    static final int PICKS = 6;

    private LotteryOdds() {
    }

    /** @return number of distinct tickets, C(49, 6) */
    public static BigInteger ticketCount() {
        return JNumberTools.combinations().unique(POOL, PICKS).count();
    }

    /** @return probability of matching exactly {@code matches} of the 6 drawn numbers */
    public static double probabilityOfExactly(int matches) {
        Calculator c = new Calculator();
        BigInteger ways = c.nCr(PICKS, matches).multiply(c.nCr(POOL - PICKS, PICKS - matches));
        return ways.doubleValue() / ticketCount().doubleValue();
    }

    /** @param numbers six distinct numbers from 1 to 49 @return the ticket's 0-based rank */
    public static BigInteger rankOfTicket(int... numbers) {
        int[] sorted = numbers.clone();
        Arrays.sort(sorted);
        int[] indexes = Arrays.stream(sorted).map(n -> n - 1).toArray();
        return JNumberTools.rankOf().combination().unique(POOL, indexes);
    }

    /** @return the ticket (numbers 1 to 49, ascending) at the given 0-based rank */
    public static List<Integer> ticketAtRank(BigInteger rank) {
        return Arrays.stream(JNumberTools.unrankOf().combination().unique(rank, POOL, PICKS))
                .map(i -> i + 1)
                .boxed()
                .toList();
    }

    /** @return a reproducible random ticket ("quick pick") */
    public static List<Integer> quickPick(long seed) {
        List<Integer> pool = IntStream.rangeClosed(1, POOL).boxed().toList();
        return JNumberTools.combinations()
                .unique(PICKS, pool)
                .sample(1, new Random(seed))
                .iterator().next();
    }

    public static void run() {
        System.out.println("Tickets in a 6/49 lottery: " + ticketCount());
        System.out.println("Jackpot odds: 1 in " + ticketCount());
        System.out.println();
        for (int m = 6; m >= 3; m--) {
            System.out.printf("Exactly %d matches: %.6f%%%n", m, 100 * probabilityOfExactly(m));
        }

        System.out.println();
        int[] mine = {7, 14, 21, 28, 35, 42};
        BigInteger rank = rankOfTicket(mine);
        System.out.println("Ticket " + Arrays.toString(mine) + " has rank " + rank + " (counting from 0) among " + ticketCount() + " tickets");
        System.out.println("The ticket with rank 10000000 is " + ticketAtRank(BigInteger.valueOf(10_000_000)));
        System.out.println("Quick pick (seed 42): " + quickPick(42));
    }

    public static void main(String[] args) {
        run();
    }
}
