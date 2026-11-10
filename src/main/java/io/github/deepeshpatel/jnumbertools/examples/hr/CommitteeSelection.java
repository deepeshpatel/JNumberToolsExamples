/*
 * Copyright (c) 2026 Deepesh Patel
 * SPDX-License-Identifier: Apache-2.0
 */

package io.github.deepeshpatel.jnumbertools.examples.hr;

import io.github.deepeshpatel.jnumbertools.base.JNumberTools;
import io.github.deepeshpatel.jnumbertools.generator.product.constrained.ConstrainedProductBuilder;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;

/**
 * Selecting a project committee with a fixed role mix.
 * <p>
 * Real-life problem: a committee needs 2 engineers, 1 designer and 1 manager from three
 * candidate pools. Each pool is its own "choose r" problem, and the committee is one choice from
 * every pool. A constrained Cartesian product models exactly that, so the library counts the
 * committees, lists them in order, or draws distinct random ones. A conflict of interest between
 * two people is not a library feature, so it is applied as a filter.
 * <p>
 * Each committee is a flat list: two engineers, then the designer, then the manager.
 */
public final class CommitteeSelection {

    static final List<String> ENGINEERS = List.of("E1", "E2", "E3", "E4", "E5");
    static final List<String> DESIGNERS = List.of("D1", "D2", "D3");
    static final List<String> MANAGERS = List.of("M1", "M2", "M3", "M4");

    private CommitteeSelection() {
    }

    private static ConstrainedProductBuilder builder() {
        return JNumberTools.cartesianProduct()
                .constrainedProductOfDistinct(2, ENGINEERS)
                .andDistinct(1, DESIGNERS)
                .andDistinct(1, MANAGERS);
    }

    /** @return number of possible committees: C(5,2) x C(3,1) x C(4,1) */
    public static BigInteger committeeCount() {
        return builder().count();
    }

    /** Assumed rule for this example: E1 and M1 are related and must not serve together. */
    static boolean hasNoConflict(List<Object> committee) {
        return !(committee.contains("E1") && committee.contains("M1"));
    }

    /** @return all committees without the conflict, in lexicographic order */
    public static List<List<Object>> validCommittees() {
        return builder().lexOrder().stream().filter(CommitteeSelection::hasNoConflict).toList();
    }

    /** @return {@code n} distinct random committees (reproducible); conflicts are not excluded here */
    public static List<List<Object>> randomShortlist(int n, long seed) {
        return builder().sample(n, new Random(seed)).stream().toList();
    }

    public static void run() {
        System.out.println("Candidates: " + ENGINEERS.size() + " engineers, " + DESIGNERS.size()
                + " designers, " + MANAGERS.size() + " managers");
        System.out.println("Committees (2 engineers + 1 designer + 1 manager): " + committeeCount());
        System.out.println("Without the E1 + M1 conflict                     : " + validCommittees().size());
        System.out.println();
        System.out.println("First three valid committees:");
        validCommittees().stream().limit(3).forEach(c -> System.out.println("  " + c));
        System.out.println("Shortlist of three random committees (seed 42):");
        randomShortlist(3, 42).forEach(c -> System.out.println("  " + c));
    }

    public static void main(String[] args) {
        run();
    }
}
