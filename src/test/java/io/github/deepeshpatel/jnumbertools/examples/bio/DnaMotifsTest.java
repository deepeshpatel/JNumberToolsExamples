/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.bio;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DnaMotifsTest {

    @Test
    void kmerCountIsFourToTheK() {
        assertEquals(BigInteger.valueOf(256), DnaMotifs.kmerCount(4));
        assertEquals(BigInteger.valueOf(16_777_216), DnaMotifs.kmerCount(12));
        assertEquals(List.of("AAAA", "AAAC", "AAAG", "AAAT", "AACA"), DnaMotifs.firstKmers(4, 5));
    }

    @Test
    void repeatedBasesDoNotProduceDuplicateRearrangements() {
        String motif = "GATTACA";
        assertEquals(BigInteger.valueOf(420), DnaMotifs.distinctRearrangements(motif));
        assertEquals(DnaMotifs.multinomialCount(motif), DnaMotifs.distinctRearrangements(motif));

        List<String> all = DnaMotifs.firstRearrangements(motif, 1000);
        assertEquals(420, all.size());
        assertEquals(420, all.stream().distinct().count());
        assertEquals(List.of("GAAATTC", "GAAATCT", "GAAACTT", "GAATATC", "GAATACT"), all.subList(0, 5));
    }

    @Test
    void everyRearrangementHasTheSameComposition() {
        char[] expected = "GATTACA".toCharArray();
        Arrays.sort(expected);
        for (String s : DnaMotifs.firstRearrangements("GATTACA", 1000)) {
            char[] actual = s.toCharArray();
            Arrays.sort(actual);
            assertTrue(Arrays.equals(expected, actual), s);
        }
    }

    @Test
    void aSingleRepeatedBaseHasExactlyOneRearrangement() {
        assertEquals(BigInteger.ONE, DnaMotifs.distinctRearrangements("AAAA"));
        assertEquals(List.of("AAAA"), DnaMotifs.firstRearrangements("AAAA", 10));
    }
}
