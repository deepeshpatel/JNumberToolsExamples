/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.groups;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SecretSantaTest {

    private static final List<String> PEOPLE = List.of("Aarav", "Bella", "Chen", "Dmitri", "Elena", "Farid");

    @Test
    void countsMatchTheSubfactorial() {
        assertEquals(BigInteger.valueOf(9), SecretSanta.validDraws(4));
        assertEquals(BigInteger.valueOf(265), SecretSanta.validDraws(6));
        assertEquals(0.368, SecretSanta.naiveShuffleSuccessRate(6), 0.001);
    }

    @Test
    void nobodyDrawsThemselvesAndEveryoneReceivesExactlyOneGift() {
        for (long seed = 0; seed < 50; seed++) {
            Map<String, String> draw = SecretSanta.assign(PEOPLE, seed);
            assertEquals(PEOPLE, List.copyOf(draw.keySet()));
            assertEquals(new HashSet<>(PEOPLE), new HashSet<>(draw.values()));
            draw.forEach((giver, receiver) -> assertNotEquals(giver, receiver));
        }
    }

    @Test
    void sameSeedGivesSameDrawAndSomeSeedsDiffer() {
        assertEquals(SecretSanta.assign(PEOPLE, 42), SecretSanta.assign(PEOPLE, 42));
        var draws = new HashSet<Map<String, String>>();
        for (long seed = 0; seed < 20; seed++) {
            draws.add(SecretSanta.assign(PEOPLE, seed));
        }
        assertTrue(draws.size() > 1);
    }

    @Test
    void twoPeopleSwap() {
        assertEquals(Map.of("A", "B", "B", "A"), SecretSanta.assign(List.of("A", "B"), 1));
    }
}
