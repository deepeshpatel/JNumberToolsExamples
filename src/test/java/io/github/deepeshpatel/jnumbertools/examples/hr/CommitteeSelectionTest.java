/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.hr;

import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommitteeSelectionTest {

    @Test
    void committeeCountIsTheProductOfTheChoices() {
        assertEquals(BigInteger.valueOf(10 * 3 * 4), CommitteeSelection.committeeCount());
    }

    @Test
    void conflictRuleRemovesCommitteesWithBothE1AndM1() {
        var valid = CommitteeSelection.validCommittees();
        // committees with E1 and M1: 4 engineer pairs containing E1 x 3 designers x 1 manager
        assertEquals(120 - 4 * 3, valid.size());
        assertEquals(List.of("E1", "E2", "D1", "M2"), valid.getFirst());
        assertTrue(valid.stream().noneMatch(c -> c.contains("E1") && c.contains("M1")));
    }

    @Test
    void everyCommitteeHasTheRequestedRoleMix() {
        for (List<Object> c : CommitteeSelection.validCommittees()) {
            assertEquals(4, c.size());
            assertTrue(CommitteeSelection.ENGINEERS.contains(c.get(0)));
            assertTrue(CommitteeSelection.ENGINEERS.contains(c.get(1)));
            assertFalse(c.get(0).equals(c.get(1)));
            assertTrue(CommitteeSelection.DESIGNERS.contains(c.get(2)));
            assertTrue(CommitteeSelection.MANAGERS.contains(c.get(3)));
        }
    }

    @Test
    void shortlistIsReproducibleDistinctAndFreeOfConflicts() {
        var shortlist = CommitteeSelection.randomShortlist(3, 42);
        assertEquals(shortlist, CommitteeSelection.randomShortlist(3, 42));
        assertEquals(3, shortlist.size());
        assertEquals(3, new HashSet<>(shortlist).size());
        assertTrue(shortlist.stream().allMatch(CommitteeSelection::hasNoConflict));
    }

    @Test
    void shortlistOfEveryValidCommitteeIsAShuffleOfThem() {
        var valid = CommitteeSelection.validCommittees();
        for (long seed = 0; seed < 20; seed++) {
            var shortlist = CommitteeSelection.randomShortlist(valid.size(), seed);
            assertEquals(valid.size(), shortlist.size());
            assertEquals(new HashSet<>(valid), new HashSet<>(shortlist));
        }
        assertEquals(valid.size(), CommitteeSelection.randomShortlist(1000, 1).size());
    }
}
