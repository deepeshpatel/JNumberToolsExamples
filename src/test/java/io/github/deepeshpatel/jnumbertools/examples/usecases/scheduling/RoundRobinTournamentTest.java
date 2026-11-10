/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.usecases.scheduling;

import io.github.deepeshpatel.jnumbertools.examples.usecases.scheduling.RoundRobinTournament.Match;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoundRobinTournamentTest {

    private static List<String> teams(int n) {
        return IntStream.rangeClosed(1, n).mapToObj(i -> "T" + i).toList();
    }

    @Test
    void matchCountIsNChooseTwo() {
        assertEquals(BigInteger.valueOf(15), RoundRobinTournament.matchCount(6));
        assertEquals(BigInteger.valueOf(190), RoundRobinTournament.matchCount(20));
    }

    @Test
    void scheduleHasNMinusOneRoundsOfHalfTheTeams() {
        var rounds = RoundRobinTournament.schedule(RoundRobinTournament.TEAMS);
        assertEquals(5, rounds.size());
        assertTrue(rounds.stream().allMatch(round -> round.size() == 3));
    }

    @Test
    void everyPairPlaysExactlyOnceAndEveryTeamPlaysOncePerRoundForAllEvenSizes() {
        for (int n = 2; n <= 24; n += 2) {
            var teams = teams(n);
            var rounds = RoundRobinTournament.schedule(teams);
            assertEquals(n - 1, rounds.size(), "n=" + n);
            assertTrue(RoundRobinTournament.everyPairingPlayedExactlyOnce(teams, rounds), "pairings, n=" + n);
            assertTrue(RoundRobinTournament.everyTeamPlaysOncePerRound(teams, rounds), "rounds, n=" + n);
        }
    }

    @Test
    void theCheckersRejectABrokenSchedule() {
        var teams = List.of("A", "B", "C", "D");
        var good = RoundRobinTournament.schedule(teams);

        // the list of pairings in lexicographic order, cut into rounds of two: A plays twice in round 1
        var pairingsInOrder = List.of(
                List.of(new Match("A", "B"), new Match("A", "C")),
                List.of(new Match("A", "D"), new Match("B", "C")),
                List.of(new Match("B", "D"), new Match("C", "D")));
        assertTrue(RoundRobinTournament.everyPairingPlayedExactlyOnce(teams, pairingsInOrder));
        assertFalse(RoundRobinTournament.everyTeamPlaysOncePerRound(teams, pairingsInOrder));

        // drop one match: a pairing is missing
        var missing = List.of(good.get(0), good.get(1), List.of(good.get(2).getFirst()));
        assertFalse(RoundRobinTournament.everyPairingPlayedExactlyOnce(teams, missing));
    }

    @Test
    void anOddOrTooSmallLeagueIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> RoundRobinTournament.schedule(teams(5)));
        assertThrows(IllegalArgumentException.class, () -> RoundRobinTournament.schedule(teams(1)));
    }
}
