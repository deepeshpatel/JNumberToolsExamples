/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.security;

import io.github.deepeshpatel.jnumbertools.base.JNumberTools;
import io.github.deepeshpatel.jnumbertools.generator.permutation.repetitive.RepetitivePermutationBuilder;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Splitting a large search space between workers without enumerating it.
 * <p>
 * Real-life problem: auditing how long an exhaustive search of your own password policy
 * would take, or splitting any big enumeration over a cluster. Every candidate has a
 * rank, so a worker can start anywhere, and no coordinator has to generate the space.
 * <ul>
 *   <li><b>Interleaved</b>: worker {@code w} of {@code n} takes ranks w, w+n, w+2n, ...
 *       ({@code lexOrderMth(n, w)}). Good for small spaces and even load.</li>
 *   <li><b>Contiguous</b>: worker {@code w} takes one rank range, jumped to directly
 *       with {@code byRanks}. Good for huge spaces, cheap checkpointing and resuming.</li>
 * </ul>
 */
public final class KeyspacePartitioning {

    static final List<Character> DIGITS = "0123456789".chars().mapToObj(c -> (char) c).toList();
    static final List<Character> ALPHANUMERIC =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
                    .chars().mapToObj(c -> (char) c).toList();

    /** One worker's contiguous rank range {@code [fromRank, toRankExclusive)} and its end candidates. */
    public record Slice(int worker, BigInteger fromRank, BigInteger toRankExclusive, String first, String last) {
        public BigInteger size() {
            return toRankExclusive.subtract(fromRank);
        }
    }

    private KeyspacePartitioning() {
    }

    static RepetitivePermutationBuilder<Character> space(int length, List<Character> alphabet) {
        return JNumberTools.permutations().repetitive(length, alphabet);
    }

    /** @return the candidates (as strings) of worker {@code worker} out of {@code workers}, interleaved */
    public static List<String> interleaved(int length, List<Character> alphabet, int workers, int worker) {
        List<String> out = new ArrayList<>();
        space(length, alphabet).lexOrderMth(workers, worker)
                .forEach(candidate -> out.add(asString(candidate)));
        return out;
    }

    /** @return one contiguous slice per worker, covering the whole space with no gap or overlap */
    public static List<Slice> contiguous(int length, List<Character> alphabet, int workers) {
        var builder = space(length, alphabet);
        BigInteger total = builder.count();
        BigInteger n = BigInteger.valueOf(workers);
        BigInteger chunk = total.add(n).subtract(BigInteger.ONE).divide(n); // ceiling

        List<Slice> slices = new ArrayList<>();
        for (int w = 0; w < workers; w++) {
            BigInteger from = chunk.multiply(BigInteger.valueOf(w)).min(total);
            BigInteger to = from.add(chunk).min(total);
            if (from.equals(to)) {
                break;
            }
            slices.add(new Slice(w, from, to,
                    candidateAt(length, alphabet, from),
                    candidateAt(length, alphabet, to.subtract(BigInteger.ONE))));
        }
        return slices;
    }

    /** Jumps straight to one rank; no preceding candidate is generated. */
    static String candidateAt(int length, List<Character> alphabet, BigInteger rank) {
        return asString(space(length, alphabet).byRanks(List.of(rank)).iterator().next());
    }

    private static String asString(List<Character> chars) {
        var sb = new StringBuilder(chars.size());
        chars.forEach(sb::append);
        return sb.toString();
    }

    public static void run() {
        int workers = 8;
        System.out.println("4-digit PIN space: " + space(4, DIGITS).count() + " candidates, " + workers + " workers, interleaved");
        for (int w = 0; w < workers; w++) {
            List<String> mine = interleaved(4, DIGITS, workers, w);
            System.out.printf("  worker %d: %d candidates, first three %s%n", w, mine.size(), mine.subList(0, 3));
        }

        System.out.println();
        int length = 8;
        BigInteger total = space(length, ALPHANUMERIC).count();
        System.out.println("8-character alphanumeric space: " + total + " candidates, 4 workers, contiguous");
        for (Slice s : contiguous(length, ALPHANUMERIC, 4)) {
            System.out.printf("  worker %d: ranks [%s, %s) %s .. %s%n",
                    s.worker(), s.fromRank(), s.toRankExclusive(), s.first(), s.last());
        }

        BigInteger guessesPerSecond = BigInteger.valueOf(1_000_000_000L);
        BigInteger hours = total.divide(guessesPerSecond.multiply(BigInteger.valueOf(4))).divide(BigInteger.valueOf(3600));
        System.out.println();
        System.out.println("At an assumed 1 billion guesses/second per worker, 4 workers need about "
                + hours + " hours for the whole space.");
    }

    public static void main(String[] args) {
        run();
    }
}
