/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.tour;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SelectionsTest {

    @Test
    void picksOfTwoFromThreeLetters() {
        assertEquals(List.of(List.of("A", "B"), List.of("A", "C"), List.of("B", "A"),
                List.of("B", "C"), List.of("C", "A"), List.of("C", "B")), Selections.orderedNoRepeats(2));
        assertEquals(9, Selections.orderedRepeats(2).size());
        assertEquals(List.of(List.of("A", "B"), List.of("A", "C"), List.of("B", "C")), Selections.unorderedNoRepeats(2));
        assertEquals(List.of(List.of("A", "A"), List.of("A", "B"), List.of("A", "C"),
                List.of("B", "B"), List.of("B", "C"), List.of("C", "C")), Selections.unorderedRepeats(2));
    }

    @Test
    void libraryCountsEqualTheFormulasForManySizes() {
        for (int n = 1; n <= 8; n++) {
            for (int r = 1; r <= n; r++) {
                assertEquals(Selections.formulaCounts(n, r), Selections.libraryCounts(n, r), "n=" + n + " r=" + r);
            }
        }
        assertEquals(BigInteger.valueOf(56), Selections.libraryCounts(6, 3).get(Selections.UNORDERED_REPEATS));
    }

    @Test
    void pickingFromABagNeverTakesMoreThanTheBagHolds() {
        var bag = new LinkedHashMap<String, Integer>();
        bag.put("A", 2);
        bag.put("B", 1);
        bag.put("C", 1);
        List<Map<String, Integer>> picks = Selections.fromBag(bag, 2);
        assertEquals(4, picks.size());
        assertEquals(Map.of("A", 2), picks.getFirst());
        for (Map<String, Integer> pick : picks) {
            assertEquals(2, pick.values().stream().mapToInt(Integer::intValue).sum());
            pick.forEach((item, taken) -> org.junit.jupiter.api.Assertions.assertTrue(taken <= bag.get(item), item));
        }
    }
}
