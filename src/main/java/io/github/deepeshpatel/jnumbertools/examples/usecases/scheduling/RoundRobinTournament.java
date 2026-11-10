/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.usecases.scheduling;

import io.github.deepeshpatel.jnumbertools.base.JNumberTools;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A round-robin tournament: every team plays every other team once, and nobody plays twice in a round.
 * <p>
 * Real-life problem: a league of 6 teams needs a fixture list. Listing the pairings is not enough,
 * because a list in lexicographic order has Lions playing three matches in the first "round". A
 * schedule must also put every team into exactly one match per round.
 * <p>
 * What the library does: it counts and lists the pairings (combinations of 2 teams), which is the
 * reference for checking a schedule. What it does not do: schedule rounds. That is the classic
 * circle method below, a few lines of plain Java: keep the first team fixed, pair team i with team
 * n-1-i, then rotate the other teams by one place for the next round.
 */
public final class RoundRobinTournament {

    static final List<String> TEAMS = List.of("Lions", "Tigers", "Bears", "Wolves", "Eagles", "Hawks");

    /** One match between two teams. */
    public record Match(String first, String second) {
        @Override
        public String toString() {
            return first + " v " + second;
        }
    }

    private RoundRobinTournament() {
    }

    /** @return number of different matches between {@code teams} teams, C(teams, 2), from the library */
    public static BigInteger matchCount(int teams) {
        return JNumberTools.combinations().unique(teams, 2).count();
    }

    /**
     * Circle method.
     *
     * @param teams an even number of teams, at least 2
     * @return {@code teams.size() - 1} rounds of {@code teams.size() / 2} matches each
     */
    public static List<List<Match>> schedule(List<String> teams) {
        int n = teams.size();
        if (n < 2 || n % 2 != 0) {
            throw new IllegalArgumentException("an even number of at least 2 teams is required, got " + n);
        }
        List<String> ring = new ArrayList<>(teams);
        List<List<Match>> rounds = new ArrayList<>();
        for (int round = 0; round < n - 1; round++) {
            List<Match> matches = new ArrayList<>();
            for (int i = 0; i < n / 2; i++) {
                matches.add(new Match(ring.get(i), ring.get(n - 1 - i)));
            }
            rounds.add(matches);
            ring.add(1, ring.removeLast()); // the first team stays, the others move one place
        }
        return rounds;
    }

    /** @return true if every pairing the library lists is played exactly once, and nothing else is played */
    public static boolean everyPairingPlayedExactlyOnce(List<String> teams, List<List<Match>> rounds) {
        Map<Set<String>, Integer> played = new HashMap<>();
        for (List<Match> round : rounds) {
            for (Match match : round) {
                played.merge(Set.of(match.first(), match.second()), 1, Integer::sum);
            }
        }
        List<List<String>> pairings = JNumberTools.combinations().unique(2, teams).lexOrder().stream().toList();
        if (played.size() != pairings.size()) {
            return false;
        }
        return pairings.stream().allMatch(pair -> Integer.valueOf(1).equals(played.get(Set.copyOf(pair))));
    }

    /** @return true if in every round each team appears in exactly one match */
    public static boolean everyTeamPlaysOncePerRound(List<String> teams, List<List<Match>> rounds) {
        for (List<Match> round : rounds) {
            Set<String> seen = new HashSet<>();
            for (Match match : round) {
                if (!seen.add(match.first()) || !seen.add(match.second())) {
                    return false;
                }
            }
            if (!seen.equals(new HashSet<>(teams))) {
                return false;
            }
        }
        return true;
    }

    public static void run() {
        List<List<Match>> rounds = schedule(TEAMS);
        System.out.println(TEAMS.size() + " teams: " + matchCount(TEAMS.size()) + " matches, " + rounds.size()
                + " rounds of " + rounds.getFirst().size());
        for (int i = 0; i < rounds.size(); i++) {
            System.out.println("  Round " + (i + 1) + ": " + rounds.get(i));
        }

        System.out.println();
        System.out.println("Every pairing played exactly once (checked against the library's pairings): "
                + everyPairingPlayedExactlyOnce(TEAMS, rounds));
        System.out.println("Every team plays once in every round                                    : "
                + everyTeamPlaysOncePerRound(TEAMS, rounds));

        System.out.println();
        for (int n : new int[]{8, 12, 20}) {
            System.out.println(String.format("%2d teams: %3d matches in %2d rounds", n, matchCount(n).intValueExact(), n - 1));
        }
    }

    public static void main(String[] args) {
        run();
    }
}
