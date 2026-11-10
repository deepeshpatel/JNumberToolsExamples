/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.usecases.lottery;

import io.github.deepeshpatel.jnumbertools.base.Calculator;
import io.github.deepeshpatel.jnumbertools.examples.usecases.lottery.PowerBall.Tier;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PowerBallTest {

    @Test
    void ticketCountIsWhiteCombinationsTimesReds() {
        BigInteger expected = new Calculator().nCr(69, 5).multiply(BigInteger.valueOf(26));
        assertEquals(expected, PowerBall.ticketCount());
        assertEquals(BigInteger.valueOf(292_201_338), PowerBall.ticketCount());
    }

    @Test
    void tiersCoverEveryTicketExactlyOnce() {
        List<Tier> tiers = PowerBall.tiers();
        assertEquals(12, tiers.size());
        assertEquals(PowerBall.ticketCount(), tiers.stream().map(Tier::tickets).reduce(BigInteger.ZERO, BigInteger::add));
        assertEquals(BigInteger.ONE, tiers.getFirst().tickets());
        assertEquals("5 + red", tiers.getFirst().label());
    }

    @Test
    void knownTierSizes() {
        assertEquals(BigInteger.valueOf(25), tier(5, false).tickets());
        assertEquals(BigInteger.valueOf(320), tier(4, true).tickets());
        assertEquals(BigInteger.valueOf(20_160), tier(3, true).tickets());
        assertEquals(BigInteger.valueOf(416_640), tier(2, true).tickets());
    }

    @Test
    void firstAndLastTicketsAreTheExtremes() {
        assertEquals(List.of(1, 2, 3, 4, 5, 1), PowerBall.ticketAtRank(BigInteger.ZERO));
        assertEquals(List.of(65, 66, 67, 68, 69, 26),
                PowerBall.ticketAtRank(PowerBall.ticketCount().subtract(BigInteger.ONE)));
    }

    @Test
    void everyTicketHasFiveDistinctAscendingWhitesAndOneValidRed() {
        for (long seed = 0; seed < 50; seed++) {
            List<Object> ticket = PowerBall.quickPick(seed);
            assertEquals(6, ticket.size());
            for (int i = 0; i < 5; i++) {
                int white = (Integer) ticket.get(i);
                assertTrue(white >= 1 && white <= 69, "white " + white);
                if (i > 0) {
                    assertTrue(white > (Integer) ticket.get(i - 1), "whites must ascend: " + ticket);
                }
            }
            int red = (Integer) ticket.get(5);
            assertTrue(red >= 1 && red <= 26, "red " + red);
        }
        assertEquals(PowerBall.quickPick(7), PowerBall.quickPick(7));
    }

    private static Tier tier(int whites, boolean red) {
        return PowerBall.tiers().stream()
                .filter(t -> t.whiteMatches() == whites && t.redMatches() == red)
                .findFirst().orElseThrow();
    }
}
