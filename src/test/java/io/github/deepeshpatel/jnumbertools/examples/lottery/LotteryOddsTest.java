/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.lottery;

import io.github.deepeshpatel.jnumbertools.base.JNumberTools;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LotteryOddsTest {

    private static final BigInteger TOTAL = BigInteger.valueOf(13_983_816);

    @Test
    void ticketCountIsFortyNineChooseSix() {
        assertEquals(TOTAL, LotteryOdds.ticketCount());
    }

    @Test
    void prizeTierProbabilitiesMatchTheKnownOdds() {
        assertEquals(1.0 / 13_983_816, LotteryOdds.probabilityOfExactly(6), 1e-15);
        assertEquals(258.0 / 13_983_816, LotteryOdds.probabilityOfExactly(5), 1e-12);
        assertEquals(0.017650, LotteryOdds.probabilityOfExactly(3), 1e-6);
        double sum = IntStream.rangeClosed(0, 6).mapToDouble(LotteryOdds::probabilityOfExactly).sum();
        assertEquals(1.0, sum, 1e-12);
    }

    @Test
    void firstAndLastTicketsHaveTheFirstAndLastRank() {
        assertEquals(BigInteger.ZERO, LotteryOdds.rankOfTicket(1, 2, 3, 4, 5, 6));
        assertEquals(TOTAL.subtract(BigInteger.ONE), LotteryOdds.rankOfTicket(44, 45, 46, 47, 48, 49));
        assertEquals(List.of(1, 2, 3, 4, 5, 6), LotteryOdds.ticketAtRank(BigInteger.ZERO));
    }

    @Test
    void rankAndUnrankRoundTripAndAgreeWithTheBuilder() {
        assertEquals(BigInteger.valueOf(8_391_494), LotteryOdds.rankOfTicket(42, 7, 21, 14, 35, 28));
        assertEquals(List.of(9, 19, 32, 34, 35, 46), LotteryOdds.ticketAtRank(BigInteger.valueOf(10_000_000)));

        var numbers = IntStream.rangeClosed(1, 49).boxed().toList();
        BigInteger rank = BigInteger.valueOf(10_000_000);
        var fromBuilder = JNumberTools.combinations().unique(6, numbers).byRanks(List.of(rank)).iterator().next();
        assertEquals(fromBuilder, LotteryOdds.ticketAtRank(rank));
    }

    @Test
    void quickPickIsReproducibleAndValid() {
        List<Integer> pick = LotteryOdds.quickPick(42);
        assertEquals(pick, LotteryOdds.quickPick(42));
        assertEquals(6, pick.size());
        assertEquals(6, pick.stream().distinct().count());
        assertTrue(pick.stream().allMatch(n -> n >= 1 && n <= 49));
    }
}
