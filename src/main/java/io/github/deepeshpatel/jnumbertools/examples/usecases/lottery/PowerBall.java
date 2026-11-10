/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.usecases.lottery;

import io.github.deepeshpatel.jnumbertools.base.Calculator;
import io.github.deepeshpatel.jnumbertools.base.JNumberTools;
import io.github.deepeshpatel.jnumbertools.generator.product.constrained.ConstrainedProductBuilder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * A two-part lottery ticket: five white balls from 69 plus one red ball from 26.
 * <p>
 * Real-life problem: the ticket is a combination (the white balls, order irrelevant) joined with a
 * single pick (the red ball). That is a Cartesian product of two selections, which the constrained
 * product builder models directly: "choose 5 distinct of 69" and then "choose 1 of 26". The library
 * counts the tickets, draws random ones, and jumps to any ticket by its position without listing the
 * ones before it. The prize-tier odds are computed from binomials and must add up to the total.
 * <p>
 * A ticket is a flat list: the five white balls in ascending order, then the red ball.
 */
public final class PowerBall {

    static final int WHITE_PICKS = 5;
    static final List<Integer> WHITE = IntStream.rangeClosed(1, 69).boxed().toList();
    static final List<Integer> RED = IntStream.rangeClosed(1, 26).boxed().toList();

    /** A prize tier: how many white balls match, whether the red ball matches, and how many tickets fall in it. */
    public record Tier(int whiteMatches, boolean redMatches, BigInteger tickets) {
        public String label() {
            return whiteMatches + (redMatches ? " + red" : "");
        }
    }

    private PowerBall() {
    }

    private static ConstrainedProductBuilder builder() {
        return JNumberTools.cartesianProduct()
                .constrainedProductOfDistinct(WHITE_PICKS, WHITE)
                .andDistinct(1, RED);
    }

    /** @return number of distinct tickets, C(69, 5) x 26 */
    public static BigInteger ticketCount() {
        return builder().count();
    }

    /**
     * @return every prize tier, best first, with the number of tickets that fall in it for one fixed
     * draw: choose the matching whites from the 5 drawn and the rest from the 64 not drawn, and
     * either the one matching red or one of the 25 other reds
     */
    public static List<Tier> tiers() {
        Calculator c = new Calculator();
        List<Tier> tiers = new ArrayList<>();
        for (int white = WHITE_PICKS; white >= 0; white--) {
            BigInteger whiteWays = c.nCr(WHITE_PICKS, white).multiply(c.nCr(WHITE.size() - WHITE_PICKS, WHITE_PICKS - white));
            tiers.add(new Tier(white, true, whiteWays));
            tiers.add(new Tier(white, false, whiteWays.multiply(BigInteger.valueOf(RED.size() - 1))));
        }
        return tiers;
    }

    /** @return the ticket at a 0-based position in the ordered list of all tickets, reached directly */
    public static List<Object> ticketAtRank(BigInteger rank) {
        return builder().byRanks(List.of(rank)).iterator().next();
    }

    /** @return a reproducible random ticket ("quick pick") */
    public static List<Object> quickPick(long seed) {
        return builder().sample(1, new Random(seed)).iterator().next();
    }

    public static void run() {
        BigInteger total = ticketCount();
        System.out.println("Tickets: " + total + " = C(69,5) x 26");
        System.out.println();
        System.out.println("Prize tier   tickets for one draw   probability   1 in");
        for (Tier tier : tiers()) {
            System.out.printf("  %-9s %12d       %.8f%%   %,.2f%n", tier.label(), tier.tickets(),
                    100 * tier.tickets().doubleValue() / total.doubleValue(),
                    total.doubleValue() / tier.tickets().doubleValue());
        }
        BigInteger sum = tiers().stream().map(Tier::tickets).reduce(BigInteger.ZERO, BigInteger::add);
        System.out.println("Tiers add up to all tickets: " + sum.equals(total));
        System.out.println();
        System.out.println("First ticket        : " + ticketAtRank(BigInteger.ZERO));
        System.out.println("Last ticket         : " + ticketAtRank(total.subtract(BigInteger.ONE)));
        System.out.println("Ticket 100,000,000  : " + ticketAtRank(BigInteger.valueOf(100_000_000)));
        System.out.println("Quick pick (seed 42): " + quickPick(42));
    }

    public static void main(String[] args) {
        run();
    }
}
